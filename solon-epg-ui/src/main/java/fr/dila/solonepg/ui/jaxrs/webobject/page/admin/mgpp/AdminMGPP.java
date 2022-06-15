package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.mgpp;

import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.model.bean.MgppParamForm;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminMGPP")
public class AdminMGPP extends SolonWebObject {

    @Path("referenceEPP")
    public Object getAdmnMGPP() {
        return newObject("referenceEPP", context);
    }

    @GET
    @Path("parametres")
    public ThTemplate displayParameter() {
        if (context.getAction(MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP) == null) {
            throw new STAuthorizationException(ResourceHelper.getString("mgpp.param.right.error"));
        }
        template.setName("pages/admin/mgpp/parametrage");
        context.clearNavigationContext();
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("admin.param.mgpp.header"),
                "/admin/mgpp/parametres",
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        MgppParamForm form = MgppUIServiceLocator.getMgppAdminUIService().getMgppParameters(context);
        template.getData().put("form", form);
        template.setContext(context);
        return template;
    }

    @POST
    @Path("parametres/sauver")
    public Response saveParameter(@SwBeanParam MgppParamForm form) {
        if (context.getAction(MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP) == null) {
            throw new STAuthorizationException(ResourceHelper.getString("mgpp.param.right.error"));
        }
        context.putInContextData(MgppContextDataKey.PARAM_FORM, form);

        MgppUIServiceLocator.getMgppAdminUIService().saveMgppParameters(context);
        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        return redirect("/admin/mgpp/parametres");
    }
}
