package fr.dila.solonepg.web.suivi;

import java.util.ArrayList;
import java.util.List;

import fr.dila.st.api.dossier.STDossier.DossierState;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;

/**
 * Noeud pour un element
 * 
 * @author asatre
 * 
 */
public class EspaceSuiviTreeNode {

    /**
     * clé identifiant
     */
    private final String key;

    /**
     * nombre à afficher de dossier
     */
    private String count;

    public Boolean opened;

    private Boolean loaded;

    private final String label;

    private final String query;

    private final OrganigrammeType type;

    private OrganigrammeNode node;

    private EspaceSuiviTreeNode parent;

    private List<EspaceSuiviTreeNode> children;

    private EspaceSuiviRoleEnum role;

    private DossierState dossierState;

    public EspaceSuiviTreeNode(String label, String key, String query, OrganigrammeType organigrammeType, DossierState dossierState) {
        this.key = key;
        this.label = label;
        this.query = query;
        this.type = organigrammeType;
        this.dossierState = dossierState;
        this.children = new ArrayList<EspaceSuiviTreeNode>();
        opened = Boolean.FALSE;
        loaded = Boolean.FALSE;
        role = EspaceSuiviRoleEnum.BASE;
    }

    public String getKey() {
        return key;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getQuery() {
        return query;
    }

    public Boolean isOpened() {
        return opened;
    }

    public void setOpened(Boolean isOpened) {
        opened = isOpened;
    }

    public Boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    public String getLabel() {
        return label;
    }

    public OrganigrammeType getType() {
        return type;
    }

    public void setNode(OrganigrammeNode node) {
        this.node = node;
    }

    public OrganigrammeNode getNode() {
        return node;
    }

    public List<EspaceSuiviTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<EspaceSuiviTreeNode> chidren) {
        this.children = chidren;
    }

    public void setDossierState(DossierState dossierState) {
        this.dossierState = dossierState;
    }

    public DossierState getDossierState() {
        return dossierState;
    }

    public void setParent(EspaceSuiviTreeNode parent) {
        this.parent = parent;
    }

    public EspaceSuiviTreeNode getParent() {
        return parent;
    }

    public void addChild(EspaceSuiviTreeNode treeNode) {
        children.add(treeNode);
    }

    public OrganigrammeType getParentType() {
        if (parent == null) {
            return OrganigrammeType.POSTE;
        } else {
            return parent.getType();
        }
    }

    public String getCompleteKey() {
        if (parent == null) {
            return getKey();
        } else {
            return parent.getKey() + getKey();
        }
    }

    public void setRole(EspaceSuiviRoleEnum role) {
        this.role = role;
    }

    public EspaceSuiviRoleEnum getRole() {
        return role;
    }

}
