package fr.dila.solonepg.api.service;

import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

/**
 * Service qui permet de gérer les statistiques
 * 
 * @author admin
 */
public interface StatistiquesService {

	/**
	 * get Users By status(active or not active)
	 * 
	 * @param active
	 *            the user status
	 * @return list of users
	 * @throws ClientException
	 */
	DocumentModelList getUsers(boolean active) throws ClientException;

	/**
	 * get Liste des utilisateurs ne s’étant pas connectés depuis une certaine durée
	 * 
	 * @param session
	 *            la session utilisateur
	 * @param dateDeConnexion
	 *            la date depuis laquelle l'utilisateur ne s'est pas connecte
	 * @return liste des users
	 * @throws ClientException
	 */
	DocumentModelList getUsers(CoreSession session, Date dateDeConnexion) throws ClientException;

}
