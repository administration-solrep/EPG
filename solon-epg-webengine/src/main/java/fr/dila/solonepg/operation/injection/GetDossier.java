package fr.dila.solonepg.operation.injection;

import fr.dila.solonepg.api.service.NORService;
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

@Operation(id = GetDossier.ID, category = Constants.CAT_DOCUMENT, label = "Get Dossier", description = "Get Dossier .")
public class GetDossier {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.GetDossier";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "numeroNor", required = true, order = 1)
    protected String numeroNor;

    @OperationMethod
    public DocumentModel run() throws Exception {
        DocumentModel docModel = null;
        repriseLog.debug("recuperation du dossier " + numeroNor);
        try {
            NORService nORService = SolonEpgServiceLocator.getNORService();
            docModel = nORService.getDossierFromNOR(session, numeroNor);
            if (docModel != null) {
                repriseLog.warn("le dossier existe deja" + numeroNor);
            } else {
                repriseLog.debug("c'est un nouveau dossier" + numeroNor);
            }
        } catch (Exception e) {
            repriseLog.error("recuperation du dossier " + numeroNor + " -> KO");
            throw new Exception("Erreur lors de la recuperation du dossier " + numeroNor, e);
        }
        return docModel;
    }
}
