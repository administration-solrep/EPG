package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.core.cases.ConseilEtatImpl;

/**
 * Adapteur de DocumentModel vers {@link ConseilEtat}.
 * 
 * @author asatre
 */
public class ConseilEtatAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(ConseilEtatConstants.CONSEIL_ETAT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ ConseilEtatConstants.CONSEIL_ETAT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ConseilEtatImpl(doc);
	}

}
