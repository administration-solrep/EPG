package fr.dila.solonepg.webengine.wsutil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.organigramme.UserNode;
import fr.dila.st.api.user.STUser;

public class MockOrganigrammeService implements EpgOrganigrammeService {

	private static final long	serialVersionUID	= 1L;

	@Override
	public String getMailFromUid(String uid) throws ClientException {

		return null;
	}

	@Override
	public boolean lockOrganigrammeNode(CoreSession session, OrganigrammeNode node) throws ClientException {

		return false;
	}

	@Override
	public boolean checkUniqueLabel(OrganigrammeNode node) throws ClientException {

		return false;
	}

	@Override
	public void copyNodeWithoutUser(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode)
			throws ClientException {

	}

	@Override
	public void copyNodeWithUsers(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode)
			throws ClientException {

	}

	@Override
	public List<OrganigrammeNode> getLockedNodes() throws ClientException {

		return null;
	}

	@Override
	public List<OrganigrammeNode> getParentList(OrganigrammeNode node) throws ClientException {

		return null;
	}

	@Override
	public boolean checkParentListContainsChildren(UniteStructurelleNode usNode, OrganigrammeNode node,
			Boolean showDeactivedNode) throws ClientException {

		return false;
	}

	@Override
	public Set<String> findLettreNorByPoste(String posteId) throws ClientException {

		return null;
	}

	@Override
	public List<STUser> getUserFromProfil(String profil) throws ClientException {

		return null;
	}

	@Override
	public EntiteNode getMinistereFromNor(String nor) throws ClientException {

		return null;
	}

	@Override
	public UniteStructurelleNode getDirectionFromMinistereAndNor(EntiteNode ministereNode, String nor)
			throws ClientException {

		return null;
	}

	@Override
	public List<OrganigrammeNode> getAllDirectionList() throws ClientException {

		return null;
	}

	@Override
	public void migrateNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode destinationNode)
			throws ClientException {

	}

	@Override
	public OrganigrammeNode getFirstChild(OrganigrammeNode parent, CoreSession session, Boolean showDeactivedNode)
			throws ClientException {

		return null;
	}

	@Override
	public boolean checkUniqueLabelInParent(CoreSession session, OrganigrammeNode adapter) throws ClientException {

		return false;
	}

	@Override
	public List<OrganigrammeNode> getChildrenList(CoreSession coreSession, OrganigrammeNode node,
			Boolean showDeactivedNode) throws ClientException {

		return null;
	}

	@Override
	public void deleteFromDn(OrganigrammeNode node, boolean notifyUser) throws ClientException {

	}

	@Override
	public void disableNodeFromDn(String selectedNode, OrganigrammeType type) throws ClientException {

	}

	@Override
	public void enableNodeFromDn(String selectedNode, OrganigrammeType type) throws ClientException {

	}

	@Override
	public void disableNodeFromDnNoChildrenCheck(String selectedNode, OrganigrammeType type) throws ClientException {

	}

	@Override
	public boolean unlockOrganigrammeNode(OrganigrammeNode node) throws ClientException {

		return false;
	}

	@Override
	public List<OrganigrammeNode> getParentList(OrganigrammeNode node, EntityManager manager) throws ClientException {

		return null;
	}

	@Override
	public OrganigrammeNode getOrganigrammeNodeById(String nodeId, OrganigrammeType type) throws ClientException {

		return null;
	}

	@Override
	public List<OrganigrammeNode> getOrganigrammeNodesById(Map<String, OrganigrammeType> elems) throws ClientException {

		return null;
	}

	@Override
	public OrganigrammeNode createNode(OrganigrammeNode node) throws ClientException {

		return null;
	}

	@Override
	public void updateNode(OrganigrammeNode node, Boolean notifyJournal) throws ClientException {

	}

	@Override
	public List<STUser> getUsersInSubNode(OrganigrammeNode nodeParent) throws ClientException {

		return null;
	}

	@Override
	public void updateNodes(List<? extends OrganigrammeNode> listNode, Boolean notifyJournal) throws ClientException {

	}

	@Override
	public List<String> findUserInSubNode(OrganigrammeNode nodeParent) throws ClientException {

		return null;
	}

	@Override
	public List<OrganigrammeNode> getOrganigrameLikeLabel(String label, OrganigrammeType type) throws ClientException {

		return null;
	}

	@Override
	public UserNode getUserNode(String userId) throws ClientException {

		return null;
	}

	@Override
	public List<GouvernementNode> getRootNodes() throws ClientException {

		return null;
	}

	@Override
	public PosteNode createDesactivatePoste(CoreSession coreSession, String id, String label) throws ClientException {

		return null;
	}

	@Override
	public UniteStructurelleNode createDesactivateUniteStructurelleModel(String id) throws ClientException {

		return null;
	}

	@Override
	public List<PosteNode> getAllSubPostes(OrganigrammeNode minNode) throws ClientException {

		return null;
	}

}