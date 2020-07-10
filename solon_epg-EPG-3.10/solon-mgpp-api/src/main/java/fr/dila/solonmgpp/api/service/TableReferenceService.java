package fr.dila.solonmgpp.api.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.dto.OrganismeDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.sword.xsd.solon.epp.Mandat;

/**
 * Interface du service des tables de references pour l'interaction epp/mgpp
 * 
 * @author asatre
 * 
 */
public interface TableReferenceService {

	/**
	 * chargement et mise en cache des tables de references nécessaires pour le MGPP <br/>
	 * <br/>
	 * <b>Méthode synchronisée</b>
	 * 
	 * @throws ClientException
	 */
	void loadAllTableReference(CoreSession session) throws ClientException;

	/**
	 * chargement et mise en cache des tables de references nécessaires pour le MGPP en prenant aussi les éléments
	 * inactifs <br/>
	 * <br/>
	 * <b>Méthode synchronisée</b>
	 * 
	 * @throws ClientException
	 */
	void loadAllTableReferenceActifAndInactif(CoreSession session) throws ClientException;

	/**
	 * recherche dans les tables de references de l'id
	 * 
	 * @param identifiant
	 * @param tableRef
	 * @return
	 * @throws ClientException
	 */
	TableReferenceDTO findTableReferenceByIdAndType(String identifiant, String tableRef, Date date, CoreSession session)
			throws ClientException;

	/**
	 * 
	 * @param identifiant
	 * @param tableRef
	 * @param date
	 * @param session
	 * @param skipDate
	 * @return
	 * @throws ClientException
	 */
	TableReferenceDTO findTableReferenceByIdAndType(String identifiant, String tableRef, Date date,
			CoreSession session, boolean skipDate) throws ClientException;

	/**
	 * Recherche dans une table de reference
	 * 
	 * @param search
	 * @param tableReference
	 * @return
	 * @throws ClientException
	 */
	List<TableReferenceDTO> searchTableReference(String search, String tableReference, CoreSession session)
			throws ClientException;

	/**
	 * Recherche d'une table de ref dans EPP
	 * 
	 * @param identifiant
	 * @param tableRef
	 * @param date
	 * @return
	 * @throws ClientException
	 */
	TableReferenceDTO findTableReferenceByIdAndTypeWS(String identifiant, String tableReference, Date date,
			CoreSession session) throws ClientException;

	/**
	 * Recherche dans les tables de references (avec appartenance au proprietaire seulement)
	 * 
	 * @param search
	 * @param tableReference
	 * @param session
	 * @param proprietaire
	 * @return
	 * @throws ClientException
	 */
	List<TableReferenceDTO> searchTableReference(String search, String tableReference, CoreSession session,
			String proprietaire, String organismeType) throws ClientException;

	List<TableReferenceEppNode> getGouvernementList(CoreSession session) throws ClientException;

	List<TableReferenceEppNode> getChildrenList(TableReferenceEppNode parentNode, CoreSession session)
			throws ClientException;

	List<TableReferenceDTO> searchTableReferenceAuteurSuggestion(String search, String tableReference,
			CoreSession session, String proprietaire, String organismeType) throws ClientException;

	void createGouvernement(GouvernementNode gouvernement, CoreSession session) throws ClientException;

	void updateGouvernement(GouvernementNode gouvernement, CoreSession session) throws ClientException;

	void createMinistere(MinistereNode ministere, CoreSession session) throws ClientException;

	void updateMinistere(MinistereNode ministere, CoreSession session) throws ClientException;

	void createMandat(MandatNode mandatNode, CoreSession session) throws ClientException;

	void updateMandat(MandatNode mandatNode, CoreSession session) throws ClientException;

	void createIdentite(IdentiteNode identiteNode, CoreSession session) throws ClientException;

	void createIdentite(MandatNode mandatNode, CoreSession session) throws ClientException;

	void updateIdentite(MandatNode mandatNode, CoreSession session) throws ClientException;

	void createActeur(MandatNode mandatNode, CoreSession session) throws ClientException;

	void updateActeur(MandatNode mandatNode, CoreSession session) throws ClientException;

	GouvernementNode getGouvernement(String gouvernementId, CoreSession session) throws ClientException;

	MinistereNode getMinistere(String ministereId, CoreSession session) throws ClientException;

	MandatNode getMandat(String mandatId, CoreSession session) throws ClientException;

	/**
	 * Récupère les mandats actifs en fonction du nor associé
	 * 
	 * @param nor
	 * @param session
	 * @return List mandats actifs rattachés au nor ; liste vide si aucun résultat
	 * @throws ClientException
	 */
	List<Mandat> getMandatsByNor(String nor, CoreSession session) throws ClientException;

	IdentiteNode getIdentiteNode(CoreSession session, String prenomNom) throws ClientException;

	MinistereNode getMinistereNode(CoreSession session, String nom, String libelle, String appellation, String edition)
			throws ClientException;

	/**
	 * Met à jour le cache des identités et des organismes
	 * 
	 * @throws ClientException
	 */
	void updateCaches(CoreSession documentManager) throws ClientException;

	Calendar getLastUpdateCache();

	List<OrganismeDTO> getAllOrganismeByType(String Type, CoreSession session);

}
