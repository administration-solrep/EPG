package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.core.cases.LigneProgrammationImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link LigneProgrammation}.
 *
 * @author asatre
 */
public class LigneProgrammationAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA);
        return new LigneProgrammationImpl(doc);
    }
}
