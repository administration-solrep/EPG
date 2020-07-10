package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.IOException;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;

public interface IDocumentExtractor {

	String parseTextFromFileData(Blob blob) throws ClientException, IOException;

}
