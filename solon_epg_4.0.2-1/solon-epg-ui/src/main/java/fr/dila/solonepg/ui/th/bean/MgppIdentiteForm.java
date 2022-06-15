package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class MgppIdentiteForm {
    @FormParam("idIdentite")
    private String idIdentite;

    @FormParam("civilite")
    private String civilite;

    @SwNotEmpty
    @FormParam("prenom")
    private String prenom;

    @SwNotEmpty
    @FormParam("nom")
    private String nom;

    @FormParam("dateNaissance")
    private Calendar dateNaissance;

    @FormParam("lieuNaissance")
    private String lieuNaissance;

    @FormParam("departementNaissance")
    private String departementNaissance;

    @FormParam("paysNaissance")
    private String paysNaissance;

    @SwRequired
    @FormParam("dateDebutIdentite")
    private Calendar dateDebutIdentite;

    @FormParam("dateFinIdentite")
    private Calendar dateFinIdentite;

    public MgppIdentiteForm() {
        this.dateDebutIdentite = Calendar.getInstance();
        this.dateFinIdentite = null;
        this.dateNaissance = null;
        this.departementNaissance = null;
        this.idIdentite = null;
        this.lieuNaissance = null;
        this.nom = null;
        this.paysNaissance = null;
        this.prenom = null;
    }

    public String getIdIdentite() {
        return idIdentite;
    }

    public void setIdIdentite(String idIdentite) {
        this.idIdentite = idIdentite;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Calendar getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Calendar dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getDepartementNaissance() {
        return departementNaissance;
    }

    public void setDepartementNaissance(String departementNaissance) {
        this.departementNaissance = departementNaissance;
    }

    public String getPaysNaissance() {
        return paysNaissance;
    }

    public void setPaysNaissance(String paysNaissance) {
        this.paysNaissance = paysNaissance;
    }

    public Calendar getDateDebutIdentite() {
        return dateDebutIdentite;
    }

    public void setDateDebutIdentite(Calendar dateDebutIdentite) {
        this.dateDebutIdentite = dateDebutIdentite;
    }

    public Calendar getDateFinIdentite() {
        return dateFinIdentite;
    }

    public void setDateFinIdentite(Calendar dateFinIdentite) {
        this.dateFinIdentite = dateFinIdentite;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getPrenomNom() {
        return getPrenom() + " " + getNom();
    }
}
