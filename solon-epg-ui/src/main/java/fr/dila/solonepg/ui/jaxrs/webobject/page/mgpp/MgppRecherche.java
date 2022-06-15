package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import static java.util.Optional.ofNullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import fr.dila.solonepg.ui.bean.MgppRechercheDynamique;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.model.bean.MgppRechercheRapideForm;
import fr.dila.solonepg.ui.th.model.mgpp.MgppDossierParlementaireTemplate;
import fr.dila.solonepg.ui.th.model.mgpp.MgppRechercheTemplate;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.bean.RequeteLigneDTO;
import fr.dila.ss.ui.jaxrs.webobject.page.SSRequeteExperte;
import fr.dila.st.core.requete.recherchechamp.RechercheChampService;
import fr.dila.st.core.requete.recherchechamp.descriptor.ChampDescriptor;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MgppRecherche")
public class MgppRecherche extends SolonWebObject implements SSRequeteExperte {
    public static final String JSON_SEARCH = "jsonSearch";
    public static final String MGPP_RECHERCHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.mgpp.recherche-experte-champ-contrib";

    private static final String CATEGORIES = "categories";
    private static final String IS_FIRST_CHAMP = "isFirstChamp";
    private static final String REQUETE_EXPERTE_DTO = "requeteExperteDTO";

    public MgppRecherche() {
        super();
    }

    @Override
    public String getSuffixForSessionKeys(SpecificContext context) {
        return "_MGPP";
    }

    @SuppressWarnings("unchecked")
    @GET
    public ThTemplate getHome() {
        template.setName("pages/mgpp/recherche-experte");
        context.removeNavigationContextTitle();
        template.setContext(context);

        RechercheChampService champService = STServiceLocator.getRechercheChampService();
        Map<String, Object> map = new HashMap<>();

        RequeteExperteDTO requeteExperteDTO = UserSessionHelper.getUserSessionParameter(
            context,
            getDtoSessionKey(context),
            RequeteExperteDTO.class
        );
        if (requeteExperteDTO == null) {
            requeteExperteDTO = new RequeteExperteDTO();
            List<ChampDescriptor> champs = champService.getChamps(MGPP_RECHERCHE_EXPERTE_CHAMP_CONTRIB_NAME);
            requeteExperteDTO.setChamps(champs);
            UserSessionHelper.putUserSessionParameter(context, getDtoSessionKey(context), requeteExperteDTO);
        } else if (
            UserSessionHelper.getUserSessionParameter(context, getResultsSessionKey(context), Map.class) != null
        ) {
            map.putAll(UserSessionHelper.getUserSessionParameter(context, getResultsSessionKey(context), Map.class));
        }

        boolean isFirstChamp = requeteExperteDTO.getRequetes().isEmpty();
        String existingChampCategory = isFirstChamp
            ? StringUtils.EMPTY
            : ofNullable(requeteExperteDTO.getRequetes().get(0))
                .map(RequeteLigneDTO::getChamp)
                .map(ChampDescriptor::getCategorie)
                .orElse(StringUtils.EMPTY);
        map.put(REQUETE_EXPERTE_DTO, requeteExperteDTO);
        map.put(IS_FIRST_CHAMP, isFirstChamp);
        map.put(
            CATEGORIES,
            isFirstChamp || StringUtils.isEmpty(existingChampCategory)
                ? requeteExperteDTO
                    .getChamps()
                    .stream()
                    .map(ChampDescriptor::getCategorie)
                    .filter(Objects::nonNull)
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList())
                : ImmutableList.of(existingChampCategory)
        );
        map.put(STTemplateConstants.DISPLAY_TABLE, false);
        map.put(STTemplateConstants.ACTION, context.getActions(MgppActionCategory.MGPP_RECHERCHE_EXPERTE_ACTIONS));
        template.setData(map);
        return template;
    }

    @Path("rapide/rechercher")
    public Object getRechercheRapide(@SwBeanParam MgppRechercheRapideForm form) {
        ThTemplate template = new MgppDossierParlementaireTemplate();

        template.setName("pages/mgpp/rechercheRapide");
        template.setData(new HashMap<>());
        return newObject("RechercheRapideMgppAjax", context, template);
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("{typeParlementaire}")
    public ThTemplate getRechercheAvancee(@PathParam("typeParlementaire") String typeParlementaire) {
        template.setName("pages/mgpp/recherche");
        context.removeNavigationContextTitle();
        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                typeParlementaire,
                "/mgpp/recherche/" + typeParlementaire,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        context.putInContextData(STContextDataKey.ID, typeParlementaire);

        String jsonSearch = UserSessionHelper.getUserSessionParameter(context, JSON_SEARCH, String.class);
        Gson gson = new Gson();
        Map<String, Serializable> mapSearch = StringUtils.isNotEmpty(jsonSearch)
            ? gson.fromJson(jsonSearch, Map.class)
            : Collections.emptyMap();
        if (!StringUtils.equals(typeParlementaire, (String) mapSearch.get("typeParlementaire"))) {
            UserSessionHelper.clearUserSessionParameter(context, JSON_SEARCH);
            mapSearch = Collections.emptyMap();
        }
        context.putInContextData(MgppContextDataKey.MAP_SEARCH, mapSearch);
        MgppRechercheDynamique dto = MgppUIServiceLocator
            .getRechercheUIService()
            .getCriteresRechercheDynamique(context);
        template.getData().put("rechercheDynamique", dto);
        template.getData().put(STTemplateConstants.DISPLAY_TABLE, false);
        template.getData().put("typeParlementaire", typeParlementaire);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new MgppRechercheTemplate();
    }
}
