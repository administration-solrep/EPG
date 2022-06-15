package fr.dila.solonepg.api.service.vocabulary;

import fr.dila.st.api.service.AbstractCommonVocabularyService;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface TypeActeService extends AbstractCommonVocabularyService<String> {
    String getId(String label);

    List<String> getTypeActesSuggestion(CoreSession session, String input, Boolean isCreation, Boolean isLoi);

    boolean isDecretCMIndividuelDisplayed(CoreSession session, String typeActe);
}
