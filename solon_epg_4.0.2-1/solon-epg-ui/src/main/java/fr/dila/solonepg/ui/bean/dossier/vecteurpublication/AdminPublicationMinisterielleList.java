package fr.dila.solonepg.ui.bean.dossier.vecteurpublication;

import fr.dila.solonepg.ui.th.bean.AdminPublicationMinisterielleForm;
import fr.dila.ss.ui.th.bean.AbstractDossierList;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class AdminPublicationMinisterielleList extends AbstractDossierList {
    private List<AdminPublicationMinisterielleDTO> liste = new ArrayList<>();

    private static final String COLUMN_SORT_NOR = "nor";
    private static final String COLUMN_SORT_INTITULE = "intitule";

    public List<AdminPublicationMinisterielleDTO> getListe() {
        return liste;
    }

    public void setListe(List<AdminPublicationMinisterielleDTO> liste) {
        this.liste = liste;
    }

    public AdminPublicationMinisterielleList() {
        super(0);
    }

    @Override
    public Integer getNbTotal() {
        return liste.size();
    }

    public List<IColonneInfo> getListeColonnes(AdminPublicationMinisterielleForm form) {
        buildColonnes(form);
        return getListeColonnes();
    }

    private void buildColonnes(AdminPublicationMinisterielleForm form) {
        getListeColonnes().clear();
        if (form != null) {
            getListeColonnes()
                .add(
                    new ColonneInfo(
                        "admin.publication.ministerielle.nor",
                        true,
                        COLUMN_SORT_NOR,
                        form.getNor(),
                        false,
                        true
                    )
                );
            getListeColonnes()
                .add(
                    new ColonneInfo(
                        "admin.publication.ministerielle.intitule",
                        true,
                        COLUMN_SORT_INTITULE,
                        form.getIntitule(),
                        false,
                        true
                    )
                );
        }
    }
}
