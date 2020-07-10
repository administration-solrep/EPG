package fr.dila.solonepg.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.feuilleroute.STRouteStep;

/**
 * Service de document routing de solon epg
 * 
 */
public interface DocumentRoutingService extends fr.dila.ss.api.service.DocumentRoutingService {

	String isNextPostsCompatibleWithNextStep(String ministereRequisId, List<DocumentModel> steps, String typeAvisCe)
			throws ClientException;

	String isNextPostCompatibleWithNextStep(String ministereRequisId, STRouteStep nextstepDocument, String typeAvisCe)
			throws ClientException;

	String isNextPostCompatibleWithNextStep(CoreSession session, STRouteStep nextstepDocument) throws ClientException;

	String isNextPostsCompatibleWithNextStep(CoreSession session, List<DocumentModel> steps) throws ClientException;

	String isNextStepCompatibleWithTypeActe(STRouteStep nextstepDocument, CoreSession documentManager, String idDoc)
			throws ClientException;

	/**
	 * Ajoute les étapes d'épreuvage à la suite de l'étape passé en paramètre
	 * 
	 * @param documentManager
	 * @param postePublicationId
	 * @param posteDanId
	 * @throws ClientException
	 */
	void createStepsPourEpreuve(CoreSession session, String posteBdcId, String postePublicationId, String posteDanId,
			DocumentModel routeDoc, String sourceDocName, String parentPath) throws ClientException;
	
	/** Valide le squelette de feuille de route. */
	DocumentRoute validateSquelette(DocumentRoute squelette, CoreSession session) throws ClientException;
	
	/**
	 * Dévalide un squelette de feuille de route (passe son lifeCycle de "validated" à "draft").
	 * 
	 * @param squelette
	 *            Squelette de feuille de route
	 * @param session
	 *            Session
	 * @return Squelette validé
	 * @throws ClientException
	 */
	DocumentRoute invalidateSquelette(final DocumentRoute squelette, CoreSession session) throws ClientException;
	
	/**
	 * Indique si le document model est un squelette de feuille de route (en opposition à un modèle notamment)
	 * @param docModel
	 * @return
	 */
	boolean isSqueletteFeuilleRoute(final DocumentModel docModel);

	/**
	 * Crée un modele à partir d'un squelette
	 * @param model
	 * @param docIds
	 * @param session
	 * @return
	 */
	public DocumentRoute createNewModelInstanceFromSquelette(final SolonEpgFeuilleRoute squelette, final DocumentModel location, 
			final String ministereId, final String bdcId, final String cdmId, final String scdmId, final String cpmId, CoreSession session);

	/**
	 * Copie la feuille de route d'un dossier vers un nouveau  
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Document modèle du dossier cible
	 * @param routeToCopyId
	 *            ID de la feuille de route à copier
	 * @return Nouveau modèle de feuille de route
	 * @throws ClientException
	 */
	public DocumentRoute duplicateFeuilleRoute(CoreSession session, DocumentModel dossierDoc,
			DocumentModel dossierFdrToCopyDoc, String routeId) throws ClientException;
	
	/**
	 * Crée un nouveau RouteStep en copiant les valeurs de celui passé en parametre.
	 * Les valeurs copiées sont celles visibles dans l'écran de création d'une étape de feuille de route.
	 * (type, distributionMailboxId, AutomaticValidation, ObligatoireSGG, ObligatoireMinistere, DeadLine)
	 * @param session
	 * @param docToCopy
	 * @param parentDocument
	 * @return
	 * @throws ClientException
	 */
	DocumentModel duplicateRouteStep(CoreSession session, DocumentModel docToCopy)
			throws ClientException;

	/**
	 * Vérifie si l'utilisateur peut valider l'étape en cours
	 * @param ssPrincipal
	 * @param dossierLink
	 * @return
	 */
	public boolean isRoutingTaskTypeValiderAllowed(SSPrincipal ssPrincipal, DossierLink dossierLink);
}
