package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.core.domain.RepresentantAUDImpl;

public class RepresentantAUDAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!RepresentantAUD.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + RepresentantAUD.DOC_TYPE);
		}

		return new RepresentantAUDImpl(doc);
	}

}
