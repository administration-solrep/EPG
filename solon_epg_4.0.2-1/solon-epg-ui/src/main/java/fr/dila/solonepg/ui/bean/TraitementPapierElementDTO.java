package fr.dila.solonepg.ui.bean;

public class TraitementPapierElementDTO {
    private String id;

    private String intituleMin;

    private String nor;

    private String titreText;

    public TraitementPapierElementDTO() {}

    public TraitementPapierElementDTO(String id, String intituleMin, String nor, String titreText) {
        this.id = id;
        this.intituleMin = intituleMin;
        this.nor = nor;
        this.titreText = titreText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntituleMin() {
        return intituleMin;
    }

    public void setIntitule(String intitule) {
        this.intituleMin = intitule;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getTitreText() {
        return titreText;
    }

    public void setTitreText(String titreText) {
        this.titreText = titreText;
    }
}
