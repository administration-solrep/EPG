package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service qui permet de gérer les tableaux dynamiques
 * 
 * @author asatre
 */
public interface TableauDynamiqueService extends Serializable {

	/**
	 * Liste les tableaux dynamiques de l'utilisateur courant
	 * 
	 * @param session
	 *            {@link CoreSession}
	 * @return les tableaux dynamiques du user
	 * @throws ClientException
	 */
	List<DocumentModel> findAll(CoreSession session) throws ClientException;

	/**
	 * Liste les tableaux dynamiques de l'utilisateur courant qu'il a créé lui-même
	 * 
	 * @param session
	 *            {@link CoreSession}
	 * @return les tableaux dynamiques du user
	 * @throws ClientException
	 */
	List<DocumentModel> findMine(CoreSession session) throws ClientException;

	/**
	 * Creation d'un tableau dynamqiue
	 * 
	 * @param session
	 * @param type
	 * @param query
	 * @param idDestinataires
	 *            liste d'id de postes/unités structurelles
	 * @param intitule
	 * @throws ClientException
	 */
	Map<Integer, String> createTableauDynamique(CoreSession session, String type, String query,
			List<String> idDestinataires, String intitule) throws ClientException;

	/**
	 * mise a jour d'un tableau dynamique
	 * 
	 * @param session
	 * @param tabModel
	 * @param idDestinataires
	 *            liste d'id de postes/unités structurelles
	 * @param intitule
	 * @throws ClientException
	 */
	Map<Integer, String> updateTableauDynamique(CoreSession session, DocumentModel tabModel,
			List<String> idDestinataires, String intitule) throws ClientException;

	/**
	 * Suppression d'un tableau dynamqiue
	 * 
	 * @param session
	 * @param tabModel
	 * @throws ClientException
	 */
	void deleteTableauDynamique(CoreSession session, DocumentModel tabModel) throws ClientException;

	/**
	 * Retourne True si le nombre de tableaux dynamiques pour l'utilisateur courant est inférieur au nombre maximal
	 * autorisé (via le parametrage de l'application).
	 * 
	 * @throws ClientException
	 * 
	 */
	Boolean userIsUnderQuota(CoreSession session, String userName) throws ClientException;

}
