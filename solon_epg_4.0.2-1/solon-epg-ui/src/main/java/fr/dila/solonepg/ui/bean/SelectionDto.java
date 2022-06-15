package fr.dila.solonepg.ui.bean;

import fr.dila.ss.api.recherche.IdLabel;
import java.util.ArrayList;
import java.util.List;

public class SelectionDto {
    private String id;
    private String label;
    private List<IdLabel> caseLinkIdsLabels = new ArrayList<>();

    public SelectionDto(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public SelectionDto(String id, String label, List<IdLabel> caseLinkIdsLabels) {
        this(id, label);
        this.caseLinkIdsLabels = caseLinkIdsLabels;
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

    public List<IdLabel> getCaseLinkIdsLabels() {
        return caseLinkIdsLabels;
    }

    public void setCaseLinkIdsLabels(List<IdLabel> caseLinkIdsLabels) {
        this.caseLinkIdsLabels = caseLinkIdsLabels;
    }
}
