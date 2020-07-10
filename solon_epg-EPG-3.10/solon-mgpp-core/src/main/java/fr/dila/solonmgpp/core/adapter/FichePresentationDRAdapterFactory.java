package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationDR}.
 * 
 * @author asatre
 */
public class FichePresentationDRAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationDR.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationDR.DOC_TYPE);
		}

		return new FichePresentationDRImpl(doc);
	}

}
