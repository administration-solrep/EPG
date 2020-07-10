package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationDecret}.
 * 
 * @author asatre
 */
public class FichePresentationDecretAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationDecret.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationDecret.DOC_TYPE);
		}

		return new FichePresentationDecretImpl(doc);
	}

}
