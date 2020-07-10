package fr.dila.solonepg.elastic.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElasticHit implements Serializable {

	private static final long	serialVersionUID	= -667527154984411616L;

	public static final String	INDEX				= "_index";
	public static final String	TYPE				= "_type";
	public static final String	ID					= "_id";
	public static final String	SCORE				= "_score";
	public static final String	SOURCE				= "_source";
	public static final String	FIELDS				= "fields";
	public static final String	INNER_HITS			= "inner_hits";
	public static final String	HIGHLIGHT			= "highlight";

	@JsonProperty(INDEX)
	private String				index;

	@JsonProperty(TYPE)
	private String				type;

	@JsonProperty(ID)
	private String				id;

	@JsonProperty(SCORE)
	private Long				score;

	// TODO Statuer sur le role des deux champs suivants
	@JsonProperty(SOURCE)
	private ElasticDossier		source;

	@JsonProperty(FIELDS)
	private ElasticFields		fields;

	@JsonProperty(INNER_HITS)
	private InnerHits			inner_hits;

	@JsonProperty(HIGHLIGHT)
	private ElasticHighlight	highlight;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public ElasticDossier getSource() {
		return source;
	}

	public void setSource(ElasticDossier source) {
		this.source = source;
	}

	public ElasticFields getFields() {
		return fields;
	}

	public void setFields(ElasticFields fields) {
		this.fields = fields;
	}

	public InnerHits getInner_hits() {
		return inner_hits;
	}

	public void setInner_hits(InnerHits inner_hits) {
		this.inner_hits = inner_hits;
	}

	public ElasticHighlight getHighlight() {
		return highlight;
	}

	public void setHighlight(ElasticHighlight highlight) {
		this.highlight = highlight;
	}

}
