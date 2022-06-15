package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EditionEtapeSqueletteDTO;
import fr.dila.solonepg.ui.bean.action.EpgRoutingActionDTO;
import fr.dila.solonepg.ui.bean.squelette.EtapeSqueletteDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgEtapeValidationStatusEnum;
import fr.dila.solonepg.ui.services.EpgFeuilleRouteUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgDocumentRoutingActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.ui.bean.EditionEtapeFdrDTO;
import fr.dila.ss.ui.bean.fdr.CreationEtapeDTO;
import fr.dila.ss.ui.bean.fdr.CreationEtapeLineDTO;
import fr.dila.ss.ui.bean.fdr.EtapeDTO;
import fr.dila.ss.ui.enums.EtapeValidationStatus;
import fr.dila.ss.ui.enums.FeuilleRouteEtapeOrder;
import fr.dila.ss.ui.enums.SSActionEnum;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.SSRoutingTaskFilterService;
import fr.dila.ss.ui.services.impl.SSFeuilleRouteUIServiceImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgFeuilleRouteUIServiceImpl extends SSFeuilleRouteUIServiceImpl implements EpgFeuilleRouteUIService {

    @Override
    protected EtapeValidationStatus getValidationStatut(final String validationStatus, final String typeEtape) {
        return EpgEtapeValidationStatusEnum.getEnumFromKey(validationStatus, typeEtape);
    }

    @Override
    public void addEtapes(SpecificContext context) {
        CreationEtapeDTO creationEtapeDTO = context.getFromContextData(SSContextDataKey.CREATION_ETAPE_DTO);
        CoreSession session = context.getSession();
        List<CreationEtapeLineDTO> lineEtapes = creationEtapeDTO.getLines();
        checkStepRight(session, lineEtapes);
        checkAllStepAreValidWithPoste(session, lineEtapes);
        super.addEtapes(context);
    }

    @Override
    public EtapeDTO saveEtape(SpecificContext context) {
        EditionEtapeFdrDTO editstep = context.getFromContextData(SSContextDataKey.EDITION_ETAPE_FDR_DTO);
        if (
            !EpgUIServiceLocator.getEpgRoutingTaskFilterService().accept(context.getSession(), editstep.getTypeEtape())
        ) {
            throw new STValidationException("epg.etape.fdr.etape.droits.message");
        }
        checkStepIsValidWithPoste(
            editstep.getDestinataire(),
            editstep.getTypeEtape(),
            getMinistereCeId(context.getSession())
        );
        return super.saveEtape(context);
    }

    @Override
    public EtapeSqueletteDTO saveEtapeSquelette(SpecificContext context) {
        EditionEtapeSqueletteDTO editstep = context.getFromContextData(EpgContextDataKey.EDIT_ETAPE_SQUELETTE_DTO);
        if (
            !EpgUIServiceLocator.getEpgRoutingTaskFilterService().accept(context.getSession(), editstep.getTypeEtape())
        ) {
            throw new STValidationException("epg.etape.fdr.etape.droits.message");
        }
        checkStepIsValidWithPoste(
            editstep.getDestinataire(),
            editstep.getTypeEtape(),
            getMinistereCeId(context.getSession())
        );

        CoreSession session = context.getSession();
        EtapeSqueletteDTO etape = new EtapeSqueletteDTO();
        etape.setAction(editstep.getTypeEtape());
        String mailboxId = StringUtils.isNotEmpty(editstep.getDestinataire())
            ? "poste-" + editstep.getDestinataire()
            : null;
        etape.setMailBoxId(mailboxId);
        etape.setPoste(getMailboxTitleFromId(mailboxId, session));
        etape.setMinistere(getMinisteresEditionFromMailboxId(mailboxId));
        etape.setDeadLine(NumberUtils.isParsable(editstep.getEcheance()) ? Long.parseLong(editstep.getEcheance()) : 0);
        etape.setValAuto(editstep.getValAuto());
        etape.setObligatoire(editstep.getObligatoire());
        etape.setTypeDestinataire(SqueletteStepTypeDestinataire.fromValue(editstep.getTypeDestinataire()));

        DocumentModel etapeDoc = session.getDocument(new IdRef(editstep.getStepId()));
        etapeDoc = convertStepSqueletteToDoc(etape, etapeDoc);
        session.saveDocument(etapeDoc);

        context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("fdr.form.updatestep.success"));
        SSRouteStep routeStep = etapeDoc.getAdapter(SSRouteStep.class);
        FeuilleRoute feuilleRoute = routeStep.getFeuilleRoute(session);
        context.setCurrentDocument(etapeDoc);
        context.putInContextData(SSContextDataKey.FEUILLE_ROUTE, feuilleRoute.getDocument());
        initStepFolderActionsDTO(context);
        return (EtapeSqueletteDTO) createStepDTO(
            context,
            feuilleRoute.getDocument(),
            editstep.getTotalNbLevel(),
            true,
            new ArrayList<>()
        );
    }

    private DocumentModel convertStepSqueletteToDoc(EtapeSqueletteDTO etape, DocumentModel etapeDoc) {
        SolonEpgRouteStep step = etapeDoc.getAdapter(SolonEpgRouteStep.class);
        step.setType(etape.getAction());
        step.setPosteLabel(etape.getPoste());
        step.setMinistereLabel(etape.getMinistere());
        step.setDistributionMailboxId(etape.getMailBoxId());
        step.setDeadLine(etape.getDeadLine());
        step.setDueDate(etape.getEcheanceDate());
        step.setDateFinEtape(etape.getTraiteDate());
        step.setObligatoireMinistere(EtapeDTO.MINISTERE.equals(etape.getObligatoire()));
        step.setObligatoireSGG(EtapeDTO.SGG.equals(etape.getObligatoire()));
        step.setAutomaticValidation(etape.isValAuto());
        step.setValidationUserLabel(etape.getUtilisateur());
        step.setTypeDestinataire(etape.getTypeDestinataire());
        return step.getDocument();
    }

    private String getMinistereCeId(CoreSession session) {
        DocumentModel tableReferenceDoc = SolonEpgServiceLocator.getTableReferenceService().getTableReference(session);
        TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
        return tableReference.getMinistereCE();
    }

    private void checkStepRight(CoreSession session, List<CreationEtapeLineDTO> lineEtapes) {
        SSRoutingTaskFilterService ssRoutingTaskFilterService = EpgUIServiceLocator.getEpgRoutingTaskFilterService();
        if (
            CollectionUtils.isNotEmpty(lineEtapes) &&
            lineEtapes.stream().anyMatch(line -> !ssRoutingTaskFilterService.accept(session, line.getTypeEtape()))
        ) {
            throw new STValidationException("epg.etape.fdr.etape.droits.message");
        }
    }

    private void checkAllStepAreValidWithPoste(CoreSession session, List<CreationEtapeLineDTO> lineEtapes) {
        if (CollectionUtils.isNotEmpty(lineEtapes)) {
            lineEtapes.forEach(
                line ->
                    checkStepIsValidWithPoste(line.getDestinataire(), line.getTypeEtape(), getMinistereCeId(session))
            );
        }
    }

    private void checkStepIsValidWithPoste(String posteId, String typeEtape, String ministereCeId) {
        List<EntiteNode> entites = STServiceLocator.getSTMinisteresService().getMinistereParentFromPoste(posteId);
        if (
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(typeEtape) &&
            entites.stream().noneMatch(entite -> entite.getId().equals(ministereCeId))
        ) {
            throw new STValidationException("epg.etape.fdr.poste.incompatible.message");
        } else if (
            !VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(typeEtape) &&
            entites.stream().anyMatch(entite -> entite.getId().equals(ministereCeId))
        ) {
            throw new STValidationException("epg.etape.fdr.etape.incompatible.message");
        }
    }

    // Méthode surchargé dans EPG pour gérer le cas des Squelettes
    @Override
    public EtapeDTO getEtapeDTO(SpecificContext context) {
        DocumentModel stepDoc = context.getCurrentDocument();
        if (SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(stepDoc.getType())) {
            return super.getEtapeDTO(context);
        } else {
            SolonEpgRouteStep step = stepDoc.getAdapter(SolonEpgRouteStep.class);
            EtapeSqueletteDTO etapeDto = docToEtapeSqueletteDto(stepDoc);
            String mailboxId = step.getDistributionMailboxId();
            etapeDto.setPosteId(StringUtils.isNotEmpty(mailboxId) ? mailboxId.substring(6) : "");
            etapeDto.setPoste(getMailboxTitleFromId(mailboxId, context.getSession()));
            return etapeDto;
        }
    }

    // Méthode surchargé dans EPG pour gérer le cas des Squelettes
    @Override
    protected EtapeDTO docToEtapeDto(DocumentModel doc) {
        if (SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(doc.getType())) {
            return super.docToEtapeDto(doc);
        } else {
            return docToEtapeSqueletteDto(doc);
        }
    }

    private EtapeSqueletteDTO docToEtapeSqueletteDto(DocumentModel doc) {
        SolonEpgRouteStep step = doc.getAdapter(SolonEpgRouteStep.class);
        EtapeSqueletteDTO dto = new EtapeSqueletteDTO();
        dto.setId(doc.getId());
        dto.setAction(step.getType());
        dto.setPoste(step.getPosteLabel());
        dto.setMinistere(step.getMinistereLabel());
        dto.setMailBoxId(step.getDistributionMailboxId());
        dto.setDeadLine(step.getDeadLine());
        dto.setEcheanceDate(step.getDueDate());
        dto.setTraiteDate(step.getDateFinEtape());
        dto.setObligatoire(step.isObligatoireMinistere() ? EtapeDTO.MINISTERE : "");
        dto.setObligatoire(step.isObligatoireSGG() ? EtapeDTO.SGG : "");
        dto.setValAuto(step.isAutomaticValidation());
        dto.setUtilisateur(step.getValidationUserLabel());
        dto.setTypeDestinataire(step.getTypeDestinataire());
        return dto;
    }

    @Override
    protected void initEtapeActionsDTO(SpecificContext context, DocumentModel feuilleRouteDoc, DocumentModel etapeDoc) {
        EpgRoutingActionDTO routingActionDTO = new EpgRoutingActionDTO();
        routingActionDTO.setIsSqueletteFeuilleRoute(
            feuilleRouteDoc.getAdapter(SolonEpgFeuilleRoute.class).isSqueletteFeuilleRoute()
        );
        context.putInContextData(SSContextDataKey.ETAPE_ACTIONS, routingActionDTO);
        super.initEtapeActionsDTO(context, feuilleRouteDoc, etapeDoc);
    }

    @Override
    protected boolean canAddEtapes(
        SpecificContext context,
        FeuilleRouteEtapeOrder order,
        DocumentModel feuilleRouteDoc,
        DocumentModel elementDoc
    ) {
        boolean canAdd = false;
        context.setCurrentDocument(elementDoc);
        if (SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(elementDoc.getType())) {
            initEtapeActionsDTO(context, feuilleRouteDoc, elementDoc);
            if (order == FeuilleRouteEtapeOrder.AFTER) {
                canAdd = context.getAction(SSActionEnum.ADD_STEP_AFTER) != null;
            } else if (order == FeuilleRouteEtapeOrder.BEFORE) {
                canAdd = context.getAction(SSActionEnum.ADD_STEP_BEFORE) != null;
            }
            return canAdd;
        } else {
            return super.canAddEtapes(context, order, feuilleRouteDoc, elementDoc);
        }
    }

    @Override
    protected void saveEtapes(SpecificContext context, DocumentModel feuilleRouteDoc) {
        EpgDocumentRoutingActionService documentRoutingActionService = SolonEpgActionServiceLocator.getEpgDocumentRoutingActionService();
        // Si l'élément est un modele alors on utilise la fdr
        // Sinon on utilise le dossier pour la journalisation
        if (feuilleRouteDoc.getAdapter(SolonEpgFeuilleRoute.class).isSqueletteFeuilleRoute()) {
            context.setCurrentDocument(feuilleRouteDoc);
            documentRoutingActionService.saveRouteElementMass(context);
        } else {
            super.saveEtapes(context, feuilleRouteDoc);
        }
    }

    @Override
    public List<EtapeDTO> getEtapesPourEpreuve(SpecificContext context) {
        CoreSession session = context.getSession();
        final EpgDocumentRoutingActionService documentRoutingActionService = SolonEpgActionServiceLocator.getEpgDocumentRoutingActionService();
        final TableReferenceService tdrService = SolonEpgServiceLocator.getTableReferenceService();
        String posteDanId = tdrService.getPosteDanId(session);
        String postePublicationId = tdrService.getPostePublicationId(session);
        String posteBdcId = documentRoutingActionService.getPosteBdcIdFromRoute(session, context.getCurrentDocument());

        List<EtapeDTO> etapes = new ArrayList<>();
        etapes.add(addEtapeEpreuve(postePublicationId, VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE, true));
        etapes.add(addEtapeEpreuve(posteDanId, VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION, true));
        etapes.add(addEtapeEpreuve(posteBdcId, VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER, false));
        etapes.add(addEtapeEpreuve(posteDanId, VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION, true));

        return etapes;
    }

    private EtapeDTO addEtapeEpreuve(String posteId, String typeEtape, boolean isObligatoireSGG) {
        EtapeDTO etape = new EtapeDTO();
        if (StringUtils.isNotBlank(posteId)) {
            etape.setPosteId(posteId);
            etape.setMapPoste(
                Collections.singletonMap(
                    posteId,
                    STServiceLocator
                        .getOrganigrammeService()
                        .getOrganigrammeNodeById(posteId, OrganigrammeType.POSTE)
                        .getLabel()
                )
            );
        }
        if (isObligatoireSGG) {
            etape.setObligatoire(EtapeDTO.SGG);
        }
        etape.setAction(typeEtape);

        return etape;
    }
}
