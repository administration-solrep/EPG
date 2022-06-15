package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.QueryParam;

@SwBean
public class MgppFichePresentationOEPForm extends AbstractSortablePaginationForm {
    @QueryParam("nom")
    private SortOrder nom;

    @QueryParam("ministere")
    private SortOrder ministere;

    @QueryParam("date")
    private SortOrder date;

    public MgppFichePresentationOEPForm() {}

    @Override
    protected void setDefaultSort() {}

    public SortOrder getNom() {
        return nom;
    }

    public void setNom(SortOrder nom) {
        this.nom = nom;
    }

    public SortOrder getMinistere() {
        return ministere;
    }

    public void setMinistere(SortOrder ministere) {
        this.ministere = ministere;
    }

    public SortOrder getDate() {
        return date;
    }

    public void setDate(SortOrder date) {
        this.date = date;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(FichePresentationOEPImpl.NOM_ORGANISME, new FormSort(nom));
        map.put(FichePresentationOEPImpl.MINISTERE_RATTACHEMENT, new FormSort(ministere));
        map.put(FichePresentationOEPImpl.DATE, new FormSort(date));
        return map;
    }
}
