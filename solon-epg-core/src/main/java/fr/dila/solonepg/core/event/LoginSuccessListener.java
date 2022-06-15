package fr.dila.solonepg.core.event;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getProfilUtilisateurService;
import static fr.dila.st.core.service.STServiceLocator.getConfigService;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractLogEventListener;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Calendar;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;

/**
 * Gestionnaire d'évènements executé au login de l'utilisateur : - Crée si nécessaire la mailbox personnelle de
 * l'utilisateur ; - Crée si nécessaire les mailbox des postes de l'utilisateur.
 *
 * @author jtremeaux
 */
public class LoginSuccessListener extends AbstractLogEventListener {
    private static final Log LOG = LogFactory.getLog(LoginSuccessListener.class);

    public LoginSuccessListener() {
        super(LOGGING_SUCCESS_EVENT_NAME);
    }

    @Override
    protected void processLogin(final CoreSession session, final Set<String> principals) {
        try {
            for (final String userName : principals) {
                if (
                    !userName.equals(
                        getConfigService().getValue(SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_USER_NAME)
                    )
                ) {
                    final UserManager userManager = STServiceLocator.getUserManager();
                    NuxeoPrincipal principal = userManager.getPrincipal(userName);
                    if (principal == null || principal.isAdministrator()) {
                        continue;
                    }
                    final DocumentModel userDoc = principal.getModel();

                    // Crée la mailbox personnelle de l'utilisateur
                    createPersonalMailbox(session, userDoc);

                    // Crée les mailbox des postes de l'utilisateur
                    createPosteMailbox(session, userDoc);

                    // Crée le profil de l'utilisateur
                    getProfilUtilisateurService().getOrCreateCurrentUserProfil(session);
                    final STUser stUser = userDoc.getAdapter(STUser.class);
                    stUser.setLogout(false);
                    stUser.setDateDerniereConnexion(Calendar.getInstance());
                    userManager.updateUser(stUser.getDocument());
                }
            }
        } catch (final Exception e) {
            LOG.error("Impossible d'associer les groupes à l'utilisateur connecté.", e);
        }
    }

    /**
     * Crée la mailbox personnelle de l'utilisateur.
     *
     * @param session
     *            Session
     * @param userDoc
     *            Document utilisateur
     */
    private void createPersonalMailbox(final CoreSession session, final DocumentModel userDoc) {
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
     */
    private void createPosteMailbox(final CoreSession session, final DocumentModel userDoc) {
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        mailboxPosteService.createPosteMailboxes(session, userDoc);
    }
}
