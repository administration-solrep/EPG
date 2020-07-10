package fr.dila.solonmgpp.core;

import org.nuxeo.ecm.core.storage.sql.SQLRepositoryTestCase;

import fr.dila.cm.test.CaseManagementTestConstants;

/*
 * Classe de test de base de l'application SOLON MGPP.
 * 
 * - Inclut un annuaire utilisateur HSQLDB chargé à partir de fichier csv.
 */
public class SolonMgppRepositoryTestCase extends SQLRepositoryTestCase {

	@Override
	protected void deployRepositoryContrib() throws Exception {
		super.deployRepositoryContrib();
		super.deployRepositoryContrib();

		// deploy repository manager
		deployBundle("org.nuxeo.ecm.core.api");

		// deploy search
		deployBundle("org.nuxeo.ecm.platform.search.api");

		// deploy api and core bundles
		deployBundle(CaseManagementTestConstants.CASE_MANAGEMENT_API_BUNDLE);
		deployBundle(CaseManagementTestConstants.CASE_MANAGEMENT_CORE_BUNDLE);
		deployBundle("org.nuxeo.ecm.core.schema");
		deployBundle("fr.dila.ecm.platform.routing.core");
		deployBundle("org.nuxeo.ecm.automation.core");
		deployBundle("org.nuxeo.ecm.directory");
		deployBundle("org.nuxeo.ecm.platform.usermanager");
		deployBundle("org.nuxeo.ecm.directory.types.contrib");
		deployBundle("org.nuxeo.ecm.directory.sql");
		deployBundle("org.nuxeo.ecm.webapp.core");
		deployBundle("org.nuxeo.ecm.platform.content.template");
		deployBundle("org.nuxeo.ecm.platform.types.api");
		deployBundle("org.nuxeo.ecm.platform.types.core");

		deployContrib("fr.dila.st.core", "OSGI-INF/st-schema-contrib.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/st-core-type-contrib.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/st-adapter-contrib.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/service/config-framework.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/service/vocabulary-framework.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/service/security-framework.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/st-schema-contrib.xml");
		deployContrib("fr.dila.st.core", "OSGI-INF/st-life-cycle-contrib.xml");

		deployBundle("fr.dila.solonmgpp.core");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-adapter-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-config-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-content-template-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-core-type-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-documentslists-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-event-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-permissions-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-schema-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-vocabulary-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/journal-audit-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/service/corbeille-framework.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/service/evenement-framework.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/service/message-framework.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/service/evenement-type-framework.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/service/metadonnees-schema-framework.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-corbeille-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-evenement-type-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/solonmgpp-metadonnees-schema-contrib.xml");
		deployContrib("fr.dila.solonmgpp.core", "OSGI-INF/version-contrib.xml");

	}
}
