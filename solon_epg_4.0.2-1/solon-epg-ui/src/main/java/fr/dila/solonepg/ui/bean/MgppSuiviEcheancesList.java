package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.th.bean.MgppSuiviEcheancesListForm;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.enums.SortOrder;
import java.util.ArrayList;
import java.util.List;

public class MgppSuiviEcheancesList {
    private List<ColonneInfo> listeColonnes = new ArrayList<>();
    private List<MgppSuiviEcheancesDTO> liste = new ArrayList<>();
    private Integer nbTotal;

    public static final String NOR = "norTri";
    public static final String OBJET = "objetTri";
    public static final String DATE_DEPOT_EFFECTIF = "dateDepotEffectifTri";
    public static final String DESTINATAIRE_1_RAPPORT = "destinataire1RapportTri";

    public MgppSuiviEcheancesList() {
        this.nbTotal = 0;
    }

    public void setListeColonnes(List<ColonneInfo> listeColonnes) {
        this.listeColonnes = listeColonnes;
    }

    public List<MgppSuiviEcheancesDTO> getListe() {
        return liste;
    }

    public void setListe(List<MgppSuiviEcheancesDTO> liste) {
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

    public List<ColonneInfo> getListeColonnes(MgppSuiviEcheancesListForm form) {
        buildColonnes(form);
        return listeColonnes;
    }

    private ColonneInfo buildColonne(String label, boolean sortable, String name, SortOrder value) {
        return new ColonneInfo(label, sortable, name, value, false, true);
    }

    private void buildColonnes(MgppSuiviEcheancesListForm form) {
        listeColonnes.clear();

        if (form != null) {
            listeColonnes.add(buildColonne("suivi.echeances.label.identifiant", true, NOR, form.getNorTri()));
            listeColonnes.add(buildColonne("suivi.echeances.label.intitule", true, OBJET, form.getObjetTri()));
            listeColonnes.add(
                buildColonne(
                    "suivi.echeances.label.dateDEnvoi",
                    true,
                    DATE_DEPOT_EFFECTIF,
                    form.getDateDepotEffectifTri()
                )
            );
        }
    }
}
