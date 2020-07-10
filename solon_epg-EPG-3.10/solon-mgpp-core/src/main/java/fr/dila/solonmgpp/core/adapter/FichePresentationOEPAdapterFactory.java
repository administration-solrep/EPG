package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationOEP}.
 * 
 * @author asatre
 */
public class FichePresentationOEPAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationOEP.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationOEP.DOC_TYPE);
		}

		return new FichePresentationOEPImpl(doc);
	}

}
