package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.core.domain.FichePresentationAUDImpl;

public class FichePresentationAUDdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationAUD.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationAUD.DOC_TYPE);
		}

		return new FichePresentationAUDImpl(doc);
	}

}
