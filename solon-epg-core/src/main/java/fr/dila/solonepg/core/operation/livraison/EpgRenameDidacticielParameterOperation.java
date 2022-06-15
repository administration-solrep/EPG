package fr.dila.solonepg.core.operation.livraison;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.operation.utils.AbstractUpdateParametersOperation;
import fr.dila.st.core.operation.utils.ParametreBean;
import java.util.List;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.core.api.CoreSession;

@Operation(
    id = EpgRenameDidacticielParameterOperation.ID,
    category = STConstant.PARAMETRE_DOCUMENT_TYPE,
    label = "Renomme le paramètre URL didacticiel en URL du portail SLQD",
    description = EpgRenameDidacticielParameterOperation.DESCRIPTION
)
@STVersion(version = "4.0.1")
public class EpgRenameDidacticielParameterOperation extends AbstractUpdateParametersOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Livraison.Update.Parametre.Didacticiel";

    public static final String DESCRIPTION =
        "Cette opération met à jour le paramètre URL didacticiel en URL du portail SLQD";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @Override
    protected OperationContext getContext() {
        return context;
    }

    @Override
    protected List<ParametreBean> getParametreBeans() {
        return ImmutableList.of(
            new ParametreBean(
                STParametreConstant.ADRESSE_URL_DIDACTICIEL,
                SolonEpgParametreConstant.ADRESSE_URL_SLQD_TITRE,
                SolonEpgParametreConstant.ADRESSE_URL_SLQD_DESC,
                SolonEpgParametreConstant.ADRESSE_URL_SLQD_UNIT,
                SolonEpgParametreConstant.ADRESSE_URL_SLQD_VALUE
            )
        );
    }
}
