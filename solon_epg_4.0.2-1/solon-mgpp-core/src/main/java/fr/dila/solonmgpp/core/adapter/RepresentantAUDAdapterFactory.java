package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.core.domain.RepresentantAUDImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class RepresentantAUDAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, RepresentantAUDImpl.DOC_TYPE);

        return new RepresentantAUDImpl(doc);
    }
}
