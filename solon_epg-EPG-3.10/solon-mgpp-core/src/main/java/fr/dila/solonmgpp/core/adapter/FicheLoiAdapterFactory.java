package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.core.domain.FicheLoiImpl;

/**
 * Adapteur de DocumentModel vers {@link FicheLoi}.
 * 
 * @author asatre
 */
public class FicheLoiAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FicheLoi.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FicheLoi.DOC_TYPE);
		}

		return new FicheLoiImpl(doc);
	}

}
