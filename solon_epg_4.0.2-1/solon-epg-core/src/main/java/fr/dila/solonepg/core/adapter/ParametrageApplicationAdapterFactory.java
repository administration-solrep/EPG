package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.core.administration.ParametrageApplicationImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers ParametrageApplication.
 *
 * @author arolin
 */
public class ParametrageApplicationAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA);
        return new ParametrageApplicationImpl(doc);
    }
}
