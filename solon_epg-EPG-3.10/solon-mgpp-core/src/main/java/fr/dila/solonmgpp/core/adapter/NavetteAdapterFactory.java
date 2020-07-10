package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.core.domain.NavetteImpl;

/**
 * Adapteur de DocumentModel vers {@link Navette}.
 * 
 * @author asatre
 */
public class NavetteAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!Navette.DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + Navette.DOC_TYPE);
		}

		return new NavetteImpl(doc);
	}

}
