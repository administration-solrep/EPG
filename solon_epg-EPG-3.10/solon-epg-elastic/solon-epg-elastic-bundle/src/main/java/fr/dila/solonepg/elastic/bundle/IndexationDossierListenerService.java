package fr.dila.solonepg.elastic.bundle;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.model.NoSuchDocumentException;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;

/**
 * Traitement d'un événement d'indexation
 */
public class IndexationDossierListenerService extends DefaultComponent implements IIndexationDossierListenerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexationDossierListenerService.class);

	private static final String[] EVENTS = new String[] {
			DocumentEventTypes.DOCUMENT_CREATED,
			DocumentEventTypes.DOCUMENT_UPDATED,
			DocumentEventTypes.DOCUMENT_REMOVED,
			DocumentEventTypes.VERSION_REMOVED,
			DocumentEventTypes.DOCUMENT_SECURITY_UPDATED,
	};

	private IIndexationPersistenceService indexationPersistenceService;

	public IndexationDossierListenerService() {
		super();
	}

	@Override
	public void handleEvent(EventBundle events) throws ClientException {
		// L'ouverture de la transaction est gérée par l'appelant
		
		Set<DocumentModel> dossiersModifies = new HashSet<DocumentModel>();
		Set<DocumentModel> dossiersSupprimes = new HashSet<DocumentModel>();
		for (Event event : events) {
			if (ArrayUtils.contains(EVENTS, event.getName())) {
				handleEvent(event, dossiersModifies, dossiersSupprimes);
			}
		}
		if (LOGGER.isInfoEnabled() && ! dossiersModifies.isEmpty()) {
			LOGGER.info(String.format("indexation post-commit event: %d dossier(s) marqué(s)", dossiersModifies.size()));
		}
		if (LOGGER.isDebugEnabled() && dossiersModifies.isEmpty()) {
			LOGGER.debug("indexation post-commit event: aucun dossier marqué");
		}
		for (DocumentModel dossierSupprime : dossiersSupprimes) {
			if (dossiersModifies.contains(dossierSupprime)) {
				dossiersModifies.remove(dossierSupprime);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("indexation post-commit event: dossier marqué supprimé: %s", dossierSupprime));
			}
			indexationPersistenceService.markIndexationContinueSuppression(dossierSupprime);
		}
		for (DocumentModel dossierModifie : dossiersModifies) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("indexation post-commit event: dossier marqué: %s", dossierModifie));
			}
			indexationPersistenceService.markIndexationContinueModification(dossierModifie);
		}
	}

	protected void handleEvent(Event event, Collection<DocumentModel> dossiersModifies, Collection<DocumentModel> dossiersSupprimes) throws ClientException {
		EventContext ctx = event.getContext();
		if (ctx instanceof DocumentEventContext) {
			DocumentEventContext docCtx = (DocumentEventContext) ctx;
			DocumentModel doc = docCtx.getSourceDocument();
			CoreSession coreSession = docCtx.getCoreSession();

			try {
				filterEvent(coreSession, event, dossiersModifies, dossiersSupprimes, doc);
			} catch (ClientException e) {
				if (e.getCause() instanceof NoSuchDocumentException) {
					// catch if document is not existing
					// example: when documents are cleaned, they are updated then deleted
					LOGGER.info(String.format("Traitement de l'événement interrompu car document %s non existant", doc.getRef().reference()));
				} else {
					LOGGER.error("Error during message processing", e);
				}
			}
			return;
		}
	}

	private void filterEvent(CoreSession coreSession, Event event,
			Collection<DocumentModel> dossiersModifies, Collection<DocumentModel> dossiersSupprimes,
			DocumentModel doc) throws ClientException {
		if (DocumentEventTypes.DOCUMENT_REMOVED.equals(event.getName())
				&& doc.getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)) {
			dossiersSupprimes.add(doc);
		} else {
			DocumentRef ref = doc.getRef();
			DocumentModel document = coreSession.getDocument(ref);
			DocumentModel dossier = null;
			while (! document.getPath().isRoot()) {
				if (document.getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)) {
					dossier = document;
					break;
				} else {
					ref = document.getParentRef();
					document = coreSession.getDocument(ref);
				}
			}
			if (dossier != null) {
				dossiersModifies.add(dossier);
			}
		}
	}

	@Override
	public void activate(ComponentContext arg0) throws Exception {
		indexationPersistenceService = Framework.getService(IIndexationPersistenceService.class);
	}

	@Override
	public void deactivate(ComponentContext arg0) throws Exception {
		indexationPersistenceService = null;
	}

}
