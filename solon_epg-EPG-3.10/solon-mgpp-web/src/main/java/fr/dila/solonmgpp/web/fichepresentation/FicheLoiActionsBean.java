package fr.dila.solonmgpp.web.fichepresentation;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
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
import org.richfaces.component.html.HtmlCalendar;
import org.richfaces.component.html.HtmlComboBox;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.birt.GenerationCourrierActionsBean;
import fr.dila.solonmgpp.web.birt.GenerationFicheActionsBean;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.NiveauLectureCode;

/**
 * bean de gestion des {@link FicheLoi}
 * 
 * @author asatre
 * 
 */
@Name("ficheLoiActions")
@Scope(ScopeType.CONVERSATION)
public class FicheLoiActionsBean extends FichePresentationBean implements Serializable, ReloadableBean {

	private static final long							serialVersionUID		= 1L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER					= STLogFactory
																						.getLog(FicheLoiActionsBean.class);

	private static final int							SIMPLES					= 0;

	private static final int							CMP						= 1;

	private static final int							NVLLES_LECT				= 2;

	private static final int							DERNIERES_LECT			= 3;

	private static final int							CONGRES					= 4;

	private static final String							REFUS_ENGAGEMENT_ASS1	= "REFUS_ENGAGEMENT_ASS1";
	private static final String							REFUS_ENGAGEMENT_ASS2	= "REFUS_ENGAGEMENT_ASS2";
	private static final String							ASSEMBLEE_DEPOT			= "ASSEMBLEE_DEPOT";

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

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true)
	protected ContentViewActions						contentViewActions;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	private DocumentModel								ficheLoi;

	private List<DocumentModel>							navettes;

	private String										currentCourier;

	private Boolean										isReadOnly				= false;

	private Boolean										fromOrganigramme		= false;

	public Boolean getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public void setFicheLoi(DocumentModel ficheLoi) {
		this.ficheLoi = ficheLoi;
	}

	public DocumentModel getFicheLoiDoc() {

		String idDossier = this.findDossierId(navigationContext);

		if (StringUtils.isBlank(idDossier)) {
			return null;
		}

		// force to always reload unless when selecting assemblee de depot
		if (!fromOrganigramme || ficheLoi == null) {
			try {
				FicheLoi fLoi = SolonMgppServiceLocator.getDossierService().findOrCreateFicheLoi(documentManager,
						idDossier);
				ficheLoi = fLoi.getDocument();
				navettes = SolonMgppServiceLocator.getNavetteService().fetchNavette(documentManager, ficheLoi.getId());
				navigationContext.setCurrentIdDossier(fLoi.getIdDossier());
			} catch (ClientException e) {
				LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC);
				facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
				TransactionHelper.setTransactionRollbackOnly();
			}
		}
		return ficheLoi;
	}

	public String saveFicheLoi() {
		NORService norService = SolonEpgServiceLocator.getNORService();

		if (ficheLoi == null) {
			return null;
		}
		String nor = ficheLoi.getAdapter(FicheLoi.class).getNumeroNor();
		if (!nor.isEmpty() && !norService.isStructNorValide(nor)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.NOR_STRUCTURE_INCORRECTE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return null;
		}
		
		try {
			DocumentModel oldFicheLoi = SolonMgppServiceLocator.getDossierService().
					findFicheLoiDocumentFromMGPP(documentManager, ficheLoi.getAdapter(FicheLoi.class).getIdDossier());
			String oldNor = oldFicheLoi.getAdapter(FicheLoi.class).getNumeroNor();
			FicheLoi fLoi = ficheLoi.getAdapter(FicheLoi.class);
			DocumentModel newCurrentDoc = null;
			
			// Si le Nor de la fiche loi est modifié, on met a jour le lien idDossier du dossier correspondant
			if (fLoi.getNumeroNor() != null && !fLoi.getNumeroNor().equals(oldNor) ||
					fLoi.getNumeroNor() == null && oldNor != null) {
				String idDossierLink;
				if (fLoi.getNumeroNor() != null && !fLoi.getNumeroNor().isEmpty()) {
					DocumentModel dossierDoc = norService.getDossierFromNOR(documentManager, fLoi.getNumeroNor());
					if (dossierDoc != null) {
						Dossier dossierEpg = dossierDoc.getAdapter(Dossier.class);
						idDossierLink = dossierEpg.getIdDossier();
						dossierEpg.setIdDossier(fLoi.getIdDossier());
						documentManager.saveDocument(dossierDoc);
						// si le dossier était reattaché a une fiche loi, casser le lien Nor côté fiche loi
						if (idDossierLink != null && !idDossierLink.isEmpty()) {
							DocumentModel ficheLoiLink = SolonMgppServiceLocator.getDossierService().
									findFicheLoiDocumentFromMGPP(documentManager, idDossierLink);
							FicheLoi fLoiLink = ficheLoiLink.getAdapter(FicheLoi.class);
							fLoiLink.setNumeroNor(null);
							documentManager.saveDocument(ficheLoiLink);
						}
					}
				}
				// si le numéro Nor de la version précédente de la fiche loi n'était pas nul : supprimer l'ancien lien idDossier
				if (oldNor != null && !oldNor.isEmpty()) {
					DocumentModel oldDossierDoc = norService.getDossierFromNOR(documentManager, oldNor);
					if (oldDossierDoc != null) {
						Dossier oldDossierEpg = oldDossierDoc.getAdapter(Dossier.class);
						oldDossierEpg.setIdDossier(null);
						documentManager.saveDocument(oldDossierDoc);
					}
					// null dans un cas normal - attribué si l'idDossier de la fiche loi est un nor existant
					newCurrentDoc = norService.getDossierFromNOR(documentManager, fLoi.getIdDossier());
				}
			}

			fLoi = SolonMgppServiceLocator.getDossierService().saveFicheLoi(documentManager, ficheLoi);
			ficheLoi = fLoi.getDocument();
			if (newCurrentDoc == null) {
				newCurrentDoc = norService.getDossierFromNOR(documentManager, fLoi.getNumeroNor());
			}
			navigationContext.setCurrentDocument(newCurrentDoc);

			if (navettes != null) {
				for (DocumentModel navetteDoc : navettes) {
					documentManager.saveDocument(navetteDoc);
				}
			}

			facesMessages.add(StatusMessage.Severity.INFO, "Fiche enregistrée.");
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
			TransactionHelper.setTransactionRollbackOnly();
		}
		return null;
	}

	public void addInstitution(String field, String valueToAdd) {
		if (ficheLoi == null) {
			return;
		}
		FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);
		if (REFUS_ENGAGEMENT_ASS1.equals(field)) {
			fiche.setRefusEngagementProcAss1(valueToAdd);
		} else if (REFUS_ENGAGEMENT_ASS2.equals(field)) {
			fiche.setRefusEngagementProcAss2(valueToAdd);
		} else if (ASSEMBLEE_DEPOT.equals(field)) {
			fiche.setAssembleeDepot(valueToAdd);
		}
		ficheLoi = fiche.getDocument();
		fromOrganigramme = true;
	}

	public void removeInstitution(String field) {
		if (ficheLoi == null) {
			return;
		}
		FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);

		if (REFUS_ENGAGEMENT_ASS1.equals(field)) {
			fiche.setRefusEngagementProcAss1(null);
		} else if (REFUS_ENGAGEMENT_ASS2.equals(field)) {
			fiche.setRefusEngagementProcAss2(null);
		} else if (ASSEMBLEE_DEPOT.equals(field)) {
			fiche.setAssembleeDepot(null);
		}

		ficheLoi = fiche.getDocument();
		fromOrganigramme = true;
	}

	public String publierFicheLoi() {

		if (ficheLoi == null) {
			return null;
		}
		try {
			FicheLoi fLoi = SolonMgppServiceLocator.getDossierService().publierFicheLoi(documentManager, ficheLoi);
			ficheLoi = fLoi.getDocument();
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, "Erreur lors de la récupération de la fiche.");
			TransactionHelper.setTransactionRollbackOnly();
		}

		return null;
	}

	public Boolean canCurrentUserEdit() throws ClientException {
		if (ficheLoi == null || getIsReadOnly()) {
			return Boolean.FALSE;
		}

		return STServiceLocator.getSTLockService().isLockByCurrentUser(documentManager, ficheLoi);
	}

	public Boolean canCurrentUserLock() throws ClientException {
		if (ficheLoi == null) {
			return Boolean.FALSE;
		}

		return STServiceLocator.getSTLockService().isLockActionnableByUser(documentManager, ficheLoi,
				(NuxeoPrincipal) documentManager.getPrincipal());
	}

	public void setNavettes(List<DocumentModel> navettes) {
		this.navettes = navettes;
	}

	public List<DocumentModel> getNavettes() {
		return navettes;
	}

	/**
	 * Récupère les navettes correspondant au type passé
	 */
	private List<DocumentModel> getNavettesType(int type) {
		List<DocumentModel> navettesType = new ArrayList<DocumentModel>();
		if (navettes != null) {
			List<String> comparators = new ArrayList<String>();
			switch (type) {
				case SIMPLES:
					comparators.add(NiveauLectureCode.AN.name());
					comparators.add(NiveauLectureCode.SENAT.name());
					break;
				case CMP:
					comparators.add(NiveauLectureCode.CMP.name());
					break;
				case NVLLES_LECT:
					comparators.add(NiveauLectureCode.NOUVELLE_LECTURE_AN.name());
					comparators.add(NiveauLectureCode.NOUVELLE_LECTURE_SENAT.name());
					break;
				case DERNIERES_LECT:
					comparators.add(NiveauLectureCode.LECTURE_DEFINITIVE.name());
					break;
				case CONGRES:
					comparators.add(NiveauLectureCode.CONGRES.name());
					break;
				default:
					break;
			}

			List<DocumentModel> navettesLast = new ArrayList<DocumentModel>();
			for (DocumentModel navDoc : navettes) {
				Navette nav = navDoc.getAdapter(Navette.class);
				if (nav != null) {
					String codeLect = nav.getCodeLecture();
					if (comparators.contains(codeLect) && nav.getDateNavette() != null) {
						navettesType.add(navDoc);
					}

					if (comparators.contains(codeLect) && nav.getDateNavette() == null) {
						navettesLast.add(navDoc);
					}
				}
			}
			navettesType.addAll(navettesLast);
		}
		return navettesType;
	}

	/**
	 * Récupère les navettes de lecture simple
	 */
	public List<DocumentModel> getNavettesSimples() {
		return getNavettesType(SIMPLES);
	}

	/**
	 * Récupère les navettes CMP
	 */
	public List<DocumentModel> getNavettesCMP() {
		return getNavettesType(CMP);
	}

	/**
	 * Récupère les navettes nouvelles lecture
	 */
	public List<DocumentModel> getNavettesNvllesLect() {
		return getNavettesType(NVLLES_LECT);
	}

	/**
	 * Récupère les navettes lecture définitive
	 */
	public List<DocumentModel> getNavettesDernieresLect() {
		return getNavettesType(DERNIERES_LECT);
	}

	/**
	 * Récupère les navettes congrès
	 */
	public List<DocumentModel> getNavettesCongres() {
		return getNavettesType(CONGRES);
	}

	@Observer(ProviderBean.RESET_CONTENT_VIEW_EVENT)
	public void resetFicheLoi() {
		setFicheLoi(null);
	}

	public void setCurrentCourier(String currentCourier) {
		this.currentCourier = currentCourier;
	}

	public String getCurrentCourier() {
		return currentCourier;
	}

	public String genererCourier() throws Exception {
		return generationCourrierActions.genererCourier(currentCourier, ficheLoi,
				SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI);
	}

	@Override
	public String reload() {
		if (ficheLoi != null) {
			try {
				ficheLoi = documentManager.getDocument(new IdRef(ficheLoi.getId()));
				navettes = SolonMgppServiceLocator.getNavetteService().fetchNavette(documentManager, ficheLoi.getId());
			} catch (ClientException e) {
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			}
		}

		return null;
	}

	public String navigateToFicheLoi(DocumentModel fLoiDoc, String contentViewName) throws ClientException {
		navigationContext.resetCurrentDocument();
		ficheLoi = fLoiDoc;

		// Assignation du DocumentModel au provider
		if (contentViewName != null) {
			@SuppressWarnings("unchecked")
			PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentViewActions
					.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
			List<?> currentPage = pageProvider.getCurrentPage();
			if (currentPage != null && currentPage.contains(fLoiDoc)) {
				pageProvider.setCurrentEntry(fLoiDoc);
			}
		}

		// load dossier from fiche loi
		FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);
		navettes = SolonMgppServiceLocator.getNavetteService().fetchNavette(documentManager, ficheLoi.getId());
		navigationContext.setCurrentIdDossier(fiche.getIdDossier());
		setIsReadOnly(false);

		try {
			// try by idDossier
			Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager,
					fiche.getIdDossier());
			if (dossier == null) {
				// try by nor
				dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager,
						fiche.getNumeroNor());
			}
			if (dossier != null) {
				List<DocumentModel> dossierLinkList = SolonEpgServiceLocator.getCorbeilleService().findDossierLink(
						documentManager, dossier.getDocument().getId());
				DocumentModel dossierLinkDoc = null;
				if (dossierLinkList.size() > 0) {
					if (dossierLinkList.size() > 1) {
						LOGGER.info(documentManager, STLogEnumImpl.ANO_DL_MULTI_TEC);
					}
					dossierLinkDoc = dossierLinkList.get(0);
				}
				if (dossierLinkDoc != null) {
					corbeilleActions.setCurrentDossierLink(dossierLinkDoc.getAdapter(DossierLink.class));
				}
				navigationContext.setCurrentDocument(dossier.getDocument());
			}
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, fiche.getIdDossier(), e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}

		// reset tab list
		Events.instance().raiseEvent(EventNames.LOCATION_SELECTION_CHANGED);
		return null;
	}

	public DocumentModel getFicheLoi() {
		if (ficheLoi == null) {
			getFicheLoiDoc();
		}
		return ficheLoi;
	}

	@Override
	protected DocumentModel getCurrentDocument() {
		return ficheLoi;
	}

	public boolean canCurrentUserForceUnlock() throws ClientException {
		return !canCurrentUserEdit() && ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UNLOCKER);
	}

	@Override
	protected String addDecorationClass(DocumentModel doc, String defaultClass) {
		return defaultClass;
	}

	public String genererPDF() throws Exception {
		return generationFicheActions.genererFichePDF(FicheReportsEnum.FICHE_LOI.getId(), ficheLoi);
	}

	public String genererXLS() throws Exception {
		return generationFicheActions.genererFicheXLS(FicheReportsEnum.FICHE_LOI.getId(), ficheLoi);
	}

	/**
	 * Ajoute une date CMP à une navette.
	 * 
	 * @param widgetId
	 *            Champ contenant la date
	 * @param navetteDoc
	 * @throws ClientException
	 */
	public void addNavetteDateCMP(String widgetId, DocumentModel navetteDoc) throws ClientException {
		if (navettes == null || !navettes.contains(navetteDoc)) {
			return;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		String value = (String) ((HtmlCalendar) findComponent(root, widgetId)).getSubmittedValue();
		if (value == null || value.isEmpty()) {
			return;
		}

		SimpleDateFormat sdf = DateUtil.simpleDateFormat("dd/MM/yyyy");
		Calendar calendar = new GregorianCalendar();
		try {
			Date date = sdf.parse(value);
			calendar.setTime(date);
		} catch (Exception e) {
			return;
		}

		Navette navette = navetteDoc.getAdapter(Navette.class);
		List<Calendar> dateList = navette.getDateCMP();
		if (!dateList.contains(calendar)) {
			dateList.add(calendar);
			navette.setDateCMP(dateList);
		}

		documentManager.saveDocument(navetteDoc);
	}

	/**
	 * Supprime une date CMP d'une navette.
	 * 
	 * @param value
	 *            Date à supprimer
	 * @param navetteDoc
	 *            Navette
	 * @throws ClientException
	 */
	public void removeNavetteDateCMP(Calendar value, DocumentModel navetteDoc) throws ClientException {
		if (navettes == null || !navettes.contains(navetteDoc)) {
			return;
		}

		Navette navette = navetteDoc.getAdapter(Navette.class);
		List<Calendar> dateList = navette.getDateCMP();
		dateList.remove(value);
		navette.setDateCMP(dateList);

		documentManager.saveDocument(navetteDoc);
	}

	/**
	 * Ajoute un chargé mission à la ficheLoi
	 * 
	 * @param widgetId
	 *            Champ contenant le nom
	 * @param listId
	 *            la liste qui contiendra le nom sur la page
	 * @throws ClientException
	 */
	public void addChargeMission(String widgetId, String listId) throws ClientException {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		UIComponent uiComponent = findComponent(root, widgetId);

		String text = (String) ((HtmlComboBox) uiComponent).getSubmittedValue();
		if (text == null || text.isEmpty()) {
			return;
		}
		if (StringUtils.isNotBlank(text)) {
			FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);
			if (fiche != null) {
				fiche.setNomCompletChargeMission(text);

			}

			UIComponent base = ComponentUtils.getBase(findComponent(root, listId));
			UIEditableList list = ComponentUtils.getComponent(base, listId, UIEditableList.class);
			for (int i = list.getRowCount(); i > 0; i--) {
				list.removeValue(i - 1);
			}
			list.addValue(text);
			// clear input
			((HtmlComboBox) uiComponent).setSubmittedValue(null);
		}
	}

	/**
	 * Supprime un charge mission
	 * 
	 * @param listId
	 *            la liste des chargesMission sur la page de la fiche
	 * @param index
	 *            l'index dans la liste à supprimer
	 * @param value
	 *            nom à supprimer
	 * @throws ClientException
	 */
	public void removeChargeMission(String listId, String index, String value) throws ClientException {
		FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);
		if (fiche != null) {
			fiche.setNomCompletChargeMission(null);
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

	/**
	 * Retourne la liste des chargés missions contenus dans la table de référence
	 * 
	 */
	public List<String> getChargesMissionReference() throws ClientException {
		final STUserService stUserService = STServiceLocator.getSTUserService();

		Set<String> listNomChargesMission = new HashSet<String>();

		TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
		DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(documentManager);
		TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
		if (tableReference.getChargesMission() != null) {
			for (String idUser : tableReference.getChargesMission()) {
				String userInfo = stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM);
				if (StringUtils.isNotEmpty(userInfo)) {
					listNomChargesMission.add(userInfo);
				}
			}
		}
		return new ArrayList<String>(listNomChargesMission);
	}

}
