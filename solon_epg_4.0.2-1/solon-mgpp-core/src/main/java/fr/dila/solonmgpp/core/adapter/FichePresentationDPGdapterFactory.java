package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.core.domain.FichePresentationDPGImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationDPGdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentationDPG.DOC_TYPE);

        return new FichePresentationDPGImpl(doc);
    }
}
