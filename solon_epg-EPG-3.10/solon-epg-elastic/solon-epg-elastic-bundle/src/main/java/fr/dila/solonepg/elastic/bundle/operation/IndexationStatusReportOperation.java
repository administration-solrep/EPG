package fr.dila.solonepg.elastic.bundle.operation;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.mapping.ISearchRequestMapper;
import fr.dila.solonepg.elastic.mapping.SearchRequestMapper;
import fr.dila.solonepg.elastic.models.ElasticHit;
import fr.dila.solonepg.elastic.models.search.SearchResponse;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;
import fr.dila.solonepg.elastic.services.RequeteurService;

@Operation(id = IndexationStatusReportOperation.ID, label = "Statut des indexations")
public class IndexationStatusReportOperation extends AbstractIndexationOperation {

	public final static String ID = "SolonEpg.Indexation.statusReport";

	protected static final Log LOG = LogFactory.getLog(IndexationStatusReportOperation.class);

	@OperationMethod
	public void run() throws Exception {
		// transaction handled manually by called methods
		TransactionHelper.commitOrRollbackTransaction();
		HashSet<String> idsManquants = new HashSet<String>();
		HashSet<String> idsSupplementaires = new HashSet<String>();
		statusReport(idsManquants, idsSupplementaires);
		
		if (!idsManquants.isEmpty()) {
			StringBuilder ids = new StringBuilder();
			boolean first = true;
			for (String id : idsManquants) {
				if (first) {
					first = false;
				} else {
					ids.append(",");
				}
				ids.append(id);
			}
			LOG.warn(String.format("%d dossiers manquants dans l'index: %s", idsManquants.size(), ids.toString()));
		}
		if (!idsSupplementaires.isEmpty()) {
			StringBuilder ids = new StringBuilder();
			boolean first = true;
			for (String id : idsSupplementaires) {
				if (first) {
					first = false;
				} else {
					ids.append(",");
				}
				ids.append(id);
			}
			LOG.warn(String.format("%d dossiers surnuméraires dans l'index: %s", idsSupplementaires.size(), ids.toString()));
		}
	}


}
