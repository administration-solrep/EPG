package fr.dila.solonepg.elastic.rest;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;

/**
 * Interface pour le client rest HTTP.
 * 
 * @author tlombard
 *
 */
public interface IElasticHttpRestClient {
	SearchResponse queryData(SearchRequest request) throws JsonProcessingException, IOException;

	SearchResponse queryDocuments(SearchRequest request) throws JsonProcessingException, IOException;
}
