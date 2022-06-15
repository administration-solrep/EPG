package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.ss.api.service.SSFondDeDossierService;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * Classe de service pour la gestion de l'arborescence du fond de dossier.
 *
 * @author ARN
 */
public interface FondDeDossierService extends SSFondDeDossierService {
    /**
     * creation du fond de dossier ainsi que l'arborescence associée.
     *
     * @param dossier
     * @param session
     *
     * @return DocumentModel
     */
    void createAndFillFondDossier(Dossier dossier, CoreSession session);

    // /////////////////
    // fond de dossier tree method
    // ////////////////

    /**
     * Récupération des répertoire racine du fond de dossier sous forme de noeud à partir de la session, du dossier et
     * de l'utilisateur.
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @param currentUser
     *            currentUser
     *
     * @return List<FondDeDossierNode>
     */
    List<FondDeDossierFolder> getAllVisibleRootFolder(
        CoreSession session,
        DocumentModel dossier,
        NuxeoPrincipal currentUser
    );

    /**
     * Récupération des répertoire racine du fond de dossier sous forme de noeud à partir de la session et du dossier.
     *
     * @param session
     * @param dossierDoc
     * @return List<FondDeDossierNode>
     */
    List<FondDeDossierFolder> getAllRootFolder(CoreSession session, Dossier dossierDoc);

    /**
     * Récupération de tous les documents du fond de dossiers visible par l'utilisateur dans une liste de DocumentModel
     *
     * @param session
     *            session
     * @param repertoireVisible
     *            liste des répertoires visibles par l'utilisateur
     *
     * @return List<FondDeDossierNode>
     */
    List<DocumentModel> storeAllChildren(CoreSession session, List<FondDeDossierFolder> repertoireVisible);

    /**
     * Récupération des répertoires fils d'un répertoire du fond de dossier. <br />
     * Note : on récupère les documents fils à partir de la liste des DocumentModel passé en paramètre : on ne passe pas
     * par la requête session.getChildren().
     *
     * @param documentId
     *            identifiant du repertoire parent
     *
     * @param session
     *            session
     *
     * @return List<FondDeDossierNode> répertoire fils.
     */
    List<FondDeDossierFolder> getChildrenFondDeDossierNodeFromdocList(
        String documentId,
        CoreSession session,
        List<DocumentModel> fddChildParentIdList
    );

    /**
     * Met à jour l'arborescence du fond de dossier suite à un changement de type d'acte(UC-SOL-DOS-26)
     *
     * @param dossier
     *            dossier courant à modifier
     * @param newTypeActe
     *            nouveeu type d'acte
     * @param session
     *            session
     */
    void updateFondDossierTree(Dossier dossier, String newTypeActe, CoreSession session);

    // /////////////////
    // fondDossier action method
    // ////////////////

    /**
     * Retourne la liste des fichiers et répertoires dans le fond de dossier donné, en respectant les droits de
     * visibilité
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers et répertoires dans le fond de dossier donné, en respectant les droits de
     *         visibilité
     */
    List<DocumentModel> getFddDocuments(CoreSession session, Dossier dossier);

    /**
     * Retourne la liste des fichiers du répertoire publique du fond de dossier donné
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers dans le fond de dossier donné
     */
    List<DocumentModel> getPublicFddFiles(CoreSession session, Dossier dossier);

    /**
     * Retourne la liste des fichiers du répertoire 'rapport de présentation' du répertoire publique du fond de dossier
     * donné
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers dans le fond de dossier donné
     */
    List<DocumentModel> getRapportPresentationFiles(CoreSession session, Dossier dossier);

    /**
     * Retourne la liste des fichiers du répertoire 'Epreuve' du répertoire publique du fond de dossier donné
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers dans le fond de dossier donné
     */
    List<DocumentModel> getEpreuvesFiles(CoreSession session, Dossier dossier);

    /**
     * Retourne la liste de tous les répertoires du fond de dossier donné, sans tenir compte des droits de visibilité
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste de tous les répertoires du fond de dossier donné, sans tenir compte des droits de visibilité
     */
    List<DocumentModel> getAllFddFolder(CoreSession session, Dossier dossier);

    /**
     * crée un fichier dans le fond de dossier.
     *
     * @param session
     *            session
     * @param fileName
     *            nom du fichier
     * @param content
     *            contenu du fichier
     * @param folderName
     *            nom du repertoire
     * @param dossier
     *            dossier
     * @return DocumentModel du fichier créé
     */
    DocumentModel createFondDeDossierFile(
        CoreSession session,
        String fileName,
        Blob content,
        String folderName,
        DocumentModel dossier
    );

    /**
     * crée un fichier dans le fond de dossier.
     *
     * @param session
     *            session
     * @param principal
     *            utilisateur creant le dossier
     * @param fileName
     *            nom du fichier
     * @param content
     *            contenu du fichier
     * @param folderId
     *            id du repertoire
     * @param dossier
     *            dossier
     * @return DocumentModel du fichier créé
     */
    DocumentModel createFile(
        CoreSession session,
        NuxeoPrincipal principal,
        String fileName,
        Blob content,
        String folderId,
        DocumentModel dossier
    );

    /**
     * crée un fichier dans le fond de dossier.
     *
     * @param session
     *            session
     * @param fileName
     *            nom du fichier
     * @param content
     *            contenu du fichier en byte
     * @param folderName
     *            nom du repertoire
     * @param dossier
     *            dossier
     * @return DocumentModel du fichier créé
     */
    DocumentModel createFondDeDossierFile(
        CoreSession session,
        String fileName,
        byte[] content,
        String folderName,
        DocumentModel dossier
    );

    /**
     * Supprime un fichier du fond de dossier et log l'info dans le journal
     *
     * @param session
     * @param document
     * @param dossierDoc
     */
    void deleteFile(CoreSession session, DocumentModel document, DocumentModel dossierDoc);

    /**
     * Supprime un fichier du fond de dossier et log l'info dans le journal en lui passant le type de fichier à vérifier
     *
     * @param session
     * @param document
     * @param dossierDoc
     */
    void deleteFileWithType(CoreSession session, DocumentModel document, DocumentModel dossierDoc, String fileType);

    /**
     * Vérifie l'unicité de nom d'un fichier dans le répertoire parent
     *
     * @param session
     * @param filename
     * @param folderId
     * @param dossierDoc
     * @return
     */
    boolean checkNameUnicity(CoreSession session, String filename, String folderId, DocumentModel dossierDoc);

    /**
     * Renvoi la liste des documents qui ont été modifiés dans le fond de dossier pour étape avis CE sauf ceux dans
     * dossier saisine rectificative
     *
     * @param session
     * @param dossier
     * @return
     */
    List<FondDeDossierFile> getAllUpdatedFilesFDDNotSaisine(
        final CoreSession session,
        final Dossier dossier,
        final Calendar dateDernierSaisineRectificative
    );

    /**
     * Renvoie la liste des documents qui ont été modifiés dans le fond de dossier répertoire saisine rectificative
     * durant l'étape pour avis CE
     *
     * @param session
     * @param dossier
     * @return
     */
    List<FondDeDossierFile> getAllUpdatedFilesFDDInSaisine(
        final CoreSession session,
        final Dossier dossier,
        final Calendar dateDernierSaisineRectificative
    );

    /**
     * Retourne la date de début de l'étape pour avis CE - Renvoie null si le dossier n'est pas à cette étape
     *
     * @param dossier
     * @param session
     * @return
     */
    Calendar getStartStepPourAvisCE(final DocumentModel dossier, CoreSession session);

    /**
     * Permet de savoir si le dossier relié au fichier est dans l'étape Pour avis CE
     *
     * @param dossier
     * @param session
     * @return
     */
    boolean isDossierEtapePourAvisCE(final DocumentModel dossier, final CoreSession session);

    /**
     * Retourne la liste des saisines rectificatives ou des envois de pièces complémentaires pour un dossier donné
     *
     * @param dossier
     * @param documentManager
     * @return
     */
    List<DocumentModel> getListSaisineRectificativeOuListTransmissionPiecesComplementaireForDossier(
        Dossier dossier,
        CoreSession documentManager,
        String typeModification
    );

    /**
     * Renvoi l'arborescence des répertoires mais seulement avec ceux qui ne sont pas vides
     *
     * @param session
     * @param dossier
     * @return
     */
    List<DocumentModel> getAllFoldersWithDocuments(final CoreSession session, final Dossier dossier);

    /**
     * Récupère tous les documents contenus dans le fondDeDossier
     *
     * @param session
     * @param dossier
     * @return
     */
    List<DocumentModel> getAllFilesFromFondDeDossierForArchivage(
        final CoreSession session,
        final Dossier dossier,
        final String folderName
    );

    /**
     * Récupère tous les documents du FondDeDossier qui ne sont pas présents dans la liste de dossiers passés en
     * paramètres
     *
     * @param session
     * @param dossier
     * @param folderNames
     * @return
     */
    List<DocumentModel> getAllFilesFromFondDeDossierForArchivageOutOfFolder(
        final CoreSession session,
        final Dossier dossier,
        final List<String> folderNames
    );

    /**
     * Return true si la liste de folders du FondDeDossier transmis contient au moins un fichier
     *
     * @param session
     * @param dossier
     *            : le dossier en cours
     * @param folderNames
     *            : liste de noms de dossiers
     * @return
     */
    Boolean hasFichiersInFoldersInFDD(final CoreSession session, final Dossier dossier, final List<String> folderNames);

    /**
     * Permet la création d'un jeton pour le WS chercherModificationDossier pour la saisine rectificative
     * @param session
     * @param dossierDoc
     * @param filesForCE
     * @param startComment le commentaire dans le journal qui sera suivi des noms de fichiers
     */
    void sendSaisineRectificative(
        CoreSession session,
        DocumentModel dossierDoc,
        List<FondDeDossierFile> filesForCE,
        String startComment
    );
    /**
     * Permet la création d'un jeton pour le WS chercherModificationDossier pour l'envoi de pièces complémentaires
     * @param session
     * @param dossierDoc
     * @param filesForCE
     * @param startComment le commentaire dans le journal qui sera suivi des noms de fichiers
     */
    void sendPiecesComplementaires(
        CoreSession session,
        DocumentModel dossierDoc,
        List<FondDeDossierFile> filesForCE,
        String startComment
    );

    /**
     * Récupère tous les documents des répertoire contenus dans le fondDeDossier
     *
     * @param session
     * @param dossier
     * @return
     */
    List<DocumentModel> getAllFilesFromFondDeDossierAllFolder(CoreSession session, Dossier dossier);

    /**
     * Retourne la liste des fichiers du répertoire 'Avis du Conseil d'Etat' du répertoire publique du fond de dossier donné
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers dans le fond de dossier donné
     */
    List<DocumentModel> getAvisCEFiles(CoreSession session, Dossier dossier);

    /**
     * Récupère un répertoire du fond de dossier à partir de son titre. Permet de la créer si besoin.
     *
     * @param dossierdocModel
     * @param session
     * @param folderName
     * @param createIfNeeded
     * @return un répertoire du fond de dossier à partir de son titre.
     */
    DocumentModel getFondDossierFolder(
        final DocumentModel dossierdocModel,
        final CoreSession session,
        final String folderName,
        boolean createIfNeeded
    );

    /**
     * Retourne la liste des fichiers d'un répertoire du fond de dossier donné
     */
    List<DocumentModel> getFilesFromFondDossierFolder(
        final CoreSession session,
        final Dossier dossier,
        final DocumentModel folder
    );
}
