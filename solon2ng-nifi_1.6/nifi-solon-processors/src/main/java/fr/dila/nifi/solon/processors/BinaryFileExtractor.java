package fr.dila.nifi.solon.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.dila.nifi.solon.processors.exception.BinaryExtractorRetryableException;
import fr.dila.nifi.solon.processors.exception.InvalidFlowFileException;
import fr.dila.nifi.solon.utils.MapperUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@CapabilityDescription(
    "Check all the documents in a folder, if the json field BINARY_PATH_JSON_PROPERTY for a document is set, read the file in BINARY_STORE_PATH/BINARY_PATH_JSON_PROPERTY and extract content in the document json field EXTRACTED_CONTENT_JSON_PROPERTY."
)
@Tags({ "tika", "file", "extract", "read", "dossier", "document" })
public class BinaryFileExtractor extends AbstractProcessor {

    public static final String ERROR_ATTRIBUTE = "binary.file.extractor.error";
    public static final String TRIAL_ATTRIBUTE = "binary.file.extractor.trial";

    private static final int MAX_TRIALS = 5;

    public static final PropertyDescriptor BINARY_STORE_PATH = new PropertyDescriptor.Builder()
        .name("path-to-binary-store")
        .displayName("Binary store path")
        .description("Path which contains the binary files.")
        .required(true)
        .expressionLanguageSupported(ExpressionLanguageScope.NONE)
        .addValidator(StandardValidators.FILE_EXISTS_VALIDATOR)
        .build();

    public static final PropertyDescriptor BINARY_PATH_JSON_PROPERTY = new PropertyDescriptor.Builder()
        .name("json-property-with-binary-path")
        .displayName("Json property with binary path")
        .description(
            "Json property which contains the relative path of a binary file (concatenated with \"Binary store path\")."
        )
        .required(true)
        .expressionLanguageSupported(ExpressionLanguageScope.NONE)
        .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
        .build();

    public static final PropertyDescriptor EXTRACTED_CONTENT_JSON_PROPERTY = new PropertyDescriptor.Builder()
        .name("json-property-for-extracted-content")
        .displayName("Json property to write")
        .description("Json property to write with the extracted content.")
        .required(true)
        .expressionLanguageSupported(ExpressionLanguageScope.NONE)
        .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
        .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
        .name("failure")
        .description("All flowfiles that fail for invalid flow file or invalid binary file.")
        .build();

    public static final Relationship REL_FAILURE_IO = new Relationship.Builder()
        .name("failure-io")
        .description(
            "All flowfiles that fail after the maximum retries of extract content of this flowfile has been reached."
        )
        .build();

    public static final Relationship REL_RETRY = new Relationship.Builder()
        .name("retry")
        .description("All flowfiles that fail for temporary error as I/O error.")
        .build();

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
        .name("success")
        .description("All flowfiles which need no extraction or with succeeded extraction.")
        .build();

    private static final Set<Relationship> relationships;
    private static final List<PropertyDescriptor> propertyDescriptors;
    private static final Tika TIKA_INSTANCE = new Tika();

    static {
        final Set<Relationship> relations = new HashSet<>();
        relations.add(REL_SUCCESS);
        relations.add(REL_FAILURE);
        relations.add(REL_FAILURE_IO);
        relations.add(REL_RETRY);
        relationships = Collections.unmodifiableSet(relations);

        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(BINARY_STORE_PATH);
        descriptors.add(BINARY_PATH_JSON_PROPERTY);
        descriptors.add(EXTRACTED_CONTENT_JSON_PROPERTY);
        propertyDescriptors = Collections.unmodifiableList(descriptors);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return propertyDescriptors;
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        final FlowFile flowFile = session.get();
        if (Objects.isNull(flowFile)) {
            return;
        }

        String trial = StringUtils.defaultString(flowFile.getAttribute(TRIAL_ATTRIBUTE), "0");
        int trialAsNumber = 1;
        if (StringUtils.isNumeric(trial)) {
            trialAsNumber = Integer.parseInt((trial) + 1);
        }
        session.putAttribute(flowFile, TRIAL_ATTRIBUTE, String.valueOf(trialAsNumber));
        if (trialAsNumber > MAX_TRIALS) {
            getLogger().error("Maximum trials of {} for this flowfile has been reached [{}].", MAX_TRIALS, flowFile);
            session.transfer(flowFile, REL_FAILURE_IO);
            return;
        }

        try {
            extractContentsForFlowFile(flowFile, context, session);
            getLogger().debug("Content successfully extracted for flow file [{}]", flowFile);
            session.transfer(flowFile, REL_SUCCESS);
        } catch (final InvalidFlowFileException e) {
            getLogger()
                .error("Error with an attribute or the content of the flowfile [{}].", new Object[] { flowFile }, e);
            session.putAttribute(flowFile, ERROR_ATTRIBUTE, e.getMessage());
            session.transfer(flowFile, REL_FAILURE);
        } catch (final BinaryExtractorRetryableException e) {
            getLogger()
                .warn(
                    "Retryable error from binary extractor for flowfile [{}], the file exists?",
                    new Object[] { flowFile },
                    e
                );
            session.putAttribute(flowFile, ERROR_ATTRIBUTE, e.getMessage());
            session.transfer(flowFile, REL_RETRY);
        }
    }

    protected void extractContentsForFlowFile(
        final FlowFile flowFile,
        final ProcessContext context,
        final ProcessSession session
    ) {
        getLogger().debug("Extract content for flow file [{}]", flowFile);
        final JsonNode dossier = MapperUtils.getJsonNodeObjectFromFlowFile(session, flowFile);
        if (dossier.has(MapperUtils.DOCUMENTS_JSON_FIELD)) {
            final JsonNode documents = dossier.get(MapperUtils.DOCUMENTS_JSON_FIELD);
            if (!documents.isArray()) {
                throw new InvalidFlowFileException(
                    String.format("Documents is not an array, its type is [%s]", documents.getNodeType())
                );
            }
            extractContentsForDocuments(flowFile, context, documents);
            MapperUtils.putJsonNodeObjectToFlowFileContent(session, flowFile, dossier);
        } else {
            getLogger().debug("The folder does not contain document for flow file [{}]", flowFile);
        }
    }

    protected void extractContentsForDocuments(
        final FlowFile flowFile,
        final ProcessContext context,
        final JsonNode documents
    ) {
        final String binaryStore = context.getProperty(BINARY_STORE_PATH).getValue();
        final String binaryPathJsonProperty = context.getProperty(BINARY_PATH_JSON_PROPERTY).getValue();
        final String extractedContentJsonProperty = context.getProperty(EXTRACTED_CONTENT_JSON_PROPERTY).getValue();
        documents.forEach(
            document ->
                extractContentForDocument(
                    flowFile,
                    document,
                    binaryStore,
                    binaryPathJsonProperty,
                    extractedContentJsonProperty
                )
        );
    }

    protected void extractContentForDocument(
        final FlowFile flowFile,
        final JsonNode document,
        final String binaryStore,
        final String binaryPathJsonProperty,
        final String extractedContentJsonProperty
    ) {
        if (!document.isObject()) {
            throw new InvalidFlowFileException(
                String.format("document [%s] is not an object, its type is [%s]", document, document.getNodeType())
            );
        }
        if (document.hasNonNull(binaryPathJsonProperty)) {
            final JsonNode binaryPathNode = document.get(binaryPathJsonProperty);
            if (!binaryPathNode.isTextual()) {
                throw new InvalidFlowFileException(
                    String.format("Binary path field is not a string, its type is [%s]", binaryPathNode.getNodeType())
                );
            }
            final String content = extractContentAsStringForFile(flowFile, binaryStore, binaryPathNode.asText());
            if (getLogger().isDebugEnabled()) {
                final String partialContentResult = content
                    .substring(0, Math.min(content.length(), 100))
                    .replace("\n", "");
                getLogger().debug("first 100 characters of content without line break are [{}]", partialContentResult);
            }
            ((ObjectNode) document).put(extractedContentJsonProperty, content);
        } else {
            getLogger()
                .debug("The document does not contain content to extract in property [{}]", binaryPathJsonProperty);
        }
    }

    protected String extractContentAsStringForFile(
        final FlowFile flowFile,
        final String parentPath,
        final String childPath
    ) {
        try {
            return TIKA_INSTANCE.parseToString(new File(parentPath, childPath));
        } catch (final IOException e) {
            throw new BinaryExtractorRetryableException(
                String.format(
                    "Unable to read file [%s] in folder [%s], please create the file or change permissions and retry",
                    childPath,
                    parentPath
                ),
                e
            );
        } catch (final TikaException e) {
            getLogger()
                .warn(
                    "Tika can not parse the flowFile [{}] file [{}] in folder [{}]",
                    new Object[] { childPath, parentPath, flowFile },
                    e
                );
            return "FICHIER EPG2NG EN ERREUR";
        }
    }
}
