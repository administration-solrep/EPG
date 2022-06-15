package fr.dila.nifi.solon.services;

import fr.dila.nifi.solon.services.domain.IndexVersionedOperationRequest;
import java.io.IOException;
import org.apache.nifi.elasticsearch.ElasticSearchClientService;
import org.apache.nifi.elasticsearch.IndexOperationResponse;

public interface ElasticSearchClientDilaService extends ElasticSearchClientService {
    /**
     * Index a document.
     *
     * @param operation A document to index.
     * @return IndexOperationResponse if successful
     * @throws IOException thrown when there is an error.
     */
    IndexOperationResponse add(IndexVersionedOperationRequest operation) throws IOException;
}
