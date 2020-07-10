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
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Organisation > Jours supplémentaire de Scéance
 * 
 * JSS-01
 * 
 * @author FLT
 *
 */
@Name("fichePresentationJSSActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationJSSActionsBean extends AbstractFichePresentationBean {

    private static final long serialVersionUID = -3042162409888469962L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FichePresentationJSSActionsBean.class);

    @Override
    protected void assignFiche() {
        try {
            String idDossier = this.findDossierId(navigationContext);
            
            if (StringUtils.isBlank(idDossier)) {
                return ;
            }

            FichePresentationJSS fichePresentation = SolonMgppServiceLocator.getDossierService().findOrCreateFicheJSS(this.documentManager, idDossier);
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
                FichePresentationJSS fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheJSS(this.documentManager, this.ficheDoc);
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
            FichePresentationJSS fichePresentation = this.ficheDoc.getAdapter(FichePresentationJSS.class);
            return fichePresentation.getIdDossier();
        }
        return null;
    }

    @Override
    public FicheReportsEnum getFicheReportsEnum() {
        return FicheReportsEnum.FICHE_JSS;
    }
    
    public String genererCourier() throws Exception {
        return generationCourrierActions.genererCourier(currentCourier, ficheDoc, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_JSS);
    }
    
    public String navigateToDetailsJSS(DocumentModel doc) throws ClientException {
        navigateToFicheDoc(doc, null) ;
        return SolonMgppViewConstant.VIEW_DETAILS_JSS ;
    } 
    
    public String navigateToFicheJSS(DocumentModel fJSSoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();
        ficheDoc = fJSSoc;

        // Assignation du DocumentModel au provider
        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(fJSSoc)) {
                pageProvider.setCurrentEntry(fJSSoc);
            }
        }
        
        FichePresentationJSS fiche = ficheDoc.getAdapter(FichePresentationJSS.class);
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
