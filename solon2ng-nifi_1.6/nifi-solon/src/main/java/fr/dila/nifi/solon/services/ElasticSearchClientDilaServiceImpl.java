package fr.dila.nifi.solon.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.nifi.solon.services.domain.HttpMethod;
import fr.dila.nifi.solon.services.domain.HttpResponseStatusFamily;
import fr.dila.nifi.solon.services.domain.IndexVersionedOperationRequest;
import fr.dila.nifi.solon.services.exception.ElasticsearchClientException;
import fr.dila.nifi.solon.services.exception.ElasticsearchRetryableClientException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.net.ssl.SSLContext;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.util.EntityUtils;
import org.apache.nifi.annotation.lifecycle.OnDisabled;
import org.apache.nifi.annotation.lifecycle.OnEnabled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.AbstractControllerService;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.elasticsearch.DeleteOperationResponse;
import org.apache.nifi.elasticsearch.ElasticSearchClientService;
import org.apache.nifi.elasticsearch.IndexOperationRequest;
import org.apache.nifi.elasticsearch.IndexOperationResponse;
import org.apache.nifi.elasticsearch.SearchResponse;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.ssl.SSLContextService;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

public class ElasticSearchClientDilaServiceImpl
    extends AbstractControllerService
    implements ElasticSearchClientDilaService {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final List<PropertyDescriptor> properties;

    private RestClient lowLevelClient;

    private Charset responseCharset;

    private String transitExampleUrl;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(ElasticSearchClientService.HTTP_HOSTS);
        props.add(ElasticSearchClientService.USERNAME);
        props.add(ElasticSearchClientService.PASSWORD);
        props.add(ElasticSearchClientService.PROP_SSL_CONTEXT_SERVICE);
        props.add(ElasticSearchClientService.CONNECT_TIMEOUT);
        props.add(ElasticSearchClientService.SOCKET_TIMEOUT);
        props.add(ElasticSearchClientService.CHARSET);

        properties = Collections.unmodifiableList(props);
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return properties;
    }

    @OnEnabled
    public void onEnabled(final ConfigurationContext context) throws InitializationException {
        try {
            setupClient(context);
            responseCharset = Charset.forName(context.getProperty(CHARSET).getValue());
        } catch (final Exception ex) {
            getLogger().error("Could not initialize ElasticSearch client.", ex);
            throw new InitializationException(ex);
        }
    }

    private void setupClient(final ConfigurationContext context) {
        final String hosts = context.getProperty(HTTP_HOSTS).evaluateAttributeExpressions().getValue();
        final String[] hostsSplit = hosts.split(",[\\s]*");
        if (ArrayUtils.isEmpty(hostsSplit)) {
            getLogger()
                .error(
                    "Error splitting hosts, the property [{}] does not contain a valid list of hosts [{}].",
                    ElasticSearchClientService.HTTP_HOSTS,
                    hosts
                );
            throw new ElasticsearchClientException("Invalid list of hosts.");
        }
        transitExampleUrl = hostsSplit[0];
        final SSLContextService sslService = context
            .getProperty(PROP_SSL_CONTEXT_SERVICE)
            .asControllerService(SSLContextService.class);
        final String username = context.getProperty(USERNAME).evaluateAttributeExpressions().getValue();
        final String password = context.getProperty(PASSWORD).evaluateAttributeExpressions().getValue();

        final Integer connectTimeout = context.getProperty(CONNECT_TIMEOUT).asInteger();
        final Integer readTimeout = context.getProperty(SOCKET_TIMEOUT).asInteger();

        final HttpHost[] httpHosts = Stream.of(hostsSplit).map(this::createHttpHost).toArray(HttpHost[]::new);
        final RestClientBuilder builder = RestClient
            .builder(httpHosts)
            .setHttpClientConfigCallback(
                httpClientBuilder -> {
                    createSslContext(sslService).ifPresent(httpClientBuilder::setSSLContext);

                    if (username != null && password != null) {
                        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(
                            AuthScope.ANY,
                            new UsernamePasswordCredentials(username, password)
                        );
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }

                    return httpClientBuilder;
                }
            )
            .setRequestConfigCallback(
                requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(connectTimeout);
                    requestConfigBuilder.setSocketTimeout(readTimeout);
                    return requestConfigBuilder;
                }
            );
        lowLevelClient = builder.build();
    }

    private HttpHost createHttpHost(final String host) {
        try {
            final URL url = new URL(host);
            getLogger()
                .debug(
                    "New HttpHost for client load balancing with scheme [{}], hostname [{}] and port [{}].",
                    url.getProtocol(),
                    url.getHost(),
                    url.getPort()
                );
            return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        } catch (final MalformedURLException e) {
            getLogger().error("the host [{}] is not a valid URL.", host);
            throw new ElasticsearchClientException("A host is invalid.");
        }
    }

    private Optional<SSLContext> createSslContext(final SSLContextService sslService) {
        try {
            return Optional
                .ofNullable(sslService)
                .filter(sslSvc -> sslSvc.isKeyStoreConfigured() || sslSvc.isTrustStoreConfigured())
                .map(SSLContextService::createContext);
        } catch (final ProcessException e) {
            getLogger().error("Error building up SSL Context from the supplied configuration.", e);
            throw new ElasticsearchClientException(e);
        }
    }

    @OnDisabled
    public void onDisabled() throws IOException {
        lowLevelClient.close();
        transitExampleUrl = null;
        responseCharset = null;
    }

    @Override
    public IndexOperationResponse add(final IndexOperationRequest operation) {
        return add(operation, null);
    }

    @Override
    public IndexOperationResponse add(IndexVersionedOperationRequest operation) throws IOException {
        return add(operation, operation.getVersion());
    }

    private IndexOperationResponse add(IndexOperationRequest operation, Long version) {
        getLogger().debug("start index operation [{}]", operation);
        if (StringUtils.isBlank(operation.getIndex())) {
            throw new IllegalArgumentException("The index name is mandatory.");
        }
        if (StringUtils.isBlank(operation.getId())) {
            throw new IllegalArgumentException("Indexing document without id is unsupported.");
        }
        if (MapUtils.isEmpty(operation.getFields())) {
            throw new IllegalArgumentException("The document to index can not be emty.");
        }
        if (StringUtils.isNotBlank(operation.getType())) {
            getLogger().warn("The document type is deprecated, your type [{}] will be ignored.", operation.getType());
        }
        final Request request = new Request(
            HttpMethod.PUT.toString(),
            String.format("%s/_doc/%s", operation.getIndex(), operation.getId())
        );
        if (version != null) {
            request.addParameter("version_type", "external_gte");
            request.addParameter("version", String.valueOf(version));
        }
        try {
            request.setJsonEntity(mapper.writeValueAsString(operation.getFields()));
        } catch (final IOException e) {
            getLogger()
                .error("Document [{}], unable to serialize document to JSON.", new Object[] { operation.getId() }, e);
            throw new ElasticsearchClientException("Unable to serialize document to JSON.", e);
        }
        try {
            final StopWatch watch = new StopWatch();
            watch.start();
            final Response indexationResponse = lowLevelClient.performRequest(request);
            watch.stop();
            handleIndexationResponse(indexationResponse, operation.getId());
            getLogger().debug("end index operation [{}], Elasticsearch took [{}]", operation, watch);
            return new IndexOperationResponse(watch.getTime());
        } catch (final ResponseException e) {
            getLogger().error("Document [{}], indexation failed on server.", new Object[] { operation.getId() }, e);
            throw handleResponseException(e, operation.getId());
        } catch (final IOException e) {
            getLogger()
                .error(
                    "Document [{}], indexation failed because none of the nodes is available.",
                    new Object[] { operation.getId() },
                    e
                );
            throw new ElasticsearchRetryableClientException(
                "Indexation failed because none of the nodes is available.",
                e
            );
        }
    }

    @Override
    public IndexOperationResponse bulk(final List<IndexOperationRequest> operations) {
        throw new UnsupportedOperationException("Bulk indexation is unsupported.");
    }

    @Override
    public Long count(String query, String index, String type) {
        throw new UnsupportedOperationException("Count is unsupported.");
    }

    @Override
    public DeleteOperationResponse deleteById(final String index, final String type, final String id) {
        throw new UnsupportedOperationException("Delete by id is unsupported.");
    }

    @Override
    public DeleteOperationResponse deleteById(final String index, final String type, final List<String> ids) {
        throw new UnsupportedOperationException("Delete by ids is unsupported.");
    }

    @Override
    public DeleteOperationResponse deleteByQuery(final String query, final String index, final String type) {
        getLogger().debug("start delete by query [{}] on index [{}] with type [{}]", query, index, type);
        if (StringUtils.isBlank(index)) {
            throw new IllegalArgumentException("The index name is mandatory.");
        }
        if (StringUtils.isBlank(query)) {
            throw new IllegalArgumentException("The query is mandatory.");
        }
        if (!StringUtils.isBlank(type)) {
            getLogger().warn("The document type is deprecated, your type [{}] will be ignored.", type);
        }
        final Request request = new Request(HttpMethod.POST.toString(), String.format("%s/_delete_by_query", index));
        request.setJsonEntity(query);
        try {
            final StopWatch watch = new StopWatch();
            watch.start();
            final Response deleteResponse = lowLevelClient.performRequest(request);
            watch.stop();
            handleDeleteByQueryResponse(deleteResponse);
            getLogger()
                .debug(
                    "start delete by query [{}] on index [{}] with type [{}], Elasticsearch took [{}]",
                    query,
                    index,
                    type,
                    watch
                );
            return new DeleteOperationResponse(watch.getTime());
        } catch (final ResponseException e) {
            getLogger().error("Delete by query failed on server.", e);
            throw handleResponseException(e);
        } catch (final IOException e) {
            getLogger().error("Delete by query failed because none of the nodes is available.", e);
            throw new ElasticsearchRetryableClientException(
                "Delete by query failed because none of the nodes is available.",
                e
            );
        }
    }

    @Override
    public Map<String, Object> get(final String index, final String type, final String id) {
        throw new UnsupportedOperationException("Get document by id is unsupported.");
    }

    @Override
    public SearchResponse search(final String query, final String index, final String type) {
        throw new UnsupportedOperationException("Search document is unsupported.");
    }

    @Override
    public String getTransitUrl(final String index, final String type) {
        StringBuilder transitUrlBuilder = new StringBuilder(transitExampleUrl);
        if (StringUtils.isNotBlank(index)) {
            transitUrlBuilder.append("/").append(index);
        }
        if (StringUtils.isNotBlank(type)) {
            transitUrlBuilder.append("/").append(type);
        }
        return transitUrlBuilder.toString();
    }

    private void handleIndexationResponse(final Response response, final String id) {
        final int statusCode = response.getStatusLine().getStatusCode();
        getLogger()
            .info("Document [{}], indexation was succesfully completed, returned status is [{}].", id, statusCode);
        if (getLogger().isDebugEnabled()) {
            try {
                final String entity = EntityUtils.toString(response.getEntity(), responseCharset);
                getLogger().debug("Document [{}], indexation response is [{}].", entity);
            } catch (ParseException | IllegalArgumentException | IOException e) {
                getLogger()
                    .warn("Document [{}] indexation is done but we can't read the response.", new Object[] { id }, e);
            }
        }
    }

    private void handleDeleteByQueryResponse(final Response response) {
        final int statusCode = response.getStatusLine().getStatusCode();
        getLogger().info("Delete by query was succesfully completed, returned status is [{}].", statusCode);
        if (getLogger().isDebugEnabled()) {
            try {
                final String entity = EntityUtils.toString(response.getEntity(), responseCharset);
                getLogger().debug("Delete by query response is [{}].", entity);
            } catch (ParseException | IllegalArgumentException | IOException e) {
                getLogger().warn("Delete by query is done but we can't read the response.", e);
            }
        }
    }

    private ElasticsearchClientException handleResponseException(final ResponseException exception) {
        final int statusCode = exception.getResponse().getStatusLine().getStatusCode();
        final Object[] logParams = new Object[] { statusCode };
        switch (HttpResponseStatusFamily.valueOf(statusCode)) {
            case CLIENT_ERROR:
                getLogger().error("Query rejected because it was invalid: status [{}].", logParams);
                return new ElasticsearchClientException(
                    "Your query is incorrect and should not be repeated.",
                    exception
                );
            case SERVER_ERROR:
                getLogger()
                    .error("Query rejected because the cluster is currently unavailable: status [{}].", logParams);
                return new ElasticsearchRetryableClientException("The cluster is unavailable, retry later.", exception);
            default:
                getLogger().error("Delete by query, unexpected error response: status [{}].", logParams);
                return new ElasticsearchClientException(
                    "Delete by query unexpected error response from the server, please not retry.",
                    exception
                );
        }
    }

    private ElasticsearchClientException handleResponseException(final ResponseException exception, final String id) {
        final int statusCode = exception.getResponse().getStatusLine().getStatusCode();
        final Object[] logParams = new Object[] { id, statusCode };
        switch (HttpResponseStatusFamily.valueOf(statusCode)) {
            case CLIENT_ERROR:
                getLogger().error("Document [{}], request rejected because it was invalid: status [{}].", logParams);
                throw new ElasticsearchClientException(
                    "Your request is incorrect and should not be repeated.",
                    exception
                );
            case SERVER_ERROR:
                final String msg =
                    "Document [{}], request rejected because the cluster is currently unavailable: status [{}].";
                getLogger().error(msg, logParams);
                throw new ElasticsearchRetryableClientException("The cluster is unavailable, retry later.", exception);
            default:
                getLogger().error("Document [{}], unexpected response: status [{}].", logParams);
                throw new ElasticsearchClientException(
                    "Unexpected response from the server, please not retry.",
                    exception
                );
        }
    }
}
