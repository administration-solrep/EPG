package fr.dila.solonepg.operation.injection;

import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.service.STServiceLocator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.ecm.platform.audit.impl.LogEntryImpl;

@Operation(
    id = JournalInjection.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Inject Log",
    description = "Inject Log"
)
public class JournalInjection {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.InjectLog";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "user", required = true, order = 1)
    protected String user;

    @Param(name = "action", required = true, order = 2)
    protected String action;

    @Param(name = "commentaire", required = true, order = 3)
    protected String commentaire;

    @Param(name = "date", required = true, order = 4)
    protected String date;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) throws Exception {
        try {
            repriseLog.trace("Injection du journal");
            JournalService journalService = STServiceLocator.getJournalService();

            List<LogEntry> entries = new ArrayList<LogEntry>();
            LogEntry entry = new LogEntryImpl();
            entry.setDocType(doc.getType());
            entry.setDocType(doc.getType());
            entry.setDocUUID(doc.getId());
            entry.setCategory("Reprise");
            entry.setEventId(action);
            entry.setDocLifeCycle("project");
            entry.setComment(commentaire);

            Date logDate = null;
            long timeInMillis = System.currentTimeMillis();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
                logDate = formatter.parse(date);
                timeInMillis = logDate.getTime();
            } catch (Exception e) {
                repriseLog.error("La date " + date + "n'est pas au format yyyyMMdd HHmmss");
            }

            entry.setEventDate(new Date(timeInMillis));

            String name = user;
            if (user == null || user.isEmpty()) {
                repriseLog.warn("user not found " + user);
            } else {
                try {
                    STUserService sTUserService = STServiceLocator.getSTUserService();

                    // récupération des postes de l'utilisateur
                    String postesUser = null;
                    try {
                        postesUser = sTUserService.getUserProfils(user);
                    } catch (Exception e) {}
                    // Essai de correction d'une NPE qui spamme les logs.
                    if (postesUser == null) {
                        repriseLog.warn("La liste des postes de l'utilisateur est nulle pour l'utilisateur " + user);
                        postesUser = "**poste inconnu**";
                    }
                    // WARN : on stocke les profils de l'utilisateur dans la variable 'DocPath' que l'on utilisais pas auparavant et qui peut cotenir jusqu'à 1024 caractères .
                    if (postesUser.length() > 1024) {
                        postesUser = postesUser.substring(0, 1023);
                    }
                    entry.setDocPath(postesUser);
                    // récupération du nom complet de l'utilisateur
                    name = sTUserService.getLegacyUserFullName(name);
                } catch (Exception e) {
                    repriseLog.error("Erreur lors de la récupération des postes et du de l'utilisateur", e);
                }
                entry.setPrincipalName(name);
            }

            entries.add(entry);
            journalService.addLogEntries(entries);
            repriseLog.trace("Injection du journal -> OK");
        } catch (Exception e) {
            repriseLog.warn("Injection du journal -> KO", e);
            throw new Exception("Erreur lors de l'injection du journal", e);
        }
        return doc;
    }
}
