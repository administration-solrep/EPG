package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.archivage;

import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.ParametreAdamantForm;
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

@WebObject(type = "AppliParamAdamant")
public class EpgArchivageParametreAdamant extends SolonWebObject {

    @GET
    public ThTemplate getParamAdamant() {
        verifyAction(EpgActionEnum.ADMIN_MENU_ARCHIVAGE_INTERMEDIAIRE, EpgURLConstants.URL_PARAMETRE_ADAMANT);
        template.setName("pages/admin/archivage/parametreAdamant");
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("parametre.admant.title"),
                EpgURLConstants.URL_PARAMETRE_ADAMANT,
                Breadcrumb.TITLE_ORDER
            )
        );
        template.setContext(context);

        ParametreAdamantForm parametreAdamantForm = EpgUIServiceLocator
            .getParametrageAdamantUIService()
            .getParametreAdamantDocument(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_FORM, parametreAdamantForm);
        template.setData(map);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
