package fr.dila.solonmgpp.web.provider;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

import fr.dila.solonepg.web.suivi.EspaceSuiviTreeBean;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;

public class DossierPageProvider extends fr.dila.solonepg.web.contentview.DossierPageProvider {

    private static final long serialVersionUID = 1467451926272580726L;
    private List<Object> params;

    @Override
    protected String getQuery(SortInfo[] sortArray, PageProviderDefinition def) throws ClientException {

        params = new ArrayList<Object>();

        CorbeilleNode corbeilleNode = getCorbeilleTree().getCurrentItem();

        if (corbeilleNode == null) {
            return EspaceSuiviTreeBean.DEFAULT_QUERY;
        }

        StringBuilder builder = SolonMgppServiceLocator.getCorbeilleService().getDossierQuery(corbeilleNode, params);
        builder.append(NXQLQueryBuilder.getSortClause(sortArray));

        return builder.toString();
    }

    private CorbeilleTreeBean getCorbeilleTree() {
        return (CorbeilleTreeBean) getProperties().get("corbeilleTree");
    }

    @Override
    protected List<Object> getQueryParams() {
        params.addAll(filtrableParameters);
        return params;
    }
}
