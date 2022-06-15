package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.core.birt.ExportPANStatImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ExportPANStatAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA);
        return new ExportPANStatImpl(doc);
    }
}
