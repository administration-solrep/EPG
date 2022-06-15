package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class TexteMaitreLoiForm {
    @FormParam("numeroNor")
    private String numeroNor;

    @FormParam("titreActeLocked")
    private boolean titreActeLocked;

    @FormParam("ministerePiloteLocked")
    private boolean ministerePiloteLocked;

    @FormParam("numeroInterneLocked")
    private boolean numeroInterneLocked;

    @FormParam("dateEntreeEnVigueurLocked")
    private boolean dateEntreeEnVigueurLocked;

    @FormParam("datePublicationLocked")
    private boolean datePublicationLocked;

    @FormParam("titreOfficielLocked")
    private boolean titreOfficielLocked;

    @FormParam("legislaturePublicationLocked")
    private boolean legislaturePublicationLocked;

    @FormParam("natureTexteLocked")
    private boolean natureTexteLocked;

    @FormParam("procedureVoteLocked")
    private boolean procedureVoteLocked;

    @FormParam("commissionAssNationaleLocked")
    private boolean commissionAssNationaleLocked;

    @FormParam("commissionSenatLocked")
    private boolean commissionSenatLocked;

    @FormParam("numeroLocked")
    private boolean numeroLocked;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreActe")
    private String titreActe;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("ministerePiloteLabel")
    private String ministerePiloteLabel;

    @FormParam("lienJORFLegifrance")
    private String lienJORFLegifrance;

    @FormParam("numeroInterne")
    private String numeroInterne;

    @FormParam("motCle")
    private String motCle;

    @FormParam("dateEntreeEnVigueur")
    private Calendar dateEntreeEnVigueur;

    @FormParam("observation")
    private String observation;

    @FormParam("champLibre")
    private String champLibre;

    @FormParam("applicationDirecte")
    private boolean applicationDirecte;

    @FormParam("datePublication")
    private Calendar datePublication;

    @FormParam("datePromulgation")
    private Calendar datePromulgation;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreOfficiel")
    private String titreOfficiel;

    @FormParam("legislaturePublication")
    private String legislaturePublication;

    @FormParam("natureTexte")
    private String natureTexte;

    @FormParam("natureTexteLabel")
    private String natureTexteLabel;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("procedureVote")
    private String procedureVote;

    @FormParam("procedureVoteLabel")
    private String procedureVoteLabel;

    @FormParam("commissionAssNationale")
    private String commissionAssNationale;

    @FormParam("commissionSenat")
    private String commissionSenat;

    @FormParam("numero")
    private String numero;

    @FormParam("dispositionHabilitation")
    private boolean dispositionHabilitation;

    @FormParam("fondementConstitutionnel")
    private boolean fondementConstitutionnel;

    @FormParam("dateCirculationCompteRendu")
    private Calendar dateCirculationCompteRendu;

    @FormParam("dateReunionProgrammation")
    private Calendar dateReunionProgrammation;

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public boolean isTitreActeLocked() {
        return titreActeLocked;
    }

    public void setTitreActeLocked(boolean titreActeLocked) {
        this.titreActeLocked = titreActeLocked;
    }

    public boolean isMinisterePiloteLocked() {
        return ministerePiloteLocked;
    }

    public void setMinisterePiloteLocked(boolean ministerePiloteLocked) {
        this.ministerePiloteLocked = ministerePiloteLocked;
    }

    public boolean isNumeroInterneLocked() {
        return numeroInterneLocked;
    }

    public void setNumeroInterneLocked(boolean numeroInterneLocked) {
        this.numeroInterneLocked = numeroInterneLocked;
    }

    public boolean isDateEntreeEnVigueurLocked() {
        return dateEntreeEnVigueurLocked;
    }

    public void setDateEntreeEnVigueurLocked(boolean dateEntreeEnVigueurLocked) {
        this.dateEntreeEnVigueurLocked = dateEntreeEnVigueurLocked;
    }

    public boolean isDatePublicationLocked() {
        return datePublicationLocked;
    }

    public void setDatePublicationLocked(boolean datePublicationLocked) {
        this.datePublicationLocked = datePublicationLocked;
    }

    public boolean isTitreOfficielLocked() {
        return titreOfficielLocked;
    }

    public void setTitreOfficielLocked(boolean titreOfficielLocked) {
        this.titreOfficielLocked = titreOfficielLocked;
    }

    public boolean isLegislaturePublicationLocked() {
        return legislaturePublicationLocked;
    }

    public void setLegislaturePublicationLocked(boolean legislaturePublicationLocked) {
        this.legislaturePublicationLocked = legislaturePublicationLocked;
    }

    public boolean isNatureTexteLocked() {
        return natureTexteLocked;
    }

    public void setNatureTexteLocked(boolean natureTexteLocked) {
        this.natureTexteLocked = natureTexteLocked;
    }

    public boolean isProcedureVoteLocked() {
        return procedureVoteLocked;
    }

    public void setProcedureVoteLocked(boolean procedureVoteLocked) {
        this.procedureVoteLocked = procedureVoteLocked;
    }

    public boolean isCommissionAssNationaleLocked() {
        return commissionAssNationaleLocked;
    }

    public void setCommissionAssNationaleLocked(boolean commissionAssNationaleLocked) {
        this.commissionAssNationaleLocked = commissionAssNationaleLocked;
    }

    public boolean isCommissionSenatLocked() {
        return commissionSenatLocked;
    }

    public void setCommissionSenatLocked(boolean commissionSenatLocked) {
        this.commissionSenatLocked = commissionSenatLocked;
    }

    public boolean isNumeroLocked() {
        return numeroLocked;
    }

    public void setNumeroLocked(boolean numeroLocked) {
        this.numeroLocked = numeroLocked;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getMinisterePiloteLabel() {
        return ministerePiloteLabel;
    }

    public void setMinisterePiloteLabel(String ministerePiloteLabel) {
        this.ministerePiloteLabel = ministerePiloteLabel;
    }

    public String getLienJORFLegifrance() {
        return lienJORFLegifrance;
    }

    public void setLienJORFLegifrance(String lienJORFLegifrance) {
        this.lienJORFLegifrance = lienJORFLegifrance;
    }

    public String getNumeroInterne() {
        return numeroInterne;
    }

    public void setNumeroInterne(String numeroInterne) {
        this.numeroInterne = numeroInterne;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public Calendar getDateEntreeEnVigueur() {
        return dateEntreeEnVigueur;
    }

    public void setDateEntreeEnVigueur(Calendar dateEntreeEnVigueur) {
        this.dateEntreeEnVigueur = dateEntreeEnVigueur;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getChampLibre() {
        return champLibre;
    }

    public void setChampLibre(String champLibre) {
        this.champLibre = champLibre;
    }

    public boolean isApplicationDirecte() {
        return applicationDirecte;
    }

    public void setApplicationDirecte(boolean applicationDirecte) {
        this.applicationDirecte = applicationDirecte;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }

    public Calendar getDatePromulgation() {
        return datePromulgation;
    }

    public void setDatePromulgation(Calendar datePromulgation) {
        this.datePromulgation = datePromulgation;
    }

    public String getTitreOfficiel() {
        return titreOfficiel;
    }

    public void setTitreOfficiel(String titreOfficiel) {
        this.titreOfficiel = titreOfficiel;
    }

    public String getLegislaturePublication() {
        return legislaturePublication;
    }

    public void setLegislaturePublication(String legislaturePublication) {
        this.legislaturePublication = legislaturePublication;
    }

    public String getNatureTexte() {
        return natureTexte;
    }

    public void setNatureTexte(String natureTexte) {
        this.natureTexte = natureTexte;
    }

    public String getNatureTexteLabel() {
        return natureTexteLabel;
    }

    public void setNatureTexteLabel(String natureTexteLabel) {
        this.natureTexteLabel = natureTexteLabel;
    }

    public String getProcedureVote() {
        return procedureVote;
    }

    public void setProcedureVote(String procedureVote) {
        this.procedureVote = procedureVote;
    }

    public String getProcedureVoteLabel() {
        return procedureVoteLabel;
    }

    public void setProcedureVoteLabel(String procedureVoteLabel) {
        this.procedureVoteLabel = procedureVoteLabel;
    }

    public String getCommissionAssNationale() {
        return commissionAssNationale;
    }

    public void setCommissionAssNationale(String commissionAssNationale) {
        this.commissionAssNationale = commissionAssNationale;
    }

    public String getCommissionSenat() {
        return commissionSenat;
    }

    public void setCommissionSenat(String commissionSenat) {
        this.commissionSenat = commissionSenat;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isDispositionHabilitation() {
        return dispositionHabilitation;
    }

    public void setDispositionHabilitation(boolean dispositionHabilitation) {
        this.dispositionHabilitation = dispositionHabilitation;
    }

    public Calendar getDateCirculationCompteRendu() {
        return dateCirculationCompteRendu;
    }

    public void setDateCirculationCompteRendu(Calendar dateCirculationCompteRendu) {
        this.dateCirculationCompteRendu = dateCirculationCompteRendu;
    }

    public Calendar getDateReunionProgrammation() {
        return dateReunionProgrammation;
    }

    public void setDateReunionProgrammation(Calendar dateReunionProgrammation) {
        this.dateReunionProgrammation = dateReunionProgrammation;
    }

    public boolean isFondementConstitutionnel() {
        return fondementConstitutionnel;
    }

    public void setFondementConstitutionnel(boolean fondementConstitutionnel) {
        this.fondementConstitutionnel = fondementConstitutionnel;
    }
}
