package fr.dila.solonepg.ui.bean.parametres;

import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class ParametreESList {
    private List<ParametreESDTO> liste = new ArrayList<>();

    private IColonneInfo colonneParametres = null;

    private Integer nbTotal = 0;

    public List<ParametreESDTO> getListe() {
        return liste;
    }

    public void setListe(List<ParametreESDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    public IColonneInfo getColonneParametres() {
        return colonneParametres;
    }

    public void setColonneParametres(IColonneInfo colonneParametres) {
        this.colonneParametres = colonneParametres;
    }

    public void buildColonnes() {
        colonneParametres = new ColonneInfo("label.parametres.parametre", false, true, false, false);
    }
}
