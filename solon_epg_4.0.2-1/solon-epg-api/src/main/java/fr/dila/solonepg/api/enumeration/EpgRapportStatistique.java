package fr.dila.solonepg.api.enumeration;

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
    LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION("liste_textes_en_cours_communication_validation"),
    LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG("liste_originaux_textes_arrives_sgg"),
    LISTE_TEXTES_SIGNATURE_PREMIER_MINISTRE_PRESIDENT("liste_textes_signature_premier_ministre_president"),
    LISTE_TEXTES_CHEMINANT_DEMATERIALISEE("liste_textes_cheminant_dematerialisee"),
    LISTE_TEXTES_AMPLIATIONS("liste_textes_ampliations"),
    TEXTES_CORBEILLES_TRAVAILLES("textes_corbeilles_travailles"),
    LISTE_EPREUVES_PAR_VECT_PUBLI("liste_epreuves_vect_publi"),
    // DELAIS_MOYENS_TRAITEMENT_ACTE_TYPE_ACTION,
    NOMBRE_ACTES_TYPE("nombre_actes_type"),
    NOMBRE_ACTES_CREES_MINISTERE("nombre_actes_crees_ministere"),
    NOMBRE_ACTES_CREES_DIRECTION("nombre_actes_crees_direction"),
    NOMBRE_ACTES_CREES_POSTE("nombre_actes_crees_poste"),
    // DELAIS_MOYENS_TRAITEMENT_ACTE_TYPE,
    TAUX_TEXTES_RETOUR_SGG("taux_textes_retour_sgg"),
    TAUX_TEXTES_RETOUR_BUREAU("taux_textes_retour_bureau"),
    TAUX_INDEXATION_TEXTE("taux_indexation_texte"),
    LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO("liste_dossiers_transmis_dila_pour_jo_bo"),
    LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC("liste_dossiers_transmis_dila_pour_epr_vec"),
    DOSSIER_ARCHIVAGE("dossier_archivage");

    private final String id;
    private final String bundleKey;

    EpgRapportStatistique(String id) {
        this.id = id;
        this.bundleKey = "espace.suivi.statistique." + id;
    }

    public String getId() {
        return id;
    }

    public String getBundleKey() {
        return bundleKey;
    }

    public static EpgRapportStatistique fromInt(int n) {
        return values()[n];
    }
}
