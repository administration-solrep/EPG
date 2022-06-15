package fr.dila.solonepg.elastic.mapping;

import static fr.dila.solonepg.elastic.utils.SearchRequestUtils.initQuery;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.config.QueryBuilderProperty;
import fr.dila.solonepg.elastic.models.config.QueryBuildersPropertiesConfiguration;
import fr.dila.solonepg.elastic.models.search.ClauseEt;
import fr.dila.solonepg.elastic.models.search.ClauseOu;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.models.search.enums.OperatorQueryEnum;
import fr.dila.solonepg.elastic.models.search.enums.QueryEnum;
import fr.dila.solonepg.elastic.models.search.enums.TypeEnum;
import fr.dila.solonepg.elastic.utils.SearchRequestUtils;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.IntervalQueryBuilder;
import org.elasticsearch.index.query.IntervalsSourceProvider;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

public class SearchRequestMapperExp {
    private static final int MAX_GAPE = 10;
    private final QueryBuildersPropertiesConfiguration properties = QueryBuildersPropertiesConfiguration.getInstance();

    public SearchRequestMapperExp() throws URISyntaxException {}

    /**
     * Transpose l'objet criteria en une requête ElasticSearch
     *
     * @param criteria L'objet contenant les différents critères de recherche et l'arborescence de la requête
     * @return la recherche elasticsearch à effectuer
     * @throws ElasticException Erreur lors de la construction de la requête
     */
    public SearchRequest from(SearchCriteriaExp criteria, String index) {
        Integer pageSize = criteria.getPageSize();
        int from = criteria.getFrom();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.from(from);
        sourceBuilder.size(pageSize);
        SearchRequest request = new SearchRequest();
        request.source(sourceBuilder);

        if (criteria.getColonnes() != null) {
            SearchRequestUtils.addColonnes(sourceBuilder, criteria.getColonnes(), properties);
        }

        if (CollectionUtils.isEmpty(criteria.getColonnes())) {
            sourceBuilder.docValueField(ElasticDossier.DOS_NUMERO_NOR);
        }

        initQuery(request);

        addMustOfShould(new ArrayList<>(criteria.getClausesOu()), (BoolQueryBuilder) request.source().query());

        if (MapUtils.isEmpty(criteria.getTris())) {
            request.source().sort(ElasticDossier.DOS_NUMERO_NOR, SortOrder.ASC);
        } else {
            SearchRequestUtils.addTri(criteria.getTris(), properties, request, sourceBuilder);
        }

        SearchRequestUtils.addPerms(criteria, request);
        SearchRequestUtils.addDroitsNomination(criteria, request);

        return request.indices(index);
    }

    /**
     * Transpose une liste d'objets ClauseOu en requête elastic et ajoute l'ensemble en must à la requête en paramètre
     * @param clausesOus la liste d'objets à transposer
     * @param query la requête dans laquelle on veut ajouter un must de l'ensemble des should
     */
    private void addMustOfShould(List<ClauseOu> clausesOus, BoolQueryBuilder query) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        for (ClauseOu clauseOu : clausesOus) {
            addShould(clauseOu, boolQuery);
        }

        query.must(boolQuery);
    }

    /**
     * Transpose l'objet ClauseOu en requête elastic et l'ajoute en should à la requête en paramêtre
     *
     * @param clauseOu l'objet à transposer
     * @param query    la requête dans laquelle on veut ajouter un should
     */
    private void addShould(ClauseOu clauseOu, BoolQueryBuilder query) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        Map<String, List<ClauseEt>> clauseEtByNestedPath = clauseOu
            .getClausesEt()
            .stream()
            .collect(Collectors.groupingBy(ClauseEt::getNestedPath));
        clauseEtByNestedPath.forEach((nestedPath, clauses) -> addMust(nestedPath, clauses, boolQuery));

        query.should(boolQuery);
    }

    private void addMust(String nestedPath, List<ClauseEt> clausesEt, BoolQueryBuilder boolQuery)
        throws ElasticException {
        BoolQueryBuilder usedBoolQuery;

        if (nestedPath.isEmpty()) {
            usedBoolQuery = boolQuery;
        } else {
            usedBoolQuery = new BoolQueryBuilder();
            boolQuery.must(createNestedQuery(nestedPath, usedBoolQuery));
        }

        clausesEt.forEach(clause -> addMust(clause, usedBoolQuery));
    }

    private QueryBuilder createNestedQuery(String nestedPath, QueryBuilder subQueryBuilder) {
        if (StringUtils.isNotEmpty(nestedPath)) {
            return new NestedQueryBuilder(nestedPath, subQueryBuilder, ScoreMode.Avg);
        }

        return subQueryBuilder;
    }

    /**
     * Transpose l'objet ClauseEt en requête elastic et l'ajoute en must à la requête en paramêtre
     *
     * @param clauseEt  l'objet à transposer
     * @param boolQuery la requête dans laquelle on veut ajouter un must
     * @throws ElasticException Erreur lors de la construction de la requête
     */
    private void addMust(ClauseEt clauseEt, BoolQueryBuilder boolQuery) throws ElasticException {
        QueryBuilderProperty property = properties.getProperty(clauseEt.getField(), clauseEt.getNestedPath());
        checkQueryType(clauseEt, property);
        OperatorQueryEnum operatorQuery = OperatorQueryEnum.transposeOperator(
            clauseEt.getOperator(),
            property.getQueryType()
        );

        if (property.getType().equals(TypeEnum.KEYWORD)) {
            createQueryForKeyword(clauseEt, operatorQuery, boolQuery);
        } else {
            createQuery(clauseEt, operatorQuery, boolQuery);
        }
    }

    /**
     * Ecrit une requête must avec le contenu de ClauseEt en fonction de l'opérateur passé en paramêtre et l'ajoute à la
     * requete passée en paramêtres
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createQuery(ClauseEt clauseEt, OperatorQueryEnum operatorQuery, BoolQueryBuilder boolQuery) {
        switch (operatorQuery.getQuery()) {
            case EXIST:
                createExistsQuery(operatorQuery, clauseEt, boolQuery);
                break;
            case MATCH:
            case MATCH_AND:
                createMatchQuery(operatorQuery, clauseEt, boolQuery);
                break;
            case RANGE:
                createRangeQuery(operatorQuery, clauseEt, boolQuery);
                break;
            case TERMS:
                createTermsQuery(operatorQuery, clauseEt, boolQuery);
                break;
            case INTERVALS:
                createIntervalQuery(clauseEt, boolQuery);
                break;
            case MATCH_PHRASE:
                createMatchPhraseQuery(operatorQuery, clauseEt, boolQuery);
                break;
            default:
                break;
        }
    }

    private void addSubQuery(BoolQueryBuilder boolQuery, OperatorQueryEnum operatorQuery, QueryBuilder subQuery) {
        if (operatorQuery.isNegative()) {
            boolQuery.mustNot(subQuery);
        } else {
            boolQuery.must(subQuery);
        }
    }

    /**
     * Ecrit une requête must pour les keyword avec le contenu de ClauseEt en fonction de l'opérateur passé
     * en paramêtre et l'ajoute à la requete passée en paramêtres.
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createQueryForKeyword(ClauseEt clauseEt, OperatorQueryEnum operatorQuery, BoolQueryBuilder boolQuery) {
        if (operatorQuery.getKeyword() == QueryEnum.NONE) {
            createQuery(clauseEt, operatorQuery, boolQuery);
        } else if (operatorQuery.getKeyword() == QueryEnum.TERM) {
            QueryBuilder termQuery = new TermQueryBuilder(clauseEt.getFieldPath(), clauseEt.getValeur());
            addSubQuery(boolQuery, operatorQuery, termQuery);
        } else if (operatorQuery.getKeyword() == QueryEnum.WILDCARD) {
            QueryBuilder termQuery = new WildcardQueryBuilder(clauseEt.getFieldPath(), clauseEt.getValeur());
            addSubQuery(boolQuery, operatorQuery, termQuery);
        }
    }

    /**
     * Ecrit une requête MatchPhrase en fonction de l'opérateur et l'ajoute en must dans la boolquery
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createMatchPhraseQuery(
        OperatorQueryEnum operatorQuery,
        ClauseEt clauseEt,
        BoolQueryBuilder boolQuery
    ) {
        QueryBuilder matchPhraseQueryBuilder = new MatchPhraseQueryBuilder(
            clauseEt.getFieldPath(),
            clauseEt.getValeur()
        );
        addSubQuery(boolQuery, operatorQuery, matchPhraseQueryBuilder);
    }

    /**
     * Ecrit une requête Match en fonction de l'opérateur et l'ajoute en must dans la boolquery
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createMatchQuery(OperatorQueryEnum operatorQuery, ClauseEt clauseEt, BoolQueryBuilder boolQuery) {
        MatchQueryBuilder matchQuery = new MatchQueryBuilder(clauseEt.getFieldPath(), clauseEt.getValeur());

        if (operatorQuery.getQuery() == QueryEnum.MATCH_AND) {
            matchQuery.operator(Operator.AND);
        }

        addSubQuery(boolQuery, operatorQuery, matchQuery);
    }

    /**
     * Ecrit une requête Exists en fonction de l'opérateur et l'ajoute en must dans la boolquery
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createExistsQuery(OperatorQueryEnum operatorQuery, ClauseEt clauseEt, BoolQueryBuilder boolQuery) {
        QueryBuilder existQueryBuilder = new ExistsQueryBuilder(clauseEt.getFieldPath());
        addSubQuery(boolQuery, operatorQuery, existQueryBuilder);
    }

    /**
     * Ecrit une requête terms en fonction de l'opérateur et l'ajoute en must dans la boolquery
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createTermsQuery(OperatorQueryEnum operatorQuery, ClauseEt clauseEt, BoolQueryBuilder boolQuery) {
        if (StringUtils.isNotEmpty(clauseEt.getValeur()) && StringUtils.isNotBlank(clauseEt.getValeur())) {
            TermQueryBuilder termQuery = new TermQueryBuilder(clauseEt.getFieldPath(), clauseEt.getValeur());
            addSubQuery(boolQuery, operatorQuery, termQuery);
        } else if (CollectionUtils.isNotEmpty(clauseEt.getListeValeur())) {
            TermsQueryBuilder termsQuery = new TermsQueryBuilder(clauseEt.getFieldPath(), clauseEt.getListeValeur());
            addSubQuery(boolQuery, operatorQuery, termsQuery);
        }
    }

    /**
     * Ecrit une requête Range en fonction de l'opérateur et l'ajoute en must dans la boolquery
     *
     * @param clauseEt      l'objet à transposer
     * @param operatorQuery L'opérateur à écrire
     * @param boolQuery     La requête à enrichir
     */
    private void createRangeQuery(OperatorQueryEnum operatorQuery, ClauseEt clauseEt, BoolQueryBuilder boolQuery) {
        RangeQueryBuilder rangeQuery = new RangeQueryBuilder(clauseEt.getFieldPath());

        if (clauseEt.getValeur() != null) {
            if (NumberUtils.isCreatable(clauseEt.getValeur())) {
                clauseEt.setValeur(convertRelativeDate(clauseEt.getValeur()));
            }

            switch (clauseEt.getOperator()) {
                case PLUS_GRAND:
                    rangeQuery.gt(clauseEt.getValeur());
                    break;
                case PLUS_GRAND_OU_EGAL:
                    rangeQuery.gte(clauseEt.getValeur());
                    break;
                case PLUS_PETIT:
                    rangeQuery.lt(clauseEt.getValeur());
                    break;
                case PLUS_PETIT_OU_EGAL:
                    rangeQuery.lte(clauseEt.getValeur());
                    break;
                default:
                    break;
            }
        } else {
            if (NumberUtils.isCreatable(clauseEt.getValeurMin())) {
                clauseEt.setValeurMin(convertRelativeDate(clauseEt.getValeurMin()));
            }

            if (NumberUtils.isCreatable(clauseEt.getValeurMax())) {
                clauseEt.setValeurMax(convertRelativeDate(clauseEt.getValeurMax()));
            }
            rangeQuery.gte(clauseEt.getValeurMin()).lte(clauseEt.getValeurMax());
        }

        addSubQuery(boolQuery, operatorQuery, rangeQuery);
    }

    /**
     * Ecrit une requête interval et l'ajoute en must dans la boolquery
     *
     * @param clauseEt  l'objet à transposer
     * @param boolQuery La requête à enrichir
     */
    private void createIntervalQuery(ClauseEt clauseEt, BoolQueryBuilder boolQuery) {
        IntervalsSourceProvider sourceProvider = new IntervalsSourceProvider.Match(
            clauseEt.getValeur(),
            MAX_GAPE,
            true,
            null,
            null,
            null
        );
        QueryBuilder intervalQuery = new IntervalQueryBuilder(clauseEt.getFieldPath(), sourceProvider);
        boolQuery.must(intervalQuery);
    }

    private void checkQueryType(ClauseEt clauseEt, QueryBuilderProperty property) throws ElasticException {
        if (!property.getQueryType().getPossibleQueries().contains(clauseEt.getOperator())) {
            throw new ElasticException("Opérateur non valide pour ce champs");
        }
    }

    private String convertRelativeDate(String date) {
        int relativeValue = Integer.parseInt(date);
        Calendar vCal = Calendar.getInstance();
        vCal.add(Calendar.DAY_OF_YEAR, relativeValue);
        return new SimpleDateFormat("yyyy-MM-dd").format(vCal.getTime());
    }
}
