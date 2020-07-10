package fr.dila.solonepg.api.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.st.api.dossier.STDossier.DossierState;

/**
 * Service qui permet de gérer les corbeilles utilisateur / postes, c'est à dire la recherche de dossiers via leur
 * distribution (DossierLink).
 * 
 * @author jtremeaux
 */
public interface CorbeilleService extends fr.dila.st.api.service.CorbeilleService {

	/**
	 * 
	 * @param documentManager
	 * @param dossierState
	 * @param mailboxList
	 * @return la requete suivant un etat {@link DossierState}
	 * @throws ClientException
	 */
	String getInfocentreQuery(CoreSession documentManager, DossierState dossierState, Set<String> mailboxDocIds,
			Set<String> mailboxIds) throws ClientException;

	/**
	 * Retourne le CaseLink correspondant à l'étape "Pour initialisation" de la feuille de route du dossier en
	 * paramètre.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @return CaseLink de l'étape "pour initialisation"
	 * @throws ClientException
	 */
	DocumentModel getCaseLinkPourInitialisation(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	/**
	 * 
	 * Trouver les dossiers link à partir d'une liste d'identificants de dossier
	 * 
	 * @param session
	 * @param dossierIds
	 * @return
	 * @throws ClientException
	 */
	List<DossierLink> findDossierLinks(CoreSession session, List<String> dossierIds) throws ClientException;

	/**
	 * Vérifie s'il existe une étape "pour avis ce" en cours (unrestricted)
	 * 
	 */
	boolean existsPourAvisCEStep(CoreSession session, String dossierId) throws ClientException;
	
	/**
	 * Récupère le documentModel du dossier link de l'étape "pour avis ce" si elle existe, null sinon (unrestricted)
	 * 
	 */
	DocumentModel getPourAvisCeDossierLinkDoc(CoreSession session, String dossierId) throws ClientException;

	/**
	 * Récupère la date de début d'étape présente dans le dossierLink
	 * @param session
	 * @param dossierId
	 * @param stepType
	 * @return
	 */
	Calendar getStartDateForRunningStep(CoreSession session, String dossierId, String stepType);

}
