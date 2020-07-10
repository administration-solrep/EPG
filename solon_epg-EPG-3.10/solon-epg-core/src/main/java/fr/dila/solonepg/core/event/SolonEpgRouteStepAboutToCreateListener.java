package fr.dila.solonepg.core.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.core.event.RouteStepAboutToCreateListener;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;

/**
 * Surcharge du listener pour prendre en compte les route step de squelettes.
 * 
 * @author jtremeaux
 */
public class SolonEpgRouteStepAboutToCreateListener extends RouteStepAboutToCreateListener {

	/**
	 * Default constructor
	 */
	public SolonEpgRouteStepAboutToCreateListener() {
		super();
	}

	@Override
	protected void doHandleEvent(final Event event, final DocumentEventContext context) throws ClientException {

		// Traite uniquement les documents de type RouteStep
		final DocumentModel doc = context.getSourceDocument();
		final String docType = doc.getType();
		if (!STConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(docType)
				&& !SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(docType)) {
			return;
		}

		// Renseigne le nom du document à partir de son type d'étape
		final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();
		documentRoutingService.setRouteStepDocName(doc);
	}
}
