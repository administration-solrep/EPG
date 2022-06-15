package fr.dila.solonepg.core.filter;

import fr.dila.ss.api.constant.SSConstant;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

/**
 * Ce filtre conserve uniquement les Document de type STRouteStep
 *
 * @author arolin
 */
public class RouteStepFilter implements Filter {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean accept(DocumentModel doc) {
        if (!SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(doc.getType())) {
            return false;
        }
        return true;
    }
}
