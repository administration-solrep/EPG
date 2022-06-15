package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationOEP}.
 *
 * @author asatre
 */
public class FichePresentationOEPAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationOEPImpl.DOC_TYPE);

        return new FichePresentationOEPImpl(doc);
    }
}
