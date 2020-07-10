package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.core.recherche.FavorisConsultationImpl;

/**
 * Adapteur de DocumentModel vers FavorisConsultation.
 * 
 * @author asatre
 */
public class FavorisConsultationAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new FavorisConsultationImpl(doc);
	}

}
