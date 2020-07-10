package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;

public interface DndService {

	String processMove(final CoreSession session, final String dndDocId, final String dndContainerId,
			NuxeoPrincipal currentUser, NavigationContext navigationContext);

	IdRef extractId(String input);

}