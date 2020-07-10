package fr.dila.solonepg.elastic.models.search.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SortElementSerializer.class)
public class SortElement {
	private String type;
	private boolean desc;

	public SortElement(String pType, boolean pDesc) {
		this.type = pType;
		this.desc = pDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}
}
