package fr.dila.solonepg.api.constant;

import fr.dila.ss.api.constant.SSFondDeDossierConstants;
import org.nuxeo.common.utils.Path;

/**
 * Constantes du fond de dossier Solon EPG
 *
 * @author antoine Rolin
 *
 */
public class SolonEpgFondDeDossierConstants extends SSFondDeDossierConstants {
    // *************************************************************
    // Fond De Dossier et Fond De Dossier model
    // *************************************************************

    public static final String DEFAULT_FDD_FOLDER_NAME = "Nouveau répertoire";

    /**
     * Nom (ecm:name) du DocumentModel "Fond de Dossier" situé dans le Dossier
     */
    public static final String DEFAULT_FDD_NAME = "Fond de dossier";

    public static final String AMPLIATION_DOCUMENT_TYPE = "Ampliation";

    public static final String FOND_DE_DOSSIER_DOCUMENT_SCHEMA = "fond_dossier_solon_epg";

    public static final String FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE = "ModeleFondDeDossier";

    public static final String FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA = "modele_fond_dossier_solon_epg";

    public static final String FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE = "FondDeDossierFolder";

    public static final String FOND_DE_DOSSIER_FOLDER_DOCUMENT_SCHEMA = "fond_dossier_folder_solon_epg";

    /**
     * Propriété du fond de dossier.
     */
    public static final String FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY = "formatAutorise";

    /**
     * Type de document racine des modèles de fond de dossier.
     */
    public static final String FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_TYPE = "FondDossierModelRoot";

    public static final String FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_SCHEMA = "modele_fond_dossier_racine_solon_epg";

    public static final String FOND_DE_DOSSIER_MODEL_ROOT_FORMAT_AUTORISE_PROPERTY = "formatAutorise";

    /**
     * Type de document utilisé pour les modèles de répertoires de fond de dossier : utilisé dans le treeServiceImpl
     */
    public static final String FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE = "ModeleFondDeDossierFolder";

    /**
     * Nom de l'événement qui signale que le modèle de fond de dossier courant doit être réinitialisé
     */
    public static final String FOND_DE_DOSSIER_MODEL_RESET_EVENT = "FondDeDossierModelResetEvent";

    /**
     * Nom de l'événement qui signale que le modèle de fond de dossier courant a changé
     */
    public static final String FOND_DE_DOSSIER_MODEL_CHANGED_EVENT = "FondDeDossierModelChangedEvent";

    /**
     * info utilisé pour l'affichage de l'arborescence du fond de dossier : type de répertoire
     */
    public static final String FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT = "FondDeDossierFolderUndeletableRoot";

    public static final String FOND_DOSSIER_FOLDER_UNDELETABLE = "FondDeDossierFolderUndeletable";

    public static final String FOND_DOSSIER_FOLDER_DELETABLE = "FondDeDossierFolderDeletable";

    public static final String FOND_DOSSIER_FILE = "FondDeDossierFile";

    public static final String FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE = "FileSolonEpg";

    // public static final String FILE_SOLON_EPG_DOCUMENT_SCHEMA = "file_solon_epg";

    // *************************************************************
    // Fond De Dossier property
    // *************************************************************

    // *************************************************************
    // Fond De Dossier Model property
    // *************************************************************

    public static final String FOND_DE_DOSSIER_MODEL_TYPE_ACTE_PROPERTY = "typeActe";

    public static final String FOND_DE_DOSSIER_MODEL_FORMAT_AUTORISE_PROPERTY = "formatAutorise";

    // *************************************************************
    // Fond De Dossier Folder property
    // *************************************************************

    /**
     * info pour savoir si le répertoire est supprimable ou pas.
     */
    public static final String FOND_DE_DOSSIER_FOLDER_IS_SUPPRIMABLE_PROPERTY = "isSupprimable";

    // *************************************************************
    // Nom des répertoires par défaut du fond de dossier
    // *************************************************************

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR =
        "Répertoire réservé au ministère porteur";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG = "Répertoire réservé au SGG";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG =
        "Répertoire réservé au ministère porteur et au SGG";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER = "Répertoire accessible à tous les utilisateurs";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES = "Epreuves";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE = "Documentation jointe";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED = "Documents hors plan de classement";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_ANNEXES_NON_PUBLIEES = "Annexes non publiées";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_NOTE_MIN = "Note pour le ministère";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION = "Rapport de présentation";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_lETTRES_ACCORD = "Lettres d'accord";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_LETTRE_SAISINE_CE = "Lettre de saisine du Conseil d'Etat";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_LISTE_COM_GOUV = "Liste des commissaires du gouvernement";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_AVIS_DIVERS = "Avis divers";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_REF = "Textes de références";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_ETUDE_IMPACT = "Etude d'impact";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_EXPOSE_MOTIF = "Exposé des motifs";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_DIVERS = "Divers";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE = "Avis du Conseil d'Etat";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_CONSOLIDES = "Textes consolidés";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE = "Saisine rectificative";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE = "PDF original signé";

    public static final String FOND_DE_DOSSIER_FOLDER_NAME_BORDEREAU_VERSEMENT = "Bordereau de versement";

    public static final String FOND_DE_DOSSIER_CONSTANTE_DOC_MODIF_POUR_AVIS_CE = "*";

    // *************************************************************
    // Nom des chemin par défaut du fond de dossier
    // *************************************************************

    public static final String FOND_DE_DOSSIER_FOLDER_PATH_EPREUVES =
        (
            new Path("").append(FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER).append(FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES)
        ).toString();

    public static final String FOND_DE_DOSSIER_FOLDER_PATH_DOC_JOINTE =
        (
            new Path("").append(FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER).append(FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE)
        ).toString();

    public static final String FOND_DE_DOSSIER_FOLDER_PATH_RAPPORT_PRESENTATION =
        (
            new Path("")
                .append(FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE)
                .append(FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION)
        ).toString();

    public static final String FOND_DE_DOSSIER_FOLDER_PATH_AVIS_CE =
        (
            new Path("").append(FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE).append(FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE)
        ).toString();

    // *******************************************************************
    // Chemin fond de dossier par type acte
    // *******************************************************************

    public static final String FOND_DE_DOSSIER_ROOT_FOLDER = "/case-management/workspaces/admin/fond-dossier";

    public static final String FOND_DE_DOSSIER_MODEL_TEXT_NON_PUB_FOLDER =
        "ModeleFondDeDossier Textes non publiés au JO";

    // Constant utility class
    protected SolonEpgFondDeDossierConstants() {
        super();
    }
}
