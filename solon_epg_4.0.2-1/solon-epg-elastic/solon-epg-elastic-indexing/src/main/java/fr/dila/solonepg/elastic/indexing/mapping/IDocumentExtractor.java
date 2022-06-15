package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.IOException;
import org.nuxeo.ecm.core.api.Blob;

public interface IDocumentExtractor {
    String parseTextFromFileData(Blob blob) throws IOException;
}
