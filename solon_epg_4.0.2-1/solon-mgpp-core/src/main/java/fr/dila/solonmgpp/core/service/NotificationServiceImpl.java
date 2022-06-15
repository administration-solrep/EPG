package fr.dila.solonmgpp.core.service;

import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppNotificationConstants;
import fr.dila.solonmgpp.api.domain.NotificationDoc;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.NotificationService;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.constant.STQueryConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.HasCommunicationNonTraiteesRequest;
import fr.sword.xsd.solon.epp.HasCommunicationNonTraiteesResponse;
import fr.sword.xsd.solon.epp.HasCommunicationNonTraiteesResponse.CorbeilleInfos;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest.Notifications;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;

/**
 * Implémentation du service des notificationDoc
 *
 */
public class NotificationServiceImpl implements NotificationService {
    private static final STLogger LOGGER = STLogFactory.getLog(NotificationServiceImpl.class);

    private static final String N_NT = "n." + SolonMgppNotificationConstants.NOTIFICATION_PREFIX + ":";

    private static final String DATE_ARRIVEE = N_NT + SolonMgppNotificationConstants.DATE_ARRIVE;

    private static final String TYPE_NOTIF = N_NT + SolonMgppNotificationConstants.TYPE_NOTIFICATION;

    private static final String MESSAGE_CORBEILLE_LIST = N_NT + SolonMgppNotificationConstants.MESSAGE_CORBEILLE_LIST;

    private static final String EVENEMENT_ID = N_NT + SolonMgppNotificationConstants.EVENEMENT_ID;

    private static final String SELECT_N_UID_FROM =
        STQueryConstant.SELECT + " n.ecm:uuid as id " + STQueryConstant.FROM;
    private static final String AS_N_WHERE = STQueryConstant.AS + " n " + STQueryConstant.WHERE;

    private static final String QUERY_NOTIF_ORDER_BY_DATE_ARRIVEE =
        SELECT_N_UID_FROM + //
        SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE +
        AS_N_WHERE + //
        TYPE_NOTIF +
        " != ? " +
        " ORDER BY " +
        DATE_ARRIVEE +
        " DESC ";

    private static final String QUERY_NOTIFICATIONS_CORBEILLE =
        SELECT_N_UID_FROM + //
        SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE +
        AS_N_WHERE + //
        MESSAGE_CORBEILLE_LIST +
        " = ? AND " +
        DATE_ARRIVEE +
        " > ? AND " +
        TYPE_NOTIF +
        " is null ";

    private static final String QUERY_NOTIFICATIONS_EVENEMENT =
        SELECT_N_UID_FROM + //
        SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE +
        AS_N_WHERE + //
        EVENEMENT_ID +
        " = ? AND " +
        DATE_ARRIVEE +
        " > ? AND (" +
        TYPE_NOTIF +
        " != '" +
        SolonMgppNotificationConstants.NOTIFICATION_TDR +
        "' OR " +
        TYPE_NOTIF +
        " is null )";

    private static final String PATH_TO_NOTIF_TDR =
        SolonMgppNotificationConstants.NOTIFICATION_ROOT_PATH + "/notificationTDR";

    private static final String MERGE_MGPP_INFO_CORB =
        "MERGE INTO " +
        SolonMgppCorbeilleConstant.MGPP_INFO_CORBEILLE_TABLE_NAME +
        " USING dual ON (" +
        SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE +
        " = :corbeilleId)" +
        " WHEN NOT MATCHED THEN INSERT (" +
        SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE +
        "," +
        SolonMgppCorbeilleConstant.COL_HASMESS_MGPP_INFO_CORBEILLE +
        ") VALUES (:corbeilleId, :hasMess) WHEN MATCHED THEN UPDATE SET " +
        SolonMgppCorbeilleConstant.COL_HASMESS_MGPP_INFO_CORBEILLE +
        " = :hasMess";

    private static volatile PersistenceProvider persistenceProvider;

    public NotificationServiceImpl() {
        // Default constructor
    }

    /**
     *
     * @param session
     * @param notifications
     * @return
     */
    @Override
    public NotificationDoc createNotication(final CoreSession session, final Notifications notifications) {
        final Message message = notifications.getMessage();

        final String evenementId = message.getIdEvenement();
        final List<String> corbeilleList = notifications.getIdCorbeille();
        final DocumentModel notificationDocModel = createBareNotificationDoc(session);
        final NotificationDoc notificationDoc = notificationDocModel.getAdapter(NotificationDoc.class);
        notificationDoc.setDateArrive(new GregorianCalendar());
        notificationDoc.setEvenementId(evenementId);
        notificationDoc.setMessageCorbeilleList(corbeilleList);
        session.saveDocument(notificationDoc.getDocument());
        session.save();

        refreshMgppInfoCorbeille(session, corbeilleList);

        return notificationDoc;
    }

    /**
     *
     * @param session
     * @return
     */
    private DocumentModel createBareNotificationDoc(final CoreSession session) {
        DocumentModel notificationDocModel = session.createDocumentModel(
            SolonMgppNotificationConstants.NOTIFICATION_ROOT_PATH,
            "nt",
            SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE
        );
        notificationDocModel = session.createDocument(notificationDocModel);
        session.save();
        return notificationDocModel;
    }

    /**
     * Compte le nombre de jetons par critères de recherche.
     *
     * @return Nombre de jetons
     */
    @Override
    public Long getCountNotificationsCorbeilleSince(
        final CoreSession session,
        final String corbeille,
        final Calendar date
    ) {
        final List<Object> paramList = new ArrayList<>();
        paramList.add(corbeille);
        paramList.add(date);

        return QueryUtils.doCountQuery(
            session,
            FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + QUERY_NOTIFICATIONS_CORBEILLE,
            paramList.toArray()
        );
    }

    @Override
    public Long getCountNotificationsEvenementSince(
        final CoreSession session,
        final String evenementId,
        final Calendar date
    ) {
        final List<Object> paramList = new ArrayList<>();
        paramList.add(evenementId);
        paramList.add(date);

        return QueryUtils.doCountQuery(
            session,
            FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + QUERY_NOTIFICATIONS_EVENEMENT,
            paramList.toArray()
        );
    }

    @Override
    public Calendar getLastNotificationDate(final CoreSession session) {
        final List<Object> paramList = new ArrayList<>();
        paramList.add(SolonMgppNotificationConstants.NOTIFICATION_TDR);
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE,
            QUERY_NOTIF_ORDER_BY_DATE_ARRIVEE,
            paramList.toArray(),
            1,
            0
        );
        //  on met un limit/offset dans la requete cela evite de récupérer 950 documents dans une liste pour faire un list.get(0)...
        if (docs.isEmpty()) {
            return null;
        }

        return docs.get(0).getAdapter(NotificationDoc.class).getDateArrive();
    }

    @Override
    public void notifyMAJTableDeReferenceEPP(final CoreSession session) {
        final DocumentModel notificationDocDoc = getOrCreateNotificationTDR(session);
        if (notificationDocDoc != null) {
            final NotificationDoc notificationDoc = notificationDocDoc.getAdapter(NotificationDoc.class);
            notificationDoc.setDateArrive(Calendar.getInstance());
            session.saveDocument(notificationDoc.getDocument());
            session.save();
        }
    }

    @Override
    public Calendar getLastNotificationUpdateCache(final CoreSession session) {
        final DocumentModel notificationDocDoc = getOrCreateNotificationTDR(session);
        if (notificationDocDoc == null) {
            return Calendar.getInstance();
        }
        final NotificationDoc notificationDoc = notificationDocDoc.getAdapter(NotificationDoc.class);
        return notificationDoc.getDateArrive();
    }

    /**
     * Récupère le document de notification de demande de maj du cache des tdr epp
     * @param session
     * @return
     */
    private DocumentModel getOrCreateNotificationTDR(final CoreSession session) {
        if (session == null) {
            return null;
        }
        DocumentModel notificationDocDoc = null;
        PathRef pathToNotif = new PathRef(PATH_TO_NOTIF_TDR);

        if (session.exists(pathToNotif)) {
            notificationDocDoc = session.getDocument(pathToNotif);
        } else {
            notificationDocDoc =
                session.createDocumentModel(
                    SolonMgppNotificationConstants.NOTIFICATION_ROOT_PATH,
                    "notificationTDR",
                    SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE
                );
            notificationDocDoc = session.createDocument(notificationDocDoc);
            final NotificationDoc notificationDoc = notificationDocDoc.getAdapter(NotificationDoc.class);
            notificationDoc.setTypeNotification(SolonMgppNotificationConstants.NOTIFICATION_TDR);
            notificationDoc.setDateArrive(Calendar.getInstance());
            notificationDocDoc = session.saveDocument(notificationDoc.getDocument());
            session.save();
        }
        return notificationDocDoc;
    }

    @Override
    public void updateMgppInfoCorbeille(CoreSession session) {
        WSEpp wsEpp = getWsEpp(session);
        HasCommunicationNonTraiteesResponse response = getHasCommunicationNonTraiteesResponse(
            session,
            wsEpp,
            new HasCommunicationNonTraiteesRequest()
        );
        updateMgppInfoCorbeilleWithData(session, response.getCorbeilleInfos());
    }

    /**
     * Récupère les informations côté epp concernant l'existence de communication non_traités/en_cours_redaction
     * pour mettre à jour la table en bdd
     * @param session
     * @param corbeilleList
     */
    private void refreshMgppInfoCorbeille(CoreSession session, List<String> corbeilleList) {
        WSEpp wsEpp = getWsEpp(session);
        HasCommunicationNonTraiteesRequest request = new HasCommunicationNonTraiteesRequest();
        request.getIdCorbeilles().addAll(corbeilleList);
        HasCommunicationNonTraiteesResponse response = getHasCommunicationNonTraiteesResponse(session, wsEpp, request);
        updateMgppInfoCorbeilleWithData(session, response.getCorbeilleInfos());
    }

    private WSEpp getWsEpp(CoreSession session) {
        WSEpp wsEpp = null;
        try {
            wsEpp = SolonMgppWsLocator.getWSEpp(session);
        } catch (WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(e);
        }
        return wsEpp;
    }

    private HasCommunicationNonTraiteesResponse getHasCommunicationNonTraiteesResponse(
        CoreSession session,
        WSEpp wsEpp,
        HasCommunicationNonTraiteesRequest request
    ) {
        HasCommunicationNonTraiteesResponse response = null;
        try {
            response = wsEpp.hasCommunicationNonTraitees(request);
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        if (response == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération des informations des corbeilles a échoué."
            );
        } else if (response.getStatut() == null || !TraitementStatut.OK.equals(response.getStatut())) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, response.getMessageErreur());
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération des corbeilles a échoué." +
                WSErrorHelper.buildCleanMessage(response.getMessageErreur())
            );
        }
        return response;
    }

    /**
     * Met à jour la bdd avec les corbeilleInfos en paramètre
     * @param session
     * @param corbeillesInfosList
     */
    private void updateMgppInfoCorbeilleWithData(CoreSession session, final List<CorbeilleInfos> corbeillesInfosList) {
        getOrCreatePersistenceProvider()
            .run(
                true,
                new RunVoid() {

                    @Override
                    public void runWith(EntityManager entityManager) {
                        for (CorbeilleInfos infos : corbeillesInfosList) {
                            Query queryMerge = entityManager.createNativeQuery(MERGE_MGPP_INFO_CORB);
                            String idCorbeille = infos.getIdCorbeille();
                            boolean hasNonTraitees = infos.isHasNonTraitees();
                            queryMerge.setParameter("corbeilleId", idCorbeille);
                            queryMerge.setParameter("corbeilleId", idCorbeille);
                            queryMerge.setParameter("hasMess", hasNonTraitees);
                            queryMerge.setParameter("hasMess", hasNonTraitees);
                            queryMerge.executeUpdate();
                            LOGGER.info(MgppLogEnumImpl.UPDATE_MGPP_INFO_CORB_TEC, idCorbeille);
                        }
                    }
                }
            );
    }

    /**
     * Must be called when the service is no longer needed
     */
    public static void dispose() {
        deactivatePersistenceProvider();
    }

    private static PersistenceProvider getOrCreatePersistenceProvider() {
        if (persistenceProvider == null) {
            synchronized (NotificationServiceImpl.class) {
                if (persistenceProvider == null) {
                    activatePersistenceProvider();
                }
            }
        }
        return persistenceProvider;
    }

    private static void activatePersistenceProvider() {
        Thread thread = Thread.currentThread();
        ClassLoader last = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
            PersistenceProviderFactory persistenceProviderFactory = ServiceUtil.getRequiredService(
                PersistenceProviderFactory.class
            );
            persistenceProvider = persistenceProviderFactory.newProvider("swcorbeille-infos");
            persistenceProvider.openPersistenceUnit();
        } finally {
            thread.setContextClassLoader(last);
        }
    }

    private static void deactivatePersistenceProvider() {
        if (persistenceProvider != null) {
            synchronized (NotificationServiceImpl.class) {
                if (persistenceProvider != null) {
                    persistenceProvider.closePersistenceUnit();
                    persistenceProvider = null;
                }
            }
        }
    }
}
