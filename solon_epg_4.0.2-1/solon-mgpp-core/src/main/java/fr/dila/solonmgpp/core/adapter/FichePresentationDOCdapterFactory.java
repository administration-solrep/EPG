package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.core.domain.FichePresentationDOCImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationDOCdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationDOC.DOC_TYPE);

        return new FichePresentationDOCImpl(doc);
    }
}
