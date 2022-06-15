package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'un echeancier de promulgation
 * @author admin
 *
 */
public interface EcheancierPromulgation {
    public static final String ECHANCIER_PROMULGATION_DOC_TYPE = "EcheancierPromulgation";
    public static final String ECHANCIER_PROMULGATION_SCHEMA = "echeancier_promulgation";
    public static final String ECHANCIER_PROMULGATION_PREFIX = "echpr";

    public static final String ID_DOSSIER = "idDossier";
    public static final String DATE_SAISINE_CC = "dateSaisieCC";
    public static final String DATE_DECISION = "dateDecision";
    public static final String DATE_RECEPTION = "dateReception";
    public static final String RETOUR_JO = "retourJO";
    public static final String ENVOI_RELECTURE = "envoiRelecture";
    public static final String RETOUR_RELECTURE = "retourRelecture";
    public static final String MIS_AU_CONTRESEING = "misAuContreseing";
    public static final String CONTRESEING_PM = "contreseingPM";
    public static final String DEPART_ELYSEE = "departElysee";
    public static final String RETOUR_ELYSEE = "retourElysee";
    public static final String DATE_LIMITE_PROMULGATION = "dateLimitePromulgation";
    public static final String PUBLICATION_JO = "publicationJO";

    String getIdDossier();
    void setIdDossier(String idDossier);

    Calendar getDateSaisieCC();
    void setDateSaisieCC(Calendar dateSaisieCC);

    Calendar getDateDecision();
    void setDateDecision(Calendar dateDecision);

    Calendar getRetourJO();
    void setRetourJO(Calendar retourJO);

    Calendar getEnvoiRelecture();
    void setEnvoiRelecture(Calendar envoiRelecture);

    Calendar getRetourRelecture();
    void setRetourRelecture(Calendar retourRelecture);

    Calendar getMisAuContreseing();
    void setMisAuContreseing(Calendar misAuContreseing);

    String getContreseingPM();
    void setContreseingPM(String contreseingPM);

    Calendar getDepartElysee();
    void setDepartElysee(Calendar departElysee);

    Calendar getRetourElysee();
    void setRetourElysee(Calendar retourElysee);

    Calendar getPublicationJO();
    void setPublicationJO(Calendar publicationJO);

    Calendar getDateLimitePromulgation();
    void setDateLimitePromulgation(Calendar dateLimitePromulgation);

    Calendar getDateReception();
    void setDateReception(Calendar dateReception);

    DocumentModel getDocument();
}
