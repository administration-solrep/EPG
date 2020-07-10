package fr.dila.solonepg.core.event;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.api.service.SSUtilisateurConnectionMonitorService;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.service.STUserManager;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractLogEventListener;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Gestionnaire d'évènements executé au login de l'utilisateur : - Crée si nécessaire la mailbox personnelle de
 * l'utilisateur ; - Crée si nécessaire les mailbox des postes de l'utilisateur.
 * 
 * @author jtremeaux
 */
public class LoginSuccessListener extends AbstractLogEventListener {

	private static final Log	log	= LogFactory.getLog(LoginSuccessListener.class);

	public LoginSuccessListener() {
		super(LOGGING_SUCCESS_EVENT_NAME);
	}

	@Override
	protected void processLogin (final CoreSession session, final Set<String> principals) throws ClientException {

		try {

			final STUserManager userManager = (STUserManager) STServiceLocator.getUserManager();
			final ConfigService configService = STServiceLocator.getConfigService();
			final ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator
					.getProfilUtilisateurService();
			final SSUtilisateurConnectionMonitorService utilisateurConnectionMonitorService = SSServiceLocator
					.getUtilisateurConnectionMonitorService();

			for (final String userName : principals) {
				if (!userName.equals(configService
						.getValue(SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_USER_NAME))) {
					final DocumentModel userDoc = userManager.getUserModel(userName);

					// Crée la mailbox personnelle de l'utilisateur
					createPersonalMailbox(session, userDoc);

					// Crée les mailbox des postes de l'utilisateur
					createPosteMailbox(session, userDoc);

					// Crée le profil de l'utilisateur
					profilUtilisateurService.getOrCreateCurrentUserProfil(session);
					final STUser stUser = userDoc.getAdapter(STUser.class);

					// monitor la date de derniere connexion
					utilisateurConnectionMonitorService.createOrUpdateInfoUtilisateurConnection(session, stUser);
				}
			}
		} catch (final Exception e) {
			log.error("Impossible d'associer les groupes à l'utilisateur connecté.", e);
		}
	}

	/**
	 * Crée la mailbox personnelle de l'utilisateur.
	 * 
	 * @param session
	 *            Session
	 * @param userDoc
	 *            Document utilisateur
	 * @throws ClientException
	 */
	private void createPersonalMailbox (final CoreSession session, final DocumentModel userDoc) throws ClientException {
		// TODO Check
		// dans nuxeo si le login supérieur à 19 caractères : la propriété
		// mailbox_id est mal définie car et cela entraine des plantages lors de
		// la création du dossier.
		// on a donc surchargé l'implémentation du nuxeo afin que celui puisse
		// accepter au moins 45 caractères
		final MailboxService mailBoxService = STServiceLocator.getMailboxService();
		if (!mailBoxService.hasUserPersonalMailbox(session, userDoc)) {
			mailBoxService.createPersonalMailboxes(session, userDoc);
		}

	}

	/**
	 * Crée les mailbox poste de l'utilisateur.
	 * 
	 * @param session
	 *            Session
	 * @param userDoc
	 *            Document utilisateur
	 * @throws ClientException
	 */
	private void createPosteMailbox (final CoreSession session, final DocumentModel userDoc) throws ClientException {
		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
		mailboxPosteService.createPosteMailboxes(session, userDoc);
	}

}
