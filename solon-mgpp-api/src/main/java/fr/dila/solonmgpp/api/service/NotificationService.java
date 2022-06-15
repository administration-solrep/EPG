package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.domain.NotificationDoc;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest.Notifications;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;

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
     */
    NotificationDoc createNotication(final CoreSession session, Notifications notifications);

    /**
     *
     * @param session
     * @param corbeille
     * @param date
     * @return
     */
    Long getCountNotificationsCorbeilleSince(final CoreSession session, final String corbeille, final Calendar date);

    /**
     *
     * @param session
     * @param evenementId
     * @param date
     * @return
     */
    Long getCountNotificationsEvenementSince(final CoreSession session, final String evenementId, final Calendar date);

    /**
     * Retourne la date de la derniere notification qui ne soit pas pour les tdr crée
     *
     * @return la date de dernière notification
     */
    Calendar getLastNotificationDate(final CoreSession session);

    /**
     * Créé une notification de maj du cache des tables de références epp
     */
    void notifyMAJTableDeReferenceEPP(CoreSession session);

    /**
     * Récupère la date d'arrivée de la dernière notification de maj tdr epp
     * @param session
     * @return
     */
    Calendar getLastNotificationUpdateCache(CoreSession session);

    /**
     * Met à jour complètement la table MGPP_INFO_CORBEILLE
     * @param session
     */
    void updateMgppInfoCorbeille(CoreSession session);
}
