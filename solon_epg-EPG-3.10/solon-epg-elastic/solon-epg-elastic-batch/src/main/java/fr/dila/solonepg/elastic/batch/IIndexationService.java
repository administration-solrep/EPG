package fr.dila.solonepg.elastic.batch;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.elastic.models.indexing.IndexationDossierStatistics;

public interface IIndexationService {

	List<String> listDossiersIdsAIndexer() throws ClientException;

	void doIndexDossier(String dossierUid);

	void doUnindexDossier(String dossierId);

	IndexationDossierStatistics doIndexDossier(CoreSession session, String dossierId, boolean deletion);

}
