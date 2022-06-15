package fr.dila.solonepg.ui.contentview;

import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.domain.STDomainObject;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.util.ObjectHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

public abstract class AbstractIdsPanPageProvider<T extends STDomainObject, S extends AbstractMapDTO>
    extends AbstractPanPageProvider<T, S> {
    private static final long serialVersionUID = 9196058656588502176L;

    private List<String> ids = new ArrayList<>();

    public void setIds(List<String> ids) {
        this.ids = ObjectHelper.requireNonNullElseGet(ids, ArrayList::new);
    }

    protected abstract int defaultSort(T val1, T val2);

    @Override
    protected void fillCurrentPageMapList(CoreSession session) {
        PrefetchInfo prefetchInfo = new PrefetchInfo(
            STSchemaConstant.DUBLINCORE_SCHEMA + "," + SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA
        );
        List<T> all = QueryHelper
            .retrieveDocuments(session, ids, prefetchInfo)
            .stream()
            .map(this::getAdapter)
            .sorted(this::defaultSort)
            .collect(Collectors.toList());

        currentItems = new ArrayList<>();
        resultsCount = ids.size();
        if (resultsCount > 0) {
            int from = Math.toIntExact(Math.min(offset, resultsCount - 1));
            int to = Math.toIntExact(Math.min(offset + getPageSize(), resultsCount));
            currentItems =
                all
                    .subList(from, to)
                    .stream()
                    .map(adapterDocToDto -> this.mapAdapterDocToDto(session, adapterDocToDto))
                    .collect(Collectors.toList());
        }
    }
}
