package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.core.cases.RetourDilaImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link RetourDila}. (gestion des retours de la dila)
 *
 * @author asatre
 */
public class RetourDilaAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, RetourDilaConstants.RETOUR_DILA_SCHEMA);
        return new RetourDilaImpl(doc);
    }
}
