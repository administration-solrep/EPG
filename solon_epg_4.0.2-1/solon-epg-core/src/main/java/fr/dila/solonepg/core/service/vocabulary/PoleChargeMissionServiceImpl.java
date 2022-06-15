package fr.dila.solonepg.core.service.vocabulary;

import static fr.dila.st.api.constant.STVocabularyConstants.VOCABULARY;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.PoleChargeMissionService;
import fr.dila.st.core.service.AbstractCommonVocabularyServiceImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PoleChargeMissionServiceImpl
    extends AbstractCommonVocabularyServiceImpl<String>
    implements PoleChargeMissionService {

    public PoleChargeMissionServiceImpl() {
        super(VocabularyConstants.POLE_CHARGE_MISSION_DIRECTORY, VOCABULARY, "pôles chargés de mission");
    }

    @Override
    protected String getId(DocumentModel doc) {
        return getDefaultId(doc);
    }
}
