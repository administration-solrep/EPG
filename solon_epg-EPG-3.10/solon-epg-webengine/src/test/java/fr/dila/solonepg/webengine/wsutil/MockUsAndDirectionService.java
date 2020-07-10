package fr.dila.solonepg.webengine.wsutil;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.STUser;

public class MockUsAndDirectionService implements STUsAndDirectionService {

	@Override
	public UniteStructurelleNode getUniteStructurelleNode(String usId) throws ClientException {
		OrganigrammeNode node = new MockUniteNode();
		node.setLabel("jhjhj");
		node.setId("1562");
		return (UniteStructurelleNode) node;
	}

	@Override
	public List<STUser> getUserFromUniteStructurelle(String uniteStructurelleId) throws ClientException {

		return null;
	}

	@Override
	public List<OrganigrammeNode> getDirectionFromPoste(String posteId) throws ClientException {

		return null;
	}

	@Override
	public List<String> findUserFromUniteStructurelle(String uniteStructurelleId) throws ClientException {

		return null;
	}

	@Override
	public List<UniteStructurelleNode> getDirectionListFromMinistere(EntiteNode ministereNode) throws ClientException {

		return null;
	}

	@Override
	public void updateUniteStructurelle(UniteStructurelleNode uniteStructurelle) throws ClientException {

	}

	@Override
	public UniteStructurelleNode createUniteStructurelle(UniteStructurelleNode newUniteStructurelle)
			throws ClientException {
		return null;
	}

	@Override
	public List<UniteStructurelleNode> getUniteStructurelleParent(UniteStructurelleNode node) throws ClientException {

		return null;
	}

	@Override
	public List<EntiteNode> getEntiteParent(UniteStructurelleNode node) throws ClientException {

		return null;
	}

	@Override
	public List<UniteStructurelleNode> getDirectionsParentsFromUser(String userId) throws ClientException {

		return null;
	}

	@Override
	public List<String> getDirectionNameParentsFromUser(String userId) throws ClientException {

		return null;
	}

	@Override
	public List<UniteStructurelleNode> getUniteStructurelleEnfant(String elementID, OrganigrammeType type)
			throws ClientException {

		return null;
	}

	@Override
	public UniteStructurelleNode getBareUniteStructurelleModel() throws ClientException {

		return null;
	}

}
