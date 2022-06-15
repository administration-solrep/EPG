package fr.dila.solonepg.core.cm.work;

import static fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl.DEFAULT;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.work.SolonWork;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DeleteDossiersWork extends SolonWork {
    private static final long serialVersionUID = -5723101558469649770L;

    private static final STLogger LOG = STLogFactory.getLog(DeleteDossiersWork.class);

    private final Collection<String> dossierIds;

    public DeleteDossiersWork(List<String> dossierIds, String originalUser) {
        super();
        setOriginatingUsername(originalUser);
        this.dossierIds = new ArrayList<>(dossierIds);
    }

    @Override
    protected void doWork() {
        openSystemSession();
        final JournalService journalService = STServiceLocator.getJournalService();
        QueryHelper.getDocuments(session, dossierIds).forEach(d -> deleteDossier(d, journalService));
    }

    private void deleteDossier(DocumentModel doc, JournalService journalService) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        String nor = Dossier.adapt(doc).getNumeroNor();
        String dosId = doc.getId();
        dossierService.saveNorIdDossierSupprime(nor, dosId);

        journalService.journaliserActionAdministration(
            session,
            doc,
            SolonEpgEventConstant.SUPPRESSION_DOSSIER_EVENT,
            SolonEpgEventConstant.SUPPRESSION_DOSSIER_COMMENT
        );

        LOG.info(DEFAULT, String.format("Suppression du dossier [uuid : %s, nor : %s]", dosId, nor));
        session.removeDocument(doc.getRef());
        dossierService.removeDossierReferences(session, dosId);
    }
}
