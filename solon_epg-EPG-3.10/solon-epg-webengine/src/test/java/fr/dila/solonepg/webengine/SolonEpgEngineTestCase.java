package fr.dila.solonepg.webengine;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ss.core.SSRepositoryTestCase;

public class SolonEpgEngineTestCase extends SSRepositoryTestCase {

	public SolonEpgEngineTestCase() {
		super();
	}

	@Override
	protected void deployRepositoryContrib() throws Exception {
		super.deployRepositoryContrib();

		// epg
		// deployBundle("fr.dila.solonepg.core");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-core-type-contrib.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-schema-contrib.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-adapter-contrib.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/parapheur-framework.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/fond-dossier-framework.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/fond-dossier-model-framework.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/tree-framework.xml");
		// deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-event-contrib.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-content-template-contrib.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/acte-framework.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/parapheur-model-framework.xml");
		deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-life-cycle-contrib.xml");

	}

	protected DocumentModel createDocument(String type, String id) throws Exception {
		DocumentModel document = session.createDocumentModel(type);
		document.setPathInfo("/", id);
		document = session.createDocument(document);
		return document;
	}
}
