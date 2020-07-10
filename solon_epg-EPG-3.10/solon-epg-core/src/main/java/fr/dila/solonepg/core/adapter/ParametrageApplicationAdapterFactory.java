package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.core.administration.ParametrageApplicationImpl;

/**
 * Adapteur de document vers ParametrageApplication.
 * 
 * @author arolin
 */
public class ParametrageApplicationAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParametrageApplicationImpl(doc);
	}

}
