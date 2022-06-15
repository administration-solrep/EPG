package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.organigramme;

import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.th.model.EpgUtilisateurTemplate;
import fr.dila.st.ui.jaxrs.webobject.pages.admin.organigramme.SharedBetweenAdminAndUser;
import fr.dila.st.ui.th.model.ThTemplate;

public interface EPGSharedBetweenAdminAndUser extends SharedBetweenAdminAndUser {
    @Override
    default ThTemplate getMyAdminTemplate() {
        return new EpgAdminTemplate();
    }

    @Override
    default ThTemplate getMyUserTemplate() {
        return new EpgUtilisateurTemplate();
    }
}
