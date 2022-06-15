package fr.dila.solonepg.ui.contentview;

import fr.dila.st.api.domain.STDomainObject;
import fr.dila.st.core.client.AbstractMapDTO;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class AbstractPanPageProvider<T extends STDomainObject, S extends AbstractMapDTO>
    extends AbstractDTOFiltrableProvider {
    private static final long serialVersionUID = 5008649110826199818L;

    protected abstract T getAdapter(DocumentModel documentModel);

    protected abstract S mapAdapterDocToDto(CoreSession session, T adapterDoc);
}
