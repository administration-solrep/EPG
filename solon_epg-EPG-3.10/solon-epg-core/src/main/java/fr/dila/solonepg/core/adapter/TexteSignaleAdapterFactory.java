package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.TexteSignale;
import fr.dila.solonepg.api.constant.TexteSignaleConstants;
import fr.dila.solonepg.core.cases.TexteSignaleImpl;

/**
 * Adapteur de DocumentModel vers {@link TexteSignale}.
 * 
 * @author asatre
 */
public class TexteSignaleAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new TexteSignaleImpl(doc);
	}

}
