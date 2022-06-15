package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.solonepg.core.administration.ParametrageAdamantImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers ParametrageAdamant.
 *
 * @author bbe
 */
public class ParametrageAdamantAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA);
        return new ParametrageAdamantImpl(doc);
    }
}
