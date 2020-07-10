package fr.dila.solonepg.core.logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.event.Event;

/**
 * Classe utilisé pour logger les différents types d'événements dans le fichier de log.
 * 
 * @author ARN
 */
class NotificationMessageLogger {

    private static final String FR_DILA_SOLONEPG_SERVER_LOGGER = "fr.dila.solonepg.server.logger";

    private static final String MESSAGE_FORMATTER_FILE = "messageFormatter.properties";

    private static final String LOG_DOSSIER_KEY = "log.dossier";

    private static final String LOG_FOND_DOSSIER_KEY = "log.fondDossier";

    private static final String LOG_PARAPHEUR_KEY = "log.parapheur";

    private static final String LOG_BORDEREAU_KEY = "log.bordereau";

    private static final String LOG_TRAITEMENT_PAPIER_KEY = "log.traitementPapier";

    private static final String LOG_JOURNAL_KEY = "log.journal";

    private static final String LOG_PROCEDURE_PARLEMETAIRE_KEY = "log.procedureParlementaire";

    private static final String LOG_ADMINISTRATION_KEY = "log.administration";

    private static final String LOG_FDR_KEY = "log.feuilleRoute";

    private final Properties properties;

    private static NotificationMessageLogger instance;

    /**
     * Constructeur privé.
     */
    private NotificationMessageLogger() {
        String filename = FileUtils.getResourcePathFromContext(MESSAGE_FORMATTER_FILE);
        properties = new Properties();
        if (filename == null || filename.isEmpty()) {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(MESSAGE_FORMATTER_FILE);
            try {
                properties.load(stream);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Fichier formatter de log non trouvé");
            } catch (IOException e) {
                throw new IllegalArgumentException("Probleme d'acces au fichier formatter de log");
            }
        } else {
            InputStream inStream = null ;
            try {
                inStream = new FileInputStream(filename) ;
                properties.load(inStream);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Fichier formatter de log non trouvé");
            } catch (IOException e) {
                throw new IllegalArgumentException("Probleme d'acces au fichier formatter de log");
            }finally{
                if(inStream != null){
                    try {
                        inStream.close() ;
                    } catch (IOException e) {
                        Log logNotification = LogFactory.getLog(FR_DILA_SOLONEPG_SERVER_LOGGER);
                        logNotification.debug("Probleme d'acces au fichier formatter de log");
                    }
                }
            }
        }
    }

    public static NotificationMessageLogger getInstance() {
        if (instance == null) {
            instance = new NotificationMessageLogger();
        }
        return instance;
    }

    /**
     * Check data
     * 
     * @param log
     * @param datas
     * @return
     */
    private boolean check(Log log, Object[] datas) {
        if (properties == null) {
            throw new IllegalArgumentException("Fichier formatter de log non trouve");
        }
        if (log == null) {
            throw new IllegalArgumentException("Logger ne peut pas être nul");
        }
        return true;
    }

    /**
     * Log les evenements sur un type d'action précise à définir
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     * @param typeEvent
     *            type d'événement à logger
     */
    protected void logEventType(Event event, Log log, Object[] datas, String typeEvent) {
        if(log.isDebugEnabled()){
            check(log, datas);
            MessageFormat formatter = new MessageFormat(properties.getProperty(typeEvent));
            String formatMessage = formatter.format(datas);
            log.debug(formatMessage);
            //
            Log logNotification = LogFactory.getLog(FR_DILA_SOLONEPG_SERVER_LOGGER);
            logNotification.debug(formatMessage);
        }
    }

    /**
     * Log les evenements sur le dossier
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logDossier(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_DOSSIER_KEY);
    }

    /**
     * Log les evenements sur la feuille de route
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logFeuilleDeRoute(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_FDR_KEY);
    }

    /**
     * Log les evenements sur le fond de dossier
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logFondDossier(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_FOND_DOSSIER_KEY);
    }

    /**
     * Log les evenements sur le parapheur
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logParapheur(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_PARAPHEUR_KEY);
    }

    /**
     * Log les evenements sur le Bordereau
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logBordereau(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_BORDEREAU_KEY);
    }

    /**
     * Log les evenements sur le Journal
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logJournal(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_JOURNAL_KEY);
    }

    /**
     * Log les evenements sur le TraitementPapier
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logTraitementPapier(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_TRAITEMENT_PAPIER_KEY);
    }

    /**
     * Log les evenements sur le ProcedureParlementaire
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logProcedureParlementaire(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_PROCEDURE_PARLEMETAIRE_KEY);
    }

    /**
     * Log les evenements sur le Administration
     * 
     * @param event
     *            Evenement
     * @param log
     *            logger
     * @param datas
     *            donnees
     */
    public void logAdministration(Event event, Log log, Object[] datas) {
        logEventType(event, log, datas, LOG_ADMINISTRATION_KEY);
    }
}
