package fr.dila.solonepg.elastic.models.search.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InnerHits implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String IGNORE_UNMAPPED = "ignore_unmapped";
	public static final String FROM = "from";
	public static final String VERSION = "version";
	public static final String EXPLAIN = "explain";
	public static final String TRACK_SCORES = "track_scores";
	public static final String SOURCE = "_source";
	public static final String STORED_FIELDS = "stored_fields";
	public static final String DOCVALUE_FIELDS = "docvalue_fields";
	public static final String HIGHLIGHT = "highlight";

	@JsonProperty(IGNORE_UNMAPPED)
	private boolean ignoreUnmapped;

	@JsonProperty(FROM)
	private int from;

	@JsonProperty(VERSION)
	private boolean version;

	@JsonProperty(EXPLAIN)
	private boolean explain;

	@JsonProperty(TRACK_SCORES)
	private boolean trackScores;

	/**
	 * @see SearchRequest#getSource()
	 */
	@JsonProperty(SOURCE)
	private Boolean source = false;

	/**
	 * @see SearchRequest#getDocvalueFields()
	 */
	@JsonProperty(DOCVALUE_FIELDS)
	private List<String> docvalueFields;

	/**
	 * @see SearchRequest#getStoredFields()
	 */
	@JsonProperty(STORED_FIELDS)
	private List<String> storedFields;

	@JsonProperty(HIGHLIGHT)
	private Highlight highlight;

	public InnerHits(boolean ignoreUnmapped, int from, boolean version, boolean explain, boolean trackScores,
			List<String> storedFields, List<String> docvalueFields) {
		super();
		this.ignoreUnmapped = ignoreUnmapped;
		this.from = from;
		this.version = version;
		this.explain = explain;
		this.trackScores = trackScores;
		this.storedFields = storedFields;
		this.docvalueFields = docvalueFields;
	}

	public boolean isIgnoreUnmapped() {
		return ignoreUnmapped;
	}

	public void setIgnoreUnmapped(boolean ignoreUnmapped) {
		this.ignoreUnmapped = ignoreUnmapped;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public Boolean getSource() {
		return source;
	}

	public void setSource(Boolean source) {
		this.source = source;
	}

	public List<String> getDocvalueFields() {
		return docvalueFields;
	}

	public void setDocvalueFields(List<String> docvalueFields) {
		this.docvalueFields = docvalueFields;
	}

	public List<String> getStoredFields() {
		return storedFields;
	}

	public void setStoredFields(List<String> storedFields) {
		this.storedFields = storedFields;
	}

	public boolean isVersion() {
		return version;
	}

	public void setVersion(boolean version) {
		this.version = version;
	}

	public boolean isExplain() {
		return explain;
	}

	public void setExplain(boolean explain) {
		this.explain = explain;
	}

	public boolean isTrackScores() {
		return trackScores;
	}

	public void setTrackScores(boolean trackScores) {
		this.trackScores = trackScores;
	}

	public Highlight getHighlight() {
		return highlight;
	}

	public void setHighlight(Highlight highlight) {
		this.highlight = highlight;
	}
}
