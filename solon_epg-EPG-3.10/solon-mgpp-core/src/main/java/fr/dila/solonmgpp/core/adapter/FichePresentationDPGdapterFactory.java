package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.core.domain.FichePresentationDPGImpl;

public class FichePresentationDPGdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationDPG.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationDPG.DOC_TYPE);
		}

		return new FichePresentationDPGImpl(doc);
	}

}
