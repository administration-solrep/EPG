package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.solonmgpp.core.dto.EvenementDTOImpl.DESTINATAIRE;
import static fr.dila.solonmgpp.core.dto.EvenementDTOImpl.DESTINATAIRE_COPIE;
import static fr.dila.solonmgpp.core.dto.EvenementDTOImpl.EMETTEUR;
import static fr.dila.solonmgpp.core.dto.EvenementDTOImpl.ID_DOSSIER;
import static fr.dila.solonmgpp.core.dto.EvenementDTOImpl.NOR;
import static fr.dila.st.ui.enums.STContextDataKey.DOSSIER_ID;
import static fr.dila.st.ui.enums.STContextDataKey.TRANSMETTRE_PAR_MEL_FORM;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppUIConstant;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.services.mgpp.MgppDossierUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppEvenementActionUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PieceJointeDescriptor;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.dto.MgppPieceJointeDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.descriptor.parlement.PropertyDescriptor;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum;
import fr.dila.st.ui.th.bean.TransmettreParMelForm;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.server.jaxrs.batch.Batch;
import org.nuxeo.ecm.automation.server.jaxrs.batch.BatchManager;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppEvenementActionUIServiceImpl implements MgppEvenementActionUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppEvenementActionUIServiceImpl.class);

    private static final String PIECE_JOINTE_FILE_SUFFIX = "-file";
    private static final String PIECE_JOINTE_UPLOAD_BATCH_ID_SUFFIX = "-uploadBatchId";
    private static final String PIECE_JOINTE_URL_SUFFIX = "-url";
    private static final String PIECE_JOINTE_TITRE_SUFFIX = "-titre";
    private static final String PIECE_JOINTE_FROM_DOSSIER_SUFFIX = "-fromDossier";
    private static final Pattern META_KEY_SUFFIXES_PATTERN = Pattern.compile(
        PIECE_JOINTE_TITRE_SUFFIX +
        "|" +
        PIECE_JOINTE_URL_SUFFIX +
        "|" +
        PIECE_JOINTE_UPLOAD_BATCH_ID_SUFFIX +
        "|" +
        PIECE_JOINTE_FROM_DOSSIER_SUFFIX
    );
    private static final Set<String> METADONNEES_CHECKED_SEPERATELY = ImmutableSet.of(
        NOR,
        ID_DOSSIER,
        EMETTEUR,
        DESTINATAIRE,
        DESTINATAIRE_COPIE
    );
    private static final Set<MgppCommunicationMetadonneeEnum> MULTI_VALUED_METADONNEES = ImmutableSet.of(
        MgppCommunicationMetadonneeEnum.LIBELLE_ANNEXE,
        MgppCommunicationMetadonneeEnum.DOSSIER_LEGISLATIF
    );
    private static final Set<MgppCommunicationMetadonneeEnum> BOOLEAN_VALUED_METADONNEES = ImmutableSet.of(
        MgppCommunicationMetadonneeEnum.REDEPOT,
        MgppCommunicationMetadonneeEnum.RECTIFICATIF,
        MgppCommunicationMetadonneeEnum.DEMANDE_VOTE
    );

    private static final Map<String, String> ALERTE_FOR_PROCEDURES = ImmutableMap
        .<String, String>builder()
        .put(VocabularyConstants.CATEGORIE_EVENEMENT_PROCEDURE_LEGISLATIVE_VALUE, EvenementType.ALERTE_01.value())
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_ORGANISATION_SESSION_EXTRAORDINAIRE_VALUE,
            EvenementType.ALERTE_03.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_CONSULTATION_ASSEMBLEE_PROJET_NOMINATION_VALUE,
            EvenementType.ALERTE_04.value()
        )
        .put(VocabularyConstants.CATEGORIE_EVENEMENT_CONVOCATION_CONGRES_VALUE, EvenementType.ALERTE_05.value())
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_DEMANDE_PROLONGATION_INTERVENTION_EXTERIEURE_VALUE,
            EvenementType.ALERTE_06.value()
        )
        .put(VocabularyConstants.CATEGORIE_EVENEMENT_RESOLUTION_ARTICLE_34_1_VALUE, EvenementType.ALERTE_07.value())
        .put(VocabularyConstants.CATEGORIE_EVENEMENT_DEPOT_RAPPORT_PARLEMENT_VALUE, EvenementType.ALERTE_08.value())
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_INSERTION_INFORMATION_PARLEMENTAIRE_JO_VALUE,
            EvenementType.ALERTE_09.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_ORGANISME_EXTRA_PARLEMENTAIRE_VALUE,
            EvenementType.ALERTE_10.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_28_3C_VALUE,
            EvenementType.ALERTE_13.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_DECLARATION_DE_POLITIQUE_GENERALE_VALUE,
            EvenementType.ALERTE_11.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C_VALUE,
            EvenementType.ALERTE_12.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_DEMANDE_AUDITION_PERSONNES_EMPLOIS_ENVISAGEE_VALUE,
            EvenementType.ALERTE_14.value()
        )
        .put(
            VocabularyConstants.CATEGORIE_EVENEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES_VALUE,
            EvenementType.ALERTE_15.value()
        )
        .put(STVocabularyConstants.CATEGORIE_EVENEMENT_PROCEDURE_CENSURE_VALUE, EvenementType.ALERTE_02.value())
        .build();

    private void checkMandatoryField(EvenementDTO evenementDTO, PropertyDescriptor prop, List<String> messageList) {
        if (!isRequiredFieldValid(evenementDTO, prop.getName())) {
            messageList.add(
                SolonMgppI18nConstant.REQUIRED_FIELD_MAP.get(prop.getName()) != null
                    ? ResourceHelper.getString(SolonMgppI18nConstant.REQUIRED_FIELD_MAP.get(prop.getName()))
                    : getStringForFieldName(prop.getName())
            );
        }
    }

    /**
     * Returns string from resource if found, else returns name
     *
     * @param name
     * @return
     */
    private String getStringForFieldName(String name) {
        String resourceName = "label.mgpp.evenement." + name;
        String message = ResourceHelper.getString(resourceName);
        return resourceName.equals(message) ? name : message;
    }

    private void checkPJSent(EvenementDTO evenementDTO, List<String> messageList, SpecificContext context) {
        EvenementTypeDescriptor descriptor = SolonMgppServiceLocator
            .getEvenementTypeService()
            .getEvenementType(evenementDTO.getTypeEvenementName());

        for (String typePieceJointe : descriptor.getPieceJointe().keySet()) {
            String typePieceJointeLabel = STServiceLocator
                .getVocabularyService()
                .getEntryLabel(VocabularyConstants.VOCABULARY_PIECE_JOINTE_DIRECTORY, typePieceJointe);
            List<MgppPieceJointeDTO> listPieceJointe = evenementDTO.getListPieceJointe(typePieceJointe);
            if (
                descriptor.getPieceJointe().get(typePieceJointe).isObligatoire() &&
                (CollectionUtils.isEmpty(listPieceJointe))
            ) {
                messageList.add(typePieceJointeLabel);
            }

            if (listPieceJointe != null) {
                listPieceJointe
                    .stream()
                    .map(MgppPieceJointeDTO::getFichier)
                    .filter(CollectionUtils::isEmpty)
                    .forEach(
                        fichier ->
                            context
                                .getMessageQueue()
                                .addErrorToQueue(
                                    ResourceHelper.getString(
                                        SolonMgppI18nConstant.PIECE_JOINTE_FICHIER_MANQUANT,
                                        typePieceJointeLabel
                                    )
                                )
                    );
            }
        }
    }

    private boolean checkNor(
        EvenementDTO evenementDTO,
        PropertyDescriptor nor,
        PropertyDescriptor dossier,
        List<String> messageList,
        SpecificContext context
    ) {
        NORService norService = SolonEpgServiceLocator.getNORService();
        boolean ok = true;

        if (checkPropertyAgainstStringValue(nor, evenementDTO.getNor())) {
            messageList.add(ResourceHelper.getString(SolonMgppI18nConstant.LABEL_NOR));
            ok = false;
        } else if (
            nor != null &&
            nor.isObligatoire() &&
            StringUtils.isNotBlank(evenementDTO.getNor()) &&
            !norService.isStructNorValide(evenementDTO.getNor())
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.NOR_STRUCTURE_INCORRECTE));
            ok = false;
        }

        if (checkPropertyAgainstStringValue(dossier, evenementDTO.getIdDossier())) {
            messageList.add(ResourceHelper.getString(SolonMgppI18nConstant.LABEL_IDENTIFIANT_DOSSIER));
            ok = false;
        }

        return ok;
    }

    private boolean checkPropertyAgainstStringValue(PropertyDescriptor prop, String value) {
        return (prop != null && prop.isObligatoire() && StringUtils.isBlank(value));
    }

    private boolean checkPropertyAgainstListValue(PropertyDescriptor prop, List<String> values) {
        return (prop != null && prop.isObligatoire() && CollectionUtils.isEmpty(values));
    }

    private boolean checkDataCoherence(EvenementDTO evenementDTO, SpecificContext context) {
        boolean ok = true;
        if (evenementDTO.getEmetteur().equals(evenementDTO.getDestinataire())) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.SAME_EMETTEUR_DESTIANTAIRE_ERROR));
            ok = false;
        }

        if (evenementDTO.getCopie() != null && evenementDTO.getCopie().contains(evenementDTO.getEmetteur())) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.COPIE_CONTAINS_EMETTEUR));

            ok = false;
        }

        if (evenementDTO.getCopie() != null && evenementDTO.getCopie().contains(evenementDTO.getDestinataire())) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.COPIE_CONTAINS_DESTINATAIRE));

            ok = false;
        }
        return ok;
    }

    private boolean checkDestinataire(
        PropertyDescriptor emetteur,
        PropertyDescriptor destinataire,
        PropertyDescriptor copie,
        SpecificContext context,
        List<String> messageList,
        EvenementDTO evenementDTO
    ) {
        boolean ok = true;
        if (checkPropertyAgainstStringValue(emetteur, evenementDTO.getEmetteur())) {
            messageList.add(ResourceHelper.getString(SolonMgppI18nConstant.LABEL_EMETTEUR));
            ok = false;
        }

        if (checkPropertyAgainstStringValue(destinataire, evenementDTO.getDestinataire())) {
            messageList.add(ResourceHelper.getString(SolonMgppI18nConstant.LABEL_DESTINATAIRE));
            ok = false;
        }

        if (checkPropertyAgainstListValue(copie, evenementDTO.getCopie())) {
            List<SelectValueDTO> lstInstit = MgppUIServiceLocator
                .getMgppSelectValueUIService()
                .getSelectableInstitutions();
            evenementDTO.setCopie(
                lstInstit
                    .stream()
                    .map(SelectValueDTO::getId)
                    .filter(id -> !id.equals(evenementDTO.getDestinataire()))
                    .collect(Collectors.toList())
            );
        }

        ok = ok && checkDataCoherence(evenementDTO, context);

        if (copie == null && evenementDTO.getCopie() != null) {
            // clear list des destinataireCopie
            evenementDTO.getCopie().clear();
        }

        return ok;
    }

    public boolean checkMetaDonnee(EvenementDTO evenementDTO, boolean publier, SpecificContext context) {
        /**
         * on récupère la liste des metadonnée qu'il faut pour valider l'événement en cours.
         */
        if (MapUtils.isEmpty(evenementDTO)) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE));
            return false;
        }

        Map<String, PropertyDescriptor> map = SolonMgppServiceLocator
            .getMetaDonneesService()
            .getMapProperty(context.getFromContextData(MgppContextDataKey.EVENT_TYPE));
        List<String> messageList = new ArrayList<>();

        // check nor / idDossier
        checkNor(evenementDTO, map.get(NOR), map.get(ID_DOSSIER), messageList, context);

        // check cohérence Emetteur/Destinataire/Copie
        checkDestinataire(
            map.get(EMETTEUR),
            map.get(DESTINATAIRE),
            map.get(DESTINATAIRE_COPIE),
            context,
            messageList,
            evenementDTO
        );

        if (publier) {
            // check field
            map
                .values()
                .stream()
                .filter(prop -> !METADONNEES_CHECKED_SEPERATELY.contains(prop.getName()))
                .filter(PropertyDescriptor::isObligatoire)
                .forEach(prop -> checkMandatoryField(evenementDTO, prop, messageList));

            // check piece jointe
            checkPJSent(evenementDTO, messageList, context);

            // si EVT 19 : RG-PROC-LEG-03
            try {
                SolonMgppServiceLocator
                    .getEvenementService()
                    .checkProcedureAcceleree(context.getSession(), evenementDTO);
            } catch (NuxeoException e) {
                LOGGER.error(MgppLogEnumImpl.FAIL_BUILD_EVENT_TEC, e);
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        }
        if (CollectionUtils.isNotEmpty(messageList)) {
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString(SolonMgppI18nConstant.METAS_MANQUANTES_LIST) +
                    " " +
                    StringUtils.join(messageList, ", ")
                );
        }
        if (messageList.isEmpty() && !context.getMessageQueue().hasMessageInQueue()) {
            if (publier) {
                context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("publier.evenement.ok"));
            } else {
                context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("modifier.evenement.ok"));
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean isRequiredFieldValid(EvenementDTO evenementDTO, String fieldName) {
        Serializable value = null;
        if (EvenementDTOImpl.DESTINATAIRE_COPIE.equals(fieldName)) {
            fieldName = EvenementDTOImpl.COPIE;
        } else if (EvenementDTOImpl.COAUTEUR.equals(fieldName)) {
            fieldName = EvenementDTOImpl.CO_AUTEUR;
        }
        String[] props = fieldName.split("\\.");
        for (String prop : props) {
            if (value == null) {
                value = evenementDTO.get(prop);
            } else {
                value = ((Map<String, Serializable>) value).get(prop);
            }
        }

        if (EvenementDTOImpl.NIVEAU_LECTURE.equals(fieldName)) {
            // recuperation du codeLecture si autre que AN ou SENAT, le niveau n'est pas obligatoire
            String code = (String) ((Map<String, Serializable>) evenementDTO.get(EvenementDTOImpl.NIVEAU_LECTURE)).get(
                    MgppUIConstant.NIVEAU_LECTURE_CODE
                );
            if (!(NiveauLectureCode.AN.value().equals(code) || NiveauLectureCode.SENAT.value().equals(code))) {
                return true;
            }
        }

        return !(
            value == null ||
            value instanceof String &&
            StringUtils.isEmpty((String) value) ||
            value instanceof Collection &&
            ((Collection) value).isEmpty()
        );
    }

    private void convertStringFieldsToMap(EvenementDTO evenementDTO, SpecificContext context) {
        String strVersionCourante = evenementDTO.get(EvenementDTOImpl.VERSION_COURANTE).toString();
        try {
            Map<String, Object> versionCourante = new ObjectMapper().readValue(strVersionCourante, Map.class);
            evenementDTO.put(EvenementDTOImpl.VERSION_COURANTE, (Serializable) versionCourante);

            EvenementType type = EvenementType.fromValue(evenementDTO.getTypeEvenementName());
            Map<String, Serializable> mapType = new HashMap<>();
            mapType.put(EvenementDTOImpl.NAME, type.value());
            mapType.put(EvenementDTOImpl.LABEL, "");
            evenementDTO.put(EvenementDTOImpl.TYPE_EVENEMENT, (Serializable) mapType);

            Map<String, Object> mapFront = context.getFromContextData(MgppContextDataKey.MAP_EVENT);
            Serializable niveauLectureCode = (Serializable) mapFront.get(EvenementDTOImpl.NIVEAU_LECTURE);
            Serializable niveauLectureNumero = (Serializable) mapFront.get(MgppEditWidgetFactory.NIVEAU_LECTURE_NUMERO);
            if (niveauLectureCode != null || niveauLectureNumero != null) {
                Map<String, Serializable> mapLecture = new HashMap<>();
                if (StringUtils.isNotBlank((String) niveauLectureCode)) {
                    mapLecture.put("code", niveauLectureCode);
                }
                if (StringUtils.isNotBlank((String) niveauLectureNumero)) {
                    mapLecture.put("niveau", niveauLectureNumero);
                }
                evenementDTO.put(EvenementDTOImpl.NIVEAU_LECTURE, (Serializable) mapLecture);
            }
        } catch (Exception e) {
            throw new NuxeoException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private EvenementDTO buildEvenementDTO(SpecificContext context, boolean isCreation) {
        Map<String, Object> map = context.getFromContextData(MgppContextDataKey.MAP_EVENT);
        EvenementService evenementService = SolonMgppServiceLocator.getEvenementService();
        MgppDossierUIService dossierUIService = MgppUIServiceLocator.getMgppDossierUIService();
        EvenementDTO evenementDTO = null;
        String typeEvent = context.getFromContextData(MgppContextDataKey.EVENT_TYPE);
        if (StringUtils.isNotBlank(context.getFromContextData(COMMUNICATION_ID))) {
            if (isCreation) {
                evenementDTO =
                    evenementService.initialiserEvenementSuccessif(
                        dossierUIService.getCurrentEvenementDTO(context),
                        typeEvent,
                        false,
                        context.getSession()
                    );
            } else {
                String fullVersion = "";
                try {
                    Map<String, Object> versionCourante = new ObjectMapper()
                    .readValue(map.get(EvenementDTOImpl.VERSION_COURANTE).toString(), Map.class);
                    fullVersion =
                        versionCourante.get(EvenementDTOImpl.MAJEUR) +
                        "." +
                        versionCourante.get(EvenementDTOImpl.MINEUR);
                } catch (Exception e) {
                    throw new NuxeoException(e);
                }
                context.putInContextData(MgppContextDataKey.VERSION_ID, fullVersion);
                evenementDTO = dossierUIService.getCurrentEvenementDTO(context);
            }
        }
        if (evenementDTO == null) {
            evenementDTO = evenementService.initialiserEvenement(typeEvent, false, context.getSession());
        }
        final EvenementDTO evtDTO = evenementDTO;

        List<String> dateFields = CommunicationMetadonneeEnum.getAllNamesFromWidgetType(WidgetTypeEnum.DATE);
        map
            .entrySet()
            .stream()
            .filter(entry -> (entry.getValue() instanceof Serializable))
            .forEach(
                entry -> addDataToEvenementDTO(evtDTO, entry.getKey(), (Serializable) entry.getValue(), dateFields)
            );
        convertStringFieldsToMap(evenementDTO, context);
        addAllPJtoEvenement(map, evenementDTO, context);
        MgppUIServiceLocator.getMgppDossierUIService().resetCurrentEvenementDTO(context);

        return evenementDTO;
    }

    private void addDataToEvenementDTO(EvenementDTO dto, String key, Serializable value, List<String> dateFields) {
        if (!CommunicationMetadonneeEnum.HORODATAGE.getName().equals(key) && value != null) {
            Serializable convertedValue = dateFields.contains(key)
                ? SolonDateConverter.DATE_SLASH.parseToDateOrNull(value.toString())
                : value;
            convertedValue = StringHelper.removeNullStrings(convertedValue);
            MgppCommunicationMetadonneeEnum metaMapper = MgppCommunicationMetadonneeEnum.fromString(key);
            if (MULTI_VALUED_METADONNEES.contains(metaMapper) && convertedValue instanceof String) {
                convertedValue = (Serializable) Collections.singletonList(convertedValue);
            }
            if (BOOLEAN_VALUED_METADONNEES.contains(metaMapper) && convertedValue instanceof String) {
                convertedValue = BooleanUtils.toBoolean((String) convertedValue);
            }
            if (dto.containsKey(key)) {
                dto.put(key, convertedValue);
            } else if (metaMapper != null && StringUtils.isNotBlank(metaMapper.getName())) {
                dto.put(metaMapper.getName(), convertedValue);
            }
        }
    }

    private void addAllPJtoEvenement(
        Map<String, Object> frontData,
        EvenementDTO evenementDTO,
        SpecificContext context
    ) {
        Map<String, PieceJointeDescriptor> lstExpectedPJ = SolonMgppServiceLocator
            .getEvenementTypeService()
            .getEvenementType(evenementDTO.getTypeEvenementName())
            .getPieceJointe();

        lstExpectedPJ
            .values()
            .stream()
            .forEach(pjDesc -> mapPJTypeToEvenement(pjDesc.getType(), frontData, evenementDTO, context));
    }

    private void mapPJTypeToEvenement(
        String type,
        Map<String, Object> frontData,
        EvenementDTO evenementDTO,
        SpecificContext context
    ) {
        List<MgppPieceJointeDTO> newPiecesJointes = new ArrayList<>();

        List<MgppPieceJointeDTO> oldPiecesJointes = ObjectHelper.requireNonNullElseGet(
            evenementDTO.getListPieceJointe(type),
            ArrayList::new
        );
        newPiecesJointes.addAll(
            oldPiecesJointes
                .stream()
                .map(pj -> updateOrDeletePieceJointe(context, pj, frontData))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );

        newPiecesJointes.addAll(createPiecesJointesForType(context, type, frontData));

        evenementDTO.put(type, Lists.newArrayList(newPiecesJointes));
    }

    private static MgppPieceJointeDTO updateOrDeletePieceJointe(
        SpecificContext context,
        MgppPieceJointeDTO pieceJointe,
        Map<String, Object> frontData
    ) {
        Map<String, Object> metadonneesPJ = frontData
            .entrySet()
            .stream()
            .filter(entry -> StringUtils.startsWith(entry.getKey(), pieceJointe.getIdInterneEpp()))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        MgppPieceJointeDTO updatedPieceJointe = null;
        if (MapUtils.isNotEmpty(metadonneesPJ)) {
            updatedPieceJointe = updatePieceJointe(context, pieceJointe, metadonneesPJ);
        }
        return updatedPieceJointe;
    }

    /**
     * Met à jour une pièce jointe
     */
    private static MgppPieceJointeDTO updatePieceJointe(
        SpecificContext context,
        MgppPieceJointeDTO pieceJointe,
        Map<String, Object> metadonneesPJ
    ) {
        String pjId = pieceJointe.getIdInterneEpp();
        if (metadonneesPJ.get(pjId + PIECE_JOINTE_TITRE_SUFFIX) != null) {
            pieceJointe.setLibelle((String) metadonneesPJ.get(pjId + PIECE_JOINTE_TITRE_SUFFIX));
        }
        if (metadonneesPJ.get(pjId + PIECE_JOINTE_URL_SUFFIX) != null) {
            pieceJointe.setUrl((String) metadonneesPJ.get(pjId + PIECE_JOINTE_URL_SUFFIX));
        }
        // Garder uniquement les fichiers qui n'ont pas été supprimés
        List<String> nomFichiers = metadonneesPJ
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().contains(PIECE_JOINTE_FILE_SUFFIX))
            .map(entry -> (String) entry.getValue())
            .collect(Collectors.toList());
        pieceJointe.setFichier(
            pieceJointe
                .getFichier()
                .stream()
                .filter(fichier -> nomFichiers.contains(fichier.getNomFichier()))
                .collect(Collectors.toList())
        );
        // Ajouter les nouveaux fichiers téléchargés
        addUploadedFichiersToPieceJointe(
            pieceJointe,
            (String) metadonneesPJ.get(pjId + PIECE_JOINTE_UPLOAD_BATCH_ID_SUFFIX),
            context.getSession()
        );
        // Ajouter les fichiers du dossier
        List<String> fichiersFromDossierIds = metadonneesPJ
            .entrySet()
            .stream()
            .filter(entry -> StringUtils.startsWith(entry.getKey(), pjId + PIECE_JOINTE_FROM_DOSSIER_SUFFIX))
            .flatMap(entry -> ((List<String>) entry.getValue()).stream())
            .collect(Collectors.toList());
        addFichiersFromDossierToPieceJointe(pieceJointe, fichiersFromDossierIds, context.getSession());
        return pieceJointe;
    }

    /**
     * Ajoute les nouveaux fichiers de l'upload batch à la pièce jointe
     */
    private static void addUploadedFichiersToPieceJointe(
        MgppPieceJointeDTO pieceJointe,
        String uploadBatchId,
        CoreSession session
    ) {
        if (StringUtils.isNotBlank(uploadBatchId)) {
            BatchManager batchManager = ServiceUtil.getRequiredService(BatchManager.class);
            Batch batch = batchManager.getBatch(uploadBatchId);
            if (batch != null) {
                List<PieceJointeFichierDTO> pieceJointeFichierList = ObjectHelper.requireNonNullElseGet(
                    pieceJointe.getFichier(),
                    ArrayList::new
                );
                pieceJointeFichierList.addAll(
                    batch
                        .getBlobs()
                        .stream()
                        .map(
                            blob ->
                                SolonMgppServiceLocator
                                    .getPieceJointeService()
                                    .createPieceJointeFichierFromBlob(blob, session)
                        )
                        .collect(Collectors.toList())
                );
                pieceJointe.setFichier(pieceJointeFichierList);
            }
        }
    }

    /**
     * Ajoute les fichiers du fond de dossier ou du parapheur à la pièce jointe
     */
    private static void addFichiersFromDossierToPieceJointe(
        MgppPieceJointeDTO pieceJointe,
        List<String> fichierIds,
        CoreSession session
    ) {
        if (CollectionUtils.isNotEmpty(fichierIds)) {
            List<PieceJointeFichierDTO> pieceJointeFichierList = ObjectHelper.requireNonNullElseGet(
                pieceJointe.getFichier(),
                ArrayList::new
            );
            pieceJointeFichierList.addAll(
                fichierIds
                    .stream()
                    .map(IdRef::new)
                    .map(session::getDocument)
                    .map(dm -> dm.getAdapter(FileSolonEpg.class))
                    .map(FileSolonEpg::getContent)
                    .map(
                        blob ->
                            SolonMgppServiceLocator
                                .getPieceJointeService()
                                .createPieceJointeFichierFromBlob(blob, session)
                    )
                    .collect(Collectors.toList())
            );
            pieceJointe.setFichier(pieceJointeFichierList);
        }
    }

    /**
     * Crée les nouvelles pièces jointes d'un même type
     */
    private static List<MgppPieceJointeDTO> createPiecesJointesForType(
        SpecificContext context,
        String typePJ,
        Map<String, Object> frontData
    ) {
        return frontData
            .keySet()
            .stream()
            .filter(key -> key.startsWith(typePJ))
            .map(key -> META_KEY_SUFFIXES_PATTERN.matcher(key).replaceAll(StringUtils.EMPTY))
            .distinct()
            .map(key -> createPieceJointe(context, typePJ, frontData, key))
            .collect(Collectors.toList());
    }

    private static MgppPieceJointeDTO createPieceJointe(
        SpecificContext context,
        String typePJ,
        Map<String, Object> frontData,
        String metaKey
    ) {
        CoreSession session = context.getSession();
        MgppPieceJointeDTO pieceJointe = new MgppPieceJointeDTOImpl();

        pieceJointe.setType(typePJ);
        pieceJointe.setLibelle((String) frontData.getOrDefault(metaKey + PIECE_JOINTE_TITRE_SUFFIX, null));
        pieceJointe.setUrl((String) frontData.getOrDefault(metaKey + PIECE_JOINTE_URL_SUFFIX, null));

        if (frontData.containsKey(metaKey + PIECE_JOINTE_UPLOAD_BATCH_ID_SUFFIX)) {
            addUploadedFichiersToPieceJointe(
                pieceJointe,
                (String) frontData.get(metaKey + PIECE_JOINTE_UPLOAD_BATCH_ID_SUFFIX),
                session
            );
        }
        if (frontData.containsKey(metaKey + PIECE_JOINTE_FROM_DOSSIER_SUFFIX)) {
            addFichiersFromDossierToPieceJointe(
                pieceJointe,
                (List<String>) frontData.get(metaKey + PIECE_JOINTE_FROM_DOSSIER_SUFFIX),
                context.getSession()
            );
        }
        return pieceJointe;
    }

    @Override
    public String creerEvenement(SpecificContext context) {
        Boolean publier = context.getFromContextData(MgppContextDataKey.PUBLIER);
        EvenementDTO evenementDTO = buildEvenementDTO(context, true);

        if (checkMetaDonnee(evenementDTO, publier, context)) {
            checkIdDossier(evenementDTO);

            // sauvegarde de la communication
            try {
                SolonMgppServiceLocator
                    .getEvenementService()
                    .createEvenement(evenementDTO, publier, context.getSession());
            } catch (NuxeoException e) {
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        }
        return evenementDTO.getIdEvenement();
    }

    @Override
    public void rectifierEvenement(SpecificContext context) {
        Boolean publier = context.getFromContextData(MgppContextDataKey.PUBLIER);
        EvenementDTO evenementDTO = buildEvenementDTO(context, false);

        if (checkMetaDonnee(evenementDTO, publier, context)) {
            checkIdDossier(evenementDTO);

            // sauvegarde de la communication
            try {
                SolonMgppServiceLocator
                    .getEvenementService()
                    .rectifierEvenement(evenementDTO, publier, context.getSession());
            } catch (NuxeoException e) {
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        }
    }

    @Override
    public void completerEvenement(SpecificContext context) {
        Boolean publier = context.getFromContextData(MgppContextDataKey.PUBLIER);
        EvenementDTO evenementDTO = buildEvenementDTO(context, false);

        if (checkMetaDonnee(evenementDTO, publier, context)) {
            checkIdDossier(evenementDTO);

            // sauvegarde de la communication
            try {
                SolonMgppServiceLocator
                    .getEvenementService()
                    .completerEvenement(evenementDTO, publier, context.getSession());
            } catch (NuxeoException e) {
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        }
    }

    private void checkIdDossier(EvenementDTO evenementDTO) {
        String idDossier = evenementDTO.getIdDossier();

        if (StringUtils.isNotBlank(idDossier)) {
            if (
                !evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT25) &&
                !evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT28) &&
                !evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT44) &&
                !evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT44TER)
            ) {
                evenementDTO.setNor(idDossier);
            }
        } else {
            idDossier = evenementDTO.getNor();
            evenementDTO.setIdDossier(idDossier);
        }
    }

    @Override
    public TransmettreParMelForm getTransmettreParMelForm(SpecificContext context) {
        String idEvenement = context.getFromContextData(COMMUNICATION_ID);
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        return new TransmettreParMelForm(
            idEvenement,
            Optional
                .ofNullable(evenementDTO)
                .map(
                    evt ->
                        Stream
                            .of(evt.getTypeEvenementLabel(), evt.getObjet())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(" "))
                )
                .orElse("")
        );
    }

    @Override
    public void transmettreParMelEnvoyer(SpecificContext context) {
        TransmettreParMelForm form = context.getFromContextData(TRANSMETTRE_PAR_MEL_FORM);

        try {
            SolonMgppServiceLocator
                .getMailService()
                .sendMailEpp(
                    form.getMessage(),
                    form.getObjet(),
                    form.getFullDestinataires(),
                    form.getIdMessage(),
                    context.getSession()
                );
            context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_MAIL));
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_MAIL));
            LOGGER.error(STLogEnumImpl.SEND_MAIL_TEC, e);
        }
    }

    @Override
    public void supprimerEvenement(SpecificContext context) {
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        try {
            SolonMgppServiceLocator.getEvenementService().supprimerEvenement(evenementDTO, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_SUPPRIMER);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_SUPPRIMER));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void annulerEvenement(SpecificContext context) {
        String idEvenement = context.getFromContextData(COMMUNICATION_ID);
        try {
            SolonMgppServiceLocator.getEvenementService().annulerEvenement(idEvenement, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_ANNULER);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_ANNULER));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void accepterVersion(SpecificContext context) {
        String idEvenement = context.getFromContextData(COMMUNICATION_ID);
        try {
            SolonMgppServiceLocator.getEvenementService().accepterVersion(idEvenement, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_ACCEPTER);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_ACCEPTER));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void rejeterVersion(SpecificContext context) {
        String idEvenement = context.getFromContextData(COMMUNICATION_ID);
        try {
            SolonMgppServiceLocator.getEvenementService().rejeterVersion(idEvenement, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_REJETER);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_REJETER));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void accuserReceptionVersion(SpecificContext context) {
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        try {
            SolonMgppServiceLocator.getEvenementService().accuserReceptionVersion(evenementDTO, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_AR);
        } catch (NuxeoException e) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_AR));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void abandonnerVersion(SpecificContext context) {
        String idEvenement = context.getFromContextData(COMMUNICATION_ID);
        try {
            SolonMgppServiceLocator.getEvenementService().abandonnerVersion(idEvenement, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_ABANDON);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_ABANDON));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void enCoursTraitementEvenement(SpecificContext context) {
        String idEvenement = context.getFromContextData(COMMUNICATION_ID);
        try {
            SolonMgppServiceLocator
                .getEvenementService()
                .enCoursDeTraitementEvenement(idEvenement, context.getSession());
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_EN_COURS_DE_TRAITEMENT);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_TRAITER));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    @Override
    public void traiterEvenement(SpecificContext context) {
        CoreSession session = context.getSession();
        MgppDossierUIService dossierUIService = MgppUIServiceLocator.getMgppDossierUIService();
        EvenementDTO evenementDTO = dossierUIService.getCurrentEvenementDTO(context);
        MgppMessageDTO messageDTO = dossierUIService.getCurrentMessageDTO(context);
        context.putInContextData(DOSSIER_ID, evenementDTO.getIdDossier());
        DocumentModel dossierDoc = Optional
            .ofNullable(dossierUIService.getCurrentDossier(context))
            .map(Dossier::getDocument)
            .orElse(null);
        DossierLink dossierLink = null;
        if (dossierDoc != null) {
            EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            dossierLink =
                corbeilleService
                    .findDossierLink(session, dossierDoc.getId())
                    .stream()
                    .findFirst()
                    .map(dm -> dm.getAdapter(DossierLink.class))
                    .orElse(null);
        }
        try {
            SolonMgppServiceLocator
                .getEvenementService()
                .traiterEvenement(messageDTO.getEtatEvenement(), evenementDTO, session, dossierLink);
            addSuccessToQueueThenReset(context, SolonMgppI18nConstant.EVENEMENT_TRAITER);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(SolonMgppI18nConstant.EVENEMENT_NON_TRAITER));
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
        }
    }

    private static void addSuccessToQueueThenReset(SpecificContext context, String message) {
        context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString(message));
        MgppDossierUIService dossierUIService = MgppUIServiceLocator.getMgppDossierUIService();
        dossierUIService.resetCurrentEvenementDTO(context);
        dossierUIService.resetCurrentMessageDTO(context);
    }

    @Override
    public String getTypeAlerteSuccessive(SpecificContext context) {
        EvenementDTO evenementPrecedent = MgppUIServiceLocator
            .getMgppDossierUIService()
            .getCurrentEvenementDTO(context);
        EvenementTypeDescriptor evtParentTypeDescriptor = SolonMgppServiceLocator
            .getEvenementTypeService()
            .getEvenementType(evenementPrecedent.getTypeEvenementName());
        return ALERTE_FOR_PROCEDURES.get(evtParentTypeDescriptor.getProcedure());
    }

    @Override
    public void publierEvenement(SpecificContext context) {
        Boolean publier = true;
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        context.putInContextData(MgppContextDataKey.EVENT_TYPE, evenementDTO.getTypeEvenementName());

        if (checkMetaDonnee(evenementDTO, publier, context)) {
            checkIdDossier(evenementDTO);

            // sauvegarde de la communication
            SolonMgppServiceLocator.getEvenementService().createEvenement(evenementDTO, publier, context.getSession());

            MgppDossierUIService dossierUIService = MgppUIServiceLocator.getMgppDossierUIService();
            dossierUIService.resetCurrentEvenementDTO(context);
            dossierUIService.resetCurrentMessageDTO(context);
        }
    }

    @Override
    public void saveModifierEvenement(SpecificContext context) {
        Boolean publier = context.getFromContextData(MgppContextDataKey.PUBLIER);
        EvenementDTO evenementDTO = buildEvenementDTO(context, false);

        if (checkMetaDonnee(evenementDTO, publier, context)) {
            checkIdDossier(evenementDTO);

            // sauvegarde de la communication
            try {
                SolonMgppServiceLocator
                    .getEvenementService()
                    .modifierEvenement(evenementDTO, publier, context.getSession());
            } catch (NuxeoException e) {
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        }
    }

    @Override
    public void mettreEnAttente(SpecificContext context) {
        //	Mettre en attente
        SolonMgppServiceLocator
            .getEvenementService()
            .mettreEnAttenteRelancer(
                context.getSession(),
                context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID),
                BooleanUtils.isTrue(context.getFromContextData(MgppContextDataKey.METTRE_EN_ATTENTE))
            );
    }
}
