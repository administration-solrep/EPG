package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.RepresentantAVI;
import fr.dila.solonmgpp.core.domain.RepresentantAVIImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link RepresentantAVI}.
 *
 * @author asatre
 */
public class RepresentantAVIAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, RepresentantAVI.DOC_TYPE);

        return new RepresentantAVIImpl(doc);
    }
}
