package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import javax.ws.rs.FormParam;

@SwBean
public class MgppRepresentantForm {
    @FormParam("idRepresentant")
    private String idRepresentant;

    @FormParam("typeRepresentant")
    private String typeRepresentant;

    @FormParam("nomine")
    private String nomine;

    @FormParam("personne")
    private String personne;

    @FormParam("representant")
    private String representant;

    @FormParam("fonction")
    private String fonction;

    @FormParam("dateDebut")
    private String dateDebut;

    @FormParam("dateFin")
    private String dateFin;

    @FormParam("dateAuditionAN")
    private String dateAuditionAN;

    @FormParam("dateAuditionSE")
    private String dateAuditionSE;

    @FormParam("numeroMandat")
    private String numeroMandat;

    @FormParam("autoriteDesignation")
    private String autoriteDesignation;

    @FormParam("commissionSaisie")
    private String commissionSaisie;

    public MgppRepresentantForm() {}

    public String getIdRepresentant() {
        return idRepresentant;
    }

    public void setIdRepresentant(String idRepresentant) {
        this.idRepresentant = idRepresentant;
    }

    public String getTypeRepresentant() {
        return typeRepresentant;
    }

    public void setTypeRepresentant(String typeRepresentant) {
        this.typeRepresentant = typeRepresentant;
    }

    public String getNomine() {
        return nomine;
    }

    public void setNomine(String nomine) {
        this.nomine = nomine;
    }

    public String getPersonne() {
        return personne;
    }

    public void setPersonne(String personne) {
        this.personne = personne;
    }

    public String getRepresentant() {
        return representant;
    }

    public void setRepresentant(String representant) {
        this.representant = representant;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateAuditionAN() {
        return dateAuditionAN;
    }

    public void setDateAuditionAN(String dateAuditionAN) {
        this.dateAuditionAN = dateAuditionAN;
    }

    public String getDateAuditionSE() {
        return dateAuditionSE;
    }

    public void setDateAuditionSE(String dateAuditionSE) {
        this.dateAuditionSE = dateAuditionSE;
    }

    public String getNumeroMandat() {
        return numeroMandat;
    }

    public void setNumeroMandat(String numeroMandat) {
        this.numeroMandat = numeroMandat;
    }

    public String getAutoriteDesignation() {
        return autoriteDesignation;
    }

    public void setAutoriteDesignation(String autoriteDesignation) {
        this.autoriteDesignation = autoriteDesignation;
    }

    public String getCommissionSaisie() {
        return commissionSaisie;
    }

    public void setCommissionSaisie(String commissionSaisie) {
        this.commissionSaisie = commissionSaisie;
    }
}
