package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.Date;

import fr.dila.st.core.util.DateUtil;

/**
 * representation d'une ordonnance dans le tableau de programmation
 * @author asatre
 *
 */
public class LigneProgrammationHabilitationDTO implements Serializable{
    
    private static final long serialVersionUID = 8624909872239413667L;
    
    private String numeroOrdre;
    private String ministerePilote;
    private String observation;
    private Boolean codification;
    private String article;
    private String objetRIM;
    private String convention;
    private Date dateTerme;
    private String conventionDepot;
    private Date datePrevisionnelleCM;
    private String legislature;
    private Date datePrevisionnelleSaisineCE;
    
    private String numeroNorOrdo;
    private String objetOrdo;
    private String ministerePiloteOrdo;
    private String legislatureOrdo;
    private Date dateSaisineCEOrdo;
    private Date datePassageCMOrdo;
    private Date datePublicationOrdo;
    private String titreOfficielOrdo;
    private String numeroOrdo;
    private String conventionDepotOrdo;
    private Date dateLimiteDepotOrdo;

    public LigneProgrammationHabilitationDTO() {

    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Boolean getCodification() {
        return codification;
    }

    public void setCodification(Boolean codification) {
        this.codification = codification;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getObjetRIM() {
        return objetRIM;
    }

    public void setObjetRIM(String objetRIM) {
        this.objetRIM = objetRIM;
    }

    public String getConvention() {
        return convention;
    }

    public void setConvention(String convention) {
        this.convention = convention;
    }

    public Date getDateTerme() {
        return DateUtil.copyDate(this.dateTerme);
    }

    public void setDateTerme(Date dateTerme) {
        this.dateTerme = DateUtil.copyDate(dateTerme);
    }

    public String getConventionDepot() {
        return conventionDepot;
    }

    public void setConventionDepot(String conventionDepot) {
        this.conventionDepot = conventionDepot;
    }

    public Date getDatePrevisionnelleCM() {
        return DateUtil.copyDate(this.datePrevisionnelleCM);
    }

    public void setDatePrevisionnelleCM(Date datePrevisionnelleCM) {
        this.datePrevisionnelleCM = DateUtil.copyDate(datePrevisionnelleCM);
    }

    public String getLegislature() {
        return legislature;
    }

    public void setLegislature(String legislature) {
        this.legislature = legislature;
    }

    public void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE) {
        this.datePrevisionnelleSaisineCE = DateUtil.copyDate(datePrevisionnelleSaisineCE);
    }

    public Date getDatePrevisionnelleSaisineCE() {
        return DateUtil.copyDate(datePrevisionnelleSaisineCE);
    }

    public String getNumeroNorOrdo() {
        return numeroNorOrdo;
    }

    public void setNumeroNorOrdo(String numeroNorOrdo) {
        this.numeroNorOrdo = numeroNorOrdo;
    }

    public String getObjetOrdo() {
        return objetOrdo;
    }

    public void setObjetOrdo(String objetOrdo) {
        this.objetOrdo = objetOrdo;
    }

    public String getMinisterePiloteOrdo() {
        return ministerePiloteOrdo;
    }

    public void setMinisterePiloteOrdo(String ministerePiloteOrdo) {
        this.ministerePiloteOrdo = ministerePiloteOrdo;
    }

    public String getLegislatureOrdo() {
        return legislatureOrdo;
    }

    public void setLegislatureOrdo(String legislatureOrdo) {
        this.legislatureOrdo = legislatureOrdo;
    }

    public Date getDateSaisineCEOrdo() {
        return DateUtil.copyDate(this.dateSaisineCEOrdo);
    }

    public void setDateSaisineCEOrdo(Date dateSaisineCEOrdo) {
        this.dateSaisineCEOrdo = DateUtil.copyDate(dateSaisineCEOrdo);
    }

    public Date getDatePassageCMOrdo() {
        return datePassageCMOrdo;
    }

    public void setDatePassageCMOrdo(Date datePassageCMOrdo) {
        this.datePassageCMOrdo = DateUtil.copyDate(datePassageCMOrdo);
    }

    public Date getDatePublicationOrdo() {
        return DateUtil.copyDate(this.datePublicationOrdo);
    }

    public void setDatePublicationOrdo(Date datePublicationOrdo) {
        this.datePublicationOrdo = DateUtil.copyDate(datePublicationOrdo);
    }

    public String getTitreOfficielOrdo() {
        return titreOfficielOrdo;
    }

    public void setTitreOfficielOrdo(String titreOfficielOrdo) {
        this.titreOfficielOrdo = titreOfficielOrdo;
    }

    public String getNumeroOrdo() {
        return numeroOrdo;
    }

    public void setNumeroOrdo(String numeroOrdo) {
        this.numeroOrdo = numeroOrdo;
    }

    public String getConventionDepotOrdo() {
        return conventionDepotOrdo;
    }

    public void setConventionDepotOrdo(String conventionDepotOrdo) {
        this.conventionDepotOrdo = conventionDepotOrdo;
    }

    public Date getDateLimiteDepotOrdo() {
        return DateUtil.copyDate(this.dateLimiteDepotOrdo);
    }

    public void setDateLimiteDepotOrdo(Date dateLimiteDepotOrdo) {
        this.dateLimiteDepotOrdo = DateUtil.copyDate(dateLimiteDepotOrdo);
    }

    
   
}
