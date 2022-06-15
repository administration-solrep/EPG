package fr.dila.solonepg.ui.services.actions;

import fr.dila.ss.ui.services.actions.SSCorbeilleActionService;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgCorbeilleActionService extends SSCorbeilleActionService {
    boolean isEtapePourAvisCE(SpecificContext context);
}
