package fr.dila.solonepg.core.event.batch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

public class CandidatToArchivageDefinitiveListener extends AbstractBatchEventListener {
	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER	= STLogFactory.getLog(CandidatToArchivageDefinitiveListener.class);

	public CandidatToArchivageDefinitiveListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIF);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_ARCHIV_DEF_TEC);
		final long startTime = Calendar.getInstance().getTimeInMillis();
		long nbArchivDef = 0;
		try {
			Calendar dua = Calendar.getInstance();
			final STParametreService paramService = STServiceLocator.getSTParametreService();
			final String duaDelai = paramService.getParametreValue(session,
					STParametreConstant.DUREE_UTILITE_ADMINISTRATIVE);
			dua.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(duaDelai) + 1);
			final String literalDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

			final DocumentModelList documentModelList = STServiceLocator.getVocabularyService().getAllEntry(
					VocabularyConstants.TYPE_ACTE_VOCABULARY);

			final HashMap<String, String> params = new HashMap<String, String>();
			String paramVal;
			for (final DocumentModel documentModel : documentModelList) {
				final String ident = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_ID).getValue();
				paramVal = paramService.getParametreValue(session, STParametreConstant.DUREE_UTILITE_ADMINISTRATIVE
						+ "-" + ident);
				if (paramVal == null || paramVal.isEmpty()) {
					paramVal = literalDate;
				} else {
					dua = Calendar.getInstance();
					dua.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(paramVal) + 1);
					paramVal = DateLiteral.dateFormatter.print(dua.getTimeInMillis());
				}
				params.put(ident, paramVal);
			}
			StringBuilder query = new StringBuilder("SELECT * FROM Dossier WHERE dos:");
			query.append(DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE);
			query.append(" = '");
			query.append(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
			query.append("' and ecm:isProxy = 0 ");
			query.append("and (");

			for (Entry<String, String> entry : params.entrySet()) {

				query.append("( dos:");
				query.append(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
				query.append(" = ");
				query.append(entry.getKey());
				query.append(" and dos:");
				query.append(DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE);
				query.append(" < TIMESTAMP '");
				query.append(entry.getValue());
				query.append(" ' ) OR ");

			}
			String queryStr = query.substring(0, query.length() - 3);
			queryStr += ")";

			DocumentModelList list = null;
			list = session.query(queryStr);
			if (list.size() > 0) {
				nbArchivDef = list.size();
				final ProfileService profileService = STServiceLocator.getProfileService();
				final List<STUser> users = profileService
						.getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
				final StringBuilder dossierNorListe = new StringBuilder();

				Integer compteurDossier = 0;
				for (final DocumentModel dossierDoc : list) {
					final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
					dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);
					session.saveDocument(dossierDoc);
					compteurDossier = compteurDossier + 1;
					if (compteurDossier.equals(50)) {
						// Remise à zéro du compteur et commit pour éviter les problèmes de rollback
						compteurDossier = 0;
						TransactionHelper.commitOrRollbackTransaction();
						TransactionHelper.startTransaction();
					}
					dossierNorListe.append(dossier.getNumeroNor());
					dossierNorListe.append(System.getProperty("line.separator"));

					// récupère les dossiersLink lié au dossier
					final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
					final List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(session,
							dossierDoc.getId());
					// modifie les dossiersLinks du dossier
					for (final DocumentModel dossierLinkDoc : dossierLinkDocList) {
						final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
						dossierLink.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);
						dossierLink.save(session);
					}
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
							STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE);
					final String text = paramService.getParametreValue(session,
							STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE);
					final String emailMessage = text + System.getProperty("line.separator")
							+ dossierNorListe.toString();
					STServiceLocator.getSTMailService().sendMail(emailMessage, objet, "mailSession", adressList);
				}
			}

		} catch (final Exception exc) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_ARCHIV_DEF_TEC, exc);
			errorCount++;
		}
		final long endTime = Calendar.getInstance().getTimeInMillis();
		try {
			STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel,
					"Dossiers candidats archivage définitif", nbArchivDef, endTime - startTime);
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_ARCHIV_DEF_TEC);
	}
}
