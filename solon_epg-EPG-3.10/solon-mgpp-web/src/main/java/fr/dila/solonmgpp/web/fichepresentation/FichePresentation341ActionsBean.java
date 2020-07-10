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

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
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
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Resolution
 * 
 * pas d'identification de communication
 * 
 * 
 * bean de gestion des {@link FichePresentation341}
 * 
 * @author asatre
 * 
 */
@Name("fichePresentation341Actions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentation341ActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

    private static final long serialVersionUID = 1L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentation341ActionsBean.class);    

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

    private DocumentModel fiche341;

    private String currentCourier;

    private Boolean traite;

    private DocumentModel fiche341Creation;

    public void setFiche341(DocumentModel fiche341) {
        this.fiche341 = fiche341;
    }

    public DocumentModel getFiche341Doc() {

        String idDossier = this.findDossierId(navigationContext) ; 
        
        if (StringUtils.isBlank(idDossier)) {
            return null;
        }
        // force always reload
        try {
            FichePresentation341 fichePresentation = SolonMgppServiceLocator.getDossierService().find341(documentManager, idDossier);
            
            fiche341 = fichePresentation != null ? fichePresentation.getDocument() : null;
            navigationContext.setCurrentIdDossier(fichePresentation != null ? fichePresentation.getIdentifiantProposition() : null);
            
        } catch (ClientException e) {            
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }
        return fiche341;
    }

    public DocumentModel getFiche341() {
        if (fiche341 == null) {
            getFiche341Doc();
        }
        return fiche341;
    }

    public String navigateTo341(DocumentModel f341Doc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();
        fiche341 = f341Doc;
        
        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(f341Doc)) {
                pageProvider.setCurrentEntry(f341Doc);
            }
        }
        
        if (f341Doc != null) {
            FichePresentation341 fichePresentation = f341Doc.getAdapter(FichePresentation341.class);
            navigationContext.setCurrentIdDossier(fichePresentation.getIdentifiantProposition());
        }
        
        // reset tab list
        Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
        return null;
    }

    public String saveFiche341() {

        if (fiche341 == null) {
            return null;
        }
        try {
            FichePresentation341 fichePresentation = SolonMgppServiceLocator.getDossierService().saveFiche341(documentManager,
                    fiche341.getAdapter(FichePresentation341.class));
            fiche341 = fichePresentation.getDocument();
            facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC,e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }

        return null;
    }

    public Boolean canCurrentUserEdit() throws ClientException {
        if (fiche341 == null) {
            return Boolean.FALSE;
        }

        return STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, fiche341);
    }

    public Boolean canCurrentUserLock() throws ClientException {
        if (fiche341 == null) {
            return Boolean.FALSE;
        }

        return STServiceLocator.getSTLockService()
                .isLockActionnableByUser(documentManager, fiche341, (NuxeoPrincipal) documentManager.getPrincipal());
    }

    @Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
    public void resetFichePresentation341() {
        setFiche341(null);
    }

    public void setCurrentCourier(String currentCourier) {
        this.currentCourier = currentCourier;
    }

    public String getCurrentCourier() {
        return currentCourier;
    }

    public String genererCourier() throws Exception {
        return generationCourrierActions.genererCourier(currentCourier, fiche341, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_RESOLUTION_341);
    }

    public String saveCreation341() {

        try {
            SolonMgppServiceLocator.getDossierService().createFiche341(documentManager, fiche341Creation.getAdapter(FichePresentation341.class));
            fiche341Creation = null;

            Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);

            if (this.traite != null && this.traite) {
                this.traite = Boolean.FALSE;
                return evenementCreationActions.suivreTransitionTraite();
            } else {
                this.traite = Boolean.FALSE;
                return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
            }
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC,e) ;
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }
    }

    public String cancelCreation341() throws ClientException {
        this.traite = Boolean.FALSE;
        fiche341Creation = null;
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public void setFiche341Creation(DocumentModel ficheIECreation) {
        this.fiche341Creation = ficheIECreation;
    }

    public DocumentModel getFiche341Creation() {
        return fiche341Creation;
    }

    public String navigateToCreation341(EvenementDTO evenementDTO, Boolean isFromTraiter) {
        try {
            FichePresentation341 fichePresentation = SolonMgppServiceLocator.getDossierService().find341(documentManager, evenementDTO.getIdDossier());
            if (fichePresentation != null) {
                traite = Boolean.FALSE;
                return evenementCreationActions.suivreTransitionTraite();
            }
            fiche341Creation = SolonMgppServiceLocator.getDossierService().createEmptyFiche341(evenementDTO, documentManager).getDocument();
            traite = isFromTraiter;
        } catch (ClientException e) {            
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC,"Erreur de creation d'une 341 vide",e) ;
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }

        return SolonMgppViewConstant.VIEW_CREATE_341;
    }

    @Override
    public String reload() {
        if (fiche341 != null) {
            try {
                fiche341 = documentManager.getDocument(new IdRef(fiche341.getId()));
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
        return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_RESOLUTION.getId(), fiche341);
    }
    
    public String genererXLS() throws Exception {
        return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_RESOLUTION.getId(), fiche341);
    }

    @Override
    protected String addDecorationClass(DocumentModel doc, String defaultClass) {
        return defaultClass;
    }

    @Override
    protected DocumentModel getCurrentDocument() {
        return fiche341;
    }

}
