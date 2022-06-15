package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaRapide;
import fr.dila.solonepg.elastic.utils.SearchRequestUtils;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.NuxeoException;

public class SearchRequestMapperRapideTest {
    SearchRequestMapperRapide mapper = new SearchRequestMapperRapide();
    private static final String DEFAULT_SORT =
        "{\n" + "  \"dos:numeroNor\" : {\n" + "    \"order\" : \"asc\"\n" + "  }\n" + "}";
    private static final String ISA_SORT =
        "{\n" + "  \"consetat:numeroISA\" : {\n" + "    \"order\" : \"desc\"\n" + "  }\n" + "}";
    private static final String AG_CE_SORT =
        "{\n" + "  \"consetat:dateAgCe\" : {\n" + "    \"order\" : \"asc\"\n" + "  }\n" + "}";

    private static final String INDEX_DOSSIERS = "dossiers";

    public SearchRequestMapperRapideTest() throws URISyntaxException {}

    @Test
    public void testRechercheRapideRightWildcard() {
        SearchRequest request = null;
        try {
            request = mapper.from(createRapideCriteria("CCOZ200*"), INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException | URISyntaxException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        Assert.assertEquals(1, Integer.parseInt(rootQuery.minimumShouldMatch()));
        List<QueryBuilder> should = rootQuery.should();
        Assert.assertEquals(1, should.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) should.get(0);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, wildcard.fieldName());
        Assert.assertEquals("CCOZ200*", wildcard.value());
    }

    @Test
    public void testRechercheRapideLeftWildcard() {
        SearchRequest request = null;
        try {
            request = mapper.from(createRapideCriteria("*OZ2002Y"), INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException | URISyntaxException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        Assert.assertEquals(1, Integer.parseInt(rootQuery.minimumShouldMatch()));
        List<QueryBuilder> should = rootQuery.should();
        Assert.assertEquals(1, should.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) should.get(0);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR + ".reverse", wildcard.fieldName());
        Assert.assertEquals("*OZ2002Y", wildcard.value());
    }

    @Test
    public void testRechercheRapideListeWildcards() {
        SearchRequest request = null;
        try {
            request = mapper.from(createRapideCriteria("*OZ2002Y; CCO*200; *OZ2*002Y; *OZ200*"), INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException | URISyntaxException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        Assert.assertEquals(1, Integer.parseInt(rootQuery.minimumShouldMatch()));
        List<QueryBuilder> should = rootQuery.should();
        Assert.assertEquals(4, should.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) should.get(0);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR + ".reverse", wildcard.fieldName());
        Assert.assertEquals("*OZ2002Y", wildcard.value());

        wildcard = (WildcardQueryBuilder) should.get(1);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, wildcard.fieldName());
        Assert.assertEquals("CCO*200", wildcard.value());

        wildcard = (WildcardQueryBuilder) should.get(2);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR + ".reverse", wildcard.fieldName());
        Assert.assertEquals("*OZ2*002Y", wildcard.value());

        wildcard = (WildcardQueryBuilder) should.get(3);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, wildcard.fieldName());
        Assert.assertEquals("*OZ200*", wildcard.value());
    }

    @Test
    public void testTriSimple() {
        SearchRequest request = null;
        SearchCriteriaRapide searchCriteriaRapide = createRapideCriteria("CCO*200");
        LinkedHashMap<String, String> tris = new LinkedHashMap<>();
        tris.put(ElasticDossier.CONSETAT_NUMERO_ISA, "desc");
        searchCriteriaRapide.setTris(tris);
        try {
            request = mapper.from(searchCriteriaRapide, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException | URISyntaxException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(ISA_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.DESC, request.source().sorts().get(0).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        Assert.assertEquals(1, Integer.parseInt(rootQuery.minimumShouldMatch()));
        List<QueryBuilder> should = rootQuery.should();
        Assert.assertEquals(1, should.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) should.get(0);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, wildcard.fieldName());
        Assert.assertEquals("CCO*200", wildcard.value());
    }

    @Test(expected = ElasticException.class)
    public void testTriImpossible() throws ElasticException {
        SearchCriteriaRapide searchCriteriaRapide = createRapideCriteria("CCO*200");
        LinkedHashMap<String, String> tris = new LinkedHashMap<>();
        tris.put(ElasticDossier.CONSETAT_RECEPTION_AVIS_CE, "desc");
        searchCriteriaRapide.setTris(tris);
        try {
            mapper.from(searchCriteriaRapide, INDEX_DOSSIERS);
        } catch (NuxeoException | URISyntaxException e) {
            Assert.fail();
        }
    }

    @Test
    public void testTriComplexe() {
        SearchRequest request = null;
        SearchCriteriaRapide searchCriteriaRapide = createRapideCriteria("CCO*200");
        LinkedHashMap<String, String> tris = new LinkedHashMap<>();
        tris.put(ElasticDossier.CONSETAT_NUMERO_ISA, "desc");
        tris.put(ElasticDossier.DOS_NUMERO_NOR, "asc");
        tris.put(ElasticDossier.CONSETAT_DATE_AG_CE, "asc");
        searchCriteriaRapide.setTris(tris);
        try {
            request = mapper.from(searchCriteriaRapide, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException | URISyntaxException e) {
            Assert.fail();
        }

        Assert.assertNotNull(request);

        Assert.assertEquals(10, request.source().size());
        Assert.assertEquals(0, request.source().from());
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, request.source().docValueFields().get(0).field);
        Assert.assertEquals(ISA_SORT, request.source().sorts().get(0).toString());
        Assert.assertEquals(SortOrder.DESC, request.source().sorts().get(0).order());
        Assert.assertEquals(DEFAULT_SORT, request.source().sorts().get(1).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(1).order());
        Assert.assertEquals(AG_CE_SORT, request.source().sorts().get(2).toString());
        Assert.assertEquals(SortOrder.ASC, request.source().sorts().get(2).order());

        BoolQueryBuilder rootQuery = (BoolQueryBuilder) request.source().query();
        Assert.assertEquals(1, Integer.parseInt(rootQuery.minimumShouldMatch()));
        List<QueryBuilder> should = rootQuery.should();
        Assert.assertEquals(1, should.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) should.get(0);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, wildcard.fieldName());
        Assert.assertEquals("CCO*200", wildcard.value());
    }

    @Test
    public void testColonnes() {
        SearchRequest request = null;
        SearchCriteriaRapide searchCriteriaRapide = createRapideCriteria("CCO*200");
        HashSet<String> colonnes = new HashSet<>(
            Arrays.asList(
                ElasticDossier.CONSETAT_NUMERO_ISA,
                ElasticDossier.DOS_NUMERO_NOR,
                ElasticDossier.CONSETAT_DATE_AG_CE
            )
        );
        searchCriteriaRapide.setColonnes(colonnes);
        try {
            request = mapper.from(searchCriteriaRapide, INDEX_DOSSIERS);
        } catch (NuxeoException | ElasticException | URISyntaxException e) {
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
        Assert.assertEquals(1, Integer.parseInt(rootQuery.minimumShouldMatch()));
        List<QueryBuilder> should = rootQuery.should();
        Assert.assertEquals(1, should.size());

        WildcardQueryBuilder wildcard = (WildcardQueryBuilder) should.get(0);
        Assert.assertEquals(ElasticDossier.DOS_NUMERO_NOR, wildcard.fieldName());
        Assert.assertEquals("CCO*200", wildcard.value());
    }

    @Test(expected = ElasticException.class)
    public void testColonnesNonRemontable() throws ElasticException {
        SearchCriteriaRapide searchCriteriaRapide = createRapideCriteria("CCO*200");
        searchCriteriaRapide.setColonnes(Collections.singleton(ElasticDossier.CONSETAT_RECEPTION_AVIS_CE));
        try {
            mapper.from(searchCriteriaRapide, INDEX_DOSSIERS);
        } catch (NuxeoException | URISyntaxException e) {
            Assert.fail();
        }
    }

    private SearchCriteriaRapide createRapideCriteria(String wildcards) {
        SearchCriteriaRapide searchCriteriaRapide = new SearchCriteriaRapide();
        searchCriteriaRapide.setPage(1);
        searchCriteriaRapide.setPageSize(10);
        searchCriteriaRapide.setWildcardList(SearchRequestUtils.generateWildcardList(wildcards));
        return searchCriteriaRapide;
    }
}
