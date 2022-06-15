package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.service.SSTreeService;
import java.util.List;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * Interface du service de gestion du parapheur.
 *
 * @author ARN
 */
public interface ParapheurService extends SSTreeService {
    /**
     * creation de l'arborescence de répertoire parapheur du dossier.
     *
     * @param dossier
     * @param session
     * @return DocumentModel
     */
    void createAndFillParapheur(Dossier dossier, CoreSession session);

    /**
     * creation de l'arborescence de répertoire parapheur du dossier spécifique à l'injection.
     *
     * @param dossier
     * @param session
     */
    void createAndFillParapheurInjection(Dossier dossier, CoreSession session);

    // /////////////////
    // parapheur tree method
    // ////////////////

    /**
     * Récupération des répertoire racine du parapheur sous forme de noeud.
     *
     * @param session
     *            session
     * @param parapheur
     *            parapheur
     *
     * @return List<ParapheurNode>
     */
    List<ParapheurFolder> getParapheurRootNode(CoreSession session, DocumentModel parapheur);

    /**
     * Récupération des répertoires fils d'un répertoire du parapheur.
     *
     * @param documentId
     *            identifiant du repertoire parent
     *
     * @param session
     *            session
     *
     * @return List<ParapheurNode> répertoire fils.
     */
    List<ParapheurFolder> getChildrenParapheurNode(String documentId, CoreSession session);

    // /////////////////
    // parapheur action method
    // ////////////////

    /**
     * définit si le document en paramètre est le répertoire contenant l'extrait ou l'acte.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    Boolean isFolderExtraitOrFolderActe(DocumentModel dossierdocModel, CoreSession session);

    /**
     * définit si le répertoire du parapheur est le répertoire contenant l'acte intégral.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    boolean isActeIntegral(ParapheurFolder parapheurFolder, CoreSession session);

    /**
     * vérifie que le dossient contient bien au moins un fichier dans chacun des répertoires du parapheur devant être
     * non vide ("acte" et "extrait") et renseigne le champ isParapheurComplet dans le dossier
     *
     * @author antoine Rolin
     *
     * @return le dossier mis à jour
     */
    DocumentModel checkParapheurComplet(DocumentModel dossierdocModel, CoreSession session);

    /**
     * Récupère le répertoire racine du parapheur à partir de son nom
     *
     * @param dossierdocModel
     *            le dossier
     * @param session
     *            the session
     * @param folderName
     *            nom du repertoire
     * @return the parapheur acte repertoire
     */
    DocumentModel getParapheurFolder(DocumentModel dossierdocModel, CoreSession session, String folderName);

    /**
     * Retourne la liste des fichiers et répertoires dans le parapheur donné
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers et répertoires dans le parapheur donné
     */
    List<DocumentModel> getParapheurDocuments(CoreSession session, Dossier dossier);

    /**
     * Retourne la liste des fichiers dans le parapheur donné sous forme de DocumentModel
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @return la liste des fichiers dans le parapheur donné sous forme de DocumentModel
     */
    List<DocumentModel> getParapheurFiles(CoreSession session, Dossier dossier, String... prefetch);

    /**
     * Retourne la pièce du parapheur souhaité (acte intégral ou extrait) ainsi que les pièces complémentaires en
     * fonction du champ du bordereau "Publication intégrale ou par extrait"
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @param Pièces
     *            complémentaires
     * @return la liste des fichiers dans le parapheur donné sous forme de DocumentModel
     */
    List<DocumentModel> getPieceParapheur(
        CoreSession session,
        Dossier dossier,
        boolean piecesComplementaires,
        String... prefetch
    );

    /**
     * crée un fichier dans le parapheur
     *
     * @param session
     *            session
     * @param fileName
     *            nom du fichier
     * @param content
     *            content en byte[]
     * @param folderName
     *            nom du repertoire
     * @param dossier
     *            dossier
     */
    void createParapheurFile(
        CoreSession session,
        String fileName,
        byte[] content,
        String folderName,
        DocumentModel dossier
    );

    /**
     * crée un fichier dans le parapheur
     *
     * @param session
     *            session
     * @param content
     *            content blob
     * @param folderName
     *            nom du repertoire
     * @param dossier
     *            dossier
     */
    void createParapheurFile(CoreSession session, Blob content, String folderName, DocumentModel dossier);

    /**
     * Met à jour l'arborescence du parapheur suite à un changement de type d'acte
     *
     * @param dossier
     * @param newTypeActe
     * @param session
     */
    void updateParapheurTree(DocumentModel dossier, String newTypeActe, CoreSession session);

    DocumentModel createFileInFolder(
        DocumentModel repertoireParent,
        Blob blob,
        CoreSession session,
        String documentType,
        NuxeoPrincipal currentUser,
        DocumentModel dossierDocument
    );

    /**
     * Créé un fichier de parapheur non persisté
     *
     * @param session
     * @return DocumentModel du fichier de parapheur non persisté
     */
    DocumentModel createBareParapheurFile(CoreSession session) throws SSException;

    /**
     * Supprime un fichier du parapheur et log l'action dans le journal
     *
     * @param session
     * @param document
     * @param dossierDoc
     */
    void deleteFile(CoreSession session, DocumentModel document, DocumentModel dossierDoc);

    /**
     * Récupère tous les fichiers dans le répertoire pour epreuve
     *
     * @param session
     * @param document
     * @return la liste des documents demandés
     */
    List<DocumentModel> getEpreuvesFiles(CoreSession session, Dossier dossier);

    /**
     * Vérifie l'unicité du filename
     *
     * @param documentManager
     * @param filename
     * @param selectedNodeName
     * @param currentDocument
     * @return
     */
    boolean checkNameUnicity(CoreSession session, String filename, String folderName, DocumentModel dossierDoc);

    /**
     * Retourne les dossiers du parapheur mais seulement ceux qui ont des fichiers
     */
    List<DocumentModel> getParapheurDocumentsWithoutEmptyFolders(final CoreSession session, final Dossier dossier);

    /**
     * Retourne la liste des fichiers contenus dans le répertoire passé en paramètre
     *
     * @param session
     * @param dossier
     * @param folderName
     * @return
     */
    List<DocumentModel> getAllFilesFromParapheurForArchivage(
        final CoreSession session,
        final Dossier dossier,
        final String folderName
    );

    /**
     * Retourne la liste des fichiers du parapheur qui ne sont pas contenus dans la liste de répertoires passée en
     * paramètre
     *
     * @param session
     * @param dossier
     * @param folderName
     * @param listFolders
     * @return
     */
    List<DocumentModel> getAllFilesFromParapheurForArchivageNotInFolders(
        final CoreSession session,
        final Dossier dossier,
        final List<String> listFolders
    );

    /**
     * Récupère tous les répertoires du parapheur
     *
     * @param session
     * @param dossier
     * @return
     */
    List<DocumentModel> getAllParapheurFolder(final CoreSession session, final Dossier dossier);

    /**
     * Retourne la liste des fichiers contenue dans tous les répertoires du parapheur
     *
     * @param session
     * @param dossier
     * @return
     */
    List<DocumentModel> getAllFilesFromParapheurAllFolder(CoreSession session, Dossier dossier);

    /*
     * Vérifie qu'un acte intégral est présent dans le parapheur
     */
    boolean hasActeIntegral(final CoreSession session, final Dossier dossier);
}
