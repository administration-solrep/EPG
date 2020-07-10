package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.parapheur.ParapheurFolderImpl;

/**
 * Adapteur de document vers la ParapheurModelFolder (répertoire d'un modèle de parapheur) .
 * 
 * @author arolin
 */
public class ParapheurFolderAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParapheurFolderImpl(doc);
	}
}
