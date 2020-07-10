package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.STRechercheService;
/**
 * 
 * 
 * @author jgomez
 * La classe de service pour les recherches
 *
 */

public interface RequeteurDossierSimpleService extends STRechercheService{
    
    public static final String REQUETE_TEXTE_INTEGRAL = "requeteTexteIntegral";

    public static final String REQUETE_CRITERES_ETAPES = "requeteCriteresEtapes";

    public static final String REQUETE_CRITERES_SECONDAIRES = "requeteCriteresSecondaires";

    public static final String REQUETE_CRITERES_PRINCIPAUX = "requeteCriteresPrincipaux";
    
	/**
	 * Retourne une nouvelle requête
	 * @param session
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	public RequeteDossierSimple getRequete(CoreSession session) throws ClientException;

    /**
     * Retourne la requête complête, construite à partir de différents models. 
     * @param model
     * @param modelNames
     * @return
     * @throws ClientException
     */
    String getFullQuery(RequeteDossierSimple requete, String... modelNames) throws ClientException;

    /**
     * Retourne la requête complête, construite à partir de différents models. 
     * @param model
     * @param modelNames
     * @return
     * @throws ClientException
     */
    DocumentModelList query(CoreSession session, RequeteDossierSimple requete) throws ClientException;
	
}
