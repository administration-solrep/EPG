package fr.dila.solonepg.elastic.models.search;

import java.util.Collection;
import java.util.LinkedHashMap;

public class SearchCriteriaExp extends AbstractCriteria {
    private LinkedHashMap<String, String> tris;
    private Collection<ClauseOu> clausesOu;

    public SearchCriteriaExp() {}

    public LinkedHashMap<String, String> getTris() {
        return tris;
    }

    public void setTris(LinkedHashMap<String, String> tris) {
        this.tris = tris;
    }

    public Collection<ClauseOu> getClausesOu() {
        return clausesOu;
    }

    public void setClausesOu(Collection<ClauseOu> clausesOu) {
        this.clausesOu = clausesOu;
    }
}
