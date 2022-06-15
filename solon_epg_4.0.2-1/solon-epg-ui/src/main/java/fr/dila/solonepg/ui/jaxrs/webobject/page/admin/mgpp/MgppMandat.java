package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.mgpp;

import static fr.dila.st.core.util.ResourceHelper.getString;

import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppActeurForm;
import fr.dila.solonepg.ui.th.bean.MgppIdentiteForm;
import fr.dila.solonepg.ui.th.bean.MgppMandatForm;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "mandatMGPP")
public class MgppMandat extends SolonWebObject {
    public static final String TITRE_LABEL = "command.admin.param.tableRefEPP.modifier.mandat.label";
    public static final String NAVIGATION_TITLE_CREATION = "command.admin.param.tableRefEPP.ajouter.mandat";
    public static final String NAVIGATION_TITLE_MODIFICATION = "command.admin.param.tableRefEPP.modifier.mandat";
    public static final String NAVIGATION_URL_CREATION = "/admin/mgpp/referenceEPP/mandat/creation";
    public static final String NAVIGATION_URL_MODIFICATION = "/admin/mgpp/referenceEPP/mandat/modification";

    public MgppMandat() {
        super();
    }

    @GET
    @Path("creation")
    public ThTemplate doCreation(@SwNotEmpty @SwRequired @QueryParam("idParent") String idParent) {
        template = getForm(null, new MgppMandatForm(), new MgppIdentiteForm(), new MgppActeurForm(), idParent);

        return template;
    }

    @GET
    @Path("modification")
    public ThTemplate doModification(@SwNotEmpty @SwRequired @QueryParam("idMandat") String idMandat) {
        context.putInContextData(STContextDataKey.ID, idMandat);
        MgppMandatForm mandat = MgppUIServiceLocator.getMgppTableReferenceUIService().getMandat(context);

        MgppIdentiteForm identite = MgppUIServiceLocator.getMgppTableReferenceUIService().getIdentite(context);
        MgppActeurForm acteur = new MgppActeurForm();
        acteur.setIdActeur(context.getFromContextData(MgppContextDataKey.ACTEUR_ID));

        template = getForm(idMandat, mandat, identite, acteur, null);
        template
            .getData()
            .put(STTemplateConstants.TITRE, getString(TITRE_LABEL, identite.getCivilite(), identite.getPrenomNom()));

        return template;
    }

    private ThTemplate getForm(
        String idMandat,
        MgppMandatForm mandat,
        MgppIdentiteForm identite,
        MgppActeurForm acteur,
        String idParent
    ) {
        template.setContext(context);

        String title;
        String urlBreadCrumb;

        template.setName("pages/admin/mgpp/mandatForm");

        if (StringUtils.isBlank(idMandat)) {
            title = ResourceHelper.getString(NAVIGATION_TITLE_CREATION);
            urlBreadCrumb = NAVIGATION_URL_CREATION;
        } else {
            title = ResourceHelper.getString(NAVIGATION_TITLE_MODIFICATION);
            urlBreadCrumb = NAVIGATION_URL_MODIFICATION;
        }

        context.setNavigationContextTitle(
            new Breadcrumb(title, urlBreadCrumb, Breadcrumb.SUBTITLE_ORDER + 1, context.getWebcontext().getRequest())
        );

        template.getData().put("typeMandats", MgppUIServiceLocator.getMgppSelectValueUIService().getAllTypesMandats());

        template.getData().put("mandatForm", mandat);
        template.getData().put("identiteForm", identite);
        template.getData().put("acteurForm", acteur);
        template.getData().put("creation", StringUtils.isBlank(idMandat));
        template.getData().put("idParent", idParent);

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("creationMandat")
    public Response sauvegardeCreation(
        @SwBeanParam MgppMandatForm mandatForm,
        @SwBeanParam MgppIdentiteForm identiteForm,
        @FormParam("idParent") String minParent
    ) {
        context.putInContextData(MgppContextDataKey.MANDAT_FORM, mandatForm);
        context.putInContextData(MgppContextDataKey.IDENTITE_FORM, identiteForm);
        context.putInContextData(STContextDataKey.MINISTERE_ID, minParent);
        MgppUIServiceLocator.getMgppTableReferenceUIService().creerMandatComplet(context);

        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces("text/html;charset=UTF-8")
    @Path("sauvegardeMandat")
    public Object sauvegardeMandat(@SwBeanParam MgppMandatForm mandatForm) {
        context.putInContextData(MgppContextDataKey.MANDAT_FORM, mandatForm);
        MgppUIServiceLocator.getMgppTableReferenceUIService().saveMandat(context);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return doModification(mandatForm.getIdMandat());
    }

    @POST
    @Produces("text/html;charset=UTF-8")
    @Path("sauvegardeIdentite")
    public Object sauvegardeIdentite(
        @SwBeanParam MgppIdentiteForm identiteForm,
        @FormParam("idMandat") String idMandat
    ) {
        saveIdentite(identiteForm, idMandat);

        return doModification(idMandat);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sauvegardeNouvelleIdentite")
    public Response sauvegardeNouvelleIdentite(
        @SwBeanParam MgppIdentiteForm identiteForm,
        @FormParam("idMandat") String idMandat
    ) {
        saveIdentite(identiteForm, idMandat);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    private void saveIdentite(MgppIdentiteForm identiteForm, String idMandat) {
        context.putInContextData(MgppContextDataKey.IDENTITE_FORM, identiteForm);

        context.putInContextData(STContextDataKey.ID, idMandat);
        MgppUIServiceLocator.getMgppTableReferenceUIService().saveIdentite(context);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
    }

    @POST
    @Produces("text/html;charset=UTF-8")
    @Path("creerActeur")
    public Object creerActeur(@FormParam("idMandat") String idMandat) {
        context.putInContextData(STContextDataKey.ID, idMandat);
        MgppUIServiceLocator.getMgppTableReferenceUIService().newActeur(context);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return doModification(idMandat);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
