package fr.dila.solonepg.ui.jaxrs.webobject.page.fdr;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.services.EpgModeleFdrListUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.ss.api.criteria.SubstitutionCriteria;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SubstitutionMass")
public class EpgSubstitutionFDRMass extends SolonWebObject {
    private static final String URL_BREADCRUMB = "/selection/substitution";
    private static final int LIST_MODELE_ORDER = Breadcrumb.SUBTITLE_ORDER + 1;
    public static final String KEY_IS_MASS_SUBSTITUTION = "isMassSubstitution";

    public EpgSubstitutionFDRMass() {
        super();
    }

    @GET
    @Path("/liste")
    public ThTemplate getListModeleSubstitution() throws IllegalAccessException, InstantiationException {
        // Créer les critéria pour filtrer les modèles par ministère du user courant
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        SubstitutionCriteria criteria = new SubstitutionCriteria(new ArrayList<String>(principal.getMinistereIdSet()));
        context.putInContextData(SSContextDataKey.SUBSTITUTION_CRITERIA, criteria);

        // Récupération de la liste des modèles disponnible pour la substitution
        EpgModeleFdrListUIService modeleFDRListUIService = EpgUIServiceLocator.getEpgModeleFdrListUIService();
        ModeleFDRList lstResults = modeleFDRListUIService.getModelesFDRSubstitution(context);

        ThTemplate template = getMyTemplate(context);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("fdr.substituer.liste.title.navigation"),
                URL_BREADCRUMB + "/liste#main_content",
                LIST_MODELE_ORDER
            )
        );
        template.setName("pages/fdr/listeModeleFdrSubstitution");
        template.setContext(context);

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.RESULT_FORM, new ModeleFDRListForm());
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        if (context.getNavigationContext().size() > 1) {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }
        map.put(KEY_IS_MASS_SUBSTITUTION, true);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/selection/substitution/liste");
        template.setData(map);

        return template;
    }

    @GET
    @Path("/consult")
    public ThTemplate getModeleSubstitutionConsult(@QueryParam("idModele") String idModele)
        throws IllegalAccessException, InstantiationException {
        context.setCurrentDocument(idModele);
        EpgModeleFdrForm modeleForm = new EpgModeleFdrForm();
        SolonEpgUIServiceLocator.getEpgModeleFdrFicheUIService().consultModeleSubstitution(context, modeleForm);

        ThTemplate template = getMyTemplate(context);
        template.setName("pages/admin/modele/consultModeleFDR");
        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("outilSelection.substitution.breadcrumb", modeleForm.getIntitule()),
                URL_BREADCRUMB + "/consult?idModele=" + modeleForm.getId(),
                LIST_MODELE_ORDER + 2
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.ID_MODELE, modeleForm.getId());
        map.put(SSTemplateConstants.TYPE_ETAPE, SSUIServiceLocator.getSSSelectValueUIService().getRoutingTaskTypes());
        map.put(SSTemplateConstants.MODELE_FORM, modeleForm);
        map.put(
            SSTemplateConstants.MODELE_LEFT_ACTIONS,
            context.getActions(EpgActionCategory.SUBSTITUTION_MASS_ACTIONS_LEFT)
        );
        map.put(
            SSTemplateConstants.MODELE_RIGHT_ACTIONS,
            context.getActions(EpgActionCategory.SUBSTITUTION_MASS_ACTIONS_RIGHT)
        );

        if (context.getNavigationContext().size() > 1) {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }
        map.put(KEY_IS_MASS_SUBSTITUTION, true);
        template.setData(map);
        return template;
    }

    @GET
    @Path("/valider")
    public Response validerSubstitutionModele(@QueryParam("idModele") String idModele) {
        context.putInContextData(SSContextDataKey.ID_MODELE, idModele);

        EpgUIServiceLocator.getEpgSelectionToolUIService().substituerMassRoute(context);

        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
        List<Breadcrumb> navigationContext = context.getNavigationContext();
        return redirect(navigationContext.get(navigationContext.size() - 3).getUrl());
    }

    protected ThTemplate getMyTemplate(SpecificContext context) throws IllegalAccessException, InstantiationException {
        @SuppressWarnings("unchecked")
        Class<ThTemplate> oldTemplate = (Class<ThTemplate>) context
            .getWebcontext()
            .getUserSession()
            .get(SpecificContext.LAST_TEMPLATE);
        if (oldTemplate == ThTemplate.class) {
            return new EpgLayoutThTemplate();
        } else {
            return oldTemplate.newInstance();
        }
    }
}
