package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.constant.SolonMgppNotificationConstants;
import fr.dila.solonmgpp.core.domain.NotificationDocImpl;

public class NotificationAdapterFactory implements DocumentAdapterFactory {

	public NotificationAdapterFactory() {
		super();
		// Default constructor
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type "
					+ SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE);
		}

		return new NotificationDocImpl(doc);
	}

}
