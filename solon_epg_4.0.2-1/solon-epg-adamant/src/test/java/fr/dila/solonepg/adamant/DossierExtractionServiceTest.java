package fr.dila.solonepg.adamant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.mockito.RuntimeService;
import org.nuxeo.runtime.test.runner.Features;

import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.adamant.test.SolonEPGAdamantFeature;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.solonepg.api.parapheur.Parapheur;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSParapheurConstants;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;

@Features(SolonEPGAdamantFeature.class)
public class DossierExtractionServiceTest extends AbstractEPGTest {

	private static final String ACTE_INTEGRAL_FILENAME = "Acte Intégral.txt";
	private static final String FILE_CONTENT = "File content";
	private static final String GENERIC_FILENAME = "target/fileName.txt";

	private static final Log LOG = LogFactory.getLog(DossierExtractionServiceTest.class);

	@Mock
	@RuntimeService
	ConversionService conversionService;

	@Mock
	@RuntimeService
	UIDGeneratorService uidGeneratorService;

	@Ignore("Il faut ajouter des assertions")
	@Test
	public void testArchivage() throws Exception {
		Mockito.doReturn("fakePDFConverter").when(conversionService).getConverterName(Matchers.any(),
				Matchers.eq("application/pdf"));
		BlobHolder bh = new SimpleBlobHolder(new StringBlob("contenu", "application/pdf", "UTF-8", "fake.pdf"));
		Mockito.doReturn(bh).when(conversionService).convert(Matchers.eq("fakePDFConverter"), Matchers.any(),
				Matchers.any());

		Long[] uid = { 0L };
		UIDSequencer sequencer = Mockito.mock(UIDSequencer.class);
		Mockito.doReturn(uid[0]++).when(sequencer).getNextLong(Matchers.anyString());
		Mockito.doReturn(sequencer).when(uidGeneratorService).getSequencer();

		DossierExtractionService dossierExtractionService = SolonEpgAdamantServiceLocator.getDossierExtractionService();
		Assert.assertNotNull("dossierExtractionService is null", dossierExtractionService);
		Dossier currentDossier = createDossierPubliePourArchivage();

		DossierExtractionLot currentLot = new DossierExtractionLot();
		currentLot.setCreationDate(new Date());
		currentLot.setName("1");
		currentLot.setStatus(ExtractionStatus.EN_ATTENTE);
		currentLot.setId(1);

		DossierExtractionBordereau bordereau = SolonEpgAdamantServiceLocator.getBordereauService()
				.initBordereau(currentDossier, currentLot);

		dossierExtractionService.genererDossierArchive(session, currentDossier, currentLot, bordereau);
	}

	private Dossier createDossierPubliePourArchivage() throws Exception {
		Dossier dossier = createDossier();
		Parapheur parapheur = createDocument(SSParapheurConstants.PARAPHEUR_DOCUMENT_TYPE, "0000")
				.getAdapter(Parapheur.class);
		FeuilleRoute feuilleRoute = createDocument(SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE, "1111")
				.getAdapter(FeuilleRoute.class);
		dossier.setNumeroNor("PRMX0000003X");
		dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES);
		dossier.setDocumentParapheurId(parapheur.getId());
		dossier.setStatut(VocabularyConstants.STATUT_PUBLIE);
		dossier.setLastDocumentRoute(feuilleRoute.getDocument().getId());

		BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
		dossier.setVecteurPublication(Collections.singletonList(
				bulletinOfficielService.getIdForLibelle(session, EpgVecteurPublication.JOURNAL_OFFICIEL)));

		byte[] data = null;
		File file = new File(GENERIC_FILENAME);
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(FILE_CONTENT);
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			throw new NuxeoException(e);
		}

		DocumentModel dossierDoc = dossier.getDocument();
		try (CloseableCoreSession openCoreSession = CoreInstance.openCoreSession(session.getRepositoryName(),
				STServiceLocator.getUserManager().getPrincipal(user))) {
			SolonEpgServiceLocator.getParapheurService().createParapheurFile(openCoreSession, ACTE_INTEGRAL_FILENAME,
					data, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME, dossierDoc);
		}
		dossierDoc = session.getDocument(dossierDoc.getRef()); // get updated dossier

		List<DocumentModel> stepsDoc = SSServiceLocator.getSSFeuilleRouteService().getSteps(session, dossierDoc);
		stepsDoc.stream().map(doc -> doc.getAdapter(SSRouteStep.class)).forEach(step -> {
			step.setValidationStatus("1");
			step.save(session);
		});

		RetourDila retour = dossierDoc.getAdapter(RetourDila.class);
		retour.setDateParutionJorf(new GregorianCalendar(2018, 1, 1));
		dossierDoc = retour.save(session);

		session.save();
		Framework.getService(EventService.class).waitForAsyncCompletion();

		return session.getDocument(dossierDoc.getRef()).getAdapter(Dossier.class);
	}
}
