package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Lanceur général des batchs
 *
 */
public class LanceurGeneralDiversBatchListener extends AbstractBatchEventListener {
    private static final String BATCH_MENSUEL_PREFIX = "batch-mensuel-";
    private static final String BATCH_HEBDO_PREFIX = "batch-hebdo-";

    /**
     * Les batchs quotidiens
     *
     */
    private enum DailyBatch {
        BATCH_EVENT_UPDATE_MGPP_INFO_CORBEILLE(SolonEpgEventConstant.BATCH_EVENT_UPDATE_INFO_CORBEILLE),
        BATCH_EVENT_CLEAN_DELTED_DOSSIER_LINK(SSEventConstant.CLEAN_DELETED_DL_EVENT),
        BATCH_EVENT_USER_DELETETION(SSEventConstant.USER_DELETION_BATCH_EVENT),
        BATCH_EVENT_TEXTE_SIGNALE(SolonEpgEventConstant.BATCH_EVENT_TEXTE_SIGNALE),
        BATCH_EVENT_SUPPRESSION_NOTIFICATION(SolonEpgEventConstant.BATCH_EVENT_SUPPRESSION_NOTIFICATION),
        BATCH_EVENT_DOSSIER_ABANDON(SolonEpgEventConstant.BATCH_EVENT_DOSSIER_ABANDON),
        BATCH_EVENT_PURGE_CALENDRIER_BATCH(SolonEpgEventConstant.BATCH_EVENT_PURGE_CALENDRIER_BATCH),
        BATCH_EVENT_CLEAN_DELETED_ALERT(SSEventConstant.CLEAN_DELETED_ALERT_EVENT),
        BATCH_EVENT_CLEAN_DELETED_REQUETE(SSEventConstant.CLEAN_DELETED_REQUETE_EVENT),
        BATCH_EVENT_DELETE_BIRT_REFRESH(SolonEpgEventConstant.BATCH_EVENT_DELETE_BIRT_REFRESH),
        BATCH_EVENT_DELETE_FDD_FILE(SSEventConstant.CLEAN_DELETED_FDD_FILE_EVENT),
        BATCH_EVENT_DELETE_PARAPHEUR_FILE(SSEventConstant.CLEAN_DELETED_PARAPHEUR_FILE_EVENT),
        BATCH_EVENT_CLEAN_DELETED_FAVORIS_RECHERCHE(SolonEpgEventConstant.BATCH_EVENT_CLEAN_FAVORIS_RECHERCHE),
        BATCH_EVENT_CLOSE_TEXTES_NON_PUBLIES(SolonEpgEventConstant.BATCH_EVENT_CLOSE_TXT_NON_PUB),
        BATCH_EVENT_INIT_CASE_ROOT(SolonEpgEventConstant.BATCH_EVENT_INIT_CASE_ROOT);

        private String eventName;

        DailyBatch(String eventName) {
            this.eventName = eventName;
        }
    }

    /**
     * Les batchs hebdomadaires
     */
    private enum WeeklyBatch {
        BATCH_EVENT_PURGE_TENTATIVES_CONNEXION(STEventConstant.BATCH_EVENT_PURGE_TENTATIVES_CONNEXION);

        private String eventName;

        WeeklyBatch(String eventName) {
            this.eventName = eventName;
        }
    }

    /**
     * Les batchs mensuels
     *
     */
    private enum MonthlyBatch {
        BATCH_EVENT_DOSSIER_CANDIDAT_ABANDON(SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ABANDON),
        BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE(
            SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE
        ),
        BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIF(
            SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIF
        ),
        BATCH_EVENT_DOSSIER_ELIMINATION(SolonEpgEventConstant.BATCH_EVENT_DOSSIER_ELIMINATION);

        private String eventName;

        MonthlyBatch(String eventName) {
            this.eventName = eventName;
        }
    }

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(LanceurGeneralDiversBatchListener.class);

    public LanceurGeneralDiversBatchListener() {
        super(LOGGER, SolonEpgEventConstant.LANCEUR_GENERAL_DIVERS);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) throws NuxeoException {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_LANCEUR_GENERAL_DIVERS_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        try {
            EventProducer eventProducer = STServiceLocator.getEventProducer();
            Map<String, Serializable> eventProperties = new HashMap<>();
            eventProperties.put(STEventConstant.BATCH_EVENT_PROPERTY_PARENT_ID, batchLoggerId);
            Calendar calendar = GregorianCalendar.getInstance();
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK);

            STParametreService parametreService = STServiceLocator.getSTParametreService();

            for (WeeklyBatch weeklyBatch : WeeklyBatch.values()) {
                String paramValue = parametreService.getParametreValue(
                    session,
                    BATCH_HEBDO_PREFIX + weeklyBatch.eventName
                );
                if (paramValue == null) {
                    LOGGER.error(
                        session,
                        EpgLogEnumImpl.FAIL_PROCESS_B_LANCEUR_GENERAL_DIVERS_TEC,
                        weeklyBatch.eventName +
                        " est déclaré comme étant un batch hebdomadaire, mais n'a pas de paramêtre pour fixer son jour d'éxécution"
                    );
                } else {
                    JoursSemaine jourSemaine = JoursSemaine.valueOf(paramValue.trim().toUpperCase());
                    if (jourSemaine != null) {
                        Integer batchDay = jourSemaine.getCalValue();
                        if (batchDay.equals(weekDay)) {
                            InlineEventContext inlineEventContext = new InlineEventContext(
                                session,
                                session.getPrincipal(),
                                eventProperties
                            );
                            eventProducer.fireEvent(inlineEventContext.newEvent(weeklyBatch.eventName));
                        }
                    }
                }
            }

            for (MonthlyBatch monthlyBatch : MonthlyBatch.values()) {
                String batchDayStr = parametreService.getParametreValue(
                    session,
                    BATCH_MENSUEL_PREFIX + monthlyBatch.eventName
                );
                if (batchDayStr == null) {
                    LOGGER.error(
                        session,
                        EpgLogEnumImpl.FAIL_PROCESS_B_LANCEUR_GENERAL_DIVERS_TEC,
                        monthlyBatch.eventName +
                        " est déclaré comme étant un batch mensuel, mais n'a pas de paramêtre pour fixer son jour d'éxécution"
                    );
                } else {
                    Integer batchDay = Integer.parseInt(batchDayStr);
                    if (batchDay != null && batchDay.equals(day)) {
                        InlineEventContext inlineEventContext = new InlineEventContext(
                            session,
                            session.getPrincipal(),
                            eventProperties
                        );
                        eventProducer.fireEvent(inlineEventContext.newEvent(monthlyBatch.eventName));
                    }
                }
            }

            for (DailyBatch dailyBatch : DailyBatch.values()) {
                InlineEventContext inlineEventContext = new InlineEventContext(
                    session,
                    session.getPrincipal(),
                    eventProperties
                );
                eventProducer.fireEvent(inlineEventContext.newEvent(dailyBatch.eventName));
            }
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_LANCEUR_GENERAL_DIVERS_TEC, exc);
            ++errorCount;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(batchLoggerModel, "Exécution du lanceur général", endTime - startTime);
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_LANCEUR_GENERAL_DIVERS_TEC);
    }
}
