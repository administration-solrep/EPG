package fr.dila.solonepg.ui.helper.mgpp;

import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import java.util.Map;

public class MgppMessageListProviderHelper {

    private MgppMessageListProviderHelper() {}

    public static MgppMessageDTO convertToMessageDTO(MessageDTO message, Map<String, String> mapNiveauLecture) {
        EvenementTypeDescriptor eventType = SolonMgppServiceLocator
            .getEvenementTypeService()
            .getEvenementType(message.getTypeEvenement());
        return new MgppMessageDTO(message, mapNiveauLecture, eventType);
    }
}
