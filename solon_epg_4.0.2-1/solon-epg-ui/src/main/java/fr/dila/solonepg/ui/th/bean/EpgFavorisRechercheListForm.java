package fr.dila.solonepg.ui.th.bean;

import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_XPATH;
import static fr.dila.st.api.constant.STSchemaConstant.DUBLINCORE_CREATOR_XPATH;
import static fr.dila.st.api.constant.STSchemaConstant.DUBLINCORE_TITLE_XPATH;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class EpgFavorisRechercheListForm extends AbstractSortablePaginationForm {
    public static final String INTITULE_SORT_NAME = "intituleSort";
    public static final String TYPE_SORT_NAME = "typeSort";
    public static final String CREATEUR_SORT_NAME = "createurSort";

    @QueryParam(INTITULE_SORT_NAME)
    private SortOrder intituleSort;

    @QueryParam(TYPE_SORT_NAME)
    private SortOrder typeSort;

    @QueryParam(CREATEUR_SORT_NAME)
    private SortOrder createurSort;

    public EpgFavorisRechercheListForm() {
        super();
    }

    public SortOrder getIntituleSort() {
        return intituleSort;
    }

    public void setIntituleSort(SortOrder intituleSort) {
        this.intituleSort = intituleSort;
    }

    public SortOrder getTypeSort() {
        return typeSort;
    }

    public void setTypeSort(SortOrder typeSort) {
        this.typeSort = typeSort;
    }

    public SortOrder getCreateurSort() {
        return createurSort;
    }

    public void setCreateurSort(SortOrder createurSort) {
        this.createurSort = createurSort;
    }

    @Override
    protected void setDefaultSort() {
        intituleSort = SortOrder.ASC;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(DUBLINCORE_TITLE_XPATH, new FormSort(getIntituleSort()));
        map.put(FAVORIS_RECHERCHE_TYPE_XPATH, new FormSort(getTypeSort()));
        map.put(DUBLINCORE_CREATOR_XPATH, new FormSort(getCreateurSort()));
        return map;
    }
}
