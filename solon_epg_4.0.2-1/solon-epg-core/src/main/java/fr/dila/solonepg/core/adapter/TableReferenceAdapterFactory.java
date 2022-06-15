package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.core.administration.TableReferenceImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers TableReference.
 *
 * @author asatre
 */
public class TableReferenceAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA);
        return new TableReferenceImpl(doc);
    }
}
