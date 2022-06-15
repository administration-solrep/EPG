package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonmgpp.core.service.TableReferenceServiceImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public enum MgppSuggestionType {
    AUTEUR("auteur", MgppSuggestionType.LABEL_INCLURE_MANDAT, MgppSuggestionType::getSuggestionsFromIdentite),
    COAUTEUR("coauteur", MgppSuggestionType.LABEL_INCLURE_MANDAT, MgppSuggestionType::getSuggestionsFromIdentite),
    COMISSIONS(
        "commissions",
        MgppSuggestionType.LABEL_INCLURE_COMMISSION,
        MgppSuggestionType::getSuggestionsFromOrganisme
    ),
    COMMISSION_SAISIE(
        "commissionSaisie",
        MgppSuggestionType.LABEL_INCLURE_COMMISSION,
        MgppSuggestionType::getSuggestionsFromOrganisme
    ),
    COMMISSION_SAISIE_AUF_OND(
        "commissionSaisieAuFond",
        MgppSuggestionType.LABEL_INCLURE_COMMISSION,
        MgppSuggestionType::getSuggestionsFromOrganisme
    ),
    COMMISSION_SAISIE_POUR_AVIS(
        "commissionSaisiePourAvis",
        MgppSuggestionType.LABEL_INCLURE_COMMISSION,
        MgppSuggestionType::getSuggestionsFromOrganisme
    ),
    EMETTEUR("emetteur", null, MgppSuggestionType::getSuggestionsFromIdentite),
    GROUPE_PARLEMENTAIRE(
        "groupeParlementaire",
        MgppSuggestionType.LABEL_INCLURE_COMMISSION,
        MgppSuggestionType::getSuggestionsForGroupeParlementaire
    ),
    OEP("oep", MgppSuggestionType::getSuggestionsForOEP),
    ORGANISME(
        "organisme",
        MgppSuggestionType.LABEL_INCLURE_COMMISSION,
        MgppSuggestionType::getSuggestionsFromAuditionOrganisme
    ),
    PARLEMENTAIRES_SUPPLEANTS(
        "parlementaireSuppleantList",
        MgppSuggestionType.LABEL_INCLURE_MANDAT,
        MgppSuggestionType::getSuggestionsFromIdentite
    ),
    PARLEMENTAIRES_TITULAIRES("parlementaireTitulaire", MgppSuggestionType::getSuggestionsFromIdentite),
    PARLEMENTAIRES_TITULAIRES_LIST(
        "parlementaireTitulaireList",
        MgppSuggestionType.LABEL_INCLURE_MANDAT,
        MgppSuggestionType::getSuggestionsFromIdentite
    ),
    PROFILS("Profils", MgppSuggestionType::getSuggestionsForProfils),
    RAPPORTEURS(
        "rapporteurList",
        MgppSuggestionType.LABEL_INCLURE_MANDAT,
        MgppSuggestionType::getSuggestionsFromIdentite
    ),
    REPRESENTANT_OEP_AN("RepresentantOEPAN", MgppSuggestionType::getSuggestionsFromIdentiteAN),
    REPRESENTANT_OEP_SE("RepresentantOEPSE", MgppSuggestionType::getSuggestionsFromIdentiteSE),
    DEFAULT("", MgppSuggestionType::getDefaultSuggestion);

    private static final int MAX_SUGGESTION = 10;

    private static final String INCLURE_PARAM_NAME = "labelInclure";
    public static final String LABEL_INCLURE_MANDAT = "label.suggestion.inclure.Identite";
    public static final String LABEL_INCLURE_COMMISSION = "label.suggestion.inclure.Organisme";
    private final String value;
    private final String labelInclusion;
    private final Function<SpecificContext, List<SuggestionDTO>> suggestions;

    MgppSuggestionType(String value, Function<SpecificContext, List<SuggestionDTO>> suggestions) {
        this(value, null, suggestions);
    }

    MgppSuggestionType(String value, String label, Function<SpecificContext, List<SuggestionDTO>> suggestions) {
        this.value = value;
        this.suggestions = suggestions;
        this.labelInclusion = label;
    }

    public String getValue() {
        return value;
    }

    public List<SuggestionDTO> getSuggestions(SpecificContext context) {
        return suggestions.apply(context);
    }

    public static MgppSuggestionType fromValue(String value) {
        return Stream
            .of(values())
            .filter(suggestion -> Objects.equals(suggestion.getValue(), value))
            .findFirst()
            .orElse(MgppSuggestionType.DEFAULT);
    }

    private static List<SuggestionDTO> getSuggestionsFromIdentite(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TABLE_REF, TableReferenceServiceImpl.IDENTITE);
        return MgppUIServiceLocator.getMgppSuggestionUIService().getSuggestions(context);
    }

    private static List<SuggestionDTO> getSuggestionsFromIdentiteAN(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TABLE_REF, TableReferenceServiceImpl.IDENTITE);
        context.putInContextData(MgppContextDataKey.PROPRIETAIRE, "ASSEMBLEE_NATIONALE");
        return MgppUIServiceLocator.getMgppSuggestionUIService().getSuggestions(context);
    }

    private static List<SuggestionDTO> getSuggestionsFromIdentiteSE(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TABLE_REF, TableReferenceServiceImpl.IDENTITE);
        context.putInContextData(MgppContextDataKey.PROPRIETAIRE, "SENAT");
        return MgppUIServiceLocator.getMgppSuggestionUIService().getSuggestions(context);
    }

    private static List<SuggestionDTO> getDefaultSuggestion(SpecificContext context) {
        context.getMessageQueue().addWarnToQueue("Type de suggestion inconnu");
        return Collections.emptyList();
    }

    private static List<SuggestionDTO> getSuggestionsForGroupeParlementaire(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TYPE_ORGANISME, "GROUPE_PARLEMENTAIRE");
        return getSuggestionsFromOrganisme(context);
    }

    private static List<SuggestionDTO> getSuggestionsFromAuditionOrganisme(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TYPE_ORGANISME, "AUDITION");
        return getSuggestionsFromOrganisme(context);
    }

    private static List<SuggestionDTO> getSuggestionsForOEP(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TYPE_ORGANISME, "OEP");
        context.putInContextData(MgppContextDataKey.BASIC_SEARCH, true);
        return getSuggestionsFromOrganisme(context);
    }

    private static List<SuggestionDTO> getSuggestionsFromOrganisme(SpecificContext context) {
        context.putInContextData(MgppContextDataKey.TABLE_REF, TableReferenceServiceImpl.ORGANISME);
        return MgppUIServiceLocator.getMgppSuggestionUIService().getSuggestions(context);
    }

    private static List<SuggestionDTO> getSuggestionsForProfils(SpecificContext context) {
        return STServiceLocator
            .getSTDirectoryService()
            .getSuggestions(
                context.getFromContextData(MgppContextDataKey.INPUT),
                STConstant.ORGANIGRAMME_PROFILE_DIR,
                "groupname"
            )
            .stream()
            .filter(profil -> !"administrators".equals(profil))
            .map(profil -> new SuggestionDTO(profil, profil))
            .limit(MAX_SUGGESTION)
            .collect(Collectors.toList());
    }

    public List<Parametre> buildParametres() {
        List<Parametre> parametres = new ArrayList<>();
        parametres.add(new Parametre("autocomplete", BooleanUtils.toStringTrueFalse(true)));
        if (StringUtils.isNotBlank(labelInclusion)) {
            parametres.add(new Parametre(INCLURE_PARAM_NAME, labelInclusion));
        }

        return parametres;
    }

    public String getLabelInclusion() {
        return labelInclusion;
    }
}
