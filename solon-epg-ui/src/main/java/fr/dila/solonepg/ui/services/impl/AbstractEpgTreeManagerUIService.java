package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.enums.STContextDataKey.ID;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.ui.services.actions.impl.SSTreeManagerActionServiceImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class AbstractEpgTreeManagerUIService extends SSTreeManagerActionServiceImpl {
    private static final STLogger LOGGER = STLogFactory.getLog(AbstractEpgTreeManagerUIService.class);

    protected AbstractEpgTreeManagerUIService(Class<? extends DocumentDTO> clazz) {
        super(clazz);
    }

    @Override
    public void deleteFile(SpecificContext context) {
        CoreSession session = context.getSession();
        LOGGER.info(session, STLogEnumImpl.DEL_FILE_FONC);
        String selectedNodeId = context.getFromContextData(ID);
        DocumentModel doc = getSelectedDocument(session, selectedNodeId);
        SSTreeFile file = doc.getAdapter(SSTreeFile.class);
        String filename = file.getSafeFilename();
        DocumentModel dossierDoc = context.getCurrentDocument();
        if (selectedNodeId != null) {
            List<DocumentModel> fichiersEpreuvages = getEpreuvesFiles(session, dossierDoc.getAdapter(Dossier.class));
            boolean isFileInEpreuve = fichiersEpreuvages
                .stream()
                .anyMatch(myFile -> selectedNodeId.equals(myFile.getId()));
            // test si l'utilisateur est autorisé à créer l'étape pour fourniture d'épreuve
            if (
                isFileInEpreuve &&
                !session.getPrincipal().isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR)
            ) {
                context.getMessageQueue().addErrorToQueue(getString(getErrorMessageDeleteFileEpreuvage(), filename));
                return;
            }
        }
        Boolean deleteAllVersion = context.getFromContextData(EpgContextDataKey.DELETE_ALL_VERSIONS);
        if (BooleanUtils.isTrue(deleteAllVersion) || file.getMajorVersion() == 1) {
            // on supprime le document et toutes les versions qui lui sont associées
            deleteAllFileVersion(session, doc, dossierDoc);
        } else {
            // Restauration de l'avant dernière version du noeud dans l'arborescence du fond de dossier.
            deleteFileAndRestoreToPreviousVersion(session, selectedNodeId, dossierDoc);
        }
        context.getMessageQueue().addSuccessToQueue(getString(getSuccessMessageDeleteFile(), filename));
    }

    protected abstract List<DocumentModel> getEpreuvesFiles(CoreSession session, Dossier dossier);

    protected abstract String getErrorMessageDeleteFileEpreuvage();

    protected abstract void deleteAllFileVersion(CoreSession session, DocumentModel doc, DocumentModel dossierDoc);

    protected abstract void deleteFileAndRestoreToPreviousVersion(
        CoreSession session,
        String selectedNodeId,
        DocumentModel dossierDoc
    );

    protected abstract String getSuccessMessageDeleteFile();
}
