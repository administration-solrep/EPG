package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.parapheur.ParapheurFileImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers la ParapheurFile (fichier de parapheur) .
 *
 */
public class ParapheurFileAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA);
        checkDocumentType(doc, SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE);
        return new ParapheurFileImpl(doc);
    }
}
