package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.constant.SolonMgppNotificationConstants;
import fr.dila.solonmgpp.core.domain.NotificationDocImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class NotificationAdapterFactory implements STDocumentAdapterFactory {

    public NotificationAdapterFactory() {
        // Default constructor
    }

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, SolonMgppNotificationConstants.NOTIFICATION_DOC_TYPE);

        return new NotificationDocImpl(doc);
    }
}
