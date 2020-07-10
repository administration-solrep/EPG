package fr.dila.solonepg.elastic.indexing.mapping;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentEmptyException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentMissingException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentNotParsedException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentTooLargeException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDocument;

public interface IDocumentMapper {

	ElasticDocument from(DocumentModel documentModel, String path, CoreSession session, boolean currentVersion)
			throws MappingException, ClientException;

	void mapFileProperties(DocumentModel documentModel, ElasticDocument exportableDocument, boolean currentVersion,
			CoreSession session) throws DocumentEmptyException, DocumentTooLargeException,
			DocumentNotParsedException, DocumentMissingException;

}