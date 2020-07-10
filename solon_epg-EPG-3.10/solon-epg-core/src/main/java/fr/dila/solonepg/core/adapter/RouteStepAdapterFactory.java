package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.ecm.platform.routing.core.impl.StepElementRunner;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.core.feuilleroute.SolonEpgRouteStepImpl;
import fr.dila.st.api.constant.STConstant;

public class RouteStepAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		String type = doc.getType();
		if (STConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(type)
				|| SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(type)) {
			return new SolonEpgRouteStepImpl(doc, new StepElementRunner());
		}

		return null;
	}

}
