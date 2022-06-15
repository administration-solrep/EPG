package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.documentmodel.FileSolonEpgImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers FileSolonEpg.
 *
 * @author arolin
 */
public class FileSolonEpgAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA);
        return new FileSolonEpgImpl(doc);
    }
}
