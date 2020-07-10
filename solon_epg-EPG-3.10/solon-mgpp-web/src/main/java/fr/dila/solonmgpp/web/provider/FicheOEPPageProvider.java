package fr.dila.solonmgpp.web.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

import fr.dila.solonepg.web.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;

public class FicheOEPPageProvider extends DocumentModelFiltrablePaginatedPageDocumentProvider {

    private static final long serialVersionUID = 3736987857992414844L;

    private Boolean dateFinAdded = Boolean.FALSE;

    @Override
    protected StringBuilder completeQueryIfNeeded(StringBuilder query) {
        if (query.toString().contains("fpo.fpoep:dateFin")) {
            dateFinAdded = Boolean.FALSE;
        } else {
            if (query.toString().toUpperCase().contains("WHERE")) {
                query.append(" AND ");
            } else {
                query.append(" WHERE ");
            }
            query.append(" fpo.fpoep:dateFin IS NULL OR fpo.fpoep:dateFin > ? ");
            dateFinAdded = Boolean.TRUE;
        }
        return query;
    }

    @Override
    protected StringBuilder getQuery(StringBuilder query) throws ClientException {
        String queryString = "ufnxql: SELECT fpo.ecm:uuid as id FROM FichePresentationOEP as fpo ";
        return new StringBuilder(NXQLQueryBuilder.getQuery(queryString, getParameters(), Boolean.FALSE, Boolean.FALSE));
    }

    @Override
    protected Collection<? extends Object> getOtherParams() {

        List<Object> list = new ArrayList<Object>();
        if (dateFinAdded) {
            list.add(Calendar.getInstance());
        }
        return list;
    }
}
