package fr.dila.solonepg.ui.bean;

import java.util.Date;

public class StatutArchivageDossierDTO {
    private String nor;
    private String titreActe;
    private String statutArchivagePeriode;
    private Date dateChangementStatut;
    private String statutEnCours;
    private String messageErreur;
    private String dossierId;
    private String dossierLinkId;

    public StatutArchivageDossierDTO() {
        super();
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public String getStatutArchivagePeriode() {
        return statutArchivagePeriode;
    }

    public void setStatutArchivagePeriode(String statutArchivagePeriode) {
        this.statutArchivagePeriode = statutArchivagePeriode;
    }

    public Date getDateChangementStatut() {
        return dateChangementStatut;
    }

    public void setDateChangementStatut(Date dateChangementStatut) {
        this.dateChangementStatut = dateChangementStatut;
    }

    public String getStatutEnCours() {
        return statutEnCours;
    }

    public void setStatutEnCours(String statutEnCours) {
        this.statutEnCours = statutEnCours;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public String getDossierLinkId() {
        return dossierLinkId;
    }

    public void setDossierLinkId(String dossierLinkId) {
        this.dossierLinkId = dossierLinkId;
    }
}
