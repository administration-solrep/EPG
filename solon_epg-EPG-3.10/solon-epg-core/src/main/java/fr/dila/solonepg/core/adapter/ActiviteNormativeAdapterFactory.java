package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.cases.ActiviteNormativeImpl;

/**
 * Adapteur de DocumentModel vers {@link ActiviteNormative}.
 * 
 * @author asatre
 */
public class ActiviteNormativeAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ActiviteNormativeImpl(doc);
	}

}
