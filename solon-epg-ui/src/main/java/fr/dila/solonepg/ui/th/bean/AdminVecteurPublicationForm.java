package fr.dila.solonepg.ui.th.bean;

import static fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DEBUT;
import static fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_FIN;
import static fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_INTITULE;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class AdminVecteurPublicationForm extends AbstractSortablePaginationForm {
    @QueryParam("intitule")
    private SortOrder intitule;

    @QueryParam("dateDebut")
    private SortOrder dateDebut;

    @QueryParam("dateFin")
    private SortOrder dateFin;

    public AdminVecteurPublicationForm() {
        super();
    }

    public SortOrder getIntitule() {
        return intitule;
    }

    public void setIntitule(SortOrder intitule) {
        this.intitule = intitule;
    }

    public SortOrder getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(SortOrder dateDebut) {
        this.dateDebut = dateDebut;
    }

    public SortOrder getDateFin() {
        return dateFin;
    }

    public void setDateFin(SortOrder dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();

        map.put(VECTEUR_PUBLICATION_INTITULE, new FormSort(intitule));
        map.put(VECTEUR_PUBLICATION_DEBUT, new FormSort(dateDebut));
        map.put(VECTEUR_PUBLICATION_FIN, new FormSort(dateFin));

        return map;
    }

    @Override
    protected void setDefaultSort() {
        intitule = SortOrder.DESC;
    }

    public static AdminVecteurPublicationForm newForm() {
        return initForm(new AdminVecteurPublicationForm());
    }
}
