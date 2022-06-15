package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.th.bean.MgppFichePresentationOEPForm;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class MgppFichePresentationOEPList {
    private List<ColonneInfo> listeColonnes = new ArrayList<>();
    private List<MgppFichePresentationOEPDTO> liste = new ArrayList<>();
    private Integer nbTotal = 0;

    public MgppFichePresentationOEPList() {
        super();
    }

    public List<ColonneInfo> getListeColonnes() {
        return listeColonnes;
    }

    public void setListeColonnes(List<ColonneInfo> listeColonnes) {
        this.listeColonnes = listeColonnes;
    }

    public List<MgppFichePresentationOEPDTO> getListe() {
        return liste;
    }

    public void setListe(List<MgppFichePresentationOEPDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    public void buildColonnes(MgppFichePresentationOEPForm form) {
        listeColonnes.clear();

        listeColonnes.add(
            new ColonneInfo(
                "suiviLibre.activiteParlementaire.table.header.organismeExtraparlementaire",
                true,
                "nom",
                form.getNom(),
                false,
                true
            )
        );
        listeColonnes.add(
            new ColonneInfo(
                "suiviLibre.activiteParlementaire.table.header.ministere",
                true,
                "ministere",
                form.getMinistere(),
                false,
                true
            )
        );
        listeColonnes.add(
            new ColonneInfo(
                "suiviLibre.activiteParlementaire.table.header.dateMAJ",
                true,
                "date",
                form.getDate(),
                false,
                true
            )
        );
        listeColonnes.add(
            new ColonneInfo("suiviLibre.activiteParlementaire.table.header.downloadAction", false, true, false, false)
        );
    }
}
