package fr.dila.solonepg.operation.injection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.DocumentHelper;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;

@Operation(id = CreateFDRNewInstance.ID, category = Constants.CAT_DOCUMENT, label = "Create new FDR Instance", description = "Create w FDR Instance .")
public class CreateFDRNewInstance {

    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.CreateNewFDRInstance";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "numeroNor", required = true, order = 1)
    protected String numeroNor;

    @Param(name = "fdrId", required = true, order = 2)
    protected String fdrId;

    @Param(name = "name", required = true, order = 3)
    protected String name;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        repriseLog.debug("Creation de la feuille de route " + numeroNor);
        DocumentModel result = null;
        try {

            NORService nORService = SolonEpgServiceLocator.getNORService();
            DocumentModel docModel = nORService.getDossierFromNOR(session, numeroNor);
            final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();
            DocumentModel routeInstance = documentRoutingService.createNewInstance(session, name, docModel.getId());

            // validateRoute(solonEpgFeuilleRoute);

            DocumentHelper.setProperties(session, routeInstance, properties);
            session.saveDocument(routeInstance);

            // SolonEpgFeuilleRoute solonEpgFeuilleRoute = routeInstance.getAdapter(SolonEpgFeuilleRoute.class);
            // final ActeService acteService = SolonEpgServiceLocator.getActeService();
            // solonEpgFeuilleRoute.setTypeActe(acteService.getActeByLabel(typeActe).getId());

            STFeuilleRoute stRouteInstance = routeInstance.getAdapter(STFeuilleRoute.class);
            stRouteInstance.setTypeCreation(STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_INSTANCIATION);
            session.save();
            repriseLog.debug("Creation de la feuille de route " + numeroNor + "-> OK");
            result = stRouteInstance.getDocument();
        } catch (Exception e) {
            repriseLog.error("Creation de la feuille de route " + numeroNor + "-> KO", e);
            throw new Exception("Erreur lors de la creation de la feuille de route " + numeroNor, e);
        }
        return result;
    }

    public DocumentModel createDocumentModel(CoreSession session, String name, String type, String path) throws ClientException {
        DocumentModel route1 = session.createDocumentModel(path, name, type);
        route1.setPropertyValue(DocumentRoutingConstants.TITLE_PROPERTY_NAME, name);
        return session.createDocument(route1);
    }

}
