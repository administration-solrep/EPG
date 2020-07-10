package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.core.administration.BulletinOfficielImpl;

/**
 * Adapteur de DocumentModel vers BulletinOfficiel.
 * 
 * @author asatre
 */
public class BulletinOfficielAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
		checkDocument(doc);
		return new BulletinOfficielImpl(doc);
	}

}
