package fr.dila.solonepg.web.suivi;

import fr.dila.st.api.organigramme.OrganigrammeType;

public enum EspaceSuiviInfocentreSggEnum {
    
    GESTION_DELISTE("GESTION_DE_LISTE","espace.suivi.infocentreSGG.gestionDeListe",null, OrganigrammeType.OTHER),
    SIGNALE("SIGNALE","espace.suivi.infocentreSGG.textes.signale",null, OrganigrammeType.OTHER);
    
    private String id;
    private String label;
    private String right;
    private OrganigrammeType type;
    
    private EspaceSuiviInfocentreSggEnum(String id, String label, String right,OrganigrammeType type) {
        this.id = id;
        this.label = label;
        this.right = right;
        this.type=type;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getRight() {
        return right;
    }
    public void setRight(String right) {
        this.right = right;
    }

    public OrganigrammeType getType() {
        return type;
    }

    public void setType(OrganigrammeType type) {
        this.type = type;
    }
}
