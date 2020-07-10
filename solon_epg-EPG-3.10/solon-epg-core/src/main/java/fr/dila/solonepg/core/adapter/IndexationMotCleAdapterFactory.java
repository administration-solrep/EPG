package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.core.administration.IndexationMotCleImpl;

/**
 * Adapteur de DocumentModel vers IndexationRubrique.
 * 
 * @author asatre
 */
public class IndexationMotCleAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
		checkDocument(doc);
		return new IndexationMotCleImpl(doc);
	}

}
