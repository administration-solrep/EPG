package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgSqueletteList;
import fr.dila.solonepg.ui.th.model.bean.SqueletteFdrForm;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface EpgSqueletteUIService {
    EpgSqueletteList getSquelettes(SpecificContext context);

    SqueletteFdrForm getSquelette(SpecificContext context);

    SqueletteFdrForm createSquelette(SpecificContext context);

    SqueletteFdrForm updateSquelette(SpecificContext context);

    DocumentModel duplicateSquelette(SpecificContext context);

    void deleteSquelette(SpecificContext context);

    void invalidateRouteSquelette(SpecificContext context);

    void initSqueletteAction(SpecificContext context);
}
