package fr.dila.solonepg.elastic.models.search;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.dila.solonepg.elastic.models.ElasticHits;
import fr.dila.solonepg.elastic.models.FacetResult;

/**
 * A response of a search request.
 */
public class SearchResponse implements Serializable {

	private static final long	serialVersionUID	= 5826250288379715778L;

	public static final String	TOOK				= "took";
	public static final String	TIMED_OUT			= "timed_out";
	public static final String	HITS				= "hits";
	public static final String	AGGREGATIONS		= "aggregations";

	@JsonProperty(TOOK)
	private Long				took;

	@JsonProperty(TIMED_OUT)
	private Boolean				timed_out;

	@JsonProperty(HITS)
	private ElasticHits			hits;

	@JsonProperty(AGGREGATIONS)
	private FacetResult			aggregations		= new FacetResult();

	public Long getTook() {
		return took;
	}

	public void setTook(Long took) {
		this.took = took;
	}

	public Boolean isTimed_out() {
		return timed_out;
	}

	public void setTimed_out(Boolean timed_out) {
		this.timed_out = timed_out;
	}

	public ElasticHits getHits() {
		return hits;
	}

	public void setHits(ElasticHits hits) {
		this.hits = hits;
	}

	public FacetResult getAggregations() {
		return aggregations;
	}

	public void setAggregations(FacetResult aggregations) {
		this.aggregations = aggregations;
	}

}
