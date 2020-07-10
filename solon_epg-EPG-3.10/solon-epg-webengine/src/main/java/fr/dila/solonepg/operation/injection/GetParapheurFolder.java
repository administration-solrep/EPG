package fr.dila.solonepg.operation.injection;

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

import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

@Operation(id = GetParapheurFolder.ID, category = Constants.CAT_DOCUMENT, label = "Get Parapheur Folder", description = "Get Parapheur Folder")
public class GetParapheurFolder {
    
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.GetParapheurFolder";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "folderName", required = true, order = 1)
    protected String folderName;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) throws Exception {
        repriseLog.debug("recuperation le repertoire parapheur " + doc.getTitle());
        DocumentModel documentModel = null;
        try {
            final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
            documentModel = parapheurService.getParapheurFolder(doc, session, folderName);
            repriseLog.debug("recuperation le repertoire parapheur " + doc.getTitle() + " -> OK");
        } catch (Exception e) {
            repriseLog.error("recuperation le repertoire parapheur " + doc.getTitle() + " -> KO");
            throw new Exception("Erreur lors de la recuperation le repertoire parapheur " + doc.getTitle(), e);
        }
        return documentModel;
    }
}
