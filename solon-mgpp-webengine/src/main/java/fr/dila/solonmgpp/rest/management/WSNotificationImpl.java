package fr.dila.solonmgpp.rest.management;

import fr.dila.reponses.rest.helper.VersionHelper;
import fr.dila.solonepp.rest.api.WSNotification;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.rest.api.NotificationDelegate;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.logger.AbstractLogger;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.commons.VersionResponse;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest;
import fr.sword.xsd.solon.epp.NotifierEvenementResponse;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceRequest;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

@WebObject(type = WSNotification.SERVICE_NAME)
@Produces("text/xml;charset=UTF-8")
public class WSNotificationImpl extends DefaultObject implements WSNotification {
    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(WSNotificationImpl.class);

    /**
     * Logger flux xml
     */
    private static final Logger LOGGER_XML = LogManager.getLogger(WSNotificationImpl.class);

    private NotificationDelegate delegate;

    public WSNotificationImpl() {
        super();
        // default empty constructor
    }

    private NotificationDelegate getNotificationDelegate(CoreSession session) {
        if (delegate == null) {
            if (session == null) {
                throw new NuxeoException("Session is null!");
            }
            delegate = new NotificationDelegateImpl(session);
        }
        return delegate;
    }

    @Override
    @GET
    @Path(WSNotification.METHOD_NAME_TEST)
    @Produces("text/plain")
    public String test() {
        return SERVICE_NAME;
    }

    @Override
    @GET
    @Path(WSNotification.METHOD_NAME_VERSION)
    public VersionResponse version() throws Exception {
        return VersionHelper.getVersionForWSEpp();
    }

    @Override
    @POST
    @Path(WSNotification.METHOD_NAME_NOTIFIER_EVENEMENT)
    public NotifierEvenementResponse notifierEvenement(NotifierEvenementRequest request) {
        long startTime = System.nanoTime();
        NotifierEvenementResponse response = new NotifierEvenementResponse();
        try {
            delegate = getNotificationDelegate(ctx.getCoreSession());
            response = delegate.notifierEvenement(request);
        } catch (NuxeoException e) {
            response.setMessageErreur(e.getMessage());
            response.setStatut(TraitementStatut.KO);
            LOGGER.error(ctx.getCoreSession(), MgppLogEnumImpl.FAIL_NOTIFY_EVENT_TEC, e);
        } catch (Exception e) {
            response.setMessageErreur(StringHelper.getStackTrace(e));
            response.setStatut(TraitementStatut.KO);
            LOGGER.error(ctx.getCoreSession(), MgppLogEnumImpl.FAIL_NOTIFY_EVENT_TEC, e);
        }
        try {
            if (LOGGER_XML.isInfoEnabled()) {
                LOGGER_XML.info(
                    STConstant.LOG_MSG_DURATION,
                    JaxBHelper.logInWsTransaction(
                        WSNotification.SERVICE_NAME,
                        WSNotification.METHOD_NAME_NOTIFIER_EVENEMENT,
                        ctx.getCoreSession().getPrincipal().getName(),
                        request,
                        NotifierEvenementRequest.class,
                        response,
                        NotifierEvenementResponse.class
                    ),
                    AbstractLogger.getDurationInMs(startTime)
                );
            }
        } catch (JAXBException jaxbe) {
            LOGGER.warn(
                ctx.getCoreSession(),
                STLogEnumImpl.FAIL_LOG_TEC,
                "Echec de log du flux xml notifierEvenement",
                jaxbe
            );
        }
        return response;
    }

    @Override
    @POST
    @Path(WSNotification.METHOD_NAME_NOTIFIER_TDR)
    public NotifierTableDeReferenceResponse notifierTableDeReference(NotifierTableDeReferenceRequest request) {
        long startTime = System.nanoTime();
        NotifierTableDeReferenceResponse response = new NotifierTableDeReferenceResponse();
        try {
            delegate = getNotificationDelegate(ctx.getCoreSession());
            response = delegate.notifierTableDeReference(request);
        } catch (NuxeoException e) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(e.getMessage());
            LOGGER.error(ctx.getCoreSession(), MgppLogEnumImpl.FAIL_NOTIFY_EVENT_TEC, e);
        }

        try {
            long duration = AbstractLogger.getDurationInMs(startTime);
            if (LOGGER_XML.isInfoEnabled()) {
                LOGGER_XML.info(
                    STConstant.LOG_MSG_DURATION,
                    JaxBHelper.logInWsTransaction(
                        WSNotification.SERVICE_NAME,
                        WSNotification.METHOD_NAME_NOTIFIER_TDR,
                        ctx.getCoreSession().getPrincipal().getName(),
                        request,
                        NotifierTableDeReferenceRequest.class,
                        response,
                        NotifierTableDeReferenceResponse.class
                    ),
                    duration
                );
            }
        } catch (JAXBException jaxbe) {
            LOGGER.warn(
                ctx.getCoreSession(),
                STLogEnumImpl.FAIL_LOG_TEC,
                "Echec de log du flux xml notifierTableDeReference",
                jaxbe
            );
        }
        return response;
    }
}
