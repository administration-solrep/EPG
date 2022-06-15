package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.Ordonnance;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface OrdonnanceHabilitationDTO extends Map<String, Serializable> {
    String getId();
    void setId(String id);

    String getNumeroNor();
    void setNumeroNor(String numeroNor);

    String getObjet();
    void setObjet(String objet);

    String getMinisterePilote();
    void setMinisterePilote(String ministerePilote);

    String getLegislature();
    void setLegislature(String legislature);

    Date getDateSaisineCE();
    void setDateSaisineCE(Date dateSaisineCE);

    Date getDatePassageCM();
    void setDatePassageCM(Date datePassageCM);

    Date getDatePublication();
    void setDatePublication(Date datePublication);

    String getTitreOfficiel();
    void setTitreOfficiel(String titreOfficiel);

    String getNumero();
    void setNumero(String numero);

    String getConventionDepot();
    void setConventionDepot(String convention);

    Date getDateLimiteDepot();
    void setDateLimiteDepot(Date dateLimiteDepot);

    Ordonnance remapField(Ordonnance ordonnance);

    // Partie Lock
    Boolean isDateSaisineCELocked();
    void setDateSaisineCELocked(Boolean dateSaisineCELocked);

    Boolean isDatePassageCMLocked();
    void setDatePassageCMLocked(Boolean datePassageCMLocked);

    Boolean isDatePublicationLocked();
    void setDatePublicationLocked(Boolean datePublicationLocked);

    Boolean isTitreOfficielLocked();
    void setTitreOfficielLocked(Boolean titreOfficielLocked);

    Boolean isNumeroLocked();
    void setNumeroLocked(Boolean numeroLocked);

    Boolean isObjetLocked();
    void setObjetLocked(Boolean objetLocked);

    Boolean isMinisterePiloteLocked();
    void setMinisterePiloteLocked(Boolean ministerePiloteLocked);

    Boolean isValidate();
    void setValidate(Boolean validation);

    Boolean hasValidation();
    void setValidation(Boolean validation);

    void setObservation(String observation);
    String getObservation();

    /**
     * Validation utilisateur du rattachement ordonnance/habilitation
     */
    Boolean hasValidationLink();
    void setValidationLink(Boolean validationLink);
}
