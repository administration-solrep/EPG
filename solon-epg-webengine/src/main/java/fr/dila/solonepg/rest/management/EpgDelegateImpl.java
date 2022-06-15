/**
 *
 */
package fr.dila.solonepg.rest.management;

import static fr.dila.solonepg.api.constant.ConseilEtatConstants.CONSEIL_ETAT_SCHEMA;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.fonddossier.FondDeDossierInstance;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.filter.RouteStepValideFilter;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.rest.api.EpgDelegate;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.caselink.STDossierLink;
import fr.dila.st.api.constant.MediaType;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.jeton.JetonDoc;
import fr.dila.st.api.jeton.JetonServiceDto;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteStep;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epg.Acte;
import fr.sword.xsd.solon.epg.ActeType;
import fr.sword.xsd.solon.epg.AttribuerNorRequest;
import fr.sword.xsd.solon.epg.AttribuerNorResponse;
import fr.sword.xsd.solon.epg.ChercherDossierRequest;
import fr.sword.xsd.solon.epg.ChercherDossierResponse;
import fr.sword.xsd.solon.epg.ChercherModificationDossierRequest;
import fr.sword.xsd.solon.epg.ChercherModificationDossierResponse;
import fr.sword.xsd.solon.epg.CodeErreur;
import fr.sword.xsd.solon.epg.CreerDossierRequest;
import fr.sword.xsd.solon.epg.CreerDossierResponse;
import fr.sword.xsd.solon.epg.DonnerAvisCERequest;
import fr.sword.xsd.solon.epg.DonnerAvisCEResponse;
import fr.sword.xsd.solon.epg.DossierEpg;
import fr.sword.xsd.solon.epg.DossierEpgWithFile;
import fr.sword.xsd.solon.epg.DossierModification;
import fr.sword.xsd.solon.epg.Fichier;
import fr.sword.xsd.solon.epg.InformationPublication;
import fr.sword.xsd.solon.epg.ListeFichiers;
import fr.sword.xsd.solon.epg.ModifierDossierCERequest;
import fr.sword.xsd.solon.epg.ModifierDossierCEResponse;
import fr.sword.xsd.solon.epg.ModifierDossierRequest;
import fr.sword.xsd.solon.epg.ModifierDossierResponse;
import fr.sword.xsd.solon.epg.ResultatRechercheInfructueuse;
import fr.sword.xsd.solon.epg.SectionCe;
import fr.sword.xsd.solon.epg.TypeModification;
import fr.sword.xsd.solon.epg.TypeValidationCe;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Permet de gerer toutes les operations sur epg
 */
public class EpgDelegateImpl implements EpgDelegate {
    private static final String ERROR_MSG_NOR_NON_TROUVE = "Nor non trouvé";

    private static final String ERROR_TYPE_ACTE_INCOMPATIBLE_AVIS_CE =
        "Le type d'acte ne peut pas recevoir d'avis du Conseil d'Etat";

    /**
     * Logger.
     */
    private static final STLogger LOGGER = STLogFactory.getLog(EpgDelegateImpl.class);

    private static final Map<ActeType, String> CREER_DOSSIER_RIGHTS = new HashMap<>();

    private static final Map<ActeType, String> MODIFIER_DOSSIER_RIGHTS = new HashMap<>();

    private static final String WS_ACCESS_DENIED = "Vous n'avez pas les droits pour accéder à ce service !";
    private static final String WS_MESURE_NOMINATIVE_FORBIDDEN =
        "Vous n'avez pas les droits pour créer une mesure nominative !";
    private static final String WS_JETON_DOSSIERS_NOT_FOUND =
        "Erreur lors de la récupération des dossiers via le jeton";
    private static final Pattern CODE_ENTITE_OR_DIRECTION_PATTERN = Pattern.compile("^[a-zA-Z]+$");

    protected CoreSession session;

    public static final String ERROR_READ_WRITE_MSG = "Vous n'avez pas les droits pour voir ou modifier ce dossier";
    public static final String ERROR_GET_POSTE = "Erreur lors de la récupération du poste du ministère : ";
    public static final String ERROR_FIND_POSTE = "Pas de poste identifié pour ce ministère : ";

    private static final String AVIS_RECTIFICATIF = "AVIS_RECTIFICATIF";

    static {
        CREER_DOSSIER_RIGHTS.put(ActeType.AVIS, STWebserviceConstant.CREER_DOSSIER_AVIS);
        MODIFIER_DOSSIER_RIGHTS.put(ActeType.AVIS, STWebserviceConstant.MODIFIER_DOSSIER_AVIS);
        CREER_DOSSIER_RIGHTS.put(ActeType.DECRET_PR_INDIVIDUEL, STWebserviceConstant.CREER_DOSSIER_DECRET_PR_IND);
        MODIFIER_DOSSIER_RIGHTS.put(ActeType.DECRET_PR_INDIVIDUEL, STWebserviceConstant.MODIFIER_DOSSIER_DECRET_PR_IND);
        CREER_DOSSIER_RIGHTS.put(ActeType.INFORMATIONS_PARLEMENTAIRES, STWebserviceConstant.CREER_DOSSIER_INFOS_PARL);
        MODIFIER_DOSSIER_RIGHTS.put(
            ActeType.INFORMATIONS_PARLEMENTAIRES,
            STWebserviceConstant.MODIFIER_DOSSIER_INFOS_PARL
        );
    }

    public EpgDelegateImpl(final CoreSession documentManager) {
        session = documentManager;
    }

    /**
     * Vérification des droits nécessaires pour le WS attribuerNor.
     * @param attribuerNorResponse
     * @return
     */
    private boolean checkDroitsAttribuerNor(
        final AttribuerNorResponse attribuerNorResponse,
        final ActeService acteService,
        final String typeActe
    ) {
        final List<String> userGroupList = ((SSPrincipal) session.getPrincipal()).getGroups();
        if (!userGroupList.contains(STWebserviceConstant.ATTRIBUER_NOR)) {
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur(WS_ACCESS_DENIED);
            return false;
        }

        // On vérifie que l'utilisateur a le droit de créer des mesures
        // nominatives
        if (
            acteService.hasTypeActeMesureNominative(typeActe) &&
            !userGroupList.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER)
        ) {
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.DROIT_ACCESS_INSUFFISANT);
            attribuerNorResponse.setMessageErreur(WS_MESURE_NOMINATIVE_FORBIDDEN);
            return false;
        }

        return true;
    }

    @Override
    public AttribuerNorResponse attribuerNor(final AttribuerNorRequest request) {
        // appel des services utilisés
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        final EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        final NORService norService = SolonEpgServiceLocator.getNORService();

        // récupération des informations de la requete
        final ActeType acteType = request.getTypeActe();
        final String codeEntite = checkCodeEntite(request.getCodeEntite());
        final String codeDirection = checkCodeDirection(request.getCodeDirection());

        LOGGER.info(
            session,
            EpgLogEnumImpl.INIT_WS_ATTRIBUER_NOR_TEC,
            "type acte = '" +
            acteType +
            "', code entite = '" +
            codeEntite +
            "', code direction = '" +
            codeDirection +
            "'"
        );

        // initialisation de la réponse
        final AttribuerNorResponse attribuerNorResponse = new AttribuerNorResponse();

        // récupération type Acte à partir de l'ActeType ws
        final String typeActe = WsUtil.getDossierActeTypeFromWsTypeActe(acteType);

        if (!checkDroitsAttribuerNor(attribuerNorResponse, acteService, typeActe)) {
            return attribuerNorResponse;
        }

        // On ne traite pas le cas de l'acte conv. collective parce qu'il n'est
        // pas géré non plus dans le cas de la création de dossier simple
        String nature = VocabularyConstants.NATURE_REGLEMENTAIRE;
        if (acteService.isNonReglementaire(typeActe)) {
            nature = VocabularyConstants.NATURE_NON_REGLEMENTAIRE;
        }

        // récupère le ministere à partir des 3 lettres nor du ministère
        EntiteNode ministereEntiteNode = null;
        try {
            ministereEntiteNode = epgOrganigrammeService.getMinistereFromNor(codeEntite);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_MINISTERE_TEC, codeEntite, e);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur("Erreur lors de la récupération du ministère :" + e.getMessage());
            return attribuerNorResponse;
        }
        if (ministereEntiteNode == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_MINISTERE_TEC, "Code ministère inconnu " + codeEntite);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.ENTITE_INCONNUE);
            return attribuerNorResponse;
        }

        // on vérifie que ce ministère correspond bien au ministère de l'utilisateur technique webservice
        final SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        final String ministereRespFinal = ministereEntiteNode.getId();
        final Set<String> ministereIdsUser = ssPrincipal.getMinistereIdSet();
        if (ministereIdsUser == null || !ministereIdsUser.contains(ministereRespFinal)) {
            LOGGER.warn(
                session,
                STLogEnumImpl.FAIL_CREATE_DOSSIER_TEC,
                "Droit accès insuffisant pour créer un dossier dans ce ministère"
            );
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.DROIT_ACCESS_INSUFFISANT);
            attribuerNorResponse.setMessageErreur("Vous n'avez pas le droit de créer un dossier dans ce ministère !");
            return attribuerNorResponse;
        }

        // récupère la direction à partir de la lettres nor de la direction et du ministère
        UniteStructurelleNode epgUniteStructurelleNode;
        try {
            epgUniteStructurelleNode =
                epgOrganigrammeService.getDirectionFromMinistereAndNor(ministereEntiteNode, codeDirection);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, codeDirection, e);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur("Erreur lors de la récupération de la  direction :" + e.getMessage());
            return attribuerNorResponse;
        }
        if (epgUniteStructurelleNode == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, "Code direction inconnu" + codeDirection);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.DIRECTION_INCONNUE);
            return attribuerNorResponse;
        }
        final String directionRespFinal = epgUniteStructurelleNode.getId();

        OrganigrammeNode poste;
        Set<String> postesId = ssPrincipal.getPosteIdSet();
        try {
            if (postesId.size() == 1) {
                poste = STServiceLocator.getSTPostesService().getPoste(postesId.iterator().next());
            } else {
                poste = epgOrganigrammeService.getOrganigrammeNodeById(ministereRespFinal, OrganigrammeType.POSTE);
            }
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_POSTE_TEC, ERROR_GET_POSTE + ministereRespFinal, e);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur(ERROR_GET_POSTE + e.getMessage());
            return attribuerNorResponse;
        }
        if (poste == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_POSTE_TEC, ERROR_FIND_POSTE + ministereEntiteNode.getLabel());
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur(ERROR_FIND_POSTE + ministereEntiteNode.getLabel());
            return attribuerNorResponse;
        }
        final String posteId = poste.getId();

        // récupération de la mailbox du poste de l'administrateur ministériel du ministère (bdc)
        Mailbox currentMailbox = null;
        try {
            currentMailbox = mailboxPosteService.getMailboxPoste(session, posteId);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_MAILBOX_TEC, posteId, e);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur(
                "Erreur lors de la récupération de la mailbox de l'admin ministériel du poste :" + e.getMessage()
            );
            return attribuerNorResponse;
        }
        if (currentMailbox == null) {
            LOGGER.warn(
                session,
                STLogEnumImpl.FAIL_GET_MAILBOX_TEC,
                "La mailbox du poste n'est pas initialisée : " + poste.getLabel()
            );
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur(
                "La mailbox du poste bdc n'est pas initialisée : " + poste.getLabel()
            );
            return attribuerNorResponse;
        }
        // Détermine les lettres de NOR du type d'acte
        final TypeActe acte = acteService.getActe(typeActe);
        final String norActe = acte.getNor();

        // Attribution du NOR
        String numeroNorDossier;
        try {
            numeroNorDossier = norService.createNOR(norActe, codeEntite, codeDirection);
        } catch (final NuxeoException e) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_CREATE_NOR_TEC, e);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur("Erreur lors de la création du nor :" + e.getMessage());
            return attribuerNorResponse;
        }

        // Initialisation du dossier
        DocumentModel dossierDoc;
        Dossier dossier;
        try {
            dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
            DublincoreSchemaUtils.setTitle(dossierDoc, numeroNorDossier);
            dossier = dossierDoc.getAdapter(Dossier.class);

            // Ajout des informations de création information du dossier
            dossier.setNumeroNor(numeroNorDossier);
            dossier.setTypeActe(typeActe);
            dossier.setMinistereResp(ministereRespFinal);
            dossier.setDirectionResp(directionRespFinal);
            // Ajout des informations de création
            dossier.setCategorieActe(nature);

            // Crée le dossier
            dossier = dossierService.createDossierWs(session, dossier.getDocument(), posteId, currentMailbox);

            // on renvoie la réponse
            attribuerNorResponse.setNor(dossier.getNumeroNor());
            LOGGER.info(
                session,
                STLogEnumImpl.CREATE_DOSSIER_TEC,
                dossier.getNumeroNor() + "' créé par le web service attribuerNor"
            );
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_DOSSIER_TEC, e);
            attribuerNorResponse.setStatut(TraitementStatut.KO);
            attribuerNorResponse.setCodeErreur(CodeErreur.AUTRE);
            attribuerNorResponse.setMessageErreur("Erreur lors de la création du dossier :" + e.getMessage());
            return attribuerNorResponse;
        }

        attribuerNorResponse.setStatut(TraitementStatut.OK);

        // log l'action dans le journal d'administration
        logWebServiceActionDossier(
            SolonEpgEventConstant.WEBSERVICE_ATTRIBUER_NOR_EVENT,
            SolonEpgEventConstant.WEBSERVICE_ATTRIBUER_NOR_COMMENT_PARAM,
            dossier.getDocument()
        );

        return attribuerNorResponse;
    }

    @Override
    public ChercherDossierResponse chercherDossierEpg(final ChercherDossierRequest request) {
        final JetonService jetonService = SolonEpgServiceLocator.getJetonService();

        // récupération des informations de la requete
        final String jetonRecu = request.getJeton();
        final List<String> norList = checkNors(request.getNor());

        LOGGER.info(
            session,
            EpgLogEnumImpl.INIT_WS_CHERCHER_DOSSIER,
            "jeton = '" + jetonRecu + "', liste nors = '" + norList + "'"
        );

        // initialisation de la variable réponse
        final ChercherDossierResponse chercherDossierResponse = new ChercherDossierResponse();
        // par defaut, le statut est à KO
        chercherDossierResponse.setStatut(TraitementStatut.KO);
        // verification des droits de l'utilisateur pour le service
        final List<String> userGroupList = ((SSPrincipal) session.getPrincipal()).getGroups();
        if (!userGroupList.contains(STWebserviceConstant.CHERCHER_DOSSIER)) {
            chercherDossierResponse.setMessageErreur(WS_ACCESS_DENIED);
            return chercherDossierResponse;
        }

        // par defaut, "Dernier envoi" est à "vrai" et "jeton" est à "-1" et le statut est à KO
        chercherDossierResponse.setDernierEnvoi(true);
        chercherDossierResponse.setJeton("-1");
        if (CollectionUtils.isEmpty(norList) && StringUtils.isEmpty(jetonRecu)) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_PARAM_TEC, "Aucun numero NOR ni jeton passé en paramètre");
            chercherDossierResponse.setDernierEnvoi(true);
            chercherDossierResponse.setMessageErreur("aucun numero NOR ni jeton passé en paramètre");
            return chercherDossierResponse;
        }

        final List<String> norNonTrouveList = new ArrayList<>();

        // Liste des nors de dossiers renvoyés
        final List<String> norTrouveList = new ArrayList<>();
        // Liste des dossiers
        List<DocumentModel> docList = null;

        if (StringUtils.isNotEmpty(jetonRecu)) {
            // on recupère les dossiers via le jeton
            final Long jeton = Long.parseUnsignedLong(jetonRecu);
            JetonServiceDto jetonServiceDto;
            try {
                jetonServiceDto =
                    jetonService.getDocuments(
                        session,
                        TableReference.MINISTERE_CE,
                        jeton,
                        STWebserviceConstant.CHERCHER_DOSSIER
                    );
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, jeton, e);
                chercherDossierResponse.setMessageErreur(WS_JETON_DOSSIERS_NOT_FOUND);
                return chercherDossierResponse;
            } catch (final OutOfMemoryError err) {
                sendOutOfMemoryMail(err, jetonRecu);
                chercherDossierResponse.setMessageErreur(WS_JETON_DOSSIERS_NOT_FOUND);
                return chercherDossierResponse;
            }

            // récupération jetonServiceDto
            if (jetonServiceDto == null) {
                LOGGER.warn(session, STLogEnumImpl.FAIL_GET_JETON_TEC, jeton);
                chercherDossierResponse.setMessageErreur(
                    "Erreur : Impossible de récupérer des documents. Veuillez saisir un jeton adéquat."
                );
                return chercherDossierResponse;
            }
            // récupération jeton sortant
            String jetonSortant = Long.toString(jeton);
            if (jetonServiceDto.getNextJetonNumber() != null) {
                jetonSortant = jetonServiceDto.getNextJetonNumber().toString();
            }
            chercherDossierResponse.setJeton(jetonSortant);

            // récupération dernier envoi
            if (jetonServiceDto.isLastSending() != null) {
                chercherDossierResponse.setDernierEnvoi(jetonServiceDto.isLastSending());
            }

            // récupération document
            docList = jetonServiceDto.getDocumentList();
            // Si la liste est vide, il y a eu une erreur dans le traitement
            if (docList == null) {
                chercherDossierResponse.setStatut(TraitementStatut.KO);
                return chercherDossierResponse;
            }
            // Si pas de documents, c'est qd même un succès
            if (docList.isEmpty()) {
                chercherDossierResponse.setStatut(TraitementStatut.OK);
                return chercherDossierResponse;
            }
        } else if (norList != null) {
            // dans le cas d'un recherche de dossier par nor, on renvoie comme valeur par défaut -1 pour le jeton =>
            // valeur déjà par défaut
            // Récupération des dossiers via leur numéro NOR
            final StringBuilder chercherDossierQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ") //
                .append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE) //
                .append(" AS l WHERE l.dos:numeroNor  IN (") //
                .append(StringHelper.getQuestionMark(norList.size())) //
                .append(")");
            final List<String> paramList = new ArrayList<>(norList);

            // récupéreration des fichier du Parapheur + fichiers publiques du Fond de Dossier
            try {
                docList = doUnrestrictedQuery(session, chercherDossierQuery.toString(), paramList);
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, StringHelper.join(norList, ", ", ""), e);
                chercherDossierResponse.setStatut(TraitementStatut.KO);
                chercherDossierResponse.setMessageErreur("Erreur lors de la récupération des dossiers");
                return chercherDossierResponse;
            }
            if (CollectionUtils.isEmpty(docList)) {
                chercherDossierResponse.setDernierEnvoi(true);
                chercherDossierResponse.setMessageErreur("Aucun résultat retourné par la requête");
                return chercherDossierResponse;
            }
        }

        // récupération de dossiers
        try {
            getDossierEpgWithFileListFromDossier(docList, session, norTrouveList, chercherDossierResponse.getDossier());
        } catch (final Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, StringHelper.join(norTrouveList, ", ", ""), e);
            chercherDossierResponse.setMessageErreur("Erreur lors de la récupération des données des dossiers");
            return chercherDossierResponse;
        } catch (final OutOfMemoryError err) {
            sendOutOfMemoryMail(err, jetonRecu);
            chercherDossierResponse.setMessageErreur("Erreur lors de la récupération des données des dossiers");
            return chercherDossierResponse;
        }

        // met à jour la liste des dossiers non récupérés
        if (norList != null && norTrouveList.size() < norList.size()) {
            for (final String norTrouve : norTrouveList) {
                norList.remove(norTrouve);
            }
            norNonTrouveList.addAll(norList);
        }

        // on renvoie la réponse
        chercherDossierResponse.setStatut(TraitementStatut.OK);
        if (!norNonTrouveList.isEmpty()) {
            // message d'erreur des dossiers non trouvés dans le cas de la recherche par dossier
            for (final String norNonTrouve : norNonTrouveList) {
                final ResultatRechercheInfructueuse resultatRechercheInfructueuse = new ResultatRechercheInfructueuse();
                resultatRechercheInfructueuse.setNor(norNonTrouve);
                resultatRechercheInfructueuse.setMessageErreur(
                    "Dossier non trouve : le dossier n'existe pas ou vous n'avez pas les droits d'accès sur ce dossier"
                );
                chercherDossierResponse.getErreur().add(resultatRechercheInfructueuse);
            }
        }

        if (CollectionUtils.isEmpty(docList)) {
            // log l'action dans le journal d'administration même si aucun dossier n'est retrouvé
            logWebServiceAction(
                SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_EVENT,
                SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_COMMENT_PARAM
            );
        } else {
            for (DocumentModel docTrouve : docList) {
                final Dossier dossierTrouve = docTrouve.getAdapter(Dossier.class);
                // On passe là si on a trouvé des dossiers. Il faudrait logger l'action pour chaque dossier trouvé
                // M156995 seulement si le dossier n'est pas dans la liste de NOR non trouvés
                if (dossierTrouve != null && !norNonTrouveList.contains(dossierTrouve.getNumeroNor())) {
                    logWebServiceActionDossier(
                        SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_EVENT,
                        SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_COMMENT_PARAM,
                        docTrouve
                    );
                }
            }
        }

        return chercherDossierResponse;
    }

    private void sendOutOfMemoryMail(final Throwable error, final String jetonRecu) {
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        final STMailService mailService = STServiceLocator.getSTMailService();
        System.gc();
        // Il arrive que la récupération des documents par le jeton provoque une erreur de java heap space
        // Dans ce cas là, on envoie un mail pour prévenir l'administrateur technique qu'il y a eu ce type d'erreur.
        try {
            final String mailContent = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_TEXTE
            );
            final String mailObject = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_OBJET
            );
            final String mailAdministrator = paramService.getParametreValue(
                session,
                STParametreConstant.MAIL_ADMIN_TECHNIQUE
            );
            final Map<String, Object> param = new HashMap<>();
            if (StringUtils.isNotEmpty(jetonRecu)) {
                param.put("concernant", "le jeton " + jetonRecu + " du webservice ministère CE");
            }
            mailService.sendTemplateMail(mailAdministrator, mailObject, mailContent, param);
        } catch (final NuxeoException ce) {
            LOGGER.error(
                session,
                STLogEnumImpl.FAIL_SEND_MAIL_TEC,
                "Erreur lors de l'envoi de mail d'erreur de java heapSpace.",
                ce
            );
        }
        LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, error);
    }

    @Override
    public DonnerAvisCEResponse donnerAvisCE(final DonnerAvisCERequest request) {
        final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        final NORService norService = SolonEpgServiceLocator.getNORService();
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();

        // initialisation des variables de la réponse
        final DonnerAvisCEResponse donnerAvisCEResponse = new DonnerAvisCEResponse();

        // verification des droits de l'utilisateur pour le service
        final List<String> userGroupList = ((SSPrincipal) session.getPrincipal()).getGroups();
        if (!userGroupList.contains(STWebserviceConstant.DONNER_AVIS_CE)) {
            donnerAvisCEResponse.setStatut(TraitementStatut.KO);
            donnerAvisCEResponse.setMessageErreur(WS_ACCESS_DENIED);
            return donnerAvisCEResponse;
        }

        // par defaut, on considère que la réponse est en échec
        donnerAvisCEResponse.setStatut(TraitementStatut.KO);

        // récupération des informations de la requete
        String nor = request.getNor();
        donnerAvisCEResponse.setNor(nor);
        try {
            nor = checkNor(nor);
        } catch (EPGException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, nor, e);
            donnerAvisCEResponse.setMessageErreur(ERROR_MSG_NOR_NON_TROUVE);
            return donnerAvisCEResponse;
        }

        LOGGER.info(session, EpgLogEnumImpl.INIT_WS_DONNER_AVIS_CE, "nor = '" + nor);

        // récupération du dossier
        DocumentModel dossierDoc;
        try {
            dossierDoc = norService.getDossierFromNOR(session, nor);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, nor, e);
            donnerAvisCEResponse.setMessageErreur(ERROR_MSG_NOR_NON_TROUVE);
            return donnerAvisCEResponse;
        }

        if (dossierDoc == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, nor);
            donnerAvisCEResponse.setMessageErreur(ERROR_MSG_NOR_NON_TROUVE);
            return donnerAvisCEResponse;
        }

        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        // On vérifie si le dossier n'est pas déjà verrouillé par un autre utilisateur - M156599
        final STLockService lockService = STServiceLocator.getSTLockService();
        final SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        try {
            List<String> docIds = Arrays.asList(dossierDoc.getId());
            // Récupération de la liste des verrous liés au dossier
            Map<String, String> mapLock = lockService.extractLockedInfo(session, docIds);
            if (!lockService.isLockByUser(session, dossierDoc, principal) && !mapLock.isEmpty()) {
                donnerAvisCEResponse.setMessageErreur("Le dossier est verrouillé par un autre utilisateur");
                return donnerAvisCEResponse;
            }
        } catch (NuxeoException ce) {
            LOGGER.error(session, STLogEnumImpl.FAIL_LOCK_DOC_TEC, ce);
            donnerAvisCEResponse.setMessageErreur("Erreur lors du verrouillage du dossier : " + ce.getMessage());
            return donnerAvisCEResponse;
        }
        boolean hasPermission = true;
        try {
            hasPermission = session.hasPermission(dossierDoc.getRef(), SecurityConstants.READ_WRITE);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, ERROR_READ_WRITE_MSG, e);
            donnerAvisCEResponse.setMessageErreur(ERROR_READ_WRITE_MSG);
            return donnerAvisCEResponse;
        }
        if (!hasPermission) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, ERROR_READ_WRITE_MSG);
            donnerAvisCEResponse.setMessageErreur(ERROR_READ_WRITE_MSG);
            return donnerAvisCEResponse;
        } else if (!acteService.isVisibleCE(dossier.getTypeActe())) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, ERROR_TYPE_ACTE_INCOMPATIBLE_AVIS_CE);
            donnerAvisCEResponse.setMessageErreur(ERROR_TYPE_ACTE_INCOMPATIBLE_AVIS_CE);
            return donnerAvisCEResponse;
        }

        // A ce niveau, l'utilisateur a les drois d'écriture sur le dossier, on
        // récupère le dossier de nouveau pour que ce dernier ait la bonne
        // CoreSession attachée
        dossierDoc = QueryHelper.getDocument(session, dossierDoc.getId());
        dossier = dossierDoc.getAdapter(Dossier.class);

        // récupère le dossierLink lié au dossier
        DocumentModel dossierLinkDoc = null;
        List<DocumentModel> dossierLinkDocList = null;
        try {
            dossierLinkDocList = corbeilleService.findDossierLink(session, dossierDoc.getId());
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierDoc, e);
            donnerAvisCEResponse.setMessageErreur(
                "Erreur lors de la récupération des dossiers link liés au dossier : " + e.getMessage()
            );
            return donnerAvisCEResponse;
        }

        // RG-INS-FDR-20 : valide l'étape en fonction du type de validation
        final TypeValidationCe typeValidationCe = request.getTypeValidationCe();

        if (typeValidationCe == null) {
            LOGGER.error(
                session,
                SSLogEnumImpl.FAIL_VALIDATE_STEP_TEC,
                "Erreur lors de la validation de l'étape nulle ou incorrecte"
            );
            donnerAvisCEResponse.setMessageErreur("Type de validation de l'étape nulle ou incorrecte");
            return donnerAvisCEResponse;
        }

        final List<Fichier> fichierList = request.getProjetDecret();
        final List<Fichier> noteGvtList = request.getNoteGouvernement();

        final ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);

        if (typeValidationCe.toString().equals(AVIS_RECTIFICATIF)) {
            if (!dossier.getStatut().equals(VocabularyConstants.STATUT_LANCE)) {
                LOGGER.warn(
                    session,
                    STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC,
                    "Le dossier n'est pas à l'état Lancé ! Il n'est pas possible de donner un avis rectificatif du Conseil d'Etat sur ce dossier."
                );
                donnerAvisCEResponse.setMessageErreur(
                    "Le dossier n'est pas à l'état Lancé ! Il n'est pas possible de donner un avis rectificatif du Conseil d'Etat sur ce dossier."
                );
                return donnerAvisCEResponse;
            } else if (!dossierService.hasEtapeAvisCEValide(session, dossier)) {
                LOGGER.warn(
                    session,
                    STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC,
                    "Aucun avis du conseil d'Etat n'a été rendu sur ce dossier ! Il n'est pas possible de donner un avis rectificatif."
                );
                donnerAvisCEResponse.setMessageErreur(
                    "Aucun avis du conseil d'Etat n'a été rendu sur ce dossier ! Il n'est pas possible de donner un avis rectificatif."
                );
                return donnerAvisCEResponse;
            }
            try {
                WsUtil.createOrUpdateFileListInFdd(
                    session,
                    dossierDoc,
                    fichierList,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE
                );
                WsUtil.createOrUpdateFileListInFdd(
                    session,
                    dossierDoc,
                    noteGvtList,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE
                );
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
                donnerAvisCEResponse.setMessageErreur(
                    "Erreur lors de la création des fichiers du Conseil d'Etat : " + e.getMessage()
                );
                return donnerAvisCEResponse;
            }
        } else {
            if (dossierLinkDocList != null) {
                for (final DocumentModel dossierLinkDocElement : dossierLinkDocList) {
                    // on verifie que le dossierLink est bien à l'étape "pour avis ce"
                    final DossierLink dossierLink = dossierLinkDocElement.getAdapter(DossierLink.class);
                    if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(dossierLink.getRoutingTaskType())) {
                        dossierLinkDoc = dossierLinkDocElement;
                        break;
                    }
                }
            }

            if (dossierLinkDoc == null) {
                LOGGER.warn(
                    session,
                    STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC,
                    "Le dossier n'est pas à l'étape 'Pour avis CE' ! Il n'est pas possible de donner un avis du Conseil d'Etat sur ce dossier !"
                );
                donnerAvisCEResponse.setMessageErreur(
                    "Le dossier n'est pas à l'étape 'Pour avis CE' ! Il n'est pas possible de donner un avis du Conseil d'Etat sur ce dossier !"
                );
                return donnerAvisCEResponse;
            }

            // création des fichier dans le répertoire "Avis du Conseil d'Etat" du fond de dossier
            // création des fichiers projet Decret et note gvt
            try {
                WsUtil.createFilesInFdd(
                    session,
                    dossierDoc,
                    fondDeDossierService,
                    fichierList,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE
                );
                WsUtil.createFilesInFdd(
                    session,
                    dossierDoc,
                    fondDeDossierService,
                    noteGvtList,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE
                );
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
                donnerAvisCEResponse.setMessageErreur(
                    "Erreur lors de la création des fichiers du Conseil d'Etat : " + e.getMessage()
                );
                return donnerAvisCEResponse;
            }
        }

        // partie CE
        final SectionCe sectionCe = request.getSectionCe();
        try {
            fillConseilEtatFromSectionCeWsUnrestricted(conseilEtat, sectionCe, false);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, e);
            donnerAvisCEResponse.setMessageErreur(
                "Erreur lors de la mise à jour des informations du Conseil d'Etat : " + e.getMessage()
            );
            return donnerAvisCEResponse;
        }

        try {
            String validationStatus = "";
            switch (typeValidationCe) {
                case AVIS_RENDU: // validation acceptée
                    validationStatus = SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE;
                    dossierDistributionService.validerEtape(session, dossierDoc, dossierLinkDoc, validationStatus);
                    break;
                case DESSAISISSEMENT: // refus de validation
                    validationStatus = SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE;
                    dossierDistributionService.validerEtapeRefus(session, dossierDoc, dossierLinkDoc, validationStatus);
                    break;
                case RETRAIT: // refus de validation
                    validationStatus = SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE;
                    dossierDistributionService.validerEtapeRefus(session, dossierDoc, dossierLinkDoc, validationStatus);
                    break;
                case REJET: // refus de validation
                    validationStatus = SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE;
                    dossierDistributionService.validerEtapeRefus(session, dossierDoc, dossierLinkDoc, validationStatus);
                    break;
                case RENVOI: // refus de validation
                    validationStatus = SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE;
                    dossierDistributionService.validerEtapeRefus(session, dossierDoc, dossierLinkDoc, validationStatus);
                    break;
                case AVIS_RECTIFICATIF: // demande de rectification
                    dossierDistributionService.avisRectificatif(session, dossierDoc);
                    break;
                default:
                    dossierDistributionService.validerEtapeRefus(session, dossierDoc, dossierLinkDoc, validationStatus);
                    break;
            }
        } catch (final Exception e) {
            LOGGER.error(session, SSLogEnumImpl.FAIL_VALIDATE_STEP_TEC, typeValidationCe.toString(), e);
            donnerAvisCEResponse.setMessageErreur(
                "Erreur lors de la validation de l'étape " + typeValidationCe + " : " + e.getMessage()
            );
            return donnerAvisCEResponse;
        }

        logWebServiceActionDossier(
            SolonEpgEventConstant.WEBSERVICE_DONNER_AVIS_CE_EVENT,
            SolonEpgEventConstant.WEBSERVICE_DONNER_AVIS_CE_COMMENT_PARAM,
            dossier.getDocument()
        );
        // signale l'action a été effectuée sans erreur
        donnerAvisCEResponse.setStatut(TraitementStatut.OK);

        return donnerAvisCEResponse;
    }

    @Override
    public ModifierDossierCEResponse modifierDossierCE(final ModifierDossierCERequest request) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();

        // récupération des informations de la requete
        final String nor = checkNor(request.getNor());
        LOGGER.info(session, EpgLogEnumImpl.INIT_WS_MODIFIER_DOSSIER_CE, "nor = '" + nor);

        // initialisation de la réponse
        final ModifierDossierCEResponse modifierDossierCEResponse = new ModifierDossierCEResponse();
        modifierDossierCEResponse.setNor(nor);
        // par defaut, on considère que la réponse est en échec
        modifierDossierCEResponse.setStatut(TraitementStatut.KO);

        // verification des droits de l'utilisateur pour le service
        final List<String> userGroupList = ((SSPrincipal) session.getPrincipal()).getGroups();
        if (!userGroupList.contains(STWebserviceConstant.MODIFIER_DOSSIER_CE)) {
            modifierDossierCEResponse.setMessageErreur(WS_ACCESS_DENIED);
            return modifierDossierCEResponse;
        }

        // récupération du dossier
        final NORService norService = SolonEpgServiceLocator.getNORService();
        DocumentModel dossierDoc = null;
        try {
            final String numeroISA = checkNumeroISA(request.getNumeroIsa());
            if (StringUtils.isNotBlank(nor)) {
                // On récupère le dossier par son numéro NOR
                dossierDoc = norService.getDossierFromNOR(session, nor, CONSEIL_ETAT_SCHEMA);
            } else if (StringUtils.isNotBlank(numeroISA)) {
                // On récupère le dossier par son numéro ISA
                dossierDoc = norService.getDossierFromISA(session, numeroISA, CONSEIL_ETAT_SCHEMA);
            }
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, nor, e);
            modifierDossierCEResponse.setCodeErreur(CodeErreur.DOSSIER_INTROUVABLE);
            modifierDossierCEResponse.setMessageErreur("Erreur lors de la récupération du dossier : " + e.getMessage());
            return modifierDossierCEResponse;
        }

        if (dossierDoc == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, nor);
            modifierDossierCEResponse.setCodeErreur(CodeErreur.DOSSIER_INTROUVABLE);
            modifierDossierCEResponse.setMessageErreur("Le dossier est introuvable");
            return modifierDossierCEResponse;
        }

        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        // On vérifie si le dossier n'est pas déjà verrouillé par un autre utilisateur - M156599
        final STLockService lockService = STServiceLocator.getSTLockService();
        final SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        try {
            List<String> docIds = Arrays.asList(dossierDoc.getId());
            // Récupération de la liste des verrous liés au dossier
            Map<String, String> mapLock = lockService.extractLockedInfo(session, docIds);
            if (!lockService.isLockByUser(session, dossierDoc, principal) && !mapLock.isEmpty()) {
                modifierDossierCEResponse.setMessageErreur("Le dossier est verrouillé par un autre utilisateur");
                return modifierDossierCEResponse;
            }
        } catch (NuxeoException ce) {
            LOGGER.error(session, STLogEnumImpl.FAIL_LOCK_DOC_TEC, ce);
            modifierDossierCEResponse.setMessageErreur("Erreur lors du verrouillage du dossier : " + ce.getMessage());
            return modifierDossierCEResponse;
        }

        boolean hasPermission = true;
        try {
            hasPermission = session.hasPermission(dossierDoc.getRef(), SecurityConstants.READ_WRITE);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, ERROR_READ_WRITE_MSG, e);
            modifierDossierCEResponse.setCodeErreur(CodeErreur.DROIT_ACCESS_INSUFFISANT);
            modifierDossierCEResponse.setMessageErreur(ERROR_READ_WRITE_MSG);
            return modifierDossierCEResponse;
        }
        if (!hasPermission) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, ERROR_READ_WRITE_MSG);
            modifierDossierCEResponse.setCodeErreur(CodeErreur.DROIT_ACCESS_INSUFFISANT);
            modifierDossierCEResponse.setMessageErreur(ERROR_READ_WRITE_MSG);
            return modifierDossierCEResponse;
        } else if (!acteService.isVisibleCE(dossier.getTypeActe())) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, ERROR_TYPE_ACTE_INCOMPATIBLE_AVIS_CE);
            modifierDossierCEResponse.setCodeErreur(CodeErreur.AUTRE);
            modifierDossierCEResponse.setMessageErreur(ERROR_TYPE_ACTE_INCOMPATIBLE_AVIS_CE);
            return modifierDossierCEResponse;
        }
        dossierDoc = QueryHelper.getDocument(session, dossierDoc.getId());
        dossier = dossierDoc.getAdapter(Dossier.class);

        // modification du dossier
        final ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);
        final SectionCe sectionCe = request.getSectionCe();
        try {
            fillConseilEtatFromSectionCeWsUnrestricted(conseilEtat, sectionCe, true);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, dossierDoc, e);
            modifierDossierCEResponse.setMessageErreur(
                "Erreur lors de la mise à jour des informations du Conseil d'Etat : " + e.getMessage()
            );
            return modifierDossierCEResponse;
        }
        // M156995 - i on a fait un appel par numéro ISA et que le NOR est donc vide, on va renseigner le NOR dans la
        // réponse
        if (StringUtils.isEmpty(modifierDossierCEResponse.getNor())) {
            modifierDossierCEResponse.setNor(dossier.getNumeroNor());
        }
        // création de la réponse
        modifierDossierCEResponse.setStatut(TraitementStatut.OK);

        // log l'action dans le journal d'administration
        logWebServiceActionDossier(
            SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT,
            SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_COMMENT_PARAM,
            dossier.getDocument()
        );

        return modifierDossierCEResponse;
    }

    @Override
    public CreerDossierResponse creerDossier(CreerDossierRequest request) {
        final CreerDossierResponse response = new CreerDossierResponse();

        // récupération des informations de la requete
        final ActeType acteType = request.getTypeActe();
        final String codeEntite = checkCodeEntite(request.getCodeEntite());
        final String codeDirection = checkCodeDirection(request.getCodeDirection());

        if (acteType == null || StringUtils.isEmpty(codeEntite) || StringUtils.isEmpty(codeDirection)) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Un champ obligatoire est manquant");
            return response;
        }

        LOGGER.info(
            session,
            EpgLogEnumImpl.INIT_WS_CREER_DOSSIER_TEC,
            "type acte = '" +
            acteType +
            "', code entite = '" +
            codeEntite +
            "', code direction = '" +
            codeDirection +
            "'"
        );

        // Vérification avant de lancer le WS:
        // - Code entité et code direction doivent correspondre à un ministère et une direction dans l'organigramme
        // sinon erreur
        // - Il y a un droit spécifique pour chaque type d'acte

        // récupère le ministere à partir des 3 lettres nor du ministère
        final EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
        EntiteNode ministereEntiteNode = null;
        try {
            ministereEntiteNode = epgOrganigrammeService.getMinistereFromNor(codeEntite);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_MINISTERE_TEC, codeEntite, e);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Erreur lors de la récupération du ministère :" + e.getMessage());
            return response;
        }
        if (ministereEntiteNode == null) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_MINISTERE_TEC, codeEntite);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Le ministère indiqué n'existe pas");
            return response;
        }

        final String ministereRespFinal = ministereEntiteNode.getId();

        // récupère la direction à partir de la lettres nor de la direction et du ministère
        UniteStructurelleNode epgUniteStructurelleNode;
        try {
            epgUniteStructurelleNode =
                epgOrganigrammeService.getDirectionFromMinistereAndNor(ministereEntiteNode, codeDirection);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, codeDirection, e);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Erreur lors de la récupération de la  direction :" + e.getMessage());
            return response;
        }
        if (epgUniteStructurelleNode == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, codeDirection);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("La direction indiquée n'existe pas");
            return response;
        }

        final String directionRespFinal = epgUniteStructurelleNode.getId();

        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        // récupération type Acte à partir de l'ActeType ws
        final String typeActe = WsUtil.getDossierActeTypeFromWsTypeActe(acteType);

        // On ne traite pas le cas de l'acte conv. collective parce qu'il n'est
        // pas géré non plus dans le cas de la création de dossier simple
        String nature = VocabularyConstants.NATURE_REGLEMENTAIRE;
        if (acteService.isNonReglementaire(typeActe)) {
            nature = VocabularyConstants.NATURE_NON_REGLEMENTAIRE;
        }

        // verification des droits de l'utilisateur pour le service
        final SSPrincipal principal = (SSPrincipal) session.getPrincipal();

        final List<String> userGroupList = principal.getGroups();
        if (!userGroupList.contains(CREER_DOSSIER_RIGHTS.get(acteType))) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(WS_ACCESS_DENIED);
            return response;
        }

        Set<String> postesIds = principal.getPosteIdSet();
        if (postesIds.isEmpty()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("L'utilisateur n'est rattaché à aucun poste");
            return response;
        }

        // vérification de la présence de l'utilisateur dans la direction du ministère
        // Set<String> directionsIds = principal.getDirectionIdSet();
        // if (directionsIds == null || !directionsIds.contains(directionRespFinal)) {
        // response.setStatut(TraitementStatut.KO);
        // response.setMessageErreur("L'utilisateur n'est pas rattaché à cette direction");
        // return response;
        // }

        // récupération de la mailbox du poste de l'utilisateur (element du set des postes)
        String posteId = postesIds.iterator().next();
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        Mailbox currentMailbox = null;
        try {
            currentMailbox = mailboxPosteService.getMailboxPoste(session, posteId);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_MAILBOX_TEC, posteId, e);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Erreur lors de la récupération de la mailbox du poste : " + e.getMessage());
            return response;
        }
        if (currentMailbox == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_MAILBOX_TEC, posteId);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("La mailbox du poste n'est pas initialisée");
            return response;
        }
        // Détermine les lettres de NOR du type d'acte
        final TypeActe acte = acteService.getActe(typeActe);
        final String norActe = checkNorActe(acte.getNor());

        // Attribution du NOR
        final NORService norService = SolonEpgServiceLocator.getNORService();
        String numeroNorDossier;
        try {
            numeroNorDossier = norService.createNOR(norActe, codeEntite, codeDirection);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_DOSSIER_TEC, norActe, e);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Erreur lors de la création du nor : " + e.getMessage());
            return response;
        }

        // Initialisation du dossier
        DocumentModel dossierDoc;
        Dossier dossier;
        try {
            dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
            DublincoreSchemaUtils.setTitle(dossierDoc, numeroNorDossier);
            dossier = dossierDoc.getAdapter(Dossier.class);

            // Ajout des informations de création information du dossier
            dossier.setNumeroNor(numeroNorDossier);
            dossier.setTypeActe(typeActe);
            dossier.setMinistereResp(ministereRespFinal);
            dossier.setDirectionResp(directionRespFinal);
            // Ajout des informations de création
            dossier.setCategorieActe(nature);

            // Crée le dossier
            final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            dossier = dossierService.createDossierWs(session, dossier.getDocument(), posteId, currentMailbox);

            DublincoreSchemaUtils.setCreator(dossier.getDocument(), principal.getName());

            // on renvoie la réponse
            DossierEpg dossierEpg = WsUtil.getDossierEpgFromDossier(dossier, session, false);
            response.setDossier(dossierEpg);
            LOGGER.info(session, STLogEnumImpl.CREATE_DOSSIER_TEC, dossier.getDocument());
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_DOSSIER_TEC, e);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Erreur lors de la création du dossier : " + e.getMessage());
            return response;
        }

        response.setStatut(TraitementStatut.OK);

        // log l'action dans le journal d'administration
        logWebServiceActionDossier(
            SolonEpgEventConstant.WEBSERVICE_CREER_DOSSIER_EVENT,
            SolonEpgEventConstant.WEBSERVICE_CREER_DOSSIER_COMMENT_PARAM,
            dossier.getDocument()
        );

        return response;
    }

    @Override
    public ModifierDossierResponse modifierDossier(ModifierDossierRequest request) {
        final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        final EpgDossierDistributionService dds = SolonEpgServiceLocator.getDossierDistributionService();
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        final STLockService lockService = STServiceLocator.getSTLockService();
        final NORService norService = SolonEpgServiceLocator.getNORService();

        final ModifierDossierResponse response = new ModifierDossierResponse();

        // récupération des informations de la requete
        final DossierEpgWithFile dossierEpg = request.getDossier();
        // Récupération de l'acte. Actuellement uniquement avis, decret pr ind et informations parlementaires
        Acte acte = dossierEpg.getDossierEpg().getAvis();
        if (acte == null) {
            acte = dossierEpg.getDossierEpg().getDecretPrInd();
        }
        if (acte == null) {
            acte = dossierEpg.getDossierEpg().getInformationsParlementaires();
        }
        if (acte == null) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Échec de récupération des données du dossier");
            return response;
        }

        LOGGER.info(session, EpgLogEnumImpl.INIT_WS_MODIFIER_DOSSIER_TEC);

        // verification des droits de l'utilisateur pour le service
        final SSPrincipal principal = (SSPrincipal) session.getPrincipal();

        final List<String> userGroupList = principal.getGroups();
        final ActeType acteType = acte.getTypeActe();
        if (!userGroupList.contains(MODIFIER_DOSSIER_RIGHTS.get(acteType))) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(WS_ACCESS_DENIED);
            return response;
        }

        List<String> postesIds = new ArrayList<>(principal.getPosteIdSet());
        if (postesIds.isEmpty()) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("L'utilisateur n'est rattaché à aucun poste");
            return response;
        }
        Set<String> mailboxPosteIds = SSServiceLocator
            .getMailboxPosteService()
            .getMailboxPosteIdSetFromPosteIdSet(postesIds);
        boolean isStepInPoste = false;
        boolean isStepPublicationJo = false;

        // récupération du dossier
        DocumentModel dossierDoc;
        String norActe = checkNor(acte.getNor());
        try {
            dossierDoc = norService.getDossierFromNOR(session, norActe);
            if (dossierDoc == null) {
                response.setStatut(TraitementStatut.KO);
                response.setMessageErreur(
                    "Le dossier avec le NOR " + norActe + " n'existe pas, il ne peut pas être modifié"
                );
                return response;
            }
        } catch (final NuxeoException exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, norActe, exc);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Nor inconnu dans l'application");
            return response;
        }

        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        // Récupération de la feuille de route dans le cas des informations parlementaires
        boolean isInfoParl = acteType == ActeType.INFORMATIONS_PARLEMENTAIRES;
        DocumentModel routeDoc = null;
        String lastRouteId = dossier.getLastDocumentRoute();
        if (isInfoParl && lastRouteId != null) {
            try {
                routeDoc = session.getDocument(new IdRef(lastRouteId));
            } catch (Exception e) {
                routeDoc = null;
            }
        }

        try {
            if (
                !lockService.isLockByUser(session, dossierDoc, principal) &&
                lockService.isLockActionnableByUser(session, dossierDoc, principal)
            ) {
                lockService.lockDoc(session, dossierDoc);
                if (routeDoc != null) {
                    lockService.lockDoc(session, routeDoc);
                }
            } else if (!lockService.isLockByUser(session, dossierDoc, principal)) {
                throw new EPGException("L'utilisateur ne peut pas obtenir le verrou sur ce document");
            }
        } catch (NuxeoException ce) {
            LOGGER.error(session, STLogEnumImpl.FAIL_LOCK_DOC_TEC, ce);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Impossible de récupérer le verrou du document : " + ce.getMessage());
            return response;
        }

        if (
            isInfoParl &&
            !dossier.getStatut().equals(VocabularyConstants.STATUT_INITIE) &&
            !dossier.getStatut().equals(VocabularyConstants.STATUT_LANCE)
        ) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Le dossier est déjà publié, impossible de le modifier");
            tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
            return response;
        }

        List<DocumentModel> runningSteps = null;
        try {
            runningSteps = feuilleRouteService.getRunningSteps(session, dossier.getLastDocumentRoute());
        } catch (NuxeoException exc) {
            LOGGER.error(session, SSLogEnumImpl.FAIL_GET_STEP_TEC, dossier.getDocument(), exc);
            response.setMessageErreur("Erreur dans la récupération des étapes en cours : " + exc.getMessage());
        }
        SolonEpgRouteStep stepForUser = null;
        DocumentModel currentStepPublicationJODoc = null;
        if (runningSteps != null) {
            for (DocumentModel runningStep : runningSteps) {
                stepForUser = runningStep.getAdapter(SolonEpgRouteStep.class);
                String mailboxId = stepForUser.getDistributionMailboxId();
                if (mailboxPosteIds.contains(mailboxId)) {
                    isStepInPoste = true;
                }
                if (stepForUser.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO)) {
                    isStepPublicationJo = true;
                    currentStepPublicationJODoc = runningStep;
                }
            }
        }

        if (!isStepInPoste && (!isInfoParl || !isStepPublicationJo)) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("L'étape n'est en cours dans aucun poste de l'utilisateur");
            tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
            return response;
        }

        List<DocumentModel> nextStep;
        try {
            nextStep =
                feuilleRouteService.findNextSteps(
                    session,
                    dossier.getLastDocumentRoute(),
                    stepForUser.getDocument(),
                    null
                );
            if (CollectionUtils.isEmpty(nextStep)) {
                if (isInfoParl && isStepPublicationJo) {
                    try {
                        // Création d'une nouvelle étape de type 'Pour publication à la DILA JO' après l'étape en cours
                        SSRouteStep currentStepPublicationJO = currentStepPublicationJODoc.getAdapter(
                            SSRouteStep.class
                        );
                        EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
                        final Filter routeStepFilter = new RouteStepValideFilter();
                        // Techniquement on n'en a pas besoin, mais ça permet de renseigner la position de notre étape actuelle
                        @SuppressWarnings("unused")
                        final DocumentModel etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(
                            session,
                            currentStepPublicationJODoc,
                            routeStepFilter,
                            true
                        );
                        // Récupère la position de l'étape en cours
                        final Integer currentStepPos = (Integer) currentStepPublicationJODoc.getContextData(
                            STConstant.POS_DOC_CONTEXT
                        );
                        FeuilleRouteStep newRouteStepDoc = documentRoutingService.createNewRouteStep(
                            session,
                            currentStepPublicationJO.getDistributionMailboxId(),
                            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO
                        );
                        documentRoutingService.addRouteElementToRoute(
                            currentStepPublicationJODoc.getParentRef(),
                            currentStepPos + 1,
                            newRouteStepDoc,
                            session
                        );
                    } catch (NuxeoException exc) {
                        LOGGER.error(session, SSLogEnumImpl.FAIL_UPDATE_FDR_TEC, dossierDoc, exc);
                        response.setStatut(TraitementStatut.KO);
                        response.setMessageErreur(
                            "Erreur dans la création d'une nouvelle étape 'Pour publication à la DILA JO'. " +
                            exc.getMessage()
                        );
                        tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
                        return response;
                    }
                } else {
                    throw new EPGException("Il n'existe pas d'étape à venir pour le dossier");
                }
            }
        } catch (NuxeoException exc) {
            LOGGER.error(session, SSLogEnumImpl.FAIL_GET_STEP_TEC, dossierDoc, exc);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Aucune étape à venir après l'étape en cours. " + exc.getMessage());
            tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
            return response;
        }

        boolean hasPermission = true;
        try {
            hasPermission = session.hasPermission(dossierDoc.getRef(), SecurityConstants.READ_WRITE);
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, ERROR_READ_WRITE_MSG, e);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(ERROR_READ_WRITE_MSG);
            tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
            return response;
        }
        if (!hasPermission) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, ERROR_READ_WRITE_MSG);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(ERROR_READ_WRITE_MSG);
            tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
            return response;
        }
        dossierDoc = QueryHelper.getDocument(session, dossierDoc.getId());
        dossier = dossierDoc.getAdapter(Dossier.class);

        try {
            WsUtil.remapDossier(session, dossier, dossierEpg.getDossierEpg(), acteType);
            dossierDoc = session.getDocument(dossierDoc.getRef());
            dossier = dossierDoc.getAdapter(Dossier.class);
        } catch (NuxeoException ce) {
            LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, dossierDoc, ce);
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur("Le dossier n'a pas pu être mis à jour. " + ce.getMessage());
            tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
            return response;
        }

        ListeFichiers fdd = dossierEpg.getFondDossier();
        if (fdd != null) {
            List<Fichier> fichiersFdd = fdd.getFichier();
            if (CollectionUtils.isNotEmpty(fichiersFdd)) {
                FondDeDossierInstance fddInstance = dossier.getFondDeDossier(session);
                DocumentModel fddInstanceDoc = fddInstance.getDocument();

                for (Fichier fichier : fichiersFdd) {
                    try {
                        DocumentModel folderDoc = getParentFromFichier(
                            session,
                            fddInstanceDoc,
                            fichier,
                            fondDeDossierService
                        );

                        DocumentModelList children = session.getChildren(folderDoc.getRef());
                        DocumentModel fileDoc = null;
                        for (DocumentModel child : children) {
                            if (child.getTitle().equals(fichier.getNom())) {
                                fileDoc = child;
                                break;
                            }
                        }

                        if (isMimeTypeAccepted(fddInstance.getFormatsAutorises(), fichier.getMimeType())) {
                            if (fileDoc == null) {
                                fondDeDossierService.createFondDeDossierFile(
                                    session,
                                    fichier.getNom(),
                                    fichier.getContenu(),
                                    folderDoc.getTitle(),
                                    dossierDoc
                                );
                            } else {
                                final Blob blob = new ByteArrayBlob(fichier.getContenu());
                                blob.setFilename(fichier.getNom());
                                fondDeDossierService.updateFile(session, fileDoc, blob, principal, dossierDoc);
                            }
                        } else {
                            throw new EPGException(
                                String.format(
                                    "Le type %s n'est pas autorisé pour le fond de dossier",
                                    fichier.getMimeType()
                                )
                            );
                        }
                    } catch (NuxeoException ce) {
                        LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, dossierDoc, ce);
                        response.setStatut(TraitementStatut.KO);
                        response.setMessageErreur(
                            "Le fichier de fond de dossier n'a pas pu être créé. " + ce.getMessage()
                        );
                        tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
                        return response;
                    }
                }
            }
        }

        ListeFichiers parapheur = dossierEpg.getParapheur();
        if (parapheur != null) {
            List<Fichier> fichiersParapheur = parapheur.getFichier();
            if (CollectionUtils.isNotEmpty(fichiersParapheur)) {
                for (Fichier fichier : fichiersParapheur) {
                    String[] cheminFichier = StringUtils.split(fichier.getCheminFichier(), "/");
                    // Le folder name apparait avant le nom du fichier qui apparait en dernier
                    String folderName = cheminFichier[cheminFichier.length - 2];

                    try {
                        DocumentModel parapheurFolderDoc = parapheurService.getParapheurFolder(
                            dossierDoc,
                            session,
                            folderName
                        );
                        ParapheurFolder parapheurFolder = parapheurFolderDoc.getAdapter(ParapheurFolder.class);

                        DocumentModelList children = session.getChildren(parapheurFolderDoc.getRef());
                        DocumentModel fileDoc = null;
                        for (DocumentModel child : children) {
                            if (child.getTitle().equals(fichier.getNom())) {
                                fileDoc = child;
                                break;
                            }
                        }

                        if (
                            !isMimeTypeAccepted(
                                parapheurFolder.getFormatsAutorisesUnrestricted(session),
                                fichier.getMimeType()
                            )
                        ) {
                            // Vérification du format échouée
                            throw new EPGException(
                                String.format("Le type %s n'est pas autorisé pour le parapheur", fichier.getMimeType())
                            );
                        } else if (fileDoc != null) {
                            // Mise à jour d'un fichier existant (pas de document existant trouvé)
                            final Blob blob = new ByteArrayBlob(fichier.getContenu());
                            blob.setFilename(fichier.getNom());
                            parapheurService.updateFile(session, fileDoc, blob, principal, dossierDoc);
                        } else if (parapheurFolder.getNbDocAccepteMaxUnrestricted(session) > children.size()) {
                            // On créé un document si aucun n'a été trouvé, et que le nombe maximum de document
                            // n'a pas été atteint
                            parapheurService.createParapheurFile(
                                session,
                                fichier.getNom(),
                                fichier.getContenu(),
                                folderName,
                                dossierDoc
                            );
                            dossierDoc = session.getDocument(dossierDoc.getRef());
                            dossier = dossierDoc.getAdapter(Dossier.class);
                            // On relocke le dossier s'il a été délocké dans la méthode précédente
                            if (
                                !lockService.isLockByUser(session, dossierDoc, principal) &&
                                lockService.isLockActionnableByUser(session, dossierDoc, principal)
                            ) {
                                lockService.lockDoc(session, dossierDoc);
                            }
                        } else if (isInfoParl && !children.isEmpty()) {
                            // Cas d'une information parlementaire où le nombre max de doc a été atteint et que le
                            // fichier qu'on souhaite ajouter est nouveau
                            // On remplace le fichier existant
                            final Blob blob = new ByteArrayBlob(fichier.getContenu());
                            blob.setFilename(fichier.getNom());
                            parapheurService.updateFile(session, children.get(0), blob, principal, dossierDoc);
                        } else {
                            // Cas où le nombre max de doc a été atteint et que le fichier qu'on souhaite ajouter est
                            // nouveau
                            throw new EPGException("Le répertoire du parapheur choisi est complet");
                        }
                    } catch (EPGException ce) {
                        LOGGER.info(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, dossierDoc, ce);
                        return handleErrorModifierDossier(
                            lockService,
                            response,
                            principal,
                            dossierDoc,
                            routeDoc,
                            ce,
                            "Le fichier de parapheur n'a pas pu être créé. "
                        );
                    } catch (NuxeoException ce) {
                        LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, dossierDoc, ce);
                        return handleErrorModifierDossier(
                            lockService,
                            response,
                            principal,
                            dossierDoc,
                            routeDoc,
                            ce,
                            "Le fichier de parapheur n'a pas pu être créé. "
                        );
                    }
                }
            }
        }

        try {
            // Lance une exception si le dossier n'est pas valide
            dds.checkDossierBeforeValidateStep(session, dossier, nextStep);
        } catch (EPGException ce) {
            LOGGER.info(session, SSLogEnumImpl.FAIL_VALIDATE_STEP_TEC, dossier.getDocument(), ce);
            return handleErrorModifierDossier(
                lockService,
                response,
                principal,
                dossierDoc,
                routeDoc,
                ce,
                "Le dossier n'est pas valide pour continuer la validation d'étape. "
            );
        } catch (NuxeoException ce) {
            LOGGER.error(session, SSLogEnumImpl.FAIL_VALIDATE_STEP_TEC, dossier.getDocument(), ce);
            return handleErrorModifierDossier(
                lockService,
                response,
                principal,
                dossierDoc,
                routeDoc,
                ce,
                "Le dossier n'est pas valide pour continuer la validation d'étape. "
            );
        }

        // Validation de l'étape en cours
        try {
            List<DocumentModel> stepsList = feuilleRouteService.getRunningSteps(
                session,
                dossier.getLastDocumentRoute()
            );
            DocumentModel stepDoc = null;
            if (stepsList != null && stepsList.size() == 1) {
                stepDoc = stepsList.get(0);
            } else {
                throw new EPGException("Impossible de valider l'étape. Plusieurs étapes en cours");
            }
            if (stepDoc == null) {
                throw new EPGException("Impossible de valider l'étape. L'étape en cours n'a pas pu être récupérée");
            }
            // Dans le cas d'une information parlementaire avec pour publication
            // en cours on utilise une session unrestricted pour valider l'étape
            if (isInfoParl && isStepPublicationJo) {
                final String stepDocId = stepDoc.getId();
                final DocumentModel dossierDocFinal = dossierDoc;
                new UnrestrictedSessionRunner(session) {

                    @Override
                    public void run() {
                        final DocumentModel dossierLinkDoc = corbeilleService.getDossierLink(session, stepDocId);
                        if (STDossier.DossierState.init.name().equals(dossierDocFinal.getCurrentLifeCycleState())) {
                            dds.lancerDossier(session, dossierDocFinal, dossierLinkDoc);
                        } else if (
                            STDossier.DossierState.running.name().equals(dossierDocFinal.getCurrentLifeCycleState())
                        ) {
                            dds.validerEtapeCorrection(session, dossierDocFinal, dossierLinkDoc);
                        } else {
                            throw new EPGException(
                                "Impossible de valider l'étape. Le dossier n'est pas dans un état 'initié' ou 'lancé'"
                            );
                        }
                    }
                }
                .runUnrestricted();
            } else {
                final DocumentModel dossierLinkDoc = corbeilleService.getDossierLink(session, stepDoc.getId());
                if (STDossier.DossierState.init.name().equals(dossierDoc.getCurrentLifeCycleState())) {
                    dds.lancerDossier(session, dossierDoc, dossierLinkDoc);
                } else if (STDossier.DossierState.running.name().equals(dossierDoc.getCurrentLifeCycleState())) {
                    dds.validerEtape(session, dossierDoc, dossierLinkDoc);
                } else {
                    throw new EPGException(
                        "Impossible de valider l'étape. Le dossier n'est pas dans un état 'initié' ou 'lancé'"
                    );
                }
            }
        } catch (EPGException ce) {
            LOGGER.info(session, SSLogEnumImpl.FAIL_VALIDATE_STEP_TEC, dossierDoc, ce);
            return handleErrorModifierDossier(
                lockService,
                response,
                principal,
                dossierDoc,
                routeDoc,
                ce,
                "La validation d'étape a echoué. "
            );
        } catch (NuxeoException ce) {
            LOGGER.error(session, SSLogEnumImpl.FAIL_VALIDATE_STEP_TEC, dossierDoc, ce);
            return handleErrorModifierDossier(
                lockService,
                response,
                principal,
                dossierDoc,
                routeDoc,
                ce,
                "La validation d'étape a echoué. "
            );
        }

        try {
            if (
                lockService.isLockByUser(session, dossierDoc, principal) &&
                lockService.isLockActionnableByUser(session, dossierDoc, principal)
            ) {
                lockService.unlockDoc(session, dossierDoc);
                if (routeDoc != null) {
                    lockService.unlockDoc(session, routeDoc);
                }
            } else if (!lockService.isLockByUser(session, dossierDoc, principal)) {
                throw new EPGException(
                    "L'utilisateur ne peut pas deverrouiller document. Le verrou n'a pas été posé par l'utilisateur"
                );
            }
        } catch (EPGException ce) {
            LOGGER.info(session, STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, ce);
            return handleErrorModifierDossierUnlockable(response, ce);
        } catch (NuxeoException ce) {
            LOGGER.error(session, STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, ce);
        }

        response.setStatut(TraitementStatut.OK);
        try {
            response.setDossier(WsUtil.getDossierEpgFromDossier(dossier, session, false));
        } catch (NuxeoException e) {
            response.setStatut(TraitementStatut.KO);
            response.setMessageErreur(
                "Erreur lors de la conversion du dossier modifié. Il est possible que la modification ait été prise en compte : " +
                e.getMessage()
            );
            return response;
        }

        // log l'action dans le journal d'administration
        logWebServiceActionDossier(
            SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_EVENT,
            SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_COMMENT_PARAM,
            dossier.getDocument()
        );

        return response;
    }

    private ModifierDossierResponse handleErrorModifierDossierUnlockable(
        final ModifierDossierResponse response,
        NuxeoException ce
    ) {
        response.setStatut(TraitementStatut.KO);
        response.setMessageErreur("Impossible de retirer le verrou du document : " + ce.getMessage());
        return response;
    }

    private ModifierDossierResponse handleErrorModifierDossier(
        final STLockService lockService,
        final ModifierDossierResponse response,
        final SSPrincipal principal,
        DocumentModel dossierDoc,
        DocumentModel routeDoc,
        NuxeoException ce,
        String messageErreur
    ) {
        response.setStatut(TraitementStatut.KO);
        response.setMessageErreur(messageErreur + ce.getMessage());
        tryUnlockDossier(response, session, dossierDoc, routeDoc, principal, lockService);
        return response;
    }

    // méthode qui permet de tenter de délocker un dossier et sa feuille de route lorsque le WS modifierDossier renvoie un KO
    private ModifierDossierResponse tryUnlockDossier(
        ModifierDossierResponse response,
        CoreSession session,
        DocumentModel dossierDoc,
        DocumentModel routeDoc,
        SSPrincipal principal,
        STLockService lockService
    ) {
        // Tentative de délocker le dossier
        try {
            if (
                lockService.isLockByUser(session, dossierDoc, principal) &&
                lockService.isLockActionnableByUser(session, dossierDoc, principal)
            ) {
                lockService.unlockDoc(session, dossierDoc);
                if (routeDoc != null) {
                    lockService.unlockDoc(session, routeDoc);
                }
            }
        } catch (NuxeoException ce) {
            response.setMessageErreur(
                response.getMessageErreur() + " L'utilisateur ne peut pas deverrouiller le document. " + ce
            );
        }
        return response;
    }

    private boolean isMimeTypeAccepted(List<String> formats, String mimeType) {
        MediaType mediaType = MediaType.fromMimeType(mimeType);
        return mediaType != null && !Collections.disjoint(formats, mediaType.supportedExtensions());
    }

    private DocumentModel getParentFromFichier(
        CoreSession session,
        DocumentModel fddRootDoc,
        Fichier fichier,
        FondDeDossierService fddService
    ) {
        String[] cheminFichier = StringUtils.split(fichier.getCheminFichier(), "/");
        // Le chemin doit comprendre au moins "Repertoire racine / fichier"
        if (cheminFichier.length < 2) {
            throw new EPGException(
                "Le fichier de fond de dossier n'a pas pu être créé. Le chemin du fichier du fond de dossier est incorrect : " +
                fichier.getCheminFichier()
            );
        }
        // Le folder name apparait avant le nom du fichier qui apparait en dernier
        DocumentModel folderParent = fddRootDoc;
        boolean rootChecked = false;
        if (!"Fond de dossier".equalsIgnoreCase(cheminFichier[0])) {
            DocumentModel firstFolder = session.getChild(folderParent.getRef(), cheminFichier[0]);
            if (firstFolder == null) {
                throw new EPGException(
                    "Le fichier de fond de dossier n'a pas pu être créé. Le chemin du fichier du fond de dossier est incorrect (le répertoire racine n'existe pas) : " +
                    fichier.getCheminFichier()
                );
            }
            folderParent = firstFolder;
            rootChecked = true;
        }

        // On parcourt tous les éléments jusqu'au répertoire parent donné par le WS
        for (int i = 1; i < cheminFichier.length - 1; i++) {
            DocumentModel folder = null;
            if (session.hasChildren(folderParent.getRef())) {
                DocumentModelList children = session.getChildren(folderParent.getRef());
                for (DocumentModel child : children) {
                    if (cheminFichier[i].equals(child.getTitle())) {
                        folder = child;
                        break;
                    }
                }
            }

            if (folder == null && !rootChecked) {
                throw new EPGException(
                    "Le fichier de fond de dossier n'a pas pu être créé. Le chemin du fichier du fond de dossier est incorrect (le répertoire racine n'existe pas) : " +
                    fichier.getCheminFichier()
                );
            } else if (!rootChecked) {
                rootChecked = true;
            }

            if (folder == null) {
                // Si le repertoire n'existe pas, on le créé
                folder = fddService.createFondDeDossierRepertoire(session, folderParent, cheminFichier[i]);
                session.save();
            }
            folderParent = folder;
        }
        return folderParent;
    }

    /**
     * remplit la partie conseil d'état d'un dossier à partir de la "section Ce" envoyé par le webservice.
     *
     * @param conseilEtat
     * @param sectionCe
     */
    private void fillConseilEtatFromSectionCeWsUnrestricted(
        final ConseilEtat conseilEtat,
        final SectionCe sectionCe,
        Boolean modifierDossier
    ) {
        final JournalService journalService = STServiceLocator.getJournalService();

        if (StringUtils.isNotEmpty(sectionCe.getSectionCe())) {
            if (modifierDossier) {
                // Changement de section - on ajoute une nouvelle étape pour avis CE assigné à la nouvelle section et on
                // valide l'étape en cours avec le statut non concerné - FEV505
                changementSectionCE(conseilEtat, sectionCe);
            } else {
                conseilEtat.setSectionCe(sectionCe.getSectionCe());
            }
        }
        // note : l'utilisateur renvoie la cilité ,nom et prénom du rapporteur et pas son id;
        if (CollectionUtils.isNotEmpty(sectionCe.getRapporteur())) {
            conseilEtat.setRapporteurCe(StringUtils.join(sectionCe.getRapporteur(), ", "));
            // inscription dans le journal technique - FEV505
            journalService.journaliserActionAdministration(
                session,
                conseilEtat.getDocument(),
                SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT,
                "Mise à jour de(s) rapporteur(s)"
            );
        }

        if (sectionCe.getDateTransmissionSectionCe() != null) {
            conseilEtat.setDateTransmissionSectionCe(sectionCe.getDateTransmissionSectionCe().toGregorianCalendar());
        }
        if (sectionCe.getDateSectionCe() != null) {
            conseilEtat.setDateSectionCe(sectionCe.getDateSectionCe().toGregorianCalendar());
            // inscription dans le journal technique - FEV505
            journalService.journaliserActionAdministration(
                session,
                conseilEtat.getDocument(),
                SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT,
                "Mise à jour de la date de la séance de la section"
            );
        }
        if (sectionCe.getDateAgCe() != null) {
            conseilEtat.setDateAgCe(sectionCe.getDateAgCe().toGregorianCalendar());
            // inscription dans le journal technique - FEV505
            journalService.journaliserActionAdministration(
                session,
                conseilEtat.getDocument(),
                SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT,
                "Mise à jour de la date de l'assemblée générale"
            );
        }
        // Correction Mantis 156992
        if (StringUtils.isNotBlank(sectionCe.getNumeroIsa())) {
            conseilEtat.setNumeroISA(sectionCe.getNumeroIsa());
            // inscription dans le journal technique - M156991
            journalService.journaliserActionAdministration(
                session,
                conseilEtat.getDocument(),
                SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT,
                "Mise à jour du numéro ISA"
            );
        }

        if (sectionCe.getPriorite() != null) {
            conseilEtat.setPriorite(sectionCe.getPriorite().toString());
            // inscription dans le journal technique
            journalService.journaliserActionAdministration(
                session,
                conseilEtat.getDocument(),
                SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT,
                "Mise à jour de la priorité"
            );
        }

        final Dossier dossier = conseilEtat.getDocument().getAdapter(Dossier.class);

        // correction redmin 7628
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        final String typeActeId = dossier.getTypeActe();
        final ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
        final ActiviteNormative activiteNormative = activiteNormativeService.findActiviteNormativeByNor(
            dossier.getNumeroNor(),
            session
        );
        if (activiteNormative != null) {
            if (acteService.isDecret(typeActeId)) {
                final Decret decret = activiteNormative.getDocument().getAdapter(Decret.class);
                if (!decret.isDateSaisineCELocked() && sectionCe.getDateSectionCe() != null) {
                    decret.setDateSectionCe(sectionCe.getDateSectionCe().toGregorianCalendar());
                }
                if (!decret.isSectionCeLocked()) {
                    decret.setSectionCe(sectionCe.getSectionCe());
                }
                if (!decret.isRapporteurCeLocked()) {
                    decret.setRapporteurCe(StringUtils.join(sectionCe.getRapporteur(), ", "));
                }
                // Correction Mantis 156992
                if (StringUtils.isNotBlank(sectionCe.getNumeroIsa())) {
                    decret.setReferenceAvisCE(sectionCe.getNumeroIsa());
                }
                // corection Mantis 146013
                if (decret.getDateSectionCe() == null) {
                    decret.setDateSectionCe(Calendar.getInstance());
                }
                if (decret.getDateSortieCE() == null) {
                    decret.setDateSortieCE(Calendar.getInstance());
                }
                if (decret.getDateSaisineCE() == null) {
                    decret.setDateSaisineCE(Calendar.getInstance());
                }
                decret.save(session);
            } else if (acteService.isLoi(typeActeId)) {
                final LoiDeRatification loiDeRatification = activiteNormative
                    .getDocument()
                    .getAdapter(LoiDeRatification.class);
                if (!loiDeRatification.isSectionCELocked()) {
                    loiDeRatification.setSectionCE(sectionCe.getSectionCe());
                }
                if (!loiDeRatification.isDateExamenCELocked() && sectionCe.getDateSectionCe() != null) {
                    loiDeRatification.setDateExamenCE(sectionCe.getDateSectionCe().toGregorianCalendar());
                }

                if (loiDeRatification.getDateExamenCE() == null) {
                    loiDeRatification.setDateExamenCE(Calendar.getInstance());
                }
                if (loiDeRatification.getDateSaisineCE() == null) {
                    loiDeRatification.setDateSaisineCE(Calendar.getInstance());
                }

                loiDeRatification.save(session);
            }

            session.save();
        }
        // Envoi d'un mail aux users qui l'ont souhaité car on a eu une MAJ du CE sur ce dossier
        if (modifierDossier) {
            try {
                sendMailMajCE(conseilEtat.getDocument());
            } catch (NuxeoException e) {
                LOGGER.error(STLogEnumImpl.FAIL_SEND_MAIL_TEC);
            }
        }

        // Event de rattachement de l'activite normative (post commit)
        final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(
            SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM,
            conseilEtat.getDocument().getId()
        );
        final InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT));

        // enregistrement des modifications dans une session unrestricted
        new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(conseilEtat.getDocument());
    }

    /**
     * Retourne la liste des dossiers WS avec les fichiers du parapheur et du répertoire publiques du fond de dossier à
     * partir de la liste des dossiers + la liste des nors des dossiers de la liste, le resultat du traitement et le
     * message d'erreur si présent
     *
     * @param dossierDocList
     *            liste des dossiers
     * @param session
     *            session
     * @param norTrouve
     * @param list
     * @return la liste des dossiers WS avec les fichiers du parapheur et du répertoire publiques du fond de dossier
     * @throws NuxeoException
     * @throws IOException
     */
    protected void getDossierEpgWithFileListFromDossier(
        final List<DocumentModel> dossierDocList,
        final CoreSession session,
        final List<String> norTrouve,
        final List<DossierEpgWithFile> list
    )
        throws IOException {
        for (final DocumentModel dossierDoc : dossierDocList) {
            if (!session.hasPermission(dossierDoc.getRef(), SecurityConstants.READ)) {
                // on n'a pas les droits de voir le dossier : on ne renvoie rien
            } else {
                final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                list.add(WsUtil.getDossierEpgWithFileFromDossier(session, dossier, false));
                if (norTrouve != null) {
                    norTrouve.add(dossier.getNumeroNor());
                }
            }
        }
    }

    /**
     * Retourne la liste des modifications des dossiers WS avec les fichiers du parapheur et du répertoire publiques du
     * fond de dossier à partir de la liste des dossiers + la liste des nors des dossiers de la liste, le resultat du
     * traitement et le message d'erreur si présent
     *
     * @param dossierDocList
     *            liste des dossiers
     * @param session
     *            session
     * @param norTrouve
     * @param list
     * @return la liste des dossiers WS avec les fichiers du parapheur et du répertoire publiques du fond de dossier
     * @throws NuxeoException
     * @throws IOException
     */
    protected void getDossierModificationEpgFromDossier(
        final List<DocumentModel> dossierDocList,
        final CoreSession session,
        final List<String> norTrouve,
        final List<DossierModification> list
    )
        throws IOException {
        for (final DocumentModel dossierDoc : dossierDocList) {
            if (!session.hasPermission(dossierDoc.getRef(), SecurityConstants.READ)) {
                // on n'a pas les droits de voir le dossier : on ne renvoie rien
            } else {
                final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                list.add(WsUtil.getModificationDossierEpgFromDossier(session, dossier));
                if (norTrouve != null) {
                    norTrouve.add(dossier.getNumeroNor());
                }
            }
        }
    }

    /**
     * Retourne la liste des dossiers WS à partir de la liste des dossiers + la liste des nors des dossiers de la liste,
     * le resultat du traitement et le message d'erreur si présent
     *
     * @param dossierDocList
     * @param traitementStatut
     * @param messageErreur
     * @param norTrouve
     * @return
     */
    protected List<DossierEpg> getDossierEpgListFromDossier(
        final List<DocumentModel> dossierDocList,
        final List<String> norTrouve
    ) {
        final List<DossierEpg> dossierEpgList = new ArrayList<>();
        for (final DocumentModel documentModel : dossierDocList) {
            final Dossier dossier = documentModel.getAdapter(Dossier.class);
            if (norTrouve != null) {
                norTrouve.add(dossier.getNumeroNor());
            }
            dossierEpgList.add(WsUtil.getDossierEpgFromDossier(dossier, session, false));
        }

        return dossierEpgList;
    }

    /**
     * Retourne la liste des dossiers WS à partir de la liste des dossiers + le resultat du traitement et le message
     * d'erreur si présent
     *
     * @param dossierDocList
     * @param traitementStatut
     * @param messageErreur
     * @return
     */
    protected List<DossierEpg> getDossierEpgListFromDossier(final List<DocumentModel> dossierDocList) {
        return getDossierEpgListFromDossier(dossierDocList, null);
    }

    /**
     * Logge l'action effectue par webservice dans le journal d'administration.
     */
    protected void logWebServiceAction(final String name, final String comment) {
        final JournalService journalService = STServiceLocator.getJournalService();
        // log du webservice dans le journal d'administration
        try {
            journalService.journaliserActionAdministration(session, name, comment);
        } catch (final NuxeoException e) {
            LOGGER.error(
                session,
                STLogEnumImpl.FAIL_LOG_TEC,
                "Erreur lors de l'envoi d'un log pour le journal technique. Nom de l'event" + name,
                e
            );
        }
    }

    /**
     * Log l'action effectuée par webservice dans le journal d'administration en lien avec le dossier concerné
     *
     * @param name
     *            nom de l'évènement de log
     * @param comment
     *            commentaire qui sera rédigé dans le log
     * @param dossier
     *            dossier auquel on ajoute le log
     */
    protected void logWebServiceActionDossier(final String name, final String comment, final DocumentModel dossierDoc) {
        final JournalService journalService = STServiceLocator.getJournalService();
        // log du webservice dans le journal du dossier
        try {
            journalService.journaliserActionAdministration(session, dossierDoc, name, comment);
        } catch (final NuxeoException e) {
            LOGGER.error(
                session,
                STLogEnumImpl.FAIL_LOG_TEC,
                "erreur lors de l'envoi d'un log pour le journal du dossier. Nom de l'event" + name,
                e
            );
        }
    }

    /**
     * Web service chercherModificationDossier
     */
    @Override
    public ChercherModificationDossierResponse chercherModificationDossierEpg(
        final ChercherModificationDossierRequest request
    ) {
        final JetonService jetonService = SolonEpgServiceLocator.getJetonService();
        String jetonRecu = request.getJeton();
        final List<String> norList = checkNors(request.getNor());

        LOGGER.info(
            session,
            EpgLogEnumImpl.INIT_WS_CHERCHER_DOSSIER,
            "jeton = '" + jetonRecu + "', liste nors = '" + norList + "'"
        );
        // initialisation de la variable réponse
        final ChercherModificationDossierResponse chercherModificationDossierResponse = new ChercherModificationDossierResponse();
        // par defaut, le statut est à KO
        chercherModificationDossierResponse.setStatut(TraitementStatut.KO);
        // verification des droits de l'utilisateur pour le service
        final List<String> userGroupList = session.getPrincipal().getGroups();
        if (!userGroupList.contains(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER)) {
            chercherModificationDossierResponse.setMessageErreur(WS_ACCESS_DENIED);
            return chercherModificationDossierResponse;
        }
        // par defaut, "Dernier envoi" est à "vrai" et "jeton" est à "-1" et le statut est à KO
        chercherModificationDossierResponse.setDernierEnvoi(true);
        chercherModificationDossierResponse.setJeton("-1");

        final List<String> numeroISA = checkNumerosISA(request.getNumeroIsa());

        if (CollectionUtils.isEmpty(norList) && StringUtils.isEmpty(jetonRecu) && CollectionUtils.isEmpty(numeroISA)) {
            LOGGER.warn(
                session,
                STLogEnumImpl.FAIL_GET_PARAM_TEC,
                "Aucun numero NOR ni jeton ni numéro ISA passé en paramètre"
            );
            chercherModificationDossierResponse.setDernierEnvoi(true);
            chercherModificationDossierResponse.setMessageErreur(
                "aucun numero NOR ni jeton ni numéro ISA passé en paramètre"
            );
            return chercherModificationDossierResponse;
        }

        List<DocumentModel> dossierDocList;
        List<DocumentModel> jetonDocList;
        Map<String, DocumentModel> idsDossierMap = new HashMap<>();

        if (StringUtils.isNotEmpty(jetonRecu)) {
            // on recupère les dossiers via le jeton
            final Long numeroJeton = Long.parseUnsignedLong(jetonRecu);
            JetonServiceDto jetonServiceDto;
            try {
                jetonServiceDto =
                    jetonService.getDocuments(
                        session,
                        TableReference.MINISTERE_CE,
                        numeroJeton,
                        STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER
                    );
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, numeroJeton, e);
                chercherModificationDossierResponse.setMessageErreur(WS_JETON_DOSSIERS_NOT_FOUND);
                return chercherModificationDossierResponse;
            } catch (final OutOfMemoryError err) {
                sendOutOfMemoryMail(err, jetonRecu);
                chercherModificationDossierResponse.setMessageErreur(WS_JETON_DOSSIERS_NOT_FOUND);
                return chercherModificationDossierResponse;
            }
            // récupération jetonServiceDto
            if (jetonServiceDto == null) {
                LOGGER.warn(session, STLogEnumImpl.FAIL_GET_JETON_TEC, numeroJeton);
                chercherModificationDossierResponse.setMessageErreur(
                    "Erreur : Impossible de récupérer des documents. Veuillez saisir un jeton adéquat."
                );
                return chercherModificationDossierResponse;
            }
            // récupération jeton sortant
            String jetonSortant = Long.toString(numeroJeton);
            if (jetonServiceDto.getNextJetonNumber() != null) {
                jetonSortant = jetonServiceDto.getNextJetonNumber().toString();
            }
            chercherModificationDossierResponse.setJeton(jetonSortant);
            // récupération dernier envoi
            if (jetonServiceDto.isLastSending() != null) {
                chercherModificationDossierResponse.setDernierEnvoi(jetonServiceDto.isLastSending());
            }
            // récupération document
            dossierDocList = jetonServiceDto.getDocumentList();
            for (DocumentModel dossierDoc : dossierDocList) {
                idsDossierMap.put(dossierDoc.getId(), dossierDoc);
            }
            jetonDocList = jetonServiceDto.getJetonDocDocList();

            // On va parcourir la liste des jetons qu'on a trouvé pour regarder à quelle modification ça correspond
            // et créer le dossier modification correspondant
            manageJetonsDocList(session, chercherModificationDossierResponse, jetonDocList, idsDossierMap);
        } else if (CollectionUtils.isNotEmpty(norList) || CollectionUtils.isNotEmpty(numeroISA)) {
            try {
                dossierDocList = getDossiersByNorIsa(session, norList, numeroISA);
            } catch (final NuxeoException e) {
                if (CollectionUtils.isNotEmpty(norList)) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, StringHelper.join(norList, ", ", ""), e);
                } else {
                    LOGGER.error(
                        session,
                        STLogEnumImpl.FAIL_GET_DOSSIER_TEC,
                        StringHelper.join(numeroISA, ", ", ""),
                        e
                    );
                }
                chercherModificationDossierResponse.setStatut(TraitementStatut.KO);
                chercherModificationDossierResponse.setMessageErreur("Erreur lors de la récupération des dossiers");
                return chercherModificationDossierResponse;
            }
            if (CollectionUtils.isEmpty(dossierDocList)) {
                chercherModificationDossierResponse.setDernierEnvoi(true);
                chercherModificationDossierResponse.setMessageErreur("Aucun résultat retourné par la requête");
                return chercherModificationDossierResponse;
            }

            for (DocumentModel dossierDoc : dossierDocList) {
                idsDossierMap.put(dossierDoc.getId(), dossierDoc);
            }

            final StringBuilder chercherJetonQuery = new StringBuilder("SELECT j.ecm:uuid AS id FROM ")
                .append(STConstant.JETON_DOC_TYPE)
                .append(" AS j WHERE j.jtd:id_doc IN (")
                .append(StringHelper.getQuestionMark(idsDossierMap.size()))
                .append(") AND j.jtd:type_webservice = ? ORDER BY j.jtd:created");
            List<Object> params = new ArrayList<>(idsDossierMap.keySet());
            params.add(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER);

            try {
                jetonDocList = doUnrestrictedQuery(session, chercherJetonQuery.toString(), params);
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_JETON_TEC, StringHelper.join(norList, ", ", ""), e);
                chercherModificationDossierResponse.setStatut(TraitementStatut.KO);
                chercherModificationDossierResponse.setMessageErreur("Erreur lors de la récupération des jetons");
                return chercherModificationDossierResponse;
            }

            manageJetonsDocList(session, chercherModificationDossierResponse, jetonDocList, idsDossierMap);
        }
        // Si la liste est null, il y a eu une erreur dans le traitement
        if (chercherModificationDossierResponse.getDossierModification() == null) {
            chercherModificationDossierResponse.setStatut(TraitementStatut.KO);
        } else {
            // Si pas de documents, c'est qd même un succès
            // Si on a trouvé des documents on met le statut à OK
            chercherModificationDossierResponse.setStatut(TraitementStatut.OK);
        }

        return chercherModificationDossierResponse;
    }

    @Override
    public boolean isChercherModificationDossierPublicationActivated(CoreSession session) {
        STParametreService paramService = STServiceLocator.getSTParametreService();
        String flagActivatedOption = paramService.getParametreValue(
            session,
            SolonEpgParametreConstant.ACTIVATION_ENVOI_INFO_PUBLICATION_CE
        );
        return "true".equalsIgnoreCase(flagActivatedOption);
    }

    /**
     * Parcourt les liste de jetonDoc afin de créer la liste de dossiermodification correspondants.
     * On ne remonte que la dernière priorisation en utilisant l'ordre de récupération des jetons (triés par date création)
     * @param session
     * @param response
     * @param jetonDocList
     * @param idsDossierMap
     */
    private void manageJetonsDocList(
        final CoreSession session,
        final ChercherModificationDossierResponse response,
        final List<DocumentModel> jetonDocList,
        final Map<String, DocumentModel> idsDossierMap
    ) {
        // On ne veut que la dernière priorisation du dossier.
        // Les jetons sont récupérés trié par date de création
        Map<String, DossierModification> idDossierModifPriorisation = new HashMap<>();
        Set<String> dossiersTraites = new HashSet<>();
        for (DocumentModel jetonDoc : jetonDocList) {
            JetonDoc jeton = jetonDoc.getAdapter(JetonDoc.class);
            DocumentModel dossierDoc = idsDossierMap.get(jeton.getIdDoc());
            DossierModification dossierModif = manageJetonDossierModification(session, jetonDoc, dossierDoc);
            if (dossierModif != null) {
                // les jetons sont triés par date de création, on ne garde que le dernier
                if (TypeModification.PRIORISATION.name().equals(jeton.getTypeModification())) {
                    idDossierModifPriorisation.put(dossierDoc.getId(), dossierModif);
                } else {
                    response.getDossierModification().add(dossierModif);
                }
                if (!dossiersTraites.contains(dossierDoc.getId())) {
                    logWebServiceActionDossier(
                        SolonEpgEventConstant.WEBSERVICE_CHERCHER_MODIFICATION_DOSSIER_CE_EVENT,
                        SolonEpgEventConstant.WEBSERVICE_CHERCHER_MODIFICATION_DOSSIER_CE_COMMENT_PARAM,
                        dossierDoc
                    );
                    dossiersTraites.add(dossierDoc.getId());
                }
            }
        }
        if (!idDossierModifPriorisation.isEmpty()) {
            response.getDossierModification().addAll(idDossierModifPriorisation.values());
        }
    }

    /**
     * Récupère une liste de dossier soit par numéro nor ou par numéros ISA.
     * @param session
     * @param norList liste des numéro NOR à récupérer. si cette liste est renseignée, c'est celle utilisée.
     * @param numerosISA liste des numéro ISA à récupérer. Utilisée si la liste des numéro nor est vide
     * @return une liste vide si les deux listes passées en paramètre sont vides. La liste de dossier correspondant sinon.
     */
    private List<DocumentModel> getDossiersByNorIsa(
        final CoreSession session,
        final List<String> norList,
        final List<String> numerosISA
    ) {
        StringBuilder chercherDossierQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ")
            .append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)
            .append(" AS l WHERE ");
        List<String> paramList;
        if (CollectionUtils.isNotEmpty(norList)) {
            // recherche par numéro de NOR
            // On a besoin de récupérer les id des dossiers pour ensuite effectuer une recherche dans la table
            // jetonDoc
            chercherDossierQuery.append("l.dos:numeroNor IN (").append(StringHelper.getQuestionMark(norList.size()));
            paramList = norList;
        } else if (CollectionUtils.isNotEmpty(numerosISA)) {
            // On recherche par le numéro ISA
            chercherDossierQuery
                .append("l.consetat:numeroISA  IN (")
                .append(StringHelper.getQuestionMark(numerosISA.size()));
            paramList = numerosISA;
        } else {
            return new ArrayList<>();
        }
        chercherDossierQuery.append(")");
        return doUnrestrictedQuery(session, chercherDossierQuery.toString(), paramList);
    }

    private List<DocumentModel> doUnrestrictedQuery(
        CoreSession session,
        String query,
        List<? extends Object> paramList
    ) {
        return CoreInstance.doPrivileged(
            session,
            systemSession -> {
                List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                    systemSession,
                    query,
                    paramList.toArray(new String[0])
                );
                docs.forEach(d -> d.detach(true));
                return docs;
            }
        );
    }

    /**
     * Génère l'objet DossierModification lié à un jeton et son dossier associé
     * @param session
     * @param jetonDoc
     * @param dossierDoc
     * @param typeModifIdDossModif la map reliant le type de modification (envoi pc, saisine, priorisation...) et le lien entre id dossier et dossier modification
     * @return le dossierModification ou null
     * @throws IOException
     */
    private DossierModification manageJetonDossierModification(
        final CoreSession session,
        final DocumentModel jetonDoc,
        final DocumentModel dossierDoc
    ) {
        JetonDoc jeton = jetonDoc.getAdapter(JetonDoc.class);
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);
        String typeModification = jeton.getTypeModification();
        DossierModification dossierModif = new DossierModification();

        dossierModif.setTypeModification(TypeModification.valueOf(typeModification));
        dossierModif.setNumeroIsa(conseilEtat.getNumeroISA());
        dossierModif.setNumeroNor(dossier.getNumeroNor());
        Calendar dateCreationJeton = jeton.getCreated();
        if (dateCreationJeton != null) {
            dossierModif.setDateModification(
                DateUtil.calendarToXMLGregorianCalendar(DateUtils.truncate(dateCreationJeton, Calendar.SECOND))
            );
        }

        List<String> idsComplementaires = jeton.getIdsComplementaires();
        switch (TypeModification.valueOf(typeModification)) {
            case PRIORISATION:
                if (CollectionUtils.isNotEmpty(idsComplementaires)) {
                    dossierModif.setPriorite(new BigInteger(idsComplementaires.get(0)));
                } else {
                    // Pas de priorité renseignée, on ne renvoit pas de dossierModification à renseigner en retour ws
                    dossierModif = null;
                }
                break;
            case REATTRIBUTION:
                dossierModif.setNouveauNor(dossier.getNumeroNor());
                if (CollectionUtils.isNotEmpty(idsComplementaires)) {
                    dossierModif.setNumeroNor(idsComplementaires.get(0));
                    if (idsComplementaires.size() >= 2) {
                        dossierModif.setNouveauNor(idsComplementaires.get(1));
                    }
                }
                break;
            case PUBLICATION:
                dossierModif.setRetourPublication(manageJetonInformationPublication(session, dossier));
                break;
            case SAISINE_RECTIFICATIVE:
            case PIECE_COMPLEMENTAIRE:
                setFilesDossierModif(session, dossierModif, idsComplementaires);
                break;
            default:
                break;
        }
        return dossierModif;
    }

    /**
     * Génère l'objet InformationPublication lié à un jeton et son dossier associé
     * @param session
     * @param dossier
     * @return InformationPublication ou null
     */
    private InformationPublication manageJetonInformationPublication(final CoreSession session, final Dossier dossier) {
        InformationPublication informationPublication = new InformationPublication();

        if (isChercherModificationDossierPublicationActivated(session)) {
            XMLGregorianCalendar datePublicationJO = null;
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (retourDila != null && retourDila.getDateParutionJorf() != null) {
                try {
                    datePublicationJO =
                        DatatypeFactory
                            .newInstance()
                            .newXMLGregorianCalendar(sdf.format(retourDila.getDateParutionJorf().getTime()));
                } catch (DatatypeConfigurationException e) {
                    LOGGER.info(session, STLogEnumImpl.LOG_EXCEPTION_TEC, e);
                }
            }
            if (datePublicationJO != null) {
                informationPublication.setDateParution(datePublicationJO);
            }
            if (retourDila != null) {
                if (retourDila.getTitreOfficiel() != null) {
                    informationPublication.setTitreOfficiel(retourDila.getTitreOfficiel());
                }
                if (retourDila.getNumeroTexteParutionJorf() != null) {
                    informationPublication.setNumeroTexte(retourDila.getNumeroTexteParutionJorf());
                }
                if (retourDila.getPageParutionJorf() != null) {
                    informationPublication.setPage(retourDila.getPageParutionJorf().intValue());
                }
            }
        }
        return informationPublication;
    }

    /**
     * Récupère les documents version et les ajoute à l'objet DossierModification
     * @param session
     * @param dossierModif
     * @param idsVersionsDocs
     */
    private void setFilesDossierModif(
        final CoreSession session,
        DossierModification dossierModif,
        List<String> idsVersionsDocs
    ) {
        for (String idVersionDoc : idsVersionsDocs) {
            DocumentModel versionDoc = null;
            try {
                versionDoc = session.getDocument(new IdRef(idVersionDoc));
            } catch (NuxeoException ce) {
                LOGGER.warn(
                    session,
                    STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
                    "versionDocId " + idVersionDoc + " non trouvé.",
                    ce
                );
            }
            if (versionDoc != null) {
                Fichier fichier = new Fichier();
                FondDeDossierFile fondDossFile = versionDoc.getAdapter(FondDeDossierFile.class);
                fichier.setNom(fondDossFile.getFilename());
                String cheminFichier = versionDoc.getPathAsString();
                String[] cheminFichierList = cheminFichier.split(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);
                if (cheminFichierList.length > 1) {
                    // On fait un substring de 1 pour virer le / au début
                    fichier.setCheminFichier(cheminFichierList[1].substring(1));
                }
                Blob blobFile = fondDossFile.getContent();
                if (blobFile != null) {
                    fichier.setMimeType(blobFile.getMimeType());
                    try {
                        fichier.setContenu(blobFile.getByteArray());
                    } catch (IOException ioe) {
                        LOGGER.warn(
                            session,
                            STLogEnumImpl.FAIL_GET_FILE_TEC,
                            "Contenu non trouvé pour la versionId " + idVersionDoc,
                            ioe
                        );
                    }
                    fichier.setTailleFichier((int) blobFile.getLength());
                }
                fichier.setVersion(fondDossFile.getMajorVersion().toString());
                dossierModif.getFichier().add(fichier);
            }
        }
    }

    /**
     * Méthode qui est appelée lors d'une modification de sectionCE : Création nouvelle étape 'Pour avis CE' affectée à
     * la nouvelle section + Validation de l'étape en cours avec le statut non concerné - FEV505
     *
     * @param conseilEtat
     * @param sectionCe
     */
    public void changementSectionCE(final ConseilEtat conseilEtat, final SectionCe sectionCe) {
        Boolean sectionCorrecte = false;
        // Ici on effectue un changement de section
        // On récupère la section
        if (
            ConseilEtatConstants.sectionConseilEtatList.contains(sectionCe.getSectionCe()) ||
            ConseilEtatConstants.sectionConseilEtatListShort.contains(sectionCe.getSectionCe().toUpperCase())
        ) {
            sectionCorrecte = true;
        }
        if (!sectionCorrecte) {
            // La section CE renseignée est incorrecte. On renvoi donc un message d'erreur
            throw new NuxeoException("La section renseignée est incorrecte");
        } else {
            // La section est correcte, on passe donc à la suite
            // Récupération de l'id du ministère CE
            DocumentModel tableReferenceDoc = SolonEpgServiceLocator
                .getTableReferenceService()
                .getTableReference(session);
            TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
            String ministereCeId = tableReference.getMinistereCE();
            final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
            final STUsAndDirectionService stUsAndDirectionService = STServiceLocator.getSTUsAndDirectionService();
            final EntiteNode ministereCENode = ministeresService.getEntiteNode(ministereCeId);
            List<UniteStructurelleNode> listDirectionCE = stUsAndDirectionService.getDirectionListFromMinistere(
                ministereCENode
            );
            UniteStructurelleNode nouvelleSectionCE = null;
            // Récupération de la direction
            for (UniteStructurelleNode unite : listDirectionCE) {
                // on gère le cas de l'intérieur, qui peut s'écrire sans accent
                if (
                    unite.getLabel().toUpperCase().contains(sectionCe.getSectionCe().toUpperCase()) ||
                    sectionCe
                        .getSectionCe()
                        .toUpperCase()
                        .contains(ConseilEtatConstants.CONSEIL_ETAT_SECTION_INTERIEUR_SHORT) &&
                    unite.getLabel().equals(ConseilEtatConstants.CONSEIL_ETAT_SECTION_INTERIEUR)
                ) {
                    nouvelleSectionCE = unite;
                    break;
                }
            }
            if (nouvelleSectionCE == null) {
                throw new NuxeoException("La section renseignée est incorrecte");
            }
            // Récupération du premier poste trouvé
            STPostesService postService = STServiceLocator.getSTPostesService();
            List<String> listPostes = postService.getPosteIdInSubNode(nouvelleSectionCE);
            String idPoste = listPostes.get(0);
            // Récupération du document
            DocumentModel dossierDoc = conseilEtat.getDocument();
            EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            // récupère le dossierLink lié au dossier
            DocumentModel dossierLinkDoc = null;
            List<DocumentModel> dossierLinkDocList = null;
            try {
                dossierLinkDocList = corbeilleService.findDossierLink(session, dossierDoc.getId());
            } catch (final NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierDoc, e);
                throw new NuxeoException(
                    "Erreur lors de la récupération des dossiers link liés au dossier : " + e.getMessage()
                );
            }
            if (dossierLinkDocList != null) {
                for (final DocumentModel dossierLinkDocElement : dossierLinkDocList) {
                    // on verifie que le dossierLink est bien à l'étape "pour avis ce"
                    final DossierLink dossierLink = dossierLinkDocElement.getAdapter(DossierLink.class);
                    if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(dossierLink.getRoutingTaskType())) {
                        dossierLinkDoc = dossierLinkDocElement;
                        break;
                    }
                }
            }
            if (dossierLinkDoc != null) {
                // l'étape en cours est bien de type 'Pour avis CE'
                final STLockService lockService = STServiceLocator.getSTLockService();
                final SSPrincipal principal = (SSPrincipal) session.getPrincipal();
                final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
                final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
                final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
                final DocumentModel etapeCouranteDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
                final Filter routeStepFilter = new RouteStepValideFilter();
                // Techniquement on n'en a pas besoin, mais ça permet de renseigner la position de notre étape
                // actuelle
                @SuppressWarnings("unused")
                final DocumentModel etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(
                    session,
                    etapeCouranteDoc,
                    routeStepFilter,
                    true
                );
                // Récupère la position de l'étape en cours
                final Integer currentStepPos = (Integer) etapeCouranteDoc.getContextData(STConstant.POS_DOC_CONTEXT);

                EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
                DocumentModel routeDoc = null;
                STDossier dossier = dossierDoc.getAdapter(STDossier.class);
                String lastRouteId = dossier.getLastDocumentRoute();
                if (lastRouteId != null) {
                    routeDoc = session.getDocument(new IdRef(lastRouteId));
                }
                // Opération de verouillage
                try {
                    if (
                        !lockService.isLockByUser(session, dossierDoc, principal) &&
                        lockService.isLockActionnableByUser(session, dossierDoc, principal)
                    ) {
                        lockService.lockDoc(session, dossierDoc);
                        // On récupère aussi la feuille de route pour la verouiller
                        if (routeDoc != null) {
                            lockService.lockDoc(session, routeDoc);
                        }
                    } else if (!lockService.isLockByUser(session, dossierDoc, principal)) {
                        throw new EPGException("L'utilisateur ne peut pas obtenir le verrou sur ce document");
                    }
                } catch (NuxeoException ce) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_LOCK_DOC_TEC, ce);
                    throw new NuxeoException("Erreur lors du verrouillage du dossier");
                }

                // Création d'une nouvelle étape de type 'Pour avis CE' affecté au poste en question final
                FeuilleRouteStep nouvelleEtapePourAvisCERouteStep = documentRoutingService.createNewRouteStep(
                    session,
                    STConstant.PREFIX_POSTE + idPoste,
                    VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE
                );
                documentRoutingService.addRouteElementToRoute(
                    etapeCouranteDoc.getParentRef(),
                    currentStepPos + 1,
                    nouvelleEtapePourAvisCERouteStep,
                    session
                );

                session.save();
                session.saveDocument(dossierDoc);
                session.saveDocument(routeDoc);

                List<DocumentModel> nextStep;
                try {
                    nextStep =
                        feuilleRouteService.findNextSteps(
                            session,
                            dossier.getLastDocumentRoute(),
                            etapeCouranteDoc,
                            null
                        );
                    if (CollectionUtils.isEmpty(nextStep)) {
                        throw new EPGException("Il n'existe pas d'étape à venir pour le dossier");
                    }
                } catch (NuxeoException exc) {
                    LOGGER.error(session, SSLogEnumImpl.FAIL_GET_STEP_TEC, dossierDoc, exc);
                }

                // On valide ensuie l'étape
                dossierDistributionService.updateStepValidationStatus(
                    session,
                    etapeCouranteDoc,
                    SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_NON_CONCERNE_VALUE,
                    dossierDoc
                );
                dossierDistributionService.validerEtapeNonConcerne(session, dossierDoc, dossierLinkDoc);

                // Puis on renseigne la nouvelle section CE
                conseilEtat.setSectionCe(sectionCe.getSectionCe());

                // Et on libère le verrou
                try {
                    if (
                        lockService.isLockByUser(session, dossierDoc, principal) &&
                        lockService.isLockActionnableByUser(session, dossierDoc, principal)
                    ) {
                        lockService.unlockDoc(session, dossierDoc);
                        if (routeDoc != null) {
                            lockService.unlockDoc(session, routeDoc);
                        }
                    } else if (!lockService.isLockByUser(session, dossierDoc, principal)) {
                        throw new EPGException("L'utilisateur ne peut pas déverouiller ce document");
                    }
                } catch (NuxeoException ce) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, ce);
                    throw new NuxeoException("Erreur lors du déverrouillage du dossier");
                }
                // Sauvegarde
                session.save();
                session.saveDocument(dossierDoc);
            } else {
                throw new NuxeoException("L'étape courrante n'est pas de type 'Pour avis CE'");
            }
        }
    }

    /**
     * Envoi d'une alerte mail pour les utilisateurs dont le dossier est passé dans leur corbeille et qui ont coché la
     * notification par mail dans leur profil
     */
    public void sendMailMajCE(DocumentModel dossierDoc) {
        // on crée une liste d'utilisateur pour ne pas envoyer plusieurs mail à la même personne si elle est dans
        // plusieurs postes
        List<STUser> listUserMail = new ArrayList<>();
        // On va les parcourir pour arriver jusqu'à notre étape 'Pour avis CE' (celle qu'on vient de valider)
        for (SSRouteStep routeStep : getAllDoneSteps(dossierDoc)) {
            if (routeStep != null) {
                String mailboxId = routeStep.getDistributionMailboxId();
                // récupération de la mailbox
                String posteIdPasse = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(mailboxId);
                // Si null, on passe à l'étape suivante
                if (posteIdPasse == null) {
                    continue;
                }
                PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteIdPasse);
                // idem
                if (posteNode == null) {
                    continue;
                }
                List<STUser> userList = posteNode.getUserList();
                // On parcours la liste des utilisateurs
                for (STUser user : userList) {
                    // On veux déterminer si l'utilisateur a coché l'option pour l'envoi de mail quand
                    // changement de
                    // section CE
                    final StringBuilder builder = new StringBuilder("SELECT u.ecm:uuid As id  FROM ");
                    builder.append(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_DOCUMENT_TYPE);
                    builder.append(" as u WHERE u.dc:lastContributor = ? ");
                    builder.append(" AND u.pusr:mailSiMajCE = 1 ");
                    final List<String> params = new ArrayList<>();
                    params.add(user.getUsername());
                    List<DocumentModel> userListDoc = new ArrayList<>();
                    try {
                        userListDoc =
                            QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
                                session,
                                builder.toString(),
                                params.toArray(new String[0])
                            );
                    } catch (final NuxeoException e) {
                        LOGGER.error(session, STLogEnumImpl.FAIL_GET_UW_TEC, user, e);
                    }
                    if (!userListDoc.isEmpty()) {
                        ProfilUtilisateur profilUser = userListDoc.get(0).getAdapter(ProfilUtilisateur.class);
                        if (profilUser != null && !listUserMail.contains(user)) {
                            // Si utilisateur trouvé, ajout la liste s'il n'y est pas déjà
                            listUserMail.add(user);
                        }
                    }
                }
            }
        }
        // Envoi du mail pour les utilisateurs de la liste
        final String objet = "[Solon] Mise à jour du Conseil d'Etat sur un dossier passé par votre poste";
        final String texte = "Le dossier \"${titreActe}\" (${nor}) a été mis à jour par le Conseil d'Etat.";
        final Map<String, Object> variablesMap = new HashMap<>();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        variablesMap.put("titreActe", dossier.getTitreActe());
        variablesMap.put("nor", dossier.getNumeroNor());
        if (!listUserMail.isEmpty()) {
            STServiceLocator
                .getSTMailService()
                .sendTemplateHtmlMailToUserList(
                    session,
                    listUserMail,
                    objet,
                    texte,
                    variablesMap,
                    Collections.singletonList(dossierDoc.getId())
                );
        }
    }

    /**
     * Récupère toutes les étapes et sous-étapes du cycle de vie qui ont le statut "done" pour le dossier passé en
     * paramètre.
     *
     * @param dossierDoc
     *            DocumentModel de type dossier
     * @return une liste de STRouteStep
     */
    private List<SSRouteStep> getAllDoneSteps(DocumentModel dossierDoc) {
        final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        List<DocumentModel> allStepsDoc = feuilleRouteService.getSteps(session, dossierDoc);

        ArrayList<SSRouteStep> resultList = new ArrayList<>();

        for (DocumentModel stepDoc : allStepsDoc) {
            if (stepDoc != null && stepDoc.getCurrentLifeCycleState().equals("done")) {
                if (FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(stepDoc.getType())) {
                    // Step folder
                    resultList.addAll(getAllDoneFolderSteps(stepDoc));
                } else {
                    // Route step
                    SSRouteStep routeStep = stepDoc.getAdapter(SSRouteStep.class);
                    resultList.add(routeStep);
                }
            }
        }

        return resultList;
    }

    /**
     * Récupère toutes les sous-étapes (récursivement) du step folder indiqué en paramètre.
     *
     * @param stepFolderDoc
     * @return
     */
    private List<SSRouteStep> getAllDoneFolderSteps(DocumentModel stepFolderDoc) {
        ArrayList<SSRouteStep> resultList = new ArrayList<>();
        for (DocumentModel stepDoc : session.getChildren(stepFolderDoc.getRef())) {
            if (stepDoc != null && stepDoc.getCurrentLifeCycleState().equals("done")) {
                if (FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(stepDoc.getType())) {
                    // Step folder
                    resultList.addAll(getAllDoneFolderSteps(stepDoc));
                } else {
                    // Route step
                    resultList.add(stepDoc.getAdapter(SSRouteStep.class));
                }
            }
        }

        return resultList;
    }

    private static List<String> checkNors(List<String> nors) {
        return nors.stream().map(EpgDelegateImpl::checkNor).collect(Collectors.toList());
    }

    private static String checkNor(String nor) {
        final NORService norService = SolonEpgServiceLocator.getNORService();
        if (nor != null && !norService.isStructNorValide(nor)) {
            throw new EPGException(String.format("Le format du NOR [%s] est invalide", nor));
        }
        return nor;
    }

    private static String checkNorActe(String norActe) {
        if (norActe != null && !norActe.matches("^[A-Z]$")) {
            throw new EPGException(String.format("Le format du norActe [%s] est invalide", norActe));
        }
        return norActe;
    }

    private static String checkCodeEntite(String codeEntite) {
        return checkCodeEntiteOrDirection(codeEntite, "Le format du code de l'entité [%s] est invalide");
    }

    private static String checkCodeDirection(String codeDirection) {
        return checkCodeEntiteOrDirection(codeDirection, "Le format du code de la direction [%s] est invalide");
    }

    private static String checkCodeEntiteOrDirection(String code, String message) {
        if (code != null && !CODE_ENTITE_OR_DIRECTION_PATTERN.matcher(code).matches()) {
            throw new EPGException(String.format(message, code));
        }
        return code;
    }

    private static List<String> checkNumerosISA(List<String> numerosISA) {
        return numerosISA.stream().map(EpgDelegateImpl::checkNumeroISA).collect(Collectors.toList());
    }

    private static String checkNumeroISA(String numeroISA) {
        final NORService norService = SolonEpgServiceLocator.getNORService();
        if (numeroISA != null && !norService.isStructNumeroISAValid(numeroISA)) {
            throw new EPGException(String.format("Le format du numeroISA [%s] est invalide", numeroISA));
        }
        return numeroISA;
    }
}
