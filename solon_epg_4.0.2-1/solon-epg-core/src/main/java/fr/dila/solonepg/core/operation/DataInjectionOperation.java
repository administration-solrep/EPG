package fr.dila.solonepg.core.operation;

import static fr.dila.st.api.constant.STBaseFunctionConstant.SUPERVISEUR_SGG_GROUP_NAME;
import static fr.dila.st.core.service.STServiceLocator.getCaseManagementDocumentTypeService;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.runtime.api.Framework;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.cm.service.MailboxCreator;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.solonepg.core.operation.livraison.SolonEpgCreateParametresOperation;
import fr.dila.solonepg.core.operation.livraison.SolonEpgUpdateParametresOperation;
import fr.dila.solonepg.core.operation.nxshell.CreateNewRepFDDTextNonPub;
import fr.dila.solonepg.core.operation.nxshell.CreateParametresAdamantOperation;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.operation.livraison.SSCreateParametresOperation;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.STProfilUtilisateurService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STGouvernementService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.BaseFunction;
import fr.dila.st.api.user.Profile;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.operation.version.STCreateParametresOperation;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STDefaultMailboxCreator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.FileUtils;

/**
 * Une opération pour injecter des données métiers dans la BDD et le repository,
 * on produit ainsi des dossiers et autres données sans passer par les
 * Webservices.
 *
 * @author slefevre
 */
@Operation(
    id = DataInjectionOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "CreationJeuDonnees",
    description = "Injecte des données dans la base de données"
)
public class DataInjectionOperation {
    private static final String FILE_CONTENT = "File content";

    private static final String GENERIC_FILENAME = FileUtils.sanitizePathTraversal("fileName.txt");

    private static final String ACTE_INTEGRAL_FILENAME = "Acte Intégral.txt";

    public static final String USER_CONTRIBUTEUR = "contributeur";

    public static final String USER_SUPERVISEUR_SGG = "superviseurSgg";

    public static final String USER_ADMINSGG = "adminsgg";

    public static final String USER_ADMINMIN = "adminMin";

    public static final String USER_WS_DILA = "ws_dila";

    private static final Log LOGGER = LogFactory.getLog(DataInjectionOperation.class);

    private static final String PASSWORD = "Solon2NG";

    private static final String POSTE_50002249 = "50002249";

    private static final String POSTE_50000656 = "50000656";
    private static final String POSTE_50000934 = "50000934";

    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "EPG.Injection.Donnees";

    public static final String MINISTERE_ECONOMIE_KEY = "Economie";
    public static final String MINISTERE_AGRICULTURE_KEY = "Agriculture";
    public static final String MINISTERE_JUSTICE_KEY = "Justice";
    public static final String MINISTERE_EDUCATION_KEY = "Education";
    public static final String MINISTERE_CULTURE_KEY = "Culture";
    public static final Map<String, ImmutableTriple<String, String, Long>> MEMBRES_GOUVERNEMENT = ImmutableMap.of(
        MINISTERE_ECONOMIE_KEY,
        ImmutableTriple.of("50000507", "Min. Economie, industrie et emploi", 7L),
        MINISTERE_AGRICULTURE_KEY,
        ImmutableTriple.of("50000938", "Min. Agriculture et pêche", 2L),
        MINISTERE_JUSTICE_KEY,
        ImmutableTriple.of("50000972", "Min. Justice", 12L),
        MINISTERE_EDUCATION_KEY,
        ImmutableTriple.of("50000968", "Min. Education Nationale", 15L),
        MINISTERE_CULTURE_KEY,
        ImmutableTriple.of("50000969", "Min. Culture", 17L)
    );

    public static final String NEW_MINISTERE_ECONOMIE_KEY = "New " + MINISTERE_ECONOMIE_KEY;
    public static final String NEW_MINISTERE_AGRICULTURE_KEY = "New " + MINISTERE_AGRICULTURE_KEY;
    public static final String NEW_MINISTERE_JUSTICE_KEY = "New " + MINISTERE_JUSTICE_KEY;
    public static final String NEW_MINISTERE_EDUCATION_KEY = "New " + MINISTERE_EDUCATION_KEY;
    public static final Map<String, ImmutableTriple<String, String, Long>> MEMBRES_NEW_GOUVERNEMENT = ImmutableMap.of(
        NEW_MINISTERE_ECONOMIE_KEY,
        ImmutableTriple.of("50001507", "New Min. Economie, industrie et emploi", 7L),
        NEW_MINISTERE_AGRICULTURE_KEY,
        ImmutableTriple.of("50001938", "New Min. Agriculture et pêche", 2L),
        NEW_MINISTERE_JUSTICE_KEY,
        ImmutableTriple.of("50001972", "New Min. Justice", 12L),
        NEW_MINISTERE_EDUCATION_KEY,
        ImmutableTriple.of("50001968", "New Min. Education Nationale", 15L)
    );

    @Context
    protected CoreSession session;

    private EpgDocumentRoutingService documentRoutingService;

    private DossierService dossierService;

    private STGouvernementService gouvernementService;

    private STMinisteresService ministereService;

    private STUsAndDirectionService usAndDirectionService;

    private MailboxCreator mailboxCreator;

    private MailboxPosteService mailboxPosteService;

    private STPostesService posteService;
    private SolonEpgUserManager userManager;
    private STProfilUtilisateurService profilService;

    /** Gouvernement actuel */
    private GouvernementNode gvtNode;
    /** Nouveau gouvernement */
    private GouvernementNode newGvtNode;

    private EntiteNode ministereEco;
    private EntiteNode ministereAgriculture;
    private EntiteNode ministereJustice;
    private EntiteNode ministereEducation;
    private EntiteNode ministereCulture;

    private EntiteNode newMinistereEco;
    private EntiteNode newMinistereAgriculture;
    private EntiteNode newMinistereJustice;
    private EntiteNode newMinistereEducation;

    private UniteStructurelleNode directionEcoBdc;
    private UniteStructurelleNode directionAgriBdc;
    private UniteStructurelleNode directionJusticeBdc;
    private UniteStructurelleNode directionEducationBdc;
    private UniteStructurelleNode directionCultureBdc;

    private UniteStructurelleNode newDirectionEcoBdc;
    private UniteStructurelleNode newDirectionAgriBdc;
    private UniteStructurelleNode newDirectionJusticeBdc;
    private UniteStructurelleNode newDirectionEducationBdc;

    private PosteNode posteAgentBdc;
    private PosteNode posteAgentDgefp;
    private PosteNode posteDlfEco;
    private PosteNode posteAgentsSecGenEco;
    private PosteNode posteAdminSolonSgg;
    private PosteNode posteConsAffEcoPm;
    private PosteNode posteSecConsAffEcoPm;
    private PosteNode posteBdcAgri;
    private PosteNode posteBdcEcoMEDAD;

    private void initServicesAndParams() {
        gouvernementService = STServiceLocator.getSTGouvernementService();
        documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        ministereService = STServiceLocator.getSTMinisteresService();
        usAndDirectionService = STServiceLocator.getSTUsAndDirectionService();
        posteService = STServiceLocator.getSTPostesService();
        userManager = (SolonEpgUserManager) STServiceLocator.getUserManager();
        profilService = STServiceLocator.getSTProfilUtilisateurService();
        dossierService = SolonEpgServiceLocator.getDossierService();
        mailboxCreator = STServiceLocator.getMailboxCreator();
        mailboxPosteService = SSServiceLocator.getMailboxPosteService();
    }

    @OperationMethod
    public void run() {
        initServicesAndParams();

        if (checkNotRun() && (Framework.isDevModeSet() || Framework.isTestModeSet())) {
            LOGGER.info("Début de l'injection de données.");
            injectOrganigramme();
            injectUserRelatedData();
            injectMailboxes();

            externalOperation();
            injectFdR();
            injectDossier();

            createTableReference();

            LOGGER.info("Fin de l'injection de données.");
        }
    }

    public void run(CoreSession session) {
        this.session = session;
        run();
    }

    private void injectUserRelatedData() {
        injectFunctions();
        injectGroups();
        injectUsers();
    }

    private void externalOperation() {
        callOperation(STCreateParametresOperation.ID);
        callOperation(SSCreateParametresOperation.ID);
        callOperation(CreateParametresAdamantOperation.ID);
        callOperation(CreateNewRepFDDTextNonPub.ID);
        callOperation(SolonEpgCreateParametresOperation.ID);
        callOperation(SolonEpgUpdateParametresOperation.ID);
    }

    private void callOperation(String... operationIds) {
        AutomationService automation = Framework.getService(AutomationService.class);
        Stream
            .of(operationIds)
            .forEach(
                id -> {
                    OperationContext ctx = new OperationContext(session);
                    try {
                        automation.run(ctx, id);
                    } catch (OperationException e) {
                        throw new NuxeoException(e);
                    }
                }
            );
    }

    private void injectFunctions() {
        injectOneFunction(
            "EspaceActiviteNormativeParamLegisUpdater",
            "Activité Normative : Accès au paramétrage du pilotage de l'activité normative"
        );
        injectOneFunction(
            "EtapeBonATirerCreator",
            "Feuille de route : Création des types d'étapes \"Pour bon à tirer\""
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_PARAPHEUR_UPDATER,
            "Dossiers : modification du parapheur"
        );
        injectOneFunction("AdministrationIndexationUpdater", "Administration de l'indexation : modification");
        injectOneFunction("AdministrationReferenceReader", "Administration des tables de référence : lecture");
        injectOneFunction(
            "CorbeilleIndexationReader",
            "Espace de traitement : accès à la corbeille \"pour indexation\""
        );
        injectOneFunction("UtilisateurUpdater", "Gestion des utilisateurs : mise à jour");
        injectOneFunction(
            "EspaceActiviteNormativePublicationExecutor",
            "Activité Normative : Accès à la publication de la législature de l'activité normative"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_DECRET_UPDATER,
            "Donne le droit de modifier le type d'acte au sein des dossiers de type décrets"
        );
        injectOneFunction(SolonEpgBaseFunctionConstant.ESPACE_RECHERCHE_READER, "Accès à l'espace de recherche");
        injectOneFunction("ProfilReader", "Gestion des profils : lecture");
        injectOneFunction("WsSpeEnvoyerPremiereDemandePE", "Webservice - SPE - envoyerPremiereDemandePE");
        injectOneFunction(
            "ZoneCommentaireSggDilaUpdater",
            "Bordereau : modification du champ ZoneCommentaireSggDila (modification possible pour les profils du sgg)"
        );
        injectOneFunction("EtapePourAttributionSggExecutor", "Dossier : exécution de l'étape pour attribution SGG");
        injectOneFunction(
            "PosteFieldsUpdater",
            "Donne le droit de modifier les champs \"Chargés de mission SGG\" et \"Conseiller du Premier ministre\" d'un poste"
        );
        injectOneFunction(
            "EspaceActiviteNormativeTraitAccUpdater",
            "Accès en modification au menu Traités et Accords de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction(SolonEpgBaseFunctionConstant.ADMINISTRATION_ORGANIGRAMME_READER, "Organigramme : lecture");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE,
            "Accès en modification au menu Application des lois de l'espace de pilotage de l'activité normative pour un profil ministériel"
        );
        injectOneFunction("AdministrationBulletinUpdater", "Administration des bulletins : modification");
        injectOneFunction(SolonEpgBaseFunctionConstant.ESPACE_TRAITEMENT_READER, "Accès à l'espace de traitement");
        injectOneFunction(
            "FDRModelPosteMassSubstitutor",
            "Administration des modèles de feuilles de route : substitution de poste"
        );
        injectOneFunction("UtilisateurCreator", "Gestion des utilisateurs : création");
        injectOneFunction("DossierArchivageIntermediaireReader", "Dossiers vers archivage intermédiaire : lecture");
        injectOneFunction(
            "AccessUnrestrictedUpdater",
            "Donne le droit de restreindre / restaurer l'accès à l'application"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVES_UPDATER,
            "Dossiers : actions spécifiques des mesures nominatives."
        );
        injectOneFunction(
            "EspaceActiviteNormativeMinistereApplicationOrdonnance",
            "Accès en modification au menu Application des ordonnances de l'espace de pilotage de l'activité normative pour un profil ministériel"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_READER,
            "Dossier : permet d'accéder au traitement papier en lecture "
        );
        injectOneFunction(
            "EspaceActiviteNormativeTraitAccReader",
            "Accès en lecture au menu Traités et Accords de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("AdministrationJournalReader", "Journal technique : lecture");
        injectOneFunction("OrganigrammeUpdater", "Organigramme : mise à jour");
        injectOneFunction(
            "WsEpgModifierDossierDecretPrInd",
            "Ws Epg : Modifier dossier - Décret du Président de la République (individuel)"
        );
        injectOneFunction("FondDossierRepertoireSGG", "Accès aux répertoires du SGG dans le fond de dossier ");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ADMINISTRATION_INDEXATION_READER,
            "Administration de l'indexation : lecture"
        );
        injectOneFunction("InfocentreGeneralReader", "Droit d'accès a l'infocentre général");
        injectOneFunction(STWebserviceConstant.ENVOYER_RETOUR_PE, "Webservice - SPE - envoyerRetourPE");
        injectOneFunction("WsEpgDonnerAvisCE", "Webservice - EPG - donnerAvisCE");
        injectOneFunction(
            "FDRInstanceRestarter",
            "Dossier : permet de relancer une instance de feuille de route à l'état terminé"
        );
        injectOneFunction("InterfaceAccess", "Accès à l'interface de l'application");
        injectOneFunction("ProfilCreator", "Gestion des profils : création");
        injectOneFunction("ProfilDeleter", "Gestion des profils : suppression");
        injectOneFunction(
            "WsEpgCreerDossierDecretPrInd",
            "Ws Epg : Créer dossier - Décret du Président de la République (individuel)"
        );
        injectOneFunction("BatchSuiviReader", "Administration : suivi des batchs");
        injectOneFunction("AllowAddPosteAllMinistere", "Autorise la sélection d'un poste d'un autre ministère");
        injectOneFunction("AdministrationParamReader", "Administration des paramétrages : lecture");
        injectOneFunction(
            "HistoriqueMajMinisterielReader",
            "Donne d'accéder aux écrans de mise à jour ministérielle pour les applications loi, les directives, les ordonnances"
        );
        injectOneFunction("AdministrationParamAdamant", "Administration du paramétrage Adamant");
        injectOneFunction(
            "EspaceActiviteNormativeAppOrdoReader",
            "Accès en lecture au menu Application des ordonnances de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("WsEpgChercherDossier", "Webservice - EPG - chercherDossier");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER,
            "Dossier : permet d'accéder au traitement papier en écriture "
        );
        injectOneFunction("EtapeObligatoireUpdater", "Feuille de route : modification des étapes obligatoires SGG");
        injectOneFunction(
            "EspaceActiviteNormativeSuivHabUpdater",
            "Accès en modification au menu Suivi des habilitations de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER,
            "Accès en lecture au menu Transposition de directives européennes de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction(
            "AdminFonctionnelEmailReceiver",
            "Email : réception des méls destinés aux administrateurs fonctionnels"
        );
        injectOneFunction("UtilisateurDeleter", "Gestion des utilisateurs : suppression");
        injectOneFunction("DossierArchivageAdamantReader", "Archivage ADAMANT");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_RATTACHEMENT_MINISTERE_READER,
            "Dossiers : visibilité des dossiers rattachés à un de ses ministères."
        );
        injectOneFunction(SolonEpgBaseFunctionConstant.ESPACE_SUIVI_READER, "Accès à l'espace de suivi");
        injectOneFunction("DossierCreator", "Création d'un dossier");
        injectOneFunction("TextesSignalesSGGReader", "Droit d'accès aux texte Signalés");
        injectOneFunction("SupprimerVersion", "Supprimer une version");
        injectOneFunction(
            "EtapePublicationDilaJoCreator",
            "Feuille de route : Création des types d'étapes \"Pour publication à la DILA JO\""
        );
        injectOneFunction("TextesSignalesUpdater", "Droit d'accès en écriture a l'infocentre du SGG");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_FOND_DOSSIER_UPDATER,
            "Dossiers : modification du fond de dossier"
        );
        injectOneFunction("AdministrationParamUpdater", "Administration des paramétrages : modification");
        injectOneFunction("EtapePourSignatureExecutor", "Dossier : exécution de l'étape pour signature");
        injectOneFunction("InfocentreGeneralDirReader", "Droit d'accès a l'infocentre général (Direction uniquement)");
        injectOneFunction(
            "WsEpgCreerDossierInfosParlementaires",
            "Ws Epg : Créer dossier - Informations parlementaires"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ESPACE_ADMINISTRATION_READER,
            "Accès à l'espace d'administration"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_INDEXATION_UPDATER,
            "Dossiers : modification des champs du bordereau relatif à l'indexation"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER,
            "Accès en lecture au menu Suivi des habilitations de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("IndexationMinUpdater", "Corbeille indexation : affichage de tous les dossiers du ministère");
        injectOneFunction("OrganigrammeAdminUnlocker", "Permet de lever le verrou dans l'organigramme");
        injectOneFunction("IndexationSGGUpdater", "Corbeille indexation : affichage de tous les dossiers");
        injectOneFunction(
            "ConseilEtatUpdater",
            "Dossiers : Modification des informations en lien avec le Conseil d’Etat"
        );
        injectOneFunction("InfocentreGeneralFullReader", "Droit d'accès a l'infocentre général");
        injectOneFunction("DroitVueMgpp", "MGPP : affichage des informations spécifiques du secteur parlementaire");
        injectOneFunction(
            "WsEpgModifierDossierInfosParlementaires",
            "Ws Epg : Modifier dossier - Informations parlementaires"
        );
        injectOneFunction("DossierArchivageDefinitifReader", "Dossiers vers archivage définitif : lecture");
        injectOneFunction("routeManagers", "Gestionnaire de routes");
        injectOneFunction("FDRModelMassCreator", "Creation en masse des modeles de feuilles de route");
        injectOneFunction("ProfilWebserviceUpdater", "Donne le droit d'ajouter le profil Webservice à un utilisateur");
        injectOneFunction(
            "EspaceActiviteNormativeRatOrdoUpdater",
            "Accès en modification au menu Ratification des Ordonnances de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction(
            "IndexationMinPubliUpdater",
            "Corbeille indexation : affichage des dossiers publié du ministère"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER,
            "Accès en lecture au menu Application des lois de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("DossierMesureNominativeReader", "Dossiers : visibilité des mesures nominatives.");
        injectOneFunction(
            "DossierDistributionAdminUpdater",
            "Dossiers : possibilité d'agir sur la feuille de route de tous les dossiers."
        );
        injectOneFunction("DossierDeleter", "Dossier : suppression");
        injectOneFunction(
            "AdminFonctionnelStatReader",
            "Statistiques : Droit d'accès liste des statistiques destinés aux administrateurs fonctionnels"
        );
        injectOneFunction("AdminFonctionnelDossierSuppression", "Liste des dossiers à supprimer : modification");
        injectOneFunction("AdminFonctionnelTransfertExecutor", "Fonctionnalités de transfert des dossier clos");
        injectOneFunction("NoteEtapeDeleter", "Note : droit de suppression des notes d'étapes");
        injectOneFunction(
            "AdminFonctionnelTableauDynamiqueDestinataireViewer",
            "Tableau dynamique : visibilité des destinataires"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_DISTRIBUTION_MINISTERE_UPDATER,
            "Dossiers : actions sur la distribution des dossiers distribués via la feuille de route dans un de ses ministères."
        );
        injectOneFunction("WsEpgCreerDossierAvis", "Ws Epg : Créer dossier - Avis");
        injectOneFunction("GestionDeListeReader", "Droit d'accès a la gestion de liste pour le traitement du papier");
        injectOneFunction("UtilisateurReader", "Gestion des utilisateurs : lecture");
        injectOneFunction("DecretArriveReader", "Droit de voir si l'original d'un décret est arrivé");
        injectOneFunction(
            "EnvoiSaisinePieceComplementaireExecutor",
            "Action d'envoi de saisine rectificative et d'envoi de pièces complémentaires"
        );
        injectOneFunction("WsEpgModifierDossierAvis", "Ws Epg : Modifier dossier - Avis");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_SUPPRESSION,
            "Liste des dossiers à supprimer : modification"
        );
        injectOneFunction("ProfilUpdater", "Gestion des profils : mise à jour");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER,
            "Accès en lecture au menu Ratification des Ordonnances de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("TexteSignaleCreator", "Ajout de dossier dans les textes signalés");
        injectOneFunction("CreerAlerte", "Lever ou poser une alerte");
        injectOneFunction(SolonEpgBaseFunctionConstant.ESPACE_CREATION_READER, "Accès à l'espace de création");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_MIN_UPDATER,
            "Dossier : possibilité d'agir sur les dossiers de son ministère sans être destinataire de la distribution"
        );
        injectOneFunction(
            "EspaceActiviteNormativeExportDataUpdater",
            "Activité normative : export complet des données "
        );
        injectOneFunction(
            "DossierAdminUpdater",
            "Dossier : modification de tous les dossiers (à l'exception des opération de distribution)"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_DISTRIBUTION_DIRECTION_READER,
            "Dossiers : visibilité des dossiers distribués via la feuille de route dans une de ses directions."
        );
        injectOneFunction(
            "EtapeFournitureEpreuveCreator",
            "Feuille de route : Création des types d'étapes \"Pour fourniture d'épreuves\""
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UNLOCKER,
            "Permet de lever le verrou sur un dossier"
        );
        injectOneFunction(
            "IndexationDirUpdater",
            "Corbeille indexation : affichage de tous les dossiers de la direction"
        );
        injectOneFunction("EtapePourContreseingExecutor", "Dossier : exécution de l'étape pour contreseing");
        injectOneFunction("AdministrationFondDeDossier", "Accès à l'espace d'administration des fonds de dossier");
        injectOneFunction("DossierNorAttribueUpdater", "Autorise la modification d'un dossier nor attribué");
        injectOneFunction("WsSpeEnvoyerDemandeSuivantePE", "Webservice - SPE - envoyerDemandeSuivantePE");
        injectOneFunction(
            STBaseFunctionConstant.ADMIN_ACCESS_UNRESTRICTED,
            "Autorise l'accès à l'application quand l'accès restreint est actif"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_PARAPHEUR_OR_FOND_DOSSIER_DELETER,
            "Dossiers : suppression d'un document du fond de dossier ou du parapheur"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_RATTACHEMENT_DIRECTION_READER,
            "Dossiers : visibilité des dossiers rattachés à une de ses directions."
        );
        injectOneFunction("WsEpgRechercherDossier", "Webservice - EPG - rechercherDossier");
        injectOneFunction(SolonEpgBaseFunctionConstant.ESPACE_PARLEMENTAIRE_READER, "Accès à l'espace parlementaire");
        injectOneFunction(
            "AdministrationChangementGouvernement",
            "Autorise l'accès aux fonctionnalités de changement de gouvernement (migration des éléments de l'organigramme)"
        );
        injectOneFunction(STBaseFunctionConstant.PROFIL_SGG, "Est un profil SGG ");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_ABANDON,
            "Liste des dossiers à abandonner : modification"
        );
        injectOneFunction("UtilisateurMinistereUpdater", "Gestion des utilisateurs du ministère: mise à jour");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.MINISTERE_ORGANIGRAMME_UPDATER,
            "Organigramme : droit de mise à jour du ministère de l'utilisateur uniquement"
        );
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_DISTRIBUTION_MINISTERE_READER,
            "Dossiers : visibilité des dossiers distribués via la feuille de route dans un de ses ministères."
        );
        injectOneFunction("DossierRechercheReader", "Dossiers : visibilité de tous les dossiers.");
        injectOneFunction(
            "IndexationDirPubliUpdater",
            "Corbeille indexation : affichage des dossiers publié de la direction"
        );
        injectOneFunction("RejeterEvenement", "Rejeter une rectification ou une annulation d'une communication");
        injectOneFunction("AdministrationBulletinReader", "Administration des bulletins : lecture");
        injectOneFunction(
            "EspaceActiviteNormativeAppLoisUpdater",
            "Accès en modification au menu Application des lois de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("UtilisateurRechercheReader", "Recherche des utilisateurs");
        injectOneFunction("AccuserReception", "Lever ou poser une alerte");
        injectOneFunction("UtilisateurMinistereDeleter", "Gestion des utilisateurs du ministère : suppression");
        injectOneFunction("WsEpgChercherModificationDossier", "Webservice - EPG - Chercher Modification Dossier");
        injectOneFunction("DossierEnder", "Autorise le basculement du dossier au statut Terminé sans publication");
        injectOneFunction(
            "FDRModelUpdater",
            "Administration des modèles de feuilles de route : administrateur ministériel"
        );
        injectOneFunction("AdminFonctionnelDossierAbandon", "Liste des dossiers à abandonner : modification");
        injectOneFunction(
            "EspaceActiviteNormativeAppOrdoUpdater",
            "Accès en modification au menu Application des ordonnances de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction("AccepterEvenement", "Accepter une rectification ou une annulation d'une communication");
        injectOneFunction(
            "EspaceActiviteNormativeTranspositionUpdater",
            "Accès en modification au menu Transposition de directives européennes de l'espace de pilotage de l'activité normative"
        );
        injectOneFunction(
            "FDRModelValidator",
            "Administration des modèles de feuilles de route : administrateur fonctionnel"
        );
        injectOneFunction(
            "DossierActiviteNormativeUpdater",
            "Dossiers : modification des champs du bordereau relatif à l'activité normative"
        );
        injectOneFunction("InfocentreSGGReader", "Droit d'accès a l'infocentre du SGG");
        injectOneFunction("AdministrationReferenceUpdater", "Administration des tables de référence : modification");
        injectOneFunction("WsEpgAttribuerNor", "Webservice - EPG - attribuerNor");
        injectOneFunction("AdministrationParapheur", "Accès à l'espace d'administration des parapheurs");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_EMAIL_RECEIVER,
            "Email : réception des méls destinés aux administrateurs ministériels"
        );
        injectOneFunction("IndexationSGGPubliUpdater", "Corbeille indexation : affichage des dossiers publié");
        injectOneFunction("WsEpgModifierDossierCE", "Webservice - EPG - modifierDossierCE");
        injectOneFunction("CorbeilleMGPPUpdater", "Visualisation des corbeilles ");
        injectOneFunction("JournalReader", "Dossier : affichage de l'onglet journal");
        injectOneFunction("FDRSqueletteUpdater", "Administration des squelettes de feuilles de route");
        injectOneFunction(
            "BordereauDateSignatureUpdater",
            "Bordereau : modification du champ DateSignatureUpdater (modification possible pour les profils du sgg)"
        );
        injectOneFunction("EspaceActualitesReader", "Administration : Accès au sous menu actualités");
        injectOneFunction(
            SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_ARRETE_UPDATER,
            "Donne le droit de modifier le type d'acte au sein des dossiers de type arrêtés"
        );
    }

    private void injectGroups() {
        injectOneGroup("Contributeur ministériel", asList("UtilisateurReader"));
        injectOneGroup(
            STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME,
            asList(
                "EspaceActiviteNormativeParamLegisUpdater",
                "EtapeBonATirerCreator",
                "DossierParapheurUpdater",
                "AdministrationIndexationUpdater",
                "AdministrationReferenceReader",
                "CorbeilleIndexationReader",
                "UtilisateurUpdater",
                "EspaceActiviteNormativePublicationExecutor",
                "DossierTypeActeDecretUpdater",
                "EspaceRechercheReader",
                "ProfilReader",
                "WsSpeEnvoyerPremiereDemandePE",
                "ZoneCommentaireSggDilaUpdater",
                "EtapePourAttributionSggExecutor",
                "PosteFieldsUpdater",
                "EspaceActiviteNormativeTraitAccUpdater",
                "OrganigrammeReader",
                "EspaceActiviteNormativeMinistereApplicationLoi",
                "AdministrationBulletinUpdater",
                "EspaceTraitementReader",
                "FDRModelPosteMassSubstitutor",
                "UtilisateurCreator",
                "DossierArchivageIntermediaireReader",
                "AccessUnrestrictedUpdater",
                SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVES_UPDATER,
                "EspaceActiviteNormativeMinistereApplicationOrdonnance",
                SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_READER,
                "EspaceActiviteNormativeTraitAccReader",
                "AdministrationJournalReader",
                "OrganigrammeUpdater",
                "WsEpgModifierDossierDecretPrInd",
                "FondDossierRepertoireSGG",
                "AdministrationIndexationReader",
                "InfocentreGeneralReader",
                "WsSpeEnvoyerRetourPE",
                "WsEpgDonnerAvisCE",
                "FDRInstanceRestarter",
                "InterfaceAccess",
                "ProfilCreator",
                "ProfilDeleter",
                "WsEpgCreerDossierDecretPrInd",
                "BatchSuiviReader",
                "AllowAddPosteAllMinistere",
                "AdministrationParamReader",
                "HistoriqueMajMinisterielReader",
                "AdministrationParamAdamant",
                "EspaceActiviteNormativeAppOrdoReader",
                "WsEpgChercherDossier",
                SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER,
                "EtapeObligatoireUpdater",
                "EspaceActiviteNormativeSuivHabUpdater",
                "EspaceActiviteNormativeTranspositionReader",
                "AdminFonctionnelEmailReceiver",
                "UtilisateurDeleter",
                "DossierArchivageAdamantReader",
                "AdminMinisterielStatReader",
                "DossierRattachementMinistereReader",
                "EspaceSuiviReader",
                "DossierCreator",
                "TextesSignalesSGGReader",
                "SupprimerVersion",
                "EtapePublicationDilaJoCreator",
                "TextesSignalesUpdater",
                "DossierFondDossierUpdater",
                "AdministrationParamUpdater",
                "EtapePourSignatureExecutor",
                "InfocentreGeneralDirReader",
                "WsEpgCreerDossierInfosParlementaires",
                "EspaceAdministrationReader",
                "DossierIndexationUpdater",
                "EspaceActiviteNormativeSuivHabReader",
                "IndexationMinUpdater",
                "OrganigrammeAdminUnlocker",
                "IndexationSGGUpdater",
                "ConseilEtatUpdater",
                "InfocentreGeneralFullReader",
                "DroitVueMgpp",
                "WsEpgModifierDossierInfosParlementaires",
                "DossierArchivageDefinitifReader",
                "routeManagers",
                "FDRModelMassCreator",
                "ProfilWebserviceUpdater",
                "EspaceActiviteNormativeRatOrdoUpdater",
                "IndexationMinPubliUpdater",
                "EspaceActiviteNormativeAppLoisReader",
                "DossierMesureNominativeReader",
                "DossierDistributionAdminUpdater",
                "DossierDeleter",
                "AdminFonctionnelStatReader",
                "AdminFonctionnelDossierSuppression",
                "AdminFonctionnelTransfertExecutor",
                "NoteEtapeDeleter",
                "AdminFonctionnelTableauDynamiqueDestinataireViewer",
                "DossierDistributionMinistereUpdater",
                "WsEpgCreerDossierAvis",
                "GestionDeListeReader",
                "UtilisateurReader",
                "DecretArriveReader",
                "EnvoiSaisinePieceComplementaireExecutor",
                "WsEpgModifierDossierAvis",
                "AdminMinisterielDossierSuppression",
                "ProfilUpdater",
                "EspaceActiviteNormativeRatOrdoReader",
                "TexteSignaleCreator",
                "CreerAlerte",
                "EspaceCreationReader",
                "DossierAdminMinUpdater",
                "EspaceActiviteNormativeExportDataUpdater",
                "DossierAdminUpdater",
                "DossierDistributionDirectionReader",
                "EtapeFournitureEpreuveCreator",
                "DossierAdminUnlocker",
                "IndexationDirUpdater",
                "EtapePourContreseingExecutor",
                "AdministrationFondDeDossier",
                "DossierNorAttribueUpdater",
                "WsSpeEnvoyerDemandeSuivantePE",
                STBaseFunctionConstant.ADMIN_ACCESS_UNRESTRICTED,
                "DossierParapheurOrFondDossierDeleter",
                "DossierRattachementDirectionReader",
                "WsEpgRechercherDossier",
                "EspaceParlementaireReader",
                "AdministrationChangementGouvernement",
                "ProfilSGG",
                "AdminMinisterielDossierAbandon",
                "UtilisateurMinistereUpdater",
                "OrganigrammeMinistereUpdater",
                "DossierDistributionMinistereReader",
                "DossierRechercheReader",
                "IndexationDirPubliUpdater",
                "RejeterEvenement",
                "AdministrationBulletinReader",
                "EspaceActiviteNormativeAppLoisUpdater",
                "UtilisateurRechercheReader",
                "AccuserReception",
                "UtilisateurMinistereDeleter",
                "WsEpgChercherModificationDossier",
                "DossierEnder",
                "FDRModelUpdater",
                "AdminFonctionnelDossierAbandon",
                "EspaceActiviteNormativeAppOrdoUpdater",
                "AccepterEvenement",
                "EspaceActiviteNormativeTranspositionUpdater",
                "FDRModelValidator",
                "DossierActiviteNormativeUpdater",
                "InfocentreSGGReader",
                "AdministrationReferenceUpdater",
                "WsEpgAttribuerNor",
                "AdministrationParapheur",
                "AdminMinisterielEmailReceiver",
                "IndexationSGGPubliUpdater",
                "WsEpgModifierDossierCE",
                "CorbeilleMGPPUpdater",
                "JournalReader",
                "FDRSqueletteUpdater",
                "BordereauDateSignatureUpdater",
                "EspaceActualitesReader",
                SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_ARRETE_UPDATER
            )
        );
        injectOneGroup(
            STBaseFunctionConstant.ADMIN_MINISTERIEL_GROUP_NAME,
            asList(
                "DossierFondDossierUpdater",
                "OrganigrammeReader",
                "EspaceAdministrationReader",
                "DossierAdminUnlocker",
                "EspaceActiviteNormativeTraitAccReader",
                "EspaceActiviteNormativeRatOrdoReader",
                "EspaceTraitementReader",
                "HistoriqueMajMinisterielReader",
                "InfocentreGeneralReader",
                "DossierParapheurOrFondDossierDeleter",
                "DossierDistributionMinistereReader",
                "DossierRattachementMinistereReader",
                "EspaceParlementaireReader",
                "DossierRattachementDirectionReader",
                "DossierAdminMinUpdater",
                "UtilisateurCreator",
                "UtilisateurReader",
                "AdministrationIndexationReader",
                "FDRModelUpdater",
                "EspaceActiviteNormativeSuivHabReader",
                "EspaceActiviteNormativeMinistereApplicationLoi",
                "AdminMinisterielStatReader",
                "AdminMinisterielDossierAbandon",
                "EspaceActiviteNormativeAppLoisReader",
                "OrganigrammeMinistereUpdater",
                "EspaceActiviteNormativeTranspositionReader",
                "routeManagers",
                "AdminMinisterielDossierSuppression",
                "DossierTypeActeDecretUpdater",
                "DossierDistributionDirectionReader",
                "UtilisateurMinistereUpdater",
                "UtilisateurRechercheReader",
                "DossierDistributionMinistereUpdater",
                "EspaceSuiviReader",
                "DossierParapheurUpdater",
                "DossierIndexationUpdater",
                "EspaceActiviteNormativeMinistereApplicationOrdonnance",
                "InterfaceAccess",
                "FDRModelPosteMassSubstitutor",
                "EspaceRechercheReader",
                "EspaceCreationReader",
                "AdminMinisterielEmailReceiver",
                "UtilisateurMinistereDeleter",
                SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_ARRETE_UPDATER
            )
        );
        injectOneGroup(
            STBaseFunctionConstant.SUPERVISEUR_SGG_GROUP_NAME,
            asList(
                "UtilisateurCreator",
                "routeManagers",
                "InterfaceAccess",
                "UtilisateurReader",
                "OrganigrammeReader",
                "JournalReader",
                "EspaceSuiviReader",
                "AllowAddPosteAllMinistere",
                "FDRInstanceRestarter"
            )
        );
        injectOneGroup(
            STBaseFunctionConstant.CONTRIBUTEUR_DILA_GROUP_NAME,
            asList(
                "DossierNorAttribueUpdater",
                "DossierFondDossierUpdater",
                "TextesSignalesUpdater",
                "DossierParapheurOrFondDossierDeleter",
                "EtapeFournitureEpreuveCreator",
                "EspaceSuiviReader",
                "DossierParapheurUpdater",
                "ProfilSGG",
                "DossierIndexationUpdater",
                "InfocentreSGGReader",
                "EtapeObligatoireUpdater",
                "NoteEtapeDeleter",
                "InterfaceAccess",
                "EspaceParlementaireReader",
                "EtapePublicationDilaJoCreator",
                "DossierRechercheReader",
                "EtapePourAttributionSggExecutor",
                "GestionDeListeReader",
                "EspaceRechercheReader",
                "EspaceCreationReader",
                "routeManagers",
                "ZoneCommentaireSggDilaUpdater",
                "FondDossierRepertoireSGG",
                "BordereauDateSignatureUpdater",
                SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER,
                SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_READER,
                "TexteSignaleCreator",
                "DecretArriveReader",
                "EtapeBonATirerCreator",
                "EspaceTraitementReader"
            )
        );
        injectOneGroup(
            STBaseFunctionConstant.WEB_SERVICE_DILA_GROUP_NAME,
            asList(
                "WsSpeEnvoyerRetourPE",
                "WsSpeEnvoyerDemandeSuivantePE",
                "WsEpgDonnerAvisCE",
                "WsSpeEnvoyerPremiereDemandePE",
                "WsEpgModifierDossierInfosParlementaires",
                "WsEpgCreerDossierInfosParlementaires",
                "WsEpgChercherDossier"
            )
        );
        injectOneGroup(
            STBaseFunctionConstant.WEB_SERVICE_MINISTERIEL_GROUP_NAME,
            asList(
                "EspaceAdministrationReader",
                "WsEpgAttribuerNor",
                "WsSpeEnvoyerRetourPE",
                "WsEpgChercherModificationDossier",
                "DossierFondDossierUpdater",
                "UtilisateurRechercheReader",
                "EspaceRechercheReader",
                "DossierDistributionMinistereUpdater",
                "DossierParapheurOrFondDossierDeleter",
                "DossierRattachementDirectionReader",
                "EspaceCreationReader",
                "EspaceSuiviReader",
                "DossierAdminMinUpdater",
                "WsEpgModifierDossierCE",
                "OrganigrammeMinistereUpdater",
                "WsEpgRechercherDossier",
                "routeManagers",
                "AdminMinisterielDossierSuppression",
                "DossierParapheurUpdater",
                "DossierDistributionMinistereReader",
                "UtilisateurCreator",
                "UtilisateurReader",
                "WsSpeEnvoyerDemandeSuivantePE",
                "AdministrationIndexationReader",
                "EnvoiSaisinePieceComplementaireExecutor",
                "DossierCreator",
                "OrganigrammeReader",
                "FDRModelUpdater",
                "DossierIndexationUpdater",
                "DossierRattachementMinistereReader",
                "DossierDistributionDirectionReader",
                "AdminMinisterielEmailReceiver",
                "UtilisateurMinistereUpdater",
                "WsEpgDonnerAvisCE",
                "WsSpeEnvoyerPremiereDemandePE",
                "UtilisateurMinistereDeleter",
                "FDRModelPosteMassSubstitutor",
                "EspaceParlementaireReader",
                "EspaceTraitementReader",
                "WsEpgChercherDossier"
            )
        );
    }

    private DocumentModel injectOneFunction(String id, String description) {
        VocabularyService functionVocabularyService = STServiceLocator.getVocabularyService();

        DocumentModel functionDoc = functionVocabularyService.getNewEntry(STConstant.ORGANIGRAMME_BASE_FUNCTION_DIR);

        BaseFunction function = functionDoc.getAdapter(BaseFunction.class);

        function.setDescription(description);
        function.setGroupname(id);

        functionVocabularyService.createDirectoryEntry(STConstant.ORGANIGRAMME_BASE_FUNCTION_DIR, functionDoc);

        return functionDoc;
    }

    private DocumentModel injectOneGroup(String groupname, List<String> functions) {
        DocumentModel groupModel = userManager.getBareGroupModel();
        Profile profile = groupModel.getAdapter(Profile.class);
        groupModel.setPropertyValue("groupname", groupname);
        profile.setBaseFunctionList(functions);
        return userManager.createGroup(groupModel);
    }

    private void injectUsers() {
        injectOneUser(
            USER_ADMINSGG,
            PASSWORD,
            asList(POSTE_50002249, POSTE_50000656),
            asList(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME)
        );
        injectOneUser(
            USER_ADMINMIN,
            PASSWORD,
            asList(POSTE_50002249, POSTE_50000656),
            asList(STBaseFunctionConstant.ADMIN_MINISTERIEL_GROUP_NAME)
        );

        injectOneUser(USER_SUPERVISEUR_SGG, PASSWORD, asList(POSTE_50000656), asList(SUPERVISEUR_SGG_GROUP_NAME));
        injectOneUser(USER_CONTRIBUTEUR, PASSWORD, asList(POSTE_50000656), asList("Contributeur ministériel"));
        injectOneUser(
            USER_WS_DILA,
            PASSWORD,
            null,
            asList(
                STBaseFunctionConstant.WEB_SERVICE_DILA_GROUP_NAME,
                STBaseFunctionConstant.WEB_SERVICE_MINISTERIEL_GROUP_NAME,
                STBaseFunctionConstant.CONTRIBUTEUR_DILA_GROUP_NAME
            )
        );

        // Ajout de AdminMin & SuperviseurSgg dans FavorisConsultation
        DocumentModel userProfileDoc = userManager.getUserModel(USER_ADMINMIN);
        ajoutFavorisConsultation(userProfileDoc);
        userProfileDoc = userManager.getUserModel(USER_SUPERVISEUR_SGG);
        ajoutFavorisConsultation(userProfileDoc);
    }

    private void injectOneUser(String username, String password, List<String> postes, List<String> groups) {
        DocumentModel user = userManager.getBareUserModel();
        STUser stuser = user.getAdapter(STUser.class);
        stuser.setUsername(username);
        stuser.setLastName(username.toUpperCase());
        stuser.setFirstName(username.toLowerCase());
        stuser.setEmail(username + "@solon2ng-epg.com");
        stuser.setPassword(password);
        stuser.setPostes(postes);
        stuser.setGroups(groups);
        stuser.setDateDebut(Calendar.getInstance());

        userManager.createUser(user);

        // rewrite password a second time
        DocumentModel userup = userManager.getUserModel(username);
        STUser stuserup = userup.getAdapter(STUser.class);
        stuserup.setPassword(password);
        stuserup.setPwdReset(false);
        Framework.doPrivileged(() -> userManager.updateUser(userup));

        // mise à jour du profil utilisateur
        DocumentModel userProfileDoc = profilService.getProfilUtilisateurDoc(session, username);
        session.saveDocument(userProfileDoc);
    }

    /**
     * Vérification : l'opération ne doit jamais avoir été exécutée. En particulier
     * aucun gouvernement ne doit avoir été injecté.
     *
     * @return true si on ne trouve pas de gouvernement.
     */
    private boolean checkNotRun() {
        return gouvernementService.getGouvernementList().isEmpty();
    }

    private void injectOrganigramme() {
        createGouvernements();
        createMinisteres();
        createUnitesStructurelles();
        createPostes();
    }

    private void createMinisteres() {
        // Gouvernement actuel
        ministereEco =
            createOneMinistere(
                MEMBRES_GOUVERNEMENT.get(MINISTERE_ECONOMIE_KEY).getLeft(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_ECONOMIE_KEY).getMiddle(),
                MINISTERE_ECONOMIE_KEY.substring(0, 3).toUpperCase(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_ECONOMIE_KEY).getRight(),
                gvtNode
            );
        ministereAgriculture =
            createOneMinistere(
                MEMBRES_GOUVERNEMENT.get(MINISTERE_AGRICULTURE_KEY).getLeft(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_AGRICULTURE_KEY).getMiddle(),
                MINISTERE_AGRICULTURE_KEY.substring(0, 3).toUpperCase(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_AGRICULTURE_KEY).getRight(),
                gvtNode
            );
        ministereJustice =
            createOneMinistere(
                MEMBRES_GOUVERNEMENT.get(MINISTERE_JUSTICE_KEY).getLeft(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_JUSTICE_KEY).getMiddle(),
                MINISTERE_JUSTICE_KEY.substring(0, 3).toUpperCase(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_JUSTICE_KEY).getRight(),
                gvtNode
            );

        ministereEducation =
            createOneMinistere(
                MEMBRES_GOUVERNEMENT.get(MINISTERE_EDUCATION_KEY).getLeft(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_EDUCATION_KEY).getMiddle(),
                MINISTERE_EDUCATION_KEY.substring(0, 3).toUpperCase(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_EDUCATION_KEY).getRight(),
                gvtNode
            );

        ministereCulture =
            createOneMinistere(
                MEMBRES_GOUVERNEMENT.get(MINISTERE_CULTURE_KEY).getLeft(),
                MEMBRES_GOUVERNEMENT.get(MINISTERE_CULTURE_KEY).getMiddle(),
                "MIC",
                MEMBRES_GOUVERNEMENT.get(MINISTERE_CULTURE_KEY).getRight(),
                gvtNode
            );

        newMinistereEco =
            createOneMinistere(
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_ECONOMIE_KEY).getLeft(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_ECONOMIE_KEY).getMiddle(),
                NEW_MINISTERE_ECONOMIE_KEY.substring(4, 7).toUpperCase(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_ECONOMIE_KEY).getRight(),
                newGvtNode
            );

        newMinistereAgriculture =
            createOneMinistere(
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_AGRICULTURE_KEY).getLeft(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_AGRICULTURE_KEY).getMiddle(),
                NEW_MINISTERE_AGRICULTURE_KEY.substring(4, 7).toUpperCase(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_AGRICULTURE_KEY).getRight(),
                newGvtNode
            );

        newMinistereJustice =
            createOneMinistere(
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_JUSTICE_KEY).getLeft(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_JUSTICE_KEY).getMiddle(),
                NEW_MINISTERE_JUSTICE_KEY.substring(4, 7).toUpperCase(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_JUSTICE_KEY).getRight(),
                newGvtNode
            );

        newMinistereEducation =
            createOneMinistere(
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_EDUCATION_KEY).getLeft(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_EDUCATION_KEY).getMiddle(),
                NEW_MINISTERE_EDUCATION_KEY.substring(4, 7).toUpperCase(),
                MEMBRES_NEW_GOUVERNEMENT.get(NEW_MINISTERE_EDUCATION_KEY).getRight(),
                newGvtNode
            );
    }

    private void createUnitesStructurelles() {
        directionEcoBdc = createOneDirection(ministereEco, "50000655", "Bureau des Cabinets (Economie)");
        directionAgriBdc = createOneDirection(ministereAgriculture, "50001443", "Agents BDC Agriculture");
        directionJusticeBdc = createOneDirection(ministereJustice, "50001086", "Cabinet du garde des sceaux");
        directionEducationBdc = createOneDirection(ministereEducation, "50001257", "Bureau des Cabinets (Education)");
        directionCultureBdc = createOneDirection(ministereCulture, "50001269", "Bureau des Cabinets (Culture)");

        newDirectionEcoBdc = createOneDirection(newMinistereEco, "50001655", "New Bureau des Cabinets (Economie)");
        newDirectionAgriBdc = createOneDirection(newMinistereAgriculture, "50002443", "New Agents BDC Agriculture");
        newDirectionJusticeBdc = createOneDirection(newMinistereJustice, "50002086", "New Cabinet du garde des sceaux");
        newDirectionEducationBdc =
            createOneDirection(newMinistereEducation, "50002257", "New Bureau des Cabinets (Education)");
    }

    private void createPostes() {
        posteAgentBdc = createOnePoste(directionEcoBdc, POSTE_50000656, "Agents BDC (Economie)", false);
        posteAgentDgefp = createOnePoste(directionEcoBdc, "50001188", "Agents DGEFP", false);
        posteDlfEco = createOnePoste(directionEcoBdc, "50001933", "DLF (Economie)", false);
        posteAgentsSecGenEco = createOnePoste(directionEcoBdc, "50001217", "Agents sec. général (Economie)", false);
        posteAdminSolonSgg = createOnePoste(directionEcoBdc, POSTE_50002249, "Administrateur S.O.L.O.N. (SGG)", false);
        posteConsAffEcoPm = createOnePoste(directionEcoBdc, "60000000", "Conseillère Aff. éco et finances PM", false);
        posteSecConsAffEcoPm =
            createOnePoste(directionEcoBdc, "50000612", "Secrétariat Conseillère Aff. éco et finances PM", false);
        posteBdcAgri = createOnePoste(directionAgriBdc, "50001454", "Agents BDC Agriculture", false);
        createOnePoste(directionJusticeBdc, "50001097", "BDC Justice", false);
        posteBdcEcoMEDAD = createOnePoste(directionAgriBdc, "50000652", "Agents BDC Ecologie MEDAD", false);

        createOnePoste(directionEcoBdc, POSTE_50000656, "Agents BDC (Economie)");
        createOnePoste(directionEcoBdc, "50001188", "Agents DGEFP");
        createOnePoste(directionEcoBdc, "50001933", "DLF (Economie)");
        createOnePoste(directionEcoBdc, "50001217", "Agents sec. général (Economie)");
        createOnePoste(directionEcoBdc, POSTE_50002249, "Administrateur S.O.L.O.N. (SGG)");
        createOnePoste(directionEcoBdc, "60000000", "Conseillère Aff. éco et finances PM");
        createOnePoste(directionEcoBdc, "50000612", "Secrétariat Conseillère Aff. éco et finances PM");
        createOnePoste(directionJusticeBdc, "50001097", "BDC Justice");
        createOnePoste(directionEducationBdc, "50000834", "Agents BDC Education");
        createOnePoste(directionCultureBdc, POSTE_50000934, "Agents BDC Culture");

        createOnePoste(newDirectionEcoBdc, "50001656", "New Agents BDC (Economie)");
        createOnePoste(newDirectionEcoBdc, "50002188", "New Agents DGEFP");
        createOnePoste(newDirectionEcoBdc, "50002933", "New DLF (Economie)");
        createOnePoste(newDirectionEcoBdc, "50002217", "New Agents sec. général (Economie)");
        createOnePoste(newDirectionEcoBdc, "50003249", "New Administrateur S.O.L.O.N. (SGG)");
        createOnePoste(newDirectionEcoBdc, "60001000", "New Conseillère Aff. éco et finances PM");
        createOnePoste(newDirectionEcoBdc, "50001612", "New Secrétariat Conseillère Aff. éco et finances PM");
        createOnePoste(newDirectionAgriBdc, "50002454", "New Agents BDC Agriculture");
        createOnePoste(newDirectionJusticeBdc, "50002097", "New BDC Justice");
        createOnePoste(newDirectionAgriBdc, "50001652", "New Agents BDC Ecologie MEDAD");
        createOnePoste(newDirectionEducationBdc, "50001834", "New Agents BDC Education");
    }

    private PosteNode createOnePoste(
        UniteStructurelleNode parentNode,
        String id,
        String label,
        boolean superviseurSGG
    ) {
        PosteNode posteNode = posteService.getBarePosteModel();

        // ajout du parent
        posteNode.setParentUniteId(parentNode.getId());
        // Création
        posteNode.setId(id);
        posteNode.setChargeMissionSGG(false);
        posteNode.setConseillerPM(false);
        posteNode.setPosteBdc(true);
        posteNode.setPosteWs(false);
        posteNode.setDateDebut(Calendar.getInstance());
        posteNode.setLabel(label);
        posteNode.setSuperviseurSGG(superviseurSGG);
        posteService.createPoste(session, posteNode);

        return posteNode;
    }

    private PosteNode createOnePoste(UniteStructurelleNode parentNode, String id, String label) {
        PosteNode posteNode = posteService.getBarePosteModel();

        // ajout du parent
        posteNode.setParentUniteId(parentNode.getId());
        // Création
        posteNode.setId(id);
        posteNode.setChargeMissionSGG(false);
        posteNode.setConseillerPM(false);
        posteNode.setPosteBdc(true);
        posteNode.setPosteWs(false);
        posteNode.setDateDebut(Calendar.getInstance());
        posteNode.setLabel(label);
        posteService.createPoste(session, posteNode);

        return posteNode;
    }

    private EntiteNode createOneMinistere(
        String id,
        String label,
        String nor,
        Long ordreProtocolaire,
        GouvernementNode parentNode
    ) {
        EntiteNode entiteNode = ministereService.getBareEntiteModel();
        // ajout du parent
        entiteNode.setParentGouvernement(parentNode.getId());

        String infosMinistre = "à renseigner";

        // Création
        entiteNode.setId(id);
        entiteNode.setDateDebut(Calendar.getInstance().getTime());
        entiteNode.setLabel(label);
        entiteNode.setFormule(label);
        entiteNode.setEdition(label);
        entiteNode.setMembreGouvernementCivilite(infosMinistre);
        entiteNode.setMembreGouvernementPrenom(infosMinistre);
        entiteNode.setMembreGouvernementNom(infosMinistre);
        entiteNode.setOrdre(ordreProtocolaire);
        entiteNode.setNorMinistere(nor);
        return ministereService.createEntite(entiteNode);
    }

    private UniteStructurelleNode createOneDirection(EntiteNode parentNode, String id, String label) {
        UniteStructurelleNode ustNode = usAndDirectionService.getBareUniteStructurelleModel();

        // ajout du parent
        ustNode.setParentEntiteId(parentNode.getId());
        // Création
        ustNode.setId(id);
        ustNode.setDateDebut(Calendar.getInstance());
        ustNode.setLabel(label);
        ustNode.setType(OrganigrammeType.DIRECTION);
        ustNode.setTypeValue("DIR");
        ustNode.setNorDirectionForMinistereId(parentNode.getId(), "X");
        return usAndDirectionService.createUniteStructurelle(ustNode);
    }

    private void createGouvernements() {
        gvtNode = STServiceLocator.getSTGouvernementService().getBareGouvernementModel();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1); // Hier
        gvtNode.setDateDebut(cal.getTime());
        gvtNode.setLabel("Gouvernement actuel");

        gouvernementService.createGouvernement(gvtNode);

        newGvtNode = STServiceLocator.getSTGouvernementService().getBareGouvernementModel();

        cal = Calendar.getInstance(); // Aujourd'hui
        newGvtNode.setDateDebut(cal.getTime());
        newGvtNode.setLabel("Nouveau gouvernement");

        gouvernementService.createGouvernement(newGvtNode);
    }

    /**
     * Injection de feuilles de route.
     */
    private void injectFdR() {
        createFdR("FDR1", ministereEco, 2L);
        createFdR("FDR2", ministereCulture, 3L);
        createFdR("FDR3", ministereEco, 4L);
        createFdR("FDR4", ministereEco, 5L);
        createFdR("FDR5", ministereEco, 6L);
        createFdR("FDR6", ministereEco, 7L);
        createFdR("FDR7", ministereEco, 8L);
        createFdR("FDR8", ministereEco, 9L);
        createFdR("FDR9", ministereEco, 10L);
        createFdR("FDR10", ministereEco, 11L);
        createFdR("FDR11", ministereEco, 12L);
        createFdR("FDR12", ministereEco, 13L);
        createFdR("FDR13", ministereEco, 14L);
        createFdR("FDR14", ministereCulture, 15L);
        createFdR("FDR15", ministereCulture, 16L);
        createFdR("FDR16", ministereCulture, 17L);

        createSquelette("Squelette 1", ministereAgriculture, "7", 4L);
        createSquelette("Squelette 2", ministereAgriculture, "18", 5L);
        createSquelette("Squelette 3", ministereAgriculture, "2", 6L);
    }

    private void createFdR(String nom, EntiteNode entite, Long numero) {
        SolonEpgFeuilleRoute fdr = initFdR(entite.getNorMinistere() + " - " + nom, entite, true, numero);

        createRouteStep(
            fdr.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG,
            3L,
            posteSecConsAffEcoPm,
            false,
            false,
            false
        );
        createRouteStep(
            fdr.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO,
            3L,
            posteAdminSolonSgg,
            false,
            false,
            false
        );

        documentRoutingService.lockDocumentRoute(fdr, session);
        documentRoutingService.validateRouteModel(fdr, session);
        documentRoutingService.unlockDocumentRoute(fdr, session);

        // Ajout dans FavorisConsultation
        ajoutFavorisConsultation(fdr.getDocument());
    }

    private SolonEpgFeuilleRoute initFdR(String fdrName, EntiteNode ministere, boolean fdrDefaut, Long numero) {
        DocumentModel docModel = session.createDocumentModel(
            SSConstant.FDR_FOLDER_PATH,
            fdrName,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE
        );
        DublincoreSchemaUtils.setTitle(docModel, fdrName);
        docModel = session.createDocument(docModel);
        session.save();

        SolonEpgFeuilleRoute fdr = docModel.getAdapter(SolonEpgFeuilleRoute.class);
        fdr.setNumero(numero);
        fdr.setFeuilleRouteDefaut(fdrDefaut);
        fdr.setMinistere(ministere.getLabel());
        fdr.setMinistere(ministere.getId());

        session.saveDocument(docModel);

        session.save();

        fdr = session.getDocument(fdr.getDocument().getRef()).getAdapter(SolonEpgFeuilleRoute.class);

        return fdr;
    }

    private void createRouteStep(
        DocumentModel parent,
        String type,
        Long deadline,
        PosteNode poste,
        boolean automaticValidation,
        boolean obligatoireMinistere,
        boolean obligatoireSGG
    ) {
        String mailboxId = posteToId(poste);
        String stepName = "etape_" + type + "_" + mailboxId;
        DocumentModel stepDoc = session.createDocumentModel(
            parent.getPathAsString(),
            stepName,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );
        DublincoreSchemaUtils.setTitle(stepDoc, stepName);
        stepDoc = session.createDocument(stepDoc);
        SolonEpgRouteStep routeStep = stepDoc.getAdapter(SolonEpgRouteStep.class);
        routeStep.setDeadLine(deadline);
        routeStep.setDistributionMailboxId(mailboxId);
        routeStep.setDocumentRouteId(parent.getId());
        routeStep.setMailSend(false);
        routeStep.setObligatoireMinistere(obligatoireMinistere);
        routeStep.setObligatoireSGG(obligatoireSGG);
        routeStep.setType(type);
        routeStep.setAutomaticValidation(automaticValidation);
        session.saveDocument(stepDoc);
    }

    private String posteToId(PosteNode poste) {
        return "poste-" + poste.getId();
    }

    private void createSquelette(String nom, EntiteNode entite, String typeActe, Long numero) {
        SolonEpgFeuilleRoute squelette = initSquelette(nom, entite, typeActe, numero);

        createRouteStepSquelette(
            squelette.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG,
            3L,
            null,
            false,
            false,
            false,
            SqueletteStepTypeDestinataire.BUREAU_DU_CABINET
        );
        createRouteStepSquelette(
            squelette.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            3L,
            posteAdminSolonSgg,
            false,
            false,
            false,
            SqueletteStepTypeDestinataire.ORGANIGRAMME
        );
        createRouteStepSquelette(
            squelette.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            3L,
            null,
            false,
            false,
            false,
            SqueletteStepTypeDestinataire.CHARGE_DE_MISSION
        );
        createRouteStepSquelette(
            squelette.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            3L,
            posteAdminSolonSgg,
            false,
            false,
            false,
            SqueletteStepTypeDestinataire.ORGANIGRAMME
        );
        createRouteStepSquelette(
            squelette.getDocument(),
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO,
            3L,
            null,
            false,
            false,
            false,
            SqueletteStepTypeDestinataire.CONSEILLER_PM
        );

        documentRoutingService.lockDocumentRoute(squelette, session);
        documentRoutingService.validateSquelette(squelette, session);
        documentRoutingService.unlockDocumentRoute(squelette, session);
    }

    private void createRouteStepSquelette(
        DocumentModel parent,
        String type,
        Long deadline,
        PosteNode poste,
        boolean automaticValidation,
        boolean obligatoireMinistere,
        boolean obligatoireSGG,
        SqueletteStepTypeDestinataire typeDestinataire
    ) {
        String mailboxId = null;
        if (poste != null) {
            mailboxId = posteToId(poste);
        }
        String stepName = "etape_" + type + "_" + mailboxId;
        DocumentModel stepDoc = session.createDocumentModel(
            parent.getPathAsString(),
            stepName,
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE
        );
        DublincoreSchemaUtils.setTitle(stepDoc, stepName);
        stepDoc = session.createDocument(stepDoc);
        SolonEpgRouteStep routeStep = stepDoc.getAdapter(SolonEpgRouteStep.class);
        routeStep.setDeadLine(deadline);
        routeStep.setDistributionMailboxId(mailboxId);
        routeStep.setDocumentRouteId(parent.getId());
        routeStep.setMailSend(false);
        routeStep.setObligatoireMinistere(obligatoireMinistere);
        routeStep.setObligatoireSGG(obligatoireSGG);
        routeStep.setType(type);
        routeStep.setAutomaticValidation(automaticValidation);
        routeStep.setTypeDestinataire(typeDestinataire);
        session.saveDocument(routeStep.getDocument());
    }

    private SolonEpgFeuilleRoute initSquelette(
        String squeletteName,
        EntiteNode ministere,
        String typeActe,
        Long numero
    ) {
        DocumentModel docModel = session.createDocumentModel(
            SolonEpgConstant.SQUELETTE_FOLDER_PATH,
            squeletteName,
            SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE
        );
        DublincoreSchemaUtils.setTitle(docModel, squeletteName);
        docModel = session.createDocument(docModel);
        session.save();

        SolonEpgFeuilleRoute squelette = docModel.getAdapter(SolonEpgFeuilleRoute.class);
        squelette.setNumero(numero);
        squelette.setMinistere(ministere.getLabel());
        squelette.setMinistere(ministere.getId());
        squelette.setTypeActe(typeActe);

        session.saveDocument(docModel);

        session.save();

        squelette = session.getDocument(squelette.getDocument().getRef()).getAdapter(SolonEpgFeuilleRoute.class);

        return squelette;
    }

    /**
     * Injection de dossier.
     */
    private void injectDossier() {
        createDossier1();
        createDossier2();
        createDossierPourArchivageIntermediaire();
        createDossierPourAbandon();
        createDossierPourElimination();
        createDossierPourEpreuve();
        createDossierPubliePourArchivage();
        createDossierPourArchivageDefinitive();
        createDossierPublieSansActeIntegralPourArchivage();
        createDossierMGPP("MICB2013097L", ministereCulture, directionCultureBdc);
        createDossierMGPP("ECOE1902865L", ministereEco, directionEcoBdc);
    }

    private void createDossierMGPP(String nor, EntiteNode ministere, UniteStructurelleNode direction) {
        byte[] data = null;
        File file = new File(GENERIC_FILENAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(FILE_CONTENT);
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new NuxeoException(e);
        }

        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            Dossier dossier = createDossier(
                POSTE_50000934,
                SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50000934),
                nor,
                "26",
                ministere.getId(),
                direction.getId()
            );
            SolonEpgServiceLocator
                .getParapheurService()
                .createParapheurFile(
                    openCoreSession,
                    ACTE_INTEGRAL_FILENAME,
                    data,
                    SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME,
                    dossier.getDocument()
                );
        }
    }

    private void createDossier1() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000001A",
            "1",
            "50000507",
            "50000655"
        );
        dossier.setCandidat(VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_FONCTIONNEL);
        byte[] data = null;
        File file = new File(GENERIC_FILENAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(FILE_CONTENT);
            writer.close();
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new NuxeoException(e);
        }

        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator
                .getParapheurService()
                .createParapheurFile(
                    openCoreSession,
                    ACTE_INTEGRAL_FILENAME,
                    data,
                    SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME,
                    dossier.getDocument()
                );
        }
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier.getDocument());
        }

        // Ajout dans FavorisConsultation
        ajoutFavorisConsultation(dossier.getDocument());

        session.save();
    }

    private void createDossier2() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000002A",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setCandidat(VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_MINISTERIEL);

        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier.getDocument());
        }

        // Ajout dans FavorisConsultation
        ajoutFavorisConsultation(dossier.getDocument());

        session.save();
    }

    private void createDossierPourArchivageIntermediaire() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000007B",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE);

        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier.getDocument());
        }

        session.saveDocument(dossier.getDocument());
    }

    private void createDossierPourAbandon() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000009A",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setStatut(VocabularyConstants.STATUT_ABANDONNE);
        dossier.setCandidat(VocabularyConstants.CANDIDAT_AUCUN);
        dossier.setTitle("Dossier abandonné");
        final Calendar dateCandidature = Calendar.getInstance();
        dateCandidature.add(Calendar.MONTH, 1);
        dossier.setDateCandidature(dateCandidature);
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier.getDocument());
        }
        session.saveDocument(dossier.getDocument());

        Dossier dossier2 = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000022L",
            "2",
            "50000507",
            "50000655"
        );
        dossier2.setStatut(VocabularyConstants.STATUT_INITIE);
        dossier2.setCandidat(VocabularyConstants.CANDIDAT_ABANDON);
        dossier2.setTitle("Test dossier candidat abandon");
        dossier2.setDateCandidature(dateCandidature);
        session.saveDocument(dossier2.getDocument());
    }

    private void createDossierPourElimination() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000066A",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setStatut(VocabularyConstants.STATUT_INITIE);
        dossier.setCandidat(VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_FONCTIONNEL);
        dossier.setTitle("Test élimination Admin fonctionnel");
        final Calendar dateCandidature = Calendar.getInstance();
        dateCandidature.add(Calendar.MONTH, 1);
        dossier.setDateCandidature(dateCandidature);
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier.getDocument());
        }
        session.saveDocument(dossier.getDocument());

        Dossier dossier2 = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000069L",
            "2",
            "50000507",
            "50000655"
        );
        dossier2.setStatut(VocabularyConstants.STATUT_INITIE);
        dossier2.setCandidat(VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_MINISTERIEL);
        dossier2.setTitle("Test élimination Admin ministériel");
        dossier2.setDateCandidature(dateCandidature);
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier2.getDocument());
        }
        session.saveDocument(dossier2.getDocument());
    }

    private void createDossierPourEpreuve() {
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            Dossier dos = createDossier(
                POSTE_50002249,
                SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
                "ECOX2000006A",
                "1",
                "50000507",
                "50000655"
            );
            // Créer Rep Epreuve FDD
            DocumentModel fddFolderAccessAllUser = SolonEpgServiceLocator
                .getFondDeDossierService()
                .getFondDossierFolder(
                    dos.getDocument(),
                    session,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
                    false
                );
            DocumentModel newFddFolder = SolonEpgServiceLocator
                .getFondDeDossierService()
                .createNewFolder(
                    session,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
                    fddFolderAccessAllUser,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES
                );
            // on le paramètre
            FondDeDossierFolder fddFolderEpreuves = newFddFolder.getAdapter(FondDeDossierFolder.class);
            fddFolderEpreuves.setIsDeletable(false);
            fddFolderEpreuves.save(session);

            //Créer Rep Epruev Parapheur
            DocumentModel parapheurDocRoot = dos.getParapheur(session).getDocument();
            DocumentModel newParapheurFolder = SolonEpgServiceLocator
                .getParapheurService()
                .createNewFolder(
                    session,
                    SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE,
                    parapheurDocRoot,
                    SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME
                );
            // on le paramètre
            ParapheurFolder parapheurFolderEpreuves = newParapheurFolder.getAdapter(ParapheurFolder.class);
            parapheurFolderEpreuves.setNbDocAccepteMax(SolonEpgParapheurConstants.NB_DOC_MAX_REP_EPREUVE);
            parapheurFolderEpreuves.save(session);
            session.save();
        }
    }

    private void createDossierPubliePourArchivage() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000003A",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setStatut(VocabularyConstants.STATUT_PUBLIE);
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        dossier.setVecteurPublication(
            Collections.singletonList(
                bulletinOfficielService.getIdForLibelle(session, EpgVecteurPublication.JOURNAL_OFFICIEL)
            )
        );

        byte[] data = null;
        File file = new File(GENERIC_FILENAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(FILE_CONTENT);
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new NuxeoException(e);
        }

        DocumentModel dossierDoc = dossier.getDocument();
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator
                .getParapheurService()
                .createParapheurFile(
                    openCoreSession,
                    ACTE_INTEGRAL_FILENAME,
                    data,
                    SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME,
                    dossierDoc
                );
        }
        dossierDoc = session.getDocument(dossierDoc.getRef()); // get updated dossier

        List<DocumentModel> stepsDoc = SSServiceLocator.getSSFeuilleRouteService().getSteps(session, dossierDoc);
        stepsDoc
            .stream()
            .map(doc -> doc.getAdapter(SSRouteStep.class))
            .forEach(
                step -> {
                    step.setValidationStatus("1");
                    step.save(session);
                }
            );

        RetourDila retour = dossierDoc.getAdapter(RetourDila.class);
        retour.setDateParutionJorf(new GregorianCalendar(2018, 1, 1));
        dossierDoc = retour.save(session);

        session.save();
    }

    private void createDossierPourArchivageDefinitive() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000008A",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);

        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator.getDossierSignaleService().verserDossier(openCoreSession, dossier.getDocument());
        }

        session.saveDocument(dossier.getDocument());
        session.save();
    }

    private void createDossierPublieSansActeIntegralPourArchivage() {
        Dossier dossier = createDossier(
            POSTE_50002249,
            SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, POSTE_50002249),
            "ECOX2000004A",
            "2",
            "50000507",
            "50000655"
        );
        dossier.setStatut(VocabularyConstants.STATUT_PUBLIE);
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        dossier.setVecteurPublication(
            Collections.singletonList(
                bulletinOfficielService.getIdForLibelle(session, EpgVecteurPublication.JOURNAL_OFFICIEL)
            )
        );

        byte[] data = null;
        File file = new File(GENERIC_FILENAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(FILE_CONTENT);
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new NuxeoException(e);
        }

        Blob blob = new ByteArrayBlob(data);
        blob.setFilename("Annexe.txt");
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            SolonEpgServiceLocator
                .getFondDeDossierService()
                .createFondDeDossierFile(
                    openCoreSession,
                    "Annexe.txt",
                    blob,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ANNEXES_NON_PUBLIEES,
                    dossier.getDocument()
                );
        }

        List<DocumentModel> stepsDoc = SSServiceLocator
            .getSSFeuilleRouteService()
            .getSteps(session, dossier.getDocument());
        stepsDoc
            .stream()
            .map(doc -> doc.getAdapter(SSRouteStep.class))
            .forEach(
                step -> {
                    step.setValidationStatus("1");
                    step.save(session);
                }
            );

        RetourDila retour = dossier.getDocument().getAdapter(RetourDila.class);
        retour.setDateParutionJorf(new GregorianCalendar(2018, 1, 1));
        retour.save(session);

        session.save();
    }

    private Dossier createDossier(
        String posteId,
        Mailbox userMailBox,
        String nor,
        String typeActe,
        String ministereResp,
        String directionResp
    ) {
        DocumentModel dossierDoc = session.createDocumentModel(STConstant.DOSSIER_DOCUMENT_TYPE);
        DublincoreSchemaUtils.setTitle(dossierDoc, nor);
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        dossier.setNumeroNor(nor);
        dossier.setTypeActe(typeActe);
        dossier.setMinistereResp(ministereResp);
        dossier.setDirectionResp(directionResp);
        Calendar calendar = DateUtil.addMonthsToNow(1);
        dossier.setDatePublication(calendar);
        return dossierService.createDossier(session, dossierDoc, posteId, userMailBox, "");
    }

    private void injectMailboxes() {
        // Mailbox utilisateurs
        createPersonalMailbox(USER_ADMINSGG);

        // Mailbox de postes
        mailboxPosteService.createPosteMailbox(session, posteAgentBdc.getId(), posteAgentBdc.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteAgentDgefp.getId(), posteAgentDgefp.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteDlfEco.getId(), posteDlfEco.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteAgentsSecGenEco.getId(), posteAgentsSecGenEco.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteAdminSolonSgg.getId(), posteAdminSolonSgg.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteConsAffEcoPm.getId(), posteConsAffEcoPm.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteSecConsAffEcoPm.getId(), posteSecConsAffEcoPm.getLabel());
        mailboxPosteService.createPosteMailbox(session, POSTE_50000934, "Agents BDC Culture");
        mailboxPosteService.createPosteMailbox(session, posteSecConsAffEcoPm.getId(), posteSecConsAffEcoPm.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteSecConsAffEcoPm.getId(), posteSecConsAffEcoPm.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteBdcAgri.getId(), posteBdcAgri.getLabel());
        mailboxPosteService.createPosteMailbox(session, posteBdcEcoMEDAD.getId(), posteBdcEcoMEDAD.getLabel());
    }

    private void createPersonalMailbox(String user) {
        // Create the personal mailbox for the user
        final String mailboxType = getCaseManagementDocumentTypeService().getMailboxType();
        DocumentModel mailboxModel = session.createDocumentModel(mailboxType);
        Mailbox mailbox = mailboxModel.getAdapter(Mailbox.class);

        // Set mailbox properties
        mailbox.setId(mailboxCreator.getPersonalMailboxId(user));
        mailbox.setTitle(user);
        mailbox.setOwner(user);
        mailbox.setType(MailboxConstants.type.personal);

        DocumentModelList res = session.query(
            String.format("SELECT * from %s", MailboxConstants.MAILBOX_ROOT_DOCUMENT_TYPE)
        );
        if (res == null || res.isEmpty()) {
            throw new NuxeoException("Cannot find any mailbox folder");
        }

        mailboxModel.setPathInfo(
            res.get(0).getPathAsString(),
            STDefaultMailboxCreator.generateMailboxName(mailbox.getTitle())
        );
        session.createDocument(mailboxModel);
        session.save();
    }

    private void createTableReference() {
        DocumentModel tableRefDoc = session.createDocumentModel(
            SolonEpgTableReferenceConstants.TABLE_REFERENCE_PATH,
            SolonEpgTableReferenceConstants.TABLE_REFERENCE_ID,
            SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE
        );
        TableReference tableRef = tableRefDoc.getAdapter(TableReference.class);
        tableRef.setPosteDanId(POSTE_50002249);
        tableRef.setMinisterePM(ministereEco.getId());
        tableRef.setDirectionsPM(Arrays.asList(directionEcoBdc.getId()));
        List<String> retourDan = new ArrayList<>();
        retourDan.add(POSTE_50002249);
        tableRef.setRetourDAN(retourDan);
        tableRef.setCabinetPM(Arrays.asList(USER_ADMINSGG));
        tableRef.setSignataires(Arrays.asList(USER_ADMINSGG, USER_CONTRIBUTEUR));

        session.createDocument(tableRefDoc);
        session.save();
    }

    private void ajoutFavorisConsultation(DocumentModel documentModel) {
        try (
            CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(
                session.getRepositoryName(),
                STServiceLocator.getUserManager().getPrincipal(USER_ADMINSGG)
            )
        ) {
            final DocumentModel docModel = STServiceLocator
                .getUserWorkspaceService()
                .getCurrentUserPersonalWorkspace(openCoreSession);
            String path = docModel.getPathAsString();
            SolonEpgServiceLocator
                .getEspaceRechercheService()
                .addToFavorisConsultation(openCoreSession, path, Lists.newArrayList(documentModel));
            openCoreSession.save();
        }
    }
}
