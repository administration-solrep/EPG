package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import org.elasticsearch.action.search.SearchRequest;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Ce mapper permet de construire une SearchRequest à destination de
 * Elasticsearch à partir d'un objet SearchCriteria.
 */
public interface ISearchRequestMapper {
    SearchRequest from(RechercheLibre criteria, CoreSession session, String index) throws ElasticException;

    SearchRequest from(String documentId, String fulltext, CoreSession session, String index);

    SearchRequest all(String index);
}
