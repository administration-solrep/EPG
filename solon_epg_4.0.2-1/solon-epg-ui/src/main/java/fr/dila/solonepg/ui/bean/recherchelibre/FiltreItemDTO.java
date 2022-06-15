package fr.dila.solonepg.ui.bean.recherchelibre;

public class FiltreItemDTO {
    private String id;

    private String name;

    private int nbElement;

    public FiltreItemDTO() {}

    public FiltreItemDTO(String id, String name, int nbElement) {
        this.id = id;
        this.name = name;
        this.nbElement = nbElement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbElement() {
        return nbElement;
    }

    public void setNbElement(int nbElement) {
        this.nbElement = nbElement;
    }
}
