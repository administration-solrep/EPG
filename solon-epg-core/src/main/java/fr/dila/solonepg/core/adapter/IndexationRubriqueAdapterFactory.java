package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.core.administration.IndexationRubriqueImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers IndexationRubrique.
 *
 * @author asatre
 */
public class IndexationRubriqueAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA);
        return new IndexationRubriqueImpl(doc);
    }
}
