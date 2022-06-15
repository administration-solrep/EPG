package fr.dila.solonepg.api.dto.tablereference;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Interface du DTO des modes de parution pour les tables de référence.
 * On y retrouve le mode, la date de début et la date de fin du mode de parution
 *
 */
public interface ModeParutionDTO extends Map<String, Serializable> {
    /**
     * retourne l'uid du document model s'il existe
     * @return
     */
    String getId();

    /**
     * Set l'uid du document model
     * @param id
     */
    void setId(String id);

    /**
     * retourne le mode de parution
     * @return
     */
    String getMode();

    /**
     * Set le mode de parution
     * @param mode
     */
    void setMode(String mode);

    /**
     * Retourne la date de début du mode de parution
     * @return
     */
    Date getDateDebut();

    /**
     * Set la date de début du mode de parution
     * @param dateDebut
     */
    void setDateDebut(Date dateDebut);

    /**
     * Retourne la date de fin du mode de parution
     * @return
     */
    Date getDateFin();

    /**
     * Set la date de fin du mode de parution
     * @param dateFin
     */
    void setDateFin(Date dateFin);
}
