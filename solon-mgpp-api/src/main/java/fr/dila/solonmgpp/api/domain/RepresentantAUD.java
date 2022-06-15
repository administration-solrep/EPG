package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface RepresentantAUD {
    String getIdFicheRepresentationAUD();

    void setIdFicheRepresentationAUD(String idFicheRepresentationAUD);

    String getPersonne();

    void setPersonne(String personne);

    String getFonction();

    void setFonction(String fonction);

    Calendar getDateDebut();

    void setDateDebut(Calendar dateDebut);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);

    DocumentModel getDocument();

    Calendar getDateAuditionSE();

    void setDateAuditionSE(Calendar dateAuditionSE);

    Calendar getDateAuditionAN();

    void setDateAuditionAN(Calendar dateAuditionAN);
}
