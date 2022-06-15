package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.StatutArchivageFacetService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class StatutArchivageFacetServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements StatutArchivageFacetService {

    public StatutArchivageFacetServiceImpl() {
        super(VocabularyConstants.STATUT_ARCHIVAGE_FACET_VOCABULARY, VOCABULARY, "statuts d'achivage");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
