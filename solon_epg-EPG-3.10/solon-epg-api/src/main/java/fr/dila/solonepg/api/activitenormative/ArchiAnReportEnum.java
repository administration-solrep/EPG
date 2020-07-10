package fr.dila.solonepg.api.activitenormative;

import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;

public enum ArchiAnReportEnum {

	/*#################################################################################
	 * 
	 * 							APPLICATION DES LOIS
	 * 
	 ##################################################################################*/

				/*##### MESURES D'APPLICATION ##### */
	MESURE_APPLICATION_PAR_MINISTERE("Mesures d'application par ministère", 
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_ALL,	true, null),

	MESURE_APPLICATION_PAR_LOI("Mesures d'application par loi",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI_ALL, false, null, false),
	
	MESURE_APPLICATION_PAR_LOI_NONAPPLI("Mesures d'application par loi non appli",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI, false, null, false),
	
	MESURE_APPLICATION_NONAPPLI_PAR_MINISTERE("Mesures d'application non appliquées par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN, true, null),
	
	MESURE_LEGISLATIVE_APPELANT_UN_DECRET("Mesures législatives appelant un décret",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_ACTIVE_ALL, false, null),
	
	MESURE_LEGISLATIVE_APPELANT_NONAPPLI_UN_DECRET("Mesures législatives non appliquées appelant un décret",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_ACTIVE, false, null),
	
	MESURE_APPLICATION_PURLESQUELLES_LA_DATE_EST_DEPASSE("Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_DEPASSE_ALL, false, null),
	
	MESURE_APPLICATION_NONAPPLI_PURLESQUELLES_LA_DATE_EST_DEPASSE("Mesures d'application non appliquées pour lesquelles la date prévisionnelle de publication est dépassée",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_DEPASSE, false, null),
	
	MESURE_AVEC_ENTREE_EN_VIGEUR_DIFFEREE("Mesures avec entrée en vigueur différée",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_DIFFEREE, false, null),


				/*##### BILAN SEMESTRIEL ##### */
	BILAN_SEMESTRIEL_PAR_MINISTERE("Bilan semestriel par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN, true, null),
	
	BILAN_SEMESTRIEL_PAR_LOI("Bilan semestriel par loi",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_LOI, false, null, false),
	
	BS_PAR_LOI("Par loi",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
			ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS, false, null),
	
	BS_PAR_MINISTERE("Par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
			ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS, false,	null),
	
	BS_PAR_NATURE_DE_TEXTE("Par nature de texte",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
			ANReportEnum.TAB_AN_BILAN_SEM_NATURE, false, null),
	
	BS_PAR_PROCEDURE_DE_VOTE("Par procédure de vote",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
			ANReportEnum.TAB_AN_BILAN_SEM_VOTE, false, null),


				/* ##### TAUX ##### */
	TAUX_EXECUTION_DES_LOIS_PROMULGUEES("Taux d'exécution des lois promulguées",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN, true, null),
	
	TAUX_APPLICATION_AU_FIL_DE_LEAU("Taux d'application au fil de l'eau par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_TAUX_APPLICATION_MIN, true, null),
	
	TAUX_APPLICATION_AU_FIL_DE_LEAU_LOI("Taux d'application au fil de l'eau par loi",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_TAUX_APPLICATION_LOI_ALL, false, null, false),

	TAUX_EXECUTION_DES_LOIS("Taux d'exécution des lois", 
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_INDICATEUR_LOLF,
			ANReportEnum.TAB_AN_STAT_DEBUT_LEGISLATURE, false, null),
	
	TAUX_EXECUTION_DES_LOIS_PROMULGUEES_AU_COURS_DE_LA_DERNIERE_SESSION_PARLEMENTAIRE("Taux d'exécution des lois promulguées au cours de la dernière session parlementaire",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS,ActiviteNormativeStatsConstants.AN_CAT_INDICATEUR_LOLF,
			ANReportEnum.TAB_AN_STAT_DERNIERE_SESSION, false, null),

	TAUX_APP_AU_FIL_EAU_GLOBAL("Global",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_GLOBAL, false, null),
	
	TAUX_APP_AU_FIL_EAU_PAR_MINISTERE("Par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_MIN_TOUS, false, null),
	
	TAUX_APP_AU_FIL_EAU_PAR_LOI("Par loi",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_LOI_TOUS_ALL, false, null),
	
	TAUX_APP_AU_FIL_EAU_PAR_NATURE_DE_TEXTE( "Par nature de texte",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_NATURE, false, null),
	
	TAUX_APP_AU_FIL_EAU_PAR_PROCEDURE_DE_VOTE("Par procédure de vote",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_VOTE, false, null),


				/* ##### DELAIS ##### */
	DELAI_DE_LA_MISE_EN_APPLICATION_DES_LOIS("Délai de mise en application des lois",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_INDICATEUR_LOLF,
			ANReportEnum.TAB_AN_STAT_MISE_APPLICATION, false, null),
	
	DM_PAR_LOI("Par loi",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
			ANReportEnum.TAB_AN_DELAI_LOI_TOUS, false, null),
	
	DM_PAR_MINISTERE("Par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
			ANReportEnum.TAB_AN_DELAI_MIN_TOUS, false, null),
	
	DM_PAR_NATURE_DE_TEXTE("Par nature de texte",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
			ANReportEnum.TAB_AN_DELAI_NATURE_TOUS, false, null),


				/* ##### AUTRES ##### */	
	STATISTIQUE_PUBLIE("Statistique sur la publication",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, "Statistique sur la publication",
			ANReportEnum.TAB_AN_COURBE_TOUS, false, null, false),
	
	TABLEAU_DES_LOIS("Tableau des lois",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DES_LOIS,
			null, false, ActiviteNormativeStatsConstants.AN_APPLICATION_DE_LOIS_TABLEAU_LOIS_REPORT_FILE),
	
	/*#################################################################################
	 * 
	 * 							APPLICATION DES ORDONNANCES
	 * 
	 ##################################################################################*/
	
				/*##### MESURES D'APPLICATION ##### */
	MESURE_APPLICATION_PAR_MINISTERE_APP_ORDO("Mesures d'application par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO_ALL, true, null),

	MESURE_APPLICATION_PAR_LOI_APP_ORDO("Mesures d'application par ordonnance",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL, false, null, false),
	
	MESURE_APPLICATION_PAR_LOI_NONAPPLI_APP_ORDO("Mesures d'application par ordonnance non appli",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES, false, null, false),
	
	MESURE_APPLICATION_NONAPPLI_PAR_MINISTERE_APP_ORDO("Mesures d'application non appliquées par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO, true, null),
	
	MESURE_LEGISLATIVE_APPELANT_UN_DECRET_APP_ORDO("Mesures des ordonnances appelant un décret",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TB_APP_ORDO_ACTIVE_ALL, false, null),
	
	MESURE_LEGISLATIVE_APPELANT_NONAPPLI_UN_DECRET_APP_ORDO("Mesures des ordonnances non appliquées appelant un décret",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TB_APP_ORDO_ACTIVE, false, null),
	
	MESURE_APPLICATION_PURLESQUELLES_LA_DATE_EST_DEPASSE_APP_ORDO("Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TB_APP_ORDO_DEPASSE_ALL, false, null),
	
	MESURE_APPLICATION_NONAPPLI_PURLESQUELLES_LA_DATE_EST_DEPASSE_APP_ORDO("Mesures d'application non appliquées pour lesquelles la date prévisionnelle de publication est dépassée",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TB_APP_ORDO_DEPASSE, false, null),
	
	MESURE_AVEC_ENTREE_EN_VIGEUR_DIFFEREE_APP_ORDO("Mesures avec entrée en vigueur différée",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TB_APP_ORDO_DIFFEREE, false, null),

	
				/* ##### BILAN SEMESTRIEL ##### */
	BILAN_SEMESTRIEL_PAR_MINISTERE_APP_ORDO("Bilan semestriel par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN_APP_ORDO, true, null),
	
	BILAN_SEMESTRIEL_PAR_ORDO_APP_ORDO("Bilan semestriel par ordonnance",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES, false, null, false),
	
	BS_PAR_ORDONNANCE("Par ordonnance",			
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
			ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS, false, null),
	
	BS_PAR_MINISTERE_ORDONNANCE("Par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_BILAN_SEMESTRIEL,
			ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS_ORDONNANCE, false, null),
	
	
				/* ##### TAUX ##### */
	TAUX_EXECUTION_DES_ORDO_PROMUL_APP_ORDO("Taux d'exécution des ordonnances publiées",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO, true, null),
	
	TAUX_APPLICATION_AU_FIL_DE_LEAU_APP_ORDO("Taux d'application au fil de l'eau par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_TAUX_APPLICATION_MIN_APP_ORDO, true, null),
	
	TAUX_APPLICATION_AU_FIL_DE_LEAU_ORDO_APP_ORDO("Taux d'application au fil de l'eau par ordonnance",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL, false, null, false),
	
	TAUX_APP_AU_FIL_EAU_GLOBAL_APP_ORDO("Global",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_GLOBAL_APP_ORDONNANCE, false, null),
	
	TAUX_APP_AU_FIL_EAU_PAR_MINISTERE_APP_ORDO("Par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_MIN_TOUS_APP_ORDONNANCE, false, null),
	
	TAUX_APP_AU_FIL_EAU_PAR_ORDO_APP_ORDO("Par ordonnance",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU,
			ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE, false, null),

	TAUX_EXECUTION_DES_ORDONNANCES("Taux d'exécution des ordonnances", 
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_INDICATEURS,
			ANReportEnum.TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE, false, null),
	
	TAUX_EXECUTION_ORDO_PROMULGUEES_AU_COURS_DE_LA_DERNIERE_SESSION_PARLEMENTAIRE("Taux d'exécution des ordonnances publiées au cours de la dernière session parlementaire",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES,ActiviteNormativeStatsConstants.AN_CAT_INDICATEURS,
			ANReportEnum.TAB_AN_INDIC_APP_ORDO_DERNIERE_SESSION, false, null),
	
	
				/* ##### DELAIS ##### */
	DM_EN_APPLICATION_DES_ORDOS("Délai de mise en application des ordonnances",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_INDICATEURS,
			ANReportEnum.TAB_AN_INDIC_APP_ORDO_MISE_APPLI, false, null),
	
	DM_PAR_ORDO_APP_ORDO("Par ordonnance",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
			ANReportEnum.TAB_AN_DELAI_ORDO_TOUS_APP_ORDO, false, null),
	
	DM_PAR_MINISTERE_APP_ORDO("Par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_DELAI_MOYEN,
			ANReportEnum.TAB_AN_DELAI_MIN_TOUS_APP_ORDO, false, null),
	
	
				/* ##### AUTRES ##### */
	STATISTIQUE_PUBLIE_APP_ORDO("Statistique sur la publication",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, "Statistique sur la publication",
			ANReportEnum.TAB_AN_COURBE_TOUS_APP_ORDO, false, null, false),
	
	TABLEAU_DES_ORDONNANCES("Tableau des ordonnances",
			ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DES_ORDOS,
			null, false, ActiviteNormativeStatsConstants.AN_APPLICATION_DES_ORDOS_TABLEAU_DES_ORDOS_REPORT_FILE),
	
	/*#################################################################################
	 * 
	 * 							SUIVI DES HABILITATIONS
	 * 
	 ##################################################################################*/
	SUIVI_DES_HABILITATIONS_DE_LA_LEGISLATURE_EN_COURS("Suivi des habilitations de la législature en cours ",
			ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_HABILITATION_FLTR_MIN, true, null),
	
	SUIVI_DE_TOUTES_HABILITATIONS_DE_LA_LEGISLATURE_EN_COURS("Suivi de toutes les habilitations de la législature en cours ",
			ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_HABILITATION_FLTR_MIN_ALL, true, null),
	
	SUIVI_DES_HABILITATIONS_PAR_MINISTERE("Suivi par ministère des habilitations ",
			ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_HABILITATION_SS_FLTR_MIN, true, null),
	
	SUIVI_DE_TOUTES_HABILITATIONS_PAR_MINISTERE("Suivi par ministère de toutes les habilitations ",
			ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_HABILITATION_SS_FLTR_MIN_ALL, true, null),
	
	SUIVI_GLOBAL_DES_HABILITATIONS("Suivi global des habilitations ",
			ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE, false, null),
	
	SUIVI_GLOBAL_DE_TOUTES_HABILITATIONS("Suivi global de toutes les habilitations ",
			ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_ALL, false, null),	
	
	/*#################################################################################
	 * 
	 * 							RATIFICATION DES ORDONNANCES
	 * 
	 ##################################################################################*/
	SUIVI_GLOBAL_DE_LA_RATIFICTION_DES_ORDONNANCES_38C("Suivi global de la ratification des ordonnances de l'article 38 C ",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_RATIFICATION_38C_MIN, true, null),
	
	SUIVI_GLOBAL_DE_TOUTE_RATIFICTION_DES_ORDONNANCES_38C("Suivi global de toutes les ratifications des ordonnances de l'article 38 C ",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE, 
			ANReportEnum.TAB_AN_RATIFICATION_38C_MIN_ALL, true, null),
	
	SUIVI_GLOBAL_DE_LA_RATIFICTION_DES_ORDONNANCES74_1(
			"Suivi global de la ratification des ordonnances de l'article 74-1",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_RATIFICATION_741_MIN, true, null),
	
	SUIVI_GLOBAL_DE_TOUTE_RATIFICTION_DES_ORDONNANCES74_1("Suivi global de toutes les ratifications des ordonnances de l'article 74-1",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_RATIFICATION_741_MIN_ALL, true, null),
	
	ORDONNACE_DE_LARTICLE38_DONT_LE_PROJET_PAS_DEPOSE("Ordonnances de l'article 38 C dont le projet de loi de ratification n'a pas été déposé",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE, false, null),
	
	SUIVI_GLOBAL_DES_ORDONNANCES_38("Suivi global de la ratification des ordonnances de l'article 38 C",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_38C, false, null),
	
	SUIVI_GLOBAL_DE_TOUTES_ORDONNANCES_38("Suivi global de toutes les ratifications des ordonnances de l'article 38 C",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_38C_ALL, false, null),
	
	SUIVI_GLOBALE_DES_ORDONNANCES74_1("Suivi global de la ratification des ordonnances de l'article 74-1",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_741, false, null),
	
	SUIVI_GLOBALE_DE_TOUTES_ORDONNANCES74_1("Suivi global de toutes les ratifications des ordonnances de l'article 74-1",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_741_ALL, false, null),
	
	SUIVI_DES_ECRETS_PRIS_EN_APPLICATION_DES_ORDONNANCES("Suivi des décrets pris en application des ordonnances",
			ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_DECRET, false, null),
	
	/*#################################################################################
	 * 
	 * 							TRANSPOSITION DES DIRECTIVES
	 * 
	 ##################################################################################*/
	SUIVI_DES_TRAVEAUX_DE_TRANSPOSITIONS_MINISTERE("Suivi des travaux de transposition  à la charge des ministères",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE,
			ANReportEnum.TAB_AN_TRANSPOSITION_ENCOURS_MIN, true, null),

	DIRECTIVES_DONT_TRANSPOSITION_ACHEVEE_PAR_MINISTERE("Directives dont la transposition est achevée par ministère",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE, 
			ANReportEnum.TAB_AN_TRANSPOSITION_ACHEVEE_MIN, true, null),
	
	DIRECTIVES_DONT_TRANSPOSITION_ACHEVEE("Directives dont la transposition est achevée",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_TRANSPOSITION_ACHEVEE, false, null), 
	
	DIRECTIVES_DONT_TRANSPOSITION_EN_COURS("Directives dont la transposition reste en cours",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_TRANSPOSITION_ENCOURS, false, null),

	INDICATEUR_DE_TRANSPOSITION("Indicateurs de transposition",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
			ANReportEnum.TAB_AN_STAT_INDICATEUR_MIN_TRANS, false, null), 
	
	RETARD_DE_TRANSPOSITION("Retards moyens de transposition",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
			ANReportEnum.TAB_AN_STAT_RETARD_MOY_TRANS, false, null), 
	
	REPARTITION_DES_TEXTES_A_PRENDRE("Répartition des textes restant à prendre",
			ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES, ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
			ANReportEnum.TAB_AN_STAT_REPARTITION_DIR_APRENDRE, false, null),

	/*#################################################################################
	 * 
	 * 							TRAITES ET ACCORDS
	 * 
	 ##################################################################################*/
	TABLEAUX_MINISTRIELS_DES_TRAITES_ET_ACCORD_DONT_LA_PUBLICATION_RESTE_A_INTERVENIR("Tableaux ministériels des traités et accords dont la publication reste à intervenir",
			ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,	ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_MAITRE, 
			ANReportEnum.TAB_AN_TRAITE_PUB_AVENIR_MIN, true, null, false), 
	
	INDICATEUR_MINISTRIELS_DE_CONFECTION_DES_ETUDES_IMPACT("Indicateurs ministériels de confection des études d'impact",
			ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD,	ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
			ANReportEnum.TAB_AN_STAT_TRAITE_ETUDES_IMPACT, false, null, false),
	
	REPARTITION_PAR_MINISTERE_DES_TRAITES_ET_ACCORDS("Répartition par ministères des traités et accords signés et restant à publier",
			ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD, ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
			ANReportEnum.TAB_AN_STAT_TRAITE_PUBLIER, false, null, false),
	
	TABLEAU_GENERAL_DES_TRAITES_ET_ACCORDS_DONT_LA_PUBLICATION_EST_INTERVENUE("Tableau général des traités et accords dont la publication est intervenue",
			ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD, ActiviteNormativeStatsConstants.AN_CAT_TABLEAU_DE_BORD,
			ANReportEnum.TAB_AN_TABLEAU_BORD_TRAITE_PUB_INTERVENUE, false, null, false),
	
	TABLEAU_GENERAL_DES_TRAITES_ET_ACCORDS_DONT_LA_PUBLICATION_A_INTERVENIR("Tableau général des traités et accords dont la publication reste à intervenir",
			ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD, ActiviteNormativeStatsConstants.AN_CAT_STATISTIQUES,
			ANReportEnum.TAB_AN_TABLEAU_BORD_TRAITE_PUB_INTERVENIR, false, null, false);

	public static final String	FORMAT_XLS	= "xls";

	private String				title;
	private String				theme;
	private String				categorie;
	private ANReportEnum		anReportEnum;
	private String				fileName;
	private boolean				supportMinistereParam;
	private boolean				generationViaBatch;

	private ArchiAnReportEnum(String title, String theme, String categorie, ANReportEnum anReportEnum,
			boolean supportMinistereParam, String fileName) {
		this(title, theme, categorie, anReportEnum, supportMinistereParam, fileName, true);
	}

	private ArchiAnReportEnum(String title, String theme, String categorie, ANReportEnum anReportEnum,
			boolean supportMinistereParam, String fileName, boolean batchGener) {
		this.title = title;
		this.theme = theme;
		this.fileName = fileName;
		this.categorie = categorie;
		this.anReportEnum = anReportEnum;
		this.supportMinistereParam = supportMinistereParam;
		this.generationViaBatch = batchGener;
	}

	public String getTheme() {
		return theme;
	}

	protected void setTheme(String theme) {
		this.theme = theme;
	}

	public String getCategorie() {
		return categorie;
	}

	protected void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public boolean isSupportMinistereParam() {
		return supportMinistereParam;
	}

	protected void setSupportMinistereParam(boolean supportMinistereParam) {
		this.supportMinistereParam = supportMinistereParam;
	}

	public String getTitle() {
		return title;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	public String getFileName(String ministereTitle) {
		StringBuilder sb = new StringBuilder();
		sb.append(getCategorie().trim());

		if (ministereTitle != null) {
			sb.append("-");
			sb.append(ministereTitle.trim());
		}
		if (!getTitle().equals(getCategorie())) {
			sb.append("-");
			sb.append(getTitle().trim());
		}
		sb.append(".");
		sb.append(FORMAT_XLS);
		return sb.toString();
	}

	public static ArchiAnReportEnum findArchiAnReportEnum(ANReportEnum anReportEnum) {
		for (ArchiAnReportEnum archRep : values()) {
			if (archRep.getAnReportEnum() != null && archRep.getAnReportEnum().equals(anReportEnum)) {
				return archRep;
			}
		}
		return null;
	}

	public ANReportEnum getAnReportEnum() {
		return anReportEnum;
	}

	public String getFileName() {
		if (fileName == null) {
			fileName = this.getAnReportEnum().getFile();
		}
		return fileName;
	}

	public boolean isGenerationViaBatch() {
		return generationViaBatch;
	}

	protected void setGenerationViaBatch(boolean generationViaBatch) {
		this.generationViaBatch = generationViaBatch;
	}
}
