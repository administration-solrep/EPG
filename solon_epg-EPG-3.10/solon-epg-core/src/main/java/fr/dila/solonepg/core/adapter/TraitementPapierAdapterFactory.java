package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.core.cases.TraitementPapierImpl;

public class TraitementPapierAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new TraitementPapierImpl(doc);
	}

}
