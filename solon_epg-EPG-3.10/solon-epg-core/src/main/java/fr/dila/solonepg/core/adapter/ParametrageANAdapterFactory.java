package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgParametresANConstants;
import fr.dila.solonepg.core.administration.ParametrageANImpl;

public class ParametrageANAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgParametresANConstants.SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgParametresANConstants.SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> itf) {
		checkDocument(doc);
		return new ParametrageANImpl(doc);
	}

}
