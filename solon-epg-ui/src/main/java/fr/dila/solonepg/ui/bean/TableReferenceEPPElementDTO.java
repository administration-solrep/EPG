package fr.dila.solonepg.ui.bean;

import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.dila.st.api.organigramme.UserNode;
import fr.dila.st.ui.bean.TreeElementDTO;
import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.platform.actions.Action;

public class TableReferenceEPPElementDTO extends TreeElementDTO {
    private TableReferenceEppNode tableReferenceEppNode;
    private UserNode currentUser;
    private List<UserNode> lstUserNode;
    private String ministereId;
    private TableReferenceEPPElementDTO parent;
    private String type;
    private Boolean isActive = true;
    private String lockDate;
    private String lockUserName;
    private MgppReferenceNode organigrammeNode;
    /**
     * Actions disponibles portant sur ce noeud.
     */
    private List<Action> actions;

    public TableReferenceEPPElementDTO() {
        super();
    }

    public TableReferenceEPPElementDTO(UserNode userNode) {
        setChilds(new ArrayList<>());
        setCurrentUser(userNode);
        lstUserNode = new ArrayList<>();
        lstUserNode.add(userNode);
        setIsLastLevel(true);
    }

    public UserNode getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserNode currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            setKey(currentUser.getId());
            setLabel(currentUser.getLabel());
            setType(currentUser.getType().getValue());
            setIsActive(currentUser.isActive());
        } else {
            setKey("");
            setLabel("");
            setType("");
            setIsActive(true);
        }
    }

    public TableReferenceEppNode getTableReferenceEppNode() {
        return tableReferenceEppNode;
    }

    public void setTableReferenceEppNode(TableReferenceEppNode tableReferenceEppNode) {
        this.tableReferenceEppNode = tableReferenceEppNode;
    }

    public String getMinistereId() {
        return ministereId;
    }

    public void setMinistereId(String ministereId) {
        this.ministereId = ministereId;
    }

    public TableReferenceEPPElementDTO getParent() {
        return parent;
    }

    public void setParent(TableReferenceEPPElementDTO parent) {
        this.parent = parent;
        setCompleteKey(String.format("%s__%s", parent.getCompleteKey(), getKey()));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public String getLockDate() {
        return lockDate;
    }

    public void setLockDate(String lockDate) {
        this.lockDate = lockDate;
    }

    public String getLockUserName() {
        return lockUserName;
    }

    public void setLockUserName(String lockUserName) {
        this.lockUserName = lockUserName;
    }

    public MgppReferenceNode getOrganigrammeNode() {
        return organigrammeNode;
    }

    public void setOrganigrammeNode(MgppReferenceNode organigrammeNode) {
        this.organigrammeNode = organigrammeNode;
    }
}
