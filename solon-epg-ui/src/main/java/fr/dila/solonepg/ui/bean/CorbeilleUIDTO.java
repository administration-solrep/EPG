package fr.dila.solonepg.ui.bean;

import java.util.List;

public class CorbeilleUIDTO {
    /**
     * Type de noeud : SECTION ou CORBEILLE.
     */
    private String type;

    /**
     * Identifiant technique du noeud.
     */
    private String name;

    /**
     * Libellé du noeud.
     */
    private String label;

    /**
     * Description du noeud.
     */
    private String description;

    private String count;

    private Boolean dynamic;

    /**
     * Liste des corbeilles contenues dans cette section (uniquement pour les noeuds
     * de type SECTION).
     */
    private List<CorbeilleUIDTO> corbeilleNodeList;

    public CorbeilleUIDTO() {
        super();
    }

    public CorbeilleUIDTO(
        String type,
        String name,
        String label,
        String description,
        String count,
        List<CorbeilleUIDTO> corbeilleNodeList,
        boolean opened
    ) {
        super();
        this.type = type;
        this.name = name;
        this.label = label;
        this.description = description;
        this.count = count;
        this.corbeilleNodeList = corbeilleNodeList;
        this.opened = opened;
    }

    /**
     * noeud ouvert
     */
    private boolean opened;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public List<CorbeilleUIDTO> getCorbeilleNodeList() {
        return corbeilleNodeList;
    }

    public void setCorbeilleNodeList(List<CorbeilleUIDTO> corbeilleNodeList) {
        this.corbeilleNodeList = corbeilleNodeList;
    }

    public Boolean getDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }
}
