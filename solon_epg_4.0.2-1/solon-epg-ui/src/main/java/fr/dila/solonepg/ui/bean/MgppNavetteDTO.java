package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.WidgetDTO;
import java.util.ArrayList;
import java.util.List;

public class MgppNavetteDTO {
    private String name;
    private List<WidgetDTO> lstWidgets = new ArrayList<>();

    public MgppNavetteDTO() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WidgetDTO> getLstWidgets() {
        return lstWidgets;
    }

    public void setLstWidgets(List<WidgetDTO> lstWidgets) {
        this.lstWidgets = lstWidgets;
    }
}
