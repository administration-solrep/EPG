/**
 *
 */
package fr.dila.solonepg.api.constant;

import fr.dila.st.api.constant.STConstant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constantes liés à l'écran de paramètrage de l'application.
 *
 * @author antoine Rolin
 *
 */
public final class SolonEpgParametrageAdamantConstants {
    //*************************************************************
    // Paramètrage evenement
    //*************************************************************

    /**
     * Nom de l'événement qui signale que les paramètrages d'Adamant ont changé
     */
    public static final String PARAMETRAGE_ADAMANT_CHANGED_EVENT = "ParametrageAdamantChangedEvent";

    //*************************************************************
    // Paramètrage application
    //*************************************************************

    /**
     * nom du document parametrage adamant
     */
    public static final String PARAMETRAGE_ADAMANT_DOCUMENT_NAME = "parametrage-adamant";

    /**
     * schema parametrage adamant
     */
    public static final String PARAMETRAGE_ADAMANT_SCHEMA = "parametrage_adamant";
    /**
     * type de document parametrage adamant
     */
    public static final String PARAMETRAGE_ADAMANT_DOCUMENT_TYPE = "ParametrageAdamant";

    /**
     *  parametrage adamant préfixe
     */
    public static final String PARAMETRAGE_ADAMANT_PREFIX = "pad";

    /**
     * Root path pour le paramètre adamant
     */
    public static final String PARAMETRE_ROOT_PATH = String.join(
        "/",
        STConstant.CASE_MANAGEMENT_PATH,
        "workspaces/admin/"
    );

    /**
     * Colonnes par défault disponible pour les utilisateurs dans la corbeille de l'espace de traitement.
     */
    public static final List<String> PARAMETRAGE_ADAMANT_DEFAULT_ESPACE_TRAITEMENT_COLUMN_LIST = Collections.unmodifiableList(
        Arrays.asList(
            new String[] {
                SolonEpgProfilUtilisateurConstants.NUMERO_NOR_PROPERTY,
                SolonEpgProfilUtilisateurConstants.TITRE_ACTE_PROPERTY,
                SolonEpgProfilUtilisateurConstants.DERNIER_CONTRIBUTEUR_PROPERTY,
                SolonEpgProfilUtilisateurConstants.DATE_ARRIVEE_DOSSIER_LINK_PROPERTY
            }
        )
    );

    /*
     * Constante de création de paramètre Adamant
     */
    public static final String PARAMETRE_ADAMANT_NAME_TITLE = "Paramètre Adamant";
    public static final String PARAMETRE_ADAMANT_NAME_DESCRIPTION = "Paramètre pour l'archivage Adamant";

    //*************************************************************
    // Paramètrage éléments du manifest
    //*************************************************************

    public static final String PARAMETRAGE_ADAMANT_NUMERO_SOLON_PROPERTY = "numeroSolon";

    public static final String PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_BLOC_IDENTIFIER_PROPERTY =
        "originAgencyBlocIdentifier";

    public static final String PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_BLOC_IDENTIFIER_PROPERTY =
        "submisAgencyBlocIdentifier";

    public static final String PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_PROPERTY = "archivalProfile";

    public static final String PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_SPECIFIC_PROPERTY = "archivalProfileSpecific";

    public static final String PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_IDENTIFIER_PROPERTY =
        "originatingAgencyIdentifier";

    public static final String PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_IDENTIFIER_PROPERTY = "submissionAgencyIdentifier";

    //*************************************************************
    // Paramètrage de l'extraction
    //*************************************************************

    public static final String PARAMETRAGE_ADAMANT_DELAI_ELIGIBILITE_PROPERTY = "delaiEligibilite";

    public static final String PARAMETRAGE_ADAMANT_NB_DOSSIER_EXTRACTION_PROPERTY = "nbDossierExtraction";

    public static final String PARAMETRAGE_ADAMANT_VECTEUR_PUBLICATION_PROPERTY = "vecteurPublication";

    public static final String PARAMETRAGE_ADAMANT_TYPE_ACTE_PROPERTY = "typeActe";

    //*************************************************************
    // xpath
    //*************************************************************

    public static final String PARAMETRAGE_ADAMANT_XPATH_NUMERO_SOLON =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_NUMERO_SOLON_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_ORIGINATING_AGENCY_IDENTIFIER =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_IDENTIFIER_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_SUBMISSION_AGENCY_IDENTIFIER =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_IDENTIFIER_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_ARCHIVAL_PROFILE =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_ARCHIVAL_PROFILE_SPECIFIC =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_SPECIFIC_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_ORIGINATING_AGENCY_BLOC_IDENTIFIER =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_BLOC_IDENTIFIER_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_SUBMISSION_AGENCY_BLOC_IDENTIFIER =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_BLOC_IDENTIFIER_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_DELAI_ELIGIBILITE =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_DELAI_ELIGIBILITE_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_NB_DOSSIER =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_NB_DOSSIER_EXTRACTION_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_VECTEUR_PUBLICATION =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_VECTEUR_PUBLICATION_PROPERTY;

    public static final String PARAMETRAGE_ADAMANT_XPATH_TYPE_ACTE =
        PARAMETRAGE_ADAMANT_PREFIX + ":" + PARAMETRAGE_ADAMANT_TYPE_ACTE_PROPERTY;

    // Constant utility class
    private SolonEpgParametrageAdamantConstants() {}
}
