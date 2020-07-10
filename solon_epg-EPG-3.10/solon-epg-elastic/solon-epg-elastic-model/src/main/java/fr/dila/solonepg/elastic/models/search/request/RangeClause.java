package fr.dila.solonepg.elastic.models.search.request;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = RangeClauseSerializer.class)
public class RangeClause extends QueryClause {

	private static final long serialVersionUID = 1L;

	public static final String RANGE = "range";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String INCLUDE_LOWER = "include_lower";
	public static final String INCLUDE_UPPER = "include_upper";

	private String type;

	private Date from;

	private Date to;

	private boolean includeLower;

	private boolean includeUpper;

	public RangeClause(double boost, String type, Date from, Date to, boolean includeLower, boolean includeUpper) {
		super(boost);
		this.type = type;
		this.from = from;
		this.to = to;
		this.includeLower = includeLower;
		this.includeUpper = includeUpper;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public boolean isIncludeLower() {
		return includeLower;
	}

	public void setIncludeLower(boolean includeLower) {
		this.includeLower = includeLower;
	}

	public boolean isIncludeUpper() {
		return includeUpper;
	}

	public void setIncludeUpper(boolean includeUpper) {
		this.includeUpper = includeUpper;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
