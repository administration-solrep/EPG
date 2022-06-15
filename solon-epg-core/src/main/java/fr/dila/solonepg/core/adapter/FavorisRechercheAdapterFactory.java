package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.core.recherche.FavorisRechercheImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers FavorisRecherche.
 *
 * @author asatre
 */
public class FavorisRechercheAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA);
        return new FavorisRechercheImpl(doc);
    }
}
