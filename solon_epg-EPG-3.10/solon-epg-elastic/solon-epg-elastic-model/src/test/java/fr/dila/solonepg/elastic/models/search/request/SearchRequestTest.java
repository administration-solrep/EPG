package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Lists;

import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.json.IndexationObjectMapperProviderUtil;
import fr.dila.solonepg.elastic.models.search.FacetEnum;
import fr.dila.solonepg.elastic.models.tests.ModelsUtils;
import junit.framework.Assert;

public class SearchRequestTest {
	private static final List<String> DOSSIER_FULL_TEXT_FIELDS = Arrays
			.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE + "^2.0",
					ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA + "^1.0",
					ElasticDossier.DOS_NUMERO_NOR + "^10.0", ElasticDossier.DOS_TITRE_ACTE + "^5.0" });

	private static final List<String> DOC_FILE_DATA_FIELDS = Arrays
			.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA + "^1.0" });

	private static final List<String> DOC_HIGHLIGHTING_FIELDS = Arrays
			.asList(new String[] { ElasticDocument.FILE_DATA + "^1.0" });

	private static final List<String> DOC_DC_TITLE_FIELDS = Arrays
			.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE + "^2.0" });

	private ObjectWriter initWriter() {
		ObjectMapper mapper = IndexationObjectMapperProviderUtil.newElasticsearchQueryResponseInstance();
		return mapper.writerWithDefaultPrettyPrinter();
	}

	private SearchRequest initRequestC84() {
		SearchRequest sr = new SearchRequest(0, 25, Arrays.asList(
				new String[] { ElasticDossier.DOS_TITRE_ACTE }),
				Arrays.asList(new String[] { ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT,
						ElasticDossier.DOS_NUMERO_NOR, ElasticDossier.RETDILA_DATE_PARUTION_JORF }));

		BoolQuery bq = new BoolQuery(1.0, false, true);

		List<QueryClause> mustSubClauses = new ArrayList<QueryClause>();

		BoolQuery qc1 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc1);

		BoolQuery qc2 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc2);
		List<QueryClause> shouldSubClauses = new ArrayList<QueryClause>();
		TermClause tc = new TermClause(1.0, ElasticDossier.DOS_MINISTERE_RESP,
				"Ministère de l’agriculture et de l’alimentation");
		shouldSubClauses.add(tc);
		qc2.setShouldSubClauses(shouldSubClauses);

		BoolQuery qc3 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc3);
		shouldSubClauses = new ArrayList<QueryClause>();
		tc = new TermClause(1.0, ElasticDossier.DOS_STATUT, "Initié");
		shouldSubClauses.add(tc);
		qc3.setShouldSubClauses(shouldSubClauses);

		BoolQuery qc4 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc4);
		shouldSubClauses = new ArrayList<QueryClause>();
		tc = new TermClause(1.0, "dos:directionResp",
				"DGPE - Direction générale de la performance économique et environnementale des entreprises");
		shouldSubClauses.add(tc);
		qc4.setShouldSubClauses(shouldSubClauses);

		bq.setMustSubClauses(mustSubClauses);
		sr.setQuery(bq);

		// highlight
		Highlight highlight = new Highlight(false);
		sr.setHighlight(highlight);

		List<HighlightField> fields = new ArrayList<HighlightField>();
		HighlightField field = new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1);
		fields.add(field);
		highlight.setFields(fields);

		sr.setSource(false);

		return sr;
	}

	private SearchRequest initRequestC85() {
		SearchRequest sr = new SearchRequest(0, 25,
				Arrays.asList(new String[] { ElasticDossier.DOS_TITRE_ACTE }),
				Arrays.asList(new String[] { ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT,
						ElasticDossier.DOS_NUMERO_NOR, "dc:created" }));

		BoolQuery bq = new BoolQuery(1.0, false, true);

		List<QueryClause> mustSubClauses = new ArrayList<QueryClause>();
		bq.setMustSubClauses(mustSubClauses);

		BoolQuery qc1 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc1);

		BoolQuery qc2 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc2);
		List<QueryClause> shouldSubClauses = new ArrayList<QueryClause>();
		TermClause tc = new TermClause(1.0, ElasticDossier.DOS_MINISTERE_RESP,
				"Ministère de l’agriculture et de l’alimentation");
		shouldSubClauses.add(tc);
		qc2.setShouldSubClauses(shouldSubClauses);

		BoolQuery qc3 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc3);
		shouldSubClauses = new ArrayList<QueryClause>();
		tc = new TermClause(1.0, ElasticDossier.DOS_STATUT, "Lancé");
		shouldSubClauses.add(tc);
		qc3.setShouldSubClauses(shouldSubClauses);

		BoolQuery qc4 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc4);
		mustSubClauses = new ArrayList<QueryClause>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 2);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		RangeClause rc = new RangeClause(1.0, "dc:created", cal.getTime(), null, true, true);
		mustSubClauses.add(rc);
		qc4.setMustSubClauses(mustSubClauses);

		BoolQuery qc5 = new BoolQuery(1.0, false, true);
		mustSubClauses.add(qc5);
		shouldSubClauses = new ArrayList<QueryClause>();
		tc = new TermClause(1.0, ElasticDossier.DOS_ARCHIVE, "true");
		shouldSubClauses.add(tc);
		qc5.setShouldSubClauses(shouldSubClauses);

		sr.setQuery(bq);

		// highlight
		Highlight highlight = new Highlight(false);
		sr.setHighlight(highlight);

		List<HighlightField> fields = new ArrayList<HighlightField>();
		HighlightField field = new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1);
		fields.add(field);
		highlight.setFields(fields);

		sr.setSource(false);

		return sr;
	}

	private SearchRequest initRequestC86() {
		SearchRequest sr = new SearchRequest(0, 25,
				Arrays.asList(
						new String[] { ElasticDossier.DOS_TITRE_ACTE }),
				Arrays
						.asList(new String[] { ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT,
								ElasticDossier.DOS_NUMERO_NOR, ElasticDossier.RETDILA_DATE_PARUTION_JORF }));

		List<SortElement> sortElements = new ArrayList<SortElement>();
		sortElements.add(new SortElement(ElasticDossier.RETDILA_DATE_PARUTION_JORF, true));
		sr.setSort(sortElements);

		BoolQuery bq = new BoolQuery(1.0, false, true);

		List<QueryClause> must0 = new ArrayList<QueryClause>();
		bq.setMustSubClauses(must0);

		BoolQuery qc1 = new BoolQuery(1.0, false, true);
		must0.add(qc1);

		BoolQuery qc2 = new BoolQuery(1.0, false, true);
		must0.add(qc2);
		List<QueryClause> shouldSubClauses = new ArrayList<QueryClause>();
		TermClause tc = new TermClause(1.0, ElasticDossier.DOS_MINISTERE_RESP, "Ministère de l’intérieur");
		shouldSubClauses.add(tc);
		qc2.setShouldSubClauses(shouldSubClauses);

		BoolQuery qc3 = new BoolQuery(1.0, false, true);
		must0.add(qc3);
		 shouldSubClauses = new ArrayList<QueryClause>();
		tc = new TermClause(1.0, ElasticDossier.DOS_STATUT, "Publié");
		 shouldSubClauses.add(tc);
		 qc3.setShouldSubClauses(shouldSubClauses);

		BoolQuery qc4 = new BoolQuery(1.0, false, true);
		must0.add(qc4);
		ArrayList<QueryClause> must1 = new ArrayList<QueryClause>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 2);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		RangeClause rc = new RangeClause(1.0, ElasticDossier.RETDILA_DATE_PARUTION_JORF, cal.getTime(), null, true, true);
		must1.add(rc);
		qc4.setMustSubClauses(must1);

		BoolQuery bool3 = new BoolQuery(1.0, false, true);
		must0.add(bool3);
		shouldSubClauses = new ArrayList<QueryClause>();

		SimpleQueryString sqs = new SimpleQueryString(1.0, "asile", DOSSIER_FULL_TEXT_FIELDS, -1,
				"or", false, false);
		shouldSubClauses.add(sqs);
		bool3.setShouldSubClauses(shouldSubClauses);

		// nested
		NestedClause nested = new NestedClause(1.0, "documents", false, "sum");
		nested.setInnerHits(new InnerHits(false, 0, false, false, false, null, Collections.singletonList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID)));

		// nested query
		BoolQuery bool4 = new BoolQuery(1.0, false, true);

		List<QueryClause> should4 = new ArrayList<QueryClause>();
		should4.add(new SimpleQueryString(1.0, "asile", DOC_FILE_DATA_FIELDS, -1,
				"or", false, false));
		should4.add(
				new SimpleQueryString(1.0, "asile", DOC_DC_TITLE_FIELDS, -1,
				"or", false, false));
		bool4.setShouldSubClauses(should4);

		nested.setQuery(bool4);

		shouldSubClauses.add(nested);

		sr.setQuery(bq);

		// highlight
		Highlight highlight = new Highlight(false);
		sr.setHighlight(highlight);

		List<HighlightField> fields = new ArrayList<HighlightField>();
		HighlightField field = new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1);
		fields.add(field);
		highlight.setFields(fields);

		sr.setSource(false);

		return sr;
	}

	private SearchRequest initRequestC89() {
		SearchRequest sr = new SearchRequest(0, 25, Arrays.asList(
				new String[] { ElasticDossier.DOS_TITRE_ACTE }),
				Arrays.asList(new String[] { ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT,
						ElasticDossier.DOS_NUMERO_NOR, "dos:typeActe", ElasticDossier.RETDILA_DATE_PARUTION_JORF }));

		// Main query
		BoolQuery bool0 = new BoolQuery(1.0, false, true);

		ArrayList<QueryClause> must0 = new ArrayList<QueryClause>();
		must0.add(new BoolQuery(1.0, false, true));

		BoolQuery bool1 = new BoolQuery(1.0, false, true);
		ArrayList<QueryClause> should1 = new ArrayList<QueryClause>();
		TermClause term0 = new TermClause(1.0, "dos:typeActe", "Arrêté ministériel");
		should1.add(term0);
		bool1.setShouldSubClauses(should1);
		must0.add(bool1);

		BoolQuery bool2 = new BoolQuery(1.0, false, true);
		ArrayList<QueryClause> must1 = new ArrayList<QueryClause>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2016);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 2);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		must1.add(new RangeClause(1.0, ElasticDossier.RETDILA_DATE_PARUTION_JORF, cal.getTime(), null, true, true));
		cal.set(Calendar.MONTH, 12);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		must1.add(new RangeClause(1.0, ElasticDossier.RETDILA_DATE_PARUTION_JORF, null, cal.getTime(), true, true));

		bool2.setMustSubClauses(must1);

		must0.add(bool2);
		bool0.setMustSubClauses(must0);

		sr.setQuery(bool0);

		Highlight highlight = new Highlight(false);
		List<HighlightField> fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1));
		highlight.setFields(fields);
		sr.setHighlight(highlight);
		sr.setSource(false);

		return sr;
	}

	private SearchRequest initRequestC901() {
		SearchRequest sr = new SearchRequest(0, 25, Arrays.asList(
				new String[] { ElasticDossier.DOS_TITRE_ACTE }),
				Arrays.asList(new String[] { ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT,
						ElasticDossier.DOS_NUMERO_NOR, ElasticDossier.RETDILA_DATE_PARUTION_JORF }));

		BoolQuery bool0 = new BoolQuery(1.0, false, true);

		ArrayList<QueryClause> must0 = new ArrayList<QueryClause>();
		BoolQuery bool1 = new BoolQuery(1.0, false, true);

		ArrayList<QueryClause> should0 = new ArrayList<QueryClause>();
		should0.add(new SimpleQueryString(1.0, "asile", DOSSIER_FULL_TEXT_FIELDS, -1, "or", false,
				false));

		NestedClause nested0 = new NestedClause(1.0, "documents", false, "sum");

		BoolQuery bool2 = new BoolQuery(1.0, false, true);
		ArrayList<QueryClause> should1 = new ArrayList<QueryClause>();
		should1.add(new SimpleQueryString(1.0, "asile", DOC_FILE_DATA_FIELDS, -1,
				"or", false, false));
		should1.add(
				new SimpleQueryString(1.0, "asile", DOC_DC_TITLE_FIELDS, -1,
				"or", false, false));

		bool2.setShouldSubClauses(should1);

		nested0.setQuery(bool2);
		nested0.setInnerHits(new InnerHits(false, 0, false, false, false, null, Collections.singletonList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID)));

		should0.add(nested0);

		bool1.setShouldSubClauses(should0);

		must0.add(bool1);
		bool0.setMustSubClauses(must0);

		sr.setQuery(bool0);

		Highlight highlight = new Highlight(false);
		List<HighlightField> fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1));
		highlight.setFields(fields);

		sr.setHighlight(highlight);
		sr.setSource(false);

		return sr;
	}

	private SearchRequest initRequestC902() {
		SearchRequest sr = new SearchRequest(null, 1, Arrays.asList(new String[] { ElasticDocument.DC_TITLE }), null);

		BoolQuery bool0 = new BoolQuery(1.0, false, true);
		ArrayList<QueryClause> must0 = new ArrayList<QueryClause>();
		must0.add(new TermClause(1.0, ElasticDossier.UID, "40ef0ec4-b202-40b5-9e1c-c7fbbcaeac6b"));
		bool0.setMustSubClauses(must0);

		ArrayList<QueryClause> should0 = new ArrayList<QueryClause>();
		should0.add(new SimpleQueryString(1.0, "asile",
				DOC_HIGHLIGHTING_FIELDS, -1, "or",
				false, false));
		bool0.setShouldSubClauses(should0);

		sr.setQuery(bool0);

		Highlight highlight = new Highlight(false);
		List<HighlightField> fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDocument.DC_TITLE, 500, 1));
		fields.add(new HighlightField(ElasticDocument.FILE_DATA, 150, 1));
		highlight.setFields(fields);

		sr.setHighlight(highlight);
		sr.setSource(false);
		return sr;
	}

	private SearchRequest initRequestFulltext() {
		SearchRequest sr = new SearchRequest(0, 25, Arrays.asList(new String[] { ElasticDossier.DOS_TITRE_ACTE  }),
				Arrays.asList(new String[] { ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT,
						ElasticDossier.DOS_NUMERO_NOR, ElasticDossier.RETDILA_DATE_PARUTION_JORF }));

		BoolQuery query0 = new BoolQuery(1.0, false, true);

		ArrayList<QueryClause> must0 = new ArrayList<QueryClause>();
		BoolQuery query1 = new BoolQuery(1.0, false, true);
		ArrayList<QueryClause> should0 = new ArrayList<QueryClause>();
		should0.add(new SimpleQueryString(
				1.0, "qualité des prestations délivrées",
				DOSSIER_FULL_TEXT_FIELDS,
				-1, "or", false, false));

		NestedClause nested0 = new NestedClause(1.0, "documents", false, "sum");
		BoolQuery query2 = new BoolQuery(1.0, false, true);
		ArrayList<QueryClause> should1 = new ArrayList<QueryClause>();
		should1.add(new SimpleQueryString(1.0, "qualité des prestations délivrées",
				DOC_FILE_DATA_FIELDS, -1,
				"or", false, false));
		should1.add(new SimpleQueryString(1.0, "qualité des prestations délivrées",
				DOC_DC_TITLE_FIELDS, -1,
				"or", false, false));
		query2.setShouldSubClauses(should1);
		nested0.setQuery(query2);
		
		InnerHits innerHits = new InnerHits(false, 0, false, false, false, Collections.singletonList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE),
				Lists.newArrayList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID));
		Highlight highlight0 = new Highlight(false);
		List<HighlightField> fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA, 150, 1));
		fields.add(new HighlightField(ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE, 500, 1));
		highlight0.setFields(fields);
		innerHits.setHighlight(highlight0);
		nested0.setInnerHits(innerHits);
		
		should0.add(nested0);

		query1.setShouldSubClauses(should0);
		must0.add(query1);
		query0.setMustSubClauses(must0);

		sr.setQuery(query0);
		
		Map<String, Aggregation> aggregations = new HashMap<String, Aggregation>();

		Aggregation agg0 = new Aggregation(ElasticDossier.DOS_MINISTERE_RESP, 100, 1, 0, false);
		List<Map<String, String>> orders = new ArrayList<Map<String, String>>();
		HashMap<String, String> order0 = new HashMap<String, String>();
		order0.put("_count", "desc");
		HashMap<String, String> order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order0);
		orders.add(order1);
		agg0.setOrders(orders);
		aggregations.put(FacetEnum.RESPONSABLE.getFacetName(), agg0);

		Aggregation agg1 = new Aggregation(ElasticDossier.DOS_VECTEUR_PUBLICATION, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg1.setOrders(orders);
		aggregations.put(FacetEnum.VECTEUR_PUBLICATION.getFacetName(), agg1);

		Aggregation agg2 = new Aggregation(ElasticDossier.DOS_STATUT, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg2.setOrders(orders);
		aggregations.put(FacetEnum.STATUT.getFacetName(), agg2);

		Aggregation agg3 = new Aggregation(ElasticDossier.DOS_CATEGORIE_ACTE, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg3.setOrders(orders);
		aggregations.put(FacetEnum.CATEGORIE_ACTE.getFacetName(), agg3);

		sr.setAggregations(aggregations);
		
		Highlight highlight = new Highlight(false);
		fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1));
		highlight.setFields(fields);

		sr.setHighlight(highlight);
		
		return sr;
	}

	private void testSerialisationRequest(SearchRequest sr, String jsonPath) throws JsonProcessingException {
		String actual = initWriter().writeValueAsString(sr);

		String fluxJson = ModelsUtils.getFileContent(jsonPath, getClass().getClassLoader());

		Assert.assertEquals(fluxJson, actual);
	}

	@Test
	public void testSerialisationRequestC84() throws JsonProcessingException {
		testSerialisationRequest(initRequestC84(), "json/request/req_c84.json");
	}

	@Test
	public void testSerialisationRequestC85() throws JsonParseException, JsonMappingException, IOException {
		testSerialisationRequest(initRequestC85(), "json/request/req_c85.json");
	}

	@Test
	public void testSerialisationRequestC86() throws JsonParseException, JsonMappingException, IOException {
		testSerialisationRequest(initRequestC86(), "json/request/req_c86.json");
	}

	@Test
	public void testSerialisationRequestC89() throws JsonParseException, JsonMappingException, IOException {
		testSerialisationRequest(initRequestC89(), "json/request/req_c89.json");
	}

	@Test
	public void testSerialisationRequestC901() throws JsonParseException, JsonMappingException, IOException {
		testSerialisationRequest(initRequestC901(), "json/request/req_c90_1.json");
	}

	@Test
	public void testSerialisationRequestC902() throws JsonParseException, JsonMappingException, IOException {
		testSerialisationRequest(initRequestC902(), "json/request/req_c90_2.json");
	}

	@Test
	public void testSerialisationRequestFulltext() throws JsonParseException, JsonMappingException, IOException {
		testSerialisationRequest(initRequestFulltext(), "json/request/fulltext.json");
	}
}
