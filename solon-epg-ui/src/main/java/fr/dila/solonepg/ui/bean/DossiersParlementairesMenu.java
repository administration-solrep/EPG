package fr.dila.solonepg.ui.bean;

import java.util.ArrayList;
import java.util.List;

public class DossiersParlementairesMenu {
    private List<CorbeilleUIDTO> corbeilles = new ArrayList<>();
    private List<RechercheDossiersParlementairesDTO> criteres = new ArrayList<>();

    public DossiersParlementairesMenu() {
        super();
    }

    public List<CorbeilleUIDTO> getCorbeilles() {
        return corbeilles;
    }

    public void setCorbeilles(List<CorbeilleUIDTO> corbeilles) {
        this.corbeilles = corbeilles;
    }

    public List<RechercheDossiersParlementairesDTO> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<RechercheDossiersParlementairesDTO> criteres) {
        this.criteres = criteres;
    }
}
