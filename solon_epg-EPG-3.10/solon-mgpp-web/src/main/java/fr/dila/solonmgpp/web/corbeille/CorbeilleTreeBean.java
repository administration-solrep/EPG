package fr.dila.solonmgpp.web.corbeille;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.ui.web.api.WebActions;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;

import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.FilterConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.CorbeilleService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.core.tree.CorbeilleNodeImpl;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Classe de gestion de l'arbre des corbeilles.
 * 
 * @author asatre
 */
@Name("corbeilleTree")
@Scope(ScopeType.CONVERSATION)
public class CorbeilleTreeBean implements Serializable {

	private static final String							ETAT_EVT_ANNULE		= "ANNULE";
	private static final String							ETAT_DOSSIER_ALERTE	= "ALERTE";
	private static final long							serialVersionUID	= -7878810387257678460L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER				= STLogFactory
																					.getLog(CorbeilleTreeBean.class);

	private Map<String, List<CorbeilleNode>>			corbeilleNodes;

	private CorbeilleService							corbeilleService	= SolonMgppServiceLocator
																					.getCorbeilleService();

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient EspaceParlementaireActionsBean	espaceParlementaireActions;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@In(create = true, required = false)
	protected transient WebActions						webActions;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true)
	protected ContentViewActions						contentViewActions;

	@In(create = true, required = false)
	protected ProviderBean								providerBean;

	private CorbeilleNode								currentItem;

	/**
	 * charge l'arbre des corbeilles
	 * 
	 * @throws ClientException
	 */
	private void loadCorbeillesDTO(String currentMenu) {
		try {
			Action action = navigationWebActions.getCurrentSecondMenuAction();
			if (action.getLabel().equals(SolonMgppActionConstant.PUBLICATION)) {
				String corbeilleType = CorbeilleTypeObjet.DOSSIER.name();
				List<CorbeilleDTO> corbeillesDTO = corbeilleService.findCorbeille(currentMenu, ssPrincipal,
						documentManager);
				if (corbeilleType.equals(CorbeilleTypeObjet.MESSAGE.name())) {
					corbeilleNodes.put(currentMenu + CorbeilleTypeObjet.MESSAGE.name(),
							buildCorbeilleNodeFromCorbeilleDTO(corbeillesDTO, currentMenu, CorbeilleTypeObjet.MESSAGE));
				} else if (corbeilleType.equals(CorbeilleTypeObjet.DOSSIER.name())) {
					corbeilleNodes.put(currentMenu + CorbeilleTypeObjet.DOSSIER.name(),
							buildCorbeilleNodeFromCorbeilleDTO(corbeillesDTO, currentMenu, CorbeilleTypeObjet.DOSSIER));
				}
			} else {
				List<CorbeilleDTO> corbeillesDTO = corbeilleService.findCorbeille(currentMenu, ssPrincipal,
						documentManager);
				corbeilleNodes.put(currentMenu + CorbeilleTypeObjet.MESSAGE.name(),
						buildCorbeilleNodeFromCorbeilleDTO(corbeillesDTO, currentMenu, CorbeilleTypeObjet.MESSAGE));
				corbeilleNodes.put(currentMenu + CorbeilleTypeObjet.DOSSIER.name(),
						buildCorbeilleNodeFromCorbeilleDTO(corbeillesDTO, currentMenu, CorbeilleTypeObjet.DOSSIER));
			}
		} catch (ClientException e) {
			String message = e.getMessage();
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_CORBEILLE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			corbeilleNodes.put(currentMenu, new ArrayList<CorbeilleNode>());
		}
	}

	private List<CorbeilleTypeObjet> ficheList() {
		return Arrays.asList(CorbeilleTypeObjet.DOSSIER, CorbeilleTypeObjet.AVI, CorbeilleTypeObjet.OEP,
				CorbeilleTypeObjet.FICHE_LOI, CorbeilleTypeObjet.FICHE_DR, CorbeilleTypeObjet.FICHE_DR_67,
				CorbeilleTypeObjet.FICHE_DPG, CorbeilleTypeObjet.FICHE_SD, CorbeilleTypeObjet.FICHE_DECRET,
				CorbeilleTypeObjet.FICHE_IE, CorbeilleTypeObjet.FICHE_341, CorbeilleTypeObjet.FICHE_JSS,
				CorbeilleTypeObjet.FICHE_DOC, CorbeilleTypeObjet.FICHE_AUD);
	}

	private List<CorbeilleNode> buildCorbeilleNodeFromCorbeilleDTO(List<CorbeilleDTO> corbeillesDTO,
			String currentMenu, CorbeilleTypeObjet corbeilleTypeObjet) throws ClientException {
		List<CorbeilleNode> list = new ArrayList<CorbeilleNode>();
		if (corbeillesDTO != null) {
			for (CorbeilleDTO corbeilleDTO : corbeillesDTO) {

				Boolean accept = Boolean.FALSE;
				boolean inFicheList = this.ficheList().contains(corbeilleDTO.getTypeObjet());
				if (CorbeilleTypeObjet.MESSAGE.equals(corbeilleTypeObjet)) {
					// type MESSAGE = tous ce qui est de l'EPP
					accept = !inFicheList;
				} else if (CorbeilleTypeObjet.DOSSIER.equals(corbeilleTypeObjet)) {
					// type DOSSIER = tous ce qui n'est pas de l'EPP
					accept = inFicheList;
				}
				if (accept) {
					CorbeilleNode corbeilleNode = new CorbeilleNodeImpl();
					corbeilleNode.setDescription(corbeilleDTO.getDescription());
					corbeilleNode.setName(corbeilleDTO.getNom());
					corbeilleNode.setId(corbeilleDTO.getIdCorbeille());
					Boolean close = SolonMgppCorbeilleConstant.CORBEILLE_NODE.equals(corbeilleDTO.getType())
							|| SolonMgppCorbeilleConstant.MGPP_NODE.equals(corbeilleDTO.getType());
					corbeilleNode.setOpened(!close);
					corbeilleNode.setType(corbeilleDTO.getType());
					corbeilleNode.setRoutingTaskType(corbeilleDTO.getRoutingTaskType());
					corbeilleNode.setTypeActe(corbeilleDTO.getTypeActe());
					corbeilleNode.setTypeObjet(corbeilleDTO.getTypeObjet());
					corbeilleNode.setAdoption(corbeilleDTO.isAdoption());
					// load message
					try {
						List<MessageDTO> listMessageDTO = SolonMgppServiceLocator.getMessageService()
								.findMessageByCorbeille(ssPrincipal, corbeilleDTO, documentManager, currentMenu);
						corbeilleNode.setMessageDTO(listMessageDTO);
						if (currentItem != null) {
							if (currentItem.getId().equals(corbeilleNode.getId())) {
								currentItem = corbeilleNode;
							}
						}
					} catch (ClientException e) {
						String message = e.getMessage();
						LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, message);
						facesMessages.add(StatusMessage.Severity.WARN, message);
					}
					// add child
					List<CorbeilleNode> listChild = buildCorbeilleNodeFromCorbeilleDTO(corbeilleDTO.getCorbeille(),
							currentMenu, corbeilleTypeObjet);

					corbeilleNode.setCorbeilleNodeList(listChild);
					list.add(corbeilleNode);

					Action action = navigationWebActions.getCurrentSecondMenuAction();
					if (action.getLabel() == SolonMgppActionConstant.PUBLICATION) {
						// traitement unrestricted spécifique à publication pour des raisons de performances
						corbeilleNode.setCount(SolonMgppServiceLocator.getCorbeilleService().countDossierUnrestricted(
								documentManager, corbeilleNode));
					} else {
						corbeilleNode.setCount(SolonMgppServiceLocator.getCorbeilleService().countDossier(
								documentManager, corbeilleNode));
					}

				}

			}

		}
		return list;
	}

	public Boolean adviseNodeOpened(UITree treeComponent) {
		Object value = treeComponent.getRowData();
		if (value instanceof CorbeilleNode) {
			CorbeilleNode minNode = (CorbeilleNode) value;
			return minNode.isOpened();
		}
		return null;
	}

	/**
	 * Méthode qui renvoie l'arbre des corbeilles "Message" complet
	 * 
	 * @return l'arbre chargé
	 * @throws ClientException
	 */
	public List<CorbeilleNode> getCorbeilleMessage() {
		if (navigationWebActions.getCurrentSecondMenuAction() != null) {
			String currentMenu = navigationWebActions.getCurrentSecondMenuAction().getId();
			if (corbeilleNodes == null) {
				corbeilleNodes = new HashMap<String, List<CorbeilleNode>>();
			}
			if (corbeilleNodes.get(currentMenu + CorbeilleTypeObjet.MESSAGE.name()) == null) {
				loadCorbeillesDTO(currentMenu);
			}
			return corbeilleNodes.get(currentMenu + CorbeilleTypeObjet.MESSAGE.name());
		}
		return new ArrayList<CorbeilleNode>();
	}

	/**
	 * Méthode qui renvoie l'arbre des corbeilles "Dossier" complet
	 * 
	 * @return l'arbre chargé
	 * @throws ClientException
	 */
	public List<CorbeilleNode> getCorbeilleDossier() {
		if (navigationWebActions.getCurrentSecondMenuAction() != null) {
			String currentMenu = navigationWebActions.getCurrentSecondMenuAction().getId();
			if (corbeilleNodes == null) {
				corbeilleNodes = new HashMap<String, List<CorbeilleNode>>();
			}
			if (corbeilleNodes.get(currentMenu + CorbeilleTypeObjet.DOSSIER.name()) == null) {
				loadCorbeillesDTO(currentMenu);
			}
			return corbeilleNodes.get(currentMenu + CorbeilleTypeObjet.DOSSIER.name());
		}
		return new ArrayList<CorbeilleNode>();
	}

	/**
	 * force le rafraichissement de la corbeille courante
	 */
	public void forceRefresh() {
		resetCurrentItem();
		forceRefreshNoReset();
	}

	/**
	 * force le rafraichissement de la corbeille courante sans reset du context
	 */
	public void forceRefreshNoReset() {
		SolonMgppServiceLocator.getMessageService().clearCache(ssPrincipal);
		if (navigationWebActions.getCurrentSecondMenuAction() != null) {
			String currentMenu = navigationWebActions.getCurrentSecondMenuAction().getId();
			if (corbeilleNodes == null) {
				corbeilleNodes = new HashMap<String, List<CorbeilleNode>>();
			}

			corbeilleNodes.put(currentMenu + CorbeilleTypeObjet.MESSAGE.name(), null);
			corbeilleNodes.put(currentMenu + CorbeilleTypeObjet.DOSSIER.name(), null);

		}
		// getCorbeilleMessage();
	}

	public void changeExpandListener(NodeExpandedEvent event) {
		UIComponent component = event.getComponent();
		if (component instanceof UITree) {
			UITree treeComponent = (UITree) component;
			Object value = treeComponent.getRowData();
			if (value instanceof CorbeilleNode) {
				CorbeilleNode corbeilleNode = (CorbeilleNode) value;
				corbeilleNode.setOpened(!corbeilleNode.isOpened());
			}
		}
	}

	public CorbeilleNode getCurrentItem() {
		return currentItem;
	}

	public String setCurrentItem(CorbeilleNode currentItem) throws ClientException {

		String itemResult = null;
		// reset du context courant
		navigationContext.resetCurrentDocument();

		resetCurrentItem();

		this.currentItem = currentItem;

		if (currentItem != null) {
			switch (currentItem.getTypeObjet()) {
				case DOSSIER:
					corbeilleActions.setCurrentTaskType(currentItem.getRoutingTaskType());
					break;
				default:
					corbeilleActions.setCurrentTaskType(null);
					break;
			}
		} else {
			corbeilleActions.setCurrentTaskType(null);
		}

		if (currentItem != null) {

			switch (currentItem.getTypeObjet()) {
				case DOSSIER:

					resetAndRefresh();

					String view = SolonMgppViewConstant.VIEW_CORBEILLE_DOSSIER;
					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(view);

					navigationContext.setCurrentView(view);

					providerBean.resetFilter(FilterConstants.CORBEILLE_DOSSIER_LIST);

					itemResult = view;
					break;
				case MESSAGE:
					this.resetAndRefresh();

					providerBean.resetFilter(FilterConstants.CORBEILLE_MESSAGE_LIST);

					itemResult = espaceParlementaireActions.navigateTo(navigationWebActions
							.getCurrentSecondMenuAction());

					break;
				case OEP:
					this.resetAndRefresh();

					String viewOEP = SolonMgppViewConstant.VIEW_LISTE_OEP;
					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewOEP);

					navigationContext.setCurrentView(viewOEP);

					providerBean.resetFilter(FilterConstants.OEP_LIST);

					itemResult = viewOEP;
					break;
				case AVI:
					this.resetAndRefresh();

					String viewAvi = SolonMgppViewConstant.VIEW_LISTE_AVI;
					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewAvi);

					navigationContext.setCurrentView(viewAvi);

					providerBean.resetFilter(FilterConstants.AVI_LIST);

					itemResult = viewAvi;
					break;

				case FICHE_LOI:
					this.resetAndRefresh();

					String viewFicheLoi = SolonMgppViewConstant.VIEW_LISTE_FICHE_LOI;

					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewFicheLoi);

					navigationContext.setCurrentView(viewFicheLoi);

					providerBean.resetFilter(FilterConstants.FICHE_LOI_LIST);

					Calendar date = Calendar.getInstance();
					final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService()
							.findParametrageMgpp(documentManager);
					Long iFiltre = parametrageMgpp.getFiltreDateCreationLoi();
					date.add(Calendar.MONTH, (int) -iFiltre);
					providerBean.setCreationDateFilter(/* FilterConstants.FICHE_LOI_LIST, */date);

					itemResult = viewFicheLoi;
					break;

				case FICHE_DR:
					this.resetAndRefresh();

					String viewFicheDR = SolonMgppViewConstant.VIEW_LISTE_DR;

					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewFicheDR);

					navigationContext.setCurrentView(viewFicheDR);

					providerBean.resetFilter(FilterConstants.FICHE_DR_LIST);

					itemResult = viewFicheDR;
					break;

				case FICHE_DR_67:
					this.resetAndRefresh();

					String viewFicheDR67 = SolonMgppViewConstant.VIEW_LISTE_DR_67;

					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewFicheDR67);

					navigationContext.setCurrentView(viewFicheDR67);

					providerBean.resetFilter(FilterConstants.FICHE_DR_67_LIST);

					itemResult = viewFicheDR67;
					break;

				case FICHE_DECRET:
					this.resetAndRefresh();

					String viewFicheDecret = SolonMgppViewConstant.VIEW_LISTE_DECRET;

					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewFicheDecret);

					navigationContext.setCurrentView(viewFicheDecret);

					providerBean.resetFilter(FilterConstants.FICHE_DECRET_LIST);

					itemResult = viewFicheDecret;
					break;

				case FICHE_IE:
					this.resetAndRefresh();

					String viewFicheIE = SolonMgppViewConstant.VIEW_LISTE_IE;

					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewFicheIE);

					navigationContext.setCurrentView(viewFicheIE);

					providerBean.resetFilter(FilterConstants.FICHE_IE_LIST);

					itemResult = viewFicheIE;
					break;

				case FICHE_341:

					this.resetAndRefresh();

					String viewFiche341 = SolonMgppViewConstant.VIEW_LISTE_341;

					// Renseigne la vue
					routingWebActions.setFeuilleRouteView(viewFiche341);

					navigationContext.setCurrentView(viewFiche341);

					providerBean.resetFilter(FilterConstants.FICHE_341_LIST);

					itemResult = viewFiche341;
					break;

				case FICHE_DPG:
					this.resetAndRefresh();

					String viewDPG = SolonMgppViewConstant.VIEW_LISTE_DPG;

					routingWebActions.setFeuilleRouteView(viewDPG);

					navigationContext.setCurrentView(viewDPG);

					providerBean.resetFilter(FilterConstants.FICHE_DPG_LIST);

					itemResult = viewDPG;
					break;

				case FICHE_SD:
					this.resetAndRefresh();

					String viewSD = SolonMgppViewConstant.VIEW_LISTE_SD;

					routingWebActions.setFeuilleRouteView(viewSD);

					navigationContext.setCurrentView(viewSD);

					providerBean.resetFilter(FilterConstants.FICHE_SD_LIST);

					itemResult = viewSD;
					break;

				case FICHE_JSS:
					this.resetAndRefresh();

					String viewJSS = SolonMgppViewConstant.VIEW_LISTE_JSS;

					routingWebActions.setFeuilleRouteView(viewJSS);

					navigationContext.setCurrentView(viewJSS);

					providerBean.resetFilter(FilterConstants.FICHE_JSS_LIST);

					itemResult = viewJSS;
					break;

				case FICHE_DOC:
					this.resetAndRefresh();

					String viewDOC = SolonMgppViewConstant.VIEW_LISTE_DOC;

					routingWebActions.setFeuilleRouteView(viewDOC);

					navigationContext.setCurrentView(viewDOC);

					providerBean.resetFilter(FilterConstants.FICHE_DOC_LIST);

					itemResult = viewDOC;
					break;

				case FICHE_AUD:
					this.resetAndRefresh();

					String viewAUD = SolonMgppViewConstant.VIEW_LISTE_AUD;

					routingWebActions.setFeuilleRouteView(viewAUD);

					navigationContext.setCurrentView(viewAUD);

					providerBean.resetFilter(FilterConstants.FICHE_AUD_LIST);

					itemResult = viewAUD;
					break;

			}
		} else {

			resetAndRefresh();

			itemResult = espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
		}

		return itemResult;
	}

	private void resetAndRefresh() {
		// reset du provider
		Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);

		// refresh du provider
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
	}

	public String navigateToMessage(MessageDTO currentMessage, String contentViewName) throws ClientException {
		return navigateToMessage(currentMessage, contentViewName, Boolean.FALSE);
	}

	public String navigateToMessage(MessageDTO currentMessage, String contentViewName, Boolean reset)
			throws ClientException {

		String view = null;

		if (reset == null || reset) {
			setCurrentItem(null);
			view = espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
		}

		// Assignation du MessageDTO au provider
		if (contentViewName != null) {
			@SuppressWarnings("unchecked")
			PageProvider<MessageDTO> pageProvider = (PageProvider<MessageDTO>) contentViewActions
					.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
			List<?> currentPage = pageProvider.getCurrentPage();
			if (currentPage != null && currentPage.contains(currentMessage)) {
				pageProvider.setCurrentEntry(currentMessage);
			}
		}

		// Reset des documents
		navigationContext.setCurrentMessage(currentMessage);

		// Navigation sur le premier onglet
		List<Action> actions = webActions.getTabsList();

		// Assigne l'onglet
		if (actions != null && !actions.isEmpty()) {
			webActions.setCurrentTabId(actions.get(0).getId());
		}

		return view;
	}

	public String navigateToMessageAnResetItem(MessageDTO currentMessage) throws ClientException {
		return navigateToMessage(currentMessage, null, Boolean.TRUE);
	}

	public MessageDTO getCurrentMessage() {
		return navigationContext.getCurrentMessage();
	}

	public Boolean isCurrentItem(final CorbeilleNode item) {
		if (item != null && currentItem != null) {
			return currentItem.getId().equals(item.getId());
		}
		return false;
	}

	public Boolean isCurrentMessage(final MessageDTO message) {
		if (message != null && getCurrentMessage() != null) {
			return message.toComparableString().equals(getCurrentMessage().toComparableString());
		}
		return false;
	}

	/**
	 * Decoration de la ligne de la mesure selectionnée dans la table des mesure
	 * 
	 * @param dto
	 * @return
	 * @throws ClientException
	 */
	public String decorate(AbstractMapDTO dto, String clazz) throws ClientException {

		MessageDTO messageDTO = navigationContext.getCurrentMessage();
		if (messageDTO != null) {
			if (messageDTO.toComparableString().equals(((MessageDTO) dto).toComparableString())) {
				clazz = "dataRowSelected";
			}
		}

		if (dto instanceof MessageDTO) {

			if (ETAT_DOSSIER_ALERTE.equals(((MessageDTO) dto).getEtatDossier())) {
				clazz += " dataRowRed";
			}

			if (ETAT_EVT_ANNULE.equals(((MessageDTO) dto).getEtatEvenement())) {
				clazz += " dataRowStrike";
			}

			if ("NON_TRAITE".equals(((MessageDTO) dto).getEtatMessage())
					|| "EN_COURS_REDACTION".equals(((MessageDTO) dto).getEtatMessage())) {
				clazz += " dataRowBold";
			}
		}
		return clazz;
	}

	public void resetCurrentItem() {
		currentItem = null;
	}

	public SSPrincipal getSSPrincipal() {
		return ssPrincipal;
	}

	public String getCurrentMenu() {
		if (navigationWebActions.getCurrentSecondMenuAction() != null) {
			return navigationWebActions.getCurrentSecondMenuAction().getId();
		} else {
			return null;
		}
	}

}
