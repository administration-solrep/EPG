package fr.dila.solonepg.elastic.bundle;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.PostCommitEventListener;
import org.nuxeo.runtime.api.Framework;

/**
 * Ce listener trace les modifications pour déclencher le cas échéant l'indexation
 */
public class IndexationDossierListener implements PostCommitEventListener {

	public IndexationDossierListener() {
		super();
	}

	@Override
	public void handleEvent(EventBundle events) throws ClientException {
		try {
			// NOTE: Nuxeo ouvre une transaction pour traiter cet événement (async=false, postCommit=true)
			Framework.getService(IIndexationDossierListenerService.class).handleEvent(events);
		} catch (Exception e) {
			throw new ClientException("Erreur de traitement de l'événement post-commit", e);
		}
	}

}
