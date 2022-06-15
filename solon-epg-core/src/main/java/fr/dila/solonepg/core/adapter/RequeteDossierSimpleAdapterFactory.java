package fr.dila.solonepg.core.adapter;

import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA;

import fr.dila.solonepg.core.recherche.dossier.RequeteDossierSimpleImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * L'apdateur pour la requête de dossier simple
 *
 * @author jgomez
 */
public class RequeteDossierSimpleAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(
            doc,
            REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA,
            REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA,
            REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA,
            REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA
        );
        return new RequeteDossierSimpleImpl(doc);
    }
}
