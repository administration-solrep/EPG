package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.core.administration.ProfilUtilisateurImpl;

/**
 * Adapteur de document vers ProfilUtilisateur.
 * 
 * @author arolin
 */
public class ProfilUtilisateurAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ProfilUtilisateurImpl(doc);
	}

}
