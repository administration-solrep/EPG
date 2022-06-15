package fr.dila.nifi.solon.services;

import static org.apache.nifi.elasticsearch.ElasticSearchClientService.CHARSET;
import static org.apache.nifi.elasticsearch.ElasticSearchClientService.CONNECT_TIMEOUT;
import static org.apache.nifi.elasticsearch.ElasticSearchClientService.HTTP_HOSTS;
import static org.apache.nifi.elasticsearch.ElasticSearchClientService.SOCKET_TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.nifi.solon.services.domain.HttpMethod;
import fr.dila.nifi.solon.services.domain.IndexVersionedOperationRequest;
import fr.dila.nifi.solon.services.exception.ElasticsearchClientException;
import fr.dila.nifi.solon.services.exception.ElasticsearchRetryableClientException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.nifi.elasticsearch.DeleteOperationResponse;
import org.apache.nifi.elasticsearch.IndexOperationRequest;
import org.apache.nifi.elasticsearch.IndexOperationResponse;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticSearchClientDilaServiceImplIT {

    private static final String INVALID_ES_HOST_URL = "http://i-am-not-an-es-node:9200";

    private static final String ES_SERVICE_ID = "esService";

    public static final AbstractProcessor processor = Mockito.spy(AbstractProcessor.class);

    public static final List<String> inteEsHosts;

    public static final String indexName = "nifi-integration-test-index";

    public static RestClient testClient;

    public static final UUID document1Id = UUID.randomUUID();
    public static final UUID document2Id = UUID.randomUUID();

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        final List<String> inteEsHostsInitilization = new ArrayList<>();
        Socket serverSocket = null;

        try {
            serverSocket = new Socket("idlv-solon2ng-demo-epg-elasticsearch-01.lyon-dev2.local", 9200);
            inteEsHostsInitilization.add("http://idlv-solon2ng-demo-epg-elasticsearch-01.lyon-dev2.local:9200");
            inteEsHostsInitilization.add("http://idlv-solon2ng-demo-epg-elasticsearch-02.lyon-dev2.local:9200");
            inteEsHostsInitilization.add("http://idlv-solon2ng-demo-epg-elasticsearch-03.lyon-dev2.local:9200");
            serverSocket.close();
        } catch (Exception e) {
            inteEsHostsInitilization.add("http://idlv-solon2ng-inte-epg-elasticsearch-01.lyon-dev2.local:9200");
            inteEsHostsInitilization.add("http://idlv-solon2ng-inte-epg-elasticsearch-02.lyon-dev2.local:9200");
            inteEsHostsInitilization.add("http://idlv-solon2ng-inte-epg-elasticsearch-03.lyon-dev2.local:9200");
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        inteEsHosts = Collections.unmodifiableList(inteEsHostsInitilization);
    }

    @BeforeClass
    public static void createIndexForTest() throws IOException {
        testClient = createTestClient();
        final String content = "{\"settings\":{\"index.number_of_shards\":1,\"index.number_of_replicas\":0}}";
        final Request request = new Request(HttpMethod.PUT.toString(), String.format("/%s", indexName));
        request.setJsonEntity(content);
        testClient.performRequest(request);
    }

    @AfterClass
    public static void deleteIndexForTest() throws IOException {
        final Request request = new Request(HttpMethod.DELETE.toString(), String.format("/%s", indexName));
        testClient.performRequest(request);
    }

    @Test(expected = ElasticsearchRetryableClientException.class)
    public void should_index_operation_with_unavailable_node_return_retryable_error() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, INVALID_ES_HOST_URL);
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        service.add(
            new IndexOperationRequest(indexName, null, document1Id.toString(), getFieldsForIndexationOfDoc1(), null)
        );
    }

    @Test(expected = ElasticsearchRetryableClientException.class)
    public void should_delete_by_query_operation_with_unavailable_node_return_retryable_error()
        throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, INVALID_ES_HOST_URL);
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        service.deleteByQuery("{\"query\":{\"term\":{\"my_field1.keyword\":\"value1\"}}}", indexName, null);
    }

    @Test(expected = ElasticsearchClientException.class)
    public void should_delete_by_query_integration_with_not_valid_document_return_exception()
        throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, inteEsHosts.stream().collect(Collectors.joining(",")));
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        service.deleteByQuery("not a json", indexName, null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_01_should_index_operation_with_valid_document_return_success()
        throws InitializationException, IOException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, inteEsHosts.stream().collect(Collectors.joining(",")));
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        final IndexOperationResponse responseDoc1 = service.add(
            new IndexOperationRequest(indexName, null, document1Id.toString(), getFieldsForIndexationOfDoc1(), null)
        );
        assertThat(responseDoc1.getTook()).as("Should have the took in response").isGreaterThan(0l);
        final IndexOperationResponse responseDoc2 = service.add(
            new IndexOperationRequest(indexName, null, document2Id.toString(), getFieldsForIndexationOfDoc2(), null)
        );
        assertThat(responseDoc2.getTook()).as("Should have the took in response").isGreaterThan(0l);
        final Map<String, Object> documents = searchDocumentsInIndex();
        final Map<String, Object> hits = (Map<String, Object>) documents.get("hits");
        final Map<String, Object> hitsTotal = (Map<String, Object>) hits.get("total");
        assertThat(hitsTotal)
            .as("Should find strictly 2 documents")
            .hasFieldOrPropertyWithValue("value", 2)
            .hasFieldOrPropertyWithValue("relation", "eq");
        final List<Map<String, Object>> docHits = (List<Map<String, Object>>) hits.get("hits");
        final List<String> expectedIds = Arrays.asList(document1Id.toString(), document2Id.toString());
        final List<String> actualIds = docHits
            .stream()
            .map(hit -> hit.get("_id").toString())
            .collect(Collectors.toList());
        assertThat(actualIds).as("Should documents have correct ids").isEqualTo(expectedIds);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_02_should_delete_by_query_integration_with_valid_document_return_success()
        throws InitializationException, IOException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, inteEsHosts.stream().collect(Collectors.joining(",")));
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        final DeleteOperationResponse response = service.deleteByQuery(
            "{\"query\":{\"term\":{\"my_field1.keyword\":\"doc1_value1\"}}}",
            indexName,
            null
        );
        assertThat(response.getTook()).as("Should have the took in response").isGreaterThan(0l);
        final Map<String, Object> documents = searchDocumentsInIndex();
        final Map<String, Object> hits = (Map<String, Object>) documents.get("hits");
        final Map<String, Object> hitsTotal = (Map<String, Object>) hits.get("total");
        assertThat(hitsTotal)
            .as("Should find strictly 1 documents")
            .hasFieldOrPropertyWithValue("value", 1)
            .hasFieldOrPropertyWithValue("relation", "eq");
        final List<Map<String, Object>> docHits = (List<Map<String, Object>>) hits.get("hits");
        final List<String> expectedIds = Arrays.asList(document2Id.toString());
        final List<String> actualIds = docHits
            .stream()
            .map(hit -> hit.get("_id").toString())
            .collect(Collectors.toList());
        assertThat(actualIds).as("Should documents have correct ids").isEqualTo(expectedIds);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_03_should_index_versionned_operation_with_valid_document_return_success()
        throws InitializationException, IOException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, StringUtils.join(inteEsHosts, ","));
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        long expextedVersion = new Date().getTime();
        final IndexOperationResponse responseDoc1 = service.add(
            new IndexVersionedOperationRequest(
                indexName,
                null,
                document1Id.toString(),
                expextedVersion,
                getFieldsForIndexationOfDoc1()
            )
        );
        assertThat(responseDoc1.getTook()).as("Should have the took in response").isGreaterThan(0l);

        final Map<String, Object> documents = searchDocumentsInIndex(true);
        final Map<String, Object> hits = (Map<String, Object>) documents.get("hits");

        final List<Map<String, Object>> docHits = (List<Map<String, Object>>) hits.get("hits");
        final String version = docHits
            .stream()
            .filter(hit -> hit.get("_id").equals(document1Id.toString()))
            .map(hit -> hit.get("_version").toString())
            .findFirst()
            .get();
        assertThat(version).isEqualTo(String.valueOf(expextedVersion));
    }

    private Map<String, Object> getFieldsForIndexationOfDoc1() {
        final Map<String, Object> subFields = new HashMap<>();
        subFields.put("my_subfield1", "doc1_subvalue1");
        subFields.put("my_subfield2", "doc1_subvalue2");
        final Map<String, Object> fields = new HashMap<>();
        fields.put("my_field1", "doc1_value1");
        fields.put("my_field2", subFields);
        return fields;
    }

    private Map<String, Object> getFieldsForIndexationOfDoc2() {
        final Map<String, Object> subFields = new HashMap<>();
        subFields.put("my_subfield1", "doc2_subvalue1");
        subFields.put("my_subfield2", "doc2_subvalue2");
        final Map<String, Object> fields = new HashMap<>();
        fields.put("my_field1", "doc2_value1");
        fields.put("my_field2", subFields);
        return fields;
    }

    private static RestClient createTestClient() {
        final HttpHost[] hosts = inteEsHosts
            .stream()
            .map(
                host -> {
                    try {
                        return new URL(host);
                    } catch (final MalformedURLException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            )
            .map(url -> new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
            .toArray(HttpHost[]::new);
        return RestClient.builder(hosts).build();
    }

    private Map<String, Object> searchDocumentsInIndex() throws IOException {
        return this.searchDocumentsInIndex(false);
    }

    private Map<String, Object> searchDocumentsInIndex(boolean includeVersion) throws IOException {
        final Request refresh = new Request(HttpMethod.POST.toString(), String.format("/%s/_refresh", indexName));
        testClient.performRequest(refresh);
        final Request search = new Request(HttpMethod.GET.toString(), String.format("/%s/_search", indexName));
        if (includeVersion) {
            search.addParameter("version", Boolean.TRUE.toString());
        }
        final Response searchResponse = testClient.performRequest(search);
        final byte[] result;
        try (InputStream inputStream = searchResponse.getEntity().getContent()) {
            result = IOUtils.toByteArray(inputStream);
        }
        return mapper.readValue(new String(result), new TypeReference<Map<String, Object>>() {});
    }
}
