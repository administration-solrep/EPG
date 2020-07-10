package fr.dila.solonepg.api.service;

import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.user.STUser;

/**
 * Implémentation du service d'organigramme pour SOLON EPG.
 * 
 * @author fesposito
 * @author jtremeaux
 */
public interface EpgOrganigrammeService extends OrganigrammeService {

	/**
	 * Recherche pour un poste donné l'ensemble des lettres NOR ministère + direction correspondantes. Remonte
	 * l'organigramme afin de trouver les lettres NOR.
	 * 
	 * @param posteId
	 *            Identifiant technique du poste
	 * @return Ensemble des lettres NOR ministère + direction
	 * @throws ClientException
	 */
	Set<String> findLettreNorByPoste(String posteId) throws ClientException;

	/**
	 * 
	 * @param profil
	 * @return la list des utilisateurs du profil
	 * @throws ClientException
	 */
	List<STUser> getUserFromProfil(String profil) throws ClientException;

	/**
	 * Retourne le ministère à partir des 3 lettres Nor qui lui sont associé
	 * 
	 * @param nor
	 * @return EpgEntiteNode
	 * @throws ClientException
	 */
	EntiteNode getMinistereFromNor(String nor) throws ClientException;

	/**
	 * Retourne la direction à partir du ministère et de la lettre Nor associé à cette direction.
	 * 
	 * @param EntiteNode
	 *            ministereNode
	 * @param nor
	 * @return EpgUniteStructurelleNode
	 * @throws ClientException
	 */
	UniteStructurelleNode getDirectionFromMinistereAndNor(EntiteNode ministereNode, String nor) throws ClientException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	PosteNode createDesactivatePoste(CoreSession coreSession, String id, String label) throws ClientException;

	UniteStructurelleNode createDesactivateUniteStructurelleModel(String id) throws ClientException;

	List<OrganigrammeNode> getAllDirectionList() throws ClientException;

	/**
	 * Déplace le contenu du noeud 'nodeToCopy' dans le noeud 'destinationNode'.
	 * 
	 * @param coreSession
	 * @param nodeToCopy
	 * @param destinationNode
	 * @throws ClientException
	 */
	void migrateNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode destinationNode)
			throws ClientException;

}
