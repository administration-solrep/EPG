package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation d'un decret
 * 
 * @author asatre
 * 
 */
public interface FichePresentationDecret {

    public static final String DOC_TYPE = "FichePresentationDecret";
    public static final String SCHEMA = "fiche_presentation_decret";
    public static final String PREFIX = "fpdec";

    public static final String NOR = "nor";
    public static final String NOR_LOI = "norLoi";
    public static final String DATE_JO = "dateJO";
    public static final String NOR_PUBLICATON = "norPublication";
    public static final String PAGE_JO = "pageJO";
    public static final String NOR_DECRET_OUVERTURE_SESSION_EXTRAORDINAIRE = "norOuvertureSessionExt";
    public static final String URL_PUBLICATION = "urlPublication";
    public static final String NUM_JO = "numJO";
    public static final String INTITULE = "intitule";
    public static final String NUMERO_ACTE = "numeroActe";
    public static final String OBJET = "objet";
    public static final String DATE = "date";
    public static final String ACTIF = "actif";
    public static final String RUBRIQUE = "rubrique";

    String getNor();

    void setNor(String nor);
    
    String getNorLoi();

    void setNorLoi(String norLoi);

    Calendar getDateJO();

    void setDateJO(Calendar dateJO);

    String getNorPublication();

    void setNorPublication(String norPublication);

    String getPageJO();

    void setPageJO(String pageJO);

    String getRubrique();

    void setRubrique(String rubrique);

    String getUrlPublication();

    void setUrlPublication(String urlPublication);

    String getNumJO();

    void setNumJO(String numJo);

    String getIntitule();

    void setIntitule(String intitule);

    String getNumeroActe();

    void setNumeroActe(String numeroActe);

    String getObjet();

    void setObjet(String objet);

    Calendar getDate();

    void getDate(Calendar date);

    Boolean isActif();

    void setActif(Boolean actif);
    
    public String getNorOuvertureSessionExt();

    public void setNorOuvertureSessionExt(String norOuvertureSessionExt) ;
    
    DocumentModel getDocument();
}
