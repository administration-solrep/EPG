package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.core.domain.ParametrageMgppImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ParametrageMgppAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, ParametrageMgppImpl.DOC_TYPE);

        return new ParametrageMgppImpl(doc);
    }
}
