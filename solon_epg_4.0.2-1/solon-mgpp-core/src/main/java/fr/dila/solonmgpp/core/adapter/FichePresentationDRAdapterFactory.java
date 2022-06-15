package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationDR}.
 *
 * @author asatre
 */
public class FichePresentationDRAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationDRImpl.DOC_TYPE);

        return new FichePresentationDRImpl(doc);
    }
}
