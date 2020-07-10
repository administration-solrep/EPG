package fr.dila.solonmgpp.api.service;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.domain.NotificationDoc;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest.Notifications;

/**
 * Service dédié au notificationDoc
 *
 */
public interface NotificationService {

    /**
     * 
     * @param session
     * @param notifications
     * @return
     * @throws ClientException
     */
    NotificationDoc createNotication(final CoreSession session, Notifications notifications) throws ClientException;

    /**
     * 
     * @param session
     * @param corbeille
     * @param date
     * @return
     * @throws ClientException
     */
    Long getCountNotificationsCorbeilleSince(final CoreSession session, final String corbeille, final Calendar date) throws ClientException;

    /**
     * 
     * @param session
     * @param evenementId
     * @param date
     * @return
     * @throws ClientException
     */
    Long getCountNotificationsEvenementSince(final CoreSession session, final String evenementId, final Calendar date) throws ClientException;

    /**
     * Retourne la date de la derniere notification qui ne soit pas pour les tdr crée 
     * 
     * @return la date de dernière notification
     * @throws ClientException
     */
    Calendar getLastNotificationDate(final CoreSession session) throws ClientException ;

    /**
     * Créé une notification de maj du cache des tables de références epp
     */
	void notifyMAJTableDeReferenceEPP(CoreSession session) throws ClientException;

	/**
	 * Récupère la date d'arrivée de la dernière notification de maj tdr epp
	 * @param session
	 * @return
	 * @throws ClientException 
	 */
	Calendar getLastNotificationUpdateCache(CoreSession session) throws ClientException;

	/**
	 * Met à jour complètement la table MGPP_INFO_CORBEILLE
	 * @param session
	 * @throws ClientException 
	 */
	void updateMgppInfoCorbeille(CoreSession session) throws ClientException;

        
}
