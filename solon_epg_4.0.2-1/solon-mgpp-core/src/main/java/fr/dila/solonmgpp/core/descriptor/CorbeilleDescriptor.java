package fr.dila.solonmgpp.core.descriptor;

import java.util.ArrayList;
import java.util.List;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Descripteur d'une collection de corbeille.
 *
 * @author asatre
 */
@XObject("corbeille")
public class CorbeilleDescriptor {
    /**
     * Identifiant technique de la corbeille.
     */
    @XNode("@name")
    private String name;

    /**
     * Libellé du noeud.
     */
    @XNode("@label")
    private String label;

    /**
     * Action du noeud.
     */
    @XNode("@action")
    private String action;

    /**
     * Liste des noeuds de la corbeille.
     */
    @XNodeList(value = "corbeilleNode", type = ArrayList.class, componentType = CorbeilleNodeDescriptor.class)
    private List<CorbeilleNodeDescriptor> corbeilleNodeList;

    /**
     * Constructeur par défaut de CorbeilleInstitutionDescriptor.
     */
    public CorbeilleDescriptor() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setCorbeilleNodeList(List<CorbeilleNodeDescriptor> corbeilleNodeList) {
        this.corbeilleNodeList = corbeilleNodeList;
    }

    public List<CorbeilleNodeDescriptor> getCorbeilleNodeList() {
        return corbeilleNodeList;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
