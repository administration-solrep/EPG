package fr.dila.solonmgpp.rest.management;

import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.NotificationService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.rest.api.NotificationDelegate;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.NotificationEvenementType;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest.Notifications;
import fr.sword.xsd.solon.epp.NotifierEvenementResponse;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceRequest;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceResponse;
import fr.sword.xsd.solon.epp.ObjetType;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Permet de gerer toutes les operations sur epg
 */
public class NotificationDelegateImpl implements NotificationDelegate {
    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(NotificationDelegateImpl.class);

    protected CoreSession session;

    /**
     * Constructor;
     *
     * @param documentManager
     */
    public NotificationDelegateImpl(CoreSession documentManager) {
        session = documentManager;
    }

    /**
     * @param request
     * @return
     * @throws NuxeoException
     * @throws Exception
     */
    @Override
    public NotifierEvenementResponse notifierEvenement(NotifierEvenementRequest request) throws Exception {
        final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
        NotifierEvenementResponse reponse = new NotifierEvenementResponse();
        List<Notifications> notificationsList = request.getNotifications();

        if (notificationsList == null || notificationsList.isEmpty()) {
            reponse.setStatut(TraitementStatut.KO);
            reponse.setMessageErreur("Aucune notifications trouvées.");
            return reponse;
        }

        boolean error = false;
        StringBuilder errorsMessages = new StringBuilder("Errors occurred : ");
        for (Notifications notifications : notificationsList) {
            NotificationEvenementType notificationEvenementType = notifications.getTypeNotification();
            // récupération informations
            Message message = notifications.getMessage();
            String idEvenement = message.getIdEvenement();
            EvenementType evenementType = message.getTypeEvenement();

            if (notificationEvenementType.equals(NotificationEvenementType.ACCUSER_RECEPTION)) {
                // Traitement des navettes
                traiterNavette(idEvenement, evenementType);
                updateFicheLoi(idEvenement, evenementType);
            } else {
                if (Institution.GOUVERNEMENT.equals(message.getDestinataireEvenement())) {
                    LOGGER.info(session, MgppLogEnumImpl.ACCUSER_RECEPTION_AUTOMATIQUE_TEC, idEvenement);
                    // AR auto pour le gouvernement
                    accuserReception(idEvenement, evenementType);
                }

                if (Institution.GOUVERNEMENT.equals(message.getEmetteurEvenement())) {
                    // Traitement des navettes
                    traiterNavette(idEvenement, evenementType);
                    updateFicheLoi(idEvenement, evenementType);
                }
            }
            try {
                notificationService.createNotication(session, notifications);
            } catch (NuxeoException ce) {
                LOGGER.warn(session, EpgLogEnumImpl.FAIL_CREATE_NOTIF_TEC, ce.getMessage());
                LOGGER.debug(session, EpgLogEnumImpl.FAIL_CREATE_NOTIF_TEC, ce);
                error = true;
                errorsMessages.append(ce.getMessage()).append(" / ");
            }
        }

        if (error) {
            reponse.setStatut(TraitementStatut.KO);
            reponse.setMessageErreur(errorsMessages.toString());
        } else {
            reponse.setStatut(TraitementStatut.OK);
        }
        return reponse;
    }

    private void traiterNavette(String idEvenement, EvenementType typeEvenement) {
        SolonMgppServiceLocator.getNavetteService().addNavetteToFicheLoi(session, idEvenement, typeEvenement);
    }

    private void updateFicheLoi(String idEvenement, EvenementType typeEvenement) {
        SolonMgppServiceLocator.getDossierService().updateFicheLoi(session, idEvenement, typeEvenement);
    }

    private void accuserReception(String idEvenement, EvenementType evenementType) {
        SolonMgppServiceLocator.getEvenementService().accuserReception(idEvenement, evenementType, session);
    }

    @Override
    public NotifierTableDeReferenceResponse notifierTableDeReference(NotifierTableDeReferenceRequest request) {
        NotifierTableDeReferenceResponse response = new NotifierTableDeReferenceResponse();

        final ObjetType type = request.getType();
        if (ObjetType.IDENTITE.equals(type) || ObjetType.MANDAT.equals(type) || ObjetType.ORGANISME.equals(type)) {
            final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
            notificationService.notifyMAJTableDeReferenceEPP(session);
        }
        response.setStatut(TraitementStatut.OK);

        return response;
    }
}
