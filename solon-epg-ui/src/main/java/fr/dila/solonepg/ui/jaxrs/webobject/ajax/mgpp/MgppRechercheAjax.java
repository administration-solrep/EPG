package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fr.dila.solonepg.ui.bean.MessageList;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppSuggestionType;
import fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp.MgppRecherche;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheMgppAjax")
public class MgppRechercheAjax extends SolonWebObject {
    public static final String RECHERCHE_RESULT_TITLE = "recherche.result.title";
    public static final String RECHERCHE_RESULT_NUMBER_RESULTS = "recherche.result.numberResults";

    @POST
    @Path("rechercher")
    public ThTemplate doRecherche(@FormParam("search") String jsonSearch) {
        // Supprimer les critères en session
        UserSessionHelper.clearUserSessionParameter(context, MgppRecherche.JSON_SEARCH);
        return getResults(jsonSearch, true);
    }

    @POST
    @Path("reinit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reinitialiserRecherche() {
        // Supprimer les critères en session
        UserSessionHelper.clearUserSessionParameter(context, MgppRecherche.JSON_SEARCH);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @SuppressWarnings("rawtypes")
    @POST
    @Path("resultats")
    public ThTemplate getResults(
        @FormParam("search") String jsonSearch,
        @FormParam("rechercheAvancee") Boolean rechercheAvancee
    ) {
        if (!Boolean.TRUE.equals(rechercheAvancee)) {
            template.setName("fragments/table/tableCommunications");
        }

        template.setContext(context);

        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        Gson gson = new Gson();
        Map<String, Object> mapSearch = jsonSearch != null ? gson.fromJson(jsonSearch, Map.class) : new HashMap<>();

        if (jsonSearch != null) {
            UserSessionHelper.putUserSessionParameter(context, MgppRecherche.JSON_SEARCH, jsonSearch);
        } else {
            if (UserSessionHelper.getUserSessionParameter(context, MgppRecherche.JSON_SEARCH, String.class) != null) {
                mapSearch =
                    gson.fromJson(
                        UserSessionHelper.getUserSessionParameter(context, MgppRecherche.JSON_SEARCH, String.class),
                        Map.class
                    );
            }
        }

        MessageListForm msgform = new MessageListForm(jsonSearch);
        context.putInContextData(MgppContextDataKey.MAP_SEARCH, mapSearch);
        context.putInContextData(MgppContextDataKey.MESSAGE_LIST_FORM, msgform);
        CritereRechercheDTO critereRecherche = MgppUIServiceLocator
            .getRechercheUIService()
            .buildCriteresForRechercheAvancee(context);

        context.putInContextData(MgppContextDataKey.CRITERE_RECHERCHE, critereRecherche);
        context.putInContextData(MgppContextDataKey.IS_RECHERCHE_RAPIDE, true);
        context.putInContextData(MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED, critereRecherche.getMenu());
        MessageList lstResults = MgppUIServiceLocator.getMgppCorbeilleUIService().getMessageListForCorbeille(context);

        template.getData().put(STTemplateConstants.DISPLAY_TABLE, lstResults.getNbTotal() > 0);
        template.getData().put(STTemplateConstants.NB_RESULTATS, lstResults.getNbTotal());
        template.getData().put(STTemplateConstants.TITRE, ResourceHelper.getString(RECHERCHE_RESULT_TITLE));
        template
            .getData()
            .put(
                STTemplateConstants.SOUS_TITRE,
                lstResults.getNbTotal() + ResourceHelper.getString(RECHERCHE_RESULT_NUMBER_RESULTS)
            );
        template.getData().put(STTemplateConstants.RESULT_LIST, lstResults);
        template.getData().put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes(msgform));
        template.getData().put(STTemplateConstants.RESULT_FORM, msgform);
        template.getData().put(STTemplateConstants.DATA_URL, "/mgpp/recherche");
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax/mgpp/recherche/resultats");

        return template;
    }

    @GET
    @Path("suggestions")
    public String getSuggestions(
        @SwNotEmpty @QueryParam("input") String inputValue,
        @SwNotEmpty @QueryParam("typeSelection") String typeSelection
    )
        throws JsonProcessingException {
        SpecificContext context = new SpecificContext();

        context.putInContextData(MgppContextDataKey.INPUT, inputValue);

        List<SuggestionDTO> list = MgppSuggestionType.fromValue(typeSelection).getSuggestions(context);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }

    @Path("rapide")
    public Object getRechercheRapide() {
        return newObject("RechercheRapideMgppAjax", context, getMyTemplate());
    }

    @Override
    protected ThTemplate getMyTemplate() {
        ThTemplate myTemplate = new AjaxLayoutThTemplate("fragments/mgpp/recherche/resultatsRecherche", getMyContext());
        myTemplate.setData(new HashMap<>());
        return myTemplate;
    }
}
