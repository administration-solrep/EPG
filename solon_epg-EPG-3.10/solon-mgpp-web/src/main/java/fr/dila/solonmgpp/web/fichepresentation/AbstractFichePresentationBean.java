package fr.dila.solonmgpp.web.fichepresentation;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.annotations.In;
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
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.web.birt.GenerationCourrierActionsBean;
import fr.dila.solonmgpp.web.birt.GenerationFicheActionsBean;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.evenement.EvenementCreationActionsBean;
import fr.dila.solonmgpp.web.filter.FilterActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.service.STServiceLocator;

public abstract class AbstractFichePresentationBean extends FichePresentationBean implements Serializable, ReloadableBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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

    protected DocumentModel ficheDoc;

    protected String currentCourier;

    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;

    public abstract String getIdDossier();

    protected abstract void assignFiche();

    public abstract String saveFiche();

    public abstract FicheReportsEnum getFicheReportsEnum();

    public boolean isButtonToDisplay() {
        return !documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
    }

    public void setCurrentCourier(String currentCourier) {
        this.currentCourier = currentCourier;
    }

    public String getCurrentCourier() {
        return currentCourier;
    }

    @Override
    public String reload() {
        if (this.ficheDoc != null) {
            try {
                this.ficheDoc = documentManager.getDocument(new IdRef(this.ficheDoc.getId()));
            } catch (ClientException e) {
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected String addDecorationClass(DocumentModel doc, String defaultClass) {
        return defaultClass;
    }

    @Override
    protected DocumentModel getCurrentDocument() {
        return this.ficheDoc;
    }

    public Boolean canCurrentUserEdit() throws ClientException {
        if (ficheDoc == null) {
            return Boolean.FALSE;
        }

        return STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheDoc);
    }

    public String genererXLS() throws Exception {
        return generationFicheActions.genererFicheXLS(this.getFicheReportsEnum().getId(), ficheDoc);
    }

    public String genererPDF() throws Exception {
        return generationFicheActions.genererFichePDF(this.getFicheReportsEnum().getId(), ficheDoc);
    }

    public Boolean canCurrentUserLock() throws ClientException {
        if (ficheDoc == null) {
            return Boolean.FALSE;
        }

        return STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheDoc, (NuxeoPrincipal) documentManager.getPrincipal());
    }

    public boolean canCurrentUserForceUnlock() throws ClientException {
        return !canCurrentUserEdit() && ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UNLOCKER);
    }

    public String navigateToFicheDoc(DocumentModel ficheDoc, String contentViewName) throws ClientException {
        navigationContext.resetCurrentDocument();

        if (contentViewName != null) {
            @SuppressWarnings("unchecked")
            PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
            List<?> currentPage = pageProvider.getCurrentPage();
            if (currentPage != null && currentPage.contains(ficheDoc)) {
                pageProvider.setCurrentEntry(ficheDoc);
            }
        }

        this.ficheDoc = ficheDoc;

        if (this.ficheDoc != null) {
            navigationContext.setCurrentIdDossier(this.getIdDossier());
        }

        Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
        return null;
    }

    public DocumentModel getFiche() {
    	if(ficheDoc == null || ficheDoc.getId() != this.findDossierId(navigationContext)){
        	this.assignFiche();
    	}
        return ficheDoc;
    }

}
