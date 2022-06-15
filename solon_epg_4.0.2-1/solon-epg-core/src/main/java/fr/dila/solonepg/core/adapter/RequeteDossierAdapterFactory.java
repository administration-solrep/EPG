package fr.dila.solonepg.core.adapter;

import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants.REQUETE_DOSSIER_SCHEMA;

import fr.dila.solonepg.core.recherche.dossier.RequeteDossierImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * L'apdateur pour la requête de dossier simple
 *
 * @author jgomez
 */
public class RequeteDossierAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, REQUETE_DOSSIER_SCHEMA);
        return new RequeteDossierImpl(doc);
    }
}
