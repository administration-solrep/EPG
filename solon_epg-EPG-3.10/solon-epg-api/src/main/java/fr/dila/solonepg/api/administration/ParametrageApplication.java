package fr.dila.solonepg.api.administration;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Interface du document contenant les paramètres de l'application (voir écran ECR_ADM_PAR_APP).
 * 
 * @author arolin
 */
public interface ParametrageApplication extends STDomainObject,Serializable {

    ///////////////////
    // getter/setter Corbeilles
    //////////////////
    
    /**
     * Getter NbDossierPage.
     */
    Long getNbDossierPage();
    
    /**
     * Setter NbDossierPage
     * 
     * @param nbDossierPage
     */
    void setNbDossierPage(Long nbDossierPage);
    
    /**
     * Getter DateRafraichissementCorbeille (minute). 
     */
    Long getDateRafraichissementCorbeille();
    
    /**
     * Setter DateRafraichissementCorbeille (minute)
     * 
     * @param dateRafraichissementCorbeille
     */
    void setDateRafraichissementCorbeille(Long dateRafraichissementCorbeille);

    /**
     * Getter MetadonneDisponibleColonneCorbeille.
     */
    List<String> getMetadonneDisponibleColonneCorbeille();
    
    /**
     * Setter MetadonneDisponibleColonneCorbeille
     * 
     * @param metadonneDisponibleColonneCorbeille
     */
    void setMetadonneDisponibleColonneCorbeille(List<String> metadonneDisponibleColonneCorbeille);
    
    ///////////////////
    // getter/setter Recherche
    //////////////////
    
    /**
     * Getter NbDerniersResultatsConsultes.
     */
    Long getNbDerniersResultatsConsultes();
    
    /**
     * Setter NbDerniersResultatsConsultes
     * 
     * @param nbDerniersResultatsConsultes
     */
    void setNbDerniersResultatsConsultes(Long nbDerniersResultatsConsultes);
    
    /**
     * Getter NbFavorisConsultation.
     */
    Long getNbFavorisConsultation();
    
    /**
     * Setter NbFavorisConsultation
     * 
     * @param nbFavorisConsultation
     */
    void setNbFavorisConsultation(Long nbFavorisConsultation);
    
    /**
     * Getter NbFavorisRecherche.
     */
    Long getNbFavorisRecherche();
    
    /**
     * Setter NbFavorisRecherche
     * 
     * @param nbFavorisRecherche
     */
    void setNbFavorisRecherche(Long nbFavorisRecherche);
    
    ///////////////////
    // getter/setter Suivi
    //////////////////

    /**
     * Getter NbDossiersSignales.
     */
    Long getNbDossiersSignales();
    
    /**
     * Setter NbDossiersSignales
     * 
     * @param nbDossiersSignales
     */
    void setNbDossiersSignales(Long nbDossiersSignales);
    
    /**
     * Getter NbTableauxDynamiques.
     */
    Long getNbTableauxDynamiques();
    
    /**
     * Setter NbTableauxDynamiques
     * 
     * @param nbTableauxDynamiques
     */
    void setNbTableauxDynamiques(Long nbTableauxDynamiques);
    
    /**
     * Getter DelaiEnvoiMailMaintienAlerte.
     */
    Long getDelaiEnvoiMailMaintienAlerte();
    
    /**
     * Setter DelaiEnvoiMailMaintienAlerte
     * 
     * @param delaiEnvoiMailMaintienAlerte
     */
    void setDelaiEnvoiMailMaintienAlerte(Long delaiEnvoiMailMaintienAlerte);
    
    /**
     * Getter DelaiAffichageMessage (sec). 
     */
    Long getDelaiAffichageMessage();
    
    /**
     * Setter DelaiAffichageMessage (sec)
     * 
     * @param delaiAffichageMessage
     */
    void setDelaiAffichageMessage(Long delaiAffichageMessage);
    
	///////////////////
	// getter/setter mails
	//////////////////
    /**
     * récupère les suffixes des mails autorisés dans l'application
     * @return
     */
    List<String> getSuffixesMailsAutorises();
    
    /**
     * set les suffixes des mails autorisés dans l'application
     * @param suffixesMailsAutorises
     */
    void setSuffixesMailsAutorises(List<String> suffixesMailsAutorises);

	// /////////////////
	// getter/setter informations parlementaires
	// ////////////////

	/**
	 * Getter UrlEppInfosParl
	 * 
	 * @return
	 */
	String getUrlEppInfosParl();

	/**
	 * Setter UrlEppInfosParl
	 * 
	 * @param urlEppInfosParl
	 */
	void setUrlEppInfosParl(String urlEppInfosParl);

	/**
	 * Getter LoginEppInfosParl
	 * 
	 * @return
	 */
	String getLoginEppInfosParl();

	/**
	 * Setter LoginEppInfosParl
	 * 
	 * @param loginEppInfosParl
	 */
	void setLoginEppInfosParl(String loginEppInfosParl);

	/**
	 * Getter PassEppInfosParl
	 * 
	 * @return
	 */
	String getPassEppInfosParl();

	/**
	 * Setter PassEppInfosParl
	 * 
	 * @param passEppInfosParl
	 * @throws ClientException
	 */
	void setPassEppInfosParl(String passEppInfosParl) throws ClientException;

}
