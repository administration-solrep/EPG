package fr.dila.solonepg.ui.bean;

public class SuiviLibreReportDTO {
    private String titre;
    private String link;
    private boolean available;

    public SuiviLibreReportDTO() {}

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
