package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.core.domain.FileSolonMgppImpl;

/**
 * Adapteur de DocumentModel vers {@link FileSolonMgpp}.
 * 
 * @author asatre
 */
public class FileSolonMgppAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!FileSolonMgpp.FILE_SOLON_MGPP_DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type "
					+ FileSolonMgpp.FILE_SOLON_MGPP_DOC_TYPE);
		}

		return new FileSolonMgppImpl(doc);
	}

}
