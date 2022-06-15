package fr.dila.solonepg.elastic.batch;

import fr.dila.solonepg.elastic.models.indexing.IndexationDossierStatistics;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface IIndexationService {
    boolean isIndexationContinueEnabled();

    List<String> listDossiersIdsAIndexer(CoreSession session);

    IndexationDossierStatistics doIndexDossier(
        CoreSession session,
        Map<String, String> vecteurPublications,
        DocumentModel dossierDoc,
        boolean deletion
    );

    /**
     * Récupération du nombre maximum de dossiers à indexer dans un worker.
     *
     * @return un int compris entre 0 et max integer
     */
    int getQueryLimit();
}
