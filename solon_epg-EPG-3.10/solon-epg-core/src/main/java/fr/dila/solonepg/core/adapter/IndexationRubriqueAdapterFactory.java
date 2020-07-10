package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.core.administration.IndexationRubriqueImpl;

/**
 * Adapteur de DocumentModel vers IndexationRubrique.
 * 
 * @author asatre
 */
public class IndexationRubriqueAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new IndexationRubriqueImpl(doc);
	}

}
