package fr.dila.solonepg.api.service;

import fr.dila.ss.api.service.SSTreeService;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * Interface générique pour gérer les arborescences de document.
 *
 * @author ARN
 */
public interface TreeService extends SSTreeService {
    // /////////////////
    // méthodes concernant les fichier
    // ////////////////

    /**
     * création ou modification d'un fichier du type choisi.
     *
     * @param documentId identifiant du repertoire parent ou du fichier à modifier
     *
     * @param fileName nom du fichier
     *
     * @param blob contenu du fichier
     *
     * @param documentType type de document du documentModel contenant le fichier
     *
     * @param currentUser utilisateur ayant créé ou modifié le fichier
     *
     * @param session session
     */
    DocumentModel createOrUpdateFileInFolder(
        String documentId,
        Blob blob,
        CoreSession session,
        String documentType,
        NuxeoPrincipal currentUser,
        DocumentModel dossierDocument
    );

    /**
     * création ou modification d'un fichier du type choisi.
     * @param session session
     * @param content contenu du fichier
     * @param documentType type de document du documentModel contenant le fichier
     * @param currentUser utilisateur ayant créé ou modifié le fichier
     * @param DocumentModel identifiant du repertoire parent ou du fichier à modifier
     */
    DocumentModel createOrUpdateFileInFolder(
        CoreSession session,
        DocumentModel dossierParentDoc,
        DocumentModel fileDoc,
        Blob content,
        String documentType,
        NuxeoPrincipal currentUser
    );

    /**
     * Création d'un document de type fichier dans le répertoire choisi.
     *
     * @param documentModel
     *
     * @param blob contenu du fichier
     *
     * @param session session
     *
     * @param documentType type de document du documentModel contenant le fichier
     *
     * @param currentUser utilisateur ayant créé ou modifié le fichier
     */
    DocumentModel createFileInFolder(
        DocumentModel repertoireParent,
        Blob blob,
        CoreSession session,
        String documentType,
        NuxeoPrincipal currentUser,
        DocumentModel dossierDocument
    );

    /**
     * récupération des types de format autorisés.
     *
     * @param DocumentModel documentCourant (Dossier)
     *
     * @param DocumentModel repertoire ou fichier sélectionné
     *
     * @param documentType type de document du documentModel du fichier
     *
     * @param session session
     *
     * @return String
     */
    String getFormatsAutorises(
        DocumentModel currentDossier,
        DocumentModel repositoryOrFileSelected,
        CoreSession session,
        String documentType
    );

    // /////////////////
    // methodes concernant l'ajout et la suppression de répertoire
    // ////////////////

    /**
     * creation d'un nouveau répertoire dans le répertoire sélectionné.
     *
     * @author antoine Rolin
     */
    void createNewDefaultFolderInTree(
        DocumentModel documentParent,
        CoreSession session,
        String documentType,
        DocumentModel dossierDocument,
        String typeActe,
        String name
    );

    void createNewDefaultFolderInTree(
        DocumentModel documentParent,
        CoreSession session,
        String documentType,
        DocumentModel dossierDocument
    );

    /**
     * creation d'un nouveau répertoire avant le répertoire sélectionné.
     *
     * @author antoine Rolin
     */
    void createNewFolderNodeBefore(
        DocumentModel documentCourant,
        CoreSession session,
        String documentType,
        DocumentModel dossierDocument,
        String typeActe,
        String name
    );

    void createNewFolderNodeBefore(
        DocumentModel documentCourant,
        CoreSession session,
        String documentType,
        DocumentModel dossierDocument
    );

    /**
     * creation d'un nouveau répertoire après le répertoire sélectionné.
     *
     * @author antoine Rolin
     */
    void createNewFolderNodeAfter(
        DocumentModel documentCourant,
        CoreSession session,
        String documentType,
        DocumentModel dossierDocument,
        String typeActe,
        String name
    );

    void createNewFolderNodeAfter(
        DocumentModel documentCourant,
        CoreSession session,
        String documentType,
        DocumentModel dossierDocument
    );

    /**
     * Enregistre les modifications apportés au répertoire sélectionné.
     *
     * @author antoine Rolin
     * @param documentCourant
     * @param session
     *
     * @return String view
     */
    DocumentModel updateFolder(DocumentModel documentCourant, CoreSession session, String typeActe);

    DocumentModel updateFolder(DocumentModel documentCourant, CoreSession session);

    /**
     * Change le titre du répertoire sélectionné.
     *
     * @author antoine Rolin
     * @param documentCourant
     * @param session
     */
    DocumentModel renameFolder(
        DocumentModel documentCourant,
        CoreSession session,
        String newTitle,
        DocumentModel dossierDocument
    );

    /**
     * Supprime le document sélectionné et génère une entrée dans le journal. Si le document est un répertoire, on supprime aussi les documents fils du répertoire. Si le document est un fichier, on supprime aussi les versions du fichier.
     */
    void deleteDocument(
        DocumentModel documentCourant,
        CoreSession session,
        DocumentModel dossierDocument,
        String typeActe
    );

    void deleteDocument(DocumentModel documentCourant, CoreSession session, DocumentModel dossierDocument);

    /**
     *
     * @param documentRacine
     * @param session
     * @param name
     * @return
     */
    DocumentModel createFondDossierModelFolderElement(DocumentModel documentRacine, CoreSession session, String name);

    /**
     * récupère le chemin du fichier dans l'arborescence en enlevant la partie commune aux autres répertoires du Modèle de Fond de Dossier
     *
     * @param folderModel documentModel du fichier
     * @param dddFolderModel nom du répertoire racine
     * @return le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres répertoires du Modèle de Fond de Dossier
     */
    String getFileFolderPath(DocumentModel folderModel, String folderRootName);

    /**
     * log dans le journal l'action de sauvegardeS
     *
     * @param docModel
     * @param session
     * @param filename
     */
    void logAction(DocumentModel docModel, CoreSession session, String filename);

    /**
     * Modification et versioning d'un document de type fichier.
     * @param session
     * @param blob
     * @param currentUser
     * @param documentId
     */
    @Override
    void updateFile(
        CoreSession session,
        DocumentModel fichier,
        Blob blob,
        NuxeoPrincipal currentUser,
        DocumentModel dossierDocument
    );
}
