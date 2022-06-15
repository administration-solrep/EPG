package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.cases.DossierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers Dossier.
 *
 * @author arolin
 */
public class DossierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, DossierSolonEpgConstants.DOSSIER_SCHEMA);
        return new DossierImpl(doc);
    }
}
