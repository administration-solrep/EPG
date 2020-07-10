package fr.dila.solonepg.api.feuilleroute;

import fr.dila.st.api.feuilleroute.STRouteStep;

/**
 * Interface des documents de type étape de feuille de route.
 * 
 * @author jtremeaux
 */
public interface SolonEpgRouteStep extends STRouteStep {
    /**
     * Retourne vrai si l'étape est de type "Pour initialisation".
     * 
     * @return Vrai pour l'étape de type "Pour initialisation"
     */
    boolean isPourInitialisation();
    
    /**
     * Fournit le numéro de version de l'acte ou de l'extrait (quand il est présent) au moment de la validation de l'étape
     * 
     * @return le numéro de version de l'acte ou de l'extrait (quand il est présent) au moment de la validation de l'étape
     */
    Long getNumeroVersion();
    
    /**
     * Définit le numéro de version de l'acte ou de l'extrait (quand il est présent) au moment de la validation de l'étape
     * 
     * @param numeroVersion Numéro de version
     */
    void setNumeroVersion(Long numeroVersion);
    
    /**
	 * Renvoie le type de destinataire de l'étape, parmi les valeurs permises.
	 * 
	 * @return une valeur de l'énumération SqueletteStepTypeDestinataire
	 */
	SqueletteStepTypeDestinataire getTypeDestinataire();

	/**
	 * Met à jour le type de destinataire de l'étape.
	 * 
	 * @param typeDestinataire
	 */
	void setTypeDestinataire(SqueletteStepTypeDestinataire typeDestinataire);
}
