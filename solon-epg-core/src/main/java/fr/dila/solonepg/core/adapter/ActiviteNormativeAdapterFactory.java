package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.cases.ActiviteNormativeImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link ActiviteNormative}.
 *
 * @author asatre
 */
public class ActiviteNormativeAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA);
        return new ActiviteNormativeImpl(doc);
    }
}
