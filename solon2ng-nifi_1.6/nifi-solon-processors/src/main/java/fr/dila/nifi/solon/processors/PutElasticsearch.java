package fr.dila.nifi.solon.processors;

import fr.dila.nifi.solon.processors.exception.InvalidFlowFileException;
import fr.dila.nifi.solon.services.ElasticSearchClientDilaService;
import fr.dila.nifi.solon.services.domain.IndexVersionedOperationRequest;
import fr.dila.nifi.solon.services.exception.ElasticsearchClientException;
import fr.dila.nifi.solon.services.exception.ElasticsearchRetryableClientException;
import fr.dila.nifi.solon.utils.MapperUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.elasticsearch.ElasticSearchClientService;
import org.apache.nifi.elasticsearch.IndexOperationRequest;
import org.apache.nifi.elasticsearch.IndexOperationResponse;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@CapabilityDescription("Put a document in an Elasticsearch index from a flowfile body ")
@Tags({ "elasticsearch", "insert", "update", "upsert", "delete", "write", "put", "http" })
@WritesAttributes(
    {
        @WritesAttribute(
            attribute = PutElasticsearch.TOOK_ATTRIBUTE,
            description = "The amount of time that it took to complete the update operation in ms."
        ),
        @WritesAttribute(
            attribute = PutElasticsearch.ERROR_ATTRIBUTE,
            description = "The error message provided by Elasticsearch if there is an error running the update."
        )
    }
)
public class PutElasticsearch extends AbstractProcessor {

    public static final String ERROR_ATTRIBUTE = "elasticsearch.update.error";
    public static final String TOOK_ATTRIBUTE = "elasticsearch.update.took";

    public static final PropertyDescriptor CLIENT_SERVICE = new PropertyDescriptor.Builder()
        .name("es-rest-client-service")
        .displayName("Client Service")
        .description("An Elasticsearch client service to use for running queries.")
        .identifiesControllerService(ElasticSearchClientService.class)
        .required(true)
        .build();

    public static final PropertyDescriptor INDEX = new PropertyDescriptor.Builder()
        .name("es-index")
        .displayName("Index")
        .description("The name of the index to use.")
        .required(true)
        .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
        .addValidator(StandardValidators.NON_BLANK_VALIDATOR)
        .build();

    public static final PropertyDescriptor ID_ATTRIBUTE = new PropertyDescriptor.Builder()
        .name("es-id-attr")
        .displayName("Identifier Attribute")
        .description("The name of the FlowFile attribute containing the identifier for the document.")
        .required(true)
        .expressionLanguageSupported(ExpressionLanguageScope.NONE)
        .addValidator(StandardValidators.ATTRIBUTE_KEY_VALIDATOR)
        .build();

    public static final PropertyDescriptor VERSION_ATTRIBUTE = new PropertyDescriptor.Builder()
        .name("es-version-attr")
        .displayName("Version Attribute")
        .description("The name of the FlowFile attribute containing the version for the document.")
        .expressionLanguageSupported(ExpressionLanguageScope.NONE)
        .addValidator(StandardValidators.ATTRIBUTE_KEY_VALIDATOR)
        .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
        .name("failure")
        .description("All flowfiles that fail for reasons unrelated to server availability go to this relationship.")
        .build();

    public static final Relationship REL_RETRY = new Relationship.Builder()
        .name("retry")
        .description("All flowfiles that fail due to server/cluster availability go to this relationship.")
        .build();

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
        .name("success")
        .description("All flowfiles that succeed in being transferred into Elasticsearch go here.")
        .build();

    private static final Set<Relationship> relationships;
    private static final List<PropertyDescriptor> propertyDescriptors;

    private volatile ElasticSearchClientDilaService clientService;

    static {
        final Set<Relationship> relations = new HashSet<>();
        relations.add(REL_SUCCESS);
        relations.add(REL_FAILURE);
        relations.add(REL_RETRY);
        relationships = Collections.unmodifiableSet(relations);

        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(ID_ATTRIBUTE);
        descriptors.add(VERSION_ATTRIBUTE);
        descriptors.add(INDEX);
        descriptors.add(CLIENT_SERVICE);

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

    @OnScheduled
    public void onScheduled(final ProcessContext context) {
        clientService = context.getProperty(CLIENT_SERVICE).asControllerService(ElasticSearchClientDilaService.class);
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        final FlowFile flowFile = session.get();
        if (Objects.isNull(flowFile)) {
            return;
        }
        final String index = context.getProperty(INDEX).evaluateAttributeExpressions(flowFile).getValue();
        final String idAttribute = context.getProperty(ID_ATTRIBUTE).getValue();
        final String versionAttribute = context.getProperty(VERSION_ATTRIBUTE).getValue();
        try {
            putDocumentInElasticsearch(session, flowFile, index, idAttribute, versionAttribute);
            session.transfer(flowFile, REL_SUCCESS);
        } catch (final ElasticsearchRetryableClientException e) {
            getLogger().error("Error from the elasticsearch cluster, retry later", e);
            session.putAttribute(flowFile, ERROR_ATTRIBUTE, e.getMessage());
            session.penalize(flowFile);
            session.transfer(flowFile, REL_RETRY);
        } catch (final ElasticsearchClientException | InvalidFlowFileException | IllegalArgumentException e) {
            getLogger().error("Error with an attribute or the content of the flowfile", e);
            session.putAttribute(flowFile, ERROR_ATTRIBUTE, e.getMessage());
            session.transfer(flowFile, REL_FAILURE);
        }
    }

    private void putDocumentInElasticsearch(
        final ProcessSession session,
        final FlowFile flowFile,
        final String index,
        final String idAttribute,
        final String versionAttribute
    ) {
        final Map<String, Object> document = MapperUtils.getJsonMapFromFlowFile(session, flowFile);
        final String id = Optional
            .ofNullable(flowFile.getAttribute(idAttribute))
            .filter(StringUtils::isNotBlank)
            .orElseThrow(
                () -> new InvalidFlowFileException(String.format("The id attribute [%s] is missing.", idAttribute))
            );
        final Optional<Long> version = Optional
            .ofNullable(flowFile.getAttribute(versionAttribute))
            .filter(StringUtils::isNotBlank)
            .map(Long::parseLong);
        try {
            final IndexOperationResponse response;
            if (version.isPresent()) {
                response =
                    clientService.add(
                        new IndexVersionedOperationRequest(index, StringUtils.EMPTY, id, version.get(), document)
                    );
            } else {
                response = clientService.add(new IndexOperationRequest(index, StringUtils.EMPTY, id, document, null));
            }
            session.putAttribute(flowFile, TOOK_ATTRIBUTE, String.valueOf(response.getTook()));
        } catch (final IOException e) {
            throw new ElasticsearchClientException("Unexpected error with elasticsearch client.", e);
        }
    }
}
