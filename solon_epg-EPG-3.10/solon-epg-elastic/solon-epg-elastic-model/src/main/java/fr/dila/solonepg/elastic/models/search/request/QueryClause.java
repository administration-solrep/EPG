package fr.dila.solonepg.elastic.models.search.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class QueryClause implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String BOOST = "boost";

	@JsonProperty(BOOST)
	private double boost;

	protected QueryClause(double boost) {
		this.boost = boost;
	}

	public double getBoost() {
		return boost;
	}

	public void setBoost(double boost) {
		this.boost = boost;
	}

}
