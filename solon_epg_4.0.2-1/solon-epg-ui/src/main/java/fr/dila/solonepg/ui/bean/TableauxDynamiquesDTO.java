package fr.dila.solonepg.ui.bean;

import fr.dila.st.core.client.AbstractMapDTO;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.platform.actions.Action;

public class TableauxDynamiquesDTO extends AbstractMapDTO {
    private static final long serialVersionUID = -6153200722514201192L;

    private static final String ID_TD = "id";
    private static final String LIBELLE_TD = "label";
    private static final String LINK_TD = "link";
    private static final String ACTIONS = "actions";

    public TableauxDynamiquesDTO() {
        super();
    }

    public TableauxDynamiquesDTO(String id, String label, String link, List<Action> actions) {
        super();
        setId(id);
        setLabel(label);
        setLink(link);
        setActions(actions);
    }

    public String getId() {
        return getString(ID_TD);
    }

    public void setId(String id) {
        put(ID_TD, id);
    }

    public String getLabel() {
        return getString(LIBELLE_TD);
    }

    public void setLabel(String label) {
        put(LIBELLE_TD, label);
    }

    public String getLink() {
        return getString(LINK_TD);
    }

    public void setLink(String link) {
        put(LINK_TD, link);
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
        return "tableauxDynamiques";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
