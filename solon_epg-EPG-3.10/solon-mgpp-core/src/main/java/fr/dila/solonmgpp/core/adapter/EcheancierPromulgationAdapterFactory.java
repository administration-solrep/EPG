package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.EcheancierPromulgation;
import fr.dila.solonmgpp.core.domain.EcheancierPromulgationImpl;

/**
 * Adapteur de DocumentModel vers {@link EcheancierPromulgation}.
 * 
 * @author admin
 */
public class EcheancierPromulgationAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!EcheancierPromulgation.ECHANCIER_PROMULGATION_DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type "
					+ EcheancierPromulgation.ECHANCIER_PROMULGATION_DOC_TYPE);
		}

		return new EcheancierPromulgationImpl(doc);
	}

}
