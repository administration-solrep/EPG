package fr.dila.solonepg.elastic.services;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.FacetResultEntry;
import fr.dila.solonepg.elastic.models.search.FacetEnum;
import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.SearchResult;

@Ignore
public class RequeteurServiceImplTest {
	@Test
	public void testSimpleCriteriaSortByPertinence() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setResponsable(Arrays.asList(new String[] { "Ministère de l’intérieur" }));

		RequeteurService requeteurService = new RequeteurServiceImpl();

		SearchResult searchResults = null;
		try {
			searchResults = requeteurService.getResults(criteria, null);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertNotNull(searchResults);

		Assert.assertEquals(new Long(22625), searchResults.getCount());
		Assert.assertEquals(25, searchResults.getResults().size());

		for (ElasticDossier dossier : searchResults.getResults()) {
			Assert.assertEquals("Ministère de l’intérieur", dossier.getDosMinistereResp());
		}

		// Check sort
		ElasticDossier dossier = searchResults.getResults().get(0);
		Assert.assertEquals("INTC1523735A", dossier.getDosNumeroNor());
		dossier = searchResults.getResults().get(1);
		Assert.assertEquals("INTA1408922V", dossier.getDosNumeroNor());
		dossier = searchResults.getResults().get(2);
		Assert.assertEquals("INTA1532914D", dossier.getDosNumeroNor());

		// Vérification de quelques facettes
		List<FacetResultEntry> entries = searchResults.getFacetEntries(FacetEnum.VECTEUR_PUBLICATION);
		Assert.assertEquals(9, entries.size());
		FacetResultEntry entry = entries.get(0);
		Assert.assertEquals(" médailles et récompenses", entry.getKey());
		Assert.assertEquals(new Long(17), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("BODMR", entry.getKey());
		Assert.assertEquals(new Long(306), entry.getDoc_count());
		entry = entries.get(2);
		Assert.assertEquals("BOMI (Ministère de l’intérieur)", entry.getKey());
		Assert.assertEquals(new Long(6264), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.CATEGORIE_ACTE);
		Assert.assertEquals(2, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Non réglementaire", entry.getKey());
		Assert.assertEquals(new Long(5273), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Réglementaire", entry.getKey());
		Assert.assertEquals(new Long(16972), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.STATUT);
		Assert.assertEquals(6, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Abandonné", entry.getKey());
		Assert.assertEquals(new Long(683), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Initié", entry.getKey());
		Assert.assertEquals(new Long(1145), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.RESPONSABLE);
		Assert.assertEquals(1, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Ministère de l’intérieur", entry.getKey());
		Assert.assertEquals(new Long(22625), entry.getDoc_count());
	}

	@Test
	public void testTypeActe() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setTypeActe(Arrays.asList(new String[] { "Amnistie" }));

		RequeteurService requeteurService = new RequeteurServiceImpl();

		SearchResult searchResults = null;
		try {
			searchResults = requeteurService.getResults(criteria, null);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertNotNull(searchResults);

		Assert.assertEquals(new Long(5), searchResults.getCount());
		Assert.assertEquals(5, searchResults.getResults().size());
	}

	@Test
	public void testSimpleCriteriaSortByPublicationDesc() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setVecteurPublication(Arrays.asList(new String[] { "BO (DEV) - Ecologie" }));
		criteria.setSortType(SearchCriteria.SortType.PUBLICATION_DESC);

		RequeteurService requeteurService = new RequeteurServiceImpl();

		SearchResult searchResults = null;
		try {
			searchResults = requeteurService.getResults(criteria, null);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertNotNull(searchResults);

		Assert.assertEquals(new Long(3151), searchResults.getCount());
		Assert.assertEquals(25, searchResults.getResults().size());

		// Check sort
		ElasticDossier dossier = searchResults.getResults().get(0);
		Assert.assertEquals("DEVP1430916A", dossier.getDosNumeroNor());
		dossier = searchResults.getResults().get(1);
		Assert.assertEquals("DEVP1510052A", dossier.getDosNumeroNor());
		dossier = searchResults.getResults().get(2);
		Assert.assertEquals("DEVP1510050A", dossier.getDosNumeroNor());

		// Vérification de quelques facettes
		List<FacetResultEntry> entries = searchResults.getFacetEntries(FacetEnum.VECTEUR_PUBLICATION);
		Assert.assertEquals(5, entries.size());
		FacetResultEntry entry = entries.get(0);
		Assert.assertEquals("BO (DEV) - Ecologie", entry.getKey());
		Assert.assertEquals(new Long(3151), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Documents administratifs", entry.getKey());
		Assert.assertEquals(new Long(3), entry.getDoc_count());
		entry = entries.get(2);
		Assert.assertEquals("Journal Officiel", entry.getKey());
		Assert.assertEquals(new Long(43), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.CATEGORIE_ACTE);
		Assert.assertEquals(2, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Non réglementaire", entry.getKey());
		Assert.assertEquals(new Long(514), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Réglementaire", entry.getKey());
		Assert.assertEquals(new Long(2624), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.STATUT);
		Assert.assertEquals(4, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Abandonné", entry.getKey());
		Assert.assertEquals(new Long(2713), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Initié", entry.getKey());
		Assert.assertEquals(new Long(32), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.RESPONSABLE);
		Assert.assertEquals(6, entries.size());
		entry = entries.get(0);
		Assert.assertEquals(
				"Ministère de l’environnement, de l’énergie et de la mer, chargé des relations internationales sur le climat",
				entry.getKey());
		Assert.assertEquals(new Long(2581), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Ministère de la transition écologique et solidaire", entry.getKey());
		Assert.assertEquals(new Long(550), entry.getDoc_count());

	}

	@Test
	public void testFulltextCriteria() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setFulltext("asile");

		RequeteurService requeteurService = new RequeteurServiceImpl();

		SearchResult searchResults = null;
		try {
			searchResults = requeteurService.getResults(criteria, null);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertNotNull(searchResults);

		Assert.assertEquals(new Long(2436), searchResults.getCount());
		Assert.assertEquals(25, searchResults.getResults().size());

		for (ElasticDossier dossier : searchResults.getResults()) {
			boolean foundInTitle = dossier.getDosTitreActe().contains("<em>");
			boolean foundInNor = dossier.getDosNumeroNor().contains("<em>");

			boolean foundInDocTitle = false;
			boolean foundInDocText = false;
			for (ElasticDocument doc : dossier.getDocuments()) {
				if (doc.getDcTitle().contains("<em>")) {
					foundInDocTitle = true;
				}
				if (doc.getFileData() != null && doc.getFileData().contains("<em>")) {
					foundInDocText = true;
				}
			}
			if (!(foundInTitle || foundInNor || foundInDocTitle || foundInDocText)) {
				Assert.fail();
			}
		}

		// Vérification de quelques facettes
		List<FacetResultEntry> entries = searchResults.getFacetEntries(FacetEnum.VECTEUR_PUBLICATION);
		Assert.assertEquals(12, entries.size());
		FacetResultEntry entry = entries.get(0);
		Assert.assertEquals(" médailles et récompenses", entry.getKey());
		Assert.assertEquals(new Long(1), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("BO (DEV) - Ecologie", entry.getKey());
		Assert.assertEquals(new Long(6), entry.getDoc_count());
		entry = entries.get(5);
		Assert.assertEquals("BOMI (Ministère de l’intérieur)", entry.getKey());
		Assert.assertEquals(new Long(36), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.CATEGORIE_ACTE);
		Assert.assertEquals(2, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Non réglementaire", entry.getKey());
		Assert.assertEquals(new Long(487), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Réglementaire", entry.getKey());
		Assert.assertEquals(new Long(1439), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.STATUT);
		Assert.assertEquals(6, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Abandonné", entry.getKey());
		Assert.assertEquals(new Long(98), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Initié", entry.getKey());
		Assert.assertEquals(new Long(8), entry.getDoc_count());

		entries = searchResults.getFacetEntries(FacetEnum.RESPONSABLE);
		Assert.assertEquals(50, entries.size());
		entry = entries.get(0);
		Assert.assertEquals("Ministère de la justice", entry.getKey());
		Assert.assertEquals(new Long(660), entry.getDoc_count());
		entry = entries.get(1);
		Assert.assertEquals("Ministère de l’intérieur", entry.getKey());
		Assert.assertEquals(new Long(517), entry.getDoc_count());
	}

	@Test
	public void testFulltextExpressionExacte() {
		SearchCriteria criteria = new SearchCriteria();

		String fulltext = "qualité des prestations délivrées";
		criteria.setFulltext(fulltext);
		criteria.setExpressionExacte(true);

		RequeteurService requeteurService = new RequeteurServiceImpl();

		SearchResult searchResults = null;
		try {
			searchResults = requeteurService.getResults(criteria, null);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(new Long(10), searchResults.getCount());
	}

	@Test
	public void testFulltextNonExpressionExacte() {
		SearchCriteria criteria = new SearchCriteria();

		String fulltext = "qualité des prestations délivrées";
		criteria.setFulltext(fulltext);
		criteria.setExpressionExacte(false);

		RequeteurService requeteurService = new RequeteurServiceImpl();

		SearchResult searchResults = null;
		try {
			searchResults = requeteurService.getResults(criteria, null);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(new Long(86106), searchResults.getCount());
	}
}
