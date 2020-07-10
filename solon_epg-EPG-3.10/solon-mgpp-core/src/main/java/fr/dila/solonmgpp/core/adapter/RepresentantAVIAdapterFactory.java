package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;
import fr.dila.solonmgpp.core.domain.RepresentantAVIImpl;

/**
 * Adapteur de DocumentModel vers {@link RepresentantAVI}.
 * 
 * @author asatre
 */
public class RepresentantAVIAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!RepresentantAVI.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + RepresentantAVI.DOC_TYPE);
		}

		return new RepresentantAVIImpl(doc);
	}

}
