package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.TexteSignale;
import fr.dila.solonepg.api.constant.TexteSignaleConstants;
import fr.dila.solonepg.core.cases.TexteSignaleImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link TexteSignale}.
 *
 * @author asatre
 */
public class TexteSignaleAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA);
        return new TexteSignaleImpl(doc);
    }
}
