package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.parapheur.ParapheurInstanceImpl;

/**
 * Adapteur de document vers ParapheurInstance.
 * 
 * @author arolin
 */
public class ParapheurInstanceAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.getDocumentType().getName().equals(SolonEpgParapheurConstants.PARAPHEUR_DOCUMENT_TYPE)) {
			throw new CaseManagementRuntimeException("Document should contain type "
					+ SolonEpgParapheurConstants.PARAPHEUR_DOCUMENT_TYPE);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParapheurInstanceImpl(doc);
	}

}
