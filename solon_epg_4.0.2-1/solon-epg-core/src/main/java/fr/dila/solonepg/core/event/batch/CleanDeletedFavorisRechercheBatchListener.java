package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.st.core.event.batch.CleanDeletedDocumentBatchListener;

/**
 * Batch de suppression des favoris recherche à l'état deleted
 *
 */
public class CleanDeletedFavorisRechercheBatchListener extends CleanDeletedDocumentBatchListener {

    public CleanDeletedFavorisRechercheBatchListener() {
        super(
            SolonEpgEventConstant.BATCH_EVENT_CLEAN_FAVORIS_RECHERCHE,
            SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE
        );
    }
}
