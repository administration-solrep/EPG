package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.solonepg.core.administration.ParametrageAdamantImpl;

/**
 * Adapteur de document vers ParametrageAdamant.
 * 
 * @author bbe
 */
public class ParametrageAdamantAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParametrageAdamantImpl(doc);
	}

}
