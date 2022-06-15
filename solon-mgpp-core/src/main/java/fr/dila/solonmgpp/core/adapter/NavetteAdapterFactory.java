package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.core.domain.NavetteImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link Navette}.
 *
 * @author asatre
 */
public class NavetteAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, Navette.DOC_TYPE);

        return new NavetteImpl(doc);
    }
}
