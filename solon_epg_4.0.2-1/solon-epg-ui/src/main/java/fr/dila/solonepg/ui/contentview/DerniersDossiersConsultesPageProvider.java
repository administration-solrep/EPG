package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.st.core.helper.PaginationHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class DerniersDossiersConsultesPageProvider extends DossierPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    protected List<String> getIdsFromQuery(CoreSession session, List<Object> params) {
        DocumentModelList resultatsConsultesDocs = QueryUtils.doQuery(session, query, 1, 0);
        List<String> ids = new ArrayList<>();
        if (!resultatsConsultesDocs.isEmpty()) {
            ResultatConsulte resultatsConsultes = resultatsConsultesDocs.get(0).getAdapter(ResultatConsulte.class);
            List<String> dossierIds = resultatsConsultes.getDossiersId();
            resultsCount = dossierIds.size();

            // récupération page courante
            offset = PaginationHelper.calculeOffSet(offset, getPageSize(), resultsCount);

            ids = SolonEpgProviderHelper.paginateList(dossierIds, offset, getPageSize(), resultsCount);
        }
        return ids;
    }
}
