package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'un representant d'un OEP
 * 
 * @author asatre
 * 
 */
public interface RepresentantOEP {

    public static final String DOC_TYPE = "RepresentantOEP";
    public static final String SCHEMA = "representant_oep";
    public static final String REPRESENTANT_PREFIX = "roep";

    public static final String ID_FICHE_ROEP = "idfroep";
    public static final String TYPE = "type";

    public static final String REPRESENTANT = "representant";
    public static final String FONCTION = "fonction";

    public static final String DATE_DEBUT = "dateDebut";
    public static final String DATE_FIN = "dateFin";

    String getIdFicheRepresentationOEP();

    void setIdFicheRepresentationOEP(String idFicheRepresentationOEP);

    String getType();

    void setType(String type);

    String getRepresentant();

    void setRepresentant(String representant);

    String getFonction();

    void setFonction(String fonction);

    Calendar getDateDebut();

    void setDateDebut(Calendar dateDebut);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);

    DocumentModel getDocument();

}
