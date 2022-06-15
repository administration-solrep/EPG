package fr.dila.solonepg.ui.contentview.mgpp;

import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.util.DateUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

public class FicheLoiPageProvider extends DocumentModelFiltrablePaginatedPageDocumentProvider {
    private static final long serialVersionUID = -1657371098274769649L;

    private boolean dateCreationAdded;

    public FicheLoiPageProvider() {
        super();
    }

    @Override
    protected StringBuilder completeQueryIfNeeded(StringBuilder query) {
        if (query.toString().contains("fl.floi:dateCreation")) {
            dateCreationAdded = false;
        } else {
            addCorrectTokenBeforeParam(query);
            query.append(" fl.floi:dateCreation > ? ");
            dateCreationAdded = true;
        }
        return query;
    }

    @Override
    protected StringBuilder getQuery(StringBuilder query) {
        String queryString = "ufnxql: SELECT fl.ecm:uuid as id FROM FicheLoi as fl ";
        return new StringBuilder(
            NXQLQueryBuilder.getQuery(queryString, getParameters(), false, false, this.searchDocumentModel)
        );
    }

    @Override
    protected Collection<? extends Object> getOtherParams() {
        List<Object> list = new ArrayList<>();
        final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator
            .getParametreMgppService()
            .findParametrageMgpp(this.getCoreSession());
        Long iFiltre = parametrageMgpp.getFiltreDateCreationLoi();
        Calendar date = DateUtil.removeMonthsToNow(iFiltre.intValue());

        if (dateCreationAdded) {
            list.add(date);
        }
        return list;
    }
}
