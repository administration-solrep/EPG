package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.PrioriteCEService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PrioriteCEServiceImpl extends AbstractCommonVocabularyServiceImpl<String> implements PrioriteCEService {

    public PrioriteCEServiceImpl() {
        super(
            VocabularyConstants.VOC_PRIORITE,
            VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
            "Priorité conseil d'état"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
