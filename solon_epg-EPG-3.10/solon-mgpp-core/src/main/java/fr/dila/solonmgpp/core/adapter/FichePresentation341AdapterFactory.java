package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.core.domain.FichePresentation341Impl;

/**
 * Adapteur de DocumentModel vers {@link FichePresentation341}.
 * 
 * @author asatre
 */
public class FichePresentation341AdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FichePresentation341.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + FichePresentation341.DOC_TYPE);
		}

		return new FichePresentation341Impl(doc);
	}

}
