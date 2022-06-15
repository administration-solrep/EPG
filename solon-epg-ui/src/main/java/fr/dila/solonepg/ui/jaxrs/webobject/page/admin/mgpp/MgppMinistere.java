package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.mgpp;

import static fr.dila.st.core.util.ResourceHelper.getString;

import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppTableReferenceUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppMinistereForm;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
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

@WebObject(type = "ministereMGPP")
public class MgppMinistere extends SolonWebObject {
    public static final String MINISTERE_FORM = "ministereForm";
    public static final String TITRE_LABEL = "command.admin.param.tableRefEPP.modifier.ministere.label";
    public static final String NAVIGATION_TITLE_CREATION = "command.admin.param.tableRefEPP.ajouter.ministere";
    public static final String NAVIGATION_TITLE_MODIFICATION = "command.admin.param.tableRefEPP.modifier.ministere";
    public static final String NAVIGATION_URL_CREATION = "/admin/mgpp/referenceEPP/ministere/creation";
    public static final String NAVIGATION_URL_MODIFICATION = "/admin/mgpp/referenceEPP/ministere/modification";
    public static final String URL_SAUVEGARDE = "/admin/mgpp/referenceEPP/ministere/sauvegarde";

    public MgppMinistere() {
        super();
    }

    @GET
    @Path("creation")
    public ThTemplate doCreation(@QueryParam("idParent") String idParent) {
        checkAdministrationAccess(context, NAVIGATION_URL_CREATION);
        template = getForm(null);

        MgppMinistereForm ministereForm = new MgppMinistereForm(idParent);

        template.getData().put(MINISTERE_FORM, ministereForm);

        return template;
    }

    @GET
    @Path("modification")
    public ThTemplate doModification(@QueryParam("idMinistere") String idMinistere) {
        checkAdministrationAccess(context, NAVIGATION_URL_MODIFICATION);
        template = getForm(idMinistere);

        context.putInContextData(STContextDataKey.ID, idMinistere);
        MgppMinistereForm ministere = MgppUIServiceLocator
            .getMgppTableReferenceUIService()
            .getMinistereFormFromId(context);

        template.getData().put(MINISTERE_FORM, ministere);
        template.getData().put(STTemplateConstants.TITRE, getString(TITRE_LABEL, ministere.getNom()));
        return template;
    }

    private ThTemplate getForm(String idMinistere) {
        template.setContext(context);

        String title;
        String urlBreadCrumb;

        if (StringUtils.isBlank(idMinistere)) {
            title = ResourceHelper.getString(NAVIGATION_TITLE_CREATION);
            urlBreadCrumb = NAVIGATION_URL_CREATION;
        } else {
            title = ResourceHelper.getString(NAVIGATION_TITLE_MODIFICATION);
            urlBreadCrumb = NAVIGATION_URL_MODIFICATION;
        }
        template.setName("pages/admin/mgpp/ministereForm");
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
    public Object saveOrUpdateMinistere(@SwBeanParam MgppMinistereForm createMinForm) {
        checkAdministrationAccess(context, URL_SAUVEGARDE);
        String ministereId = createMinForm.getIdentifiant();
        context.putInContextData(MgppContextDataKey.MINISTERE_FORM, createMinForm);
        MgppTableReferenceUIService tableReferenceUIService = MgppUIServiceLocator.getMgppTableReferenceUIService();

        boolean isCreation = StringUtils.isBlank(ministereId);
        if (isCreation) {
            tableReferenceUIService.creerMinistere(context);
        } else {
            tableReferenceUIService.modifierMinistere(context);
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
            return redirect("/admin/mgpp/referenceEPP/consulter#main_content");
        } else {
            return isCreation ? doCreation(createMinForm.getIdGouvernement()) : doModification(ministereId);
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
