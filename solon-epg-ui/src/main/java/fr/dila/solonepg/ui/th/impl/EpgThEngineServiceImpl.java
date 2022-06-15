package fr.dila.solonepg.ui.th.impl;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.th.EpgThEngineService;
import fr.dila.st.ui.th.impl.ThEngineServiceImpl;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.math.NumberUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;

public class EpgThEngineServiceImpl extends ThEngineServiceImpl implements EpgThEngineService {

    @Override
    public long getDelaiRafraichissement() {
        WebContext activeContext = WebEngine.getActiveContext();
        if (activeContext != null && activeContext.getPrincipal() != null) {
            CoreSession session = activeContext.getCoreSession();

            ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
            ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
                session
            );

            Long delaiRafraichissementCorbeille = parametrageApplication.getDelaiRafraichissementCorbeille();

            if (delaiRafraichissementCorbeille != null && delaiRafraichissementCorbeille > NumberUtils.LONG_ZERO) {
                return TimeUnit.MINUTES.toMillis(delaiRafraichissementCorbeille);
            }
        }

        return super.getDelaiRafraichissement();
    }
}
