package fr.dila.solonepg.rest.api;

import fr.dila.solonepg.rest.management.SpeDelegateImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STProfilUtilisateurConstants;
import fr.dila.st.core.logger.AbstractLogger;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.commons.VersionResponse;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.EnvoyerRetourPERequest;
import fr.sword.xsd.solon.spe.EnvoyerRetourPEResponse;
import fr.sword.xsd.solon.spe.PEstatut;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = WSSpe.SERVICE_NAME)
@Produces("text/xml;charset=UTF-8")
public class WSSpeImpl extends AbstractWSVersion<SpeDelegate> implements WSSpe {
    private static final Logger LOGGER = LogManager.getLogger(WSSpe.class);

    @Override
    protected SpeDelegate createDelegate(CoreSession session) {
        return new SpeDelegateImpl(session);
    }

    @Override
    @GET
    @Path(WSSpe.METHOD_NAME_TEST)
    @Produces("text/plain")
    public String test() {
        return SERVICE_NAME;
    }

    @Override
    @GET
    @Path(WSSpe.METHOD_NAME_VERSION)
    public VersionResponse version() throws Exception {
        return super.version();
    }

    @Override
    @POST
    @Path(WSSpe.METHOD_NAME_ENVOYER_PREMIERE_DEMANDE_PE)
    public EnvoyerPremiereDemandePEResponse envoyerPremiereDemandePE(EnvoyerPremiereDemandePERequest request)
        throws Exception {
        long startTime = System.nanoTime();
        EnvoyerPremiereDemandePEResponse response = new EnvoyerPremiereDemandePEResponse();
        if (isPasswordOutdated()) {
            response.setStatus(PEstatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatus(PEstatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.envoyerPremiereDemandePE(request);
            } catch (Exception e) {
                LOGGER.error(e);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSSpe.SERVICE_NAME,
                    WSSpe.METHOD_NAME_ENVOYER_PREMIERE_DEMANDE_PE,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    EnvoyerPremiereDemandePERequest.class,
                    response,
                    EnvoyerPremiereDemandePEResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    @Override
    @POST
    @Path(WSSpe.METHOD_NAME_ENVOYER_RETOUR_PE)
    public EnvoyerRetourPEResponse envoyerRetourPE(EnvoyerRetourPERequest request) throws Exception {
        long startTime = System.nanoTime();
        EnvoyerRetourPEResponse response = new EnvoyerRetourPEResponse();
        if (isPasswordOutdated()) {
            response.setStatus(PEstatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatus(PEstatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.envoyerRetourPE(request);
            } catch (Exception e) {
                LOGGER.error(e);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSSpe.SERVICE_NAME,
                    WSSpe.METHOD_NAME_ENVOYER_RETOUR_PE,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    EnvoyerRetourPERequest.class,
                    response,
                    EnvoyerRetourPEResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
