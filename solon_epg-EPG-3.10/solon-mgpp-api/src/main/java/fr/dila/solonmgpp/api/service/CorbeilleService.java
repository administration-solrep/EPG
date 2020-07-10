package fr.dila.solonmgpp.api.service;

import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.ss.api.security.principal.SSPrincipal;

/**
 * Interface du service des corbeilles pour l'interaction epp/mgpp
 * 
 * @author asatre
 * 
 */
public interface CorbeilleService {

	/**
	 * retourne la corbeille correspondant a l'id pour l'utilisateur gouvernement de SOLON EPP
	 * 
	 * @param idCorbeille
	 * @param ssPrincipal
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<CorbeilleDTO> findCorbeille(String idCorbeille, SSPrincipal ssPrincipal, CoreSession session)
			throws ClientException;

	/**
	 * Recherche l'id du {@link CorbeilleDescriptor} en fonction de l'id du menu courant
	 * 
	 * @param idCorbeille
	 * @param session
	 * @return
	 */
	String fetchCorbeille(String idCorbeille, CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des actions possibles sur les evenements en fonction des droits de l'utilisateur courant
	 * 
	 * @param session
	 * @param ssPrincipal
	 * @param currentEvenement
	 * @return
	 */
	Set<String> findActionPossible(CoreSession session, SSPrincipal ssPrincipal, EvenementDTO currentEvenement);

	/**
	 * Nombre de dossier dans une corbeille MGPP
	 * 
	 * @param session
	 * @param corbeilleNode
	 * @return
	 * @throws ClientException
	 */
	Long countDossier(CoreSession session, CorbeilleNode corbeilleNode) throws ClientException;

	/**
	 * Nombre de dossier dans une corbeille MGPP sans vérification des acls
	 * 
	 * @param session
	 * @param corbeilleNode
	 * @return
	 * @throws ClientException
	 */
	Long countDossierUnrestricted(CoreSession documentManager, CorbeilleNode corbeilleNode) throws ClientException;

	/**
	 * requete pour les corbeilles avec des dossiers
	 * 
	 * @param corbeilleNode
	 * @param params
	 * @return
	 */
	StringBuilder getDossierQuery(CorbeilleNode corbeilleNode, List<Object> params);

	/**
	 * Récupère l'info en BDD sur l'existence de communication non_traitees/en_cours_redaction pour une corbeille donnée
	 * 
	 * @param session
	 * @param idCorbeille
	 * @throws ClientException
	 */
	boolean hasCorbeilleMessages(CoreSession session, String idCorbeille) throws ClientException;

	/**
	 * Récupère l'info en BDD sur l'existence de communication non_traitees/en_cours_redaction pour une liste de
	 * corbeilles données <br>
	 * Si au moins une corbeille a des messages = true sinon false
	 * 
	 * @param session
	 * @param idsCorbeilles
	 * @return
	 * @throws ClientException
	 */
	boolean haveCorbeillesMessages(CoreSession session, Set<String> idsCorbeilles) throws ClientException;

}
