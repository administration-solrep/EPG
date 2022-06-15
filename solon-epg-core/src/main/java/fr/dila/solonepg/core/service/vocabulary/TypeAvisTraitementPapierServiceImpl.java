package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.TypeAvisTraitementPapierService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TypeAvisTraitementPapierServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements TypeAvisTraitementPapierService {

    public TypeAvisTraitementPapierServiceImpl() {
        super(
            VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_DIRECTORY,
            VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_SCHEMA,
            "types d'avis du traitement papier"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
