package fr.dila.nifi.solon.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.nifi.solon.processors.exception.BinaryExtractorRetryableException;
import fr.dila.nifi.solon.processors.exception.InvalidFlowFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.MapUtils;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.ProcessSession;

public class MapperUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String DOCUMENTS_JSON_FIELD = "documents";

    /**
     * Deserialize the JSON Object reads from a flow file content to a {@link Map}
     *
     * @param session current Nifi session
     * @param flowFile flowfile which contains the JSON Object as content
     * @return the JSON Object deserialized in a {@link Map}
     * @throws InvalidFlowFileException if the flow file content is empty or the content isn't a valid
     *         JSON Object
     */
    public static Map<String, Object> getJsonMapFromFlowFile(final ProcessSession session, final FlowFile flowFile) {
        Map<String, Object> document;
        try {
            document = MAPPER.readValue(session.read(flowFile), new TypeReference<Map<String, Object>>() {});
        } catch (final IOException e) {
            throw new InvalidFlowFileException(
                "Unable to read the document in the flow file content, is it a valid JSON?",
                e
            );
        }
        if (MapUtils.isEmpty(document)) {
            throw new InvalidFlowFileException(
                "The flow file content should contain the document but it is empty or blank."
            );
        }
        return document;
    }

    /**
     * Deserialize the JSON Object reads from a flow file content to a {@link JsonNode}
     *
     * @param session current Nifi session
     * @param flowFileflowfile which contains the JSON Object as content
     * @return the JSON Object deserialized in a {@link JsonNode}
     * @throws InvalidFlowFileException if the flow file content is empty or the content isn't a valid
     *         JSON Object
     */
    public static JsonNode getJsonNodeObjectFromFlowFile(final ProcessSession session, final FlowFile flowFile) {
        JsonNode document;
        try {
            document =
                Optional
                    .ofNullable(MAPPER.readTree(session.read(flowFile)))
                    .orElseThrow(
                        () ->
                            new InvalidFlowFileException(
                                "The flow file content should contain the document but it is empty or blank."
                            )
                    );
        } catch (final IOException e) {
            throw new InvalidFlowFileException(
                "Unable to read the document in the flow file content, is it a valid JSON?",
                e
            );
        }
        if (!document.isObject()) {
            throw new InvalidFlowFileException(
                String.format(
                    "The flow file content should contain the document object but it is [%s].",
                    document.getNodeType()
                )
            );
        }
        return document;
    }

    /**
     * serialize the {@link JsonNode} as new flow file content.
     *
     * @param session current Nifi session
     * @param flowFile with content to replace
     * @param contentToWrite {@link JsonNode} to serialize
     * @throws BinaryExtractorRetryableException if the flow file content can not be write
     */
    public static void putJsonNodeObjectToFlowFileContent(
        final ProcessSession session,
        final FlowFile flowFile,
        final JsonNode contentToWrite
    ) {
        try {
            session.importFrom(new ByteArrayInputStream(MAPPER.writeValueAsBytes(contentToWrite)), flowFile);
        } catch (final IOException e) {
            throw new BinaryExtractorRetryableException(
                "We can not write th flow file content now, please retry later.",
                e
            );
        }
    }
}
