package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.TypeMesureService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TypeMesureServiceImpl extends AbstractCommonVocabularyServiceImpl<String> implements TypeMesureService {

    public TypeMesureServiceImpl() {
        super(VocabularyConstants.TYPE_MESURE_VOCABULARY, VOCABULARY, "types de mesure");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
