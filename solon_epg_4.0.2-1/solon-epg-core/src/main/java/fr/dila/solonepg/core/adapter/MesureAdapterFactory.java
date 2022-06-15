package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.core.cases.MesureApplicativeImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link MesureApplicative}.
 *
 * @author asatre
 */
public class MesureAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA);
        return new MesureApplicativeImpl(doc);
    }
}
