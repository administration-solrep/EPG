package fr.dila.solonepg.elastic.mapping;

import static com.google.common.base.Predicates.not;

import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.ElasticStep;
import fr.dila.solonepg.elastic.models.config.QueryBuildersPropertiesConfiguration;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.RechercheLibre.SortType;
import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import fr.dila.solonepg.elastic.utils.SearchRequestUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.nuxeo.ecm.core.api.CoreSession;

public class SearchRequestMapper implements ISearchRequestMapper {
    private static final int BUILDER_SIZE = 10000;
    private static final int AGGREGATION_SIZE_100 = 100;
    private static final int AGGREGATION_SIZE_10 = 10;
    private static final int FRAGMENT_SIZE_1500 = 1500;
    private static final int FRAGMENT_SIZE_500 = 500;
    private static final int FRAGMENT_SIZE_150 = 150;
    private final QueryBuildersPropertiesConfiguration properties = QueryBuildersPropertiesConfiguration.getInstance();

    private static final String MARK_PRE_TAG = "<mark class=\"highlight-text\">";
    private static final String MARK_POST_TAG = "</mark>";

    @Override
    public SearchRequest all(String index) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(BUILDER_SIZE);
        sourceBuilder.docValueField(ElasticDossier.UID);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);
        return searchRequest.indices(index);
    }

    @Override
    public SearchRequest from(RechercheLibre criteria, CoreSession session, String index) throws ElasticException {
        Integer page = criteria.getPage();
        Integer pageSize = criteria.getPageSize();

        int from = (page - 1) * pageSize;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.from(from);
        sourceBuilder.size(pageSize);
        if (CollectionUtils.isNotEmpty(criteria.getColonnes())) {
            SearchRequestUtils.addColonnes(sourceBuilder, criteria.getColonnes(), properties);
        } else {
            // Fonctionnement elastic
            // docvalue_fields : permet de récupérer les valeurs pour les types non analysés ; performance ++
            //                   activé par défaut pour tous les champs
            // stored_fields : permet de récupérer les valeurs même sur un champ analysé
            //                 nécessite d'être activé sur le champ
            List<String> storedFields = Collections.singletonList(ElasticDossier.DOS_TITRE_ACTE);
            List<String> docvalueFields = Arrays.asList(
                ElasticDossier.UID,/* pour le debugging */
                ElasticDossier.DOS_MINISTERE_RESP,
                ElasticDossier.DOS_STATUT,
                ElasticDossier.DOS_NUMERO_NOR,
                ElasticDossier.DOS_CREATED,
                ElasticDossier.RETDILA_DATE_PARUTION_JORF
                /* informations affichées */
            );

            sourceBuilder.storedFields(storedFields);
            for (String docvalueField : docvalueFields) {
                sourceBuilder.docValueField(docvalueField);
            }
        }

        SearchRequest request = new SearchRequest();
        request.source(sourceBuilder);

        SearchRequestUtils.initQuery(request);

        SearchRequestUtils.addVecteurPublication(criteria, request);
        SearchRequestUtils.addCategorieActe(criteria, request);
        SearchRequestUtils.addStatut(criteria, request);
        SearchRequestUtils.addDirectionAttache(criteria, request);
        SearchRequestUtils.addMinistereAttache(criteria, request);
        SearchRequestUtils.addTypeActe(criteria, request);
        SearchRequestUtils.addStatutArchivage(criteria, request);

        SearchRequestUtils.addDatesPublication(criteria, request);
        SearchRequestUtils.addDatesPublicationJo(criteria, request);
        SearchRequestUtils.addDatesCreation(criteria, request);
        SearchRequestUtils.addDatesSignature(criteria, request);

        SearchRequestUtils.addArchive(criteria, request);
        SearchRequestUtils.addPoste(criteria, request);

        addFulltext(criteria, request, session);

        addAggregations(request);

        addHighlight(request);

        addSort(criteria, request);

        SearchRequestUtils.addPerms(criteria, request);

        SearchRequestUtils.addDroitsNomination(criteria, request);

        return request.indices(index);
    }

    /**
     * @param request
     */
    private void addAggregations(SearchRequest request) {
        TermsAggregationBuilder agg0 = AggregationBuilders
            .terms(FacetEnum.MINISTERE_ATTACHE.getFacetName())
            .field(ElasticDossier.DOS_MINISTERE_ATTACHE)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(Arrays.asList(BucketOrder.count(false), BucketOrder.key(true)));

        TermsAggregationBuilder agg1 = AggregationBuilders
            .terms(FacetEnum.VECTEUR_PUBLICATION.getFacetName())
            .field(ElasticDossier.DOS_VECTEUR_PUBLICATION)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(Arrays.asList(BucketOrder.count(false), BucketOrder.key(true)));

        TermsAggregationBuilder agg2 = AggregationBuilders
            .terms(FacetEnum.STATUT.getFacetName())
            .field(ElasticDossier.DOS_STATUT)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        TermsAggregationBuilder agg3 = AggregationBuilders
            .terms(FacetEnum.CATEGORIE_ACTE.getFacetName())
            .field(ElasticDossier.DOS_CATEGORIE_ACTE)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        TermsAggregationBuilder agg4 = AggregationBuilders
            .terms(FacetEnum.TYPE_ACTE.getFacetName())
            .field(ElasticDossier.DOS_TYPE_ACTE)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        TermsAggregationBuilder agg5 = AggregationBuilders
            .terms(FacetEnum.STATUT_ARCHIVAGE.getFacetName())
            .field(ElasticDossier.DOS_STATUT_ARCHIVAGE)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        TermsAggregationBuilder agg6 = AggregationBuilders
            .terms(FacetEnum.DIRECTION_ATTACHE.getFacetName())
            .field(ElasticDossier.DOS_DIRECTION_ATTACHE)
            .size(AGGREGATION_SIZE_100)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(Arrays.asList(BucketOrder.count(false), BucketOrder.key(true)));

        TermsAggregationBuilder agg7 = AggregationBuilders
            .terms(FacetEnum.MAILBOX_DISTRIBUTION.getFacetName())
            .field(ElasticStep.RTSK_DISTRIBUTION_MAILBOX_ID)
            .size(AGGREGATION_SIZE_10)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        TermsAggregationBuilder agg8 = AggregationBuilders
            .terms(FacetEnum.DATE_SIGNATURE_DOSSIER.getFacetName())
            .field(ElasticDossier.DOS_DATE_SIGNATURE)
            .size(AGGREGATION_SIZE_10)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        TermsAggregationBuilder agg9 = AggregationBuilders
            .terms(FacetEnum.DATE_PARUTION_JORF.getFacetName())
            .field(ElasticDossier.RETDILA_DATE_PARUTION_JORF)
            .size(AGGREGATION_SIZE_10)
            .minDocCount(1)
            .shardMinDocCount(0)
            .showTermDocCountError(false)
            .order(BucketOrder.key(true));

        request.source().aggregation(agg0);
        request.source().aggregation(agg1);
        request.source().aggregation(agg2);
        request.source().aggregation(agg3);
        request.source().aggregation(agg4);
        request.source().aggregation(agg5);
        request.source().aggregation(agg6);
        request.source().aggregation(agg7);
        request.source().aggregation(agg8);
        request.source().aggregation(agg9);
    }

    private String escapeText(String toEscape) {
        return StringUtils.wrap(toEscape, "\"");
    }

    private void addFulltext(RechercheLibre criteria, SearchRequest request, CoreSession session) {
        String fulltext = criteria.getFulltext();

        if (StringUtils.isNotEmpty(fulltext)) {
            if (criteria.isExpressionExacte()) {
                fulltext = escapeText(fulltext);
            } else {
                String delimiter = " ";
                fulltext =
                    Arrays
                        .stream(fulltext.split(delimiter))
                        .filter(not(String::isEmpty))
                        .map(this::escapeText)
                        .collect(Collectors.joining(delimiter));
            }
            SimpleQueryStringBuilder simpleQueryStringBuilder = QueryBuilders
                .simpleQueryStringQuery(fulltext)
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(true);
            SearchRequestUtils.getDossierFullTextFieldsAndPonderation(session).forEach(simpleQueryStringBuilder::field);

            InnerHitBuilder innerHitBuilder = new InnerHitBuilder()
                .setIgnoreUnmapped(false)
                .setFrom(0)
                .setVersion(false)
                .setExplain(false)
                .setTrackScores(false)
                .addDocValueField(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID);

            BoolQueryBuilder boolQueryBuilderNested = QueryBuilders.boolQuery();
            SimpleQueryStringBuilder simpleQueryStringBuilderInner = QueryBuilders
                .simpleQueryStringQuery(fulltext)
                .defaultOperator(Operator.OR)
                .lenient(false)
                .analyzeWildcard(false);
            SearchRequestUtils.getDocumentFieldsAndPonderation(session).forEach(simpleQueryStringBuilderInner::field);
            boolQueryBuilderNested.must(simpleQueryStringBuilderInner);
            if (criteria.isDocCurrentVersion()) {
                boolQueryBuilderNested.must(
                    QueryBuilders.termQuery(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_CURRENTVERSION, true)
                );
            }

            NestedQueryBuilder nestedQueryBuilder = QueryBuilders
                .nestedQuery(ElasticDossier.DOCUMENTS, boolQueryBuilderNested, ScoreMode.Total)
                .innerHit(innerHitBuilder);

            BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) request.source().query();
            BoolQueryBuilder shouldQuery = QueryBuilders
                .boolQuery()
                .should(simpleQueryStringBuilder)
                .should(nestedQueryBuilder);
            SearchRequestMapperRapide.createQueryForWildcard(Arrays.asList(criteria.getFulltext()), shouldQuery);
            boolQueryBuilder.must(shouldQuery);
        }
    }

    private void addHighlight(SearchRequest request) {
        request
            .source()
            .highlighter(
                new HighlightBuilder()
                    .field(ElasticDossier.DOS_TITRE_ACTE, FRAGMENT_SIZE_1500, 1)
                    .field(ElasticDossier.RETDILA_NUMERO_TEXTE_PARUTION_JORF, FRAGMENT_SIZE_1500, 1)
                    .preTags(MARK_PRE_TAG)
                    .postTags(MARK_POST_TAG)
                    .requireFieldMatch(false)
            );
    }

    @Override
    public SearchRequest from(String documentId, String fulltext, CoreSession session, String index) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            .storedField(ElasticDocument.DC_TITLE)
            .docValueField(ElasticDocument.UID)
            .size(1);
        searchSourceBuilder.docValueField("uid:major_version");
        searchSourceBuilder.docValueField("uid:minor_version");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery(ElasticDocument.UID, documentId));
        SimpleQueryStringBuilder simpleQueryStringBuilder = QueryBuilders
            .simpleQueryStringQuery(fulltext)
            .defaultOperator(Operator.OR)
            .lenient(false)
            .analyzeWildcard(false);
        SearchRequestUtils.getDocumentFullTextFieldsAndPonderation().forEach(simpleQueryStringBuilder::field);
        boolQueryBuilder.should(simpleQueryStringBuilder);
        SearchRequest request = new SearchRequest();
        searchSourceBuilder.query(boolQueryBuilder);
        request.source(searchSourceBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder()
            .field(ElasticDocument.DC_TITLE, FRAGMENT_SIZE_500, 1)
            .preTags(MARK_PRE_TAG)
            .postTags(MARK_POST_TAG)
            .field(ElasticDocument.FILE_DATA, FRAGMENT_SIZE_150, 1)
            .preTags(MARK_PRE_TAG)
            .postTags(MARK_POST_TAG)
            .requireFieldMatch(false);

        searchSourceBuilder.highlighter(highlightBuilder);

        return request.indices(index);
    }

    private void addSort(RechercheLibre criteria, SearchRequest request) {
        SortType sortType = criteria.getSortType();
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort(sortType.getField()).order(sortType.getSortOrder());
        request.source().sort(sortBuilder);
    }
}
