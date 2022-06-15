package fr.dila.solonepg.ui.enums.pan;

import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.dto.TexteMaitreTraiteDTO;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.api.dto.TranspositionDirectiveDTO;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.AbstractPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.ConsultTexteMaitreDTO;
import fr.dila.solonepg.ui.bean.pan.PANExportParametreDTO;
import fr.dila.solonepg.ui.bean.pan.PanActionsDTO;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationOuSuiviDTO;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.enums.ContextDataKey;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public enum PanContextDataKey implements ContextDataKey {
    ACTIVITE_NORMATIVE_NOR(String.class, "activiteNormativeNor"),
    ACTIVITE_NORMATIVE_PROGRAMMATION_ID(String.class, "activiteNormativeProgrammationId"),
    ANNEE(Integer.class, "annee"),
    AN_REPORT(ANReport.class, "ANReport"),
    CONSULT_TEXTEM_DTO(ConsultTexteMaitreDTO.class, "consultTexteMaitreDto"),
    CURRENT_SECTION(String.class, "currentSection"),
    CURRENT_SECTION_ENUM(ActiviteNormativeEnum.class, "currentSectionEnum"),
    CURRENT_SUB_TAB(String.class, "currentPanSubtab"),
    CURRENT_TAB(String.class, "currentPanTab"),
    DATE_SIGNATURE(Date.class, "dateSignature"),
    DOSSIER_ID(String.class, "dossierId"),
    EXPORT_PAN_STAT(ExportPANStat.class, "exportPANStat"),
    FIRST_TABLE_ELT(TexteMaitre.class, "firstTableElt"),
    FIRST_TABLE_ELT_DTO(AbstractMapDTO.class, "firstTableEltDto"),
    FIRST_TABLE_ELT_FORM(Object.class, "firstTableEltForm"),
    FIRST_TABLE_ELT_NOR(String.class, "firstTableEltNor"),
    FIRST_TABLE_ID(String.class, "firstTableId"),
    FIRST_TABLE_LIST(List.class, "firstTableList"),
    FIRST_TABLE_TYPE(String.class, "premierTableauType"),
    FORCE_EXPORT_LEGIS_PREC(Boolean.class, "forceExportLegisPrec"),
    FORCE_NON_PAGINATED_TABLEAU_MAITRE(Boolean.class, "forceNonPaginatedTableauMaitre"),
    FRAGMENT_FILE(String.class, "fragmentFile"),
    HABILITATION_DTO(HabilitationDTO.class, "habilitationDto"),
    HABILITATION_LIST(List.class, "habilitationList"),
    HAS_SURBRILLANCE(Boolean.class, "hasSurbrillance"),
    INPUT_VALUES(Map.class, "inputValues"),
    IS_PERIODE_PAR_MOIS(Boolean.class, "isPeriodeParMois"),
    JSON_SEARCH_HISTORIQUE_MAJ(Map.class, "jsonSearchHistoriquemaj"),
    JSON_SEARCH_RECHERCHE_EXPERTE(Map.class, "jsonSearchRechercheExperte"),
    JSON_SEARCH_STATISTIQUES_PUBLICATION(Map.class, "jsonSearchStatistiquesPublication"),
    JSON_SEARCH_TABLEAU_LOIS(Map.class, "jsonSearchTableauLois"),
    JSON_SEARCH_TABLEAU_MAITRE(Map.class, "jsonSearchTableauMaitre"),
    JSON_SEARCH_TABLEAU_MAITRE_MINISTERES(Map.class, "jsonSearchTableauMaitreMinisteres"),
    LEGISLATURE_EN_COURS(Boolean.class, "legisEnCours"),
    LEGISLATURE_INPUT(String.class, "legislatureInput"),
    LEGISLATURE_LIST(List.class, "legislatureList"),
    LEGISLATURE_OLD_INPUT(String.class, "legislatureOldInput"),
    LOI_RATIFICATION(String.class, "loiRatification"),
    LOI_RATIFICATION_DTO(LoiDeRatificationDTO.class, "loiRatificationDto"),
    LOI_RATIFICATION_LIST(List.class, "loiRatificationList"),
    MAP_SEARCH(Map.class, "mapSearch"),
    MASQUER_APPLIQUE(Boolean.class, "masquerApplique"),
    MASQUER_RATIFIE(Boolean.class, "masquerRATIFIE"),
    MESURE_APPLICATIVE_NUMBER(Integer.class, "mesureApplicativeNumber"),
    MESURE_APPLICATIVE_ORDER(Integer.class, "mesureApplicativeOrder"),
    MINISTERE_PILOTE_ID(String.class, "ministerePiloteId"),
    MOIS(Integer.class, "mois"),
    NEW_TEXTE(String.class, "newText"),
    NUMERO_NOR(String.class, "numeroNor"),
    ONGLET_ACTIF(PanOnglet.class, "ongletActif"),
    ORDONNANCE_DTO(OrdonnanceDTO.class, "ordonnanceDto"),
    ORDONNANCE_HABILITATION_DTO(OrdonnanceHabilitationDTO.class, "ordonnanceHabilitationDto"),
    ORDONNANCE_HABILITATION_LIST(List.class, "ordonnanceHabilitationList"),
    ORDONNANCE_HABILITATION_MAP(Map.class, "ordonnanceHabilitationMap"),
    PAN_ACTION_DTO(PanActionsDTO.class, "panActionDTO"),
    PAN_CONTEXT(String.class, "panContext"),
    PAN_EXPORT_PARAMETRE_DTO(PANExportParametreDTO.class, "PANExportParametreDTO"),
    PAN_FILTERS(Map.class, "panFilters"),
    PAN_FORM(AbstractSortablePaginationForm.class, "form"),
    PAN_TREE_NODE(PanTreeNode.class, "PanTreeNode"),
    PARAMETER_STAT_DOC_ID(String.class, "parameterStatDocId"),
    PUBLICATION(Boolean.class, "publication"),
    PUBLICATION_LEGIS_PREC_VISIBLE(Boolean.class, "publicationLegisPrecVisible"),
    RELOAD_FROM_DOSSIER(Boolean.class, "reloadFromDossier"),
    SEARCH_FORM_KEY(String.class, "searchFormKey"),
    SECOND_TABLE_ELT_DTO(AbstractMapDTO.class, "secondTableEltDto"),
    SECOND_TABLE_ELT_FORM(Object.class, "secondTableEltForm"),
    SECOND_TABLE_ELT_ID(String.class, "secondTableEltId"),
    SECOND_TABLE_ELT_NOR(String.class, "secondTableEltNor"),
    SECOND_TABLE_LIST(AbstractPanUnsortedList.class, "secondTableList"),
    SECOND_TABLE_MAP(Map.class, "secondTableMap"),
    SECOND_TABLE_TYPE(String.class, "secondTableauType"),
    SECOND_TABLE_TYPE_NOR(String.class, "secondTableauTypeNor"),
    SUIVI_CURRENT_SECTION(String.class, "suiviCurrentSection"),
    SUIVI_CURRENT_TAB(String.class, "suiviCurrentTab"),
    SUIVI_TYPE_ACTE(String.class, "suiviTypeActe"),
    TABLEAU_PROGRAMMATION_DTO(TableauProgrammationOuSuiviDTO.class, "tableauProgrammationDto"),
    TEMPORARY_LEGIS(Map.class, "temporaryLegis"),
    TEMPORARY_LEGIS_REVERSED(Map.class, "temporaryLegisReversed"),
    TEXTE_MAITRE_DTO(AbstractMapDTO.class, "texteMaitreDto"),
    TEXTE_MAITRE_FORM(Object.class, "texteMaitreForm"),
    TEXTE_MAITRE_ID(String.class, "texteMaitreId"),
    TEXTE_MAITRE_TRAITE_DTO(TexteMaitreTraiteDTO.class, "texteMaitreTraiteDto"),
    TEXTE_TRANSPOSITION_DTO(TexteTranspositionDTO.class, "texteTranspositionDTO"),
    TEXTE_TRANSPOSITION_LIST(List.class, "texteTranspositionList"),
    TITRE(String.class, "titre"),
    TRANSPOSITION_DIRECTIVE_DTO(TranspositionDirectiveDTO.class, "transpositionDirectiveDto"),
    TRANSPOSITION_DIRECTIVE_ID(String.class, "transpositionDirectiveId"),
    TRANSPOSITION_DIRECTIVE_LIST(List.class, "transpositionDirectiveList");

    private final Class<?> valueType;
    private final String specificKey;

    PanContextDataKey(Class<?> valueType, String specificKey) {
        this.valueType = valueType;
        this.specificKey = specificKey;
    }

    @Override
    public String getName() {
        return StringUtils.defaultIfBlank(specificKey, name());
    }

    @Override
    public Class<?> getValueType() {
        return valueType;
    }
}
