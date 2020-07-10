package fr.dila.solonepg.elastic.services;


import java.io.IOException;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.ClientException;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;

public interface RequeteurService {

	/**
	 * Execute la recherche en fonction des critères donnés et retourne le résultat (liste de {@link ElasticDossier})
	 * paginé, avec facettes.
	 * 
	 * @param criteria
	 * @return une liste de {@link Dossier}, paginée, avec facettes
	 */
	SearchResult getResults(SearchCriteria criteria, CoreSession session) throws JsonProcessingException, IOException, ClientException;

	/**
	 * Méthode de requêtage bas-niveau.
	 */
	SearchResponse getResults(SearchRequest searchRequest) throws JsonProcessingException, IOException;

}
