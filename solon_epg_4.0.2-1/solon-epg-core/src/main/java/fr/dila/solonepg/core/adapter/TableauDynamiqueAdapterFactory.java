package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.core.recherche.TableauDynamiqueImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers FavorisRecherche.
 *
 * @author asatre
 */
public class TableauDynamiqueAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA);
        return new TableauDynamiqueImpl(doc);
    }
}
