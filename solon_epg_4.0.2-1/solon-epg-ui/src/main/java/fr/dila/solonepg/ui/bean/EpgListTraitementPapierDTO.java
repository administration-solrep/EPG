package fr.dila.solonepg.ui.bean;

public class EpgListTraitementPapierDTO {
    private String id;
    private String titre;
    private String type;

    public EpgListTraitementPapierDTO(String id, String titre, String type) {
        this.id = id;
        this.titre = titre;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
