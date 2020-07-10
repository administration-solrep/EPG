package fr.dila.solonmgpp.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.action.WebActionsBean;

import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.enumeration.MgppGroupementCorbeilleEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleActionsBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;
import fr.dila.solonmgpp.web.suivi.CalendrierParlementaireActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Bean espace parlementaire MGPP
 * 
 */
@Name("espaceParlementaireActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class EspaceParlementaireActionsBean extends fr.dila.solonepg.web.espace.EspaceParlementaireActionsBean {

	public static final String								REFRESH_PAGE			= "REFRESH_PAGE";

	private static final long								serialVersionUID		= -2577382202917469495L;

	private static final STLogger							LOGGER					= STLogFactory
																							.getLog(EspaceParlementaireActionsBean.class);

	@In(create = true, required = false)
	private transient NavigationContextBean					navigationContext;

	@In(create = true, required = false)
	private transient DocumentRoutingWebActionsBean			routingWebActions;

	@In(create = true)
	private transient NavigationWebActionsBean				navigationWebActions;

	@In(create = true, required = false)
	private transient ActionManager							actionManager;

	@In(create = true, required = false)
	private transient WebActionsBean						webActions;

	@In(required = true, create = true)
	private SSPrincipal										ssPrincipal;

	@In(required = false, create = true)
	private CorbeilleTreeBean								corbeilleTree;

	@In(create = true, required = true)
	private transient CalendrierParlementaireActionsBean	calendrierParlementaireActions;

	@In(create = true, required = true)
	private transient CoreSession							documentManager;

	private List<Action>									subTabsActionList;

	private Action											mainMenu;

	private boolean											isMainCategorySelected	= false;

	/**
	 * navigation vers un menu secondaire
	 * 
	 * @param secondMenuAction
	 * @return
	 * @throws ClientException
	 */
	public String navigateTo(final Action secondMenuAction) throws ClientException {
		final String view = SolonMgppViewConstant.VIEW_CORBEILLE_MESSAGE;

		this.registerSubActionList(secondMenuAction);

		webActions.resetTabList();

		// Réinitialise le context courant
		navigationContext.resetCurrentDocument();

		// Renseigne le menu du haut
		final Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE);
		navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

		// Renseigne le second menu
		if (isMainCategorySelected && !getSubTabsActionList().isEmpty()) {
			navigationWebActions.setCurrentSecondMenuAction(getSubTabsActionList().get(0));
		} else {
			navigationWebActions.setCurrentSecondMenuAction(secondMenuAction);
		}

		// Renseigne le menu de gauche
		final Action leftMenuAction = actionManager.getAction(SolonMgppActionConstant.LEFT_MENU_ESPACE_CORBEILLE);
		navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

		// Renseigne la vue
		routingWebActions.setFeuilleRouteView(view);

		navigationContext.setCurrentView(view);

		return view;
	}

	@Observer(REFRESH_PAGE)
	public void refreshPage() {
		corbeilleTree.resetCurrentItem();

		// reset du provider
		Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);

		// refresh de la corbeille courante
		Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);

		// refresh du provider
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
	}

	/**
	 * Navigation vers l'espace parlementaire.
	 * 
	 * @return Vue de l'espace de parlementaire
	 * @throws ClientException
	 */
	@Override
	public String navigateTo() throws ClientException {
		String view = null;
		// navigation vers le premier menu dispo
		if (ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.CORBEILLE_MGPP_UPDATER)) {
			view = navigateToProcedureLegislative();
		} else {
			view = navigateToNomination();
		}

		return view;
	}

	private String navigateToMenuAndRefreshPage(final String menuAction) throws ClientException {
		final Action secondMenuAction = actionManager.getAction(menuAction);
		final String view = navigateTo(secondMenuAction);
		Events.instance().raiseEvent(REFRESH_PAGE);
		return view;
	}

	/**
	 * Navigation vers l'espace parlementaire.
	 * 
	 * @return Vue de l'espace de parlementaire
	 * @throws ClientException
	 */
	public String navigateToProcedureLegislative() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.PROCEDURE_LEGISLATIVE);
	}

	/**
	 * Navigation vers Déclaration de politique générale.
	 * 
	 * @return Vue de Déclaration de politique générale
	 * @throws ClientException
	 */
	public String navigateToDeclarationPolitique() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE);
	}

	/**
	 * Navigation vers Demande de mise en oeuvre de l'article 28-3C.
	 * 
	 * @return Vue de Demande de mise en oeuvre de l'article 28-3C
	 * @throws ClientException
	 */
	public String navigateToArticle283C() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C);
	}

	/**
	 * Navigation vers Déclaration sur un sujet determine.
	 * 
	 * @return Vue de Déclaration sur un sujet determine
	 * @throws ClientException
	 */
	public String navigateToDeclarationSurUnSujetDetermine() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE);
	}

	/**
	 * Navigation vers Documents transmis aux assemblées.
	 * 
	 * @return Vue Documents transmis aux assemblées
	 * @throws ClientException
	 */
	public String navigateToAutresDocumentsTransmis() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES);
	}

	/**
	 * Navigation vers Documents Demande d'audition.
	 * 
	 * @return Vue Documents Demande d'audition
	 * @throws ClientException
	 */
	public String navigateToDemandeAudition() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DEMANDE_AUDITION);
	}

	public String navigateToReport() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppViewConstant.CATEGORY_RAPORT_ID);

	}

	public String navigateToOrganisation() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppViewConstant.CATEGORY_ORGANISATION_ID);
	}

	public String navigateToDeclaration() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppViewConstant.CATEGORY_DECLARATION_ID);
	}

	public String navigateToNomination() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppViewConstant.CATEGORY_NOMINATION_ID);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Publication)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Publication)
	 * @throws ClientException
	 */
	public String navigateToPublication() throws ClientException {

		final String view = SolonMgppViewConstant.VIEW_CORBEILLE_DOSSIER;

		webActions.resetTabList();

		// Réinitialise le context courant
		navigationContext.resetCurrentDocument();

		// Renseigne le menu du haut
		final Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE);
		navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

		// Renseigne le second menu
		final Action secondMenuAction = actionManager.getAction(SolonMgppActionConstant.PUBLICATION);
		this.registerSubActionList(secondMenuAction);
		navigationWebActions.setCurrentSecondMenuAction(secondMenuAction);

		// Renseigne le menu de gauche
		final Action leftMenuAction = actionManager.getAction(SolonMgppActionConstant.LEFT_MENU_ESPACE_CORBEILLE);
		navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

		// Renseigne la vue
		routingWebActions.setFeuilleRouteView(view);

		navigationContext.setCurrentView(view);

		Events.instance().raiseEvent(REFRESH_PAGE);
		return view;
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Depot de rapport)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Depot de rapport)
	 * @throws ClientException
	 */
	public String navigateToDepotDeRapport() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DEPOT_DE_RAPPORT);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Desigantion OEP)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Desigantion OEP)
	 * @throws ClientException
	 */
	public String navigateToDesignationOEP() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DESIGNATION_OEP);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Avis nomination)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Avis nomination)
	 * @throws ClientException
	 */
	public String navigateToAvisNomination() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.AVIS_NOMINATION);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Decret)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Decret)
	 * @throws ClientException
	 */
	public String navigateToDecret() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.DECRET);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Intervention exterieure)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Intervention exterieure)
	 * @throws ClientException
	 */
	public String navigateToInterventionExterieure() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.INTERVENTION_EXTERIEURE);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Resolution article 341)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Resolution article 341)
	 * @throws ClientException
	 */
	public String navigateToResolutionArticle341() throws ClientException {
		return navigateToMenuAndRefreshPage(SolonMgppActionConstant.RESOLUTION_ARTICLE_341);
	}

	/**
	 * Navigation vers l'espace parlementaire (menu secondaire Suivi)
	 * 
	 * @return Vue de l'espace de parlementaire (menu secondaire Suivi)
	 * @throws ClientException
	 */
	public String navigateToSuivi() throws ClientException {
		final Action secondMenuAction = actionManager.getAction(SolonMgppActionConstant.SUIVI);
		calendrierParlementaireActions.setDateDeDebutFiltre(new Date());
		calendrierParlementaireActions.setDateDeFinFiltre(null);
		navigateTo(secondMenuAction);
		Events.instance().raiseEvent(REFRESH_PAGE);
		return SolonMgppViewConstant.VIEW_SUIVI;
	}

	/**
	 * Navigation vers la recherche.
	 * 
	 * @return Vue la recherche
	 * @throws ClientException
	 */
	public String navigateToRecherche() throws ClientException {
		final Action secondMenuAction = actionManager.getAction("mgpp_recherche");
		navigateTo(secondMenuAction);
		Events.instance().raiseEvent(REFRESH_PAGE);
		return SolonMgppViewConstant.VIEW_RECHERCHE;
	}

	public void setSubTabsActionList(final List<Action> subTabsActionList) {
		this.subTabsActionList = subTabsActionList;
	}

	public List<Action> getSubTabsActionList() {
		if (subTabsActionList == null) {
			subTabsActionList = new ArrayList<Action>();
		}
		return subTabsActionList;
	}

	public boolean isTabSelected(final String idTab) {
		return this.navigationWebActions.getCurrentSecondMenuAction() != null
				&& this.navigationWebActions.getCurrentSecondMenuAction().getId().equals(idTab)
				&& this.mainMenu != null && this.mainMenu.getId().equals(idTab);
	}

	public boolean hasTabMessages(final String actionId) {
		List<String> corbeillesIds = MgppGroupementCorbeilleEnum.getCorbeilleIdsFromActionMgppId(actionId);
		try {
			if (!corbeillesIds.isEmpty()) {
				return SolonMgppServiceLocator.getCorbeilleService().haveCorbeillesMessages(documentManager,
						new HashSet<String>(corbeillesIds));
			}
		} catch (ClientException exc) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_INFO_CORBEILLE_FONC, exc);
		}
		return false;
	}

	private boolean inCategory(final Action secondMenuAction, final String actionId, final String category) {

		boolean result = false;
		final String menuActionId = secondMenuAction.getId();
		result = actionId.equals(menuActionId);
		this.isMainCategorySelected = result;
		if (!result) {
			final String[] categories = secondMenuAction.getCategories();
			for (int i = 0; i < categories.length; i++) {
				if (category.equals(categories[i])) {
					return true;
				}
			}
		}
		if (result) {
			this.mainMenu = secondMenuAction;
		}
		return result;

	}

	private void registerSubActionList(final Action secondMenuAction) {

		// Clear current sub tabs
		this.getSubTabsActionList().clear();

		if (this.inCategory(secondMenuAction, SolonMgppViewConstant.CATEGORY_RAPORT_ID,
				SolonMgppViewConstant.CATEGORY_RAPORT)) {
			this.subTabsActionList = webActions.getActionsList(SolonMgppViewConstant.CATEGORY_RAPORT);
			return;
		} else if (this.inCategory(secondMenuAction, SolonMgppViewConstant.CATEGORY_NOMINATION_ID,
				SolonMgppViewConstant.CATEGORY_NOMINATION)) {
			this.subTabsActionList = webActions.getActionsList(SolonMgppViewConstant.CATEGORY_NOMINATION);
			return;
		} else if (this.inCategory(secondMenuAction, SolonMgppViewConstant.CATEGORY_ORGANISATION_ID,
				SolonMgppViewConstant.CATEGORY_ORGANISATION)) {
			this.subTabsActionList = webActions.getActionsList(SolonMgppViewConstant.CATEGORY_ORGANISATION);
			return;
		}
		if (this.inCategory(secondMenuAction, SolonMgppViewConstant.CATEGORY_DECLARATION_ID,
				SolonMgppViewConstant.CATEGORY_DECLARATION)) {
			this.subTabsActionList = webActions.getActionsList(SolonMgppViewConstant.CATEGORY_DECLARATION);
			return;
		}

		this.mainMenu = null;
	}
}
