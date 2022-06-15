package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.parapheur.ParapheurModelImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers Parapheur.
 *
 * @author arolin
 */
public class ParapheurModelAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_SCHEMA);
        return new ParapheurModelImpl(doc);
    }
}
