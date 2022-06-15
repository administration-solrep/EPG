package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_EPREUVE;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_PUBLICATION;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_SIGNATURE;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE_LABEL;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION_LABEL;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR;
import static fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgListTraitementForm;
import fr.dila.solonepg.ui.bean.EpgListTraitementPapierDTO;
import fr.dila.solonepg.ui.bean.TraitementPapierElementDTO;
import fr.dila.solonepg.ui.bean.TraitementPapierList;
import fr.dila.solonepg.ui.bean.action.EpgGestionListeActionDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgListTraitementDateEnum;
import fr.dila.solonepg.ui.services.EpgTraitementPapierListeUIService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.organigramme.ProtocolarOrderComparator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgTraitementPapierListeUIServiceImpl implements EpgTraitementPapierListeUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgTraitementPapierListeUIServiceImpl.class);

    private static final List<String> SIGNATAIRE_ELIGIBLE_TRAITEMENT_SERIE = ImmutableList.of(
        LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR,
        LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM,
        LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG
    );

    private static final Map<String, String> REPORT_NAME_BY_TYPE_LIST = ImmutableMap
        .<String, String>builder()
        .put(LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE, BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_SIGNATURE)
        .put(LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE, BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_EPREUVE)
        .put(LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION, BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_PUBLICATION)
        .build();

    @Override
    public TraitementPapierList consultTraitementPapierListe(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel listeDoc = context.getCurrentDocument();
        ListeTraitementPapier liste = listeDoc.getAdapter(ListeTraitementPapier.class);
        TraitementPapierList listeDto = new TraitementPapierList();
        listeDto.setTitre(
            ResourceHelper.getString(
                "traitement.papier.liste.titre.edition",
                buildListeName(liste.getTypeListe(), liste.getNumeroListe(), liste.getTypeSignature())
            )
        );
        listeDto.setListe(buildTraitementPapierListDTO(session, liste.getIdsDossier()));
        listeDto.setIdListe(listeDoc.getId());
        listeDto.setDatesList(buildDateTypeList(liste.getTypeListe(), liste.getTypeSignature()));
        return listeDto;
    }

    @Override
    public List<EpgListTraitementPapierDTO> getListeTraitementPapierSignature(SpecificContext context) {
        String date = context.getFromContextData(EpgContextDataKey.DATE);
        return getListeTraitementPapier(
            context.getSession(),
            date,
            Collections.singletonList(LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE)
        );
    }

    @Override
    public List<EpgListTraitementPapierDTO> getListeTraitementPapierAutre(SpecificContext context) {
        String date = context.getFromContextData(EpgContextDataKey.DATE);

        return getListeTraitementPapier(
            context.getSession(),
            date,
            ImmutableList.of(LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE, LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION)
        );
    }

    private List<EpgListTraitementDateEnum> buildDateTypeList(String listeType, String typeSignature) {
        List<EpgListTraitementDateEnum> dateTypeList = null;
        if (LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE.equals(listeType) && StringUtils.isNotEmpty(typeSignature)) {
            switch (typeSignature.toUpperCase()) {
                case LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR:
                    dateTypeList =
                        ImmutableList.of(
                            EpgListTraitementDateEnum.DATE_ENVOI_PR,
                            EpgListTraitementDateEnum.DATE_RETOUR_PR
                        );
                    break;
                case LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM:
                    dateTypeList = Collections.singletonList(EpgListTraitementDateEnum.DATE_RETOUR_PM);
                    break;
                case LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG:
                    dateTypeList = Collections.singletonList(EpgListTraitementDateEnum.DATE_RETOUR_SGG);
                    break;
                default:
                    break;
            }
        } else if (LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE.equals(listeType)) {
            dateTypeList = Collections.singletonList(EpgListTraitementDateEnum.DATE_DEMANDE_EPREUVE);
        }
        return dateTypeList;
    }

    private List<EpgListTraitementPapierDTO> getListeTraitementPapier(
        CoreSession session,
        String date,
        List<String> type
    ) {
        Calendar localDate;
        if (StringUtils.isEmpty(date)) {
            localDate = Calendar.getInstance();
        } else {
            localDate = SolonDateConverter.DATE_SLASH.parseToCalendar(date);
        }
        DateUtil.setDateToBeginingOfDay(localDate);
        String dateDebut = SolonDateConverter.DATETIME_DASH_REVERSE_T_SECOND_COLON_Z.format(localDate, true);
        DateUtil.setDateToEndOfDay(localDate);
        String dateFin = SolonDateConverter.DATETIME_DASH_REVERSE_T_SECOND_COLON_Z.format(localDate, true);

        DocumentModelList docList = SolonEpgServiceLocator
            .getListeTraitementPapierService()
            .getListeTraitementPapierFromTypeAndDate(session, dateDebut, dateFin, type);

        return docList
            .stream()
            .map(doc -> doc.getAdapter(ListeTraitementPapier.class))
            .map(EpgTraitementPapierListeUIServiceImpl::createEpgListTraitementPapierDTO)
            .collect(Collectors.toList());
    }

    private static EpgListTraitementPapierDTO createEpgListTraitementPapierDTO(ListeTraitementPapier liste) {
        String listeName = buildListeName(liste.getTypeListe(), liste.getNumeroListe(), liste.getTypeSignature());
        String typeLabel = buildTypeListeLabel(liste.getTypeListe());
        return new EpgListTraitementPapierDTO(liste.getId(), listeName, typeLabel);
    }

    private static String buildListeName(String listeType, Long listeNumero, String typeSignature) {
        String listeName;
        switch (listeType) {
            case LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE:
                listeName = StringUtils.defaultString(StringUtils.upperCase(typeSignature)) + listeNumero;
                break;
            case LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE:
                listeName = LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE_LABEL + listeNumero;
                break;
            case LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION:
                listeName = LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION_LABEL + listeNumero;
                break;
            default:
                listeName = "";
        }
        return listeName;
    }

    private static String buildTypeListeLabel(String typeListe) {
        String typeListeLabel;
        switch (typeListe) {
            case LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE:
                typeListeLabel = ResourceHelper.getString("suivi.gestionListes.demande.epreuve.label");
                break;
            case LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION:
                typeListeLabel = ResourceHelper.getString("suivi.gestionListes.demande.publication.label");
                break;
            default:
                typeListeLabel = "";
        }
        return typeListeLabel;
    }

    private static List<TraitementPapierElementDTO> buildTraitementPapierListDTO(
        CoreSession session,
        List<String> idsDossier
    ) {
        return idsDossier
            .stream()
            .map(IdRef::new)
            .map(session::getDocument)
            .map(doc -> doc.getAdapter(Dossier.class))
            .map(EpgTraitementPapierListeUIServiceImpl::createTraitementPapierElementDTO)
            .collect(Collectors.toList());
    }

    private static TraitementPapierElementDTO createTraitementPapierElementDTO(Dossier dossier) {
        String libelleMin = SolonEpgServiceLocator
            .getEpgOrganigrammeService()
            .getOrganigrammeNodeById(dossier.getMinistereResp(), OrganigrammeType.MINISTERE)
            .getLabel();

        return new TraitementPapierElementDTO(
            dossier.getDocument().getId(),
            libelleMin,
            dossier.getNumeroNor(),
            dossier.getTitreActe()
        );
    }

    @Override
    public boolean removeElementFromListe(SpecificContext context) {
        CoreSession session = context.getSession();
        List<String> idsDossierToRmv = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        DocumentModel listeDoc = context.getCurrentDocument();
        ListeTraitementPapier liste = context.getCurrentDocument().getAdapter(ListeTraitementPapier.class);
        boolean isListDeleted;

        List<String> newIdsListe = liste
            .getIdsDossier()
            .stream()
            .filter(id -> !idsDossierToRmv.contains(id))
            .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newIdsListe)) {
            liste.setIdsDossier(newIdsListe);
            session.saveDocument(listeDoc);
            isListDeleted = false;
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("suivi.gestionListes.message.element.supprimer"));
        } else {
            session.removeDocument(listeDoc.getRef());
            isListDeleted = true;
            context
                .getMessageQueue()
                .addInfoToQueue(ResourceHelper.getString("suivi.gestionListes.message.liste.supprimer"));
        }
        return isListDeleted;
    }

    @Override
    public File exportListeModal(SpecificContext context) {
        CoreSession session = context.getSession();
        try {
            return SolonEpgServiceLocator
                .getListeTraitementPapierService()
                .exportPdfListe(session, context.getCurrentDocument());
        } catch (EPGException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("traitement.papier.liste.export.error.create.file"));
        }
        return null;
    }

    @Override
    public void traiterEnSerieListe(SpecificContext context) {
        CoreSession session = context.getSession();
        ListeTraitementPapier liste = context.getCurrentDocument().getAdapter(ListeTraitementPapier.class);
        EpgListTraitementForm form = context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_LIST_FORM);

        List<DocumentModel> docList = session.getDocuments(
            form.getFoldersId().stream().map(IdRef::new).toArray(IdRef[]::new)
        );
        if (LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE.equals(liste.getTypeListe())) {
            switch (liste.getTypeSignature().toUpperCase()) {
                case LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR:
                    SolonEpgServiceLocator
                        .getListeTraitementPapierService()
                        .traiterEnSerieSignatairePRUnrestricted(
                            session,
                            docList,
                            form.getDateRetourPR(),
                            form.getDateEnvoiPR()
                        );
                    break;
                case LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM:
                    SolonEpgServiceLocator
                        .getListeTraitementPapierService()
                        .traiterEnSerieSignatairePMUnrestricted(session, docList, form.getDateRetourPM());
                    break;
                case LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG:
                    SolonEpgServiceLocator
                        .getListeTraitementPapierService()
                        .traiterEnSerieSignataireSGGUnrestricted(session, docList, form.getDateRetourSGG());
                    break;
                default:
                    break;
            }
        } else if (LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE.equals(liste.getTypeListe())) {
            SolonEpgServiceLocator
                .getListeTraitementPapierService()
                .traiterEnSerieDemandeEpreuveUnrestricted(session, docList, form.getDateDemandeEpreuve());
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("suivi.detailListe.modale.traitementSerie.message.error"));
        }
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("suivi.detailListe.modale.traitementSerie.message.success"));
    }

    @Override
    public void initGestionListeActionContext(SpecificContext context) {
        EpgGestionListeActionDTO gestionListeActionDto = new EpgGestionListeActionDTO();
        gestionListeActionDto.setCanTraiterListeSerie(canTraiterListeSerie(context));
        context.putInContextData(EpgContextDataKey.GESTION_LIST_ACTIONS, gestionListeActionDto);
    }

    private boolean canTraiterListeSerie(SpecificContext context) {
        ListeTraitementPapier liste = context.getCurrentDocument().getAdapter(ListeTraitementPapier.class);
        return (
            (
                LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE.equals(liste.getTypeListe()) &&
                StringUtils.isNotEmpty(liste.getTypeSignature()) &&
                SIGNATAIRE_ELIGIBLE_TRAITEMENT_SERIE.contains(liste.getTypeSignature().toUpperCase())
            ) ||
            LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE.equals(liste.getTypeListe())
        );
    }

    @Override
    public Response imprimerListe(SpecificContext context) {
        ListeTraitementPapier liste = context.getCurrentDocument().getAdapter(ListeTraitementPapier.class);
        BirtOutputFormat outputFormat = context.getFromContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT);

        Map<String, Serializable> scalarParameters = new HashMap<>();
        StringBuilder ministereParam = new StringBuilder();
        final List<EntiteNode> ministereList = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
        ministereList.sort(new ProtocolarOrderComparator());
        // Récupération de la liste des ministères
        for (final OrganigrammeNode node : ministereList) {
            ministereParam
                .append("$id$=")
                .append(node.getId())
                .append(";;$label$=")
                .append(node.getLabel())
                .append(";;&");
        }

        scalarParameters.put("LISTEID_PARAM", liste.getDocument().getId());
        scalarParameters.put("MINISTERES_PARAM", ministereParam.toString());

        File file = null;
        String reportName = REPORT_NAME_BY_TYPE_LIST.get(liste.getTypeListe());
        if (StringUtils.isNotBlank(reportName)) {
            file =
                SSServiceLocator
                    .getBirtGenerationService()
                    .generate(reportName, null, outputFormat, scalarParameters, null, true);
        }

        return FileDownloadUtils.getAttachmentDocx(
            file,
            FileUtils.generateCompleteDocxFilename("Liste_de_traitement_papier-" + liste.getNumeroListe())
        );
    }
}
