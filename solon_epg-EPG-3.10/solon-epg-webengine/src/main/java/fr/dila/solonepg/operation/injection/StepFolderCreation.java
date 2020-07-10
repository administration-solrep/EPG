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
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;

@Operation(id = StepFolderCreation.ID, category = Constants.CAT_DOCUMENT, label = "Create Step Folder", description = "Create Step Folder .")
public class StepFolderCreation {

    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.CreateStepFolder";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "name", required = true, order = 1)
    protected String name;

    @Param(name = "parentPath", required = true, order = 2)
    protected String parentPath;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        repriseLog.debug("Ajout de l'etape parallelle " + name);
        DocumentModel stepFolder = null;
        try {
            stepFolder = session.createDocumentModel(parentPath, name, DocumentRoutingConstants.STEP_FOLDER_DOCUMENT_TYPE);
            stepFolder = session.createDocument(stepFolder);
            stepFolder.followTransition("toValidated");
            stepFolder.followTransition("toReady");
            
            DocumentHelper.setProperties(session, stepFolder, properties);
            session.saveDocument(stepFolder);
            session.save();
            repriseLog.debug("l'ajout de l'etape parallelle " + name + " -> OK");
        } catch (Exception e) {
            repriseLog.error("l'ajout de l'etape parallelle " + name + " -> KO", e);
            throw new Exception("Erreur lors de l'ajout de l'etape parallelle " + name, e);
        }
        return stepFolder;
    }

}
