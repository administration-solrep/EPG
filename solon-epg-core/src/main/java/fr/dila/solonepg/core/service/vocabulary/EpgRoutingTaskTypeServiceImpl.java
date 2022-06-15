package fr.dila.solonepg.core.service.vocabulary;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.EpgRoutingTaskTypeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.core.service.vocabulary.RoutingTaskTypeServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class EpgRoutingTaskTypeServiceImpl extends RoutingTaskTypeServiceImpl implements EpgRoutingTaskTypeService {
    /**
     * List des types d'étapes qui ne peuvent pas être ajoutées manuellement sur une feuille de route
     */
    private static final List<Integer> ROUTING_TASK_TYPE_TO_EXCLUDE_FOR_AJOUT = ImmutableList.of(
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION),
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_SIGNATURE)
    );

    private static final List<Integer> ROUTING_TASK_TYPE_TO_EXCLUDE = ImmutableList.of(
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_SIGNATURE)
    );

    private static final List<Integer> ROUTING_TASK_TYPE_TO_EXCLUDE_FDR_NON_PUB_JO = ImmutableList.of(
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION),
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_SIGNATURE),
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO),
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO),
        Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE)
    );

    @Override
    public List<ImmutablePair<Integer, String>> getEntries() {
        return getEntriesFiltered(ROUTING_TASK_TYPE_TO_EXCLUDE);
    }

    @Override
    public List<ImmutablePair<Integer, String>> getFilteredEntries(NuxeoPrincipal principal, String typeActe) {
        List<Integer> routingTaskToExclude = new ArrayList<>(ROUTING_TASK_TYPE_TO_EXCLUDE);
        if (!principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_BON_A_TIRER_CREATOR)) {
            routingTaskToExclude.add(Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER));
        }
        if (!principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR)) {
            routingTaskToExclude.add(Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE));
        }
        if (!principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_PUBLICATION_DILA_JO_CREATOR)) {
            routingTaskToExclude.add(Integer.valueOf(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO));
        }
        routingTaskToExclude.addAll(ROUTING_TASK_TYPE_TO_EXCLUDE_FOR_AJOUT);
        if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(typeActe)) {
            routingTaskToExclude.addAll(ROUTING_TASK_TYPE_TO_EXCLUDE_FDR_NON_PUB_JO);
        }
        return getEntriesFiltered(routingTaskToExclude);
    }
}
