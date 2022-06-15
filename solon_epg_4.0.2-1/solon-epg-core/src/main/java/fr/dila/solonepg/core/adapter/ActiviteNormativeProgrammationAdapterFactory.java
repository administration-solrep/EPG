package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.constant.ActiviteNormativeProgrammationConstants;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link ActiviteNormativeProgrammation}.
 *
 * @author asatre
 */
public class ActiviteNormativeProgrammationAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA);
        return new ActiviteNormativeProgrammationImpl(doc);
    }
}
