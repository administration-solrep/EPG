package fr.dila.solonepg.ui.services.impl;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTranspositionImmutable;
import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.core.cases.typescomplexe.DossierTranspositionImpl;
import fr.dila.solonepg.core.cases.typescomplexe.TranspositionsContainerImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.travail.DossierCreationForm;
import fr.dila.solonepg.ui.bean.travail.OrdonnanceForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.exceptions.RequiredPosteSelection;
import fr.dila.solonepg.ui.services.EpgDossierCreationUIService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.NorDirection;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.security.principal.STPrincipal;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.services.actions.DossierLockActionService;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.services.actions.STLockActionService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

public class EpgDossierCreationUIServiceImpl implements EpgDossierCreationUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgDossierCreationUIServiceImpl.class);

    private String getEntitePrm(CoreSession session) {
        // on récupère le ministere et la direction à partir du NOR ministère
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        return tableReferenceService.getMinisterePrm(session);
    }

    private String getDirectionPrm(CoreSession session) {
        List<VocabularyEntry> norPrmList = getDirectionPrmList(session);
        if (CollectionUtils.isNotEmpty(norPrmList)) {
            return norPrmList.get(0).getId();
        }
        return "";
    }

    private List<VocabularyEntry> getDirectionPrmList(CoreSession session) {
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        return tableReferenceService.getNorDirectionsForCreation(session);
    }

    @Override
    public String getDefaultMinistere(SpecificContext context) {
        String posteId = getPosteDefautOrFirstPoste(context);
        if (StringUtils.isNotEmpty(posteId)) {
            List<EntiteNode> ministereNodes = STServiceLocator
                .getSTMinisteresService()
                .getMinistereParentFromPoste(posteId);

            if (CollectionUtils.isNotEmpty(ministereNodes)) {
                // Sélectionne le premier ministère de l'utilisateur
                return ministereNodes.get(0).getId();
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getDefaultDirection(SpecificContext context) {
        String posteId = getPosteDefautOrFirstPoste(context);
        if (StringUtils.isNotEmpty(posteId)) {
            List<OrganigrammeNode> directionList = STServiceLocator
                .getSTUsAndDirectionService()
                .getDirectionFromPoste(posteId);
            if (CollectionUtils.isNotEmpty(directionList)) {
                UniteStructurelleNode directionNode = (UniteStructurelleNode) directionList.get(0);
                return directionNode.getId();
            }
        }
        return StringUtils.EMPTY;
    }

    private String getPosteDefautOrFirstPoste(SpecificContext context) {
        String posteId = context.getFromContextData(EpgContextDataKey.POSTE_DEFAUT);
        if (StringUtils.isEmpty(posteId)) {
            STPrincipal principal = (STPrincipal) context.getSession().getPrincipal();
            Set<String> postesId = principal.getPosteIdSet();
            if (CollectionUtils.isNotEmpty(postesId)) {
                posteId = postesId.iterator().next();
            } else {
                posteId = StringUtils.EMPTY;
            }
        }
        return posteId;
    }

    @Override
    public List<SelectValueDTO> getUserPostes(SpecificContext context) {
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        Set<String> postesId = principal.getPosteIdSet();
        return STServiceLocator
            .getSTPostesService()
            .getPostesNodes(postesId)
            .stream()
            .map(elt -> new SelectValueDTO(elt.getId(), elt.getLabel()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getNorPrmList(SpecificContext context) {
        return getDirectionPrmList(context.getSession())
            .stream()
            .map(elt -> new SelectValueDTO(elt.getId(), elt.getLabel()))
            .collect(Collectors.toList());
    }

    /**
     * Crée le dossier avec les données enregistrées au cours de l'assistant
     *
     * @return Vue de l'espace création
     * @throws Exception
     */
    @Override
    public String creerDossier(SpecificContext context) {
        DossierCreationForm dossierCreationForm = context.getFromContextData(EpgContextDataKey.DOSSIER_CREATION_FORM);
        Objects.requireNonNull(dossierCreationForm, "Un dossier creation form est requis");

        // on vérifie que tous les champs requis sont remplis
        if (!isRequiredFieldsFilled(context, dossierCreationForm)) {
            return "";
        }

        // Enregistrement du dossier
        TypeActeService typeActeService = SolonEpgServiceLocator.getTypeActeService();
        String typeActeId = typeActeService.getId(dossierCreationForm.getTypeActe());
        boolean estRectificatif = TypeActeConstants.TYPE_ACTE_RECTIFICATIF.equals(typeActeId);

        // données du dossier à remplir
        String ministereRespFinal = null;
        String directionRespFinal = null;
        Dossier oldDossier = null;
        String norMinistere = null;
        String norDirection = null;

        // récupération du ministère et de la direction
        CoreSession session = context.getSession();

        String tableReferenceMinisterePrm = getEntitePrm(session);
        String tableReferenceDirectionPrm = getDirectionPrm(session);

        if (estRectificatif) {
            // on récupère l'ancien dossier
            DocumentModel oldDossierModel = getDossierARectifier(context, dossierCreationForm.getNorRectificatif());

            if (oldDossierModel == null) {
                return "";
            }

            oldDossier = oldDossierModel.getAdapter(Dossier.class);

            // on définit le ministère et la direction responsable à partir
            // du dossier
            ministereRespFinal = oldDossier.getMinistereResp();
            directionRespFinal = oldDossier.getDirectionResp();

            // on définit les nors des ministeres et direction
            String oldDossierNumeroNor = oldDossier.getNumeroNor();
            norMinistere = oldDossierNumeroNor.substring(0, 3);
            norDirection = oldDossierNumeroNor.substring(3, 4);
        } else {
            String norPrm = dossierCreationForm.getNorPrm();
            if (StringUtils.isNotEmpty(norPrm)) {
                ministereRespFinal = tableReferenceMinisterePrm;
                directionRespFinal = tableReferenceDirectionPrm;
            } else {
                ministereRespFinal = dossierCreationForm.getEntite();
                directionRespFinal = dossierCreationForm.getDirection();
            }

            // Détermine les lettres de NOR du ministère
            STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();
            EntiteNode ministereNode = stMinisteresService.getEntiteNode(ministereRespFinal);
            norMinistere = ministereNode.getNorMinistere();
            STUsAndDirectionService stUsAndDirectionService = STServiceLocator.getSTUsAndDirectionService();
            UniteStructurelleNode directionNode = stUsAndDirectionService.getUniteStructurelleNode(directionRespFinal);
            String nor = getNorFromDirectionAndMinistere(ministereNode, directionNode);
            if (directionNode != null && directionNode.getType() != OrganigrammeType.DIRECTION) {
                context
                    .getMessageQueue()
                    .addErrorToQueue(ResourceHelper.getString("epg.dossier.creation.direction.pas.direction"));
            }

            if (StringUtils.isEmpty(nor) && directionNode != null) {
                // Détermine les lettres de NOR de la direction
                norDirection = directionNode.getNorDirectionForMinistereId(ministereRespFinal);
                if (
                    "".equals(norDirection) &&
                    directionNode.getUniteStructurelleParentList().size() == 1 &&
                    OrganigrammeType.DIRECTION == directionNode.getUniteStructurelleParentList().get(0).getType()
                ) {
                    // On a une direction sous une direction et on n'a pas
                    // réussi à trouver le NOR
                    // On prend donc le NOR de la direction parente
                    norDirection =
                        directionNode
                            .getUniteStructurelleParentList()
                            .get(0)
                            .getNorDirectionForMinistereId(ministereRespFinal);
                } else {
                    norDirection = nor;
                }
            } else {
                norDirection = nor;
            }
        }

        checkMinistereAndDirectionDroits(context, ministereRespFinal, directionRespFinal, tableReferenceMinisterePrm);
        if (context.getMessageQueue().hasMessageInQueue()) {
            return "";
        }

        String dossierId = performCreationDossier(
            context,
            estRectificatif,
            oldDossier,
            typeActeId,
            norMinistere,
            norDirection,
            ministereRespFinal,
            directionRespFinal
        );

        if (StringUtils.isNotBlank(dossierId)) {
            SSServiceLocator
                .getSsProfilUtilisateurService()
                .addDossierToListDerniersDossierIntervention(context.getSession(), dossierId);
        }

        return dossierId;
    }

    private void checkMinistereAndDirectionDroits(
        SpecificContext context,
        String ministereRespFinal,
        String directionRespFinal,
        String tableRefMinisterePrm
    ) {
        STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();
        STUsAndDirectionService stUsAndDirectionService = STServiceLocator.getSTUsAndDirectionService();

        NuxeoPrincipal principal = context.getSession().getPrincipal();
        EntiteNode ministereNode = stMinisteresService.getEntiteNode(ministereRespFinal);
        boolean isProfilSgg = principal.isMemberOf(STBaseFunctionConstant.PROFIL_SGG);
        if (
            !(
                isProfilSgg ||
                Objects.equals(ministereRespFinal, tableRefMinisterePrm) ||
                stMinisteresService.isUserFromMinistere(ministereRespFinal, principal.getName())
            )
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString("epg.dossier.creation.ministere.denied.error", ministereNode.getLabel())
                );
        }

        UniteStructurelleNode directionNode = stUsAndDirectionService.getUniteStructurelleNode(directionRespFinal);
        EntiteNode tableRefMinisterePrmNode = stMinisteresService.getEntiteNode(tableRefMinisterePrm);
        if (
            !(
                isProfilSgg ||
                stUsAndDirectionService.isDirectionFromMinistere(directionNode, tableRefMinisterePrmNode) ||
                stUsAndDirectionService.isDirectionFromMinistere(directionNode, ministereNode)
            )
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString(
                        "epg.dossier.creation.direction.denied.error",
                        directionNode.getLabel(),
                        ministereNode.getLabel()
                    )
                );
        }
    }

    private String performCreationDossier(
        SpecificContext context,
        boolean estRectificatif,
        Dossier oldDossier,
        String typeActeId,
        String norMinistere,
        String norDirection,
        String ministereRespFinal,
        String directionRespFinal
    ) {
        CoreSession session = context.getSession();
        SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        DossierCreationForm dossierCreationForm = context.getFromContextData(EpgContextDataKey.DOSSIER_CREATION_FORM);
        String posteId = getPosteId(principal, dossierCreationForm, norMinistere, norDirection);

        Dossier dossier = null;
        String nature = null;
        String norDossierCopieFdr;
        String numeroNorDossier;

        // Crée le dossier
        try {
            NORService norService = SolonEpgServiceLocator.getNORService();
            if (estRectificatif) {
                numeroNorDossier = norService.createRectificatifNOR(oldDossier);
            } else {
                // Détermine les lettres de NOR du type d'acte
                final ActeService acteService = SolonEpgServiceLocator.getActeService();

                TypeActe acte = acteService.getActe(typeActeId);
                String norActe = acte.getNor();
                // si le type d'acte est de type individuel, l'acte est non
                // réglementaire
                if (acteService.isNonReglementaire(typeActeId)) {
                    nature = VocabularyConstants.NATURE_NON_REGLEMENTAIRE;
                } else if (acteService.isReglementaire(typeActeId)) {
                    nature = VocabularyConstants.NATURE_REGLEMENTAIRE;
                }

                // Vérification des arguments
                if (norActe == null || norActe.length() != 1) {
                    context
                        .getMessageQueue()
                        .addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.nor.acte.invalide"));
                }
                if (norMinistere == null || norMinistere.length() != 3) {
                    context
                        .getMessageQueue()
                        .addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.nor.ministere.invalide"));
                }
                if (norDirection == null || norDirection.length() != 1) {
                    context
                        .getMessageQueue()
                        .addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.nor.direction.invalide"));
                }

                if (context.getMessageQueue().hasMessageInQueue()) {
                    return "";
                }

                // Attribution du NOR
                numeroNorDossier = norService.createNOR(norActe, norMinistere, norDirection);
            }

            if (context.getMessageQueue().hasMessageInQueue()) {
                return "";
            }

            // Création du dossier
            DocumentModel dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
            DublincoreSchemaUtils.setTitle(dossierDoc, numeroNorDossier);
            dossier = dossierDoc.getAdapter(Dossier.class);

            // Ajout des informations de création information du dossier
            dossier.setNumeroNor(numeroNorDossier);
            dossier.setTypeActe(typeActeId);
            dossier.setMinistereResp(ministereRespFinal);
            dossier.setDirectionResp(directionRespFinal);
            dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_AUCUN);
            if (!estRectificatif) {
                // Ajout des informations de création
                dossier.setCategorieActe(nature);
                dossier.setApplicationLoi(toTranspositionsContainer(dossierCreationForm.getMesuresApplicationLoi()));
                dossier.setTranspositionOrdonnance(
                    toTranspositionsContainer(dossierCreationForm.getMesuresApplicationOrdonnance())
                );
                dossier.setTranspositionDirective(
                    toTranspositionsContainer(dossierCreationForm.getMesuresTransposition())
                );

                OrdonnanceForm ordonnance = dossierCreationForm.getOrdonnance();
                boolean hasOrdonnance = ordonnance != null && StringUtils.isNotBlank(ordonnance.getReference());
                dossier.setDispositionHabilitation(hasOrdonnance);
                if (hasOrdonnance) {
                    dossier.setHabilitationRefLoi(ordonnance.getReference());
                    dossier.setHabilitationNumeroArticles(ordonnance.getNumeroArticle());
                    dossier.setHabilitationCommentaire(ordonnance.getCommentaire());
                    dossier.setHabilitationNumeroOrdre(String.valueOf(ordonnance.getNumeroOrdre()));
                }
            }

            // Create dossier à lancer avant la maj du pan pour avoir l'id du
            // dossier
            norDossierCopieFdr = dossierCreationForm.getNorCopie();
            DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            dossier = createDossier(session, dossierService, dossier, norDossierCopieFdr, posteId);

            // on regarde si les modifications ont eu lieu sur les applications
            // des loi, les transpositions ou les
            // ordonances
            // afin de mettre à jour l'historique des maj ministérielles
            final HistoriqueMajMinisterielleService historiqueMajService = SolonEpgServiceLocator.getHistoriqueMajMinisterielleService();
            historiqueMajService.registerMajDossier(session, dossier.getDocument());

            if (estRectificatif) {
                // met à jour le nombre de rectification du dossier d'origine
                if (oldDossier.getNbDossierRectifie() == null) {
                    oldDossier.setNbDossierRectifie(1L);
                } else {
                    oldDossier.setNbDossierRectifie(oldDossier.getNbDossierRectifie() + 1);
                }
                oldDossier.save(session);
            }
        } catch (NuxeoException e) {
            context.getMessageQueue().addWarnToQueue(e.getMessage());
            return "";
        }

        // Event de rattachement de l'activite normative (post commit)
        EventProducer eventProducer = STServiceLocator.getEventProducer();
        Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(
            SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM,
            dossier.getDocument().getId()
        );
        InlineEventContext inlineEventContext = new InlineEventContext(session, principal, eventProperties);
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT));

        context.setCurrentDocument(dossier.getDocument());
        lockFolderIfPossible(context);
        generateSuccessMessageCreation(context, typeActeId, norDossierCopieFdr, dossier);
        return dossier.getDocument().getId();
    }

    private void lockFolderIfPossible(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        boolean isDossier = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType());

        DossierLockActionService dossierLockActionService = STActionsServiceLocator.getDossierLockActionService();
        boolean isDossierLockable = dossierLockActionService.getCanLockCurrentDossier(context);

        CoreSession session = context.getSession();
        NuxeoPrincipal principal = session.getPrincipal();
        boolean isDossierLoadedOrUpdater =
            SSActionsServiceLocator.getSSCorbeilleActionService().isDossierLoadedInCorbeille(context) ||
            principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER);

        STLockActionService lockActionService = STActionsServiceLocator.getSTLockActionService();
        boolean currentDocIsLockActionnableByCurrentUser = lockActionService.currentDocIsLockActionnableByCurrentUser(
            session,
            dossierDoc,
            principal
        );

        if (isDossier && isDossierLockable && isDossierLoadedOrUpdater && currentDocIsLockActionnableByCurrentUser) {
            dossierLockActionService.lockCurrentDossier(context);
        }
    }

    private void generateSuccessMessageCreation(
        SpecificContext context,
        String typeActeId,
        String norDossierCopieFdr,
        Dossier dossier
    ) {
        LOGGER.info(STLogEnumImpl.CREATE_DOSSIER_TEC, "Création d'un dossier de type : " + typeActeId);
        // Affichage du message de création
        if (StringUtils.isNotEmpty(norDossierCopieFdr)) {
            context
                .getMessageQueue()
                .addInfoToQueue(
                    ResourceHelper.getString(
                        "feedback.solonepg.dossier.creation.from.fdrDossier",
                        dossier.getNumeroNor(),
                        norDossierCopieFdr
                    )
                );
        } else {
            context
                .getMessageQueue()
                .addInfoToQueue(ResourceHelper.getString("feedback.solonepg.dossier.creation", dossier.getNumeroNor()));
        }
    }

    private String getPosteId(
        SSPrincipal principal,
        DossierCreationForm dossierCreationForm,
        String norMinistere,
        String norDirection
    ) {
        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        // Si un poste est sélectionné manuellement, on affecte ce poste
        String posteId = dossierCreationForm.getPoste();
        if (StringUtils.isEmpty(posteId)) {
            // Tente d'affecter le poste automatiquement
            final Set<String> posteIdSet = principal.getPosteIdSet();
            posteId = dossierDistributionService.getPosteForNor(posteIdSet, norMinistere, norDirection);
            if (StringUtils.isEmpty(posteId)) {
                throw new RequiredPosteSelection();
            }
        }

        return posteId;
    }

    private TranspositionsContainer toTranspositionsContainer(
        Collection<? extends DossierTranspositionImmutable> transpositionsForm
    ) {
        return transpositionsForm
            .stream()
            .map(DossierTranspositionImpl::new)
            .map(DossierTransposition.class::cast)
            .collect(Collectors.collectingAndThen(Collectors.toList(), TranspositionsContainerImpl::new));
    }

    private boolean isRequiredFieldsFilled(SpecificContext context, DossierCreationForm dossierCreationForm) {
        // Cas du rectificatif
        TypeActeService typeActeService = SolonEpgServiceLocator.getTypeActeService();
        String typeActe = dossierCreationForm.getTypeActe();
        String typeActeId = StringUtils.isNotBlank(typeActe) ? typeActeService.getId(typeActe) : "";
        boolean estRectificatif = TypeActeConstants.TYPE_ACTE_RECTIFICATIF.equals(typeActeId);
        if (estRectificatif) {
            return true;
        }

        String norCopie = dossierCreationForm.getNorCopie();
        if (StringUtils.isNotEmpty(norCopie) && !isNorFdrCopieValide(context, norCopie)) {
            return false;
        }

        // Contrôle des champs obligatoire : titre de l'acte / entite /
        // direction
        if (
            StringUtils.isEmpty(typeActe) ||
            StringUtils.isBlank(dossierCreationForm.getNorPrm()) &&
            (
                StringUtils.isEmpty(dossierCreationForm.getEntite()) ||
                StringUtils.isEmpty(dossierCreationForm.getDirection())
            )
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.choix.direction.error.message"));
            return false;
        }
        return true;
    }

    private boolean isNorFdrCopieValide(SpecificContext context, String norDossierCopieFdr) {
        NORService norService = SolonEpgServiceLocator.getNORService();
        boolean isNorFdrCopieValide =
            norService.getDossierFromNORWithACL(context.getSession(), norDossierCopieFdr) != null;
        if (!isNorFdrCopieValide) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.copier.fdr.error.message"));
        }

        return isNorFdrCopieValide;
    }

    private DocumentModel getDossierARectifier(SpecificContext context, String numeroNor) {
        if (StringUtils.isBlank(numeroNor)) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.nor.rectificatif.error.message"));
            return null;
        }

        // mise en forme : on enleve les espace et on met en majuscule le NOR
        // saisi par l'utilisateur
        numeroNor = numeroNor.replaceAll(" ", "").toUpperCase();

        // on récupère le dossier dont le nor existe et dont le type d'acte
        // n'est pas rectificatif, dont le statut est
        // publié
        NORService nORService = SolonEpgServiceLocator.getNORService();
        CoreSession session = context.getSession();
        DocumentModel dossier = nORService.getDossierFromNOR(
            session,
            numeroNor,
            STSchemaConstant.COMMON_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_SCHEMA
        );

        String erreurNumeroNor = "";
        if (dossier == null) {
            erreurNumeroNor = ResourceHelper.getString("epg.dossiers.creation.inexistant.dossier", numeroNor);
        } else if (!session.hasPermission(dossier.getRef(), SecurityConstants.READ)) {
            erreurNumeroNor = ResourceHelper.getString("epg.dossiers.creation.invalid.perms", numeroNor);
        } else {
            Dossier dossierDoc = dossier.getAdapter(Dossier.class);
            Long nbDossierRectifie = dossierDoc.getNbDossierRectifie();
            if (!dossierDoc.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                erreurNumeroNor = ResourceHelper.getString("epg.dossiers.creation.dossier.non.publie", numeroNor);
            } else if (dossierDoc.getTypeActe().equals(TypeActeConstants.TYPE_ACTE_RECTIFICATIF)) {
                erreurNumeroNor = ResourceHelper.getString("epg.dossiers.creation.dossier.rectificatif", numeroNor);
            } else if (nbDossierRectifie != null && nbDossierRectifie == 3L) {
                erreurNumeroNor =
                    ResourceHelper.getString("epg.dossiers.creation.dossier.trop.rectificatif", numeroNor);
            } else if (nbDossierRectifie != null && nbDossierRectifie > 0L) {
                // le dernier rectificatif du dossier doit être publié
                Dossier rectificatifDoc = nORService.getRectificatifFromNORAndNumRect(
                    session,
                    numeroNor,
                    nbDossierRectifie
                );
                if (rectificatifDoc != null && !rectificatifDoc.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                    erreurNumeroNor =
                        ResourceHelper.getString("epg.dossiers.creation.dossier.rectificatif.non.publie", numeroNor);
                }
            }
        }
        if (!erreurNumeroNor.isEmpty()) {
            context.getMessageQueue().addErrorToQueue(erreurNumeroNor);
            return null;
        }

        return dossier;
    }

    private String getNorFromDirectionAndMinistere(EntiteNode ministereNode, UniteStructurelleNode directionNode) {
        String nor = "";
        if (ministereNode != null && directionNode != null) {
            List<NorDirection> norDirections = directionNode.getNorDirectionList();
            if (norDirections.size() == 1) {
                nor = norDirections.get(0).getNor();
            } else {
                List<NorDirection> norMinistere = norDirections
                    .stream()
                    .filter(dn -> ministereNode.getId().equals(dn.getId()))
                    .collect(Collectors.toList());
                if (CollectionUtils.size(norMinistere) == 1) {
                    nor = norMinistere.get(0).getNor();
                }
            }
        }

        return nor;
    }

    private Dossier createDossier(
        CoreSession session,
        DossierService dossierService,
        Dossier dossier,
        String norDossierCopieFdr,
        String posteId
    ) {
        MailboxService mailboxService = STServiceLocator.getMailboxService();
        Mailbox mailbox = mailboxService.getUserPersonalMailboxUFNXQL(session, session.getPrincipal().getName());
        return dossierService.createDossier(session, dossier.getDocument(), posteId, mailbox, norDossierCopieFdr);
    }
}
