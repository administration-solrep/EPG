package fr.dila.solonepg.ui.contentview.mgpp;

import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

public class FicheOEPPageProvider extends DocumentModelFiltrablePaginatedPageDocumentProvider {
    private static final long serialVersionUID = 3736987857992414844L;

    private boolean dateFinAdded;

    public FicheOEPPageProvider() {
        super();
    }

    @Override
    protected StringBuilder completeQueryIfNeeded(StringBuilder query) {
        if (query.toString().contains("fpo.fpoep:dateFin")) {
            dateFinAdded = false;
        } else {
            addCorrectTokenBeforeParam(query);
            query.append(" fpo.fpoep:dateFin IS NULL OR fpo.fpoep:dateFin > ? ");
            dateFinAdded = true;
        }
        return query;
    }

    @Override
    protected StringBuilder getQuery(StringBuilder query) {
        String queryString = "ufnxql: SELECT fpo.ecm:uuid as id FROM FichePresentationOEP as fpo ";
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
