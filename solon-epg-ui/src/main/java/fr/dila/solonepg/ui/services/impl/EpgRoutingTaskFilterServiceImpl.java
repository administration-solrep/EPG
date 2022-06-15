package fr.dila.solonepg.ui.services.impl;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.ss.ui.services.SSRoutingTaskFilterService;
import java.util.Map;
import java.util.function.Function;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class EpgRoutingTaskFilterServiceImpl implements SSRoutingTaskFilterService {
    private static final Map<String, Function<NuxeoPrincipal, Boolean>> ROUTING_TASK_TYPE_ACCEPTED = ImmutableMap.of(
        VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION,
        principal -> false,
        VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER,
        principal -> principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_BON_A_TIRER_CREATOR),
        VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
        principal -> principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR),
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO,
        principal -> principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_PUBLICATION_DILA_JO_CREATOR)
    );

    @Override
    public boolean accept(CoreSession session, String routingTaskType) {
        return ROUTING_TASK_TYPE_ACCEPTED
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().equals(routingTaskType))
            .findFirst()
            .map(v -> v.getValue().apply(session.getPrincipal()))
            .orElse(true);
    }
}
