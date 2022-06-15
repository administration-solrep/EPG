package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.elastic.models.search.SearchResult;
import org.elasticsearch.action.search.SearchResponse;

public interface ISearchResultMapper {
    SearchResult from(SearchResponse response);
}
