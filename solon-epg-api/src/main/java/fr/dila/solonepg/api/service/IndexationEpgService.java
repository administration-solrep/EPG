package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.st.api.organigramme.EntiteNode;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public interface IndexationEpgService extends Serializable {
    DocumentModelList findAllIndexationRubrique(CoreSession session);

    DocumentModelList findAllIndexationMotCle(CoreSession session);

    void createIndexationRubrique(CoreSession session, String intitule);

    DocumentModel createIndexationMotCle(CoreSession session, String intitule, List<String> ministereIds);

    void updateIndexationMotCle(CoreSession session, IndexationMotCle indexationMotCle);

    void updateIndexationRubrique(CoreSession session, IndexationRubrique indexationRubrique);

    void deleteIndexationMotCle(CoreSession session, IndexationMotCle indexationMotCle);

    void deleteIndexationRubrique(CoreSession session, IndexationRubrique indexationRubrique);

    List<EntiteNode> findAllMinistereForIndexation(CoreSession session);

    List<String> findAllIndexationMotCleForDossier(CoreSession session, DocumentModel dossierDoc, String indexation);

    List<String> findAllIndexationRubrique(CoreSession session, String indexation);

    void updateIndexationMotCleAfterChange(CoreSession session, IndexationMotCle indexationMotCle);

    /**
     * Migre les mots clés de l'écran "Gestion de l'indexation".
     *
     * @param session
     * @param ancienMinistereId
     * @param nouveauMinistereId
     * @param migrationLoggerModel
     */
    void migrerGestionIndexation(
        CoreSession session,
        String ancienMinistereId,
        String nouveauMinistereId,
        MigrationLoggerModel migrationLoggerModel
    );

    /**
     * Récupère les mots clés de l'indexation liés au ministère ayant l'id <b>idMinistere</b>
     *
     * @param session
     * @param idMinistere
     * @return
     */
    List<DocumentModel> getIndexationMotCleQueryFromIdMinistere(CoreSession session, String idMinistere);
}
