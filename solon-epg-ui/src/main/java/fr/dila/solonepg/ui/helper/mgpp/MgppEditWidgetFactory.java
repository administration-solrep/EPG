package fr.dila.solonepg.ui.helper.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum.DEST_COPIE;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.AUTEUR;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.COMMISSIONS;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.COMMISSION_SAISIE;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.COMMISSION_SAISIE_AU_FOND;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.COMMISSION_SAISIE_POUR_AVIS;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.DESTINATAIRE;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.DESTINATAIRE_COPIE;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.DOSSIER_LEGISLATIF;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.EMETTEUR;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.GROUPE_PARLEMENTAIRE;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.ORGANISME;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.PARLEMENTAIRE_SUPPLEANT_LIST;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.PARLEMENTAIRE_TITULAIRE_LIST;
import static fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum.RAPPORTEUR_LIST;
import static java.util.Optional.ofNullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppSelectValueType;
import fr.dila.solonepg.ui.enums.mgpp.MgppSuggestionType;
import fr.dila.solonepg.ui.enums.mgpp.MgppUIConstant;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.core.service.TableReferenceServiceImpl;
import fr.dila.st.api.descriptor.parlement.PropertyDescriptor;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum;
import fr.dila.st.ui.enums.parlement.WidgetModeEnum;
import fr.dila.st.ui.th.bean.mapper.MapPropertyDescToWidget;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class MgppEditWidgetFactory {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppWidgetFactory.class);

    public static final String VALEUR = "valeur";
    public static final String VALUE_BEGINNING = "valueBeginning";
    public static final String VALUE_ENDING = "valueEnding";
    public static final String LST_VALUES_PARAM_NAME = "lstValues";
    public static final String LST_SUGGEST_NAME = "lstSuggestValues";
    public static final String NIVEAU_LECTURE_NUMERO = "niveauLectureNumero";
    public static final ImmutableSet<CommunicationMetadonneeEnum> METADONNEES_INSTITUTION = ImmutableSet.of(
        EMETTEUR,
        DESTINATAIRE,
        DESTINATAIRE_COPIE
    );
    public static final ImmutableSet<String> METADONNEES_IDENTITE = ImmutableSet.of(AUTEUR.getName());
    public static final ImmutableSet<String> METADONNEES_ORGANISME = ImmutableSet.of(
        ORGANISME.getName(),
        COMMISSION_SAISIE.getName(),
        COMMISSION_SAISIE_AU_FOND.getName()
    );
    public static final ImmutableSet<String> METADONNEES_IDENTITE_LIST = ImmutableSet.of(
        MgppCommunicationMetadonneeEnum.COAUTEUR.getName(),
        CommunicationMetadonneeEnum.COAUTEUR.getName(),
        RAPPORTEUR_LIST.getName(),
        PARLEMENTAIRE_TITULAIRE_LIST.getName(),
        PARLEMENTAIRE_SUPPLEANT_LIST.getName()
    );
    public static final ImmutableSet<String> METADONNEES_ORGANISME_LIST = ImmutableSet.of(
        COMMISSIONS.getName(),
        COMMISSION_SAISIE_POUR_AVIS.getName(),
        GROUPE_PARLEMENTAIRE.getName()
    );
    public static final ImmutableSet<String> METADONNEES_LISTSIMPLE = ImmutableSet.of(
        MgppCommunicationMetadonneeEnum.LIBELLE_ANNEXE.getName(),
        DOSSIER_LEGISLATIF.getName()
    );

    private MgppEditWidgetFactory() {}

    public static WidgetDTO getWidgetForEditView(
        SpecificContext context,
        Entry<String, PropertyDescriptor> entry,
        EvenementDTO event,
        boolean edit
    ) {
        PropertyDescriptor propertyDescriptor = entry.getValue();
        CommunicationMetadonneeEnum metaEnum = CommunicationMetadonneeEnum.fromString(propertyDescriptor.getName());
        MgppCommunicationMetadonneeEnum mgppMapper = MgppCommunicationMetadonneeEnum.fromString(
            propertyDescriptor.getName()
        );
        WidgetModeEnum mode = MapPropertyDescToWidget.getWidgetMode(propertyDescriptor, context, false);

        if (metaEnum == null && mgppMapper.getParent() != null) {
            metaEnum = mgppMapper.getParent();
        }

        WidgetDTO widget = MapPropertyDescToWidget.initWidget(
            propertyDescriptor,
            context,
            false,
            MapPropertyDescToWidget.getTypeChamp(metaEnum, mode),
            "label.mgpp.evenement."
        );

        String key = StringUtils.isBlank(mgppMapper.getName()) ? entry.getKey() : mgppMapper.getName();
        Serializable entryValue = event.get(key);
        List<SuggestionDTO> suggestList = ofNullable(entryValue)
            .map(value -> getSuggestList(propertyDescriptor.getName(), value))
            .orElseGet(Collections::emptyList);
        Serializable institution = entryValue;
        if (mgppMapper == DEST_COPIE && entryValue instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> destCopieInsts = ((List<String>) entryValue);
            if (CollectionUtils.isNotEmpty(destCopieInsts)) {
                institution = destCopieInsts.get(0);
            }
        }
        List<Parametre> complexParametres = Lists.newArrayList(
            new Parametre(LST_SUGGEST_NAME, suggestList),
            new Parametre("institution", METADONNEES_INSTITUTION.contains(metaEnum) ? institution : null)
        );
        if (edit) {
            complexParametres.addAll(
                getAdditionalParametres(metaEnum, event, entryValue, widget.getTypeChamp(), mode, suggestList)
            );
            complexParametres.add(new Parametre("hiddenValue", entryValue));
        }

        widget.getParametres().addAll(complexParametres);

        widget.setModifiedInCurVersion(
            isModifiedInCurVersion(propertyDescriptor.getNameWS(), event.getMetasModifiees())
        );

        return widget;
    }

    public static boolean isModifiedInCurVersion(String nameWS, List<String> metadonneesModifiees) {
        if (CollectionUtils.isEmpty(metadonneesModifiees)) {
            return false;
        }
        boolean isModifiedInCurVersion = metadonneesModifiees.contains(nameWS);

        if ("niveau_lecture".equals(nameWS)) {
            isModifiedInCurVersion =
                metadonneesModifiees.contains("niveau_lecture.code") ||
                metadonneesModifiees.contains("niveau_lecture.niveau");
        }

        return isModifiedInCurVersion;
    }

    private static Serializable getReadOnlyValue(Serializable value, WidgetModeEnum mode) {
        if (value instanceof Date) {
            if (mode == WidgetModeEnum.VIEW) {
                return SolonDateConverter.DATETIME_SLASH_A_MINUTE_COLON.format((Date) value);
            }
            return SolonDateConverter.DATE_SLASH.format((Date) value);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static List<Parametre> getAdditionalParametres(
        CommunicationMetadonneeEnum metaEnum,
        EvenementDTO evenement,
        Serializable entryValue,
        String typeChamp,
        WidgetModeEnum mode,
        List<SuggestionDTO> suggestList
    ) {
        List<Parametre> parametres = new ArrayList<>();

        if (metaEnum != null) {
            MgppSelectValueType selectParamBuilder = MgppSelectValueType.fromValue(metaEnum);

            if (!selectParamBuilder.equals(MgppSelectValueType.DEFAULT)) {
                parametres.addAll(selectParamBuilder.buildParametreSelect(entryValue, typeChamp));
            }

            MgppSuggestionType suggestionParamBuilder = MgppSuggestionType.fromValue(metaEnum.getName());

            if (!suggestionParamBuilder.equals(MgppSuggestionType.DEFAULT)) {
                parametres.addAll(suggestionParamBuilder.buildParametres());
                if (CollectionUtils.isNotEmpty(suggestList)) {
                    parametres.add(
                        new Parametre(
                            VALEUR,
                            suggestList.stream().map(SuggestionDTO::getLabel).collect(Collectors.joining(", "))
                        )
                    );
                }
            }
        }

        Map<String, Serializable> niveauLecture = (Map<String, Serializable>) evenement.get(
            EvenementDTOImpl.NIVEAU_LECTURE
        );
        if (CommunicationMetadonneeEnum.NIVEAU_LECTURE == metaEnum && niveauLecture != null) {
            Integer niveauNumero = (Integer) niveauLecture.get(MgppUIConstant.NIVEAU_LECTURE_NIVEAU);
            parametres.add(new Parametre(NIVEAU_LECTURE_NUMERO, niveauNumero));

            if (WidgetTypeEnum.TEXT.toString().equals(typeChamp)) {
                String niveauCode = (String) niveauLecture.get(MgppUIConstant.NIVEAU_LECTURE_CODE);
                String assembleeValue = STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.VOCABULARY_NIVEAU_LECTURE_DIRECTORY, niveauCode);
                String niveauStr = (niveauNumero == null ? "" : niveauNumero + " - ") + assembleeValue;
                parametres.add(new Parametre(VALEUR, niveauStr));
            }
        } else if (parametres.stream().noneMatch(param -> VALEUR.equals(param.getName()))) {
            parametres.add(new Parametre(VALEUR, getReadOnlyValue(entryValue, mode)));
        }
        return parametres;
    }

    private static String getTableRefTypeFromName(String name) {
        if (METADONNEES_IDENTITE.contains(name) || METADONNEES_IDENTITE_LIST.contains(name)) {
            return TableReferenceServiceImpl.IDENTITE;
        } else if (METADONNEES_ORGANISME.contains(name) || METADONNEES_ORGANISME_LIST.contains(name)) {
            return TableReferenceServiceImpl.ORGANISME;
        }

        return "";
    }

    public static List<SuggestionDTO> getSuggestList(String name, Object valeur) {
        List<SuggestionDTO> lstSuggestion = new ArrayList<>();
        if (valeur != null) {
            if (METADONNEES_LISTSIMPLE.contains(name)) {
                if (valeur instanceof String) {
                    Stream
                        .of(((String) valeur).split(", "))
                        .map(text -> new SuggestionDTO(text, text))
                        .forEach(lstSuggestion::add);
                } else if (valeur instanceof List) {
                    lstSuggestion =
                        ((List<String>) valeur).stream()
                            .map(text -> new SuggestionDTO(text, text))
                            .collect(Collectors.toList());
                }
            } else {
                String type = getTableRefTypeFromName(name);

                if (StringUtils.isNotBlank(type)) {
                    final String typeTable = type;
                    List<String> lstValues = new ArrayList<>();
                    if (valeur instanceof String) {
                        lstValues.add((String) valeur);
                    } else if (valeur instanceof List) {
                        lstValues.addAll((List<String>) valeur);
                    }
                    lstSuggestion.addAll(
                        lstValues
                            .stream()
                            .map(cle -> new SuggestionDTO(cle, getLabelFromTableReference(cle, typeTable)))
                            .collect(Collectors.toList())
                    );
                }
            }
        }
        return lstSuggestion;
    }

    private static String getLabelFromTableReference(Object valeur, String type) {
        TableReferenceDTO tableRow = null;
        if (valeur instanceof String) {
            try {
                tableRow =
                    SolonMgppServiceLocator
                        .getTableReferenceService()
                        .findTableReferenceByIdAndType((String) valeur, type, null);
            } catch (Exception e) {
                LOGGER.error(
                    null,
                    STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC,
                    "TableReference = " + type + ", valeur = " + valeur,
                    e
                );
            }

            if (tableRow == null || StringUtils.isBlank(tableRow.getTitle())) {
                return "**inconnu**";
            }
            return tableRow.getTitle();
        }
        return "";
    }
}
