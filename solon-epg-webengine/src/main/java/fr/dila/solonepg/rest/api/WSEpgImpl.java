package fr.dila.solonepg.rest.api;

import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.rest.management.EpgDelegateImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.constant.STProfilUtilisateurConstants;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.logger.AbstractLogger;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.commons.VersionResponse;
import fr.sword.xsd.solon.epg.AttribuerNorRequest;
import fr.sword.xsd.solon.epg.AttribuerNorResponse;
import fr.sword.xsd.solon.epg.ChercherDossierRequest;
import fr.sword.xsd.solon.epg.ChercherDossierResponse;
import fr.sword.xsd.solon.epg.ChercherModificationDossierRequest;
import fr.sword.xsd.solon.epg.ChercherModificationDossierResponse;
import fr.sword.xsd.solon.epg.CreerDossierRequest;
import fr.sword.xsd.solon.epg.CreerDossierResponse;
import fr.sword.xsd.solon.epg.DonnerAvisCERequest;
import fr.sword.xsd.solon.epg.DonnerAvisCEResponse;
import fr.sword.xsd.solon.epg.ModifierDossierCERequest;
import fr.sword.xsd.solon.epg.ModifierDossierCEResponse;
import fr.sword.xsd.solon.epg.ModifierDossierRequest;
import fr.sword.xsd.solon.epg.ModifierDossierResponse;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = WSEpg.SERVICE_NAME)
@Produces("text/xml;charset=UTF-8")
public class WSEpgImpl extends AbstractWSVersion<EpgDelegate> implements WSEpg {
    private static final Logger LOGGER = LogManager.getLogger(WSEpg.class);

    public WSEpgImpl() {}

    @Override
    protected EpgDelegate createDelegate(CoreSession session) {
        return new EpgDelegateImpl(session);
    }

    @Override
    @GET
    @Path(WSEpg.METHOD_NAME_TEST)
    @Produces("text/plain")
    public String test() {
        return SERVICE_NAME;
    }

    @Override
    @GET
    @Path(WSEpg.METHOD_NAME_VERSION)
    public VersionResponse version() throws Exception {
        return super.version();
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_ATTRIBUER_NOR)
    public AttribuerNorResponse attribuerNor(AttribuerNorRequest request) throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_ATTRIBUER_NOR);
        AttribuerNorResponse response = new AttribuerNorResponse();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.attribuerNor(request);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                response.setStatut(TraitementStatut.KO);
                response.setMessageErreur(e.getMessage());
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSEpg.SERVICE_NAME,
                    WSEpg.METHOD_NAME_ATTRIBUER_NOR,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    AttribuerNorRequest.class,
                    response,
                    AttribuerNorResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_CHERCHER_DOSSIER)
    public ChercherDossierResponse chercherDossierEpg(ChercherDossierRequest request) throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_CHERCHER_DOSSIER);
        ChercherDossierResponse response = new ChercherDossierResponse();
        final STMailService mailService = STServiceLocator.getSTMailService();
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        CoreSession session = ctx.getCoreSession();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(session);
                response = delegate.chercherDossierEpg(request);
            } catch (Exception e) {
                if (response.getJeton() == null) {
                    if (request.getJeton() == null) {
                        response.setJeton("");
                    } else {
                        response.setJeton(getJeton(request.getJeton()));
                    }
                }
                LOGGER.error(e);
                response.setStatut(TraitementStatut.KO);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(
                    STConstant.LOG_MSG_DURATION,
                    JaxBHelper.logInWsTransaction(
                        WSEpg.SERVICE_NAME,
                        WSEpg.METHOD_NAME_CHERCHER_DOSSIER,
                        session.getPrincipal().getName(),
                        request,
                        ChercherDossierRequest.class,
                        response,
                        ChercherDossierResponse.class
                    ),
                    AbstractLogger.getDurationInMs(startTime)
                );
            }
        } catch (OutOfMemoryError err) {
            System.gc();
            // Il arrive que la récupération des documents par le jeton provoque une erreur de java heap space
            // Dans ce cas là, on envoie un mail pour prévenir l'administrateur technique qu'il y a eu ce type d'erreur.
            try {
                String mailContent = paramService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_TEXTE
                );
                String mailObject = paramService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_OBJET
                );
                String mailAdministrator = paramService.getParametreValue(
                    session,
                    STParametreConstant.MAIL_ADMIN_TECHNIQUE
                );
                Map<String, Object> param = new HashMap<>();
                param.put("concernant", "l'écriture du log de la lecture d'un jeton");
                mailService.sendTemplateMail(mailAdministrator, mailObject, mailContent, param);
            } catch (NuxeoException ce) {
                LOGGER.error("Erreur lors de l'envoi de mail d'erreur de java heapSpace.", ce);
            }
            LOGGER.error("Erreur lors de l'écriture du log de la récupération des dossiers via le jeton", err);
        }
        return response;
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_DONNER_AVIS_CE)
    public DonnerAvisCEResponse donnerAvisCE(DonnerAvisCERequest request) throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_DONNER_AVIS_CE);
        DonnerAvisCEResponse response = new DonnerAvisCEResponse();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.donnerAvisCE(request);
            } catch (Exception e) {
                LOGGER.error(e);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSEpg.SERVICE_NAME,
                    WSEpg.METHOD_NAME_DONNER_AVIS_CE,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    DonnerAvisCERequest.class,
                    response,
                    DonnerAvisCEResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    private long getStartTime(String wsName) {
        long startTime = System.nanoTime();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting Webservice {}", wsName);
        }

        return startTime;
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_MODIFIER_DOSSIER_CE)
    public ModifierDossierCEResponse modifierDossierCE(ModifierDossierCERequest request) throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_MODIFIER_DOSSIER_CE);
        ModifierDossierCEResponse response = new ModifierDossierCEResponse();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.modifierDossierCE(request);
            } catch (Exception e) {
                LOGGER.error(e);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSEpg.SERVICE_NAME,
                    WSEpg.METHOD_NAME_MODIFIER_DOSSIER_CE,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    ModifierDossierCERequest.class,
                    response,
                    ModifierDossierCEResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_CREER_DOSSIER)
    public CreerDossierResponse creerDossier(CreerDossierRequest request) throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_CREER_DOSSIER);
        CreerDossierResponse response = new CreerDossierResponse();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.creerDossier(request);
            } catch (Exception e) {
                LOGGER.error(e);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSEpg.SERVICE_NAME,
                    WSEpg.METHOD_NAME_CREER_DOSSIER,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    CreerDossierRequest.class,
                    response,
                    CreerDossierResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_MODIFIER_DOSSIER)
    public ModifierDossierResponse modifierDossier(ModifierDossierRequest request) throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_MODIFIER_DOSSIER);
        ModifierDossierResponse response = new ModifierDossierResponse();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(ctx.getCoreSession());
                response = delegate.modifierDossier(request);
            } catch (Exception e) {
                LOGGER.error(e);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                STConstant.LOG_MSG_DURATION,
                JaxBHelper.logInWsTransaction(
                    WSEpg.SERVICE_NAME,
                    WSEpg.METHOD_NAME_MODIFIER_DOSSIER,
                    ctx.getCoreSession().getPrincipal().getName(),
                    request,
                    ModifierDossierRequest.class,
                    response,
                    ModifierDossierResponse.class
                ),
                AbstractLogger.getDurationInMs(startTime)
            );
        }
        return response;
    }

    @Override
    @POST
    @Path(WSEpg.METHOD_NAME_CHERCHER_MODIFICATION_DOSSIER)
    public ChercherModificationDossierResponse chercherModificationDossier(ChercherModificationDossierRequest request)
        throws Exception {
        long startTime = getStartTime(WSEpg.METHOD_NAME_CHERCHER_MODIFICATION_DOSSIER);
        ChercherModificationDossierResponse response = new ChercherModificationDossierResponse();
        final STMailService mailService = STServiceLocator.getSTMailService();
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        CoreSession session = ctx.getCoreSession();
        if (isPasswordOutdated()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_OUTDATED_INFO);
        } else if (isPasswordTemporary()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(STProfilUtilisateurConstants.PASSWORD_IS_TEMPORARY_INFO);
        } else {
            try {
                delegate = getDelegate(session);
                response = delegate.chercherModificationDossierEpg(request);
            } catch (Exception e) {
                if (response.getJeton() == null) {
                    if (request.getJeton() == null) {
                        response.setJeton("");
                    } else {
                        response.setJeton(getJeton(request.getJeton()));
                    }
                }
                LOGGER.error(e);
                response.setStatut(TraitementStatut.KO);
                response.setMessageErreur(StringHelper.getStackTrace(e));
            }
        }
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(
                    STConstant.LOG_MSG_DURATION,
                    JaxBHelper.logInWsTransaction(
                        WSEpg.SERVICE_NAME,
                        WSEpg.METHOD_NAME_CHERCHER_MODIFICATION_DOSSIER,
                        session.getPrincipal().getName(),
                        request,
                        ChercherModificationDossierRequest.class,
                        response,
                        ChercherModificationDossierResponse.class
                    ),
                    AbstractLogger.getDurationInMs(startTime)
                );
            }
        } catch (OutOfMemoryError err) {
            System.gc();
            // Il arrive que la récupération des documents par le jeton provoque une erreur de java heap space
            // Dans ce cas là, on envoie un mail pour prévenir l'administrateur technique qu'il y a eu ce type d'erreur.
            try {
                String mailContent = paramService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_TEXTE
                );
                String mailObject = paramService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_OBJET
                );
                String mailAdministrator = paramService.getParametreValue(
                    session,
                    STParametreConstant.MAIL_ADMIN_TECHNIQUE
                );
                Map<String, Object> param = new HashMap<>();
                param.put("concernant", "l'écriture du log de la lecture d'un jeton");
                mailService.sendTemplateMail(mailAdministrator, mailObject, mailContent, param);
            } catch (NuxeoException ce) {
                LOGGER.error("Erreur lors de l'envoi de mail d'erreur de java heapSpace.", ce);
            }
            LOGGER.error(
                "Erreur lors de l'écriture du log de la récupération de la modification d'un dossier via le jeton",
                err
            );
        }
        return response;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
