package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationDecret}.
 *
 * @author asatre
 */
public class FichePresentationDecretAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationDecretImpl.DOC_TYPE);

        return new FichePresentationDecretImpl(doc);
    }
}
