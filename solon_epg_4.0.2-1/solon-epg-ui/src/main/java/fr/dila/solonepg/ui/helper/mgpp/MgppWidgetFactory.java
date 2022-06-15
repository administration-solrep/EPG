package fr.dila.solonepg.ui.helper.mgpp;

import static java.util.Optional.ofNullable;

import fr.dila.solonepg.ui.enums.mgpp.MgppUIConstant;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.core.service.TableReferenceServiceImpl;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.bean.PieceJointeDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;

public class MgppWidgetFactory {

    private MgppWidgetFactory() {}

    public static WidgetDTO createTextWidget(Object obj) {
        WidgetDTO widget = new WidgetDTO("name", "label", "text");

        widget.getParametres().add(new Parametre("valeur", obj == null ? "" : obj.toString()));

        return widget;
    }

    public static WidgetDTO createDateTextWidget(Object obj) {
        return createDateTextWidget(obj, SolonDateConverter.DATE_SLASH);
    }

    public static WidgetDTO createDateTextWidget(Object obj, SolonDateConverter converter) {
        Date date = (Date) obj;

        return createTextWidget(converter.format(date));
    }

    public static WidgetDTO createMultipleDateTextWidget(Object obj) {
        String dateStr = "";
        if (obj != null) {
            dateStr =
                ((ArrayList<Date>) obj).stream()
                    .map(SolonDateConverter.DATE_SLASH::format)
                    .collect(Collectors.joining(", "));
        }

        return createTextWidget(dateStr);
    }

    @SuppressWarnings("unchecked")
    public static boolean isNiveauLectureDisplayable(Object obj) {
        Map<String, Serializable> map = (HashMap<String, Serializable>) obj;
        String niveauCode = (String) map.get(MgppUIConstant.NIVEAU_LECTURE_CODE);
        return niveauCode != null;
    }

    @SuppressWarnings("unchecked")
    public static WidgetDTO createNiveauLectureTextWidget(Object obj) {
        Map<String, Serializable> map = (HashMap<String, Serializable>) obj;
        String niveauCode = (String) map.get(MgppUIConstant.NIVEAU_LECTURE_CODE);
        Integer codeValue = (Integer) map.get(MgppUIConstant.NIVEAU_LECTURE_NIVEAU);
        String assembleeValue = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(VocabularyConstants.VOCABULARY_NIVEAU_LECTURE_DIRECTORY, niveauCode);

        String niveauStr = (codeValue == null ? "" : (codeValue + " - ")) + assembleeValue;

        return createTextWidget(niveauStr);
    }

    public static WidgetDTO createPropertyTextWidget(Object obj) {
        return createTextWidget(getStringOrEmpty((String) obj));
    }

    @SuppressWarnings("unchecked")
    public static WidgetDTO createMultiplePropertyTextWidget(Object obj) {
        String propertiesStr = "";
        if (obj != null) {
            propertiesStr =
                ((ArrayList<String>) obj).stream().map(str -> getStringOrEmpty(str)).collect(Collectors.joining(", "));
        }
        return createTextWidget(propertiesStr);
    }

    private static String getStringOrEmpty(String str) {
        return str == null ? "" : ResourceHelper.getString(str);
    }

    public static WidgetDTO createIdentiteTextWidget(Object obj) {
        String title = identiteToTitle(obj);
        return createTextWidget(title);
    }

    private static String identiteToTitle(Object obj) {
        String mandat = (String) obj;
        TableReferenceDTO dto = SolonMgppServiceLocator
            .getTableReferenceService()
            .findTableReferenceByIdAndType(mandat, TableReferenceServiceImpl.IDENTITE, null);
        return dto == null ? "" : dto.getTitle();
    }

    @SuppressWarnings("unchecked")
    public static WidgetDTO createMultipleIdentiteTextWidget(Object obj) {
        if (obj != null) {
            String titles =
                ((ArrayList<String>) obj).stream()
                    .map(MgppWidgetFactory::identiteToTitle)
                    .collect(Collectors.joining(", "));
            return createTextWidget(titles);
        }
        return createTextWidget("");
    }

    public static WidgetDTO createOrganismeTextWidget(Object obj) {
        String title = organismeToTitle(obj);
        return createTextWidget(title);
    }

    private static String organismeToTitle(Object obj) {
        String id = (String) obj;
        TableReferenceDTO dto = SolonMgppServiceLocator
            .getTableReferenceService()
            .findTableReferenceByIdAndType(id, TableReferenceServiceImpl.ORGANISME, null);
        return dto == null ? "" : dto.getTitle();
    }

    @SuppressWarnings("unchecked")
    public static WidgetDTO createMultipleOrganismeTextWidget(Object obj) {
        if (obj != null) {
            String titles =
                ((ArrayList<String>) obj).stream()
                    .map(MgppWidgetFactory::organismeToTitle)
                    .collect(Collectors.joining(", "));
            return createTextWidget(titles);
        }
        return createTextWidget("");
    }

    @SuppressWarnings("unchecked")
    public static WidgetDTO createFileMultiWidget(Object obj) {
        WidgetDTO widget = new WidgetDTO("name", "label", "file-multi");

        List<PieceJointeDTO> pjs =
            ((List<MgppPieceJointeDTO>) obj).stream()
                .map(MgppWidgetFactory::toPieceJointeDTO)
                .collect(Collectors.toList());

        widget.getLstPieces().addAll(pjs);

        return widget;
    }

    public static List<PieceJointeDTO> getListPieceJointe(String typePieceJointe, EvenementDTO eventDTO) {
        List<MgppPieceJointeDTO> pieceJointeList = eventDTO.getListPieceJointe(typePieceJointe);
        return ofNullable(pieceJointeList)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(MgppWidgetFactory::toPieceJointeDTO)
            .collect(Collectors.toList());
    }

    private static PieceJointeDTO toPieceJointeDTO(MgppPieceJointeDTO pj) {
        PieceJointeDTO dto = new PieceJointeDTO();
        dto.setPieceJointeId(pj.getIdInterneEpp());
        dto.setPieceJointeTitre(pj.getLibelle());
        dto.setPieceJointeUrl(pj.getUrl());
        dto.setPieceJointeType(pj.getType());

        dto.setModifiedMetaList(new ArrayList<>());
        dto.setModifiedFileList(new ArrayList<>());
        dto.setListPieceJointeFichier(
            pj.getFichier().stream().map(MgppWidgetFactory::toDocumentDTO).collect(Collectors.toList())
        );
        return dto;
    }

    private static DocumentDTO toDocumentDTO(PieceJointeFichierDTO fichier) {
        return new DocumentDTO(
            fichier.getNomFichier(),
            fichier.getNomFichier(),
            "auteur",
            "date",
            "version",
            "link",
            "extension"
        );
    }

    public static WidgetDTO createBooleanTextWidget(Object obj) {
        return createBooleanTextWidget((Boolean) obj, MgppUIConstant.LABEL_YES, MgppUIConstant.LABEL_NO);
    }

    private static WidgetDTO createBooleanTextWidget(Boolean bool, String labelTrue, String labelFalse) {
        String msg = "";
        if (BooleanUtils.isTrue(bool)) {
            msg = ResourceHelper.getString(labelTrue);
        } else if (BooleanUtils.isFalse(bool)) {
            msg = ResourceHelper.getString(labelFalse);
        }
        return createTextWidget(msg);
    }

    public static WidgetDTO createPositionAlerteTextWidget(Object obj) {
        return createBooleanTextWidget(
            (Boolean) obj,
            MgppUIConstant.LABEL_DEBUT_ALERTE,
            MgppUIConstant.LABEL_FIN_ALERTE
        );
    }

    private static WidgetDTO createVocabularyTextWidget(Object obj, String directory) {
        String text = getStringOrEmpty((String) obj);
        String entryLabel = STServiceLocator.getVocabularyService().getEntryLabel(directory, text);
        return createTextWidget(VocabularyServiceImpl.UNKNOWN_ENTRY.equals(entryLabel) ? text : entryLabel);
    }

    public static WidgetDTO createResultatCMPTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_RESULTAT_CMP_DIRECTORY);
    }

    public static WidgetDTO createNatureRapportTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_NATURE_RAPPORT_DIRECTORY);
    }

    public static WidgetDTO createTypeLoiTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_TYPE_LOI_DIRECTORY);
    }

    public static WidgetDTO createDecisionProcAccTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_DECISION_PROC_ACC_DIRECTORY);
    }

    public static WidgetDTO createSortAdoptionTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY);
    }

    public static WidgetDTO createTypeActeEppTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_TYPE_ACTE_EPP_DIRECTORY);
    }

    public static WidgetDTO createTypeSensAvisTextWidget(Object obj) {
        return createVocabularyTextWidget(obj, VocabularyConstants.VOCABULARY_SENS_AVIS_DIRECTORY);
    }
}
