package fr.dila.solonepg.ui.bean;

import static fr.dila.solonepg.ui.th.bean.StatutArchivageDossierForm.*;

import fr.dila.solonepg.ui.th.bean.StatutArchivageDossierForm;
import fr.dila.ss.ui.th.bean.AbstractDossierList;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class StatutArchivageDossierList extends AbstractDossierList {
    private List<StatutArchivageDossierDTO> liste = new ArrayList<>();

    public StatutArchivageDossierList(Integer nbTotal) {
        super(nbTotal);
    }

    public List<StatutArchivageDossierDTO> getListe() {
        return liste;
    }

    public void setListe(List<StatutArchivageDossierDTO> liste) {
        this.liste = liste;
    }

    public void buildColonnes(StatutArchivageDossierForm form) {
        getListeColonnes().clear();
        getListeColonnes().add(new ColonneInfo("statut.archivage.dossier.nor", true, NOR_NAME, form.getNorTri()));
        getListeColonnes()
            .add(new ColonneInfo("statut.archivage.dossier.titreActe", true, TITRE_ACTE_NAME, form.getTitreActeTri()));
        getListeColonnes()
            .add(
                new ColonneInfo(
                    "statut.archivage.dossier.periode",
                    true,
                    STATUT_ARCHIVAGE_NAME,
                    form.getStatutArchivagePeriodeTri()
                )
            );
        getListeColonnes()
            .add(
                new ColonneInfo(
                    "statut.archivage.dossier.dateChangement",
                    true,
                    DATE_CHANGEMENT_STATUT_NAME,
                    form.getDateChangementStatutTri()
                )
            );
        getListeColonnes()
            .add(
                new ColonneInfo(
                    "statut.archivage.dossier.encours",
                    true,
                    STATUT_EN_COURS_NAME,
                    form.getStatutEnCoursTri()
                )
            );
        getListeColonnes()
            .add(new ColonneInfo("statut.archivage.dossier.message", true, MESSAGE_NAME, form.getMessageTri()));
    }
}
