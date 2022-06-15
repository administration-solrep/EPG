package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.BordereauLabelService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class BordereauLabelServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements BordereauLabelService {

    public BordereauLabelServiceImpl() {
        super(STVocabularyConstants.BORDEREAU_LABEL, STVocabularyConstants.VOCABULARY, "Labels bordereau");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
