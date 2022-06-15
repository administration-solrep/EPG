package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;

import com.google.common.collect.Lists;
import fr.dila.cm.cases.CaseLifeCycleConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgFdrDTO;
import fr.dila.solonepg.ui.bean.EpgSqueletteActionDTO;
import fr.dila.solonepg.ui.bean.EpgSqueletteList;
import fr.dila.solonepg.ui.bean.SqueletteDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSqueletteUIService;
import fr.dila.solonepg.ui.services.actions.EpgModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgSqueletteListForm;
import fr.dila.solonepg.ui.th.model.bean.SqueletteFdrForm;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.bean.FdrDTO;
import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.services.actions.impl.ModeleFeuilleRouteActionServiceImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.contentview.UfnxqlPageDocumentProvider;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteAlreadyLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteNotLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;

public class EpgSqueletteUIServiceImpl implements EpgSqueletteUIService {
    private static final STLogger LOG = STLogFactory.getLog(EpgSqueletteUIServiceImpl.class);

    @Override
    public EpgSqueletteList getSquelettes(SpecificContext context) {
        EpgSqueletteList listResult = new EpgSqueletteList();
        EpgSqueletteListForm listForm = context.getFromContextData(EpgContextDataKey.SQUELETTE_LIST_FORM);

        // Navigue vers la racine des squelettes de feuilles de route
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        DocumentModel currentDocument = feuilleRouteModelService.getFeuilleRouteSqueletteFolder(context.getSession());
        context.setCurrentDocument(currentDocument);

        UfnxqlPageDocumentProvider provider = buildProvider(context, listForm);

        List<DocumentModel> docs = provider.getCurrentPage();

        listResult.setListeSquelettes(
            docs.stream().map(doc -> this.getFeuilleRouteDTOFromDoc(doc, context)).collect(Collectors.toList())
        );

        listResult.setNbTotal((int) provider.getResultsCount());
        listResult.buildColonnes(listForm);
        listResult.setHasSelect(true);
        listResult.setHasPagination(true);
        listForm.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));

        return listResult;
    }

    @Override
    public SqueletteFdrForm getSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel squeletteDoc = context.getCurrentDocument();

        if (
            !LockUtils.isLocked(session, squeletteDoc.getRef()) &&
            FeuilleRouteElement.ElementLifeCycleState.draft.name().equals(squeletteDoc.getCurrentLifeCycleState())
        ) {
            session.setLock(squeletteDoc.getRef());
        }
        return convertDocToSquelette(context, squeletteDoc);
    }

    @Override
    public SqueletteFdrForm createSquelette(SpecificContext context) {
        SqueletteFdrForm form = context.getFromContextData(EpgContextDataKey.SQUELETTE_FDR_FORM);
        CoreSession session = context.getSession();

        if (canUserCreateSquelette(context)) {
            DocumentModel squeletteDoc = initSquelette(session, form);
            MapDoc2Bean.beanToDoc(form, squeletteDoc);
            context.setCurrentDocument(squeletteDoc);
            squeletteDoc = session.createDocument(squeletteDoc);
            form.setId(squeletteDoc.getId());
            context
                .getMessageQueue()
                .addSuccessToQueue(ResourceHelper.getString("admin.squelette.creation.message.success"));
        }
        return form;
    }

    @Override
    public SqueletteFdrForm updateSquelette(SpecificContext context) {
        EpgModeleFeuilleRouteActionService modeleAction = SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService();
        SqueletteFdrForm form = context.getFromContextData(EpgContextDataKey.SQUELETTE_FDR_FORM);
        CoreSession session = context.getSession();

        if (canUserModifySquelette(context)) {
            DocumentModel squeletteDoc = session.getDocument(new IdRef(form.getId()));
            MapDoc2Bean.beanToDoc(form, squeletteDoc);
            context.setCurrentDocument(squeletteDoc);
            modeleAction.updateSquelette(context);
            if (context.getMessageQueue().getWarnQueue().isEmpty()) {
                context
                    .getMessageQueue()
                    .addSuccessToQueue(ResourceHelper.getString("admin.squelette.modification.message.success"));
            }
        } else {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("admin.modele.message.error.right"));
        }
        return form;
    }

    @Override
    public void deleteSquelette(SpecificContext context) {
        if (SSActionsServiceLocator.getModeleFeuilleRouteActionService().canUserDeleteRoute(context)) {
            context.getSession().removeDocument(context.getCurrentDocument().getRef());
            context.getMessageQueue().addToastSuccess("admin.squelette.action.supprimer.succces");
        }
    }

    private UfnxqlPageDocumentProvider buildProvider(SpecificContext context, EpgSqueletteListForm listForm) {
        UfnxqlPageDocumentProvider provider = listForm.getPageProvider(
            context.getSession(),
            "liste_squelettes",
            "f.",
            null
        );

        Map<String, Serializable> props = provider.getProperties();
        props.put("typeDocument", SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE);
        props.put("parameters", Lists.newArrayList(context.getCurrentDocument().getId()));
        provider.setProperties(props);

        return provider;
    }

    private SqueletteDTO getFeuilleRouteDTOFromDoc(DocumentModel doc, SpecificContext context) {
        context.setCurrentDocument(doc);
        FeuilleRouteDTO fdrDto = SSActionsServiceLocator
            .getRechercheModeleFeuilleRouteActionService()
            .getFeuilleRouteDTOFromDoc(doc, context.getSession());

        SolonEpgFeuilleRoute squelette = doc.getAdapter(SolonEpgFeuilleRoute.class);
        String typeActe = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, squelette.getTypeActe());

        SqueletteDTO dto = new SqueletteDTO(
            typeActe,
            fdrDto.getId(),
            fdrDto.getEtat(),
            fdrDto.getIntitule(),
            fdrDto.getMinistere(),
            fdrDto.getAuteur(),
            fdrDto.getDerniereModif(),
            fdrDto.getlock(),
            fdrDto.getlockOwner()
        );

        initSqueletteAction(context);
        dto.setActions(new ArrayList<>(context.getActions(EpgActionCategory.SQUELETTE_ACTIONS)));

        return dto;
    }

    private boolean canUserCreateSquelette(SpecificContext context) {
        return context.getAction(EpgActionEnum.TAB_SQUELETTE_CREATION) != null;
    }

    private boolean canUserModifySquelette(SpecificContext context) {
        return context
            .getSession()
            .getPrincipal()
            .isMemberOf(SolonEpgBaseFunctionConstant.FEUILLE_DE_ROUTE_SQUELETTE_UDPATER);
    }

    private DocumentModel initSquelette(CoreSession session, SqueletteFdrForm form) {
        DocumentModel squelette = session.createDocumentModel(
            SolonEpgConstant.SQUELETTE_FOLDER_PATH,
            form.getIntitule().replace("/", ""),
            SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE
        );
        DublincoreSchemaUtils.setTitle(squelette, form.getIntitule());
        // Initialiser le numéro de la fdr
        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        UIDSequencer sequencer = uidGeneratorService.getSequencer();
        squelette
            .getAdapter(SolonEpgFeuilleRoute.class)
            .setNumero(sequencer.getNextLong("SOLON_EPG_FDR_SQUELETTE") + 1);
        return squelette;
    }

    private SqueletteFdrForm convertDocToSquelette(SpecificContext context, DocumentModel squeletteDoc) {
        CoreSession session = context.getSession();
        SqueletteFdrForm form = new SqueletteFdrForm();
        SolonEpgFeuilleRoute squelette = squeletteDoc.getAdapter(SolonEpgFeuilleRoute.class);
        form.setId(squeletteDoc.getId());
        form.setIntitule(squelette.getTitle());
        form.setDescription(squelette.getDescription());
        form.setEtat(StatutModeleFDR.getStatutFromDoc(squeletteDoc));
        form.setTypeActe(squelette.getTypeActe());
        VocabularyService vocService = ServiceUtil.getRequiredService(VocabularyService.class);
        form.setTypeActeLibelle(
            vocService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, squelette.getTypeActe())
        );
        form.setIsLock(LockUtils.isLocked(session, squeletteDoc.getRef()));
        form.setIsLockByCurrentUser(LockUtils.isLockedByCurrentUser(session, squeletteDoc.getRef()));
        form.setSqueletteDto(getFeuileRouteSquelette(context));
        return form;
    }

    private FdrDTO getFeuileRouteSquelette(SpecificContext context) {
        context.putInContextData(STContextDataKey.ID, context.getCurrentDocument().getId());

        EpgFdrDTO dto = new EpgFdrDTO();
        dto.setTable(SSUIServiceLocator.getSSFeuilleRouteUIService().getFeuilleRouteDTO(context));
        dto.buildColonnesFdrSquelette();

        dto.setTabActions(context.getActions(SSActionCategory.MODELE_FDR_STEP_ACTIONS));
        return dto;
    }

    public boolean canInvalidateRouteSquelette(SpecificContext context) {
        return context.getAction(EpgActionEnum.TAB_SQUELETTE_CREATION) != null;
    }

    @Override
    public void invalidateRouteSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel doc = context.getCurrentDocument();
        SSFeuilleRoute currentRouteModel = SSActionsServiceLocator
            .getModeleFeuilleRouteActionService()
            .getRelatedRoute(session, doc);

        final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();
        try {
            documentRoutingService.lockDocumentRoute(currentRouteModel, session);
        } catch (FeuilleRouteAlreadyLockedException e) {
            LOG.error(session, STLogEnumImpl.FAIL_LOCK_DOC_TEC, e);
            context
                .getMessageQueue()
                .addWarnToQueue(getString(ModeleFeuilleRouteActionServiceImpl.MESSAGE_ROUTE_ALREADY_LOCKED));
            return;
        }
        try {
            documentRoutingService.invalidateRouteModel(currentRouteModel, session);
        } catch (FeuilleRouteNotLockedException e) {
            LOG.error(session, STLogEnumImpl.FAIL_LOCK_DOC_TEC, e);
            context
                .getMessageQueue()
                .addWarnToQueue(getString(ModeleFeuilleRouteActionServiceImpl.MESSAGE_ROUTE_NOT_LOCKED));
            return;
        }

        // Affiche un message d'information
        context.getMessageQueue().addInfoToQueue(getString("admin.squelette.action.invalidated.success"));
    }

    @Override
    public void initSqueletteAction(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel squeletteDoc = context.getCurrentDocument();
        EpgSqueletteActionDTO squeletteActionDTO = new EpgSqueletteActionDTO();
        squeletteActionDTO.setEtatValidee(
            CaseLifeCycleConstants.STATE_VALIDATED.equals(squeletteDoc.getCurrentLifeCycleState())
        );
        squeletteActionDTO.setEtatBrouillon(
            CaseLifeCycleConstants.STATE_DRAFT.equals(squeletteDoc.getCurrentLifeCycleState())
        );
        squeletteActionDTO.setIsLock(LockUtils.isLocked(session, squeletteDoc.getRef()));
        squeletteActionDTO.setIsLockByCurrentUser(LockUtils.isLockedByCurrentUser(session, squeletteDoc.getRef()));
        context.putInContextData(EpgContextDataKey.SQUELETTE_ACTIONS, squeletteActionDTO);
    }

    @Override
    public DocumentModel duplicateSquelette(SpecificContext context) {
        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        DocumentModel doc = context.getCurrentDocument();
        CoreSession session = context.getSession();

        SolonEpgFeuilleRoute newSquelette = documentRoutingService.duplicateSquelette(session, doc);

        // Affiche un message d'information
        context.getMessageQueue().addInfoToQueue("admin.squelette.action.dupliquer.success");

        return newSquelette.getDocument();
    }
}
