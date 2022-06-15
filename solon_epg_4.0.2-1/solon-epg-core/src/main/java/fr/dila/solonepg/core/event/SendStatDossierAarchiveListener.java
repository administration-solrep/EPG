package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

public class SendStatDossierAarchiveListener implements PostCommitEventListener {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(SendStatDossierAarchiveListener.class);

    @Override
    public void handleEvent(EventBundle events) {
        if (!events.containsEventName(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_EVENT)) {
            return;
        }
        for (final Event event : events) {
            if (SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_EVENT.equals(event.getName())) {
                handleEvent(event);
            }
        }
    }

    private void handleEvent(Event event) {
        final EventContext eventCtx = event.getContext();
        // récupération des propriétés de l'événement
        final Map<String, Serializable> eventProperties = eventCtx.getProperties();
        @SuppressWarnings("unchecked")
        final List<DossierArchivageStatDTO> listDossiersArchives = (List<DossierArchivageStatDTO>) eventProperties.get(
            SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_LIST_DOS_PROPERTY
        );
        final String recipient = (String) eventProperties.get(
            SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_RECIPIENT_PROPERTY
        );
        final Date startDate = (Date) eventProperties.get(
            SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_STARTDATE_PROPERTY
        );
        final Date endDate = (Date) eventProperties.get(
            SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_ENDDATE_PROPERTY
        );
        final Date dateDemande = new Date(event.getTime());
        final Map<String, String> libelleStatutArchivage = VocabularyConstants.LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID;
        final String statutArchivage = libelleStatutArchivage.get(listDossiersArchives.get(0).getStatutPeriode());

        DataSource excelFile = EpgExcelUtil.exportStatDossiersArchives(listDossiersArchives);
        final STMailService mailService = STServiceLocator.getSTMailService();
        final String content =
            "Bonjour, veuillez trouver en pièce jointe l'export demandé le " +
            SolonDateConverter.DATE_SLASH.format(dateDemande) +
            " concernant la statistique sur les dossiers archivés au statut \"" +
            statutArchivage +
            "\" " +
            "sur la période du " +
            SolonDateConverter.DATE_SLASH.format(startDate) +
            " au " +
            SolonDateConverter.DATE_SLASH.format(endDate) +
            ".";
        final String object = "[SOLON-EPG] Votre demande d'export de la statistique sur les dossiers archivés";
        final String nomFichier = "export_stat_dossiers_archives.xls";

        try {
            mailService.sendMailWithAttachement(
                Collections.singletonList(recipient),
                object,
                content,
                nomFichier,
                excelFile
            );
        } catch (Exception exc) {
            LOGGER.error(STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
        }
    }
}
