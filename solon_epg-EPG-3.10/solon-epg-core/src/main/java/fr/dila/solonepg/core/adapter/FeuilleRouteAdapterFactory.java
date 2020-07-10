package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants.ExecutionTypeValues;
import fr.dila.ecm.platform.routing.core.impl.ParallelRunner;
import fr.dila.ecm.platform.routing.core.impl.SerialRunner;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.core.feuilleroute.SolonEpgFeuilleRouteImpl;
import fr.dila.ss.core.schema.StepFolderSchemaUtils;
import fr.dila.st.api.constant.STConstant;

/**
 * Adaptateur de Document vers FeuilleRoute.
 * 
 * @author jtremeaux
 */
public class FeuilleRouteAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        String type = doc.getType();
        if (STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equalsIgnoreCase(type)) {
            ExecutionTypeValues executionType = StepFolderSchemaUtils.getExecutionType(doc);
            switch (executionType) {
            case serial:
                return new SolonEpgFeuilleRouteImpl(doc, new SerialRunner());
            case parallel:
                return new SolonEpgFeuilleRouteImpl(doc, new ParallelRunner());
            default : return null;
            }
        } else if(SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equalsIgnoreCase(type)) {
        	return new SolonEpgFeuilleRouteImpl(doc, new SerialRunner());
        }

        return null;
	}
	
}
