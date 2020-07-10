package fr.dila.solonepg.api.recherche;

import java.util.Calendar;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Objet métier favori de recherche.
 * 
 * @author jgomez
 * @author jtremeaux
 */
public interface FavorisRecherche extends STDomainObject {

    // *************************************************************
    // Propriétés communes aux favoris de recherche.
    // *************************************************************
    /**
     * Retourne le type de favori de recherche.
     * 
     * @return
     */
    String getType();
    
    /**
     * Renseigne le type de favori de recherche.
     * 
     * @param type Type de favori de recherche
     */
    void setType(String type);

    /**
     * Retourne la requête des critères de recherche.
     * 
     * @return Requête des critères de recherche
     */
    String getQueryPart();
	
    /**
     * Renseigne la requête des critères de recherche.
     * 
     * @param query Requête des critères de recherche
     */
	void setQueryPart(String query);

    // *************************************************************
    // Critères de recherche des modèles de feuilles de route.
    // *************************************************************
	/**
	 * Retourne l'intitulé de la feuille de route.
	 * 
	 * @return Intitulé de la feuille de route
	 */
	String getFeuilleRouteTitle();
	
    /**
     * Retourne l'identifiant technique de l'utilisateur qui a créé la feuille de route.
     * 
     * @return Identifiant technique de l'utilisateur qui a créé la feuille de route
     */
    String getFeuilleRouteCreationUtilisateur();
    
    /**
     * Renseigne l'identifiant technique de l'utilisateur qui a créé la feuille de route.
     * 
     * @param feuilleRouteCreationUtilisateur Identifiant technique de l'utilisateur qui a créé la feuille de route
     */
    void setFeuilleRouteCreationUtilisateur(String feuilleRouteCreationUtilisateur);

    /**
     * Retourne la date de création de la feuille de route (date minimum).
     * 
     * @return Date de création de la feuille de route (date minimum).
     */
    Calendar getFeuilleRouteCreationDateMin();
    
    /**
     * Renseigne la date de création de la feuille de route (date minimum).
     * 
     * @param feuilleRouteCreationDateMin Date de création de la feuille de route (date minimum).
     */
    void setFeuilleRouteCreationDateMin(Calendar feuilleRouteCreationDateMin);

    /**
     * Retourne la date de création de la feuille de route (date maximum).
     * 
     * @return Date de création de la feuille de route (date maximum).
     */
    Calendar getFeuilleRouteCreationDateMax();
    
    /**
     * Renseigne la date de création de la feuille de route (date maximum).
     * 
     * @param feuilleRouteCreationDateMax Date de création de la feuille de route (date maximum).
     */
    void setFeuilleRouteCreationDateMax(Calendar feuilleRouteCreationDateMax);

	/**
	 * Renseigne l'intitulé de la feuille de route.
	 * 
	 * @param feuilleRouteTitle Intitulé de la feuille de route
	 */
	void setFeuilleRouteTitle(String feuilleRouteTitle);
	  
    /**
     * Retourne le numéro de la feuille de route.
     * 
     * @return Numero de la feuille de route
     */
    String getFeuilleRouteNumero();
    
    /**
     * Renseigne le numéro de la feuille de route.
     * 
     * @param feuilleRouteNumero Numero de la feuille de route
     */
    void setFeuilleRouteNumero(String feuilleRouteNumero);

    /**
     * Renseigne le type d'acte.
     * 
     * @param typeActe Type d'acte
     */
    void setFeuilleRouteTypeActe(String typeActe);

    /**
     * Retourne le type d'acte.
     * 
     * @return Type d'acte
     */
    String getFeuilleRouteTypeActe();
    
    /**
     * Retourne le ministère propriétaire du modèle.
     * 
     * @return Ministère propriétaire du modèle
     */
    String getFeuilleRouteMinistere();
    
    /**
     * Renseigne le ministère propriétaire du modèle.
     * 
     * @param feuilleRouteMinistere Ministère propriétaire du modèle
     */
    void setFeuilleRouteMinistere(String feuilleRouteMinistere);

    /**
     * Retourne la direction propriétaire du modèle.
     * 
     * @return Direction propriétaire du modèle
     */
    String getFeuilleRouteDirection();
    
    /**
     * Renseigne la direction propriétaire du modèle.
     * 
     * @param feuilleRouteDirection Direction propriétaire du modèle
     */
    void setFeuilleRouteDirection(String feuilleRouteDirection);

    /**
     * Retourne vrai si la feuille de route est validée (true, false, non renseigné).
     * 
     * @return Feuille de route validée (true, false, non renseigné)
     */
    String getFeuilleRouteValidee();
    
    /**
     * Renseigne si la feuille de route validée (true, false, non renseigné).
     * 
     * @param feuilleRouteValidee Feuille de route validée (true, false, non renseigné)
     */
    void setFeuilleRouteValidee(String feuilleRouteValidee);
    
    /**
     * Retourne le type d'étape de la feuille de route (pour attribution, pour information...).
     * 
     * @return Type d'étape de la feuille de route (pour attribution, pour information...)
     */
    String getRouteStepRoutingTaskType();
    
    /**
     * Renseigne la direction propriétaire du modèle.
     * 
     * @param routeStepRoutingTaskType Type d'étape de la feuille de route (pour attribution, pour information...)
     */
    void setRouteStepRoutingTaskType(String routeStepRoutingTaskType);
    
    /**
     * Retourne la mailbox de distribution de l'étape de la feuille de route.
     * 
     * @return Mailbox de distribution de l'étape de la feuille de route
     */
    String getRouteStepDistributionMailboxId();
    
    /**
     * Renseigne la mailbox de distribution de l'étape de la feuille de route.
     * 
     * @param routeStepDistributionMailboxId Mailbox de distribution de l'étape de la feuille de route
     */
    void setRouteStepDistributionMailboxId(String routeStepDistributionMailboxId);
    
    /**
     * Retourne l'échéance indicative en nombre de jours.
     * 
     * @return Échéance indicative en nombre de jours
     */
    Long getRouteStepEcheanceIndicative();
    
    /**
     * Renseigne l'échéance indicative en nombre de jours.
     * 
     * @param routeStepEcheanceIndicative Échéance indicative en nombre de jours
     */
    void setRouteStepEcheanceIndicative(Long routeStepEcheanceIndicative);
    
    /**
     * Retourne la mailbox de distribution de l'étape de la feuille de route.
     * 
     * @return Mailbox de distribution de l'étape de la feuille de route
     */
    String getRouteStepAutomaticValidation();
    
    /**
     * Renseigne la validation automatique de l'étape (true, false, non renseigné).
     * 
     * @param routeStepAutomaticValidation Validation automatique de l'étape (true, false, non renseigné)
     */
    void setRouteStepAutomaticValidation(String routeStepAutomaticValidation);
    
    /**
     * Retourne l'état obligatoire SGG (true, false, non renseigné).
     * 
     * @return État obligatoire SGG (true, false, non renseigné)
     */
    String getRouteStepObligatoireSgg();
    
    /**
     * Renseigne l'état obligatoire SGG (true, false, non renseigné).
     * 
     * @param routeStepObligatoireSgg État obligatoire SGG (true, false, non renseigné)
     */
    void setRouteStepObligatoireSgg(String routeStepObligatoireSgg);
    
    /**
     * Retourne l'état obligatoire ministère (true, false, non renseigné).
     * 
     * @return État obligatoire ministère (true, false, non renseigné)
     */
    String getRouteStepObligatoireMinistere();
    
    /**
     * Renseigne l'état obligatoire ministère (true, false, non renseigné).
     * 
     * @param routeStepObligatoireMinistere État obligatoire ministère (true, false, non renseigné)
     */
    void setRouteStepObligatoireMinistere(String routeStepObligatoireMinistere);
    
    // *************************************************************
    // Propriétés calculées.
    // *************************************************************
	/**
	 * Détermine si le favori de recherche est de type dossier expert.
	 * 
	 * @return Vrai si le favori de recherche est de type dossier expert
	 */
	boolean isTypeDossier();
	
	/**
     * Détermine si le favori de recherche est de type dossier simple.
     * 
     * @return Vrai si le favori de recherche est de type dossier simple
     */
    boolean isTypeDossierSimple();
	
    /**
     * Détermine si le favori de recherche est de type utilisateur.
     * 
     * @return Vrai si le favori de recherche est de type utilisateur
     */
	boolean isTypeUser();
	
    /**
     * Détermine si le favori de recherche est de type modèle de feuille de route.
     * 
     * @return Vrai si le favori de recherche est de type modèle de feuille de route
     */
	boolean isTypeModeleFeuilleRoute();
	
    /**
     * Détermine si le favori de recherche est de type modèle de activité normative.
     * 
     * @return Vrai si le favori de recherche est de type activité normative
     */
	boolean isTypeActiviteNormative();
	
	
    /**
     * Retourne La cible du favori (application des lois, ordonnance, ...).
     * 
     * @return La cible du favori
     */
	public String getActiviteNormativeTarget();
}
