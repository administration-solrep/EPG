package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum.DOC_COPIES3_LETTRES_TRANSMISSION;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.EVENEMENT_DTO;
import static java.util.Optional.ofNullable;

import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.enums.mgpp.MgppCommunicationMetadonneeEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppWidgetTypeEnum;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.services.mgpp.MgppEvenementDetailsUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.descriptor.parlement.PropertyDescriptor;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.SelectValueGroupDTO;
import fr.dila.st.ui.bean.VersionSelectDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.xsd.solon.epp.Action;
import fr.sword.xsd.solon.epp.EtatVersion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppEvenementDetailsUIServiceImpl implements MgppEvenementDetailsUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppEvenementDetailsUIServiceImpl.class);

    private static final String CURRENT_EVENT = "currentEvent";
    private static final String ACTION_CREER_EVENEMENT = Action.CREER_EVENEMENT.name();
    private static final String ACTION_PASSAGE_MESSAGE_EN_COURS_DE_TRAITEMENT = Action.PASSAGE_MESSAGE_EN_COURS_DE_TRAITEMENT.name();
    private static final String ACTION_MARQUAGE_MESSAGE_TRAITE = Action.MARQUAGE_MESSAGE_TRAITE.name();
    private static final String ACTION_MARQUAGE_MESSAGE_TRAITE_OEP_NON_LIE =
        ACTION_MARQUAGE_MESSAGE_TRAITE + "_OEP_NON_LIE";
    private static final String ACTION_TRANSMETTRE_MEL = Action.TRANSMETTRE_MEL.name();
    private static final String ETAT_ABANDONNE = EtatVersion.ABANDONNE.name();
    private static final String ETAT_BROUILLON = EtatVersion.BROUILLON.name();
    private static final String ETAT_EN_ATTENTE_VALIDATION = EtatVersion.EN_ATTENTE_VALIDATION.name();
    private static final String ETAT_OBSOLETE = EtatVersion.OBSOLETE.name();
    private static final String ETAT_PUBLIE = EtatVersion.PUBLIE.name();
    private static final String ETAT_REJETE = EtatVersion.REJETE.name();
    private static final String GOUVERNEMENT = "GOUVERNEMENT";
    private static final String SUFFIX_SECONDAIRE = "_SECONDAIRE";
    private static final String SUFFIX_TERTIAIRE = "_TERTIAIRE";

    @Override
    public MgppDossierCommunicationConsultationFiche getDetails(SpecificContext context) {
        EvenementDTO evenementDTO = context.getFromContextData(MgppContextDataKey.EVENEMENT_DTO);

        UserSessionHelper.putUserSessionParameter(context, CURRENT_EVENT, evenementDTO);
        return toDossierCommunicationConsultationFiche(evenementDTO);
    }

    private MgppDossierCommunicationConsultationFiche toDossierCommunicationConsultationFiche(
        EvenementDTO evenementDTO
    ) {
        EvenementDTO tempEvenementDTO = new EvenementDTOImpl();
        tempEvenementDTO.putAll(evenementDTO);
        MgppDossierCommunicationConsultationFiche fiche = new MgppDossierCommunicationConsultationFiche();
        extractDepotMap(tempEvenementDTO, EvenementDTOImpl.DEPOT);
        extractDepotMap(tempEvenementDTO, EvenementDTOImpl.DEPOT_RAPPORT);
        extractDepotMap(tempEvenementDTO, EvenementDTOImpl.DEPOT_TEXTE);
        extractCommissionMap(tempEvenementDTO, EvenementDTOImpl.COMMISSION);
        correctCommission(tempEvenementDTO);
        correctDateAr(tempEvenementDTO);

        List<String> metadonneesModifiees = tempEvenementDTO.getMetasModifiees();
        fiche.setLstWidgetsDetails(
            tempEvenementDTO
                .entrySet()
                .stream()
                .filter(MgppCommunicationMetadonneeEnum::getFilterViewableMetadonnee)
                .sorted(
                    (e1, e2) ->
                        MgppCommunicationMetadonneeEnum
                            .getViewableMetadonneeComparator()
                            .compare(e1.getKey(), e2.getKey())
                )
                .map(entry -> displayWidget(entry, tempEvenementDTO.getTypeEvenementName(), metadonneesModifiees))
                .collect(Collectors.toList())
        );

        return fiche;
    }

    private void extractDepotMap(EvenementDTO evenementDTO, String depotKey) {
        Serializable depotValue = evenementDTO.get(depotKey);
        if (depotValue instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Serializable> depotMap = (Map<String, Serializable>) depotValue;
            evenementDTO.put(
                StringUtils.join(EvenementDTOImpl.DATE, StringUtils.capitalize(depotKey)),
                depotMap.get(EvenementDTOImpl.DATE)
            );
            evenementDTO.put(
                StringUtils.join(EvenementDTOImpl.NUMERO, StringUtils.capitalize(depotKey)),
                depotMap.get(EvenementDTOImpl.NUMERO)
            );
            evenementDTO.remove(depotKey);
        }
    }

    private void extractCommissionMap(EvenementDTO evenementDTO, String key) {
        Serializable value = evenementDTO.get(key);
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Serializable> map = (Map<String, Serializable>) value;
            map
                .entrySet()
                .stream()
                .forEach(
                    entry ->
                        evenementDTO.put(
                            StringUtils.join(key, StringUtils.capitalize(entry.getKey())),
                            entry.getValue()
                        )
                );
            evenementDTO.remove(key);
        }
    }

    /**
     * Remplacer "commission" par "commissionSaisie" pour les événements de type EVT04BIS
     * @param evenementDTO
     */
    private void correctCommission(EvenementDTO evenementDTO) {
        if (TypeEvenementConstants.TYPE_EVENEMENT_EVT04BIS.equals(evenementDTO.getTypeEvenementName())) {
            evenementDTO.put(EvenementDTOImpl.COMMISSION_SAISIE, evenementDTO.get(EvenementDTOImpl.COMMISSION));
            evenementDTO.remove(EvenementDTOImpl.COMMISSION);
        }
    }

    @SuppressWarnings("unchecked")
    private void correctDateAr(EvenementDTO evenementDTO) {
        evenementDTO.put(
            EvenementDTOImpl.DATE_AR,
            ((Map<String, Serializable>) evenementDTO.get(EvenementDTOImpl.VERSION_COURANTE)).get(
                    EvenementDTOImpl.DATE_AR
                )
        );
    }

    private WidgetDTO displayWidget(
        Entry<String, Serializable> entry,
        String typeEvenement,
        List<String> metadonneesModifiees
    ) {
        // Property à afficher
        String key = entry.getKey();
        String widgetKey = SolonMgppI18nConstant.REQUIRED_FIELD_MAP.get(key);

        // Type de widget
        MgppCommunicationMetadonneeEnum cme = MgppCommunicationMetadonneeEnum.fromString(key);

        MgppWidgetTypeEnum widgetType = cme.getWidgetType();

        if (
            widgetKey == null &&
            widgetType != MgppWidgetTypeEnum.FILE_MULTI &&
            widgetType != MgppWidgetTypeEnum.PIECE_JOINTE
        ) {
            widgetKey = "label.mgpp.evenement." + key;
        } else if (widgetKey == null) {
            widgetKey =
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(
                        VocabularyConstants.VOCABULARY_PIECE_JOINTE_DIRECTORY,
                        cme == DOC_COPIES3_LETTRES_TRANSMISSION ? "COPIES_3_LETTRES_TRANSMISSION" : cme.getName()
                    );
        }

        Function<Object, WidgetDTO> func = cme.getFactoryFunction();
        if (func == null) {
            func = widgetType.getFactoryFunction();
        }
        WidgetDTO widget = func.apply(entry.getValue());
        widget.setName(key);
        widget.setLabel(widgetKey);

        String name = cme.getParent() != null ? cme.getParent().getName() : cme.getName();
        String nameWS = ofNullable(
                SolonMgppServiceLocator.getMetaDonneesService().getMapProperty(typeEvenement).get(name)
            )
            .map(PropertyDescriptor::getNameWS)
            .orElse("");
        widget.setModifiedInCurVersion(MgppEditWidgetFactory.isModifiedInCurVersion(nameWS, metadonneesModifiees));

        return widget;
    }

    @Override
    public EvenementDTO initFicheDetailCommunication(SpecificContext context) {
        String eventType = context.getFromContextData(MgppContextDataKey.EVENT_TYPE);
        String idMessagePrecedent = context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID);
        EvenementDTO precMessage = UserSessionHelper.getUserSessionParameter(
            context,
            CURRENT_EVENT,
            EvenementDTO.class
        );
        if (StringUtils.isBlank(idMessagePrecedent)) {
            return SolonMgppServiceLocator.getEvenementService().initialiserEvenement(eventType, context.getSession());
        } else if (
            precMessage == null || !idMessagePrecedent.equals(precMessage.getIdEvenement()) || !precMessage.hasData()
        ) {
            throw new NuxeoException(ResourceHelper.getString("mgpp.comsuccessive.creation.error"));
        }

        EvenementDTO evenementDTO = SolonMgppServiceLocator
            .getEvenementService()
            .initialiserEvenementSuccessif(precMessage, eventType, context.getSession());

        FichePresentationOEP fpOEP = SolonMgppServiceLocator
            .getDossierService()
            .findFicheOEP(context.getSession(), precMessage.getIdDossier());
        if (fpOEP != null) {
            evenementDTO.setIdDossier(fpOEP.getIdDossier());
        }
        return evenementDTO;
    }

    @Override
    public List<SelectValueGroupDTO> getCommunicationsSuccessivesList(SpecificContext context) {
        List<SelectValueGroupDTO> communicationsSuccessives = new ArrayList<>();

        EvenementDTO evenementDTO = context.getFromContextData(EVENEMENT_DTO);
        if (CollectionUtils.isNotEmpty(evenementDTO.getEvenementsSuivants())) {
            communicationsSuccessives.add(
                new SelectValueGroupDTO(
                    "Communications conseillées",
                    evenementDTO
                        .getEvenementsSuivants()
                        .stream()
                        .map(evtType -> new SelectValueDTO(evtType, getEvenementTypeLabel(context, evtType)))
                        .sorted(Comparator.comparing(SelectValueDTO::getId))
                        .collect(Collectors.toList())
                )
            );
        }
        communicationsSuccessives.add(
            new SelectValueGroupDTO(
                "Autres communications",
                SolonMgppServiceLocator
                    .getEvenementTypeService()
                    .findEvenementTypeSuccessif(
                        evenementDTO.getTypeEvenementName(),
                        evenementDTO.getEvenementsSuivants()
                    )
                    .stream()
                    .map(evtTypeDesc -> new SelectValueDTO(evtTypeDesc.getName(), evtTypeDesc.getLabel()))
                    .sorted(Comparator.comparing(SelectValueDTO::getId))
                    .collect(Collectors.toList())
            )
        );

        return communicationsSuccessives;
    }

    private static String getEvenementTypeLabel(SpecificContext context, String evenementType) {
        String label;
        try {
            label = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(evenementType).getLabel();
        } catch (NuxeoException e) {
            LOGGER.error(context.getSession(), STLogEnumImpl.FAIL_GET_EVENT_TYPE_TEC, e);
            label = evenementType;
        }
        return label;
    }

    @Override
    public boolean displayCreerCommunicationSuccessive(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        EvenementDTO evenementDTO = context.getFromContextData(EVENEMENT_DTO);
        return SolonMgppServiceLocator
            .getCorbeilleService()
            .findActionPossible(session, ssPrincipal, evenementDTO)
            .contains(ACTION_CREER_EVENEMENT);
    }

    @Override
    public List<String> getActionsSuivantes(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        EvenementDTO evenementDTO = context.getFromContextData(EVENEMENT_DTO);
        List<String> actionsSuivantes = new ArrayList<>(
            SolonMgppServiceLocator.getCorbeilleService().findActionPossible(session, ssPrincipal, evenementDTO)
        );

        String etat = evenementDTO.getVersionCouranteEtat();
        boolean isEmetteur = GOUVERNEMENT.equals(evenementDTO.getEmetteur());
        boolean isDestinataire = GOUVERNEMENT.equals(evenementDTO.getDestinataire());
        boolean isEvenementTypeAlerte = evenementDTO.getTypeEvenementName().toUpperCase().startsWith("ALERTE");
        boolean isBrouillonEmetteur = isEmetteur && ETAT_BROUILLON.equals(etat);
        boolean isObsoleteDestinataire =
            isDestinataire && (ETAT_EN_ATTENTE_VALIDATION.equals(etat) || ETAT_OBSOLETE.equals(etat));
        if (actionsSuivantes.contains(ACTION_TRANSMETTRE_MEL)) {
            if ((!isEvenementTypeAlerte && ETAT_PUBLIE.equals(etat)) || isBrouillonEmetteur || isObsoleteDestinataire) {
                actionsSuivantes.set(
                    actionsSuivantes.indexOf(ACTION_TRANSMETTRE_MEL),
                    ACTION_TRANSMETTRE_MEL + SUFFIX_TERTIAIRE
                );
            } else if (isEmetteur && ETAT_EN_ATTENTE_VALIDATION.equals(etat)) {
                actionsSuivantes.set(
                    actionsSuivantes.indexOf(ACTION_TRANSMETTRE_MEL),
                    ACTION_TRANSMETTRE_MEL + SUFFIX_SECONDAIRE
                );
            }
        }
        if (actionsSuivantes.contains(ACTION_PASSAGE_MESSAGE_EN_COURS_DE_TRAITEMENT) && isDestinataire) {
            actionsSuivantes.set(
                actionsSuivantes.indexOf(ACTION_PASSAGE_MESSAGE_EN_COURS_DE_TRAITEMENT),
                ACTION_PASSAGE_MESSAGE_EN_COURS_DE_TRAITEMENT + SUFFIX_SECONDAIRE
            );
        }
        if (actionsSuivantes.contains(ACTION_MARQUAGE_MESSAGE_TRAITE)) {
            actionsSuivantes.add(ACTION_MARQUAGE_MESSAGE_TRAITE_OEP_NON_LIE);
        }

        return actionsSuivantes;
    }

    @Override
    public boolean peutMettreEnAttente(SpecificContext context) {
        EvenementDTO evenementDTO = context.getFromContextData(EVENEMENT_DTO);
        MgppMessageDTO curMessage = MgppUIServiceLocator.getMgppDossierUIService().getCurrentMessageDTO(context);

        return (
            evenementDTO != null &&
            (
                TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName()) ||
                (
                    TypeEvenementConstants.TYPE_EVENEMENT_EVT04BIS.equals(evenementDTO.getTypeEvenementName()) &&
                    isPropositionLoi(context)
                )
            ) &&
            curMessage != null &&
            !curMessage.isEnAttente()
        );
    }

    private boolean isPropositionLoi(SpecificContext context) {
        String idDossier = context.getFromContextData(STContextDataKey.DOSSIER_ID);
        HistoriqueDossierDTO historiqueDTO = SolonMgppServiceLocator
            .getDossierService()
            .findDossier(idDossier, context.getSession());

        return historiqueDTO
            .getRootEvents()
            .values()
            .stream()
            .anyMatch(e -> TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(e.getTypeEvenementName()));
    }

    @Override
    public String getNatureVersion(SpecificContext context) {
        EvenementDTO evenementDTO = context.getFromContextData(EVENEMENT_DTO);
        StringBuilder label = new StringBuilder();
        List<String> nature = Optional.ofNullable(evenementDTO).map(EvenementDTO::getNature).orElse(null);

        if (CollectionUtils.isNotEmpty(nature)) {
            // La liste de natures peut contenir au maximum 2 éléments
            // et dans ce cas il y aura obligatoirement VERSION_COURANTE
            for (String s : nature) {
                if (!"VERSION_COURANTE".equals(s)) {
                    label.append(ResourceHelper.getString("metadonnees.evenement.nature." + s));
                }
            }
            if (nature.contains("VERSION_COURANTE")) {
                if (label.length() != 0) {
                    label.append(" - ");
                }
                label.append(ResourceHelper.getString("metadonnees.evenement.nature.VERSION_COURANTE"));
            }
        }
        return label.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VersionSelectDTO> getListVersions(SpecificContext context) {
        EvenementDTO evenementDTO = context.getFromContextData(EVENEMENT_DTO);
        List<Map<String, Object>> versionsDisponibles = (List<Map<String, Object>>) evenementDTO.get(
            EvenementDTOImpl.VERSION_DISPONIBLE
        );
        return versionsDisponibles
            .stream()
            .map(MgppEvenementDetailsUIServiceImpl::convertToVersionSelectDTO)
            .collect(Collectors.toList());
    }

    private static VersionSelectDTO convertToVersionSelectDTO(Map<String, Object> version) {
        String text = version.get(EvenementDTOImpl.MAJEUR) + "." + version.get(EvenementDTOImpl.MINEUR);
        String etat = (String) version.get(EvenementDTOImpl.ETAT);
        boolean isRejete = ETAT_REJETE.equals(etat) || ETAT_ABANDONNE.equals(etat);
        Date dateAr = (Date) version.get(EvenementDTOImpl.DATE_AR);
        boolean hasAR = dateAr != null;
        String date = SolonDateConverter.DATE_DASH.format(dateAr);
        return new VersionSelectDTO(text, text, isRejete, hasAR, date);
    }

    @Override
    public boolean peutLierUnOEP(SpecificContext context) {
        CoreSession session = context.getSession();
        MgppMessageDTO curMessage = MgppUIServiceLocator.getMgppDossierUIService().getCurrentMessageDTO(context);
        boolean peutLierUnOEP = false;
        if (curMessage != null && TypeEvenementConstants.isEvenementOEP(curMessage.getTypeEvenement())) {
            try {
                FichePresentationOEP fpOEPSelected = SolonMgppServiceLocator
                    .getDossierService()
                    .findFicheOEP(session, curMessage.getIdDossier());
                // On check dans les dossiers liées
                if (fpOEPSelected == null) {
                    fpOEPSelected =
                        SolonMgppServiceLocator
                            .getDossierService()
                            .findFicheOEPbyIdDossierEPP(session, curMessage.getIdDossier());
                }
                // Une fiche OEP n'a pas pu être créée donc il faut afficher le bouton
                if (fpOEPSelected == null) {
                    return true;
                }
                // Si l'identifiant commun n'est composé que de chiffres, alors on n'affiche pas le bouton
                // Il s'agit probablement d'une communication bien renseignée
                if (fpOEPSelected.getIdCommun().matches("\\d*")) {
                    return false;
                }
                List<String> idDossiersEPPLies = Arrays.asList(fpOEPSelected.getIdsANATLies().split(";"));
                // Quand plusieurs ids sont liés, on est sûr que ce n'est pas une fiche créée automatiquement
                // On affiche le bouton quand il n'y a qu'un seul ou aucun id lié
                if (idDossiersEPPLies.size() <= 1) {
                    peutLierUnOEP = true;
                }
            } catch (NuxeoException e) {
                LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_FICHE_PRESENTATION_TEC, e);
                peutLierUnOEP = true;
            }
        }
        return peutLierUnOEP;
    }

    @Override
    public void lierOEP(SpecificContext context) {
        CoreSession session = context.getSession();
        String organismeOEP = context.getFromContextData(MgppContextDataKey.ORGANISME_OEP);
        try {
            FichePresentationOEP fpOEPSelected = SolonMgppServiceLocator
                .getDossierService()
                .findFicheOEPbyIdDossierEPP(session, organismeOEP);
            if (fpOEPSelected == null) {
                context
                    .getMessageQueue()
                    .addErrorToQueue("Impossible de récupérer la fiche de présentation de l'OEP liée.");
            } else {
                EvenementDTO evenementDTO = MgppUIServiceLocator
                    .getMgppDossierUIService()
                    .getCurrentEvenementDTO(context);
                // Ajout de l'OEP désigné dans la communication
                fpOEPSelected.addToIdsANATLies(evenementDTO.getIdDossier());

                // Suppression de la fiche de présentation de l'ancienne OEP
                FichePresentationOEP fpOEPOld = SolonMgppServiceLocator
                    .getDossierService()
                    .findFicheOEP(session, evenementDTO.getIdDossier());
                if (fpOEPOld != null) {
                    session.removeDocument(fpOEPOld.getDocument().getRef());
                }
                session.saveDocument(fpOEPSelected.getDocument());
                session.save();

                context
                    .getMessageQueue()
                    .addSuccessToQueue("La liaison avec l'organisme extra-parlementaire a été effectuée.");
            }
        } catch (NuxeoException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_ADD_OEP_PROCESS_TEC, e);
            context
                .getMessageQueue()
                .addErrorToQueue("Impossible de lier le dossier EPP à un organisme extra-parlementaire.");
        }
    }
}
