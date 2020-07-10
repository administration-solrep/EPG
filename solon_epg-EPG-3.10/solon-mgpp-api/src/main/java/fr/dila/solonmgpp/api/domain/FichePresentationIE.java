package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation d'une intervention exterieure
 * 
 * @author asatre
 * 
 */
public interface FichePresentationIE {

    public static final String DOC_TYPE = "FichePresentationIE";
    public static final String SCHEMA = "fiche_presentation_ie";
    public static final String PREFIX = "fpie";

    public static final String IDENTIFIANT_PROPOSITION = "identifiantProposition";
    public static final String AUTEUR = "auteur";
    public static final String INTITULE = "intitule";
    public static final String OBSERVATION = "observation";
    public static final String DATE = "date";

    String getIdentifiantProposition();

    void setIdentifiantProposition(String identifiantProposition);

    String getAuteur();

    void setAuteur(String auteur);

    String getIntitule();

    void setIntitule(String intitule);

    String getObservation();

    void setObservation(String observation);

    Calendar getDate();

    void setDate(Calendar date);

    DocumentModel getDocument();

}
