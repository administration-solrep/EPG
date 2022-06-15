package fr.dila.solonepg.api.activitenormative;

import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.st.api.constant.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * Enum qui represente tous les rapports des stats pour l'activite
 * normative
 */
public enum ANReportEnum {
    // ************************
    // * APPLICATION DES LOIS *
    // ************************
    TAB_AN_TABLEAU_BORD_ACTIVE(
        new Builder("pan_stat01", "mesuresApplicaticationActiveNonPubliees")
            .setLibelle("Mesures législatives appelant un décret et non appliquées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForLoi(true)
            .setDisplayLienPubliee(true)
    ),

    TAB_AN_TABLEAU_BORD_ACTIVE_ALL(
        new Builder("pan_stat02", "mesureApplicaticationActive")
            .setLibelle(Constants.MESURES_LEGISLATIVES_APPELANT_UN_DECRET)
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.MESURES_LEGISLATIVES_APPELANT_UN_DECRET,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_MESURES_APPLICATION_LOI(
        new Builder("pan_stat86", "mesuresApplicaticationParLoiNonPubliees")
            .setLibelle("Mesures d'application de la loi non appliquées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setForLoi(true)
            .setArchiAnReportInfoSuivi(
                "Mesures d'application par loi non appliquées",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_MESURES_RESTANT,
                SuiviConstants.SUIVI_ACC_DETAILS
            )
    ),

    TAB_AN_TABLEAU_BORD_DIFFEREE(
        new Builder("pan_stat14", "mesureApplicaticationDifferee")
            .setLibelle(Constants.MESURES_AVEC_ENTREE_EN_VIGUEUR_DIFFEREE)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.MESURES_AVEC_ENTREE_EN_VIGUEUR_DIFFEREE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_MESURES_DIFFEREES_PAR_LOI,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                3,
                SuiviConstants.SUIVI_MESURES_DIFFEREE_PAR_ELM
            )
    ),

    TAB_AN_MESURES_APPLICATION_LOI_ALL(
        new Builder("pan_stat87", "mesuresApplicaticationParLoi")
            .setLibelle("Mesures d'application de la loi")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForLoi(true)
            .setArchiAnReportInfoSuivi(
                "Mesures d'application par loi",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_MESURES_APPLICATION_LOI,
                SuiviConstants.SUIVI_ACC_DETAILS
            )
    ),

    TAB_AN_BILAN_SEMESTRIEL_LOI(
        new Builder("pan_stat03", "bilanSemestrielsParLoi")
            .setLibelle("Bilan semestriel de la loi")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForLoi(true)
            .setArchiAnReportInfo(
                "Bilan semestriel par loi",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false
            )
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
    ),

    TAB_AN_TAUX_APPLICATION_LOI(
        new Builder("pan_stat88", "tauxDapplicationAuFilDeLeauParLoiAffichageInitiale")
            .setLibelle("tauxDapplicationAuFilDeLeauParLoiAffichageInitiale")
            .setBatchTriggerable(true)
            .setForLoi(true)
    ),

    TAB_AN_TAUX_APPLICATION_LOI_ALL(
        new Builder("pan_stat04", "tauxDapplicationAuFilDeLeauParLoiAffichageAvencee")
            .setLibelle("Taux d'application au fil de l'eau de la loi")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForLoi(true)
            .setArchiAnReportInfoSuivi(
                "Taux d'application au fil de l'eau par loi",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_TAUX_APPLICATION_LOI,
                SuiviConstants.SUIVI_DETAILS
            )
    ),

    TAB_AN_MESURE_APPLICATION_MIN(
        new Builder("pan_stat06", "mesuresApplicaticationParMinistereNonPubliees")
            .setLibelle("Mesures d'application du ministère, non appliquées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                "Mesures d'application non appliquées par ministère",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                "Mesures en attente de décret",
                SuiviConstants.SUIVI_MINISTERES_2_LOIS
            )
    ),

    TAB_AN_MESURE_APPLICATION_MIN_ALL(
        new Builder("pan_stat05", "mesuresApplicaticationParMinistere")
            .setLibelle("Mesures d'application du ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Mesures d'application par ministère",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_DEBUT_LEGISLATURE_MIN(
        new Builder("pan_stat07", "taux_execution_lois_promulguees_debut_legislature_par_ministere")
            .setLibelle("Taux d'exécution des lois promulguées depuis le début de la législature du ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                "Taux d'exécution des lois promulguées",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                "Taux d'exécution des lois promulguées",
                SuiviConstants.SUIVI_MINISTERES_1_LOIS
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC)
    ),

    TAB_AN_BILAN_SEMESTRIEL_MIN(
        new Builder("pan_stat08", "bilanSemestrielsParMinistere")
            .setLibelle("Bilan semestriel du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                Constants.BILAN_SEMESTRIEL_PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
    ),

    TAB_AN_TAUX_APPLICATION_MIN(
        new Builder("pan_stat09", "tauxDapplicationAuFilDeLeauParMinistere")
            .setLibelle("Taux d'application au fil de l'eau du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_MESURES_APPELANT_DECRET_NON_PUB(
        new Builder("pan_stat10", "mesuresLegislativesAppelantDecret")
            .setLibelle(Constants.MESURES_LEGISLATIVES_APPELANT_UN_DECRET)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfoSuivi(
                SuiviConstants.SUIVI_LIBELLE_MESURES_ATTENTE_PAR_LOI,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_MESURES_ATTENTE_PAR_LOI,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                2,
                SuiviConstants.SUIVI_MESURES_ATTENTE_PAR_ELM
            )
    ),

    TAB_AN_MESURES_APPELANT_DECRET_TOUS(
        new Builder("pan_stat11", "mesuresLegislativesAppelantDecret")
            .setLibelle(Constants.MESURES_LEGISLATIVES_APPELANT_UN_DECRET)
            .setBatchTriggerable(true)
            .setDisplayLienPubliee(true)
    ),

    TAB_AN_TABLEAU_BORD_DEPASSE(
        new Builder("pan_stat12", "mesuresApplicaticationDepassesNonPubliees")
            .setLibelle(
                "Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée, non appliquées"
            )
            .setBatchTriggerable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfo(
                "Mesures d'application non appliquées pour lesquelles la date prévisionnelle de publication est dépassée",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_DEPASSE_ALL(
        new Builder("pan_stat13", "mesureApplicaticationDepasses")
            .setLibelle(Constants.MESURES_D_APPLICATION_POUR_LESQUELLES_LA_DATE)
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.MESURES_D_APPLICATION_POUR_LESQUELLES_LA_DATE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_STAT_DEBUT_LEGISLATURE(
        new Builder("pan_stat15", "taux_execution_lois_promulguees_debut_legislature")
            .setLibelle("Taux d'exécution des lois promulguées depuis le début de la législature")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfo(
                "Taux d'exécution des lois",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_INDICATEUR_LOLF,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC)
    ),

    TAB_AN_STAT_MISE_APPLICATION(
        new Builder("pan_stat16", "delais_mise_application")
            .setLibelle(Constants.DELAI_DE_MISE_EN_APPLICATION_DES_LOIS)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.DELAI_DE_MISE_EN_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_INDICATEUR_LOLF,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_DELAI_MISE_APP_PAR_LOI,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                1,
                SuiviConstants.SUIVI_DELAI_MISE_APPLICATION_PAR_ELM
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC_SESSION_PARLEMENTAIRE)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC)
    ),

    TAB_AN_STAT_DERNIERE_SESSION(
        new Builder("pan_stat17", "taux_execution_lois_promulguees_derniere_session_parlementaire")
            .setLibelle("Taux d'exécution des lois promulguées au cours de la dernière session parlementaire")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Taux d'exécution des lois promulguées au cours de la dernière session parlementaire",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_INDICATEUR_LOLF,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC_SESSION_PARLEMENTAIRE)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_TAUX_EXEC_SESSION_PARLEMENTAIRE)
    ),

    TAB_AN_BILAN_SEM_LOI_TOUS(
        new Builder("pan_stat18", "bilan_semestriels_loi_tous")
            .setLibelle("Bilan semestriel par loi")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_LOI,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_LOI,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                0,
                SuiviConstants.SUIVI_BILAN_SEMESTRIEL_PAR_ELM
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
    ),

    TAB_AN_BILAN_SEM_MIN_TOUS(
        new Builder("pan_stat19", "bilan_semestriels_ministere_tous")
            .setLibelle(Constants.BILAN_SEMESTRIEL_PAR_MINISTERE)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_MINISTERE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                2,
                SuiviConstants.SUIVI_BILAN_SEMESTRIEL_PAR_MINISTERE
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
    ),

    TAB_AN_BILAN_SEM_NATURE(
        new Builder("pan_stat20", "bilan_semestriels_par_nature_texte")
            .setLibelle("Bilan semestriel par nature de texte")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.PAR_NATURE_DE_TEXTE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setIsGraphique(true)
    ),

    TAB_AN_BILAN_SEM_VOTE(
        new Builder("pan_stat21", "bilan_semestriels_par_procedure_vote")
            .setLibelle("Bilan semestriel par procédure de vote")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Par procédure de vote",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_LOIS_BILANS_SEMESTRIELS)
            .setIsGraphique(true)
    ),

    TAB_AN_FIL_EAU_GLOBAL(
        new Builder("pan_stat22", "taux_dapplication_fil_eau_taux_global")
            .setLibelle("Taux d'application au fil de l'eau global")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                "Global",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_TX_APP_GLOBAL,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                0,
                SuiviConstants.SUIVI_TAUX_APPLICATION_GLOBAL
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
            .setIsGraphique(true)
    ),

    TAB_AN_FIL_EAU_LOI_TOUS(
        new Builder("pan_stat89", "taux_dapplication_fil_eau_taux_loi_tous")
            .setLibelle("taux_dapplication_fil_eau_taux_loi_tous")
            .setBatchTriggerable(true)
    ),

    TAB_AN_FIL_EAU_LOI_TOUS_ALL(
        new Builder("pan_stat23", "taux_dapplication_fil_eau_taux_loi_tous_all")
            .setLibelle("Taux d'application au fil de l'eau par loi")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.PAR_LOI,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),
    TAB_AN_FIL_EAU_MIN_TOUS(
        new Builder("pan_stat24", "taux_dapplication_fil_eau_taux_ministere_tous")
            .setLibelle(Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_MINISTERE)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_TX_APP_PAR_MINISTERE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                1,
                SuiviConstants.SUIVI_TAUX_APPLICATION_PAR_MINISTERE
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_FIL_EAU_NATURE(
        new Builder("pan_stat25", "taux_dapplication_fil_eau_taux_nature_texte")
            .setLibelle("Taux d'application au fil de l'eau par nature de texte")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.PAR_NATURE_DE_TEXTE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
            .setIsGraphique(true)
    ),

    TAB_AN_FIL_EAU_VOTE(
        new Builder("pan_stat26", "taux_dapplication_fil_eau_taux_procedure_vote")
            .setLibelle("Taux d'application au fil de l'eau par procédure de vote")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Par procédure de vote",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
            .setIsGraphique(true)
    ),

    TAB_AN_DELAI_LOI_TOUS(
        new Builder("pan_stat27", "delai_loi_tous")
            .setLibelle("Délais moyens par loi")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_LOI,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_DELAI_PUBLICATION_LOI,
                SuiviConstants.SUIVI_ACC_DETAILS
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_DELAI_MIN_TOUS(
        new Builder("pan_stat28", "delai_ministere_tous")
            .setLibelle("Délais moyens par ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_LOIS,
                SuiviConstants.SUIVI_LIBELLE_DELAI_PAR_MINISTERE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                3,
                SuiviConstants.SUIVI_DELAI_MISE_APPLICATION_PAR_MINISTERE
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_DELAI_NATURE_TOUS(
        new Builder("pan_stat29", "delai_par_nature_texte")
            .setLibelle("Délais moyens par nature de texte")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.PAR_NATURE_DE_TEXTE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_COURBE_TOUS(
        new Builder("pan_stat30", "courbe_publication")
            .setLibelle("Statistiques sur la publication des décrets d'application")
            .setBatchTriggerable(true)
            .setReportCourbe(true)
            .setArchiAnReportInfo(
                Constants.STATISTIQUE_SUR_LA_PUBLICATION,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,
                Constants.STATISTIQUE_SUR_LA_PUBLICATION,
                false,
                false
            )
            .setIsGraphique(true)
    ),

    // *******************************
    // * APPLICATION DES ORDONNANCES *
    // *******************************
    TAB_AN_MESURES_APP_ORDONNANCES(
        new Builder("pan_stat31", "mesuresApplicaticationParOrdoAppOrdoNonPubliees")
            .setLibelle("Mesures d'application de l'ordonnance non appliquées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setForTexte(true)
            .setArchiAnReportInfoSuivi(
                "Mesures d'application par ordonnance non appliquées",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_MESURES_RESTANT,
                SuiviConstants.SUIVI_ACC_DETAILS
            )
    ),

    TAB_AN_MESURES_APP_ORDONNANCES_ALL(
        new Builder("pan_stat32", "mesuresApplicaticationParOrdoAppOrdo")
            .setLibelle("Mesures d'application de l'ordonnance")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForTexte(true)
            .setArchiAnReportInfoSuivi(
                "Mesures d'application par ordonnance",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_MESURES_APPLICATION_ORDONNANCE,
                SuiviConstants.SUIVI_ACC_DETAILS
            )
    ),

    TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES(
        new Builder("pan_stat33", "bilanSemestrielsParOrdonnance")
            .setLibelle("Bilan semestriel de l'ordonnance")
            .setBatchTriggerable(true)
            .setForTexte(true)
            .setArchiAnReportInfo(
                "Bilan semestriel par ordonnance",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
    ),

    TAB_AN_TAUX_APPLICATION_ORDO(
        new Builder("pan_stat90", "tauxDapplicationAuFilDeLeauParOrdonnanceAffichageInitiale")
            .setLibelle("tauxDapplicationAuFilDeLeauParOrdonnanceAffichageInitiale")
            .setBatchTriggerable(true)
            .setForTexte(true)
    ),

    TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL(
        new Builder("pan_stat34", "tauxDapplicationAuFilDeLeauParOrdonnanceAffichageAvencee")
            .setLibelle("Taux d'application au fil de l'eau de l'ordonnance")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForTexte(true)
            .setArchiAnReportInfoSuivi(
                Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_ORDONNANCE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                false,
                false,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_TAUX_APPLICATION_ORDONNANCE,
                SuiviConstants.SUIVI_DETAILS
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO(
        new Builder("pan_stat35", "mesuresApplicaticationParMinistereNonPublieesAppOrdo")
            .setLibelle("Mesures d'application du ministère, non appliquées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                "Mesures d'application non appliquées par ministère",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                "Mesures d'application non appliquées par ministère",
                SuiviConstants.SUIVI_MINISTERES_2_ORDONNANCES
            )
    ),

    TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO_ALL(
        new Builder("pan_stat36", "mesuresApplicaticationParMinistereAppOrdo")
            .setLibelle("Mesures d'application du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Mesures d'application par ministère",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO(
        new Builder("pan_stat37", "taux_execution_lois_promulguees_debut_legislature_par_ministere_app_ordo")
            .setLibelle("Taux d'exécution des ordonnances depuis le début de la législature du ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                "Taux d'exécution des ordonnances publiées",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                "Taux d'exécution des ordonnances publiées",
                SuiviConstants.SUIVI_MINISTERES_1_ORDONNANCES
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC)
    ),

    TAB_AN_BILAN_SEMESTRIEL_MIN_APP_ORDO(
        new Builder("pan_stat38", "bilanSemestrielsParMinistereAppOrdo")
            .setLibelle("Bilan semestriel du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                Constants.BILAN_SEMESTRIEL_PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
    ),

    TAB_AN_TAUX_APPLICATION_MIN_APP_ORDO(
        new Builder("pan_stat39", "tauxDapplicationAuFilDeLeauParMinistereAppOrdo")
            .setLibelle("Taux d'application au fil de l'eau du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_FIL_EAU_GLOBAL_APP_ORDONNANCE(
        new Builder("pan_stat50", "taux_dapplication_fil_eau_taux_global_app_ordonnance")
            .setLibelle("Taux d'application au fil de l'eau global")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                "Global",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_TX_APP_GLOBAL,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                0,
                SuiviConstants.SUIVI_TAUX_APPLICATION_GLOBAL
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
            .setIsGraphique(true)
    ),

    TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE(
        new Builder("pan_stat91", "taux_dapplication_fil_eau_taux_loi_tous_app_ordonnance")
            .setLibelle(Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_ORDONNANCE)
            .setBatchTriggerable(true)
            .setForTexte(true)
            .setArchiAnReportInfo(
                Constants.PAR_ORDONNANCE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE_ALL(
        new Builder("pan_stat51", "taux_dapplication_fil_eau_taux_loi_tous_app_ordonnance_all")
            .setLibelle(Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_ORDONNANCE)
            .setBatchTriggerable(true)
            .setForTexte(true)
    ),

    TAB_AN_FIL_EAU_MIN_TOUS_APP_ORDONNANCE(
        new Builder("pan_stat52", "taux_dapplication_fil_eau_taux_ministere_tous_app_ordo")
            .setLibelle(Constants.TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_MINISTERE)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_TX_APP_PAR_MINISTERE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                2,
                SuiviConstants.SUIVI_TAUX_APPLICATION_PAR_MINISTERE
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_DELAI_ORDO_TOUS_APP_ORDO(
        new Builder("pan_stat54", "delai_ordo_tous_app_ordo")
            .setLibelle("Délais moyens par ordonnance")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_ORDONNANCE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_DELAI_PUBLICATION_ORDONNANCE,
                SuiviConstants.SUIVI_ACC_DETAILS
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_DELAI_MIN_TOUS_APP_ORDO(
        new Builder("pan_stat53", "delai_ministere_tous_app_ordo")
            .setLibelle("Délais moyens par ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_DELAI_PAR_MINISTERE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                2,
                SuiviConstants.SUIVI_DELAI_MISE_APPLICATION_PAR_MINISTERE
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.DATE_DEBUT_LEGIS)
    ),

    TAB_AN_COURBE_TOUS_APP_ORDO(
        new Builder("pan_stat55", "courbe_publication_app_ordo")
            .setLibelle("Statistiques sur la publication des décrets d'application")
            .setBatchTriggerable(true)
            .setReportCourbe(true)
            .setArchiAnReportInfo(
                Constants.STATISTIQUE_SUR_LA_PUBLICATION,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                Constants.STATISTIQUE_SUR_LA_PUBLICATION,
                false,
                false
            )
            .setIsGraphique(true)
    ),

    TAB_AN_TB_APP_ORDO_DEPASSE(
        new Builder("pan_stat92", "mesuresApplicationAppOrdoDepassesNonPubliees")
            .setLibelle(
                "Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée, non appliquées"
            )
            .setBatchTriggerable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfo(
                "Mesures d'application non appliquées pour lesquelles la date prévisionnelle de publication est dépassée",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TB_APP_ORDO_DEPASSE_ALL(
        new Builder("pan_stat43", "mesureApplicationAppOrdoDepasses")
            .setLibelle(Constants.MESURES_D_APPLICATION_POUR_LESQUELLES_LA_DATE)
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                Constants.MESURES_D_APPLICATION_POUR_LESQUELLES_LA_DATE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TB_APP_ORDO_ACTIVE(
        new Builder("pan_stat40", "mesuresApplicaticationActiveNonPublieesAppOrdo")
            .setLibelle("Mesures législatives appelant un décret et non appliquées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfoSuivi(
                "Mesures des ordonnances non appliquées appelant un décret",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_MESURES_ATTENTE_PAR_ORDONNANCE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                2,
                SuiviConstants.SUIVI_MESURES_ATTENTE_PAR_ELM
            )
    ),

    TAB_AN_TB_APP_ORDO_ACTIVE_ALL(
        new Builder("pan_stat41", "mesureApplicaticationAppOrdoActive")
            .setLibelle(Constants.MESURES_LEGISLATIVES_APPELANT_UN_DECRET)
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Mesures des ordonnances appelant un décret",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TB_APP_ORDO_MESURES_DATE_PREV_DEPASSEE(
        new Builder("pan_stat42", "mesuresApplicationDatePrevisionnelleDepassee")
            .setLibelle(Constants.MESURES_D_APPLICATION_POUR_LESQUELLES_LA_DATE)
            .setBatchTriggerable(true)
    ),

    TAB_AN_TB_APP_ORDO_DIFFEREE(
        new Builder("pan_stat44", "mesureApplicaticationDiffereeAppOrdo")
            .setLibelle(Constants.MESURES_AVEC_ENTREE_EN_VIGUEUR_DIFFEREE)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.MESURES_AVEC_ENTREE_EN_VIGUEUR_DIFFEREE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_MESURES_DIFFEREES_PAR_ORDONNANCE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                3,
                SuiviConstants.SUIVI_MESURES_DIFFEREE_PAR_ELM
            )
    ),

    TAB_AN_TB_APP_ORDO_TX_EXEC(
        new Builder("pan_stat45", "tauxExecutionOrdonnances").setLibelle("Taux d'éxécution des ordonnances")
    ),

    TAB_AN_TB_APP_ORDO_DELAI_APPLI(
        new Builder("pan_stat46", "delaiMiseApplicationOrdonnances")
            .setLibelle("Délai de mise en application des ordonnances")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                "Délai de mise en application des ordonnances",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_INDICATEURS,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_DELAI_MISE_APP_PAR_ORDONNANCE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                1,
                SuiviConstants.SUIVI_DELAI_MISE_APPLICATION_PAR_ELM
            )
    ),

    TAB_AN_BILAN_SEM_ORDONNANCE_TOUS(
        new Builder("pan_stat49", "bilan_semestriels_ordonnance_tous")
            .setLibelle("Bilan semestriel par ordonnance")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_ORDONNANCE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_ORDONNANCE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_AUTRES,
                0,
                SuiviConstants.SUIVI_BILAN_SEMESTRIEL_PAR_ELM
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
    ),

    TAB_AN_BILAN_SEM_MIN_TOUS_ORDONNANCE(
        new Builder("pan_stat48", "bilan_semestriels_ministere_tous_ordonnance")
            .setLibelle(Constants.BILAN_SEMESTRIEL_PAR_MINISTERE)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                Constants.PAR_MINISTERE,
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_MINISTERE,
                SuiviConstants.SUIVI_EXTRADATA_KEY_STATISTIQUES,
                2,
                SuiviConstants.SUIVI_BILAN_SEMESTRIEL_PAR_MINISTERE
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDO_BILANS_SEMESTRIELS)
    ),

    TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE(
        new Builder("pan_stat47", "taux_execution_ordonnances_promulguees_debut_legislature")
            .setLibelle("Taux d'exécution des ordonnances publiées depuis le début de la législature")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Taux d'exécution des ordonnances",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_INDICATEURS,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC)
    ),

    TAB_AN_INDIC_APP_ORDO_MISE_APPLI(
        new Builder("pan_stat16", "delais_mise_application_app_ordo")
            .setLibelle(Constants.DELAI_DE_MISE_EN_APPLICATION_DES_LOIS)
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC_SESSION_PARLEMENTAIRE)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC)
    ),

    TAB_AN_INDIC_APP_ORDO_DERNIERE_SESSION(
        new Builder("pan_stat17", "taux_execution_ordonnances_promulguees_derniere_session")
            .setLibelle("Taux d'exécution des ordonnances publiées au cours de la dernière session parlementaire")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Taux d'exécution des ordonnances publiées au cours de la dernière session parlementaire",
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_INDICATEURS,
                false,
                true
            )
            .setGenerateReportParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC_SESSION_PARLEMENTAIRE)
            .setPublishPanParamAnType(ParamAnTypeEnum.APPLI_ORDOS_TAUX_EXEC_SESSION_PARLEMENTAIRE)
    ),

    // ********************************
    // * TRANSPOSITION DES DIRECTIVES *
    // ********************************
    TAB_AN_TABLEAU_BORD_TRANSPOSITION_ACHEVEE(
        new Builder("pan_stat59", "tableau_bord_transposition_achevee")
            .setLibelle("Directives dont la transposition est achevée")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Directives dont la transposition est achevée",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_TRANSPOSITION_MIN_ACHEVEE(
        new Builder("pan_stat58", "tableau_bord_transposition_min_achevee")
            .setLibelle("tableau_bord_transposition_min_achevee")
            .setBatchTriggerable(true)
    ),

    TAB_AN_TABLEAU_BORD_TRANSPOSITION_ENCOURS(
        new Builder("pan_stat61", "tableau_bord_transposition_encours")
            .setLibelle("Directives dont la transposition reste en cours")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Directives dont la transposition reste en cours",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_TRANSPOSITION_MIN_ENCOURS(
        new Builder("pan_stat60", "tableau_bord_transposition_min_encours")
            .setLibelle("tableau_bord_transposition_min_encours")
            .setBatchTriggerable(true)
    ),

    TAB_AN_STAT_INDICATEUR_MIN_TRANS(
        new Builder("pan_stat62", "stat_indicateur_min_trans")
            .setLibelle("Indicateurs de transposition des directives")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Indicateurs de transposition",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
                false,
                true
            )
    ),

    TAB_AN_STAT_RETARD_MOY_TRANS(
        new Builder("pan_stat63", "stat_retard_moy_trans")
            .setLibelle("Tableau des retards moyens de transposition")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Retards moyens de transposition",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
                false,
                true
            )
    ),

    TAB_AN_STAT_REPARTITION_DIR_APRENDRE(
        new Builder("pan_stat64", "stat_repartition_dir_aprendre")
            .setLibelle("Répartition par ministère des textes de transposition restant à prendre")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Répartition des textes restant à prendre",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
                false,
                true
            )
            .setIsGraphique(true)
    ),

    TAB_AN_TRANSPOSITION_ACHEVEE_MIN(
        new Builder("pan_stat57", "transposition_achevee_min")
            .setLibelle("Directives dont la transposition est achevée par le ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Directives dont la transposition est achevée par ministère",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_TRANSPOSITION_ENCOURS_MIN(
        new Builder("pan_stat56", "transposition_encours_min")
            .setLibelle("Suivi des travaux de transposition à la charge du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Suivi des travaux de transposition  à la charge des ministères",
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    // ***************************
    // * SUIVI DES HABILITATIONS *
    // ***************************
    TAB_AN_HABILITATION_FLTR_MIN(
        new Builder("pan_stat66", "tab_an_habilitation_fltr_min")
            .setLibelle("Suivi des habilitations non utilisées, de la législature en cours, du ministère")
            .setBatchTriggerable(true)
            .setDisplayLienPubliee(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Suivi des habilitations de la législature en cours ",
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_HABILITATION_FLTR_MIN_ALL(
        new Builder("pan_stat65", "tab_an_habilitation_fltr_min_all")
            .setLibelle("Suivi des habilitations de la législature en cours, du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Suivi de toutes les habilitations de la législature en cours ",
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_HABILITATION_SS_FLTR_MIN(
        new Builder("pan_stat68", "tab_an_habilitation_ss_fltr_min")
            .setLibelle("Suivi des habilitations non utilisées du ministère")
            .setBatchTriggerable(true)
            .setDisplayLienPubliee(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Suivi par ministère des habilitations ",
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_HABILITATION_SS_FLTR_MIN_ALL(
        new Builder("pan_stat67", "tab_an_habilitation_ss_fltr_min_all")
            .setLibelle("Suivi des habilitations du ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                "Suivi par ministère de toutes les habilitations ",
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                "Suivi des habilitations du ministère",
                SuiviConstants.SUIVI_MINISTERES_0_SUIVI_ORDONNANCES
            )
    ),

    TAB_AN_TABLEAU_BORD_HABILITATION_FILTRE(
        new Builder("pan_stat93", "an_an_tableau_bord_habilitation_filtre")
            .setLibelle("an_an_tableau_bord_habilitation_filtre")
            .setBatchTriggerable(true)
            .setDisplayLienPubliee(true)
    ),

    TAB_AN_TABLEAU_BORD_HABILITATION_FILTRE_ALL(
        new Builder("pan_stat94", "an_tableau_bord_habilitation_filtre_all")
            .setLibelle("an_tableau_bord_habilitation_filtre_all")
            .setBatchTriggerable(true)
    ),

    TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE(
        new Builder("pan_stat70", "an_tableau_bord_habilitation_ss_filtre")
            .setLibelle("Suivi global des habilitations non utilisées")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfo(
                "Suivi global des habilitations ",
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_ALL(
        new Builder("pan_stat69", "an_tableau_bord_habilitation_ss_filtre_all")
            .setLibelle("Suivi global des habilitations")
            .setBatchTriggerable(true)
            .setArchiAnReportInfoSuivi(
                "Suivi global de toutes les habilitations ",
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_LISTE_HABILITATIONS,
                SuiviConstants.SUIVI_LISTE_HABILITATIONS
            )
    ),

    // ********************************
    // * RATIFICATION DES ORDONNANCES *
    // ********************************

    TAB_AN_RATIFICATION_741_MIN(
        new Builder("pan_stat74", "tab_an_ratification_741_min")
            .setLibelle("Suivi de la ratification des ordonnances de l'article 74-1, non aboutie, du ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                Constants.SUIVI_GLOBAL_DE_LA_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                "Suivi de la ratification des ordonnances de l'article 74-1, non aboutie, du ministère",
                SuiviConstants.SUIVI_MINISTERES_2_SUIVI_ORDONNANCES
            )
    ),

    TAB_AN_RATIFICATION_741_MIN_ALL(
        new Builder("pan_stat73", "tab_an_ratification_741_min_all")
            .setLibelle("Suivi de la ratification des ordonnances de l'article 74-1 du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Suivi global de toutes les ratifications des ordonnances de l'article 74-1",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_RATIFICATION_38C_MIN(
        new Builder("pan_stat72", "tab_an_ratification_38c_min")
            .setLibelle("Suivi de la ratification des ordonnances de l'article 38 C, non aboutie, du ministère")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setForMinistere(true)
            .setArchiAnReportInfoSuivi(
                "Suivi global de la ratification des ordonnances de l'article 38 C ",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                "Suivi de la ratification des ordonnances de l'article 38 C, non aboutie, du ministère",
                SuiviConstants.SUIVI_MINISTERES_1_SUIVI_ORDONNANCES
            )
    ),

    TAB_AN_RATIFICATION_38C_MIN_ALL(
        new Builder("pan_stat71", "tab_an_ratification_38c_min_all")
            .setLibelle("Suivi de la ratification des ordonnances de l'article 38 C du ministère")
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Suivi global de toutes les ratifications des ordonnances de l'article 38 C ",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE(
        new Builder("pan_stat75", "an_tableau_bord_ratification_pas_depose")
            .setLibelle("Ordonnances de l'article 38 C dont le projet de loi de ratification n'a pas été déposé")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setArchiAnReportInfoSuivi(
                "Ordonnances de l'article 38 C dont le projet de loi de ratification n'a pas été déposé",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_RATIFICATION_PAS_DEPOSES,
                SuiviConstants.SUIVI_EXTRADATA_KEY_RATIFICATIONS,
                0,
                SuiviConstants.SUIVI_SUIVI_ORDONNANCES_RATIFICATION_PAS_DEPOSES
            )
    ),

    TAB_AN_TABLEAU_BORD_RATIFICATION_38C(
        new Builder("pan_stat77", "an_tableau_bord_ratification_38c")
            .setLibelle("Suivi global de la ratification des ordonnances de l'article 38 C, non aboutie")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfoSuivi(
                "Suivi global de la ratification des ordonnances de l'article 38 C",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_RATIFICATION_38C,
                SuiviConstants.SUIVI_EXTRADATA_KEY_RATIFICATIONS,
                1,
                SuiviConstants.SUIVI_SUIVI_ORDONNANCES_RATIFICATION_38C
            )
    ),

    TAB_AN_TABLEAU_BORD_RATIFICATION_38C_ALL(
        new Builder("pan_stat76", "an_tableau_bord_ratification_38c_all")
            .setLibelle("Suivi global de la ratification des ordonnances de l'article 38 C")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Suivi global de toutes les ratifications des ordonnances de l'article 38 C",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_RATIFICATION_741(
        new Builder("pan_stat79", "an_tableau_bord_ratification_741")
            .setLibelle("Suivi global de la ratification des ordonnances de l'article 74-1, non aboutie")
            .setBatchTriggerable(true)
            .setBatchPubliable(true)
            .setDisplayLienPubliee(true)
            .setArchiAnReportInfoSuivi(
                Constants.SUIVI_GLOBAL_DE_LA_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true,
                SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES,
                SuiviConstants.SUIVI_LIBELLE_RATIFICATION_741,
                SuiviConstants.SUIVI_EXTRADATA_KEY_RATIFICATIONS,
                2,
                SuiviConstants.SUIVI_SUIVI_ORDONNANCES_RATIFICATION_741
            )
    ),

    TAB_AN_TABLEAU_BORD_RATIFICATION_741_ALL(
        new Builder("pan_stat78", "an_tableau_bord_ratification_741_all")
            .setLibelle(Constants.SUIVI_GLOBAL_DE_LA_RATIFICATION_DES_ORDONNANCES)
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Suivi global de toutes les ratifications des ordonnances de l'article 74-1",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    TAB_AN_TABLEAU_BORD_RATIFICATION_DECRET(
        new Builder("pan_stat80", "an_tableau_bord_ratification_decret")
            .setLibelle("Suivi des décrets pris en application des ordonnances")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Suivi des décrets pris en application des ordonnances",
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                true
            )
    ),

    // **********************
    // * TRAITES ET ACCORDS *
    // **********************

    TAB_AN_TRAITE_PUB_AVENIR_MIN(
        new Builder("pan_stat81", "an_traite_pub_avenir_min")
            .setLibelle(
                "Tableaux ministériels des traités et accord dont la publication reste à intervenir du ministère"
            )
            .setBatchTriggerable(true)
            .setForMinistere(true)
            .setArchiAnReportInfo(
                "Tableaux ministériels des traités et accords dont la publication reste à intervenir",
                ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
                true,
                false
            )
    ),

    TAB_AN_TABLEAU_BORD_TRAITE_PUB_INTERVENUE(
        new Builder("pan_stat82", "an_tableau_bord_traite_pub_intervenue")
            .setLibelle("Tableau général des traités et accords dont la publication est intervenue")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Tableau général des traités et accords dont la publication est intervenue",
                ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,
                ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
                false,
                false
            )
    ),

    TAB_AN_TABLEAU_BORD_TRAITE_PUB_INTERVENIR(
        new Builder("pan_stat83", "an_tableau_bord_traite_pub_intervenir")
            .setLibelle("Tableau général des traités et accords dont la publication reste à intervenir")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Tableau général des traités et accords dont la publication reste à intervenir",
                ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,
                ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
                false,
                false
            )
    ),

    TAB_AN_STAT_TRAITE_ETUDES_IMPACT(
        new Builder("pan_stat84", "an_stat_traite_etudes_impact")
            .setLibelle("Indicateurs ministériels de confection des études d'impact")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Indicateurs ministériels de confection des études d'impact",
                ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,
                ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
                false,
                false
            )
    ),

    TAB_AN_STAT_TRAITE_PUBLIER(
        new Builder("pan_stat85", "an_stat_traite_publier")
            .setLibelle("Répartition par ministères des traités et accords signés et restant à publier")
            .setBatchTriggerable(true)
            .setArchiAnReportInfo(
                "Répartition par ministères des traités et accords signés et restant à publier",
                ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,
                ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
                false,
                false
            )
    ),

    // **************
    // * EXPORT CSV *
    // **************

    CSV_APPLICATION_LOI_TEXTES_MAITRES(new Builder("pan_stat95", "textMaitre")),
    CSV_APPLICATION_LOI_TABLEAU_LOIS(new Builder("pan_stat96", "TableauDeLoi")),
    CSV_APPLICATION_LOI_TABLEAU_SUIVI_MASQUER(new Builder("pan_stat97", "TableauDeSuivi")),
    CSV_APPLICATION_LOI_TABLEAU_SUIVI_AFFICHER(new Builder("pan_stat98", "TableauDeSuivi")),
    CSV_APPLICATION_LOI_TABLEAU_PROGRAMMATION(new Builder("pan_stat99", "TableauDeProgrammation")),
    CSV_APPLICATION_ORDO_TEXTES_MAITRES(new Builder("pan_stat100", "textMaitreOrdonnance")),
    CSV_APPLICATION_ORDO_TABLEAU_ORDOS(new Builder("pan_stat101", "TableauDesOrdonnances")),
    CSV_APPLICATION_ORDO_TABLEAU_SUIVI_MASQUER(new Builder("pan_stat102", "TableauDeSuiviOrdonnances")),
    CSV_APPLICATION_ORDO_TABLEAU_SUIVI_AFFICHER(new Builder("pan_stat103", "TableauDeSuiviOrdonnances")),
    CSV_APPLICATION_ORDO_TABLEAU_PROGRAMMATION(new Builder("pan_stat104", "TableauDeProgrammation")),
    CSV_SUIVI_HABILIT_TABLEAU_SUIVI(new Builder("pan_stat105", "tableauDeSuivi")),
    CSV_SUIVI_HABILIT_TABLEAU_PROGRAMMATION(new Builder("pan_stat106", "tableauDeProgrammation")),
    CSV_TRANSPO_DIRECTIVES_TEXTES_MAITRES(new Builder("pan_stat107", "textMaitre"));

    /**
     * Nom du fichier généré (sans l'extension)
     */
    private final String filename;

    /**
     * Libellé du rapport (les libellés étant identiques au nom de fichier n'ont pas
     * été trouvés dans l'application ; dans un souci de bon fonctionnement, ils ont
     * été gardés, mais les fichiers et libellés n'ont pas forcément lieu d'être)
     */
    private final String libelle;

    /**
     * Id du report dans list_reports.xml
     */
    private final String rptdesignId;

    /**
     * Est-ce que le calcul de cette stat doit être automatiquement relancé sur les
     * events de recalcul
     */
    private final boolean batchTriggerable;

    /**
     * Est dans la liste des rapports publiables (sur la page d'accueil) ?
     */
    private final boolean batchPubliable;

    /**
     * Affichage du lien publier sur la stat ?
     */
    private final boolean displayLienPubliee;

    /**
     * Stat concernant un ministère ?
     */
    private final boolean forMinistere;

    /**
     * Stat concernant une loi (à préciser dans les inputValues) ?
     */
    private final boolean forLoi;

    /**
     * Le rapport généré est-il une courbe temporelle (période à préciser dans
     * inputValues) ?
     */
    private final boolean reportCourbe;

    /**
     * Stat concernant un texte maitre (à intégrer dans inputValues via texte
     * maitre)?
     */
    private final boolean forTexte;

    /**
     * Permet d'affecter les bons paramètres date depuis le paramètre d'activité
     * normative
     */
    private final ParamAnTypeEnum generateReportParamAnType;

    /**
     * Permet d'affecter les bons paramètres date depuis le paramètre d'activité
     * normative
     */
    private final ParamAnTypeEnum publishPanParamAnType;

    // Données provenant de ArchiAnReportEnum
    private final String title;
    private final String theme;
    private final String categorie;
    private final boolean supportMinistereParam;
    private final boolean generationViaBatch;

    private final boolean isGraphique;

    private final String suiviUrl;
    private final String suiviCategorie;
    private final String suiviExtradata;
    private final int suiviOrder;
    private final String suiviLibelle;

    ANReportEnum(Builder builder) {
        this.filename = builder.name;
        this.libelle = builder.libelle;
        this.rptdesignId = builder.rptdesignId;
        this.batchTriggerable = builder.batchTriggerable;
        this.batchPubliable = builder.batchPubliable;
        this.displayLienPubliee = builder.displayLienPubliee;
        this.forMinistere = builder.forMinistere;
        this.forLoi = builder.forLoi;
        this.reportCourbe = builder.reportCourbe;
        this.forTexte = builder.forTexte;

        this.generateReportParamAnType = builder.generateReportParamAnType;
        this.publishPanParamAnType = builder.publishPanParamAnType;

        this.title = builder.title;
        this.theme = builder.theme;
        this.categorie = builder.categorie;
        this.supportMinistereParam = builder.supportMinistereParam;
        this.generationViaBatch = builder.generationViaBatch;
        this.isGraphique = builder.isGraphique;

        this.suiviUrl = builder.suiviUrl;
        this.suiviExtradata = builder.suiviExtraData;
        this.suiviCategorie = builder.suiviCategorie;
        this.suiviOrder = builder.suiviOrder;
        this.suiviLibelle = builder.suiviLibelle;
    }

    public static ANReportEnum getByReportId(String reportId) {
        return Stream
            .of(ANReportEnum.values())
            .filter(elem -> elem.getRptdesignId().equals(reportId))
            .findFirst()
            .orElse(null);
    }

    public String getFilename() {
        return filename;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getRptdesignId() {
        return rptdesignId;
    }

    public boolean isBatchTriggerable() {
        return batchTriggerable;
    }

    public boolean isBatchPubliable() {
        return batchPubliable;
    }

    public boolean isForMinistere() {
        return forMinistere;
    }

    public boolean isDisplayLienPubliee() {
        return displayLienPubliee;
    }

    public boolean isForLoi() {
        return forLoi;
    }

    public boolean isReportCourbe() {
        return reportCourbe;
    }

    public boolean isForTexte() {
        return forTexte;
    }

    public String getSuiviExtradata() {
        return suiviExtradata;
    }

    public String getSuiviCategorie() {
        return suiviCategorie;
    }

    public int getSuiviOrder() {
        return suiviOrder;
    }

    public String getSuiviLibelle() {
        return suiviLibelle;
    }

    public boolean isGraphique() {
        return isGraphique;
    }

    private static class Builder {
        private String name;
        private String libelle;
        private String rptdesignId;
        private boolean batchTriggerable = false;
        private boolean batchPubliable = false;
        private boolean displayLienPubliee = false;
        private boolean forMinistere = false;
        private boolean forLoi = false;
        private boolean reportCourbe = false;
        private boolean forTexte = false;
        private ParamAnTypeEnum generateReportParamAnType = ParamAnTypeEnum.NONE;
        private ParamAnTypeEnum publishPanParamAnType = ParamAnTypeEnum.NONE;

        // From ArchiAnReportEnum
        private String title;
        private String theme;
        private String categorie;
        private boolean supportMinistereParam;
        private boolean generationViaBatch;
        private boolean isGraphique = false;

        // Hors connexion
        private String suiviLibelle;
        private String suiviUrl;
        private String suiviExtraData;
        private String suiviCategorie;
        private int suiviOrder;

        private Builder(String rptdesignId, String name) {
            this.name = name;
            this.rptdesignId = rptdesignId;
        }

        private Builder setLibelle(String libelle) {
            this.libelle = libelle;
            return this;
        }

        private Builder setBatchTriggerable(boolean batchTriggerable) {
            this.batchTriggerable = batchTriggerable;
            return this;
        }

        private Builder setBatchPubliable(boolean batchPubliable) {
            this.batchPubliable = batchPubliable;
            return this;
        }

        private Builder setDisplayLienPubliee(boolean displayLienPubliee) {
            this.displayLienPubliee = displayLienPubliee;
            return this;
        }

        private Builder setForMinistere(boolean forMinistere) {
            this.forMinistere = forMinistere;
            return this;
        }

        private Builder setForLoi(boolean forLoi) {
            this.forLoi = forLoi;
            return this;
        }

        private Builder setReportCourbe(boolean reportCourbe) {
            this.reportCourbe = reportCourbe;
            return this;
        }

        private Builder setForTexte(boolean forTexte) {
            this.forTexte = forTexte;
            return this;
        }

        private Builder setGenerateReportParamAnType(ParamAnTypeEnum generateReportParamAnType) {
            this.generateReportParamAnType = generateReportParamAnType;
            return this;
        }

        private Builder setPublishPanParamAnType(ParamAnTypeEnum publishPanParamAnType) {
            this.publishPanParamAnType = publishPanParamAnType;
            return this;
        }

        public String getSuiviExtraData() {
            return suiviExtraData;
        }

        public String getSuiviUrl() {
            return suiviUrl;
        }

        public void setSuiviUrl(String suiviUrl) {
            this.suiviUrl = suiviUrl;
        }

        private Builder setArchiAnReportInfo(
            String title,
            String theme,
            String categorie,
            boolean supportMinistereParam,
            boolean generationViaBatch
        ) {
            this.title = title;
            this.theme = theme;
            this.categorie = categorie;
            this.supportMinistereParam = supportMinistereParam;
            this.generationViaBatch = generationViaBatch;
            return this;
        }

        private Builder setArchiAnReportInfoSuivi(
            String title,
            String theme,
            String categorie,
            boolean supportMinistereParam,
            boolean generationViaBatch,
            String suiviCategorie,
            String suiviLibelle,
            String suiviUrl
        ) {
            this.title = title;
            this.theme = theme;
            this.categorie = categorie;
            this.supportMinistereParam = supportMinistereParam;
            this.generationViaBatch = generationViaBatch;
            this.suiviCategorie = suiviCategorie;
            this.suiviLibelle = suiviLibelle;
            this.suiviUrl = suiviUrl;
            return this;
        }

        private Builder setArchiAnReportInfoSuivi(
            String title,
            String theme,
            String categorie,
            boolean supportMinistereParam,
            boolean generationViaBatch,
            String suiviCategorie,
            String suiviLibelle,
            String suiviExtraData,
            int suiviOrder,
            String url
        ) {
            this.title = title;
            this.theme = theme;
            this.categorie = categorie;
            this.supportMinistereParam = supportMinistereParam;
            this.generationViaBatch = generationViaBatch;
            this.suiviExtraData = suiviExtraData;
            this.suiviUrl = url;
            this.suiviCategorie = suiviCategorie;
            this.suiviOrder = suiviOrder;
            this.suiviLibelle = suiviLibelle;
            return this;
        }

        private Builder setArchiAnReportInfoSuivi(String suiviCategorie, String suiviLibelle, String suiviUrl) {
            this.suiviCategorie = suiviCategorie;
            this.suiviLibelle = suiviLibelle;
            this.suiviUrl = suiviUrl;
            return this;
        }

        public Builder setIsGraphique(boolean isGraphique) {
            this.isGraphique = isGraphique;
            return this;
        }
    }

    public boolean isArchiAnReportEnum() {
        return StringUtils.isNotEmpty(title);
    }

    public static List<ANReportEnum> archiAnReportEnumValues() {
        return Stream
            .of(values())
            .filter(ANReportEnum::isArchiAnReportEnum)
            .filter(ANReportEnum::isGenerationViaBatch)
            .collect(Collectors.toList());
    }

    public static List<ANReportEnum> getSuiviReport(String section, String tab) {
        return Stream
            .of(values())
            .filter(it -> tab.equals(it.getSuiviUrl()) && section.equals(it.getSuiviCategorie()))
            .collect(Collectors.toList());
    }

    public String getTitle() {
        return title;
    }

    public String getTheme() {
        return theme;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getSuiviUrl() {
        return suiviUrl;
    }

    public boolean isSupportMinistereParam() {
        return supportMinistereParam;
    }

    public boolean isGenerationViaBatch() {
        return generationViaBatch;
    }

    public String getFileName(String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append(getCategorie().trim());

        if (suffix != null) {
            sb.append("-");
            sb.append(suffix.trim());
        }
        if (!getTitle().equals(getCategorie())) {
            sb.append("-");
            sb.append(getTitle().trim());
        }
        sb.append(".");
        sb.append(MediaType.APPLICATION_MS_EXCEL.extension());
        return sb.toString();
    }

    public static List<ANReportEnum> getOtherSuiviReportBySection(String section) {
        return Stream
            .of(values())
            .filter(it -> section.equals(it.getSuiviCategorie()) && StringUtils.isNotBlank(it.getSuiviExtradata()))
            .sorted(Comparator.comparing(ANReportEnum::getSuiviOrder))
            .collect(Collectors.toList());
    }

    public ParamAnTypeEnum getGenerateReportParamAnType() {
        return generateReportParamAnType;
    }

    public ParamAnTypeEnum getPublishPanParamAnType() {
        return publishPanParamAnType;
    }

    private static class Constants {
        private static final String MESURES_LEGISLATIVES_APPELANT_UN_DECRET = "Mesures législatives appelant un décret";
        private static final String MESURES_AVEC_ENTREE_EN_VIGUEUR_DIFFEREE = "Mesures avec entrée en vigueur différée";
        private static final String BILAN_SEMESTRIEL_PAR_MINISTERE = "Bilan semestriel par ministère";
        private static final String TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_MINISTERE =
            "Taux d'application au fil de l'eau par ministère";
        private static final String MESURES_D_APPLICATION_POUR_LESQUELLES_LA_DATE =
            "Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée";
        private static final String DELAI_DE_MISE_EN_APPLICATION_DES_LOIS = "Délai de mise en application des lois";
        private static final String PAR_LOI = "Par loi";
        private static final String PAR_MINISTERE = "Par ministère";
        private static final String PAR_NATURE_DE_TEXTE = "Par nature de texte";
        private static final String STATISTIQUE_SUR_LA_PUBLICATION = "Statistique sur la publication";
        private static final String TAUX_D_APPLICATION_AU_FIL_DE_L_EAU_PAR_ORDONNANCE =
            "Taux d'application au fil de l'eau par ordonnance";
        private static final String PAR_ORDONNANCE = "Par ordonnance";
        private static final String SUIVI_GLOBAL_DE_LA_RATIFICATION_DES_ORDONNANCES =
            "Suivi global de la ratification des ordonnances de l'article 74-1";
    }

    public static class SuiviConstants {
        public static final String SUIVI_DETAILS = "details";
        public static final String SUIVI_ACC_DETAILS = "detailsAccordion";
        public static final String SUIVI_MINISTERES = "ministere";
        public static final String SUIVI_MINISTERES_0_SUIVI_ORDONNANCES = "ministere0Habilitations";
        public static final String SUIVI_MINISTERES_1_LOIS = "ministere1Lois";
        public static final String SUIVI_MINISTERES_1_ORDONNANCES = "ministere1Ordonnances";
        public static final String SUIVI_MINISTERES_1_SUIVI_ORDONNANCES = "ministere1Habilitations";
        public static final String SUIVI_MINISTERES_2_LOIS = "ministere2Lois";
        public static final String SUIVI_MINISTERES_2_ORDONNANCES = "ministere2Ordonnances";
        public static final String SUIVI_MINISTERES_2_SUIVI_ORDONNANCES = "ministere2Habilitations";

        public static final String SUIVI_CATEGORIE_LOIS = "applicationLois";
        public static final String SUIVI_CATEGORIE_ORDONNANCES = "applicationOrdonnances";
        public static final String SUIVI_CATEGORIE_SUIVI_ORDONNANCES = "suiviOrdonnances";
        public static final String SUIVI_EXTRADATA_KEY_AUTRES = "autres";
        public static final String SUIVI_EXTRADATA_KEY_STATISTIQUES = "statistiques";
        public static final String SUIVI_EXTRADATA_KEY_RATIFICATIONS = "ratifications";

        public static final String SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_LOI = "Bilan semestriel, par loi";
        public static final String SUIVI_LIBELLE_DELAI_MISE_APP_PAR_LOI = "Délai de mise en application, par loi";
        public static final String SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_ORDONNANCE = "Bilan semestriel, par ordonnance";
        public static final String SUIVI_LIBELLE_DELAI_MISE_APP_PAR_ORDONNANCE =
            "Délai de mise en application, par ordonnance";
        public static final String SUIVI_LIBELLE_MESURES_ATTENTE_PAR_LOI =
            "Mesures en attente de décret d'application, par loi";
        public static final String SUIVI_LIBELLE_MESURES_DIFFEREES_PAR_LOI =
            "Mesures avec entrée en vigueur différée, par loi";
        public static final String SUIVI_LIBELLE_MESURES_ATTENTE_PAR_ORDONNANCE =
            "Mesures en attente de décret d'application, par ordonnance";
        public static final String SUIVI_LIBELLE_MESURES_DIFFEREES_PAR_ORDONNANCE =
            "Mesures avec entrée en vigueur différée, par ordonnance";
        public static final String SUIVI_LIBELLE_TX_APP_GLOBAL = "Taux d'application au fil de l'eau global";
        public static final String SUIVI_LIBELLE_TX_APP_PAR_MINISTERE =
            "Taux d'application au fil de l'eau par ministere";
        public static final String SUIVI_LIBELLE_BILAN_SEMESTRIEL_PAR_MINISTERE = "Bilan semestriel par ministère";
        public static final String SUIVI_LIBELLE_DELAI_PAR_MINISTERE = "Délai de mise en application, par ministère";

        public static final String SUIVI_LIBELLE_RATIFICATION_PAS_DEPOSES =
            "Ordonnances de l'article 38 dont le projet de loi de ratification n'a pas été déposé";
        public static final String SUIVI_LIBELLE_RATIFICATION_38C =
            "Ordonnances prises sur le fondement de l'article 38 de la Constitution";
        public static final String SUIVI_LIBELLE_RATIFICATION_741 =
            "Ordonnances prises sur le fondement de l'article 74-1 de la Constitution";
        public static final String SUIVI_LIBELLE_LISTE_HABILITATIONS =
            "Liste des habilitations depuis la XIIIe législature";
        public static final String SUIVI_LIBELLE_MESURES_APPLICATION_LOI = "Suivi des mesures d'application de la loi";
        public static final String SUIVI_LIBELLE_MESURES_RESTANT = "Suivi des seules mesures restant à prendre";
        public static final String SUIVI_LIBELLE_DELAI_PUBLICATION_LOI = "Délai de publication des décrets par loi";
        public static final String SUIVI_LIBELLE_MESURES_APPLICATION_ORDONNANCE =
            "Suivi des mesures d'application de l'ordonnance";
        public static final String SUIVI_LIBELLE_DELAI_PUBLICATION_ORDONNANCE =
            "Délai de publication des décrets par ordonnance";
        public static final String SUIVI_LIBELLE_TAUX_APPLICATION_LOI = "Taux d'application de la loi";
        public static final String SUIVI_LIBELLE_TAUX_APPLICATION_ORDONNANCE = "Taux d'application de l'ordonnance";
        public static final String SUIVI_LIBELLE_REPARTITION_MESURES_MINISTERE =
            "Répartition des mesures par ministère";

        public static final String SUIVI_LISTE_HABILITATIONS = "liste-habilitations";

        public static final String SUIVI_BILAN_SEMESTRIEL_PAR_ELM = "bilan-semestriel-par-elm";
        public static final String SUIVI_DELAI_MISE_APPLICATION_PAR_ELM = "delai-mise-application-par-elm";
        public static final String SUIVI_MESURES_ATTENTE_PAR_ELM = "mesures-en-attente-par-elm";
        public static final String SUIVI_MESURES_DIFFEREE_PAR_ELM = "mesures-differee-par-elm";
        public static final String SUIVI_TAUX_APPLICATION_GLOBAL = "taux-application-global";
        public static final String SUIVI_TAUX_APPLICATION_PAR_MINISTERE = "taux-application-par-ministere";
        public static final String SUIVI_BILAN_SEMESTRIEL_PAR_MINISTERE = "bilan-semestriel-par-ministere";
        public static final String SUIVI_DELAI_MISE_APPLICATION_PAR_MINISTERE = "delai-mise-application-par-ministere";

        public static final String SUIVI_SUIVI_ORDONNANCES_RATIFICATION_PAS_DEPOSES = "ratification-pas-deposes";
        public static final String SUIVI_SUIVI_ORDONNANCES_RATIFICATION_38C = "ratification-38c";
        public static final String SUIVI_SUIVI_ORDONNANCES_RATIFICATION_741 = "ratification-74-1";
    }
}
