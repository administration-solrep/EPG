package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.PropertyHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PanMajMinPageProvider extends AbstractDTOFiltrableProvider {
    private static final long serialVersionUID = 2877765954625608671L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();

        Object[] params = getQueryParams().toArray();
        resultsCount = QueryUtils.doCountQuery(coreSession, query, params);
        if (resultsCount > 0) {
            List<String> ids = QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset, params);
            List<DocumentModel> dml = QueryUtils.retrieveDocuments(
                coreSession,
                ActiviteNormativeConstants.MAJ_MIN_DOCUMENT_TYPE,
                ids,
                true
            );
            dml.forEach(doc -> currentItems.add(getMapWithActiviteNormativeId(coreSession, doc)));
        }
    }

    private Map<String, Serializable> getMapWithActiviteNormativeId(CoreSession coreSession, DocumentModel doc) {
        final Map<String, Serializable> mapForProvider = PropertyHelper.getMapForProviderFromDocumentProps(
            doc,
            ActiviteNormativeConstants.MAJ_MIN_SCHEMA
        );

        if (MapUtils.isNotEmpty(mapForProvider)) {
            String anId = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .findActiviteNormativeIdByNumero(coreSession, (String) mapForProvider.get("ref"));
            mapForProvider.put("activiteNormativeId", anId);
        }

        return mapForProvider;
    }
}
