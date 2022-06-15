package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.DirectiveEtatService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DirectiveEtatServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements DirectiveEtatService {

    public DirectiveEtatServiceImpl() {
        super(VocabularyConstants.VOC_DIRECTIVE_ETAT, VOCABULARY, "etats des directives");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
