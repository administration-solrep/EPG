package fr.dila.solonepg.core.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import com.google.common.collect.Lists;

import fr.dila.cm.cases.HasParticipants;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.cases.DossierImpl;
import fr.dila.st.api.service.VocabularyService;

public class TestDossierDistributionService {
	DossierDistributionServiceImpl	service;
	ActeService						acteService				= mock(ActeService.class);
	DossierBordereauService			bordereauService		= mock(DossierBordereauService.class);
	BulletinOfficielService			bulletinService			= mock(BulletinOfficielService.class);
	TableReferenceService			tabRefService			= mock(TableReferenceService.class);
	VocabularyService				vocService				= mock(VocabularyService.class);
	ParapheurService				parapheurService		= mock(ParapheurService.class);
	FondDeDossierService			fondDeDossierService	= mock(FondDeDossierService.class);
	FeuilleRouteService				feuilleRouteService		= mock(FeuilleRouteService.class);
	CoreSession						session					= mock(CoreSession.class);

	public void initCheckDossierBeforeValidate() {
		service = new DossierDistributionServiceImpl(bordereauService, bulletinService, tabRefService, vocService,
				parapheurService, fondDeDossierService, acteService, feuilleRouteService);
	}

	@Test
	public void testCasCheckDossierFinBeforeValidateOK() {
		initCheckDossierBeforeValidate();

		// Cas 1 : strict minimum pour valider le dossier sachant que le dossier
		// n'a pas d'étape future
		Dossier dossCas1 = createDossier();
		if (dossCas1 != null) {
			when(dossCas1.getIsParapheurComplet()).thenReturn(true);
			when(bordereauService.isBordereauComplet(dossCas1)).thenReturn(true);
			try {
				service.checkDossierBeforeValidateStep(session, dossCas1, new ArrayList<DocumentModel>());

			} catch (Exception e) {
				e.printStackTrace();
				fail("Erreur lors de la validation du dossier : " + e.getMessage());
			}
		}

	}

	@Test
	public void testCasCheckDossierAvantEtapePublicationOK() {
		initCheckDossierBeforeValidate();

		// Cas 2 : dossier avec sa prochaine étape qui est pour publication dila jo
		Dossier dossCas2 = createDossier();
		// minimum pour que les tests sans les étapes passe
		when(dossCas2.getIsParapheurComplet()).thenReturn(true);
		when(bordereauService.isBordereauComplet(dossCas2)).thenReturn(true);

		// minimum pour que les tests des étapes passent
		RetourDila mockRetour = mock(RetourDila.class);
		Calendar newDate = Calendar.getInstance();
		newDate.add(Calendar.MONTH, 1);
		when(dossCas2.getDocument().getAdapter(RetourDila.class)).thenReturn(mockRetour);
		when(mockRetour.getModeParution()).thenReturn("Electronique");
		when(dossCas2.getDelaiPublication()).thenReturn(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
		when(dossCas2.getDatePreciseePublication()).thenReturn(newDate);
		when(dossCas2.getPublicationIntegraleOuExtrait()).thenReturn("Intégrale");
		when(dossCas2.getVecteurPublication()).thenReturn(Lists.newArrayList("BODMR"));

		if (dossCas2 != null) {
			try {

				// Validation des vecteurs de publication
				IdRef docRef = new IdRef("BODMR");
				when(session.exists(docRef)).thenReturn(true);
				VecteurPublication vecteur = mock(VecteurPublication.class);
				DocumentModel docVecteur = mock(DocumentModel.class);
				when(vecteur.getIntitule()).thenReturn("BODMR");
				when(docVecteur.getAdapter(VecteurPublication.class)).thenReturn(vecteur);
				when(docVecteur.getId()).thenReturn("123");
				when(vecteur.getDocument()).thenReturn(docVecteur);
				when(session.getDocument(docRef)).thenReturn(docVecteur);
				when(docVecteur.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)).thenReturn(
						true);

				when(bulletinService.isAllVecteurPublicationActif(session, Lists.newArrayList("123"))).thenReturn(true);
				// Fin de validation des vecteurs de publication

				when(parapheurService.getPieceParapheur(session, dossCas2, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				String[] nextSteps = { VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.toString() };
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));

			} catch (Exception e) {
				e.printStackTrace();
				fail("Erreur lors de la validation du dossier : " + e.getMessage());
			}
		}
	}

	@Test
	public void testCasCheckDossierAvantDerniereEtapeOK() {
		initCheckDossierBeforeValidate();

		// Cas 2 : dossier avec sa prochaine étape qui n'est pas une étape finale
		// et où l'étape finale est pour publication dila BO
		Dossier dossCas3 = createDossier();
		// minimum pour que les tests sans les étapes passe
		when(dossCas3.getIsParapheurComplet()).thenReturn(true);
		when(bordereauService.isBordereauComplet(dossCas3)).thenReturn(true);

		// minimum pour que les tests des étapes passent
		RetourDila mockRetour = mock(RetourDila.class);
		Calendar newDate = Calendar.getInstance();
		newDate.add(Calendar.MONTH, 1);
		when(dossCas3.getDocument().getAdapter(RetourDila.class)).thenReturn(mockRetour);
		when(mockRetour.getModeParution()).thenReturn("Electronique");
		when(dossCas3.getDelaiPublication()).thenReturn(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
		when(dossCas3.getDatePreciseePublication()).thenReturn(newDate);
		when(dossCas3.getPublicationIntegraleOuExtrait()).thenReturn("Intégrale");
		when(dossCas3.getVecteurPublication()).thenReturn(Lists.newArrayList("BODMR"));

		if (dossCas3 != null) {
			try {

				// Validation des vecteurs de publication
				IdRef docRef = new IdRef("BODMR");
				when(session.exists(docRef)).thenReturn(false);
				VecteurPublication vecteur = mock(VecteurPublication.class);
				DocumentModel docVecteur = mock(DocumentModel.class);
				when(vecteur.getIntitule()).thenReturn("BODMR");
				when(docVecteur.getAdapter(VecteurPublication.class)).thenReturn(vecteur);
				when(docVecteur.getId()).thenReturn("BODMR");
				when(vecteur.getDocument()).thenReturn(docVecteur);
				when(session.getDocument(docRef)).thenReturn(docVecteur);

				when(bulletinService.isAllBulletinOfficielActif(session, Lists.newArrayList("BODMR"))).thenReturn(true);
				// Fin de validation des vecteurs de publication

				when(parapheurService.getPieceParapheur(session, dossCas3, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				String[] nextSteps = { VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE,
						VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO };
				service.checkDossierBeforeValidateStep(session, dossCas3, createListOfSteps(nextSteps));

			} catch (Exception e) {
				e.printStackTrace();
				fail("Erreur lors de la validation du dossier : " + e.getMessage());
			}
		}
	}

	@Test
	public void testCasCheckDossierAvantEtapePublicationKO() {
		initCheckDossierBeforeValidate();

		// Cas 2 : dossier avec sa prochaine étape qui est pour publication dila jo
		Dossier dossCas2 = createDossier();
		String[] nextSteps = { VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.toString() };
		Calendar oldDate = Calendar.getInstance();
		oldDate.set(2015, 01, 01);
		Calendar newDate = Calendar.getInstance();
		newDate.add(Calendar.MONTH, 1);

		when(dossCas2.getIsParapheurComplet()).thenReturn(true);
		when(bordereauService.isBordereauComplet(dossCas2)).thenReturn(true);

		if (dossCas2 != null) {
			try {
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("Le dossier ne peut pas être validé sans mode de parution", e.getMessage());
			}

			RetourDila mockRetour = mock(RetourDila.class);
			when(dossCas2.getDocument().getAdapter(RetourDila.class)).thenReturn(mockRetour);
			when(mockRetour.getModeParution()).thenReturn("Electronique");
			try {
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("Le répertoire \"Acte intégral\" ou \"Extrait\" du parapheur n'est pas complet",
						e.getMessage());
			}

			try {
				when(parapheurService.getPieceParapheur(session, dossCas2, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("L'étape suivante n'est pas compatible avec le délai de publication renseigné",
						e.getMessage());
			}

			when(dossCas2.getDelaiPublication()).thenReturn(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
			try {
				when(parapheurService.getPieceParapheur(session, dossCas2, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("L'étape suivante n'est pas compatible avec la date de publication précisée",
						e.getMessage());
			}

			when(dossCas2.getDatePreciseePublication()).thenReturn(oldDate);
			try {
				when(parapheurService.getPieceParapheur(session, dossCas2, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("L'étape suivante n'est pas compatible avec la date de publication précisée",
						e.getMessage());
			}

			when(dossCas2.getDatePreciseePublication()).thenReturn(newDate);
			try {
				when(parapheurService.getPieceParapheur(session, dossCas2, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals(
						"L'étape suivante n'est pas compatible avec la publication intégrale ou par extrait renseignée",
						e.getMessage());
			}

			when(dossCas2.getPublicationIntegraleOuExtrait()).thenReturn("Intégrale");
			when(dossCas2.getVecteurPublication()).thenReturn(Lists.newArrayList("BODMR"));
			try {
				when(parapheurService.getPieceParapheur(session, dossCas2, false)).thenReturn(
						Lists.newArrayList(mock(DocumentModel.class)));
				service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals(
						"L'étape suivante n'est pas compatible avec les données de vecteurs de publication qui ont été renseignées",
						e.getMessage());
			}
		}
	}

	@Test
	public void testCasCheckDossierFinBeforeValidateKO() {
		initCheckDossierBeforeValidate();

		// Cas 1 : dossier initialisé pas d'étape suivante dans la FdR
		Dossier dossCas1 = createDossier();
		if (dossCas1 != null) {
			try {
				service.checkDossierBeforeValidateStep(session, dossCas1, new ArrayList<DocumentModel>());
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("Il faut obligatoirement ajouter un extrait dans le parapheur", e.getMessage());
			}

			when(dossCas1.getIsParapheurComplet()).thenReturn(true);
			when(bordereauService.isBordereauComplet(dossCas1)).thenReturn(false);
			when(bordereauService.getBordereauMetadonnesObligatoiresManquantes(dossCas1.getDocument())).thenReturn(
					"tout");
			try {
				service.checkDossierBeforeValidateStep(session, dossCas1, new ArrayList<DocumentModel>());
				fail("La validation aurait dû échouer");
			} catch (Exception e) {
				assertEquals("Il manque des méta-données obligatoires dans le bordereau (tout)", e.getMessage());
			}
		}
	}

	private Dossier createDossier() {
		DocumentModel model = mock(DocumentModel.class);
		when(model.getAdapter(HasParticipants.class)).thenReturn(mock(HasParticipants.class));
		DossierImpl dossier = new DossierImpl(model);

		return dossier;
	}

	private List<DocumentModel> createListOfSteps(String[] stepsName) {
		List<DocumentModel> lstEtapes = new ArrayList<DocumentModel>();

		for (String etape : stepsName) {
			DocumentModel docEtape = mock(DocumentModel.class);
			SolonEpgRouteStep beanEtape = mock(SolonEpgRouteStep.class);
			when(beanEtape.getType()).thenReturn(etape);
			when(docEtape.getAdapter(SolonEpgRouteStep.class)).thenReturn(beanEtape);

			lstEtapes.add(docEtape);

		}

		return lstEtapes;
	}

}
