package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.GERER_FICHE_PRESENTATION;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.VERSION_ID;
import static fr.dila.st.ui.enums.STContextDataKey.DOSSIER_ID;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.export.MgppExportConfig;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.MgppExportable;
import fr.dila.solonepg.ui.bean.MgppFichePresentationOEPDTO;
import fr.dila.solonepg.ui.bean.MgppFichePresentationOEPList;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.contentview.MgppFichePresentationOEPDiffusePageProvider;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.helper.mgpp.MgppMessageListProviderHelper;
import fr.dila.solonepg.ui.services.mgpp.MgppDossierUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppFicheUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppFichePresentationOEPForm;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
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
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.core.util.ExportUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class MgppDossierUIServiceImpl implements MgppDossierUIService {
    public static final int EXPORT_EXCEL_HEADER_ROW_SPAN = 1;

    @Override
    public EvenementDTO getFreshCurrentEvenementDTO(SpecificContext context) {
        String evenementId = context.getFromContextData(COMMUNICATION_ID);
        String version = context.getFromContextData(VERSION_ID);
        boolean gererFichePresentation = BooleanUtils.isTrue(context.getFromContextData(GERER_FICHE_PRESENTATION));

        EvenementDTO evenementDTO = SolonMgppServiceLocator
            .getEvenementService()
            .findEvenement(evenementId, version, context.getSession(), gererFichePresentation);
        UserSessionHelper.putUserSessionParameter(context, MgppUserSessionKey.EVENEMENT_DTO, evenementDTO);

        return evenementDTO;
    }

    @Override
    public EvenementDTO getCurrentEvenementDTO(SpecificContext context) {
        String evenementId = context.getFromContextData(COMMUNICATION_ID);
        String version = context.getFromContextData(VERSION_ID);

        EvenementDTO evenementDTO = UserSessionHelper.getUserSessionParameter(
            context,
            MgppUserSessionKey.EVENEMENT_DTO
        );
        if (
            evenementDTO == null ||
            !StringUtils.equals(evenementId, evenementDTO.getIdEvenement()) ||
            (StringUtils.isNotBlank(version) && !version.equals(evenementDTO.getVersionCourante()))
        ) {
            return getFreshCurrentEvenementDTO(context);
        }
        return evenementDTO;
    }

    @Override
    public MgppMessageDTO getCurrentMessageDTO(SpecificContext context) {
        String evenementId = context.getFromContextData(COMMUNICATION_ID);
        boolean forceRefresh = BooleanUtils.isTrue(context.getFromContextData(MgppContextDataKey.FORCE_REFRESH));

        MgppMessageDTO messageDTO = UserSessionHelper.getUserSessionParameter(context, MgppUserSessionKey.MESSAGE_DTO);
        if (messageDTO == null || !StringUtils.equals(evenementId, messageDTO.getId()) || forceRefresh) {
            CritereRechercheDTO critereRecherche = new CritereRechercheDTOImpl();
            critereRecherche.setIdEvenement(evenementId);

            Map<String, String> mapNiveauLecture = STServiceLocator
                .getVocabularyService()
                .getAllEntries(VocabularyConstants.VOCABULARY_NIVEAU_LECTURE_DIRECTORY);
            messageDTO =
                SolonMgppServiceLocator
                    .getRechercheService()
                    .findMessage(critereRecherche, context.getSession())
                    .stream()
                    .findFirst()
                    .map(message -> MgppMessageListProviderHelper.convertToMessageDTO(message, mapNiveauLecture))
                    .orElse(null);
            UserSessionHelper.putUserSessionParameter(context, MgppUserSessionKey.MESSAGE_DTO, messageDTO);
            context.putInContextData(MgppContextDataKey.FORCE_REFRESH, false);
        }
        return messageDTO;
    }

    @Override
    public Dossier getCurrentDossier(SpecificContext context) {
        return getCurrentDossier(context, null);
    }

    @Override
    public Dossier getCurrentDossier(SpecificContext context, DocumentModel currentFicheDoc) {
        String idDossier = context.getFromContextData(DOSSIER_ID);
        CoreSession session = context.getSession();
        MgppFicheUIService mgppFicheUIService = MgppUIServiceLocator.getMgppFicheUIService();
        fr.dila.solonepg.api.service.DossierService dossierService = SolonEpgServiceLocator.getDossierService();

        Dossier dossier = dossierService.findDossierFromIdDossier(session, idDossier);

        if (dossier == null && currentFicheDoc != null) {
            if (mgppFicheUIService.isFicheLoiVisible(context)) {
                FicheLoi ficheLoi = currentFicheDoc.getAdapter(FicheLoi.class);
                dossier = dossierService.findDossierFromNumeroNOR(session, ficheLoi.getNumeroNor());
                if (dossier == null) {
                    dossier = dossierService.findDossierFromNumeroNOR(session, idDossier);
                }
            } else {
                String dossierParlementaire = ObjectUtils.defaultIfNull(
                    UserSessionHelper.getUserSessionParameter(context, MgppUserSessionKey.DOSSIER_PARLEMENTAIRE),
                    ""
                );

                switch (dossierParlementaire) {
                    case SolonMgppActionConstant.DEPOT_DE_RAPPORT:
                        FichePresentationDR fichePresentationDR = currentFicheDoc.getAdapter(FichePresentationDR.class);
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationDR.getIdDossier());
                        break;
                    case SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES:
                        FichePresentationDOC fichePresentationDOC = currentFicheDoc.getAdapter(
                            FichePresentationDOC.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationDOC.getIdDossier());
                        break;
                    case SolonMgppActionConstant.AVIS_NOMINATION:
                        FichePresentationAVI fichePresentationAVI = currentFicheDoc.getAdapter(
                            FichePresentationAVI.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationAVI.getIdDossier());
                        break;
                    case SolonMgppActionConstant.DESIGNATION_OEP:
                        FichePresentationOEP fichePresentationOEP = currentFicheDoc.getAdapter(
                            FichePresentationOEP.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationOEP.getIdDossier());
                        break;
                    case SolonMgppActionConstant.DEMANDE_AUDITION:
                        FichePresentationAUD fichePresentationAUD = currentFicheDoc.getAdapter(
                            FichePresentationAUD.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationAUD.getIdDossier());
                        break;
                    case SolonMgppActionConstant.DECRET:
                        FichePresentationDecret fichePresentationDecret = currentFicheDoc.getAdapter(
                            FichePresentationDecret.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationDecret.getNor());
                        break;
                    case SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C:
                        FichePresentationJSS fichePresentationJSS = currentFicheDoc.getAdapter(
                            FichePresentationJSS.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationJSS.getIdDossier());
                        break;
                    case SolonMgppActionConstant.INTERVENTION_EXTERIEURE:
                        FichePresentationIE fichePresentationIE = currentFicheDoc.getAdapter(FichePresentationIE.class);
                        dossier =
                            dossierService.findDossierFromNumeroNOR(
                                session,
                                fichePresentationIE.getIdentifiantProposition()
                            );
                        break;
                    case SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE:
                        FichePresentationDPG fichePresentationDPG = currentFicheDoc.getAdapter(
                            FichePresentationDPG.class
                        );
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationDPG.getIdDossier());
                        break;
                    case SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE:
                        FichePresentationSD fichePresentationSD = currentFicheDoc.getAdapter(FichePresentationSD.class);
                        dossier = dossierService.findDossierFromNumeroNOR(session, fichePresentationSD.getIdDossier());
                        break;
                    case SolonMgppActionConstant.RESOLUTION_ARTICLE_341:
                        FichePresentation341 fichePresentation341 = currentFicheDoc.getAdapter(
                            FichePresentation341.class
                        );
                        dossier =
                            dossierService.findDossierFromNumeroNOR(
                                session,
                                fichePresentation341.getIdentifiantProposition()
                            );
                        break;
                    default:
                }
            }
        }
        return dossier;
    }

    @Override
    public void resetCurrentEvenementDTO(SpecificContext context) {
        UserSessionHelper.clearUserSessionParameter(context, MgppUserSessionKey.EVENEMENT_DTO);
    }

    @Override
    public void resetCurrentMessageDTO(SpecificContext context) {
        UserSessionHelper.clearUserSessionParameter(context, MgppUserSessionKey.MESSAGE_DTO);
    }

    @Override
    public List<String> getCurrentStepLabels(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> etapesEnCours = SolonEpgServiceLocator
            .getEPGFeuilleRouteService()
            .getEtapesEnCours(context.getSession(), dossier.getLastDocumentRoute());
        return CollectionUtils.isNotEmpty(etapesEnCours) ? etapesEnCours : Collections.singletonList("Aucune");
    }

    @Override
    public List<String> getNextStepLabels(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> etapesAVenir = SolonEpgServiceLocator
            .getEPGFeuilleRouteService()
            .getEtapesAVenir(context.getSession(), dossier.getLastDocumentRoute());
        return CollectionUtils.isNotEmpty(etapesAVenir) ? etapesAVenir : Collections.singletonList("Aucune");
    }

    @Override
    public MgppFichePresentationOEPList getFichePresentationOEP(SpecificContext context) {
        MgppFichePresentationOEPForm form = context.getFromContextData(MgppContextDataKey.FORM);
        MgppFichePresentationOEPDiffusePageProvider pageProvider = form.getPageProvider(
            context.getSession(),
            "mgppFichePresentationOEPDiffusePageProvider",
            "d.fpoep:",
            Arrays.asList(new Object[] { true })
        );
        List<MgppFichePresentationOEPDTO> fiches = pageProvider
            .getCurrentPage()
            .stream()
            .map(MgppFichePresentationOEPDTO.class::cast)
            .collect(Collectors.toList());
        MgppFichePresentationOEPList liste = new MgppFichePresentationOEPList();
        liste.setListe(fiches);
        liste.buildColonnes(form);
        return liste;
    }

    @Override
    public void rattacherDossierPuisTraiter(SpecificContext context) {
        EvenementDTO evenementDTO = getCurrentEvenementDTO(context);
        if (evenementDTO == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("mgpp.dossier.sans.communication.error"));
            return;
        }
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        dossierService.attachIdDossierToDosier(session, dossier, evenementDTO.getIdDossier());
        if (TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName())) {
            dossierService.updateFicheLoi(session, evenementDTO);
        }
        MgppUIServiceLocator.getEvenementActionService().traiterEvenement(context);
    }

    @Override
    public File exportDossier(SpecificContext context) {
        boolean isPDF = BooleanUtils.isTrue(context.getFromContextData(MgppContextDataKey.IS_PDF));
        MgppExportable list = context.getFromContextData(MgppContextDataKey.EXPORT_LIST);
        MgppExportConfig exportConfig = new MgppExportConfig(
            list.getDataForExport(),
            list.getExportName(),
            list.getHeaders(),
            isPDF
        );

        return ExportUtils.createXlsOrPdfFromDataSource(exportConfig.getDataSource(context.getSession()), isPDF);
    }

    @Override
    public Dossier findDossierFromId(SpecificContext context) {
        Dossier currentDossier = null;
        CoreSession session = context.getSession();
        String id = context.getFromContextData(STContextDataKey.ID);
        if (session.exists(new IdRef(id))) {
            currentDossier = session.getDocument(new IdRef(id)).getAdapter(Dossier.class);
        } else {
            // sinon on vérifie si id est un NOR pour récupérer le dossier ou un id de communication
            DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session, id);
            currentDossier =
                Optional
                    .ofNullable(dossierDoc)
                    .map(doc -> doc.getAdapter(Dossier.class))
                    .orElse(
                        SolonMgppServiceLocator.getDossierService().findDossierFromIdDossier(context.getSession(), id)
                    );
        }
        return currentDossier;
    }
}
