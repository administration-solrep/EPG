/**
 *
 */
package fr.dila.solonepg.api.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constantes liés à l'écran de paramètrage de l'application.
 *
 * @author antoine Rolin
 *
 */
public final class SolonEpgParametrageApplicationConstants {
    //*************************************************************
    // Paramètrage evenement
    //*************************************************************

    /**
     * Nom de l'événement qui signale que les paramètrages de l'application ont changé
     */
    public static final String PARAMETRAGE_APPLICATION_CHANGED_EVENT = "ParametrageApplicationChangedEvent";

    //*************************************************************
    // Paramètrage application
    //*************************************************************

    /**
     * nom du document parametrage application
     */
    public static final String PARAMETRAGE_APPLICATION_DOCUMENT_NAME = "parametrage-application";

    /**
     * schema parametrage application
     */
    public static final String PARAMETRAGE_APPLICATION_SCHEMA = "parametrage_application";

    /**
     * type de document parametrage application
     */
    public static final String PARAMETRAGE_APPLICATION_DOCUMENT_TYPE = "ParametrageApplication";

    /**
     *  parametrage application préfixe
     */
    public static final String PARAMETRAGE_APPLICATION_PREFIX = "pa";

    /**
     * Colonnes par défault disponible pour les utilisateurs dans la corbeille de l'espace de traitement.
     */
    public static final List<String> PARAMETRAGE_APPLICATION_DEFAULT_ESPACE_TRAITEMENT_COLUMN_LIST = Collections.unmodifiableList(
        Arrays.asList(
            new String[] {
                SolonEpgProfilUtilisateurConstants.NUMERO_NOR_PROPERTY,
                SolonEpgProfilUtilisateurConstants.TITRE_ACTE_PROPERTY,
                SolonEpgProfilUtilisateurConstants.DERNIER_CONTRIBUTEUR_PROPERTY,
                SolonEpgProfilUtilisateurConstants.DATE_ARRIVEE_DOSSIER_LINK_PROPERTY
            }
        )
    );

    //*************************************************************
    // Paramètrage corbeille property
    //*************************************************************

    public static final String PARAMETRAGE_APPLICATION_NB_DOSSIER_PAGE_PROPERTY = "nbDossierPage";

    public static final String PARAMETRAGE_APPLICATION_DELAI_RAFRAICHISSEMENT_CORBEILLE_PROPERTY =
        "delaiRafraichissementCorbeille";

    public static final String PARAMETRAGE_APPLICATION_META_DISPO_CORBEILLE_PROPERTY = "metaDispoCorbeille";

    //*************************************************************
    // Paramètrage recherche property
    //*************************************************************

    public static final String PARAMETRAGE_APPLICATION_NB_DERNIERS_RESULTATS_PROPERTY = "nbDerniersResultats";

    public static final String PARAMETRAGE_APPLICATION_NB_FAVORIS_CONSULTATION_PROPERTY = "nbFavorisConsultation";

    public static final String PARAMETRAGE_APPLICATION_NB_FAVORIS_RECHERCHE_PROPERTY = "nbFavorisRecherche";

    //*************************************************************
    // Paramètrage : suivi property
    //*************************************************************

    public static final String PARAMETRAGE_APPLICATION_NB_DOSSIERS_SIGNALES_PROPERTY = "nbDossiersSignales";

    public static final String PARAMETRAGE_APPLICATION_NB_TABLEAUX_DYNAMIQUES_PROPERTY = "nbTableauxDynamiques";

    public static final String PARAMETRAGE_APPLICATION_DELAI_ENVOI_MAIL_ALERTE_PROPERTY = "delaiEnvoiMailAlerte";

    public static final String PARAMETRAGE_APPLICATION_DELAI_AFFICHAGE_MESSAGE_PROPERTY = "delaiAffichageMessage";

    //*************************************************************
    // Paramètrage : suivi property
    //*************************************************************

    public static final String PARAMETRAGE_APPLICATION_SUFF_MAILS_AUTORISES_PROPERTY = "suffixesMailsAutorises";

    // *************************************************************
    // Paramètrage : suivi property
    // *************************************************************

    public static final String PARAMETRAGE_APPLICATION_URL_EPP_INFOS_PARL_PROPERTY = "urlEppInfosParl";

    public static final String PARAMETRAGE_APPLICATION_LOGIN_EPP_INFOS_PARL_PROPERTY = "loginEppInfosParl";

    public static final String PARAMETRAGE_APPLICATION_PASS_EPP_INFOS_PARL_PROPERTY = "passEppInfosParl";

    // *************************************************************
    // xpath
    // *************************************************************
    public static final String PARAMETRAGE_APPLICATION_XPATH_NB_DOSSIER_PAGE =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_NB_DOSSIER_PAGE_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_DELAI_RAFRAICHISSEMENT_CORBEILLE =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_DELAI_RAFRAICHISSEMENT_CORBEILLE_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_META_DISPO_CORBEILLE =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_META_DISPO_CORBEILLE_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_NB_DERNIERS_RESULTATS =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_NB_DERNIERS_RESULTATS_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_NB_FAVORIS_CONSULTATION =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_NB_FAVORIS_CONSULTATION_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_NB_FAVORIS_RECHERCHE =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_NB_FAVORIS_RECHERCHE_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_NB_DOSSIERS_SIGNALES =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_NB_DOSSIERS_SIGNALES_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_NB_TABLEAUX_DYNAMIQUES =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_NB_TABLEAUX_DYNAMIQUES_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_DELAI_ENVOI_MAIL_ALERTE =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_DELAI_ENVOI_MAIL_ALERTE_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_DELAI_AFFICHAGE_MESSAGE =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_DELAI_AFFICHAGE_MESSAGE_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_SUFF_MAILS_AUTORISES =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_SUFF_MAILS_AUTORISES_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_URL_EPP_INFOS_PARL =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_URL_EPP_INFOS_PARL_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_LOGIN_EPP_INFOS_PARL =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_LOGIN_EPP_INFOS_PARL_PROPERTY;

    public static final String PARAMETRAGE_APPLICATION_XPATH_PASS_EPP_INFOS_PARL =
        PARAMETRAGE_APPLICATION_PREFIX + ":" + PARAMETRAGE_APPLICATION_PASS_EPP_INFOS_PARL_PROPERTY;

    // Constant utility class
    private SolonEpgParametrageApplicationConstants() {}
}
