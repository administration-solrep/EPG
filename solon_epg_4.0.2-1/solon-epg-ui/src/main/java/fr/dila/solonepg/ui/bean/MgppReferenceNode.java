package fr.dila.solonepg.ui.bean;

public class MgppReferenceNode {
    private String id;
    private String type;
    private String ministereId;

    public MgppReferenceNode(String id, String type, String ministereId) {
        super();
        this.id = id;
        this.type = type;
        this.ministereId = ministereId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinistereId() {
        return ministereId;
    }

    public void setMinistereId(String ministereId) {
        this.ministereId = ministereId;
    }
}
