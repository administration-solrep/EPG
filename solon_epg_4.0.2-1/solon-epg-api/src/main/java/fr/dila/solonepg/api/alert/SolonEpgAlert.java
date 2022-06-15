package fr.dila.solonepg.api.alert;

import fr.dila.st.api.alert.Alert;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Alerte Solon EPG.
 * <p>Represente le modèle  "Alerte" de Solon Epg</p>
 *
 */
public interface SolonEpgAlert extends Alert, Serializable {
    ///////////////////
    // getter/setter property
    //////////////////

    /**
     * Getter dateDemandeConfirmation
     *
     * @return Calendar
     */
    Calendar getDateDemandeConfirmation();

    /**
     * Setter dateDemandeConfirmation
     *
     * @param dateDemandeConfirmation
     */
    void setDateDemandeConfirmation(Calendar dateDemandeConfirmation);

    /**
     * Getter hasDemandeConfirmation : définit si une demande de confirmation de l'alerte est en cours.
     *
     * @return Boolean
     */
    Boolean getHasDemandeConfirmation();

    /**
     * Setter hasDemandeConfirmation
     *
     * @param hasDemandeConfirmation
     */
    void setHasDemandeConfirmation(Boolean hasDemandeConfirmation);
}
