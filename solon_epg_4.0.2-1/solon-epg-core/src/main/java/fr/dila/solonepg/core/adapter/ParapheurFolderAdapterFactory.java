package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.parapheur.ParapheurFolderImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers la ParapheurModelFolder (répertoire d'un modèle de parapheur) .
 *
 * @author arolin
 */
public class ParapheurFolderAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA);
        return new ParapheurFolderImpl(doc);
    }
}
