package fr.dila.solonepg.ui.services.mgpp.impl;

import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppAdminUIService;
import fr.dila.solonepg.ui.th.model.bean.MgppParamForm;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.service.ParametreMgppService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppAdminUIServiceImpl implements MgppAdminUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppCorbeilleUIServiceImpl.class);

    private ParametrageMgpp getBackMGPPParameters(CoreSession session) {
        ParametreMgppService parametreMgppService = SolonMgppServiceLocator.getParametreMgppService();
        return parametreMgppService.findParametrageMgpp(session);
    }

    @Override
    public MgppParamForm getMgppParameters(SpecificContext context) {
        ParametrageMgpp param = getBackMGPPParameters(context.getSession());
        MgppParamForm form = new MgppParamForm();

        form.setDelaiPurgeMois(param.getDelaiPurgeCalendrier());
        form.setDelaiVerifSec(param.getDelai());
        form.setFiltreLoi(param.getFiltreDateCreationLoi());
        form.setInitComPM(param.getAuteurLex01());
        form.setInitCourrier(param.isMinistre());
        form.setNbJourPers(param.getNbJourAffichable());
        form.setPresAN(param.getNomAN());
        form.setPresSenat(param.getNomSenat());
        form.setTextAccueil(param.getTexteLibreListeOep());
        form.setUrl(param.getUrlEpp());
        form.setUsername(param.getLoginEpp());
        form.setInitComDirecteurAdjointSGG(param.getNomDirecteurAdjointSGG());
        form.setInitComSGG(param.getNomSGG());

        return form;
    }

    @Override
    public void saveMgppParameters(SpecificContext context) {
        MgppParamForm form = context.getFromContextData(MgppContextDataKey.PARAM_FORM);
        ParametrageMgpp parametrage = getBackMGPPParameters(context.getSession());

        parametrage.setAuteurLex01(form.getInitComPM());
        parametrage.setDelai(form.getDelaiVerifSec());
        parametrage.setDelaiPurgeCalendrier(form.getDelaiPurgeMois());
        parametrage.setFiltreDateCreationLoi(form.getFiltreLoi());
        parametrage.setIsMinistre(form.getInitCourrier());
        parametrage.setLoginEpp(form.getUsername());
        parametrage.setNbJourAffichable(form.getNbJourPers());
        parametrage.setNomAN(form.getPresAN());
        parametrage.setNomSenat(form.getPresSenat());
        parametrage.setPassEpp(form.getPassword());
        parametrage.setTexteLibreListeOep(form.getTextAccueil());
        parametrage.setUrlEpp(form.getUrl());
        parametrage.setNomSGG(form.getInitComSGG());
        parametrage.setNomDirecteurAdjointSGG(form.getInitComDirecteurAdjointSGG());

        try {
            SolonMgppServiceLocator
                .getParametreMgppService()
                .saveParametrageMgpp(context.getSession(), parametrage, true);
            context.getMessageQueue().addInfoToQueue(ResourceHelper.getString("mgpp.param.save.success"));
        } catch (NuxeoException exc) {
            LOGGER.debug(EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC, exc);
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("mgpp.param.save.error"));
        }
    }
}
