package fr.dila.solonepg.elastic.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.elastic.models.json.IndexationObjectMapperProviderUtil;
import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.service.STServiceLocator;

public class ElasticHttpClient implements IElasticHttpRestClient {
	private static final Log log = LogFactory.getLog(ElasticHttpClient.class);

	private ConfigService configService;

	private String hostUrl;
	private Integer hostPort;
	private String contextData;
	private String contextDocuments;

	public ElasticHttpClient() {
		// Initialisation des paramètres
		configService = STServiceLocator.getConfigService();

		initParams();
	}

	ElasticHttpClient(ConfigService configService) {
		this.configService = configService;

		initParams();
	}

	private void initParams() {
		hostUrl = configService.getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_HOST);
		hostPort = configService.getIntegerValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_PORT);
		contextData = configService.getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_CONTEXT_DATA);
		contextDocuments = configService.getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_CONTEXT_DOCUMENTS);
	}

	protected ObjectMapper getSearchRequestObjectMapper() {
		ObjectMapper mapper = IndexationObjectMapperProviderUtil.newElasticsearchQueryResponseInstance();
		return mapper;
	}

	protected ObjectMapper getSearchResponseObjectMapper() {
		ObjectMapper mapper = IndexationObjectMapperProviderUtil.newElasticsearchQueryResponseInstance();
		return mapper;
	}

	String buildUrlData() {
		return hostUrl + ":" + hostPort + "/" + contextData;
	}

	String buildUrlDocuments() {
		return hostUrl + ":" + hostPort + "/" + contextDocuments;
	}

	@Override
	public SearchResponse queryData(SearchRequest request) throws ClientProtocolException, IOException {
		return query(request, buildUrlData(), contextData);
	}

	@Override
	public SearchResponse queryDocuments(SearchRequest request) throws ClientProtocolException, IOException {
		return query(request, buildUrlDocuments(),contextDocuments);
	}

	private SearchResponse query(SearchRequest request, String url, String context) throws ClientProtocolException, IOException {
		String jsonRequest = getSearchRequestObjectMapper().writeValueAsString(request);
		
		StringBuilder logMsg = new StringBuilder("Requête ES : " + jsonRequest);

		String responseStr = query(jsonRequest, url);
		
		SearchResponse searchResponse = getSearchResponseObjectMapper().readValue(responseStr, SearchResponse.class);
		
		logMsg.append(" [context: " + context + "]").append(" [took: " + searchResponse.getTook() + " ms]");
		log.info(logMsg.toString());

		return searchResponse;
	}

	String query(String request, String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(request.getBytes()));

		httpPost.setEntity(reqEntity);

		CloseableHttpResponse response = httpClient.execute(httpPost);

		String responseStr = null;
		try {
			responseStr = EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
		}

		return responseStr;
	}
}
