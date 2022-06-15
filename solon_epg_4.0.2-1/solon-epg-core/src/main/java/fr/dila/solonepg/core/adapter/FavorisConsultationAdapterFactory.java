package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.core.recherche.FavorisConsultationImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers FavorisConsultation.
 *
 * @author asatre
 */
public class FavorisConsultationAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA);
        return new FavorisConsultationImpl(doc);
    }
}
