package fr.dila.solonmgpp.web.fichepresentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

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
import org.nuxeo.ecm.platform.ui.web.component.list.UIEditableList;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.birt.GenerationCourrierActionsBean;
import fr.dila.solonmgpp.web.birt.GenerationFicheActionsBean;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * RAPORT > DEPOT DE RAPPORT
 * 
 * Communication RAP
 * 
 * bean de gestion des {@link FichePresentationDR}
 * 
 * @author asatre
 * 
 */
@Name("fichePresentationDRActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationDRActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

	private static final long							serialVersionUID	= 1L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER				= STLogFactory
																					.getLog(FichePresentationDRActionsBean.class);

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient GenerationCourrierActionsBean	generationCourrierActions;

	@In(create = true, required = false)
	protected transient GenerationFicheActionsBean		generationFicheActions;

	@In(create = true)
	protected ContentViewActions						contentViewActions;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@In(create = true, required = true)
	protected transient CorbeilleActionsBean			corbeilleActions;

	private DocumentModel								ficheDR;

	private String										currentCourier;

	public void setFicheDR(DocumentModel ficheDR) {
		this.ficheDR = ficheDR;
	}

	public DocumentModel getFicheDRDoc() {

		String idDossier = this.findDossierId(navigationContext);

		if (StringUtils.isBlank(idDossier)) {
			return null;
		}

		try {
			FichePresentationDR fichePresentation = SolonMgppServiceLocator.getDossierService().findOrCreateFicheDR(
					documentManager, idDossier);
			ficheDR = fichePresentation != null ? fichePresentation.getDocument() : null;
			navigationContext.setCurrentIdDossier(fichePresentation != null ? fichePresentation.getIdDossier() : null);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
			if (e.getMessage().startsWith("Plusieurs fiches DR trouvées pour")) {
				facesMessages.add(StatusMessage.Severity.WARN, "Plusieurs fiches portent ce numéro NOR comme identifiant.");
			}
			TransactionHelper.setTransactionRollbackOnly();
		}
		return ficheDR;
	}

	public DocumentModel getFicheDR() {
		if (ficheDR == null) {
			getFicheDRDoc();
		}
		return ficheDR;
	}

	public String navigateToDR(DocumentModel drDoc, String contentViewName) throws ClientException {
		navigationContext.resetCurrentDocument();
		ficheDR = drDoc;

		// Assignation du DocumentModel au provider
		if (contentViewName != null) {
			@SuppressWarnings("unchecked")
			PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions
					.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
			List<?> currentPage = pageProvider.getCurrentPage();
			if (currentPage != null && currentPage.contains(drDoc)) {
				pageProvider.setCurrentEntry(drDoc);
			}
		}

		FichePresentationDR fiche = ficheDR.getAdapter(FichePresentationDR.class);
		String idDossier = fiche.getIdDossier();
		navigationContext.setCurrentIdDossier(idDossier);
		try {
			Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager,
					idDossier);
			if (dossier == null) {
				// try by nor
				dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, idDossier);
			}
			if (dossier != null) {
				navigationContext.setCurrentDocument(dossier.getDocument());
				corbeilleActions.readDossierLink(null, dossier.getDocument().getId(), true, null, null);
			}
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
		// reset tab list
		Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
		return null;
	}

	public String saveFicheDR() {

		if (ficheDR == null) {
			return null;
		}
		try {
			FichePresentationDR fibcePresentaion = SolonMgppServiceLocator.getDossierService().saveFicheDR(
					documentManager, ficheDR);
			ficheDR = fibcePresentaion.getDocument();
			facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
			TransactionHelper.setTransactionRollbackOnly();
		}

		return null;
	}

	public String publierFicheDR() {

		if (ficheDR == null) {
			return null;
		}
		try {
			FichePresentationDR fichePresentation = SolonMgppServiceLocator.getDossierService().publierFicheDR(
					documentManager, ficheDR);
			ficheDR = fichePresentation.getDocument();
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
			TransactionHelper.setTransactionRollbackOnly();
		}

		return null;
	}

	public Boolean canCurrentUserEdit() throws ClientException {
		if (ficheDR == null) {
			return Boolean.FALSE;
		}

		return STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheDR);
	}

	public Boolean canCurrentUserLock() throws ClientException {
		if (ficheDR == null) {
			return Boolean.FALSE;
		}

		return STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheDR,
				(NuxeoPrincipal) documentManager.getPrincipal());
	}

	@Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
	public void resetFichePresentationDR() {
		setFicheDR(null);
	}

	public void setCurrentCourier(String currentCourier) {
		this.currentCourier = currentCourier;
	}

	public String getCurrentCourier() {
		return currentCourier;
	}

	public String genererCourier() throws Exception {
		return generationCourrierActions.genererCourier(currentCourier, ficheDR,
				SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DEPOT_RAPPORT);
	}

	@Override
	public String reload() {
		if (ficheDR != null) {
			try {
				ficheDR = documentManager.getDocument(new IdRef(ficheDR.getId()));
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
		return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_RAPPORT.getId(), ficheDR);
	}

	public String genererXLS() throws Exception {
		return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_RAPPORT.getId(), ficheDR);
	}

	@Override
	protected String addDecorationClass(DocumentModel doc, String defaultClass) {
		return defaultClass;
	}

	@Override
	protected DocumentModel getCurrentDocument() {
		return ficheDR;
	}

	@SuppressWarnings("unchecked")
	public void addPoleItem(String widgetId, String listId) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		UIComponent uiComponent = findComponent(root, widgetId);

		String text = (String) ((UIInput) uiComponent).getSubmittedValue();
		if (text == null || text.isEmpty()) {
			return;
		}
		if (StringUtils.isNotBlank(text)) {
			FichePresentationDR fiche = ficheDR.getAdapter(FichePresentationDR.class);
			if (fiche != null) {
				List<String> listPole = fiche.getPole();
				if (listPole == null) {
					listPole = new ArrayList<String>();
				}
				if (!listPole.contains(text)) {
					listPole.add(text);
					fiche.setPole(listPole);
				}
			}

			UIComponent base = ComponentUtils.getBase(findComponent(root, listId));
			UIEditableList list = ComponentUtils.getComponent(base, listId, UIEditableList.class);

			if (list != null) {
				List<Object> dataList = (List<Object>) list.getEditableModel().getWrappedData();
				if (!dataList.contains(text)) {
					list.addValue(text);
				}
			}

			// clear input
			((UIInput) uiComponent).setSubmittedValue(null);
		}
	}

	public void removePoleItem(String listId, String index, String value) {
		FichePresentationDR fiche = ficheDR.getAdapter(FichePresentationDR.class);
		if (fiche != null) {
			fiche.getPole().remove(value);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		UIComponent base = ComponentUtils.getBase(findComponent(root, listId));
		UIEditableList list = ComponentUtils.getComponent(base, listId, UIEditableList.class);

		if (list != null) {
			// remove selected value from the list
			if (StringUtils.isNotBlank(index)) {
				list.getEditableModel().removeValue(Integer.valueOf(index).intValue());
			}
		}
	}

	public String navigateToDetailsDR(DocumentModel doc) throws ClientException {
		navigateToDR(doc, null);
		return SolonMgppViewConstant.VIEW_DETAILS_DR;
	}
}
