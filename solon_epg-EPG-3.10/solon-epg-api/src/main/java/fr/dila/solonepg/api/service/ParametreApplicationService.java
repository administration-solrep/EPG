package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.st.api.service.LogDocumentUpdateService;

/**
 * Interface du service de gestion des paramètres de l'application (voir écran ECR_ADM_PAR_APP).
 * 
 * @author arolin
 */
public interface ParametreApplicationService extends LogDocumentUpdateService, Serializable {

    /**
     * Récupère le document contenant les informations sur le paramètrage de l'application.
     * 
     * @param session
     * @return
     */
    ParametrageApplication getParametreApplicationDocument(CoreSession session) throws ClientException;

    /**
     * Récupère toutes la liste de toutes les intitulés pouvant être utilisé comme nom de colonne dans la vue des corbeilles à l'exception des intitulés déjà utilisés.
     * 
     * @param session
     * @return List<DocumentModel>
     */
    List<DocumentModel> getAllNonAvailableColumnsDocument(DocumentModel parametreApplication);
    
    /**
     * Récupère la liste des intitulés proposés aux utilisateurs comme colonnes dans la vue des corbeilles.
     * 
     * @param parametreApplication parametreApplication
     * @param session
     * @return List<DocumentModel>
     */
    List<DocumentModel> getAvailablesColumnsDocument(DocumentModel parametreApplication);

    /**
     * Renvoie la liste d'identifiants des colonnes de l'espace de traitement disponible dans le profil utilisateur.
     * 
     * @param parametreApplication
     * @return la liste d'identifiants de toutes les colonnes de l'espace de traitement.
     */
    List<String> getAvailablesColumnsIds(DocumentModel parametreApplication);

}
