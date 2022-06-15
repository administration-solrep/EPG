package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacetResultEntry {
    public static final String KEY = "key";
    public static final String DOC_COUNT = "doc_count";

    @JsonProperty(KEY)
    private String key;

    @JsonProperty(DOC_COUNT)
    private Long doc_count;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDoc_count() {
        return doc_count;
    }

    public void setDoc_count(Long doc_count) {
        this.doc_count = doc_count;
    }
}
