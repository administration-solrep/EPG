package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class LoiRatificationForm {
    @FormParam("dateLimiteDepot")
    private Calendar dateLimiteDepot;

    @FormParam("numeroNor")
    private String numeroNor;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreActe")
    private String titreActe;

    @FormParam("dateSaisineCE")
    private Calendar dateSaisineCE;

    @FormParam("dateExamenCE")
    private Calendar dateExamenCE;

    @FormParam("dateExamenCM")
    private Calendar dateExamenCM;

    @FormParam("sectionCE")
    private String sectionCE;

    @FormParam("chambreDepot")
    private String chambreDepot;

    @FormParam("dateDepot")
    private Calendar dateDepot;

    @FormParam("numeroDepot")
    private String numeroDepot;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreOfficiel")
    private String titreOfficiel;

    @FormParam("datePublication")
    private Calendar datePublication;

    public LoiRatificationForm() {}

    public Calendar getDateLimiteDepot() {
        return dateLimiteDepot;
    }

    public void setDateLimiteDepot(Calendar dateLimiteDepot) {
        this.dateLimiteDepot = dateLimiteDepot;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public Calendar getDateSaisineCE() {
        return dateSaisineCE;
    }

    public void setDateSaisineCE(Calendar dateSaisineCE) {
        this.dateSaisineCE = dateSaisineCE;
    }

    public Calendar getDateExamenCE() {
        return dateExamenCE;
    }

    public void setDateExamenCE(Calendar dateExamenCE) {
        this.dateExamenCE = dateExamenCE;
    }

    public Calendar getDateExamenCM() {
        return dateExamenCM;
    }

    public void setDateExamenCM(Calendar dateExamenCM) {
        this.dateExamenCM = dateExamenCM;
    }

    public String getSectionCE() {
        return sectionCE;
    }

    public void setSectionCE(String sectionCE) {
        this.sectionCE = sectionCE;
    }

    public String getChambreDepot() {
        return chambreDepot;
    }

    public void setChambreDepot(String chambreDepot) {
        this.chambreDepot = chambreDepot;
    }

    public Calendar getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(Calendar dateDepot) {
        this.dateDepot = dateDepot;
    }

    public String getNumeroDepot() {
        return numeroDepot;
    }

    public void setNumeroDepot(String numeroDepot) {
        this.numeroDepot = numeroDepot;
    }

    public String getTitreOfficiel() {
        return titreOfficiel;
    }

    public void setTitreOfficiel(String titreOfficiel) {
        this.titreOfficiel = titreOfficiel;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }
}
