package fr.dila.solonepg.ui.bean;

public class RechercheDossiersParlementairesDTO {
    private String name;

    private String label;

    private boolean visible;

    public RechercheDossiersParlementairesDTO() {
        super();
    }

    public RechercheDossiersParlementairesDTO(String name, String label, boolean visible) {
        super();
        this.name = name;
        this.label = label;
        this.visible = visible;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
