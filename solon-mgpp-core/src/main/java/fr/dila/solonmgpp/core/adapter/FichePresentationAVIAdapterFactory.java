package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.core.domain.FichePresentationAVIImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationAVI}.
 *
 * @author asatre
 */
public class FichePresentationAVIAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationAVI.DOC_TYPE);

        return new FichePresentationAVIImpl(doc);
    }
}
