package fr.dila.solonepg.elastic.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.elastic.mapping.ISearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.ISearchResultMapper;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.SearchResultMapper;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticHighlight;
import fr.dila.solonepg.elastic.models.ElasticHit;
import fr.dila.solonepg.elastic.models.ElasticHits;
import fr.dila.solonepg.elastic.models.InnerHits.ElasticInnerHit;
import fr.dila.solonepg.elastic.models.InnerHits.InnerFields;
import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;
import fr.dila.solonepg.elastic.rest.ElasticHttpClient;
import fr.dila.solonepg.elastic.rest.IElasticHttpRestClient;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.core.util.CollectionUtil;
import fr.dila.st.core.util.StringUtil;

public class RequeteurServiceImpl implements RequeteurService {

	@Override
	public SearchResult getResults(SearchCriteria criteria, CoreSession session) throws JsonProcessingException,
			IOException, ClientException {
		ISearchRequestMapper searchRequestMapper = new SearchRequestMapper();

		// On consolide la requête avec les droits de l'utilisateur
		if (session != null) {
			final NuxeoPrincipal currentUser = (NuxeoPrincipal) session.getPrincipal();
			criteria.setPermsUtilisateur(getUserPermsAsString((SSPrincipal) currentUser));

			// SI l'utilisateur n'a pas le droit de voir les mesures nominatives, on mets une restriction
			criteria.setHasDroitsNomination(getUserDroitsNomination((SSPrincipal) currentUser));
		}

		SearchRequest requestData = searchRequestMapper.from(criteria, session);

		IElasticHttpRestClient httpClient = new ElasticHttpClient();

		// Requete 1 : data
		SearchResponse reponseData = httpClient.queryData(requestData);

		ISearchResultMapper searchResultMapper = new SearchResultMapper();
		SearchResult searchResult = searchResultMapper.from(reponseData);

		// Requete 2 : highlighting des documents (si full text)
		if (StringUtil.isNotEmpty(criteria.getFulltext())) {
			ElasticHits elasticHits = reponseData.getHits();
			if (elasticHits != null) {
				List<ElasticHit> dossierHits = elasticHits.getHits();

				for (int i = 0; i < dossierHits.size(); i++) {
					ElasticHit dossierHit = dossierHits.get(i);

					ElasticDossier elasticDossier = searchResult.getResults().get(i);

					highlightDossierTitreActe(dossierHit, elasticDossier);

					highlightDocuments(criteria, searchRequestMapper, httpClient, dossierHit, elasticDossier, session);
				}
			}
		}

		return searchResult;
	}

	@Override
	public SearchResponse getResults(SearchRequest searchRequest) throws JsonProcessingException, IOException {
		IElasticHttpRestClient httpClient = new ElasticHttpClient();

		// Requete 1 : data
		SearchResponse reponseData = httpClient.queryData(searchRequest);
		return reponseData;
	}

	private void highlightDocuments(SearchCriteria criteria, ISearchRequestMapper searchRequestMapper,
			IElasticHttpRestClient httpClient, ElasticHit dossierHit, ElasticDossier elasticDossier, CoreSession session)
			throws JsonProcessingException, IOException, ClientException {
		ArrayList<ElasticDocument> elasticDocuments = new ArrayList<ElasticDocument>();

		for (ElasticInnerHit elasticInnerHit : dossierHit.getInner_hits().getDocuments().getHits().getHits()) {
			InnerFields fields = elasticInnerHit.getFields();

			// Requete documents sur chacun des ids pour récupérer les
			// highlights sur le doc
			if (fields != null && fields.getIds() != null) {
				for (String docId : fields.getIds()) {
					SearchRequest requestDocument = searchRequestMapper.from(docId, criteria.getFulltext(), session);
					SearchResponse responseDoc = httpClient.queryDocuments(requestDocument);

					// si requête pendant une mise à jour de l'index, le document peut être manquant
					if (responseDoc.getHits() != null && responseDoc.getHits().getHits() != null
							&& !responseDoc.getHits().getHits().isEmpty()) {
						ElasticHit hit = responseDoc.getHits().getHits().get(0);
						ElasticHighlight highlight = hit.getHighlight();
						String title = hit.getFields().getDocTitle().get(0);
						long majorVersion = hit.getFields().getUidMajorVersion().get(0);
						long minorVersion = hit.getFields().getUidMinorVersion().get(0);

						ElasticDocument elasticDocument = new ElasticDocument();
						elasticDocument.setDcTitle(title);
						elasticDocument.setUidMajorVersion(majorVersion);
						elasticDocument.setUidMinorVersion(minorVersion);

						// l'hilight peut être absent
						if (highlight != null) {
							List<String> titles = highlight.getTitle();
							if (CollectionUtil.isNotEmpty(titles)) {
								// On remplace le titre avec la version highlightée
								elasticDocument.setDcTitle(titles.get(0));
							}

							List<String> datas = highlight.getData();
							if (CollectionUtil.isNotEmpty(datas)) {
								elasticDocument.setFileData(datas.get(0));
							}

							elasticDocuments.add(elasticDocument);
						}
					}
				}
			}

			elasticDossier.setDocuments(elasticDocuments);
		}
	}

	private void highlightDossierTitreActe(ElasticHit dossierHit, ElasticDossier elasticDossier) {
		ElasticHighlight dossierHighlight = dossierHit.getHighlight();
		if (dossierHighlight != null) {
			List<String> titreActe = dossierHighlight.getTitreacte();
			if (CollectionUtil.isNotEmpty(titreActe)) {
				elasticDossier.setDosTitreActe(titreActe.get(0));
			}
		}
	}

	private Collection<String> getUserPermsAsString(SSPrincipal currentUser) throws ClientException {
		Collection<String> perms = new ArrayList<String>();
		List<String> profilsAsString = currentUser.getAllGroups();

		if (getUserDroitVisibiliteTousDossiers(currentUser)) {
			perms.add(SolonEpgConstant.ES_DROITS_ADMINISTRATORS);
			// 22/10/2018 : on ne peut pas exploit ce droit directement, à cause de la casse. dans l'ideal, on prefererait l 'utiliser que "administrators"
//			perms.add(SolonEpgBaseFunctionConstant.DOSSIER_RECHERCHE_READER);
		} else if (profilsAsString.contains(STBaseFunctionConstant.ADMIN_MINISTERIEL_GROUP_NAME)) {
			perms.addAll(currentUser.getPosteIdSet());
			perms.addAll(currentUser.getMinistereIdSet());
		} else {
			perms.addAll(currentUser.getPosteIdSet());
		}
		return perms;
	}

	private boolean getUserDroitsNomination(SSPrincipal currentUser) throws ClientException {
		return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
	}
	
	// ce droit permet de voir tous les dossierds pendant la recherche
	private boolean getUserDroitVisibiliteTousDossiers(SSPrincipal currentUser) throws ClientException {
		return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_RECHERCHE_READER);
	}

}
