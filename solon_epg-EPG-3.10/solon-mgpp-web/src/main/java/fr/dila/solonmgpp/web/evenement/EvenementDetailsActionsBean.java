package fr.dila.solonmgpp.web.evenement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.action.WebActionsBean;

import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.ss.api.security.principal.SSPrincipal;

@Name("evenementDetailsActions")
@Scope(ScopeType.CONVERSATION)
public class EvenementDetailsActionsBean {

	@In(create = true, required = false)
	protected transient WebActionsBean			webActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	@In(required = true, create = true)
	protected SSPrincipal						ssPrincipal;

	@In(create = true, required = false)
	protected transient ActionManager			actionManager;

	@In(create = true, required = true)
	protected transient CoreSession				documentManager;

	private Set<String>							actionStringList;

	/**
	 * Retourne la liste des actions possible sur la version courante
	 * 
	 * @return Liste d'action nuxeo
	 * @throws ClientException
	 */
	public List<Action> getActionList() throws ClientException {
		if (actionStringList == null) {
			actionStringList = SolonMgppServiceLocator.getCorbeilleService().findActionPossible(documentManager,
					ssPrincipal, navigationContext.getCurrentEvenement());
		}

		List<Action> actionPossibleList = new ArrayList<Action>();

		List<Action> actionList = webActions.getActionsList(SolonMgppActionConstant.VERSION_ACTION_CATEGORY);

		for (Action action : actionList) {
			if (actionStringList.contains(action.getId())) {
				actionPossibleList.add(action);
			}
			// Ajouter les actions en_attente
			else if (SolonMgppActionConstant.VERSION_ACTION_METTRE_EN_ATTENTE.equals(action.getId())
					|| SolonMgppActionConstant.VERSION_ACTION_RELANCER_MESSAGE.equals(action.getId())
					|| SolonMgppActionConstant.VERSION_LIAISON_OEP.equals(action.getId())) {
				actionPossibleList.add(action);
			}
		}

		return actionPossibleList;
	}

	public Action getActionAlerte() {
		if (actionStringList == null) {
			actionStringList = SolonMgppServiceLocator.getCorbeilleService().findActionPossible(documentManager,
					ssPrincipal, navigationContext.getCurrentEvenement());
		}

		if (actionStringList.contains(SolonMgppActionConstant.CREER_ALERTE)) {
			return actionManager.getAction(SolonMgppActionConstant.CREER_ALERTE);
		}

		return null;

	}

	/**
	 * Retourne true si l'action est autorisée
	 * 
	 * @param action
	 *            id de l'action
	 * @return
	 * @throws ClientException
	 */
	public boolean isActionPossible(String action) throws ClientException {

		if (actionStringList == null) {
			actionStringList = SolonMgppServiceLocator.getCorbeilleService().findActionPossible(documentManager,
					ssPrincipal, navigationContext.getCurrentEvenement());
		}

		return actionStringList.contains(action);
	}

	public void reset() {
		actionStringList = null;
	}

}
