package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.CategorieActeService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class CategorieActeServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements CategorieActeService {

    public CategorieActeServiceImpl() {
        super(
            VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE,
            STVocabularyConstants.VOCABULARY,
            "Categorie d'acte"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
