package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.parametres;

import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION_ADD_RUBRIQUE;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION_CREATE_LIST;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION_DELETE_RUBRIQUE;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION_INIT_CREATE_LIST;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION_LIST;

import fr.dila.solonepg.ui.bean.EpgIndexationMotCleForm;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.actions.EpgGestionIndexationActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "EpgParametresIndexationAjax")
public class EpgParametresIndexationAjax extends SolonWebObject {

    public EpgParametresIndexationAjax() {
        super();
    }

    @POST
    @Path("rubrique/ajouter")
    public ThTemplate ajouterRubrique(@SwRequired @FormParam("rubriques") String rubriques) {
        verifyAction(EpgActionEnum.ADD_RUBRIQUE, URL_INDEXATION_ADD_RUBRIQUE);

        context.putInContextData(EpgContextDataKey.INDEXATION_RUBRIQUE, rubriques);
        SolonEpgActionServiceLocator.getEpgGestionIndexationActionService().addRubrique(context);

        return buildInputListTemplateForRubriques();
    }

    @POST
    @Path("rubrique/supprimer")
    public ThTemplate supprimerRubrique(@SwId @SwRequired @FormParam("id") String id) {
        verifyAction(EpgActionEnum.REMOVE_RUBRIQUE, URL_INDEXATION_DELETE_RUBRIQUE);

        context.setCurrentDocument(id);
        EpgGestionIndexationActionService indexationActionService = SolonEpgActionServiceLocator.getEpgGestionIndexationActionService();
        indexationActionService.deleteIndexation(context);

        return buildInputListTemplateForRubriques();
    }

    @POST
    @Path("liste/creer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterListeMotsCles(@SwBeanParam EpgIndexationMotCleForm liste) {
        verifyAction(EpgActionEnum.CREATE_LIST_MOTS_CLES, URL_INDEXATION_CREATE_LIST);

        context.putInContextData(EpgContextDataKey.INDEXATION_MOT_CLE_FORM, liste);
        EpgGestionIndexationActionService indexationActionService = SolonEpgActionServiceLocator.getEpgGestionIndexationActionService();
        indexationActionService.addMotCleList(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @GET
    @Path("liste/creer/init")
    public ThTemplate initCreationListeMotsCles() {
        verifyAction(EpgActionEnum.CREATE_LIST_MOTS_CLES, URL_INDEXATION_INIT_CREATE_LIST);

        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/components/modal-ajout-liste-mots-cles-content",
            context
        );

        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();

        Map<String, Object> map = new HashMap<>();
        map.put(EpgTemplateConstants.IS_REATTRIBUTION, false);
        map.put(
            SSTemplateConstants.IS_ADMIN_FONCTIONNEL,
            principal.isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME)
        );
        Set<String> ministerSet = principal.getMinistereIdSet();
        if (ministerSet.size() == 1) {
            String idMin = ministerSet.stream().findFirst().get();
            map.put(
                EpgTemplateConstants.MINISTERE,
                Collections.singletonMap(
                    idMin,
                    STServiceLocator
                        .getOrganigrammeService()
                        .getOrganigrammeNodeById(idMin, OrganigrammeType.MINISTERE)
                        .getLabel()
                )
            );
        }
        template.setData(map);
        return template;
    }

    @GET
    @Path("liste/{listeId}/charger")
    public ThTemplate chargerListeMotsCles(@PathParam("listeId") String listeId) {
        verifyAction(EpgActionEnum.REATTRIBUER_LIST_MOTS_CLES, URL_INDEXATION_LIST + listeId + "/charger");

        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/components/modal-ajout-liste-mots-cles-content",
            context
        );

        context.setCurrentDocument(listeId);
        Map<String, Object> map = new HashMap<>();
        map.put(
            STTemplateConstants.RESULT_LIST,
            SolonEpgActionServiceLocator.getEpgGestionIndexationActionService().getMotCleList(context)
        );
        map.put(EpgTemplateConstants.IS_REATTRIBUTION, true);
        map.put(
            SSTemplateConstants.IS_ADMIN_FONCTIONNEL,
            context.getSession().getPrincipal().isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME)
        );
        template.setData(map);
        return template;
    }

    @POST
    @Path("liste/{listeId}/reattribuer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reattribuerListeMotsCles(
        @PathParam("listeId") String listeId,
        @SwNotEmpty @FormParam("ministereId") String ministereId
    ) {
        verifyAction(EpgActionEnum.REATTRIBUER_LIST_MOTS_CLES, URL_INDEXATION_LIST + listeId + "/reattribuer");

        context.setCurrentDocument(listeId);
        context.putInContextData(STContextDataKey.MINISTERE_ID, ministereId);

        EpgGestionIndexationActionService indexationActionService = SolonEpgActionServiceLocator.getEpgGestionIndexationActionService();
        indexationActionService.changeMotCleList(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("liste/{listeId}/supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerListeMotsCles(@PathParam("listeId") String listeId) {
        verifyAction(EpgActionEnum.REMOVE_LIST_MOTS_CLES, URL_INDEXATION_LIST + listeId + "/supprimer");

        context.setCurrentDocument(listeId);
        EpgGestionIndexationActionService indexationActionService = SolonEpgActionServiceLocator.getEpgGestionIndexationActionService();
        indexationActionService.deleteIndexation(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("liste/{listeId}/ajouter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterMotsCles(
        @PathParam("listeId") String listeId,
        @SwRequired @FormParam("motsCles") String motsCles
    ) {
        verifyAction(EpgActionEnum.ADD_MOTS_CLES, URL_INDEXATION_LIST + listeId + "/ajouter");
        context.setCurrentDocument(listeId);
        context.putInContextData(EpgContextDataKey.INDEXATION_MOT_CLE_LIST, motsCles);
        SolonEpgActionServiceLocator.getEpgGestionIndexationActionService().addMotCle(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("liste/{listeId}/retirer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retirerMotsCles(
        @PathParam("listeId") String listeId,
        @SwRequired @FormParam("motsCles") String motsCles
    ) {
        verifyAction(EpgActionEnum.REMOVE_MOTS_CLES, URL_INDEXATION_LIST + listeId + "/retirer");
        context.setCurrentDocument(listeId);
        context.putInContextData(EpgContextDataKey.INDEXATION_MOT_CLE, motsCles);
        SolonEpgActionServiceLocator.getEpgGestionIndexationActionService().removeMotCle(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    private ThTemplate buildInputListTemplateForRubriques() {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/input-list", context);
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.ID, "rubriques");
        map.put(EpgTemplateConstants.LABEL, "param.indexation.rubriques");
        map.put(EpgTemplateConstants.LEGEND, "param.indexation.rubriques.ajouter");
        map.put(
            STTemplateConstants.RESULT_LIST,
            SolonEpgActionServiceLocator.getEpgGestionIndexationActionService().getListIndexationRubrique(context)
        );
        map.put(EpgTemplateConstants.ADD_ACTION, context.getAction(EpgActionEnum.ADD_RUBRIQUE));
        template.setData(map);

        return template;
    }
}
