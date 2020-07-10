package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.core.alert.SolonEpgAlertImpl;
import fr.dila.st.api.constant.STAlertConstant;

/**
 * Adapteur de document vers SolonEpgAlert.
 * 
 * @author arolin
 */
public class SolonEpgAlertAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(STAlertConstant.ALERT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema " + STAlertConstant.ALERT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new SolonEpgAlertImpl(doc);
	}
}
