package fr.dila.solonepg.ui.bean.recherchelibre;

import java.util.List;

public class MinistereAttacheDTO {
    private List<FiltreItemDTO> items;

    public MinistereAttacheDTO() {}

    public MinistereAttacheDTO(List<FiltreItemDTO> items) {
        this.items = items;
    }

    public List<FiltreItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FiltreItemDTO> items) {
        this.items = items;
    }
}
