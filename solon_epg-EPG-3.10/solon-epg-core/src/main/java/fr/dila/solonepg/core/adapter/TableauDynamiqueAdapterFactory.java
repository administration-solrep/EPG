package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.core.recherche.TableauDynamiqueImpl;

/**
 * Adapteur de DocumentModel vers FavorisRecherche.
 * 
 * @author asatre
 */
public class TableauDynamiqueAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new TableauDynamiqueImpl(doc);
	}

}
