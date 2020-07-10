package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.core.cases.RetourDilaImpl;

/**
 * Adapteur de DocumentModel vers {@link RetourDila}. (gestion des retours de la dila)
 * 
 * @author asatre
 */
public class RetourDilaAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(RetourDilaConstants.RETOUR_DILA_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ RetourDilaConstants.RETOUR_DILA_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new RetourDilaImpl(doc);
	}

}
