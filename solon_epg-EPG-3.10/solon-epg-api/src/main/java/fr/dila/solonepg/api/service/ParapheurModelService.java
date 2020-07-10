package fr.dila.solonepg.api.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.parapheur.ParapheurModelNode;

/**
 * Classe de service pour la gestion de l'arborescence du parapheur.
 * 
 * @author ARN
 */
public interface ParapheurModelService extends Serializable {

    // /////////////////
    // parapheur Model method
    // ////////////////
    
    /**
     * Retourne le répertoire racine des modèles de parapheur.
     * 
     * @return Répertoire des modèles de parapheur
     * 
     * @throws ClientException
     */
    DocumentModel getParapheurModelRoot(CoreSession session) throws ClientException;

    /**
     * creation du modele de parapheur.
     * 
     * @param documentRacine
     * @param session
     * @param typeActe
     * 
     * @return DocumentModel
     */
    public DocumentModel createParapheurModele(DocumentModel documentRacine, CoreSession session, String typeActe );

    /**
     * Création des modèles de répertoires du modèle de parapheur donné.
     * 
     * @param documentRacine
     *            document documentRacine à initialiser
     * @param session
     *            session
     * @throws ClientException
     */
    void createParapheurDefaultRepository(DocumentModel parapheurModel, CoreSession session);

    /**
     * Retourne le modèle de parapheur correspond au type acte envoyé en paramètre.
     * Si plusieurs modèles de parapheur correspondent au type d'acte, seul le premier parapheur trouvé est renvoyé.
     * 
     * @param session session
     * 
     * @param typeActe type d'acte du modèle de parapheur
     * 
     * @return DocumentModel modèle de parapheur
     * 
     * @throws ClientException
     */
    DocumentModel getParapheurModelFromTypeActe(CoreSession session,String typeActe) throws ClientException;
    
    // /////////////////
    // parapheur Model tree method
    // ////////////////

    /**
     * Récupération des répertoire racine du modèle de parapheur sous forme de noeud.
     * 
     * @param session
     *            session
     * 
     * @param parapheurModel
     *            modèle de feuille de route
     * 
     * @return List<parapheurModelNode>
     * 
     * @throws ClientException
     */
    List<ParapheurModelNode> getParapheurModelRootNode(CoreSession session, DocumentModel parapheurModel);

    /**
     * Récupération de répertoire fils du modèle de parapheur sous forme de noeud.
     * 
     * @param documentId
     *            identifiant du repertoire
     * 
     * @param session
     *            session
     * 
     * @return List<parapheurModelNode> répertoire fils.
     * 
     * @throws ClientException
     */
    List<ParapheurModelNode> getChildrenParapheurModelNode(String documentId, CoreSession session);

    // /////////////////
    // parapheur Model action method
    // ////////////////

    /**
     * creation d'un nouveau répertoire dans le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void createNewDefaultFolderInTree(DocumentModel documentParent, CoreSession session) throws ClientException;

    /**
     * creation d'un nouveau répertoire avant le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void createNewFolderNodeBefore(DocumentModel documentCourant, CoreSession session) throws ClientException;

    /**
     * creation d'un nouveau répertoire après le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void createNewFolderNodeAfter(DocumentModel documentCourant, CoreSession session) throws ClientException;

    /**
     * enregistre les modifications apportés au répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    DocumentModel editFolder(DocumentModel documentCourant, CoreSession session, String name, Boolean estNonVide, Long NbMaxDoc, List<File> feuilleStyleFiles, List<String> formatsAutorisesIds) throws ClientException;

    /**
     * enregistre les modifications apportés au répertoire sélectionné.
     * 
     * @author antoine Rolin
     * @param documentCourant
     * @param session
     * 
     * @return String view
     */
    DocumentModel saveFolder(DocumentModel documentCourant, CoreSession session,String typeActe) throws ClientException;

    DocumentModel saveFolder(DocumentModel documentCourant, CoreSession session) throws ClientException;
    
    /**
     * supprime le répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @return String view
     */
    void deleteFolder(DocumentModel documentCourant, CoreSession session) throws ClientException;
    
    /**
     * met à jour les formats de fichiers autorisés.
     * 
     * @author antoine Rolin
     * @param documentCourant
     * @param session
     * @param formatFichier
     * 
     * @return String view
     */
    DocumentModel updateFormatFile(DocumentModel documentCourant, List<String> formatFichier) throws ClientException;

    /**
     * @param documentCourant
     * @return
     * @throws ClientException
     */
    List<String> getFormatFile(DocumentModel documentCourant) throws ClientException;
}
