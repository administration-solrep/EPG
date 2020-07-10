package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.core.fondedossier.FondDeDossierInstanceImpl;

/**
 * Adapteur de document vers FondDeDossierInstance.
 * 
 * @author arolin
 */
public class FondDeDossierInstanceAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new FondDeDossierInstanceImpl(doc);
	}

}
