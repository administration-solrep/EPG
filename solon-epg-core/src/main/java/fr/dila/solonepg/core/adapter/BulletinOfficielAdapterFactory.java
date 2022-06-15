package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.core.administration.BulletinOfficielImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers BulletinOfficiel.
 *
 * @author asatre
 */
public class BulletinOfficielAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, @SuppressWarnings("rawtypes") Class arg1) {
        checkDocumentSchemas(doc, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA);
        return new BulletinOfficielImpl(doc);
    }
}
