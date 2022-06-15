package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan.PanRechercheExperteAjax;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.page.SSRequeteExperte;
import fr.dila.st.core.requete.recherchechamp.descriptor.ChampDescriptor;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.POST;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanRecherchean")
public class PanRechercheExperte extends PanAbstractOngletContextuel implements SSRequeteExperte {
    private static final String CATEGORIES = "categories";
    private static final String IS_FIRST_CHAMP = "isFirstChamp";
    private static final String REQUETE_EXPERTE_DTO = "requeteExperteDTO";

    public static final String DIRECTIVES_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.pan.recherche-experte-pan-directives-champs-contrib";
    public static final String ORDONANCES_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.pan.recherche-experte-pan-ordonances-champs-contrib";
    public static final String HABILITATIONS_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.pan.recherche-experte-pan-habilitations-champs-contrib";
    public static final String LOIS_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.pan.recherche-experte-pan-lois-champs-contrib";
    public static final String RATIFICATIONS_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.pan.recherche-experte-pan-ratifications-champs-contrib";
    public static final String TRAITES_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.pan.recherche-experte-pan-traites-champs-contrib";

    public PanRechercheExperte() {
        super();
    }

    public static String getChampsforSection(SpecificContext context) {
        ActiviteNormativeEnum anEnum = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        switch (anEnum) {
            case APPLICATION_DES_LOIS:
                return LOIS_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME;
            case APPLICATION_DES_ORDONNANCES:
                return ORDONANCES_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME;
            case TRANSPOSITION:
                return DIRECTIVES_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME;
            case ORDONNANCES_38C:
                return HABILITATIONS_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME;
            case ORDONNANCES:
                return RATIFICATIONS_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME;
            case TRAITES_ET_ACCORDS:
                return TRAITES_RECHECHE_EXPERTE_CHAMP_CONTRIB_NAME;
            default:
                return null;
        }
    }

    @Override
    public void loadTabContent(Map<String, Serializable> mapSearch) {
        RequeteExperteDTO requeteExperteDTO = UserSessionHelper.getUserSessionParameter(
            context,
            getDtoSessionKey(context),
            RequeteExperteDTO.class
        );

        if (requeteExperteDTO == null) {
            List<ChampDescriptor> champs = STServiceLocator
                .getRechercheChampService()
                .getChamps(getChampsforSection(context));
            requeteExperteDTO = new RequeteExperteDTO();
            requeteExperteDTO.setChamps(champs);
            UserSessionHelper.putUserSessionParameter(context, getDtoSessionKey(context), requeteExperteDTO);
        } else if (
            UserSessionHelper.getUserSessionParameter(context, getResultsSessionKey(context), Map.class) != null
        ) {
            template
                .getData()
                .putAll(UserSessionHelper.getUserSessionParameter(context, getResultsSessionKey(context), Map.class));
        }

        if (
            UserSessionHelper.getUserSessionParameter(
                context,
                getDtoSessionKey(context) + "_resetSearch",
                Boolean.class
            ) !=
            null
        ) {
            LoisSuiviesForm form = UserSessionHelper.getUserSessionParameter(
                context,
                getDtoSessionKey(context) + "_rechercheForm",
                LoisSuiviesForm.class
            );
            UserSessionHelper.clearUserSessionParameter(context, getDtoSessionKey(context) + "_resetSearch");

            PanRechercheExperteAjax.initRecherche(context, getDtoSessionKey(context), template.getData(), form);
        }

        context.putInContextData(
            SSContextDataKey.REQUETE_EXPERTE_DTO,
            UserSessionHelper.getUserSessionParameter(context, getDtoSessionKey(context), RequeteExperteDTO.class)
        );

        template.getData().put(REQUETE_EXPERTE_DTO, requeteExperteDTO);
        template.getData().put(IS_FIRST_CHAMP, requeteExperteDTO.getRequetes().isEmpty());
        template
            .getData()
            .put(
                CATEGORIES,
                requeteExperteDTO
                    .getChamps()
                    .stream()
                    .map(ChampDescriptor::getCategorie)
                    .filter(Objects::nonNull)
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList())
            );
        template
            .getData()
            .put(STTemplateConstants.ACTION, context.getActions(EpgActionCategory.PAN_RECHERCHE_EXPERTE_ACTIONS));
        template.getData().put(EpgTemplateConstants.RECHERCHE_SESSION_KEY, getDtoSessionKey(context));
        template
            .getData()
            .put(
                STTemplateConstants.DATA_AJAX_URL,
                "/ajax/suivi/pan/" +
                context.getFromContextData(PanContextDataKey.CURRENT_SECTION) +
                "/recherche/experte/resultats"
            );
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/recherche-an";
    }

    @Override
    public String getSuffixForSessionKeys(SpecificContext context) {
        return "_pan_" + context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
    }

    @Override
    protected PanUserSessionKey getSortPaginateParameterKey() {
        return PanUserSessionKey.JSON_SEARCH_RECHERCHE_EXPERTE;
    }

    @Override
    @POST
    public ThTemplate sortPaginateTable(String jsonSearch) {
        return super.sortPaginateTable(jsonSearch);
    }
}
