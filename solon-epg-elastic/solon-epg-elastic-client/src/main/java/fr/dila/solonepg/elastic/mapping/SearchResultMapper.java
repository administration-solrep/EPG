package fr.dila.solonepg.elastic.mapping;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

public class SearchResultMapper implements ISearchResultMapper {

    @Override
    public SearchResult from(SearchResponse reponse) {
        SearchResult result = new SearchResult();
        if (reponse != null) {
            SearchHits elasticHits = reponse.getHits();
            if (elasticHits != null) {
                TotalHits totalHits = elasticHits.getTotalHits();
                result.setCount(totalHits.value);
                Arrays.stream(elasticHits.getHits()).map(this::toElasticDossier).forEach(result.getResults()::add);
            }

            // Ajout des facettes
            if (reponse.getAggregations() != null) {
                addFacetResult(result, FacetEnum.CATEGORIE_ACTE, reponse.getAggregations());
                addFacetResult(result, FacetEnum.DIRECTION_ATTACHE, reponse.getAggregations());
                addFacetResult(result, FacetEnum.MINISTERE_ATTACHE, reponse.getAggregations());
                addFacetResult(result, FacetEnum.STATUT, reponse.getAggregations());
                addFacetResult(result, FacetEnum.STATUT_ARCHIVAGE, reponse.getAggregations());
                addFacetResult(result, FacetEnum.TYPE_ACTE, reponse.getAggregations());
                addFacetResult(result, FacetEnum.VECTEUR_PUBLICATION, reponse.getAggregations());
            }
        }

        return result;
    }

    private void addFacetResult(SearchResult result, FacetEnum facet, Aggregations aggregations) {
        ParsedStringTerms parsedStringTerms = aggregations.get(facet.getFacetName());
        if (result != null && parsedStringTerms != null) {
            result.addFacetEntry(facet.toString(), parsedStringTerms);
        }
    }

    private ElasticDossier toElasticDossier(SearchHit esHit) {
        ElasticDossier dossier = new ElasticDossier();
        dossier.setUid(esHit.getId());

        String numeroNor = esHit.getFields().get(ElasticDossier.DOS_NUMERO_NOR).getValue();
        dossier.setDosNumeroNor(StringUtils.upperCase(numeroNor));

        STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();

        DocumentField dosMinistereRespField = esHit.getFields().get(ElasticDossier.DOS_MINISTERE_RESP);
        if (dosMinistereRespField != null) {
            dossier.setDosMinistereResp(getMinistereLabel(stMinisteresService, dosMinistereRespField.getValue()));
        }

        DocumentField dosMinistereAttacheField = esHit.getFields().get(ElasticDossier.DOS_MINISTERE_ATTACHE);
        if (dosMinistereAttacheField != null) {
            dossier.setDosMinistereAttache(getMinistereLabel(stMinisteresService, dosMinistereAttacheField.getValue()));
        }

        DocumentField dateParutionField = esHit.getFields().get(ElasticDossier.RETDILA_DATE_PARUTION_JORF);
        if (dateParutionField != null) {
            String dateParution = dateParutionField.getValue();
            if (StringUtils.isNotEmpty(dateParution)) {
                dossier.setRetdilaDateParutionJorf(
                    SolonDateConverter.DATETIME_DASH_REVERSE_T_MILLI_Z.parseToDate(dateParution)
                );
            }
        }

        DocumentField doscreatedField = esHit.getFields().get(ElasticDossier.DOS_CREATED);
        if (doscreatedField != null) {
            String dosCreatedValue = doscreatedField.getValue();
            if (StringUtils.isNotEmpty(dosCreatedValue)) {
                dossier.setDosCreated(SolonDateConverter.DATETIME_DASH_REVERSE_T_MILLI_Z.parseToDate(dosCreatedValue));
            }
        }

        DocumentField titreField = esHit.getFields().get(ElasticDossier.DOS_TITRE_ACTE);
        if (titreField != null) {
            dossier.setDosTitreActe(titreField.getValue());
        }

        DocumentField prenomRespDossierField = esHit.getFields().get(ElasticDossier.DOS_PRENOM_RESP_DOSSIER);
        if (prenomRespDossierField != null) {
            dossier.setDosPrenomRespDossier(prenomRespDossierField.getValue());
        }

        DocumentField nomRespDossierField = esHit.getFields().get(ElasticDossier.DOS_NOM_RESP_DOSSIER);
        if (nomRespDossierField != null) {
            dossier.setDosNomRespDossier(nomRespDossierField.getValue());
        }

        DocumentField statusDocumentField = esHit.getFields().get(ElasticDossier.DOS_STATUT);
        if (statusDocumentField != null) {
            dossier.setDosStatut(statusDocumentField.getValue());
        }

        Map<String, HighlightField> highlight = esHit.getHighlightFields();

        // Titre acte
        if (
            highlight != null &&
            highlight.get(ElasticDossier.DC_TITLE) != null &&
            highlight.get(ElasticDossier.DC_TITLE).getFragments().length > 0
        ) {
            dossier.setDosTitreActe(highlight.get(ElasticDossier.DC_TITLE).getFragments()[0].string());
        }

        return dossier;
    }

    private String getMinistereLabel(STMinisteresService ministeresService, String ministereId) {
        return Optional
            .ofNullable(ministeresService.getEntiteNode(ministereId))
            .map(EntiteNode::getLabel)
            .orElse(ministereId);
    }
}
