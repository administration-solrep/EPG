package fr.dila.solonepg.api.dto;

import java.io.Serializable;

public interface TexteBilanSemestrielDTO extends Serializable {
    String getTitre();

    void setTitre(String titre);

    long getMesuresAttendues();

    void setMesuresAttendues(long mesuresAttendues);

    long getMesuresAppliquees();

    void setMesuresAppliquees(long mesuresAppliquees);

    long getMesuresEnAttente();

    void setMesuresEnAttente(long mesuresEnAttente);

    String getType();

    void setType(String type);

    String getId();

    void setId(String id);

    /**
     * Retourne le taux d'application des mesures pour le texte équivalent à
     * (mesuresAppliquees/mesuresAttendues)*100.
     */
    Double getTaux();
}
