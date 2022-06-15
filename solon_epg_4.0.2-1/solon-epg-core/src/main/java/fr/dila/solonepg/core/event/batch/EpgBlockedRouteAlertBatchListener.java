package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.ss.core.event.batch.AbstractBlockedRouteAlertBatchListener;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

public class EpgBlockedRouteAlertBatchListener extends AbstractBlockedRouteAlertBatchListener {
    private static final String QUERY_DOSSIERS_BLOQUES =
        "SELECT id FROM dossier_solon_epg dos " +
        " WHERE dos.id IN (SELECT id from misc where misc.lifecyclestate = 'running') " +
        " AND dos.lastdocumentroute IN (SELECT documentrouteid FROM routing_task r1 WHERE r1.id IN (SELECT id FROM misc WHERE misc.lifecyclestate = 'ready'))" +
        " AND dos.lastdocumentroute NOT IN (SELECT documentrouteid FROM routing_task r2 WHERE r2.id IN (SELECT id FROM misc where misc.lifecyclestate = 'running'))";

    private static final Log LOGGER = LogFactory.getLog(EpgBlockedRouteAlertBatchListener.class);

    @Override
    protected DataSource generateData(CoreSession session, List<String> idsDossiersDoc) {
        List<DocumentModel> dossiersDoc = new ArrayList<>();
        for (String id : idsDossiersDoc) {
            try {
                dossiersDoc.add(session.getDocument(new IdRef(id)));
            } catch (NuxeoException e) {
                LOGGER.error("Erreur de récupération du document " + id + ". " + e.getMessage());
            }
        }
        DataSource fichierExcelResultat = null;
        fichierExcelResultat = EpgExcelUtil.creationListDossierExcel(dossiersDoc);
        return fichierExcelResultat;
    }

    @Override
    protected String getQuery() {
        return QUERY_DOSSIERS_BLOQUES;
    }
}
