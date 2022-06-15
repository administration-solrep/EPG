package fr.dila.solonepg.ui.th.bean;

import com.google.gson.Gson;
import fr.dila.solonepg.ui.bean.MgppSuiviEcheancesList;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;

public class MgppSuiviEcheancesListForm extends AbstractSortablePaginationForm {
    private SortOrder norTri;

    private SortOrder objetTri;

    private SortOrder dateDepotEffectifTri;

    private SortOrder destinataire1RapportTri;

    @SuppressWarnings("unchecked")
    public MgppSuiviEcheancesListForm(String json) {
        super();
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);

        if (map != null) {
            setNorTri(SortOrder.fromValue((String) map.get(MgppSuiviEcheancesList.NOR)));
            setObjetTri(SortOrder.fromValue((String) map.get(MgppSuiviEcheancesList.OBJET)));
            setDateDepotEffectifTri(SortOrder.fromValue((String) map.get(MgppSuiviEcheancesList.DATE_DEPOT_EFFECTIF)));
            setDestinataire1RapportTri(
                SortOrder.fromValue((String) map.get(MgppSuiviEcheancesList.DESTINATAIRE_1_RAPPORT))
            );
            setPage((String) map.get(PAGE_PARAM_NAME));
            setSize((String) map.get(SIZE_PARAM_NAME));
        }
    }

    public SortOrder getNorTri() {
        return norTri;
    }

    public void setNorTri(SortOrder norTri) {
        this.norTri = norTri;
    }

    public SortOrder getObjetTri() {
        return objetTri;
    }

    public void setObjetTri(SortOrder objetTri) {
        this.objetTri = objetTri;
    }

    public SortOrder getDateDepotEffectifTri() {
        return dateDepotEffectifTri;
    }

    public void setDateDepotEffectifTri(SortOrder dateDepotEffectifTri) {
        this.dateDepotEffectifTri = dateDepotEffectifTri;
    }

    public SortOrder getDestinataire1RapportTri() {
        return destinataire1RapportTri;
    }

    public void setDestinataire1RapportTri(SortOrder destinataire1RapportTri) {
        this.destinataire1RapportTri = destinataire1RapportTri;
    }

    @Override
    protected void setDefaultSort() {
        // no default sort
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(FichePresentationDRImpl.ID_DOSSIER, new FormSort(norTri));
        map.put(FichePresentationDRImpl.OBJET, new FormSort(objetTri));
        map.put(FichePresentationDRImpl.DATE_DEPOT_EFFECTIF, new FormSort(dateDepotEffectifTri));
        map.put(FichePresentationDRImpl.DESTINATAIRE_1_RAPPORT + "/0", new FormSort(destinataire1RapportTri));
        return map;
    }
}
