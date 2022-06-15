package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.favoris;

import fr.dila.solonepg.ui.bean.EpgUserList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgUserListForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEpgRechercheFavorisUtilisateur extends SolonWebObject {

    protected Map<String, Object> getTemplateData(EpgUserListForm userListForm) {
        context.putInContextData(EpgContextDataKey.USER_LIST_FORM, userListForm);

        EpgUserList userList = EpgUIServiceLocator.getEpgUserListUIService().getFavorisConsultationUsers(context);

        Map<String, Object> map = new HashMap<>();

        map.put(STTemplateConstants.RESULTAT_LIST, userList.getListe());
        map.put(STTemplateConstants.LST_COLONNES, userList.getListeColonnes(userListForm));
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.RECHERCHE_FAVORIS_UTILISATEURS);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.AJAX_RECHERCHE_FAVORIS_UTILISATEURS);
        map.put(STTemplateConstants.NB_RESULTS, userList.getListe().size());
        map.put(
            STTemplateConstants.GENERALE_ACTIONS,
            context.getActions(EpgActionCategory.FAVORIS_UTILISATEUR_ACTIONS)
        );
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(STTemplateConstants.BASE_URL, EpgURLConstants.RECHERCHE_FAVORIS_UTILISATEUR);
        map.put(STTemplateConstants.IS_CHECKBOX, true);

        return map;
    }
}
