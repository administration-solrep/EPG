package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.ElasticStep;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.FieldAndFormat;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.NuxeoException;

public class SearchRequestMapperTest {
    private static final String INDEX_DOCUMENTS = "documents";

    private static final String INDEX_DOSSIERS = "dossiers";

    @Test
    public void testVecteursPublication() throws URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String[] vecteurs = new String[] { "BO (DEV) - Ecologie", "Journal Officiel" };

        criteria.setVecteurPublication(Arrays.asList(vecteurs));

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.must();

        Assert.assertEquals(2, clauses.size());

        checkTermClause(ElasticDossier.DOS_VECTEUR_PUBLICATION, vecteurs[0], (TermQueryBuilder) clauses.get(0));
        checkTermClause(ElasticDossier.DOS_VECTEUR_PUBLICATION, vecteurs[1], (TermQueryBuilder) clauses.get(1));
    }

    @Test
    public void testCategorieActe() throws URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String[] categories = new String[] { "Non réglementaire" };

        criteria.setCategorieActe(Arrays.asList(categories));

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.must();

        Assert.assertEquals(1, clauses.size());

        checkTermClause(ElasticDossier.DOS_CATEGORIE_ACTE, categories[0], (TermQueryBuilder) clauses.get(0));
    }

    @Test
    public void testStatut() throws URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String[] status = new String[] { "Publié" };

        criteria.setStatut(Arrays.asList(status));

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.must();

        Assert.assertEquals(1, clauses.size());

        checkTermClause(ElasticDossier.DOS_STATUT, status[0], (TermQueryBuilder) clauses.get(0));
    }

    @Test
    public void testMinistereAttache() throws URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String[] ministeres = new String[] { "Ministère de la justice" };

        criteria.setMinistereAttache(Arrays.asList(ministeres));

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.must();

        Assert.assertEquals(1, clauses.size());

        checkTermClause(ElasticDossier.DOS_MINISTERE_ATTACHE, ministeres[0], (TermQueryBuilder) clauses.get(0));
    }

    @Test
    public void testStatutArchivage() throws URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        List<String> statutArchivage = new ArrayList<>();
        statutArchivage.add(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE);
        statutArchivage.add(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_GENERE);

        criteria.setStatutArchivage(statutArchivage);

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.should();

        Assert.assertEquals(2, clauses.size());

        checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE, statutArchivage.get(0), (TermQueryBuilder) clauses.get(0));
        checkTermClause(ElasticDossier.DOS_STATUT_ARCHIVAGE, statutArchivage.get(1), (TermQueryBuilder) clauses.get(1));
    }

    @Test
    public void testDatesPublication() throws ParseException, URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String dateMin = "01/01/2017";
        String dateMax = "31/01/2017";

        criteria.setDatePublicationMin(dateMin);
        criteria.setDatePublicationMax(dateMax);

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.must();

        Assert.assertEquals(1, clauses.size());

        RangeQueryBuilder rangeClause = (RangeQueryBuilder) clauses.get(0);
        Assert.assertEquals(ElasticDossier.DOS_DATE_PUBLICATION, rangeClause.fieldName());
        Assert.assertEquals(1.0, rangeClause.boost(), 0);

        Assert.assertEquals(dateMin, new SimpleDateFormat("dd/MM/yyyy").format((Date) rangeClause.from()));
        Assert.assertEquals(dateMax, new SimpleDateFormat("dd/MM/yyyy").format((Date) rangeClause.to()));
    }

    @Test
    public void testDatesCreation() throws ParseException, URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String dateMin = "01/01/2017";
        String dateMax = "31/01/2017";

        criteria.setDateCreationMin(dateMin);
        criteria.setDateCreationMax(dateMax);

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        List<QueryBuilder> clauses = mustClause.must();

        Assert.assertEquals(1, clauses.size());

        checkRangeClause(ElasticDossier.DOS_CREATED, dateMin, dateMax, (RangeQueryBuilder) clauses.get(0));
    }

    @Test
    public void testFulltext() throws URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String fulltext = "qualité des prestations délivrées";

        criteria.setFulltext(fulltext);

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        TermQueryBuilder clause = (TermQueryBuilder) mustClause.must().get(0);

        Assert.assertEquals(ElasticDossier.DOS_ARCHIVE, clause.fieldName());

        // 1 : recherche text
        BoolQueryBuilder shouldQuery = (BoolQueryBuilder) query.must().get(1);
        List<QueryBuilder> subClauses = shouldQuery.should();

        Assert.assertEquals(3, subClauses.size());

        String expectedFulltext = Arrays
            .stream(fulltext.split(" "))
            .map(word -> "\"" + word + "\"")
            .collect(Collectors.joining(" "));
        checkFulltextClause(
            expectedFulltext,
            (SimpleQueryStringBuilder) subClauses.get(0),
            (NestedQueryBuilder) subClauses.get(1)
        );

        // 2 : perms
        mustClause = (BoolQueryBuilder) query.must().get(2);
        clause = (TermQueryBuilder) mustClause.should().get(0);
        Assert.assertEquals(ElasticDossier.PERMS_KEYWORD, clause.fieldName());

        // 3 : dos:mesureNominative
        mustClause = (BoolQueryBuilder) query.must().get(3);
        clause = (TermQueryBuilder) mustClause.must().get(0);
        Assert.assertEquals(ElasticDossier.DOS_MESURE_NOMINATIVE, clause.fieldName());
    }

    @Test
    public void testColonnes() throws URISyntaxException {
        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;

        HashSet<String> colonnes = new HashSet<>(
            Arrays.asList(
                ElasticDossier.CONSETAT_NUMERO_ISA,
                ElasticDossier.DOS_NUMERO_NOR,
                ElasticDossier.CONSETAT_DATE_AG_CE
            )
        );
        RechercheLibre searchCriteria = new RechercheLibre();
        searchCriteria.setColonnes(colonnes);
        searchCriteria.setFulltext(" test");
        try {
            request = mapper.from(searchCriteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(
            colonnes,
            request.source().docValueFields().stream().map(f -> f.field).collect(Collectors.toSet())
        );

        checkAllAggregations(request);

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        BoolQueryBuilder mustClause = (BoolQueryBuilder) query.must().get(0);

        TermQueryBuilder clause = (TermQueryBuilder) mustClause.must().get(0);

        Assert.assertEquals(ElasticDossier.DOS_ARCHIVE, clause.fieldName());

        // 1 : recherche text
        BoolQueryBuilder shouldQuery = (BoolQueryBuilder) query.must().get(1);
        List<QueryBuilder> subClauses = shouldQuery.should();

        Assert.assertEquals(3, subClauses.size());

        checkFulltextClause(
            "\"test\"",
            (SimpleQueryStringBuilder) subClauses.get(0),
            (NestedQueryBuilder) subClauses.get(1)
        );

        // 2 : perms
        mustClause = (BoolQueryBuilder) query.must().get(2);
        clause = (TermQueryBuilder) mustClause.should().get(0);
        Assert.assertEquals(ElasticDossier.PERMS_KEYWORD, clause.fieldName());

        // 3 : dos:mesureNominative
        mustClause = (BoolQueryBuilder) query.must().get(3);
        clause = (TermQueryBuilder) mustClause.must().get(0);
        Assert.assertEquals(ElasticDossier.DOS_MESURE_NOMINATIVE, clause.fieldName());
    }

    @Test
    public void testAggregation() throws URISyntaxException {
        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(new RechercheLibre(), null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        checkAllAggregations(request);
    }

    @Test
    public void testHighlight() throws URISyntaxException {
        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(new RechercheLibre(), null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        checkHighlights(request);
    }

    @Test
    public void testReqC85() throws ParseException, URISyntaxException {
        RechercheLibre criteria = new RechercheLibre();

        String[] ministeresResp = new String[] { "Ministère de l’agriculture et de l’alimentation" };
        String[] statuts = new String[] { "Lancé" };
        String from = "01/01/2017";

        criteria.setMinistereAttache(Arrays.asList(ministeresResp));
        criteria.setStatut(Arrays.asList(statuts));
        criteria.setDateCreationMin(from);

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(criteria, null, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(10, request.source().size());

        List<QueryBuilder> mainQuery = ((BoolQueryBuilder) request.source().query()).must();

        // statut, ministere resp, date de création, archive, permissions et mesure nominative -> 6 éléments
        Assert.assertEquals(6, mainQuery.size());

        {
            // vérification contrainte de ministère
            // on cherche un BoolQuery avec 1 TermQueryBuilder sur le terme DOS_MINISTERE_ATTACHE
            TermQueryBuilder ministereClause = null;
            BoolQueryBuilder ministereQuery = null;
            for (QueryBuilder clause : mainQuery) {
                if (clause instanceof BoolQueryBuilder) {
                    BoolQueryBuilder bool = (BoolQueryBuilder) clause;
                    if (bool.must() != null && bool.must().size() == 1) {
                        if (((BoolQueryBuilder) clause).must().get(0) instanceof TermQueryBuilder) {
                            TermQueryBuilder candidate = (TermQueryBuilder) ((BoolQueryBuilder) clause).must().get(0);
                            if (ElasticDossier.DOS_MINISTERE_ATTACHE.equals(candidate.fieldName())) {
                                ministereClause = candidate;
                                ministereQuery = (BoolQueryBuilder) clause;
                            }
                        }
                    }
                }
            }
            Assert.assertNotNull("TermQueryBuilder ministère n'a pas pu être trouvée", ministereClause);
            Assert.assertEquals("Ministère de l’agriculture et de l’alimentation", ministereClause.value());
            Assert.assertEquals(1.0, ministereQuery.boost(), 0);
            Assert.assertTrue(ministereQuery.adjustPureNegative());
        }

        {
            // vérification contrainte de statut
            // on cherche un BoolQuery avec 1 TermQueryBuilder sur le terme DOS_STATUT
            TermQueryBuilder statutClause = null;
            BoolQueryBuilder statutQuery = null;
            for (QueryBuilder clause : mainQuery) {
                if (clause instanceof BoolQueryBuilder) {
                    BoolQueryBuilder bool = (BoolQueryBuilder) clause;
                    if (bool.must() != null && bool.must().size() == 1) {
                        if (((BoolQueryBuilder) clause).must().get(0) instanceof TermQueryBuilder) {
                            TermQueryBuilder candidate = (TermQueryBuilder) ((BoolQueryBuilder) clause).must().get(0);
                            if (ElasticDossier.DOS_STATUT.equals(candidate.fieldName())) {
                                statutClause = candidate;
                                statutQuery = (BoolQueryBuilder) clause;
                            }
                        }
                    }
                }
            }
            Assert.assertNotNull("TermQueryBuilder status n'a pas pu être trouvée", statutClause);
            Assert.assertEquals("Lancé", statutClause.value());
            Assert.assertEquals(1.0, statutQuery.boost(), 0);
            Assert.assertTrue(statutQuery.adjustPureNegative());
        }

        {
            // vérification contrainte de dates
            // on cherche un BoolQuery avec 1 RangeQueryBuilder sur le terme DOS_CREATED
            RangeQueryBuilder rangeClause = null;
            for (QueryBuilder clause : mainQuery) {
                if (clause instanceof BoolQueryBuilder) {
                    BoolQueryBuilder bool = (BoolQueryBuilder) clause;
                    if (bool.must() != null && bool.must().size() == 1) {
                        if (((BoolQueryBuilder) clause).must().get(0) instanceof RangeQueryBuilder) {
                            RangeQueryBuilder candidate = (RangeQueryBuilder) ((BoolQueryBuilder) clause).must().get(0);
                            if (ElasticDossier.DOS_CREATED.equals(candidate.fieldName())) {
                                rangeClause = candidate;
                            }
                        }
                    }
                }
            }
            Assert.assertNotNull("RangeQueryBuilder n'a pas pu être trouvée", rangeClause);

            Assert.assertEquals(1.0, rangeClause.boost(), 0);
            Assert.assertTrue(rangeClause.includeLower());
            Assert.assertTrue(rangeClause.includeUpper());
            Assert.assertNull(rangeClause.to());
            Assert.assertEquals(from, new SimpleDateFormat("dd/MM/yyyy").format((Date) rangeClause.from()));
        }

        checkAllAggregations(request);
        checkHighlights(request);
    }

    @Test
    public void testDocumentQuery() throws URISyntaxException {
        String documentId = "40ef0ec4-b202-40b5-9e1c-c7fbbcaeac6b";
        String fulltext = "asile";

        ISearchRequestMapper mapper = new SearchRequestMapper();
        SearchRequest request = null;
        try {
            request = mapper.from(documentId, fulltext, null, INDEX_DOCUMENTS);
        } catch (NuxeoException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(1, request.source().size());

        List<String> storedFields = request.source().storedFields().fieldNames();
        Assert.assertEquals(1, storedFields.size());
        Assert.assertEquals(ElasticDocument.DC_TITLE, storedFields.get(0));

        BoolQueryBuilder query = (BoolQueryBuilder) request.source().query();

        List<QueryBuilder> must = query.must();
        Assert.assertEquals(1, must.size());
        TermQueryBuilder term = (TermQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDocument.UID, term.fieldName());
        Assert.assertEquals(documentId, term.value());
        Assert.assertEquals(1.0, term.boost(), 0);

        List<QueryBuilder> should = query.should();
        Assert.assertEquals(1, should.size());
        SimpleQueryStringBuilder sqs = (SimpleQueryStringBuilder) should.get(0);
        Assert.assertEquals(fulltext, sqs.value());
        Set<String> fields = sqs.fields().keySet();
        Assert.assertEquals(2, fields.size());
        Assert.assertTrue(fields.contains(ElasticDocument.FILE_DATA));
        Assert.assertTrue(fields.contains(ElasticDocument.DC_TITLE));

        HighlightBuilder highlight = request.source().highlighter();
        Assert.assertFalse(highlight.requireFieldMatch());
        List<HighlightBuilder.Field> highlightFields = highlight.fields();
        Assert.assertEquals(2, highlightFields.size());

        HighlightBuilder.Field field = highlightFields.get(0);
        Assert.assertEquals(ElasticDocument.DC_TITLE, field.name());
        Assert.assertEquals(new Integer(500), field.fragmentSize());
        Assert.assertEquals(new Integer(1), field.numOfFragments());

        field = highlightFields.get(1);
        Assert.assertEquals(ElasticDocument.FILE_DATA, field.name());
        Assert.assertEquals(new Integer(150), field.fragmentSize());
        Assert.assertEquals(new Integer(1), field.numOfFragments());
    }

    private void checkAggregation(
        TermsAggregationBuilder agg,
        String expectedField,
        int expectedSize,
        BucketOrder expectedOrders
    ) {
        Assert.assertEquals(expectedField, agg.field());
        Assert.assertEquals(expectedSize, agg.size());
        Assert.assertEquals(1, agg.minDocCount());
        Assert.assertEquals(0, agg.shardMinDocCount());
        Assert.assertFalse(agg.showTermDocCountError());
        Assert.assertEquals(expectedOrders, agg.order());
    }

    private void checkAllAggregations(SearchRequest request) {
        AggregatorFactories.Builder aggs = request.source().aggregations();

        Assert.assertEquals(10, aggs.count());
        Map<String, AggregationBuilder> map = request
            .source()
            .aggregations()
            .getAggregatorFactories()
            .stream()
            .collect(Collectors.toMap(AggregationBuilder::getName, Function.identity()));

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.CATEGORIE_ACTE.getFacetName()),
            ElasticDossier.DOS_CATEGORIE_ACTE,
            100,
            BucketOrder.key(true)
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.VECTEUR_PUBLICATION.getFacetName()),
            ElasticDossier.DOS_VECTEUR_PUBLICATION,
            100,
            BucketOrder.compound(BucketOrder.count(false), BucketOrder.key(true))
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.STATUT.getFacetName()),
            ElasticDossier.DOS_STATUT,
            100,
            BucketOrder.key(true)
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.STATUT_ARCHIVAGE.getFacetName()),
            ElasticDossier.DOS_STATUT_ARCHIVAGE,
            100,
            BucketOrder.key(true)
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.MINISTERE_ATTACHE.getFacetName()),
            ElasticDossier.DOS_MINISTERE_ATTACHE,
            100,
            BucketOrder.compound(BucketOrder.count(false), BucketOrder.key(true))
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.DIRECTION_ATTACHE.getFacetName()),
            ElasticDossier.DOS_DIRECTION_ATTACHE,
            100,
            BucketOrder.compound(BucketOrder.count(false), BucketOrder.key(true))
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.MAILBOX_DISTRIBUTION.getFacetName()),
            ElasticStep.RTSK_DISTRIBUTION_MAILBOX_ID,
            10,
            BucketOrder.key(true)
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.DATE_SIGNATURE_DOSSIER.getFacetName()),
            ElasticDossier.DOS_DATE_SIGNATURE,
            10,
            BucketOrder.key(true)
        );

        checkAggregation(
            (TermsAggregationBuilder) map.get(FacetEnum.DATE_PARUTION_JORF.getFacetName()),
            ElasticDossier.RETDILA_DATE_PARUTION_JORF,
            10,
            BucketOrder.key(true)
        );
    }

    private void checkFulltextClause(String expectedText, SimpleQueryStringBuilder sqs, NestedQueryBuilder nested) {
        Assert.assertEquals(expectedText, sqs.value());
        Set<String> keySet = sqs.fields().keySet();
        ArrayList<String> sqsList = new ArrayList<>(keySet);
        Assert.assertEquals(getDossierFullTextFieldsAndPonderation().size(), sqsList.size());
        Assert.assertTrue(
            getDossierFullTextFieldsAndPonderation().containsAll(sqsList) &&
            sqsList.containsAll(getDocumentFieldsAndPonderation())
        );
        Assert.assertEquals(Operator.OR, sqs.defaultOperator());
        Assert.assertFalse(sqs.lenient());
        Assert.assertTrue(sqs.analyzeWildcard());
        Assert.assertEquals(1.0, sqs.boost(), 0);

        Assert.assertFalse(nested.ignoreUnmapped());
        Assert.assertEquals(ScoreMode.Total, nested.scoreMode());
        Assert.assertEquals(1.0, nested.boost(), 0);

        InnerHitBuilder innerHits = nested.innerHit();
        Assert.assertFalse(innerHits.isIgnoreUnmapped());
        Assert.assertEquals(0, innerHits.getFrom());
        Assert.assertFalse(innerHits.isVersion());
        Assert.assertFalse(innerHits.isExplain());
        Assert.assertFalse(innerHits.isTrackScores());
        FieldAndFormat fieldAndFormat = new FieldAndFormat(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID, null);
        Assert.assertEquals(Collections.singletonList(fieldAndFormat), innerHits.getDocValueFields());

        BoolQueryBuilder nQuery = (BoolQueryBuilder) nested.query();
        Assert.assertEquals(1.0, nQuery.boost(), 0);
        Assert.assertTrue(nQuery.adjustPureNegative());

        List<QueryBuilder> nClauses = nQuery.must();
        // contrainte sur la recherche textuelle et contrainte booléen sur dernière version
        Assert.assertEquals(2, nClauses.size());

        sqs = (SimpleQueryStringBuilder) nClauses.get(0);
        Set<String> keySetBis = sqs.fields().keySet();
        ArrayList<String> sqsListBis = new ArrayList<>(keySetBis);
        Assert.assertEquals(getDossierFullTextFieldsAndPonderation().size(), sqsList.size());
        Assert.assertTrue(
            getDossierFullTextFieldsAndPonderation().containsAll(sqsListBis) &&
            sqsListBis.containsAll(getDocumentFieldsAndPonderation())
        );
        Assert.assertEquals(Operator.OR, sqs.defaultOperator());
        Assert.assertFalse(sqs.lenient());
        Assert.assertFalse(sqs.analyzeWildcard());
        Assert.assertEquals(1.0, sqs.boost(), 0);

        TermQueryBuilder tc = (TermQueryBuilder) nClauses.get(1);
        Assert.assertEquals("true", tc.value().toString());
        Assert.assertEquals(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_CURRENTVERSION, tc.fieldName());
        Assert.assertEquals(1.0, tc.boost(), 0);
    }

    private void checkHighlights(SearchRequest request) {
        HighlightBuilder highlight = request.source().highlighter();
        Assert.assertFalse(highlight.requireFieldMatch());
        List<HighlightBuilder.Field> fields = highlight.fields();

        Assert.assertEquals(2, fields.size());
        HighlightBuilder.Field field = fields.get(0);

        Assert.assertEquals(ElasticDossier.DOS_TITRE_ACTE, field.name());
        Assert.assertEquals(new Integer(1500), field.fragmentSize());
        Assert.assertEquals(new Integer(1), field.numOfFragments());

        field = fields.get(1);

        Assert.assertEquals(ElasticDossier.RETDILA_NUMERO_TEXTE_PARUTION_JORF, field.name());
        Assert.assertEquals(new Integer(1500), field.fragmentSize());
        Assert.assertEquals(new Integer(1), field.numOfFragments());
    }

    private void checkRangeClause(
        String expectedType,
        String expectedMin,
        String expectedMax,
        RangeQueryBuilder rangeClause
    )
        throws ParseException {
        Assert.assertEquals(expectedType, rangeClause.fieldName());
        Assert.assertEquals(1.0, rangeClause.boost(), 0);
        if (expectedMin == null) {
            Assert.assertNull(rangeClause.from());
        } else {
            Assert.assertEquals(expectedMin, new SimpleDateFormat("dd/MM/yyyy").format((Date) rangeClause.from()));
        }
        if (expectedMax == null) {
            Assert.assertNull(rangeClause.to());
        } else {
            Assert.assertEquals(expectedMax, new SimpleDateFormat("dd/MM/yyyy").format((Date) rangeClause.to()));
        }
    }

    private void checkTermClause(String expectedType, String expectedValue, TermQueryBuilder TermQueryBuilder) {
        Assert.assertEquals(expectedType, TermQueryBuilder.fieldName());
        Assert.assertEquals(1.0, TermQueryBuilder.boost(), 0);
        Assert.assertEquals(expectedValue, TermQueryBuilder.value());
    }

    private List<String> getDocumentFieldsAndPonderation() {
        return Arrays.asList(
            ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE,
            ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA
        );
    }

    private List<String> getDossierFullTextFieldsAndPonderation() {
        return Arrays.asList(
            ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE,
            ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA,
            ElasticDossier.DOS_NUMERO_NOR,
            ElasticDossier.DOS_TITRE_ACTE
        );
    }
}
