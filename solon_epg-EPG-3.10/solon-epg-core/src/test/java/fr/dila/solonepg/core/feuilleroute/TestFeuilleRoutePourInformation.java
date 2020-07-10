package fr.dila.solonepg.core.feuilleroute;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.cm.cases.Case;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.service.CaseDistributionService;
import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.exception.DocumentRouteAlredayLockedException;
import fr.dila.ecm.platform.routing.api.exception.DocumentRouteNotLockedException;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;

public class TestFeuilleRoutePourInformation extends SolonEpgRepositoryTestCase {

	private static final String	TYPE_ACTE_ID		= "1";

	private static final String	MINISTERE_ID		= "1";

	private static final String	DIRECTION_ID		= "1";

	private static final String	POSTE1				= "poste1";

	private static final String	POSTE2				= "poste2";

	private static final String	POSTE3				= "poste3";

	private static final String	NOR					= "ECOX9800017A";

	private static final String	DEFAULT_ROUTE_NAME	= "DefaultRouteModel";

	// Une feuille de route qui ressemble un plus à celle utilisée par réponse
	private DocumentRoute createFeuilleRoute(CoreSession session) throws Exception {
		final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);
		// Crée la feuille de route
		DocumentModel route = createDocumentModel(session, DEFAULT_ROUTE_NAME, STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
				feuilleRouteModelFolder.getPathAsString());
		SolonEpgFeuilleRoute feuilleRoute = route.getAdapter(SolonEpgFeuilleRoute.class);
		assertNotNull(feuilleRoute);

		feuilleRoute.setTypeActe(TYPE_ACTE_ID);
		feuilleRoute.setMinistere(MINISTERE_ID);
		feuilleRoute.setDirection(DIRECTION_ID);
		feuilleRoute.setFeuilleRouteDefaut(true);
		session.saveDocument(feuilleRoute.getDocument());
		session.save();

		createSerialStep(session, route, POSTE1, "Pour attribution agents BDC",
				VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION, STConstant.ROUTE_STEP_DOCUMENT_TYPE);
		createSerialStep(session, route, POSTE2, "Pour Information DLF",
				VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION, STConstant.ROUTE_STEP_DOCUMENT_TYPE);
		createSerialStep(session, route, POSTE3, "Pour attribution", VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
				STConstant.ROUTE_STEP_DOCUMENT_TYPE);
		session.save();
		return route.getAdapter(DocumentRoute.class);
	}

	private Dossier createDossier(CoreSession session) throws Exception {
		// Crée le dossier
		DocumentModel dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		DublincoreSchemaUtils.setTitle(dossierDoc, NOR);
		final CaseDistributionService caseDistribService = STServiceLocator.getCaseDistributionService();
		Mailbox userMailbox = getPersonalMailbox(user1);
		Case emptyCase = caseDistribService.createEmptyCase(session, dossierDoc, userMailbox);
		Dossier dossier = emptyCase.getDocument().getAdapter(Dossier.class);

		// Ajout des informations de création
		dossier.setTypeActe(TYPE_ACTE_ID);
		dossier.setMinistereResp(MINISTERE_ID);
		dossier.setDirectionResp(DIRECTION_ID);
		dossier.setNumeroNor(NOR);

		// Crée le dossier
		dossierService.createDossier(session, dossier, POSTE1, null);
		return dossier;
	}

	private void createAndValidateFdr() throws Exception, DocumentRouteAlredayLockedException, ClientException,
			DocumentRouteNotLockedException {
		// Crée la feuille de route par défaut
		DocumentRoute documentRoute = createFeuilleRoute(session);

		// Valide la feuille de route
		documentRoutingService.lockDocumentRoute(documentRoute, session);
		documentRoute = documentRoutingService.validateRouteModel(documentRoute, session);
		session.saveDocument(documentRoute.getDocument());
		session.save();
		documentRoutingService.unlockDocumentRoute(documentRoute, session);
		Framework.getLocalService(EventService.class).waitForAsyncCompletion();

		assertEquals("validated", documentRoute.getDocument().getCurrentLifeCycleState());
		assertEquals("validated", session.getChildren(documentRoute.getDocument().getRef()).get(0)
				.getCurrentLifeCycleState());
	}

	public void testValidationPourInformationStep() throws Exception {
		openSession();

		createAndValidateFdr();

		Dossier dossier = createDossier(session);
		session.saveDocument(dossier.getDocument());

		// Démarre la feuille de route associée au dossier
		dossierDistributionService.startDefaultRoute(session, dossier, POSTE1);

		// validation de l'étape d'initialisation
		validateUserTask(POSTE1);

		// validation de l'étape 1
		validateUserTask(POSTE1);

		// la fdr doit maintenant être à l'étape 3
		validateUserTask(POSTE3);

		closeSession();
	}

}
