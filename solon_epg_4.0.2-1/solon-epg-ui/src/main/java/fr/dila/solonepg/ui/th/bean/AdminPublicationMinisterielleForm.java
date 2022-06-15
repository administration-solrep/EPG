package fr.dila.solonepg.ui.th.bean;

import static fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_INTITULE;
import static fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_NOR;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class AdminPublicationMinisterielleForm extends AbstractSortablePaginationForm {
    @QueryParam("nor")
    private SortOrder nor;

    @QueryParam("intitule")
    private SortOrder intitule;

    public AdminPublicationMinisterielleForm() {
        super();
    }

    public SortOrder getNor() {
        return nor;
    }

    public void setNor(SortOrder nor) {
        this.nor = nor;
    }

    public SortOrder getIntitule() {
        return intitule;
    }

    public void setIntitule(SortOrder intitule) {
        this.intitule = intitule;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();

        map.put(BULLETIN_OFFICIEL_NOR, new FormSort(nor));
        map.put(BULLETIN_OFFICIEL_INTITULE, new FormSort(intitule));

        return map;
    }

    @Override
    protected void setDefaultSort() {
        nor = SortOrder.DESC;
    }

    public static AdminPublicationMinisterielleForm newForm() {
        return initForm(new AdminPublicationMinisterielleForm());
    }
}
