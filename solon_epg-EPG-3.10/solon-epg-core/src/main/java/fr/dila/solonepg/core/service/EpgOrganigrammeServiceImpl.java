package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.OrganigrammeServiceImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.exception.LocalizedClientException;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.NorDirection;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import fr.dila.st.core.organigramme.GouvernementNodeImpl;
import fr.dila.st.core.organigramme.PosteNodeImpl;
import fr.dila.st.core.organigramme.UniteStructurelleNodeImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.LdapSessionUtil;
import fr.dila.st.core.util.PropertyUtil;

public class EpgOrganigrammeServiceImpl extends OrganigrammeServiceImpl implements EpgOrganigrammeService {
	private final static String	ALL_DIRECTION_QUERY	= "SELECT u1 FROM UniteStructurelleNode u1 WHERE u1.type = '"
															+ OrganigrammeType.DIRECTION.getValue() + "'";

	/**
	 * loggeur
	 */
	private static final Log	log					= LogFactory.getLog(EpgOrganigrammeServiceImpl.class);

	/**
	 * UID
	 */
	private static final long	serialVersionUID	= -5639299537909554785L;

	/**
	 * Constructeur
	 * 
	 * @throws ClientException
	 */
	public EpgOrganigrammeServiceImpl() throws ClientException {
		super();
	}

	@Override
	public Set<String> findLettreNorByPoste(String posteId) throws ClientException {
		Set<String> lettreNORSet = new HashSet<String>();

		// on récupère les directions des postes
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		List<OrganigrammeNode> directionList = STServiceLocator.getSTUsAndDirectionService().getDirectionFromPoste(
				posteId);
		for (OrganigrammeNode organigrammeNode : directionList) {
			if (organigrammeNode instanceof UniteStructurelleNode) {
				UniteStructurelleNode direction = (UniteStructurelleNode) organigrammeNode;
				List<NorDirection> norDirectionList = direction.getNorDirectionList();
				if (norDirectionList == null || norDirectionList.size() < 1) {
					continue;
				}

				// on récupère les ministère associée aux directions
				List<EntiteNode> ministereList = ministeresService.getMinistereParentFromUniteStructurelle(direction
						.getId());
				for (EntiteNode entite : ministereList) {
					if (entite instanceof EntiteNode) {
						EntiteNode ministere = (EntiteNode) entite;
						String norMinistere = ministere.getNorMinistere();
						if (norMinistere == null) {
							continue;
						}
						String ministereId = entite.getId();
						String norDirection = direction.getNorDirectionForMinistereId(ministereId);
						if (norDirection == null) {
							continue;
						}
						lettreNORSet.add(norMinistere + norDirection);
					}
				}
			}
		}

		return lettreNORSet;
	}

	@Override
	public List<STUser> getUserFromProfil(String profilId) throws ClientException {

		List<STUser> usersList = new ArrayList<STUser>();
		Session profilSession = null;

		final UserManager userManager = STServiceLocator.getUserManager();

		try {
			profilSession = LdapSessionUtil.getLdapSession(STConstant.ORGANIGRAMME_PROFILE_DIR);

			DocumentModel profilDocument = profilSession.getEntry(profilId);
			List<String> userIdList = PropertyUtil.getStringListProperty(profilDocument, "group", "members");

			for (String userId : userIdList) {
				DocumentModel userModel = userManager.getUserModel(userId);
				if (userModel == null) {
					log.error("Aucun utilisateur ayant pour identifiant " + userId + " n'existe, non ajouté");
				} else {
					STUser user = userModel.getAdapter(STUser.class);
					usersList.add(user);
				}
			}
		} finally {
			if (profilSession != null) {
				profilSession.close();
			}
		}

		return usersList;
	}

	@Override
	public void copyNodeWithoutUser(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode)
			throws ClientException {
		copyNode(coreSession, nodeToCopy, parentNode, Boolean.FALSE);
	}

	@Override
	public void copyNodeWithUsers(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode)
			throws ClientException {
		copyNode(coreSession, nodeToCopy, parentNode, Boolean.TRUE);
	}

	private void copyNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode,
			Boolean withUser) throws ClientException {

		if (!isCopyAllowed(nodeToCopy, parentNode)) {
			// Erreur
			throw new LocalizedClientException("error.organigramme.service.copy.not.allowed");
		}
		boolean addCopyOfLabel = false;

		// si nom identique sous le même parent : ajout de "Copie de" pour
		// le premier noeud
		String nodeToCopyLabel = nodeToCopy.getLabel();
		if (StringUtils.isNotEmpty(nodeToCopyLabel)) {
			List<OrganigrammeNode> childList = getChildrenList(null, parentNode, Boolean.TRUE);
			for (OrganigrammeNode child : childList) {
				if (nodeToCopyLabel.equals(child.getLabel())) {
					addCopyOfLabel = true;
					break;
				}
			}
		}

		// si copie sous le même parent : ajout de "Copie de" pour le
		// premier noeud
		if (getParentList(nodeToCopy).contains(parentNode)) {
			addCopyOfLabel = true;
		}

		Map<String, OrganigrammeNode> oldAndNewIdTable = new HashMap<String, OrganigrammeNode>();
		recursivCopyNode(coreSession, nodeToCopy, parentNode, null, oldAndNewIdTable, withUser, addCopyOfLabel);

	}

	@Override
	public void migrateNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode destinationNode)
			throws ClientException {

		boolean canMigrateNode = nodeToCopy != null && !nodeToCopy.getId().equals(destinationNode.getId());

		if (canMigrateNode) {

			if (!isMigrateAllowed(nodeToCopy, destinationNode)) {
				// Erreur
				throw new LocalizedClientException("error.organigramme.service.migrate.not.allowed");
			}

			Map<String, OrganigrammeNode> oldAndNewIdTable = new HashMap<String, OrganigrammeNode>();
			// mise à jour des directions lors d'un migration de ministère
			if (nodeToCopy instanceof EntiteNode) {
				if (destinationNode != null) {
					// on récupère les ids des ministères
					String oldMinistereId = nodeToCopy.getId();
					String newMinistereId = destinationNode.getId();

					if (!oldMinistereId.equals(newMinistereId)) {
						// récupération des noeuds directions du ministères
						EntiteNode epgEntite = (EntiteNode) nodeToCopy;
						List<UniteStructurelleNode> uniteStructurelleList = STServiceLocator
								.getSTUsAndDirectionService().getDirectionListFromMinistere(epgEntite);
						for (UniteStructurelleNode epgUniteStructurelleNode : uniteStructurelleList) {
							updateNorDirection(coreSession, epgUniteStructurelleNode, oldMinistereId, newMinistereId);
						}
					}
				}
			}
			migrateNodeChildrenToDestinationNode(coreSession, nodeToCopy, destinationNode, oldAndNewIdTable);

		}
	}

	/**
	 * Met à jour les lettres NOR de la direction.
	 * 
	 * @param coreSession
	 * @param nodeOldMinistere
	 * @param epgUniteStructurelleNode
	 * @param destinationNodeMinistere
	 * @throws ClientException
	 */
	protected void updateNorDirection(CoreSession coreSession, UniteStructurelleNode epgUniteStructurelleNode,
			String oldMinistereId, String newMinistereId) throws ClientException {
		// récupère la lettre nor de la direction
		String lettreDirection = epgUniteStructurelleNode.getNorDirectionForMinistereId(oldMinistereId);

		// récupère les associations NOR - Ministere_id du noeud
		List<NorDirection> norOldDirectionList = epgUniteStructurelleNode.getNorDirectionList();

		// on créé les nouvelles associations NOR - Ministere_id du noeud en enlevant l'id de l'ancien ministère et du
		// nouveau
		List<NorDirection> norNewDirectionList = new ArrayList<NorDirection>();
		for (NorDirection norRef : norOldDirectionList) {
			if (!oldMinistereId.equals(norRef.getId()) && !newMinistereId.equals(norRef.getId())) {
				norNewDirectionList.add(norRef);
			}
		}

		norNewDirectionList.add(new NorDirection(newMinistereId, lettreDirection));

		epgUniteStructurelleNode.setNorDirectionList(norNewDirectionList);
		updateNode(epgUniteStructurelleNode, true);
	}

	/**
	 * Check si la copie est possible dans le parent
	 * 
	 * @param nodeToCopy
	 * @param parentNode
	 * @return
	 */
	private boolean isCopyAllowed(OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode) {
		// si le noeud à copier est un ministère, verifie que le parent est le
		// gouvernement
		if (nodeToCopy instanceof EntiteNode) {
			if (!(parentNode instanceof GouvernementNode)) {
				return false;
			}
		}
		// si le noeud à copier est une unité structurelle, verifie que le
		// parent est un ministere ou une unité structurelle
		else if (nodeToCopy instanceof UniteStructurelleNode) {
			if (!(parentNode instanceof UniteStructurelleNode) && !(parentNode instanceof EntiteNode)) {
				return false;
			}
		}
		// si le noeud à copier est un poste, verifie que le parent est une
		// unité structurelle
		else if (nodeToCopy instanceof PosteNode) {
			if (!(parentNode instanceof UniteStructurelleNode)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check si le déplacement des éléments est possible
	 * 
	 * @param nodeToCopy
	 * @param destinationNode
	 * @return
	 */
	private boolean isMigrateAllowed(OrganigrammeNode nodeToCopy, OrganigrammeNode destinationNode) {

		// si le noeud à copier est un ministère, verifie que le noeud de destination est un ministère
		if (nodeToCopy instanceof EntiteNode) {
			if (!(destinationNode instanceof EntiteNode)) {
				return false;
			}
		}
		// si le noeud à copier est une unité structurelle, verifie que le noeud de destination est une unité
		// structurelle
		else if (nodeToCopy instanceof UniteStructurelleNode) {
			if (!(destinationNode instanceof UniteStructurelleNode)) {
				return false;
			}
		}
		// si le noeud à copier est un poste, verifie que le noeud de destination est un poste
		else if (nodeToCopy instanceof PosteNode) {
			if (!(destinationNode instanceof PosteNode)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public EntiteNode getMinistereFromNor(String nor) throws ClientException {
		List<EntiteNode> list = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
		EntiteNode result = null;
		for (EntiteNode entiteNode : list) {
			final String norMinistere = entiteNode.getNorMinistere();
			if (nor.equalsIgnoreCase(norMinistere)) {
				result = entiteNode;
				break;
			}
		}
		return result;
	}

	@Override
	public UniteStructurelleNode getDirectionFromMinistereAndNor(EntiteNode ministereNode, String nor)
			throws ClientException {
		List<UniteStructurelleNode> list = STServiceLocator.getSTUsAndDirectionService().getDirectionListFromMinistere(
				ministereNode);
		String idMinistere = ministereNode.getId();
		UniteStructurelleNode result = null;
		for (UniteStructurelleNode uniteStructurelleNode : list) {
			if (nor.equalsIgnoreCase(uniteStructurelleNode.getNorDirectionForMinistereId(idMinistere))) {
				result = uniteStructurelleNode;
				break;
			}
		}
		return result;
	}

	/**
	 * Copie l'élement nodeToCopy et les sous éléments de la branche récursivement
	 * 
	 * @param coreSession
	 * @param nodeToCopy
	 * @param parentNode
	 * @param oldAndNewIdTable
	 * @param withUsers
	 * @throws ClientException
	 */
	protected void recursivCopyNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode,
			String originalParentId, Map<String, OrganigrammeNode> oldAndNewIdTable, boolean withUsers,
			boolean addCopyOfLabel) throws ClientException {

		OrganigrammeNode newNode = null;
		if (nodeToCopy instanceof GouvernementNode) {
			newNode = new GouvernementNodeImpl((GouvernementNode) nodeToCopy);
		} else if (nodeToCopy instanceof EntiteNode) {
			newNode = new EntiteNodeImpl((EntiteNode) nodeToCopy);
		} else if (nodeToCopy instanceof UniteStructurelleNode) {
			newNode = new UniteStructurelleNodeImpl((UniteStructurelleNode) nodeToCopy);
		} else if (nodeToCopy instanceof PosteNode) {
			newNode = new PosteNodeImpl((PosteNode) nodeToCopy);
		}

		List<OrganigrammeNode> parentList = new ArrayList<OrganigrammeNode>();
		parentList.add(parentNode);
		newNode.setParentList(parentList);

		if (addCopyOfLabel) {
			newNode.setLabel("Copie de " + newNode.getLabel());
		}

		if (!withUsers && newNode instanceof PosteNode) {
			((PosteNode) newNode).setMembers(null);
		}

		if (newNode instanceof EntiteNode || newNode instanceof UniteStructurelleNode) {
			if (newNode instanceof UniteStructurelleNode) {
				// Copie la lettre du nor
				if (originalParentId != null) {
					String nor = ((UniteStructurelleNode) nodeToCopy).getNorDirectionForMinistereId(originalParentId);
					((UniteStructurelleNode) newNode).setNorDirectionForMinistereId(parentNode.getId(), nor);
				} else {
					List<NorDirection> listNorNodeToCopy = ((UniteStructurelleNode) newNode).getNorDirectionList();
					if (!listNorNodeToCopy.isEmpty()) {
						// On prend arbitrairement la première
						String lettreNor = listNorNodeToCopy.get(0).getNor();
						if (lettreNor.length() > 1) {
							lettreNor = lettreNor.substring(0, 1);
						}
						((UniteStructurelleNode) newNode).setNorDirectionForMinistereId(parentNode.getId(), lettreNor);
					}
				}
			}
			createNode(newNode);
		} else if (newNode instanceof PosteNode) {
			STServiceLocator.getSTPostesService().createPoste(coreSession, (PosteNode) newNode);
		}

		oldAndNewIdTable.put(nodeToCopy.getId(), newNode);

		List<OrganigrammeNode> nodeList = getChildrenList(null, nodeToCopy, Boolean.TRUE);
		parentList = null;
		for (OrganigrammeNode childNode : nodeList) {

			if (oldAndNewIdTable.get(childNode.getId()) != null) {
				OrganigrammeNode newChildNode = oldAndNewIdTable.get(childNode.getId());
				OrganigrammeNode newParentNode = oldAndNewIdTable.get(nodeToCopy.getId());
				// Add link
				parentList = getParentList(newChildNode);
				parentList.add(newParentNode);
				newChildNode.setParentList(parentList);
				updateNode(newChildNode, false);

			} else {
				recursivCopyNode(coreSession, childNode, newNode, nodeToCopy.getId(), oldAndNewIdTable, withUsers,
						false);
			}
		}
	}

	/**
	 * Déplace les sous éléments de l'élement nodeToMove dans l'élément destinationNode. note : les éléments fils de
	 * l'ancien noeud pointe vers le nouveau noeud du coup pas besoin de parcourir récursivement les éléments fils.
	 * 
	 * @param coreSession
	 * @param nodeToMove
	 * @param destinationNode
	 * @param oldAndNewIdTable
	 * @param withUsers
	 * @throws ClientException
	 */
	protected void migrateNodeChildrenToDestinationNode(CoreSession coreSession, OrganigrammeNode nodeToMove,
			OrganigrammeNode destinationNode, Map<String, OrganigrammeNode> oldAndNewIdTable) throws ClientException {
		// migration du noeud
		if (nodeToMove instanceof PosteNode) {

			// déplacement des utilisateurs si les noeuds sont de type poste.
			List<String> actualMembers = ((PosteNode) destinationNode).getMembers();
			if (actualMembers == null) {
				actualMembers = new ArrayList<String>();
			}
			List<String> membersToAdd = ((PosteNode) nodeToMove).getMembers();
			if (membersToAdd != null && membersToAdd.size() > 0) {
				actualMembers.addAll(membersToAdd);
				// ajout des utilisateurs dans le nouveau noeud et enregistrement
				((PosteNode) destinationNode).setMembers(actualMembers);
				updateNode(destinationNode, true);
			}
			// suppression des utilisateurs dans l'ancien noeud et enregistrement
			((PosteNode) nodeToMove).setMembers(null);
			updateNode(nodeToMove, true);
		} else if (nodeToMove instanceof UniteStructurelleNode) {

			// déplacement des postes et uniteStructurelle si les noeuds sont de type unités structurelle.

			// déplacement des postes enfants : on les rattache à la bonne unité
			List<PosteNode> actualPoste = ((UniteStructurelleNode) destinationNode).getSubPostesList();
			if (actualPoste == null) {
				actualPoste = new ArrayList<PosteNode>();
			}
			List<PosteNode> posteToAdd = ((UniteStructurelleNode) nodeToMove).getSubPostesList();
			for (PosteNode posteEnfant : posteToAdd) {
				List<String> unitesParents = posteEnfant.getParentUnitIds();
				unitesParents.remove(nodeToMove.getId());
				unitesParents.add(destinationNode.getId());

				posteEnfant.setParentUnitIds(unitesParents);
			}

			updateNodes(posteToAdd, true);

			// déplacement des unités structurelles
			List<UniteStructurelleNode> actualUniteStructurelle = ((UniteStructurelleNode) destinationNode)
					.getSubUnitesStructurellesList();
			if (actualUniteStructurelle == null) {
				actualUniteStructurelle = new ArrayList<UniteStructurelleNode>();
			}
			List<UniteStructurelleNode> uniteStructurelleToAdd = ((UniteStructurelleNode) nodeToMove)
					.getSubUnitesStructurellesList();
			for (UniteStructurelleNode uniteEnfant : uniteStructurelleToAdd) {
				List<String> unitesParents = uniteEnfant.getParentUnitIds();

				unitesParents.remove(nodeToMove.getId());
				unitesParents.add(destinationNode.getId());

				uniteEnfant.setParentUnitIds(unitesParents);
			}

			// enregistrement des nouvelles unites structurelles et postes
			updateNodes(uniteStructurelleToAdd, true);
		} else if (nodeToMove instanceof EntiteNode) {

			// déplacement des postes et uniteStructurelle si les noeuds sont de type unités structurelle.

			// déplacement des postes
			List<PosteNode> posteToAdd = ((EntiteNode) nodeToMove).getSubPostesList();
			for (PosteNode posteEnfant : posteToAdd) {
				List<String> entitesParents = posteEnfant.getParentEntiteIds();
				entitesParents.remove(nodeToMove.getId());
				entitesParents.add(destinationNode.getId());

				posteEnfant.setParentEntiteIds(entitesParents);
			}

			updateNodes(posteToAdd, true);

			// déplacement des unités structurelles
			List<UniteStructurelleNode> uniteStructurelleToAdd = ((EntiteNode) nodeToMove)
					.getSubUnitesStructurellesList();
			for (UniteStructurelleNode uniteEnfant : uniteStructurelleToAdd) {
				List<String> entitesParents = uniteEnfant.getParentEntiteIds();

				entitesParents.remove(nodeToMove.getId());
				entitesParents.add(destinationNode.getId());

				uniteEnfant.setParentEntiteIds(entitesParents);
			}

			// enregistrement des nouvelles unites structurelles et postes
			updateNodes(uniteStructurelleToAdd, true);
		}
	}

	/**
	 * déplacement des postes et uniteStructurelle si les noeuds sont de type unités structurelle.
	 * 
	 * @param session
	 * @param nodeToMove
	 * @param destinationNode
	 * @throws ClientException
	 */
	protected void moveUniteStructurelle(CoreSession session, OrganigrammeNode nodeToMove,
			OrganigrammeNode destinationNode) throws ClientException {
		// déplacement des postes
		List<PosteNode> actualPoste = ((UniteStructurelleNode) destinationNode).getSubPostesList();
		if (actualPoste == null) {
			actualPoste = new ArrayList<PosteNode>();
		}
		List<PosteNode> posteToAdd = ((UniteStructurelleNode) nodeToMove).getSubPostesList();
		if (posteToAdd != null && posteToAdd.size() > 0) {
			actualPoste.addAll(posteToAdd);
			// ajout des postes dans le nouveau noeud
			((UniteStructurelleNode) destinationNode).setSubPostesList(actualPoste);
		}
		// suppression des postes dans l'ancien noeud
		((UniteStructurelleNode) nodeToMove).setSubPostesList(null);

		// déplacement des unités structurelles
		List<UniteStructurelleNode> actualUniteStructurelle = ((UniteStructurelleNode) destinationNode)
				.getSubUnitesStructurellesList();
		if (actualUniteStructurelle == null) {
			actualUniteStructurelle = new ArrayList<UniteStructurelleNode>();
		}
		List<UniteStructurelleNode> uniteStructurelleToAdd = ((UniteStructurelleNode) nodeToMove)
				.getSubUnitesStructurellesList();
		if (uniteStructurelleToAdd != null && uniteStructurelleToAdd.size() > 0) {
			actualUniteStructurelle.addAll(uniteStructurelleToAdd);
			// ajout des unités structurelles dans le nouveau noeud
			((UniteStructurelleNode) destinationNode).setSubUnitesStructurellesList(actualUniteStructurelle);
		}
		// enregistrement des nouvelles unites structurelles et postes
		updateNode(destinationNode, true);
		// suppression des unités structurelles dans l'ancien noeud
		((UniteStructurelleNode) nodeToMove).setSubUnitesStructurellesList(null);
		updateNode(nodeToMove, true);
	}

	@Override
	protected void validateDeleteNode(CoreSession coreSession, OrganigrammeNode node) throws ClientException {
		// Cherche les postes dans les enfants si des FDR
		// actives sont liées à ces postes ou si un des utilisateurs du poste
		// n'est associé à aucun autre
		SSFeuilleRouteService fdrService = SSServiceLocator.getSSFeuilleRouteService();
		boolean fdrActive = false;
		boolean onePosteOnly = false;

		OrganigrammeNode nodeUpToDate = getOrganigrammeNodeById(node.getId(), node.getType());
		List<OrganigrammeNode> nodeList = getChildrenList(coreSession, nodeUpToDate, Boolean.TRUE);

		if (node instanceof EntiteNode) {
			String ministereId = node.getId();
			String norMinistere = ((EntiteNode) node).getNorMinistere();
			List<DocumentModel> listeDocumentAttacheEntite = null;

			// Aucun dossier ne doit être rattaché à l'entité
			DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			listeDocumentAttacheEntite = dossierService.getDossierRattacheToMinistereAndDirection(coreSession,
					ministereId, null, false);
			if (listeDocumentAttacheEntite != null && listeDocumentAttacheEntite.size() > 0) {
				throw new LocalizedClientException("organigramme.error.delete.ministere.rattache.dossier");
			}

			// Aucun modèle de feuille de route ne doit être rattaché à l'entité
			FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
			listeDocumentAttacheEntite = feuilleRouteModelService.getFdrModelFromMinistereAndDirection(coreSession,
					ministereId, null, false);
			if (listeDocumentAttacheEntite != null && listeDocumentAttacheEntite.size() > 0) {
				throw new LocalizedClientException("organigramme.error.delete.ministere.rattache.fdr.modele");
			}

			// Aucun bulletin officiel ne doit être rattaché à l'entité
			BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
			listeDocumentAttacheEntite = bulletinOfficielService.getBulletinOfficielQueryFromNor(coreSession,
					norMinistere);
			if (listeDocumentAttacheEntite != null && listeDocumentAttacheEntite.size() > 0) {
				throw new LocalizedClientException("organigramme.error.delete.ministere.rattache.bulletin");
			}

			// Aucune liste de mots clés ne doit être rattachée à l'entité
			IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
			listeDocumentAttacheEntite = indexationEpgService.getIndexationMotCleQueryFromIdMinistere(coreSession,
					ministereId);
			if (listeDocumentAttacheEntite != null && listeDocumentAttacheEntite.size() > 0) {
				throw new LocalizedClientException("organigramme.error.delete.ministere.rattache.indexation.motscles");
			}

			// RG_ADM_USR_016 : verifier les règles RG_ADM_USR_014 et RG_ADM_USR_015 pour les postes fils

		} else if (node instanceof UniteStructurelleNode && node.getType() == OrganigrammeType.DIRECTION) {
			String directionId = node.getId();
			List<DocumentModel> listeDocumentAttacheEntite = null;

			// Aucun dossier ne doit être rattaché à la direction
			DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			listeDocumentAttacheEntite = dossierService.getDossierRattacheToMinistereAndDirection(coreSession, null,
					directionId, true);
			if (listeDocumentAttacheEntite != null && listeDocumentAttacheEntite.size() > 0) {
				throw new LocalizedClientException("organigramme.error.delete.direction.rattache.dossier");
			}

			// Aucun modèle de feuille de route ne doit être rattaché à la direction
			FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
			listeDocumentAttacheEntite = feuilleRouteModelService.getFdrModelFromMinistereAndDirection(coreSession,
					null, directionId, true);
			if (listeDocumentAttacheEntite != null && listeDocumentAttacheEntite.size() > 0) {
				throw new LocalizedClientException("organigramme.error.delete.direction.rattache.fdr.modele");
			}

			// RG_ADM_USR_016 : verifier les règles RG_ADM_USR_014 et RG_ADM_USR_015 pour les postes fils

		} else if (node instanceof UniteStructurelleNode && node.getType() == OrganigrammeType.OTHER) {

			// RG_ADM_USR_016 : verifier les règles RG_ADM_USR_014 et RG_ADM_USR_015 pour les postes fils

		} else if (node instanceof PosteNode) {
			// RG_ADM_USR_014
			fdrActive = fdrService.isActiveOrFutureRouteStepForPosteId(coreSession, node.getId());
			if (fdrActive) {
				throw new LocalizedClientException("organigramme.error.delete.fdrActive");
			}

			// RG_ADM_USR_015
			onePosteOnly = STServiceLocator.getSTPostesService().userHasOnePosteOnly((PosteNode) node);
			if (onePosteOnly) {
				throw new LocalizedClientException("organigramme.error.delete.onlyOnePoste");
			}

		}

		for (OrganigrammeNode childNode : nodeList) {
			validateDeleteNode(coreSession, childNode);
		}
	}

	@Override
	public PosteNode createDesactivatePoste(CoreSession coreSession, String id, String label) throws ClientException {

		PosteNode node = new PosteNodeImpl();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, -2);
		node.setDateDebut(cal.getTime());
		cal.set(Calendar.MONTH, -1);
		node.setDateFin(cal.getTime());
		node.setId(id);
		node.setLabel(label);
		STServiceLocator.getSTPostesService().createPoste(coreSession, node);

		return node;
	}

	@Override
	public UniteStructurelleNode createDesactivateUniteStructurelleModel(String id) throws ClientException {

		UniteStructurelleNode node = new UniteStructurelleNodeImpl();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, -1);
		node.setDateDebut(cal.getTime());
		node.setDateFin(cal.getTime());
		node.setId(id);

		STServiceLocator.getSTUsAndDirectionService().createUniteStructurelle(node);

		return node;
	}

	@Override
	public List<OrganigrammeNode> getAllDirectionList() throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<OrganigrammeNode>>() {

			@Override
			public List<OrganigrammeNode> runWith(EntityManager em) throws ClientException {

				return getAllDirectionList(em);
			}
		});

	}

	@SuppressWarnings("unchecked")
	private List<OrganigrammeNode> getAllDirectionList(EntityManager manager) throws ClientException {
		List<OrganigrammeNode> directionList = new LinkedList<OrganigrammeNode>();
		Query query = manager.createQuery(ALL_DIRECTION_QUERY);

		directionList.addAll(query.getResultList());

		return directionList;
	}
}
