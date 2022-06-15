package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.core.domain.FicheLoiImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FicheLoi}.
 *
 * @author asatre
 */
public class FicheLoiAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FicheLoi.DOC_TYPE);

        return new FicheLoiImpl(doc);
    }
}
