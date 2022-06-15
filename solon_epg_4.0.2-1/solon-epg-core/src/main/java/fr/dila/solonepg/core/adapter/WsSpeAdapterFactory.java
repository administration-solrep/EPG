package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.spe.WsSpe;
import fr.dila.solonepg.core.spe.WsSpeImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link WsSpe}.
 *
 * @author arolin
 */
public class WsSpeAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgSchemaConstant.SPE_SCHEMA);
        return new WsSpeImpl(doc);
    }
}
