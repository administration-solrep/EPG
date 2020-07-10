package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation de depot de rapport
 * 
 * @author asatre
 * 
 */
public interface FichePresentation341 {

    public static final String DOC_TYPE = "FichePresentation341";
    public static final String SCHEMA = "fiche_presentation_341";
    public static final String PREFIX = "fp341";

    public static final String IDENTIFIANT_PROPOSITION = "identifiantProposition";
    public static final String AUTEUR = "auteur";
    public static final String CO_AUTEUR = "coAuteur";
    public static final String INTITULE = "intitule";
    public static final String OBJET = "objet";
    public static final String NUMERO_DEPOT = "numeroDepot";
    public static final String DATE_DEPOT = "dateDepot";
    public static final String DATE = "date";

    String getIdentifiantProposition();

    void setIdentifiantProposition(String identifiantProposition);

    String getAuteur();

    void setAuteur(String auteur);

    List<String> getCoAuteur();

    void setCoAuteur(List<String> coAuteur);

    String getIntitule();

    void setIntitule(String intitule);

    String getObjet();

    void setObjet(String objet);

    String getNumeroDepot();

    void setNumeroDepot(String numeroDepot);

    Calendar getDateDepot();

    void setDateDepot(Calendar dateDepot);

    Calendar getDate();

    void setDate(Calendar date);

    DocumentModel getDocument();

}
