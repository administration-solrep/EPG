package fr.dila.solonepg.api.cases;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * interface des textes signalés
 * 
 * @author asatre
 * 
 */
public interface TexteSignale extends Serializable {

    Calendar getDateDemandePublicationTS();

    void setDateDemandePublicationTS(Calendar dateDemandePublicationTS);

    String getVecteurPublicationTS();

    void setVecteurPublicationTS(String vecteurPublicationTS);

    String getObservationTS();

    void setObservationTS(String observationTS);

    String getIdDossier();

    void setIdDossier(String idDossier);

    DocumentModel getDocument();

    String getType();

    void setType(String type);
}
