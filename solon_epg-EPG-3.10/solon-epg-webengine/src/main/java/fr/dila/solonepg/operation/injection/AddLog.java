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

@Operation(id = AddLog.ID, category = Constants.CAT_DOCUMENT, label = "Add Log", description = "Add new log .")
public class AddLog {

    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddLog";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "message", required = true, order = 1)
    protected String message;

    @Param(name = "level", required = false, order = 2)
    protected String level;

    @OperationMethod
    public void run() throws Exception {
        if ("WARN".equalsIgnoreCase(level)) {
            repriseLog.warn(message);
        } else if ("INFO".equalsIgnoreCase(level)) {
            repriseLog.info(message);
        } else if ("TRACE".equalsIgnoreCase(level)) {
            repriseLog.trace(message);
        }
        if ("ERROR".equalsIgnoreCase(level)) {
            repriseLog.error(message);
        } else {
            repriseLog.debug(message);
        }
    }

}
