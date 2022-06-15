package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier;

import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.constants.STURLConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminDossiersFdd")
public class AdminDossiersFdd extends SolonWebObject {

    @GET
    public ThTemplate getHome() {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_FDD, EpgURLConstants.URL_ADMIN_DOSSIERS_FDD);
        template.setName("pages/admin/dossier/admin-fdd");

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.dossier.fdd.title"),
                EpgURLConstants.URL_ADMIN_DOSSIERS_FDD + STURLConstants.MAIN_CONTENT_ID,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template
            .getData()
            .put(
                EpgTemplateConstants.FORMATS_AUTORISES,
                EpgUIServiceLocator.getEpgFondDeDossierUIService().getAdminFondFormatsAutorises(context)
            );

        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
