package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.core.domain.FichePresentationAUDImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationAUDdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationAUD.DOC_TYPE);

        return new FichePresentationAUDImpl(doc);
    }
}
