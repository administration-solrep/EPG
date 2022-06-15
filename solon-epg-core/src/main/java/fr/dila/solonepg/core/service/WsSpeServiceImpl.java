package fr.dila.solonepg.core.service;

import static fr.dila.solonepg.api.constant.SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_ALIAS_KEY_SSL;
import static fr.dila.solonepg.api.constant.SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_NB_RETRY_BEFORE_DROP;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.InformationsParlementairesService;
import fr.dila.solonepg.api.service.WsSpeService;
import fr.dila.solonepg.api.spe.WsSpe;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.rest.api.WSSpe;
import fr.dila.solonepg.rest.client.WSSpeCaller;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.core.util.UnrestrictedQueryRunner;
import fr.dila.st.rest.client.WSProxyFactory;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.solon.epg.DossierEpgWithFile;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.PEDemandeType;
import fr.sword.xsd.solon.spe.PEstatut;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

public class WsSpeServiceImpl implements WsSpeService {
    private static final Log LOGGER = LogFactory.getLog(WsSpeServiceImpl.class);
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOG = STLogFactory.getLog(WsSpeServiceImpl.class);

    private static DocumentModel wsDemandePublicationFolder;

    /**
     * url du serveur de la dila à solliciter pour envoyer les webservices (constante)
     */
    private static String dilaUrl;

    /**
     * nom de l'utilisateur qui envoie le webservice de publication (constante)
     */
    private static String userName;

    /**
     * mot de passe de l'utilisateur qui envoie le webservice de publication (constante)
     */
    private static String password;

    /**
     * alias de la clé SSL : non utilisé (constante)
     */
    private static String keyAlias;

    /**
     * nombre d'essai avant que l'on n'arrête de relancer le webservice. (constante)
     */
    private static int nbRetryBeforeDrop;

    @Override
    public void envoiPremiereDemandePublicationPourDila(
        final Dossier dossier,
        final String webservice,
        final CoreSession session,
        final String typePublication,
        final SSRouteStep runningStep
    ) {
        LOGGER.debug("Début - Envoi d'une première demande de publication à la DILA de type " + typePublication);
        if (dossier == null) {
            LOGGER.error("Type de document non pris en charge par la notification : ");
            return;
        }

        if (BooleanUtils.isNotTrue(dossier.hasFeuilleRoute())) {
            LOGGER.error("Ce dossier ne comporte pas de feuille de route : " + dossier.getDocument().getId());
            return;
        }

        // remplit les constants de la dila et vérifie que ces valeurs sont non nulles.
        if (!checkDilaParameter()) {
            // Les paramètres ne sont pas bon, on trace dans le journal l'échec de notification
            // et on envoie un mail à l'admin technique
            LOGGER.error("Les paramètres ne sont pas complets");
            STServiceLocator
                .getJournalService()
                .journaliserActionAdministration(
                    session,
                    dossier.getDocument(),
                    SolonEpgEventConstant.NOTIF_ECHOUEE_EVENT,
                    SolonEpgEventConstant.NOTIF_ECHOUEE_COMMENT
                );
            final STParametreService paramService = STServiceLocator.getSTParametreService();
            final String object = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.OBJET_MAIL_DILA_PARAMETERS
            );
            final String content = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.TEXTE_MAIL_DILA_PARAMETERS
            );

            final String mailAdmin = paramService.getParametreValue(session, STParametreConstant.MAIL_ADMIN_TECHNIQUE);

            STServiceLocator
                .getSTMailService()
                .sendTemplateMail(mailAdmin, object, content, new HashMap<String, Object>());
            return;
        }

        LOGGER.debug("Les paramètres sont (url,login,password) : " + dilaUrl + ":" + userName + ":" + password);
        final String posteId = SSServiceLocator
            .getMailboxPosteService()
            .getPosteIdFromMailboxId(runningStep.getDistributionMailboxId());

        final PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteId);
        callNotifier(dossier, posteNode, webservice, session, typePublication, dilaUrl, userName, password);
        LOGGER.debug("Fin - Envoi d'une première demande de publication à la DILA de type " + typePublication);
    }

    @Override
    public long retryPremiereDemandePublication(
        final CoreSession session,
        final BatchLoggerModel batchLogger,
        long nbError
    ) {
        // Chargement des services

        final STParametreService paramService = STServiceLocator.getSTParametreService();
        long newNbError = nbError;

        // récupération du mail administrateur
        final String mailAdmin = paramService.getParametreValue(session, STParametreConstant.MAIL_ADMIN_TECHNIQUE);
        LOGGER.debug("Début - retryPremiereDemandePublication");

        if (!checkDilaParameter()) {
            // Les paramètres ne sont pas bon, on envoie un mail à l'admin technique
            LOGGER.error("Les paramètres ne sont pas complets");
            final String object = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.OBJET_MAIL_DILA_PARAMETERS
            );
            final String content = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.TEXTE_MAIL_DILA_PARAMETERS
            );
            STServiceLocator
                .getSTMailService()
                .sendTemplateMail(mailAdmin, object, content, new HashMap<String, Object>());
            newNbError = nbError + 1;
            return newNbError;
        }

        final List<DocumentModel> spePublicationDocs = session.getChildren(
            getWsSpeFolderUnrestricted(session).getRef()
        );

        final JournalService journalService = STServiceLocator.getJournalService();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        final STPostesService postesService = STServiceLocator.getSTPostesService();

        for (final DocumentModel spePublicationDoc : spePublicationDocs) {
            final long startTime = Calendar.getInstance().getTimeInMillis();
            final WsSpe spePublication = spePublicationDoc.getAdapter(WsSpe.class);
            // Récupération du poste
            final PosteNode posteNode = postesService.getPoste(spePublication.getPosteId());
            String norDossier = "";
            DocumentModel dossierDoc = null;
            Dossier dossier = null;
            try {
                dossierDoc = session.getDocument(new IdRef(spePublication.getIdDossier()));
                dossier = dossierDoc.getAdapter(Dossier.class);
                norDossier = dossier.getNumeroNor();
            } catch (final NuxeoException e2) {
                LOGGER.error("Erreur lors de la récupération du dossier pour la publication au webservice", e2);
            }
            if (spePublication.getNbEssais() < nbRetryBeforeDrop) {
                try {
                    if (dossier == null) {
                        LOGGER.error("Le dossier est null - la notification n'a pas pu être envoyée");
                        newNbError = nbError + 1;
                    } else {
                        notifyWebservice(
                            dossier,
                            spePublication.getWebservice(),
                            session,
                            spePublication.getTypePublication(),
                            dilaUrl,
                            userName,
                            password
                        );
                        // La notification est passée sans encombres, on peut donc supprimer le document notification
                        // qui avait été créé pour le rejeu
                        LOG.info(session, EpgLogEnumImpl.DEL_WS_SPE_TEC, spePublicationDoc);
                        session.removeDocument(spePublicationDoc.getRef());

                        // Journalisation de l'envoi de notification dans le dossier
                        if (dossierDoc != null) {
                            journalService.journaliserActionAdministration(
                                session,
                                dossierDoc,
                                SolonEpgEventConstant.NOTIF_REUSSIE_EVENT,
                                SolonEpgEventConstant.NOTIF_REUSSIE_COMMENT
                            );
                        }
                    }
                } catch (final Exception e) {
                    LOGGER.error(e);
                    // Le rejeu de la notification a échoué, on incrémente le nombre d'essai
                    // Et on envoie un mail aux utilisateurs du poste et admin tech
                    newNbError = nbError + 1;
                    spePublication.setNbEssais(spePublication.getNbEssais() + 1);
                    final String object = paramService.getParametreValue(
                        session,
                        SolonEpgParametreConstant.OBJET_MAIL_ECHEC_REJEU_NOTIF
                    );
                    final String content = paramService.getParametreValue(
                        session,
                        SolonEpgParametreConstant.TEXTE_MAIL_ECHEC_REJEU_NOTIF
                    );
                    sendTemplateMailNotif(norDossier, posteNode, mailAdmin, object, content);

                    session.saveDocument(spePublicationDoc);
                    // Journalisation de l'échec du rejeu d'envoi de notification dans le dossier
                    if (dossierDoc != null) {
                        journalService.journaliserActionAdministration(
                            session,
                            dossierDoc,
                            SolonEpgEventConstant.NOTIF_ECHOUEE_EVENT,
                            SolonEpgEventConstant.NOTIF_ECHOUEE_COMMENT
                        );
                    }
                }
            } else {
                // Trop d'essais effectués, abandon de la notification et envoi d'un mail aux utilisateurs du poste
                final String object = paramService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.OBJET_MAIL_ABANDON_NOTIF
                );
                final String content = paramService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.TEXTE_MAIL_ABANDON_NOTIF
                );
                sendTemplateMailNotif(norDossier, posteNode, mailAdmin, object, content);

                LOG.info(session, EpgLogEnumImpl.DEL_WS_SPE_TEC, spePublicationDoc);
                session.removeDocument(spePublicationDoc.getRef());

                // Journalisation de l'abandon du rejeu d'envoi de notification dans le dossier
                if (dossierDoc != null) {
                    journalService.journaliserActionAdministration(
                        session,
                        dossierDoc,
                        SolonEpgEventConstant.NOTIF_ABANDON_REJEU_EVENT,
                        SolonEpgEventConstant.NOTIF_ABANDON_REJEU_COMMENT
                    );
                }
            }
            final long endTime = Calendar.getInstance().getTimeInMillis();
            try {
                suiviBatchService.createBatchResultFor(
                    batchLogger,
                    "Notification de première demande de publication : " +
                    spePublication.getTypePublication() +
                    " pour " +
                    spePublication.getWebservice(),
                    endTime - startTime
                );
            } catch (Exception e) {
                LOG.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
            }
        }
        session.save();
        LOGGER.debug("Fin - retryPremiereDemandePublication");
        return newNbError;
    }

    private void callNotifier(
        final Dossier dossier,
        final PosteNode posteNode,
        final String webservice,
        final CoreSession session,
        final String typePublication,
        final String dilaUrl,
        final String userName,
        final String password
    ) {
        final JournalService journalService = STServiceLocator.getJournalService();
        try {
            notifyWebservice(dossier, webservice, session, typePublication, dilaUrl, userName, password);
            journalService.journaliserActionAdministration(
                session,
                dossier.getDocument(),
                SolonEpgEventConstant.NOTIF_REUSSIE_EVENT,
                SolonEpgEventConstant.NOTIF_REUSSIE_COMMENT
            );
        } catch (final Exception e) {
            LOGGER.error("Exception in envoi DemandePublicationWebservice", e);

            try {
                sendFailureMailNotif(dossier, posteNode, webservice, session, typePublication);
            } catch (NuxeoException e1) {
                LOGGER.error("Exception sur l'envoi de notification par mail", e1);
            }

            // Journalisation d'échec d'envoi de notification
            journalService.journaliserActionAdministration(
                session,
                dossier.getDocument(),
                SolonEpgEventConstant.NOTIF_ECHOUEE_EVENT,
                SolonEpgEventConstant.NOTIF_ECHOUEE_COMMENT
            );
        }
        if (dossier.getTypeActe().equals(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES)) {
            InformationsParlementairesService infosParService = SolonEpgServiceLocator.getInformationsParlementairesService();
            WSEvenement wsEvenement;
            try {
                wsEvenement = ((InformationsParlementairesServiceImpl) infosParService).getWsEvenement(session);
                envoiAccuserReception(dossier, session, infosParService, wsEvenement);
            } catch (WSProxyFactoryException e) {
                LOGGER.error(e);
            }
        }
    }

    private void envoiAccuserReception(
        final Dossier dossier,
        final CoreSession session,
        final InformationsParlementairesService infosParService,
        final WSEvenement webservice
    ) {
        Thread t = new Thread(
            () -> {
                try {
                    Thread.sleep(10000);
                    infosParService.callWsAccuserReception(session, dossier, webservice);
                } catch (Exception e) {
                    LOGGER.info("Réponse Webservice AccuserReception KO  Message d'erreur : " + e.getMessage(), e);
                }
            }
        );

        t.start();
    }

    private void sendFailureMailNotif(
        final Dossier dossier,
        final PosteNode posteNode,
        final String webservice,
        final CoreSession session,
        final String typePublication
    ) {
        // Chargement des services
        final STParametreService paramService = STServiceLocator.getSTParametreService();

        // on envoie un mail signalant que l'envoi au webservice de la DILA a échoué
        final String mailAdmin = paramService.getParametreValue(session, STParametreConstant.MAIL_ADMIN_TECHNIQUE);
        final String object = paramService.getParametreValue(
            session,
            SolonEpgParametreConstant.OBJET_MAIL_ECHEC_DEMANDE_PUBLI
        );
        final String content = paramService.getParametreValue(
            session,
            SolonEpgParametreConstant.TEXTE_MAIL_ECHEC_DEMANDE_PUBLI
        );

        sendTemplateMailNotif(dossier.getNumeroNor(), posteNode, mailAdmin, object, content);

        if (posteNode != null) {
            // On créé une demande de publication a re-exécuter plus tard
            final DocumentModel demandePublicationSpeDoc = session.createDocumentModel(
                getWsSpeFolderUnrestricted(session).getPathAsString(),
                "notification-" + posteNode.getId(),
                SolonEpgSchemaConstant.SPE_TYPE
            );
            final WsSpe demandePublicationSpe = demandePublicationSpeDoc.getAdapter(WsSpe.class);

            demandePublicationSpe.setPosteId(posteNode.getId());
            demandePublicationSpe.setWebservice(webservice);
            demandePublicationSpe.setIdDossier(dossier.getDocument().getId());
            demandePublicationSpe.setTypePublication(typePublication);
            session.createDocument(demandePublicationSpeDoc);
            session.saveDocument(demandePublicationSpeDoc);
        }
    }

    private void notifyWebservice(
        final Dossier dossier,
        final String webservice,
        final CoreSession session,
        final String typePublication,
        final String dilaUrl,
        final String userName,
        String value
    )
        throws Exception {
        LOGGER.info("Début de la notification au WebService de publication");
        final WSProxyFactory proxyFactory = new WSProxyFactory(dilaUrl, null, userName, keyAlias);

        final EnvoyerPremiereDemandePERequest request = new EnvoyerPremiereDemandePERequest();
        final PEDemandeType peDemandeType = WsUtil.getTypeDemande(typePublication);
        if (webservice.equals(STWebserviceConstant.ENVOYER_PREMIERE_DEMANDE_PE)) {
            final DossierEpgWithFile dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossierPublication(
                session,
                dossier
            );
            // ajoute le type de publication
            request.setTypeDemande(peDemandeType);
            // ajoute le dossier dans la requete
            request.getDossier().add(dossierEpgWithFile);
        }

        if (peDemandeType == null) {
            throw new NuxeoException("Webservice non notifiable.");
        }

        final WSSpeCaller wsSpeCaller = new WSSpeCaller(proxyFactory.getService(WSSpe.class, value));
        LOGGER.info("Envoi de la requête EnvoyerPremiereDemandePERequest");

        final EnvoyerPremiereDemandePEResponse response = wsSpeCaller.envoyerDemandePublication(request);
        LOGGER.info("Réception de la réponse EnvoyerPremiereDemandePEResponse");
        if (response.getStatus() != PEstatut.OK) {
            LOGGER.info(
                "Réponse Webservice EnvoyerPremiereDemandePE KO  Message d'erreur : " + response.getMessageErreur()
            );
            throw new NuxeoException("Echec d'envoi de la demande de publication / epreuvage.");
        }
        LOGGER.info("Envoi et réception du Webservice EnvoyerPremiereDemandePE effectuée avec succès.");
    }

    /**
     * Récupération du répertoire contenant les demandes de publications qui ont échouées et qui devront être rejouée.
     *
     * @param session
     * @return répertoire contenant les demandes de publications qui ont échouées et qui devront être rejouée.
     */
    private DocumentModel getWsSpeFolderUnrestricted(final CoreSession session) {
        if (wsDemandePublicationFolder == null) {
            final StringBuilder query = new StringBuilder("SELECT * FROM ");
            query.append(SolonEpgSchemaConstant.SPE_FOLDER_TYPE);
            query.append(" where ecm:isProxy = 0 ");
            final DocumentModelList list = new UnrestrictedQueryRunner(session, query.toString()).findAll();
            if (CollectionUtils.isEmpty(list)) {
                throw new NuxeoException("Racine de demandes publications non trouvée");
            } else if (list.size() > 1) {
                throw new NuxeoException("Plusieurs racines de demandes publications trouvées");
            }

            wsDemandePublicationFolder = list.get(0);
        }
        return wsDemandePublicationFolder;
    }

    /**
     * Renvoie vrai si toutes les constantes nécessaire à l'envoi des webservices sont non vide.
     *
     * @return vrai si toutes les constantes nécessaire à l'envoi des webservices sont non vide.
     */
    private boolean checkDilaParameter() {
        // l'url de la Dila est une constante à récupèrer
        final ConfigService configService = STServiceLocator.getConfigService();
        dilaUrl = configService.getValue(SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_URL);
        if (StringUtils.isEmpty(dilaUrl)) {
            LOGGER.error("L'url du serveur de la dila à solliciter pour envoyer les webservices n'est pas défini ! ");
            return false;
        }

        // le nom et le mot de passe de l'utilisateur qui envoi le webservice à la Dila est une constante à récupèrer
        userName = configService.getValue(SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_USER_NAME);
        if (StringUtils.isEmpty(userName)) {
            LOGGER.error("Le nom de l'utilisateur qui envoie le webservice de publication n'est pas défini ! ");
            return false;
        }

        password = configService.getValue(SolonEpgConfigConstant.DILA_WEBSERVICE_PUBLICATION_USER_PASSWORD);
        if (StringUtils.isEmpty(password)) {
            LOGGER.error(
                "Le mot de passe  de l'utilisateur qui envoi le webservice de publication n'est pas défini ! "
            );
            return false;
        }

        // récupère l'alias de la clé SSL si définie
        try {
            keyAlias = configService.getValue(DILA_WEBSERVICE_PUBLICATION_ALIAS_KEY_SSL);
        } catch (final Exception e) {
            LOGGER.warn(String.format("Config key not found: %s", DILA_WEBSERVICE_PUBLICATION_ALIAS_KEY_SSL), e);
            keyAlias = null;
        }
        if (StringUtils.isEmpty(keyAlias)) {
            LOGGER.info("L'alias de la clé SSL pour communiquer avec les webservice de la DILA n'est pas définie !");
            keyAlias = null;
        }

        // récupère le nombre d'essai maximum pour relancer les webservices.
        String nbRetryBeforeDropString;
        try {
            nbRetryBeforeDropString = configService.getValue(DILA_WEBSERVICE_PUBLICATION_NB_RETRY_BEFORE_DROP);
        } catch (final Exception e) {
            LOGGER.warn(String.format("Config key not found: %s", DILA_WEBSERVICE_PUBLICATION_NB_RETRY_BEFORE_DROP), e);
            nbRetryBeforeDropString = null;
        }

        if (StringUtils.isEmpty(nbRetryBeforeDropString)) {
            LOGGER.info(
                "Le nombre d'essai maximum pour relancer les webservices n'est pas définie ! : 3 essais maximum par défaut"
            );
            nbRetryBeforeDrop = 3;
        } else {
            try {
                nbRetryBeforeDrop = Integer.parseInt(nbRetryBeforeDropString);
            } catch (final NumberFormatException e) {
                LOGGER.info(
                    "Le nombre d'essai maximum pour relancer les webservices n'est pas un entier ! : 3 essais maximum par défaut"
                );
                nbRetryBeforeDrop = 3;
            }
        }

        return true;
    }

    @Override
    public void envoiDemandePublicationSuivante(
        final Dossier dossier,
        final CoreSession session,
        final String typePublication,
        final SSRouteStep runningStep
    ) {
        if (dossier == null) {
            LOGGER.error("Type de document non pris en charge par la notification : ");
            return;
        }
        LOGGER.debug("Debut - envoiDemandePublicationSuivante - envoi d'un mail");

        final String posteId = SSServiceLocator
            .getMailboxPosteService()
            .getPosteIdFromMailboxId(runningStep.getDistributionMailboxId());
        final PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteId);
        final STParametreService paramService = STServiceLocator.getSTParametreService();

        // Chargement texte et objet du mail de demande publi suivante
        String objet = paramService.getParametreValue(
            session,
            SolonEpgParametreConstant.OBJET_MAIL_DEMANDE_PUBLI_SUIVANTE
        );
        String texte = paramService.getParametreValue(
            session,
            SolonEpgParametreConstant.TEXTE_MAIL_DEMANDE_PUBLI_SUIVANTE
        );

        // On change la variable nor_dossier pour afficher le nor du dossier
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nor_dossier", dossier.getNumeroNor());
        objet = StringHelper.renderFreemarker(objet, paramMap);
        texte = StringHelper.renderFreemarker(texte, paramMap);

        final List<String> idDossierListSingle = new ArrayList<>();
        idDossierListSingle.add(dossier.getDocument().getId());
        // note optimisation possible : envoi du DocumentModel du Dossier à la place de l'id pour eviter une requête BD
        // envoi du mail avec lien sur le document
        STServiceLocator
            .getSTMailService()
            .sendHtmlMailToUserListWithLinkToDossiers(
                session,
                posteNode.getUserList(),
                objet,
                texte,
                idDossierListSingle
            );
        LOGGER.debug("Fin - envoiDemandePublicationSuivante - envoi d'un mail");
    }

    /**
     * Envoi d'une notification de publication : échec 1ere demande, échec rejeu, abandon Attention : utilisation de
     * template pour le mail envoyé, variable ${nor_dossier}
     */
    private void sendTemplateMailNotif(
        final String norDossier,
        final PosteNode posteNode,
        final String mailAdmin,
        final String object,
        final String content
    ) {
        final List<STUser> stUserList = posteNode.getUserList();
        final List<Address> recipients = new ArrayList<>();
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nor_dossier", norDossier);

        for (final STUser user : stUserList) {
            final String mailAdress = user.getEmail();
            if (StringUtils.isNotEmpty(mailAdress)) {
                Address address;
                try {
                    address = new InternetAddress(mailAdress);
                } catch (final AddressException e) {
                    throw new NuxeoException(e);
                }
                recipients.add(address);
            }
        }
        if (mailAdmin.isEmpty()) {
            Address address;
            try {
                address = new InternetAddress(mailAdmin);
            } catch (final AddressException e) {
                throw new NuxeoException(e);
            }
            recipients.add(address);
        }

        STServiceLocator.getSTMailService().sendTemplateHtmlMail(recipients, object, content, paramMap);
    }
}
