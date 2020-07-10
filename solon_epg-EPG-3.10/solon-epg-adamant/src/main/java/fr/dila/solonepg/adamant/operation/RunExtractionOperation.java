package fr.dila.solonepg.adamant.operation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Opération qui lance le batch d'extraction (évite d'avoir à lancer
 * l'événement).
 * 
 * Exemple d'appel dans le nxshell : SolonEpg.Extraction.Run
 * 
 * @author tlombard
 */
@Operation(id = RunExtractionOperation.ID, label = "Vérification éligibilité et création de lot pour archivage", description = RunExtractionOperation.DESCRIPTION)
public class RunExtractionOperation {
	public static final String ID = "SolonEpg.Extraction.Run";

	public static final String DESCRIPTION = "Cette opération lance le batch d'extraction avec les paramètres standards";

	public static final String LABEL = "Lancement du batch d'extraction";
	
	private static final STLogger LOGGER		= STLogFactory.getLog(RunExtractionOperation.class);

	@Context
	protected CoreSession session;

	@OperationMethod
	public void run() throws ClientException {
		LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Lancement manuel de l'extraction Adamant par l'opération Nuxeo Shell");
		EventProducer eventProducer = STServiceLocator.getEventProducer();
		Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
		InlineEventContext inlineEventContext = new InlineEventContext(session, session.getPrincipal(),
				eventProperties);
		eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.BATCH_EVENT_EXTRACTION_ADAMANT));
	}
}
