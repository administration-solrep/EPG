package fr.dila.solonmgpp.core.operation.reprise;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.service.ParametreMgppService;
import fr.dila.solonmgpp.core.domain.ParametrageMgppImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(
    id = ReprisePasswordMgpp.ID,
    label = ReprisePasswordMgpp.LABEL,
    description = ReprisePasswordMgpp.DESCRIPTION
)
public class ReprisePasswordMgpp {
    public static final String ID = "SolonEpg.Reprise.MgppPwd";

    public static final String LABEL = "Reprise du mot de passe MGPP";

    public static final String DESCRIPTION =
        "Cette opération reprend le mot de passe non crypté de connexion à EPP pour MGPP et le crypte";

    private static final String PASS_EPP_PROP = ParametrageMgppImpl.PREFIX + ":" + ParametrageMgppImpl.PASSWORD_EPP;

    private static final STLogger LOGGER = STLogFactory.getLog(ReprisePasswordMgpp.class);

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run() throws Exception {
        LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Début de procédure de reprise du mot de passe MGPP");

        ParametreMgppService parametreMgppService = SolonMgppServiceLocator.getParametreMgppService();
        ParametrageMgpp parametrageMgpp = parametreMgppService.findParametrageMgpp(session);
        DocumentModel parametrageMgppDoc = parametrageMgpp.getDocument();

        parametrageMgpp.setPassEpp((String) parametrageMgppDoc.getPropertyValue(PASS_EPP_PROP));

        parametrageMgppDoc = parametreMgppService.saveParametrageMgpp(session, parametrageMgpp, false).getDocument();
        SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(session);

        LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Fin de procédure de reprise du mot de passe MGPP");
    }
}
