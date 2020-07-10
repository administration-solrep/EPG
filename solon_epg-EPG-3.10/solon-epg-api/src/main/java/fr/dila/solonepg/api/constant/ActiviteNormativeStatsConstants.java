package fr.dila.solonepg.api.constant;

public class ActiviteNormativeStatsConstants {

	public static final String	BIRT_REFRESH_FICHIER_SCHEMA															= "birt_refresh_fichier";

	public static final String	BIRT_REFRESH_ROOT_TYPE																= "BirtRefreshRoot";

	public static final String	BIRT_REFRESH_FICHIER_PREFIX															= "breff";

	public static final String	BIRT_REFRESH_FICHIER_FILE_NAME_PROPERTY												= "filename";

	public static final String	BIRT_REFRESH_FICHIER_OWNER_PROPERTY													= "owner";

	public static final String	BIRT_REFRESH_FICHIER_DATE_PROPERTY													= "dateRequest";

	public static final String	BIRT_REFRESH_FICHIER_CONTENT_PROPERTY												= "content";

	public static final String	BIRT_REFRESH_FICHIER_CONTENT_CSV_PROPERTY											= "contentCsv";

	public static final String	BIRT_REFRESH_FICHIER_CONTENT_PDF_PROPERTY											= "contentPdf";
	
	public static final String	BIRT_REFRESH_FICHIER_PARAM_LIST_PROPERTY											= "paramList";

	public static final String	BIRT_REFRESH_FICHIER_TYPE															= "BirtRefreshFichier";

	public static final String	BIRT_RESULTAT_FICHIER_SCHEMA														= "birt_resultat_fichier";

	public static final String	BIRT_RESULTAT_FICHIER_PATH															= "/case-management/birt-resultat-root";

	public static final String	BIRT_RESULTAT_FICHIER_PREFIX														= "brf";

	public static final String	BIRT_RESULTAT_FICHIER_FILE_NAME_PROPERTY											= "filename";

	public static final String	BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY												= "content";

	public static final String	BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY											= "contentCsv";

	public static final String	BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY											= "contentPdf";

	public static final String	BIRT_RESULTAT_FICHIER_MODIFIED														= "modified";

	public static final String	BIRT_RESULTAT_FICHIER_TYPE															= "BirtResultatFichier";

	public static final String	BIRT_REPORT_ROOT																	= "birtReports/";

	public static final String	BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER												= BIRT_REPORT_ROOT
																															+ "activitenormative/";

	public static final String	BIRT_REPORT_APP_ORDONNANCES_FOLDER													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "application_ordonnances/";

	public static final String	TABLEAU_BORD_MESURES_APPLICATION_ACTIVES_GENERATED_REPORT_HTML						= "tableau_bord_mesures_application_active.xhtml";

	public static final String	AN_MESURES_APPLICATION_ACTIVES_NON_PUBLIEES_REPORT_NAME								= "mesuresApplicaticationActiveNonPubliees";
	public static final String	AN_MESURES_APPLICATION_ACTIVES_NON_PUBLIEES_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_actives_decret_non_pub.rptdesign";

	public static final String	AN_MESURES_APPLICATION_ACTIVES_ALL_REPORT_NAME										= "mesureApplicaticationActive";
	public static final String	AN_MESURES_APPLICATION_ACTIVES_ALL_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_actives_tous_decret.rptdesign";

	public static final String	AN_MESURES_APPLICATION_DIFFEREE_REPORT_NAME											= "mesureApplicaticationDifferee";
	public static final String	AN_MESURES_APPLICATION_DIFFEREE_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_differee.rptdesign";

	public static final String	AN_MESURES_APPLICATION_DEPASSES_NON_PUBLIEES_REPORT_FILE							= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_previsionnelle_depassee_decret_non_pub.rptdesign";
	public static final String	AN_MESURES_APPLICATION_DEPASSES_NON_PUBLIEES_REPORT_NAME							= "mesuresApplicaticationDepassesNonPubliees";

	public static final String	AN_MESURES_APPLICATION_DEPASSES_ALL_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_previsionnelle_depassee_tous_decret.rptdesign";
	public static final String	AN_MESURES_APPLICATION_DEPASSES_ALL_REPORT_NAME										= "mesureApplicaticationDepasses";

	public static final String	AN_MESURES_APPLICATION_PAR_LOI_NON_PUBLIEES_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_par_loi_decret_non_pub.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_LOI_NON_PUBLIEES_REPORT_NAME								= "mesuresApplicaticationParLoiNonPubliees";

	public static final String	AN_MESURES_APPLICATION_PAR_ORDO_APP_ORDO_NON_PUBLIEES_REPORT_NAME					= "mesuresApplicaticationParOrdoAppOrdoNonPubliees";

	public static final String	AN_MESURES_APPLICATION_PAR_ORDO_APP_ORDO_NON_PUBLIEES_REPORT_FILE					= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_par_ordo_app_ordo_decret_non_pub.rptdesign";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_LOI_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_par_loi.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_PAR_LOI_REPORT_NAME											= "bilanSemestrielsParLoi";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_ORDO_APP_ORDO_REPORT_FILE									= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_par_ordonnance.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_PAR_ORDO_APP_ORDO_REPORT_NAME									= "bilanSemestrielsParOrdonnance";

	public static final String	AN_TAUX_APPLICATION_PAR_LOI_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_loi_affichage_initial.rptdesign";
	public static final String	AN_TAUX_APPLICATION_PAR_LOI_REPORT_NAME												= "tauxDapplicationAuFilDeLeauParLoiAffichageInitiale";

	public static final String	AN_TAUX_APPLICATION_PAR_ORDO_APP_ORDO_REPORT_FILE									= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_ordonnance_affichage_initial.rptdesign";
	public static final String	AN_TAUX_APPLICATION_PAR_ORDO_APP_ORDO_REPORT_NAME									= "tauxDapplicationAuFilDeLeauParOrdonnanceAffichageInitiale";

	public static final String	AN_TAUX_APPLICATION_PAR_LOI_ALL_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_loi.rptdesign";
	public static final String	AN_TAUX_APPLICATION_PAR_LOI_ALL_REPORT_NAME											= "tauxDapplicationAuFilDeLeauParLoiAffichageAvencee";

	public static final String	AN_TAUX_APPLICATION_PAR_ORDO_ALL_APP_ORDO_REPORT_FILE								= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_ordonnance.rptdesign";
	public static final String	AN_TAUX_APPLICATION_PAR_ORDO_ALL_APP_ORDO_REPORT_NAME								= "tauxDapplicationAuFilDeLeauParOrdonnanceAffichageAvencee";

	public static final String	AN_MESURES_APPLICATION_PAR_LOI_ALL_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_par_loi_tous_decret.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_LOI_ALL_REPORT_NAME										= "mesuresApplicaticationParLoi";

	public static final String	AN_MESURES_APPLICATION_PAR_ORDO_APP_ORDO_ALL_REPORT_FILE							= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_par_ordo_app_ordo_tous_decret.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_ORDO_APP_ORDO_ALL_REPORT_NAME							= "mesuresApplicaticationParOrdoAppOrdo";

	public static final String	AN_MESURES_APPLICATION_PAR_MIN_NON_PUBLIEES_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_par_ministere_decret_non_pub.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_MIN_NON_PUBLIEES_REPORT_NAME								= "mesuresApplicaticationParMinistereNonPubliees";

	public static final String	AN_MESURES_APPLICATION_PAR_MIN_NON_PUBLIEES_APP_ORDO_REPORT_FILE					= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_par_ministere_decret_non_pub_app_ordo.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_MIN_NON_PUBLIEES_APP_ORDO_REPORT_NAME					= "mesuresApplicaticationParMinistereNonPublieesAppOrdo";

	public static final String	AN_MESURES_APPLICATION_PAR_MIN_ALL_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_application_par_ministere_tous_decret.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_MIN_ALL_REPORT_NAME										= "mesuresApplicaticationParMinistere";

	public static final String	AN_MESURES_APPLICATION_PAR_MIN_APP_ORDO_ALL_REPORT_FILE								= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_par_ministere_tous_decret_app_ordo.rptdesign";
	public static final String	AN_MESURES_APPLICATION_PAR_MIN_APP_ORDO_ALL_REPORT_NAME								= "mesuresApplicaticationParMinistereAppOrdo";

	public static final String	AN_TAUX_DEBUT_LEGISLATURE_PAR_MIN_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_execution_lois_promulguees_par_ministere.rptdesign";
	public static final String	AN_TAUX_DEBUT_LEGISLATURE_PAR_MIN_REPORT_NAME										= "taux_execution_lois_promulguees_debut_legislature_par_ministere";

	public static final String	AN_TAUX_DEBUT_LEGISLATURE_PAR_MIN_APP_ORDO_REPORT_FILE								= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_execution_ordo_promulguees_par_ministere_app_ordo.rptdesign";
	public static final String	AN_TAUX_DEBUT_LEGISLATURE_PAR_MIN_APP_ORDO_REPORT_NAME								= "taux_execution_lois_promulguees_debut_legislature_par_ministere_app_ordo";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_MIN_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_par_ministere.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_PAR_MIN_REPORT_NAME											= "bilanSemestrielsParMinistere";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_MIN_APP_ORDO_REPORT_FILE									= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_par_ministere_app_ordo.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_PAR_MIN_APP_ORDO_REPORT_NAME									= "bilanSemestrielsParMinistereAppOrdo";

	public static final String	AN_TAUX_APPLICATION_PAR_MIN_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_ministere.rptdesign";
	public static final String	AN_TAUX_APPLICATION_PAR_MIN_REPORT_NAME												= "tauxDapplicationAuFilDeLeauParMinistere";

	public static final String	AN_TAUX_APPLICATION_PAR_MIN_APP_ORDO_REPORT_FILE									= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_ministere_app_ordo.rptdesign";
	public static final String	AN_TAUX_APPLICATION_PAR_MIN_APP_ORDO_REPORT_NAME									= "tauxDapplicationAuFilDeLeauParMinistereAppOrdo";

	public static final String	AN_TAUX_EXECUTION_LOIS_PROMULGUEES_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_execution_lois_promulguees.rptdesign";
	public static final String	AN_TAUX_EXECUTION_LOIS_PROMULGUEES_DEBUT_LEGISTRATURE_REPORT_NAME					= "taux_execution_lois_promulguees_debut_legislature";

	public static final String	AN_DELAIS_MISE_APPLICATION_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_delais_mise_application.rptdesign";
	public static final String	AN_DELAIS_MISE_APPLICATION_REPORT_NAME												= "delais_mise_application";

	public static final String	AN_TAUX_EXECUTION_LOIS_PROMULGUEES_DERNIERE_SESSION_PARLEMENTAIRE_REPORT_NAME		= "taux_execution_lois_promulguees_derniere_session_parlementaire";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_LOI_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_tous_loi.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_TOUS_LOI_REPORT_NAME											= "bilan_semestriels_loi_tous";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_ORDONNANCE_REPORT_FILE									= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_tous_ordonnance.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_TOUS_ORDONNANCE_REPORT_NAME									= "bilan_semestriels_ordonnance_tous";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_MINISTERE_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_tous_ministere.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_TOUS_MINISTERE_REPORT_NAME										= "bilan_semestriels_ministere_tous";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_MINISTERE_ORDONNANCE_REPORT_NAME							= "bilan_semestriels_ministere_tous_ordonnance";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_MINISTERE_ORDONNANCE_REPORT_FILE							= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_tous_ministere_ordonnance.rptdesign";					;

	public static final String	AN_BILAN_SEMESTRIELS_PAR_NATURE_TEXTE_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_par_categorie.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_PAR_NATURE_TEXTE_REPORT_NAME									= "bilan_semestriels_par_nature_texte";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_PROCEDURE_VOTE_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_bilan_semestriels_au_cours_legislature_par_procedure_vote.rptdesign";
	public static final String	AN_BILAN_SEMESTRIELS_PAR_PROCEDURE_VOTE_REPORT_NAME									= "bilan_semestriels_par_procedure_vote";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_GLOBAL_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_taux_global.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_GLOBAL_REPORT_NAME								= "taux_dapplication_fil_eau_taux_global";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_GLOBAL_APP_ORDONNANCE_REPORT_FILE					= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_taux_global_app_ordonnance.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_GLOBAL_APP_ORDONNANCE_REPORT_NAME					= "taux_dapplication_fil_eau_taux_global_app_ordonnance";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_tous_loi_affichage_initial.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_REPORT_NAME								= "taux_dapplication_fil_eau_taux_loi_tous";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_APP_ORDONNANCE_REPORT_FILE					= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_tous_ordo_affichage_initial_app_ordonnance.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_APP_ORDONNANCE_REPORT_NAME					= "taux_dapplication_fil_eau_taux_loi_tous_app_ordonnance";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_ALL_REPORT_FILE							= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_tous_loi.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_ALL_REPORT_NAME							= "taux_dapplication_fil_eau_taux_loi_tous_all";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_ORDO_APP_ORDO_ALL_REPORT_FILE				= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_tous_ordo_app_ordonnance.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_ORDO_APP_ORDO_ALL_REPORT_NAME				= "taux_dapplication_fil_eau_taux_loi_tous_app_ordonnance_all";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_MINISTERE_REPORT_FILE						= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_tous_ministere.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_MINISTERE_REPORT_NAME						= "taux_dapplication_fil_eau_taux_ministere_tous";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_MINISTERE_APP_ORDO_REPORT_FILE				= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_application_fil_de_leau_tous_ministere_app_ordo.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_MINISTERE_APP_ORDO_REPORT_NAME				= "taux_dapplication_fil_eau_taux_ministere_tous_app_ordo";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_NATURE_TEXTE_REPORT_FILE							= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_categorie.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_NATURE_TEXTE_REPORT_NAME							= "taux_dapplication_fil_eau_taux_nature_texte";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_PROCEDURE_VOTE_REPORT_FILE						= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_taux_application_fil_de_leau_par_procedure_vote.rptdesign";
	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_PROCEDURE_VOTE_REPORT_NAME						= "taux_dapplication_fil_eau_taux_procedure_vote";

	public static final String	AN_DELAI_LOI_REPORT_FILE															= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_delais_publication_tous_loi.rptdesign";
	public static final String	AN_DELAI_LOI_REPORT_NAME															= "delai_loi_tous";

	public static final String	AN_DELAI_ORDO_REPORT_FILE_APP_ORDO													= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_delais_publication_tous_ordo_app_ordo.rptdesign";
	public static final String	AN_DELAI_ORDO_REPORT_NAME_APP_ORDO													= "delai_ordo_tous_app_ordo";

	public static final String	AN_DELAI_MINISTERE_REPORT_FILE														= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_delais_publication_tous_ministere.rptdesign";
	public static final String	AN_DELAI_MINISTERE_REPORT_NAME														= "delai_ministere_tous";

	public static final String	AN_DELAI_MINISTERE_APP_ORDO_REPORT_FILE												= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_delais_publication_tous_ministere_app_ordo.rptdesign";
	public static final String	AN_DELAI_MINISTERE_APP_ORDO_REPORT_NAME												= "delai_ministere_tous_app_ordo";

	public static final String	AN_DELAI_NATURE_TEXTE_REPORT_FILE													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_delais_publication_tous_nature.rptdesign";
	public static final String	AN_DELAI_NATURE_TEXTE_REPORT_NAME													= "delai_par_nature_texte";

	public static final String	AN_COURBE_PUBLICATION_REPORT_FILE													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_mesures_publication_courbe.rptdesign";
	public static final String	AN_COURBE_PUBLICATION_REPORT_NAME													= "courbe_publication";

	public static final String	AN_COURBE_PUBLICATION_APP_ORDO_REPORT_FILE											= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_publication_courbe_app_ordo.rptdesign";
	public static final String	AN_COURBE_PUBLICATION_APP_ORDO_REPORT_NAME											= "courbe_publication_app_ordo";

	public static final String	AN_TRANSPOSITION_ACHEVEE_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_directives_transposition_achevee.rptdesign";
	public static final String	AN_TRANSPOSITION_ACHEVEE_REPORT_NAME												= "tableau_bord_transposition_achevee";

	public static final String	AN_TRANSPOSITION_MIN_ACHEVEE_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_directives_transposition_achevee_tous_ministere.rptdesign";
	public static final String	AN_TRANSPOSITION_MIN_ACHEVEE_REPORT_NAME											= "tableau_bord_transposition_min_achevee";

	public static final String	AN_TRANSPOSITION_ENCOURS_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_directives_transposition_en_cours.rptdesign";
	public static final String	AN_TRANSPOSITION_ENCOURS_REPORT_NAME												= "tableau_bord_transposition_encours";

	public static final String	AN_TRANSPOSITION_MIN_ENCOURS_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_directives_transposition_en_cours_tous_ministere.rptdesign";
	public static final String	AN_TRANSPOSITION_MIN_ENCOURS_REPORT_NAME											= "tableau_bord_transposition_min_encours";

	public static final String	AN_STAT_INDICATEUR_MIN_TRANS_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_indicateurs_ministeriels_transposition_directives.rptdesign";
	public static final String	AN_STAT_INDICATEUR_MIN_TRANS_REPORT_NAME											= "stat_indicateur_min_trans";

	public static final String	AN_STAT_RETARD_MOY_TRANS_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_retards_moyens_transposition.rptdesign";
	public static final String	AN_STAT_RETARD_MOY_TRANS_REPORT_NAME												= "stat_retard_moy_trans";

	public static final String	AN_STAT_REPARTITION_DIR_APRENDRE_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_textes_transposition_a_prendre.rptdesign";
	public static final String	AN_STAT_REPARTITION_DIR_APRENDRE_REPORT_NAME										= "stat_repartition_dir_aprendre";

	public static final String	AN_TRANSPOSITION_ACHEVEE_MIN_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_directives_transposition_achevee_par_ministere.rptdesign";
	public static final String	AN_TRANSPOSITION_ACHEVEE_MIN_REPORT_NAME											= "transposition_achevee_min";

	public static final String	AN_TRANSPOSITION_ENCOURS_MIN_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_europ_directives_transposition_en_cours_par_ministere.rptdesign";
	public static final String	AN_TRANSPOSITION_ENCOURS_MIN_REPORT_NAME											= "transposition_encours_min";

	public static final String	AN_RATIFICATION_741_MIN_REPORT_FILE													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_74_par_ministere_masquer.rptdesign";
	public static final String	AN_RATIFICATION_741_MIN_REPORT_NAME													= "tab_an_ratification_741_min";

	public static final String	AN_RATIFICATION_741_MIN_ALL_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_74_par_ministere_afficher.rptdesign";
	public static final String	AN_RATIFICATION_741_MIN_ALL_REPORT_NAME												= "tab_an_ratification_741_min_all";

	public static final String	AN_RATIFICATION_38C_MIN_REPORT_FILE													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_38_par_minitere_masquer.rptdesign";
	public static final String	AN_RATIFICATION_38C_MIN_REPORT_NAME													= "tab_an_ratification_38c_min";

	public static final String	AN_RATIFICATION_38C_MIN_ALL_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_38_par_ministere_afficher.rptdesign";
	public static final String	AN_RATIFICATION_38C_MIN_ALL_REPORT_NAME												= "tab_an_ratification_38c_min_all";

	public static final String	AN_HABILITATION_FLTR_MIN_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_avec_filtre_par_ministere_masquer.rptdesign";
	public static final String	AN_HABILITATION_FLTR_MIN_REPORT_NAME												= "tab_an_habilitation_fltr_min";

	public static final String	AN_HABILITATION_FLTR_MIN_ALL_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_avec_filtre_par_ministere_afficher.rptdesign";
	public static final String	AN_HABILITATION_FLTR_MIN_ALL_REPORT_NAME											= "tab_an_habilitation_fltr_min_all";

	public static final String	AN_HABILITATION_SS_FLTR_MIN_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_ss_filtre_par_ministere_masquer.rptdesign";
	public static final String	AN_HABILITATION_SS_FLTR_MIN_REPORT_NAME												= "tab_an_habilitation_ss_fltr_min";

	public static final String	AN_HABILITATION_SS_FLTR_MIN_ALL_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_ss_filtre_par_ministere_afficher.rptdesign";
	public static final String	AN_HABILITATION_SS_FLTR_MIN_ALL_REPORT_NAME											= "tab_an_habilitation_ss_fltr_min_all";

	public static final String	AN_TRAITE_PUB_AVENIR_MIN_REPORT_FILE												= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_traite_publication_a_intervenir_par_ministere.rptdesign";
	public static final String	AN_TRAITE_PUB_AVENIR_MIN_REPORT_NAME												= "an_traite_pub_avenir_min";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE_REPORT_NAME									= "an_tableau_bord_ratification_pas_depose";
	public static final String	AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_38_pas_depose.rptdesign";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_38C_REPORT_NAME										= "an_tableau_bord_ratification_38c";
	public static final String	AN_TABLEAU_BORD_RATIFICATION_38C_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_38_masquer.rptdesign";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_38C_ALL_REPORT_NAME									= "an_tableau_bord_ratification_38c_all";
	public static final String	AN_TABLEAU_BORD_RATIFICATION_38C_ALL_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_38_afficher.rptdesign";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_741_REPORT_NAME										= "an_tableau_bord_ratification_741";
	public static final String	AN_TABLEAU_BORD_RATIFICATION_741_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_74_masquer.rptdesign";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_741_ALL_REPORT_NAME									= "an_tableau_bord_ratification_741_all";
	public static final String	AN_TABLEAU_BORD_RATIFICATION_741_ALL_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_ordonnance_74_afficher.rptdesign";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_DECRET_REPORT_NAME										= "an_tableau_bord_ratification_decret";
	public static final String	AN_TABLEAU_BORD_RATIFICATION_DECRET_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_ordonnance_ratification.rptdesign";

	public static final String	AN_AN_TABLEAU_BORD_HABILITATION_FILTRE_REPORT_NAME									= "an_an_tableau_bord_habilitation_filtre";
	public static final String	AN_AN_TABLEAU_BORD_HABILITATION_FILTRE_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_avec_filtre_masquer.rptdesign";

	public static final String	AN_TABLEAU_BORD_HABILITATION_FILTRE_ALL_REPORT_NAME									= "an_tableau_bord_habilitation_filtre_all";
	public static final String	AN_TABLEAU_BORD_HABILITATION_FILTRE_ALL_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_avec_filtre_afficher.rptdesign";

	public static final String	AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_REPORT_NAME									= "an_tableau_bord_habilitation_ss_filtre";
	public static final String	AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_ss_filtre_masquer.rptdesign";

	public static final String	AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_ALL_REPORT_NAME								= "an_tableau_bord_habilitation_ss_filtre_all";
	public static final String	AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_ALL_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_habilitation_suivi_ss_filtre_afficher.rptdesign";

	public static final String	AN_TABLEAU_BORD_TRAITE_PUB_INTERVENUE_REPORT_NAME									= "an_tableau_bord_traite_pub_intervenue";
	public static final String	AN_TABLEAU_BORD_TRAITE_PUB_INTERVENUE_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_traite_publication_intervenue.rptdesign";

	public static final String	AN_TABLEAU_BORD_TRAITE_PUB_INTERVENIR_REPORT_NAME									= "an_tableau_bord_traite_pub_intervenir";
	public static final String	AN_TABLEAU_BORD_TRAITE_PUB_INTERVENIR_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_traite_publication_a_intervenir.rptdesign";

	public static final String	AN_STAT_TRAITE_ETUDES_IMPACT_REPORT_NAME											= "an_stat_traite_etudes_impact";
	public static final String	AN_STAT_TRAITE_ETUDES_IMPACT_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_traite_confection_etudes_impact.rptdesign";

	public static final String	AN_STAT_TRAITE_PUBLIER_REPORT_NAME													= "an_stat_traite_publier";
	public static final String	AN_STAT_TRAITE_PUBLIER_REPORT_FILE													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "r_an_traite_a_publier.rptdesign";

	public static final String	AN_APPLICATION_DE_LOIS_TEXT_MAITRE_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_application_texte_maitre_parloi.rptdesign";
	public static final String	AN_APPLICATION_DE_LOIS_TABLEAU_DE_PROGRAMMATION_REPORT_FILE							= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_application_tableau_programmation_parloi.rptdesign";
	public static final String	AN_APPLICATION_DE_LOIS_TABLEAU_SUIVI_AFFICHER_REPORT_FILE							= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_application_tableau_suivi_parloi_aficher.rptdesign";
	public static final String	AN_APPLICATION_DE_LOIS_TABLEAU_SUIVI_MASQUER_REPORT_FILE							= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_application_tableau_suivi_parloi_masquer.rptdesign";
	public static final String	AN_APPLICATION_DE_LOIS_TABLEAU_LOIS_REPORT_FILE										= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_application_texte_maitre_tousloi.rptdesign";

	public static final String	AN_HABILITATION_TABLEAU_DE_PROGRAMMATION_REPORT_FILE								= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_habilitations_tableau_programmation_parloi.rptdesign";
	public static final String	AN_HABILITATION_TABLEAU_SUIVI_AFFICHER_REPORT_FILE									= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_habilitations_tableau_suivi_parloi.rptdesign";

	public static final String	AN_TRAITE_TEXT_MAITRE_REPORT_FILE													= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "rapportTest.rptdesign";

	public static final String	AN_TRANSPOSITION_TEXT_MAITRE_REPORT_FILE											= BIRT_REPORT_ACTIVITE_NORMATIVE_FOLDER
																															+ "v_an_transposition_texte_maitre_parloi.rptdesign";
	public static final String	AN_MES_APP_DEP_NON_PUB_APP_ORDO_REPORT_FILE											= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_previsionnelle_depassee_decret_non_pub.rptdesign";
	public static final String	AN_MES_APP_DEP_NON_PUB_APP_ORDO_REPORT_NAME											= "mesuresApplicationAppOrdoDepassesNonPubliees";

	public static final String	AN_MES_APP_DEP_ALL_APP_ORDO_REPORT_FILE												= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_previsionnelle_depassee_tous_decret.rptdesign";
	public static final String	AN_MES_APP_DEP_ALL_APP_ORDO_REPORT_NAME												= "mesureApplicationAppOrdoDepasses";

	public static final String	AN_MES_APP_ACTIVES_NON_PUB_APP_ORDO_REPORT_NAME										= "mesuresApplicaticationActiveNonPublieesAppOrdo";
	public static final String	AN_MES_APP_ACTIVES_NON_PUB_APP_ORDO_REPORT_FILE										= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_actives_decret_non_pub.rptdesign";

	public static final String	AN_MES_APP_ACTIVES_ALL_APP_ORDO_REPORT_NAME											= "mesureApplicaticationAppOrdoActive";
	public static final String	AN_MES_APP_ACTIVES_ALL_APP_ORDO_REPORT_FILE											= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_actives_tous_decret.rptdesign";

	public static final String	AN_MES_APP_DIFFEREE_APP_ORDO_REPORT_NAME											= "mesureApplicaticationDiffereeAppOrdo";
	public static final String	AN_MES_APP_DIFFEREE_APP_ORDO_REPORT_FILE											= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_mesures_application_differee.rptdesign";
	
	public static final String	AN_TAUX_EXECUTION_ORDOS_PROMULGUEES_DEBUT_LEGISLATURE_REPORT_NAME					= "taux_execution_ordonnances_promulguees_debut_legislature";

	public static final String	AN_DELAIS_MISE_APPLI_APP_ORDO_REPORT_NAME											= "delais_mise_application_app_ordo";
	public static final String	AN_DELAIS_MISE_APPLI_APP_ORDO_REPORT_FILE											= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_delais_mise_application.rptdesign";

	public static final String	AN_TAUX_EXECUTION_ORDOS_PROMULGUEES_REPORT_FILE										= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "r_an_taux_execution_lois_promulguees.rptdesign";
	public static final String	AN_TAUX_EXECUTION_ORDOS_PROMULGUEES_DERNIERE_SESSION_PARLEMENTAIRE_REPORT_NAME		= "taux_execution_ordonnances_promulguees_derniere_session";
	
	public static final String	AN_APPLICATION_DES_ORDOS_TABLEAU_DES_ORDOS_REPORT_FILE								= BIRT_REPORT_APP_ORDONNANCES_FOLDER 
																															+ "v_an_application_texte_maitre_tousordo.rptdesign";
	
	public static final String	AN_APPLICATION_DES_ORDOS_TEXTE_MAITRE_REPORT_FILE									= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "v_an_application_texte_maitre_parloi.rptdesign";
	
	public static final String	AN_APPLICATION_DES_ORDOS_TABLEAU_SUIVI_AFFICHER_REPORT_FILE							= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "v_an_application_tableau_suivi_parloi_aficher.rptdesign";
	
	public static final String	AN_APPLICATION_DES_ORDOS_TABLEAU_SUIVI_MASQUER_REPORT_FILE							= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "v_an_application_tableau_suivi_parloi_masquer.rptdesign";
	
	public static final String	AN_APPLICATION_DES_ORDOS_TABLEAU_DE_PROGRAMMATION_REPORT_FILE						= BIRT_REPORT_APP_ORDONNANCES_FOLDER
																															+ "v_an_application_tableau_programmation_parordo.rptdesign";

	public static final String	AN_THEME_APPLICATION_DES_LOIS														= "Application_lois";
	public static final String	AN_THEME_TRANSPOSITION_DES_DIRECTIVES												= "Transposition_directives";
	public static final String	AN_THEME_SUIVI_DES_HABILITATIONS													= "Suivi_habilitations";
	public static final String	AN_THEME_RATIFICATION_DES_ORDONNANCES												= "Ratification_ordonnances";
	public static final String	AN_THEME_TRAITES_ET_ACCORD															= "Traites_accords";
	public static final String	AN_THEME_APPLICATION_DES_ORDONNANCES												= "Application_ordonnances";

	public static final String	AN_CAT_TABLEAU_MAITRE																= "Tableau-maître";
	public static final String	AN_CAT_TABLEAU_DE_BORD																= "Tableau de bord";
	public static final String	AN_CAT_INDICATEUR_LOLF																= "Indicateurs LOLF";
	public static final String	AN_CAT_INDICATEURS																	= "Indicateurs";
	public static final String	AN_CAT_BILAN_SEMESTRIEL																= "Bilan semestriel";
	public static final String	AN_CAT_DELAI_MOYEN																	= "Délais moyens";
	public static final String	AN_CAT_TABLEAU_DES_LOIS																= "Tableau des lois";
	public static final String	AN_CAT_TABLEAU_DES_ORDOS															= "Tableau des ordonnances";
	public static final String	AN_CAT_STATISTIQUES																	= "Statistiques";
	public static final String	AN_CAT_TAUX_APPLICATION_AU_FIL_DE_LEAU												= "Taux d'application au fil de l'eau";

	/* ******************************************************************************************************
	 * LIBELLES DES STATISTIQUES
	 * ******************************************************************************************************
	 */
	// (les libellés étant identiques au nom de fichier n'ont pas été trouvés dans l'application ;
	// dans un souci de bon fonctionnement, ils ont été gardés, mais
	// les fichiers et libellés n'ont pas forcément lieu d'être)
	public static final String	AN_MESURES_APPLICATION_ACTIVES_NON_PUBLIEES_REPORT_LIBELLE							= "Mesures législatives appelant un décret et non appliquées";

	public static final String	AN_MESURES_APPLICATION_ACTIVES_ALL_REPORT_LIBELLE									= "Mesures législatives appelant un décret";

	public static final String	AN_MESURES_APPLICATION_PAR_LOI_NON_PUBLIEES_REPORT_LIBELLE							= "Mesures d'application de la loi non appliquées";

	public static final String	AN_MESURES_APPLICATION_PAR_ORDO_APP_ORDO_NON_PUBLIEES_REPORT_LIBELLE				= "Mesures d'application de l'ordonnance non appliquées";

	public static final String	AN_MESURES_APPLICATION_DIFFEREE_REPORT_LIBELLE										= "Mesures avec entrée en vigueur différée";

	public static final String	AN_MESURES_APPLICATION_PAR_LOI_ALL_REPORT_LIBELLE									= "Mesures d'application de la loi";

	public static final String	AN_MESURES_APPLICATION_PAR_ORDO_APP_ORDO_ALL_REPORT_LIBELLE							= "Mesures d'application de l'ordonnance";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_LOI_REPORT_LIBELLE											= "Bilan semestriel de la loi";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_ORDO_APP_ORDO_REPORT_LIBELLE								= "Bilan semestriel de l'ordonnance";

	public static final String	AN_TAUX_APPLICATION_PAR_LOI_REPORT_LIBELLE											= "tauxDapplicationAuFilDeLeauParLoiAffichageInitiale";

	public static final String	AN_TAUX_APPLICATION_PAR_ORDO_REPORT_LIBELLE											= "tauxDapplicationAuFilDeLeauParOrdonnanceAffichageInitiale";

	public static final String	AN_TAUX_APPLICATION_PAR_LOI_ALL_REPORT_LIBELLE										= "Taux d'application au fil de l'eau de la loi";

	public static final String	AN_TAUX_APPLICATION_PAR_ORDO_ALL_REPORT_LIBELLE										= "Taux d'application au fil de l'eau de l'ordonnance";

	public static final String	AN_MESURES_APPLICATION_PAR_MIN_NON_PUBLIEES_REPORT_LIBELLE							= "Mesures d'application du ministère, non appliquées";

	public static final String	AN_MESURES_APPLICATION_PAR_MIN_ALL_REPORT_LIBELLE									= "Mesures d'application du ministère";

	public static final String	AN_TAUX_DEBUT_LEGISLATURE_PAR_MIN_REPORT_LIBELLE									= "Taux d'exécution des lois promulguées depuis le début de la législature du ministère";

	public static final String	AN_TAUX_DEBUT_LEGISLATURE_PAR_MIN_APP_ORDO_REPORT_LIBELLE							= "Taux d'exécution des ordonnances depuis le début de la législature du ministère";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_MIN_REPORT_LIBELLE											= "Bilan semestriel du ministère";

	public static final String	AN_TAUX_APPLICATION_PAR_MIN_REPORT_LIBELLE											= "Taux d'application au fil de l'eau du ministère";

	public static final String	AN_MESURES_APPLICATION_DEPASSES_NON_PUBLIEES_REPORT_LIBELLE							= "Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée, non appliquées";

	public static final String	AN_MESURES_APPLICATION_DEPASSES_ALL_REPORT_LIBELLE									= "Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée";

	public static final String	AN_DELAIS_MISE_APPLICATION_REPORT_LIBELLE											= "Délai de mise en application des lois";

	public static final String	AN_TAUX_EXECUTION_LOIS_PROMULGUEES_DEBUT_LEGISLATURE_REPORT_LIBELLE					= "Taux d'exécution des lois promulguées depuis le début de la législature";
	
	public static final String	AN_TAUX_EXECUTION_ORDOS_PROMULGUEES_DEBUT_LEGISLATURE_REPORT_LIBELLE				= "Taux d'exécution des ordonnances publiées depuis le début de la législature";

	public static final String	AN_TAUX_EXECUTION_LOIS_PROMULGUEES_DERNIERE_SESSION_PARLEMENTAIRE_REPORT_LIBELLE	= "Taux d'exécution des lois promulguées au cours de la dernière session parlementaire";

	public static final String	AN_TAUX_EXECUTION_ORDOS_PROMULGUEES_DERNIERE_SESSION_PARLEMENTAIRE_REPORT_LIBELLE	= "Taux d'exécution des ordonnances publiées au cours de la dernière session parlementaire";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_REPORT_LIBELLE							= "taux_dapplication_fil_eau_taux_loi_tous";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_REPORT_LIBELLE_APP_ORDONNANCE				= "taux_dapplication_fil_eau_taux_ordo_tous";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_LOI_REPORT_LIBELLE										= "Bilan semestriel par loi";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_ORDONNANCE_REPORT_LIBELLE									= "Bilan semestriel par ordonnance";

	public static final String	AN_BILAN_SEMESTRIELS_TOUS_MINISTERE_REPORT_LIBELLE									= "Bilan semestriel par ministère";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_NATURE_TEXTE_REPORT_LIBELLE								= "Bilan semestriel par nature de texte";

	public static final String	AN_BILAN_SEMESTRIELS_PAR_PROCEDURE_VOTE_REPORT_LIBELLE								= "Bilan semestriel par procédure de vote";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_GLOBAL_REPORT_LIBELLE								= "Taux d'application au fil de l'eau global";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_ALL_REPORT_LIBELLE						= "Taux d'application au fil de l'eau par loi";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_LOI_ORDO_APP_ORDO_REPORT_LIBELLE				= "Taux d'application au fil de l'eau par ordonnance";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_TOUS_MINISTERE_REPORT_LIBELLE						= "Taux d'application au fil de l'eau par ministère";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_NATURE_TEXTE_REPORT_LIBELLE						= "Taux d'application au fil de l'eau par nature de texte";

	public static final String	AN_TAUX_DAPPLICATION_FIL_EAU_TAUX_PROCEDURE_VOTE_REPORT_LIBELLE						= "Taux d'application au fil de l'eau par procédure de vote";

	public static final String	AN_DELAI_LOI_REPORT_LIBELLE															= "Délais moyens par loi";

	public static final String	AN_DELAI_ORDO_APP_ORDO_REPORT_LIBELLE												= "Délais moyens par ordonnance";

	public static final String	AN_DELAI_MINISTERE_REPORT_LIBELLE													= "Délais moyens par ministère";

	public static final String	AN_DELAI_NATURE_TEXTE_REPORT_LIBELLE												= "Délais moyens par nature de texte";

	public static final String	AN_COURBE_PUBLICATION_REPORT_LIBELLE												= "Statistiques sur la publication des décrets d'application";

	public static final String	AN_TRANSPOSITION_ACHEVEE_REPORT_LIBELLE												= "Directives dont la transposition est achevée";

	public static final String	AN_TRANSPOSITION_MIN_ACHEVEE_REPORT_LIBELLE											= "tableau_bord_transposition_min_achevee";

	public static final String	AN_TRANSPOSITION_ENCOURS_REPORT_LIBELLE												= "Directives dont la transposition reste en cours";

	public static final String	AN_TRANSPOSITION_MIN_ENCOURS_REPORT_LIBELLE											= "tableau_bord_transposition_min_encours";

	public static final String	AN_STAT_INDICATEUR_MIN_TRANS_REPORT_LIBELLE											= "Indicateurs de transposition des directives";

	public static final String	AN_STAT_RETARD_MOY_TRANS_REPORT_LIBELLE												= "Tableau des retards moyens de transposition";

	public static final String	AN_STAT_REPARTITION_DIR_APRENDRE_REPORT_LIBELLE										= "Répartition par ministère des textes de transposition restant à prendre";

	public static final String	AN_TRANSPOSITION_ACHEVEE_MIN_REPORT_LIBELLE											= "Directives dont la transposition est achevée par le ministère";

	public static final String	AN_TRANSPOSITION_ENCOURS_MIN_REPORT_LIBELLE											= "Suivi des travaux de transposition à la charge du ministère";

	public static final String	AN_RATIFICATION_741_MIN_REPORT_LIBELLE												= "Suivi de la ratification des ordonnances de l'article 74-1, non aboutie, du ministère";

	public static final String	AN_RATIFICATION_741_MIN_ALL_REPORT_LIBELLE											= "Suivi de la ratification des ordonnances de l'article 74-1 du ministère";

	public static final String	AN_RATIFICATION_38C_MIN_REPORT_LIBELLE												= "Suivi de la ratification des ordonnances de l'article 38 C, non aboutie, du ministère";

	public static final String	AN_RATIFICATION_38C_MIN_ALL_REPORT_LIBELLE											= "Suivi de la ratification des ordonnances de l'article 38 C du ministère";

	public static final String	AN_HABILITATION_FLTR_MIN_REPORT_LIBELLE												= "Suivi des habilitations non utilisées, de la législature en cours, du ministère";

	public static final String	AN_HABILITATION_FLTR_MIN_ALL_REPORT_LIBELLE											= "Suivi des habilitations de la législature en cours, du ministère";

	public static final String	AN_HABILITATION_SS_FLTR_MIN_REPORT_LIBELLE											= "Suivi des habilitations non utilisées du ministère";

	public static final String	AN_HABILITATION_SS_FLTR_MIN_ALL_REPORT_LIBELLE										= "Suivi des habilitations du ministère";

	public static final String	AN_TRAITE_PUB_AVENIR_MIN_REPORT_LIBELLE												= "Tableaux ministériels des traités et accord dont la publication reste à intervenir du ministère";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE_REPORT_LIBELLE								= "Ordonnances de l'article 38 C dont le projet de loi de ratification n'a pas été déposé";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_38C_REPORT_LIBELLE										= "Suivi global de la ratification des ordonnances de l'article 38 C, non aboutie";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_38C_ALL_REPORT_LIBELLE									= "Suivi global de la ratification des ordonnances de l'article 38 C";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_741_REPORT_LIBELLE										= "Suivi global de la ratification des ordonnances de l'article 74-1, non aboutie";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_741_ALL_REPORT_LIBELLE									= "Suivi global de la ratification des ordonnances de l'article 74-1";

	public static final String	AN_TABLEAU_BORD_RATIFICATION_DECRET_REPORT_LIBELLE									= "Suivi des décrets pris en application des ordonnances";

	public static final String	AN_AN_TABLEAU_BORD_HABILITATION_FILTRE_REPORT_LIBELLE								= "an_an_tableau_bord_habilitation_filtre";

	public static final String	AN_TABLEAU_BORD_HABILITATION_FILTRE_ALL_REPORT_LIBELLE								= "an_tableau_bord_habilitation_filtre_all";

	public static final String	AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_REPORT_LIBELLE								= "Suivi global des habilitations non utilisées";

	public static final String	AN_TABLEAU_BORD_HABILITATION_SS_FILTRE_ALL_REPORT_LIBELLE							= "Suivi global des habilitations";

	public static final String	AN_TABLEAU_BORD_TRAITE_PUB_INTERVENUE_REPORT_LIBELLE								= "Tableau général des traités et accords dont la publication est intervenue";

	public static final String	AN_TABLEAU_BORD_TRAITE_PUB_INTERVENIR_REPORT_LIBELLE								= "Tableau général des traités et accords dont la publication reste à intervenir";

	public static final String	AN_STAT_TRAITE_ETUDES_IMPACT_REPORT_LIBELLE											= "Indicateurs ministériels de confection des études d'impact";

	public static final String	AN_STAT_TRAITE_PUBLIER_REPORT_LIBELLE												= "Répartition par ministères des traités et accords signés et restant à publier";

	public static final String	EXPORT_PAN_STAT_SCHEMA																= "export_pan_stat";

	public static final String	EXPORT_PAN_STAT_ROOT_TYPE															= "ExportPANStatRoot";

	public static final String	EXPORT_PAN_STAT_PREFIX																= "expanstat";

	public static final String	EXPORT_PAN_STAT_OWNER_PROPERTY														= "owner";

	public static final String	EXPORT_PAN_STAT_DATE_PROPERTY														= "dateRequest";

	public static final String	EXPORT_PAN_STAT_TYPE																= "ExportPANStat";

	public static final String	EXPORT_PAN_STAT_CONTENT_PROPERTY													= "content";

	public static final String	EXPORT_PAN_STAT_LEGISLATURE_PROPERTY												= "legislatures";

	private ActiviteNormativeStatsConstants() {
		// Private default constructor
	}
}
