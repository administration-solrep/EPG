package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.EcheancierPromulgation;
import fr.dila.solonmgpp.core.domain.EcheancierPromulgationImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link EcheancierPromulgation}.
 *
 * @author admin
 */
public class EcheancierPromulgationAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, EcheancierPromulgation.ECHANCIER_PROMULGATION_DOC_TYPE);

        return new EcheancierPromulgationImpl(doc);
    }
}
