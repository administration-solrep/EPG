package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.core.domain.ActiviteParlementaireImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ActiviteParlementaireAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE);

        return new ActiviteParlementaireImpl(doc);
    }
}
