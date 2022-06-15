package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.th.model.EpgUtilisateurTemplate;
import fr.dila.ss.ui.services.impl.SSTemplateUIServiceImpl;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;

public class TemplateUIServiceImpl extends SSTemplateUIServiceImpl {

    @Override
    public ThTemplate getLeftMenuTemplate(SpecificContext context) {
        if (context == null) {
            return new ThTemplate();
        }

        if (context.getWebcontext().getPrincipal().isMemberOf(STBaseFunctionConstant.ESPACE_ADMINISTRATION_READER)) {
            return new EpgAdminTemplate();
        } else {
            return new EpgUtilisateurTemplate();
        }
    }
}
