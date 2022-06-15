package fr.dila.solonepg.adamant.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = RetourVitamRapportVersement.ENTITY)
public class RetourVitamRapportVersement extends AbstractVitamCommonData {
    public static final String ENTITY = "RETOUR_VITAM_RAPPORT_VERSEMENT";

    /**
     * Statut de versement VITAM
     */
    @Column(name = "STATUT_VERSEMENT")
    private String statutVersement;

    /**
     * Date de versement VITAM
     */
    @Column(name = "DATE_VERSEMENT")
    private Date dateVersement;

    /**
     * Identifiant VITAM
     */
    @Column(name = "ID_VITAM")
    private String idVitam;

    /**
     * Message rejet VITAM
     */
    @Column(name = "MESSAGE_REJET")
    private String messageRejet;

    public RetourVitamRapportVersement() {
        super();
    }

    public String getStatutVersement() {
        return statutVersement;
    }

    public void setStatutVersement(String statutVersement) {
        this.statutVersement = statutVersement;
    }

    public Date getDateVersement() {
        return dateVersement;
    }

    public void setDateVersement(Date dateVersement) {
        this.dateVersement = dateVersement;
    }

    public String getIdVitam() {
        return idVitam;
    }

    public void setIdVitam(String idVitam) {
        this.idVitam = idVitam;
    }

    public String getMessageRejet() {
        return messageRejet;
    }

    public void setMessageRejet(String messageRejet) {
        this.messageRejet = messageRejet;
    }
}
