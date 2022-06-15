package fr.dila.solonepg.ui.bean.recherchelibre;

import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RechercheDossierList {
    private List<RechercheDossierDTO> liste = new ArrayList<>();

    private Map<FacetEnum, List<FiltreItemDTO>> facets = new EnumMap<>(FacetEnum.class);

    private Integer nbTotal;

    public RechercheDossierList() {
        this.nbTotal = 0;
    }

    public List<RechercheDossierDTO> getListe() {
        return liste;
    }

    public void setListe(List<RechercheDossierDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    public Map<FacetEnum, List<FiltreItemDTO>> getFacets() {
        return facets;
    }

    public void setFacets(Map<FacetEnum, List<FiltreItemDTO>> facets) {
        this.facets = facets;
    }
}
