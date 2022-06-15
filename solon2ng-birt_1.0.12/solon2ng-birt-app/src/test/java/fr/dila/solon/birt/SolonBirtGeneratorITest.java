package fr.dila.solon.birt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import fr.dila.solon.birt.common.BirtOutputFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

/**
 * Tests d'intégration.
 *
 * @author tlombard
 */
public class SolonBirtGeneratorITest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    private static final String GOOD_PARAMETER =
        "{\"reportModelName\":\"hello_world\",\"outputFormat\":\"HTML\",\"scalarParameters\":{\"PERMISSIONS_PARAM\":\"Browse&Classify&Read&ReadProperties&ReadRemove&ReadWrite&Everything&\",\"DISTRIBUTIONMAILBOXID_PARAM\":\"poste-50002248\",\"DISTRIBUTIONMAILBOXLABEL_PARAM\":\"Administrateur S.O.L.O.N. (SGG)\",\"USERS_PARAM\":\"Administrateur fonctionnel&Contributeur du SGG&EspaceActiviteNormativeSuivHabUpdater&OrganigrammeAdminUnlocker&GestionDeListeReader&DossierAdminUnlocker&AdminFonctionnelTransfertExecutor&SupprimerVersion&DossierAdminUpdater&WsSpeEnvoyerRetourPE&WsSpeEnvoyerDemandeSuivantePE&EspaceTraitementReader&routeManagers&DossierRechercheReader&AdminFonctionnelTableauDynamiqueDestinataireViewer&AdministrationBulletinReader&AdminFonctionnelEmailReceiver&UtilisateurReader&EtapePourContreseingExecutor&EspaceActiviteNormativeTraitAccUpdater&FondDossierRepertoireSGG&AdministrationChangementGouvernement&AccuserReception&EtapeBonATirerCreator&FDRModelValidator&EspaceRechercheReader&DossierParapheurOrFondDossierDeleter&WsEpgModifierDossierCE&AdminFonctionnelStatReader&AdministrationFondDeDossier&DecretArriveReader&EtapePourAttributionSggExecutor&EspaceSuiviReader&OrganigrammeReader&WsEpgModifierDossierInfosParlementaires&WsEpgCreerDossierAvis&WsEpgCreerDossierInfosParlementaires&TraitementPapierWriter&EspaceActiviteNormativeExportDataUpdater&WsEpgChercherModificationDossier&WsEpgModifierDossierDecretPrInd&AdministrationParamAdamant&UtilisateurUpdater&WsEpgAttribuerNor&EspaceActiviteNormativeTranspositionReader&EtapePourSignatureExecutor&EspaceActiviteNormativePublicationExecutor&EspaceActiviteNormativeAppOrdoReader&FDRModelUpdater&DossierParapheurUpdater&EspaceActiviteNormativeParamLegisUpdater&TexteSignaleCreator&UtilisateurCreator&ZoneCommentaireSggDilaUpdater&ConseilEtatUpdater&DossierArchivageAdamantReader&DossierIndexationUpdater&AdminAccessUnrestricted&EspaceActiviteNormativeRatOrdoUpdater&AccessUnrestrictedUpdater&EspaceActiviteNormativeAppOrdoUpdater&DossierMesureNominativeUpdater&WsEpgModifierDossierAvis&EspaceActiviteNormativeTraitAccReader&OrganigrammeUpdater&WsEpgChercherDossier&DossierArchivageIntermediaireReader&AdminFonctionnelDossierAbandon&InfocentreGeneralReader&ProfilSGG&UtilisateurDeleter&ProfilCreator&PosteFieldsUpdater&DroitVueMgpp&AdministrationJournalReader&ProfilDeleter&EtapeObligatoireUpdater&InterfaceAccess&RejeterEvenement&DossierDistributionMinistereReader&FDRInstanceRestarter&CreerAlerte&EspaceActiviteNormativeTranspositionUpdater&AdministrationIndexationUpdater&DossierRattachementDirectionReader&CorbeilleMGPPUpdater&AccepterEvenement&EtapeFournitureEpreuveCreator&EnvoiSaisinePieceComplementaireExecutor&AdministrationReferenceReader&TextesSignalesSGGReader&DossierActiviteNormativeUpdater&EspaceActiviteNormativeAppLoisReader&DossierRattachementMinistereReader&HistoriqueMajMinisterielReader&WsEpgRechercherDossier&ProfilUpdater&FDRModelMassCreator&NoteEtapeDeleter&AdminFonctionnelDossierSuppression&JournalReader&AdministrationParapheur&AllowAddPosteAllMinistere&EspaceAdministrationReader&AdministrationParamReader&WsEpgCreerDossierDecretPrInd&DossierFondDossierUpdater&DossierEnder&DossierCreator&EtapePublicationDilaJoCreator&EspaceParlementaireReader&EspaceActiviteNormativeAppLoisUpdater&DossierNorAttribueUpdater&WsSpeEnvoyerPremiereDemandePE&UtilisateurRechercheReader&TextesSignalesUpdater&InfocentreGeneralDirReader&WsEpgDonnerAvisCE&IndexationSGGUpdater&DossierDistributionAdminUpdater&EspaceActiviteNormativeSuivHabReader&EspaceCreationReader&FDRSqueletteUpdater&TraitementPapierReader&ProfilReader&InfocentreGeneralFullReader&AdministrationIndexationReader&EspaceActiviteNormativeRatOrdoReader&FDRModelPosteMassSubstitutor&DossierArchivageDefinitifReader&InfocentreSGGReader&DossierDistributionDirectionReader&DossierMesureNominativeReader&DossierTypeActeDecretUpdater&BatchSuiviReader&BordereauDateSignatureUpdater&ProfilWebserviceUpdater&AdminMinisterielEmailReceiver&mailbox_user-nbortoluzzi&mailbox_poste-50001425&poste-50000682&poste-50002248&poste-60001894&mailbox_poste-50002248&mailbox_poste-50000682&mailbox_poste-60001894&mailbox_poste-50001565&poste-50001425&poste-50001565&min-60001561&min-50000607&min-60001567&dossier_dist_min-50000607&dossier_dist_min-60001561&dossier_dist_min-60001567&dossier_dist_dir-60001892&dossier_dist_dir-50000615&dossier_rattach_min-60001561&dossier_rattach_min-60001567&dossier_rattach_min-50000607&dossier_rattach_dir-50000615&dossier_rattach_dir-60001892&nbortoluzzi&Everyone&\"},\"jdbcUrl\":\"jdbc:oracle:thin:@(DESCRIPTION=(ENABLE=BROKEN)(ADDRESS=(PROTOCOL=tcp)(PORT=1521)(HOST=idlv-solon-db-ora-112-inte-1.lyon-dev2.local))(CONNECT_DATA=(SID=ORA112)))\",\"jdbcUser\":\"SOLONEPG_INTE\",\"jdbcPassword\":\"SOLONEPG_INTE\",\"jdbcDriver\":\"oracle.jdbc.driver.OracleDriver\"}";
    private static final String BAD_PARAMETER_REPORT_UNKNOWN = GOOD_PARAMETER.replace("hello_world", "fake");
    private static final String BAD_PARAMETER_FORMAT_UNKNOWN = GOOD_PARAMETER.replace("HTML", "unknown");
    private String configFilepath = "";

    @Before
    public void setUp() {
        configFilepath = this.getClass().getClassLoader().getResource("config.properties").getFile();
    }

    @Test
    public void testMainOk() {
        exit.expectSystemExitWithStatus(SolonBirtGenerator.CODE_OK);
        String args[] = new String[] { GOOD_PARAMETER, configFilepath };
        SolonBirtGenerator.main(args);
    }

    @Test
    public void testMainNoArgument() {
        exit.expectSystemExitWithStatus(SolonBirtGenerator.CODE_ERROR_WRONG_NUMBER_OF_PARAMS);
        String args[] = new String[] {};
        SolonBirtGenerator.main(args);
    }

    @Test
    public void testMainThreeArguments() {
        exit.expectSystemExitWithStatus(SolonBirtGenerator.CODE_ERROR_WRONG_NUMBER_OF_PARAMS);
        String args[] = new String[] { GOOD_PARAMETER, configFilepath, "too many arguments" };
        SolonBirtGenerator.main(args);
    }

    @Test
    public void testMainReportDoesNotExist() {
        exit.expectSystemExitWithStatus(SolonBirtGenerator.CODE_ERROR_UNKNOWN_REPORT);
        String args[] = new String[] { BAD_PARAMETER_REPORT_UNKNOWN, configFilepath };
        SolonBirtGenerator.main(args);
    }

    @Test
    public void testMainFormatNotRecognized() {
        exit.expectSystemExitWithStatus(SolonBirtGenerator.CODE_ERROR_JSON_PARAM);
        String args[] = new String[] { BAD_PARAMETER_FORMAT_UNKNOWN, configFilepath };
        SolonBirtGenerator.main(args);
    }

    @Test
    public void testVersion() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        String args[] = new String[] { SolonBirtGenerator.MODE_VERSION };

        SolonBirtGenerator generator = new SolonBirtGenerator();
        int result = generator.run(args);

        assertEquals(SolonBirtGenerator.CODE_OK, result);
        assertNotNull(out.toString());

        exit.expectSystemExitWithStatus(0);
        SolonBirtGenerator.main(args);
    }

    @Test
    public void testInitRenderOption() throws IOException {
        SolonBirtGenerator generator = new SolonBirtGenerator();

        generator.properties = new Properties();
        generator.properties.put(SolonBirtGenerator.BIRT_OUTPUT_DIR_KEY, "/tmp");

        String givenFilename = "/tmp/desiredName.docx";
        String generatedOutputName = "generatedOutputName.xlsx";
        RenderOption renderOption = generator.initRenderOption(
            BirtOutputFormat.HTML,
            generatedOutputName,
            givenFilename,
            false
        );
        assertEquals(givenFilename, renderOption.getOutputFileName());

        renderOption = generator.initRenderOption(BirtOutputFormat.HTML, generatedOutputName, null, false);
        assertTrue(renderOption.getOutputFileName().contains(generatedOutputName));
    }
}
