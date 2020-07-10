package fr.dila.solonmgpp.api.domain;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface du document contenant les paramètres du mgpp.
 * 
 * @author asatre
 */
public interface ParametrageMgpp extends Serializable {

    public static final String PATH = "/case-management/workspaces/admin";

    public static final String DOC_TYPE = "ParametrageMgpp";
    public static final String SCHEMA = "parametrage_mgpp";
    public static final String PREFIX = "pmgpp";

    public static final String URL_EPP = "urlEpp";
    public static final String LOGIN_EPP = "loginEpp";
    public static final String PASSWORD_EPP = "passEpp";

    public static final String NB_JOUR_AFFICHABLE = "nbJourAffichable";

    public static final String NOM_AN = "nomAN";
    public static final String NOM_SENAT = "nomSenat";

    public static final String DELAI = "delai";

    public static final String AUTEUR_LEX01 = "auteurLEX01";

    public static final String TEXTE_LIBRE_LISTE_OEP = "texteLibreListeOep";
    
    public static final String DELAI_PURGE_CALENDRIER = "delaiPurgeCalendrier";
    
    public static final String MINISTRE_OR_SECRETAIRE = "ministreOrSecretaire";
    
    public static final String FILTRE_DATE_CREATION_LOI = "filtreDateCreationLoi";
    
    String getUrlEpp();

    void setUrlEpp(String urlEpp);

    String getLoginEpp();

    void setLoginEpp(String loginEpp);

    String getPassEpp();

    void setPassEpp(String passEpp) throws ClientException;

    Long getNbJourAffichable();

    void setNbJourAffichable(Long nbJourAffichable);

    String getNomAN();

    void setNomAN(String nomAN);

    String getNomSenat();

    void setNomSenat(String nomSenat);

    DocumentModel getDocument();

    Long getDelai();

    void setDelai(Long delai);

    String getAuteurLex01();

    void setAuteurLex01(String auteurLex01);

    String getTexteLibreListeOep();

    void setTexteLibreListeOep(String texteLibreListeOep);
        
    Long getDelaiPurgeCalendrier() ;

    void setDelaiPurgeCalendrier(Long delaiPurgeCalendrier) ;
    
    Long getFiltreDateCreationLoi() ;

    void setFiltreDateCreationLoi(Long filtreDateCreationLoi) ;

	Boolean isMinistre();
	
	void setIsMinistre(Boolean isMinistre);
}
