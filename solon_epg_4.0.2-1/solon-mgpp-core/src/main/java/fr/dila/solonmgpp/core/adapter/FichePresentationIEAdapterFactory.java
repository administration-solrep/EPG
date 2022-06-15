package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.core.domain.FichePresentationIEImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FichePresentationIE}.
 *
 * @author asatre
 */
public class FichePresentationIEAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationIE.DOC_TYPE);

        return new FichePresentationIEImpl(doc);
    }
}
