package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.annot.SwBean;
import javax.ws.rs.FormParam;

@SwBean
public class MgppMinistereForm {
    @FormParam("identifiant")
    private String identifiant;

    @FormParam("idGouvernement")
    private String idGouvernement;

    @FormParam("nom")
    private String nom;

    @FormParam("libelle")
    private String libelle;

    @FormParam("appellation")
    private String appellation;

    @FormParam("edition")
    private String edition;

    @FormParam("dateDebut")
    private String dateDebut = SolonDateConverter.DATE_SLASH.formatNow();

    @FormParam("dateFin")
    private String dateFin;

    public MgppMinistereForm() {
        super();
    }

    public MgppMinistereForm(String idGouvernement) {
        super();
        this.idGouvernement = idGouvernement;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getIdGouvernement() {
        return idGouvernement;
    }

    public void setIdGouvernement(String idGouvernement) {
        this.idGouvernement = idGouvernement;
    }
}
