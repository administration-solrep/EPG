package fr.dila.solonepg.rest.management;

import static fr.dila.solonepg.api.constant.RetourDilaConstants.RETOUR_DILA_SCHEMA;
import static fr.dila.st.api.constant.STSchemaConstant.COMMON_SCHEMA;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.InformationsParlementairesService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.rest.api.SpeDelegate;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.xsd.solon.epg.TypeModification;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.EnvoyerRetourPERequest;
import fr.sword.xsd.solon.spe.EnvoyerRetourPEResponse;
import fr.sword.xsd.solon.spe.PEActeRetourBo;
import fr.sword.xsd.solon.spe.PEActeRetourJo;
import fr.sword.xsd.solon.spe.PEDemandeType;
import fr.sword.xsd.solon.spe.PEFichier;
import fr.sword.xsd.solon.spe.PERetourEpreuvage;
import fr.sword.xsd.solon.spe.PERetourGestion;
import fr.sword.xsd.solon.spe.PERetourPublicationBo;
import fr.sword.xsd.solon.spe.PERetourPublicationJo;
import fr.sword.xsd.solon.spe.PEstatut;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;

/**
 * Permet de gerer toutes les operations sur spe.
 */
public class SpeDelegateImpl implements SpeDelegate {
    protected CoreSession session;

    public static final String ERROR_READ_WRITE_MSG = "Vous n'avez pas les droits pour voir ou modifier ce dossier";

    /**
     * Logger.
     */
    private static final STLogger LOGGER = STLogFactory.getLog(SpeDelegateImpl.class);

    public SpeDelegateImpl(CoreSession documentManager) {
        session = documentManager;
    }

    @Override
    public EnvoyerPremiereDemandePEResponse envoyerPremiereDemandePE(EnvoyerPremiereDemandePERequest request) {
        // note : stub utilisé pour tester dans qa et via selenium l'envoi des demandes de publication à la DILA

        // initialisation variable réponse
        EnvoyerPremiereDemandePEResponse envoyerPremiereDemandePEResponse = new EnvoyerPremiereDemandePEResponse();

        // log l'action dans le journal d'administration
        logWebServiceAction(
            SolonEpgEventConstant.WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_EVENT,
            SolonEpgEventConstant.WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_COMMENT_PARAM
        );
        envoyerPremiereDemandePEResponse.setStatus(PEstatut.OK);
        return envoyerPremiereDemandePEResponse;
    }

    @Override
    public EnvoyerRetourPEResponse envoyerRetourPE(EnvoyerRetourPERequest request) {
        // récupération des informations de la requete
        PEDemandeType demandeType = request.getType();
        LOGGER.info(
            STLogEnumImpl.START_EVENT_TEC,
            "initialisation envoyerRetourPE service : type de demande = '" + demandeType.toString() + "'"
        );

        // initialisation variable réponse
        EnvoyerRetourPEResponse envoyerRetourPEResponse = new EnvoyerRetourPEResponse();
        // par defaut, on considère que la réponse est en échec
        envoyerRetourPEResponse.setStatus(PEstatut.KO);
        // verification des droits de l'utilisateur pour le service
        List<String> userGroupList = ((SSPrincipal) session.getPrincipal()).getGroups();
        if (!userGroupList.contains(STWebserviceConstant.ENVOYER_RETOUR_PE)) {
            envoyerRetourPEResponse.setMessageErreur("Vous n'avez pas les droits pour accéder à ce service !");
            return envoyerRetourPEResponse;
        }

        final NORService norService = SolonEpgServiceLocator.getNORService();
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        final List<DocumentModel> dossiersList = new ArrayList<>();
        final List<String> norNonTrouve = new ArrayList<>();
        // on détermine la type de demande
        if (demandeType.equals(PEDemandeType.EPREUVAGE)) {
            PERetourEpreuvage peRetourEpreuvage = request.getRetourEpreuvage();

            // récupération du dossier
            String nor = checkNor(peRetourEpreuvage.getNor());
            DocumentModel dossierDoc = null;

            try {
                dossierDoc = norService.getDossierFromNOR(session, nor, COMMON_SCHEMA, RETOUR_DILA_SCHEMA);
                dossiersList.add(dossierDoc);
            } catch (NuxeoException e) {
                LOGGER.error(STLogEnumImpl.FAIL_GET_DOSSIER_TEC, e, "Erreur lors de la récupération du dossier " + nor);
                envoyerRetourPEResponse.setMessageErreur("Erreur lors de la récupération du dossier " + nor);
                return envoyerRetourPEResponse;
            }

            if (dossierDoc == null) {
                LOGGER.error(STLogEnumImpl.DEFAULT, "Echec de récupération du dossier avec le nor : '" + nor);
                envoyerRetourPEResponse.setMessageErreur("Echec de récupération du dossier avec le nor : '" + nor);
                return envoyerRetourPEResponse;
            }

            // création des fichiers dans le répertoire du fdd epreuve
            PEFichier epreuveFichier = peRetourEpreuvage.getEpreuve();
            if (epreuveFichier != null) {
                try {
                    WsUtil.createEpreuveFileInParapheur(
                        session,
                        dossierDoc,
                        epreuveFichier,
                        Optional.ofNullable(peRetourEpreuvage.getCompare())
                    );
                } catch (NuxeoException e) {
                    LOGGER.error(
                        STLogEnumImpl.CREATE_FILE_TEC,
                        e,
                        "Erreur lors de la création des fichiers de l'Epreuvage"
                    );
                    envoyerRetourPEResponse.setMessageErreur("Erreur lors de la création des fichiers de l'Epreuvage");
                    return envoyerRetourPEResponse;
                }
            } else {
                LOGGER.error(
                    STLogEnumImpl.DEFAULT,
                    "Erreur lors du Web service Retour pour epreuvage, un ou plusieurs documents manquants"
                );
                envoyerRetourPEResponse.setMessageErreur("Un ou plusieurs documents manquants");
                return envoyerRetourPEResponse;
            }
            // note : le dossier n'est pas publié dans le cas d'un dossier pour epreuve
            envoyerRetourPEResponse.setStatus(PEstatut.OK);
            // log l'action dans le journal d'administration
            logWebServiceActionDossier(
                SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT,
                SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM,
                dossierDoc.getAdapter(Dossier.class)
            );
        } else if (demandeType.equals(PEDemandeType.PUBLICATION_JO)) {
            PERetourPublicationJo peRetourPublication = request.getRetourPublicationJo();
            final List<PEActeRetourJo> pEActeRetourList = peRetourPublication.getActe();
            if (CollectionUtils.isNotEmpty(pEActeRetourList)) {
                // récupération des informations communes
                PERetourGestion peRetourGestion = peRetourPublication.getGestion();
                final Calendar dateParutionJorf = peRetourGestion.getDateParution().toGregorianCalendar();
                final List<String> norErreurPublication = new ArrayList<>();

                // on ouvre une unrestricted session pour mettre à jour les droits d'indexation à la publication
                new UnrestrictedSessionRunner(session) {

                    @Override
                    public void run() {
                        for (PEActeRetourJo peActeRetour : pEActeRetourList) {
                            String nor = peActeRetour.getNor();
                            try {
                                DocumentModel dossierDoc = norService.getDossierFromNOR(
                                    session,
                                    nor,
                                    COMMON_SCHEMA,
                                    RETOUR_DILA_SCHEMA
                                );
                                if (dossierDoc == null) {
                                    norNonTrouve.add(nor);
                                } else {
                                    dossiersList.add(dossierDoc);
                                    // maj des données parutions Jorf
                                    RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);
                                    retourDila.setDateParutionJorf(dateParutionJorf);

                                    if (StringUtils.isNotEmpty(peActeRetour.getNumeroTexte())) {
                                        retourDila.setNumeroTexteParutionJorf(peActeRetour.getNumeroTexte());
                                    }

                                    Dossier dossier = retourDila.getDocument().getAdapter(Dossier.class);
                                    if (StringUtils.isNotEmpty(peActeRetour.getNumeroSommaire())) {
                                        dossier.setBaseLegaleNumeroTexte(peActeRetour.getNumeroSommaire());
                                    }

                                    retourDila.setTitreOfficiel(peActeRetour.getTitreOfficiel());

                                    // Passe le dossier à l'état publié si ce n'est pas déjà le cas
                                    if (!dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                                        dossierService.publierDossier(dossier, session, true);
                                        dossier.save(session);
                                    } else {
                                        SolonEpgServiceLocator
                                            .getActiviteNormativeService()
                                            .setPublicationInfo(retourDila, session);
                                        dossier.save(session);
                                    }
                                    // log l'action dans le journal d'administration
                                    logWebServiceActionDossier(
                                        SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT,
                                        SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM,
                                        dossier
                                    );
                                    if (
                                        dossier
                                            .getTypeActe()
                                            .equals(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES)
                                    ) {
                                        // Appel du WebService TransmissionDatePublicationJO de EPP
                                        InformationsParlementairesService infosParService = SolonEpgServiceLocator.getInformationsParlementairesService();
                                        try {
                                            infosParService.callWsCreerVersionEvt48(session, dossier);
                                        } catch (Exception e) {
                                            throw new NuxeoException(e.getMessage());
                                        }
                                    }
                                    if (dossierService.hasEtapeAvisCE(session, dossier)) {
                                        // Création d'un nouveau jeton car on est dans étape pour Avis CE
                                        final JetonService jetonService = STServiceLocator.getJetonService();
                                        if (dossier != null) {
                                            jetonService.addDocumentInBasket(
                                                session,
                                                STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
                                                TableReference.MINISTERE_CE,
                                                dossierDoc,
                                                dossier.getNumeroNor(),
                                                TypeModification.PUBLICATION.name(),
                                                null
                                            );
                                        }
                                    }
                                }
                            } catch (NuxeoException e) {
                                LOGGER.error(STLogEnumImpl.DEFAULT, e, "Erreur lors de la publication JO");
                                norErreurPublication.add(nor);
                            }
                        }
                    }
                }
                .runUnrestricted();

                if (CollectionUtils.isNotEmpty(norErreurPublication)) {
                    envoyerRetourPEResponse.setStatus(PEstatut.KO);
                    envoyerRetourPEResponse.setMessageErreur(
                        "Erreur lors de la publication JO sur le(s) dossier(s) : " +
                        StringUtils.join(norErreurPublication, ", ")
                    );
                    return envoyerRetourPEResponse;
                }
                if (pEActeRetourList.size() == 1 && !norNonTrouve.isEmpty()) {
                    envoyerRetourPEResponse.setStatus(PEstatut.KO);
                } else {
                    envoyerRetourPEResponse.setStatus(PEstatut.OK);
                }
            }
        } else if (demandeType.equals(PEDemandeType.PUBLICATION_BO)) {
            PERetourPublicationBo peRetourPublication = request.getRetourPublicationBo();
            final List<PEActeRetourBo> pEActeRetourBoList = peRetourPublication.getActe();
            if (CollectionUtils.isNotEmpty(pEActeRetourBoList)) {
                // récupération des informations communes
                PERetourGestion peRetourGestion = peRetourPublication.getGestion();
                final Calendar dateParutionBo = peRetourGestion.getDateParution().toGregorianCalendar();

                // on ouvre une unrestricted session pour mettre à jour les droits d'indexation à la publication
                try {
                    new UnrestrictedSessionRunner(session) {

                        @Override
                        public void run() {
                            for (PEActeRetourBo peActeRetour : pEActeRetourBoList) {
                                String nor = peActeRetour.getNor();
                                DocumentModel dossierDoc = norService.getDossierFromNOR(
                                    session,
                                    nor,
                                    COMMON_SCHEMA,
                                    RETOUR_DILA_SCHEMA
                                );
                                if (dossierDoc == null) {
                                    norNonTrouve.add(nor);
                                } else {
                                    // maj des données parutions Bo
                                    dossiersList.add(dossierDoc);
                                    RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);
                                    List<ParutionBo> parutionBoList = retourDila.getParutionBo();
                                    if (parutionBoList == null) {
                                        parutionBoList = new ArrayList<>();
                                    }

                                    ParutionBo parutionBo = new ParutionBoImpl();
                                    parutionBo.setDateParutionBo(dateParutionBo);
                                    if (StringUtils.isNotEmpty(peActeRetour.getNumeroTexte())) {
                                        parutionBo.setNumeroTexteParutionBo(peActeRetour.getNumeroTexte());
                                    }
                                    parutionBo.setPageParutionBo((long) peActeRetour.getPage());

                                    parutionBoList.add(parutionBo);
                                    retourDila.setParutionBo(parutionBoList);

                                    // Passe le dossier à l'état publié si ce n'est pas déjà le cas
                                    Dossier dossier = retourDila.getDocument().getAdapter(Dossier.class);
                                    if (!dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                                        dossierService.publierDossier(dossier, session, true);
                                    } else {
                                        dossier.save(session);
                                    }
                                    // log l'action dans le journal d'administration
                                    logWebServiceActionDossier(
                                        SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT,
                                        SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM,
                                        dossier
                                    );
                                }
                            }
                        }
                    }
                    .runUnrestricted();
                } catch (NuxeoException e) {
                    LOGGER.error(STLogEnumImpl.DEFAULT, e, "Erreur lors de la publication BO");
                    envoyerRetourPEResponse.setMessageErreur("Erreur lors de la publication BO " + e.getMessage());
                    return envoyerRetourPEResponse;
                }
                if (pEActeRetourBoList.size() == 1 && !norNonTrouve.isEmpty()) {
                    envoyerRetourPEResponse.setStatus(PEstatut.KO);
                } else {
                    envoyerRetourPEResponse.setStatus(PEstatut.OK);
                }
            }
        }

        // Gestion des erreurs
        final List<DocumentModel> dossierLinkDocEnErreurList = new ArrayList<>();

        // récupère le dossierLink lié au dossier
        try {
            new UnrestrictedSessionRunner(session) {

                @Override
                public void run() {
                    for (DocumentModel dossierDoc : dossiersList) {
                        DocumentModel dossierLinkDoc = null;
                        List<DocumentModel> dossierLinkDocList = null;
                        try {
                            dossierLinkDocList =
                                SolonEpgServiceLocator
                                    .getCorbeilleService()
                                    .findDossierLink(session, dossierDoc.getId());
                        } catch (Exception e) {
                            LOGGER.error(
                                STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC,
                                e,
                                "erreur lors de la récupération des dossiers link liés au dossier"
                            );
                        }

                        if (dossierLinkDocList != null) {
                            for (DocumentModel dossierLinkDocElement : dossierLinkDocList) {
                                // on verifie que le dossierLink est bien à l'étap "pour publication pu pour euprevage"
                                DossierLink dossierLink = dossierLinkDocElement.getAdapter(DossierLink.class);
                                if (
                                    VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(
                                        dossierLink.getRoutingTaskType()
                                    ) ||
                                    VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(
                                        dossierLink.getRoutingTaskType()
                                    ) ||
                                    VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(
                                        dossierLink.getRoutingTaskType()
                                    )
                                ) {
                                    dossierLinkDoc = dossierLinkDocElement;
                                    break;
                                }
                            }
                        }

                        if (dossierLinkDoc != null) {
                            try {
                                SolonEpgServiceLocator
                                    .getDossierDistributionService()
                                    .validerEtape(
                                        session,
                                        dossierDoc,
                                        dossierLinkDoc,
                                        SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_VALIDE_AUTOMATIQUEMENT_VALUE
                                    );
                            } catch (Exception e) {
                                dossierLinkDocEnErreurList.add(dossierDoc);
                                LOGGER.error(STLogEnumImpl.DEFAULT, e, "erreur lors de la validation de l'etape : ");
                            }
                        }
                    }
                }
            }
            .runUnrestricted();
        } catch (NuxeoException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e, "Erreur lors de la gestion du dossier link");
        }

        // En cas d'erreur, envoi de mail
        if (CollectionUtils.isNotEmpty(dossierLinkDocEnErreurList)) {
            try {
                envoyerRetourPEResponse.setStatus(PEstatut.KO);
                String mailContent;
                String mailObject;
                String email;
                List<STUser> users;
                List<String> emailsList;
                Map<String, Object> param;
                users =
                    STServiceLocator
                        .getProfileService()
                        .getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
                emailsList = new ArrayList<>();
                for (STUser user : users) {
                    email = user.getEmail();
                    if (email != null && !email.isEmpty()) {
                        emailsList.add(email);
                    }
                }

                StringBuilder dossierNorBuilder = new StringBuilder();
                if (!emailsList.isEmpty()) {
                    for (final DocumentModel doc : dossierLinkDocEnErreurList) {
                        final Dossier dossier = doc.getAdapter(Dossier.class);
                        if (dossierNorBuilder.length() != 0) {
                            dossierNorBuilder.append(",");
                        }
                        dossierNorBuilder.append(dossier.getNumeroNor());
                    }
                    // Envoyer Un seul Mail pour tous les dossier en erreur
                    final STParametreService paramService = STServiceLocator.getSTParametreService();
                    mailContent =
                        paramService.getParametreValue(
                            session,
                            SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_TEXTE
                        );
                    mailObject =
                        paramService.getParametreValue(
                            session,
                            SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_OBJET
                        );
                    param = new HashMap<>();
                    param.put("NOR", dossierNorBuilder.toString());
                    STServiceLocator.getSTMailService().sendTemplateMail(emailsList, mailObject, mailContent, param);
                }
            } catch (NuxeoException ce) {
                LOGGER.error(
                    STLogEnumImpl.FAIL_SEND_MAIL_TEC,
                    ce,
                    "Erreur lors de l'envoi de mail d'erreur validation etape."
                );
            }
        }

        return envoyerRetourPEResponse;
    }

    /**
     * Logge l'action effectue par webservice dans le journal d'administration.
     */
    protected void logWebServiceAction(String name, String comment) {
        // log du webservice dans le journal d'administration
        EventProducer producer = STServiceLocator.getEventProducer();
        // création de l'évenement
        final EventContextImpl envContext = new EventContextImpl(session, session.getPrincipal());
        envContext.setProperty("comment", comment);
        // on lance un evenement
        try {
            producer.fireEvent(envContext.newEvent(name));
        } catch (NuxeoException e) {
            LOGGER.error(
                STLogEnumImpl.FAIL_SEND_EVENT_TEC,
                e,
                "erreur lors de l'envoi d'un log pour le journal technique. Nom de l'event" + name
            );
        }
    }

    /**
     * Log l'action effectuée par webservice dans le journal du dossier
     *
     * @param name
     *            nom de l'évènement de log
     * @param comment
     *            commentaire qui sera rédigé dans le log
     * @param dossier
     *            dossier auquel on ajoute le log
     */
    protected void logWebServiceActionDossier(String name, String comment, Dossier dossier) {
        try {
            JournalService journalService = STServiceLocator.getJournalService();
            journalService.journaliserActionAdministration(session, dossier.getDocument(), name, comment);
        } catch (Exception e) {
            LOGGER.error(
                STLogEnumImpl.FAIL_SEND_EVENT_TEC,
                e,
                "erreur lors de l'envoi d'un log pour le journal du dossier. Nom de l'event" + name
            );
        }
    }

    /**
     * Vérifie la validité d'un NOR.
     *
     * @param nor NOR à vérifier.
     * @return
     */
    private String checkNor(String nor) {
        final NORService norService = SolonEpgServiceLocator.getNORService();
        if (nor != null && !norService.isStructNorValide(nor)) {
            throw new EPGException(String.format("Le format du NOR [%s] est invalide", nor));
        }
        return nor;
    }
}
