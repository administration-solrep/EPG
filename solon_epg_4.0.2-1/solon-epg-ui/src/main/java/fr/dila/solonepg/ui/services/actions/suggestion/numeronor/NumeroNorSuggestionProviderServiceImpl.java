package fr.dila.solonepg.ui.services.actions.suggestion.numeronor;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.enumeration.TypeNorAutoComplete;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.services.actions.suggestion.SuggestionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NumeroNorSuggestionProviderServiceImpl implements NumeroNorSuggestionProviderService {
    private static final String NAME = "NUMERO_NOR";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getSuggestions(String input, SpecificContext context) {
        TypeNorAutoComplete typeNorAutoComplete = TypeNorAutoComplete.findValueOf(
            context.getFromContextData(STContextDataKey.SELECTION_FILTER)
        );

        Set<String> typeActeIds = getTypeActeIds(typeNorAutoComplete);
        if (typeActeIds.isEmpty()) {
            return ImmutableList.of();
        }

        String query = getQuery(typeActeIds, input);
        return SuggestionHelper.buildSuggestionsListUFNXQL("numeroNor", context.getSession(), query, null);
    }

    private Set<String> getTypeActeIds(TypeNorAutoComplete typeNorAutoComplete) {
        Set<String> typeActeIds = new HashSet<>();
        switch (typeNorAutoComplete) {
            case LOI:
                typeActeIds.addAll(SolonEpgServiceLocator.getActeService().getLoiList());
                break;
            case DECRET:
                typeActeIds.addAll(SolonEpgServiceLocator.getActeService().getDecretList());
                break;
            case ORDONNANCE:
                typeActeIds.addAll(SolonEpgServiceLocator.getActeService().getOrdonanceList());
                break;
            case TOUS:
                typeActeIds.addAll(SolonEpgServiceLocator.getActeService().getLoiList());
                typeActeIds.addAll(SolonEpgServiceLocator.getActeService().getDecretList());
                typeActeIds.addAll(SolonEpgServiceLocator.getActeService().getOrdonanceList());
                break;
        }
        return typeActeIds;
    }

    private String getQuery(Set<String> typeActeIds, String input) {
        String paramIdType = typeActeIds.stream().collect(Collectors.joining("', '", "'", "'"));
        String search =
            "%" +
            Normalizer
                .normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toUpperCase() +
            "%";

        return (
            "SELECT DISTINCT d.dos:numeroNor AS numeroNor " +
            "FROM Dossier AS d " +
            "WHERE d.dos:typeActe IN (" +
            paramIdType +
            ") " +
            "AND d.dos:numeroNor LIKE '" +
            search +
            "' "
        );
    }
}
