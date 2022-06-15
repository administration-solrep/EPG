package fr.dila.solonepg.adamant.service;

import fr.dila.solonepg.adamant.model.RetourVitamBordereauLivraison;
import fr.dila.solonepg.adamant.model.RetourVitamRapportVersement;
import java.io.IOException;
import org.nuxeo.ecm.core.api.CoreSession;

public interface RetourVitamService {
    void saveLineBorderauLivraison(RetourVitamBordereauLivraison bordereau);

    void saveLineRapportVersement(RetourVitamRapportVersement rapport);

    void updateStatutDossierFromBordereauLivraison(
        CoreSession session,
        RetourVitamBordereauLivraison bordereau,
        int indexLine
    );

    void updateStatutDossierFromRapportVersement(
        CoreSession session,
        RetourVitamRapportVersement rapport,
        int indexLine
    );

    void moveFileToError(CoreSession session, String fileName) throws IOException;

    void moveFileToDone(CoreSession session, String fileName) throws IOException;

    String getPathDirRetourVitamTodo();

    void saveBordereauVersementDossier(CoreSession session, String nor, byte[] content);
}
