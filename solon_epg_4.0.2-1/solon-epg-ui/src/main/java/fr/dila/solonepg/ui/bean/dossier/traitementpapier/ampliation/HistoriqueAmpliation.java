package fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation;

import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import java.util.Calendar;

public class HistoriqueAmpliation {
    private Calendar dateEnvoi;

    private String destinataire;

    public HistoriqueAmpliation(InfoHistoriqueAmpliation ampliationBack) {
        this.dateEnvoi = ampliationBack.getDateEnvoiAmpliation();
        this.destinataire = String.join(";", ampliationBack.getAmpliationDestinataireMails());
    }

    public HistoriqueAmpliation(Calendar dateEnvoi, String destinataire) {
        this.dateEnvoi = dateEnvoi;
        this.destinataire = destinataire;
    }

    public Calendar getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Calendar dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }
}
