package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.solonepg.core.dto.DossierLinkMinimalMapper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.SelectionDto;
import fr.dila.solonepg.ui.bean.action.EpgSelectionToolActionDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgSelectionTypeEnum;
import fr.dila.solonepg.ui.services.EpgSelectionToolUIService;
import fr.dila.solonepg.ui.services.actions.EpgDocumentRoutingActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.recherche.IdLabel;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement.ElementLifeCycleState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.thymeleaf.util.StringUtils;

public class EpgSelectionToolUIServicerImpl implements EpgSelectionToolUIService {
    public static final String SELECTION_TYPE_SESSION_KEY = "selectionType";
    public static final String SELECTION_LIST_SESSION_KEY = "selectionList";

    private static final String ERROR_SELECTION_TYPE_FDR = "outilSelection.action.create.list.error.type.fdr";

    public static final List<String> INCOMPATIBLE_STATUS_WITH_PUBLICATION = ImmutableList.of(
        VocabularyConstants.STATUT_ABANDONNE,
        VocabularyConstants.STATUT_CLOS,
        VocabularyConstants.STATUT_PUBLIE,
        VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION
    );

    @Override
    public void addDocToSelection(SpecificContext context) {
        DocumentModel doc = context.getCurrentDocument();
        EpgSelectionTypeEnum currentSelectionType = getSelectionType(context);
        Map<String, SelectionDto> selectionMap = getSelectionMap(context);
        String docType = doc.getType();

        if (currentSelectionType != null && MapUtils.isNotEmpty(selectionMap)) {
            if (selectionMap.containsKey(doc.getId())) {
                context.getMessageQueue().addErrorToQueue(getString("outilSelection.error.deja.present"));
            } else if (currentSelectionType == EpgSelectionTypeEnum.getByDocType(docType)) {
                putItemInSelectionMap(context, doc, selectionMap);
                context.putInContextData(EpgContextDataKey.UPDATED_IDS, Collections.singletonList(doc.getId()));
            } else {
                context.getMessageQueue().addErrorToQueue(getString("outilSelection.error.current.type.incompatible"));
            }
        } else if (EpgSelectionTypeEnum.getByDocType(docType) != null) {
            setSelectionType(context, EpgSelectionTypeEnum.getByDocType(docType));
            selectionMap = new HashMap<>();
            putItemInSelectionMap(context, doc, selectionMap);
            setSelectionList(context, selectionMap);
            context.putInContextData(EpgContextDataKey.UPDATED_IDS, Collections.singletonList(doc.getId()));
        } else {
            context.getMessageQueue().addErrorToQueue(getString("outilSelection.error.type.doc"));
        }
    }

    @Override
    public void removeDocFromSelection(SpecificContext context) {
        DocumentModel doc = context.getCurrentDocument();
        Map<String, SelectionDto> selectionMap = getSelectionMap(context);
        if (selectionMap != null && selectionMap.containsKey(doc.getId())) {
            selectionMap.remove(doc.getId());
            context.putInContextData(EpgContextDataKey.UPDATED_IDS, Collections.singletonList(doc.getId()));
            if (STConstant.DOSSIER_DOCUMENT_TYPE.equals(doc.getType())) {
                context.getMessageQueue().addToastSuccess(getString("outilSelection.dossier.retire"));
            } else if (SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equals(doc.getType())) {
                context.getMessageQueue().addToastSuccess(getString("outilSelection.fdr.retiree"));
            }
        } else {
            context.getMessageQueue().addErrorToQueue(getString("outilSelection.error.suppression.impossible"));
        }
    }

    @Override
    public void createListDemandeEpreuve(SpecificContext context) {
        final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator.getListeTraitementPapierService();
        List<SelectionDto> selectionList = getSelectionList(context);
        List<DocumentModel> docList = new ArrayList<>();
        List<String> norErreur = new ArrayList<>();

        if (
            getSelectionType(context) == EpgSelectionTypeEnum.DOSSIER &&
            CollectionUtils.isNotEmpty(getSelectionList(context))
        ) {
            for (SelectionDto dto : selectionList) {
                context.setCurrentDocument(dto.getId());
                Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
                if (TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE.equals(dossier.getTypeActe())) {
                    norErreur.add(dossier.getNumeroNor());
                } else {
                    docList.add(dossier.getDocument());
                }
            }
            if (CollectionUtils.isNotEmpty(docList)) {
                listeTraitementPapierService.creerNouvelleListeTraitementPapierDemandesEpreuve(
                    context.getSession(),
                    docList
                );
                // une fois traité retire les elements de la sélection
                context.putInContextData(EpgContextDataKey.UPDATED_IDS, removeDocIdsFromMap(context, docList));
            }
            createMessage(
                context,
                norErreur,
                "outilSelection.action.create.list.epreuve.error.tyepActe",
                "outilSelection.action.create.list.epreuve.success"
            );
        } else {
            context.getMessageQueue().addErrorToQueue(getString(ERROR_SELECTION_TYPE_FDR));
        }
    }

    @Override
    public void createListDemandePublication(SpecificContext context) {
        final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator.getListeTraitementPapierService();
        List<SelectionDto> selectionList = getSelectionList(context);
        List<DocumentModel> docList = new ArrayList<>();
        List<String> norErreur = new ArrayList<>();

        if (
            getSelectionType(context) == EpgSelectionTypeEnum.DOSSIER &&
            CollectionUtils.isNotEmpty(getSelectionList(context))
        ) {
            for (SelectionDto dto : selectionList) {
                context.setCurrentDocument(dto.getId());
                Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
                if (
                    INCOMPATIBLE_STATUS_WITH_PUBLICATION.contains(dossier.getStatut()) ||
                    TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE.equals(dossier.getTypeActe())
                ) {
                    norErreur.add(dossier.getNumeroNor());
                } else {
                    docList.add(dossier.getDocument());
                }
            }
            if (CollectionUtils.isNotEmpty(docList)) {
                listeTraitementPapierService.creerNouvelleListeTraitementPapierDemandesDePublication(
                    context.getSession(),
                    docList
                );
                // une fois traité retire les elements de la sélection
                context.putInContextData(EpgContextDataKey.UPDATED_IDS, removeDocIdsFromMap(context, docList));
            }
            createMessage(
                context,
                norErreur,
                "outilSelection.action.create.list.publication.error",
                "outilSelection.action.create.list.publication.success"
            );
        } else {
            context.getMessageQueue().addErrorToQueue(getString(ERROR_SELECTION_TYPE_FDR));
        }
    }

    @Override
    public void createListMiseEnSignature(SpecificContext context) {
        List<SelectionDto> selectionList = getSelectionList(context);

        if (getSelectionType(context) == EpgSelectionTypeEnum.DOSSIER && CollectionUtils.isNotEmpty(selectionList)) {
            List<DocumentModel> docs = context
                .getSession()
                .getDocuments(selectionList.stream().map(SelectionDto::getId).collect(Collectors.toList()), null);
            createListeSignatureIfAllow(context, docs, true);
        } else {
            context.getMessageQueue().addErrorToQueue(getString(ERROR_SELECTION_TYPE_FDR));
        }
    }

    @Override
    public void createListeSignatureIfAllow(
        SpecificContext context,
        List<DocumentModel> docs,
        boolean isSelectionTools
    ) {
        final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator.getListeTraitementPapierService();
        TraitementPapier traitementPapier = docs.get(0).getAdapter(TraitementPapier.class);
        String typeSignataireId = traitementPapier.getSignataire();

        boolean signatairesMultiple =
            docs.stream().map(d -> d.getAdapter(TraitementPapier.class).getSignataire()).distinct().count() > 1;

        List<DocumentModel> docsSansSignataire = docs
            .stream()
            .filter(
                d ->
                    VocabularyConstants.TYPE_SIGNATAIRE_TP_AUCUN.equals(
                        d.getAdapter(TraitementPapier.class).getSignataire()
                    )
            )
            .collect(Collectors.toList());

        List<DocumentModel> docAlreadyInList = docs
            .stream()
            .filter(
                d ->
                    CollectionUtils.isNotEmpty(
                        listeTraitementPapierService.getListeTraitementPapierOfDossierAndType(
                            context.getSession(),
                            d.getId(),
                            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE
                        )
                    )
            )
            .collect(Collectors.toList());

        if (!signatairesMultiple && docsSansSignataire.isEmpty()) {
            List<DocumentModel> docList = new ArrayList<>(CollectionUtils.subtract(docs, docAlreadyInList));
            if (CollectionUtils.isNotEmpty(docList)) {
                ListeTraitementPapier liste = listeTraitementPapierService.creerNouvelleListeTraitementPapierMiseEnSignature(
                    context.getSession(),
                    typeSignataireId,
                    docList
                );
                if (isSelectionTools) {
                    // une fois traité retire les elements de la sélection
                    context.putInContextData(EpgContextDataKey.UPDATED_IDS, removeDocIdsFromMap(context, docList));
                }
                context.putInContextData(EpgContextDataKey.ID_LISTE, liste.getId());
                context
                    .getMessageQueue()
                    .addToastSuccess(getString("outilSelection.action.create.list.signature.success"));
            }
        }

        if (CollectionUtils.isNotEmpty(docAlreadyInList)) {
            context.putInContextData(
                EpgContextDataKey.MESSAGE_ERROR,
                getString(
                    "outilSelection.action.create.list.signature.error.already.in.list",
                    docAlreadyInList
                        .stream()
                        .map(doc -> doc.getAdapter(Dossier.class))
                        .map(Dossier::getNumeroNor)
                        .collect(Collectors.joining(", "))
                )
            );
        }
        if (signatairesMultiple) {
            docs.removeAll(docAlreadyInList);
            context
                .getMessageQueue()
                .addErrorToQueue(
                    getString(
                        "outilSelection.action.create.list.signature.error",
                        collectStringNorSignataireError(docs)
                    )
                );
        }

        if (!docsSansSignataire.isEmpty()) {
            context
                .getMessageQueue()
                .addErrorToQueue(
                    getString(
                        "outilSelection.action.create.list.signature.error.no.signataire",
                        docsSansSignataire
                            .stream()
                            .map(d -> d.getAdapter(Dossier.class).getNumeroNor())
                            .collect(Collectors.joining(", "))
                    )
                );
        }
    }

    private List<String> collectStringNorSignataireError(List<DocumentModel> docs) {
        return docs
            .stream()
            .map(EpgSelectionToolUIServicerImpl::createStringNorSignataire)
            .collect(Collectors.toList());
    }

    private static String createStringNorSignataire(DocumentModel doc) {
        Dossier dossier = doc.getAdapter(Dossier.class);
        TraitementPapier traitementPapier = doc.getAdapter(TraitementPapier.class);

        TypeSignataireTraitementPapierService typeSignataireTraitementPapierService = SolonEpgServiceLocator.getTypeSignataireTraitementPapierService();
        SelectValueDTO signataire = typeSignataireTraitementPapierService
            .getEntry(traitementPapier.getSignataire())
            .map(t -> new SelectValueDTO(t.getKey(), t.getValue()))
            .orElseGet(SelectValueDTO::new);

        return ResourceHelper.getString(
            "outilSelection.action.create.list.signature.detail.error",
            dossier.getNumeroNor(),
            signataire.getValue()
        );
    }

    private void createMessage(SpecificContext context, List<String> paramError, String errorKey, String successKey) {
        createMessage(context, paramError, new ArrayList<>(), errorKey, successKey);
    }

    private void createMessage(
        SpecificContext context,
        List<String> paramError,
        List<String> paramSuccess,
        String errorKey,
        String successKey
    ) {
        if (CollectionUtils.isNotEmpty(paramError)) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString(errorKey, paramError.stream().collect(Collectors.joining(", "))));
        } else {
            context
                .getMessageQueue()
                .addToastSuccess(getString(successKey, paramSuccess.stream().collect(Collectors.joining(", "))));
        }
    }

    @Override
    public void initSelectionActionContext(SpecificContext context) {
        EpgSelectionToolActionDTO selectionActionDto = new EpgSelectionToolActionDTO();
        selectionActionDto.setSelectionNotEmpty(CollectionUtils.isEmpty(getSelectionList(context)));
        selectionActionDto.setSelectionTypeDossier(getSelectionType(context) == EpgSelectionTypeEnum.DOSSIER);
        selectionActionDto.setSelectionTypeModele(getSelectionType(context) == EpgSelectionTypeEnum.MODELE);
        context.putInContextData(EpgContextDataKey.SELECTION_ACTIONS, selectionActionDto);
    }

    @Override
    public void setSelectionList(SpecificContext context, Map<String, SelectionDto> selectionList) {
        UserSessionHelper.putUserSessionParameter(
            context,
            EpgSelectionToolUIServicerImpl.SELECTION_LIST_SESSION_KEY,
            selectionList
        );
    }

    @Override
    public void setSelectionType(SpecificContext context, EpgSelectionTypeEnum type) {
        UserSessionHelper.putUserSessionParameter(
            context,
            EpgSelectionToolUIServicerImpl.SELECTION_TYPE_SESSION_KEY,
            type
        );
    }

    @Override
    public String removeDocIdFromMap(SpecificContext context) {
        List<String> id = removeDocIdsFromMap(context, Collections.singletonList(context.getCurrentDocument()));
        return id.get(0);
    }

    private List<String> removeDocIdsFromMap(SpecificContext context, List<DocumentModel> docs) {
        Map<String, SelectionDto> selectionMap = getSelectionMap(context);
        docs
            .stream()
            .forEach(
                doc -> {
                    if (selectionMap.containsKey(doc.getId())) {
                        selectionMap.remove(doc.getId());
                    }
                }
            );
        return docs.stream().map(doc -> doc.getId()).collect(Collectors.toList());
    }

    private Map<String, SelectionDto> getSelectionMap(SpecificContext context) {
        return UserSessionHelper.getUserSessionParameter(
            context,
            EpgSelectionToolUIServicerImpl.SELECTION_LIST_SESSION_KEY
        );
    }

    @Override
    public EpgSelectionTypeEnum getSelectionType(SpecificContext context) {
        return UserSessionHelper.getUserSessionParameter(
            context,
            EpgSelectionToolUIServicerImpl.SELECTION_TYPE_SESSION_KEY
        );
    }

    @Override
    public List<SelectionDto> getSelectionList(SpecificContext context) {
        return Optional
            .ofNullable(getSelectionMap(context))
            .map(
                map -> map.values().stream().map(dto -> getEtapesForDossier(dto, context)).collect(Collectors.toList())
            )
            .orElseGet(Collections::emptyList);
    }

    @Override
    public boolean isInSelectionTool(SpecificContext context, String id) {
        Map<String, SelectionDto> selectionMap = getSelectionMap(context);
        return selectionMap != null && selectionMap.containsKey(id);
    }

    private void putItemInSelectionMap(
        SpecificContext context,
        DocumentModel doc,
        Map<String, SelectionDto> selectionMap
    ) {
        EpgSelectionTypeEnum selectionType = EpgSelectionTypeEnum.getByDocType(doc.getType());
        if (selectionType != null) {
            selectionType.getAddFunction().accept(doc, selectionMap);
            context.getMessageQueue().addToastSuccess(getString(selectionType.getMessage()));
        }
    }

    @Override
    public void addMultipleItemsToSelection(SpecificContext context) {
        EpgSelectionTypeEnum currentSelectionType = getSelectionType(context);
        Map<String, SelectionDto> selectionMap = getSelectionMap(context);
        EpgSelectionTypeEnum selectedType = context.getFromContextData(EpgContextDataKey.SELECTION_TYPE);

        if (currentSelectionType == null || MapUtils.isEmpty(selectionMap)) {
            setSelectionType(context, selectedType);
            selectionMap = new HashMap<>();
            setSelectionList(context, selectionMap);
        } else if (currentSelectionType != selectedType) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("outilSelection.error.current.type.incompatible"));
            return;
        }

        List<String> ids = context.getFromContextData(STContextDataKey.IDS);
        List<DocumentModel> docs = ids
            .stream()
            .map(id -> context.getSession().getDocument(new IdRef(id)))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        for (DocumentModel doc : docs) {
            selectedType.getAddFunction().accept(doc, selectionMap);
        }
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString(StringUtils.concat(selectedType.getMessage(), ".multiple")));
    }

    @Override
    public void addDossiersToDossiersSuivis(SpecificContext context) {
        List<DocumentModel> docs = context.getSession().getDocuments(getSelectionMap(context).keySet(), null);

        if (CollectionUtils.isNotEmpty(docs)) {
            if (SolonEpgServiceLocator.getDossierSignaleService().verserDossiers(context.getSession(), docs) == -1) {
                context
                    .getMessageQueue()
                    .addErrorToQueue(ResourceHelper.getString("outilSelection.ajout.dossiers.suivis.error"));
            } else {
                context
                    .getMessageQueue()
                    .addToastSuccess(ResourceHelper.getString("outilSelection.ajout.dossiers.suivis.success"));
                // une fois traité retire les elements de la sélection
                context.putInContextData(EpgContextDataKey.UPDATED_IDS, removeDocIdsFromMap(context, docs));
            }
        }
    }

    @Override
    public void deleteIdFromSelectionTool(SpecificContext context) {
        Map<String, SelectionDto> selectionMap = getSelectionMap(context);
        if (selectionMap != null) {
            List<String> idsToDelete = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
            idsToDelete.forEach(selectionMap::remove);
        }
    }

    @Override
    public void abandonnerDossiers(SpecificContext context) {
        List<String> selectedIds = getSelectionList(context)
            .stream()
            .map(SelectionDto::getId)
            .collect(Collectors.toList());
        List<DocumentModel> docList = new ArrayList<>();
        List<String> norErreur = new ArrayList<>();
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        if (
            getSelectionType(context) == EpgSelectionTypeEnum.DOSSIER &&
            CollectionUtils.isNotEmpty(getSelectionList(context))
        ) {
            for (DocumentModel doc : context.getSession().getDocuments(selectedIds, null)) {
                Dossier dossier = doc.getAdapter(Dossier.class);
                if (
                    VocabularyConstants.STATUT_LANCE.equals(dossier.getStatut()) &&
                    ElementLifeCycleState.running.name().equals(doc.getCurrentLifeCycleState())
                ) {
                    docList.add(doc);
                    dossierService.abandonDossier(context.getSession(), doc);
                } else {
                    norErreur.add(dossier.getNumeroNor());
                }
            }
            // une fois traité retire les elements de la sélection
            context.putInContextData(EpgContextDataKey.UPDATED_IDS, removeDocIdsFromMap(context, docList));
            createMessage(
                context,
                norErreur,
                "outilSelection.action.abandon.error",
                "outilSelection.action.abandon.success"
            );
        } else {
            context.getMessageQueue().addErrorToQueue(getString("outilSelection.action.abandon.error.type.fdr"));
        }
    }

    @Override
    public void reattribuerDossiers(SpecificContext context) {
        List<DocumentModel> docs = context.getSession().getDocuments(getSelectionMap(context).keySet(), null);
        String ministere = context.getFromContextData(STContextDataKey.MINISTERE_ID);
        String direction = context.getFromContextData(STContextDataKey.DIRECTION_ID);

        List<DocumentModel> reattributedDocs = docs
            .stream()
            .map(doc -> doc.getAdapter(Dossier.class))
            .filter(dossier -> SolonEpgServiceLocator.getDossierService().isReattribuable(dossier))
            .map(Dossier::getDocument)
            .collect(Collectors.toList());
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.reattribuerDossiersUnrestricted(reattributedDocs, ministere, direction, context.getSession());
        removeDocIdsFromMap(context, reattributedDocs);
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("outilSelection.reattribution.succes"));
    }

    /**
     * Substitution de la feuille de route des dossiers dans l'outil de sélection
     */
    @Override
    public void substituerMassRoute(SpecificContext context) {
        CoreSession session = context.getSession();
        List<SelectionDto> selectionList = getSelectionList(context);
        List<DocumentModel> docList = new ArrayList<>();
        List<String> norErreur = new ArrayList<>();
        List<DocumentModel> dossierTraite = new ArrayList<>();

        String routeId = context.getFromContextData(SSContextDataKey.ID_MODELE);
        DocumentModel newRouteDoc = session.getDocument(new IdRef(routeId));

        if (newRouteDoc != null) {
            for (SelectionDto dto : selectionList) {
                DocumentModel dossierDoc = session.getDocument(new IdRef(dto.getId()));
                if (session.hasPermission(dossierDoc.getRef(), SecurityConstants.WRITE)) {
                    docList.add(dossierDoc);
                } else {
                    norErreur.add(dossierDoc.getAdapter(Dossier.class).getNumeroNor());
                }
            }

            for (DocumentModel dossierDoc : docList) {
                // recupere l'ancien feuille de route
                Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                EpgDocumentRoutingActionService routingActions = SolonEpgActionServiceLocator.getEpgDocumentRoutingActionService();
                final SSFeuilleRoute oldRoute = routingActions.getRelatedRoute(session, dossierDoc);
                if (VocabularyConstants.STATUT_INITIE.equals(dossier.getStatut())) {
                    DocumentModel oldRouteDoc = oldRoute.getDocument();
                    // Substitue la feuille de route
                    final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
                    dossierDistributionService.substituerFeuilleRouteAndUnlockFdr(
                        session,
                        dossierDoc,
                        oldRouteDoc,
                        newRouteDoc,
                        STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_SUBSTITUTION
                    );
                    dossierTraite.add(dossierDoc);
                }
            }
            // une fois traité retire les elements de la sélection
            removeDocIdsFromMap(context, dossierTraite);

            createMessage(
                context,
                norErreur,
                dossierTraite
                    .stream()
                    .map(doc -> doc.getAdapter(Dossier.class))
                    .map(Dossier::getNumeroNor)
                    .collect(Collectors.toList()),
                "outilSelection.action.substitution.fdr.message.error",
                "outilSelection.action.substitution.fdr.message.success"
            );
        } else {
            context.getMessageQueue().addErrorToQueue(getString("feedback.reponses.document.route.no.valid.route"));
        }
    }

    @Override
    public void substituerPostes(SpecificContext context) {
        List<DocumentModel> modelesDocs = context
            .getSession()
            .getDocuments(
                getSelectionList(context).stream().map(SelectionDto::getId).collect(Collectors.toList()),
                null
            );
        String ancienPoste = context.getFromContextData(EpgContextDataKey.ANCIEN_POSTE);
        String nouveauPoste = context.getFromContextData(EpgContextDataKey.NOUVEAU_POSTE);
        SSServiceLocator
            .getFeuilleRouteModelService()
            .substituerPoste(context.getSession(), modelesDocs, ancienPoste, nouveauPoste);

        // On vide l'outil de sélection
        setSelectionType(context, null);
        setSelectionList(context, new HashMap<>());
        context.getMessageQueue().addToastSuccess(getString("outilSelection.substitution.poste.success"));
    }

    @Override
    public void transfererDossiers(SpecificContext context) {
        List<DocumentModel> docs = context.getSession().getDocuments(getSelectionMap(context).keySet(), null);
        String ministere = context.getFromContextData(STContextDataKey.MINISTERE_ID);
        String direction = context.getFromContextData(STContextDataKey.DIRECTION_ID);

        List<DocumentModel> transferedDocs = docs
            .stream()
            .map(doc -> doc.getAdapter(Dossier.class))
            .filter(dossier -> SolonEpgServiceLocator.getDossierService().isTransferable(dossier))
            .map(Dossier::getDocument)
            .collect(Collectors.toList());
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.transfertDossiersUnrestricted(transferedDocs, ministere, direction, context.getSession());
        removeDocIdsFromMap(context, transferedDocs);
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("outilSelection.transfert.succes"));
    }

    private SelectionDto getEtapesForDossier(SelectionDto dto, SpecificContext context) {
        if (getSelectionType(context) == EpgSelectionTypeEnum.DOSSIER) {
            EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            List<DocumentModel> dossierLinks = corbeilleService.findDossierLink(context.getSession(), dto.getId());
            List<IdLabel> caseLinkIdsLabels = dossierLinks
                .stream()
                .map(DossierLink::getAdapter)
                .map(DossierLinkMinimalMapper::getIdLabelFromDossierLink)
                .collect(Collectors.toList());
            dto.setCaseLinkIdsLabels(caseLinkIdsLabels);
        }
        return dto;
    }
}
