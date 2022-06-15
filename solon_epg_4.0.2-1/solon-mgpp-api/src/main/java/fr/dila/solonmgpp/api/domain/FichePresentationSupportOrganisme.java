package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface FichePresentationSupportOrganisme {
    public static final String ID_DOSSIER = "idDossier";
    public static final String ID_ORGANISME_EPP = "idOrganismeEPP";
    public static final String NOM_ORGANISME = "nomOrganisme";
    public static final String BASE_LEGALE = "baseLegale";

    public static final String DATE = "date";
    public static final String DATE_FIN = "dateFin";

    String getIdDossier();

    void setIdDossier(String idDossier);

    String getIdOrganismeEPP();

    void setIdOrganismeEPP(String idOrganismeEPP);

    DocumentModel getDocument();

    String getNomOrganisme();

    void setNomOrganisme(String nomOrganisme);

    Calendar getDate();

    void setDate(Calendar date);

    String getBaseLegale();

    void setBaseLegale(String baseLegale);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);
}
