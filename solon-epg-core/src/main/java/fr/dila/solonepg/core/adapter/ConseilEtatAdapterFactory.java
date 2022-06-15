package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.core.cases.ConseilEtatImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link ConseilEtat}.
 *
 * @author asatre
 */
public class ConseilEtatAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ConseilEtatConstants.CONSEIL_ETAT_SCHEMA);
        return new ConseilEtatImpl(doc);
    }
}
