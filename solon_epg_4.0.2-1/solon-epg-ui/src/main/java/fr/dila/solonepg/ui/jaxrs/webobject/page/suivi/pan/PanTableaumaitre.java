package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.helper.PanHelper;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTableaumaitre")
public class PanTableaumaitre extends PanAbstractOngletContextuel {
    public static final String MASQUER_RATIFICATION = "masquerRatification";

    public PanTableaumaitre() {
        super();
    }

    @Override
    public void loadTabContent(Map<String, Serializable> mapSearch) {
        context.putInContextData(
            PanContextDataKey.JSON_SEARCH_TABLEAU_MAITRE,
            mapSearch != null ? mapSearch : new HashMap<>()
        );

        Map<String, Object> templateData = template.getData();
        String currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);

        if (currentSection.equals(ActiviteNormativeEnum.ORDONNANCES.getId())) {
            Boolean masquer = ObjectHelper.requireNonNullElse(
                UserSessionHelper.getUserSessionParameter(context, MASQUER_RATIFICATION),
                true
            );
            context.putInContextData(PanContextDataKey.MASQUER_RATIFIE, masquer);

            templateData.put(MASQUER_RATIFICATION, masquer);
        }

        LoisSuiviesForm loisSuiviForm = PanHelper.getPaginateFormFromUserSession(mapSearch, context);
        context.putInContextData(PanContextDataKey.PAN_FORM, loisSuiviForm);
        ActiviteNormativeEnum currentSectionAN = context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM);

        @SuppressWarnings("rawtypes")
        AbstractPanSortedList<AbstractMapDTO> textesMaitres = PanUIServiceLocator
            .getPanUIService()
            .getTableauMaitre(context);

        Map<String, Object> otherParams = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        templateData.put(STTemplateConstants.OTHER_PARAMETER, otherParams);

        List<Action> addAction = context.getActions(PanActionCategory.TEXTE_MAITRE_ADD);

        templateData.put(PanTemplateConstants.TEXTE_MAITRE_ADD_ACTION, addAction.isEmpty() ? null : addAction.get(0));
        templateData.put(
            PanTemplateConstants.IS_TRAITES_ACCORDS,
            currentSection.equals(ActiviteNormativeEnum.TRAITES_ET_ACCORDS.getId())
        );
        templateData.put(
            PanTemplateConstants.IS_TRANSPOSITION_DIRECTIVES,
            currentSection.equals(ActiviteNormativeEnum.TRANSPOSITION.getId())
        );

        templateData.put(STTemplateConstants.RESULT_FORM, loisSuiviForm);
        templateData.put(STTemplateConstants.RESULT_LIST, textesMaitres);
        templateData.put(STTemplateConstants.LST_COLONNES, textesMaitres.getListeColonnes());
        templateData.put(STTemplateConstants.NB_RESULTS, textesMaitres.getNbTotal());
        templateData.put(
            PanTemplateConstants.TEXTE_MAITRE_DELETE_ACTION,
            context.getAction(PanActionEnum.TEXTE_MAITRE_DELETE)
        );
        templateData.put(PanTemplateConstants.PAN_CONTEXT, context.getFromContextData(PanContextDataKey.PAN_CONTEXT));
        templateData.put(
            PanTemplateConstants.ACCESS_TAB_MIN,
            context.getAction(PanActionEnum.TAB_TABLEAU_MAITRE_MINISTERES) != null
        );
        templateData.put(PanTemplateConstants.TYPE_NOR_AUTO_COMPLETE, currentSectionAN.getNorType());
        templateData.put(STTemplateConstants.LST_SORTED_COLONNES, textesMaitres.getListeSortedColonnes());
        templateData.put(STTemplateConstants.LST_SORTABLE_COLONNES, textesMaitres.getListeSortableAndVisibleColonnes());

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, textesMaitres, templateData);
    }

    @Path("supprimer/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerTexteMaitre(@PathParam("id") String idTexteMaitre) {
        ActiviteNormativeEnum currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM);
        verifyUpdaterOnlyAccess(context.getSession(), currentSection);
        context.setCurrentDocument(idTexteMaitre);
        PanUIServiceLocator.getPanUIService().removeFromActiviteNormative(context);
        // On recharge toute la page du tableau-maître car on ne veut pas que le texte-maitre reste affiché après l'avoir supprimé
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("ajouter/{nor}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterTexteMaitre(@PathParam("nor") String nor) throws UnsupportedEncodingException {
        ActiviteNormativeEnum currentSection = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        verifyMinisterOrUpdaterAccess(context.getSession(), currentSection);
        nor = URLDecoder.decode(nor, "UTF-8"); // à cause du format du numéro de directive qui contient des "/"
        context.putInContextData(PanContextDataKey.ACTIVITE_NORMATIVE_NOR, nor);
        PanUIServiceLocator.getPanUIService().addToActiviteNormative(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Override
    public ThTemplate sortPaginateTable(@FormParam("search") String jsonSearch) {
        String idContextuel = UserSessionHelper.getUserSessionParameter(context, PanTemplateConstants.ID_CONTEXTUEL);
        template.getData().put(PanTemplateConstants.ID_CONTEXTUEL, idContextuel); // pour la surbrillance dans le tableau des mesures

        Map<String, Serializable> mapSearch = FiltreUtils.extractMapSearch(jsonSearch);
        context.putInContextData(PanContextDataKey.MAP_SEARCH, mapSearch);

        return super.sortPaginateTable(jsonSearch);
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/tableau-maitre";
    }

    @Override
    protected PanUserSessionKey getSortPaginateParameterKey() {
        return PanUserSessionKey.JSON_SEARCH_TABLEAU_MAITRE;
    }
}
