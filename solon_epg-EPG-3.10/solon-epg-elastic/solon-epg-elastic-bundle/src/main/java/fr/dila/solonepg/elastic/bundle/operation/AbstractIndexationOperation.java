package fr.dila.solonepg.elastic.bundle.operation;

import java.util.HashSet;
import java.util.Set;

import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.mapping.ISearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapper;
import fr.dila.solonepg.elastic.models.ElasticHit;
import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;
import fr.dila.solonepg.elastic.services.RequeteurService;

public class AbstractIndexationOperation {

	public void statusReport(HashSet<String> manquantsDansLIndex, HashSet<String> manquantsDansLaBase) throws Exception {
		RequeteurService requeteurService = Framework.getService(RequeteurService.class);
		IIndexationService indexationService = Framework.getService(IIndexationService.class);
		ISearchRequestMapper searchRequestMapper = new SearchRequestMapper();
		SearchRequest searchRequest = searchRequestMapper.all();
		SearchResponse response = requeteurService.getResults(searchRequest);
		Set<String> idsIndexes = new HashSet<String>();
		
		if (response.getHits().getHits().size() < response.getHits().getTotal()) {
			// si le résultat n'est pas complet, il faut tout interrompre
			throw new IllegalStateException(String.format("La taille maximum de résultat Elastic (%d) n'est pas suffisante pour récupérer tous les résultats", searchRequest.getSize()));
		}
		
		for (ElasticHit doc : response.getHits().getHits()) {
			idsIndexes.add(doc.getId());
		}
		Set<String> idsAIndexer = new HashSet<String>(indexationService.listDossiersIdsAIndexer());
		
		manquantsDansLIndex.addAll(idsAIndexer);
		manquantsDansLIndex.removeAll(idsIndexes);
		
		manquantsDansLaBase.addAll(idsIndexes);
		manquantsDansLaBase.removeAll(idsAIndexer);
	}

}
