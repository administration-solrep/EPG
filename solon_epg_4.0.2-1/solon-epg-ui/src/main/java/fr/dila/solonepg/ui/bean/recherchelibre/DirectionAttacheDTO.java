package fr.dila.solonepg.ui.bean.recherchelibre;

import java.util.List;

public class DirectionAttacheDTO {
    private List<FiltreItemDTO> items;

    public DirectionAttacheDTO() {}

    public DirectionAttacheDTO(List<FiltreItemDTO> items) {
        this.items = items;
    }

    public List<FiltreItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FiltreItemDTO> items) {
        this.items = items;
    }
}
