package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.fonddossier.FondDeDossierModelNode;

/**
 * Classe de service pour la gestion de l'arborescence du fond de dossier.
 * 
 * @author ARN
 */
public interface FondDeDossierModelService extends Serializable {

    // /////////////////
    // Fond de dossier Model method
    // ////////////////

    /**
     * Retourne le répertoire racine des modèles de fond de dossier.
     * 
     * @return Répertoire des modèles de fond de dossier
     * 
     * @throws ClientException
     */
    DocumentModel getFondDossierModelRoot(CoreSession session) throws ClientException;

    // TODO mettre la methode unrestricted dans le FondDeDossierModelRoot
    /**
     * Retourne la liste des formats autorisés des modèles de fond de dossier.
     * 
     * @return Répertoire des modèles de fond de dossier
     * 
     * @throws ClientException
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
     * @throws ClientException
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
     * 
     * @throws ClientException
     */
    DocumentModel getFondDossierModelFromTypeActe(CoreSession session, String typeActe) throws ClientException;

    /**
     * Retourne les répertoires du modèle de fond de dossier correspondant au type acte envoyé en paramètre.
     * 
     * @param session session
     * 
     * @param typeActe type d'acte du modèle de fond de dossier
     * 
     * @return List<DocumentModel> les répertoires du modèle de fond de dossier correspondant au type acte envoyé en paramètre.
     * 
     * @throws ClientException
     */
    List<DocumentModel> getListFondDossierFolderModelFromTypeActe(CoreSession session, String typeActe) throws ClientException;

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
     * 
     * @throws ClientException
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
     * 
     * @throws ClientException
     */
    List<FondDeDossierModelNode> getChildrenFondDeDossierModelNode(String documentId, CoreSession session);

    // /////////////////
    // Fond de dossier Model action method
    // ////////////////

    /**
     * creation d'un nouveau répertoire dans le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void createNewDefaultFolderInTree(DocumentModel documentParent, CoreSession session, String typeActe) throws ClientException;

    /**
     * creation d'un nouveau répertoire avant le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void createNewFolderNodeBefore(DocumentModel documentCourant, CoreSession session, String typeActe) throws ClientException;

    /**
     * creation d'un nouveau répertoire après le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void createNewFolderNodeAfter(DocumentModel documentCourant, CoreSession session, String typeActe) throws ClientException;

    /**
     * enregistre les modifications apportés au répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    DocumentModel editFolder(DocumentModel documentCourant, CoreSession session, String name, String typeActe) throws ClientException;

    /**
     * supprime le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void deleteFolder(DocumentModel documentCourant, CoreSession session, String typeActe) throws ClientException;

}
