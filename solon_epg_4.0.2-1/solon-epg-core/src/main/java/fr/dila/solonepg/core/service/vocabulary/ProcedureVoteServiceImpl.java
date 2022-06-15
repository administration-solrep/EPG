package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.ProcedureVoteService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ProcedureVoteServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements ProcedureVoteService {

    public ProcedureVoteServiceImpl() {
        super(VocabularyConstants.PROCEDURE_VOTE_DIRECTORY, VOCABULARY, "procédures de vote");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
