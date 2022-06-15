package fr.dila.nifi.solon.services;

import static org.apache.nifi.elasticsearch.ElasticSearchClientService.CHARSET;
import static org.apache.nifi.elasticsearch.ElasticSearchClientService.CONNECT_TIMEOUT;
import static org.apache.nifi.elasticsearch.ElasticSearchClientService.HTTP_HOSTS;
import static org.apache.nifi.elasticsearch.ElasticSearchClientService.SOCKET_TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.nifi.elasticsearch.IndexOperationRequest;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.LogMessage;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Test;
import org.mockito.Mockito;

public class ElasticSearchClientDilaServiceImplTest {

    private static final String ES_SERVICE_ID = "esService";
    public static final AbstractProcessor processor = Mockito.spy(AbstractProcessor.class);

    @Test
    public void should_pass_a_list_of_host_for_es_client_call_many_add_host() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, "https://host1:9200,http://host2:9201,http://host3:9202");
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        final String expectedLogMessages =
            "{} New HttpHost for client load balancing with scheme [{}], hostname [{}] and port [{}].";
        final List<Object[]> debugLogs = runner
            .getControllerServiceLogger(ES_SERVICE_ID)
            .getDebugMessages()
            .stream()
            .filter(message -> expectedLogMessages.equals(message.getMsg()))
            .map(LogMessage::getArgs)
            .collect(Collectors.toList());
        assertThat(debugLogs)
            .as("We should find the 3 hosts in the debug logs")
            .extracting(log -> log[1], log -> log[2], log -> log[3])
            .containsExactlyInAnyOrder(
                tuple("https", "host1", 9200),
                tuple("http", "host2", 9201),
                tuple("http", "host3", 9202)
            );
    }

    @Test
    public void should_pass_one_host_for_es_client_call_once_add_host() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, "http://host1:9200");
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        final String expectedLogMessages =
            "{} New HttpHost for client load balancing with scheme [{}], hostname [{}] and port [{}].";
        final List<Object[]> debugLogs = runner
            .getControllerServiceLogger(ES_SERVICE_ID)
            .getDebugMessages()
            .stream()
            .filter(message -> expectedLogMessages.equals(message.getMsg()))
            .map(LogMessage::getArgs)
            .collect(Collectors.toList());
        assertThat(debugLogs)
            .as("We should find only on host in the debug log")
            .flatExtracting(log -> log[1], log -> log[2], log -> log[3])
            .containsExactly("http", "host1", 9200);
    }

    @Test
    public void should_pass_invalid_host_throw_initialization_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, "invalid@host");
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        AssertionError throwable = catchThrowableOfType(
            () -> runner.enableControllerService(service),
            AssertionError.class
        );
        assertThat(throwable).isNotNull().hasMessageContaining("org.apache.nifi.reporting.InitializationException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_index_operation_without_index_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        final IndexOperationRequest request = new IndexOperationRequest(
            "",
            "doc",
            "id",
            getFieldsForIndexation(),
            null
        );
        service.add(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_index_operation_without_id_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        final IndexOperationRequest request = new IndexOperationRequest(
            "index",
            "doc",
            "",
            getFieldsForIndexation(),
            null
        );
        service.add(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_index_operation_without_fields_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        final IndexOperationRequest request = new IndexOperationRequest("index", "doc", "id", new HashMap<>(), null);
        service.add(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_delete_by_query_without_index_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.deleteByQuery("query", "", "type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_delete_by_query_without_query_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.deleteByQuery("", "index", "type");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_bulk_add_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.bulk(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_delete_by_id_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.deleteById("", "", "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_bulk_delete_by_id_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.deleteById("", "", new ArrayList<>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_get_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.get("", "", "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void should_search_throw_exception() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        service.search("", "", "");
    }

    @Test
    public void should_transit_url_return_valid_url() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(processor);
        final ElasticSearchClientDilaServiceImpl service = new ElasticSearchClientDilaServiceImpl();
        runner.addControllerService(ES_SERVICE_ID, service);
        runner.setProperty(service, HTTP_HOSTS, "http://host1:9200");
        runner.setProperty(service, CHARSET, CHARSET.getDefaultValue());
        runner.setProperty(service, CONNECT_TIMEOUT, CONNECT_TIMEOUT.getDefaultValue());
        runner.setProperty(service, SOCKET_TIMEOUT, SOCKET_TIMEOUT.getDefaultValue());
        runner.assertValid(service);
        runner.enableControllerService(service);
        assertThat(service.getTransitUrl("index", "type"))
            .as("should have valid transit url")
            .isEqualTo("http://host1:9200/index/type");
        assertThat(service.getTransitUrl("index", ""))
            .as("should have valid transit url")
            .isEqualTo("http://host1:9200/index");
        assertThat(service.getTransitUrl("", "type"))
            .as("should have valid transit url")
            .isEqualTo("http://host1:9200/type");
    }

    private Map<String, Object> getFieldsForIndexation() {
        final Map<String, Object> fields = new HashMap<>();
        fields.put("my_field1", "value1");
        return fields;
    }
}
