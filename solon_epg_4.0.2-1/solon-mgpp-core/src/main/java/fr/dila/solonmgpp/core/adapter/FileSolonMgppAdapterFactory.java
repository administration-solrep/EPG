package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.core.domain.FileSolonMgppImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers {@link FileSolonMgpp}.
 *
 * @author asatre
 */
public class FileSolonMgppAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, FileSolonMgppImpl.FILE_SOLON_MGPP_DOC_TYPE);

        return new FileSolonMgppImpl(doc);
    }
}
