package fr.dila.solonepg.elastic.mapping;

import static fr.dila.solonepg.elastic.utils.SearchRequestUtils.initQuery;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.config.QueryBuildersPropertiesConfiguration;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaRapide;
import fr.dila.solonepg.elastic.utils.SearchRequestUtils;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

public class SearchRequestMapperRapide {
    private final QueryBuildersPropertiesConfiguration properties = QueryBuildersPropertiesConfiguration.getInstance();

    public SearchRequestMapperRapide() throws URISyntaxException {
        // Default constructor
    }

    /**
     * Transpose l'objet criteria en un requête ElasticSearch
     *
     * @param criteria L'objet contenant les différents critères de recherche et l'arborescence de la requête
     * @return la recherche elasticsearch à effectuer
     * @throws ElasticException   Erreur lors de la construction de la requête
     * @throws URISyntaxException Erreur lors de la lecture du fichier de propriétés
     */
    public SearchRequest from(SearchCriteriaRapide criteria, String index) throws ElasticException, URISyntaxException {
        Integer page = criteria.getPage();
        Integer pageSize = criteria.getPageSize();

        int from = (page - 1) * pageSize;

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.from(from);
        sourceBuilder.size(pageSize);
        SearchRequest request = new SearchRequest();
        request.source(sourceBuilder);
        if (criteria.getColonnes() != null) {
            List<String> storedFields = new ArrayList<>();
            for (String colonne : criteria.getColonnes()) {
                if (properties.getProperty(colonne).isStored()) {
                    storedFields.add(colonne);
                } else if (properties.getProperty(colonne).isReturnable()) {
                    if (properties.getProperty(colonne).isHasKeyword()) {
                        sourceBuilder.docValueField(colonne + ".keyword");
                    } else {
                        sourceBuilder.docValueField(colonne);
                    }
                } else {
                    throw new ElasticException("La colonne suivante ne peut pas être remontée " + colonne);
                }
            }
            sourceBuilder.storedFields(storedFields);
        }

        if (CollectionUtils.isEmpty(criteria.getColonnes())) {
            sourceBuilder.docValueField(ElasticDossier.DOS_NUMERO_NOR);
        }

        initQuery(request);

        createQueryForWildcard(criteria.getWildcardList(), (BoolQueryBuilder) request.source().query());

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
     * Ecrit une requête Should pour les critères de recherche contenant un wildcard
     *
     * @param wildcards Liste des valeurs pouvant contenir un wildcard
     * @param boolQuery La requête à enrichir
     */
    public static void createQueryForWildcard(List<String> wildcards, BoolQueryBuilder boolQuery) {
        boolQuery.minimumShouldMatch(1);
        for (String wildcardValue : wildcards) {
            WildcardQueryBuilder wildcardQuery;

            if (wildcardValue.startsWith("*") && !wildcardValue.endsWith("*")) {
                wildcardQuery = new WildcardQueryBuilder(ElasticDossier.DOS_NUMERO_NOR + ".reverse", wildcardValue);
            } else {
                wildcardQuery = new WildcardQueryBuilder(ElasticDossier.DOS_NUMERO_NOR, wildcardValue);
            }
            boolQuery.should(wildcardQuery);
        }
    }
}
