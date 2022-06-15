package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.cases.MajMinImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link MajMin}.
 *
 * @author jgomez
 */
public class MajMinAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, ActiviteNormativeConstants.MAJ_MIN_SCHEMA);
        return new MajMinImpl(doc);
    }
}
