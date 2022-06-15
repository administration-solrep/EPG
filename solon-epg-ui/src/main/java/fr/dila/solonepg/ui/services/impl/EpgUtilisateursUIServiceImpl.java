package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.core.event.helper.DocumentSearchEventHelper;
import fr.dila.st.ui.services.impl.STUtilisateursUIServiceImpl;
import fr.dila.st.ui.th.bean.UserForm;
import fr.dila.st.ui.th.model.SpecificContext;

public class EpgUtilisateursUIServiceImpl extends STUtilisateursUIServiceImpl {

    @Override
    public UserForm getUtilisateur(SpecificContext context) {
        DocumentSearchEventHelper.raiseEvent(context.getSession(), context.getCurrentDocument());
        return super.getUtilisateur(context);
    }
}
