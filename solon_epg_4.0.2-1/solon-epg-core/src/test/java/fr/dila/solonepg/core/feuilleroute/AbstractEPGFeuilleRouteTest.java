package fr.dila.solonepg.core.feuilleroute;

import fr.dila.cm.caselink.ActionableCaseLink;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.core.helper.FeuilleRouteTestHelper;
import fr.dila.st.api.caselink.STDossierLink;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.junit.Assert;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class AbstractEPGFeuilleRouteTest extends AbstractEPGTest {
    @Inject
    protected EpgDocumentRoutingService documentRoutingService;

    @Inject
    protected FeuilleRouteModelService feuilleRouteModelService;

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
     */
    protected void createSerialStep(
        CoreSession session,
        DocumentModel documentRouteDoc,
        String posteId,
        String routeStepTitle,
        String routingTaskType,
        String docType
    ) {
        Mailbox userMailbox = getPosteMailbox(posteId);
        FeuilleRouteTestHelper.createSerialStep(
            session,
            documentRouteDoc,
            userMailbox.getId(),
            routeStepTitle,
            routingTaskType
        );
    }

    /**
     * Valide une étape de feuille de route.
     *
     * @param posteId
     *            Identifiant technique du poste
     */
    protected void validateUserTask(String posteId) {
        Mailbox posteMailbox = getPosteMailbox(posteId);
        List<STDossierLink> links = distributionService.getReceivedCaseLinks(session, posteMailbox, 0, 0);

        List<STDossierLink> linkOk = new ArrayList<>();

        for (STDossierLink caseLink : links) {
            DossierLink dl = caseLink.getDocument().getAdapter(DossierLink.class);
            if (!dl.getDeleted()) {
                linkOk.add(caseLink);
            }
        }
        Assert.assertEquals(1, linkOk.size());

        ActionableCaseLink actionableLink = null;
        for (STDossierLink link : linkOk) {
            if (link.isActionnable()) {
                actionableLink = link.getDocument().getAdapter(ActionableCaseLink.class);
                actionableLink.validate(session);
            }
        }
        Assert.assertNotNull(actionableLink);

        session.save();
        eventService.waitForAsyncCompletion();
    }

    /**
     * Met à jour l'état d'une étape après sa validation.
     *
     * @param routeStepDoc
     *            Etape
     * @param validationStatus
     *            État de validation
     */
    protected void updateRouteStep(DocumentModel routeStepDoc, String validationStatus) {
        SSRouteStep routeStep = routeStepDoc.getAdapter(SSRouteStep.class);
        routeStep.setValidationStatus(validationStatus);
        session.saveDocument(routeStepDoc);
    }
}
