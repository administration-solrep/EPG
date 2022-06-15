package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.fonddossier.FondDeDossierModelNode;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Classe de service pour la gestion de l'arborescence du fond de dossier.
 *
 * @author ARN
 */
public interface FondDeDossierModelService extends Serializable {
    void cleanCache();

    // /////////////////
    // Fond de dossier Model method
    // ////////////////

    /**
     * Retourne le répertoire racine des modèles de fond de dossier.
     *
     * @return Répertoire des modèles de fond de dossier
     */
    DocumentModel getFondDossierModelRoot(CoreSession session);

    /**
     * Retourne la liste des formats autorisés des modèles de fond de dossier.
     *
     * @return Répertoire des modèles de fond de dossier
     */
    List<String> getFormatsAutorises(CoreSession session);

    /**
     * Met à jour le répertoire racine des modèles de fond de dossier (fixe les formats autorisés par défaut).
     *
     * @return Répertoire des modèles de fond de dossier
     */
    DocumentModel updateFondDossierModelRoot(CoreSession session, DocumentModel docModel);

    /**
     * creation du modele du fond de dossier.
     *
     * @param documentRacine
     * @param session
     * @param typeActe
     *
     * @return DocumentModel
     */
    DocumentModel createFondDossierModele(DocumentModel documentRacine, CoreSession session, String typeActe);

    /**
     * Création des répertoires par défaut pour le modèle de fond de dossier donné.
     *
     * @param documentRacine document documentRacine à initialiser
     * @param session session
     */
    void createFondDeDossierDefaultRepository(DocumentModel fddModel, CoreSession session);

    /**
     * Retourne le modèle de fond de dossier correspondant au type acte envoyé en paramètre.
     *
     * @param session session
     *
     * @param typeActe type d'acte du modèle de fond de dossier
     *
     * @return DocumentModel le modèle de fond de dossier correspondant au type acte envoyé en paramètre.
     */
    DocumentModel getFondDossierModelFromTypeActe(CoreSession session, String typeActe);

    /**
     * Retourne les répertoires du modèle de fond de dossier correspondant au type acte envoyé en paramètre.
     *
     * @param session session
     *
     * @param typeActe type d'acte du modèle de fond de dossier
     *
     * @return List<DocumentModel> les répertoires du modèle de fond de dossier correspondant au type acte envoyé en paramètre.
     */
    List<DocumentModel> getListFondDossierFolderModelFromTypeActe(CoreSession session, String typeActe);

    // /////////////////
    // Fond de dossier Model tree method
    // ////////////////

    /**
     * Récupération des répertoires à la racine du modèle de fond de dossier sous forme de noeud.
     *
     * @param session session
     *
     * @param fondDeDossierModel modèle de feuille de route
     *
     * @return List<FondDeDossierModelNode>
     */
    List<FondDeDossierModelNode> getFondDeDossierModelRootNode(CoreSession session, DocumentModel fondDeDossierModel);

    /**
     * Récupération de répertoire fils du modèle de fond de dossier sous forme de noeud.
     *
     * @param documentId identifiant du repertoire
     *
     * @param session session
     *
     * @return List<FondDeDossierModelNode> répertoire fils.
     */
    List<FondDeDossierModelNode> getChildrenFondDeDossierModelNode(String documentId, CoreSession session);

    // /////////////////
    // Fond de dossier Model action method
    // ////////////////

    /**
     * creation d'un nouveau répertoire dans le répertoire sélectionné.
     * @param session session
     * @param documentParent dossier parent
     * @param name nom du dossier
     * @param typeActe type d'acte
     */
    void createNewDefaultFolderInTree(CoreSession session, DocumentModel documentParent, String name, String typeActe);

    /**
     * creation d'un nouveau répertoire avant le répertoire sélectionné.
     *
     * @param session session
     * @param documentParent dossier parent
     * @param name nom du dossier
     * @param typeActe type d'acte
     */
    void createNewFolderNodeBefore(CoreSession session, DocumentModel documentParent, String name, String typeActe);

    /**
     * creation d'un nouveau répertoire après le répertoire sélectionné.
     *
     * @param session session
     * @param documentParent dossier parent
     * @param name nom du dossier
     * @param typeActe type d'acte
     */
    void createNewFolderNodeAfter(CoreSession session, DocumentModel documentParent, String name, String typeActe);

    /**
     * enregistre les modifications apportés au répertoire sélectionné.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    DocumentModel editFolder(DocumentModel documentCourant, CoreSession session, String name, String typeActe);

    /**
     * supprime le répertoire sélectionné.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    void deleteFolder(DocumentModel documentCourant, CoreSession session, String typeActe);
}
