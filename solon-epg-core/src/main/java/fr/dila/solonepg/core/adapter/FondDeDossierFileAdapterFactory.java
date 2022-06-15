package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.core.fondedossier.FondDeDossierFileImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers FondDeDossierFile.
 *
 */
public class FondDeDossierFileAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
        checkDocumentSchemas(doc, SolonEpgSchemaConstant.FILE_SOLON_EPG_SCHEMA);
        return new FondDeDossierFileImpl(doc);
    }
}
