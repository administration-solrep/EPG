package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.NatureTexteService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class NatureTexteServiceImpl extends AbstractCommonVocabularyServiceImpl<String> implements NatureTexteService {

    public NatureTexteServiceImpl() {
        super(VocabularyConstants.NATURE_TEXTE_DIRECTORY, VOCABULARY, "natures de texte");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
