package fr.dila.solonepg.elastic.models.search.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonPropertyOrder({ "requireFieldMatch", "type", "fields" })
public class Highlight implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String REQUIRE_FIELD_MATCH = "require_field_match";

	public static final String TYPE_PLAIN = "plain";
	public static final String TYPE_UNIFIED = "unified";
	
	@JsonProperty(REQUIRE_FIELD_MATCH)
	private Boolean requireFieldMatch;
	
	@JsonProperty
	private String type;
	
	@JsonSerialize(using = HighlightFieldsSerializer.class)
	private List<HighlightField> fields;

	public Highlight(Boolean requireFieldMatch) {
		this.requireFieldMatch = requireFieldMatch;
		this.type = TYPE_PLAIN;
	}

	public Boolean getRequireFieldMatch() {
		return requireFieldMatch;
	}

	public void setRequireFieldMatch(Boolean requireFieldMatch) {
		this.requireFieldMatch = requireFieldMatch;
	}

	public List<HighlightField> getFields() {
		return fields;
	}

	public void setFields(List<HighlightField> fields) {
		this.fields = fields;
	}
}
