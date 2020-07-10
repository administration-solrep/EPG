package fr.dila.solonepg.core.adapter;

import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants.REQUETE_DOSSIER_SCHEMA;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.core.recherche.dossier.RequeteDossierImpl;

/**
 * L'apdateur pour la requête de dossier simple
 * 
 * @author jgomez
 */
public class RequeteDossierAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!(doc.hasSchema(REQUETE_DOSSIER_SCHEMA))) {
			throw new CaseManagementRuntimeException("Document should contain schemaS of RequeteDossier");
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new RequeteDossierImpl(doc);
	}

}
