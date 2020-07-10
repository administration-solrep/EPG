package fr.dila.solonmgpp.web.fichepresentation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Nomination > demande d'audition
 * 
 * AUD-01
 * 
 * @author FLT
 *
 */
@Name("fichePresentationAUDActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationAUDActionsBean extends AbstractFichePresentationBean {

    private static final long serialVersionUID = -3042162409888469962L;

	private static final String[] LOCK_UNLOCK_REFRESH_COMPONENTS_IDS = new String[] {"personneA4J"};

    // List des doc representant AUD
    private List<DocumentModel> listRepresentant;
    
    private String idDossierLock;
    
    

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentationAUDActionsBean.class);

    @Override
    protected void assignFiche() {
        try {
            String idDossier = this.findDossierId(navigationContext);
            
            if (StringUtils.isBlank(idDossier)) {
                return ;
            }

            FichePresentationAUD fichePresentation = SolonMgppServiceLocator.getDossierService().findOrCreateFicheAUD(this.documentManager, idDossier);
			this.ficheDoc = (fichePresentation == null) ? null : fichePresentation.getDocument();
			if (ficheDoc != null && !ficheDoc.getId().equals(idDossierLock)) {
                try {
                    listRepresentant = SolonMgppServiceLocator.getDossierService().fetchPersonneAUD(documentManager, ficheDoc.getId());
                    idDossierLock = ficheDoc.getId();
                } catch (ClientException e) {
                    LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, "Erreur lors de la récupération de la liste des personnes à auditionner  " + ficheDoc.getId(), e);
                    facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la liste des personnes à auditionner pour la fiche " + ficheDoc.getId());
                    TransactionHelper.setTransactionRollbackOnly();
                }
            }
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
                FichePresentationAUD fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheAUD(documentManager, ficheDoc, getListRepresentant());
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
            FichePresentationAUD fichePresentation = this.ficheDoc.getAdapter(FichePresentationAUD.class);
            return fichePresentation.getIdDossier();
        }
        return null;
    }

    public String navigateToCreationAUD(EvenementDTO evenementDTO) throws ClientException {
        FichePresentationAUD fichePresentationAUD = SolonMgppServiceLocator.getDossierService().createFicheRepresentationAUD(documentManager, evenementDTO);
        this.ficheDoc = fichePresentationAUD.getDocument();
        return SolonMgppViewConstant.VIEW_CREATE_AUD;
    }

    public String navigateToCreationAUD() throws ClientException {
        return navigateToCreationAUD(null);
    }

    /**
     * Ajouter une nouvelle Personne
     * 
     * @return
     */
    public String ajouterNouvellePersonne() {
        DocumentModel modelDesired = new DocumentModelImpl("/case-management/fiche-dossier", "" + Calendar.getInstance().getTimeInMillis(), RepresentantAUD.DOC_TYPE);
        RepresentantAUD representantAUD = modelDesired.getAdapter(RepresentantAUD.class);
        representantAUD.setDateDebut(Calendar.getInstance());
        this.getListRepresentant().add(representantAUD.getDocument());
        idDossierLock = ficheDoc.getId();
        return null;
    }

    /**
     * Liste des personnes à auditionner
     * 
     * @return
     */
    public List<DocumentModel> fetchPersonnesAUD() {
		if (this.listRepresentant == null && ficheDoc != null) {

			if (StringUtils.isBlank(ficheDoc.getId())) {
				return this.getListRepresentant();
			}
			try {
				listRepresentant = SolonMgppServiceLocator.getDossierService().fetchPersonneAUD(documentManager,
						ficheDoc.getId());
			} catch (ClientException e) {
				LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC,
						"Erreur lors de la récupération de la liste des personnes à auditionner  " + ficheDoc.getId(),
						e);
				facesMessages.add(StatusMessage.Severity.WARN,
						"Erreur lors de la récupération de la liste des personnes à auditionner pour la fiche "
								+ ficheDoc.getId());
				TransactionHelper.setTransactionRollbackOnly();
			}

		}
        return this.getListRepresentant();
    }

    public String saveCreationAUD() throws ClientException {
        try {
            SolonMgppServiceLocator.getDossierService().createFicheRepresentationAUD(documentManager, ficheDoc, listRepresentant);
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }
        facesMessages.add(StatusMessage.Severity.INFO, "Organisme créé.");
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public String cancelCreationAUD() throws ClientException {
        listRepresentant = null;
        Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
        return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
    }

    public String removePersonne(DocumentModel representantDoc) {
        if (listRepresentant != null) {
            listRepresentant.remove(representantDoc);
        }
        idDossierLock = ficheDoc.getId();
        return null;
    }

    public List<DocumentModel> getListRepresentant() {
        if (this.listRepresentant == null) {
            this.listRepresentant = new ArrayList<DocumentModel>();
        }
        return listRepresentant;
    }

    @Override
    public FicheReportsEnum getFicheReportsEnum() {       
        return FicheReportsEnum.FICHE_AUD;
    }

    
    public String navigateToDetailsAUD(DocumentModel doc) throws ClientException {
        navigateToFicheDoc(doc, null) ;
        return SolonMgppViewConstant.VIEW_DETAILS_AUD ;
    } 
    
    public String genererCourier() throws Exception {
        return generationCourrierActions.genererCourier(currentCourier, ficheDoc, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_AUDIT);
    }
    
    public String navigateToFicheAUD(DocumentModel fAUDDoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();
        ficheDoc = fAUDDoc;

        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(fAUDDoc)) {
                pageProvider.setCurrentEntry(fAUDDoc);
            }
        }
        
        FichePresentationAUD fiche = ficheDoc.getAdapter(FichePresentationAUD.class);
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

	/**
	 * @see FichePresentationBean#getLockUnlockComponentIdsToRebuild()
	 */
	@Override
	protected String[] getLockUnlockComponentIdsToRebuild() {
		return LOCK_UNLOCK_REFRESH_COMPONENTS_IDS;
	}
}
