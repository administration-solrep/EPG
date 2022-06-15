package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TypeSignataireTraitementPapierServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements TypeSignataireTraitementPapierService {

    public TypeSignataireTraitementPapierServiceImpl() {
        super(
            VocabularyConstants.TYPE_SIGNATAIRE_TP_DIRECTORY_NAME,
            VOCABULARY,
            "types de signataire du traitement papier"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
