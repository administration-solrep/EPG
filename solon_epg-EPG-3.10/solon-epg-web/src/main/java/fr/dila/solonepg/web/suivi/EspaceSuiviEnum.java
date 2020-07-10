package fr.dila.solonepg.web.suivi;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.st.api.organigramme.OrganigrammeType;

public enum EspaceSuiviEnum {
	
	MON_INFOCENTRE("MON_INFOCENTRE", "espace.suivi.moninfocentre", null, OrganigrammeType.BASE),
	INFOCENTRE_GENERAL("INFOCENTRE_GENERAL", "espace.suivi.infocentregeneral", SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_READER, OrganigrammeType.BASE),
	INFOCENTRE_SGG("INFOCENTRE_SGG", "espace.suivi.infocentreSGG", SolonEpgBaseFunctionConstant.INFOCENTRE_SGG_READER , OrganigrammeType.BASE),
	TABLEAU_DYNAMIQUE("TABLEAU_DYNAMIQUE", "espace.suivi.tableuxdynamiques", null, OrganigrammeType.BASE),
	DOSSIER_SIGNALE("INFOCENTRE_GENERAL", "espace.suivi.dossiersignale", null, OrganigrammeType.OTHER),
	MES_ALERTES("MES_ALERTES", "espace.suivi.mesalertes", null, OrganigrammeType.OTHER),
	STATISTIQUES("STATISTIQUES", "espace.suivi.statistiques", null, OrganigrammeType.OTHER);
	
	private String id;
	private String label;
	private String right;
	private OrganigrammeType type;

	EspaceSuiviEnum(String id, String label, String right, OrganigrammeType type){
		this.id = id;
		this.label = label;
		this.right = right;
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getRight() {
		return right;
	}

	public void setType(OrganigrammeType type) {
		this.type = type;
	}

	public OrganigrammeType getType() {
		return type;
	}

}
