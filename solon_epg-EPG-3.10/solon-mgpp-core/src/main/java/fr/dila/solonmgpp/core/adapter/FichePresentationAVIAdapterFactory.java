package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.core.domain.FichePresentationAVIImpl;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationAVI}.
 * 
 * @author asatre
 */
public class FichePresentationAVIAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentationAVI.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentationAVI.DOC_TYPE);
		}

		return new FichePresentationAVIImpl(doc);
	}

}
