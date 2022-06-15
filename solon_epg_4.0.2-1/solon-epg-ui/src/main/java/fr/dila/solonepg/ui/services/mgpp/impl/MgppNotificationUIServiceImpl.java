package fr.dila.solonepg.ui.services.mgpp.impl;

import static java.util.Optional.ofNullable;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.solonmgpp.api.service.NotificationService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.bean.NotificationDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.STUserSessionKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.services.NotificationUIService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Service d'alerte utilisateur lorsque la corbeille et/ou la communication
 * courante sont modifiées.
 */
public class MgppNotificationUIServiceImpl implements NotificationUIService {
    private static final int MILLE = 1000;
    private static final int DELAI_CHECK_NOTIF_DEFAULT = 30000;

    public MgppNotificationUIServiceImpl() {
        // Default constructor
    }

    @Override
    public NotificationDTO getNotificationDTO(SpecificContext context) {
        CoreSession session = context.getSession();
        String corbeilleId = context.getFromContextData(STContextDataKey.CORBEILLE_ID);
        String evenementId = context.getFromContextData(STContextDataKey.EVENEMENT_ID);

        if (StringUtils.isNotBlank(corbeilleId)) {
            corbeilleId =
                ofNullable(MgppCorbeilleName.fromValue(corbeilleId)).map(MgppCorbeilleName::name).orElse(corbeilleId);
        }

        NotificationDTO notificationDTO = new NotificationDTO();
        final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
        // Modifications sur la corbeille
        if (StringUtils.isNotEmpty(corbeilleId)) {
            Long count = notificationService.getCountNotificationsCorbeilleSince(
                session,
                corbeilleId,
                getLastUpdate(context)
            );
            if (count > 0) {
                notificationDTO.setCorbeilleModified(true);
            }
        }

        // Modifications sur l'évènement
        if (StringUtils.isNotEmpty(evenementId)) {
            Long count = notificationService.getCountNotificationsEvenementSince(
                session,
                evenementId,
                getLastUpdate(context)
            );
            if (count > 0) {
                notificationDTO.setEvenementModified(true);
            }
        }

        UserSessionHelper.putUserSessionParameter(
            context,
            STUserSessionKey.LAST_USER_NOTIFICATION,
            Calendar.getInstance()
        );

        return notificationDTO;
    }

    @Override
    public long getNotificationDelai(SpecificContext context) {
        ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator
            .getParametreMgppService()
            .findParametrageMgpp(context.getSession());
        return parametrageMgpp.getDelai() == null ? DELAI_CHECK_NOTIF_DEFAULT : parametrageMgpp.getDelai() * MILLE;
    }

    @Override
    public void reloadCacheTdrEppIfNecessary(SpecificContext context) {
        final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
        CoreSession session = context.getSession();
        Calendar notificationCalendar = notificationService.getLastNotificationUpdateCache(session);
        final TableReferenceService tdrService = SolonMgppServiceLocator.getTableReferenceService();
        final Calendar lastUpdateCache = tdrService.getLastUpdateCache();
        if (lastUpdateCache == null || lastUpdateCache.before(notificationCalendar)) {
            tdrService.updateCaches(session);
        }
    }
}
