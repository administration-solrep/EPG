package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.core.administration.VecteurPublicationImpl;

/**
 * Adapteur de DocumentModel vers BulletinOfficiel.
 * 
 * @author asatre
 */
public class VecteurPublicationAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
		checkDocument(doc);
		return new VecteurPublicationImpl(doc);
	}

}
