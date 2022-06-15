package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.mgpp;

import static fr.dila.st.core.util.ResourceHelper.getString;

import fr.dila.solonepg.ui.services.mgpp.MgppTableReferenceUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.GouvernementForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "gouvernementMGPP")
public class MgppGouvernement extends SolonWebObject {
    public static final String TITRE_LABEL = "command.admin.param.tableRefEPP.modifier.gouvernement.label";
    public static final String GOUVERNEMENT_FORM = "gouvernementForm";
    public static final String NAVIGATION_TITLE_CREATION = "command.admin.param.tableRefEPP.ajouter.gouvernement";
    public static final String NAVIGATION_TITLE_MODIFICATION = "command.admin.param.tableRefEPP.modifier.gouvernement";
    public static final String NAVIGATION_URL_CREATION = "/admin/mgpp/referenceEPP/gouvernement/creation#main_content";
    public static final String NAVIGATION_URL_MODIFICATION =
        "/admin/mgpp/referenceEPP/gouvernement/modification#main_content";
    public static final String URL_SAUVEGARDE = "/admin/mgpp/referenceEPP/gouvernement/sauvegarde";

    public MgppGouvernement() {
        super();
    }

    @GET
    @Path("creation")
    public ThTemplate doCreation() {
        checkAdministrationAccess(context, NAVIGATION_TITLE_CREATION);
        template = getForm(null);

        GouvernementForm gouvernement = new GouvernementForm();

        template.getData().put(GOUVERNEMENT_FORM, gouvernement);

        return template;
    }

    @GET
    @Path("modification")
    public ThTemplate doModification(@QueryParam("idGvt") String idGvt) {
        checkAdministrationAccess(context, NAVIGATION_TITLE_MODIFICATION);
        template = getForm(idGvt);

        context.putInContextData(STContextDataKey.ID, idGvt);
        GouvernementForm gouvernement = MgppUIServiceLocator
            .getMgppTableReferenceUIService()
            .getGouvernementFormFromId(context);

        template.getData().put(GOUVERNEMENT_FORM, gouvernement);
        template.getData().put(STTemplateConstants.TITRE, getString(TITRE_LABEL, gouvernement.getAppellation()));

        return template;
    }

    private ThTemplate getForm(String idGvt) {
        template.setContext(context);

        String title;
        String urlBreadCrumb;

        if (StringUtils.isBlank(idGvt)) {
            title = ResourceHelper.getString(NAVIGATION_TITLE_CREATION);
            urlBreadCrumb = NAVIGATION_URL_CREATION;
        } else {
            title = ResourceHelper.getString(NAVIGATION_TITLE_MODIFICATION);
            urlBreadCrumb = NAVIGATION_URL_MODIFICATION;
        }
        template.setName("pages/admin/mgpp/gouvernementForm");
        context.setNavigationContextTitle(
            new Breadcrumb(title, urlBreadCrumb, Breadcrumb.SUBTITLE_ORDER + 1, context.getWebcontext().getRequest())
        );

        if (context.getNavigationContext().size() > 1) {
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        return template;
    }

    @POST
    @Produces("text/html;charset=UTF-8")
    @Path("sauvegarde")
    public Object saveOrUpdateGouvernement(@SwBeanParam GouvernementForm createGvForm) {
        checkAdministrationAccess(context, URL_SAUVEGARDE);
        String gouvernementId = createGvForm.getId();
        context.putInContextData(STContextDataKey.GVT_FORM, createGvForm);
        MgppTableReferenceUIService tableReferenceUIService = MgppUIServiceLocator.getMgppTableReferenceUIService();

        boolean isCreation = StringUtils.isBlank(gouvernementId);
        if (isCreation) {
            tableReferenceUIService.creerGouvernement(context);
        } else {
            tableReferenceUIService.modifierGouvernement(context);
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
            return redirect("/admin/mgpp/referenceEPP/consulter#main_content");
        } else {
            return isCreation ? doCreation() : doModification(gouvernementId);
        }
    }

    private static void checkAdministrationAccess(SpecificContext context, String ressourceName) {
        if (!context.getSession().getPrincipal().isMemberOf(STBaseFunctionConstant.ESPACE_ADMINISTRATION_READER)) {
            throw new STAuthorizationException(ressourceName);
        }
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
