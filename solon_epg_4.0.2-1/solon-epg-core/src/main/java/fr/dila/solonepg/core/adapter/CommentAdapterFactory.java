package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.core.domain.comment.CommentImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers Comment.
 *
 * @author jtremeaux
 */
public class CommentAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentType(doc, STConstant.COMMENT_DOCUMENT_TYPE);
        return new CommentImpl(doc);
    }
}
