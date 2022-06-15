package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'un representant d'un Avis nomination
 *
 * @author asatre
 *
 */
public interface RepresentantAVI {
    public static final String DOC_TYPE = "RepresentantAVI";
    public static final String SCHEMA = "representant_avi";
    public static final String REPRESENTANT_AVI_PREFIX = "ravi";

    public static final String ID_FICHE_RAVI = "idfpavi";

    public static final String NOMINE = "nomine";

    public static final String DATE_DEBUT = "dateDebut";
    public static final String DATE_FIN = "dateFin";

    public static final String DATE_AUDITION_AN = "dateAuditionAN";
    public static final String DATE_AUDITION_SE = "dateAuditionSE";

    String getIdFicheRepresentationAVI();

    void setIdFicheRepresentationAVI(String idFicheRepresentationAVI);

    String getNomine();

    void setNomine(String nomine);

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
