package fr.dila.solonepg.elastic.batch;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.runtime.api.Framework;

/**
 * Ce listener lance un batch d'indexation.
 */
public class IndexationContinueListener implements EventListener {

	public IndexationContinueListener() {
		super();
	}

	@Override
	public void handleEvent(Event event) throws ClientException {
		IIndexationDossiersBatch indexationDossiersBatch = null;
		try {
			indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
		} catch (Exception e) {
			throw new ClientException(String.format("Erreur de récupération du service %s", IIndexationDossiersBatch.class.getName()));
		}
		if (indexationDossiersBatch.isIndexationContinueEnabled()) {
			indexationDossiersBatch.indexationContinue(event);
		}
	}
}
