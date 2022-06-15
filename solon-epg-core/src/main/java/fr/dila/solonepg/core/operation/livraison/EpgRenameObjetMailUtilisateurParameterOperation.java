package fr.dila.solonepg.core.operation.livraison;

import com.google.common.collect.ImmutableList;
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
    id = EpgRenameObjetMailUtilisateurParameterOperation.ID,
    category = STConstant.PARAMETRE_DOCUMENT_TYPE,
    label = "Renomme le paramètre d'objet de mail utilisateur",
    description = EpgRenameObjetMailUtilisateurParameterOperation.DESCRIPTION
)
@STVersion(version = "4.0.2")
public class EpgRenameObjetMailUtilisateurParameterOperation extends AbstractUpdateParametersOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Livraison.Update.Parametre.Objet.Mail.Utilisateur";

    public static final String DESCRIPTION = "Cette opération met à jour le paramètre d'objet de mail utilisateur";

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
                STParametreConstant.OBJET_MAIL_EXPORT_USERS_NAME,
                STParametreConstant.OBJET_MAIL_EXPORT_USERS_NAME_TITRE,
                STParametreConstant.OBJET_MAIL_EXPORT_USERS_NAME_DESCRIPTION,
                STParametreConstant.OBJET_MAIL_EXPORT_USERS_NAME_UNIT,
                STParametreConstant.OBJET_MAIL_EXPORT_USERS_NAME_VALUE
            )
        );
    }
}
