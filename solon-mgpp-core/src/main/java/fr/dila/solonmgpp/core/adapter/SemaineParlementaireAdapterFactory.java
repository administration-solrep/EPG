package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.SemaineParlementaire;
import fr.dila.solonmgpp.core.domain.SemaineParlementaireImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class SemaineParlementaireAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE);

        return new SemaineParlementaireImpl(doc);
    }
}
