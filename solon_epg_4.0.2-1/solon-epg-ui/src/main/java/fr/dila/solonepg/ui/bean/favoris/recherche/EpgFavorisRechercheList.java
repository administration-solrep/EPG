package fr.dila.solonepg.ui.bean.favoris.recherche;

import static fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm.CREATEUR_SORT_NAME;
import static fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm.INTITULE_SORT_NAME;
import static fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm.TYPE_SORT_NAME;

import fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm;
import fr.dila.ss.ui.th.bean.AbstractDossierList;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class EpgFavorisRechercheList extends AbstractDossierList {
    private List<EpgFavorisRechercheDTO> listeFavoris = new ArrayList<>();

    public EpgFavorisRechercheList() {
        super(0);
    }

    public EpgFavorisRechercheList(Integer nbTotal) {
        super(nbTotal);
    }

    public List<EpgFavorisRechercheDTO> getListeFavoris() {
        return listeFavoris;
    }

    public void setListeFavoris(List<EpgFavorisRechercheDTO> listeFavoris) {
        this.listeFavoris = listeFavoris;
    }

    public void buildColonnes(EpgFavorisRechercheListForm form) {
        getListeColonnes().clear();
        getListeColonnes()
            .add(new ColonneInfo("recherche.favoris.liste.intitule", true, INTITULE_SORT_NAME, form.getIntituleSort()));
        getListeColonnes()
            .add(new ColonneInfo("recherche.favoris.liste.type", true, TYPE_SORT_NAME, form.getTypeSort()));
        getListeColonnes()
            .add(new ColonneInfo("recherche.favoris.liste.createur", true, CREATEUR_SORT_NAME, form.getCreateurSort()));
    }
}
