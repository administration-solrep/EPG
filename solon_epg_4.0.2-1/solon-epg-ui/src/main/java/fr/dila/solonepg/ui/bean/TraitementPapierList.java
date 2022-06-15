package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.enums.EpgListTraitementDateEnum;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.enums.SortOrder;
import java.util.ArrayList;
import java.util.List;

public class TraitementPapierList {
    private static final String COLUMN_INTITULE = "intituleTri";
    private static final String COLUMN_NOR = "norTri";
    private static final String COLUMN_TITRE_TEXT = "titreTextTri";

    private final List<ColonneInfo> listeColonnes = new ArrayList<>();

    private List<TraitementPapierElementDTO> liste = new ArrayList<>();

    private List<EpgListTraitementDateEnum> datesList = new ArrayList<>();

    private String idListe;

    private String titre;

    public TraitementPapierList() {
        buildColonnes();
    }

    public List<TraitementPapierElementDTO> getListe() {
        return liste;
    }

    public void setListe(List<TraitementPapierElementDTO> liste) {
        this.liste = liste;
    }

    public String getIdListe() {
        return idListe;
    }

    public void setIdListe(String idListe) {
        this.idListe = idListe;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<ColonneInfo> getListeColonnes() {
        return listeColonnes;
    }

    public List<EpgListTraitementDateEnum> getDatesList() {
        return datesList;
    }

    public void setDatesList(List<EpgListTraitementDateEnum> datesList) {
        this.datesList = datesList;
    }

    private void buildColonnes() {
        listeColonnes.clear();
        listeColonnes.add(
            new ColonneInfo("traitement.papier.liste.colonne.intituleMin", true, COLUMN_INTITULE, SortOrder.ASC)
        );
        listeColonnes.add(new ColonneInfo("traitement.papier.liste.colonne.nor", true, COLUMN_NOR, null));
        listeColonnes.add(new ColonneInfo("traitement.papier.liste.colonne.titreText", true, COLUMN_TITRE_TEXT, null));
    }
}
