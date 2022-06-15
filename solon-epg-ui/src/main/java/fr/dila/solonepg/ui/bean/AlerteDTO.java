package fr.dila.solonepg.ui.bean;

import fr.dila.st.core.client.AbstractMapDTO;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.platform.actions.Action;

public class AlerteDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 1L;
    private static final String ID = "id";
    private static final String LABEL = "label";
    private static final String LINK = "link";
    private static final String ACTIVATED = "activated";
    private static final String ACTIONS = "alerteActions";

    public AlerteDTO() {
        super();
    }

    public AlerteDTO(String id, String label, String link, boolean activated, List<Action> actions) {
        super();
        setId(id);
        setLabel(label);
        setLink(link);
        setActivated(activated);
        setActions(actions);
    }

    public String getId() {
        return getString(ID);
    }

    public void setId(String id) {
        put(ID, id);
    }

    public String getLabel() {
        return getString(LABEL);
    }

    public void setLabel(String label) {
        put(LABEL, label);
    }

    public String getLink() {
        return getString(LINK);
    }

    public void setLink(String link) {
        put(LINK, link);
    }

    public void setActivated(boolean suspendu) {
        put(ACTIVATED, suspendu);
    }

    public boolean isActivated() {
        return getBoolean(ACTIVATED);
    }

    @SuppressWarnings("unchecked")
    public List<Action> getActions() {
        return (List<Action>) get(ACTIONS);
    }

    public void setActions(List<Action> actions) {
        put(ACTIONS, (Serializable) actions);
    }

    @Override
    public String getType() {
        return "alertes";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
