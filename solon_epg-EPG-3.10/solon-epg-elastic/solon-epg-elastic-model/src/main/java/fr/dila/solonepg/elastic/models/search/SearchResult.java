package fr.dila.solonepg.elastic.models.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.FacetResultEntry;

public class SearchResult {

	private List<ElasticDossier> results = new ArrayList<ElasticDossier>();
	private Long								count;
	private Boolean								maxResultCountExceeded	= false;
	private Map<String, List<FacetResultEntry>> facets = new HashMap<String, List<FacetResultEntry>>();

	public List<ElasticDossier> getResults() {
		return results;
	}

	public void setResults(List<ElasticDossier> results) {
		this.results = results;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Boolean isMaxResultCountExceeded() {
		return maxResultCountExceeded;
	}

	public void setMaxResultCountExceeded(Boolean maxResultCountExceeded) {
		this.maxResultCountExceeded = maxResultCountExceeded;
	}

	public List<FacetResultEntry> getFacetEntries(FacetEnum facetKey) {
		return facets.get(facetKey.toString());
	}

	private void initIfNull(String key) {
		if (facets.get(key) == null) {
			facets.put(key, new ArrayList<FacetResultEntry>());
		}
	}

	public void addFacetEntry(String key, FacetResultEntry facetEntry) {
		initIfNull(key);
		facets.get(key).add(facetEntry);
	}

	public void addFacetEntries(String key, List<FacetResultEntry> facetEntries) {
		initIfNull(key);
		facets.get(key).addAll(facetEntries);
	}

}
