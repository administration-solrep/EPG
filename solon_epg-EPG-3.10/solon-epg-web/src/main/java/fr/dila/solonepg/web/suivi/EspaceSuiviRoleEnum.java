package fr.dila.solonepg.web.suivi;


public enum EspaceSuiviRoleEnum {
	
	BASE("BASE"),
	EXECUTOR("EXECUTOR"),
	GOD("GOD");
	
	private String id;

	EspaceSuiviRoleEnum(String id){
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	


}
