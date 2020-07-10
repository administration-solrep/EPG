package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.core.cases.LigneProgrammationImpl;

/**
 * Adapteur de DocumentModel vers {@link LigneProgrammation}.
 * 
 * @author asatre
 */
public class LigneProgrammationAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new LigneProgrammationImpl(doc);
	}

}
