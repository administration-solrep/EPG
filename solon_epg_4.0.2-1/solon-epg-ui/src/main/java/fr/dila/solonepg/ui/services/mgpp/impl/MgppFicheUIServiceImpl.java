package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.DOSSIER_COMMUNICATION_CONSULTATION_FICHE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_ARTICLE_493;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_ASSEMBLEE_DEPOT;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_CHARGE_MISSION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_ADOPTION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_CM;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_DECISION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_DEPART_ELYSEE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_DEPOT;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_JO;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_LIMITE_PROMULGATION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_PROJET;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_RECEPTION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_REFUS_PROC_ASS1;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_RETOUR_ELYSEE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DATE_SAISIE_CC;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DIFFUSION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_DIFFUSION_GENERAL;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_ID_DOSSIER;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_INTITULE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_MIN_RESP;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_NUMERO_DEPOT;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_NUMERO_ISA;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_NUMERO_NOR;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_OBSERVATION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_OPPOSITION_ENG_ASS1;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_OPPOSITION_ENG_ASS2;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_OPPOSITION_ENG_DEC_ASS2;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_PROCEDURE_ACC;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_SECTION_CE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.FICHE_LOI_KEY_TITRE_OFFICIEL;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_DATE_ADOPTION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_DATE_ADOPTION_AN;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_DATE_ADOPTION_SE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_DATE_CMP;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_DATE_NAVETTE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_DATE_TRANSMISSION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_ID_NAVETTE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_NUMERO_TEXT;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_RESULTAT_CMP;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_SORT_ADOPTION;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_SORT_ADOPTION_AN;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_SORT_ADOPTION_SE;
import static fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant.NAVETTE_KEY_URL_TEXT;
import static fr.dila.st.ui.enums.STContextDataKey.DOSSIER_ID;
import static fr.dila.st.ui.enums.WidgetTypeEnum.DATE;
import static fr.dila.st.ui.enums.WidgetTypeEnum.DATE_TIME;
import static fr.dila.st.ui.enums.WidgetTypeEnum.INPUT_TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.INPUT_TEXT_HIDDEN;
import static fr.dila.st.ui.enums.WidgetTypeEnum.MULTIPLE_DATE;
import static fr.dila.st.ui.enums.WidgetTypeEnum.RADIO;
import static fr.dila.st.ui.enums.WidgetTypeEnum.SELECT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.TEXT_AREA;
import static fr.dila.st.ui.enums.WidgetTypeEnum.URL;
import static java.util.Optional.ofNullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.bean.MgppNavetteDTO;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppFichePresentationMetadonneesEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppSelectValueType;
import fr.dila.solonepg.ui.enums.mgpp.MgppSuggestionType;
import fr.dila.solonepg.ui.enums.mgpp.MgppUIConstant;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.services.mgpp.MgppFicheUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppRepresentantForm;
import fr.dila.solonepg.ui.th.bean.MgppRepresentantTableForm;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.domain.FicheLoiImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.domain.RepresentantAUDImpl;
import fr.dila.solonmgpp.core.domain.RepresentantOEPImpl;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.st.api.enums.EtatEvenementEPPEnum;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.enums.AlertType;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class MgppFicheUIServiceImpl implements MgppFicheUIService {
    private static final String LABEL_FICHEDOSSIER_PREFIX = "label.mgpp.fichedossier.";
    private static final String LABEL_NUMERO_NOR = "label.mgpp.fichedossier.numeroNor";
    private static final String LABEL_SORT_ADOPTION = "label.mgpp.fichedossier.sortAdoption";
    private static final String LABEL_DATE_ADOPTION = "label.mgpp.fichedossier.dateAdoption";
    private static final String LABEL_DATE_TRANSMISSION_NAVETTE = "label.mgpp.fichedossier.dateTransmissionNavette";
    private static final String LABEL_NAVETTE_LECTURE = "label.mgpp.fichedossier.navette.lecture";
    private static final String LABEL_REPRESENTANT_DATE_FIN = "label.mgpp.representant.dateFin";
    private static final String LABEL_REPRESENTANT_DATE_DEBUT = "label.mgpp.representant.dateDebut";
    private static final String NAVETTES_SIMPLES = "NAVETTES_SIMPLES";
    private static final String NAVETTES_CMP = "NAVETTES_CMP";
    private static final String NAVETTES_NVLLES_LECT = "NAVETTES_NVLLES_LECT";
    private static final String NAVETTES_DERNIERES_LECT = "NAVETTES_DERNIERES_LECT";
    private static final String NAVETTES_CONGRES = "NAVETTES_CONGRES";

    private static final Map<String, String> FICHE_PRESENTATION_DOC_TYPE_TO_SCHEMA = new ImmutableMap.Builder<String, String>()
        .put(FichePresentationDRImpl.DOC_TYPE, FichePresentationDRImpl.SCHEMA)
        .put(FichePresentationDOC.DOC_TYPE, FichePresentationDOC.SCHEMA)
        .put(FichePresentationAVI.DOC_TYPE, FichePresentationAVI.SCHEMA)
        .put(FichePresentationOEPImpl.DOC_TYPE, FichePresentationOEPImpl.SCHEMA)
        .put(FichePresentationAUD.DOC_TYPE, FichePresentationAUD.SCHEMA)
        .put(FichePresentationDecretImpl.DOC_TYPE, FichePresentationDecretImpl.SCHEMA)
        .put(FichePresentationJSS.DOC_TYPE, FichePresentationJSS.SCHEMA)
        .put(FichePresentationIE.DOC_TYPE, FichePresentationIE.SCHEMA)
        .put(FichePresentationDPG.DOC_TYPE, FichePresentationDPG.SCHEMA)
        .put(FichePresentationSD.DOC_TYPE, FichePresentationSD.SCHEMA)
        .put(FichePresentation341.DOC_TYPE, FichePresentation341.SCHEMA)
        .build();

    public static final String NAME_IDENTIFIANT_PROPOSITION = "identifiantProposition";
    public static final String NAME_DATE_LETTRE_PM = "dateLettrePM";
    private static final String LST_VALUES_PARAMETER_NAME = "lstValues";

    public static final Set<String> TYPES_FICHE_SUPPRIMABLES = ImmutableSet.of(
        FicheLoi.DOC_TYPE,
        FichePresentationAUD.DOC_TYPE
    );

    private static final STLogger LOGGER = STLogFactory.getLog(MgppFicheUIServiceImpl.class);

    @Override
    public boolean isFicheLoiVisible(SpecificContext context) {
        String dossierParlementaire = UserSessionHelper.getUserSessionParameter(
            context,
            MgppUserSessionKey.DOSSIER_PARLEMENTAIRE
        );

        return (
            SolonMgppActionConstant.PROCEDURE_LEGISLATIVE.equals(dossierParlementaire) ||
            SolonMgppActionConstant.PUBLICATION.equals(dossierParlementaire)
        );
    }

    @Override
    public MgppDossierCommunicationConsultationFiche remplirFicheDossier(SpecificContext context) {
        MgppDossierCommunicationConsultationFiche fiche = ObjectHelper.requireNonNullElseGet(
            context.getFromContextData(DOSSIER_COMMUNICATION_CONSULTATION_FICHE),
            MgppDossierCommunicationConsultationFiche::new
        );
        boolean isVerrouille = context.getFromContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE);

        if (isFicheLoiVisible(context)) {
            FicheLoi ficheLoi = getFicheForDossier(context).getAdapter(FicheLoi.class);
            remplirFicheLoi(context, fiche, isVerrouille, ficheLoi);
            fiche.setTypeFiche(FicheLoiImpl.DOC_TYPE);
        } else {
            DocumentModel fichePresentationDoc = getFicheForDossier(context);
            if (fichePresentationDoc != null) {
                remplirFichePresentation(context, fiche, isVerrouille, fichePresentationDoc);
                fiche.setTypeFiche(fichePresentationDoc.getType());
            }
        }
        return fiche;
    }

    private List<String> convertRepresentantAVIToList(DocumentModel representantAVIDoc) {
        RepresentantAVI representantAVI = representantAVIDoc.getAdapter(RepresentantAVI.class);
        return Arrays.asList(
            representantAVI.getNomine(),
            SolonDateConverter.DATE_SLASH.format(representantAVI.getDateDebut()),
            SolonDateConverter.DATE_SLASH.format(representantAVI.getDateFin()),
            SolonDateConverter.DATE_SLASH.format(representantAVI.getDateAuditionAN()),
            SolonDateConverter.DATE_SLASH.format(representantAVI.getDateAuditionSE())
        );
    }

    private List<String> convertRepresentantOEPToList(DocumentModel representantOEPDoc, boolean isUserMgpp) {
        RepresentantOEP representantOEP = representantOEPDoc.getAdapter(RepresentantOEP.class);
        TableReferenceDTO tableRefDto = SolonMgppServiceLocator
            .getTableReferenceService()
            .findTableReferenceByIdAndType(representantOEP.getRepresentant(), "Identite", null);
        String fonction = StringUtils.isNotEmpty(representantOEP.getFonction())
            ? SolonEpgServiceLocator
                .getSolonEpgVocabularyService()
                .getEntryLabel(VocabularyConstants.VOCABULARY_FONCTION_DIRECTORY, representantOEP.getFonction())
            : "";
        List<String> repDataList = new ArrayList<>(
            Arrays.asList(
                ofNullable(tableRefDto).map(TableReferenceDTO::getTitle).orElse(""),
                fonction,
                SolonDateConverter.DATE_SLASH.format(representantOEP.getDateDebut()),
                SolonDateConverter.DATE_SLASH.format(representantOEP.getDateFin())
            )
        );
        if (isUserMgpp) {
            repDataList.addAll(
                Arrays.asList(
                    ofNullable(representantOEP.getNumeroMandat()).map(String::valueOf).orElse(""),
                    representantOEP.getAutoriteDesignation(),
                    representantOEP.getCommissionSaisie()
                )
            );
        }
        return repDataList;
    }

    private List<String> convertRepresentantAUDToList(DocumentModel representantAUDDoc) {
        RepresentantAUD representantAUD = representantAUDDoc.getAdapter(RepresentantAUD.class);
        return Arrays.asList(
            representantAUD.getPersonne(),
            representantAUD.getFonction(),
            SolonDateConverter.DATE_SLASH.format(representantAUD.getDateDebut()),
            SolonDateConverter.DATE_SLASH.format(representantAUD.getDateFin()),
            SolonDateConverter.DATE_SLASH.format(representantAUD.getDateAuditionAN()),
            SolonDateConverter.DATE_SLASH.format(representantAUD.getDateAuditionSE())
        );
    }

    private void remplirFicheLoi(
        SpecificContext context,
        MgppDossierCommunicationConsultationFiche fiche,
        boolean isVerrouille,
        FicheLoi ficheLoi
    ) {
        List<DocumentModel> navettes = SolonMgppServiceLocator
            .getNavetteService()
            .fetchNavette(context.getSession(), ficheLoi.getDocument().getId());
        fiche.setLstWidgetsFiche(getLstWidgetsFiche(context.getSession(), ficheLoi, isVerrouille));
        fiche.setLstWidgetsDepot(getLstWidgetsDepot(ficheLoi, isVerrouille));
        fiche.setLstNavettes(getLstNavettes(navettes, isVerrouille));
        fiche.setLstWidgetsLoiVotee(getLstWidgetsLoiVotee(ficheLoi, isVerrouille));
    }

    private boolean isUserMgpp(SpecificContext context) {
        return context.getSession().getPrincipal().isMemberOf(SolonMgppBaseFunctionConstant.DROIT_VUE_MGPP);
    }

    private void remplirFichePresentation(
        SpecificContext context,
        MgppDossierCommunicationConsultationFiche fiche,
        boolean isVerrouille,
        DocumentModel fichePresentationDoc
    ) {
        boolean isUserMgpp = isUserMgpp(context);
        fiche.setLstWidgetsPresentation(getWidgetsForPresentation(fichePresentationDoc, isVerrouille, isUserMgpp));
        if (FichePresentationAVI.DOC_TYPE.equals(fichePresentationDoc.getType())) {
            fiche.setLstTablesRepresentants(
                Collections.singletonList(getRepresentantAVITable(context, fichePresentationDoc.getId()))
            );
        } else if (FichePresentationOEPImpl.DOC_TYPE.equals(fichePresentationDoc.getType())) {
            fiche.setLstTablesRepresentants(
                Arrays.asList(
                    getRepresentantOEPTables(
                        context,
                        fichePresentationDoc.getId(),
                        VocabularyConstants.REPRESENTANT_TYPE_AN,
                        isUserMgpp
                    ),
                    getRepresentantOEPTables(
                        context,
                        fichePresentationDoc.getId(),
                        VocabularyConstants.REPRESENTANT_TYPE_SE,
                        isUserMgpp
                    )
                )
            );
            FichePresentationOEP fichePresentationOEP = fichePresentationDoc.getAdapter(FichePresentationOEP.class);
            fiche.setFicheDiffusee(fichePresentationOEP.isDiffuse());
            fiche.setIdDossier(fichePresentationOEP.getIdDossier());
        } else if (FichePresentationAUD.DOC_TYPE.equals(fichePresentationDoc.getType())) {
            fiche.setLstTablesRepresentants(
                Collections.singletonList(getRepresentantAUDTable(context, fichePresentationDoc.getId()))
            );
        }
    }

    private MgppRepresentantTableForm getRepresentantAVITable(SpecificContext context, String idFiche) {
        MgppRepresentantTableForm nominesTable = new MgppRepresentantTableForm(
            "nomines",
            RepresentantAVI.DOC_TYPE,
            "label.mgpp.dossier.onglets.fichePresentation.nomines"
        );
        nominesTable.setLstColonnes(
            Arrays.asList(
                "label.mgpp.nomine",
                LABEL_REPRESENTANT_DATE_DEBUT,
                LABEL_REPRESENTANT_DATE_FIN,
                "label.mgpp.representant.dateAuditionAN",
                "label.mgpp.representant.dateAuditionSE"
            )
        );
        nominesTable.setItems(
            SolonMgppServiceLocator
                .getDossierService()
                .fetchNomineAVI(context.getSession(), idFiche)
                .stream()
                .collect(Collectors.toMap(DocumentModel::getId, this::convertRepresentantAVIToList))
        );
        nominesTable.setTableActions(context.getActions(MgppActionCategory.REPRESENTANTS_TABLE_ACTIONS));
        nominesTable.setLineActions(context.getActions(MgppActionCategory.REPRESENTANTS_TABLE_LINE_ACTIONS));
        return nominesTable;
    }

    private MgppRepresentantTableForm getRepresentantOEPTables(
        SpecificContext context,
        String idFiche,
        String typeRepresentant,
        boolean isUserMgpp
    ) {
        MgppRepresentantTableForm representantsTable = new MgppRepresentantTableForm(
            "representants" + typeRepresentant,
            RepresentantOEPImpl.DOC_TYPE + typeRepresentant,
            "label.mgpp.dossier.onglets.fichePresentation.representants" + typeRepresentant
        );
        List<String> lstColonnes = new ArrayList<>(
            Arrays.asList(
                "label.mgpp.representant",
                "label.mgpp.representant.fonction",
                LABEL_REPRESENTANT_DATE_DEBUT,
                LABEL_REPRESENTANT_DATE_FIN
            )
        );
        if (isUserMgpp) {
            lstColonnes.addAll(
                Arrays.asList(
                    "label.mgpp.representant.numeroMandat",
                    "label.mgpp.representant.autoriteDesignation",
                    "label.mgpp.representant.commissionSaisie"
                )
            );
        }
        representantsTable.setLstColonnes(lstColonnes);
        representantsTable.setItems(
            SolonMgppServiceLocator
                .getDossierService()
                .fetchRepresentantOEP(context.getSession(), typeRepresentant, idFiche)
                .stream()
                .collect(
                    Collectors.toMap(DocumentModel::getId, repOEP -> convertRepresentantOEPToList(repOEP, isUserMgpp))
                )
        );
        representantsTable.setTableActions(context.getActions(MgppActionCategory.REPRESENTANTS_TABLE_ACTIONS));
        representantsTable.setLineActions(context.getActions(MgppActionCategory.REPRESENTANTS_TABLE_LINE_ACTIONS));
        return representantsTable;
    }

    private MgppRepresentantTableForm getRepresentantAUDTable(SpecificContext context, String idFiche) {
        MgppRepresentantTableForm personnesTable = new MgppRepresentantTableForm(
            "personnesAud",
            RepresentantAUDImpl.DOC_TYPE,
            "label.mgpp.dossier.onglets.fichePresentation.personnesAud"
        );
        personnesTable.setLstColonnes(
            Arrays.asList(
                "label.mgpp.personneAud",
                "label.mgpp.representant.fonction",
                LABEL_REPRESENTANT_DATE_DEBUT,
                LABEL_REPRESENTANT_DATE_FIN,
                "label.mgpp.representant.dateAuditionAN",
                "label.mgpp.representant.dateAuditionSE"
            )
        );
        personnesTable.setItems(
            SolonMgppServiceLocator
                .getDossierService()
                .fetchPersonneAUD(context.getSession(), idFiche)
                .stream()
                .collect(Collectors.toMap(DocumentModel::getId, this::convertRepresentantAUDToList))
        );
        personnesTable.setTableActions(context.getActions(MgppActionCategory.REPRESENTANTS_TABLE_ACTIONS));
        personnesTable.setLineActions(context.getActions(MgppActionCategory.REPRESENTANTS_TABLE_LINE_ACTIONS));
        return personnesTable;
    }

    @Override
    public MgppDossierCommunicationConsultationFiche getFicheRemplie(SpecificContext context) {
        MgppDossierCommunicationConsultationFiche fiche = new MgppDossierCommunicationConsultationFiche();
        boolean isVerrouille = context.getFromContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE);
        DocumentModel ficheDoc = context.getCurrentDocument();

        fiche.setTypeFiche(ficheDoc.getType());
        if (FicheLoi.DOC_TYPE.equals(ficheDoc.getType())) {
            remplirFicheLoi(context, fiche, isVerrouille, ficheDoc.getAdapter(FicheLoi.class));
        } else {
            remplirFichePresentation(context, fiche, isVerrouille, ficheDoc);
        }

        return fiche;
    }

    @Override
    public DocumentModel getFicheForDossier(SpecificContext context) {
        DocumentModel doc = null;
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        CoreSession session = context.getSession();
        String idDossier = context.getFromContextData(DOSSIER_ID);
        if (isFicheLoiVisible(context)) {
            FicheLoi ficheLoi = dossierService.findOrCreateFicheLoi(session, idDossier);
            doc = ficheLoi.getDocument();
        } else {
            String dossierParlementaire = ObjectUtils.defaultIfNull(
                UserSessionHelper.getUserSessionParameter(context, MgppUserSessionKey.DOSSIER_PARLEMENTAIRE),
                ""
            );
            switch (dossierParlementaire) {
                case SolonMgppActionConstant.DEPOT_DE_RAPPORT:
                    doc = dossierService.findOrCreateFicheDR(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES:
                    doc = dossierService.findOrCreateFicheDOC(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.AVIS_NOMINATION:
                    doc =
                        ofNullable(dossierService.findFicheAVI(session, idDossier))
                            .map(FichePresentationAVI::getDocument)
                            .orElse(null);
                    break;
                case SolonMgppActionConstant.DESIGNATION_OEP:
                    doc = findOrCreateFicheOEP(context, idDossier);
                    break;
                case SolonMgppActionConstant.DEMANDE_AUDITION:
                    doc = dossierService.findOrCreateFicheAUD(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.DECRET:
                    doc = dossierService.findOrCreateFicheDecret(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C:
                    doc = dossierService.findOrCreateFicheJSS(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.INTERVENTION_EXTERIEURE:
                    doc =
                        ofNullable(dossierService.findFicheIE(session, idDossier))
                            .map(FichePresentationIE::getDocument)
                            .orElse(null);
                    break;
                case SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE:
                    doc = dossierService.findOrCreateFicheDPG(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE:
                    doc = dossierService.findOrCreateFicheSD(session, idDossier).getDocument();
                    break;
                case SolonMgppActionConstant.RESOLUTION_ARTICLE_341:
                    doc =
                        ofNullable(dossierService.find341(session, idDossier))
                            .map(FichePresentation341::getDocument)
                            .orElse(null);
                    break;
                default:
                    break;
            }
        }
        return doc;
    }

    public DocumentModel findOrCreateFicheOEP(SpecificContext context, String idDossier) {
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        CoreSession session = context.getSession();
        FichePresentationOEP fichePresentationOEP;
        if (
            TypeEvenementConstants.TYPE_EVENEMENT_EVT49.equals(evenementDTO.getTypeEvenementName()) ||
            TypeEvenementConstants.TYPE_EVENEMENT_EVT49_0.equals(evenementDTO.getTypeEvenementName())
        ) {
            fichePresentationOEP = dossierService.findOrCreateFicheRepresentationOEP(session, evenementDTO);
            session.saveDocument(fichePresentationOEP.getDocument());
            session.save();
        } else {
            fichePresentationOEP = dossierService.findFicheOEP(session, idDossier);
        }
        return ofNullable(fichePresentationOEP).map(FichePresentationOEP::getDocument).orElse(null);
    }

    private static List<WidgetDTO> getLstWidgetsFiche(CoreSession session, FicheLoi ficheLoi, boolean isVerrouille) {
        List<WidgetDTO> widgets = new ArrayList<>();

        widgets.add(
            createWidgetDTOWithLabel(
                FICHE_LOI_KEY_NUMERO_NOR,
                LABEL_NUMERO_NOR,
                isVerrouille ? INPUT_TEXT : TEXT,
                ficheLoi.getNumeroNor()
            )
        );
        widgets.add(createWidgetDTO(FICHE_LOI_KEY_ID_DOSSIER, TEXT, ficheLoi.getIdDossier()));
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectList(
                    FICHE_LOI_KEY_MIN_RESP,
                    SELECT,
                    ficheLoi.getMinistereResp(),
                    MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableMinisteres()
                )
            );
            widgets.add(
                createWidgetDTOSelectList(
                    FICHE_LOI_KEY_CHARGE_MISSION,
                    SELECT,
                    ficheLoi.getNomCompletChargeMission(),
                    MgppUIServiceLocator.getMgppSelectValueUIService().getChargesMission(session)
                )
            );
        } else {
            EntiteNode ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(ficheLoi.getMinistereResp());
            widgets.add(createWidgetDTO(FICHE_LOI_KEY_MIN_RESP, TEXT, ministere != null ? ministere.getLabel() : null));
            widgets.add(createWidgetDTO(FICHE_LOI_KEY_CHARGE_MISSION, TEXT, ficheLoi.getNomCompletChargeMission()));
        }
        widgets.add(createWidgetDTO(FICHE_LOI_KEY_INTITULE, isVerrouille ? INPUT_TEXT : TEXT, ficheLoi.getIntitule()));
        widgets.add(
            createWidgetDTOListValue(
                FICHE_LOI_KEY_DATE_PROJET,
                isVerrouille ? DATE_TIME : TEXT,
                getDateTimeList(ficheLoi.getDateProjet())
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_SECTION_CE,
                TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateSectionCe())
            )
        );
        widgets.add(createWidgetDTO(FICHE_LOI_KEY_NUMERO_ISA, TEXT, ficheLoi.getNumeroISA()));
        widgets.add(
            createWidgetDTO(FICHE_LOI_KEY_DIFFUSION, isVerrouille ? INPUT_TEXT : TEXT, ficheLoi.getDiffusion())
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DIFFUSION_GENERAL,
                isVerrouille ? INPUT_TEXT : TEXT,
                ficheLoi.getDiffusionGenerale()
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_CM,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateCM())
            )
        );
        widgets.add(
            createWidgetDTO(FICHE_LOI_KEY_OBSERVATION, isVerrouille ? TEXT_AREA : TEXT, ficheLoi.getObservation())
        );

        return widgets;
    }

    private static List<String> getDateTimeList(Calendar date) {
        return ofNullable(date)
            .map(SolonDateConverter.DATETIME_SLASH_MINUTE_COLON::format)
            .map(StringUtils::split)
            .map(Arrays::asList)
            .orElseGet(Collections::emptyList);
    }

    private static List<WidgetDTO> getLstWidgetsDepot(FicheLoi ficheLoi, boolean isVerrouille) {
        List<WidgetDTO> widgets = new ArrayList<>();

        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectList(
                    FICHE_LOI_KEY_ASSEMBLEE_DEPOT,
                    SELECT,
                    ficheLoi.getAssembleeDepot(),
                    MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableInstitutions()
                )
            );
        } else {
            widgets.add(
                createWidgetDTO(
                    FICHE_LOI_KEY_ASSEMBLEE_DEPOT,
                    TEXT,
                    StringUtils.isNotBlank(ficheLoi.getAssembleeDepot())
                        ? ResourceHelper.getString(ficheLoi.getAssembleeDepot())
                        : ""
                )
            );
        }
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_DEPOT,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateDepot())
            )
        );
        widgets.add(
            createWidgetDTO(FICHE_LOI_KEY_NUMERO_DEPOT, isVerrouille ? INPUT_TEXT : TEXT, ficheLoi.getNumeroDepot())
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_PROCEDURE_ACC,
                isVerrouille ? DATE : TEXT,
                ofNullable(ficheLoi.getProcedureAcceleree())
                    .map(SolonDateConverter.DATE_SLASH::format)
                    .orElseGet(
                        () -> isVerrouille ? StringUtils.EMPTY : ResourceHelper.getString(MgppUIConstant.LABEL_NO)
                    )
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_ARTICLE_493,
                isVerrouille ? RADIO : TEXT,
                BooleanUtils.toString(
                    ficheLoi.isArticle493(),
                    ResourceHelper.getString(MgppUIConstant.LABEL_YES),
                    ResourceHelper.getString(MgppUIConstant.LABEL_NO),
                    ""
                )
            )
        );
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectList(
                    FICHE_LOI_KEY_OPPOSITION_ENG_ASS1,
                    SELECT,
                    ficheLoi.getRefusEngagementProcAss1(),
                    MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableInstitutions()
                )
            );
        } else if (StringUtils.isNotBlank(ficheLoi.getRefusEngagementProcAss1())) {
            widgets.add(
                createWidgetDTO(
                    FICHE_LOI_KEY_OPPOSITION_ENG_ASS1,
                    TEXT,
                    ResourceHelper.getString(ficheLoi.getRefusEngagementProcAss1())
                )
            );
        }
        if (isVerrouille) {
            widgets.add(
                createWidgetDTO(
                    FICHE_LOI_KEY_DATE_REFUS_PROC_ASS1,
                    TEXT,
                    SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateRefusEngProcAss1())
                )
            );
        } else if (ficheLoi.getDateRefusEngProcAss1() != null) {
            widgets.add(
                createWidgetDTO(
                    FICHE_LOI_KEY_DATE_REFUS_PROC_ASS1,
                    DATE,
                    SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateRefusEngProcAss1())
                )
            );
        }
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectList(
                    FICHE_LOI_KEY_OPPOSITION_ENG_DEC_ASS2,
                    SELECT,
                    ficheLoi.getDecisionEngagementAssemblee2(),
                    MgppUIServiceLocator
                        .getMgppSelectValueUIService()
                        .getSelectValuesFromVocabulary(VocabularyConstants.VOCABULARY_DECISION_PROC_ACC_DIRECTORY)
                )
            );
        } else if (StringUtils.isNotBlank(ficheLoi.getDecisionEngagementAssemblee2())) {
            widgets.add(
                createWidgetDTO(
                    FICHE_LOI_KEY_OPPOSITION_ENG_DEC_ASS2,
                    TEXT,
                    STServiceLocator
                        .getVocabularyService()
                        .getEntryLabel(
                            VocabularyConstants.VOCABULARY_DECISION_PROC_ACC_DIRECTORY,
                            ficheLoi.getDecisionEngagementAssemblee2()
                        )
                )
            );
        }
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectList(
                    FICHE_LOI_KEY_OPPOSITION_ENG_ASS2,
                    SELECT,
                    ficheLoi.getRefusEngagementProcAss2(),
                    MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableInstitutions()
                )
            );
        } else if (StringUtils.isNotBlank(ficheLoi.getRefusEngagementProcAss2())) {
            widgets.add(
                createWidgetDTO(
                    FICHE_LOI_KEY_OPPOSITION_ENG_ASS2,
                    TEXT,
                    ResourceHelper.getString(ficheLoi.getRefusEngagementProcAss2())
                )
            );
        }

        return widgets;
    }

    private static List<WidgetDTO> getLstWidgetsLoiVotee(FicheLoi ficheLoi, boolean isVerrouille) {
        List<WidgetDTO> widgets = new ArrayList<>();

        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_ADOPTION,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateAdoption())
            )
        );
        widgets.add(
            createWidgetDTOListValue(
                FICHE_LOI_KEY_DATE_RECEPTION,
                isVerrouille ? DATE_TIME : TEXT,
                getDateTimeList(ficheLoi.getDateReception())
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_LIMITE_PROMULGATION,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateLimitePromulgation())
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_SAISIE_CC,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateSaisieCC())
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_DECISION,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateDecision())
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_DEPART_ELYSEE,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDepartElysee())
            )
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_RETOUR_ELYSEE,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getRetourElysee())
            )
        );
        widgets.add(
            createWidgetDTO(FICHE_LOI_KEY_TITRE_OFFICIEL, isVerrouille ? INPUT_TEXT : TEXT, ficheLoi.getTitreOfficiel())
        );
        widgets.add(
            createWidgetDTO(
                FICHE_LOI_KEY_DATE_JO,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateJO())
            )
        );

        return widgets;
    }

    private static List<MgppNavetteDTO> getLstNavettes(List<DocumentModel> navettes, boolean isVerrouille) {
        List<MgppNavetteDTO> navetteDTOList = new ArrayList<>();

        navetteDTOList.addAll(getNavettesForType(navettes, NAVETTES_SIMPLES, isVerrouille));
        navetteDTOList.addAll(getNavettesForType(navettes, NAVETTES_CMP, isVerrouille));
        navetteDTOList.addAll(getNavettesForType(navettes, NAVETTES_NVLLES_LECT, isVerrouille));
        navetteDTOList.addAll(getNavettesForType(navettes, NAVETTES_DERNIERES_LECT, isVerrouille));
        navetteDTOList.addAll(getNavettesForType(navettes, NAVETTES_CONGRES, isVerrouille));

        return navetteDTOList;
    }

    private static List<MgppNavetteDTO> getNavettesForType(
        List<DocumentModel> navettes,
        String navetteType,
        boolean isVerrouille
    ) {
        List<String> comparators = new ArrayList<>();
        switch (navetteType) {
            case NAVETTES_SIMPLES:
                comparators.add(NiveauLectureCode.AN.name());
                comparators.add(NiveauLectureCode.SENAT.name());
                break;
            case NAVETTES_CMP:
                comparators.add(NiveauLectureCode.CMP.name());
                break;
            case NAVETTES_NVLLES_LECT:
                comparators.add(NiveauLectureCode.NOUVELLE_LECTURE_AN.name());
                comparators.add(NiveauLectureCode.NOUVELLE_LECTURE_SENAT.name());
                break;
            case NAVETTES_DERNIERES_LECT:
                comparators.add(NiveauLectureCode.LECTURE_DEFINITIVE.name());
                break;
            case NAVETTES_CONGRES:
                comparators.add(NiveauLectureCode.CONGRES.name());
                break;
            default:
                break;
        }

        return navettes
            .stream()
            .map(dm -> dm.getAdapter(Navette.class))
            .filter(Objects::nonNull)
            .filter(navette -> comparators.contains(navette.getCodeLecture()))
            .map(navette -> convertToMgppNavetteDTO(navette, isVerrouille))
            .collect(Collectors.toList());
    }

    private static MgppNavetteDTO convertToMgppNavetteDTO(Navette navette, boolean isVerrouille) {
        MgppNavetteDTO navetteDTO = new MgppNavetteDTO();
        boolean isCMP = NiveauLectureCode.CMP.name().equals(navette.getCodeLecture());

        String idNavette = navette.getDocument().getId();

        StringBuilder navetteName = new StringBuilder();
        if (!isCMP && navette.getNiveauLecture() != null) {
            String niveauLecture = ResourceHelper.getString(LABEL_NAVETTE_LECTURE, navette.getNiveauLecture());
            navetteName.append(niveauLecture);
            navetteName.append(" ");
        }
        navetteName.append(
            STServiceLocator
                .getVocabularyService()
                .getEntryLabel(VocabularyConstants.VOCABULARY_NIVEAU_LECTURE_DIRECTORY, navette.getCodeLecture())
        );
        navetteDTO.setName(navetteName.toString());

        List<WidgetDTO> widgets = new ArrayList<>();

        widgets.add(createWidgetDTO(NAVETTE_KEY_ID_NAVETTE, INPUT_TEXT_HIDDEN, navette.getDocument().getId()));
        widgets.add(
            createWidgetDTOListValue(
                idNavette + "_" + NAVETTE_KEY_DATE_NAVETTE,
                LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_DATE_NAVETTE,
                isVerrouille ? DATE_TIME : TEXT,
                getDateTimeList(navette.getDateNavette())
            )
        );
        if (isCMP) {
            widgets.addAll(getWidgetsForNavetteCMP(navette, isVerrouille, idNavette));
        } else {
            widgets.add(
                createWidgetDTOListValue(
                    idNavette + "_" + NAVETTE_KEY_DATE_TRANSMISSION,
                    LABEL_DATE_TRANSMISSION_NAVETTE,
                    isVerrouille ? DATE_TIME : TEXT,
                    getDateTimeList(navette.getDateTransmission())
                )
            );
            if (isVerrouille) {
                widgets.add(
                    createWidgetDTOSelectListWithLabel(
                        idNavette + "_" + NAVETTE_KEY_SORT_ADOPTION,
                        LABEL_SORT_ADOPTION,
                        SELECT,
                        navette.getSortAdoption(),
                        MgppUIServiceLocator
                            .getMgppSelectValueUIService()
                            .getSelectValuesFromVocabulary(VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY)
                    )
                );
                widgets.add(
                    createWidgetDTOWithLabel(
                        idNavette + "_" + NAVETTE_KEY_DATE_ADOPTION,
                        LABEL_DATE_ADOPTION,
                        DATE,
                        SolonDateConverter.DATE_SLASH.format(navette.getDateAdoption())
                    )
                );
            } else {
                widgets.add(
                    createWidgetDTOWithLabel(
                        idNavette + "_" + NAVETTE_KEY_DATE_ADOPTION,
                        StringUtils.isNotBlank(navette.getSortAdoption())
                            ? LABEL_DATE_ADOPTION + "." + navette.getSortAdoption()
                            : LABEL_DATE_ADOPTION,
                        TEXT,
                        SolonDateConverter.DATE_SLASH.format(navette.getDateAdoption())
                    )
                );
            }
            widgets.add(
                createWidgetDTOWithLabel(
                    idNavette + "_" + NAVETTE_KEY_NUMERO_TEXT,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_NUMERO_TEXT,
                    isVerrouille ? INPUT_TEXT : TEXT,
                    navette.getNumeroTexte()
                )
            );
            if (isVerrouille) {
                widgets.add(
                    createWidgetDTOWithLabel(
                        idNavette + "_" + NAVETTE_KEY_URL_TEXT,
                        LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_URL_TEXT,
                        INPUT_TEXT,
                        navette.getUrl()
                    )
                );
            } else if (StringUtils.isNotBlank(navette.getUrl())) {
                widgets.add(
                    createWidgetDTOWithLabel(
                        idNavette + "_" + NAVETTE_KEY_URL_TEXT,
                        LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_URL_TEXT,
                        URL,
                        navette.getUrl()
                    )
                );
            }
        }

        navetteDTO.setLstWidgets(widgets);

        return navetteDTO;
    }

    private static List<WidgetDTO> getWidgetsForNavetteCMP(Navette navette, boolean isVerrouille, String idNavette) {
        List<WidgetDTO> widgets = new ArrayList<>();
        VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        widgets.add(
            createWidgetDTOListValue(
                idNavette + "_" + NAVETTE_KEY_DATE_CMP,
                LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_DATE_CMP,
                isVerrouille ? MULTIPLE_DATE : TEXT,
                navette
                    .getDateCMP()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(SolonDateConverter.DATE_SLASH::format)
                    .collect(Collectors.toList())
            )
        );
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectListWithLabel(
                    idNavette + "_" + NAVETTE_KEY_RESULTAT_CMP,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_RESULTAT_CMP,
                    SELECT,
                    navette.getResultatCMP(),
                    MgppUIServiceLocator
                        .getMgppSelectValueUIService()
                        .getSelectValuesFromVocabulary(VocabularyConstants.VOCABULARY_RESULTAT_CMP_DIRECTORY)
                )
            );
        } else {
            widgets.add(
                createWidgetDTOWithLabel(
                    idNavette + "_" + NAVETTE_KEY_RESULTAT_CMP,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_RESULTAT_CMP,
                    TEXT,
                    ofNullable(navette.getResultatCMP())
                        .map(
                            str ->
                                vocabularyService.getEntryLabel(
                                    VocabularyConstants.VOCABULARY_RESULTAT_CMP_DIRECTORY,
                                    str
                                )
                        )
                        .orElse("")
                )
            );
        }
        widgets.add(
            createWidgetDTOWithLabel(
                idNavette + "_" + NAVETTE_KEY_DATE_ADOPTION_AN,
                LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_DATE_ADOPTION_AN,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(navette.getDateAdoptionAN())
            )
        );
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectListWithLabel(
                    idNavette + "_" + NAVETTE_KEY_SORT_ADOPTION_AN,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_SORT_ADOPTION_AN,
                    SELECT,
                    navette.getSortAdoptionAN(),
                    MgppUIServiceLocator
                        .getMgppSelectValueUIService()
                        .getSelectValuesFromVocabulary(VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY)
                )
            );
        } else {
            widgets.add(
                createWidgetDTOWithLabel(
                    idNavette + "_" + NAVETTE_KEY_SORT_ADOPTION_AN,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_SORT_ADOPTION_AN,
                    TEXT,
                    ofNullable(navette.getSortAdoptionAN())
                        .map(
                            str ->
                                vocabularyService.getEntryLabel(
                                    VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY,
                                    str
                                )
                        )
                        .orElse("")
                )
            );
        }
        widgets.add(
            createWidgetDTOWithLabel(
                idNavette + "_" + NAVETTE_KEY_DATE_ADOPTION_SE,
                LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_DATE_ADOPTION_SE,
                isVerrouille ? DATE : TEXT,
                SolonDateConverter.DATE_SLASH.format(navette.getDateAdoptionSE())
            )
        );
        if (isVerrouille) {
            widgets.add(
                createWidgetDTOSelectListWithLabel(
                    idNavette + "_" + NAVETTE_KEY_SORT_ADOPTION_SE,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_SORT_ADOPTION_SE,
                    SELECT,
                    navette.getSortAdoptionSE(),
                    MgppUIServiceLocator
                        .getMgppSelectValueUIService()
                        .getSelectValuesFromVocabulary(VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY)
                )
            );
        } else {
            widgets.add(
                createWidgetDTOWithLabel(
                    idNavette + "_" + NAVETTE_KEY_SORT_ADOPTION_SE,
                    LABEL_FICHEDOSSIER_PREFIX + NAVETTE_KEY_SORT_ADOPTION_SE,
                    TEXT,
                    ofNullable(navette.getSortAdoptionSE())
                        .map(
                            str ->
                                vocabularyService.getEntryLabel(
                                    VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY,
                                    str
                                )
                        )
                        .orElse("")
                )
            );
        }
        return widgets;
    }

    private static List<WidgetDTO> getWidgetsForPresentation(
        DocumentModel fichePresentationDoc,
        boolean isVerrouille,
        boolean isUserMgpp
    ) {
        return MgppFichePresentationMetadonneesEnum
            .getMetadonneesForFichePresentation(
                FICHE_PRESENTATION_DOC_TYPE_TO_SCHEMA.get(fichePresentationDoc.getType())
            )
            .stream()
            .filter(meta -> isUserMgpp || !meta.isRestricted())
            .map(meta -> createWidgetForPresentationMetadonnee(fichePresentationDoc, meta, isVerrouille))
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static WidgetDTO createWidgetForPresentationMetadonnee(
        DocumentModel fichePresentationDoc,
        MgppFichePresentationMetadonneesEnum metadonnee,
        boolean isVerrouille
    ) {
        WidgetDTO widget;
        String property = metadonnee.getProperty();
        String name = metadonnee.getName();
        WidgetTypeEnum widgetType = isVerrouille ? metadonnee.getEditWidgetType() : TEXT;
        MgppSelectValueType selectValueType = metadonnee.getSelectValueType();
        MgppSuggestionType suggestionType = metadonnee.getSuggestionType();
        Object value = metadonnee.getValue(fichePresentationDoc);
        String stringValue;
        if (value instanceof Boolean) {
            stringValue =
                isVerrouille
                    ? BooleanUtils.toStringTrueFalse((Boolean) value)
                    : BooleanUtils.toString(
                        (Boolean) value,
                        ResourceHelper.getString(MgppUIConstant.LABEL_YES),
                        ResourceHelper.getString(MgppUIConstant.LABEL_NO),
                        ""
                    );
        } else if (value instanceof Calendar) {
            stringValue = SolonDateConverter.DATE_SLASH.format((Calendar) value);
        } else if (value instanceof Long) {
            stringValue = Long.toString((Long) value);
        } else if (value instanceof List) {
            if (suggestionType == MgppSuggestionType.DEFAULT) {
                stringValue =
                    CollectionUtils.emptyIfNull((List<String>) value).stream().collect(Collectors.joining(", "));
            } else {
                stringValue =
                    MgppEditWidgetFactory
                        .getSuggestList(property, value)
                        .stream()
                        .map(SuggestionDTO::getLabel)
                        .collect(Collectors.joining(", "));
            }
            if (metadonnee == MgppFichePresentationMetadonneesEnum.DR_DESTINATAIRE_1_RAPPORT) {
                value = CollectionUtils.emptyIfNull((List<String>) value).stream().findFirst().orElse(null);
            }
        } else if (suggestionType != MgppSuggestionType.DEFAULT) {
            stringValue =
                MgppEditWidgetFactory
                    .getSuggestList(property, value)
                    .stream()
                    .findFirst()
                    .map(SuggestionDTO::getLabel)
                    .orElse("");
        } else {
            stringValue = (String) value;
        }
        if (selectValueType != MgppSelectValueType.DEFAULT) {
            widget = createWidgetDTOWithoutValue(property, LABEL_FICHEDOSSIER_PREFIX + name, widgetType);
            widget
                .getParametres()
                .addAll(selectValueType.buildParametreSelect((Serializable) value, widgetType.toString()));
        } else if (suggestionType != MgppSuggestionType.DEFAULT) {
            widget = createWidgetDTOWithLabel(property, LABEL_FICHEDOSSIER_PREFIX + name, widgetType, stringValue);
            widget.getParametres().addAll(suggestionType.buildParametres());
            widget
                .getParametres()
                .add(
                    new Parametre(
                        MgppEditWidgetFactory.LST_SUGGEST_NAME,
                        MgppEditWidgetFactory.getSuggestList(property, value)
                    )
                );
        } else {
            widget = createWidgetDTOWithLabel(property, LABEL_FICHEDOSSIER_PREFIX + name, widgetType, stringValue);
            if (widgetType == WidgetTypeEnum.MULTIPLE_INPUT_TEXT && value instanceof List) {
                widget
                    .getParametres()
                    .add(
                        new Parametre(
                            MgppEditWidgetFactory.LST_SUGGEST_NAME,
                            CollectionUtils
                                .emptyIfNull(((List<String>) value))
                                .stream()
                                .map(SelectValueDTO::new)
                                .collect(Collectors.toList())
                        )
                    );
            }
        }
        widget.setRequired(metadonnee.isRequired());
        return widget;
    }

    @Override
    public DocumentModel saveFiche(SpecificContext context) {
        DocumentModel ficheDoc = context.getCurrentDocument();
        boolean isCreation = false;
        String typeFiche;
        if (ficheDoc != null) {
            typeFiche = ficheDoc.getType();
        } else {
            Map<String, Object> ficheMap = context.getFromContextData(MgppContextDataKey.FICHE_METADONNEES_MAP);
            DossierService dossierService = SolonMgppServiceLocator.getDossierService();
            CoreSession session = context.getSession();
            isCreation = true;
            typeFiche = (String) ficheMap.get(MgppTemplateConstants.TYPE_FICHE);
            context.putInContextData(
                MgppContextDataKey.COMMUNICATION_ID,
                ficheMap.getOrDefault(MgppTemplateConstants.ID_COMMUNICATION_A_TRAITER, null)
            );
            switch (typeFiche) {
                case FichePresentationDOC.DOC_TYPE:
                    ficheDoc =
                        dossierService
                            .createFicheDoc(session, (String) ficheMap.get(FichePresentationDOC.ID_DOSSIER))
                            .getDocument();
                    break;
                case FichePresentationAVI.DOC_TYPE:
                    ficheDoc = dossierService.createFicheRepresentationAVI(session, null).getDocument();
                    break;
                case FichePresentationOEPImpl.DOC_TYPE:
                    ficheDoc = dossierService.createFicheRepresentationOEP(session, null, false).getDocument();
                    break;
                case FichePresentationAUD.DOC_TYPE:
                    ficheDoc = dossierService.createFicheRepresentationAUD(session, null).getDocument();
                    break;
                case FichePresentationIE.DOC_TYPE:
                    ficheDoc = dossierService.createEmptyFicheIE(null, session).getDocument();
                    break;
                case FichePresentation341.DOC_TYPE:
                    ficheDoc =
                        dossierService
                            .createEmptyFiche341(
                                MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context),
                                session
                            )
                            .getDocument();
                    break;
                default:
                    break;
            }
        }
        if (FicheLoi.DOC_TYPE.equals(typeFiche)) {
            saveFicheLoi(context);
        } else if (ficheDoc != null) {
            saveFichePresentation(context, ficheDoc, isCreation, typeFiche);
        } else {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("mgpp.fiche.save.not.found"));
        }
        ficheDoc = context.getSession().saveDocument(ficheDoc);
        return ficheDoc;
    }

    private void saveFichePresentation(
        SpecificContext context,
        DocumentModel ficheDoc,
        boolean isCreation,
        String typeFiche
    ) {
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        CoreSession session = context.getSession();
        final DocumentModel fichePresentationDoc = ficheDoc;
        Map<String, Object> ficheMap = context.getFromContextData(MgppContextDataKey.FICHE_METADONNEES_MAP);
        MgppFichePresentationMetadonneesEnum
            .getMetadonneesForFichePresentation(FICHE_PRESENTATION_DOC_TYPE_TO_SCHEMA.get(typeFiche))
            .stream()
            .filter(meta -> meta.getEditWidgetType() != WidgetTypeEnum.TEXT)
            .forEach(meta -> meta.setValue(fichePresentationDoc, ficheMap.getOrDefault(meta.getProperty(), null)));
        switch (typeFiche) {
            case FichePresentationDRImpl.DOC_TYPE:
                dossierService.saveFicheDR(session, fichePresentationDoc);
                break;
            case FichePresentationDOC.DOC_TYPE:
                dossierService.saveFicheDOC(session, fichePresentationDoc);
                break;
            case FichePresentationAVI.DOC_TYPE:
                saveOrCreateFicheAVI(context, fichePresentationDoc, isCreation);
                break;
            case FichePresentationOEPImpl.DOC_TYPE:
                saveOrCreateFicheOEP(context, fichePresentationDoc, isCreation);
                break;
            case FichePresentationAUD.DOC_TYPE:
                saveOrCreateFicheAUD(session, fichePresentationDoc, isCreation);
                break;
            case FichePresentationDecretImpl.DOC_TYPE:
                dossierService.saveFicheDecret(session, fichePresentationDoc);
                break;
            case FichePresentationJSS.DOC_TYPE:
                dossierService.saveFicheJSS(session, fichePresentationDoc);
                break;
            case FichePresentationIE.DOC_TYPE:
                saveOrCreateFicheIE(session, fichePresentationDoc, isCreation);
                break;
            case FichePresentationDPG.DOC_TYPE:
                dossierService.saveFicheDPG(session, fichePresentationDoc);
                break;
            case FichePresentationSD.DOC_TYPE:
                dossierService.saveFicheSD(session, fichePresentationDoc);
                break;
            case FichePresentation341.DOC_TYPE:
                saveOrCreateFiche341(context, fichePresentationDoc, isCreation);
                break;
            default:
                break;
        }
        context
            .getMessageQueue()
            .addMessageToQueue(
                ResourceHelper.getString("message.mgpp.fiche.presentation.save"),
                AlertType.TOAST_SUCCESS
            );
    }

    private void saveOrCreateFicheAVI(SpecificContext context, DocumentModel fichePresentationDoc, boolean isCreation) {
        CoreSession session = context.getSession();
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        if (isCreation) {
            Map<String, Object> ficheMap = context.getFromContextData(MgppContextDataKey.FICHE_METADONNEES_MAP);
            FichePresentationAVI fichePresentationAVI = fichePresentationDoc.getAdapter(FichePresentationAVI.class);
            fichePresentationAVI.setIdDossier(
                (String) ficheMap.getOrDefault(
                    MgppFichePresentationMetadonneesEnum.AVI_ID_DOSSIER.getName(),
                    fichePresentationAVI.getIdDossier()
                )
            );
            dossierService.createFicheRepresentationAVI(session, fichePresentationDoc, Collections.emptyList());
            if (StringUtils.isNotBlank(context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID))) {
                MgppUIServiceLocator.getEvenementActionService().traiterEvenement(context);
            }
        } else {
            dossierService.saveFicheAVI(
                session,
                fichePresentationDoc,
                dossierService.fetchNomineAVI(session, fichePresentationDoc.getId())
            );
        }
    }

    private void saveOrCreateFicheOEP(
        SpecificContext context,
        final DocumentModel fichePresentationDoc,
        boolean isCreation
    ) {
        CoreSession session = context.getSession();
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        if (isCreation) {
            Map<String, Object> ficheMap = context.getFromContextData(MgppContextDataKey.FICHE_METADONNEES_MAP);
            FichePresentationOEP fichePresentationOEP = fichePresentationDoc.getAdapter(FichePresentationOEP.class);
            fichePresentationOEP.setIdCommun(
                (String) ficheMap.getOrDefault(
                    MgppFichePresentationMetadonneesEnum.OEP_ID_COMMUN.getName(),
                    fichePresentationOEP.getIdCommun()
                )
            );
            dossierService.createFicheRepresentationOEP(
                session,
                fichePresentationDoc,
                Collections.emptyList(),
                Collections.emptyList()
            );
            if (StringUtils.isNotBlank(context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID))) {
                MgppUIServiceLocator.getEvenementActionService().traiterEvenement(context);
            }
        } else {
            dossierService.saveFicheOEP(
                session,
                fichePresentationDoc,
                dossierService.fetchRepresentantOEP(
                    session,
                    VocabularyConstants.REPRESENTANT_TYPE_AN,
                    fichePresentationDoc.getId()
                ),
                dossierService.fetchRepresentantOEP(
                    session,
                    VocabularyConstants.REPRESENTANT_TYPE_SE,
                    fichePresentationDoc.getId()
                )
            );
        }
    }

    private void saveOrCreateFicheAUD(
        CoreSession session,
        final DocumentModel fichePresentationDoc,
        boolean isCreation
    ) {
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        if (isCreation) {
            dossierService.createFicheRepresentationAUD(session, fichePresentationDoc, Collections.emptyList());
        } else {
            dossierService.saveFicheAUD(
                session,
                fichePresentationDoc,
                dossierService.fetchPersonneAUD(session, fichePresentationDoc.getId())
            );
        }
    }

    private void saveOrCreateFicheIE(
        CoreSession session,
        final DocumentModel fichePresentationDoc,
        boolean isCreation
    ) {
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        if (isCreation) {
            dossierService.createFicheIE(session, fichePresentationDoc.getAdapter(FichePresentationIE.class));
        } else {
            dossierService.saveFicheIE(session, fichePresentationDoc.getAdapter(FichePresentationIE.class));
        }
    }

    private void saveOrCreateFiche341(SpecificContext context, DocumentModel fichePresentationDoc, boolean isCreation) {
        CoreSession session = context.getSession();
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        if (isCreation) {
            dossierService.createFiche341(session, fichePresentationDoc.getAdapter(FichePresentation341.class));
            if (StringUtils.isNotBlank(context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID))) {
                MgppUIServiceLocator.getEvenementActionService().traiterEvenement(context);
            }
        } else {
            dossierService.saveFiche341(session, fichePresentationDoc.getAdapter(FichePresentation341.class));
        }
    }

    @SuppressWarnings("unchecked")
    private void saveFicheLoi(SpecificContext context) {
        CoreSession session = context.getSession();
        Map<String, Object> ficheLoiMap = context.getFromContextData(MgppContextDataKey.FICHE_METADONNEES_MAP);
        FicheLoi ficheLoi = context.getCurrentDocument().getAdapter(FicheLoi.class);
        // fiche loi
        ficheLoi.setNumeroNor((String) ficheLoiMap.get(FICHE_LOI_KEY_NUMERO_NOR));
        ficheLoi.setMinistereResp((String) ficheLoiMap.get(FICHE_LOI_KEY_MIN_RESP));
        ficheLoi.setNomCompletChargeMission((String) ficheLoiMap.get(FICHE_LOI_KEY_CHARGE_MISSION));
        ficheLoi.setIntitule((String) ficheLoiMap.get(FICHE_LOI_KEY_INTITULE));
        ficheLoi.setDateProjet(checkEmptyAndParseWithHour((List<String>) ficheLoiMap.get(FICHE_LOI_KEY_DATE_PROJET)));
        ficheLoi.setDiffusion((String) ficheLoiMap.get(FICHE_LOI_KEY_DIFFUSION));
        ficheLoi.setDiffusionGenerale((String) ficheLoiMap.get(FICHE_LOI_KEY_DIFFUSION_GENERAL));
        ficheLoi.setDateCM(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_CM)));
        ficheLoi.setObservation((String) ficheLoiMap.get(FICHE_LOI_KEY_OBSERVATION));
        // dépôt
        ficheLoi.setAssembleeDepot((String) ficheLoiMap.get(FICHE_LOI_KEY_ASSEMBLEE_DEPOT));
        ficheLoi.setDateDepot(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_DEPOT)));
        ficheLoi.setNumeroDepot((String) ficheLoiMap.get(FICHE_LOI_KEY_NUMERO_DEPOT));
        ficheLoi.setProcedureAcceleree(
            ResourceHelper.getString(MgppUIConstant.LABEL_NO).equals(ficheLoiMap.get(FICHE_LOI_KEY_PROCEDURE_ACC))
                ? null
                : checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_PROCEDURE_ACC))
        );
        ficheLoi.setArticle493(BooleanUtils.toBoolean((String) ficheLoiMap.get(FICHE_LOI_KEY_ARTICLE_493)));
        ficheLoi.setRefusEngagementProcAss1((String) ficheLoiMap.get(FICHE_LOI_KEY_OPPOSITION_ENG_ASS1));
        ficheLoi.setDateRefusEngProcAss1(
            checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_REFUS_PROC_ASS1))
        );
        ficheLoi.setDecisionEngagementAssemblee2((String) ficheLoiMap.get(FICHE_LOI_KEY_OPPOSITION_ENG_DEC_ASS2));
        ficheLoi.setRefusEngagementProcAss2((String) ficheLoiMap.get(FICHE_LOI_KEY_OPPOSITION_ENG_ASS2));
        // loi voté
        ficheLoi.setDateAdoption(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_ADOPTION)));
        ficheLoi.setDateReception(
            checkEmptyAndParseWithHour((List<String>) ficheLoiMap.get(FICHE_LOI_KEY_DATE_RECEPTION))
        );
        ficheLoi.setDateLimitePromulgation(
            checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_LIMITE_PROMULGATION))
        );
        ficheLoi.setDateSaisieCC(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_SAISIE_CC)));
        ficheLoi.setDateDecision(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_DECISION)));
        ficheLoi.setDepartElysee(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_DEPART_ELYSEE)));
        ficheLoi.setRetourElysee(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_RETOUR_ELYSEE)));
        ficheLoi.setTitreOfficiel((String) ficheLoiMap.get(FICHE_LOI_KEY_TITRE_OFFICIEL));
        ficheLoi.setDateJO(checkEmptyAndParse((String) ficheLoiMap.get(FICHE_LOI_KEY_DATE_JO)));
        // navette
        List<String> idNavettes;
        try {
            idNavettes = (List<String>) ficheLoiMap.get(NAVETTE_KEY_ID_NAVETTE);
        } catch (ClassCastException e) {
            LOGGER.info(STLogEnumImpl.DEFAULT, e);
            idNavettes = Collections.singletonList((String) ficheLoiMap.get(NAVETTE_KEY_ID_NAVETTE));
        }
        saveNavettes(session, ficheLoiMap, idNavettes);
        SolonMgppServiceLocator.getDossierService().saveFicheLoi(session, ficheLoi.getDocument());
        context
            .getMessageQueue()
            .addMessageToQueue(ResourceHelper.getString("message.mgpp.fiche.loi.save"), AlertType.TOAST_SUCCESS);
    }

    private void saveNavettes(CoreSession session, Map<String, Object> ficheLoiMap, List<String> idNavettes) {
        CollectionUtils
            .emptyIfNull(idNavettes)
            .forEach(
                idNavette -> {
                    String prefix = idNavette + "_";
                    DocumentModel navetteDoc = session.getDocument(new IdRef(idNavette));
                    Navette navette = navetteDoc.getAdapter(Navette.class);
                    boolean isCMP = NiveauLectureCode.CMP.name().equals(navette.getCodeLecture());

                    navette.setDateNavette(
                        checkEmptyAndParseWithHour((List<String>) ficheLoiMap.get(prefix + NAVETTE_KEY_DATE_NAVETTE))
                    );
                    if (isCMP) {
                        saveNavetteCmpData(ficheLoiMap, prefix, navette);
                    } else {
                        saveNavetteData(ficheLoiMap, prefix, navette);
                    }
                    session.saveDocument(navetteDoc);
                }
            );
    }

    private void saveNavetteData(Map<String, Object> ficheLoiMap, String prefix, Navette navette) {
        navette.setDateTransmission(
            checkEmptyAndParseWithHour((List<String>) ficheLoiMap.get(prefix + NAVETTE_KEY_DATE_TRANSMISSION))
        );
        navette.setSortAdoption((String) ficheLoiMap.get(prefix + NAVETTE_KEY_SORT_ADOPTION));
        navette.setDateAdoption(checkEmptyAndParse((String) ficheLoiMap.get(prefix + NAVETTE_KEY_DATE_ADOPTION)));
        navette.setNumeroTexte((String) ficheLoiMap.get(prefix + NAVETTE_KEY_NUMERO_TEXT));
        navette.setUrl((String) ficheLoiMap.get(prefix + NAVETTE_KEY_URL_TEXT));
    }

    private void saveNavetteCmpData(Map<String, Object> ficheLoiMap, String prefix, Navette navette) {
        @SuppressWarnings("unchecked")
        List<String> dateCMP = (ArrayList<String>) ficheLoiMap.get(prefix + NAVETTE_KEY_DATE_CMP);
        navette.setDateCMP(
            dateCMP != null
                ? dateCMP.stream().map(MgppFicheUIServiceImpl::checkEmptyAndParse).collect(Collectors.toList())
                : new ArrayList<>()
        );
        navette.setResultatCMP((String) ficheLoiMap.get(prefix + NAVETTE_KEY_RESULTAT_CMP));
        navette.setDateAdoptionAN(checkEmptyAndParse((String) ficheLoiMap.get(prefix + NAVETTE_KEY_DATE_ADOPTION_AN)));
        navette.setSortAdoptionAN((String) ficheLoiMap.get(prefix + NAVETTE_KEY_SORT_ADOPTION_AN));
        navette.setDateAdoptionSE(checkEmptyAndParse((String) ficheLoiMap.get(prefix + NAVETTE_KEY_DATE_ADOPTION_SE)));
        navette.setSortAdoptionSE((String) ficheLoiMap.get(prefix + NAVETTE_KEY_SORT_ADOPTION_SE));
    }

    private static Calendar checkEmptyAndParse(String dateToParse) {
        return StringUtils.isNotEmpty(dateToParse) ? SolonDateConverter.DATE_SLASH.parseToCalendar(dateToParse) : null;
    }

    private static Calendar checkEmptyAndParseWithHour(List<String> dateToParse) {
        return CollectionUtils.size(dateToParse) == 2
            ? SolonDateConverter.DATETIME_SLASH_MINUTE_COLON.parseToCalendar(
                dateToParse.stream().collect(Collectors.joining(" "))
            )
            : null;
    }

    private static WidgetDTO createWidgetDTO(String name, WidgetTypeEnum typeChamp, String value) {
        return createWidgetDTOWithLabel(name, LABEL_FICHEDOSSIER_PREFIX + name, typeChamp, value);
    }

    private static WidgetDTO createWidgetDTOSelectList(
        String name,
        WidgetTypeEnum typeChamp,
        String value,
        List<SelectValueDTO> selectContent
    ) {
        WidgetDTO widget = createWidgetDTOWithLabel(name, LABEL_FICHEDOSSIER_PREFIX + name, typeChamp, value);
        widget.getParametres().add(new Parametre(LST_VALUES_PARAMETER_NAME, selectContent));
        return widget;
    }

    private static WidgetDTO createWidgetDTOWithLabel(
        String name,
        String label,
        WidgetTypeEnum typeChamp,
        String value
    ) {
        WidgetDTO widget = createWidgetDTOWithoutValue(name, label, typeChamp);
        widget.getParametres().add(new Parametre("valeur", ofNullable(value).orElse("")));
        return widget;
    }

    private static WidgetDTO createWidgetDTOWithoutValue(String name, String label, WidgetTypeEnum typeChamp) {
        return new WidgetDTO(name, label, typeChamp.toString());
    }

    private static WidgetDTO createWidgetDTOSelectListWithLabel(
        String name,
        String label,
        WidgetTypeEnum typeChamp,
        String value,
        List<SelectValueDTO> selectContent
    ) {
        WidgetDTO widget = createWidgetDTOWithLabel(name, label, typeChamp, value);
        widget.getParametres().add(new Parametre(LST_VALUES_PARAMETER_NAME, selectContent));
        return widget;
    }

    private static WidgetDTO createWidgetDTOListValue(
        String name,
        String label,
        WidgetTypeEnum typeChamp,
        List<String> selectContent
    ) {
        String value = TEXT.equals(typeChamp) ? StringUtils.join(selectContent, " ") : null;
        WidgetDTO widget = createWidgetDTOWithLabel(name, label, typeChamp, value);
        widget.getParametres().add(new Parametre(LST_VALUES_PARAMETER_NAME, selectContent));
        return widget;
    }

    private static WidgetDTO createWidgetDTOListValue(
        String name,
        WidgetTypeEnum typeChamp,
        List<String> selectContent
    ) {
        return createWidgetDTOListValue(name, LABEL_FICHEDOSSIER_PREFIX + name, typeChamp, selectContent);
    }

    @Override
    public String getIdDossierFromFiche(SpecificContext context) {
        DocumentModel currentDoc = context.getCurrentDocument();
        String idDossier = null;
        if (currentDoc.hasSchema(FichePresentationIE.SCHEMA)) {
            idDossier = currentDoc.getAdapter(FichePresentationIE.class).getIdentifiantProposition();
        } else if (currentDoc.hasSchema(FichePresentationDecretImpl.SCHEMA)) {
            idDossier = currentDoc.getAdapter(FichePresentationDecret.class).getNor();
        } else if (currentDoc.hasSchema(FichePresentationAVI.SCHEMA)) {
            idDossier = currentDoc.getAdapter(FichePresentationAVI.class).getIdDossier();
        } else if (currentDoc.hasSchema(FichePresentation341.SCHEMA)) {
            idDossier = currentDoc.getAdapter(FichePresentation341.class).getIdentifiantProposition();
        } else if (currentDoc.hasSchema(FicheLoi.SCHEMA)) {
            idDossier = currentDoc.getAdapter(FicheLoi.class).getIdDossier();
        } else if (currentDoc.hasSchema(FichePresentationDRImpl.SCHEMA)) {
            idDossier = currentDoc.getAdapter(FichePresentationDR.class).getIdDossier();
        } else if (currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            Dossier dossier = currentDoc.getAdapter(Dossier.class);
            idDossier = dossier.getIdDossier();
            if (idDossier == null) {
                idDossier = dossier.getNumeroNor();
            }
        }
        return idDossier;
    }

    @Override
    public MgppRepresentantTableForm getRepresentantTable(SpecificContext context) {
        String idFiche = context.getCurrentDocument().getId();
        String typeRepresentant = context.getFromContextData(MgppContextDataKey.TYPE_REPRESENTANT);
        MgppRepresentantTableForm representantTable = null;
        if (RepresentantAVI.DOC_TYPE.equals(typeRepresentant)) {
            representantTable = getRepresentantAVITable(context, idFiche);
        } else if (typeRepresentant.startsWith(RepresentantOEPImpl.DOC_TYPE)) {
            representantTable =
                getRepresentantOEPTables(
                    context,
                    idFiche,
                    StringUtils.remove(typeRepresentant, RepresentantOEPImpl.DOC_TYPE),
                    isUserMgpp(context)
                );
        } else if (RepresentantAUDImpl.DOC_TYPE.equals(typeRepresentant)) {
            representantTable = getRepresentantAUDTable(context, idFiche);
        }
        return representantTable;
    }

    @Override
    public String saveRepresentant(SpecificContext context) {
        MgppRepresentantForm representantForm = context.getFromContextData(MgppContextDataKey.REPRESENTANT_FORM);
        String typeRepresentant;
        switch (representantForm.getTypeRepresentant()) {
            case RepresentantAVI.DOC_TYPE:
                saveRepresentantAVI(context, representantForm);
                typeRepresentant = representantForm.getTypeRepresentant();
                break;
            case RepresentantOEPImpl.DOC_TYPE + VocabularyConstants.REPRESENTANT_TYPE_AN:
                saveRepresentantOEP(context, representantForm, VocabularyConstants.REPRESENTANT_TYPE_AN);
                typeRepresentant = representantForm.getTypeRepresentant();
                break;
            case RepresentantOEPImpl.DOC_TYPE + VocabularyConstants.REPRESENTANT_TYPE_SE:
                saveRepresentantOEP(context, representantForm, VocabularyConstants.REPRESENTANT_TYPE_SE);
                typeRepresentant = representantForm.getTypeRepresentant();
                break;
            case RepresentantAUDImpl.DOC_TYPE:
                saveRepresentantAUD(context, representantForm);
                typeRepresentant = representantForm.getTypeRepresentant();
                break;
            default:
                typeRepresentant = "";
                break;
        }
        return typeRepresentant;
    }

    private void saveRepresentantAVI(SpecificContext context, MgppRepresentantForm representantForm) {
        CoreSession session = context.getSession();
        DocumentModel ficheDoc = context.getCurrentDocument();
        DocumentModel representantDoc;
        RepresentantAVI representantAVI;
        if (StringUtils.isEmpty(representantForm.getIdRepresentant())) {
            representantDoc =
                session.createDocumentModel(
                    ficheDoc.getPathAsString(),
                    "REP-" + UUID.randomUUID().toString() + "-AN",
                    RepresentantAVI.DOC_TYPE
                );
            representantAVI = representantDoc.getAdapter(RepresentantAVI.class);
            representantAVI.setIdFicheRepresentationAVI(ficheDoc.getId());
            session.createDocument(representantDoc);
        } else {
            representantDoc = session.getDocument(new IdRef(representantForm.getIdRepresentant()));
            representantAVI = representantDoc.getAdapter(RepresentantAVI.class);
        }
        representantAVI.setNomine(representantForm.getNomine());
        representantAVI.setDateDebut(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateDebut())
        );
        representantAVI.setDateFin(SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateFin()));
        representantAVI.setDateAuditionAN(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateAuditionAN())
        );
        representantAVI.setDateAuditionSE(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateAuditionSE())
        );
        session.saveDocument(representantDoc);
        session.save();
    }

    private void saveRepresentantAUD(SpecificContext context, MgppRepresentantForm representantForm) {
        CoreSession session = context.getSession();
        DocumentModel ficheDoc = context.getCurrentDocument();
        DocumentModel representantDoc;
        RepresentantAUD representantAUD;
        if (StringUtils.isEmpty(representantForm.getIdRepresentant())) {
            representantDoc =
                session.createDocumentModel(
                    ficheDoc.getPathAsString(),
                    "REP-" + UUID.randomUUID().toString() + "-AUD",
                    RepresentantAUDImpl.DOC_TYPE
                );
            representantAUD = representantDoc.getAdapter(RepresentantAUD.class);
            representantAUD.setIdFicheRepresentationAUD(ficheDoc.getId());
            session.createDocument(representantDoc);
        } else {
            representantDoc = session.getDocument(new IdRef(representantForm.getIdRepresentant()));
            representantAUD = representantDoc.getAdapter(RepresentantAUD.class);
        }
        representantAUD.setPersonne(representantForm.getPersonne());
        representantAUD.setFonction(representantForm.getFonction());
        representantAUD.setDateDebut(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateDebut())
        );
        representantAUD.setDateFin(SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateFin()));
        representantAUD.setDateAuditionAN(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateAuditionAN())
        );
        representantAUD.setDateAuditionSE(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateAuditionSE())
        );
        session.saveDocument(representantDoc);
        session.save();
    }

    private void saveRepresentantOEP(
        SpecificContext context,
        MgppRepresentantForm representantForm,
        String typeRepresentant
    ) {
        CoreSession session = context.getSession();
        DocumentModel ficheDoc = context.getCurrentDocument();
        DocumentModel representantDoc;
        RepresentantOEP representantOEP;
        if (StringUtils.isEmpty(representantForm.getIdRepresentant())) {
            representantDoc =
                session.createDocumentModel(
                    ficheDoc.getPathAsString(),
                    "REP-" + UUID.randomUUID().toString() + "-" + typeRepresentant,
                    RepresentantOEPImpl.DOC_TYPE
                );
            representantOEP = representantDoc.getAdapter(RepresentantOEP.class);
            representantOEP.setIdFicheRepresentationOEP(ficheDoc.getId());
            representantOEP.setType(typeRepresentant);
            session.createDocument(representantDoc);
        } else {
            representantDoc = session.getDocument(new IdRef(representantForm.getIdRepresentant()));
            representantOEP = representantDoc.getAdapter(RepresentantOEP.class);
        }
        representantOEP.setRepresentant(representantForm.getRepresentant());
        representantOEP.setFonction(representantForm.getFonction());
        representantOEP.setDateDebut(
            SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateDebut())
        );
        representantOEP.setDateFin(SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(representantForm.getDateFin()));
        if (StringUtils.isNotBlank(representantForm.getNumeroMandat())) {
            representantOEP.setNumeroMandat(Long.parseLong(representantForm.getNumeroMandat()));
        }
        representantOEP.setAutoriteDesignation(representantForm.getAutoriteDesignation());
        representantOEP.setCommissionSaisie(representantForm.getCommissionSaisie());
        session.saveDocument(representantDoc);
        session.save();
    }

    @Override
    public boolean isFicheSupprimable(SpecificContext context) {
        MgppDossierCommunicationConsultationFiche fiche = context.getFromContextData(
            DOSSIER_COMMUNICATION_CONSULTATION_FICHE
        );
        // On ne peut supprimer que des fiches loi et des fiches d'audition
        if (!TYPES_FICHE_SUPPRIMABLES.contains(fiche.getTypeFiche())) {
            return false;
        }

        // Recherche des messages liés à partir de l'idDossier, aucun ne doit être publié
        String idDossier = context.getFromContextData(DOSSIER_ID);
        if (StringUtils.isEmpty(idDossier)) {
            DocumentModel currentDoc = context.getCurrentDocument();
            if (currentDoc != null && FicheLoi.DOC_TYPE.equals(currentDoc.getType())) {
                idDossier = currentDoc.getAdapter(FicheLoi.class).getIdDossier();
            } else if (currentDoc != null && FichePresentationAUD.DOC_TYPE.equals(currentDoc.getType())) {
                idDossier = currentDoc.getAdapter(FichePresentationAUD.class).getIdDossier();
            }
        }
        if (StringUtils.isEmpty(idDossier)) {
            return false;
        }

        CritereRechercheDTO critereRecherche = new CritereRechercheDTOImpl();
        critereRecherche.setIdDossier(idDossier);
        return SolonMgppServiceLocator
            .getRechercheService()
            .findMessage(critereRecherche, context.getSession())
            .stream()
            .map(MessageDTO::getEtatEvenement)
            .noneMatch(etat -> !EtatEvenementEPPEnum.BROUILLON.name().equals(etat));
    }

    @Override
    public void diffuserFiche(SpecificContext context) {
        try {
            SolonMgppServiceLocator
                .getDossierService()
                .diffuserFicheOEP(context.getSession(), context.getCurrentDocument());
            SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(context.getSession());
            context
                .getMessageQueue()
                .addMessageToQueue(ResourceHelper.getString("message.mgpp.ficheOEP.diffuser"), AlertType.TOAST_SUCCESS);
        } catch (Exception e) {
            LOGGER.error(SSLogEnumImpl.FAIL_PUBLISH_BIRT_TEC, e);
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("message.mgpp.ficheOEP.diffuser.error"));
        }
    }

    @Override
    public void annulerDiffusionFiche(SpecificContext context) {
        CoreSession session = context.getSession();
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        dossierService.annulerDiffusionFicheOEP(session, Arrays.asList(context.getCurrentDocument()));
        dossierService.updateListeOEPPubliee(session);
        context
            .getMessageQueue()
            .addMessageToQueue(
                ResourceHelper.getString("message.mgpp.ficheOEP.annuler.diffusion"),
                AlertType.TOAST_SUCCESS
            );
    }
}
