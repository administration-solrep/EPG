package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_CONTENT;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_DETAILS;

import com.google.common.collect.ImmutableSet;
import com.sun.jersey.core.header.FormDataContentDisposition;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppFondDossierUIService;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.BlobUtils;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.transaction.TransactionHelper;

public class MgppFondDossierUIServiceImpl implements MgppFondDossierUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppFondDossierUIServiceImpl.class);

    private static final Set<String> FICHE_DOC_TYPES_WITH_FOND_DOSSIER = ImmutableSet.of(
        FichePresentationDOC.DOC_TYPE,
        FichePresentationAVI.DOC_TYPE,
        FichePresentationOEPImpl.DOC_TYPE,
        FichePresentationAUD.DOC_TYPE,
        FichePresentationJSS.DOC_TYPE,
        FichePresentationIE.DOC_TYPE,
        FichePresentationDPG.DOC_TYPE,
        FichePresentationSD.DOC_TYPE
    );

    @Override
    public boolean isFondDossierVisible(SpecificContext context) {
        String idFiche = context.getFromContextData(MgppContextDataKey.FICHE_ID);
        return (
            StringUtils.isNotBlank(idFiche) &&
            FICHE_DOC_TYPES_WITH_FOND_DOSSIER.contains(context.getSession().getDocument(new IdRef(idFiche)).getType())
        );
    }

    @Override
    public List<DocumentDTO> getFondDeDossierDocuments(SpecificContext context) {
        CoreSession session = context.getSession();
        String idFiche = context.getFromContextData(MgppContextDataKey.FICHE_ID);
        return SolonMgppServiceLocator
            .getFondDossierService()
            .findFileFor(session, session.getDocument(new IdRef(idFiche)))
            .stream()
            .map(dm -> toDocumentDTO(context, dm))
            .collect(Collectors.toList());
    }

    private DocumentDTO toDocumentDTO(SpecificContext context, DocumentModel dm) {
        FileSolonMgpp file = dm.getAdapter(FileSolonMgpp.class);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(dm.getId());
        documentDTO.setNom(FilenameUtils.removeExtension(FileUtils.getShorterName(file.getSafeFilename())));
        documentDTO.setAuteur(STServiceLocator.getSTUserService().getUserFullName(file.getCreator()));
        documentDTO.setDate(SolonDateConverter.DATE_SLASH.format(file.getCreatedDate()));
        documentDTO.setExtension(FileUtils.getExtensionWithSeparator(file.getSafeFilename()));
        documentDTO.setLstActions(context.getActions(MgppActionCategory.MGPP_FDD_DOCUMENT_ACTIONS));
        return documentDTO;
    }

    @Override
    public void addFile(SpecificContext context) {
        Blob blob = getFileBlob(context);

        SolonMgppServiceLocator
            .getFondDossierService()
            .addFileFor(context.getSession(), context.getCurrentDocument(), blob);

        context.getMessageQueue().addSuccessToQueue(getString("fondDossier.add.file.success", blob.getFilename()));
    }

    private static Blob getFileBlob(SpecificContext context) {
        FormDataContentDisposition fileDetails = context.getFromContextData(FILE_DETAILS);
        if (fileDetails == null) {
            throw new STValidationException("fondDossier.file.details.error");
        }

        String fileName = null;
        if (fileDetails.getFileName() != null) {
            fileName =
                new String(fileDetails.getFileName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
        try (InputStream fileContent = context.getFromContextData(FILE_CONTENT)) {
            return BlobUtils.createSerializableBlob(fileContent, fileName, null);
        } catch (IOException ioe) {
            throw new NuxeoException(getString("fondDossier.server.file.creation.error"), ioe);
        }
    }

    @Override
    public void deleteFile(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel fichierDoc = session.getDocument(new IdRef(context.getFromContextData(STContextDataKey.ID)));
        String filename = fichierDoc.getAdapter(FileSolonMgpp.class).getSafeFilename();
        try {
            SolonMgppServiceLocator.getFondDossierService().removeFileFor(session, fichierDoc);
        } catch (NuxeoException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_DEL_FICHE_LOI_TEC, e);
            context.getMessageQueue().addErrorToQueue(getString("fondDossier.delete.file.error", filename));
            TransactionHelper.setTransactionRollbackOnly();
        }
        context.getMessageQueue().addSuccessToQueue(getString("fondDossier.delete.file.success", filename));
    }
}
