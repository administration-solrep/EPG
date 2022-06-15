package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface du service sur les dossiers signalés de l'espace de suivi.
 *
 * @author ARN
 */
public interface DossierSignaleService extends Serializable {
    /**
     * Ajoute les dossiers à la liste des dossiers signalés.
     *
     * @param session
     * @param docs
     */
    int verserDossiers(CoreSession session, List<DocumentModel> docs);

    /**
     * Ajoute un dossier à la liste des dossiers signalés de l'utilisateur.
     *
     * @param session
     * @param document dossier
     * @return
     */
    int verserDossier(CoreSession session, DocumentModel document);

    /**
     * Enleve tous les dossiers signalés lié à un dossier de tous les espaces utilisateurs.
     *
     * @param dossier auquel est lié le dossier signalé
     * @param session
     * @param docs
     */
    //    void deleteAllDossierSignaleUnrestricted(CoreSession session, DocumentModel doc) ;

    /**
     * Enleve le dossier signalé.
     *
     * @param dossier auquel est lié le dossier signalé
     * @param repertoire contenant les dossiers signalés de l'utilisateur
     * @param session
     * @param docs
     */
    void retirer(CoreSession session, DocumentModel doc, DocumentModel rootFolder);

    /**
     * Enleve les dossiers signales de la liste des dossiers signalés.
     *
     * @param list des dossier auquel sont rattachés les dossier signales
     * @param session
     * @param docs
     */
    void retirer(CoreSession session, List<DocumentModel> docs);

    /**
     * Vide la corbeille des dossier signalés de l'utilisateur.
     *
     * @param session
     */
    void viderCorbeilleDossiersSignales(CoreSession session);

    /**
     * Renvoie la requête qui récupère tous les dossiers signalés d'un utilisateur
     *
     * @param dossierSignaleRootId
     * @return
     */
    String getIdsDossierSignale(CoreSession session);

    /**
     *
     * @param userworkspacePath
     * @param session
     * @return
     */
    DocumentModel getDossiersSignales(String userworkspacePath, CoreSession session);
}
