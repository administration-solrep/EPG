package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.DispositionHabilitationService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DispositionHabilitationServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements DispositionHabilitationService {

    public DispositionHabilitationServiceImpl() {
        super(
            VocabularyConstants.DISPOSITION_HABILITATION_DIRECTORY_NAME,
            VOCABULARY,
            "disposition habilitation (fondement constitutionnel)"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
