package fr.dila.solonepg.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.web.context.CaseManagementContextHolderBean;
import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;
import fr.dila.solonepg.api.service.MailboxService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierActionsBean;
import fr.dila.solonepg.web.refresh.RefreshActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

@Name("espaceTraitementActions")
@Scope(CONVERSATION)
public class EspaceTraitementActionsBean implements Serializable {
	private static final long							serialVersionUID						= 1L;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient ActionManager					actionManager;

	@In(create = true)
	protected transient NuxeoPrincipal					currentNuxeoPrincipal;

	@In(create = true)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient DossierActionsBean				dossierActions;

	@In(create = true, required = false)
	protected transient CaseManagementContextHolderBean	cmContextHolder;

	@In(create = true, required = false)
	protected transient RefreshActionsBean				refreshActions;

	private static final String							ESPACE_TRAITEMENT_DOSSIER_CONTENT		= "espace_traitement_dossier_content";

	private static final String							ESPACE_TRAITEMENT_INDEXATION_CONTENT	= "espace_traitement_indexation_content";

	private String										activeContentViewName;

	private Long										dossierIndexationQueryCount				= null;

	protected List<Map<String, Serializable>>			mailboxesEpg							= null;

	private List<Mailbox>								mailboxes;

	/**
	 * Navigation vers l'espace de traitement.
	 * 
	 * @return Vue de l'espace de traitement
	 * @throws ClientException
	 */
	public String navigateTo() throws ClientException {
		// Réinitialise le document courant
		navigationContext.resetCurrentDocument();

		// Renseigne le menu du haut
		Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_TRAITEMENT);
		navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

		// Renseigne le menu de gauche
		Action action = actionManager.getAction(SolonEpgActionConstant.LEFT_MENU_ESPACE_TRAITEMENT);
		navigationWebActions.setCurrentLeftMenuAction(action);

		activeContentViewName = ESPACE_TRAITEMENT_DOSSIER_CONTENT;

		// Renseigne la vue de route des étapes de feuille de route vers les dossiers
		routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_TRAITEMENT_VIEW);

		// load des mailbox
		getMailboxList();

		if (mailboxes != null && !mailboxes.isEmpty()) {
			navigateToCorbeilleTraitement(mailboxes.get(0).getDocument());
		}

		Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);

		return SolonEpgViewConstant.ESPACE_TRAITEMENT_VIEW;
	}

	/**
	 * Affichage de la contentview des mailbox poste.
	 * 
	 * @param mailboxDoc
	 * @return Vue
	 * @throws ClientException
	 */
	public String navigateToCorbeilleTraitement(DocumentModel mailboxDoc) throws ClientException {
		navigationContext.setCurrentDocument(mailboxDoc);
		activeContentViewName = ESPACE_TRAITEMENT_DOSSIER_CONTENT;
		Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);
		return SolonEpgViewConstant.ESPACE_TRAITEMENT_VIEW;
	}

	/**
	 * Affichage de la contentview des dossiers pour indexation.
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public String navigateToCorbeilleIndexation() throws ClientException {
		navigationContext.resetCurrentDocument();
		activeContentViewName = ESPACE_TRAITEMENT_INDEXATION_CONTENT;

		// Réinitialise la Mailbox en cours (la corbeille d'indexation est une corbeille virtuelle)
		cmContextHolder.setCurrentMailbox(null);
		Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);

		return SolonEpgViewConstant.ESPACE_TRAITEMENT_VIEW;
	}

	/**
	 * Retourne la contentview active
	 * 
	 * @return
	 */
	public String getActiveContentViewName() {
		return activeContentViewName;
	}

	/**
	 * Retourne la liste des mailbox poste de l'utilisateur connecté.
	 * 
	 * @return Liste des mailbox poste de l'utilisateur connecté
	 * @throws ClientException
	 */
	public List<Map<String, Serializable>> getMailboxList() throws ClientException {
		// on ne recharge pas les mailboxes si elles sont déjà chargées
		if (mailboxesEpg == null) {
			MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
			MailboxService mailboxService = SolonEpgServiceLocator.getMailboxServiceService();
			mailboxes = mailboxPosteService.getMailboxPosteList(documentManager);
			mailboxesEpg = new ArrayList<Map<String, Serializable>>();

			// FEV508, on positionne la corbeille par défaut en premiere position
			try {
				DocumentModel profil = SolonEpgServiceLocator.getProfilUtilisateurService().getProfilUtilisateurDoc(
						documentManager, currentNuxeoPrincipal.getName());
				ProfilUtilisateur profiladapte = profil.getAdapter(ProfilUtilisateur.class);
				String posteID = profiladapte.getPosteDefaut().toString();
				if (posteID != null) {
					Mailbox mlbxDefaut = mailboxPosteService.getMailboxPoste(documentManager, posteID);
					for (Mailbox mailbox : mailboxes) {
						if (mailbox.getId().equals(mlbxDefaut.getId())) {
							mailboxes.remove(mailbox);
							mailboxes.add(0, mailbox);
							break;
						}
					}
				}

			} catch (Exception e) {
				// on ne fait rien quant à la sélection d'une corbeille par défaut
			}

			// test nécessaire si 'mailboxes' est vide (Administrator)
			if (mailboxes != null && !mailboxes.isEmpty()) {
				for (Mailbox mailbox : mailboxes) {
					Map<String, Serializable> mapBox = new HashMap<String, Serializable>();
					mapBox.put("title", mailbox.getTitle());
					mapBox.put("document", mailbox.getDocument());
					mapBox.put("numberOfDossierLinkIds",
							mailboxService.getNumberOfDossierLinkIds(documentManager, (SolonEpgMailbox) mailbox));
					mailboxesEpg.add(mapBox);
				}
			}
		}
		return mailboxesEpg;
	}

	/**
	 * Retourne vrai si on est dans la liste des dossiers de l'espace de traitement
	 * 
	 * @return
	 */
	public boolean isInEspaceTraitementDossierContent() {
		return ESPACE_TRAITEMENT_DOSSIER_CONTENT.equals(activeContentViewName)
				&& navigationWebActions.getCurrentMainMenuAction() != null
				&& SolonEpgActionConstant.ESPACE_TRAITEMENT.equals(navigationWebActions.getCurrentMainMenuAction()
						.getId());
	}

	/**
	 * Retourne true si on est dans la corbeille pour indexation
	 * 
	 * @return
	 */
	public boolean isInCorbeilleIndexation() {
		return ESPACE_TRAITEMENT_INDEXATION_CONTENT.equals(activeContentViewName)
				&& SolonEpgActionConstant.ESPACE_TRAITEMENT.equals(navigationWebActions.getCurrentMainMenuAction()
						.getId());
	}

	/**
	 * Retourne vrai si le Dossier est urgent
	 * 
	 * @return Vrai si le Dossier est urgent
	 * @throws ClientException
	 */
	public boolean isDossierUrgent() throws ClientException {
		return dossierActions.isDossierUrgent(navigationContext.getCurrentDocument());
	}

	/**
	 * Enlève un dossier de la corbeille pour indexation active
	 * 
	 * @param dossierDoc
	 * @return
	 * @throws ClientException
	 * @throws PropertyException
	 */
	public String removeFromCorbeilleIndexation(DocumentModel dossierDoc) throws PropertyException, ClientException {
		String property = getCurrentUserIndexationProperty();
		if (property != null) {
			dossierDoc.setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, property, false);
			new UnrestrictedCreateOrSaveDocumentRunner(documentManager).saveDocument(dossierDoc);
			navigationContext.resetCurrentDocument();
		}
		// on rafraichit le nb de dossier "Pour Indexation"
		dossierIndexationQueryCount = null;
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		return null;
	}

	private String getCurrentUserIndexationProperty() {
		String property = null;
		NuxeoPrincipal userName = (NuxeoPrincipal) documentManager.getPrincipal();
		if (userName.isMemberOf(SolonEpgBaseFunctionConstant.INDEXATION_SGG_UPDATER)) {
			property = DossierSolonEpgConstants.DOSSIER_INDEXATION_SGG_PROPERTY;
		}
		if (userName.isMemberOf(SolonEpgBaseFunctionConstant.INDEXATION_SGG_PUBLI_UPDATER)) {
			property = DossierSolonEpgConstants.DOSSIER_INDEXATION_SGG_PUB_PROPERTY;
		}
		if (userName.isMemberOf(SolonEpgBaseFunctionConstant.INDEXATION_MIN_UPDATER)) {
			property = DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PROPERTY;
		}
		if (userName.isMemberOf(SolonEpgBaseFunctionConstant.INDEXATION_MIN_PUBLI_UPDATER)) {
			property = DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PUB_PROPERTY;
		}
		if (userName.isMemberOf(SolonEpgBaseFunctionConstant.INDEXATION_DIR_UPDATER)) {
			property = DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PROPERTY;
		}
		if (userName.isMemberOf(SolonEpgBaseFunctionConstant.INDEXATION_DIR_PUBLI_UPDATER)) {
			property = DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PUB_PROPERTY;
		}
		return property;
	}

	/**
	 * Renvoi le nombre de dossier dans la "corbeille" indexation pour l'utilisateur courant.
	 * 
	 * @return le nombre de dossier dans la "corbeille" indexation pour l'utilisateur courant.
	 * 
	 * @throws ClientException
	 */
	public Long getDossierIndexationQueryCount() throws ClientException {
		if (dossierIndexationQueryCount == null) {
			dossierIndexationQueryCount = QueryUtils.doCountQuery(documentManager, getDossierIndexationQuery());
		}
		return dossierIndexationQueryCount;
	}

	/**
	 * Renvoie la requête pour récupérer les dossiers pour indexation.
	 * 
	 * @return la requête pour récupérer les dossiers pour indexation.
	 */
	public String getDossierIndexationQuery() {
		StringBuilder dossierIndexationQueryBuilder = new StringBuilder(
				"SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.dos:");
		dossierIndexationQueryBuilder.append(DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY);
		dossierIndexationQueryBuilder.append(" IN('");
		// dossierIndexationQueryBuilder.append(VocabularyConstants.STATUT_INITIE);
		// dossierIndexationQueryBuilder.append("','" );
		dossierIndexationQueryBuilder.append(VocabularyConstants.STATUT_LANCE);
		dossierIndexationQueryBuilder.append("') AND ");
		dossierIndexationQueryBuilder.append(" testAcl(d.ecm:uuid) = 1 AND ");
		dossierIndexationQueryBuilder.append(getUFNXQLIndexationPart());

		return QueryUtils.ufnxqlToFnxqlQuery(dossierIndexationQueryBuilder.toString());
	}

	/**
	 * Condition NXQL pour filtrer les dossiers enlevés de la corbeille pour indexation
	 * 
	 * @return
	 */
	public String getUFNXQLIndexationPart() {

		String property = getCurrentUserIndexationProperty();
		if (property == null) {
			return null;
		}

		StringBuilder queryIndexationParam = new StringBuilder();
		queryIndexationParam.append("d.dos:").append(property).append(" = 1");

		if (property.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PROPERTY)
				|| property.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PUB_PROPERTY)) {

			// on vérifie que le ministere attaché au dossier correspond bien à un ministère de l'utilisateur
			SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
			Set<String> ministereIds = ssPrincipal.getMinistereIdSet();
			String[] ministereIdsArrayList = ministereIds.toArray(new String[ministereIds.size()]);
			String userMinisteres = StringUtil.join(ministereIdsArrayList, ",", "'");
			queryIndexationParam.append(" and d.dos:")
					.append(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY).append(" IN(")
					.append(userMinisteres).append(")");
		} else if (property.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PROPERTY)
				|| property.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PUB_PROPERTY)) {

			// on vérifie que le ministere attaché au dossier correspond bien à une direction de l'utilisateur
			SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
			Set<String> directionIds = ssPrincipal.getDirectionIdSet();
			String[] directionIdsArrayList = directionIds.toArray(new String[directionIds.size()]);
			String userDirection = StringUtil.join(directionIdsArrayList, ",", "'");

			queryIndexationParam.append(" and d.dos:")
					.append(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY).append(" IN(")
					.append(userDirection).append(")");
		}
		return queryIndexationParam.toString();
	}

	/**
	 * Réinitialise les listes et valeurs du bandeau vertical gauche de l'espace de traitement.
	 */
	public void refreshPage() {
		resetCount();
		refreshActions.refreshPage();
	}

	public void resetCount() {
		dossierIndexationQueryCount = null;
		mailboxesEpg = null;
	}

}
