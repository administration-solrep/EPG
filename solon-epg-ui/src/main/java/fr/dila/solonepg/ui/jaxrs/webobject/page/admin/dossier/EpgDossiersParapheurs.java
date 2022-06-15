package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersParapheur")
public class EpgDossiersParapheurs extends SolonWebObject {

    @GET
    public ThTemplate getParapheurs() {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_PARAPHEUR, EpgURLConstants.URL_ADMIN_DOSSIERS_PARAPHEUR);
        template.setContext(context);
        template.setName("pages/admin/dossier/parapheurs");

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.dossier.parapheur.title"),
                EpgURLConstants.URL_ADMIN_DOSSIERS_PARAPHEUR,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.EDIT_ACTIONS, context.getActions(EpgActionCategory.ADMIN_PARAPHEURS_ACTIONS));
        map.put(STTemplateConstants.EDIT_ACTION, context.getAction(EpgActionEnum.EDITER_PARAPHEURS));
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
