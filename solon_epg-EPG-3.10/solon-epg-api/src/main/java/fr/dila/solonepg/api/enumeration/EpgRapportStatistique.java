package fr.dila.solonepg.api.enumeration;

import fr.dila.solonepg.api.constant.SolonEpgStatistiquesConstant;

/**
 * Enum rassemblant les informations liés à un rapport de statistique.
 * <ol>
 * <li>clé de bundle <b>(sera préfixée par "espace.suivi.statistique.")</b> du titre du rapport</li>
 * <li>nom du rapport Birt</li>
 * </ol>
 * 
 * @author eboussaton
 * 
 */
public enum EpgRapportStatistique {

	NOMBRE_UTILISATEUR_CONNECTES("nombre_utilisateur_connectes", "r_stats_nombre_utilisateurs_connectes.rptdesign"), NOMS_UTILISATEUR_CONNECTES(
			"noms_utilisateur_connectes", "r_stats_noms_utilisateurs_connectes.rptdesign"),
	// LISTE_UTILISATEURS_ACTIFS,
	// LISTE_UTILISATEURS_NON_ACTIFS,
	LISTE_UTILISATEURS_PAS_CONNECTES_DUREE("liste_utilisateurs_pas_connectes_duree",
			"r_stats_noms_utilisateurs_actifs.rptdesign"), LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION(
			"liste_textes_en_cours_communication_validation", "r_stats_liste_texte_com.rptdesign"), LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG(
			"liste_originaux_textes_arrives_sgg", "r_stats_liste_texte_arrive_sgg.rptdesign"), LISTE_TEXTES_SIGNATURE_PREMIER_MINISTRE_PRESIDENT(
			"liste_textes_signature_premier_ministre_president", "r_stats_liste_texte_sig_pm_pr.rptdesign"), LISTE_TEXTES_CHEMINANT_DEMATERIALISEE(
			"liste_textes_cheminant_dematerialisee", "r_stats_liste_texte_dila_rig.rptdesign"), LISTE_TEXTES_AMPLIATIONS(
			"liste_textes_ampliations", "r_stats_liste_texte_ampliation.rptdesign"), TEXTES_CORBEILLES_TRAVAILLES(
			"textes_corbeilles_travailles", "r_stats_liste_texte_corbeille.rptdesign"), LISTE_EPREUVES_PAR_VECT_PUBLI(
			"liste_epreuves_vect_publi", "r_stats_liste_texte_epreuve.rptdesign"),
	// DELAIS_MOYENS_TRAITEMENT_ACTE_TYPE_ACTION,
	NOMBRE_ACTES_TYPE("nombre_actes_type", "r_stats_nombre_acte_par_type.rptdesign"), NOMBRE_ACTES_CREES_MINISTERE(
			"nombre_actes_crees_ministere", "r_stats_nbr_acte_type_periode.rptdesign"), NOMBRE_ACTES_CREES_DIRECTION(
			"nombre_actes_crees_direction", "r_stats_nbr_acte_type_periode.rptdesign"), NOMBRE_ACTES_CREES_POSTE(
			"nombre_actes_crees_poste", "r_stats_nbr_acte_type_periode.rptdesign"),
	// DELAIS_MOYENS_TRAITEMENT_ACTE_TYPE,
	TAUX_TEXTES_RETOUR_SGG("taux_textes_retour_sgg", "r_stats_taux_retour_textes_sgg.rptdesign"), TAUX_TEXTES_RETOUR_BUREAU(
			"taux_textes_retour_bureau", "r_stats_taux_retour_textes_bdc.rptdesign"), TAUX_INDEXATION_TEXTE(
			"taux_indexation_texte", "r_stats_taux_indexation_texte.rptdesign"), LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO(
			"liste_dossiers_transmis_dila_pour_jo_bo", "r_stats_liste_texte_dila_jo_bo.rptdesign"), LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC(
			"liste_dossiers_transmis_dila_pour_epr_vec", "r_stats_liste_texte_dila_epr_vec.rptdesign"),
	DOSSIER_ARCHIVAGE("dossier_archivage", "r.stats_list_dossiers_archivage");

	public final String	bundleKey;
	public final String	birtReport;

	private EpgRapportStatistique(String bundleKey, String birtReport) {
		this.bundleKey = "espace.suivi.statistique." + bundleKey;
		this.birtReport = SolonEpgStatistiquesConstant.BIRT_REPORT_STATISTIQUE_FOLDER + birtReport;
	}

	public static EpgRapportStatistique fromInt(int n) {
		return values()[n];
	}
}
