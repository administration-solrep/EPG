package fr.dila.solonepg.elastic.models.search;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;

public class SearchResult {
    private List<ElasticDossier> results = new ArrayList<>();
    private Long count;
    private Boolean maxResultCountExceeded = false;
    private Map<String, List<ParsedStringTerms>> facets = new HashMap<>();

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

    public List<ParsedStringTerms> getFacetEntries(FacetEnum facetKey) {
        return facets.get(facetKey.toString());
    }

    private void initIfNull(String key) {
        if (facets.get(key) == null) {
            facets.put(key, new ArrayList<>());
        }
    }

    public void addFacetEntry(String key, ParsedStringTerms parsedStringTerms) {
        initIfNull(key);
        facets.get(key).add(parsedStringTerms);
    }

    public void addFacetEntries(String key, List<ParsedStringTerms> parsedStringTerms) {
        initIfNull(key);
        facets.get(key).addAll(parsedStringTerms);
    }
}
