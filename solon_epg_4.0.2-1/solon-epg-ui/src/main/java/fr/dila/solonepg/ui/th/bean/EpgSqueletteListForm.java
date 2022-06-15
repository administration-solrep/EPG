package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import java.util.Map;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class EpgSqueletteListForm extends ModeleFDRListForm {
    @FormParam("typeActe")
    private SortOrder typeActe;

    @FormParam("typeActeOrder")
    private Integer typeActeOrder;

    public EpgSqueletteListForm() {
        super();
    }

    public SortOrder getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(SortOrder typeActe) {
        this.typeActe = typeActe;
    }

    public Integer getTypeActeOrder() {
        return typeActeOrder;
    }

    public void setTypeActeOrder(Integer typeActeOrder) {
        this.typeActeOrder = typeActeOrder;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = super.getSortForm();

        map.put(SolonEpgSchemaConstant.SOLON_EPG_TYPE_ACTE_FEUILLE_ROUTE_XPATH, new FormSort(typeActe, typeActeOrder));

        return map;
    }
}
