package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.ss.ui.contentview.ModeleFDRPageProvider;
import fr.dila.st.core.helper.PaginationHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class DerniersModeleFDRConsultesPageProvider extends ModeleFDRPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();

        DocumentModelList resultatsConsultesDocs = QueryUtils.doQuery(coreSession, query, 1, 0);
        if (!resultatsConsultesDocs.isEmpty()) {
            ResultatConsulte resultatsConsultes = resultatsConsultesDocs.get(0).getAdapter(ResultatConsulte.class);
            List<String> fdrsId = resultatsConsultes.getFdrsId();
            resultsCount = fdrsId.size();

            // récupération page courante
            offset = PaginationHelper.calculeOffSet(offset, getPageSize(), resultsCount);

            List<String> ids = SolonEpgProviderHelper.paginateList(fdrsId, offset, getPageSize(), resultsCount);
            populateFromFeuilleRouteIds(coreSession, ids);
        }
    }
}
