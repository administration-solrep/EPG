package fr.dila.solonepg.elastic.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.FacetEnum;
import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.request.Aggregation;
import fr.dila.solonepg.elastic.models.search.request.BoolQuery;
import fr.dila.solonepg.elastic.models.search.request.Highlight;
import fr.dila.solonepg.elastic.models.search.request.HighlightField;
import fr.dila.solonepg.elastic.models.search.request.InnerHits;
import fr.dila.solonepg.elastic.models.search.request.NestedClause;
import fr.dila.solonepg.elastic.models.search.request.QueryClause;
import fr.dila.solonepg.elastic.models.search.request.RangeClause;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;
import fr.dila.solonepg.elastic.models.search.request.SimpleQueryString;
import fr.dila.solonepg.elastic.models.search.request.TermClause;
import fr.dila.st.core.util.DateUtil;
import junit.framework.Assert;

public class SearchRequestMapperTest {
	private List<String> getDossierFullTextFieldsAndPonderation() {
		return Arrays.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE + "^2.0",
				ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA + "^1.0",
				ElasticDossier.DOS_NUMERO_NOR + "^10.0", ElasticDossier.DOS_TITRE_ACTE + "^5.0" });
	}

	private List<String> getDocumentFieldsAndPonderation() {
		return Arrays.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE + "^2.0",
				ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA + "^1.0" });
	}

	@Test
	public void testVecteursPublication() {
		SearchCriteria criteria = new SearchCriteria();

		String[] vecteurs = new String[] { "BO (DEV) - Ecologie", "Journal Officiel" };

		criteria.setVecteurPublication(Arrays.asList(vecteurs));

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getMustSubClauses();

		Assert.assertEquals(2, clauses.size());

		checkTermClause(ElasticDossier.DOS_VECTEUR_PUBLICATION, vecteurs[0], (TermClause) clauses.get(0));
		checkTermClause(ElasticDossier.DOS_VECTEUR_PUBLICATION, vecteurs[1], (TermClause) clauses.get(1));
	}

	@Test
	public void testCategorieActe() {
		SearchCriteria criteria = new SearchCriteria();

		String[] categories = new String[] { "Non réglementaire" };

		criteria.setCategorieActe(Arrays.asList(categories));

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getMustSubClauses();

		Assert.assertEquals(1, clauses.size());

		checkTermClause(ElasticDossier.DOS_CATEGORIE_ACTE, categories[0], (TermClause) clauses.get(0));
	}

	@Test
	public void testStatut() {
		SearchCriteria criteria = new SearchCriteria();

		String[] status = new String[] { "Publié" };

		criteria.setStatut(Arrays.asList(status));

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getMustSubClauses();

		Assert.assertEquals(1, clauses.size());

		checkTermClause(ElasticDossier.DOS_STATUT, status[0], (TermClause) clauses.get(0));
	}

	@Test
	public void testMinistereResp() {
		SearchCriteria criteria = new SearchCriteria();

		String[] ministeres = new String[] { "Ministère de la justice" };

		criteria.setResponsable(Arrays.asList(ministeres));

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getMustSubClauses();

		Assert.assertEquals(1, clauses.size());

		checkTermClause(ElasticDossier.DOS_MINISTERE_RESP, ministeres[0], (TermClause) clauses.get(0));
	}

	@Test
	public void testStatutArchivage() {
		SearchCriteria criteria = new SearchCriteria();

		List<String> statutArchivage = new ArrayList<String>();
		statutArchivage.add(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE);
		statutArchivage.add(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_GENERE);

		criteria.setStatutArchivage(statutArchivage);

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getShouldSubClauses();

		Assert.assertEquals(5, clauses.size());

		checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, statutArchivage.get(0), (TermClause) clauses.get(0));
		checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, VocabularyConstants.STATUT_ARCHIVAGE_AUCUN, (TermClause) clauses.get(1));
		checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE, (TermClause) clauses.get(2));
		checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE, (TermClause) clauses.get(3));
		checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE, (TermClause) clauses.get(4));
	}

	@Test
	public void testDatesPublication() {
		SearchCriteria criteria = new SearchCriteria();

		String dateMin = "01/01/2017";
		String dateMax = "31/01/2017";

		criteria.setDatePublicationMin(dateMin);
		criteria.setDatePublicationMax(dateMax);

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getMustSubClauses();

		Assert.assertEquals(2, clauses.size());

		RangeClause rangeClause = (RangeClause) clauses.get(0);
		Assert.assertEquals(ElasticDossier.RETDILA_DATE_PARUTION_JORF, rangeClause.getType());
		Assert.assertEquals(1.0, rangeClause.getBoost());
		Assert.assertEquals(dateMin, DateUtil.formatDDMMYYYYSlash(rangeClause.getFrom()));
		Assert.assertNull(rangeClause.getTo());

		rangeClause = (RangeClause) clauses.get(1);
		Assert.assertEquals(ElasticDossier.RETDILA_DATE_PARUTION_JORF, rangeClause.getType());
		Assert.assertEquals(1.0, rangeClause.getBoost());
		Assert.assertEquals(dateMax, DateUtil.formatDDMMYYYYSlash(rangeClause.getTo()));
		Assert.assertNull(rangeClause.getFrom());
	}

	@Test
	public void testDatesCreation() {
		SearchCriteria criteria = new SearchCriteria();

		String dateMin = "01/01/2017";
		String dateMax = "31/01/2017";

		criteria.setDateCreationMin(dateMin);
		criteria.setDateCreationMax(dateMax);

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		BoolQuery query = (BoolQuery) request.getQuery().getMustSubClauses().get(0);

		List<QueryClause> clauses = query.getMustSubClauses();

		Assert.assertEquals(2, clauses.size());

		checkRangeClause(ElasticDossier.DOS_CREATED, dateMin, null, (RangeClause) clauses.get(0));
		checkRangeClause(ElasticDossier.DOS_CREATED, null, dateMax, (RangeClause) clauses.get(1));
	}

	@Test
	public void testFulltext() {
		SearchCriteria criteria = new SearchCriteria();

		String fulltext = "qualité des prestations délivrées";

		criteria.setFulltext(fulltext);

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		List<QueryClause> clauses = request.getQuery().getMustSubClauses();
		// 0 : dos:archive
		BoolQuery query = (BoolQuery) clauses.get(0);
		Assert.assertEquals(ElasticDossier.DOS_ARCHIVE, ((TermClause) query.getMustSubClauses().get(0)).getType());
		
		// 1 : recherche text
		query = (BoolQuery) clauses.get(1);
		List<QueryClause> subClauses = query.getShouldSubClauses();

		Assert.assertEquals(2, subClauses.size());

		checkFulltextClause(fulltext, (SimpleQueryString) subClauses.get(0), (NestedClause) subClauses.get(1));
		
		// 2 : perms
		query = (BoolQuery) clauses.get(2);
		Assert.assertEquals(ElasticDossier.PERMS, ((TermClause) query.getShouldSubClauses().get(0)).getType());
		
		// 3 : dos:mesureNominative
		query = (BoolQuery) clauses.get(3);
		Assert.assertEquals(ElasticDossier.DOS_MESURE_NOMINATIVE, ((TermClause) query.getMustSubClauses().get(0)).getType());
		
	}

	private void checkFulltextClause(String expectedText, SimpleQueryString sqs, NestedClause nested) {
		Assert.assertEquals(expectedText, sqs.getQuery());
		Assert.assertEquals(getDossierFullTextFieldsAndPonderation(), sqs.getFields());
		Assert.assertEquals(-1, sqs.getFlags());
		Assert.assertEquals("or", sqs.getDefaultOperator());
		Assert.assertFalse(sqs.isLenient());
		Assert.assertFalse(sqs.isAnalyzeWildcard());
		Assert.assertEquals(1.0, sqs.getBoost());

		Assert.assertEquals(ElasticDossier.DOCUMENTS, nested.getPath());
		Assert.assertFalse(nested.isIgnoreUnmapped());
		Assert.assertEquals("sum", nested.getScoreMode());
		Assert.assertEquals(1.0, nested.getBoost());

		InnerHits innerHits = nested.getInnerHits();
		Assert.assertFalse(innerHits.isIgnoreUnmapped());
		Assert.assertEquals(0, innerHits.getFrom());
		Assert.assertFalse(innerHits.isVersion());
		Assert.assertFalse(innerHits.isExplain());
		Assert.assertFalse(innerHits.isTrackScores());
		Assert.assertEquals(Collections.singletonList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID),
				innerHits.getDocvalueFields());

		BoolQuery nQuery = nested.getQuery();
		Assert.assertEquals(1.0, nQuery.getBoost());
		Assert.assertFalse(nQuery.getDisableCoord());
		Assert.assertTrue(nQuery.getAdjustPureNegative());

		List<QueryClause> nClauses = nQuery.getMustSubClauses();
		// contrainte sur la recherche textuelle et contrainte booléen sur dernière version
		Assert.assertEquals(2, nClauses.size());

		sqs = (SimpleQueryString) nClauses.get(0);
		Assert.assertEquals(expectedText, sqs.getQuery());
		Assert.assertEquals(getDocumentFieldsAndPonderation(), sqs.getFields());
		Assert.assertEquals(-1, sqs.getFlags());
		Assert.assertEquals("or", sqs.getDefaultOperator());
		Assert.assertFalse(sqs.isLenient());
		Assert.assertFalse(sqs.isAnalyzeWildcard());
		Assert.assertEquals(1.0, sqs.getBoost());

		TermClause tc = (TermClause) nClauses.get(1);
		Assert.assertEquals("true", tc.getValue());
		Assert.assertEquals(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_CURRENTVERSION, tc.getType());
		Assert.assertEquals(1.0, tc.getBoost());
	}

	private void checkTermClause(String expectedType, String expectedValue, TermClause termClause) {
		Assert.assertEquals(expectedType, termClause.getType());
		Assert.assertEquals(1.0, termClause.getBoost());
		Assert.assertEquals(expectedValue, termClause.getValue());
	}

	private void checkRangeClause(String expectedType, String expectedMin, String expectedMax,
			RangeClause rangeClause) {
		Assert.assertEquals(expectedType, rangeClause.getType());
		Assert.assertEquals(1.0, rangeClause.getBoost());
		if (expectedMin == null) {
			Assert.assertNull(rangeClause.getFrom());
		} else {
			Assert.assertEquals(expectedMin, DateUtil.formatDDMMYYYYSlash(rangeClause.getFrom()));
		}
		if (expectedMax == null) {
			Assert.assertNull(rangeClause.getTo());
		} else {
			Assert.assertEquals(expectedMax, DateUtil.formatDDMMYYYYSlash(rangeClause.getTo()));
		}
	}

	@Test
	public void testAggregation() {
		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(new SearchCriteria(), null);
		} catch (ClientException e) {
			Assert.fail();
		}

		checkAllAggregations(request);
	}

	private void checkAllAggregations(SearchRequest request) {
		Map<String, Aggregation> map = request.getAggregations();

		Assert.assertEquals(6, map.size());

		checkAggregation(map.get(FacetEnum.CATEGORIE_ACTE.getFacetName()), ElasticDossier.DOS_CATEGORIE_ACTE);
		checkAggregation(map.get(FacetEnum.VECTEUR_PUBLICATION.getFacetName()), ElasticDossier.DOS_VECTEUR_PUBLICATION);
		checkAggregation(map.get(FacetEnum.STATUT.getFacetName()), ElasticDossier.DOS_STATUT);
		checkAggregation(map.get(FacetEnum.STATUT_ARCHIVAGE.getFacetName()), ElasticDossier.DOS_STATUT_ARCHIVAGE_ID);

		Aggregation agg = map.get(FacetEnum.RESPONSABLE.getFacetName());
		Assert.assertEquals(ElasticDossier.DOS_MINISTERE_RESP, agg.getField());
		Assert.assertEquals(100, agg.getSize());
		Assert.assertEquals(1, agg.getMinDocCount());
		Assert.assertEquals(0, agg.getShardMinDocCount());
		Assert.assertFalse(agg.isShowTermDocCountError());
		List<Map<String, String>> orders = agg.getOrders();
		Assert.assertEquals(2, orders.size());

		Set<String> keys = orders.get(0).keySet();
		Assert.assertEquals(1, keys.size());
		Assert.assertEquals("desc", orders.get(0).get("_count"));

		keys = orders.get(1).keySet();
		Assert.assertEquals(1, keys.size());
		Assert.assertEquals("asc", orders.get(1).get("_term"));
	}

	private void checkAggregation(Aggregation agg, String expectedField) {
		Assert.assertEquals(expectedField, agg.getField());
		Assert.assertEquals(100, agg.getSize());
		Assert.assertEquals(1, agg.getMinDocCount());
		Assert.assertEquals(0, agg.getShardMinDocCount());
		Assert.assertFalse(agg.isShowTermDocCountError());
		List<Map<String, String>> orders = agg.getOrders();
		Assert.assertEquals(1, orders.size());
		Set<String> keys = orders.get(0).keySet();
		Assert.assertEquals(1, keys.size());
		Assert.assertEquals("asc", orders.get(0).get("_term"));
	}

	@Test
	public void testHighlight() {
		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(new SearchCriteria(), null);
		} catch (ClientException e) {
			Assert.fail();
		}

		checkHighlights(request);
	}

	private void checkHighlights(SearchRequest request) {
		Highlight highlight = request.getHighlight();
		Assert.assertFalse(highlight.getRequireFieldMatch());
		List<HighlightField> fields = highlight.getFields();

		Assert.assertEquals(1, fields.size());
		HighlightField field = fields.get(0);

		Assert.assertEquals(ElasticDossier.DOS_TITRE_ACTE, field.getType());
		Assert.assertEquals(1500, field.getFragmentSize());
		Assert.assertEquals(1, field.getNumberOfFragments());

	}

	@Test
	public void testReqC85() throws ClientException { // TODO
		SearchCriteria criteria = new SearchCriteria();

		String[] ministeresResp = new String[] { "Ministère de l’agriculture et de l’alimentation" };
		String[] statuts = new String[] { "Lancé" };
		String from = "01/01/2017";

		criteria.setResponsable(Arrays.asList(ministeresResp));
		criteria.setStatut(Arrays.asList(statuts));
		criteria.setDateCreationMin(from);
		
		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(criteria, null);
		} catch (ClientException e) {
			Assert.fail();
		}
		
		Assert.assertEquals(new Integer(0), request.getFrom());
		Assert.assertEquals(25, request.getSize());

		List<QueryClause> mainQuery = request.getQuery().getMustSubClauses();
		// statut, ministere resp, date de création, archive, permissions et mesure nominative -> 6 éléments
		Assert.assertEquals(6, mainQuery.size());

		{
			// vérification contrainte de ministère
			// on cherche un BoolQuery avec 1 TermClause sur le terme DOS_MINISTERE_RESP
			TermClause ministereClause = null;
			BoolQuery ministereQuery = null;
			for (QueryClause clause : mainQuery) {
				if (clause instanceof BoolQuery) {
					BoolQuery bool = (BoolQuery) clause;
					if (bool.getMustSubClauses() != null && bool.getMustSubClauses().size() == 1) {
						if (((BoolQuery) clause).getMustSubClauses().get(0) instanceof TermClause) {
							TermClause candidate = (TermClause) ((BoolQuery) clause).getMustSubClauses().get(0);
							if (ElasticDossier.DOS_MINISTERE_RESP.equals(candidate.getType())) {
								ministereClause = candidate;
								ministereQuery = (BoolQuery) clause;
							}
						}
					}
				}
			}
			Assert.assertNotNull("TermClause ministère n'a pas pu être trouvée", ministereClause);
			Assert.assertEquals("Ministère de l’agriculture et de l’alimentation", ministereClause.getValue());
			Assert.assertEquals(1.0, ministereQuery.getBoost());
			Assert.assertFalse(ministereQuery.getDisableCoord());
			Assert.assertTrue(ministereQuery.getAdjustPureNegative());
		}

		{
			// vérification contrainte de statut
			// on cherche un BoolQuery avec 1 TermClause sur le terme DOS_STATUT
			TermClause statutClause = null;
			BoolQuery statutQuery = null;
			for (QueryClause clause : mainQuery) {
				if (clause instanceof BoolQuery) {
					BoolQuery bool = (BoolQuery) clause;
					if (bool.getMustSubClauses() != null && bool.getMustSubClauses().size() == 1) {
						if (((BoolQuery) clause).getMustSubClauses().get(0) instanceof TermClause) {
							TermClause candidate = (TermClause) ((BoolQuery) clause).getMustSubClauses().get(0);
							if (ElasticDossier.DOS_STATUT.equals(candidate.getType())) {
								statutClause = candidate;
								statutQuery = (BoolQuery) clause;
							}
						}
					}
				}
			}
			Assert.assertNotNull("TermClause status n'a pas pu être trouvée", statutClause);
			Assert.assertEquals("Lancé", statutClause.getValue());
			Assert.assertEquals(1.0, statutQuery.getBoost());
			Assert.assertFalse(statutQuery.getDisableCoord());
			Assert.assertTrue(statutQuery.getAdjustPureNegative());
		}

		{
			// vérification contrainte de dates
			// on cherche un BoolQuery avec 1 RangeClause sur le terme DOS_CREATED
			RangeClause rangeClause = null;
			for (QueryClause clause : mainQuery) {
				if (clause instanceof BoolQuery) {
					BoolQuery bool = (BoolQuery) clause;
					if (bool.getMustSubClauses() != null && bool.getMustSubClauses().size() == 1) {
						if (((BoolQuery) clause).getMustSubClauses().get(0) instanceof RangeClause) {
							RangeClause candidate = (RangeClause) ((BoolQuery) clause).getMustSubClauses().get(0);
							if (ElasticDossier.DOS_CREATED.equals(candidate.getType())) {
								rangeClause = candidate;
							}
						}
					}
				}
			}
			Assert.assertNotNull("RangeClause n'a pas pu être trouvée", rangeClause);
	
			Assert.assertEquals(1.0, rangeClause.getBoost());
			Assert.assertTrue(rangeClause.isIncludeLower());
			Assert.assertTrue(rangeClause.isIncludeUpper());
			Assert.assertNull(rangeClause.getTo());
			Assert.assertEquals(from, DateUtil.formatDDMMYYYYSlash(rangeClause.getFrom()));
		}

		checkAllAggregations(request);
		checkHighlights(request);
	}

	@Test
	public void testDocumentQuery() {
		String documentId = "40ef0ec4-b202-40b5-9e1c-c7fbbcaeac6b";
		String fulltext = "asile";

		ISearchRequestMapper mapper = new SearchRequestMapper();
		SearchRequest request = null;
		try {
			request = mapper.from(documentId, fulltext, null);
		} catch (ClientException e) {
			Assert.fail();
		}

		Assert.assertNotNull(request);

		Assert.assertEquals(1, request.getSize());

		List<String> storedFields = request.getStoredFields();
		Assert.assertEquals(1, storedFields.size());
		Assert.assertEquals(ElasticDocument.DC_TITLE, storedFields.get(0));

		BoolQuery query = (BoolQuery) request.getQuery();

		List<QueryClause> must = query.getMustSubClauses();
		Assert.assertEquals(1, must.size());
		TermClause term = (TermClause) must.get(0);
		Assert.assertEquals(ElasticDocument.UID, term.getType());
		Assert.assertEquals(documentId, term.getValue());
		Assert.assertEquals(1.0, term.getBoost());

		List<QueryClause> should = query.getShouldSubClauses();
		Assert.assertEquals(1, should.size());
		SimpleQueryString sqs = (SimpleQueryString) should.get(0);
		Assert.assertEquals(fulltext, sqs.getQuery());
		List<String> fields = sqs.getFields();
		Assert.assertEquals(2, fields.size());
		Assert.assertTrue(fields.contains(ElasticDocument.FILE_DATA + "^1.0"));
		Assert.assertTrue(fields.contains(ElasticDocument.DC_TITLE + "^1.0"));

		Highlight highlight = request.getHighlight();
		Assert.assertFalse(highlight.getRequireFieldMatch());
		List<HighlightField> highlightFields = highlight.getFields();
		Assert.assertEquals(2, highlightFields.size());

		HighlightField field = highlightFields.get(0);
		Assert.assertEquals(ElasticDocument.DC_TITLE, field.getType());
		Assert.assertEquals(500, field.getFragmentSize());
		Assert.assertEquals(1, field.getNumberOfFragments());

		field = highlightFields.get(1);
		Assert.assertEquals(ElasticDocument.FILE_DATA, field.getType());
		Assert.assertEquals(150, field.getFragmentSize());
		Assert.assertEquals(1, field.getNumberOfFragments());
	}
}
