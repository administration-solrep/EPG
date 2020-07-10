package fr.dila.solonmgpp.web.tree;

import java.util.ArrayList;
import java.util.List;

import fr.dila.solonmgpp.api.dto.EvenementDTO;

public class HistoriqueDataNode {

    private String label;
    private EvenementDTO evenementDTO;
    private Boolean evenementCourant = false;
    private List<HistoriqueDataNode> children = new ArrayList<HistoriqueDataNode>();
    private Boolean opened = true;

    boolean isOpened() {
        return opened;
    }

    void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getEvenementCourant() {
        return evenementCourant;
    }

    public void setEvenementCourant(Boolean evenementCourant) {
        this.evenementCourant = evenementCourant;
    }

    public HistoriqueDataNode(String label, Boolean evenementCourant, EvenementDTO evenementDTO) {
        super();
        this.label = label;
        this.evenementCourant = evenementCourant;
        this.evenementDTO = evenementDTO;
    }

    public void setEvenementDTO(EvenementDTO evenementDTO) {
        this.evenementDTO = evenementDTO;
    }

    public EvenementDTO getEvenementDTO() {
        return evenementDTO;
    }

    public void setChildren(List<HistoriqueDataNode> children) {
        this.children = children;
    }

    public List<HistoriqueDataNode> getChildren() {
        return children;
    }

    public void addChild(int index, HistoriqueDataNode node) {
        children.add(node);
    }

}
