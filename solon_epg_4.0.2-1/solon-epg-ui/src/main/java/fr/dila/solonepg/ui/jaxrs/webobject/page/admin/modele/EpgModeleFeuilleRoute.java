package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.modele;

import static fr.dila.st.core.util.ResourceHelper.getString;

import fr.dila.solonepg.ui.bean.EpgSqueletteList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgModeleFdrFicheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgCreateMassModeleForm;
import fr.dila.solonepg.ui.th.bean.EpgSqueletteListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.solonepg.ui.th.model.bean.SqueletteFdrForm;
import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.ss.ui.jaxrs.webobject.page.admin.modele.SSModeleFeuilleRoute;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ModeleFeuilleRoute")
public class EpgModeleFeuilleRoute extends SSModeleFeuilleRoute {
    private static final String URL_CREATION_MASS = "/admin/fdr/modele/creation/masse#main_content";
    public static final String SQUELETTE_FORM_KEY = "squeletteForm";
    protected static final String URL_MODIFICATION_SQUELETTE = "/admin/fdr/squelette/modification?id=%s" + MAIN_CONTENT;

    public EpgModeleFeuilleRoute() {
        super();
    }

    @GET
    @Path("modele/modification")
    public ThTemplate getModeleFdrModification(@QueryParam("id") @SwRequired @SwId String id)
        throws IllegalAccessException, InstantiationException {
        template.setContext(context);
        context.setCurrentDocument(id);

        // si pas le droit de consulter le modele -> retour liste modele
        if (!SSActionsServiceLocator.getModeleFeuilleRouteActionService().canUserReadRoute(context)) {
            return getRechercheModeleFdr();
        }

        EpgModeleFdrForm modeleForm = new EpgModeleFdrForm();
        EpgUIServiceLocator.getEpgModeleFdrFicheUIService().getModeleFdrForm(context, modeleForm);

        setTemplateDataModeleModification(template, modeleForm);
        context.putInContextData(SSContextDataKey.MODELE_FORM, modeleForm);

        Map<String, Object> map = buildMapModeleModification(id, modeleForm);
        // Données spécifiques EPG
        map.put(EpgTemplateConstants.TYPE_ACTE, EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());
        template.setData(map);
        return template;
    }

    @GET
    @Path("modele/creation")
    public ThTemplate getModeleFdrCreation() {
        template.setName("pages/admin/modele/createModeleFDR");
        template.setContext(context);

        // si pas le droit de consulter le modele -> retour liste modele
        if (!SSActionsServiceLocator.getModeleFeuilleRouteActionService().canUserCreateRoute(context)) {
            throw new STAuthorizationException(getString("admin.modele.message.error.right"));
        }

        EpgModeleFdrForm modeleForm = UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.MODELE_FORM);
        // si retour de la création après erreur -> récupérer le formulaire en session
        if (modeleForm == null) {
            modeleForm = new EpgModeleFdrForm();
        }

        setNavigationContextCreationModele();
        context.putInContextData(SSContextDataKey.MODELE_FORM, modeleForm);

        Map<String, Object> map = buildMapCreationModele(modeleForm);
        map.put(EpgTemplateConstants.TYPE_ACTE, EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());

        map.put(SSTemplateConstants.TYPE_ETAPE, SSUIServiceLocator.getSSSelectValueUIService().getRoutingTaskTypes());
        template.setData(map);

        return template;
    }

    @POST
    @Path("modele/sauvegarde")
    @Produces("text/html;charset=UTF-8")
    public Object saveFormModele(@SwBeanParam EpgModeleFdrForm modeleForm) {
        EpgModeleFdrFicheUIService modeleFDRFicheUIService = EpgUIServiceLocator.getEpgModeleFdrFicheUIService();
        EpgModeleFeuilleRouteActionService modeleActionService = SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService();
        String id = modeleForm.getId();
        boolean isCreation = StringUtils.isBlank(id);

        context.putInContextData(EpgContextDataKey.EPG_MODELE_FORM, modeleForm);
        if (modeleActionService.checkBeforeSave(context)) {
            if (isCreation) {
                // Si modeleForm.id == null
                // creation
                modeleFDRFicheUIService.createModele(context, modeleForm);
            } else {
                // Sinon update
                context.setCurrentDocument(id);
                modeleFDRFicheUIService.updateModele(context, modeleForm);
            }
        }
        return endSaveFormModele(modeleForm, isCreation);
    }

    @GET
    @Path("modele/creation/masse")
    public ThTemplate getMasseModeleFdrCreation() {
        template.setName("pages/admin/modele/createMassModeleFDR");
        template.setContext(context);

        verifyAction(EpgActionEnum.ADMIN_MENU_MODELE_CREER_MASSE, "modele/creation/masse");

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.modele.creer.masse.title"),
                "/admin/fdr/modele/creation/masse",
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        Map<String, Object> map = new HashedMap<>();
        map.put(EpgTemplateConstants.CREATE_MASS_MODELE_FORM, new EpgCreateMassModeleForm());

        String urlPreviousPage = "";
        if (context.getNavigationContext().size() > 1) {
            urlPreviousPage = context.getUrlPreviousPage();
        }
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, urlPreviousPage);
        template.setData(map);
        return template;
    }

    @POST
    @Path("modele/masse/creer")
    @Produces("text/html;charset=UTF-8")
    public Object createMassModele(@SwBeanParam EpgCreateMassModeleForm modeleForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_MODELE_CREER_MASSE, "modele/masse/creer");
        context.putInContextData(EpgContextDataKey.CREATE_MASS_MODELE_FORM, modeleForm);
        SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService().createModeleFDRMass(context);
        addMessageQueueInSession();
        return redirect(URL_CREATION_MASS);
    }

    @GET
    @Path("squelettes")
    public ThTemplate getSquelettes() {
        verifyAction(EpgActionEnum.ADMIN_MENU_MODELE_SQUELETTE, "squelettes");
        template.setContext(context);
        template.setName("pages/admin/squelettes/gestion-squelettes");
        context.setNavigationContextTitle(
            new Breadcrumb(
                "admin.squelettes.title",
                "/admin/fdr/squelettes" + MAIN_CONTENT,
                Breadcrumb.TITLE_ORDER,
                template.getContext().getWebcontext().getRequest()
            )
        );

        EpgSqueletteListForm listForm = new EpgSqueletteListForm();
        context.putInContextData(EpgContextDataKey.SQUELETTE_LIST_FORM, listForm);

        Map<String, Object> map = new HashMap<>();

        EpgSqueletteList lstResults = EpgUIServiceLocator.getEpgSqueletteUIService().getSquelettes(context);

        map.put(EpgTemplateConstants.STATUT_SQUELETTE, StatutModeleFDR.values());
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.URL_SQUELETTES);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_SQUELETTES);
        map.put(STTemplateConstants.RESULT_FORM, listForm);
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());

        map.put(STTemplateConstants.ACTION, context.getAction(EpgActionEnum.TAB_SQUELETTE_CREATION));
        template.setData(map);
        return template;
    }

    @GET
    @Path("squelette/dupliquer")
    public Object dupliquerSquelette(@SwRequired @QueryParam("id") String id) {
        context.setCurrentDocument(id);
        verifyAction(EpgActionEnum.TAB_SQUELETTE_DUPLIQUER, "squelette/dupliquer");
        EpgUIServiceLocator.getEpgSqueletteUIService().duplicateSquelette(context);
        addMessageQueueInSession();
        return redirect("admin/fdr/squelettes#main_content");
    }

    @GET
    @Path("squelette/creation")
    public ThTemplate getSqueletteFdrCreation() {
        verifyAction(EpgActionEnum.TAB_SQUELETTE_CREATION, "squelettes");
        template.setName("pages/admin/squelettes/creation-squelette");
        template.setContext(context);

        SqueletteFdrForm squeletteForm = UserSessionHelper.getUserSessionParameter(
            context,
            SQUELETTE_FORM_KEY,
            SqueletteFdrForm.class
        );
        // si retour de la création après erreur -> récupérer le formulaire en session
        if (squeletteForm == null) {
            squeletteForm = new SqueletteFdrForm();
        }

        context.setNavigationContextTitle(
            new Breadcrumb(
                "admin.squelette.creation.title",
                "/admin/fdr/squelette/creation",
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(SQUELETTE_FORM_KEY, squeletteForm);
        map.put(EpgTemplateConstants.TYPE_ACTE, EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());
        template.setData(map);

        return template;
    }

    @POST
    @Path("squelette/sauvegarde")
    @Produces("text/html;charset=UTF-8")
    public Object saveFormSqelette(@SwBeanParam SqueletteFdrForm squeletteFdrForm) {
        context.putInContextData(EpgContextDataKey.SQUELETTE_FDR_FORM, squeletteFdrForm);
        boolean isCreation = false;

        if (StringUtils.isNotBlank(squeletteFdrForm.getId())) {
            // Si modeleForm.id != null
            // update
            context.setCurrentDocument(squeletteFdrForm.getId());
            EpgUIServiceLocator.getEpgSqueletteUIService().updateSquelette(context);
        } else {
            // Sinon creation
            EpgUIServiceLocator.getEpgSqueletteUIService().createSquelette(context);
            isCreation = true;
        }

        addMessageQueueInSession();
        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            return redirect(String.format(URL_MODIFICATION_SQUELETTE, squeletteFdrForm.getId()));
        } else {
            if (isCreation) {
                UserSessionHelper.putUserSessionParameter(context, SQUELETTE_FORM_KEY, squeletteFdrForm);
                return redirect("/admin/fdr/squelette/creation" + MAIN_CONTENT);
            } else {
                return redirect(String.format(URL_MODIFICATION_SQUELETTE, squeletteFdrForm.getId()));
            }
        }
    }

    @GET
    @Path("squelette/modification")
    public ThTemplate getSqueletteModification(@QueryParam("id") @SwRequired @SwId String id) {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSqueletteUIService().initSqueletteAction(context);
        verifyAction(EpgActionEnum.ADMIN_MENU_MODELE_SQUELETTE, "squelette/modification");
        SqueletteFdrForm squeletteForm = EpgUIServiceLocator.getEpgSqueletteUIService().getSquelette(context);
        template.setContext(context);

        if (
            StatutModeleFDR.BROUILLON.name().equals(squeletteForm.getEtat()) &&
            Boolean.TRUE.equals(squeletteForm.getIsLockByCurrentUser())
        ) {
            template.setName("pages/admin/squelettes/edit-squelette");
        } else {
            template.setName("pages/admin/squelettes/consult-squelette");
        }

        context.setNavigationContextTitle(
            new Breadcrumb(
                squeletteForm.getIntitule(),
                String.format(URL_MODIFICATION_SQUELETTE, squeletteForm.getId()),
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        Map<String, Object> map = new HashMap<>();
        // type d'étape (pour modal ajout étape)
        map.put(SSTemplateConstants.TYPE_ETAPE, getTypeEtapeAjout(id));
        map.put(
            SSTemplateConstants.TYPE_DESTINATAIRE,
            EpgUIServiceLocator.getEpgSelectValueUIService().getSqueletteTypeDestinataire()
        );
        map.put(SQUELETTE_FORM_KEY, squeletteForm);
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSqueletteUIService().initSqueletteAction(context);
        map.put(SSTemplateConstants.MODELE_LEFT_ACTIONS, context.getActions(EpgActionCategory.SQUELETTE_LEFT_ACTIONS));
        map.put(
            SSTemplateConstants.MODELE_RIGHT_ACTIONS,
            context.getActions(EpgActionCategory.SQUELETTE_RIGHT_ACTIONS)
        );
        map.put(SSTemplateConstants.PROFIL, context.getWebcontext().getPrincipal().getGroups());
        map.put(SSTemplateConstants.IS_SQUELETTE, true);
        map.put(EpgTemplateConstants.TYPE_ACTE, EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());

        if (context.getNavigationContext().size() > 1) {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }
        template.setData(map);
        return template;
    }

    @GET
    @Path("squelette/modifier")
    public Object modifierSquelette(@QueryParam("id") String id) {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSqueletteUIService().initSqueletteAction(context);
        verifyAction(EpgActionEnum.TAB_SQUELETTE_MODIFICATION, "squelette/modifier");
        EpgUIServiceLocator.getEpgSqueletteUIService().invalidateRouteSquelette(context);
        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getInfoQueue())) {
            addMessageQueueInSession();
            return redirect(String.format(URL_MODIFICATION_SQUELETTE, id));
        } else {
            return getSquelettes();
        }
    }

    // récupération des types d'actes EPG
    @Override
    protected void setSpecificDataMap(Map<String, Object> map) {
        map.put(EpgTemplateConstants.TYPE_ACTE, EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_FDR_ACTIONS)
        );
    }

    @Override
    protected List<SelectValueDTO> getTypeEtapeAjout(String idModele) {
        return EpgUIServiceLocator
            .getEpgSelectValueUIService()
            .getRoutingTaskTypesFilteredByIdFdr(context.getSession(), idModele);
    }

    @Override
    public ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
