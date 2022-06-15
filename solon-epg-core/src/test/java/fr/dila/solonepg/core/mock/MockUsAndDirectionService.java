package fr.dila.solonepg.core.mock;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.organigramme.STUsAndDirectionServiceImpl;
import java.util.ArrayList;
import java.util.List;

public class MockUsAndDirectionService extends STUsAndDirectionServiceImpl {

    @Override
    public UniteStructurelleNode getUniteStructurelleNode(String usId) {
        UniteStructurelleNode node = new MockUniteNode();
        node.setLabel("jhjhj");
        node.setId("1562");
        return node;
    }

    @Override
    public List<STUser> getUserFromUniteStructurelle(String uniteStructurelleId) {
        return new ArrayList<>();
    }

    @Override
    public List<OrganigrammeNode> getDirectionFromPoste(String posteId) {
        return new ArrayList<>();
    }

    @Override
    public List<OrganigrammeNode> getUniteStructurelleFromPoste(String posteId) {
        return new ArrayList<>();
    }

    @Override
    public List<String> findUserFromUniteStructurelle(String uniteStructurelleId) {
        return new ArrayList<>();
    }

    @Override
    public boolean isDirectionFromMinistere(UniteStructurelleNode directionNode, EntiteNode ministereNode) {
        return false;
    }

    @Override
    public List<UniteStructurelleNode> getDirectionListFromMinistere(EntiteNode ministereNode) {
        return new ArrayList<>();
    }

    @Override
    public void updateUniteStructurelle(UniteStructurelleNode uniteStructurelle) {}

    @Override
    public UniteStructurelleNode createUniteStructurelle(UniteStructurelleNode newUniteStructurelle) {
        return null;
    }

    @Override
    public List<UniteStructurelleNode> getUniteStructurelleParent(UniteStructurelleNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<EntiteNode> getEntiteParent(UniteStructurelleNode node) {
        return new ArrayList<>();
    }

    @Override
    public List<UniteStructurelleNode> getDirectionsParentsFromUser(String userId) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getDirectionNameParentsFromUser(String userId) {
        return new ArrayList<>();
    }

    @Override
    public List<UniteStructurelleNode> getUniteStructurelleEnfant(String elementID, OrganigrammeType type) {
        return new ArrayList<>();
    }

    @Override
    public UniteStructurelleNode getBareUniteStructurelleModel() {
        return null;
    }

    @Override
    public String getLabel(String idUniteStructurelle, String defaultLabel) {
        return defaultLabel;
    }

    @Override
    public String getLabel(String directionPilote) {
        return null;
    }
}
