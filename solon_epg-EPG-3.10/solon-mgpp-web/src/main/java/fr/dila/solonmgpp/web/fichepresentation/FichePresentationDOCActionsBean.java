package fr.dila.solonmgpp.web.fichepresentation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;


/**
 * Rapport > Document
 * 
 * Communication DOC-01
 *
 */

@Name("fichePresentationDOCActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationDOCActionsBean extends AbstractFichePresentationBean {

    private static final long serialVersionUID = -3042162409888469962L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentationDOCActionsBean.class);

    @Override
    protected void assignFiche() {
        try {
            String idDossier = this.findDossierId(navigationContext);
            
            if (StringUtils.isBlank(idDossier)) {
                return ;
            }

            FichePresentationDOC fichePresentation = SolonMgppServiceLocator.getDossierService().findOrCreateFicheDOC(this.documentManager, idDossier);
            this.ficheDoc = fichePresentation == null ? null : fichePresentation.getDocument();
            if (fichePresentation != null) {
                navigationContext.setCurrentIdDossier(fichePresentation.getIdDossier());
            }
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }
    }

    @Override
    public String saveFiche() {

        if (this.ficheDoc != null) {
            try {
                FichePresentationDOC fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheDOC(this.documentManager, this.ficheDoc);
                this.ficheDoc = fichePresentation.getDocument();
                facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
            } catch (ClientException e) {
                LOGGER.error(documentManager, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, e);
                facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la sauvegarde de la fiche.");
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
                TransactionHelper.setTransactionRollbackOnly();
            }
        }
        return null;
    }

    @Override
    public String getIdDossier() {
        if (this.ficheDoc != null) {
            FichePresentationDOC fichePresentation = this.ficheDoc.getAdapter(FichePresentationDOC.class);
            return fichePresentation.getIdDossier();
        }
        return null;
    }

    public String navigateToCreationDOC() throws ClientException {
        return navigateToCreationDOC(null);
    }

    public String navigateToCreationDOC(EvenementDTO evenementDTO) throws ClientException {
        FichePresentationDOC fichePresentationDOC = SolonMgppServiceLocator.getDossierService().createFicheRepresentationDOC(documentManager, evenementDTO);
        this.ficheDoc = fichePresentationDOC.getDocument();
        return SolonMgppViewConstant.VIEW_CREATE_DOC;
    }

    public String cancelCreationDOC() throws ClientException {
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public String saveCreationDOC() throws ClientException {
        try {
            SolonMgppServiceLocator.getDossierService().saveFicheDOC(documentManager, ficheDoc);
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }
        facesMessages.add(StatusMessage.Severity.INFO, "Document créé.");
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    @Override
    public FicheReportsEnum getFicheReportsEnum() {        
        return FicheReportsEnum.FICHE_DOC;
    }
    
    public String genererCourier() throws Exception {
        return generationCourrierActions.genererCourier(currentCourier, ficheDoc, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DOC);
    }

    public String navigateToDetailsDOC(DocumentModel doc) throws ClientException {
        navigateToFicheDoc(doc, null) ;
        return SolonMgppViewConstant.VIEW_DETAILS_DOC;
    }
    
    public String navigateToFicheDOC(DocumentModel fDoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();
        ficheDoc = fDoc;

        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(fDoc)) {
                pageProvider.setCurrentEntry(fDoc);
            }
        }
        
        FichePresentationDOC fiche = ficheDoc.getAdapter(FichePresentationDOC.class);
        navigationContext.setCurrentIdDossier(fiche.getIdDossier());
        try {
            Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, fiche.getIdDossier());
            if (dossier == null) {
                // try by nor
                dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, fiche.getIdDossier());
            }
            if (dossier != null) {

                navigationContext.setCurrentDocument(dossier.getDocument());
            }
        } catch (ClientException e) {            
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, fiche.getIdDossier(),e) ;
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }
        // reset tab list
        Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
        return null;
    }
}
