package fr.dila.solonepg.core.event.batch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
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

public class DossierCandidatAbandonListener extends AbstractBatchEventListener {
	private static final STLogger	LOGGER	= STLogFactory.getLog(DossierCandidatAbandonListener.class);

	public DossierCandidatAbandonListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ABANDON);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_CANDIDAT_ABANDON_TEC);
		final long startTime = Calendar.getInstance().getTimeInMillis();
		try {
			final Calendar dua = Calendar.getInstance();
			final STParametreService paramService = STServiceLocator.getSTParametreService();
			final String objet = paramService.getParametreValue(session,
					STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ABANDONNE);
			String text = paramService.getParametreValue(session,
					STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ABANDONNE);
			final String delai = paramService.getParametreValue(session,
					STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS);
			final String delaiUnit = paramService.getParametre(session,
					STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS).getUnit();
			text = text.replace("${DELAI}", delai + " " + delaiUnit);
			final String duaDelai = paramService.getParametreValue(session,
					STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS);
			// TODO prendre en compte l'unité du paramètre !
			// String unit = paramService.getParametre(session,
			// STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS).getUnit();
			dua.add(Calendar.MONTH, -Integer.parseInt(duaDelai) + 1);
			final String literalDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

			final String query = "SELECT * FROM Dossier WHERE ecm:currentLifeCycleState = 'running' and dos:"
					+ DossierSolonEpgConstants.DOSSIER_CANDIDAT_PROPERTY + " = '" + VocabularyConstants.CANDIDAT_AUCUN
					+ "' and dos:" + DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY + " = '"
					+ VocabularyConstants.STATUT_LANCE + "' and  dc:modified < TIMESTAMP '" + literalDate
					+ " ' and ecm:isProxy = 0 ";

			DocumentModelList dossierDocList = null;
			String emailMessage = null;
			final List<Dossier> dossierList = new ArrayList<Dossier>();
			dossierDocList = session.query(query);
			if (dossierDocList.size() > 0) {
				final ProfileService profileService = STServiceLocator.getProfileService();
				final List<STUser> users = profileService
						.getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
				// users.addAll(profileService.getUsersFromBaseFunction(SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_EMAIL_RECEIVER));
				StringBuilder builder = new StringBuilder();

				Integer compteurDossier = 0;
				for (final DocumentModel dossierDoc : dossierDocList) {
					final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
					dossier.setCandidat(VocabularyConstants.CANDIDAT_ABANDON);
					final Calendar dateCandidature = GregorianCalendar.getInstance();
					dossier.setDateCandidature(dateCandidature);
					session.saveDocument(dossierDoc);
					compteurDossier = compteurDossier + 1;
					if (compteurDossier.equals(50)) {
						compteurDossier = 0;
						TransactionHelper.commitOrRollbackTransaction();
						TransactionHelper.startTransaction();
					}
					dossierList.add(dossier);
					builder.append(dossier.getNumeroNor());
					builder.append(System.getProperty("line.separator"));
				}

				session.save();

				final List<String> emailsList = new ArrayList<String>();

				for (final STUser user : users) {
					final String email = user.getEmail();
					if (email != null) {
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
					emailMessage = text + System.getProperty("line.separator") + builder.toString();
					STServiceLocator.getSTMailService().sendMail(emailMessage, objet, "mailSession", adressList);
				} else {
					LOGGER.warn(session, STLogEnumImpl.FAIL_GET_PARAM_TEC);
				}

				final List<STUser> adminMins = profileService
						.getUsersFromBaseFunction(SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_EMAIL_RECEIVER);
				final UserManager userManager = STServiceLocator.getUserManager();
				for (final STUser user : adminMins) {
					builder = new StringBuilder();
					final SSPrincipal principal = (SSPrincipal) userManager.getPrincipal(user.getUsername());
					final Address[] adressList = new InternetAddress[1];
					final String email = user.getEmail();
					if (email != null) {
						adressList[0] = new InternetAddress(email);
						for (final Dossier currentDossier : dossierList) {
							if (principal.getMinistereIdSet().contains(currentDossier.getMinistereAttache())) {
								builder.append(currentDossier.getNumeroNor());
								builder.append(System.getProperty("line.separator"));
							}
						}
						if (builder.length() > 0) {
							if (emailMessage == null) {
								emailMessage = text + System.getProperty("line.separator") + builder.toString();
							}
							STServiceLocator.getSTMailService()
									.sendMail(emailMessage, objet, "mailSession", adressList);
						}
					}
				}

			}
		} catch (final Exception exc) {
			LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
			errorCount++;
		}
		final long endTime = Calendar.getInstance().getTimeInMillis();
		try {
			STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel,
					"Exécution du batch de candidature d'abandon des dossiers", endTime - startTime);
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_CANDIDAT_ABANDON_TEC);
	}
}
