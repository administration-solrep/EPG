package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.ResponsableAmendementService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ResponsableAmendementServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements ResponsableAmendementService {

    public ResponsableAmendementServiceImpl() {
        super(VocabularyConstants.RESPONSABLE_AMENDEMENT_DIRECTORY, VOCABULARY, "responsables amendement");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
