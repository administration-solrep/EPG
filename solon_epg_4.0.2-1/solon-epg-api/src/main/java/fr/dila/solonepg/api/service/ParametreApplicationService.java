package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.st.api.service.LogDocumentUpdateService;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface du service de gestion des paramètres de l'application (voir écran ECR_ADM_PAR_APP).
 *
 * @author arolin
 */
public interface ParametreApplicationService extends LogDocumentUpdateService {
    /**
     * Récupère le document contenant les informations sur le paramètrage de l'application.
     */
    ParametrageApplication getParametreApplicationDocument(CoreSession session);

    /**
     * Récupère toutes la liste de toutes les intitulés pouvant être utilisé comme nom de colonne dans la vue des corbeilles à l'exception des intitulés déjà utilisés.
     */
    List<DocumentModel> getAllNonAvailableColumnsDocument(DocumentModel parametreApplication);

    /**
     * Récupère la liste des intitulés proposés aux utilisateurs comme colonnes dans la vue des corbeilles.
     */
    List<DocumentModel> getAvailablesColumnsDocument(DocumentModel parametreApplication);

    /**
     * Renvoie la liste d'identifiants des colonnes de l'espace de traitement disponible dans le profil utilisateur.
     */
    List<String> getAvailablesColumnsIds(DocumentModel parametreApplication);

    ParametrageApplication saveParametrageApplication(
        CoreSession session,
        ParametrageApplication parametrageApplication
    );
}
