package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.ElasticStep;
import fr.dila.solonepg.elastic.models.search.ClauseEt;
import fr.dila.solonepg.elastic.models.search.ClauseOu;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.models.search.enums.ElasticOperatorEnum;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.IntervalQueryBuilder;
import org.elasticsearch.index.query.IntervalsSourceProvider;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.NuxeoException;

public class SearchRequestMapperExpTest {
    SearchRequestMapperExp mapper = new SearchRequestMapperExp();
    private static final String DEFAULT_SORT =
        "{\n" + "  \"dos:numeroNor\" : {\n" + "    \"order\" : \"asc\"\n" + "  }\n" + "}";
    private static final String ISA_SORT =
        "{\n" + "  \"consetat:numeroISA\" : {\n" + "    \"order\" : \"desc\"\n" + "  }\n" + "}";
    private static final String AG_CE_SORT =
        "{\n" + "  \"consetat:dateAgCe\" : {\n" + "    \"order\" : \"asc\"\n" + "  }\n" + "}";

    private static final String INDEX_DOSSIERS = "dossiers";

    public SearchRequestMapperExpTest() throws URISyntaxException {}

    @Test
    public void testRechercheExperteDateEntreAbsolue() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.ENTRE,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        null,
                        "2019-05-03",
                        "2019-08-07",
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        RangeQueryBuilder range = (RangeQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertTrue(range.includeLower());
        Assert.assertTrue(range.includeUpper());
        Assert.assertEquals("2019-05-03", range.from());
        Assert.assertEquals("2019-08-07", range.to());
    }

    @Test
    public void testRechercheExperteDateEntreRelative() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.ENTRE,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        null,
                        "-15",
                        "5",
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        RangeQueryBuilder range = (RangeQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertTrue(range.includeLower());
        Assert.assertTrue(range.includeUpper());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Assert.assertEquals(LocalDate.now().minusDays(15).format(formatter), range.from());
        Assert.assertEquals(LocalDate.now().plusDays(5).format(formatter), range.to());
    }

    @Test
    public void testRechercheExperteDateEntreAbsolueEtRelative() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.ENTRE,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        null,
                        "2019-05-03",
                        "-2",
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        RangeQueryBuilder range = (RangeQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertTrue(range.includeLower());
        Assert.assertTrue(range.includeUpper());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Assert.assertEquals("2019-05-03", range.from());
        Assert.assertEquals(LocalDate.now().minusDays(2).format(formatter), range.to());
    }

    @Test
    public void testRechercheExperteDatePasEntreAbsolue() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.PAS_ENTRE,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        null,
                        "2019-05-03",
                        "2019-08-07",
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> mustNot = orQuery.mustNot();
        Assert.assertEquals(1, mustNot.size());

        RangeQueryBuilder range = (RangeQueryBuilder) mustNot.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertTrue(range.includeLower());
        Assert.assertTrue(range.includeUpper());
        Assert.assertEquals("2019-05-03", range.from());
        Assert.assertEquals("2019-08-07", range.to());
    }

    @Test
    public void testRechercheExperteDatePlusPetitAbsolue() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.PLUS_PETIT,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        "2019-05-03",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> must = rootQuery.must();
        Assert.assertEquals(3, must.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) must.get(0);
        List<QueryBuilder> subShould = orQuery.should();
        Assert.assertEquals(1, subShould.size());

        List<QueryBuilder> subSubMust = ((BoolQueryBuilder) subShould.get(0)).must();
        RangeQueryBuilder range = (RangeQueryBuilder) subSubMust.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertFalse(range.includeUpper());
        Assert.assertEquals("2019-05-03", range.to());
    }

    @Test
    public void testRechercheExperteDatePlusGrandAbsolue() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.PLUS_GRAND,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        "2019-05-03",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        RangeQueryBuilder range = (RangeQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertFalse(range.includeLower());
        Assert.assertEquals("2019-05-03", range.from());
    }

    @Test
    public void testRechercheExperteDatePlusPetitEgalAbsolue() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.PLUS_PETIT_OU_EGAL,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        "2019-05-03",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        RangeQueryBuilder range = (RangeQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertTrue(range.includeUpper());
        Assert.assertEquals("2019-05-03", range.to());
    }

    @Test
    public void testRechercheExperteDatePlusGrandEgalAbsolue() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.PLUS_GRAND_OU_EGAL,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        "2019-05-03",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        RangeQueryBuilder range = (RangeQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, range.fieldName());
        Assert.assertTrue(range.includeLower());
        Assert.assertEquals("2019-05-03", range.from());
    }

    @Test
    public void testRechercheExperteDateVide() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.EST_VIDE,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        null,
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> mustNot = orQuery.mustNot();
        Assert.assertEquals(1, mustNot.size());

        ExistsQueryBuilder exist = (ExistsQueryBuilder) mustNot.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist.fieldName());
    }

    @Test
    public void testRechercheExperteDatePasVide() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.N_EST_PAS_VIDE,
                        ElasticDossier.CONSETAT_DATE_AG_CE,
                        null,
                        null,
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        ExistsQueryBuilder exist = (ExistsQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist.fieldName());
    }

    @Test
    public void testRechercheExperteTexteCommeText() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.TOUS_LES_MOTS,
                        ElasticDossier.CONSETAT_RAPPORTEUR_CE,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        IntervalQueryBuilder intervalsQuery = (IntervalQueryBuilder) must.get(0);
        IntervalsSourceProvider sourceProvider = new IntervalsSourceProvider.Match(
            "test des textes",
            10,
            true,
            null,
            null,
            null
        );
        IntervalQueryBuilder intervals = new IntervalQueryBuilder(
            ElasticDossier.CONSETAT_RAPPORTEUR_CE,
            sourceProvider
        );
        Assert.assertEquals(intervals, intervalsQuery);
    }

    @Test
    public void testRechercheExperteTexteCommeKeyword() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.TOUS_LES_MOTS,
                        ElasticDossier.CONSETAT_NUMERO_ISA,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_NUMERO_ISA, wildcard.fieldName());
        Assert.assertEquals("test des textes", wildcard.value());
    }

    @Test
    public void testRechercheExperteTextePasComme() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.AUCUN_DES_MOTS,
                        ElasticDossier.CONSETAT_RAPPORTEUR_CE,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> mustNot = orQuery.mustNot();
        Assert.assertEquals(1, mustNot.size());

        MatchQueryBuilder match = (MatchQueryBuilder) mustNot.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_RAPPORTEUR_CE, match.fieldName());
        Assert.assertEquals("test des textes", match.value());
        Assert.assertEquals(Operator.AND, match.operator());
    }

    @Test
    public void testRechercheExperteTexteEgalText() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.EGAL,
                        ElasticDossier.RETDILA_PAGE_PARUTION_JORF,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        TermQueryBuilder matchPhrase = (TermQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.RETDILA_PAGE_PARUTION_JORF, matchPhrase.fieldName());
        Assert.assertEquals("test des textes", matchPhrase.value());
    }

    @Test
    public void testRechercheExperteTexteEgalKeyword() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.EGAL,
                        ElasticDossier.CONSETAT_NUMERO_ISA,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> must = rootQuery.must();
        Assert.assertEquals(3, must.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) must.get(0);
        List<QueryBuilder> subShould = orQuery.should();
        Assert.assertEquals(1, subShould.size());

        List<QueryBuilder> subSubMust = ((BoolQueryBuilder) subShould.get(0)).must();
        TermQueryBuilder term = (TermQueryBuilder) subSubMust.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_NUMERO_ISA, term.fieldName());
        Assert.assertEquals("test des textes", term.value());
    }

    @Test
    public void testRechercheExperteNestedGrouped() {
        SearchRequest request = null;
        try {
            SearchCriteriaExp createExpertCriteria = createExpertCriteria(
                ElasticOperatorEnum.EGAL,
                ElasticStep.RTSK_OBLIGATOIRE_MINISTERE,
                ElasticDossier.STEPS,
                "true",
                null,
                null,
                null
            );

            ClauseEt type = new ClauseEt();
            type.setField(ElasticStep.RTSK_TYPE);
            type.setNestedPath(ElasticDossier.STEPS);
            type.setOperator(ElasticOperatorEnum.CONTIENT);
            type.setValeur("10");
            ClauseOu clauseOu = createExpertCriteria.getClausesOu().iterator().next();
            clauseOu.setClausesEt(Arrays.asList(clauseOu.getClausesEt().iterator().next(), type));

            request = mapper.from(createExpertCriteria, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        NestedQueryBuilder nestedQuery = (NestedQueryBuilder) must.get(0);
        List<QueryBuilder> nestedMust = ((BoolQueryBuilder) nestedQuery.query()).must();
        Assert.assertEquals(2, nestedMust.size());

        TermQueryBuilder obligatoireMinistere = (TermQueryBuilder) nestedMust.get(0);
        Assert.assertEquals(
            ElasticDossier.STEPS + "." + ElasticStep.RTSK_OBLIGATOIRE_MINISTERE,
            obligatoireMinistere.fieldName()
        );
        Assert.assertEquals("true", obligatoireMinistere.value());

        TermQueryBuilder type = (TermQueryBuilder) nestedMust.get(1);
        Assert.assertEquals(ElasticDossier.STEPS + "." + ElasticStep.RTSK_TYPE, type.fieldName());
        Assert.assertEquals("10", type.value());
    }

    @Test
    public void testRechercheExperteTextePartielComme() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.TOUS_LES_MOTS,
                        ElasticDossier.DOS_LIBRE,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        MatchQueryBuilder match = (MatchQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.DOS_LIBRE, match.fieldName());
        Assert.assertEquals("test des textes", match.value());
        Assert.assertEquals(Operator.AND, match.operator());
    }

    @Test
    public void testRechercheExperteTextePartielPasComme() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.AUCUN_DES_MOTS,
                        ElasticDossier.DOS_LIBRE,
                        null,
                        "test des textes",
                        null,
                        null,
                        null
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> mustNot = orQuery.mustNot();
        Assert.assertEquals(1, mustNot.size());

        MatchQueryBuilder match = (MatchQueryBuilder) mustNot.get(0);
        Assert.assertEquals(ElasticDossier.DOS_LIBRE, match.fieldName());
        Assert.assertEquals("test des textes", match.value());
        Assert.assertEquals(Operator.OR, match.operator());
    }

    @Test
    public void testRechercheExperteListeContient() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.CONTIENT,
                        ElasticDossier.DOS_CATEGORIE_ACTE,
                        null,
                        null,
                        null,
                        null,
                        Collections.singletonList("test liste")
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        TermsQueryBuilder terms = (TermsQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.DOS_CATEGORIE_ACTE, terms.fieldName());
        Assert.assertEquals(Collections.singletonList("test liste"), terms.values());
    }

    @Test
    public void testRechercheExperteListeNeContientPas() {
        SearchRequest request = null;
        try {
            request =
                mapper.from(
                    createExpertCriteria(
                        ElasticOperatorEnum.NE_CONTIENT_PAS,
                        ElasticDossier.DOS_CATEGORIE_ACTE,
                        null,
                        null,
                        null,
                        null,
                        Collections.singletonList("test liste")
                    ),
                    INDEX_DOSSIERS
                );
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> mustNot = orQuery.mustNot();
        Assert.assertEquals(1, mustNot.size());

        TermsQueryBuilder terms = (TermsQueryBuilder) mustNot.get(0);
        Assert.assertEquals(ElasticDossier.DOS_CATEGORIE_ACTE, terms.fieldName());
        Assert.assertEquals(Collections.singletonList("test liste"), terms.values());
    }

    @Test
    public void testUnOuPlusieursEt() {
        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        searchCriteriaExp.setPage(1);
        searchCriteriaExp.setPageSize(10);
        ClauseEt clauseEt1 = new ClauseEt();
        clauseEt1.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt1.setField(ElasticDossier.CONSETAT_DATE_AG_CE);
        ClauseEt clauseEt2 = new ClauseEt();
        clauseEt2.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt2.setField(ElasticDossier.CONSETAT_DATE_SECTION_CE);
        ClauseOu clauseOu = new ClauseOu();
        clauseOu.setClausesEt(Arrays.asList(clauseEt1, clauseEt2));
        searchCriteriaExp.setClausesOu(Collections.singletonList(clauseOu));

        SearchRequest request = null;
        try {
            request = mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> must = rootQuery.must();
        Assert.assertEquals(3, must.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) must.get(0);
        List<QueryBuilder> subShoulds = orQuery.should();
        Assert.assertEquals(1, subShoulds.size());
        BoolQueryBuilder subShould = (BoolQueryBuilder) subShoulds.get(0);

        List<QueryBuilder> mustNot = subShould.mustNot();
        Assert.assertEquals(2, mustNot.size());

        ExistsQueryBuilder exist1 = (ExistsQueryBuilder) mustNot.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist1.fieldName());
        ExistsQueryBuilder exist2 = (ExistsQueryBuilder) mustNot.get(1);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_SECTION_CE, exist2.fieldName());
    }

    @Test
    public void testUnEtPlusieursOu() {
        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        searchCriteriaExp.setPage(1);
        searchCriteriaExp.setPageSize(10);
        ClauseEt clauseEt1 = new ClauseEt();
        clauseEt1.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt1.setField(ElasticDossier.CONSETAT_DATE_AG_CE);
        ClauseOu clauseOu1 = new ClauseOu();
        clauseOu1.setClausesEt(Collections.singletonList(clauseEt1));
        ClauseEt clauseEt2 = new ClauseEt();
        clauseEt2.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt2.setField(ElasticDossier.CONSETAT_DATE_SECTION_CE);
        ClauseOu clauseOu2 = new ClauseOu();
        clauseOu2.setClausesEt(Collections.singletonList(clauseEt2));
        searchCriteriaExp.setClausesOu(Arrays.asList(clauseOu1, clauseOu2));

        SearchRequest request = null;
        try {
            request = mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());

        BoolQueryBuilder orQuery1 = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);
        List<QueryBuilder> mustNot1 = orQuery1.mustNot();
        Assert.assertEquals(1, mustNot1.size());
        ExistsQueryBuilder exist1 = (ExistsQueryBuilder) mustNot1.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist1.fieldName());

        BoolQueryBuilder orQuery2 = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(1);
        List<QueryBuilder> mustNot2 = orQuery2.mustNot();
        Assert.assertEquals(1, mustNot2.size());
        ExistsQueryBuilder exist2 = (ExistsQueryBuilder) mustNot2.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_SECTION_CE, exist2.fieldName());
    }

    @Test
    public void testPlusieursEtPlusieursOu() {
        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        searchCriteriaExp.setPage(1);
        searchCriteriaExp.setPageSize(10);
        ClauseEt clauseEt1 = new ClauseEt();
        clauseEt1.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt1.setField(ElasticDossier.CONSETAT_DATE_AG_CE);
        ClauseOu clauseOu1 = new ClauseOu();
        ClauseEt clauseEt2 = new ClauseEt();
        clauseEt2.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt2.setField(ElasticDossier.CONSETAT_DATE_SECTION_CE);
        clauseOu1.setClausesEt(Arrays.asList(clauseEt1, clauseEt2));
        ClauseEt clauseEt3 = new ClauseEt();
        clauseEt3.setOperator(ElasticOperatorEnum.EST_VIDE);
        clauseEt3.setField(ElasticDossier.CONSETAT_RAPPORTEUR_CE);
        ClauseOu clauseOu2 = new ClauseOu();
        clauseOu2.setClausesEt(Collections.singletonList(clauseEt3));
        searchCriteriaExp.setClausesOu(Arrays.asList(clauseOu1, clauseOu2));

        SearchRequest request = null;
        try {
            request = mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> must = rootQuery.must();
        Assert.assertEquals(3, must.size());

        BoolQueryBuilder orQuery1 = (BoolQueryBuilder) ((BoolQueryBuilder) must.get(0)).should().get(0);
        List<QueryBuilder> mustNot1 = orQuery1.mustNot();
        Assert.assertEquals(2, mustNot1.size());
        ExistsQueryBuilder exist1 = (ExistsQueryBuilder) mustNot1.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist1.fieldName());
        ExistsQueryBuilder exist2 = (ExistsQueryBuilder) mustNot1.get(1);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_SECTION_CE, exist2.fieldName());

        BoolQueryBuilder orQuery2 = (BoolQueryBuilder) ((BoolQueryBuilder) must.get(0)).should().get(1);
        List<QueryBuilder> mustNot2 = orQuery2.mustNot();
        Assert.assertEquals(1, mustNot2.size());
        ExistsQueryBuilder exist3 = (ExistsQueryBuilder) mustNot2.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_RAPPORTEUR_CE, exist3.fieldName());
    }

    @Test
    public void testTriSimple() {
        SearchRequest request = null;
        SearchCriteriaExp searchCriteriaExp = createExpertCriteria(
            ElasticOperatorEnum.N_EST_PAS_VIDE,
            ElasticDossier.CONSETAT_DATE_AG_CE,
            null,
            null,
            null,
            null,
            null
        );
        LinkedHashMap<String, String> tris = new LinkedHashMap<>();
        tris.put(ElasticDossier.CONSETAT_NUMERO_ISA, "desc");
        searchCriteriaExp.setTris(tris);
        try {
            request = mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ISA_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.DESC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        ExistsQueryBuilder exist = (ExistsQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist.fieldName());
    }

    @Test(expected = ElasticException.class)
    public void testTriImpossible() throws ElasticException {
        SearchCriteriaExp searchCriteriaExp = createExpertCriteria(
            ElasticOperatorEnum.N_EST_PAS_VIDE,
            ElasticDossier.CONSETAT_DATE_AG_CE,
            null,
            null,
            null,
            null,
            null
        );
        LinkedHashMap<String, String> tris = new LinkedHashMap<>();
        tris.put(ElasticDossier.CONSETAT_RECEPTION_AVIS_CE, "desc");
        searchCriteriaExp.setTris(tris);
        try {
            mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException e) {
            Assert.fail();
        }
    }

    @Test
    public void testTriComplexe() {
        SearchRequest request = null;
        SearchCriteriaExp searchCriteriaExp = createExpertCriteria(
            ElasticOperatorEnum.N_EST_PAS_VIDE,
            ElasticDossier.CONSETAT_DATE_AG_CE,
            null,
            null,
            null,
            null,
            null
        );
        LinkedHashMap<String, String> tris = new LinkedHashMap<>();
        tris.put(ElasticDossier.CONSETAT_NUMERO_ISA, "desc");
        tris.put(ElasticDossier.DOS_NUMERO_NOR, "asc");
        tris.put(ElasticDossier.CONSETAT_DATE_AG_CE, "asc");
        searchCriteriaExp.setTris(tris);
        try {
            request = mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ISA_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.DESC, request.source().sorts().get(0).order());
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(1).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(1).order());
        Assert.assertEquals(AG_CE_SORT, request.source().sorts().get(2).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(2).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        ExistsQueryBuilder exist = (ExistsQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist.fieldName());
    }

    @Test
    public void testColonnes() {
        SearchRequest request = null;
        SearchCriteriaExp searchCriteriaExp = createExpertCriteria(
            ElasticOperatorEnum.N_EST_PAS_VIDE,
            ElasticDossier.CONSETAT_DATE_AG_CE,
            null,
            null,
            null,
            null,
            null
        );
        HashSet<String> colonnes = new HashSet<>(
            Arrays.asList(
                ElasticDossier.CONSETAT_NUMERO_ISA,
                ElasticDossier.DOS_NUMERO_NOR,
                ElasticDossier.CONSETAT_DATE_AG_CE
            )
        );
        searchCriteriaExp.setColonnes(colonnes);
        try {
            request = mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
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

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> rootMusts = rootQuery.must();
        Assert.assertEquals(3, rootMusts.size());
        BoolQueryBuilder orQuery = (BoolQueryBuilder) ((BoolQueryBuilder) rootMusts.get(0)).should().get(0);

        List<QueryBuilder> must = orQuery.must();
        Assert.assertEquals(1, must.size());

        ExistsQueryBuilder exist = (ExistsQueryBuilder) must.get(0);
        Assert.assertEquals(ElasticDossier.CONSETAT_DATE_AG_CE, exist.fieldName());
    }

    @Test(expected = ElasticException.class)
    public void testColonnesNonRemontable() throws ElasticException {
        SearchCriteriaExp searchCriteriaExp = createExpertCriteria(
            ElasticOperatorEnum.N_EST_PAS_VIDE,
            ElasticDossier.CONSETAT_DATE_AG_CE,
            null,
            null,
            null,
            null,
            null
        );
        searchCriteriaExp.setColonnes(Collections.singleton(ElasticDossier.CONSETAT_RECEPTION_AVIS_CE));
        try {
            mapper.from(searchCriteriaExp, INDEX_DOSSIERS);
        } catch (NuxeoException e) {
            Assert.fail();
        }
    }

    @Test(expected = ElasticException.class)
    public void testMauvaisOperateur() throws ElasticException {
        try {
            mapper.from(
                createExpertCriteria(
                    ElasticOperatorEnum.AUCUN_DES_MOTS,
                    ElasticDossier.DOS_CATEGORIE_ACTE,
                    null,
                    "test des textes",
                    null,
                    null,
                    null
                ),
                INDEX_DOSSIERS
            );
        } catch (NuxeoException e) {
            Assert.fail();
        }
    }

    private SearchCriteriaExp createExpertCriteria(
        ElasticOperatorEnum operator,
        String field,
        String nestedPath,
        String valeur,
        String valeurMin,
        String valeurMax,
        List<String> valeurs
    ) {
        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        searchCriteriaExp.setPage(1);
        searchCriteriaExp.setPageSize(10);
        searchCriteriaExp.setTotal(15);
        ClauseEt clauseEt = new ClauseEt();
        clauseEt.setOperator(operator);
        clauseEt.setField(field);
        clauseEt.setNestedPath(nestedPath);
        clauseEt.setValeur(valeur);
        clauseEt.setValeurMin(valeurMin);
        clauseEt.setValeurMax(valeurMax);
        clauseEt.setListeValeur(valeurs);
        ClauseOu clauseOu = new ClauseOu();
        clauseOu.setClausesEt(Collections.singletonList(clauseEt));
        searchCriteriaExp.setClausesOu(Collections.singletonList(clauseOu));
        return searchCriteriaExp;
    }
}
