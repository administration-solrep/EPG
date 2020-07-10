package fr.dila.solonmgpp.web.fichepresentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.forms.layout.api.BuiltinModes;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;
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
import fr.dila.solonmgpp.web.filter.FilterActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Nomination > Avis de Nomination
 * 
 * Communication NOM-01
 * 
 * bean de gestion des {@link FichePresentationAVI}
 * 
 * @author asatre
 * 
 */
@Name("fichePresentationAVIActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationAVIActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

    private static final long serialVersionUID = -3042162409888469962L;

	private static final String[] LOCK_UNLOCK_REFRESH_COMPONENTS_IDS = new String[] {"nomineA4J"};

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentationAVIActionsBean.class);    

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

    private DocumentModel ficheAVI;

    private String currentCourier;

    private Boolean isFromTraiter;

    private List<DocumentModel> listRepresentant;

    public void setFicheAVI(DocumentModel ficheAVI) {
        this.ficheAVI = ficheAVI;
    }

    public DocumentModel getFicheAVIDoc() {
        String idDossier = this.findDossierId(navigationContext) ; 
        
        if (StringUtils.isBlank(idDossier)) {
            return null;
        }

        return getFicheAVI(idDossier);
    }

    public DocumentModel getFicheAVI(String idDossier) {
        if (StringUtils.isBlank(idDossier)) {
            return null;
        }

        // force always reload
        try {
            listRepresentant = null;
            FichePresentationAVI fichePresentation = SolonMgppServiceLocator.getDossierService().findFicheAVI(documentManager, idDossier);
            ficheAVI = fichePresentation == null ? null : fichePresentation.getDocument();
            if (fichePresentation != null) {
                navigationContext.setCurrentIdDossier(fichePresentation.getIdDossier());
            } else {
                facesMessages.add(StatusMessage.Severity.INFO, "Il n'y a pas de fiche pour ce dossier.");
            }
        } catch (ClientException e) {            
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC,e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
            TransactionHelper.setTransactionRollbackOnly();
        }
        return ficheAVI;
    }

    public String saveFicheAVI() {

        if (ficheAVI == null) {
            return null;
        }
        try {
            FichePresentationAVI fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheAVI(documentManager, ficheAVI, listRepresentant);
            ficheAVI = fichePresentation.getDocument();
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_UPDATE_FICHE_LOI_TEC,e) ;
            facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la sauvegarde de la fiche.");
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }

        return null;
    }

    public Boolean canCurrentUserEdit() throws ClientException {
        if (ficheAVI == null) {
            return Boolean.FALSE;
        }

        return filterActions.isUpdater() && STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheAVI);
    }

    public Boolean canCurrentUserLock() throws ClientException {
        if (ficheAVI == null) {
            return Boolean.FALSE;
        }

        return filterActions.isUpdater()
                && STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheAVI,
                        (NuxeoPrincipal) documentManager.getPrincipal());
    }

    @Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
    public void resetFichePresentationAVI() {
        setFicheAVI(null);
        isFromTraiter = Boolean.FALSE;
    }

    public void setCurrentCourier(String currentCourier) {
        this.currentCourier = currentCourier;
    }

    public String getCurrentCourier() {
        return currentCourier;
    }

    public String genererCourier() throws Exception {
        return generationCourrierActions.genererCourier(currentCourier, ficheAVI,null);
    }

    /**
     * Navigation vers la creation d'un nouveau AVI
     * 
     * @param evenementDTO
     * @param isFromTraiter
     * @return
     * @throws ClientException
     */
    public String navigateToCreationAVI(EvenementDTO evenementDTO, Boolean isFromTraiter) throws ClientException {
        this.isFromTraiter = isFromTraiter;
        listRepresentant = null;

        FichePresentationAVI fichePresentationAVI = SolonMgppServiceLocator.getDossierService().createFicheRepresentationAVI(documentManager,
                evenementDTO);
        ficheAVI = fichePresentationAVI.getDocument();

        return SolonMgppViewConstant.VIEW_CREATE_AVI;
    }

    public DocumentModel getFicheAVICreation() {
        return ficheAVI;
    }

    public String navigateToCreationAVI() throws ClientException {
        return navigateToCreationAVI(null, Boolean.FALSE);
    }

    public String saveCreationAVI() throws ClientException {
        try {
            SolonMgppServiceLocator.getDossierService().createFicheRepresentationAVI(documentManager, ficheAVI, listRepresentant);
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC,e) ;
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }

        if (isFromTraiter != null && isFromTraiter) {
            return evenementCreationActions.suivreTransitionTraite();
        }

        facesMessages.add(StatusMessage.Severity.INFO, "Organisme créé.");
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public String cancelCreationAVI() throws ClientException {
        this.isFromTraiter = Boolean.FALSE;
        listRepresentant = null;
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public List<DocumentModel> fetchNomine() {
        if (listRepresentant != null) {
            return listRepresentant;
        } else {
            listRepresentant = new ArrayList<DocumentModel>();
            if (ficheAVI != null) {
                if (StringUtils.isBlank(ficheAVI.getId())) {
                    return listRepresentant;
                }
                try {
                    listRepresentant = SolonMgppServiceLocator.getDossierService().fetchNomineAVI(documentManager, ficheAVI.getId());
                } catch (ClientException e) {
                    LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC,"Erreur lors de la récupération des nominés pour la fiche " + ficheAVI.getId(),e) ;
                    facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération des nominés pour la fiche " + ficheAVI.getId());
                    TransactionHelper.setTransactionRollbackOnly();
                }
            }
            return listRepresentant;
        }
    }

    public String addNewNomine() {

        if (listRepresentant == null) {
            listRepresentant = new ArrayList<DocumentModel>();
        }

        DocumentModel modelDesired = new DocumentModelImpl(ficheAVI.getPathAsString(), "" + Calendar.getInstance().getTimeInMillis(),
                RepresentantAVI.DOC_TYPE);

        RepresentantAVI representantAVI = modelDesired.getAdapter(RepresentantAVI.class);
        representantAVI.setDateDebut(Calendar.getInstance());

        listRepresentant.add(representantAVI.getDocument());
        return null;
    }

    public String removeNomine(DocumentModel representantDoc) {
        if (listRepresentant != null) {
            listRepresentant.remove(representantDoc);
        }
        return null;
    }

    public String navigateToAVI(DocumentModel AVIDoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();

        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(AVIDoc)) {
                pageProvider.setCurrentEntry(AVIDoc);
            }
        }
        
        listRepresentant = null;
        ficheAVI = AVIDoc;

        if (ficheAVI != null) {
            FichePresentationAVI fichePresentation = ficheAVI.getAdapter(FichePresentationAVI.class);
            navigationContext.setCurrentIdDossier(fichePresentation.getIdDossier());
        }

        // reset tab list
        Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
        return null;
    }

    public String navigateToDetailsAVI(DocumentModel AVIDoc) throws ClientException {
        navigateToAVI(AVIDoc, null);
        return SolonMgppViewConstant.VIEW_DETAILS_AVI;
    }

    public DocumentModel getFicheAVI() {
    	String idDossier = this.findDossierId(navigationContext);
    	if (ficheAVI == null) {
    		getFicheAVIDoc();
    	} else {
    		FichePresentationAVI fiche = ficheAVI.getAdapter(FichePresentationAVI.class);
    		if (!fiche.getIdDossier().equals(idDossier)) {
    			getFicheAVIDoc();
    		}
    	}
        return ficheAVI;
    }

    public String getEditModeIdDossier() {
        if (this.isFromTraiter != null && this.isFromTraiter) {
            return BuiltinModes.VIEW;
        }

        if (ficheAVI != null && StringUtils.isNotBlank(ficheAVI.getId())) {
            return BuiltinModes.VIEW;
        }

        return BuiltinModes.EDIT;
    }

    @Override
    protected DocumentModel getCurrentDocument() {
        return ficheAVI;
    }

    @Override
    public String reload() {
        if (ficheAVI != null) {
            try {
                ficheAVI = documentManager.getDocument(new IdRef(ficheAVI.getId()));
            } catch (ClientException e) {
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            }
        }
        listRepresentant = null;

        return null;
    }

    public boolean canCurrentUserForceUnlock() throws ClientException {
        return !canCurrentUserEdit() && ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UNLOCKER);
    }

    @Override
    protected String addDecorationClass(DocumentModel doc, String defaultClass) {
        if (doc != null && doc.hasSchema(FichePresentationAVI.SCHEMA)) {
            FichePresentationAVI fichePresentationAVI = doc.getAdapter(FichePresentationAVI.class);
            if (fichePresentationAVI.getDateFin() != null && fichePresentationAVI.getDateFin().before(Calendar.getInstance())) {
                defaultClass += " dataRowItalic";
            }
        }
        return defaultClass;
    }
    
    public String genererPDF() throws Exception {
        return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_AVIS_NOMINATION.getId(), ficheAVI);
    }
    
    public String genererXLS() throws Exception {
        return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_AVIS_NOMINATION.getId(), ficheAVI);
    }

	/**
	 * @see FichePresentationBean#getLockUnlockComponentIdsToRebuild()
	 */
	@Override
	protected String[] getLockUnlockComponentIdsToRebuild() {
		return LOCK_UNLOCK_REFRESH_COMPONENTS_IDS;
	}
}
