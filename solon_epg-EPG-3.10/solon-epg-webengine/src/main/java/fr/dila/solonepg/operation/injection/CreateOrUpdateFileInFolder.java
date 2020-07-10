package fr.dila.solonepg.operation.injection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.service.TreeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

@Operation(id = CreateOrUpdateFileInFolder.ID,
        category = Constants.CAT_DOCUMENT,
        label = "CreateOrUpdateFileInFolder",
        description = "CreateOrUpdateFileInFolder.")
public class CreateOrUpdateFileInFolder {

    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.CreateOrUpdateFileInFolder";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "documentId", required = true, order = 1)
    protected String documentId;

    @Param(name = "fileName", required = true, order = 2)
    protected String fileName;

    @Param(name = "documentType", required = true, order = 3)
    protected String documentType;

    @Param(name = "user", required = true, order = 4)
    protected String user;

    @Param(name = "dossierId", required = true, order = 5)
    protected String dossierId;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run(final Blob blob) throws Exception {
        repriseLog.debug("Upload du fichier : fileName = " + fileName + " ; documentType =" + documentType);
        DocumentModel result = null;
        try {
            final DocumentModel dossierDocument = session.getDocument(new IdRef(dossierId));
            // final UserManager userManager = STServiceLocator.getUserManager();
            // NuxeoPrincipal principal = userManager.getPrincipal(user);

            final TreeService treeService = SolonEpgServiceLocator.getTreeService();
            result = treeService.createOrUpdateFileInFolder(documentId, fileName, blob, session, documentType, (NuxeoPrincipal) ctx.getPrincipal(),
                    dossierDocument);
            repriseLog.debug("Upload du fichier : " + "fileName = " + fileName + "-> OK");
        } catch (final Exception e) {
            repriseLog.error("Upload du fichier : " + "fileName = " + fileName + "-> KO", e);
            throw new Exception("Erreur lors de l'upload de fichier " + fileName, e);
        }
        return result;
    }
}
