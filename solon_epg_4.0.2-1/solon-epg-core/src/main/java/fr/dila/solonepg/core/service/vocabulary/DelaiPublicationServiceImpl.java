package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.DelaiPublicationService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DelaiPublicationServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements DelaiPublicationService {

    public DelaiPublicationServiceImpl() {
        super(VocabularyConstants.DELAI_PUBLICATION, STVocabularyConstants.VOCABULARY, "Délai publication");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
