package fr.dila.solonepg.ui.bean.dossier.similaire;

import fr.dila.ss.ui.th.bean.AbstractDossierList;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class DossierSimilaireList extends AbstractDossierList {
    private List<DossierSimilaireDTO> liste = new ArrayList<>();

    public DossierSimilaireList() {
        super(0);
    }

    public List<DossierSimilaireDTO> getListe() {
        return liste;
    }

    public void setListe(List<DossierSimilaireDTO> liste) {
        this.liste = liste;
    }

    public void buildColonnes(DossierSimilaireListForm form) {
        getListeColonnes().clear();
        getListeColonnes()
            .add(
                ColonneInfo
                    .builder("dossier.similaire.table.header.nor", true, true, false, true)
                    .sortName("nor")
                    .sortId(getSortId("nor"))
                    .sortValue(form == null ? null : form.getNor())
                    .build()
            );
        getListeColonnes()
            .add(
                ColonneInfo
                    .builder("dossier.similaire.table.header.titre", true, true, false, true)
                    .sortName("titre")
                    .sortId(getSortId("titre"))
                    .sortValue(form == null ? null : form.getTitre())
                    .build()
            );
        getListeColonnes()
            .add(
                ColonneInfo
                    .builder("dossier.similaire.table.header.statut", true, true, false, true)
                    .sortName("statut")
                    .sortId(getSortId("statut"))
                    .sortValue(form == null ? null : form.getStatut())
                    .build()
            );
        getListeColonnes()
            .add(
                ColonneInfo
                    .builder("dossier.similaire.table.header.auteur", true, true, false, true)
                    .sortName("auteur")
                    .sortId(getSortId("auteur"))
                    .sortValue(form == null ? null : form.getAuteur())
                    .build()
            );
    }

    private String getSortId(String columnName) {
        return columnName + "Header";
    }
}
