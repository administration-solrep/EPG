package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.NatureTexteBaseLegaleService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class NatureTexteBaseLegaleServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements NatureTexteBaseLegaleService {

    public NatureTexteBaseLegaleServiceImpl() {
        super(VocabularyConstants.BASE_LEGALE_NATURE_TEXTE_DIRECTORY_NAME, VOCABULARY, "nature de texte (base légale)");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
