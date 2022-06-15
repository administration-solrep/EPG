package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.dossier;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgAdminFddPositionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersFddAjax")
public class EpgDossierFddAjax extends SolonWebObject {
    private static final String REF = "ref";
    private static final String NAME = "name";
    private static final String TYPE_ACTE = "typeActe";

    @GET
    @Path("charger/{typeActe}")
    public ThTemplate charger(@PathParam(TYPE_ACTE) String typeActe) {
        template.setContext(context);
        context.putInContextData(EpgContextDataKey.FDD_TYPE, typeActe);
        template
            .getData()
            .put(
                EpgTemplateConstants.CATEGORIES,
                EpgUIServiceLocator.getEpgFondDeDossierUIService().getAdminFondDTO(context)
            );
        template
            .getData()
            .put(STTemplateConstants.GENERALE_ACTIONS, context.getActions(EpgActionCategory.ADMIN_FDD_ACTIONS));
        template
            .getData()
            .put(
                EpgTemplateConstants.GENERALE_ACTIONS_ROOT,
                context.getActions(EpgActionCategory.ADMIN_FDD_ROOT_ACTIONS)
            );
        template
            .getData()
            .put(
                EpgTemplateConstants.GENERALE_ACTIONS_NO_EDIT,
                context.getActions(EpgActionCategory.ADMIN_FDD_NOEDIT_ACTIONS)
            );

        template.getData().put(EpgTemplateConstants.TYPE_ACTE, typeActe);

        return template;
    }

    @POST
    @Path("ajouterNoeud")
    public ThTemplate ajouter(
        @SwRequired @FormParam(REF) String ref,
        @SwRequired @FormParam("position") String position,
        @SwRequired @FormParam(NAME) String name,
        @SwRequired @FormParam(TYPE_ACTE) String typeActe
    ) {
        context.putInContextData(EpgContextDataKey.FDD_ID, ref);
        context.putInContextData(EpgContextDataKey.FDD_ADD_POSITION, EpgAdminFddPositionEnum.fromString(position));
        context.putInContextData(EpgContextDataKey.FDD_NAME, name);
        context.putInContextData(EpgContextDataKey.FDD_TYPE, typeActe);
        EpgUIServiceLocator.getEpgFondDeDossierUIService().addAdminFond(context);
        return charger(typeActe);
    }

    @POST
    @Path("supprimerNoeud")
    public ThTemplate supprimer(
        @SwRequired @FormParam(REF) String ref,
        @SwRequired @FormParam(TYPE_ACTE) String typeActe
    ) {
        context.putInContextData(EpgContextDataKey.FDD_ID, ref);
        EpgUIServiceLocator.getEpgFondDeDossierUIService().deleteAdminFond(context);
        return charger(typeActe);
    }

    @POST
    @Path("editerNoeud")
    public ThTemplate editer(
        @SwRequired @FormParam(REF) String ref,
        @SwRequired @FormParam(NAME) String name,
        @SwRequired @FormParam(TYPE_ACTE) String typeActe
    ) {
        context.putInContextData(EpgContextDataKey.FDD_ID, ref);
        context.putInContextData(EpgContextDataKey.FDD_NAME, name);
        EpgUIServiceLocator.getEpgFondDeDossierUIService().updateAdminFond(context);
        return charger(typeActe);
    }

    @POST
    @Path("enregistrer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enregistrer(@FormParam("formatsAcceptes[]") List<String> formats) {
        context.putInContextData(EpgContextDataKey.FDD_FORMATS, formats);
        EpgUIServiceLocator.getEpgFondDeDossierUIService().updateAdminFondFormats(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/fdd/organigrammeFdd", context);
    }
}
