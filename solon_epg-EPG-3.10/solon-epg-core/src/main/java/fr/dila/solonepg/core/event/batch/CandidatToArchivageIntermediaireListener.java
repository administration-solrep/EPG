package fr.dila.solonepg.core.event.batch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

public class CandidatToArchivageIntermediaireListener extends AbstractBatchEventListener {
	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER	= STLogFactory.getLog(CandidatToArchivageIntermediaireListener.class);

	public CandidatToArchivageIntermediaireListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_ARCHIV_INTER_TEC);
		final long startTime = Calendar.getInstance().getTimeInMillis();
		long nbArchivInter = 0;
		try {
			final Calendar dua = Calendar.getInstance();
			final STParametreService paramService = STServiceLocator.getSTParametreService();
			final String duaDelai = paramService.getParametreValue(session,
					STParametreConstant.DELAI_VERSEMENT_BASE_INTERMEDIAIRE);
			dua.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(duaDelai) + 1);
			final String literalDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());
			final String currentDate = DateLiteral.dateFormatter.print(Calendar.getInstance().getTime().getTime());

			final String query = "SELECT * FROM Dossier WHERE ecm:isProxy = 0 AND ( dos:"
					+ DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE + " is null OR dos:"
					+ DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE + " = '"
					+ VocabularyConstants.STATUT_ARCHIVAGE_AUCUN + "') and (( dos:"
					+ DossierSolonEpgConstants.DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION + " is not null and dos:"
					+ DossierSolonEpgConstants.DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION + " < TIMESTAMP '" + currentDate
					+ " ' ) OR dos:" + DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY + " = '"
					+ VocabularyConstants.STATUT_NOR_ATTRIBUE + "' OR dos:"
					+ DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY + " = '" + VocabularyConstants.STATUT_ABANDONNE
					+ "' OR dos:" + DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY + " = '"
					+ VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION + "' OR  ( dos:"
					+ DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY + " = '" + VocabularyConstants.STATUT_PUBLIE
					+ "' and  " + RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX + ":"
					+ RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY + " < TIMESTAMP '" + literalDate
					+ " ' ))";

			DocumentModelList list = null;
			list = session.query(query);
			if (list.size() > 0) {
				nbArchivInter = list.size();
				final ProfileService profileService = STServiceLocator.getProfileService();
				final List<STUser> users = profileService
						.getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
				final StringBuilder dossierNorListe = new StringBuilder();

				Integer compteurDossier = 0;
				for (final DocumentModel dossierDoc : list) {
					final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
					dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE);
					session.saveDocument(dossierDoc);
					compteurDossier = compteurDossier + 1;
					if (compteurDossier.equals(50)) {
						// Remise à zéro du compteur et commit pour éviter problèmes de rollback
						compteurDossier = 0;
						TransactionHelper.commitOrRollbackTransaction();
						TransactionHelper.startTransaction();
					}
					dossierNorListe.append(dossier.getNumeroNor());
					dossierNorListe.append(System.getProperty("line.separator"));
				}
				session.save();
				final List<String> emailsList = new ArrayList<String>();

				for (final STUser user : users) {
					final String email = user.getEmail();
					if (email != null && !email.isEmpty()) {
						emailsList.add(email);
					}
				}

				if (emailsList.size() > 0) {
					final Address[] adressList = new InternetAddress[emailsList.size()];
					int index = 0;
					for (final String email : emailsList) {
						adressList[index] = new InternetAddress(email);
						index = index + 1;
					}

					final String objet = paramService.getParametreValue(session,
							STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE);
					final String text = paramService.getParametreValue(session,
							STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE);
					final String emailMessage = text + System.getProperty("line.separator")
							+ dossierNorListe.toString();
					STServiceLocator.getSTMailService().sendMail(emailMessage, objet, "mailSession", adressList);
				}
			}

		} catch (final Exception exc) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_ARCHIV_INTER_TEC, exc);
			errorCount++;
		}
		final long endTime = Calendar.getInstance().getTimeInMillis();
		try {
			STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel,
					"Dossiers candidats archivage intermédiaire", nbArchivInter, endTime - startTime);
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_ARCHIV_INTER_TEC);
	}
}
