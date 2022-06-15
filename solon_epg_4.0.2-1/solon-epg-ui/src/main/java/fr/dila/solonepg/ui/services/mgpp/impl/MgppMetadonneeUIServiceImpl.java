package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.st.ui.enums.STContextDataKey.MODE_CREATION;
import static fr.dila.st.ui.enums.WidgetTypeEnum.FILE_MULTI;
import static fr.dila.st.ui.enums.WidgetTypeEnum.PIECE_JOINTE;

import fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.helper.mgpp.MgppWidgetFactory;
import fr.dila.solonepg.ui.services.mgpp.MgppMetadonneeUIService;
import fr.dila.solonmgpp.api.constant.ModeCreationConstants;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PieceJointeDescriptor;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.descriptor.parlement.PropertyDescriptor;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.bean.DetailCommunication;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.th.bean.mapper.MapPropertyDescToWidget;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class MgppMetadonneeUIServiceImpl implements MgppMetadonneeUIService {

    @Override
    public DetailCommunication getDetailCommunication(SpecificContext context) {
        String eventType = context.getFromContextData(MgppContextDataKey.EVENT_TYPE);
        Map<String, PropertyDescriptor> mapProperties = SolonMgppServiceLocator
            .getMetaDonneesService()
            .getMapProperty(eventType);
        EvenementDTO eventDTO = context.getFromContextData(MgppContextDataKey.CURRENT_EVENT);

        if (
            StringUtils.isBlank(context.getFromContextData(MODE_CREATION)) &&
            ModeCreationConstants.BROUILLON_COMPLETION.equals(eventDTO.getVersionCouranteModeCreation())
        ) {
            context.putInContextData(MODE_CREATION, "completer");
        } else if (
            StringUtils.isBlank(context.getFromContextData(MODE_CREATION)) &&
            ModeCreationConstants.BROUILLON_RECTIFICATION.equals(eventDTO.getVersionCouranteModeCreation())
        ) {
            context.putInContextData(MODE_CREATION, "rectifier");
        }
        boolean isEditMode = context.getFromContextData(MapPropertyDescToWidget.IS_EDIT_MODE);
        DetailCommunication com = new DetailCommunication();

        com.setLstWidgets(
            mapProperties
                .entrySet()
                .stream()
                .filter(entry -> MgppCommunicationMetadonneeEnum.getFilterEditableMetadonnee(entry.getKey()))
                .sorted(
                    (entry1, entry2) ->
                        MgppCommunicationMetadonneeEnum
                            .getViewableMetadonneeComparator()
                            .compare(entry1.getKey(), entry2.getKey())
                )
                .map(entry -> MgppEditWidgetFactory.getWidgetForEditView(context, entry, eventDTO, isEditMode))
                .collect(Collectors.toList())
        );

        com.getLstWidgets().addAll(getPieceJointeWidgets(context, eventDTO));
        com.setLstComSuccessives(getLisEventSuccessifs(eventDTO));

        return com;
    }

    private List<WidgetDTO> getPieceJointeWidgets(SpecificContext context, EvenementDTO dto) {
        List<WidgetDTO> widgets = new ArrayList<>();

        String eventType = context.getFromContextData(MgppContextDataKey.EVENT_TYPE);
        boolean isEditMode = context.getFromContextData(MapPropertyDescToWidget.IS_EDIT_MODE);
        Map<String, PieceJointeDescriptor> pjTypes = SolonMgppServiceLocator
            .getEvenementTypeService()
            .getEvenementType(eventType)
            .getPieceJointe();
        for (Entry<String, PieceJointeDescriptor> pj : pjTypes.entrySet()) {
            String pjType = pj.getKey();
            PieceJointeDescriptor pjDescriptor = pj.getValue();
            WidgetDTO pjWidget = new WidgetDTO();
            pjWidget.setName(pjType);
            pjWidget.setLabel(STServiceLocator.getVocabularyService().getEntryLabel("type_piece_jointe", pjType));
            pjWidget.setTypeChamp(isEditMode ? PIECE_JOINTE.toString() : FILE_MULTI.toString());
            pjWidget.setPjMultiValue(pjDescriptor.isMultivalue());
            pjWidget.setLstPieces(MgppWidgetFactory.getListPieceJointe(pjType, dto));
            pjWidget.setRequired(pjDescriptor.isObligatoire());
            widgets.add(pjWidget);
        }
        return widgets;
    }

    private List<SelectValueDTO> getLisEventSuccessifs(EvenementDTO eventDTO) {
        List<EvenementTypeDescriptor> lstEvents = SolonMgppServiceLocator
            .getEvenementTypeService()
            .findEvenementTypeSuccessif(eventDTO.getTypeEvenementName(), eventDTO.getEvenementsSuivants());
        return lstEvents
            .stream()
            .map(eventType -> new SelectValueDTO(eventType.getName(), eventType.getLabel()))
            .collect(Collectors.toList());
    }
}
