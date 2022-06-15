package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class TexteMaitreTraiteForm {
    @SwNotEmpty
    @FormParam("intitule")
    private String intitule;

    @SwNotEmpty
    @FormParam("dateSignature")
    private Calendar dateSignature;

    @SwRequired
    @FormParam("publication")
    private Boolean publication;

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Calendar getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(Calendar dateSignature) {
        this.dateSignature = dateSignature;
    }

    public Boolean getPublication() {
        return publication;
    }

    public void setPublication(Boolean publication) {
        this.publication = publication;
    }

    @FormParam("categorie")
    private String categorie;

    @FormParam("pays")
    private String pays;

    @FormParam("thematique")
    private String thematique;

    @FormParam("dateEntreeEnVigueur")
    private Calendar dateEntreeEnVigueur;

    @FormParam("autorisation")
    private Boolean autorisation;

    @FormParam("observation")
    private String observation;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("degrePriorite")
    private String degrePriorite;

    @FormParam("datePJL")
    private Calendar datePJL;

    @FormParam("etudeImpact")
    private Boolean etudeImpact;

    @FormParam("dispoEtudeImpact")
    private Boolean dispoEtudeImpact;

    @FormParam("dateDepotRatification")
    private Calendar dateDepotRatification;

    @FormParam("ratificationNumeroNor")
    private String ratificationNumeroNor;

    @FormParam("ratificationEtapeSolon")
    private String ratificationEtapeSolon;

    @FormParam("ratificationNumero")
    private String ratificationNumero;

    @FormParam("ratificationDateCreation")
    private Calendar ratificationDateCreation;

    @FormParam("ratificationTitreOfficiel")
    private String ratificationTitreOfficiel;

    @FormParam("ratificationDateSaisineCE")
    private Calendar ratificationDateSaisineCE;

    @FormParam("ratificationDatePublication")
    private Calendar ratificationDatePublication;

    @FormParam("decretNumeroNor")
    private String decretNumeroNor;

    @FormParam("decretEtapeSolon")
    private String decretEtapeSolon;

    @FormParam("decretNumero")
    private String decretNumero;

    @FormParam("decretDatePublication")
    private Calendar decretDatePublication;

    @FormParam("decretTitreOfficiel")
    private String decretTitreOfficiel;

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getThematique() {
        return thematique;
    }

    public void setThematique(String thematique) {
        this.thematique = thematique;
    }

    public Calendar getDateEntreeEnVigueur() {
        return dateEntreeEnVigueur;
    }

    public void setDateEntreeEnVigueur(Calendar dateEntreeEnVigueur) {
        this.dateEntreeEnVigueur = dateEntreeEnVigueur;
    }

    public Boolean getAutorisation() {
        return autorisation;
    }

    public void setAutorisation(Boolean autorisation) {
        this.autorisation = autorisation;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getDegrePriorite() {
        return degrePriorite;
    }

    public void setDegrePriorite(String degrePriorite) {
        this.degrePriorite = degrePriorite;
    }

    public Calendar getDatePJL() {
        return datePJL;
    }

    public void setDatePJL(Calendar datePJL) {
        this.datePJL = datePJL;
    }

    public Boolean getEtudeImpact() {
        return etudeImpact;
    }

    public void setEtudeImpact(Boolean etudeImpact) {
        this.etudeImpact = etudeImpact;
    }

    public Boolean getDispoEtudeImpact() {
        return dispoEtudeImpact;
    }

    public void setDispoEtudeImpact(Boolean dispoEtudeImpact) {
        this.dispoEtudeImpact = dispoEtudeImpact;
    }

    public Calendar getDateDepotRatification() {
        return dateDepotRatification;
    }

    public void setDateDepotRatification(Calendar dateDepotRatification) {
        this.dateDepotRatification = dateDepotRatification;
    }

    public String getRatificationNumeroNor() {
        return ratificationNumeroNor;
    }

    public void setRatificationNumeroNor(String ratificationNumeroNor) {
        this.ratificationNumeroNor = ratificationNumeroNor;
    }

    public String getRatificationEtapeSolon() {
        return ratificationEtapeSolon;
    }

    public void setRatificationEtapeSolon(String ratificationEtapeSolon) {
        this.ratificationEtapeSolon = ratificationEtapeSolon;
    }

    public String getRatificationNumero() {
        return ratificationNumero;
    }

    public void setRatificationNumero(String ratificationNumero) {
        this.ratificationNumero = ratificationNumero;
    }

    public Calendar getRatificationDateCreation() {
        return ratificationDateCreation;
    }

    public void setRatificationDateCreation(Calendar ratificationDateCreation) {
        this.ratificationDateCreation = ratificationDateCreation;
    }

    public String getRatificationTitreOfficiel() {
        return ratificationTitreOfficiel;
    }

    public void setRatificationTitreOfficiel(String ratificationTitreOfficiel) {
        this.ratificationTitreOfficiel = ratificationTitreOfficiel;
    }

    public Calendar getRatificationDateSaisineCE() {
        return ratificationDateSaisineCE;
    }

    public void setRatificationDateSaisineCE(Calendar ratificationDateSaisineCE) {
        this.ratificationDateSaisineCE = ratificationDateSaisineCE;
    }

    public Calendar getRatificationDatePublication() {
        return ratificationDatePublication;
    }

    public void setRatificationDatePublication(Calendar ratificationDatePublication) {
        this.ratificationDatePublication = ratificationDatePublication;
    }

    public String getDecretNumeroNor() {
        return decretNumeroNor;
    }

    public void setDecretNumeroNor(String decretNumeroNor) {
        this.decretNumeroNor = decretNumeroNor;
    }

    public String getDecretEtapeSolon() {
        return decretEtapeSolon;
    }

    public void setDecretEtapeSolon(String decretEtapeSolon) {
        this.decretEtapeSolon = decretEtapeSolon;
    }

    public String getDecretNumero() {
        return decretNumero;
    }

    public void setDecretNumero(String decretNumero) {
        this.decretNumero = decretNumero;
    }

    public Calendar getDecretDatePublication() {
        return decretDatePublication;
    }

    public void setDecretDatePublication(Calendar decretDatePublication) {
        this.decretDatePublication = decretDatePublication;
    }

    public String getDecretTitreOfficiel() {
        return decretTitreOfficiel;
    }

    public void setDecretTitreOfficiel(String decretTitreOfficiel) {
        this.decretTitreOfficiel = decretTitreOfficiel;
    }
}
