package fr.dila.solonepg.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

@Name("espaceCreationActions")
@Scope(CONVERSATION)
public class EspaceCreationActionsBean implements Serializable {
	private static final long							serialVersionUID	= 1L;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient ActionManager					actionManager;

	@In(create = true)
	protected transient NavigationWebActionsBean		navigationWebActions;

	/**
	 * Contient la requête utilisée pour afficher l'espace de création : on stocke la requête car elle est appelée
	 * plusieurs fois.
	 */
	protected String									postesQuery			= null;

	/**
	 * Navigue vers l'espace de création.
	 * 
	 * @return Espace de création
	 * @throws ClientException
	 */
	public String navigateTo() throws ClientException {
		navigationContext.resetCurrentDocument();

		// Renseigne le menu du haut
		Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_CREATION);
		navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

		// Renseigne le menu de gauche
		Action leftMenuAction = actionManager.getAction(SolonEpgActionConstant.LEFT_MENU_ESPACE_CREATION);
		navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

		// Renseigne la vue de route des étapes de feuille de route vers les dossiers
		routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_CREATION_VIEW);

		return SolonEpgViewConstant.ESPACE_CREATION_VIEW;
	}

	/**
	 * Contient la requête utilisée pour afficher l'espace de création : on stocke la requête car elle est appelée
	 * plusieurs fois.
	 * 
	 * @return la requête utilisée pour afficher l'espace de création : on stocke la requête car elle est appelée
	 *         plusieurs fois.
	 * @throws ClientException
	 */
	public String getPostesQuery() throws ClientException {
		if (org.apache.commons.lang.StringUtils.isEmpty(postesQuery)) {
			MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
			List<Mailbox> mailboxList = mailboxPosteService.getMailboxPosteList(documentManager);
			List<String> mailboxIds = new ArrayList<String>();
			for (Mailbox mailbox : mailboxList) {
				mailboxIds.add(mailbox.getDocument().getId());
			}
			postesQuery = new StringBuilder("('").append(StringUtils.join(mailboxIds, "','")).append("')").toString();
		}
		return postesQuery;
	}

	/**
	 * Réinitialise les listes et valeurs du bandeau vertical gauche de l'espace de creation.
	 */
	public void refresh() {
		postesQuery = null;
	}
}
