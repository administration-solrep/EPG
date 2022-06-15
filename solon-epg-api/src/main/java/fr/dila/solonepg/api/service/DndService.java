package fr.dila.solonepg.api.service;
import fr.dila.solonepg.api.dto.CopyFileStatusDTO;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface DndService {
    CopyFileStatusDTO processMove(
    CoreSession session,
    String dndDocId,
    String dndContainerId,
    NuxeoPrincipal currentUser,
    DocumentModel currentDocument
    );
}