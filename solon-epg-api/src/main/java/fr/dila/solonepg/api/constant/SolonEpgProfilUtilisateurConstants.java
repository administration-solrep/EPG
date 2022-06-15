/**
 *
 */
package fr.dila.solonepg.api.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constantes liés au profil utilisateur et à ses éléments (schéma,type,métadonnées).
 *
 * @author antoine Rolin
 *
 */
public class SolonEpgProfilUtilisateurConstants {
    // *************************************************************
    // Profil Utilisateur evenement
    // *************************************************************

    /**
     * Nom de l'événement qui signale que le profil utilisateur a changé
     */
    public static final String PROFIL_UTILISATEUR_CHANGED_EVENT = "ProfilUtilisateurChangedEvent";

    // *************************************************************
    // Profil Utilisateur
    // *************************************************************

    /**
     * nom du document profil utilisateur dans le répertoire personnel de l'utilisateur
     */
    public static final String PROFIL_UTILISATEUR_DOCUMENT_NAME = "profil-utilisateur";

    /**
     * schema profil_utilisateur
     */
    public static final String PROFIL_UTILISATEUR_SCHEMA = "profil_utilisateur_solon_epg";

    /**
     * type de document profil utilisateur
     */
    public static final String PROFIL_UTILISATEUR_DOCUMENT_TYPE = "ProfilUtilisateur";

    // *************************************************************
    // Profil Utilisateur notification property
    // *************************************************************

    public static final String PROFIL_UTILISATEUR_NOTIFICATION_TYPE_ACTES_PROPERTY = "notificationTypeActes";

    public static final String PROFIL_UTILISATEUR_NOTIFICATION_DOSSIER_URGENT_PROPERTY = "notificationDossierUrgent";

    public static final String PROFIL_UTILISATEUR_NOTIFICATION_RETOUR_SGG_PROPERTY = "notificationRetourSgg";

    public static final String PROFIL_UTILISATEUR_NOTIFICATION_MESURE_NOMINATIVE_PROPERTY =
        "notificationMesureNominative";

    public static final String PROFIL_UTILISATEUR_NOTIFICATION_MAIL_SI_FRANCHISSEMENT_AUTOMATIQUE_PROPERTY =
        "notificationMailSiFranchissementAutomatique";

    public static final String PROFIL_UTILISATEUR_PRESENCE_SOLON_EDIT = "presenceLiveEdit";

    public static final String PROFIL_UTILISATEUR_MAIL_SI_MAJ_CE = "mailSiMajCE";
    /**
     * Paramètre utilisé pour socker le nombre maximum de dossiers visibles dans une liste pour un utilisateur.
     */

    public static final String PROFIL_UTILISATEUR_NB_DOSSIERS_VISIBLES_MAX_PROPERTY = "nbDossiersVisiblesMax";

    /**
     * Paremetre de l'id du poste par defaut d'un utilisateur
     */
    public static final String PROFIL_UTILISATEUR_POSTE_DEFAUT = "posteDefaut";
    // *************************************************************
    // Profil Utilisateur : listes des identifiants de colonnes affichées dans l'espace de traitement
    // *************************************************************

    public static final String PROFIL_UTILISATEUR_ID_COLONNE_ESPACE_TRAITEMENT_LIST_PROPERTY =
        "idColonneEspaceTraitementVisible";

    // *************************************************************
    // Profil Utilisateur : listes des identifiants de colonnes pouvant être affichées dans les instances de feuilles de
    // route
    // *************************************************************

    public static final String PROFIL_UTILISATEUR_ID_COLONNE_INSTANCE_FEUILLE_ROUTE_LIST_PROPERTY =
        "idColonneInstanceFeuilleRouteVisible";

    // *************************************************************
    // Profil Utilisateur : nom des colonnes pouvant être affichées dans l'espace de traitement
    // *************************************************************

    // *************************************************************
    // Profil Utilisateur info generales dossier property
    // *************************************************************

    public static final String DATE_CREATION_DOSSIER_PROPERTY = "dateCreationDossier";

    public static final String DATE_ARRIVEE_DOSSIER_LINK_PROPERTY = "dateArriveeDossierLink";

    public static final String CREE_PAR_PROPERTY = "idCreateurDossier";

    public static final String NUMERO_NOR_PROPERTY = "numeroNor";

    public static final String DERNIER_CONTRIBUTEUR_PROPERTY = "dernierContributeur";

    public static final String STATUT_PROPERTY = "statut";

    public static final String TYPE_ACTE_PROPERTY = "typeActe";

    public static final String TITRE_ACTE_PROPERTY = "titreActe";

    public static final String COMPLET_PROPERTY = "complet";

    // *************************************************************
    // Profil Utilisateur info responsables dossier property
    // *************************************************************

    public static final String MINISTERE_RESP_PROPERTY = "ministereResp";

    public static final String DIRECTION_RESP_PROPERTY = "directionResp";

    public static final String MINISTERE_ATTACHE_PROPERTY = "ministereAttache";

    public static final String DIRECTION_ATTACHE_PROPERTY = "directionAttache";

    public static final String NOM_RESP_DOSSIER_PROPERTY = "nomRespDossier";

    public static final String PRENOM_RESP_DOSSIER_PROPERTY = "prenomRespDossier";

    public static final String QUALITE_RESP_DOSSIER_PROPERTY = "qualiteRespDossier";

    public static final String TEL_RESP_DOSSIER_PROPERTY = "telRespDossier";

    public static final String MAIL_RESP_DOSSIER_PROPERTY = "mailRespDossier";

    // *************************************************************
    // Profil Utilisateur info diverse property
    // *************************************************************

    public static final String CATEGORIE_ACTE_PROPERTY = "categorieActe";

    public static final String BASE_LEGALE_ACTE_PROPERTY = "baseLegaleActe";

    public static final String DATE_PUBLICATION_PROPERTY = "datePublication";

    public static final String PROFIL_UTILISATEUR_PREFIX = "pusr";
    public static final String PROFIL_UTILISATEUR_COLUMNS_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_ID_COLONNE_ESPACE_TRAITEMENT_LIST_PROPERTY;
    public static final String PROFIL_UTILISATEUR_POSTE_DEFAUT_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_POSTE_DEFAUT;
    public static final String PROFIL_UTILISATEUR_NB_DOSSIER_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_NB_DOSSIERS_VISIBLES_MAX_PROPERTY;
    public static final String PROFIL_UTILISATEUR_INSTALL_SOLON_EDIT_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_PRESENCE_SOLON_EDIT;
    public static final String PROFIL_UTILISATEUR_VAL_AUTO_ETAP_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_NOTIFICATION_MAIL_SI_FRANCHISSEMENT_AUTOMATIQUE_PROPERTY;
    public static final String PROFIL_UTILISATEUR_MAJ_CONSEIL_ETAT_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_MAIL_SI_MAJ_CE;
    public static final String PROFIL_UTILISATEUR_SUR_SIGNATURE_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_NOTIFICATION_DOSSIER_URGENT_PROPERTY;
    public static final String PROFIL_UTILISATEUR_RETOUR_SGG_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_NOTIFICATION_RETOUR_SGG_PROPERTY;
    public static final String PROFIL_UTILISATEUR_MESURE_NOMINATIVE_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_NOTIFICATION_MESURE_NOMINATIVE_PROPERTY;
    public static final String PROFIL_UTILISATEUR_TYPE_ACTE_ID_XPATH =
        PROFIL_UTILISATEUR_PREFIX + ":" + PROFIL_UTILISATEUR_NOTIFICATION_TYPE_ACTES_PROPERTY;

    // *************************************************************
    // Profil Utilisateur validation et signature property
    // *************************************************************

    public static final String CHARGE_MISSION_IDS_PROPERTY = "chargeMissionIds";

    public static final String CONSEILLER_PM_IDS_PROPERTY = "conseillerPmIds";

    public static final String DATE_SIGNATURE_PROPERTY = "dateSignature";

    public static final String DATE_SIGNATURE_PM_PROPERTY = "dateSignaturePm";

    public static final String DATE_SIGNATURE_PR_PROPERTY = "dateSignaturePr";

    // *************************************************************
    // Profil Utilisateur publication property
    // *************************************************************

    public static final String POUR_FOURNITURE_EPREUVE_PROPERTY = "pourFournitureEpreuve";

    public static final String VECTEUR_PUBLICATION_PROPERTY = "vecteurPublication";

    public static final String PUBLICATION_CONJOINTE_PROPERTY = "publicationConjointes";

    public static final String PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY = "publicationIntegraleOuExtrait";

    public static final String DECRET_NUMEROTE_PROPERTY = "decretNumerote";

    public static final String MODE_PARUTION_PROPERTY = "modeParution";

    public static final String DELAI_PUBLICATION_PROPERTY = "delaiPublication";

    public static final String DATE_PRECISEE_PUBLICATION_PROPERTY = "datePreciseePublication";

    // *************************************************************
    // Profil Utilisateur parution property
    // *************************************************************

    public static final String DATE_PARUTION_JORF_PROPERTY = "dateParutionJorf";

    public static final String NUMERO_TEXTE_PARUTION_JORF_PROPERTY = "numeroTexteParutionJorf";

    public static final String PAGE_PARUTION_JORF_PROPERTY = "pageParutionJorf";

    public static final String NUMERO_TEXTE_PARUTION_BO_PROPERTY = "parutionBo";

    public static final String ZONE_COM_SGG_DILA_PROPERTY = "zoneComSggDila";

    // *************************************************************
    // Profil Utilisateur indexation property
    // *************************************************************

    public static final String RUBRIQUE_PROPERTY = "rubriques";

    public static final String MOTS_CLES_PROPERTY = "motscles";

    public static final String CHAMP_LIBRE_PROPERTY = "libre";

    // *************************************************************
    // Profil Utilisateur Transposition property
    // *************************************************************

    public static final String LOI_REF_PROPERTY = "applicationLoi";

    public static final String ORDONNANCE_REF_LOI_PROPERTY = "transpositionOrdonnance";

    public static final String DIRECTIVE_REF_LOI_PROPERTY = "transpositionDirective";

    // *************************************************************
    // Profil Utilisateur Applications et transposition Habilitation property
    // *************************************************************

    public static final String HABILITATION_REF_LOI_PROPERTY = "habilitationRefLoi";

    public static final String HABILITATION_NUMERO_ARTICLES_PROPERTY = "habilitationNumeroArticles";

    public static final String HABILITATION_COMMENTAIRE_PROPERTY = "habilitationCommentaire";

    // *************************************************************
    // Profil Utilisateur Applications et transposition Loi property
    // *************************************************************

    public static final String LOI_REF_LOI_PROPERTY = "loiRefLoi";

    public static final String LOI_TITRE_LOI_PROPERTY = "loiTitreLoi";

    public static final String LOI_NUMERO_ARTICLES_PROPERTY = "loiNumeroArticles";

    public static final String LOI_REF_MESURE_PROPERTY = "loiRefMesure";

    public static final String LOI_COMMENTAIRE_PROPERTY = "loiCommentaire";

    // *************************************************************
    // Profil Utilisateur Applications et transposition Ordonnance property
    // *************************************************************

    public static final String ORDONNANCE_REF_ORDONNANCE_PROPERTY = "ordonnanceRefOrdonnance";

    public static final String ORDONNANCE_TITRE_DIRECTIVE_PROPERTY = "ordonnanceTitreDirective";

    public static final String ORDONNANCE_NUMERO_ARTICLES_PROPERTY = "ordonnanceNumeroArticles";

    public static final String ORDONNANCE_COMMENTAIRE_PROPERTY = "ordonnanceCommentaire";

    // *************************************************************
    // Profil Utilisateur Applications et transposition Directive property
    // *************************************************************

    public static final String DIRECTIVE_REF_DIRECTIVE_PROPERTY = "directiveRefDirective";

    public static final String DIRECTIVE_TITRE_DIRECTIVE_PROPERTY = "directiveTitreDirective";

    public static final String DIRECTIVE_NUMERO_ARTICLES_PROPERTY = "directiveNumeroArticles";

    public static final String DIRECTIVE_COMMENTAIRE_PROPERTY = "directiveCommentaire";

    // *************************************************************
    // Profil Utilisateur Feuille de route property
    // *************************************************************

    public static final String FDR_ETAPE_PROPERTY = "etape";

    public static final String FDR_MINISTERE_PROPERTY = "ministere";

    public static final String FDR_POSTE_PROPERTY = "poste";

    public static final String FDR_NUM_VERSION_PROPERTY = "numeroVersion";

    public static final String NOTE_PROPERTY = "note";

    public static final String ETAT_PROPERTY = "etat";

    public static final String FDR_TYPE_ACTION_PROPERTY = "typeAction";

    public static final String FDR_AUTEUR_PROPERTY = "auteur";

    public static final String FDR_ECHEANCE_PROPERTY = "echeance";

    public static final String FDR_DATE_TRAITEMENT_PROPERTY = "dateTraitement";

    public static final String FDR_ETAPE_OBLIGATOIRE_PROPERTY = "obligatoire";

    public static final String PROFIL_UTILISATEUR_LISTE_DERNIERS_DOSSIERS_INTERVENTION = "derniersDossiersIntervention";

    // *************************************************************
    // Profil Utilisateur Colonne par défaut
    // *************************************************************

    /**
     * Colonnes par défaut affichée pour les utilisateurs dans la corbeille de l'espace de traitement.
     */
    public static final List<String> PARAMETRAGE_APPLICATION_DEFAULT_ESPACE_TRAITEMENT_COLUMN_LIST = Collections.unmodifiableList(
        Arrays.asList(NUMERO_NOR_PROPERTY, TITRE_ACTE_PROPERTY, DATE_CREATION_DOSSIER_PROPERTY)
    );

    /**
     * Colonnes par défaut affichée pour les utilisateurs dans les instances de feuille de route.
     */
    public static final List<String> PARAMETRAGE_APPLICATION_DEFAULT_FEUILLE_ROUTE_COLUMN_LIST = Collections.unmodifiableList(
        Arrays.asList(
            FDR_ETAPE_PROPERTY,
            FDR_MINISTERE_PROPERTY,
            FDR_POSTE_PROPERTY,
            FDR_NUM_VERSION_PROPERTY,
            NOTE_PROPERTY,
            ETAT_PROPERTY,
            FDR_TYPE_ACTION_PROPERTY,
            FDR_AUTEUR_PROPERTY,
            FDR_ECHEANCE_PROPERTY,
            FDR_DATE_TRAITEMENT_PROPERTY,
            FDR_ETAPE_OBLIGATOIRE_PROPERTY
        )
    );

    public static final int PARAMETRAGE_APPLICATION_DEFAULT_NB_DOSSIERS_VISIBLES_MAX = 10;

    // Constant utility class
    private SolonEpgProfilUtilisateurConstants() {}
}
