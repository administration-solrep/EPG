package fr.dila.solonepg.core.component;

import fr.dila.solonepg.api.service.SolonEpgCorbeilleTreeService;
import fr.dila.solonepg.core.service.SolonEpgCorbeilleTreeServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class SolonEpgCorbeilleTreeComponent
    extends ServiceEncapsulateComponent<SolonEpgCorbeilleTreeService, SolonEpgCorbeilleTreeServiceImpl> {

    public SolonEpgCorbeilleTreeComponent() {
        super(SolonEpgCorbeilleTreeService.class, new SolonEpgCorbeilleTreeServiceImpl());
    }
}
