package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.batch.AbstractDailyReminderChangePasswordListener;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Batch d'envoi des mails journaliers de prévenance de renouvellement de mot de passe
 *
 * @author JBT
 *
 */
public class DailyReminderChangePasswordListener extends AbstractDailyReminderChangePasswordListener {

    public DailyReminderChangePasswordListener() {
        super();
    }

    @Override
    protected Set<STUser> getUsersList(CoreSession session) {
        return SolonEpgServiceLocator.getProfilUtilisateurService().getToRemindChangePasswordUserList(session);
    }

    @Override
    protected int getDelayForUser(CoreSession session, STUser user) {
        return SolonEpgServiceLocator.getProfilUtilisateurService().getNumberDayBeforeOutdatedPassword(session, user);
    }
}
