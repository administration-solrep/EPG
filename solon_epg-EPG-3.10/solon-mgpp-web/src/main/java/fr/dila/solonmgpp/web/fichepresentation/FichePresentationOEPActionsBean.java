package fr.dila.solonmgpp.web.fichepresentation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

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
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.DossierService;
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
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * 
 * * Nomination > OEP
 * 
 * Communication OEP
 * 
 * bean de gestion des {@link FichePresentationOEP}
 * 
 * @author asatre
 * 
 */
@Name("fichePresentationOEPActions")
@Scope(ScopeType.CONVERSATION)
public class FichePresentationOEPActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

	private static final long							serialVersionUID		= -3042162409888469962L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER					= STLogFactory
																						.getLog(FichePresentationOEPActionsBean.class);

	private static final String[]						LOCK_UNLOCK_REFRESH_COMPONENTS_IDS	= new String[] {"representantANA4J", "representantSEA4J"};

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient EvenementCreationActionsBean	evenementCreationActions;

	@In(create = true, required = false)
	protected transient EspaceParlementaireActionsBean	espaceParlementaireActions;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient GenerationCourrierActionsBean	generationCourrierActions;

	@In(create = true, required = false)
	protected transient GenerationFicheActionsBean		generationFicheActions;

	@In(create = true, required = false)
	protected transient FilterActionsBean				filterActions;
	
	@In(create = true, required = false)
	protected transient STLockActionsBean				stLockActions;

	@In(create = true)
	protected ContentViewActions						contentViewActions;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	private DocumentModel								ficheOEP;

	private String										currentCourier;

	private Boolean										isFromTraiter;

	private Map<String, List<DocumentModel>>			mapRepresentant;

	public static final String							REPLACE_FP_OEP_EVENT	= "REPLACE_FP_OEP_EVENT";

	@In(create = true)
	protected transient DocumentsListsManager			documentsListsManager;

	public void setFicheOEP(DocumentModel ficheOEP) {
		this.ficheOEP = ficheOEP;
	}

	public DocumentModel getFicheOEPDoc() {
		String idDossier = this.findDossierId(navigationContext);

		if (StringUtils.isBlank(idDossier)) {
			return null;
		}

		return getFicheOEP(idDossier);
	}

	public DocumentModel getFicheOEP(String idDossier) {
		if (StringUtils.isBlank(idDossier)) {
			return null;
		}

		// force always reload
		try {
			mapRepresentant = null;
			FichePresentationOEP fichePresentation = SolonMgppServiceLocator.getDossierService().findFicheOEP(
					documentManager, idDossier);
			ficheOEP = fichePresentation == null ? null : fichePresentation.getDocument();
			if (fichePresentation != null) {
				navigationContext.setCurrentIdDossier(fichePresentation.getIdDossier());
			}
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
			TransactionHelper.setTransactionRollbackOnly();
		}
		return ficheOEP;
	}
	
	public void unlockDocument(DocumentModel doc) throws ClientException {

		if(checkMetasOep()) {
			stLockActions.unlockDocument(doc);
		}
	}
	
	public void unlockDocumentUnrestricted(DocumentModel doc) throws ClientException {

		if(checkMetasOep()) {
			stLockActions.unlockDocumentUnrestricted(doc);
		}
	}

	public String saveFicheOEP() {

		if (ficheOEP == null) {
			return null;
		}
		try {
			if(checkMetasOep()) {
				FichePresentationOEP fichePresentation = SolonMgppServiceLocator.getDossierService().saveFicheOEP(
						documentManager, ficheOEP, mapRepresentant.get(VocabularyConstants.REPRESENTANT_TYPE_AN),
						mapRepresentant.get(VocabularyConstants.REPRESENTANT_TYPE_SE));
				ficheOEP = fichePresentation.getDocument();
				facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
			}
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la sauvegarde de la fiche.");
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
		return null;
	}
	
	public boolean checkMetasOep() {

		String messageList="";	
		List<DocumentModel> representantsAN = mapRepresentant.get(VocabularyConstants.REPRESENTANT_TYPE_AN);
		List<DocumentModel> representantsSE = mapRepresentant.get(VocabularyConstants.REPRESENTANT_TYPE_SE);
		
		for (DocumentModel representantAN : representantsAN) {
			if (StringUtils.isBlank(representantAN.getAdapter(RepresentantOEP.class).getRepresentant())) {
				if (StringUtils.isBlank(representantAN.getAdapter(RepresentantOEP.class).getFonction())) {
					messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_NOM_AN));
					messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_FONCTION_AN));
				} else {
					messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_NOM_AN));
				}
			} else if (StringUtils.isBlank(representantAN.getAdapter(RepresentantOEP.class).getFonction())) {
				messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_FONCTION_AN));
			}
		}
		for (DocumentModel representantSE : representantsSE) {
			if (StringUtils.isBlank(representantSE.getAdapter(RepresentantOEP.class).getRepresentant())) {
				if (StringUtils.isBlank(representantSE.getAdapter(RepresentantOEP.class).getFonction())) {
					messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_NOM_SE));
					messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_FONCTION_SE));
				} else {
					messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_NOM_SE));
				}
			} else if (StringUtils.isBlank(representantSE.getAdapter(RepresentantOEP.class).getFonction())) {
				messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_REPRESENTANT_FONCTION_SE));
			}
		}
		
		if(!messageList.isEmpty()) {
			messageList = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.METAS_MANQUANTES_LIST) + " " + messageList ;
			facesMessages.add(StatusMessage.Severity.WARN, messageList);
			return false;
		}
		return true;
	}
	
	public String completeMessage(String message, String ajout) {
		if(message.isEmpty()) {
			message = ajout;
		}else {
			if (message.indexOf(ajout) ==-1) {
				message = message + ", "+ ajout;
			}
		}
		return message;
	}

	public boolean isButtonToDisplay() {
		return !documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
	}

	public boolean isDiffuse() {

		if (ficheOEP != null) {
			FichePresentationOEP fichePresentation = ficheOEP.getAdapter(FichePresentationOEP.class);
			return fichePresentation.isDiffuse();
		}
		return false;
	}

	public Boolean canCurrentUserEdit() {
		if (ficheOEP == null) {
			return Boolean.FALSE;
		}
		try {
			return filterActions.isUpdater()
					&& STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheOEP);
		} catch (ClientException e) {
			LOGGER.warn(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_PRESENTATION_TEC);
			ficheOEP = null;
			return false;
		}
	}

	public Boolean canCurrentUserLock() throws ClientException {
		if (ficheOEP == null) {
			return Boolean.FALSE;
		}
		try {
			return filterActions.isUpdater()
					&& STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheOEP,
							(NuxeoPrincipal) documentManager.getPrincipal());
		} catch (ClientException e) {
			LOGGER.warn(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_PRESENTATION_TEC);
			ficheOEP = null;
			return false;
		}
	}

	@Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
	public void resetFichePresentationOEP() {
		setFicheOEP(null);
		isFromTraiter = Boolean.FALSE;
	}

	public void setCurrentCourier(String currentCourier) {
		this.currentCourier = currentCourier;
	}

	public String getCurrentCourier() {
		return currentCourier;
	}

	public String genererCourier() throws Exception {
		return generationCourrierActions.genererCourier(currentCourier, ficheOEP,
				SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP);
	}

	/**
	 * Navigation vers la creation d'un nouveau OEP
	 * 
	 * @param evenementDTO
	 * @param isFromTraiter
	 * @return
	 * @throws ClientException
	 */
	public String navigateToCreationOEP(EvenementDTO evenementDTO, Boolean isFromTraiter) throws ClientException {
		this.isFromTraiter = isFromTraiter;
		mapRepresentant = null;

		FichePresentationOEP fichePresentationOEP = SolonMgppServiceLocator.getDossierService()
				.createFicheRepresentationOEP(documentManager, evenementDTO, !isFromTraiter);
		ficheOEP = fichePresentationOEP.getDocument();

		return SolonMgppViewConstant.VIEW_CREATE_OEP;
	}

	public DocumentModel getFicheOEPCreation() {
		return ficheOEP;
	}

	public String navigateToCreationOEP() throws ClientException {
		return navigateToCreationOEP(null, Boolean.FALSE);
	}

	public String saveCreationOEP() throws ClientException {
		try {
			SolonMgppServiceLocator.getDossierService().createFicheRepresentationOEP(documentManager, ficheOEP,
					mapRepresentant.get(VocabularyConstants.REPRESENTANT_TYPE_AN),
					mapRepresentant.get(VocabularyConstants.REPRESENTANT_TYPE_SE));
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_REPRESENTANT_OEP_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		if (isFromTraiter != null && isFromTraiter) {
			return evenementCreationActions.suivreTransitionTraite();
		}

		facesMessages.add(StatusMessage.Severity.INFO, "Oep créé.");
		Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
		return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
	}

	public String cancelCreationOEP() throws ClientException {
		this.isFromTraiter = Boolean.FALSE;
		mapRepresentant = null;
		Events.instance().raiseEvent(EspaceParlementaireActionsBean.REFRESH_PAGE);
		return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
	}

	public List<DocumentModel> fetchRepresentant(String typeRepresentant) {
		List<DocumentModel> list = null;
		if (mapRepresentant == null) {
			mapRepresentant = new HashMap<String, List<DocumentModel>>();
		}
		if (ficheOEP != null) {
			if (mapRepresentant.get(typeRepresentant) == null) {
				try {
					if (StringUtils.isBlank(ficheOEP.getId())) {
						// creation from event
						list = SolonMgppServiceLocator.getDossierService().fetchRepresentantOEP(typeRepresentant,
								navigationContext.getCurrentEvenement(), documentManager);
					} else {
						list = SolonMgppServiceLocator.getDossierService().fetchRepresentantOEP(documentManager,
								typeRepresentant, ficheOEP.getId());
					}
					mapRepresentant.put(typeRepresentant, list);
				} catch (ClientException e) {
					LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_REPRESENTANT_OEP_TEC, e);
					facesMessages.add(StatusMessage.Severity.WARN,
							"Erreur lors de la récupération des representants de type " + typeRepresentant
									+ "pour la fiche " + ficheOEP.getId());
					TransactionHelper.setTransactionRollbackOnly();
				}
			}
		}
		return mapRepresentant.get(typeRepresentant);
	}

	public String addNewRepresentant(String typeRepresentant) {

		List<DocumentModel> listDoc = mapRepresentant.get(typeRepresentant);

		if (listDoc == null) {
			listDoc = new ArrayList<DocumentModel>();
		}

		DocumentModel modelDesired = new DocumentModelImpl(ficheOEP.getPathAsString(), ""
				+ Calendar.getInstance().getTimeInMillis(), RepresentantOEP.DOC_TYPE);

		RepresentantOEP representantOEP = modelDesired.getAdapter(RepresentantOEP.class);
		representantOEP.setDateDebut(Calendar.getInstance());

		representantOEP.setType(typeRepresentant);

		listDoc.add(representantOEP.getDocument());
		mapRepresentant.put(typeRepresentant, listDoc);
		return null;
	}

	public String removeRepresentant(String typeRepresentant, DocumentModel representantDoc) {
		List<DocumentModel> listDoc = mapRepresentant.get(typeRepresentant);
		if (listDoc != null) {
			listDoc.remove(representantDoc);
		}
		mapRepresentant.put(typeRepresentant, listDoc);

		return null;
	}

	public String navigateToOep(DocumentModel oepDoc, String contentViewName) throws ClientException {
		navigationContext.resetCurrentDocument();

		// Assignation du DocumentModel au provider
		if (contentViewName != null) {
			@SuppressWarnings("unchecked")
			PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions
					.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
			List<?> currentPage = pageProvider.getCurrentPage();
			if (currentPage != null && currentPage.contains(oepDoc)) {
				pageProvider.setCurrentEntry(oepDoc);
			}
		}

		mapRepresentant = null;
		ficheOEP = oepDoc;

		if (ficheOEP != null) {
			FichePresentationOEP fichePresentation = ficheOEP.getAdapter(FichePresentationOEP.class);
			navigationContext.setCurrentIdDossier(fichePresentation.getIdDossier());
		}

		// reset tab list
		Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
		return null;
	}

	public String navigateToDetailsOep(DocumentModel oepDoc) throws ClientException {
		navigateToOep(oepDoc, null);
		return SolonMgppViewConstant.VIEW_DETAILS_OEP;
	}

	public DocumentModel getFicheOEP() {
		String idDossier = this.findDossierId(navigationContext);
		if (ficheOEP == null) {
			return getFicheOEPDoc();
		} else {
			FichePresentationOEP fiche = ficheOEP.getAdapter(FichePresentationOEP.class);
    		if (!fiche.getIdDossier().equals(idDossier)) {
    			getFicheOEPDoc();
    		}
		}
		return ficheOEP;
	}

	@Observer(REPLACE_FP_OEP_EVENT)
	public void forceReplace(DocumentModel newId) {

		ficheOEP = newId;
	}

	@Override
	protected DocumentModel getCurrentDocument() {
		return ficheOEP;
	}

	@Override
	public String reload() {
		mapRepresentant = null;
		if (ficheOEP != null) {
			try {
				ficheOEP = documentManager.getDocument(new IdRef(ficheOEP.getId()));
			} catch (ClientException e) {
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			}
		}

		return null;
	}

	public boolean canCurrentUserForceUnlock() throws ClientException {
		return !canCurrentUserEdit() && ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UNLOCKER);
	}

	@Override
	protected String addDecorationClass(DocumentModel doc, String defaultClass) {
		if (doc != null && doc.hasSchema(FichePresentationOEP.SCHEMA)) {
			FichePresentationOEP fichePresentationOEP = doc.getAdapter(FichePresentationOEP.class);
			if (fichePresentationOEP.getDateFin() != null
					&& fichePresentationOEP.getDateFin().before(Calendar.getInstance())) {
				defaultClass += " dataRowItalic";
			}
		}
		return defaultClass;
	}

	public String genererPDF() throws Exception {
		return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_OEP.getId(), ficheOEP);
	}

	public String genererXLS() throws Exception {
		return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_OEP.getId(), ficheOEP);
	}

	public boolean isUserMgppOnly() {
		return ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.DROIT_VUE_MGPP);
	}

	public void diffuserOEP() {

		try {
			FichePresentationOEP fichePresentation = SolonMgppServiceLocator.getDossierService().diffuserFicheOEP(
					documentManager, ficheOEP);
			SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(documentManager);
			ficheOEP = fichePresentation.getDocument();

		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
	}

	public void diffuserMassOEP() {
		List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		if (!documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION)) {
			try {
				SolonMgppServiceLocator.getDossierService().diffuserFicheOEP(documentManager, docs);
				SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(documentManager);

			} catch (ClientException e) {
				LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				TransactionHelper.setTransactionRollbackOnly();
			}
		}
	}

	public void annulerDiffusionOEP() {
		try {
			SolonMgppServiceLocator.getDossierService().annulerDiffusionFicheOEP(documentManager, ficheOEP);
			SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(documentManager);

		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
	}

	public void annulerMassOEP() {
		List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		if (!documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION)) {
			try {
				SolonMgppServiceLocator.getDossierService().annulerDiffusionFicheOEP(documentManager, docs);
				SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(documentManager);

			} catch (ClientException e) {
				LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				TransactionHelper.setTransactionRollbackOnly();
			}
		}
	}

	/**
	 * afficher le contenu de resultat de birt en format pdf
	 * 
	 * @return
	 * @throws Exception
	 */
	public void displayPdf() throws Exception {
		FichePresentationOEP fiche = ficheOEP.getAdapter(FichePresentationOEP.class);
		String pdfName = SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX + fiche.getIdDossier();
		DossierService dossierService = SolonMgppServiceLocator.getDossierService();
		String generatedReportPath = dossierService.getPathAPPublieRepertoire();

		File file = new File(generatedReportPath + "/" + pdfName + ".pdf");
		if (pdfName != null && file.exists()) {

			HttpServletResponse response = getHttpServletResponse();
			if (response == null) {
				return;
			}

			response.reset();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=" + fiche.getIdDossier() + ".pdf");

			// récupération réponse
			OutputStream outputStream = response.getOutputStream();

			InputStream inputStream = new FileInputStream(file);
			BufferedInputStream fif = new BufferedInputStream(inputStream);
			// copie le fichier dans le flux de sortie
			int data;
			while ((data = fif.read()) != -1) {
				outputStream.write(data);
			}
			outputStream.flush();
			outputStream.close();
			fif.close();
			FacesContext.getCurrentInstance().responseComplete();
		}
	}

	private static HttpServletResponse getHttpServletResponse() {
		ServletResponse response = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			response = (ServletResponse) facesContext.getExternalContext().getResponse();
		}
		if (response != null && response instanceof HttpServletResponse) {
			return (HttpServletResponse) response;
		}
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
