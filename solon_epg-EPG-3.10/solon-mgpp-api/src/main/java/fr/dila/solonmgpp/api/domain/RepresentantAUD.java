package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface RepresentantAUD {

    public static final String DOC_TYPE = "RepresentantAUD";
    public static final String SCHEMA = "representant_aud";
    public static final String REPRESENTANT_AUD_PREFIX = "raud";

    public static final String ID_FICHE_RAUD = "idfpaud";
    public static final String PERSONNE = "personne";
    public static final String FONCTION = "fonction";
    public static final String DATE_DEBUT = "dateDebut";
    public static final String DATE_FIN = "dateFin";
    public static final String DATE_AUDITION_AN = "dateAuditionAN";
    public static final String DATE_AUDITION_SENAT = "dateAuditionSenat";

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
