package fr.dila.solonepg.elastic.models.search.request;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BoolQuerySerializer.class)
public class BoolQuery extends QueryClause {

	private static final long serialVersionUID = 1L;

	public static final String BOOL = "bool";
	public static final String MUST = "must";
	public static final String SHOULD = "should";
	public static final String DISABLE_COORD = "disable_coord";
	public static final String ADJUST_PURE_NEGATIVE = "adjust_pure_negative";

	private List<QueryClause> mustSubClauses;

	private List<QueryClause> shouldSubClauses;

	private Boolean disableCoord;

	private Boolean adjustPureNegative;

	public BoolQuery(double boost, Boolean disableCoord, Boolean adjustPureNegative) {
		super(boost);
		this.disableCoord = disableCoord;
		this.adjustPureNegative = adjustPureNegative;
	}

	public List<QueryClause> getMustSubClauses() {
		return mustSubClauses;
	}

	public void setMustSubClauses(List<QueryClause> mustSubQueries) {
		this.mustSubClauses = mustSubQueries;
	}

	public List<QueryClause> getShouldSubClauses() {
		return shouldSubClauses;
	}

	public void setShouldSubClauses(List<QueryClause> shouldSubQueries) {
		this.shouldSubClauses = shouldSubQueries;
	}

	public Boolean getDisableCoord() {
		return disableCoord;
	}

	public void setDisableCoord(boolean disableCoord) {
		this.disableCoord = disableCoord;
	}

	public Boolean getAdjustPureNegative() {
		return adjustPureNegative;
	}

	public void setAdjustPureNegative(boolean adjustPureNegative) {
		this.adjustPureNegative = adjustPureNegative;
	}
}
