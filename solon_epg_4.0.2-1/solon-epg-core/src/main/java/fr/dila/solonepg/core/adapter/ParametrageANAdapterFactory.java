package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgParametresANConstants;
import fr.dila.solonepg.core.administration.ParametrageANImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ParametrageANAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        checkDocumentSchemas(doc, SolonEpgParametresANConstants.SCHEMA);
        return new ParametrageANImpl(doc);
    }
}
