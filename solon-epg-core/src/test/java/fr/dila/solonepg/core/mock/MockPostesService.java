package fr.dila.solonepg.core.mock;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.organigramme.STPostesServiceImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;

public class MockPostesService extends STPostesServiceImpl {

    @Override
    public PosteNode getPoste(String posteId) {
        PosteNode node = new MockPosteNode();
        node.setLabel("jhjhj");
        node.setId(posteId);
        return node;
    }

    @Override
    public String getPosteLabel(String posteId) {
        return null;
    }

    @Override
    public PosteNode getSGGPosteNode() {
        return null;
    }

    @Override
    public PosteNode getBarePosteModel() {
        return null;
    }

    @Override
    public void updatePoste(CoreSession coreSession, PosteNode poste) {}

    @Override
    public List<String> getEntiteWithoutBDCInGouvernement(OrganigrammeNode gouvernementNode) {
        return new ArrayList<>();
    }

    @Override
    public List<STUser> getUserFromPoste(String posteId) {
        return new ArrayList<>();
    }

    @Override
    public List<PosteNode> getPostesNodes(Collection<String> postesId) {
        return new ArrayList<>();
    }

    @Override
    public void deactivateBdcPosteList(List<OrganigrammeNode> bdcList) {}

    @Override
    public List<PosteNode> getPosteBdcNodeList() {
        return new ArrayList<>();
    }

    @Override
    public void addBdcPosteToNewPosteBdc(Map<String, List<OrganigrammeNode>> posteBdcToMigrate) {}

    @Override
    public List<String> getPosteIdInSubNode(OrganigrammeNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<OrganigrammeNode> getPosteBdcListInEntite(String entiteId) {
        return new ArrayList<>();
    }

    @Override
    public PosteNode getPosteBdcInEntite(String entiteId) {
        return null;
    }

    @Override
    public void createPoste(CoreSession coreSession, PosteNode newPoste) {}

    @Override
    public boolean userHasOnePosteOnly(PosteNode posteNode) {
        return false;
    }

    @Override
    public List<String> getUserNamesFromPoste(String posteId) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getEntitesParents(PosteNode poste) {
        return new ArrayList<>();
    }

    @Override
    public List<UniteStructurelleNode> getUniteStructurelleParentList(PosteNode poste) {
        return new ArrayList<>();
    }

    @Override
    public List<PosteNode> getAllPostesForUser(String userId) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllPosteNameForUser(String userId) {
        return new ArrayList<>();
    }

    @Override
    public List<PosteNode> getPosteNodeEnfant(String nodeId, OrganigrammeType type) {
        return new ArrayList<>();
    }

    @Override
    public void addUserToPostes(List<String> postes, String username) {}

    @Override
    public List<String> getAllPosteIdsForUser(String userId) {
        return new ArrayList<>();
    }

    @Override
    public void removeUserFromPoste(String poste, String userName) {}

    @Override
    public List<PosteNode> getAllPostes() {
        return new ArrayList<>();
    }

    @Override
    public String deleteUserFromAllPostes(String userId) {
        return null;
    }

    @Override
    public Collection<STUser> getUsersHavingOnePosteOnly(PosteNode posteNode) {
        return null;
    }

    @Override
    public boolean haveAnyCommonPoste(String userId1, String userId2) {
        return false;
    }

    @Override
    public String prefixPosteId(String id) {
        return null;
    }
}
