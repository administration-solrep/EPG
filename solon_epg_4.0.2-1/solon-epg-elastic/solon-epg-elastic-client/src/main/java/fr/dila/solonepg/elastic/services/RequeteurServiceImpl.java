package fr.dila.solonepg.elastic.services;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.elastic.mapping.ISearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.ISearchResultMapper;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapperExp;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapperRapide;
import fr.dila.solonepg.elastic.mapping.SearchResultMapper;
import fr.dila.solonepg.elastic.mapping.SearchResultMapperExp;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.search.AbstractCriteria;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaRapide;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.models.search.SearchResultExp;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.service.STServiceLocator;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class RequeteurServiceImpl implements RequeteurService {
    private static final String HOSTS = STServiceLocator
        .getConfigService()
        .getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_HOSTS, StringUtils.EMPTY);
    private static final Integer HOST_THREAD_COUNT = STServiceLocator
        .getConfigService()
        .getIntegerValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_IO_THREAD_COUNT);
    private static final Integer HOST_CONN_PER_ROUTE = STServiceLocator
        .getConfigService()
        .getIntegerValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_CONN_PER_ROUTE);
    private static final Integer HOST_CON_TOTAL = STServiceLocator
        .getConfigService()
        .getIntegerValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_CON_TOTAL);
    private static final String INDEX_DOCUMENTS = STServiceLocator
        .getConfigService()
        .getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_INDEX_DOCUMENTS, "documents");
    private static final String INDEX_DOSSIERS = STServiceLocator
        .getConfigService()
        .getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_INDEX_DOSSIERS, "dossiers");

    // buffer limit is 150MB
    private static final int BUFFER_LIMIT = 150 * 1024 * 1024;

    private final RestHighLevelClient httpClient;
    private final RequestOptions requestOptions;

    public RequeteurServiceImpl() {
        final String[] hostsSplit = HOSTS.split(",[\\s]*");
        final HttpHost[] httpHosts = Stream.of(hostsSplit).map(this::createHttpHost).toArray(HttpHost[]::new);
        httpClient =
            new RestHighLevelClient(
                RestClient
                    .builder(httpHosts)
                    .setHttpClientConfigCallback(
                        httpAsyncClientBuilder ->
                            httpAsyncClientBuilder
                                .setDefaultIOReactorConfig(
                                    IOReactorConfig.custom().setIoThreadCount(HOST_THREAD_COUNT).build()
                                )
                                .setMaxConnPerRoute(HOST_CONN_PER_ROUTE)
                                .setMaxConnTotal(HOST_CON_TOTAL)
                    )
            );
        RequestOptions.Builder requestOptionsBuilder = RequestOptions.DEFAULT.toBuilder();
        requestOptionsBuilder.setHttpAsyncResponseConsumerFactory(
            new HeapBufferedResponseConsumerFactory(BUFFER_LIMIT)
        );
        requestOptions = requestOptionsBuilder.build();
    }

    @Override
    public String getIndexDocuments() {
        return INDEX_DOCUMENTS;
    }

    @Override
    public String getIndexDossiers() {
        return INDEX_DOSSIERS;
    }

    @Override
    public SearchResult getResults(RechercheLibre criteria, CoreSession session) throws IOException, ElasticException {
        return getResults(criteria, session, true);
    }

    @Override
    public SearchResult getResultsWithoutHighlight(RechercheLibre criteria, CoreSession session)
        throws IOException, ElasticException {
        return getResults(criteria, session, false);
    }

    private SearchResult getResults(RechercheLibre criteria, CoreSession session, boolean highlightDocuments)
        throws IOException, ElasticException {
        ISearchRequestMapper searchRequestMapper = new SearchRequestMapper();

        SearchRequest requestData = getSearchRequest(criteria, session, searchRequestMapper);

        // Requete 1 : data
        SearchResponse elasticSearchResponse = httpClient.search(requestData, requestOptions);

        ISearchResultMapper searchResultMapper = new SearchResultMapper();
        SearchResult searchResult = searchResultMapper.from(elasticSearchResponse);

        // Requete 2 : highlighting des documents (si full text)
        if (highlightDocuments && StringUtils.isNotEmpty(criteria.getFulltext())) {
            SearchHits elasticHits = elasticSearchResponse.getHits();
            if (elasticHits != null) {
                List<SearchHit> dossierHits = Arrays.asList(elasticHits.getHits());

                for (int i = 0; i < dossierHits.size(); i++) {
                    SearchHit dossierHit = dossierHits.get(i);

                    ElasticDossier elasticDossier = searchResult.getResults().get(i);

                    highlightDossierTitreActe(dossierHit, elasticDossier);

                    highlightDocuments(criteria, searchRequestMapper, dossierHit, elasticDossier, session);
                }
            }
        }

        return searchResult;
    }

    @Override
    public long getCountResults(RechercheLibre criteria, CoreSession session) throws IOException {
        return getCountResults(getSearchRequest(criteria, session, new SearchRequestMapper()));
    }

    @Override
    public String getESJsonQuery(RechercheLibre criteria, CoreSession session) {
        return getSearchRequest(criteria, session, new SearchRequestMapper()).source().toString();
    }

    private SearchRequest getSearchRequest(
        RechercheLibre criteria,
        CoreSession session,
        ISearchRequestMapper searchRequestMapper
    ) {
        setUserRightsCriteria(criteria, session);

        return searchRequestMapper.from(criteria, session, INDEX_DOSSIERS);
    }

    @Override
    public SearchResult getResults(String query, CoreSession session) {
        throw new UnsupportedOperationException(
            "La méthode getResults, basée sur une requête JSON n'est, pour l'heure, pas implémentée"
        );
    }

    @Override
    public SearchResultExp getResults(SearchCriteriaExp criteriaExp, CoreSession session)
        throws IOException, ElasticException, URISyntaxException {
        return getResults(criteriaExp, session, false);
    }

    @Override
    public SearchResultExp getResults(SearchCriteriaExp criteriaExp, CoreSession session, boolean isExport)
        throws IOException, ElasticException, URISyntaxException {
        SearchRequestMapperExp searchRequestMapper = new SearchRequestMapperExp();

        setUserRightsCriteria(criteriaExp, session);

        SearchRequest requestData = searchRequestMapper.from(criteriaExp, INDEX_DOSSIERS);

        setSourceForExport(isExport, requestData);

        // Requete 1 : data
        SearchResponse reponseData = httpClient.search(requestData, requestOptions);

        SearchResultMapperExp searchResultMapper = new SearchResultMapperExp();

        return searchResultMapper.from(reponseData);
    }

    @Override
    public long getCountResults(SearchCriteriaExp criteriaExp, CoreSession session)
        throws IOException, URISyntaxException {
        SearchRequestMapperExp searchRequestMapper = new SearchRequestMapperExp();

        setUserRightsCriteria(criteriaExp, session);

        return getCountResults(searchRequestMapper.from(criteriaExp, INDEX_DOSSIERS));
    }

    private long getCountResults(SearchRequest requestData) throws IOException {
        SearchResponse elasticSearchResponse = httpClient.search(requestData, requestOptions);

        return Optional
            .ofNullable(elasticSearchResponse.getHits().getTotalHits())
            .map(totalHits -> totalHits.value)
            .orElse(0L);
    }

    @Override
    public SearchResultExp getResults(SearchCriteriaRapide criteriaRapide, CoreSession session, boolean isExport)
        throws IOException, ElasticException, URISyntaxException {
        SearchRequestMapperRapide searchRequestMapper = new SearchRequestMapperRapide();

        setUserRightsCriteria(criteriaRapide, session);

        SearchRequest requestData = searchRequestMapper.from(criteriaRapide, INDEX_DOSSIERS);

        setSourceForExport(isExport, requestData);

        // Requete 1 : data
        SearchResponse reponseData = httpClient.search(requestData, requestOptions);

        SearchResultMapperExp searchResultMapper = new SearchResultMapperExp();

        return searchResultMapper.from(reponseData);
    }

    @Override
    public SearchResultExp getResults(SearchCriteriaRapide criteriaRapide, CoreSession session)
        throws IOException, ElasticException, URISyntaxException {
        return getResults(criteriaRapide, session, false);
    }

    @Override
    public long getCountResults(SearchCriteriaRapide criteriaRapide, CoreSession session)
        throws IOException, URISyntaxException {
        SearchRequestMapperRapide searchRequestMapper = new SearchRequestMapperRapide();

        setUserRightsCriteria(criteriaRapide, session);

        return getCountResults(searchRequestMapper.from(criteriaRapide, INDEX_DOSSIERS));
    }

    private void setUserRightsCriteria(AbstractCriteria criteria, CoreSession session) {
        // On consolide la requête avec les droits de l'utilisateur
        if (session != null) {
            final NuxeoPrincipal currentUser = session.getPrincipal();
            criteria.setPermsUtilisateur(getUserPermsAsString((SSPrincipal) currentUser));

            // Si l'utilisateur n'a pas le droit de voir les mesures
            // nominatives, on met une restriction
            criteria.setHasDroitsNomination(getUserDroitsNomination((SSPrincipal) currentUser));
        }
    }

    @Override
    public SearchResponse getResults(SearchRequest searchRequest) throws IOException {
        return httpClient.search(searchRequest, requestOptions);
    }

    @SuppressWarnings("unchecked")
    private void highlightDocuments(
        RechercheLibre criteria,
        ISearchRequestMapper searchRequestMapper,
        SearchHit dossierHit,
        ElasticDossier elasticDossier,
        CoreSession session
    )
        throws IOException {
        ArrayList<ElasticDocument> elasticDocuments = new ArrayList<>();

        for (SearchHit elasticInnerHit : dossierHit.getInnerHits().get(ElasticDossier.DOCUMENTS).getHits()) {
            Map<String, DocumentField> fields = elasticInnerHit.getFields();

            // Requete documents sur chacun des ids pour récupérer les
            // highlights sur le doc
            if (fields != null && fields.get(ElasticDossier.DOCUMENTS_UID) != null) {
                List<String> docIds = (List<String>) (List<?>) fields.get(ElasticDossier.DOCUMENTS_UID).getValues();
                for (String docId : docIds) {
                    SearchRequest requestDocument = searchRequestMapper.from(
                        docId,
                        criteria.getFulltext(),
                        session,
                        INDEX_DOCUMENTS
                    );
                    SearchResponse responseDoc = httpClient.search(requestDocument, requestOptions);

                    // si requête pendant une mise à jour de l'index, le
                    // document peut être manquant
                    if (
                        responseDoc.getHits() != null &&
                        responseDoc.getHits().getHits() != null &&
                        responseDoc.getHits().getHits().length != 0
                    ) {
                        SearchHit hit = responseDoc.getHits().getHits()[0];
                        Map<String, HighlightField> highlight = hit.getHighlightFields();
                        String title = hit.getFields().get(ElasticDocument.DC_TITLE).getValue();
                        Integer majorVersion = hit.getFields().get(ElasticDocument.UID_MAJOR_VERSION).getValue();
                        Integer minorVersion = hit.getFields().get(ElasticDocument.UID_MINOR_VERSION).getValue();

                        ElasticDocument elasticDocument = new ElasticDocument();
                        elasticDocument.setDcTitle(title);
                        elasticDocument.setUidMajorVersion(majorVersion.longValue());
                        elasticDocument.setUidMinorVersion(minorVersion.longValue());

                        // l'hilight peut être absent
                        if (highlight != null) {
                            addHighlight(highlight, elasticDocument);
                            elasticDocuments.add(elasticDocument);
                        }
                    }
                }
            }

            elasticDossier.setDocuments(elasticDocuments);
        }
    }

    private void addHighlight(Map<String, HighlightField> highlight, ElasticDocument elasticDocument) {
        HighlightField dcTitle = highlight.get(ElasticDocument.DC_TITLE);
        if (dcTitle != null) {
            List<Text> titles = Arrays.asList(dcTitle.getFragments());
            if (CollectionUtils.isNotEmpty(titles)) {
                // On remplace le titre avec la version highlightée
                elasticDocument.setDcTitle(titles.get(0).toString());
            }
        }

        HighlightField fileData = highlight.get(ElasticDocument.FILE_DATA);
        if (fileData != null) {
            List<Text> datas = Arrays.asList(fileData.getFragments());
            if (CollectionUtils.isNotEmpty(datas)) {
                elasticDocument.setFileData(datas.get(0).toString());
            }
        }
    }

    private void highlightDossierTitreActe(SearchHit dossierHit, ElasticDossier elasticDossier) {
        Map<String, HighlightField> dossierHighlight = dossierHit.getHighlightFields();
        if (dossierHighlight != null) {
            HighlightField highlightField = dossierHighlight.get(ElasticDossier.DOS_TITRE_ACTE);
            if (highlightField != null && ArrayUtils.isNotEmpty(highlightField.getFragments())) {
                elasticDossier.setDosTitreActe(highlightField.getFragments()[0].toString());
            }
        }
    }

    private Collection<String> getUserPermsAsString(SSPrincipal currentUser) {
        return currentUser.getAllGroups();
    }

    private boolean getUserDroitsNomination(SSPrincipal currentUser) {
        return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
    }

    private HttpHost createHttpHost(final String host) {
        URL url;
        try {
            url = new URL(host);
            return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        } catch (MalformedURLException e) {
            throw new NuxeoException(String.format("L'URL %s du serveur elasticsearch est malformée", host), e);
        }
    }

    @Override
    public void deleteDocument(String uid) throws IOException {
        DeleteByQueryRequest requestData = new DeleteByQueryRequest();

        requestData.setQuery(QueryBuilders.matchQuery("_id", uid)).indices("_all");

        httpClient.deleteByQuery(requestData, requestOptions);
    }

    private void setSourceForExport(boolean isExport, SearchRequest requestData) {
        // S'il s'agit d'un export de dossiers, on ne ramnène que les champs utilisés dans les exports
        if (isExport) {
            SearchSourceBuilder sourceBuilder = requestData.source();
            String[] includeFields = new String[] {
                ElasticDossier.DOS_NUMERO_NOR,
                ElasticDossier.DOS_TITRE_ACTE,
                ElasticDossier.DOS_CREATED,
                ElasticDossier.DC_LAST_CONTRIBUTOR,
                ElasticDossier.DOS_STATUT,
                ElasticDossier.DOS_ID_CREATEUR_DOSSIER,
                ElasticDossier.DOS_TYPE_ACTE,
                ElasticDossier.DOS_MINISTERE_RESP,
                ElasticDossier.DOS_MINISTERE_ATTACHE,
                ElasticDossier.DOS_PRENOM_RESP_DOSSIER,
                ElasticDossier.DOS_NOM_RESP_DOSSIER
            };
            sourceBuilder.fetchSource(includeFields, null);
            requestData.source(sourceBuilder);
        }
    }
}
