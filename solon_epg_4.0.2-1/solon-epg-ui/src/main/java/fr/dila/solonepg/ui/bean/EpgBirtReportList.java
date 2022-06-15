package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.services.impl.StatistiquesItem;
import fr.dila.ss.ui.th.bean.BirtReportListForm;
import fr.dila.st.ui.bean.ColonneInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EpgBirtReportList implements Serializable {
    private static final long serialVersionUID = -3672935305629247139L;

    private final List<ColonneInfo> listeColonnes = new ArrayList<>();

    private LinkedList<StatistiquesItem> liste = new LinkedList<>();

    private Integer nbTotal;

    public EpgBirtReportList() {
        nbTotal = 0;
    }

    public List<StatistiquesItem> getListe() {
        return liste;
    }

    public void setListe(LinkedList<StatistiquesItem> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    public List<ColonneInfo> getListeColonnes() {
        return listeColonnes;
    }

    public void buildColonnes(BirtReportListForm form) {
        listeColonnes.clear();

        if (form != null) {
            listeColonnes.add(
                new ColonneInfo("label.birt.reporting.report.title", true, "titre", form.getTitre(), false, true)
            );
        }
    }
}
