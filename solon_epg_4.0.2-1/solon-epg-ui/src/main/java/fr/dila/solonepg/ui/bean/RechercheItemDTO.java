package fr.dila.solonepg.ui.bean;

import java.util.List;

public class RechercheItemDTO {
    private String label;
    private String icon;
    private boolean isTitle;
    private String url;
    private List<RechercheItemDTO> items;

    public RechercheItemDTO(String label, String icon, String url, boolean isTitle, List<RechercheItemDTO> items) {
        super();
        this.label = label;
        this.icon = icon;
        this.isTitle = isTitle;
        this.items = items;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public List<RechercheItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RechercheItemDTO> items) {
        this.items = items;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
