package fr.dila.solonepg.ui.bean.pan.birt;

import avro.shaded.com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.ui.constants.pan.PanConstants;
import fr.dila.st.core.util.ResourceHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PanBirtStats {
    private PanConstants.Section section;
    private PanConstants.Tab tab;
    private PanConstants.SubTab subtab;
    private PanConstants.SourceMapping sourceMapping;

    public static final Map<String, PanBirtStats> PAN_BIRT_TREE_RELATION = ImmutableMap
        .<String, PanBirtStats>builder()
        // ----------------------
        // LOIS
        // ----------------------
        .put(
            ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TAB_M_MESURES_APP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TAB_M_MESURES_APP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_LOI.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TAB_M_BS,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TAUX_APPLICATION_LOI_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_LOI,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TAB_MIN_MESURES_APP_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TAB_MIN_MESURES_APP_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TAB_MIN_TX_EXE_LOIS,
                PanConstants.SourceMapping.TAUX_EXECUTION_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_BS_PAR_MIN,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_TAUX_APPLICATION_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_MIN,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURES_APPELANT_DECRET_NON_PUB.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DEC,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURES_APPELANT_DECRET_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DEC,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_DEPASSE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DATE_DEP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_DEPASSE_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DATE_DEP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_DIFFEREE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DIFF,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_DEBUT_LEGISLATURE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_INDICATEURS_LOLF,
                PanConstants.SubTab.SUBTAB_IND_TX_EXE_LOIS,
                PanConstants.SourceMapping.TAUX_EXECUTION_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_MISE_APPLICATION.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_INDICATEURS_LOLF,
                PanConstants.SubTab.SUBTAB_IND_DELAI_MISE_APP_LOIS,
                PanConstants.SourceMapping.INDICATEURS_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_DERNIERE_SESSION.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_INDICATEURS_LOLF,
                PanConstants.SubTab.SUBTAB_IND_TX_EXE_SESSION_LOIS,
                PanConstants.SourceMapping.INDICATEURS_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_BS,
                PanConstants.SubTab.SUBTAB_BS_PAR_LOI,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_BS,
                PanConstants.SubTab.SUBTAB_BS_PAR_MIN,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEM_NATURE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_BS,
                PanConstants.SubTab.SUBTAB_BS_PAR_NAT_TXT,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEM_VOTE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_BS,
                PanConstants.SubTab.SUBTAB_BS_PAR_PROC_VOT,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_LOIS
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_GLOBAL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_GLOBAL,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_LOI_TOUS_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_LOI,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_MIN_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_MIN,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_NATURE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_NAT_TXT,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_VOTE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_PROC_VOT,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_DELAI_LOI_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_DEL_MOY,
                PanConstants.SubTab.SUBTAB_DEL_MOY_PAR_LOI,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_DELAI_MIN_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_DEL_MOY,
                PanConstants.SubTab.SUBTAB_DEL_MOY_PAR_MIN,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_DELAI_NATURE_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_DEL_MOY,
                PanConstants.SubTab.SUBTAB_DEL_MOY_PAR_NAT_TXT,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_COURBE_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_LOIS,
                PanConstants.Tab.TAB_STATISTIQUES_PUBLICATION,
                null,
                null
            )
        )
        // ----------------------
        // ORDONNANCES
        // ----------------------
        .put(
            ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TAB_M_MESURES_APP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TAB_M_MESURES_APP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TAB_M_BS,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_ORDO,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TAB_MIN_MESURES_APP_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TAB_MIN_MESURES_APP_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TAB_MIN_TX_EXE_ORDONNANCES,
                PanConstants.SourceMapping.TAUX_EXECUTION_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_BS_PAR_MIN,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_TAUX_APPLICATION_MIN_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_ACTIVE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DEC,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_ACTIVE_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DEC,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_MESURES_DATE_PREV_DEPASSEE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DATE_DEP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_DEPASSE_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DATE_DEP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_DIFFEREE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_TAB_BORD_MESURES_DIFF,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_TX_EXEC.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_INDICATEURS,
                PanConstants.SubTab.SUBTAB_IND_TX_EXE_LOIS,
                PanConstants.SourceMapping.TAUX_EXECUTION_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_TB_APP_ORDO_DELAI_APPLI.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_INDICATEURS,
                PanConstants.SubTab.SUBTAB_IND_DELAI_MISE_APP_ORDONNANCES,
                PanConstants.SourceMapping.INDICATEURS_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_INDICATEURS,
                PanConstants.SubTab.SUBTAB_IND_TX_EXE_SESSION_ORDONNANCES,
                PanConstants.SourceMapping.INDICATEURS_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS_ORDONNANCE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_BS,
                PanConstants.SubTab.SUBTAB_BS_PAR_MIN,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_BS,
                PanConstants.SubTab.SUBTAB_BS_PAR_ORDO,
                PanConstants.SourceMapping.BILAN_SEMESTRIEL_ORDONNANCES
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_GLOBAL_APP_ORDONNANCE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_GLOBAL,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_ORDO,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_FIL_EAU_MIN_TOUS_APP_ORDONNANCE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_TX_APP_FIL_EAU,
                PanConstants.SubTab.SUBTAB_TX_APP_FIL_EAU_PAR_MIN,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_DELAI_MIN_TOUS_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_DEL_MOY,
                PanConstants.SubTab.SUBTAB_DEL_MOY_PAR_ORDO,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_DELAI_ORDO_TOUS_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_DEL_MOY,
                PanConstants.SubTab.SUBTAB_DEL_MOY_PAR_MIN,
                PanConstants.SourceMapping.TAUX_APPLICATION_FIL_EAU
            )
        )
        .put(
            ANReportEnum.TAB_AN_COURBE_TOUS_APP_ORDO.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_ORDONNANCES,
                PanConstants.Tab.TAB_STATISTIQUES_PUBLICATION,
                null,
                null
            )
        )
        // ----------------------
        // DIRECTIVES
        // ----------------------
        .put(
            ANReportEnum.TAB_AN_TRANSPOSITION_ENCOURS_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TRANSP_CHARGE_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TRANSPOSITION_ACHEVEE_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TRANSP_ACH_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_TRANSPOSITION_MIN_ACHEVEE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_MESURES_TRANSPO_ACH,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_TRANSPOSITION_ACHEVEE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_MESURES_TRANSPO_ACH,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_TRANSPOSITION_MIN_ENCOURS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_MESURES_TRANSPO_ENC,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_TRANSPOSITION_ENCOURS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_MESURES_TRANSPO_ENC,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_INDICATEUR_MIN_TRANS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_STATISTIQUES,
                PanConstants.SubTab.SUBTAB_IND_TRANSPO,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_RETARD_MOY_TRANS.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_STATISTIQUES,
                PanConstants.SubTab.SUBTAB_RET_MOY_TRANSPO,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_REPARTITION_DIR_APRENDRE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_DIRECTIVES,
                PanConstants.Tab.TAB_STATISTIQUES,
                PanConstants.SubTab.SUBTAB_REP_TXT_PRENDRE,
                null
            )
        )
        // ----------------------
        // SUIVI HABILITATIONS
        // ----------------------
        .put(
            ANReportEnum.TAB_AN_HABILITATION_FLTR_MIN_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_HABILITATIONS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_HABILITATIONS_EN_COURS_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_HABILITATION_FLTR_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_HABILITATIONS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_HABILITATIONS_EN_COURS_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_HABILITATION_SS_FLTR_MIN_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_HABILITATIONS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_HABILITATIONS_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_HABILITATION_SS_FLTR_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_HABILITATIONS,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_HABILITATIONS_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_HABILITATIONS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_GLOBAL,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_HABILITATIONS,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_GLOBAL,
                null
            )
        )
        // ----------------------
        // RATIFICATION ORDONNANCES
        // ----------------------
        .put(
            ANReportEnum.TAB_AN_RATIFICATION_38C_MIN_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_SUIVI_38C_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_RATIFICATION_38C_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_SUIVI_38C_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_RATIFICATION_741_MIN_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_SUIVI_741_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_RATIFICATION_741_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_SUIVI_741_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_ORDO_ARTICLE_38C,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_38C_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_GLOBAL_38C,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_38C.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_GLOBAL_38C,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_741_ALL.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_GLOBAL_741,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_741.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_GLOBAL_741,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_DECRET.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_RATIFICATION,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_SUIVI_DEC_APP_ORDO,
                null
            )
        )
        // ----------------------
        // TRAITES ET ACCORDS
        // ----------------------
        .put(
            ANReportEnum.TAB_AN_TRAITE_PUB_AVENIR_MIN.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_TRAITES,
                PanConstants.Tab.TAB_TAB_MAITRE_MIN,
                PanConstants.SubTab.SUBTAB_TRT_PUBLICATION_INTERVENIR_MIN,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_TRAITE_PUB_INTERVENUE.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_TRAITES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_PUBLICATION_INTERVENUE,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_TABLEAU_BORD_TRAITE_PUB_INTERVENIR.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_TRAITES,
                PanConstants.Tab.TAB_TAB_BORD,
                PanConstants.SubTab.SUBTAB_PUBLICATION_INTERVENIR,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_TRAITE_ETUDES_IMPACT.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_TRAITES,
                PanConstants.Tab.TAB_STATISTIQUES,
                PanConstants.SubTab.SUBTAB_IND_MIN_CONF_ETU_IMP,
                null
            )
        )
        .put(
            ANReportEnum.TAB_AN_STAT_TRAITE_PUBLIER.getRptdesignId(),
            new PanBirtStats(
                PanConstants.Section.SECTION_TRAITES,
                PanConstants.Tab.TAB_STATISTIQUES,
                PanConstants.SubTab.SUBTAB_REP_ACC_SIGN,
                null
            )
        )
        .build();

    public PanBirtStats(
        PanConstants.Section section,
        PanConstants.Tab tab,
        PanConstants.SubTab subtab,
        PanConstants.SourceMapping sourceMapping
    ) {
        super();
        this.section = section;
        this.tab = tab;
        this.subtab = subtab;
        this.sourceMapping = sourceMapping;
    }

    public static Map<String, Set<String>> getAllBySource() {
        Map<String, Set<String>> res = new HashMap<>();

        PAN_BIRT_TREE_RELATION.forEach(
            (type, rep) -> {
                if (rep.sourceMapping != null) {
                    String val =
                        ResourceHelper.getString(rep.getTab().getLabel()) +
                        " / " +
                        ResourceHelper.getString(rep.getSubtab().getLabel());
                    if (res.containsKey(rep.sourceMapping.getTag())) {
                        res.get(rep.sourceMapping.getTag()).add(val);
                    } else {
                        res.put(rep.sourceMapping.getTag(), new HashSet<>(Collections.singletonList(val)));
                    }
                }
            }
        );
        return res;
    }

    public PanConstants.Section getSection() {
        return section;
    }

    public void setSection(PanConstants.Section section) {
        this.section = section;
    }

    public PanConstants.Tab getTab() {
        return tab;
    }

    public void setTab(PanConstants.Tab tab) {
        this.tab = tab;
    }

    public PanConstants.SubTab getSubtab() {
        return subtab;
    }

    public void setSubtab(PanConstants.SubTab subtab) {
        this.subtab = subtab;
    }
}
