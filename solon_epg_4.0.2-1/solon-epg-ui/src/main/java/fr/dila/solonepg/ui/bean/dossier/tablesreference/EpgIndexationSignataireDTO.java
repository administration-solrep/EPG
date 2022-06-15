package fr.dila.solonepg.ui.bean.dossier.tablesreference;

import org.nuxeo.ecm.platform.actions.Action;

public class EpgIndexationSignataireDTO {
    private String id;

    private String label;

    private Action action;

    public EpgIndexationSignataireDTO() {}

    public EpgIndexationSignataireDTO(String id, String label, Action action) {
        this.id = id;
        this.label = label;
        this.action = action;
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
