package fr.dila.solonmgpp.web.fichepresentation;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.birt.GenerationCourrierActionsBean;
import fr.dila.solonmgpp.web.birt.GenerationFicheActionsBean;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.evenement.EvenementCreationActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * DECLARATION > INTERVENTION EXTERIEUR
 * 
 * Communication OPEX
 * 
 * bean de gestion des {@link FichePresentationIE}
 * 
 * @author asatre
 * 
 */
@Name("fichePresentationIEActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationIEActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

    private static final long serialVersionUID = 1455651539463191518L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentationIEActionsBean.class);    

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient EvenementCreationActionsBean evenementCreationActions;

    @In(create = true, required = false)
    protected transient EspaceParlementaireActionsBean espaceParlementaireActions;

    @In(create = true, required = false)
    protected transient NavigationWebActionsBean navigationWebActions;

    @In(create = true, required = false)
    protected transient GenerationCourrierActionsBean generationCourrierActions;
    
    @In(create = true, required = false)
    protected transient GenerationFicheActionsBean generationFicheActions;
    
    @In(create = true)
    protected ContentViewActions contentViewActions;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    private DocumentModel ficheIE;

    private DocumentModel ficheIECreation;

    private String currentCourier;

    private Boolean traite;

    public void setFicheIE(DocumentModel ficheIE) {
        this.ficheIE = ficheIE;
    }

    public String saveFicheIE() {

        if (ficheIE == null) {
            return null;
        }
        try {
            FichePresentationIE fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheIE(documentManager,
                    ficheIE.getAdapter(FichePresentationIE.class));
            ficheIE = fichePresentation.getDocument();
            facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }

        return null;
    }

    public DocumentModel getFicheIEDoc() {
        
        String idDossier = this.findDossierId(navigationContext) ; 

        if (StringUtils.isBlank(idDossier)) {
            return null;
        }

        // force always reload
        try {
            FichePresentationIE fichePresentation = SolonMgppServiceLocator.getDossierService().findFicheIE(documentManager, idDossier);
            ficheIE = fichePresentation != null ? fichePresentation.getDocument() : null;
            navigationContext.setCurrentIdDossier(fichePresentation != null ? fichePresentation.getIdentifiantProposition() : null);
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }
        return ficheIE;
    }

    public DocumentModel getFicheIE() {
        if (ficheIE == null) {
            getFicheIEDoc();
        }
        return ficheIE;
    }

    public String navigateToIE(DocumentModel ieDoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();
        ficheIE = ieDoc;

        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(ieDoc)) {
                pageProvider.setCurrentEntry(ieDoc);
            }
        }
        
        FichePresentationIE fiche = ficheIE.getAdapter(FichePresentationIE.class);
        navigationContext.setCurrentIdDossier(fiche.getIdentifiantProposition());
        try {
            Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, fiche.getIdentifiantProposition());
            if (dossier == null) {
                // try by nor
                dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, fiche.getIdentifiantProposition());
            }
            if (dossier != null) {
                navigationContext.setCurrentDocument(dossier.getDocument());
            }
        } catch (ClientException e) {            
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, fiche.getIdentifiantProposition(),e) ;
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }
        // reset tab list
        Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
        return null;
    }

    public Boolean canCurrentUserEdit() throws ClientException {
        if (ficheIE == null) {
            return Boolean.FALSE;
        }

        return STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheIE);
    }

    public Boolean canCurrentUserLock() throws ClientException {
        if (ficheIE == null) {
            return Boolean.FALSE;
        }

        return STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheIE, (NuxeoPrincipal) documentManager.getPrincipal());
    }

    @Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
    public void resetFichePresentationIE() {
        setFicheIE(null);
    }

    public void setCurrentCourier(String currentCourier) {
        this.currentCourier = currentCourier;
    }

    public String getCurrentCourier() {
        return currentCourier;
    }

    public String genererCourier() throws Exception {
        return generationCourrierActions.genererCourier(currentCourier, ficheIE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_INTERVENTION_EXT);
    }

    /**
     * Navigation vers la creation d'un nouveau IE
     * 
     * @param evenementDTO
     * @param isFromTraiter
     * @return
     */
    public String navigateToCreationIE() {
        this.traite = Boolean.FALSE;
        return navigateToCreationIE(null);
    }

    public String saveCreationIE() {

        try {
            SolonMgppServiceLocator.getDossierService().createFicheIE(documentManager, ficheIECreation.getAdapter(FichePresentationIE.class));
            ficheIECreation = null;

            Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);

            if (this.traite != null && this.traite) {
                this.traite = Boolean.FALSE;
                return evenementCreationActions.suivreTransitionTraite();
            } else {
                this.traite = Boolean.FALSE;
                return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
            }
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e) ;
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }
    }

    public String cancelCreationIE() throws ClientException {
        this.traite = Boolean.FALSE;
        ficheIECreation = null;
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public void setFicheIECreation(DocumentModel ficheIECreation) {
        this.ficheIECreation = ficheIECreation;
    }

    public DocumentModel getFicheIECreation() {
        return ficheIECreation;
    }

    private String navigateToCreationIE(EvenementDTO evenementDTO) {
        try {
            ficheIECreation = SolonMgppServiceLocator.getDossierService().createEmptyFicheIE(evenementDTO, documentManager).getDocument();
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e) ;
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }

        return SolonMgppViewConstant.VIEW_CREATE_IE;
    }

    @Override
    public String reload() {
        if (ficheIE != null) {
            try {
                ficheIE = documentManager.getDocument(new IdRef(ficheIE.getId()));
            } catch (ClientException e) {
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            }
        }
        return null;
    }

    public boolean canCurrentUserForceUnlock() throws ClientException {
        return !canCurrentUserEdit() && ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UNLOCKER);
    }

    public String genererPDF() throws Exception {
        return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_IE.getId(), ficheIE);
    }
    
    public String genererXLS() throws Exception {
        return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_IE.getId(), ficheIE);
    }

    @Override
    protected String addDecorationClass(DocumentModel doc, String defaultClass) {
        return defaultClass;
    }

    @Override
    protected DocumentModel getCurrentDocument() {
        return ficheIE;
    }
}
