package fr.dila.nifi.solon.services.domain;

import java.util.Map;
import org.apache.nifi.elasticsearch.IndexOperationRequest;

public class IndexVersionedOperationRequest extends IndexOperationRequest {

    private final Long version;

    public IndexVersionedOperationRequest(
        String index,
        String type,
        String id,
        Long version,
        Map<String, Object> fields
    ) {
        super(index, type, id, fields, null);
        this.version = version;
    }

    public Long getVersion() {
        return this.version;
    }
}
