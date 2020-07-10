package fr.dila.solonepg.elastic.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

public class ElasticHits implements Serializable {

	private static final long	serialVersionUID	= 6853423885546800116L;

	public static final String	TOTAL				= "total";
	public static final String	MAX_SCORE			= "max_score";
	public static final String	HITS				= "hits";

	@JsonProperty(TOTAL)
	private Long				total;

	@JsonProperty(MAX_SCORE)
	private BigDecimal			max_score;

	@JsonProperty(HITS)
	private List<ElasticHit>	hits;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public BigDecimal getMax_score() {
		return max_score;
	}

	public void setMax_score(BigDecimal max_score) {
		this.max_score = max_score;
	}

	public List<ElasticHit> getHits() {
		return hits;
	}

	public void setHits(List<ElasticHit> hits) {
		this.hits = hits;
	}

}
