package fr.dila.solonepg.adamant.work;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.work.SolonWork;
import java.util.ArrayList;
import java.util.Collection;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class CreationLotArchivageWork extends SolonWork {
    private static final long serialVersionUID = 222608934321085779L;

    public static final String CATEGORY = "CreationLotArchivageWork";
    public static final String QUEUE_ID = "CreationLotArchivage";

    private Collection<String> idsDossiers;
    private String idLot;

    public CreationLotArchivageWork(String idLot, Collection<String> idsDossiers) {
        super();
        this.idLot = idLot;
        this.idsDossiers = new ArrayList<>(idsDossiers);
    }

    @Override
    protected void doWork() {
        openSystemSession();

        DocumentModelList dossierDocs = QueryHelper.getDocuments(
            session,
            idsDossiers,
            DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE_XPATH,
            DossierSolonEpgConstants.DOSSIER_ID_EXTRACTION_LOT_XPATH
        );

        dossierDocs
            .stream()
            .map(doc -> doc.getAdapter(Dossier.class))
            .forEach(
                dos -> {
                    dos.setIdExtractionLot(idLot);
                    dos.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_EXTRACTION_EN_COURS);
                    dos.save(session);
                }
            );
    }

    @Override
    public String getCategory() {
        return CATEGORY;
    }
}
