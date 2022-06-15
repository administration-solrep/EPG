package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.PeriodiciteRapportService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PeriodiciteRapportServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements PeriodiciteRapportService {

    public PeriodiciteRapportServiceImpl() {
        super(
            VocabularyConstants.PERIODICITE_RAPPORT_DIRECTORY_NAME,
            STVocabularyConstants.VOCABULARY,
            "Periodicité du rapport"
        );
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
