package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface ActiviteParlementaire {
    public static final String ACTIVITE_PARLEMENTAIRE_DOC_TYPE = "ActiviteParlementaire";
    public static final String ACTIVITE_PARLEMENTAIRE_SCHEMA = "activite_parlementaire";
    public static final String ACTIVITE_PARLEMENTAIRE_PREFIX = "ap";

    public static final String ID_DOSSIER = "idDossier";
    public static final String INTITULE = "intitule";
    public static final String ACTIVITE = "activite";
    public static final String ASSEMBLEE = "assemblee";
    public static final String DATE = "date";

    String getIdDossier();

    void setIdDossier(String idDossier);

    String getIntitule();

    void setIntitule(String intitule);

    String getActivite();

    void setActivite(String activite);

    String getAssemblee();

    void setAssemblee(String assemblee);

    Calendar getDate();

    void setDate(Calendar date);

    DocumentModel getDocument();
}
