package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.Calendar;
import javax.ws.rs.QueryParam;

@SwBean
public class ArchivageAdamantForm {
    @QueryParam("numEntreeSolon")
    private int dossierExtraction;

    @SwNotEmpty
    @QueryParam("intervalleEligibiliteDebut")
    private Calendar intervalleEligibiliteDebut;

    @SwNotEmpty
    @QueryParam("intervalleEligibiliteFin")
    private Calendar intervalleEligibiliteFin;

    @QueryParam("statut")
    private String statut;

    public ArchivageAdamantForm() {}

    public ArchivageAdamantForm(
        int dossierExtraction,
        Calendar intervalleEligibiliteDebut,
        Calendar intervalleEligibiliteFin,
        String statut
    ) {
        this.dossierExtraction = dossierExtraction;
        this.intervalleEligibiliteDebut = intervalleEligibiliteDebut;
        this.intervalleEligibiliteFin = intervalleEligibiliteFin;
        this.statut = statut;
    }

    public int getDossierExtraction() {
        return dossierExtraction;
    }

    public void setDossierExtraction(int dossierExtraction) {
        this.dossierExtraction = dossierExtraction;
    }

    public Calendar getIntervalleEligibiliteDebut() {
        return intervalleEligibiliteDebut;
    }

    public void setIntervalleEligibiliteDebut(Calendar intervalleEligibiliteDebut) {
        this.intervalleEligibiliteDebut = intervalleEligibiliteDebut;
    }

    public Calendar getIntervalleEligibiliteFin() {
        return intervalleEligibiliteFin;
    }

    public void setIntervalleEligibiliteFin(Calendar intervalleEligibiliteFin) {
        this.intervalleEligibiliteFin = intervalleEligibiliteFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
