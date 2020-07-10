package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.core.fondedossier.FondDeDossierFileImpl;

/**
 * Adapteur de document vers FondDeDossierFile.
 * 
 */
public class FondDeDossierFileAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgSchemaConstant.FILE_SOLON_EPG_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgSchemaConstant.FILE_SOLON_EPG_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
		checkDocument(doc);
		return new FondDeDossierFileImpl(doc);
	}

}
