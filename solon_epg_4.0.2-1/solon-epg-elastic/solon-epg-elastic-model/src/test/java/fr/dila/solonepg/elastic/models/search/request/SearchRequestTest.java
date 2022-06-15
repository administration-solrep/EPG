package fr.dila.solonepg.elastic.models.search.request;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import fr.dila.solonepg.elastic.models.tests.ModelsUtils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SearchRequestTest {

    @Test
    public void testSerialisationRequestC84() throws JSONException {
        testSerialisationRequest(initRequestC84(), "json/request/req_c84.json");
    }

    @Test
    public void testSerialisationRequestC85() throws JSONException {
        testSerialisationRequest(initRequestC85(), "json/request/req_c85.json");
    }

    @Test
    public void testSerialisationRequestC86() throws JSONException {
        testSerialisationRequest(initRequestC86(), "json/request/req_c86.json");
    }

    @Test
    public void testSerialisationRequestC89() throws JSONException {
        testSerialisationRequest(initRequestC89(), "json/request/req_c89.json");
    }

    @Test
    public void testSerialisationRequestC901() throws JSONException {
        testSerialisationRequest(initRequestC901(), "json/request/req_c90_1.json");
    }

    @Test
    public void testSerialisationRequestC902() throws JSONException {
        testSerialisationRequest(initRequestC902(), "json/request/req_c90_2.json");
    }

    @Test
    public void testSerialisationRequestFulltext() throws JSONException {
        testSerialisationRequest(initRequestFulltext(), "json/request/fulltext.json");
    }

    private static final Map<String, Float> DOSSIER_FULL_TEXT_FIELDS = ImmutableMap.of(
        ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE,
        2.0F,
        ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA,
        1.0F,
        ElasticDossier.DOS_NUMERO_NOR,
        10.0F,
        ElasticDossier.DOS_TITRE_ACTE,
        5.0F
    );

    private static final Map<String, Float> DOC_FILE_DATA_FIELDS = ImmutableMap.of(
        ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA,
        1.0F
    );

    private static final Map<String, Float> DOC_HIGHLIGHTING_FIELDS = ImmutableMap.of(ElasticDocument.FILE_DATA, 1.0F);

    private static final Map<String, Float> DOC_DC_TITLE_FIELDS = ImmutableMap.of(
        ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE,
        2.0F
    );

    private void testSerialisationRequest(SearchRequest sr, String jsonPath) throws JSONException {
        String actual = new JSONObject(sr.source().toString()).toString(4);

        String fluxJson = new JSONObject(ModelsUtils.getFileContent(jsonPath, getClass().getClassLoader())).toString(4);

        Assert.assertEquals(fluxJson, actual);
    }

    private SearchRequest initRequestC84() {
        List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
        List<String> docvalueFields = Arrays.asList(
            ElasticDossier.DOS_MINISTERE_RESP,
            ElasticDossier.DOS_STATUT,
            ElasticDossier.DOS_NUMERO_NOR,
            ElasticDossier.RETDILA_DATE_PARUTION_JORF
        );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(25);
        sourceBuilder.storedFields(storedFields);
        for (String docvalueField : docvalueFields) {
            sourceBuilder.docValueField(docvalueField, "use_field_mapping");
        }
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        BoolQueryBuilder bq = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> mustClauses = bq.must();

        BoolQueryBuilder qc1 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc1);

        BoolQueryBuilder qc2 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc2);
        TermQueryBuilder shouldClauses = QueryBuilders
            .termQuery(ElasticDossier.DOS_MINISTERE_RESP, "Ministère de l’agriculture et de l’alimentation")
            .boost(1);
        qc2.should(shouldClauses);

        BoolQueryBuilder qc3 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc3);
        shouldClauses = QueryBuilders.termQuery(ElasticDossier.DOS_STATUT, "Initié").boost(1);
        qc3.should(shouldClauses);

        BoolQueryBuilder qc4 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc4);
        shouldClauses =
            QueryBuilders
                .termQuery(
                    "dos:directionResp",
                    "DGPE - Direction générale de la performance économique et environnementale des entreprises"
                )
                .boost(1);
        qc4.should(shouldClauses);

        sr.source().query(bq);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDossier.DOS_TITRE_ACTE, 1500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        sr.source().fetchSource(false);

        return sr;
    }

    private SearchRequest initRequestC85() {
        List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
        List<String> docvalueFields = Arrays.asList(
            ElasticDossier.DOS_MINISTERE_RESP,
            ElasticDossier.DOS_STATUT,
            ElasticDossier.DOS_NUMERO_NOR,
            "dc:created"
        );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(25);
        sourceBuilder.storedFields(storedFields);
        for (String docvalueField : docvalueFields) {
            sourceBuilder.docValueField(docvalueField, "use_field_mapping");
        }
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        BoolQueryBuilder bq = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> mustClauses = bq.must();

        BoolQueryBuilder qc1 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc1);

        BoolQueryBuilder qc2 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc2);
        TermQueryBuilder shouldClauses = QueryBuilders
            .termQuery(ElasticDossier.DOS_MINISTERE_RESP, "Ministère de l’agriculture et de l’alimentation")
            .boost(1);
        qc2.should(shouldClauses);

        BoolQueryBuilder qc3 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc3);
        shouldClauses = QueryBuilders.termQuery(ElasticDossier.DOS_STATUT, "Lancé").boost(1);
        qc3.should(shouldClauses);

        BoolQueryBuilder qc4 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        mustClauses.add(qc4);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        RangeQueryBuilder mustSubClauses = QueryBuilders.rangeQuery("dc:created").gte(cal.getTime()).boost(1);
        qc4.must(mustSubClauses);

        BoolQueryBuilder qc5 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        qc4.must(qc5);
        shouldClauses = QueryBuilders.termQuery(ElasticDossier.DOS_ARCHIVE, "true").boost(1);
        qc5.should(shouldClauses);

        sr.source().query(bq);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDossier.DOS_TITRE_ACTE, 1500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        sr.source().fetchSource(false);

        return sr;
    }

    private SearchRequest initRequestC86() {
        List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
        List<String> docvalueFields = Arrays.asList(
            ElasticDossier.DOS_MINISTERE_RESP,
            ElasticDossier.DOS_STATUT,
            ElasticDossier.DOS_NUMERO_NOR,
            ElasticDossier.RETDILA_DATE_PARUTION_JORF
        );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(25);
        sourceBuilder.storedFields(storedFields);
        for (String docvalueField : docvalueFields) {
            sourceBuilder.docValueField(docvalueField, "use_field_mapping");
        }
        sourceBuilder.sort(SortBuilders.fieldSort(ElasticDossier.RETDILA_DATE_PARUTION_JORF).order(SortOrder.DESC));
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        BoolQueryBuilder bq = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> must0 = bq.must();

        BoolQueryBuilder qc1 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(qc1);

        BoolQueryBuilder qc2 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(qc2);
        TermQueryBuilder shouldClauses = QueryBuilders
            .termQuery(ElasticDossier.DOS_MINISTERE_RESP, "Ministère de l’intérieur")
            .boost(1);
        qc2.should(shouldClauses);

        BoolQueryBuilder qc3 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(qc3);
        shouldClauses = QueryBuilders.termQuery(ElasticDossier.DOS_STATUT, "Publié").boost(1);
        qc3.should(shouldClauses);

        BoolQueryBuilder qc4 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(qc4);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        RangeQueryBuilder mustSubClauses = QueryBuilders
            .rangeQuery(ElasticDossier.RETDILA_DATE_PARUTION_JORF)
            .gte(cal.getTime())
            .boost(1);
        qc4.must(mustSubClauses);

        BoolQueryBuilder bool3 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(bool3);
        SimpleQueryStringBuilder sqs = QueryBuilders
            .simpleQueryStringQuery("asile")
            .defaultOperator(Operator.OR)
            .lenient(false)
            .analyzeWildcard(false);
        DOSSIER_FULL_TEXT_FIELDS.forEach(sqs::field);
        bool3.should(sqs);

        sr.source().fetchSource(false);

        // nested query
        BoolQueryBuilder bool4 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        // nested
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder()
            .setIgnoreUnmapped(false)
            .setFrom(0)
            .setVersion(false)
            .setExplain(false)
            .setTrackScores(false)
            .setFetchSourceContext(sr.source().fetchSource())
            .addDocValueField(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID);
        NestedQueryBuilder nested = QueryBuilders
            .nestedQuery("documents", bool4, ScoreMode.Total)
            .boost(1.0F)
            .ignoreUnmapped(false)
            .innerHit(innerHitBuilder);

        sqs =
            QueryBuilders
                .simpleQueryStringQuery("asile")
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
        DOC_FILE_DATA_FIELDS.forEach(sqs::field);
        bool4.should(sqs);
        sqs =
            QueryBuilders
                .simpleQueryStringQuery("asile")
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
        DOC_DC_TITLE_FIELDS.forEach(sqs::field);
        bool4.should(sqs);

        bool3.should(nested);

        sr.source().query(bq);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDossier.DOS_TITRE_ACTE, 1500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        return sr;
    }

    private SearchRequest initRequestC89() {
        List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
        List<String> docvalueFields = Arrays.asList(
            ElasticDossier.DOS_MINISTERE_RESP,
            ElasticDossier.DOS_STATUT,
            ElasticDossier.DOS_NUMERO_NOR,
            "dos:typeActe",
            ElasticDossier.RETDILA_DATE_PARUTION_JORF
        );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(25);
        sourceBuilder.storedFields(storedFields);
        for (String docvalueField : docvalueFields) {
            sourceBuilder.docValueField(docvalueField, "use_field_mapping");
        }
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        // Main Query
        BoolQueryBuilder bool0 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> must0 = bool0.must();
        must0.add(QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true));

        BoolQueryBuilder bool1 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(bool1);
        TermQueryBuilder shouldClauses = QueryBuilders.termQuery("dos:typeActe", "Arrêté ministériel").boost(1);
        bool1.should(shouldClauses);

        BoolQueryBuilder bool2 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);
        must0.add(bool2);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        RangeQueryBuilder mustSubClauses = QueryBuilders
            .rangeQuery(ElasticDossier.RETDILA_DATE_PARUTION_JORF)
            .gte(cal.getTime())
            .boost(1);
        bool2.must(mustSubClauses);
        cal.set(Calendar.MONTH, 12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        mustSubClauses =
            QueryBuilders.rangeQuery(ElasticDossier.RETDILA_DATE_PARUTION_JORF).lte(cal.getTime()).boost(1);
        bool2.must(mustSubClauses);

        sr.source().query(bool0);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDossier.DOS_TITRE_ACTE, 1500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        sr.source().fetchSource(false);

        return sr;
    }

    private SearchRequest initRequestC901() {
        List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
        List<String> docvalueFields = Arrays.asList(
            ElasticDossier.DOS_MINISTERE_RESP,
            ElasticDossier.DOS_STATUT,
            ElasticDossier.DOS_NUMERO_NOR,
            ElasticDossier.RETDILA_DATE_PARUTION_JORF
        );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(25);
        sourceBuilder.storedFields(storedFields);
        for (String docvalueField : docvalueFields) {
            sourceBuilder.docValueField(docvalueField, "use_field_mapping");
        }
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        BoolQueryBuilder bool0 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> must0 = bool0.must();

        BoolQueryBuilder bool1 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> should0 = bool1.should();
        SimpleQueryStringBuilder sqs = QueryBuilders
            .simpleQueryStringQuery("asile")
            .defaultOperator(Operator.OR)
            .lenient(false)
            .analyzeWildcard(false);
        DOSSIER_FULL_TEXT_FIELDS.forEach(sqs::field);
        should0.add(sqs);

        sr.source().fetchSource(false);

        // nested query
        BoolQueryBuilder bool2 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        // nested
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder()
            .setIgnoreUnmapped(false)
            .setFrom(0)
            .setVersion(false)
            .setExplain(false)
            .setTrackScores(false)
            .setFetchSourceContext(sr.source().fetchSource())
            .addDocValueField(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID);
        NestedQueryBuilder nested0 = QueryBuilders
            .nestedQuery("documents", bool2, ScoreMode.Total)
            .boost(1.0F)
            .ignoreUnmapped(false)
            .innerHit(innerHitBuilder);

        sqs =
            QueryBuilders
                .simpleQueryStringQuery("asile")
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
        DOC_FILE_DATA_FIELDS.forEach(sqs::field);
        bool2.should(sqs);
        sqs =
            QueryBuilders
                .simpleQueryStringQuery("asile")
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
        DOC_DC_TITLE_FIELDS.forEach(sqs::field);
        bool2.should(sqs);

        should0.add(nested0);

        must0.add(bool1);

        sr.source().query(bool0);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDossier.DOS_TITRE_ACTE, 1500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        return sr;
    }

    private SearchRequest initRequestC902() {
        List<String> storedFields = Collections.singletonList(ElasticDocument.DC_TITLE);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(1);
        sourceBuilder.storedFields(storedFields);
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        BoolQueryBuilder bool0 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> must0 = bool0.must();

        must0.add(QueryBuilders.termQuery(ElasticDossier.UID, "40ef0ec4-b202-40b5-9e1c-c7fbbcaeac6b").boost(1));

        List<QueryBuilder> should0 = bool0.should();
        SimpleQueryStringBuilder sqs = QueryBuilders
            .simpleQueryStringQuery("asile")
            .defaultOperator(Operator.OR)
            .lenient(false)
            .analyzeWildcard(false);
        DOC_HIGHLIGHTING_FIELDS.forEach(sqs::field);
        should0.add(sqs);

        sr.source().query(bool0);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDocument.DC_TITLE, 500, 1)
            .field(ElasticDocument.FILE_DATA, 150, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        sr.source().fetchSource(false);

        return sr;
    }

    private SearchRequest initRequestFulltext() {
        List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
        List<String> docvalueFields = Arrays.asList(
            ElasticDossier.DOS_MINISTERE_RESP,
            ElasticDossier.DOS_STATUT,
            ElasticDossier.DOS_NUMERO_NOR,
            ElasticDossier.RETDILA_DATE_PARUTION_JORF
        );

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(25);
        sourceBuilder.storedFields(storedFields);
        for (String docvalueField : docvalueFields) {
            sourceBuilder.docValueField(docvalueField, "use_field_mapping");
        }
        SearchRequest sr = new SearchRequest();
        sr.source(sourceBuilder);

        BoolQueryBuilder query0 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> must0 = query0.must();

        BoolQueryBuilder query1 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        List<QueryBuilder> should0 = query1.should();

        SimpleQueryStringBuilder sqs = QueryBuilders
            .simpleQueryStringQuery("qualité des prestations délivrées")
            .defaultOperator(Operator.OR)
            .lenient(false)
            .analyzeWildcard(false);
        DOSSIER_FULL_TEXT_FIELDS.forEach(sqs::field);
        should0.add(sqs);

        // nested query
        BoolQueryBuilder query2 = QueryBuilders.boolQuery().boost(1.0F).adjustPureNegative(true);

        sr.source().fetchSource(false);

        HighlightBuilder highlight0 = new HighlightBuilder()
            .field(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA, 150, 1)
            .field(ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE, 500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");

        // nested
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder()
            .setIgnoreUnmapped(false)
            .setFrom(0)
            .setVersion(false)
            .setExplain(false)
            .setTrackScores(false)
            .setFetchSourceContext(sr.source().fetchSource())
            .addDocValueField(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID)
            .setStoredFieldNames(Collections.singletonList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE))
            .setHighlightBuilder(highlight0);
        NestedQueryBuilder nested0 = QueryBuilders
            .nestedQuery("documents", query2, ScoreMode.Total)
            .boost(1.0F)
            .ignoreUnmapped(false)
            .innerHit(innerHitBuilder);

        sqs =
            QueryBuilders
                .simpleQueryStringQuery("qualité des prestations délivrées")
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
        DOC_FILE_DATA_FIELDS.forEach(sqs::field);
        query2.should(sqs);
        sqs =
            QueryBuilders
                .simpleQueryStringQuery("qualité des prestations délivrées")
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
        DOC_DC_TITLE_FIELDS.forEach(sqs::field);
        query2.should(sqs);

        should0.add(nested0);

        must0.add(query1);

        sr.source().query(query0);

        TermsAggregationBuilder agg0 = AggregationBuilders
            .terms(FacetEnum.CATEGORIE_ACTE.getFacetName())
            .field(ElasticDossier.DOS_CATEGORIE_ACTE)
            .size(100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.aggregation("_term", true));

        TermsAggregationBuilder agg1 = AggregationBuilders
            .terms(FacetEnum.VECTEUR_PUBLICATION.getFacetName())
            .field(ElasticDossier.DOS_VECTEUR_PUBLICATION)
            .size(100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.aggregation("_term", true));

        TermsAggregationBuilder agg2 = AggregationBuilders
            .terms(FacetEnum.MINISTERE_ATTACHE.getFacetName())
            .field(ElasticDossier.DOS_MINISTERE_ATTACHE)
            .size(100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(Arrays.asList(BucketOrder.count(false), BucketOrder.aggregation("_term", true)));

        TermsAggregationBuilder agg3 = AggregationBuilders
            .terms(FacetEnum.STATUT.getFacetName())
            .field(ElasticDossier.DOS_STATUT)
            .size(100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.aggregation("_term", true));

        sr.source().aggregation(agg0);
        sr.source().aggregation(agg1);
        sr.source().aggregation(agg2);
        sr.source().aggregation(agg3);

        // highlight
        HighlightBuilder highlight = new HighlightBuilder()
            .field(ElasticDossier.DOS_TITRE_ACTE, 1500, 1)
            .requireFieldMatch(false)
            .highlighterType("plain");
        sr.source().highlighter(highlight);

        return sr;
    }
}
