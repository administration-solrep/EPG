package fr.dila.solonepg.elastic.batch;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;

public interface IIndexationDossiersBatch {

	void indexationMassive() throws ClientException;

	void indexationContinue(Event event);

	boolean isIndexationContinueEnabled();

}