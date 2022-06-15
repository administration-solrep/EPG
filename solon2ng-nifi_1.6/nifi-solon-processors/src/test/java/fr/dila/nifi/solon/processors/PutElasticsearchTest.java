package fr.dila.nifi.solon.processors;

import static fr.dila.nifi.solon.processors.PutElasticsearch.CLIENT_SERVICE;
import static fr.dila.nifi.solon.processors.PutElasticsearch.ERROR_ATTRIBUTE;
import static fr.dila.nifi.solon.processors.PutElasticsearch.ID_ATTRIBUTE;
import static fr.dila.nifi.solon.processors.PutElasticsearch.INDEX;
import static fr.dila.nifi.solon.processors.PutElasticsearch.REL_FAILURE;
import static fr.dila.nifi.solon.processors.PutElasticsearch.REL_RETRY;
import static fr.dila.nifi.solon.processors.PutElasticsearch.REL_SUCCESS;
import static fr.dila.nifi.solon.processors.PutElasticsearch.TOOK_ATTRIBUTE;
import static fr.dila.nifi.solon.processors.PutElasticsearch.VERSION_ATTRIBUTE;
import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.nifi.solon.services.ElasticSearchClientDilaService;
import fr.dila.nifi.solon.services.domain.IndexVersionedOperationRequest;
import fr.dila.nifi.solon.services.exception.ElasticsearchClientException;
import fr.dila.nifi.solon.services.exception.ElasticsearchRetryableClientException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.elasticsearch.IndexOperationRequest;
import org.apache.nifi.elasticsearch.IndexOperationResponse;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PutElasticsearchTest {

    private static final String DOCUMENT_JSON_VALID = "{\"key\":\"value\"}";

    private static final String INDEX_NAME = "index-test";

    private static final String DOCUMENT_ID_ATTRIBUTE = "document.id";

    private static final String DOCUMENT_VERSION_ATTRIBUTE = "document.version";

    private static final String ES_SERVICE_ID = "es-service";

    private static final String DOCUMENT_ID = UUID.randomUUID().toString();

    @Mock
    private ElasticSearchClientDilaService esService;

    private TestRunner runner;

    @Before
    public void init() throws InitializationException {
        Mockito.when(esService.getIdentifier()).thenReturn(ES_SERVICE_ID);
        runner = TestRunners.newTestRunner(PutElasticsearch.class);
        runner.addControllerService(ES_SERVICE_ID, esService);
        runner.enableControllerService(esService);
        runner.setProperty(CLIENT_SERVICE, ES_SERVICE_ID);
        runner.setProperty(INDEX, INDEX_NAME);
        runner.setProperty(ID_ATTRIBUTE, DOCUMENT_ID_ATTRIBUTE);
        runner.setProperty(VERSION_ATTRIBUTE, DOCUMENT_VERSION_ATTRIBUTE);
    }

    @Test
    public void testMandatoriesProperties() throws InitializationException {
        // without es service
        final TestRunner esServiceTestRunner = TestRunners.newTestRunner(PutElasticsearch.class);
        esServiceTestRunner.setProperty(INDEX, INDEX_NAME);
        esServiceTestRunner.setProperty(ID_ATTRIBUTE, DOCUMENT_ID_ATTRIBUTE);
        esServiceTestRunner.assertNotValid();
        // without index
        esServiceTestRunner.addControllerService(ES_SERVICE_ID, esService);
        esServiceTestRunner.enableControllerService(esService);
        esServiceTestRunner.setProperty(CLIENT_SERVICE, ES_SERVICE_ID);
        esServiceTestRunner.removeProperty(INDEX);
        esServiceTestRunner.assertNotValid();
        // with blank index
        esServiceTestRunner.setProperty(INDEX, " ");
        esServiceTestRunner.assertNotValid();
        // without id attribute
        esServiceTestRunner.setProperty(INDEX, INDEX_NAME);
        esServiceTestRunner.removeProperty(ID_ATTRIBUTE);
        esServiceTestRunner.assertNotValid();
        // with blank id attribute
        esServiceTestRunner.setProperty(ID_ATTRIBUTE, " ");
        esServiceTestRunner.assertNotValid();
        // with all properties set
        esServiceTestRunner.setProperty(ID_ATTRIBUTE, DOCUMENT_ID_ATTRIBUTE);
        esServiceTestRunner.assertValid();
    }

    @Test
    public void should_flowfile_without_document_id_goto_failure() {
        runner.enqueue(DOCUMENT_JSON_VALID);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertIdAttributeMissingError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_with_blank_document_id_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, "   ");
        runner.enqueue(DOCUMENT_JSON_VALID, attributes);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertIdAttributeMissingError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_without_content_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        final MockFlowFile flowFile = new MockFlowFile(1l);
        flowFile.putAttributes(attributes);
        runner.enqueue(flowFile);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertContentIsInvalidJsonError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_with_not_json_content_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue("not a json", attributes);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertContentIsInvalidJsonError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_with_empty_string_content_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue(StringUtils.EMPTY, attributes);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertContentIsInvalidJsonError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_with_blank_string_content_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue("\t", attributes);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertContentIsInvalidJsonError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_with_json_array_content_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue("[{\"key\":\"value\"}]", attributes);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        assertContentIsInvalidJsonError(runner.getFlowFilesForRelationship(REL_FAILURE).get(0));
    }

    @Test
    public void should_flowfile_with_empty_json_content_goto_failure() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue("{}", attributes);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_FAILURE).get(0);
        resultFlowFile.assertAttributeEquals(
            ERROR_ATTRIBUTE,
            String.format(
                "The flow file content should contain the document but it is empty or blank.",
                DOCUMENT_ID_ATTRIBUTE
            )
        );
        resultFlowFile.assertAttributeNotExists(TOOK_ATTRIBUTE);
    }

    @Test
    public void should_indexation_which_return_not_retryable_exception_goto_failure() throws IOException {
        Mockito
            .when(esService.add(Mockito.any(IndexOperationRequest.class)))
            .thenThrow(new ElasticsearchClientException("this is the expected error"));
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue(DOCUMENT_JSON_VALID, attributes);
        runner.run();
        Mockito.verify(esService).add(Mockito.argThat(new IndexOperationRequestMatcher()));
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_FAILURE).get(0);
        assertExceptionFromElasticsearchClientServiceIsExpected(resultFlowFile);
        assertThat(resultFlowFile.isPenalized()).as("Flow file should not be penalized").isFalse();
    }

    @Test
    public void should_indexation_which_return_not_tryable_exception_goto_retry() throws IOException {
        Mockito
            .when(esService.add(Mockito.any(IndexOperationRequest.class)))
            .thenThrow(new ElasticsearchRetryableClientException("this is the expected error"));
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue(DOCUMENT_JSON_VALID, attributes);
        runner.run();
        Mockito.verify(esService).add(Mockito.argThat(new IndexOperationRequestMatcher()));
        runner.assertAllFlowFilesTransferred(REL_RETRY);
        runner.assertTransferCount(REL_RETRY, 1);
        runner.assertAllFlowFilesContainAttribute(ERROR_ATTRIBUTE);
        MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_RETRY).get(0);
        assertExceptionFromElasticsearchClientServiceIsExpected(resultFlowFile);
        assertThat(resultFlowFile.isPenalized()).as("Flow file should be penalized").isTrue();
    }

    @Test
    public void should_indexation_which_return_success_goto_success() throws IOException {
        Mockito
            .when(esService.add(Mockito.any(IndexOperationRequest.class)))
            .thenReturn(new IndexOperationResponse(10l));
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        runner.enqueue(DOCUMENT_JSON_VALID, attributes);
        runner.run();
        Mockito.verify(esService).add(Mockito.argThat(new IndexOperationRequestMatcher()));
        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);
        runner.assertAllFlowFilesContainAttribute(TOOK_ATTRIBUTE);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        resultFlowFile.assertAttributeEquals(TOOK_ATTRIBUTE, "10");
        resultFlowFile.assertAttributeNotExists(ERROR_ATTRIBUTE);
        assertThat(resultFlowFile.isPenalized()).as("Flow file should not be penalized").isFalse();
    }

    @Test
    public void should_flowfile_with_version_goto_success() throws IOException {
        Mockito
            .when(esService.add(Mockito.any(IndexVersionedOperationRequest.class)))
            .thenReturn(new IndexOperationResponse(10l));
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(DOCUMENT_ID_ATTRIBUTE, DOCUMENT_ID);
        attributes.put(DOCUMENT_VERSION_ATTRIBUTE, "60000");
        runner.enqueue(DOCUMENT_JSON_VALID, attributes);
        runner.run();
        Mockito.verify(esService).add(Mockito.argThat(new IndexVersionedOperationRequestMatcher()));
        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);
        runner.assertAllFlowFilesContainAttribute(TOOK_ATTRIBUTE);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        resultFlowFile.assertAttributeEquals(TOOK_ATTRIBUTE, "10");
        resultFlowFile.assertAttributeNotExists(ERROR_ATTRIBUTE);
        assertThat(resultFlowFile.isPenalized()).as("Flow file should not be penalized").isFalse();
    }

    public static class IndexOperationRequestMatcher implements ArgumentMatcher<IndexOperationRequest> {

        @Override
        public boolean matches(final IndexOperationRequest argument) {
            assertThat(argument.getIndex()).isEqualTo(INDEX_NAME);
            assertThat(argument.getId()).isEqualTo(DOCUMENT_ID);
            assertThat(argument.getFields()).containsKey("key");
            assertThat(argument.getFields()).containsEntry("key", "value");
            return true;
        }
    }

    public static class IndexVersionedOperationRequestMatcher
        implements ArgumentMatcher<IndexVersionedOperationRequest> {

        @Override
        public boolean matches(final IndexVersionedOperationRequest argument) {
            assertThat(argument.getVersion()).isEqualTo(60000L);
            return new IndexOperationRequestMatcher().matches(argument);
        }
    }

    private void assertIdAttributeMissingError(final MockFlowFile flowFile) {
        flowFile.assertAttributeEquals(
            ERROR_ATTRIBUTE,
            String.format("The id attribute [%s] is missing.", DOCUMENT_ID_ATTRIBUTE)
        );
        flowFile.assertAttributeNotExists(TOOK_ATTRIBUTE);
    }

    private void assertContentIsInvalidJsonError(final MockFlowFile flowFile) {
        flowFile.assertAttributeEquals(
            ERROR_ATTRIBUTE,
            "Unable to read the document in the flow file content, is it a valid JSON?"
        );
        flowFile.assertAttributeNotExists(TOOK_ATTRIBUTE);
    }

    private void assertExceptionFromElasticsearchClientServiceIsExpected(final MockFlowFile flowFile) {
        flowFile.assertAttributeEquals(ERROR_ATTRIBUTE, "this is the expected error");
        flowFile.assertAttributeNotExists(TOOK_ATTRIBUTE);
    }
}
