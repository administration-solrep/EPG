package fr.dila.solonepg.core.mock;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.organigramme.STMinisteresServiceImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;

public class MockMinisteresService extends STMinisteresServiceImpl {

    @Override
    public EntiteNode getEntiteNode(String entiteId) {
        EntiteNode node = new MockEntiteNode();
        node.setLabel("jhjhj");
        node.setId("1562");
        return node;
    }

    @Override
    public List<EntiteNode> getMinistereParentFromPoste(String posteId) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getMinistereParentFromPostes(Set<String> posteIds) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getMinistereParentFromUniteStructurelle(String ustId) {
        return new ArrayList<>();
    }

    @Override
    public List<STUser> getUserFromMinistere(String ministereId) {
        return new ArrayList<>();
    }

    @Override
    public List<STUser> getAdminsMinisterielsFromMinistere(String ministereId) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getMinisteresParents(OrganigrammeNode node) {
        return new ArrayList<>();
    }

    @Override
    public void migrateEntiteToNewGouvernement(String currentMin, String newMin) {}

    @Override
    public void migrateUnchangedEntiteToNewGouvernement(String entiteId, String newGouvernementId) {}

    @Override
    public List<String> findUserFromMinistere(String ministereId) {
        return new ArrayList<>();
    }

    @Override
    public EntiteNode createEntite(EntiteNode newEntite) {
        return null;
    }

    @Override
    public void updateEntite(CoreSession session, EntiteNode entite) {}

    @Override
    public void getMinistereParent(OrganigrammeNode node, List<EntiteNode> resultList) {}

    @Override
    public List<EntiteNode> getEntiteNodeEnfant(String nodeID) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getCurrentMinisteres() {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getAllMinisteres() {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getMinisteres(boolean active) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getEntiteNodes(Collection<String> entiteIds) {
        return new ArrayList<>();
    }

    @Override
    public EntiteNode getBareEntiteModel() {
        return null;
    }
}
