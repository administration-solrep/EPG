package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.core.domain.comment.CommentImpl;
import fr.dila.st.api.constant.STConstant;

/**
 * Adapteur de DocumentModel vers Comment.
 * 
 * @author jtremeaux
 */
public class CommentAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new CommentImpl(doc);
	}

	/**
	 * Vérifie que le document est de type Comment.
	 * 
	 * @param doc
	 */
	protected void checkDocument(DocumentModel doc) {
		if (!STConstant.COMMENT_DOCUMENT_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type " + STConstant.COMMENT_DOCUMENT_TYPE);
		}
	}
}
