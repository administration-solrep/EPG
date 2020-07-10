package fr.dila.solonepg.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;

/**
 * Service permettant de gérer un catalogue de modèles de feuille de route de l'application SOLON EPG.
 * 
 * @author jtremeaux
 */
public interface FeuilleRouteModelService extends fr.dila.ss.api.service.FeuilleRouteModelService {
    /**
     * Recherche et retourne le modèle de feuille de route par défaut pour initialiser un dossier.
     * 
     * @param session Session
     * @param typeActeId Identifiant technique du type d'acte (obligatoire)
     * @param ministereId Identifiant technique du ministère (obligatoire)
     * @param directionId Identifiant technique de la direction (obligatoire)
     * @return Modèle de feuille de route par défaut
     * @throws ClientException
     */
    DocumentRoute getDefaultRoute(CoreSession session, String typeActeId, String ministereId, String directionId) throws ClientException;

    /**
     * Migre les modèles de feuille de route d'un ministère ou une direction vers un autre ministère / direction.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @param migrationLoggerModel
     * @throws ClientException
     */
    void migrerModeleFdrMinistere(CoreSession session, EntiteNode oldNode, EntiteNode newNode, MigrationLoggerModel migrationLoggerModel, Boolean desactivateModelFdr)
            throws ClientException;

    /**
     * Migre les modèles de feuille de route d'un edirection vers une autredirection.
     * 
     * @param session
     * @param oldMinistereNode
     * @param oldDirectionNode
     * @param newMinistereNode
     * @param newDirectionNode
     * @throws ClientException
     */
    void migrerModeleFdrDirection(CoreSession session, EntiteNode oldMinistereNode, UniteStructurelleNode oldDirectionNode,
    		EntiteNode newMinistereNode, UniteStructurelleNode newDirectionNode, MigrationLoggerModel migrationLoggerModel, Boolean desactivateModelFdr) throws ClientException;

    /**
     * Renvoie les modèles de feuille de route rattaché à l'entité et à la direction si <b>hasDirection</b> est vrai.
     * 
     * @param session
     * @param ministereId
     * @param directionId
     * @param hasDirection
     * @return les modèles de feuille de route rattaché à l'entité et à la direction si <b>hasDirection</b> est vrai.
     * @throws ClientException
     */
    List<DocumentModel> getFdrModelFromMinistereAndDirection(CoreSession session, String ministereId, String directionId, boolean hasDirection)
            throws ClientException;
    
    /**
     * Renvoie les modèles de feuille de route rattaché à l'entité et à la direction si <b>hasDirection</b> est vrai qui ne sont pas à l'état deleted
     * 
     * @param session
     * @param ministereId
     * @param directionId
     * @param hasDirection
     * @return les modèles de feuille de route rattaché à l'entité et à la direction si <b>hasDirection</b> est vrai.
     * @throws ClientException
     */
    List<DocumentModel> getNonDeletedFdrModelFromMinistereAndDirection(CoreSession session, String ministereId, String directionId, boolean hasDirection)
            throws ClientException;

    /**
	 * Retourne le répertoire racine des squelettes de feuilles de route.
	 * 
	 * @return Répertoire des squelettes de feuilles de route
	 * @throws ClientException
	 */
    DocumentModel getFeuilleRouteSqueletteFolder(CoreSession session) throws ClientException;

	/**
	 * FEV525
	 * Crée les modèles d'un ministere en utilisant les squelettes actifs.
	 * @throws ClientException 
	 * @return : le nombre de modèles créés
	 * 	 */
	public Integer creeModelesViaSquelette(CoreSession session, String ministereId, String bdcId, String cdmId, String scdmId, String cpmId) throws ClientException;
	
	/**
	 * Indique s'il existe déjà un squelette validé avec le type d'acte indiqué.
	 * 
	 * @return true si un tel squelette est trouvé.
	 */
	boolean existsSqueletteWithTypeActe(CoreSession session, String typeActeId) throws ClientException;
	
	/**
	 * Retourne la liste des squelettes actifs.
	 * 
	 */
	List<DocumentModel> getAllValidatedSquelette(CoreSession session) throws ClientException;
	
	
	/**
	* FEV525
	* Genere le titre d'un modele de feuille de route à partir de son ministere, poste, type d'acte et titre libre.
	* Le format est "*trigramme_ministère* - *lettre_direction* - *type_acte* - intitulé libre"
	* Seul l'intitulé libre est obligatoire.
	* @throws ClientException 
	*/
	public String creeTitleModeleFDR(String idMinistere, String idDirection, String idTypeActe, String titreLibre) throws ClientException;

	/**
     * Recherche et retourne le modèle de feuille de route par défaut global.
     * 
     * @param session Session
     * @return Modèle de feuille de route par défaut global
     * @throws ClientException
     */
	public DocumentRoute getDefaultRouteGlobal(CoreSession session) throws ClientException;
}
