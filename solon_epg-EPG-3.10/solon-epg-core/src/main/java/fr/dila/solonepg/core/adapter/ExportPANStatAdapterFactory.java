package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.core.birt.ExportPANStatImpl;

public class ExportPANStatAdapterFactory implements DocumentAdapterFactory {

	protected void checkDocument(DocumentModel doc) {
		if (!doc.hasSchema(ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA);
		}
	}

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ExportPANStatImpl(doc);
	}

}
