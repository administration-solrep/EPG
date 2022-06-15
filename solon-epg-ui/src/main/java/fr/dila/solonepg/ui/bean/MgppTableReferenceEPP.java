package fr.dila.solonepg.ui.bean;

import java.util.ArrayList;
import java.util.List;

public class MgppTableReferenceEPP {
    private List<TableReferenceEPPElementDTO> liste = new ArrayList<>();

    public List<TableReferenceEPPElementDTO> getListe() {
        return liste;
    }

    public void setListe(List<TableReferenceEPPElementDTO> liste) {
        this.liste = liste;
    }
}
