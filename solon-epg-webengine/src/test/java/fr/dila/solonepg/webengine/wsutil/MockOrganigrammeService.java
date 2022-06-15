package fr.dila.solonepg.webengine.wsutil;

import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.organigramme.UserNode;
import fr.dila.st.api.user.STUser;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;

public class MockOrganigrammeService implements EpgOrganigrammeService {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMailFromUsername(String uid) {
        return null;
    }

    @Override
    public boolean lockOrganigrammeNode(CoreSession session, OrganigrammeNode node) {
        return false;
    }

    @Override
    public boolean checkUniqueLabel(OrganigrammeNode node) {
        return false;
    }

    @Override
    public void copyNodeWithoutUser(
        CoreSession coreSession,
        OrganigrammeNode nodeToCopy,
        OrganigrammeNode parentNode
    ) {}

    @Override
    public void copyNodeWithUsers(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode) {}

    @Override
    public List<OrganigrammeNode> getLockedNodes() {
        return null;
    }

    @Override
    public List<OrganigrammeNode> getParentList(OrganigrammeNode node) {
        return null;
    }

    @Override
    public boolean checkNodeContainsChild(OrganigrammeNode node, OrganigrammeNode childNode) {
        return false;
    }

    @Override
    public Set<String> findLettreNorByPoste(String posteId) {
        return null;
    }

    @Override
    public List<STUser> getUserFromProfil(String profil) {
        return null;
    }

    @Override
    public EntiteNode getMinistereFromNor(String nor) {
        return null;
    }

    @Override
    public UniteStructurelleNode getDirectionFromMinistereAndNor(EntiteNode ministereNode, String nor) {
        return null;
    }

    @Override
    public List<OrganigrammeNode> getAllDirectionList() {
        return null;
    }

    @Override
    public void migrateNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode destinationNode) {}

    @Override
    public OrganigrammeNode getFirstChild(OrganigrammeNode parent, CoreSession session, Boolean showDeactivedNode) {
        return null;
    }

    @Override
    public boolean checkEntiteUniqueLabelInParent(CoreSession session, EntiteNode adapter) {
        return false;
    }

    @Override
    public List<OrganigrammeNode> getChildrenList(
        CoreSession coreSession,
        OrganigrammeNode node,
        Boolean showDeactivedNode
    ) {
        return null;
    }

    @Override
    public Object deleteFromDn(CoreSession session, OrganigrammeNode node, boolean notifyUser) {
        return null;
    }

    @Override
    public void disableNodeFromDn(CoreSession session, String selectedNode, OrganigrammeType type) {}

    @Override
    public void enableNodeFromDn(CoreSession session, String selectedNode, OrganigrammeType type) {}

    @Override
    public void disableNodeFromDnNoChildrenCheck(String selectedNode, OrganigrammeType type) {}

    @Override
    public boolean unlockOrganigrammeNode(OrganigrammeNode node) {
        return false;
    }

    @Override
    public <T extends OrganigrammeNode> T getOrganigrammeNodeById(String nodeId, OrganigrammeType type) {
        return null;
    }

    @Override
    public List<OrganigrammeNode> getOrganigrammeNodesById(Map<String, OrganigrammeType> elems) {
        return null;
    }

    @Override
    public OrganigrammeNode createNode(OrganigrammeNode node) {
        return null;
    }

    @Override
    public void updateNode(OrganigrammeNode node, Boolean notifyJournal) {}

    @Override
    public boolean isUserInSubNode(OrganigrammeNode nodeParent, String username) {
        return false;
    }

    @Override
    public List<STUser> getUsersInSubNode(OrganigrammeNode nodeParent) {
        return null;
    }

    @Override
    public void updateNodes(List<? extends OrganigrammeNode> listNode, Boolean notifyJournal) {}

    @Override
    public List<String> findUserInSubNode(OrganigrammeNode nodeParent) {
        return null;
    }

    @Override
    public List<OrganigrammeNode> getOrganigrameLikeLabels(String label, List<OrganigrammeType> type) {
        return null;
    }

    @Override
    public UserNode getUserNode(String userId) {
        return null;
    }

    @Override
    public List<GouvernementNode> getRootNodes() {
        return null;
    }

    @Override
    public PosteNode createDesactivatePoste(CoreSession coreSession, String id, String label) {
        return null;
    }

    @Override
    public UniteStructurelleNode createDesactivateUniteStructurelleModel(String id) {
        return null;
    }

    @Override
    public List<PosteNode> getAllSubPostes(OrganigrammeNode minNode) {
        return null;
    }

    @Override
    public <T> List<T> query(String query, Map<String, Object> params) {
        return null;
    }

    @Override
    public OrganigrammeNode getOrganigrammeNodeById(String nodeId) {
        return null;
    }

    @Override
    public List<String> getAllUserInSubNode(Set<String> ministereId) {
        return null;
    }
}
