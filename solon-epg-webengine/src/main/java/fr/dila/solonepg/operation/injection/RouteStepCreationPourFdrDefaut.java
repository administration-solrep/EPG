package fr.dila.solonepg.operation.injection;

import fr.dila.cm.cases.CaseDistribConstants;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

@Operation(
    id = RouteStepCreationPourFdrDefaut.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Create Route Step Init pour Fdr default",
    description = "Create Route Step Init pour Fdr default."
)
public class RouteStepCreationPourFdrDefaut {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.RouteStepCreationPourFdrDefaut";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "numeroNor", required = true, order = 1)
    protected String numeroNor;

    @Param(name = "parentPath", required = true, order = 2)
    protected String parentPath;

    @Param(name = "fdrId", required = true, order = 3)
    protected String fdrId;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        final String name = "ETAPE_INIT";
        repriseLog.debug("Ajout de l'etape " + name);
        String poste = null;
        DocumentModel step = null;
        try {
            final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
            final String minterePm = tableReferenceService.getMinisterePrm(session);
            if (minterePm == null) {
                repriseLog.error("Ajout de l'etape " + name + " -> KO");
            } else {
                poste = "poste-reprise-" + minterePm;
                step = session.createDocumentModel(parentPath, name, SSConstant.ROUTE_STEP_DOCUMENT_TYPE);
                step = session.createDocument(step);
                final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
                // final OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();

                final Mailbox userMailbox = mailboxPosteService.getOrCreateMailboxPoste(session, poste);
                step.setPropertyValue(
                    CaseDistribConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME,
                    userMailbox.getId()
                );
                step.setPropertyValue("rtsk:documentRouteId", fdrId);
                step.setPropertyValue("routing_task:type", "1");
                DublincoreSchemaUtils.setTitle(step, "ETAPE_INIT");
                step.followTransition("toReadyByCopy");

                final NORService nORService = SolonEpgServiceLocator.getNORService();
                final Dossier dossier = nORService.findDossierFromNOR(session, numeroNor);
                dossier.setCreatorPoste(userMailbox.getId());
                dossier.save(session);

                final SSRouteStep stRouteInstance = step.getAdapter(SSRouteStep.class);

                new UnrestrictedSessionRunner(session) {

                    @Override
                    public void run() {
                        stRouteInstance.run(session);
                    }
                }
                .runUnrestricted();

                if (step.getParentRef() != null) {
                    final DocumentModel stepParent = session.getDocument(step.getParentRef());

                    if (stepParent.getCurrentLifeCycleState().equals("ready")) {
                        // Passage du dossier à l'état lancé
                        stepParent.followTransition("toRunning");
                    }
                }
                repriseLog.debug("Ajout de l'etape " + name + " -> OK");
            }
        } catch (final Exception e) {
            repriseLog.error("Ajout de l'etape " + name + " -> KO", e);
            throw new Exception("Ajout de l'etape " + name + " -> KO", e);
        }
        return step;
    }
}
