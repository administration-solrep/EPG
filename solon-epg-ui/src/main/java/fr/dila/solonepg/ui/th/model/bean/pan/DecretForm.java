package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwRegex;
import javax.ws.rs.FormParam;

@SwBean
public class DecretForm {
    @FormParam("numeroNor")
    private String numeroNor;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titre")
    private String titreOfficiel;

    @FormParam("categorie")
    private String typeActe;

    @FormParam("sectionCE")
    private String sectionCE;

    @FormParam("dateSaisineCE")
    private String dateSaisineCE;

    @FormParam("dateExamenCE")
    private String dateExamenCE;

    @FormParam("rapporteurCE")
    private String rapporteurCE;

    @FormParam("referenceAvisCE")
    private String referenceAvisCE;

    @SwRegex(value = "20[0-9]{2}-[0-9]+")
    @FormParam("numerosTexte")
    private String numeroTexte;

    @FormParam("dateSignature")
    private String dateSignature;

    @FormParam("datePublication")
    private String datePublication;

    @FormParam("numeroJOPublication")
    private String numeroJO;

    @FormParam("numeroPage")
    private String numeroPage;

    public DecretForm() {}

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public String getTitreOfficiel() {
        return titreOfficiel;
    }

    public void setTitreOfficiel(String titreOfficiel) {
        this.titreOfficiel = titreOfficiel;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getSectionCE() {
        return sectionCE;
    }

    public void setSectionCE(String sectionCE) {
        this.sectionCE = sectionCE;
    }

    public String getDateSaisineCE() {
        return dateSaisineCE;
    }

    public void setDateSaisineCE(String dateSaisineCE) {
        this.dateSaisineCE = dateSaisineCE;
    }

    public String getDateExamenCE() {
        return dateExamenCE;
    }

    public void setDateExamenCE(String dateExamenCE) {
        this.dateExamenCE = dateExamenCE;
    }

    public String getRapporteurCE() {
        return rapporteurCE;
    }

    public void setRapporteurCE(String rapporteurCE) {
        this.rapporteurCE = rapporteurCE;
    }

    public String getReferenceAvisCE() {
        return referenceAvisCE;
    }

    public void setReferenceAvisCE(String referenceAvisCE) {
        this.referenceAvisCE = referenceAvisCE;
    }

    public String getNumeroTexte() {
        return numeroTexte;
    }

    public void setNumeroTexte(String numeroTexte) {
        this.numeroTexte = numeroTexte;
    }

    public String getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(String dateSignature) {
        this.dateSignature = dateSignature;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public String getNumeroJO() {
        return numeroJO;
    }

    public void setNumeroJO(String numeroJO) {
        this.numeroJO = numeroJO;
    }

    public String getNumeroPage() {
        return numeroPage;
    }

    public void setNumeroPage(String numeroPage) {
        this.numeroPage = numeroPage;
    }
}
