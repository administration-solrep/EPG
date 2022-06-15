package fr.dila.solonepg.elastic.indexing.mapping;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentEmptyException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentMissingException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentNotParsedException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentTooLargeException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.st.api.constant.MediaType;
import fr.dila.st.api.constant.STConfigConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

public class DocumentMapper extends DefaultComponent implements IDocumentMapper {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(DocumentMapper.class);

    private Integer documentTailleMaximumKo;

    private List<MediaType> allowedMimeTypes = Collections.emptyList();

    private int pdfTextDocumentCaracteres;

    @Override
    public void activate(ComponentContext context) {
        ConfigService configService = STServiceLocator.getConfigService();
        documentTailleMaximumKo =
            configService.getIntegerValue(
                SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_TAILLE_MAXIMUM_KO
            );

        pdfTextDocumentCaracteres =
            configService.getIntegerValue(
                SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_PDF_CARACTERES
            );

        String allowedMimeTypesAsString = configService.getValue(
            SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_AUTORISES
        );
        allowedMimeTypes =
            Stream
                .of(StringUtils.split(allowedMimeTypesAsString, ','))
                .map(MediaType::fromMimeType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.dila.solonepg.elastic.indexing.mapping.IDocumentMapper#from(org.nuxeo.
     * ecm.core.api.DocumentModel, java.lang.String,
     * org.nuxeo.ecm.core.api.CoreSession, boolean)
     */
    @Override
    public ElasticDocument from(DocumentModel documentModel, String path, CoreSession session, boolean currentVersion)
        throws MappingException {
        ElasticDocument exportableDocument = new ElasticDocument();

        /* Technical uid */
        exportableDocument.setUid(documentModel.getId());
        exportableDocument.setPath(path);

        /* common:... */
        exportableDocument.setCommonIcon((String) documentModel.getPropertyValue(ElasticDocument.COMMON_ICON));
        exportableDocument.setCommonIconExpanded(
            (String) documentModel.getPropertyValue(ElasticDocument.COMMON_ICON_EXPANDED)
        );

        /* dc:... */
        exportableDocument.setDcCoverage((String) documentModel.getPropertyValue(ElasticDocument.DC_COVERAGE));
        exportableDocument.setDcCreated(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDocument.DC_CREATED))
        );
        exportableDocument.setDcCreator((String) documentModel.getPropertyValue(ElasticDocument.DC_CREATOR));
        exportableDocument.setDcExpired(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDocument.DC_EXPIRED))
        );
        exportableDocument.setDcFormat((String) documentModel.getPropertyValue(ElasticDocument.DC_FORMAT));
        exportableDocument.setDcIssued((String) documentModel.getPropertyValue(ElasticDocument.DC_ISSUED));
        exportableDocument.setDcLanguage((String) documentModel.getPropertyValue(ElasticDocument.DC_LANGUAGE));
        exportableDocument.setDcLastContributor(
            (String) documentModel.getPropertyValue(ElasticDocument.DC_LAST_CONTRIBUTOR)
        );
        exportableDocument.setDcModified(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDocument.DC_MODIFIED))
        );
        exportableDocument.setDcNature((String) documentModel.getPropertyValue(ElasticDocument.DC_NATURE));
        exportableDocument.setDcRights((String) documentModel.getPropertyValue(ElasticDocument.DC_RIGHTS));
        exportableDocument.setDcSource((String) documentModel.getPropertyValue(ElasticDocument.DC_SOURCE));
        exportableDocument.setDcTitle(documentModel.getTitle());
        exportableDocument.setDcValid((String) documentModel.getPropertyValue(ElasticDocument.DC_VALID));

        /* filepg:... */
        exportableDocument.setFilepgEntite((String) documentModel.getPropertyValue(ElasticDocument.FILEPG_ENTITE));
        exportableDocument.setFilepgFiletypeId(
            (Long) documentModel.getPropertyValue(ElasticDocument.FILEPG_FILETYPE_ID)
        );
        exportableDocument.setFilepgRelatedDocument(
            (String) documentModel.getPropertyValue(ElasticDocument.FILEPG_RELATED_DOCUMENT)
        );

        /* uid:... */
        exportableDocument.setUidMajorVersion((Long) documentModel.getPropertyValue(ElasticDocument.UID_MAJOR_VERSION));
        exportableDocument.setUidMinorVersion((Long) documentModel.getPropertyValue(ElasticDocument.UID_MINOR_VERSION));

        return exportableDocument;
    }

    /**
     * Extraction des propriétés directement relatives au fichier, dont
     * l'extraction du contenu textuel.
     *
     * @throws DocumentMissingException
     */
    @Override
    public void mapFileProperties(
        DocumentModel documentModel,
        ElasticDocument exportableDocument,
        boolean currentVersion,
        CoreSession session
    )
        throws DocumentEmptyException, DocumentTooLargeException, DocumentNotParsedException, DocumentMissingException {
        /* file:... */
        Blob fileContent = null;
        try {
            fileContent = (Blob) documentModel.getPropertyValue(ElasticDocument.FILE_CONTENT);
        } catch (NuxeoException e) {
            throw new DocumentEmptyException(String.format("Document %s vide", documentModel));
        }

        if (fileContent != null && fileContent.getFilename() != null) {
            String logDocumentName = String.format("id:%s (%s)", documentModel.getId(), fileContent.getFilename());
            long fileLength = fileContent.getLength();

            // Si le fileContent est vide, le document n'est pas exporté
            if (fileLength == 0) {
                throw new DocumentEmptyException(String.format("Document %s vide", logDocumentName));
            }
            // Si le fichier dépasse la taille maximum autorisée, il n'est pas
            // indexé
            if (fileLength > documentTailleMaximumKo * 1000) {
                throw new DocumentTooLargeException(
                    String.format(
                        "Document %s trop volumineux (%d o., limite %d ko)",
                        logDocumentName,
                        fileLength,
                        documentTailleMaximumKo
                    )
                );
            }

            exportableDocument.setFileLength(fileLength);
            exportableDocument.setFileFilename(fileContent.getFilename()); // Titre
            // du
            // doc
            // associé
            exportableDocument.setFileMimeType(fileContent.getMimeType());
            exportableDocument.setFileEncoding(fileContent.getEncoding());
            exportableDocument.setFileDigest(fileContent.getDigest());
            exportableDocument.setFileName(fileContent.getFilename());
            exportableDocument.setCurrentVersion(currentVersion);

            if (isDocumentDataExportable(fileContent, logDocumentName)) {
                String filePath = fileContent.getFile().getPath();
                String binaryStorePath = STServiceLocator
                    .getConfigService()
                    .getValue(STConfigConstants.REPOSITORY_BINARY_STORE_PATH);
                String relativeFilePath = filePath.replace(binaryStorePath, "");
                exportableDocument.setFileSystemPath(relativeFilePath);
            } else {
                throw new DocumentNotParsedException(String.format("Document %s non parsable", logDocumentName));
            }

            LOGGER.debug(
                session,
                EpgLogEnumImpl.PROCESS_B_EXPORT_DOSSIERS_TEC,
                "Document " + fileContent.getFilename() + " : " + fileContent.getLength()
            );
        } else {
            throw new DocumentMissingException(String.format("Document %s manquant", documentModel.getId()));
        }
    }

    private boolean isDocumentDataExportable(Blob fileContent, String logDocumentName)
        throws DocumentNotParsedException {
        MediaType fileMediaType = MediaType.fromMimeType(fileContent.getMimeType());
        return (
            allowedMimeTypes.contains(fileMediaType) &&
            fileMediaType != MediaType.APPLICATION_PDF ||
            isTextPdfDocument(fileContent, logDocumentName)
        );
    }

    private boolean isTextPdfDocument(Blob fileContent, String logDocumentName) throws DocumentNotParsedException {
        MediaType fileMediaType = MediaType.fromMimeType(fileContent.getMimeType());
        if (fileMediaType != MediaType.APPLICATION_PDF || !allowedMimeTypes.contains(MediaType.APPLICATION_PDF)) {
            return false;
        }

        try (PDDocument pdDocument = PDDocument.load(fileContent.getStream())) {
            PdfTextDocumentScanner scanner = new PdfTextDocumentScanner(pdDocument);
            return scanner.containsNumberCharacters(pdfTextDocumentCaracteres);
        } catch (IOException e) {
            throw new DocumentNotParsedException(String.format("Document %s non parsé", logDocumentName), e);
        }
    }
}
