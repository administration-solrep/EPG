package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.cases.DossiersSignalesImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DossiersSignalesAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, DossierSolonEpgConstants.DOSSIERS_SIGNALES_SCHEMA);
        return new DossiersSignalesImpl(doc);
    }
}
