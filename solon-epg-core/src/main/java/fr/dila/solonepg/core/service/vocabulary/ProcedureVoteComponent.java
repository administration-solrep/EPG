package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.ProcedureVoteService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ProcedureVoteComponent
    extends ServiceEncapsulateComponent<ProcedureVoteService, ProcedureVoteServiceImpl> {

    public ProcedureVoteComponent() {
        super(ProcedureVoteService.class, new ProcedureVoteServiceImpl());
    }
}
