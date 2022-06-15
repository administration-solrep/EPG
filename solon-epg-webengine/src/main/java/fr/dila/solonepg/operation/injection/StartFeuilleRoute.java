package fr.dila.solonepg.operation.injection;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.st.api.constant.STVocabularyConstants;
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
import org.nuxeo.ecm.core.api.IdRef;

@Operation(
    id = StartFeuilleRoute.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Start Feuille Route",
    description = "Start Feuille Route"
)
public class StartFeuilleRoute {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.StartFeuilleRoute";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "numeroNor", required = true, order = 1)
    protected String numeroNor;

    @Param(name = "fdrId", required = true, order = 2)
    protected String fdrId;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        repriseLog.debug("Lancement de la feuile de route " + numeroNor);
        DocumentModel routeInstanceDoc = null;
        try {
            NORService nORService = SolonEpgServiceLocator.getNORService();
            DocumentModel docModel = nORService.getDossierFromNOR(session, numeroNor);
            Dossier dossier = docModel.getAdapter(Dossier.class);
            routeInstanceDoc = session.getDocument(new IdRef(fdrId));

            SSFeuilleRoute stRouteInstance = routeInstanceDoc.getAdapter(SSFeuilleRoute.class);
            stRouteInstance.setValidated(session);

            stRouteInstance.setTypeCreation(STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_INSTANCIATION);
            stRouteInstance.setReady(session);
            stRouteInstance.setRunning(session);
            session.saveDocument(stRouteInstance.getDocument());

            //final DocumentRoute stRoute = stRouteInstance.getDocumentRoute(session);
            //            new UnrestrictedSessionRunner(session) {
            //                @Override
            //                public void run() {
            //                    stRoute.run(session);
            //                }
            //            }.runUnrestricted();

            // Renseigne la dernière feuille de route du dossier exécutée sur le dossier
            dossier.setLastDocumentRoute(routeInstanceDoc.getId());

            dossier.save(session);
            session.save();
            repriseLog.debug("Lancement de la feuile de route " + numeroNor + " -> OK");
        } catch (Exception e) {
            repriseLog.error("Lancement de la feuile de route " + numeroNor + " -> KO", e);
            throw new Exception("Erreur lors du lancement la feuille de route " + numeroNor, e);
        }
        return routeInstanceDoc;
    }
}
