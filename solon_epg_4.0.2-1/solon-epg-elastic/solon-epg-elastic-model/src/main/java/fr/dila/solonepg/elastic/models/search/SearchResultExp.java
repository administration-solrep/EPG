package fr.dila.solonepg.elastic.models.search;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import java.util.LinkedHashMap;

public class SearchResultExp {
    private int pageNum;
    private long count;
    private int pageSize;
    private int numResults;
    private LinkedHashMap<String, ElasticDossier> results = new LinkedHashMap<>();

    public SearchResultExp() {}

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public LinkedHashMap<String, ElasticDossier> getResults() {
        return results;
    }

    public void setResults(LinkedHashMap<String, ElasticDossier> results) {
        this.results = results;
    }
}
