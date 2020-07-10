package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.action.WebActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueApplicationOrdonnanceDTO;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueLoiDTO;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueOrdonnanceDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.birt.BirtReportActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de l'espace d'activite normative
 * 
 * @author asatre
 */
@Name("activiteNormativeActions")
@Scope(ScopeType.CONVERSATION)
public class ActiviteNormativeActionsBean implements Serializable {

	private static final long serialVersionUID = 2776349205588506388L;

	private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeActionsBean.class);

	@In(create = true, required = false)
	protected transient ActionManager actionManager;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean navigationWebActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean navigationContext;

	@In(create = true, required = false)
	protected transient WebActionsBean webActions;

	@In(create = true, required = false)
	protected transient EspaceActiviteNormativeTreeBean espaceActiviteNormativeTree;

	@In(create = true, required = true)
	protected transient CoreSession documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor resourcesAccessor;

	@In(create = true, required = false)
	protected transient ActiviteNormativeStatsActionsBean activiteNormativeStatsActions;

	@In(create = false, required = false)
	protected transient TableauProgrammationActionsBean tableauProgrammationActions;

	@In(create = true, required = false)
	protected transient TexteMaitreActionsBean texteMaitreActions;

	@In(create = true, required = true)
	protected transient BirtReportActionsBean birtReportingActions;

	@In(required = true, create = true)
	protected SSPrincipal ssPrincipal;

	private List<Action> tabsActionsList;

	private List<Action> subTabsActionsList;

	/**
	 * Onglet courant de la partie haute
	 */
	private Action currentTabAction;

	/**
	 * Onglet courant de la partie basse
	 */
	private Action currentSubTabAction;

	protected String activiteNormativeNor;

	/**
	 * Initialise le contexte de l'espace de l'activite normative
	 * 
	 * @param currentTabId
	 */
	protected void initEspaceActiviteNormative() throws ClientException {
		// Renseigne le menu du haut
		Action mainMenuAction = actionManager.getAction(ActiviteNormativeConstants.MAIN_MENU_ACTIVITE_NORMATIVE_ACTION);
		navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

		// Renseigne le menu de gauche
		Action leftMenuAction = actionManager.getAction(ActiviteNormativeConstants.LEFT_MENU_ACTIVITE_NORMATIVE_ACTION);
		navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

		// Réinitialise le document en cours
		navigationContext.resetCurrentDocument();

		navigationWebActions.setCurrentLeftMenuItemAction(null);

		texteMaitreActions.setCurrentMinistere(null);
	}

	/**
	 * reset du sub tab courant
	 * 
	 * @throws ClientException
	 */
	public void resetCurrentSubTabAction() throws ClientException {
		subTabsActionsList = webActions.getActionsList(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ACTION_SUB_TAB);
		if (subTabsActionsList != null && !subTabsActionsList.isEmpty()) {
			setCurrentSubTabAction(subTabsActionsList.get(0));
		} else {
			currentSubTabAction = null;
		}
		activiteNormativeNor = null;
		// Réinitialise le document en cours
		navigationContext.resetCurrentDocument();
	}

	/**
	 * reset du tab courant
	 * 
	 * @throws ClientException
	 */
	public void resetCurrentTabAction() throws ClientException {
		if (tabsActionsList != null && !tabsActionsList.isEmpty()) {
			setCurrentTabAction(tabsActionsList.get(0));
		} else {
			setCurrentTabAction(null);
		}
		activiteNormativeNor = null;
	}

	/**
	 * Navigue vers l'espace d'activite normative.
	 * 
	 * @param string
	 * 
	 * @return Écran d'accueil de l'espace de l'activite normative
	 * @throws ClientException
	 */
	public String navigateToEspaceActiviteNormative() throws ClientException {
		initEspaceActiviteNormative();
		return espaceActiviteNormativeTree.getCurrentView();
	}

	public List<Action> getTabsActionsList() {
		return tabsActionsList;
	}

	public void setTabsActionsList(List<Action> tabsActionsList) {
		this.tabsActionsList = tabsActionsList;
	}

	public List<Action> getSubTabsActionsList() {
		return subTabsActionsList;
	}

	public void setSubTabsActionsList(List<Action> subTabsActionsList) {
		this.subTabsActionsList = subTabsActionsList;
	}

	public Action getCurrentTabAction() {
		return currentTabAction;
	}

	public void setCurrentTabAction(Action currentTabAction) throws ClientException {
		resetCurrentSubTabAction();
		this.currentTabAction = currentTabAction;
		texteMaitreActions.setCurrentMinistere(null);
		activiteNormativeStatsActions.resetCurrentSubTabAction();
	}

	public Action getCurrentSubTabAction() {
		return currentSubTabAction;
	}

	public void setCurrentSubTabAction(Action currentSubTabAction) throws ClientException {
		this.currentSubTabAction = currentSubTabAction;
		activiteNormativeStatsActions.setCurrentSubTabAction(currentSubTabAction);
	}

	public String navigateToItem(String itemId) throws ClientException {
		tabsActionsList = webActions.getActionsList(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ACTION_TAB);
		resetCurrentTabAction();
		return espaceActiviteNormativeTree.getCurrentView();
	}

	/**
	 * 
	 * @return le nom de la contentview par rapport a l'item selectionné dans
	 *         l'arbre et l'onglet
	 */
	public String getContentViewName() {
		return espaceActiviteNormativeTree.getCurrentContentViewName(currentTabAction);
	}

	public String addToActiviteNormative() throws ClientException {
		ActiviteNormativeEnum currentMenu = espaceActiviteNormativeTree.getCurrentItem()
				.getEspaceActiviteNormativeEnum();
		if (StringUtils.isNotEmpty(activiteNormativeNor) && currentMenu != null) {
			try {
				SolonEpgServiceLocator.getActiviteNormativeService().addDossierToTableauMaitre(
						activiteNormativeNor.trim(), currentMenu.getTypeActiviteNormative(), documentManager);
				activiteNormativeNor = null;
				if (ActiviteNormativeEnum.APPICATION_DES_LOIS.equals(currentMenu)
						|| ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE.equals(currentMenu)) {
					SolonEpgServiceLocator.getActiviteNormativeService().updateLoiListePubliee(documentManager, true);
				} else if (ActiviteNormativeEnum.ORDONNANCES_38C.equals(currentMenu)) {
					SolonEpgServiceLocator.getActiviteNormativeService().updateHabilitationListePubliee(documentManager,
							true);
				} else if (ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.equals(currentMenu)
						|| ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE.equals(currentMenu)) {
					SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(documentManager,
							true);
				}
			} catch (ActiviteNormativeException e) {
				String message = resourcesAccessor.getMessages().get(e.getMessage());
				facesMessages.add(StatusMessage.Severity.WARN, message);
			} catch (CaseManagementRuntimeException e1) {
				String message = "Le dossier est verouillé par un autre utilisateur, ajout impossible";

				LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_DOCUMENT_TEC, message, e1);
				facesMessages.add(StatusMessage.Severity.WARN, message);
			}
		}

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		return getContentViewName();
	}

	public String removeFromActiviteNormative(DocumentModel doc) throws ClientException {
		ActiviteNormativeEnum currentMenu = espaceActiviteNormativeTree.getCurrentItem()
				.getEspaceActiviteNormativeEnum();
		if (currentMenu != null) {
			try {
				SolonEpgServiceLocator.getActiviteNormativeService().removeActiviteNormativeFrom(doc,
						currentMenu.getTypeActiviteNormative(), documentManager);
				String message = resourcesAccessor.getMessages().get("activite.normative.supprimé");
				facesMessages.add(StatusMessage.Severity.INFO, message);
				navigationContext.setCurrentDocument(null);
				if (ActiviteNormativeEnum.APPICATION_DES_LOIS.equals(currentMenu)) {
					SolonEpgServiceLocator.getActiviteNormativeService().updateLoiListePubliee(documentManager, true);
				} else if (ActiviteNormativeEnum.ORDONNANCES_38C.equals(currentMenu)) {
					SolonEpgServiceLocator.getActiviteNormativeService().updateHabilitationListePubliee(documentManager,
							true);
				} else if (ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.equals(currentMenu)) {
					SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(documentManager,
							true);
				}
			} catch (CaseManagementRuntimeException e1) {
				String message = "Le dossier est verouillé par un autre utilisateur, suppression impossible";

				LOGGER.error(documentManager, STLogEnumImpl.FAIL_DEL_DOC_TEC, message, e1);
				facesMessages.add(StatusMessage.Severity.WARN, message);
			}
		}

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		return getContentViewName();
	}

	public String getActiviteNormativeNor() {
		return activiteNormativeNor;
	}

	public void setActiviteNormativeNor(String activiteNormativeNor) {
		this.activiteNormativeNor = activiteNormativeNor;
	}

	/**
	 * Mode d'affichage des widgets dans les subTabs
	 * 
	 * @return
	 */
	public Boolean isEditMode() {
		// Vérifie que le texteMaitre en cours est verrouillé
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (doc != null) {
			TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
			return SolonEpgServiceLocator.getActiviteNormativeService().isTexteMaitreLockByCurrentUser(texteMaitre,
					documentManager);
		}
		return false;
	}

	public Boolean isEditModeForBordereau() {
		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE)
				&& !ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)) {
			return false;
		} else {
			return isEditMode();
		}
	}

	public Boolean isEditModeForBordereauAppOrdonnances() {
		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE)
				&& !ssPrincipal.isMemberOf(
						SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER)) {
			return false;
		} else {
			return isEditMode();
		}
	}

	public Boolean isInApplicationDesLois() {
		if (espaceActiviteNormativeTree.getCurrentItem() != null) {
			return ActiviteNormativeEnum.APPICATION_DES_LOIS
					.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum())
					|| ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE
							.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum());
		}
		return Boolean.FALSE;
	}

	public Boolean isInApplicationDesOrdonnances() {
		if (espaceActiviteNormativeTree.getCurrentItem() != null) {
			return ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES
					.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum())
					|| ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE
							.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum());

		}
		return Boolean.FALSE;
	}

	public Boolean isInApplicationDesLoisOrOrdonnances() {
		return isInApplicationDesLois() || isInApplicationDesOrdonnances();
	}

	public Boolean isInOrdonnances() {
		if (espaceActiviteNormativeTree.getCurrentItem() != null) {
			return ActiviteNormativeEnum.ORDONNANCES
					.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum());
		}
		return Boolean.FALSE;
	}

	public Boolean isInOrdonnances38C() {
		if (espaceActiviteNormativeTree.getCurrentItem() != null) {
			return ActiviteNormativeEnum.ORDONNANCES_38C
					.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum());
		}
		return Boolean.FALSE;
	}

	public Boolean isInTransposition() {
		if (espaceActiviteNormativeTree.getCurrentItem() != null) {
			return ActiviteNormativeEnum.TRANSPOSITION
					.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum());
		}
		return Boolean.FALSE;
	}

	public Boolean isInTraiteAccord() {
		if (espaceActiviteNormativeTree.getCurrentItem() != null) {
			return ActiviteNormativeEnum.TRAITES_ET_ACCORDS
					.equals(espaceActiviteNormativeTree.getCurrentItem().getEspaceActiviteNormativeEnum());
		}
		return Boolean.FALSE;
	}

	public Boolean isInTexteMaitre() {
		if (currentSubTabAction != null) {
			return currentSubTabAction.getId().startsWith(ActiviteNormativeConstants.TAB_AN_TEXTE_MAITRE);
		}
		return Boolean.FALSE;
	}

	public Boolean isInTableauLois() {

		if (currentTabAction != null) {

			return currentTabAction.getId().startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_LOIS);
		}

		return Boolean.FALSE;
	}

	public Boolean isInTableauOrdonnances() {

		if (currentTabAction != null) {

			return currentTabAction.getId().startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_ORDONNANCES);
		}

		return Boolean.FALSE;
	}

	public Boolean isInTableauProgrammation() {
		if (currentSubTabAction != null) {
			return currentSubTabAction.getId().startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_PROGRAMMATION);
		}
		return Boolean.FALSE;
	}

	public Boolean isInTableauSuivi() {
		if (currentSubTabAction != null) {
			return currentSubTabAction.getId().startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_SUIVI);
		}
		return Boolean.FALSE;
	}

	public Boolean showLockCurrentDossier() {
		return isInTexteMaitre();
	}

	public List<Action> getUserActionsList() {
		return webActions.getActionsList(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ACTION_TOOLBAR);
	}

	public Boolean isUtilisateurMinistereLoi() {
		return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE);
	}

	public Boolean isUtilisateurMinistereOrdonnance() {
		return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE);
	}

	/**
	 * Retourne vrai si l'utilisateur courant n'a que des droits du type
	 * ministère
	 * 
	 * @return
	 */
	public Boolean isOnlyUtilisateurMinistereLoiOrOrdonnance() {
		if ((ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE)
				&& !ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER))
				|| (ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE)
						&& !ssPrincipal.isMemberOf(
								SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Validation de la valeur du ministere
	 * 
	 * @param facesContext
	 * @param uIComponent
	 * @param object
	 * @throws ValidatorException
	 */
	public void validateMinistere(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
		String value = (String) object;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			// ce n'est pas un numero donc un NOR a été trouver par le converter
			FacesMessage message = new FacesMessage();
			message.setSummary("Le nor ne correspond pas à un ministère.");
			throw new ValidatorException(message);
		}
	}

	/**
	 * Construction de la fiche signaletique d'une loi
	 * 
	 * @return
	 * @throws ClientException
	 */
	public FicheSignaletiqueLoiDTO getCurrentFicheSignaletiqueLoi() throws ClientException {
		return new FicheSignaletiqueLoiDTO(navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class),
				documentManager);
	}

	/**
	 * Construction de la fiche signaletique d'une application d'ordonnance
	 * 
	 * @return
	 * @throws ClientException
	 */
	public FicheSignaletiqueApplicationOrdonnanceDTO getCurrentFicheSignaletiqueApplicationOrdonnance()
			throws ClientException {
		return new FicheSignaletiqueApplicationOrdonnanceDTO(
				navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
	}

	/**
	 * Construction de la fiche signaletique d'une ordonnance
	 * 
	 * @return
	 * @throws ClientException
	 */
	public FicheSignaletiqueOrdonnanceDTO getCurrentFicheSignaletiqueOrdonnance() throws ClientException {
		return new FicheSignaletiqueOrdonnanceDTO(
				navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
	}

	public Boolean isActiviteNormativeUpdater() {
		if (ssPrincipal != null) {
			return isInApplicationDesLois()
					&& ssPrincipal
							.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)
					|| isInOrdonnances() && ssPrincipal
							.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER)
					|| isInOrdonnances38C() && ssPrincipal
							.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER)
					|| isInTraiteAccord() && ssPrincipal
							.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER)
					|| isInTransposition() && ssPrincipal
							.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER)
					|| isInApplicationDesOrdonnances() && ssPrincipal.isMemberOf(
							SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER);
		}

		return Boolean.FALSE;
	}

	/**
	 * Verrouillage du document courant
	 * 
	 * @throws ClientException
	 */
	public void lockCurrentDocument() throws ClientException {
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (doc != null) {
			TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
			texteMaitre = SolonEpgServiceLocator.getActiviteNormativeService().lockTexteMaitre(texteMaitre,
					documentManager);
			navigationContext.setCurrentDocument(null);
			navigationContext.setCurrentDocument(texteMaitre.getDocument());
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		}
	}

	/**
	 * Déverrouillage du document courant
	 * 
	 * @throws ClientException
	 */
	public void unlockCurrentDocument() throws ClientException {
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (doc != null) {
			TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
			texteMaitre = SolonEpgServiceLocator.getActiviteNormativeService().unlockTexteMaitre(texteMaitre,
					documentManager);
			navigationContext.setCurrentDocument(null);
			navigationContext.setCurrentDocument(texteMaitre.getDocument());

			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		}
	}

	public Boolean isTexteMaitreLocked() {
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (doc != null) {
			TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
			return texteMaitre.getDocLockUser() != null;
		}
		return Boolean.FALSE;
	}

	public String getCurrentLockTime() {
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (doc != null) {
			TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
			Calendar cal = texteMaitre.getDocLockDate();
			if (cal != null) {
				return DateUtil.formatForClient(cal.getTime());
			}
		}

		return null;
	}

	public String getCurrentLockOwnerInfo() {
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (doc != null) {
			TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
			STUserService userService = STServiceLocator.getSTUserService();
			return userService.getUserFullName(texteMaitre.getDocLockUser());
		}

		return null;
	}

	public void genererCsv() throws Exception {

		String dossierId = "";
		String ministerePilote = "";

		if (navigationContext != null && navigationContext.getCurrentDocument() != null) {
			dossierId = navigationContext.getCurrentDocument().getId();
		}

		if (texteMaitreActions != null && texteMaitreActions.getCurrentMinistere() != null) {
			ministerePilote = texteMaitreActions.getCurrentMinistere().getId();
		}

		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("MINISTEREPILOTE_PARAM", ministerePilote);
		inputValues.put("DOSSIERID_PARAM", dossierId);

		String reportFile = null;
		String name = "";
		if (this.isInApplicationDesLois()) {
			if (this.isInTexteMaitre()) {
				reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DE_LOIS_TEXT_MAITRE_REPORT_FILE;
				name = "textMaitre";
			}
			if (this.isInTableauLois()) {
				reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DE_LOIS_TABLEAU_LOIS_REPORT_FILE;
				name = "TableauDeLoi";
			} else if (this.isInTableauSuivi()) {
				if (tableauProgrammationActions.getMasquerApplique()) {
					reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DE_LOIS_TABLEAU_SUIVI_MASQUER_REPORT_FILE;
				} else {
					reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DE_LOIS_TABLEAU_SUIVI_AFFICHER_REPORT_FILE;
				}
				name = "TableauDeSuivi";
			} else if (this.isInTableauProgrammation()) {
				String figer = (!tableauProgrammationActions.isCurrentProgrammationLoiLocked()) ? "true" : "false";
				inputValues.put("FIGER_PARAM", figer);
				reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DE_LOIS_TABLEAU_DE_PROGRAMMATION_REPORT_FILE;
				name = "TableauDeProgrammation";
			}
		} else if (isInApplicationDesOrdonnances()) {
			if (isInTexteMaitre()) {
				reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DES_ORDOS_TEXTE_MAITRE_REPORT_FILE;
				name = "textMaitreOrdonnance";
			}
			if (isInTableauOrdonnances()) {
				reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DES_ORDOS_TABLEAU_DES_ORDOS_REPORT_FILE;
				name = "TableauDesOrdonnances";
			} else if (this.isInTableauSuivi()) {
				if (tableauProgrammationActions.getMasquerApplique()) {
					reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DES_ORDOS_TABLEAU_SUIVI_MASQUER_REPORT_FILE;
				} else {
					reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DES_ORDOS_TABLEAU_SUIVI_AFFICHER_REPORT_FILE;
				}
				name = "TableauDeSuiviOrdonnances";
			} else if (isInTableauProgrammation()) {
				String figer = (!tableauProgrammationActions.isCurrentProgrammationLoiLocked()) ? "true" : "false";
				inputValues.put("FIGER_PARAM", figer);
				reportFile = ActiviteNormativeStatsConstants.AN_APPLICATION_DES_ORDOS_TABLEAU_DE_PROGRAMMATION_REPORT_FILE;
				name = "TableauDeProgrammation";
			}
		} else if (this.isInOrdonnances38C()) {
			if (this.isInTableauSuivi()) {
				reportFile = ActiviteNormativeStatsConstants.AN_HABILITATION_TABLEAU_SUIVI_AFFICHER_REPORT_FILE;
				name = "tableauDeSuivi";
			} else if (this.isInTableauProgrammation()) {
				reportFile = ActiviteNormativeStatsConstants.AN_HABILITATION_TABLEAU_DE_PROGRAMMATION_REPORT_FILE;
				name = "tableauDeProgrammation";
			}
		} else if (this.isInTraiteAccord()) {
			if (this.isInTexteMaitre()) {
				reportFile = ActiviteNormativeStatsConstants.AN_TRAITE_TEXT_MAITRE_REPORT_FILE;
				name = "textMaitre";
			}
		} else if (this.isInTransposition()) {
			if (this.isInTexteMaitre()) {
				reportFile = ActiviteNormativeStatsConstants.AN_TRANSPOSITION_TEXT_MAITRE_REPORT_FILE;
				name = "textMaitre";
			}
		}
		if (reportFile != null) {

			String ministeresParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getMinisteresListBirtReportParam(documentManager);
			String directionsParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getDirectionsListBirtReportParam();
			inputValues.put("MINISTERES_PARAM", ministeresParam);
			inputValues.put("DIRECTIONS_PARAM", directionsParam);
			birtReportingActions.generateXls(reportFile, inputValues, name + ".xls");
		}
	}

	public String displayJournalTechnique() throws ClientException {
		return "view_journal_pan";
	}

}
