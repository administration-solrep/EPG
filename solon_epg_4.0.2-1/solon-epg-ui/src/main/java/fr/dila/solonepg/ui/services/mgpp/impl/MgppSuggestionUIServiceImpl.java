package fr.dila.solonepg.ui.services.mgpp.impl;

import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppSuggestionUIService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppSuggestionUIServiceImpl implements MgppSuggestionUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppSuggestionUIServiceImpl.class);

    @Override
    public List<SuggestionDTO> getSuggestions(SpecificContext context) {
        String input = context.getFromContextData(MgppContextDataKey.INPUT);
        String tableReference = context.getFromContextData(MgppContextDataKey.TABLE_REF);
        String typeOrganisme = context.getFromContextData(MgppContextDataKey.TYPE_ORGANISME);
        String proprietaire = ObjectUtils.defaultIfNull(
            context.getFromContextData(MgppContextDataKey.PROPRIETAIRE),
            "WILDCARD"
        );
        try {
            boolean basicSearch = BooleanUtils.toBooleanDefaultIfNull(
                context.getFromContextData(MgppContextDataKey.BASIC_SEARCH),
                false
            );
            return SolonMgppServiceLocator
                .getTableReferenceService()
                .searchTableReference(
                    input,
                    basicSearch,
                    tableReference,
                    context.getSession(),
                    proprietaire,
                    typeOrganisme
                )
                .stream()
                .map(table -> new SuggestionDTO(table.getId(), table.getTitle()))
                .collect(Collectors.toList());
        } catch (NuxeoException e) {
            LOGGER.error(
                context.getSession(),
                STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC,
                "tableReference = " + tableReference,
                e
            );
            context.getMessageQueue().addErrorToQueue("La suggestion " + tableReference + " n'a pas été trouvée");
        }
        return Collections.emptyList();
    }
}
