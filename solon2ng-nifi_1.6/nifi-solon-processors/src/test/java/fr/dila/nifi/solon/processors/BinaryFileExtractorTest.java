package fr.dila.nifi.solon.processors;

import static fr.dila.nifi.solon.processors.BinaryFileExtractor.ERROR_ATTRIBUTE;
import static fr.dila.nifi.solon.processors.BinaryFileExtractor.REL_FAILURE;
import static fr.dila.nifi.solon.processors.BinaryFileExtractor.REL_FAILURE_IO;
import static fr.dila.nifi.solon.processors.BinaryFileExtractor.REL_RETRY;
import static fr.dila.nifi.solon.processors.BinaryFileExtractor.REL_SUCCESS;
import static fr.dila.nifi.solon.processors.BinaryFileExtractor.TRIAL_ATTRIBUTE;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BinaryFileExtractorTest {

    private TestRunner runner;
    private Path temporaryFolder;

    private static final String DOSSIER_JSON_WITH_DOCUMENT_CONTENT_FOR_ALL_DOCUMENTS =
        "{\"documents\":[{\"content:path\":\"odt.odt\"},{\"content:path\":\"word.docx\"},{\"content:path\":\"pdf.pdf\"}]}";
    private static final String DOSSIER_JSON_EXPECTED_AFTER_EXTRACTION_FOR_ALL_DOCUMENTS =
        "{\"documents\":[{\"content:path\":\"odt.odt\",\"content:extracted\":\"Test d’un document odt.\\n\\n\"},{\"content:path\":\"word.docx\",\"content:extracted\":\"Test d’un document docx.\\n\"},{\"content:path\":\"pdf.pdf\",\"content:extracted\":\"\\nTest d’un document pdf. \\n\\n\\n\"}]}";
    private static final String DOSSIER_JSON_WITH_DOCUMENT_CONTENT_FOR_ONE_DOCUMENTS =
        "{\"documents\":[{\"content:path\":\"odt.odt\"},{},{}]}";
    private static final String DOSSIER_JSON_EXPECTED_AFTER_EXTRACTION_FOR_ONE_DOCUMENTS =
        "{\"documents\":[{\"content:path\":\"odt.odt\",\"content:extracted\":\"Test d’un document odt.\\n\\n\"},{},{}]}";
    private static final String DOSSIER_JSON_WITHOUT_DOCUMENT_CONTENT_FOR_ALL_DOCUMENTS = "{\"documents\":[{},{},{}]}";
    private static final String DOSSIER_JSON_EXPECTED_AFTER_EXTRACTION_FOR_ALL_DOCUMENTS_WITHOUT_CONTENT =
        "{\"documents\":[{},{},{}]}";
    private static final String DOSSIER_JSON_WITH_DOCUMENT_MISSING_FILE =
        "{\"documents\":[{\"content:path\":\"odt.odt\"},{\"content:path\":\"word.docx\"},{\"content:path\":\"pdf.pdf\"},{\"content:path\":\"missing_file\"}]}";
    private static final String EXTRACTED_CONTENT_PATH = "content:extracted";

    @Before
    public void init() throws InitializationException, IOException, URISyntaxException {
        temporaryFolder = Files.createTempDirectory("nifi-binary-processor-test");
        runner = TestRunners.newTestRunner(BinaryFileExtractor.class);
        runner.setProperty(BinaryFileExtractor.BINARY_STORE_PATH, temporaryFolder.toString());
        runner.setProperty(BinaryFileExtractor.BINARY_PATH_JSON_PROPERTY, "content:path");
        runner.setProperty(BinaryFileExtractor.EXTRACTED_CONTENT_JSON_PROPERTY, EXTRACTED_CONTENT_PATH);
        final File resourcesPath = new File(ClassLoader.getSystemResource("binary-processor-test-files").toURI());
        final File temporaryPath = temporaryFolder.toFile();
        FileUtils.copyDirectory(resourcesPath, temporaryPath);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(temporaryFolder.toFile());
    }

    @Test
    public void should_documents_contains_extracted_content() {
        runner.enqueue(DOSSIER_JSON_WITH_DOCUMENT_CONTENT_FOR_ALL_DOCUMENTS);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        resultFlowFile.assertAttributeNotExists(ERROR_ATTRIBUTE);
        assertThat(resultFlowFile.isContentEqual(DOSSIER_JSON_EXPECTED_AFTER_EXTRACTION_FOR_ALL_DOCUMENTS))
            .as("documents should contains the text extracted")
            .isTrue();
    }

    @Test
    public void should_only_documents_with_binary_path_json_property_field_contains_extracted_content() {
        runner.enqueue(DOSSIER_JSON_WITH_DOCUMENT_CONTENT_FOR_ONE_DOCUMENTS);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        resultFlowFile.assertAttributeNotExists(ERROR_ATTRIBUTE);
        assertThat(resultFlowFile.isContentEqual(DOSSIER_JSON_EXPECTED_AFTER_EXTRACTION_FOR_ONE_DOCUMENTS))
            .as("only documents with BINARY_PATH_JSON_PROPERTY should contains the text extracted")
            .isTrue();
    }

    @Test
    public void should_all_documents_without_binary_path_json_property_field_not_contain_extracted_content() {
        runner.enqueue(DOSSIER_JSON_WITHOUT_DOCUMENT_CONTENT_FOR_ALL_DOCUMENTS);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        resultFlowFile.assertAttributeNotExists(ERROR_ATTRIBUTE);
        assertThat(
            resultFlowFile.isContentEqual(DOSSIER_JSON_EXPECTED_AFTER_EXTRACTION_FOR_ALL_DOCUMENTS_WITHOUT_CONTENT)
        )
            .as("no document should have a content extracted")
            .isTrue();
    }

    @Test
    public void should_documents_with_missing_binary_file_go_to_retry() {
        runner.enqueue(DOSSIER_JSON_WITH_DOCUMENT_MISSING_FILE);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_RETRY);
        runner.assertTransferCount(REL_RETRY, 1);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_RETRY).get(0);
        resultFlowFile.assertAttributeExists(ERROR_ATTRIBUTE);
        resultFlowFile.assertAttributeExists(TRIAL_ATTRIBUTE);
        assertThat(resultFlowFile.getAttribute(TRIAL_ATTRIBUTE)).isEqualTo("1");
        assertThat(resultFlowFile.isContentEqual(DOSSIER_JSON_WITH_DOCUMENT_MISSING_FILE))
            .as("json should not be modifier if at least one file is missing")
            .isTrue();
    }

    @Test
    public void should_documents_with_two_much_missing_binary_file_go_to_failure_io() {
        runner.enqueue(DOSSIER_JSON_WITH_DOCUMENT_MISSING_FILE, ImmutableMap.of(TRIAL_ATTRIBUTE, "5"));
        runner.run();
        runner.assertTransferCount(REL_FAILURE_IO, 1);
    }

    @Test
    public void should_go_to_failure_if_flow_file_content_is_not_a_json_object() {
        runner.enqueue("");
        runner.enqueue("[]");
        runner.enqueue("content with text only");
        runner.enqueue("[{\"name\": \"JSON is an array\"}]");
        runner.run(4);
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 4);
        runner.assertAllFlowFilesContainAttribute(REL_FAILURE, ERROR_ATTRIBUTE);
    }

    @Test
    public void should_dossier_without_document_got_to_success() {
        final String dossier = "{\"name\":\"test\"}";
        runner.enqueue(dossier);
        runner.run();
        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);
        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        resultFlowFile.assertAttributeNotExists(ERROR_ATTRIBUTE);
        assertThat(resultFlowFile.isContentEqual(dossier))
            .as("dossier without document should not be modified")
            .isTrue();
    }

    @Test
    public void should_corrupted_file_go_to_success_with_specific_extracted_content() {
        final String dossier = "{\"name\":\"test\",\"documents\":[{\"content:path\":\"invalid_file\"}]}";
        runner.enqueue(dossier);
        runner.run();

        runner.assertAllFlowFilesTransferred(REL_SUCCESS);
        runner.assertTransferCount(REL_SUCCESS, 1);

        final MockFlowFile resultFlowFile = runner.getFlowFilesForRelationship(REL_SUCCESS).get(0);
        assertThat(new String(resultFlowFile.getData()))
            .contains(EXTRACTED_CONTENT_PATH + "\":\"FICHIER EPG2NG EN ERREUR");
    }

    @Test
    public void should_documents_is_not_a_json_array_go_to_failure() {
        runner.enqueue("{\"documents\":null}");
        runner.enqueue("{\"documents:{\"name\":\"invalid\"}}");
        runner.enqueue("{\"documents\":{}}");
        runner.run(3);
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 3);
        runner.assertAllFlowFilesContainAttribute(REL_FAILURE, ERROR_ATTRIBUTE);
    }

    @Test
    public void should_binary_path_json_property_field_with_invalid_json_content_go_to_failure() {
        runner.enqueue("{\"documents\":[{\"content:path\":{}}]}");
        runner.enqueue("{\"documents\":[{\"content:path\":[]}]}");
        runner.enqueue("{\"documents\":[{\"content:path\":true}]}");
        runner.run(3);
        runner.assertAllFlowFilesTransferred(REL_FAILURE);
        runner.assertTransferCount(REL_FAILURE, 3);
        runner.assertAllFlowFilesContainAttribute(REL_FAILURE, ERROR_ATTRIBUTE);
    }
}
