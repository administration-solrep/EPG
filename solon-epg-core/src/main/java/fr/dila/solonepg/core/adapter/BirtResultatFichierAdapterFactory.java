package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.core.birt.BirtResultatFichierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers BirtRefreshFichier.
 */
public class BirtResultatFichierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA);
        return new BirtResultatFichierImpl(doc);
    }
}
