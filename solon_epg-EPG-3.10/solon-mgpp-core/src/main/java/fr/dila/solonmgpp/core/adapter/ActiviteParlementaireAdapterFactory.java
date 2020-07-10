package fr.dila.solonmgpp.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.core.domain.ActiviteParlementaireImpl;

public class ActiviteParlementaireAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {

		if (!ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE.equals(doc.getType())) {
			throw new CaseManagementRuntimeException("Document should be of type "
					+ ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE);
		}

		return new ActiviteParlementaireImpl(doc);
	}
}
