package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class EpgListTraitementForm {
    @FormParam("foldersId[]")
    @SwRequired
    private ArrayList<String> foldersId;

    @FormParam("dateRetourPM")
    private Calendar dateRetourPM;

    @FormParam("dateRetourSGG")
    private Calendar dateRetourSGG;

    @FormParam("dateEnvoiPR")
    private Calendar dateEnvoiPR;

    @FormParam("dateRetourPR")
    private Calendar dateRetourPR;

    @FormParam("dateDemandeEpreuve")
    private Calendar dateDemandeEpreuve;

    public EpgListTraitementForm() {}

    public ArrayList<String> getFoldersId() {
        return foldersId;
    }

    public void setFoldersId(ArrayList<String> foldersId) {
        this.foldersId = foldersId;
    }

    public Calendar getDateRetourPM() {
        return dateRetourPM;
    }

    public void setDateRetourPM(Calendar dateRetourPM) {
        this.dateRetourPM = dateRetourPM;
    }

    public Calendar getDateRetourSGG() {
        return dateRetourSGG;
    }

    public void setDateRetourSGG(Calendar dateRetourSGG) {
        this.dateRetourSGG = dateRetourSGG;
    }

    public Calendar getDateEnvoiPR() {
        return dateEnvoiPR;
    }

    public void setDateEnvoiPR(Calendar dateEnvoiPR) {
        this.dateEnvoiPR = dateEnvoiPR;
    }

    public Calendar getDateRetourPR() {
        return dateRetourPR;
    }

    public void setDateRetourPR(Calendar dateRetourPR) {
        this.dateRetourPR = dateRetourPR;
    }

    public Calendar getDateDemandeEpreuve() {
        return dateDemandeEpreuve;
    }

    public void setDateDemandeEpreuve(Calendar dateDemandeEpreuve) {
        this.dateDemandeEpreuve = dateDemandeEpreuve;
    }
}
