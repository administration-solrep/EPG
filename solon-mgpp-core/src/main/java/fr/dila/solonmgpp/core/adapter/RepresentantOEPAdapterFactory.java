package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.core.domain.RepresentantOEPImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link RepresentantOEP}.
 *
 * @author asatre
 */
public class RepresentantOEPAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, RepresentantOEPImpl.DOC_TYPE);

        return new RepresentantOEPImpl(doc);
    }
}
