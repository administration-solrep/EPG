package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.IOException;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.storage.sql.coremodel.SQLBlob;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentEmptyException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentMissingException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentNotParsedException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentTooLargeException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

public class DocumentMapper extends DefaultComponent implements IDocumentMapper {
	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER				= STLogFactory.getLog(DocumentMapper.class);

	private IDocumentExtractor documentExtractor;
	private Integer documentTailleMaximumKo;

	@Override
	public void activate(ComponentContext context) throws Exception {
		documentExtractor = Framework.getService(IDocumentExtractor.class);
		ConfigService configService = STServiceLocator.getConfigService();
		documentTailleMaximumKo = configService.getIntegerValue(SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_TAILLE_MAXIMUM_KO);
	}

	@Override
	public void deactivate(ComponentContext context) throws Exception {
		documentExtractor = null;
	}

	/* (non-Javadoc)
	 * @see fr.dila.solonepg.elastic.indexing.mapping.IDocumentMapper#from(org.nuxeo.ecm.core.api.DocumentModel, java.lang.String, org.nuxeo.ecm.core.api.CoreSession, boolean)
	 */
	@Override
	public ElasticDocument from(DocumentModel documentModel, String path, CoreSession session, boolean currentVersion) throws MappingException, ClientException {
		ElasticDocument exportableDocument = new ElasticDocument();

		/* Technical uid */
		exportableDocument.setUid(documentModel.getId());
		exportableDocument.setPath(path);

		/* common:... */
		exportableDocument.setCommonIcon((String) documentModel.getPropertyValue(ElasticDocument.COMMON_ICON));
		exportableDocument.setCommonIconExpanded((String) documentModel
				.getPropertyValue(ElasticDocument.COMMON_ICON_EXPANDED));
		exportableDocument.setCommonSize((Long) documentModel.getPropertyValue(ElasticDocument.COMMON_SIZE));

		/* dc:... */
		exportableDocument.setDcCoverage((String) documentModel.getPropertyValue(ElasticDocument.DC_COVERAGE));
		exportableDocument.setDcCreated(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDocument.DC_CREATED)));
		exportableDocument.setDcCreator((String) documentModel.getPropertyValue(ElasticDocument.DC_CREATOR));
		exportableDocument.setDcExpired(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDocument.DC_EXPIRED)));
		exportableDocument.setDcFormat((String) documentModel.getPropertyValue(ElasticDocument.DC_FORMAT));
		exportableDocument.setDcIssued((String) documentModel.getPropertyValue(ElasticDocument.DC_ISSUED));
		exportableDocument.setDcLanguage((String) documentModel.getPropertyValue(ElasticDocument.DC_LANGUAGE));
		exportableDocument.setDcLastContributor((String) documentModel
				.getPropertyValue(ElasticDocument.DC_LAST_CONTRIBUTOR));
		exportableDocument.setDcModified(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDocument.DC_MODIFIED)));
		exportableDocument.setDcNature((String) documentModel.getPropertyValue(ElasticDocument.DC_NATURE));
		exportableDocument.setDcRights((String) documentModel.getPropertyValue(ElasticDocument.DC_RIGHTS));
		exportableDocument.setDcSource((String) documentModel.getPropertyValue(ElasticDocument.DC_SOURCE));
		exportableDocument.setDcTitle((String) documentModel.getTitle());
		exportableDocument.setDcValid((String) documentModel.getPropertyValue(ElasticDocument.DC_VALID));

		/* filepg:... */
		exportableDocument.setFilepgEntite((String) documentModel.getPropertyValue(ElasticDocument.FILEPG_ENTITE));
		exportableDocument.setFilepgFiletypeId((Long) documentModel
				.getPropertyValue(ElasticDocument.FILEPG_FILETYPE_ID));
		exportableDocument.setFilepgRelatedDocument((String) documentModel
				.getPropertyValue(ElasticDocument.FILEPG_RELATED_DOCUMENT));

		/* uid:... */
		exportableDocument.setUidMajorVersion((Long) documentModel
				.getPropertyValue(ElasticDocument.UID_MAJOR_VERSION));
		exportableDocument.setUidMinorVersion((Long) documentModel
				.getPropertyValue(ElasticDocument.UID_MINOR_VERSION));

		return exportableDocument;
	}

	/**
	 * Extraction des propriétés directement relatives au fichier, dont l'extraction du contenu textuel.
	 * @throws DocumentMissingException 
	 */
	@Override
	public void mapFileProperties(DocumentModel documentModel, ElasticDocument exportableDocument,
			boolean currentVersion, CoreSession session) throws DocumentEmptyException, DocumentTooLargeException, DocumentNotParsedException, DocumentMissingException {
		/* file:... */
		SQLBlob fileContent = null;
		try {
			fileContent = (SQLBlob) documentModel.getPropertyValue(ElasticDocument.FILE_CONTENT);
		} catch (ClientException e) {
			throw new DocumentEmptyException(String.format("Document %s vide", documentModel));
		}

		if (fileContent != null && fileContent.getFilename() != null) {
			String logDocumentName = String.format("id:%s (%s)", documentModel.getId(), fileContent.getFilename());
			long fileLength = fileContent.getLength();

			// Si le fileContent est vide, le document n'est pas exporté
			if (fileLength == 0) {
				throw new DocumentEmptyException(String.format("Document %s vide", logDocumentName));
			}
			// Si le fichier dépasse la taille maximum autorisée, il n'est pas indexé
			if (fileLength > documentTailleMaximumKo * 1000) {
				throw new DocumentTooLargeException(String.format("Document %s trop volumineux (%d o., limite %d ko)",
						logDocumentName, fileLength, documentTailleMaximumKo));
			}

			exportableDocument.setFileLength(fileLength);
			exportableDocument.setFileFilename(fileContent.getFilename()); // Titre du doc associé
			exportableDocument.setFileMimeType(fileContent.getMimeType());
			exportableDocument.setFileEncoding(fileContent.getEncoding());
			exportableDocument.setFileDigest(fileContent.getDigest());
			exportableDocument.setFileName(fileContent.getFilename());
			exportableDocument.setCurrentVersion(currentVersion);

			LOGGER.debug(session, EpgLogEnumImpl.PROCESS_B_EXPORT_DOSSIERS_TEC, "Document " + fileContent.getFilename()
					+ " : " + fileContent.getLength());

			try {
				exportableDocument.setFileData(documentExtractor.parseTextFromFileData(fileContent));
			} catch (ClientException e) {
				throw new DocumentNotParsedException(String.format("Document %s non extrait en texte", logDocumentName), e);
			} catch (IOException ioe) {
				throw new DocumentNotParsedException(String.format("Document %s non extrait en texte", logDocumentName), ioe);
			}
		} else {
			throw new DocumentMissingException(String.format("Document %s manquant", documentModel.getId()));
		}
	}
}
