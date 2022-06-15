package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.ui.annot.NxProp;
import java.util.Calendar;

public class ConseilEtatDTO {

    public ConseilEtatDTO() {
        super();
    }

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String prioriteCE;

    private String prioriteCELibelle;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String sectionCE;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String rapporteur;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar dateTransmissionSectionCE;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar datePrevisionnelleSectionCe;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar datePrevisionnelleAgCe;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String numeroISA;

    private Boolean isEditableCE = Boolean.FALSE;

    public String getPrioriteCE() {
        return prioriteCE;
    }

    public void setPrioriteCE(String prioriteCE) {
        this.prioriteCE = prioriteCE;
    }

    public String getPrioriteCELibelle() {
        return prioriteCELibelle;
    }

    public void setPrioriteCELibelle(String prioriteCELibelle) {
        this.prioriteCELibelle = prioriteCELibelle;
    }

    public String getSectionCE() {
        return sectionCE;
    }

    public void setSectionCE(String sectionCE) {
        this.sectionCE = sectionCE;
    }

    public String getRapporteur() {
        return rapporteur;
    }

    public void setRapporteur(String rapporteur) {
        this.rapporteur = rapporteur;
    }

    public Calendar getDateTransmissionSectionCE() {
        return dateTransmissionSectionCE;
    }

    public void setDateTransmissionSectionCE(Calendar dateTransmissionSectionCE) {
        this.dateTransmissionSectionCE = dateTransmissionSectionCE;
    }

    public Calendar getDatePrevisionnelleSectionCe() {
        return datePrevisionnelleSectionCe;
    }

    public void setDatePrevisionnelleSectionCe(Calendar datePrevisionnelleSectionCe) {
        this.datePrevisionnelleSectionCe = datePrevisionnelleSectionCe;
    }

    public Calendar getDatePrevisionnelleAgCe() {
        return datePrevisionnelleAgCe;
    }

    public void setDatePrevisionnelleAgCe(Calendar datePrevisionnelleAgCe) {
        this.datePrevisionnelleAgCe = datePrevisionnelleAgCe;
    }

    public String getNumeroISA() {
        return numeroISA;
    }

    public void setNumeroISA(String numeroISA) {
        this.numeroISA = numeroISA;
    }

    public Boolean getIsEditableCE() {
        return isEditableCE;
    }

    public void setIsEditableCE(Boolean isEditableCE) {
        this.isEditableCE = isEditableCE;
    }
}
