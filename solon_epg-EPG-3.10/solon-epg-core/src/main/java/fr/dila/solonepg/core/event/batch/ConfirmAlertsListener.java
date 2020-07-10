package fr.dila.solonepg.core.event.batch;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.EpgAlertService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STSuiviBatchsConstants.TypeBatch;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Gestionnaire d'évènements qui permet de traiter les demandes de confirmation d'alertes :
 * - envoi d'un mail de confirmation tous les x jours
 * - désactivation des alertes dont la lien n'a pas été cliqué au bout de 3 semaines.
 * 
 * @author arolin
 */
public class ConfirmAlertsListener extends AbstractBatchEventListener {
    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ConfirmAlertsListener.class);

    public ConfirmAlertsListener(){
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_CONFIRM_ALERT);
    }
    
    @Override
    protected void processEvent(CoreSession session, Event event) throws ClientException {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_CONFIRM_ALERT_TEC);
        batchType = TypeBatch.FONCTIONNEL;
        EpgAlertService alertConfirmationService = SolonEpgServiceLocator.getAlertConfirmationSchedulerService();
        Long nbError = Long.valueOf(0L);
        alertConfirmationService.sendMailsConfirmation(session,batchLoggerModel,nbError);
        errorCount+=nbError.longValue();
        final long startTime = Calendar.getInstance().getTimeInMillis();

        alertConfirmationService.desactivationAlerteNonConfirme(session);

        final long endTime = Calendar.getInstance().getTimeInMillis();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        suiviBatchService.createBatchResultFor(batchLoggerModel, "Désactivation des alertes", endTime-startTime);
        LOGGER.info(session, EpgLogEnumImpl.END_B_CONFIRM_ALERT_TEC);
    }
}
