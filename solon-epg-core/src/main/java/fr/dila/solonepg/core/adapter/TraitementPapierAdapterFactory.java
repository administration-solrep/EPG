package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.core.cases.TraitementPapierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TraitementPapierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA);
        return new TraitementPapierImpl(doc);
    }
}
