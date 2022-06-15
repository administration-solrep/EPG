package fr.dila.solonepg.api.cases;

import java.util.Calendar;
import java.util.List;

/**
 * Interface pour les ordonnances de l'activite normative.
 *
 * @author asatre
 *
 */
public interface Ordonnance extends TexteMaitre {
    String getLegislature();
    void setLegislature(String legislature);

    Calendar getDateSaisineCE();
    void setDateSaisineCE(Calendar dateSaisineCE);

    Calendar getDatePassageCM();
    void setDatePassageCM(Calendar datePassageCM);

    String getConventionDepot();
    void setConventionDepot(String convention);

    Calendar getDateLimiteDepot();
    void setDateLimiteDepot(Calendar dateLimiteDepot);

    // Partie Lock
    Boolean isObjetLocked();
    void setObjetLocked(Boolean objetLocked);

    Boolean isDateSaisineCELocked();
    void setDateSaisineCELocked(Boolean dateSaisineCELocked);

    Boolean isDatePassageCMLocked();
    void setDatePassageCMLocked(Boolean datePassageCMLocked);

    Boolean isMinisterePiloteLocked();
    void setMinisterePiloteLocked(Boolean ministerePiloteLocked);

    /**
     * récupère la liste des décrets de la mesure qui n'ont pas été validés pour cette ordonnance
     * @return
     */
    List<String> getDecretsIdsInvalidated();
    void setDecretsIdsInvalidated(List<String> decretsIdsNotValidated);
}
