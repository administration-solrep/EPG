package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.constant.ActiviteNormativeProgrammationConstants;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;

/**
 * Adapteur de DocumentModel vers {@link ActiviteNormativeProgrammation}.
 * 
 * @author asatre
 */
public class ActiviteNormativeProgrammationAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ActiviteNormativeProgrammationImpl(doc);
	}

}
