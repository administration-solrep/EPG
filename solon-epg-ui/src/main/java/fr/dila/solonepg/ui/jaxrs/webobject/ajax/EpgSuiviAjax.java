package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgEspaceSuiviUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getSolonEpgDossierListUIService;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_KEY;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_NODE_KEY;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_STATE;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.EpgListTraitementForm;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliSuiviAjax")
public class EpgSuiviAjax extends SolonWebObject {
    public static final String CLE = "cle";

    public EpgSuiviAjax() {
        super();
    }

    @Path("pan")
    public Object doPAN() {
        return newObject("PanAjax", context);
    }

    @Path("tableaux")
    public Object doTableauxDynamiques() {
        return newObject("TableauxDynamiquesAjax", context, template);
    }

    @POST
    @Path("liste")
    public ThTemplate getResults(@SwBeanParam DossierListForm resultform) {
        // Je déclare mon template et j'instancie mon context
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/table/tableDossiers");
        SpecificContext context = getMyContext();
        template.setContext(context);

        Map<String, Object> sessionSuivi = UserSessionHelper.getUserSessionParameter(context, SUIVI_DOSSIER_KEY);

        String key = (String) sessionSuivi.get(SUIVI_DOSSIER_NODE_KEY);
        String state = (String) sessionSuivi.get(SUIVI_DOSSIER_STATE);
        resultform = ObjectHelper.requireNonNullElseGet(resultform, DossierListForm::newForm);

        context.putInContextData(STContextDataKey.DOSSIER_STATE, STDossier.DossierState.valueOf(state));
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, resultform);
        context.setNavigationContextTitle(
            new Breadcrumb(
                String.format("Clé %s", key),
                "/classement/liste",
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        context.putInContextData(STContextDataKey.ID, key);
        Map<String, Set<String>> mailboxMap = getEpgEspaceSuiviUIService().fillMailbox(context);

        context.putInContextData(STContextDataKey.MAILBOX_MAP, mailboxMap);
        EpgDossierList lstResults = getSolonEpgDossierListUIService().getDossiersFromByMailBoxAndState(context);

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();
        resultform.setColumnVisibility(lstResults.getListeColonnes());

        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.RESULT_FORM, resultform);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
            context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
        );
        map.put(STTemplateConstants.DATA_URL, "/suivi/liste");
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/suivi/liste");
        template.setData(map);

        return template;
    }

    @GET
    @Path("gestionListes")
    public ThTemplate getGestionListes(@SwRequired @QueryParam("date") String date) {
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/table/tables-gestion-listes");

        context.putInContextData(EpgContextDataKey.DATE, date);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(
            EpgTemplateConstants.AUTRES_LISTES,
            EpgUIServiceLocator.getEpgTraitementPapierListeUIService().getListeTraitementPapierAutre(context)
        );
        map.put(
            EpgTemplateConstants.LISTES_SIGNATURE,
            EpgUIServiceLocator.getEpgTraitementPapierListeUIService().getListeTraitementPapierSignature(context)
        );

        template.setData(map);
        return template;
    }

    @POST
    @Path("liste/{listId}/retirer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFoldersFromlist(
        @SwRequired @FormParam("foldersId[]") List<String> foldersId,
        @PathParam("listId") String listId
    ) {
        verifyAction(EpgActionEnum.REMOVE_DOSSIERS_LIST, String.format("liste/%s/retirer", listId));
        context.setCurrentDocument(listId);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, foldersId);
        EpgUIServiceLocator.getEpgTraitementPapierListeUIService().removeElementFromListe(context);
        addMessageQueueInSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("liste/{listId}/imprimer")
    @Produces("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public Response printList(@PathParam("listId") String listId) {
        context.setCurrentDocument(listId);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT, BirtOutputFormat.DOCX);
        verifyAction(EpgActionEnum.PRINT_LIST_BIRT, String.format("liste/%s/imprimer", listId));
        return EpgUIServiceLocator.getEpgTraitementPapierListeUIService().imprimerListe(context);
    }

    @POST
    @Path("liste/{listId}/traiter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response traiterList(@PathParam("listId") String listId, @SwBeanParam EpgListTraitementForm form) {
        context.setCurrentDocument(listId);
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_LIST_FORM, form);
        EpgUIServiceLocator.getEpgTraitementPapierListeUIService().initGestionListeActionContext(context);
        verifyAction(EpgActionEnum.TRAITER_SERIE_LIST, String.format("liste/%s/traiter", listId));
        EpgUIServiceLocator.getEpgTraitementPapierListeUIService().traiterEnSerieListe(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
