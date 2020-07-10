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
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.birt.GenerationCourrierActionsBean;
import fr.dila.solonmgpp.web.birt.GenerationFicheActionsBean;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.evenement.EvenementCreationActionsBean;
import fr.dila.solonmgpp.web.filter.FilterActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 *Organisation > Decret du président de la république
 *
 * Communication SE-01
 * 
 * 
 * bean de gestion des {@link FichePresentationDecret}
 * 
 * @author asatre
 * 
 */
@Name("fichePresentationDecretActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationDecretActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

    private static final long serialVersionUID = -3042162409888469962L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentationDecretActionsBean.class);    

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

    @In(create = true, required = false)
    protected transient FilterActionsBean filterActions;
    
    @In(create = true)
    protected ContentViewActions contentViewActions;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    private DocumentModel ficheDecret;

    private String currentCourier;

    public void setFicheDecret(DocumentModel ficheDecret) {
        this.ficheDecret = ficheDecret;
    }

    public DocumentModel getFicheDecretDoc() {
                
        String nor = this.findDossierId(navigationContext) ; 

        if (StringUtils.isBlank(nor)) {
            return null;
        }

        return getFicheDecret(nor);
    }

    public DocumentModel getFicheDecret(String nor) {
        if (StringUtils.isBlank(nor)) {
            return null;
        }

        // force always reload
        try {
            FichePresentationDecret fichePresentation = SolonMgppServiceLocator.getDossierService().findOrCreateFicheDecret(documentManager, nor);
            ficheDecret = fichePresentation == null ? null : fichePresentation.getDocument();
            if (fichePresentation != null) {
                navigationContext.setCurrentIdDossier(fichePresentation.getNor());
            }
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }
        return ficheDecret;
    }

    public String navigateToDecret(DocumentModel decretDoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();
        ficheDecret = decretDoc;

        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions
            		.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(decretDoc)) {
                pageProvider.setCurrentEntry(decretDoc);
            }
        }
        
        FichePresentationDecret fiche = ficheDecret.getAdapter(FichePresentationDecret.class);

        if (fiche != null) {
            navigationContext.setCurrentIdDossier(fiche.getNor());
            
            try {
                Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, fiche.getNor());
                if (dossier == null) {
                    // try by nor
                    dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, fiche.getNor());
                }
                if (dossier != null) {
                    navigationContext.setCurrentDocument(dossier.getDocument());
                }
            } catch (ClientException e) {
                LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC,"Erreur de recuperation du dossier " + fiche.getNor() ,e) ;
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
                TransactionHelper.setTransactionRollbackOnly();
            }          
            
        }

        // reset tab list
        Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
        return null;
    }
    
    public String navigateToDetailsDecret(DocumentModel DecretDoc) throws ClientException {
        navigateToDecret(DecretDoc, null);
        return SolonMgppViewConstant.VIEW_DETAILS_DECRET;
    }

    public String saveFicheDecret() {

        if (ficheDecret == null) {
            return null;
        }
        try {
            FichePresentationDecret fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheDecret(documentManager, ficheDecret);
            ficheDecret = fichePresentation.getDocument();
            facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
        } catch (ClientException e) {
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la sauvegarde de la fiche.");
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }

        return null;
    }

    public Boolean canCurrentUserEdit() throws ClientException {
        if (ficheDecret == null) {
            return Boolean.FALSE;
        }

        return filterActions.isUpdater() && STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheDecret);
    }

    public Boolean canCurrentUserLock() throws ClientException {
        if (ficheDecret == null) {
            return Boolean.FALSE;
        }

        return filterActions.isUpdater()
                && STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheDecret,
                        (NuxeoPrincipal) documentManager.getPrincipal());
    }

    @Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
    public void resetFichePresentationDecret() {
        setFicheDecret(null);
    }

    public void setCurrentCourier(String currentCourier) {
        this.currentCourier = currentCourier;
    }

    public String getCurrentCourier() {
        return currentCourier;
    }

    public String genererCourier() throws Exception {
      FichePresentationDecret decret = ficheDecret==null ? null : ficheDecret.getAdapter(FichePresentationDecret.class);
        return generationCourrierActions.genererCourier(currentCourier, ficheDecret, decret, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT);
    }

    public DocumentModel getFicheDecretCreation() {
        return ficheDecret;
    }

    public DocumentModel getFicheDecret() {
    	String idDossier = this.findDossierId(navigationContext);
    	if (ficheDecret == null) {
            getFicheDecretDoc();
        } else {
        	FichePresentationDecret fiche = ficheDecret.getAdapter(FichePresentationDecret.class);
    		if (!fiche.getNor().equals(idDossier)) {
    			getFicheDecretDoc();
    		}
        }
        return ficheDecret;
    }

    @Override
    public String reload() {
        if (ficheDecret != null) {
            try {
                ficheDecret = documentManager.getDocument(new IdRef(ficheDecret.getId()));
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
        return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_DECRET_PR.getId(), ficheDecret);
    }
    
    public String genererXLS() throws Exception {
        return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_DECRET_PR.getId(), ficheDecret);
    }

    @Override
    protected String addDecorationClass(DocumentModel doc, String defaultClass) {
        return defaultClass;
    }

    @Override
    protected DocumentModel getCurrentDocument() {
        return ficheDecret;
    }

}
