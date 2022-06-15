package fr.dila.solonepg.ui.bean;

public class SelectionSummaryDTO {
    private String id;
    private String nor;
    private String label;

    public SelectionSummaryDTO() {}

    public SelectionSummaryDTO(String nor, String label) {
        this.nor = nor;
        this.label = label;
    }

    public SelectionSummaryDTO(String nor, String label, String id) {
        this(nor, label);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
