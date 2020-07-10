package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.core.domain.FichePresentationJSSImpl;

public class FichePresentationJSSdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationJSS.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationJSS.DOC_TYPE);
		}

		return new FichePresentationJSSImpl(doc);
	}

}
