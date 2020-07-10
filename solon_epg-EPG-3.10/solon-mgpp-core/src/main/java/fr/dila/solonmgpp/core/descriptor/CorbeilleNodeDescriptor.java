package fr.dila.solonmgpp.core.descriptor;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Descripteur d'un corbeille spécifique MGPP
 * 
 * @author asatre
 */
@XObject("corbeilleNode")
public class CorbeilleNodeDescriptor {

    /**
     * Identifiant technique du noeud.
     */
    @XNode("@name")
    private String name;

    /**
     * Libellé du noeud.
     */
    @XNode("@label")
    private String label;

    /**
     * Type d'etape de FDR du noeud.
     */
    @XNode("@routingTaskType")
    private String routingTaskType;

    /**
     * Description du noeud.
     */
    @XNode("@description")
    private String description;

    /**
     * TypeActe du noeud.
     */
    @XNode("@typeActe")
    private String typeActe;

    /**
     * restriction visibilité
     */
    @XNode("@ifMember")
    private String ifMember;

    /**
     * type
     */
    @XNode("@type")
    private String type;

    /**
     * adoption
     */
    @XNode("@adoption")
    private String adoption;

    /**
     * Noeud parent du noeud.
     */
    @XNode("@parent")
    private String parent;

    /**
     * Constructeur par défaut de CorbeilleNodeDescriptor.
     */
    public CorbeilleNodeDescriptor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoutingTaskType(String routingTaskType) {
        this.routingTaskType = routingTaskType;
    }

    public String getRoutingTaskType() {
        return routingTaskType;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setIfMember(String ifMember) {
        this.ifMember = ifMember;
    }

    public String getIfMember() {
        return ifMember;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getAdoption() {
        return adoption;
    }

    public Boolean isAdoption() {
        return "1".equals(adoption);
    }

    public void setAdoption(String adoption) {
        this.adoption = adoption;
    }

    public String getParent() {
      return parent;
    }

    public void setParent(String parent) {
      this.parent = parent;
    }

}
