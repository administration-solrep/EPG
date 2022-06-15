package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.core.domain.FichePresentationJSSImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationJSSdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationJSS.DOC_TYPE);

        return new FichePresentationJSSImpl(doc);
    }
}
