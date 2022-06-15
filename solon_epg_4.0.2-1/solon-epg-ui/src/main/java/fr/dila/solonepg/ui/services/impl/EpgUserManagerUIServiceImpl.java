package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.ss.ui.services.impl.SSUserManagerUIServiceImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgUserManagerUIServiceImpl extends SSUserManagerUIServiceImpl {

    @Override
    public void deleteUser(SpecificContext context) {
        if (canDeleteUser(context)) {
            super.deleteUser(context);

            // retirer le user des favoris de consultation et derniers users consultés
            DocumentModel userDoc = context.getCurrentDocument();
            SolonEpgUserManager userManager = (SolonEpgUserManager) STServiceLocator.getUserManager();
            userManager.deleteUserConsultation(context.getSession(), userDoc);
        } else {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("admin.user.delete.not.allow.tab.ref"));
        }
    }

    private boolean canDeleteUser(SpecificContext context) {
        SolonEpgUserManager userManager = (SolonEpgUserManager) STServiceLocator.getUserManager();
        String userId = userManager.getUserId(context.getCurrentDocument());
        return userManager.canDeleteUser(context.getSession(), userId);
    }
}
