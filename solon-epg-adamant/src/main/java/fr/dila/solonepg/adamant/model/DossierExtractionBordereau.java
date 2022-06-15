package fr.dila.solonepg.adamant.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = DossierExtractionBordereau.ENTITY)
public class DossierExtractionBordereau extends AbstractAdamantCommonData {
    public static final String ENTITY = "DOSSIER_EXTRACTION_BORDEREAU";

    @EmbeddedId
    private BordereauPK bordereauPK;

    @Column(name = "EXTRACTION_DATE")
    private Date extractionDate;

    /**
     * Statut succès (true) / échec (false) de l'extraction
     */
    @Column(name = "SUCCESS")
    private Boolean success;

    /**
     * Message associé à la génération, potentiellement vide. Peut contenir la
     * trace de l'erreur rencontrée lors de la génération.
     */
    @Column(name = "MESSAGE")
    private String message;

    public DossierExtractionBordereau() {
        super();
    }

    public BordereauPK getBordereauPK() {
        return bordereauPK;
    }

    public void setBordereauPK(BordereauPK bordereauPK) {
        this.bordereauPK = bordereauPK;
    }

    public Date getExtractionDate() {
        return extractionDate;
    }

    public void setExtractionDate(Date extractionDate) {
        this.extractionDate = extractionDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
