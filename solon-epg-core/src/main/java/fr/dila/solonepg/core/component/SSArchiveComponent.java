package fr.dila.solonepg.core.component;

import fr.dila.solonepg.core.service.ArchiveServiceImpl;
import fr.dila.ss.api.service.SSArchiveService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class SSArchiveComponent extends ServiceEncapsulateComponent<SSArchiveService, ArchiveServiceImpl> {

    public SSArchiveComponent() {
        super(SSArchiveService.class, new ArchiveServiceImpl());
    }
}
