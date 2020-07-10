package fr.dila.solonepg.core.event.helper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.service.STServiceLocator;

/**
 * utility class used to raise CURRENT_DOCUMENT_SEARCH_CHANGED_EVENT event 
 * @author SPL
 *
 */
public final class DocumentSearchEventHelper {

	/**
	 * Utilty class
	 */
	private DocumentSearchEventHelper(){
		// do nothing
	}
	
	public static void raiseEvent(final CoreSession session, final DocumentModel documentModel, final String resultatConsulteRootPath) throws ClientException {
		final EspaceRechercheService.DocKind kind = docToKind(documentModel);
		if(kind == null){
			return;
		}
	    final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
        eventProperties.put(SolonEpgEventConstant.RESULTAT_CONSULTE_TYPE, kind);
        eventProperties.put(SolonEpgEventConstant.RESULTAT_CONSULTE_ID, documentModel.getId());
        eventProperties.put(SolonEpgEventConstant.RESULTAT_CONSULTE_CURRENT_PATH, resultatConsulteRootPath);
        final InlineEventContext inlineEventContext = new InlineEventContext(session, session.getPrincipal(), eventProperties);
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.CURRENT_DOCUMENT_SEARCH_CHANGED_EVENT));
	}
	
	/**
	 * en fonction du documentModel, retrouve le type de resultat concerné
	 */
	protected static EspaceRechercheService.DocKind docToKind(final DocumentModel documentToAdd) {
		if (documentToAdd.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
			return EspaceRechercheService.DocKind.DOSSIER;
		} else if (documentToAdd.hasSchema(STSchemaConstant.FEUILLE_ROUTE_SCHEMA)) {
			return EspaceRechercheService.DocKind.FEUILLE_ROUTE;
		} else if (documentToAdd.hasSchema(STSchemaConstant.USER_SCHEMA)) {
			return EspaceRechercheService.DocKind.USER;
		}
		return null;
	}
	
}
