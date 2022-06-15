package fr.dila.solonepg.elastic.persistence.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity(name = DossierIndexationStatus.ENTITY)
public class DossierIndexationStatus {
    public static final String ENTITY = "DossierIndexationStatus";

    @Id
    private String dossierId;

    /**
     * Date de la dernière indexation réussie
     */
    private Date lastIndexationDate;

    /**
     * Date de la dernière indexation erreur
     */
    private Date lastErrorDate;

    /**
     * Message associé à l'indexation ; vide en cas de succès complet.
     * On peut avoir un message d'avertissement sur une indexation réussie
     */
    private String message;

    /**
     * Status de la dernière indexation
     */
    @Enumerated(EnumType.STRING)
    private IndexationStatus status;

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public Date getLastIndexationDate() {
        return lastIndexationDate;
    }

    public void setLastIndexationDate(Date lastIndexationDate) {
        this.lastIndexationDate = lastIndexationDate;
    }

    public Date getLastErrorDate() {
        return lastErrorDate;
    }

    public void setLastErrorDate(Date lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IndexationStatus getStatus() {
        return status;
    }

    public void setStatus(IndexationStatus status) {
        this.status = status;
    }
}
