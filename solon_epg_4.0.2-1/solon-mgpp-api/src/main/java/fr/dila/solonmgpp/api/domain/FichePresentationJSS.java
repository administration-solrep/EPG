package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface FichePresentationJSS {
    public static final String DOC_TYPE = "FichePresentationJSS";
    public static final String SCHEMA = "fiche_presentation_jss";
    public static final String PREFIX = "fpjss";

    public static final String ID_DOSSIER = "idDossier";
    public static final String OBJET = "objet";
    public static final String DATE_LETTRE_PM = "dateLettrePm";

    public void setIdDossier(String idDossier);

    public String getIdDossier();

    String getObjet();

    void setObjet(String objet);

    Calendar getDateLettrePm();

    void setDateLettrePm(Calendar dateLettrePm);

    DocumentModel getDocument();
}
