package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;

import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.web.context.NavigationContextBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STViewConstant;

/**
 * Classe de gestion du menu de l'acitivite normative.
 * 
 * @author asatre
 */
@Name("espaceActiviteNormativeTree")
@Scope(ScopeType.CONVERSATION)
public class EspaceActiviteNormativeTreeBean implements Serializable {

	private static final long							serialVersionUID	= 1L;

	private List<EspaceActiviteNormativeTreeNode>		rootNodes;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true)
	protected transient ActiviteNormativeActionsBean	activiteNormativeActions;

	@In(create = true, required = true)
	protected ContentViewActions						contentViewActions;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@In(create = true)
	protected transient DocumentsListsManager			documentsListsManager;

	private EspaceActiviteNormativeTreeNode				currentItem;

	public List<EspaceActiviteNormativeTreeNode> getEspaceActiviteNormative() {
		if (rootNodes == null) {
			load();
		}
		return rootNodes;
	}

	private void load() {
		rootNodes = new ArrayList<EspaceActiviteNormativeTreeNode>();
		Boolean hasDroitApplicationDeslois = false;
		Boolean hasDroitApplicationDesOrdonnances = false;
		for (ActiviteNormativeEnum espaceANEnum : ActiviteNormativeEnum.values()) {
			if (espaceANEnum.getRight() == null || ssPrincipal.isMemberOf(espaceANEnum.getRight())) {
				// Si utilisateur ministère et qu'on a déjà ajouté les liens pour profil du PAN
				// On n'ajoute pas à nouveau les liens pour profil ministère
				// Car sinon liens en double
				if ((espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE) && hasDroitApplicationDeslois)
						|| (espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE) && hasDroitApplicationDesOrdonnances)) {
					continue;
				}
				if (espaceANEnum.equals(ActiviteNormativeEnum.APPICATION_DES_LOIS)) {
					hasDroitApplicationDeslois = true;
				} else if (espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES)) {
					hasDroitApplicationDesOrdonnances = true;
				}
				String label = resourcesAccessor.getMessages().get(espaceANEnum.getLabel());
				EspaceActiviteNormativeTreeNode treeNode = new EspaceActiviteNormativeTreeNode(label,
						espaceANEnum.getId(), espaceANEnum);
				treeNode.setOpened(true);
				rootNodes.add(treeNode);
			}
		}
	}

	public Boolean adviseNodeOpened(UITree treeComponent) {
		Object value = treeComponent.getRowData();
		if (value instanceof EspaceActiviteNormativeTreeNode) {
			EspaceActiviteNormativeTreeNode minNode = (EspaceActiviteNormativeTreeNode) value;
			return minNode.isOpened();
		}
		return null;
	}

	public void changeExpandListener(NodeExpandedEvent event) {
		UIComponent component = event.getComponent();
		if (component instanceof UITree) {
			UITree treeComponent = (UITree) component;
			Object value = treeComponent.getRowData();
			if (value instanceof EspaceActiviteNormativeTreeNode) {
				EspaceActiviteNormativeTreeNode node = (EspaceActiviteNormativeTreeNode) value;
				if (node.isOpened()) {
					node.setOpened(Boolean.FALSE);
				} else {
					node.setOpened(Boolean.TRUE);
				}
			}
		}
	}

	public String setElement(EspaceActiviteNormativeTreeNode item) throws ClientException {
		navigationContext.resetCurrentDocument();
		ContentView contentView = contentViewActions.getCurrentContentView();
		if (contentView != null) {
			documentsListsManager.resetWorkingList(contentView.getSelectionListName());
		}
		if (item != null) {
			currentItem = item;
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE);
			return activiteNormativeActions.navigateToItem(item.getEspaceActiviteNormativeEnum().getId());
		}
		return null;
	}

	public String setElementId(String itemId) throws ClientException {
		for (EspaceActiviteNormativeTreeNode rootNode : rootNodes) {
			if (rootNode.getEspaceActiviteNormativeEnum().getId().equals(itemId)) {
				return setElement(rootNode);
			}
		}
		return null;
	}

	public String getCurrentView() {
		if (currentItem != null) {
			return SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE;
		}
		return STViewConstant.EMPTY_VIEW;
	}

	public Boolean isCurrentItem(EspaceActiviteNormativeTreeNode item) {
		if (currentItem != null && item != null) {
			return currentItem.getKey().equals(item.getKey());
		}
		return false;
	}

	public String reset() throws ClientException {
		return setElement(null);
	}

	/**
	 * 
	 * @param currentTabAction
	 *            l'onglet courant
	 * @return la contentview correspondant au menu et a l'onglet (haut) selectionné
	 */
	public String getCurrentContentViewName(Action currentTabAction) {
		if (currentTabAction != null && currentItem != null) {
			return EspaceActiviteNormativeTabEnum.getContentViewName(currentTabAction.getId(),
					currentItem.getEspaceActiviteNormativeEnum());
		}
		return null;
	}

	public EspaceActiviteNormativeTreeNode getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(EspaceActiviteNormativeTreeNode currentItem) {
		this.currentItem = currentItem;
	}

	public ActiviteNormativeEnum getActiviteNormativeEnum() {
		EspaceActiviteNormativeTreeNode node = getCurrentItem();
		if (node == null) {
			return null;
		}
		ActiviteNormativeEnum anEnum = node.getEspaceActiviteNormativeEnum();
		return anEnum;
	}

}
