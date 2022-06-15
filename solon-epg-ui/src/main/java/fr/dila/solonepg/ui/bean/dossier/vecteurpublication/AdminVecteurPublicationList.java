package fr.dila.solonepg.ui.bean.dossier.vecteurpublication;

import fr.dila.solonepg.ui.th.bean.AdminVecteurPublicationForm;
import fr.dila.ss.ui.th.bean.AbstractDossierList;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class AdminVecteurPublicationList extends AbstractDossierList {
    private List<AdminVecteurPublicationDTO> liste = new ArrayList<>();

    private static final String COLUMN_SORT_INTITULE = "intitule";
    private static final String COLUMN_SORT_DATE_DEBUT = "dateDebut";
    private static final String COLUMN_SORT_DATE_FIN = "dateFin";

    public AdminVecteurPublicationList() {
        super(0);
    }

    public List<AdminVecteurPublicationDTO> getListe() {
        return liste;
    }

    public void setListe(List<AdminVecteurPublicationDTO> liste) {
        this.liste = liste;
    }

    @Override
    public Integer getNbTotal() {
        return liste.size();
    }

    public List<IColonneInfo> getListeColonnes(AdminVecteurPublicationForm form) {
        buildColonnes(form);
        return getListeColonnes();
    }

    private void buildColonnes(AdminVecteurPublicationForm form) {
        getListeColonnes().clear();
        if (form != null) {
            getListeColonnes()
                .add(
                    new ColonneInfo(
                        "admin.vecteur.publication.jo",
                        true,
                        COLUMN_SORT_INTITULE,
                        form.getIntitule(),
                        false,
                        true
                    )
                );
            getListeColonnes()
                .add(
                    new ColonneInfo(
                        "admin.vecteur.publication.table.date.debut",
                        true,
                        COLUMN_SORT_DATE_DEBUT,
                        form.getDateDebut(),
                        false,
                        true
                    )
                );
            getListeColonnes()
                .add(
                    new ColonneInfo(
                        "admin.vecteur.publication.table.date.fin",
                        true,
                        COLUMN_SORT_DATE_FIN,
                        form.getDateFin(),
                        false,
                        true
                    )
                );
        }
    }
}
