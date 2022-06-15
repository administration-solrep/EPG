package fr.dila.solonepg.ui.bean.recherchelibre;

import java.util.List;

public class StatutDTO {
    private List<FiltreItemDTO> items;

    public StatutDTO() {}

    public StatutDTO(List<FiltreItemDTO> items) {
        this.items = items;
    }

    public List<FiltreItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FiltreItemDTO> items) {
        this.items = items;
    }
}
