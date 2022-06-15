package fr.dila.solonepg.ui.contentview.mgpp;

import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

public class MgppDossierPageProvider extends DocumentModelFiltrablePaginatedPageDocumentProvider {
    private static final long serialVersionUID = 1467451926272580726L;
    public static final String CORBEILLE_NODE = "corbeilleNode";

    public MgppDossierPageProvider() {
        super();
    }

    @Override
    protected void buildQuery(CoreSession session) {
        SortInfo[] sortArray = getSortInfosWithAdditionalSort();
        CorbeilleNode corbeilleNode = (CorbeilleNode) this.properties.get(CORBEILLE_NODE);
        List<Object> params = new ArrayList<>();

        StringBuilder newQuery = new StringBuilder(
            SolonMgppServiceLocator.getCorbeilleService().getDossierQuery(corbeilleNode, params).toString()
        );
        final PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
        if (pageSelections.getEntries() != null) {
            for (final PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
                buildDynamicFilters(newQuery, pageSelection);
            }
        }
        params.addAll(filtrableParameters);
        filtrableParameters.clear();

        query =
            FlexibleQueryMaker.KeyCode.UFXNQL.getKey() +
            NXQLQueryBuilder.getQuery(
                newQuery.toString(),
                params.toArray(),
                true,
                false,
                this.searchDocumentModel,
                sortArray
            );
    }
}
