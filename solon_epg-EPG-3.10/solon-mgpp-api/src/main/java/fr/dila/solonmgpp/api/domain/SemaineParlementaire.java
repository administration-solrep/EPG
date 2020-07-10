package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface SemaineParlementaire {

    public static final String SEMAINE_PARLEMENTAIRE_DOC_TYPE = "SemaineParlementaire";
    public static final String SEMAINE_PARLEMENTAIRE_SCHEMA = "semaine_parlementaire";
    public static final String SEMAINE_PARLEMENTAIRE_PREFIX = "sp";

    public static final String INTITULE = "intitule";
    public static final String ORIENTATION = "orientation";
    public static final String ASSEMBLEE = "assemblee";
    public static final String DATE_DEBUT = "dateDebut";
    public static final String DATE_FIN = "dateFin";


    String getIntitule();

    void setIntitule(String intitule);

    String getOrientation();

    void setOrientation(String orientation);
    
    String getAssemblee();

    void setAssemblee(String assemblee);
    
    Calendar getDateDebut();

    void setDateDebut(Calendar dateDebut);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);

    DocumentModel getDocument();

}
