package fr.dila.solonepg.operation.injection;

import fr.dila.solonepg.api.service.TreeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(
    id = CreateFondDossierFolder.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Create new Fond Dossier Folder",
    description = "Create new Fond Dossier Folder"
)
public class CreateFondDossierFolder {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.CreateFondDossierFolder";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "name", required = true, order = 1)
    protected String name;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) throws Exception {
        repriseLog.debug("Create Fond Dossier Folder : name = " + name);
        DocumentModel result = null;
        try {
            final TreeService treeService = SolonEpgServiceLocator.getTreeService();
            result = treeService.createFondDossierModelFolderElement(doc, session, name);
            repriseLog.debug("Create Fond Dossier Folder : name = " + name + "-> OK");
        } catch (Exception e) {
            repriseLog.debug("Create Fond Dossier Folder : name = " + name + "-> KO", e);
            throw new Exception("Create Fond Dossier Folder : name = " + name, e);
        }
        return result;
    }
}
