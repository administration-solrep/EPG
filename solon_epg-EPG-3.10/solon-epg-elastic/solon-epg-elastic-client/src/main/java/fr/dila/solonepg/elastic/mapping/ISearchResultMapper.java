package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.SearchResult;

public interface ISearchResultMapper {
	SearchResult from(SearchResponse response);
}
