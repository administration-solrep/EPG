package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.documentmodel.FileSolonEpgImpl;

/**
 * Adapteur de document vers FileSolonEpg.
 * 
 * @author arolin
 */
public class FileSolonEpgAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new FileSolonEpgImpl(doc);
	}
}
