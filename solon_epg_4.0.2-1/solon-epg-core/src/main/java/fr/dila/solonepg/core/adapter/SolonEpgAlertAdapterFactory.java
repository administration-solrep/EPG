package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.core.alert.SolonEpgAlertImpl;
import fr.dila.st.api.constant.STAlertConstant;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers SolonEpgAlert.
 *
 * @author arolin
 */
public class SolonEpgAlertAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, STAlertConstant.ALERT_SCHEMA);
        return new SolonEpgAlertImpl(doc);
    }
}
