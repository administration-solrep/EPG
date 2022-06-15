package fr.dila.solonepg.core.service.vocabulary;

import com.beust.jcommander.internal.Lists;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

public class TypeActeServiceImpl extends AbstractCommonVocabularyServiceImpl<String> implements TypeActeService {

    public TypeActeServiceImpl() {
        super(
            VocabularyConstants.TYPE_ACTE_VOCABULARY,
            VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
            "types d'acte"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }

    @Override
    public String getId(String label) {
        List<ImmutablePair<String, String>> filteredEntries = getFilteredEntries(doc -> getLabel(doc).equals(label));

        if (filteredEntries.isEmpty()) {
            throw new NuxeoException(
                String.format("L'entrée %s n'a pas été trouvée dans le vocabulaire des types d'actes", label)
            );
        }
        return filteredEntries.get(0).getKey();
    }

    @Override
    public List<String> getTypeActesSuggestion(CoreSession session, String input, Boolean isCreation, Boolean isLoi) {
        if (BooleanUtils.isTrue(isLoi)) {
            ActeService acteService = SolonEpgServiceLocator.getActeService();
            return getFilteredSuggestions(input, d -> acteService.isLoi(d.getId()));
        }
        if (BooleanUtils.isFalse(isCreation)) {
            return getFilteredEntries(input);
        }

        List<String> idsToExclude = Lists.newArrayList(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES);
        if (!session.getPrincipal().isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVES_UPDATER)) {
            idsToExclude.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
        }

        return getFilteredSuggestions(input, d -> !idsToExclude.contains(d.getId()));
    }

    @Override
    public boolean isDecretCMIndividuelDisplayed(CoreSession session, String typeActe) {
        return (
            session.getPrincipal().isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVES_UPDATER) ||
            !TypeActeConstants.TYPE_ACTE_DECRET_CM_IND.equals(typeActe)
        );
    }
}
