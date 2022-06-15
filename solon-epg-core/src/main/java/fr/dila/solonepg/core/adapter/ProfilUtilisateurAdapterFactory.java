package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.core.administration.ProfilUtilisateurImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers ProfilUtilisateur.
 *
 * @author arolin
 */
public class ProfilUtilisateurAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA);
        return new ProfilUtilisateurImpl(doc);
    }
}
