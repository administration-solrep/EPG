package fr.dila.solon.birt.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SolonBirtParametersTest {
	private static final String JDBC_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ENABLE=BROKEN)(ADDRESS=(PROTOCOL=tcp)(PORT=1521)(HOST=idlv-solon-db-ora-112-inte-1.lyon-dev2.local))(CONNECT_DATA=(SID=ORA112)))";

	private static final String JDBC_PWD = "SOLONEPG_INTE";

	private static final String JDBC_USR = "SOLONEPG_INTE";

	private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	@Test
	public void testSerialization() {
		SolonBirtParameters params = new SolonBirtParameters();

		params.setOutputFormat(BirtOutputFormat.HTML);

		params.setReportModelName("reportName");
		HashMap<String, Serializable> scalarParameters = new HashMap<>();
		scalarParameters.put("PERMISSIONS_PARAM",
				"Browse&Classify&Read&ReadProperties&ReadRemove&ReadWrite&Everything&");
		scalarParameters.put("USERS_PARAM",
				"Administrateur fonctionnel&Contributeur du SGG&EspaceActiviteNormativeSuivHabUpdater&OrganigrammeAdminUnlocker&GestionDeListeReader&DossierAdminUnlocker&AdminFonctionnelTransfertExecutor&SupprimerVersion&DossierAdminUpdater&WsSpeEnvoyerRetourPE&WsSpeEnvoyerDemandeSuivantePE&EspaceTraitementReader&routeManagers&DossierRechercheReader&AdminFonctionnelTableauDynamiqueDestinataireViewer&AdministrationBulletinReader&AdminFonctionnelEmailReceiver&UtilisateurReader&EtapePourContreseingExecutor&EspaceActiviteNormativeTraitAccUpdater&FondDossierRepertoireSGG&AdministrationChangementGouvernement&AccuserReception&EtapeBonATirerCreator&FDRModelValidator&EspaceRechercheReader&DossierParapheurOrFondDossierDeleter&WsEpgModifierDossierCE&AdminFonctionnelStatReader&AdministrationFondDeDossier&DecretArriveReader&EtapePourAttributionSggExecutor&EspaceSuiviReader&OrganigrammeReader&WsEpgModifierDossierInfosParlementaires&WsEpgCreerDossierAvis&WsEpgCreerDossierInfosParlementaires&TraitementPapierWriter&EspaceActiviteNormativeExportDataUpdater&WsEpgChercherModificationDossier&WsEpgModifierDossierDecretPrInd&AdministrationParamAdamant&UtilisateurUpdater&WsEpgAttribuerNor&EspaceActiviteNormativeTranspositionReader&EtapePourSignatureExecutor&EspaceActiviteNormativePublicationExecutor&EspaceActiviteNormativeAppOrdoReader&FDRModelUpdater&DossierParapheurUpdater&EspaceActiviteNormativeParamLegisUpdater&TexteSignaleCreator&UtilisateurCreator&ZoneCommentaireSggDilaUpdater&ConseilEtatUpdater&DossierArchivageAdamantReader&DossierIndexationUpdater&AdminAccessUnrestricted&EspaceActiviteNormativeRatOrdoUpdater&AccessUnrestrictedUpdater&EspaceActiviteNormativeAppOrdoUpdater&DossierMesureNominativeUpdater&WsEpgModifierDossierAvis&EspaceActiviteNormativeTraitAccReader&OrganigrammeUpdater&WsEpgChercherDossier&DossierArchivageIntermediaireReader&AdminFonctionnelDossierAbandon&InfocentreGeneralReader&ProfilSGG&UtilisateurDeleter&ProfilCreator&PosteFieldsUpdater&DroitVueMgpp&AdministrationJournalReader&ProfilDeleter&EtapeObligatoireUpdater&InterfaceAccess&RejeterEvenement&DossierDistributionMinistereReader&FDRInstanceRestarter&CreerAlerte&EspaceActiviteNormativeTranspositionUpdater&AdministrationIndexationUpdater&DossierRattachementDirectionReader&CorbeilleMGPPUpdater&AccepterEvenement&EtapeFournitureEpreuveCreator&EnvoiSaisinePieceComplementaireExecutor&AdministrationReferenceReader&TextesSignalesSGGReader&DossierActiviteNormativeUpdater&EspaceActiviteNormativeAppLoisReader&DossierRattachementMinistereReader&HistoriqueMajMinisterielReader&WsEpgRechercherDossier&ProfilUpdater&FDRModelMassCreator&NoteEtapeDeleter&AdminFonctionnelDossierSuppression&JournalReader&AdministrationParapheur&AllowAddPosteAllMinistere&EspaceAdministrationReader&AdministrationParamReader&WsEpgCreerDossierDecretPrInd&DossierFondDossierUpdater&DossierEnder&DossierCreator&EtapePublicationDilaJoCreator&EspaceParlementaireReader&EspaceActiviteNormativeAppLoisUpdater&DossierNorAttribueUpdater&WsSpeEnvoyerPremiereDemandePE&UtilisateurRechercheReader&TextesSignalesUpdater&InfocentreGeneralDirReader&WsEpgDonnerAvisCE&IndexationSGGUpdater&DossierDistributionAdminUpdater&EspaceActiviteNormativeSuivHabReader&EspaceCreationReader&FDRSqueletteUpdater&TraitementPapierReader&ProfilReader&InfocentreGeneralFullReader&AdministrationIndexationReader&EspaceActiviteNormativeRatOrdoReader&FDRModelPosteMassSubstitutor&DossierArchivageDefinitifReader&InfocentreSGGReader&DossierDistributionDirectionReader&DossierMesureNominativeReader&DossierTypeActeDecretUpdater&BatchSuiviReader&BordereauDateSignatureUpdater&ProfilWebserviceUpdater&AdminMinisterielEmailReceiver&mailbox_user-nbortoluzzi&mailbox_poste-50001425&poste-50000682&poste-50002248&poste-60001894&mailbox_poste-50002248&mailbox_poste-50000682&mailbox_poste-60001894&mailbox_poste-50001565&poste-50001425&poste-50001565&min-60001561&min-50000607&min-60001567&dossier_dist_min-50000607&dossier_dist_min-60001561&dossier_dist_min-60001567&dossier_dist_dir-60001892&dossier_dist_dir-50000615&dossier_rattach_min-60001561&dossier_rattach_min-60001567&dossier_rattach_min-50000607&dossier_rattach_dir-50000615&dossier_rattach_dir-60001892&nbortoluzzi&Everyone&");
		scalarParameters.put("DISTRIBUTIONMAILBOXLABEL_PARAM", "Administrateur S.O.L.O.N. (SGG)");
		scalarParameters.put("DISTRIBUTIONMAILBOXID_PARAM", "poste-50002248");
		params.setScalarParameters(scalarParameters);
		
		params.setJdbcDriver(JDBC_DRIVER);
		params.setJdbcUser(JDBC_USR);
		params.setJdbcPassword(JDBC_PWD);
		params.setJdbcUrl(JDBC_URL);

		String jsonStr = null;
		try {
			jsonStr = SerializationUtils.serialize(params);
		} catch (JsonProcessingException e) {
			fail();
		}

		assertNotNull(jsonStr);
		SolonBirtParameters parameters2 = null;
		try {
			parameters2 = SerializationUtils.deserialize(jsonStr);
		} catch (Exception e) {
			fail();
		}

		assertNotNull(parameters2);
		
		assertEquals(params.getOutputFormat(), parameters2.getOutputFormat());
		assertEquals(params.getReportModelName(), parameters2.getReportModelName());
		assertEquals(params.getScalarParameters(), parameters2.getScalarParameters());
		assertEquals(params.getJdbcDriver(), parameters2.getJdbcDriver());
		assertEquals(params.getJdbcUser(), parameters2.getJdbcUser());
		assertEquals(params.getJdbcPassword(), parameters2.getJdbcPassword());
		assertEquals(params.getJdbcUrl(), parameters2.getJdbcUrl());
	}
}
