package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.core.recherche.EpgUserListingDTO;
import fr.dila.solonepg.ui.th.bean.EpgUserListForm;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class EpgUserList {
    private static final String COLUMN_HEADER_NOM = "users.column.header.nom";
    private static final String COLUMN_HEADER_PRENOM = "users.column.header.prenom";
    private static final String COLUMN_HEADER_UTILISATEUR = "users.column.header.utilisateur";
    private static final String COLUMN_HEADER_MEL = "users.column.header.mel";
    private static final String COLUMN_HEADER_DATE_DEBUT = "users.column.header.dateDebut";

    private List<ColonneInfo> listeColonnes = new ArrayList<>();
    private final boolean haveSortableColumns;
    private List<EpgUserListingDTO> liste = new ArrayList<>();
    private Integer nbTotal = 0;

    public EpgUserList() {
        this(false);
    }

    public EpgUserList(boolean haveSortableColumns) {
        this.haveSortableColumns = haveSortableColumns;
        buildColonnes(null);
    }

    public List<ColonneInfo> getListeColonnes() {
        return getListeColonnes(null);
    }

    public List<ColonneInfo> getListeColonnes(EpgUserListForm form) {
        buildColonnes(form);
        return listeColonnes;
    }

    public void setListeColonnes(List<ColonneInfo> listeColonnes) {
        this.listeColonnes = listeColonnes;
    }

    public List<EpgUserListingDTO> getListe() {
        return liste;
    }

    public void setListe(List<EpgUserListingDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    private void buildColonnes(EpgUserListForm form) {
        listeColonnes.clear();
        if (form != null && haveSortableColumns) {
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_NOM, true, EpgUserListForm.NOM, form.getNom()));
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_PRENOM, true, EpgUserListForm.PRENOM, form.getPrenom()));
            listeColonnes.add(
                new ColonneInfo(COLUMN_HEADER_UTILISATEUR, true, EpgUserListForm.UTILISATEUR, form.getUtilisateur())
            );
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_MEL, true, EpgUserListForm.MEL, form.getMel()));
            listeColonnes.add(
                new ColonneInfo(COLUMN_HEADER_DATE_DEBUT, true, EpgUserListForm.DATE_DEBUT, form.getDateDebut())
            );
        } else {
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_NOM, false, true, false, true));
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_PRENOM, false, true, false, true));
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_UTILISATEUR, false, true, false, true));
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_MEL, false, true, false, true));
            listeColonnes.add(new ColonneInfo(COLUMN_HEADER_DATE_DEBUT, false, true, false, true));
        }
    }
}
