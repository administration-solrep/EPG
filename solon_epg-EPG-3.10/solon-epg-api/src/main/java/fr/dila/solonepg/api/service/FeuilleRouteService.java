package fr.dila.solonepg.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.ss.api.service.SSFeuilleRouteService;

/**
 * Service permettant d'effectuer des actions spécifiques sur les instances de feuille de route dans SOLON EPG.
 * 
 * @author jtremeaux
 */
public interface FeuilleRouteService extends SSFeuilleRouteService {
	/**
	 * Modifie l'étape "Pour initialisation" à l'instanciation d'une feuille de route, afin de renseigner dynamiquement
	 * le destinataire de cette étape.
	 * 
	 * @param session
	 *            Session
	 * @param route
	 *            Instance de feuille de route
	 * @param posteId
	 *            Identifiant technique du poste
	 * @throws ClientException
	 */
	void initEtapePourInitialisation(CoreSession session, DocumentRoute route, String posteId) throws ClientException;

	/**
	 * Recherche de l'étape "pour initialisation" d'une feuille de route. L'étape doit exister et être définie de façon
	 * unique.
	 * 
	 * @param session
	 *            Session
	 * @param route
	 *            Feuille de route
	 * @return Étape "pour initialisation"
	 * @throws ClientException
	 */
	DocumentModel getEtapePourInitialisation(CoreSession session, DocumentRoute route) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param dueDossierLinks
	 * @throws ClientException
	 */
	void validerAuromatiquementDossier(CoreSession session, List<DocumentModel> dueDossierLinks) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param dossierDoc
	 * @return
	 * @throws ClientException
	 */
	DocumentModel getEtapeInitialisationDossier(final CoreSession session, final DocumentModel dossierDoc)
			throws ClientException;

	/**
	 * Vérifie si la prochaine étape est compatible avec le type d'acte texte non publié
	 * 
	 * @param session
	 * @param feuilleRouteDocId
	 *            la feuille de route
	 * @param stepDoc
	 *            l'étape à partir de laquelle chercher
	 * @return vrai si l'étape suivante est compatible, faux sinon
	 * @throws ClientException
	 */
	boolean isNextStepCompatibleWithActeTxtNonPub(CoreSession session, String feuilleRouteDocId, DocumentModel stepDoc)
			throws ClientException;

	/**
	 * @return le nom du dernier chargé de mission ayant validé une étape "Pour avis"
	 * @throws ClientException
	 * 
	 */
	String getLastChargeMission(final CoreSession session, final DocumentModel dossierDoc) throws ClientException;

	/**
	 * @return la dernière étape de la feuille de route
	 * @throws ClientException
	 * 
	 */
	SolonEpgRouteStep getLastStep(CoreSession session, DocumentModel dossierDoc) throws ClientException;
}
