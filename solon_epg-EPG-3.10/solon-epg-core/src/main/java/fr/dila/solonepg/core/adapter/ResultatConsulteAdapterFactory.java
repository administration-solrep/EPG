package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.core.recherche.ResultatConsulteImpl;

/**
 * Adapteur de DocumentModel vers ResultatConsulte.
 * 
 * @author asatre
 */
public class ResultatConsulteAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ResultatConsulteImpl(doc);
	}

}
