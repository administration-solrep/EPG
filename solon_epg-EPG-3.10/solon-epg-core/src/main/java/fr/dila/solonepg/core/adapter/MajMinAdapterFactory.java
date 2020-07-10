package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.cases.MajMinImpl;

/**
 * Adapteur de DocumentModel vers {@link MajMin}.
 * 
 * @author jgomez
 */
public class MajMinAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(ActiviteNormativeConstants.MAJ_MIN_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ ActiviteNormativeConstants.MAJ_MIN_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new MajMinImpl(doc);
	}

}
