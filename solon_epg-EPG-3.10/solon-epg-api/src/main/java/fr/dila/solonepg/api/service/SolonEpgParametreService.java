package fr.dila.solonepg.api.service;

import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.st.api.service.STParametreService;

/**
 * Surcharge du service de paramètre
 * 
 */
public interface SolonEpgParametreService extends STParametreService {

	/**
	 * Renvoie l'url de la page de suivi de l'application des lois
	 * 
	 * @return
	 * @throws ClientException
	 * @throws LoginException
	 */
	public String getUrlSuiviApplicationLois() throws ClientException, LoginException;

	/**
	 * Permet de retourner le document qui contient tout le paramétrage des législatures pour l'AN
	 * 
	 * @param documentManager
	 * @return
	 * @throws ClientException
	 */
	public ParametrageAN getDocAnParametre(CoreSession documentManager) throws ClientException;

	/**
	 * Permet de mettre à jour les textes-maitres impactés par un changement d'une législature de la liste
	 * 
	 * @param oldAndNewLegis
	 *            map de remplacement des valeurs
	 * @return
	 */
	public void updateLegislaturesFromTextesMaitres(CoreSession session, HashMap<String, String> oldAndNewLegis)
			throws ClientException;

}
