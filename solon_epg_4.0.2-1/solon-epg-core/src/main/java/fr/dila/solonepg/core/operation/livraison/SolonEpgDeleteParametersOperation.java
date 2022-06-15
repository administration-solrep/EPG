package fr.dila.solonepg.core.operation.livraison;

import static fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl.FAIL_GET_DOCUMENT_TEC;

import com.google.common.collect.ImmutableList;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.operation.utils.ParametreBean;
import fr.dila.st.core.service.STServiceLocator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;

@Operation(
    id = SolonEpgDeleteParametersOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Récupère et supprime les paramètres renseignés",
    description = SolonEpgDeleteParametersOperation.DESCRIPTION
)
@STVersion(version = "4.0.0")
public class SolonEpgDeleteParametersOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Livraison.Delete.Parametres";

    private static final STLogger LOGGER = STLogFactory.getLog(SolonEpgDeleteParametersOperation.class);

    public static final String DESCRIPTION = "Cette opération récupère et suprime les paramètres renseignés";

    @Context
    protected OperationContext context;

    @OperationMethod
    public void deleteParametres() {
        if (!context.getPrincipal().isAdministrator()) {
            throw new NuxeoException(
                "Seul un administrateur Nuxeo peux créer des paramètres",
                HttpServletResponse.SC_FORBIDDEN
            );
        }
        CoreSession session = context.getCoreSession();

        getParametreBeans().forEach(cpb -> deleteParametre(session, cpb));
    }

    protected void deleteParametre(CoreSession session, ParametreBean parametreBean) {
        String parameterRootPath = STServiceLocator
            .getSTParametreService()
            .getParametreFolder(session)
            .getPath()
            .toString();
        DocumentRef pathRef = new PathRef(parameterRootPath, parametreBean.getParameterName());
        if (!session.exists(pathRef)) {
            LOGGER.warn(FAIL_GET_DOCUMENT_TEC, pathRef.toString());
            return;
        }
        session.removeDocument(pathRef);
    }

    protected List<ParametreBean> getParametreBeans() {
        return ImmutableList.of(new ParametreBean("archivage-repertoire"));
    }
}
