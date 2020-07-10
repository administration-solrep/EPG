package fr.dila.solonepg.core.filter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

import fr.dila.st.api.constant.STConstant;

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
               
        if (!STConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(doc.getType()) ) {
            return false;
        }
        return true;
    }

}
