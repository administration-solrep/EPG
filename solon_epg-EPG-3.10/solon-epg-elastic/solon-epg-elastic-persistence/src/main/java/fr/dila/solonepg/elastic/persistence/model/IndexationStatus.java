package fr.dila.solonepg.elastic.persistence.model;

public enum IndexationStatus {

	SUCCESS,
	SUCCESS_WARNING,
	SUCCESS_DELETE,
	ERROR,
	UNKNOWN;

	public boolean isSuccess() {
		return this.equals(SUCCESS) || this.equals(SUCCESS_WARNING) || this.equals(SUCCESS_DELETE);
	}

}
