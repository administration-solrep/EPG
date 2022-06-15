package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.parapheur.ParapheurModelNode;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

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
     */
    DocumentModel getParapheurModelRoot(CoreSession session);

    /**
     * creation du modele de parapheur.
     *
     * @param documentRacine
     * @param session
     * @param typeActe
     *
     * @return DocumentModel
     */
    public DocumentModel createParapheurModele(DocumentModel documentRacine, CoreSession session, String typeActe);

    /**
     * Création des modèles de répertoires du modèle de parapheur donné.
     *
     * @param documentRacine
     *            document documentRacine à initialiser
     * @param session
     *            session
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
     */
    DocumentModel getParapheurModelFromTypeActe(CoreSession session, String typeActe);

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
    void createNewDefaultFolderInTree(DocumentModel documentParent, CoreSession session);

    /**
     * creation d'un nouveau répertoire avant le répertoire sélectionné.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    void createNewFolderNodeBefore(DocumentModel documentCourant, CoreSession session);

    /**
     * creation d'un nouveau répertoire après le répertoire sélectionné.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    void createNewFolderNodeAfter(DocumentModel documentCourant, CoreSession session);

    /**
     * enregistre les modifications apportés au répertoire sélectionné.
     *
     * @author antoine Rolin
     *
     * @return String view
     */
    DocumentModel editFolder(
        DocumentModel documentCourant,
        CoreSession session,
        String name,
        Boolean estNonVide,
        Long NbMaxDoc,
        List<File> feuilleStyleFiles,
        List<String> formatsAutorisesIds
    );

    /**
     * enregistre les modifications apportés au répertoire sélectionné.
     *
     * @author antoine Rolin
     * @param documentCourant
     * @param session
     *
     * @return String view
     */
    DocumentModel saveFolder(DocumentModel documentCourant, CoreSession session, String typeActe);

    DocumentModel saveFolder(DocumentModel documentCourant, CoreSession session);

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
    DocumentModel updateFormatFile(DocumentModel documentCourant, List<String> formatFichier);

    /**
     * @param documentCourant
     * @return
     */
    List<String> getFormatFile(DocumentModel documentCourant);
}
