package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.TypePublicationService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TypePublicationServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements TypePublicationService {

    public TypePublicationServiceImpl() {
        super(VocabularyConstants.TYPE_PUBLICATION, VOCABULARY, "types de publication");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
