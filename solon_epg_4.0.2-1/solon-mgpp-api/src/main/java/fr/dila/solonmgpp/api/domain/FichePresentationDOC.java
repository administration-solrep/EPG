package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface FichePresentationDOC {
    public static final String DOC_TYPE = "FichePresentationDOC";
    public static final String SCHEMA = "fiche_presentation_doc";
    public static final String PREFIX = "fpdoc";

    public static final String ID_DOSSIER = "idDossier";
    public static final String OBJET = "objet";
    public static final String DATE_LETTRE_PM = "dateLettrePm";
    public static final String BASE_LEGALE = "baseLegale";
    public static final String COMMISSIONS = "commissions";

    public void setIdDossier(String idDossier);

    public String getIdDossier();

    String getObjet();

    void setObjet(String objet);

    Calendar getDateLettrePm();

    void setDateLettrePm(Calendar dateLettrePm);

    String getBaseLegale();

    void setBaseLegale(String BaseLegale);

    List<String> getCommissions();

    void setCommissions(List<String> listCommissions);

    DocumentModel getDocument();
}
