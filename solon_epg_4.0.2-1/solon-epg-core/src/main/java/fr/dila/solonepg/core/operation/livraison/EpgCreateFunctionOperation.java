package fr.dila.solonepg.core.operation.livraison;

import avro.shaded.com.google.common.collect.ImmutableList;
import com.beust.jcommander.internal.Lists;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.operation.utils.AbstractCreateFunctionOperation;
import fr.dila.st.core.operation.utils.FunctionBean;
import java.util.List;
import org.nuxeo.ecm.automation.core.annotations.Operation;

/**
 * Operation pour ajouter des nouvelles fonctions dans EPG
 *
 */
@STVersion(version = "4.0.0")
@Operation(id = EpgCreateFunctionOperation.ID, category = STConstant.ORGANIGRAMME_BASE_FUNCTION_DIR)
public class EpgCreateFunctionOperation extends AbstractCreateFunctionOperation {
    public static final String ID = "SolonEpg.Function.Creation";

    @Override
    protected List<FunctionBean> getFunctions() {
        return ImmutableList.of(
            new FunctionBean(
                SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_ARRETE_UPDATER,
                "Donne le droit de modifier le type d'acte au sein des dossiers de type arrêtés",
                Lists.newArrayList(
                    STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME,
                    STBaseFunctionConstant.ADMIN_MINISTERIEL_GROUP_NAME
                )
            )
        );
    }
}
