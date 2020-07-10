package fr.dila.solonepg.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

import com.sun.jersey.spi.inject.Inject;

import fr.dila.reponses.rest.helper.VersionHelper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.rest.management.SpeDelegateImpl;
import fr.dila.st.api.constant.STProfilUtilisateurConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.commons.VersionResponse;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.EnvoyerRetourPERequest;
import fr.sword.xsd.solon.spe.EnvoyerRetourPEResponse;
import fr.sword.xsd.solon.spe.PEstatut;

@WebObject(type = WSSpe.SERVICE_NAME)
@Produces("text/xml;charset=UTF-8")
public class WSSpeImpl extends DefaultObject implements WSSpe {

	private static final Logger	LOGGER	= Logger.getLogger(WSSpe.class);

	@Inject
	NuxeoPrincipalImpl			principal;

	protected SpeDelegate		delegate;

	protected SpeDelegate getSpeDelegate(CoreSession session) {
		if (delegate == null && session != null) {
			delegate = new SpeDelegateImpl(session);
		}
		return delegate;
	}

	@GET
	@Path(WSSpe.METHOD_NAME_TEST)
	@Produces("text/plain")
	public String test() {
		return SERVICE_NAME;
	}

	@GET
	@Path(WSSpe.METHOD_NAME_VERSION)
	public VersionResponse version() throws Exception {
		return VersionHelper.getVersionForWSsolonEpg();
	}

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
				delegate = getSpeDelegate(ctx.getCoreSession());
				response = delegate.envoyerPremiereDemandePE(request);
			} catch (Exception e) {
				LOGGER.error(e);
				response.setMessageErreur(StringUtil.getStackTrace(e));
			}
		}
		long duration = (System.nanoTime() - startTime) / 1000000;
		LOGGER.info(JaxBHelper.logInWsTransaction(WSSpe.SERVICE_NAME, WSSpe.METHOD_NAME_ENVOYER_PREMIERE_DEMANDE_PE,
				ctx.getCoreSession().getPrincipal().getName(), request, EnvoyerPremiereDemandePERequest.class,
				response, EnvoyerPremiereDemandePEResponse.class)
				+ "---DURATION : " + duration + "ms ---\n");
		return response;
	}

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
				delegate = getSpeDelegate(ctx.getCoreSession());
				response = delegate.envoyerRetourPE(request);
			} catch (Exception e) {
				LOGGER.error(e);
				response.setMessageErreur(StringUtil.getStackTrace(e));
			}
		}
		long duration = (System.nanoTime() - startTime) / 1000000;
		LOGGER.info(JaxBHelper.logInWsTransaction(WSSpe.SERVICE_NAME, WSSpe.METHOD_NAME_ENVOYER_RETOUR_PE, ctx
				.getCoreSession().getPrincipal().getName(), request, EnvoyerRetourPERequest.class, response,
				EnvoyerRetourPEResponse.class)
				+ "---DURATION : " + duration + "ms ---\n");
		return response;
	}

	private boolean isPasswordOutdated() {
		CoreSession session = ctx.getCoreSession();
		try {
			if (SolonEpgServiceLocator.getProfilUtilisateurService().isUserPasswordOutdated(session,
					session.getPrincipal().getName())) {
				STServiceLocator.getSTUserService().forceChangeOutdatedPassword(session.getPrincipal().getName());
				return true;
			}
			return false;
		} catch (ClientException e) {
			LOGGER.warn("Impossible de vérifier la validité de la date de changement de mot de passe", e);
			return false;
		}
	}

	private boolean isPasswordTemporary() {
		CoreSession session = ctx.getCoreSession();
		try {
			if (STServiceLocator.getSTUserService().isUserPasswordResetNeeded(session.getPrincipal().getName())) {
				return true;
			}
			return false;
		} catch (ClientException e) {
			LOGGER.warn("Impossible de vérifier si le mot de passe est temporaire", e);
			return false;
		}
	}

}
