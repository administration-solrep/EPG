package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.core.domain.FichePresentation341Impl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FichePresentation341}.
 *
 * @author asatre
 */
public class FichePresentation341AdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FichePresentation341.DOC_TYPE);

        return new FichePresentation341Impl(doc);
    }
}
