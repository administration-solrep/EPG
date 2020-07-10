package fr.dila.solonepg.elastic.bundle.operation;

import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;

@Operation(id = IndexationFixOperation.ID, label = "Fix des indexations")
public class IndexationFixOperation extends AbstractIndexationOperation {

	public final static String ID = "SolonEpg.Indexation.fix";

	protected static final Log LOG = LogFactory.getLog(IndexationFixOperation.class);

	/**
	 * true pour indexer tout document qui est manquant dans l'index. Sinon, aucune indexation massive n'est lancée.
	 * Ce traitement sera géré par la prochaine indexation massive.
	 */
	@Param(name = "index", required = true)
	protected String index;

	/**
	 * true pour supprimer les index surnuméraires. Sinon, aucune suppression n'est demandée. Ce traitement est
	 * réalisé par l'indexation continue.
	 */
	@Param(name = "clean", required = true)
	protected String clean;

	@OperationMethod
	public void run() throws Exception {
		// transaction handled manually by called methods
		TransactionHelper.commitOrRollbackTransaction();
		HashSet<String> idsManquants = new HashSet<String>();
		HashSet<String> idsSupplementaires = new HashSet<String>();
		statusReport(idsManquants, idsSupplementaires);
		IIndexationPersistenceService indexationPersistenceService = Framework.getService(IIndexationPersistenceService.class);
		
		if (!idsManquants.isEmpty() && Boolean.TRUE.toString().equals(index.toLowerCase())) {
			StringBuilder ids = new StringBuilder();
			boolean first = true;
			indexationPersistenceService.markIndexationMassive(idsManquants);
			for (String id : idsManquants) {
				if (first) {
					first = false;
				} else {
					ids.append(",");
				}
				ids.append(id);
			}
			LOG.warn(String.format("%d dossiers manquants marqués à indexer: %s", idsManquants.size(), ids.toString()));
		}
		if (!idsSupplementaires.isEmpty() && Boolean.TRUE.toString().equals(clean.toLowerCase())) {
			StringBuilder ids = new StringBuilder();
			boolean first = true;
			for (String id : idsSupplementaires) {
				indexationPersistenceService.markTransactionalIndexationContinueSuppression(id);
				if (first) {
					first = false;
				} else {
					ids.append(",");
				}
				ids.append(id);
			}
			LOG.warn(String.format("%d dossiers surnuméraires marqués à désindexer: %s", idsSupplementaires.size(), ids.toString()));
		}
	}


}
