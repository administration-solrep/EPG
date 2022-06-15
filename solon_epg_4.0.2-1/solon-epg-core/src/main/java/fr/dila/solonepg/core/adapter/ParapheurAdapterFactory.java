package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.core.parapheur.ParapheurImpl;
import fr.dila.ss.api.constant.SSParapheurConstants;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers Parapheur.
 *
 * @author arolin
 */
public class ParapheurAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, SSParapheurConstants.PARAPHEUR_DOCUMENT_TYPE);
        return new ParapheurImpl(doc);
    }
}
