package fr.dila.solonepg.ui.constants.pan;

import java.util.stream.Stream;

public class PanConstants {

    // paramètres BIRT
    public enum BirtParam {
        DEBUT_INTERVALLE1_PARAM,
        FIN_INTERVALLE1_PARAM,
        DEBUT_INTERVALLE2_PARAM,
        FIN_INTERVALLE2_PARAM,
        MINISTEREPILOTE_PARAM,
        MINISTEREPILOTELABEL_PARAM,
        PERIODE_PARAM,
        MOIS_PARAM,
        ANNEE_PARAM,
        FIGER_PARAM,
        DOSSIERID_PARAM,
        DEBUTLEGISLATURE_PARAM,
        LEGISLATURES,
        LEGISLATURES_LABEL,
        MINISTERES_PARAM,
        DIRECTIONS_PARAM,
        DERNIERE_SESSION_PARAM;

        public static BirtParam findByName(String key) {
            for (BirtParam p : values()) {
                if (p.name().equals(key)) {
                    return p;
                }
            }
            return null;
        }
    }

    // Sources des données (export global)
    public enum SourceMapping {
        BILAN_SEMESTRIEL_LOIS("BSLois"),
        BILAN_SEMESTRIEL_ORDONNANCES("BSOrdonnances"),
        TAUX_EXECUTION_LOIS("TXLois"),
        TAUX_EXECUTION_ORDONNANCES("TXOrdonnances"),
        TAUX_APPLICATION_FIL_EAU("APPGlobal"),
        INDICATEURS_LOIS("TXDLois"),
        INDICATEURS_ORDONNANCES("TXDOrdonnances");

        private final String tag;

        SourceMapping(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    // nom des sections, onglets, sous-onglets
    public enum Section {
        SECTION_LOIS("lois"),
        SECTION_ORDONNANCES("ordonnances"),
        SECTION_HABILITATIONS("habilitations"),
        SECTION_DIRECTIVES("directives"),
        SECTION_RATIFICATION("ratification"),
        SECTION_TRAITES("traites");

        public final String key;

        Section(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public static Section findByKey(String key) {
            return Stream.of(values()).filter(section -> section.getKey().equals(key)).findFirst().orElse(null);
        }
    }

    public enum Tab {
        TAB_TAB_MAITRE("tableau-maitre"),
        TAB_TXT_MAITRE("texte-maitre"),
        TAB_FICHE_SIGN("fiche-signaletique"),
        TAB_TAB_PROG("tableau-programmation"),
        TAB_TAB_SUIVI("tableau-suivi"),
        TAB_BS("bilan-semestriel", "pan.onglet.bilanSemestriel"),
        TAB_INDICATEURS_LOLF("indicateurs-lolf", "pan.onglet.indicateursLOLF"),
        TAB_INDICATEURS("indicateurs-lolf", "pan.onglet.indicateurs"),
        TAB_TX_APP_FIL_EAU("taux-application", "pan.onglet.tauxApplication"),
        TAB_TAB_BORD("tableaux-bord"),
        TAB_DEL_MOY("delais-moyens", "pan.onglet.delaisMoyens"),
        TAB_STATISTIQUES("statistiques"),
        TAB_STATISTIQUES_PUBLICATION("statistiques-publication"),
        TAB_TAB_MAITRE_MIN("tableau-maitre-ministeres", "pan.onglet.tableauMaitre.ministeres");

        private final String key;
        private String label;

        Tab(String key) {
            this.key = key;
        }

        Tab(String key, String label) {
            this.key = key;
            this.label = label;
        }

        public String getKey() {
            return key;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum SubTab {
        SUBTAB_TAB_M_MESURES_APP("mesures-application"),
        SUBTAB_TAB_M_BS("bilan-semestriel"),
        SUBTAB_TAB_M_PROG("tableau-programmation"),
        SUBTAB_TAB_MIN_MESURES_APP_MIN("mesures-application-ministeres"),
        SUBTAB_TAB_MIN_TX_EXE_LOIS("taux-execution", "pan.sous.onglet.applicationLois.ministeres.tauxExecution"),
        SUBTAB_TAB_MIN_TX_EXE_ORDONNANCES(
            "taux-execution",
            "pan.sous.onglet.applicationOrdonnances.ministeres.tauxExecution"
        ),
        SUBTAB_TAB_MIN_BS(
            "bilan-semestriel-ministeres",
            "pan.sous.onglet.applicationLois.ministeres.bilanSemestriel.parMinistere"
        ),
        SUBTAB_TAB_MIN_TX_APP("taux-application-ministeres"),
        SUBTAB_TAB_BORD_MESURES_DEC("mesures-decret"),
        SUBTAB_TAB_BORD_MESURES_DATE_DEP("mesures-date-depassee"),
        SUBTAB_TAB_BORD_MESURES_DIFF("mesures-differee"),
        SUBTAB_IND_DELAI_MISE_APP_LOIS(
            "delai-mise-application",
            "pan.sous.onglet.applicationLois.indicateursLOLF.delaiMiseApplication"
        ),
        SUBTAB_IND_DELAI_MISE_APP_ORDONNANCES(
            "delai-mise-application",
            "pan.sous.onglet.applicationOrdonnances.indicateursLOLF.delaiMiseApplication"
        ),
        SUBTAB_IND_TX_EXE_LOIS(
            "indicateurs-taux-execution",
            "pan.sous.onglet.applicationLois.indicateursLOLF.tauxExecution"
        ),
        SUBTAB_IND_TX_EXE_ORDONNANCES(
            "indicateurs-taux-execution",
            "pan.sous.onglet.applicationOrdonnances.indicateursLOLF.tauxExecution"
        ),
        SUBTAB_IND_TX_EXE_SESSION_LOIS(
            "taux-execution-derniere-session",
            "pan.sous.onglet.applicationLois.indicateursLOLF.tauxExecution.derniereSession"
        ),
        SUBTAB_IND_TX_EXE_SESSION_ORDONNANCES(
            "taux-execution-derniere-session",
            "pan.sous.onglet.applicationOrdonnances.indicateursLOLF.tauxExecution.derniereSession"
        ),
        SUBTAB_BS_PAR_LOI("bilan-semestriel-par-loi", "pan.sous.onglet.applicationLois.bilanSemestriel.parLoi"),
        SUBTAB_BS_PAR_ORDO(
            "bilan-semestriel-par-ordonnance",
            "pan.sous.onglet.applicationOrdonnances.bilanSemestriel.parOrdonnance"
        ),
        SUBTAB_BS_PAR_MIN(
            "bilan-semestriel-par-ministere",
            "pan.sous.onglet.applicationLois.bilanSemestriel.parMinistere"
        ),
        SUBTAB_BS_PAR_NAT_TXT(
            "bilan-semestriel-par-nature-de-texte",
            "pan.sous.onglet.applicationLois.bilanSemestriel.parNatureDeTexte"
        ),
        SUBTAB_BS_PAR_PROC_VOT(
            "bilan-semestriel-par-procedure-de-vote",
            "pan.sous.onglet.applicationLois.bilanSemestriel.parProcedureDeVote"
        ),
        SUBTAB_TX_APP_FIL_EAU_GLOBAL(
            "taux-application-global",
            "pan.sous.onglet.applicationLois.tauxApplication.global"
        ),
        SUBTAB_TX_APP_FIL_EAU_PAR_LOI(
            "taux-application-par-loi",
            "pan.sous.onglet.applicationLois.tauxApplication.parLoi"
        ),
        SUBTAB_TX_APP_FIL_EAU_PAR_MIN(
            "taux-application-par-ministere",
            "pan.sous.onglet.applicationLois.tauxApplication.parMinistere"
        ),
        SUBTAB_TX_APP_FIL_EAU_PAR_ORDO(
            "taux-application-par-ordonnance",
            "pan.sous.onglet.applicationOrdonnances.tauxApplication.parOrdonnance"
        ),
        SUBTAB_TX_APP_FIL_EAU_PAR_NAT_TXT(
            "taux-application-par-nature-de-texte",
            "pan.sous.onglet.applicationLois.tauxApplication.parNatureDeTexte"
        ),
        SUBTAB_TX_APP_FIL_EAU_PAR_PROC_VOT(
            "taux-application-par-procedure-de-vote",
            "pan.sous.onglet.applicationLois.tauxApplication.parProcedureDeVote"
        ),
        SUBTAB_DEL_MOY_PAR_LOI("delais-moyens-par-loi", "pan.sous.onglet.applicationLois.delaisMoyens.parLoi"),
        SUBTAB_DEL_MOY_PAR_MIN(
            "delais-moyens-par-ministere",
            "pan.sous.onglet.applicationLois.delaisMoyens.parMinistere"
        ),
        SUBTAB_DEL_MOY_PAR_ORDO(
            "delais-moyens-par-ordonnance",
            "pan.sous.onglet.applicationOrdonnances.delaisMoyens.parOrdonnance"
        ),
        SUBTAB_DEL_MOY_PAR_NAT_TXT(
            "delais-moyens-par-nature-de-texte",
            "pan.sous.onglet.applicationLois.delaisMoyens.parNatureDeTexte"
        ),

        SUBTAB_TRANSP_CHARGE_MIN("transposition-a-charge"),
        SUBTAB_TRANSP_ACH_MIN("transposition-achevee"),
        SUBTAB_HABILITATIONS_EN_COURS_MIN("habilitations-en-cours"),
        SUBTAB_HABILITATIONS_MIN("habilitations"),
        SUBTAB_TRT_PUBLICATION_INTERVENIR_MIN("traites-accords-publication-a-intervenir"),
        SUBTAB_MESURES_TRANSPO_ACH("mesures-transposition-achevee"),
        SUBTAB_MESURES_TRANSPO_ENC("mesures-transposition-en-cours"),
        SUBTAB_SUIVI_GLOBAL("suivi-global"),
        SUBTAB_SUIVI_38C_MIN("ratification-ordonnances-38-c"),
        SUBTAB_SUIVI_741_MIN("ratification-ordonnances-74-1"),
        SUBTAB_SUIVI_GLOBAL_38C("suivi-global-38-c"),
        SUBTAB_SUIVI_GLOBAL_741("suivi-global-74-1"),
        SUBTAB_ORDO_ARTICLE_38C("ordonnances-article-38-c"),
        SUBTAB_PUBLICATION_INTERVENUE("publication-intervenue"),
        SUBTAB_PUBLICATION_INTERVENIR("publication-a-intervenir"),
        SUBTAB_IND_TRANSPO("indicateurs-transposition"),
        SUBTAB_IND_MIN_CONF_ETU_IMP("indicateurs-ministeriels-confection-etudes-impact"),
        SUBTAB_RET_MOY_TRANSPO("retards-moyens-transposition"),
        SUBTAB_REP_TXT_PRENDRE("repartition-textes-a-prendre"),
        SUBTAB_REP_ACC_SIGN("repatition-accords-signes"),
        SUBTAB_SUIVI_DEC_APP_ORDO("suivi-decrets-application-ordonnances");

        private final String key;
        private String label;

        SubTab(String key) {
            this.key = key;
        }

        SubTab(String key, String label) {
            this.key = key;
            this.label = label;
        }

        public String getKey() {
            return key;
        }

        public String getLabel() {
            return label;
        }
    }
}
