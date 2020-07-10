package fr.dila.solonepg.elastic.models.search.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TermClauseSerializer.class)
public class TermClause extends QueryClause {

	private static final long serialVersionUID = 1L;

	public static final String TERM = "term";

	public static final String VALUE = "value";

	private String type;

	@JsonProperty(VALUE)
	private String value;

	public TermClause(double boost, String type, String value) {
		super(boost);
		this.type = type;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
