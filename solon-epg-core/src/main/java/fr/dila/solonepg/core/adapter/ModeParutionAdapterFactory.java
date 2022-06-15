package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.core.tablereference.ModeParutionImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link Habilitation}.
 *
 * @author asatre
 */
public class ModeParutionAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA);
        return new ModeParutionImpl(doc);
    }
}
