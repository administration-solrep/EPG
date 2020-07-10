package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
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
     * @throws ClientException
     */
    int verserDossiers(CoreSession session, List<DocumentModel> docs) throws ClientException;

    /**
     * Ajoute un dossier à la liste des dossiers signalés de l'utilisateur.
     * 
     * @param session
     * @param document dossier
     * @return
     * @throws ClientException
     */
    int verserDossier(CoreSession session, DocumentModel document) throws ClientException;

    /**
     * Enleve tous les dossiers signalés lié à un dossier de tous les espaces utilisateurs.
     * 
     * @param dossier auquel est lié le dossier signalé
     * @param session
     * @param docs
     * @throws ClientException
     */
//    void deleteAllDossierSignaleUnrestricted(CoreSession session, DocumentModel doc) throws ClientException;

    /**
     * Enleve le dossier signalé.
     * 
     * @param dossier auquel est lié le dossier signalé
     * @param repertoire contenant les dossiers signalés de l'utilisateur
     * @param session
     * @param docs
     * @throws ClientException
     */
    void retirer(CoreSession session, DocumentModel doc, DocumentModel rootFolder) throws ClientException;

    /**
     * Enleve les dossiers signales de la liste des dossiers signalés.
     * 
     * @param list des dossier auquel sont rattachés les dossier signales
     * @param session
     * @param docs
     * @throws ClientException
     */
    void retirer(CoreSession session, List<DocumentModel> docs) throws ClientException;

    /**
     * Vide la corbeille des dossier signalés de l'utilisateur.
     * 
     * @param session
     * @throws ClientException
     */
    void viderCorbeilleDossiersSignales(CoreSession session) throws ClientException;

    /**
     * Renvoie la requête qui récupère tous les dossiers signalés d'un utilisateur
     * 
     * @param dossierSignaleRootId
     * @return
     */
    String getQueryDossierSignale(CoreSession session) throws ClientException;
    
    /**
     * 
     * @param userworkspacePath
     * @param session
     * @return
     * @throws ClientException
     */
    public DocumentModel getDossiersSignales(String userworkspacePath, CoreSession session) throws ClientException ;
}
