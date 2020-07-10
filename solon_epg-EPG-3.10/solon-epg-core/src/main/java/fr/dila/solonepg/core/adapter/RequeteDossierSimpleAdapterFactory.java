package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.*;
import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.core.recherche.dossier.RequeteDossierSimpleImpl;

/**
 * L'apdateur pour la requête de dossier simple
 * 
 * @author jgomez
 */
public class RequeteDossierSimpleAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!(doc.hasSchema(REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA)
				&& doc.hasSchema(REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA)
				&& doc.hasSchema(REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA) && doc
				.hasSchema(REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA))) {
			throw new CaseManagementRuntimeException("Document should contain schemaS of RequeteDossierSimple");
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new RequeteDossierSimpleImpl(doc);
	}

}
