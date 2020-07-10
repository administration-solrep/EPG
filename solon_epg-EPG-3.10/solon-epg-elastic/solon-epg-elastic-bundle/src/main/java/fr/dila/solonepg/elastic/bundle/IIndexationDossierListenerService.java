package fr.dila.solonepg.elastic.bundle;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

public interface IIndexationDossierListenerService extends PostCommitEventListener {

	void handleEvent(EventBundle events) throws ClientException;

}