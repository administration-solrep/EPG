package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface IndexationUIService {
    void addIndexationToDossier(String currentIndexation, String indexationType, DocumentModel dossierDoc);

    void removeIndexation(String indexationType, DocumentModel dossierDoc, String indexation);

    List<String> getIndexations(String indexationType, DocumentModel dossierDoc);

    List<String> findAllIndexation(String indexationType, CoreSession session, DocumentModel dossierDoc);

    List<String> findAllIndexationRubrique(CoreSession session);

    List<SelectValueDTO> getAllIndexationRubriqueSelectValues(CoreSession session);
}
