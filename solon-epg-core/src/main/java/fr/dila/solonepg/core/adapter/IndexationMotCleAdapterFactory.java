package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.core.administration.IndexationMotCleImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers IndexationRubrique.
 *
 * @author asatre
 */
public class IndexationMotCleAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
        checkDocumentSchemas(doc, SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA);
        return new IndexationMotCleImpl(doc);
    }
}
