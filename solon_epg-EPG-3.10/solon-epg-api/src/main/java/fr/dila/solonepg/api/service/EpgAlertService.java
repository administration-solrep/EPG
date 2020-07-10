package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.st.api.event.batch.BatchLoggerModel;
import fr.dila.st.api.service.STAlertService;

/**
 * Le service de gestion de confirmation alertes, chargé d'envoyer les mails de confirmations des alertes et de désactiver ces alertes si l'utilisateur ne le fais pas.
 * 
 * @author arolin
 */
public interface EpgAlertService extends STAlertService {

    /**
     * Execute la requête et envoie le mail de confirmation des alertes.
     * 
     * @param session
     * @throws Exception
     */
    void sendMailsConfirmation(CoreSession session, BatchLoggerModel batchLoggerModel, Long nbError) throws ClientException;

    /**
     * Execute la requête de désactivation des alertes qui pas été confirmée.
     * 
     * @param session
     * @throws Exception
     */
    void desactivationAlerteNonConfirme(CoreSession session) throws ClientException;
    
    /**
     * Confirmation de l'alerte via son id
     * 
     * @param alertId
     * @throws ClientException
     */
    String confirmationAlerte(String alertId) throws ClientException;

}
