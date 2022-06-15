package fr.dila.solonepg.elastic.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.SearchResultExp;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import java.util.Arrays;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.nuxeo.ecm.core.api.NuxeoException;

public class SearchResultMapperExp {
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final STLogger LOGGER = STLogFactory.getLog(SearchRequestMapperExp.class);

    public SearchResultExp from(SearchResponse reponse) {
        SearchResultExp result = new SearchResultExp();
        if (reponse != null) {
            if (reponse.getFailedShards() > 0) {
                LOGGER.error(EpgLogEnumImpl.FAIL_ELASTIC_SEARCH_TEC, reponse.getShardFailures()[0].getCause());
            }

            SearchHits elasticHits = reponse.getHits();
            if (elasticHits != null) {
                TotalHits totalHits = elasticHits.getTotalHits();
                result.setCount(totalHits.value);
                result.setNumResults(reponse.getHits().getHits().length);
                Arrays
                    .stream(elasticHits.getHits())
                    .map(this::toElasticDossier)
                    .forEach(d -> result.getResults().put(d.getDosNumeroNor(), d));
            }
        }
        return result;
    }

    private ElasticDossier toElasticDossier(SearchHit hit) {
        String sourceString = hit.getSourceAsString();
        try {
            return objectMapper.readValue(sourceString, ElasticDossier.class);
        } catch (JsonProcessingException e) {
            // UnrecognizedPropertyException : Le dossier présent dans elasticsearch contient des champs avec des propriétés non défini dans ElasticDossier.
            // Vérifiez que des propriétés non pas changés. sinon relancez une indexation complète.
            throw new NuxeoException(e);
        }
    }
}
