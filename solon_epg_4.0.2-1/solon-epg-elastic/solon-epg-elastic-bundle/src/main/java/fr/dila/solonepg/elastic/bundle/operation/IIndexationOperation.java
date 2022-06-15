package fr.dila.solonepg.elastic.bundle.operation;

import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.mapping.ISearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapper;
import fr.dila.solonepg.elastic.services.RequeteurService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.api.Framework;

public interface IIndexationOperation {
    default void statusReport(
        HashSet<String> manquantsDansLIndex,
        HashSet<String> manquantsDansLaBase,
        CoreSession session
    )
        throws IOException, URISyntaxException {
        RequeteurService requeteurService = Framework.getService(RequeteurService.class);

        ISearchRequestMapper searchRequestMapper = new SearchRequestMapper();
        SearchRequest searchRequest = searchRequestMapper.all(requeteurService.getIndexDossiers());
        SearchResponse response = requeteurService.getResults(searchRequest);

        if (response.getHits().getHits().length < response.getHits().getTotalHits().value) {
            // si le résultat n'est pas complet, il faut tout interrompre
            throw new IllegalStateException(
                String.format(
                    "La taille maximum de résultat Elastic (%d) n'est pas suffisante pour récupérer tous les résultats",
                    searchRequest.source().size()
                )
            );
        }
        Set<String> idsIndexes = new HashSet<>();
        for (SearchHit doc : response.getHits().getHits()) {
            idsIndexes.add(doc.getId());
        }
        IIndexationService indexationService = Framework.getService(IIndexationService.class);
        Set<String> idsAIndexer = new HashSet<>(indexationService.listDossiersIdsAIndexer(session));

        manquantsDansLIndex.addAll(idsAIndexer);
        manquantsDansLIndex.removeAll(idsIndexes);

        manquantsDansLaBase.addAll(idsIndexes);
        manquantsDansLaBase.removeAll(idsAIndexer);
    }
}
