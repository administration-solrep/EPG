package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.ReattributionNorSummaryList;
import fr.dila.solonepg.ui.bean.SelectionSummaryDTO;
import fr.dila.solonepg.ui.bean.SelectionSummaryList;
import fr.dila.solonepg.ui.bean.SubstitutionMassSummaryList;
import fr.dila.solonepg.ui.bean.TransfertSummaryList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUndeletableCauseEnum;
import fr.dila.solonepg.ui.services.EpgSelectionSummaryUIService;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.util.PermissionHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.services.actions.STLockActionService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgSelectionSummaryUIServiceImpl implements EpgSelectionSummaryUIService {

    @Override
    public SelectionSummaryList getSelectionSummaryList(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        SelectionSummaryList selectionSummaryList = new SelectionSummaryList();
        boolean isDeletable = true;

        for (String idDoc : idDossiers) {
            String label = "";
            DocumentModel documentModel = context.getSession().getDocument(new IdRef(idDoc));
            Dossier dossier = documentModel.getAdapter(Dossier.class);
            SelectionSummaryDTO selectionSummaryDTO = new SelectionSummaryDTO();

            CoreSession session = context.getSession();
            STLockActionService lockActions = STActionsServiceLocator.getSTLockActionService();
            if (StringUtils.isNotEmpty(lockActions.getLockTime(documentModel, session))) {
                label = EpgUndeletableCauseEnum.VERROU.getValue();
                isDeletable = false;
            }

            if (isDeletable && !dossier.getStatut().equals(VocabularyConstants.STATUT_INITIE)) {
                label = EpgUndeletableCauseEnum.NOT_INIT.getValue();
                isDeletable = false;
            }

            // Il ne s’agit pas d’une loi dont le NOR existe dans une fiche loi du MGPP
            DossierService dossierService = SolonMgppServiceLocator.getDossierService();
            String numeroNor = dossier.getNumeroNor();
            if (
                isDeletable &&
                ObjectUtils.anyNotNull(
                    dossierService.findFicheLoiDocumentFromNor(session, numeroNor),
                    dossierService.findFicheLoiDocumentFromMGPP(session, numeroNor)
                )
            ) {
                label = EpgUndeletableCauseEnum.EXISTING_NOR.getValue();
                isDeletable = false;
            }

            if (BooleanUtils.isFalse(context.getFromContextData(EpgContextDataKey.IS_FROM_ADMINISTRATION))) {
                SSPrincipal ssPrincipal = (SSPrincipal) context.getSession().getPrincipal();
                if (
                    isDeletable &&
                    PermissionHelper.isAdminMinisteriel(ssPrincipal) &&
                    !ssPrincipal.getMinistereIdSet().contains(dossier.getMinistereAttache())
                ) {
                    label = EpgUndeletableCauseEnum.MINISTERE.getValue();
                    isDeletable = false;
                }
            }

            selectionSummaryDTO.setNor(numeroNor);
            selectionSummaryDTO.setId(idDoc);
            if (isDeletable) {
                selectionSummaryDTO.setLabel(dossier.getTitreActe());
                selectionSummaryList.getDeletableDossiers().add(selectionSummaryDTO);
            } else {
                selectionSummaryDTO.setLabel(label);
                selectionSummaryList.getIgnoredDossiers().add(selectionSummaryDTO);
                isDeletable = true;
            }
        }
        return selectionSummaryList;
    }

    @Override
    public ReattributionNorSummaryList getReattributionSummaryList(SpecificContext context) {
        ReattributionNorSummaryList reattributionNorSummary = new ReattributionNorSummaryList();

        List<DocumentModel> docs = getDossierDocsFromSelectionTool(context);

        for (DocumentModel doc : docs) {
            Dossier dossier = doc.getAdapter(Dossier.class);

            SelectionSummaryDTO selectionSummaryDTO = new SelectionSummaryDTO(
                dossier.getNumeroNor(),
                dossier.getTitreActe(),
                doc.getId()
            );

            if (BooleanUtils.isFalse(SolonEpgServiceLocator.getDossierService().isReattribuable(dossier))) {
                reattributionNorSummary.getNonReattribuables().add(selectionSummaryDTO);
                selectionSummaryDTO.setLabel(ResourceHelper.getString("outilSelection.reattribution.raison.erreur"));
            } else if (
                BooleanUtils.isTrue(SolonEpgServiceLocator.getActeService().isRectificatif(dossier.getTypeActe()))
            ) {
                reattributionNorSummary.getRectificatifs().add(selectionSummaryDTO);
            } else {
                reattributionNorSummary.getReattribuables().add(selectionSummaryDTO);
            }
        }
        reattributionNorSummary.setHasNoError(
            CollectionUtils.isEmpty(reattributionNorSummary.getNonReattribuables()) &&
            CollectionUtils.isEmpty(reattributionNorSummary.getRectificatifs())
        );

        return reattributionNorSummary;
    }

    @Override
    public TransfertSummaryList getTransfertSummaryList(SpecificContext context) {
        List<DocumentModel> docs = getDossierDocsFromSelectionTool(context);

        TransfertSummaryList transfertSummary = new TransfertSummaryList();

        for (DocumentModel doc : docs) {
            Dossier dossier = doc.getAdapter(Dossier.class);

            SelectionSummaryDTO selectionSummaryDTO = new SelectionSummaryDTO(
                dossier.getNumeroNor(),
                dossier.getTitreActe(),
                doc.getId()
            );

            if (BooleanUtils.isTrue(SolonEpgServiceLocator.getDossierService().isTransferable(dossier))) {
                transfertSummary.getTransferableFolders().add(selectionSummaryDTO);
            } else {
                selectionSummaryDTO.setLabel(ResourceHelper.getString("outilSelection.transfert.raison.erreur"));
                transfertSummary.getIgnoredFolders().add(selectionSummaryDTO);
            }
        }

        return transfertSummary;
    }

    @Override
    public SubstitutionMassSummaryList getDossierSubstitutionSummaryList(SpecificContext context) {
        List<DocumentModel> docList = getDossierDocsFromSelectionTool(context);
        SubstitutionMassSummaryList summaryList = new SubstitutionMassSummaryList();

        for (DocumentModel doc : docList) {
            Dossier dossier = doc.getAdapter(Dossier.class);
            SelectionSummaryDTO selectionSummaryDTO = new SelectionSummaryDTO(
                dossier.getNumeroNor(),
                dossier.getTitreActe(),
                doc.getId()
            );

            if (VocabularyConstants.STATUT_INITIE.equals(dossier.getStatut())) {
                summaryList.getSubstituableDossiers().add(selectionSummaryDTO);
            } else {
                summaryList.getIgnoredDossiers().add(selectionSummaryDTO);
            }
        }
        return summaryList;
    }

    private List<DocumentModel> getDossierDocsFromSelectionTool(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);

        return QueryHelper.getDocuments(
            context.getSession(),
            idDossiers,
            DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_XPATH,
            DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_XPATH
        );
    }
}
