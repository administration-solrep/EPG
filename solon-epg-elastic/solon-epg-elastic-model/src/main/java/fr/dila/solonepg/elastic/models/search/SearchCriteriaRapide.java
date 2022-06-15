package fr.dila.solonepg.elastic.models.search;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchCriteriaRapide extends AbstractCriteria {
    private List<String> wildcardList;
    private Collection<String> colonnes;
    private LinkedHashMap<String, String> tris;

    public SearchCriteriaRapide() {}

    public List<String> getWildcardList() {
        return wildcardList;
    }

    public void setWildcardList(List<String> wildcardList) {
        this.wildcardList = wildcardList;
    }

    public Collection<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(Collection<String> colonnes) {
        this.colonnes = colonnes;
    }

    public LinkedHashMap<String, String> getTris() {
        return tris;
    }

    public void setTris(LinkedHashMap<String, String> tris) {
        this.tris = tris;
    }
}
