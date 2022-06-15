package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.core.requeteur.EpgRequeteExperteImpl;
import fr.dila.st.api.constant.STRequeteConstants;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgRequeteExperteAdapterFactory implements STDocumentAdapterFactory {

    /**
     * Default constructor
     */
    public EpgRequeteExperteAdapterFactory() {
        // do nothing
    }

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, STRequeteConstants.SMART_FOLDER_SCHEMA);
        return new EpgRequeteExperteImpl(doc);
    }
}
