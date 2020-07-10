package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.st.api.parametre.STParametre;

/**
 * Service permettant de gérer les paramètres du moteur elastic search
 * 
 * @author Amaury Bianchi
 */
public interface ElasticParametrageService {

	/**
	 * Retourne la valeur du paramètre avec l'identifiant id.
	 * 
	 * @param session
	 *            Session
	 * @param anid
	 *            Identifiant technique du paramètre
	 * @return Valeur du paramètre
	 * @throws ClientException
	 */
	String getParametreValue(CoreSession session, String anid) throws ClientException;

	/**
	 * Retourne le document paramètre avec l'identifieant id .
	 * 
	 * @param session
	 *            Session
	 * @param anid
	 *            Identifiant technique du paramètre
	 * @return Objet métier paramètre
	 * @throws ClientException
	 */
	STParametre getParametre(CoreSession session, String anid) throws ClientException;

	/**
	 * Retourne la racine des paramètres.
	 * 
	 * @param session
	 *            Session
	 * @return Document racine des paramètres
	 * @throws ClientException
	 */
	DocumentModel getParametreFolder(CoreSession session) throws ClientException;

}
