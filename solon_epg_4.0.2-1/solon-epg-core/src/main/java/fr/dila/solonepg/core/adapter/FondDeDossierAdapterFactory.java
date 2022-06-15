package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.core.fondedossier.FondDeDossierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers FondDeDossier.
 *
 * @author arolin
 */
public class FondDeDossierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA);
        return new FondDeDossierImpl(doc);
    }
}
