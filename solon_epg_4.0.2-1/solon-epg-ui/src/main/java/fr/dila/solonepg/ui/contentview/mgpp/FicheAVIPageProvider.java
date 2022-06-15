package fr.dila.solonepg.ui.contentview.mgpp;

import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

public class FicheAVIPageProvider extends DocumentModelFiltrablePaginatedPageDocumentProvider {
    private static final long serialVersionUID = -1657371098274769649L;

    private boolean dateFinAdded;

    public FicheAVIPageProvider() {
        super();
    }

    @Override
    protected StringBuilder completeQueryIfNeeded(StringBuilder query) {
        if (query.toString().contains("fpa.fpavi:dateFin")) {
            dateFinAdded = false;
        } else {
            addCorrectTokenBeforeParam(query);
            query.append(" fpa.fpavi:dateFin IS NULL OR fpa.fpavi:dateFin > ? ");
            dateFinAdded = true;
        }
        return query;
    }

    @Override
    protected StringBuilder getQuery(StringBuilder query) {
        String queryString = "ufnxql: SELECT fpa.ecm:uuid as id FROM FichePresentationAVI as fpa ";
        return new StringBuilder(
            NXQLQueryBuilder.getQuery(queryString, getParameters(), false, false, this.searchDocumentModel)
        );
    }

    @Override
    protected Collection<? extends Object> getOtherParams() {
        List<Object> list = new ArrayList<>();
        if (dateFinAdded) {
            list.add(Calendar.getInstance());
        }
        return list;
    }
}
