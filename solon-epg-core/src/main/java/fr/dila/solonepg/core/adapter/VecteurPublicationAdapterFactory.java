package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.core.administration.VecteurPublicationImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers BulletinOfficiel.
 *
 * @author asatre
 */
public class VecteurPublicationAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
        checkDocumentSchemas(doc, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA);
        return new VecteurPublicationImpl(doc);
    }
}
