package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.ArrayList;
import javax.ws.rs.FormParam;

@SwBean
public class MesureForm {
    @FormParam("valider")
    private Boolean valider;

    @FormParam("numeroOrdre")
    private String numeroOrdre;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("article")
    private String article;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("baseLegale")
    private String baseLegale;

    @FormParam("objet")
    private String objet;

    @FormParam("directionResponsable")
    private String directionResponsable;

    @FormParam("consultationHorsCE")
    private String consultationHorsCE;

    @FormParam("calendrierHorsCE")
    private String calendrierHorsCE;

    @FormParam("datePrevisionnelleSaisineCE")
    private String datePrevisionnelleSaisineCE;

    @FormParam("dateObjectifPublication")
    private String dateObjectifPublication;

    @FormParam("typeMesure")
    private String typeMesure;

    @FormParam("differeApplication")
    private Boolean differeApplication;

    @FormParam("applicationRecu")
    private Boolean applicationRecu;

    @FormParam("norDecrets")
    private ArrayList<String> decretsNors;

    @FormParam("dateMiseApplication")
    private String dateMiseApplication;

    @FormParam("dateEntreeVigueur")
    private String dateEntreeVigueur;

    @FormParam("codeModifie")
    private String codeModifie;

    @FormParam("poleChargeMission")
    private String poleChargeMission;

    @FormParam("amendement")
    private Boolean amendement;

    @FormParam("responsableAmendement")
    private String responsableAmendement;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("datePassageCM")
    private String datePassageCM;

    @FormParam("numeroQuestion")
    private String numeroQuestion;

    @FormParam("champLibre")
    private String champLibre;

    @FormParam("communicationMinisterielle")
    private String communicationMinisterielle;

    @FormParam("observation")
    private String observation;

    public MesureForm() {}

    public Boolean getValider() {
        return valider;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getBaseLegale() {
        return baseLegale;
    }

    public void setBaseLegale(String baseLegale) {
        this.baseLegale = baseLegale;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDirectionResponsable() {
        return directionResponsable;
    }

    public void setDirectionResponsable(String directionResponsable) {
        this.directionResponsable = directionResponsable;
    }

    public String getConsultationHorsCE() {
        return consultationHorsCE;
    }

    public void setConsultationHorsCE(String consultationHorsCE) {
        this.consultationHorsCE = consultationHorsCE;
    }

    public String getCalendrierHorsCE() {
        return calendrierHorsCE;
    }

    public void setCalendrierHorsCE(String calendrierHorsCE) {
        this.calendrierHorsCE = calendrierHorsCE;
    }

    public String getDatePrevisionnelleSaisineCE() {
        return datePrevisionnelleSaisineCE;
    }

    public void setDatePrevisionnelleSaisineCE(String datePrevisionnelleSaisineCE) {
        this.datePrevisionnelleSaisineCE = datePrevisionnelleSaisineCE;
    }

    public String getDateObjectifPublication() {
        return dateObjectifPublication;
    }

    public void setDateObjectifPublication(String dateObjectifPublication) {
        this.dateObjectifPublication = dateObjectifPublication;
    }

    public String getTypeMesure() {
        return typeMesure;
    }

    public void setTypeMesure(String typeMesure) {
        this.typeMesure = typeMesure;
    }

    public Boolean getDiffereApplication() {
        return differeApplication;
    }

    public void setDiffereApplication(Boolean differeApplication) {
        this.differeApplication = differeApplication;
    }

    public Boolean getApplicationRecu() {
        return applicationRecu;
    }

    public void setApplicationRecu(Boolean applicationRecu) {
        this.applicationRecu = applicationRecu;
    }

    public ArrayList<String> getDecretsNors() {
        return decretsNors;
    }

    public void setDecretsNors(ArrayList<String> idDecrets) {
        this.decretsNors = idDecrets;
    }

    public String getDateMiseApplication() {
        return dateMiseApplication;
    }

    public void setDateMiseApplication(String dateMiseApplication) {
        this.dateMiseApplication = dateMiseApplication;
    }

    public String getDateEntreeVigueur() {
        return dateEntreeVigueur;
    }

    public void setDateEntreeVigueur(String dateEntreeVigueur) {
        this.dateEntreeVigueur = dateEntreeVigueur;
    }

    public String getCodeModifie() {
        return codeModifie;
    }

    public void setCodeModifie(String codeModifie) {
        this.codeModifie = codeModifie;
    }

    public String getPoleChargeMission() {
        return poleChargeMission;
    }

    public void setPoleChargeMission(String poleChargeMission) {
        this.poleChargeMission = poleChargeMission;
    }

    public Boolean getAmendement() {
        return amendement;
    }

    public void setAmendement(Boolean amendement) {
        this.amendement = amendement;
    }

    public String getResponsableAmendement() {
        return responsableAmendement;
    }

    public void setResponsableAmendement(String responsableAmendement) {
        this.responsableAmendement = responsableAmendement;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getDatePassageCM() {
        return datePassageCM;
    }

    public void setDatePassageCM(String datePassageCM) {
        this.datePassageCM = datePassageCM;
    }

    public String getNumeroQuestion() {
        return numeroQuestion;
    }

    public void setNumeroQuestion(String numeroQuestion) {
        this.numeroQuestion = numeroQuestion;
    }

    public String getChampLibre() {
        return champLibre;
    }

    public void setChampLibre(String champLibre) {
        this.champLibre = champLibre;
    }

    public String getCommunicationMinisterielle() {
        return communicationMinisterielle;
    }

    public void setCommunicationMinisterielle(String communicationMinisterielle) {
        this.communicationMinisterielle = communicationMinisterielle;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
