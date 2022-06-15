package fr.dila.solonepg.core.operation.distribution;

import fr.dila.cm.caselink.ActionableCaseLink;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.st.api.caselink.STDossierLink;
import fr.dila.st.api.constant.STOperationConstant;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteStep;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Opération permettant de supprimer les anciens DossierLink des Mailbox, lors de la validation d'une étape de feuille
 * de route.
 *
 * @author jgomez
 * @author jtremeaux
 * @author arolin
 */
@Operation(
    id = RemoveCaseLinkOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Remove Case Links from Mailboxes",
    description = RemoveCaseLinkOperation.DESCRIPTION
)
public class RemoveCaseLinkOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Distribution.RemoveCaseLink";

    public static final String DESCRIPTION =
        "Cette opération enlève les case link des mailbox et met à jour la date de fin d'étape";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @OperationMethod
    public void removeCaseLink() {
        final FeuilleRouteStep step = (FeuilleRouteStep) context.get(FeuilleRouteConstant.OPERATION_STEP_DOCUMENT_KEY);
        final SolonEpgRouteStep routeStep = step.getDocument().getAdapter(SolonEpgRouteStep.class);

        final List<STDossierLink> caseLinkList = fetchCaseLinks();

        @SuppressWarnings("unchecked")
        final List<DocumentModel> dossierDocList = (List<DocumentModel>) context.get(
            STOperationConstant.OPERATION_CASES_KEY
        );

        // Met à jour les données de l'étape après validation
        final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        feuilleRouteService.updateRouteStepFieldAfterValidation(session, routeStep, dossierDocList, caseLinkList);

        // mise à jour de la date de fin
        updateDateFinEtape();

        boolean calculEcheance = true;
        for (final STDossierLink link : caseLinkList) {
            final DossierLink dossierLink = link.getDocument().getAdapter(DossierLink.class);
            if (calculEcheance) {
                // on recalcule les échéances opérationnelles du dossier
                final Dossier dossier = dossierLink.getDossier(session, Dossier.class);
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
        final FeuilleRouteStep step = (FeuilleRouteStep) context.get(FeuilleRouteConstant.OPERATION_STEP_DOCUMENT_KEY);
        final SSRouteStep stStep = step.getDocument().getAdapter(SSRouteStep.class);
        stStep.setDateFinEtape(GregorianCalendar.getInstance());
        stStep.save(session);
    }

    protected List<STDossierLink> fetchCaseLinks() {
        final List<STDossierLink> links = new ArrayList<>();
        final STDossierLink link = (STDossierLink) context.get(CaseConstants.OPERATION_CASE_LINK_KEY);
        if (link != null) {
            links.add(link);
        }
        @SuppressWarnings("unchecked")
        final List<STDossierLink> attachedLinks = (List<STDossierLink>) context.get(
            CaseConstants.OPERATION_CASE_LINKS_KEY
        );
        if (attachedLinks != null && !attachedLinks.isEmpty()) {
            links.addAll(attachedLinks);
        }
        if (links.isEmpty()) {
            links.addAll(fetchCaseLinksFromStep());
        }
        return links;
    }

    protected List<ActionableCaseLink> fetchCaseLinksFromStep() {
        final FeuilleRouteStep step = (FeuilleRouteStep) context.get(FeuilleRouteConstant.OPERATION_STEP_DOCUMENT_KEY);

        final String query =
            " SELECT d.ecm:uuid as id FROM DossierLink as d where d.acslk:deleted = 0 and d.acslk:stepDocumentId = ? ";
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "DossierLink",
            query,
            new Object[] { step.getDocument().getId() }
        );

        final List<ActionableCaseLink> result = new ArrayList<>();

        for (final DocumentModel doc : docs) {
            result.add(doc.getAdapter(ActionableCaseLink.class));
        }
        return result;
    }
}
