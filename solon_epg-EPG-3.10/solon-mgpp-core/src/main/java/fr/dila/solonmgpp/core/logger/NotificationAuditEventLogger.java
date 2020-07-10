package fr.dila.solonmgpp.core.logger;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.logger.STNotificationAuditEventLogger;
import fr.dila.st.core.util.SessionUtil;

/**
 * Listener asynchrone post-commit qui enregistre l'Audit log pour l'application Solon MGPP.
 * 
 * @author asatre
 */
public class NotificationAuditEventLogger extends STNotificationAuditEventLogger {

  /**
   * Logger surcouche socle de log4j
   */
  private static final STLogger LOGGER = STLogFactory.getLog(NotificationAuditEventLogger.class);

  @Override
  protected void loggerProcess(Event event) throws ClientException {

    CoreSession session = null;
    try {
      session = SessionUtil.getCoreSession();

      if (event.getContext() instanceof DocumentEventContext) {
        DocumentEventContext docCtx = (DocumentEventContext) event.getContext();
        DocumentModel model = docCtx.getSourceDocument();

        if (model != null) {
          LOGGER.debug(session, STLogEnumImpl.PROCESS_NOTIFICATION_AUDIT_EVENT_LOGGER_TEC, "calling : " + model);
        }
      } 
    } finally {
      SessionUtil.close(session);
    }
  }

}
