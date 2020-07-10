package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.parapheur.ParapheurFileImpl;

/**
 * Adapteur de document vers la ParapheurFile (fichier de parapheur) .
 * 
 */
public class ParapheurFileAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (doc == null || !doc.hasSchema(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA)
				|| !SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document type should be "
					+ SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE + " and document should contain schema "
					+ DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParapheurFileImpl(doc);
	}
}
