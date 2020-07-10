package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.core.domain.FichePresentationIEImpl;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationIE}.
 * 
 * @author asatre
 */
public class FichePresentationIEAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationIE.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationIE.DOC_TYPE);
		}

		return new FichePresentationIEImpl(doc);
	}

}
