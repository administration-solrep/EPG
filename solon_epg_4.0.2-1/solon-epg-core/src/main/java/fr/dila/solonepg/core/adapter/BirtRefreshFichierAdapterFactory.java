package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.core.birt.BirtRefreshFichierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers BirtRefreshFichier.
 *
 */
public class BirtRefreshFichierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA);
        return new BirtRefreshFichierImpl(doc);
    }
}
