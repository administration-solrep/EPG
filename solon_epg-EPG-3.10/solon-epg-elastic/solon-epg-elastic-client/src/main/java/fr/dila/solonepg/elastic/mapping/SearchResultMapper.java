package fr.dila.solonepg.elastic.mapping;

import java.util.Iterator;
import java.util.List;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticHighlight;
import fr.dila.solonepg.elastic.models.ElasticHit;
import fr.dila.solonepg.elastic.models.ElasticHits;
import fr.dila.solonepg.elastic.models.FacetResult.FacetResultWrap;
import fr.dila.solonepg.elastic.models.FacetResultEntry;
import fr.dila.solonepg.elastic.models.search.FacetEnum;
import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.st.core.util.CollectionUtil;

public class SearchResultMapper implements ISearchResultMapper {

	@Override
	public SearchResult from(SearchResponse reponse) {
		SearchResult result = new SearchResult();

		if (reponse != null) {
			ElasticHits elasticHits = reponse.getHits();
			if (elasticHits != null) {
				result.setCount(elasticHits.getTotal());

				for (ElasticHit dossierHit : elasticHits.getHits()) {
					ElasticDossier elasticDossier = new ElasticDossier(dossierHit.getFields());

					ElasticHighlight highlight = dossierHit.getHighlight();

					// Titre acte
					if (highlight != null && CollectionUtil.isNotEmpty(highlight.getTitle())) {
						elasticDossier.setDosTitreActe(highlight.getTitle().get(0));
					}

					result.getResults().add(elasticDossier);
				}
			}

			// Ajout des facettes
			if (reponse.getAggregations() != null) {
				addFacetResult(result, FacetEnum.CATEGORIE_ACTE, reponse.getAggregations().getCategorie_acte());
				addFacetResult(result, FacetEnum.RESPONSABLE, reponse.getAggregations().getReponsable());
				addFacetResult(result, FacetEnum.STATUT, reponse.getAggregations().getStatut());
				addFacetResult(result, FacetEnum.VECTEUR_PUBLICATION,
						reponse.getAggregations().getVecteur_publication());
				addFacetResult(result, FacetEnum.TYPE_ACTE, reponse.getAggregations().getType_acte());
				addFacetResult(result, FacetEnum.STATUT_ARCHIVAGE, reponse.getAggregations().getStatutArchivage());
			}
		}

		return result;
	}

	private void addFacetResult(SearchResult result, FacetEnum facet, FacetResultWrap facetResult) {
		if (result != null && facetResult != null) {
			List<FacetResultEntry> facetEntries = facetResult.getBuckets();
			// Pour le statut d'archivage, si des statuts équivalents à initial sont présents,
			// ils sont remplacés par le premier statut équivalent à initial
			if (facet.equals(FacetEnum.STATUT_ARCHIVAGE) && facetEntries != null && !facetEntries.isEmpty()) {
				Long totalDocCount = 0L;
				Iterator<FacetResultEntry> i = facetEntries.iterator();
				while (i.hasNext()) {
					FacetResultEntry facetEntry = (FacetResultEntry) i.next();
					if (VocabularyConstants.STATUT_ARCHIVAGE_INITIAL_FACET.contains(facetEntry.getKey())) {
						totalDocCount += facetEntry.getDoc_count();
						i.remove();
					}
				}
				if (totalDocCount > 0) {
					FacetResultEntry initialFacetEntry = new FacetResultEntry();
					initialFacetEntry.setKey(VocabularyConstants.STATUT_ARCHIVAGE_INITIAL_FACET.get(1));
					initialFacetEntry.setDoc_count(totalDocCount);
					facetEntries.add(0, initialFacetEntry);
				}
			}
			result.addFacetEntries(facet.toString(), facetEntries);
		}
	}
}
