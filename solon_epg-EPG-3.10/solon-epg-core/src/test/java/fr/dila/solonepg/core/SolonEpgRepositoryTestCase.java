package fr.dila.solonepg.core;

import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.DirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.ldap.LDAPSession;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import fr.dila.cm.caselink.ActionableCaseLink;
import fr.dila.cm.caselink.CaseLink;
import fr.dila.cm.cases.Case;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.service.CaseDistributionService;
import fr.dila.cm.service.CaseManagementDistributionTypeService;
import fr.dila.cm.service.CaseManagementDocumentTypeService;
import fr.dila.cm.service.MailboxManagementService;
import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DocumentRoutingService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.SSRepositoryTestCase;
import fr.dila.ss.core.helper.FeuilleRouteTestHelper;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.core.schema.DublincoreSchemaUtils;

/**
 * Classe de test de base de l'application SOLON EPG..cm.
 * 
 * - Inclut un annuaire utilisateur HSQLDB chargé à partir de fichier csv.
 */
public class SolonEpgRepositoryTestCase extends SSRepositoryTestCase {

	private static final Log						LOG						= LogFactory
																					.getLog(SolonEpgRepositoryTestCase.class);

	protected UserManager							userManager;

	protected MailboxManagementService				correspMailboxService;

	protected CaseDistributionService				distributionService;

	protected CaseManagementDistributionTypeService	correspDistributionTypeService;

	protected CaseManagementDocumentTypeService		correspDocumentTypeService;

	protected AutomationService						automationService;

	protected DocumentRoutingService				documentRoutingService;

	protected FeuilleRouteModelService				feuilleRouteModelService;

	protected FeuilleRouteService					feuilleRouteService;

	protected DossierService						dossierService;

	protected DossierDistributionService			dossierDistributionService;

	protected static final String					administrator			= "Administrator";

	protected static final String					user					= "user";

	protected static final String					user1					= "user1";

	protected static final String					user2					= "user2";

	protected static final String					user3					= "user3";

	protected static final String					POSTE1					= "poste1";

	protected static final String					POSTE2					= "poste2";

	protected static final String					POSTE3					= "poste3";

	protected static final String					DEFAULT_TYPE_ACTE_ID	= "1";

	protected static final String					DEFAULT_MINISTERE_ID	= "1";

	protected static final String					DEFAULT_DIRECTION_ID	= "1";

	protected static final String					DEFAULT_NOR				= "ECOX9800017A";

	protected static final String					DEFAULT_ROUTE_NAME		= "FeuilleRouteTest";

	public static final String						CASE_TITLE				= "moncase";

	protected static final String					USER_DIRECTORY			= "userLdapDirectory";

	protected static final String					GROUP_DIRECTORY			= "groupLdapDirectory";

	@Override
	protected void deployRepositoryContrib() throws Exception {
		super.deployRepositoryContrib();

		// deploy api and core bundles
		deployBundle("org.nuxeo.ecm.core.schema");

		deployBundle("org.nuxeo.ecm.platform.userworkspace.api");
		deployBundle("org.nuxeo.ecm.platform.userworkspace.core");
		deployBundle("org.nuxeo.ecm.platform.userworkspace.types");

		deployBundle("org.nuxeo.ecm.platform.birt.reporting");

		deployBundle("fr.dila.solonepg.core");
		// Init. du mock LDAP
		// deployContrib("fr.dila.solonepg.core.tests", "ldap/TypeService.xml");
		// deployContrib("fr.dila.solonepg.core.tests", "ldap/DirectoryService.xml");
		deployContrib("fr.dila.solonepg.core.tests", "ldap/DirectoryTypes.xml");
		deployContrib("fr.dila.solonepg.core.tests", "ldap/LDAPDirectoryFactory.xml");
		deployContrib("fr.dila.solonepg.core.tests", "ldap/default-ldap-users-directory-bundle.xml");
		deployContrib("fr.dila.solonepg.core.tests", "ldap/default-ldap-groups-directory-bundle.xml");
		deployContrib("fr.dila.solonepg.core.tests", "ldap/test-event-contrib.xml");

		deployLDAPServer();
		getLDAPDirectory(USER_DIRECTORY).setTestServer(SERVER);
		getLDAPDirectory(UNITE_STRUCTURELLE_DIRECTORY).setTestServer(SERVER);
		getLDAPDirectory(POSTE_DIRECTORY).setTestServer(SERVER);

		LDAPSession session = (LDAPSession) getLDAPDirectory(POSTE_DIRECTORY).getSession();
		try {
			DirContext ctx = session.getContext();
			loadDataFromLdif("test-ldap.ldif", ctx);
		} finally {
			session.close();
		}

		// deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-sql-directories-contrib.xml");
		deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-st-computedgroups-contrib.xml");
		deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-feuille-route-ecm-type-contrib.xml");
		deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-mockministeres-framework.xml");
		deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-mockusanddirection-framework.xml");
		deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-mockpostes-framework.xml");
	}

	@Override
	public void setUp() throws Exception {
		LOG.debug("ENTER SETUP");
		super.setUp();

		userManager = Framework.getService(UserManager.class);
		assertNotNull(userManager);

		correspMailboxService = Framework.getService(MailboxManagementService.class);
		assertNotNull(correspMailboxService);

		distributionService = Framework.getService(CaseDistributionService.class);
		assertNotNull(distributionService);

		correspDistributionTypeService = Framework.getService(CaseManagementDistributionTypeService.class);
		assertNotNull(correspDistributionTypeService);

		correspDocumentTypeService = Framework.getService(CaseManagementDocumentTypeService.class);
		assertNotNull(correspDocumentTypeService);

		documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
		assertNotNull(documentRoutingService);

		feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		assertNotNull(feuilleRouteModelService);

		feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		assertNotNull(feuilleRouteService);

		dossierService = SolonEpgServiceLocator.getDossierService();
		assertNotNull(dossierService);

		dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
		assertNotNull(dossierDistributionService);

		automationService = Framework.getService(AutomationService.class);

		LOG.debug("EXIT SETUP");
	}

	public Mailbox getPersonalMailbox(String name) {
		return correspMailboxService.createPersonalMailboxes(session, name).get(0);
	}

	public Mailbox getPosteMailbox(String posteId) throws ClientException {
		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
		Mailbox mailbox = mailboxPosteService.getMailboxPoste(session, posteId);
		if (mailbox == null) {
			return mailboxPosteService.createPosteMailbox(session, posteId, posteId);
		}
		return mailbox;
	}

	public DocumentModel createDocumentModel(CoreSession session, String name, String type, String path)
			throws ClientException {
		DocumentModel route1 = session.createDocumentModel(path, name, type);
		route1.setPropertyValue(DocumentRoutingConstants.TITLE_PROPERTY_NAME, name);
		return session.createDocument(route1);
	}

	protected DocumentModel createDocument(String type, String id) throws Exception {
		DocumentModel document = session.createDocumentModel(type);
		document.setPathInfo("/", id);
		document = session.createDocument(document);
		return document;
	}

	/**
	 * Crée une étape série.
	 * 
	 * @param session
	 *            Session
	 * @param documentRouteDoc
	 *            Feuille de route
	 * @param posteId
	 *            Identifiant technique du poste
	 * @param routeStepTitle
	 *            Libellé de l'étape
	 * @param routingTaskType
	 *            Type d'étape
	 * @param docType
	 *            Type de document étape
	 * @throws ClientException
	 */
	public void createSerialStep(CoreSession session, DocumentModel documentRouteDoc, String posteId,
			String routeStepTitle, String routingTaskType, String docType) throws ClientException {
		Mailbox userMailbox = getPosteMailbox(posteId);
		FeuilleRouteTestHelper.createSerialStep(session, documentRouteDoc, userMailbox.getId(), routeStepTitle,
				routingTaskType);
	}

	/**
	 * Crée une dossier standard.
	 * 
	 * @return Dossier
	 * @throws ClientException
	 */
	protected Dossier createDossier() throws ClientException {
		// Crée le dossier
		DocumentModel dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		DublincoreSchemaUtils.setTitle(dossierDoc, DEFAULT_NOR);

		Mailbox userMailbox = getPersonalMailbox(user);
		Case emptyCase = distributionService.createEmptyCase(session, dossierDoc, userMailbox);
		Dossier dossier = emptyCase.getDocument().getAdapter(Dossier.class);

		// Ajout des informations de création
		dossier.setTypeActe(DEFAULT_TYPE_ACTE_ID);
		dossier.setMinistereResp(DEFAULT_MINISTERE_ID);
		dossier.setDirectionResp(DEFAULT_DIRECTION_ID);
		dossier.setNumeroNor(DEFAULT_NOR);

		// Crée le dossier
		dossierService.createDossier(session, dossier, POSTE1, null);
		return dossier;
	}

	/**
	 * Valide une étape de feuille de route.
	 * 
	 * @param posteId
	 *            Identifiant technique du poste
	 * @throws ClientException
	 */
	protected void validateUserTask(String posteId) throws ClientException {
		Mailbox posteMailbox = getPosteMailbox(posteId);
		List<CaseLink> links = distributionService.getReceivedCaseLinks(session, posteMailbox, 0, 0);

		List<CaseLink> linkOk = new ArrayList<CaseLink>();

		for (CaseLink caseLink : links) {
			DossierLink dl = caseLink.getDocument().getAdapter(DossierLink.class);
			if (!dl.getDeleted()) {
				linkOk.add(caseLink);
			}
		}
		assertEquals(1, linkOk.size());

		ActionableCaseLink actionableLink = null;
		for (CaseLink link : linkOk) {
			if (link.isActionnable()) {
				actionableLink = (ActionableCaseLink) link;
				actionableLink.validate(session);
			}
		}
		assertNotNull(actionableLink);
	}

	/**
	 * Met à jour l'état d'une étape après sa validation.
	 * 
	 * @param routeStepDoc
	 *            Etape
	 * @param validationStatus
	 *            État de validation
	 * @throws ClientException
	 */
	protected void updateRouteStep(DocumentModel routeStepDoc, String validationStatus) throws ClientException {
		STRouteStep routeStep = routeStepDoc.getAdapter(STRouteStep.class);
		routeStep.setValidationStatus(validationStatus);
		session.saveDocument(routeStepDoc);
	}
}
