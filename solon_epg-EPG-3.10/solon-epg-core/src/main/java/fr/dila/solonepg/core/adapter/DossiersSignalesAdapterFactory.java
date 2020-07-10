package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.cases.DossiersSignalesImpl;

;

public class DossiersSignalesAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(DossierSolonEpgConstants.DOSSIERS_SIGNALES_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ DossierSolonEpgConstants.DOSSIERS_SIGNALES_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new DossiersSignalesImpl(doc);
	}

}
