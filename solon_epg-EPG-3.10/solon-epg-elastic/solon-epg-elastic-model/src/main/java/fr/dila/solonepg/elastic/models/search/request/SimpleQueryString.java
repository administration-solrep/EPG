package fr.dila.solonepg.elastic.models.search.request;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SimpleQueryStringSerializer.class)
public class SimpleQueryString extends QueryClause {

	private static final long serialVersionUID = 1L;

	public static final String SIMPLE_QUERY_STRING = "simple_query_string";
	public static final String QUERY = "query";
	public static final String FIELDS = "fields";
	public static final String FLAGS = "flags";
	public static final String DEFAULT_OPERATOR = "default_operator";
	public static final String LENIENT = "lenient";
	public static final String ANALYZE_WILDCARD = "analyze_wildcard";

	private String query;

	private List<String> fields;

	private int flags;

	private String defaultOperator;

	private boolean lenient;

	private boolean analyzeWildcard;

	public SimpleQueryString(double boost, String query, List<String> fields, int flags, String defaultOperator,
			boolean lenient, boolean analyzeWildcard) {
		super(boost);
		this.query = query;
		this.fields = fields;
		this.flags = flags;
		this.defaultOperator = defaultOperator;
		this.lenient = lenient;
		this.analyzeWildcard = analyzeWildcard;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public String getDefaultOperator() {
		return defaultOperator;
	}

	public void setDefaultOperator(String defaultOperator) {
		this.defaultOperator = defaultOperator;
	}

	public boolean isLenient() {
		return lenient;
	}

	public void setLenient(boolean lenient) {
		this.lenient = lenient;
	}

	public boolean isAnalyzeWildcard() {
		return analyzeWildcard;
	}

	public void setAnalyzeWildcard(boolean analyzeWildcard) {
		this.analyzeWildcard = analyzeWildcard;
	}
}

