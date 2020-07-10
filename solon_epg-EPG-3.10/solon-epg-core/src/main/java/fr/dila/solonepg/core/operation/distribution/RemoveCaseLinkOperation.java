package fr.dila.solonepg.core.operation.distribution;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.cm.caselink.ActionableCaseLink;
import fr.dila.cm.caselink.CaseLink;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.ecm.platform.routing.api.DocumentRouteStep;
import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STOperationConstant;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.core.query.QueryUtils;

/**
 * Opération permettant de supprimer les anciens DossierLink des Mailbox, lors de la validation d'une étape de feuille
 * de route.
 * 
 * @author jgomez
 * @author jtremeaux
 * @author arolin
 */
@Operation(id = RemoveCaseLinkOperation.ID, category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY, label = "Remove Case Links from Mailboxes", description = RemoveCaseLinkOperation.DESCRIPTION)
public class RemoveCaseLinkOperation {
	/**
	 * Identifiant technique de l'opération.
	 */
	public final static String	ID			= "SolonEpg.Distribution.RemoveCaseLink";

	public final static String	DESCRIPTION	= "Cette opération enlève les case link des mailbox et met à jour la date de fin d'étape";

	@Context
	protected OperationContext	context;

	@Context
	protected CoreSession		session;

	@OperationMethod
	public void removeCaseLink() throws ClientException {

		final DocumentRouteStep step = (DocumentRouteStep) context
				.get(DocumentRoutingConstants.OPERATION_STEP_DOCUMENT_KEY);
		final SolonEpgRouteStep routeStep = step.getDocument().getAdapter(SolonEpgRouteStep.class);

		final List<CaseLink> caseLinkList = fetchCaseLinks();

		@SuppressWarnings("unchecked")
		final List<DocumentModel> dossierDocList = (List<DocumentModel>) context
				.get(STOperationConstant.OPERATION_CASES_KEY);

		// Met à jour les données de l'étape après validation
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		feuilleRouteService.updateRouteStepFieldAfterValidation(session, routeStep, dossierDocList, caseLinkList);

		// mise à jour de la date de fin
		updateDateFinEtape();

		boolean calculEcheance = true;
		for (final CaseLink link : caseLinkList) {
			final DossierLink dossierLink = link.getDocument().getAdapter(DossierLink.class);
			if (calculEcheance) {
				// on recalcule les échéances opérationnelles du dossier
				final Dossier dossier = dossierLink.getCase(session).getDocument().getAdapter(Dossier.class);
				if (dossier != null) {
					feuilleRouteService.calculEcheanceFeuilleRoute(session, dossier);
					calculEcheance = false;
				}
			}

			dossierLink.setDeleted(Boolean.TRUE);

			session.saveDocument(dossierLink.getDocument());

		}
	}

	/**
	 * Met à jour la date de fin effective de l'étape de feuille de route.
	 */
	protected void updateDateFinEtape() {
		final DocumentRouteStep step = (DocumentRouteStep) context
				.get(DocumentRoutingConstants.OPERATION_STEP_DOCUMENT_KEY);
		final STRouteStep stStep = step.getDocument().getAdapter(STRouteStep.class);
		stStep.setDateFinEtape(GregorianCalendar.getInstance());
		stStep.save(session);
	}

	protected List<CaseLink> fetchCaseLinks() throws ClientException {
		final List<CaseLink> links = new ArrayList<CaseLink>();
		final CaseLink link = (CaseLink) context.get(CaseConstants.OPERATION_CASE_LINK_KEY);
		if (link != null) {
			links.add(link);
		}
		@SuppressWarnings("unchecked")
		final List<CaseLink> attachedLinks = (List<CaseLink>) context.get(CaseConstants.OPERATION_CASE_LINKS_KEY);
		if (attachedLinks != null && !attachedLinks.isEmpty()) {
			links.addAll(attachedLinks);
		}
		if (links.isEmpty()) {
			links.addAll(fetchCaseLinksFromStep());
		}
		return links;
	}

	protected List<CaseLink> fetchCaseLinksFromStep() throws ClientException {
		final DocumentRouteStep step = (DocumentRouteStep) context
				.get(DocumentRoutingConstants.OPERATION_STEP_DOCUMENT_KEY);

		final String query = " SELECT d.ecm:uuid as id FROM DossierLink as d where d.acslk:deleted = 0 and d.acslk:stepDocumentId = ? ";
		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, "DossierLink", query,
				new Object[] { step.getDocument().getId() });

		final List<CaseLink> result = new ArrayList<CaseLink>();

		for (final DocumentModel doc : docs) {
			result.add(doc.getAdapter(ActionableCaseLink.class));
		}
		return result;
	}
}
