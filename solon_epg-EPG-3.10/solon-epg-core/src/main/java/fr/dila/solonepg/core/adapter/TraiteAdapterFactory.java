package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.Traite;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.core.cases.TraiteImpl;

/**
 * Adapteur de DocumentModel vers {@link Traite}.
 * 
 * @author asatre
 */
public class TraiteAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ TexteMaitreConstants.TEXTE_MAITRE_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new TraiteImpl(doc);
	}

}
