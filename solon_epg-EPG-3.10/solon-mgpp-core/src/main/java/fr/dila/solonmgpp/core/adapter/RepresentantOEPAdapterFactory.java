package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.core.domain.RepresentantOEPImpl;

/**
 * Adapteur de DocumentModel vers {@link RepresentantOEP}.
 * 
 * @author asatre
 */
public class RepresentantOEPAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!RepresentantOEP.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + RepresentantOEP.DOC_TYPE);
		}

		return new RepresentantOEPImpl(doc);
	}

}
