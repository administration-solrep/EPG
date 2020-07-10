package fr.dila.solonepg.elastic.mapping;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;

/**
 * Ce mapper permet de construire une SearchRequest à destination de
 * Elasticsearch à partir d'un objet SearchCriteria.
 */
public interface ISearchRequestMapper {
	SearchRequest from(SearchCriteria criteria, CoreSession session) throws ClientException;

	SearchRequest from(String documentId, String fulltext, CoreSession session) throws ClientException;

	SearchRequest all();
}
