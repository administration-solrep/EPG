package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.core.documentmodel.ListeTraitementPapierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ListeTraitementPapierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA);
        return new ListeTraitementPapierImpl(doc);
    }
}
