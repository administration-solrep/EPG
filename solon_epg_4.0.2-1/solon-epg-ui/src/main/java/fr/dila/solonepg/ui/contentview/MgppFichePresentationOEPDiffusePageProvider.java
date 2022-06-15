package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.ui.bean.MgppFichePresentationOEPDTO;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

public class MgppFichePresentationOEPDiffusePageProvider extends AbstractDTOPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();
        resultsCount = QueryUtils.doCountQuery(coreSession, query);
        if (resultsCount > 0) {
            List<String> ids = QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset);
            populateFromIds(coreSession, ids);
        }
    }

    protected void populateFromIds(CoreSession session, List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            PrefetchInfo prefetchInfo = new PrefetchInfo(
                STSchemaConstant.DUBLINCORE_SCHEMA + "," + FichePresentationOEPImpl.DOC_TYPE
            );
            List<DocumentModel> documents = QueryHelper.retrieveDocuments(session, ids, prefetchInfo);

            List<MgppFichePresentationOEPDTO> collect = documents
                .stream()
                .map(MgppFichePresentationOEPDTO::new)
                .collect(Collectors.toList());
            currentItems.addAll(collect);
        }
    }
}
