package fr.dila.solonepg.elastic.models.search.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = NestedClauseSerializer.class)
public class NestedClause extends QueryClause {

	private static final long serialVersionUID = 1L;

	public static final String NESTED = "nested";
	public static final String QUERY = "query";
	public static final String PATH = "path";
	public static final String IGNORE_UNMAPPED = "ignore_unmapped";
	public static final String SCORE_MODE = "score_mode";
	public static final String INNER_HITS = "inner_hits";

	private BoolQuery query;

	private String path;

	private boolean ignoreUnmapped;

	private String scoreMode;

	private InnerHits innerHits;

	public NestedClause(double boost, String path, boolean ignoreUnmapped, String scoreMode) {
		super(boost);
		this.path = path;
		this.ignoreUnmapped = ignoreUnmapped;
		this.scoreMode = scoreMode;
	}

	public BoolQuery getQuery() {
		return query;
	}

	public void setQuery(BoolQuery query) {
		this.query = query;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isIgnoreUnmapped() {
		return ignoreUnmapped;
	}

	public void setIgnoreUnmapped(boolean ignoreUnmapped) {
		this.ignoreUnmapped = ignoreUnmapped;
	}

	public String getScoreMode() {
		return scoreMode;
	}

	public void setScoreMode(String scoreMode) {
		this.scoreMode = scoreMode;
	}

	public InnerHits getInnerHits() {
		return innerHits;
	}

	public void setInnerHits(InnerHits innerHits) {
		this.innerHits = innerHits;
	}

}
