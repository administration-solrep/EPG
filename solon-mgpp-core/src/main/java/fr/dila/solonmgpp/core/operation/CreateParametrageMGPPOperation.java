package fr.dila.solonmgpp.core.operation;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.service.ParametreMgppService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.util.function.FourConsumer;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;

@Operation(
    id = CreateParametrageMGPPOperation.ID,
    category = Constants.CAT_DOCUMENT,
    label = "CreateParametrageMGPPOperation",
    description = "Initialisation du paramétrage MGPP, prend 4 paramètres"
)
public class CreateParametrageMGPPOperation {
    public static final String ID = "MGPP.Parametrage.Init";

    private static final String MGPP_APPLICATION = "mgpp";
    private static final String INFOS_PARL_APPLICATION = "infos_parl";

    private static final Map<String, FourConsumer<CoreSession, String, String, String>> SAVE_METHODS = ImmutableMap.of(
        MGPP_APPLICATION,
        CreateParametrageMGPPOperation::saveParametrageMgpp,
        INFOS_PARL_APPLICATION,
        CreateParametrageMGPPOperation::saveParametrageInfosParl
    );

    @Context
    private CoreSession session;

    @Param(name = "url")
    private String url = "";

    @Param(name = "login")
    private String login = "";

    @Param(name = "pwd")
    private String pwd = "";

    @Param(name = "application")
    private String application = "";

    @OperationMethod
    public void run() {
        FourConsumer<CoreSession, String, String, String> saveMethod = SAVE_METHODS.get(application);

        if (saveMethod == null) {
            throw new IllegalArgumentException(
                "Le paramètre application est incorrect [" +
                application +
                "], il doit avoir une des valeurs suivantes : " +
                MGPP_APPLICATION +
                "|" +
                INFOS_PARL_APPLICATION
            );
        }

        saveMethod.accept(session, url, login, pwd);
    }

    private static void saveParametrageMgpp(CoreSession session, String url, String login, String pwd) {
        ParametreMgppService parametreMgppService = SolonMgppServiceLocator.getParametreMgppService();
        ParametrageMgpp parametrageMgpp = parametreMgppService.findParametrageMgpp(session);

        if (StringUtils.isNoneBlank(url, login, pwd)) {
            parametrageMgpp.setUrlEpp(url);
            parametrageMgpp.setLoginEpp(login);
            parametrageMgpp.setPassEpp(pwd);
            parametreMgppService.saveParametrageMgpp(session, parametrageMgpp, false);
        }
    }

    private static void saveParametrageInfosParl(CoreSession session, String url, String login, String pwd) {
        ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            session
        );

        if (StringUtils.isNoneBlank(url, login, pwd)) {
            parametrageApplication.setUrlEppInfosParl(url);
            parametrageApplication.setLoginEppInfosParl(login);
            parametrageApplication.setPassEppInfosParl(pwd);
            parametreApplicationService.saveParametrageApplication(session, parametrageApplication);
        }
    }
}
