package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.mgpp;

import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "referenceEPP")
public class TableReferenceEPP extends SolonWebObject {
    private static final String NAVIGATION_URL_CONSULTER = "/admin/mgpp/referenceEPP/consulter";

    public TableReferenceEPP() {
        super();
    }

    @Path("consulter")
    public Object doHome() {
        verifyAction(MgppActionEnum.ADMIN_MENU_PARAM_REFERENCE_EPP, NAVIGATION_URL_CONSULTER);
        template.setContext(context);
        template.setName("pages/admin/mgpp/referenceEPP");
        context.clearNavigationContext();
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("admin.param.tableRefEPP.title"),
                NAVIGATION_URL_CONSULTER,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        return newObject("TableReferenceEPPAjax", context, template);
    }

    @Path("gouvernement")
    public Object getGouvernement() {
        return newObject("gouvernementMGPP", context);
    }

    @Path("mandat")
    public Object getMandat() {
        return newObject("mandatMGPP", context);
    }

    @Path("ministere")
    public Object getMinistere() {
        return newObject("ministereMGPP", context);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
