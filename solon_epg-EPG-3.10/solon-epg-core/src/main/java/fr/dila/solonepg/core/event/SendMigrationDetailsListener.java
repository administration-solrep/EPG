package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

public class SendMigrationDetailsListener implements PostCommitEventListener {

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger LOGGER = STLogFactory.getLog(SendMigrationDetailsListener.class);

	@Override
	public void handleEvent(EventBundle events) throws ClientException {
		if (!events.containsEventName(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_EVENT)) {
			return;
		}
		for (final Event event : events) {
			if (SolonEpgEventConstant.SEND_MIGRATION_DETAILS_EVENT.equals(event.getName())) {
				handleEvent(event);
			}
		}
	}

	private void handleEvent(Event event) throws ClientException {
		final EventContext eventCtx = event.getContext();
		// récupération des propriétés de l'événement
		final Map<String, Serializable> eventProperties = eventCtx.getProperties();
		@SuppressWarnings("unchecked")
		final List<MigrationDetailModel> detailDocs = (List<MigrationDetailModel>) eventProperties
				.get(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_DETAILS_PROPERTY);
		final String recipient = (String) eventProperties
				.get(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_RECIPIENT_PROPERTY);
		final MigrationLoggerModel migrationLogger = (MigrationLoggerModel) eventProperties
				.get(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_LOGGER_PROPERTY);
		final Date dateDemande = new Date(event.getTime());
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy à HH:mm");

		DataSource excelFile = ExcelUtil.exportMigrationDetails(detailDocs);
		final STMailService mailService = STServiceLocator.getSTMailService();
		final String content = "Bonjour, veuillez trouver en pièce jointe l'export demandé le "
				+ sdf.format(dateDemande) + " des détails de la migration '"
				+ SolonEpgServiceLocator.getChangementGouvernementService().getLogMessage(migrationLogger)
				+ "' lancée le " + sdf2.format(migrationLogger.getStartDate()) + ".";
		final String object = "[SOLON-EPG] Votre demande d'export des détails d'une migration";
		final String nomFichier = "export_details_migration.xls";

		try {
			mailService.sendMailWithAttachement(Collections.singletonList(recipient), object, content, nomFichier,
					excelFile);
		} catch (Exception exc) {
			LOGGER.error(STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
		}

	}
}
