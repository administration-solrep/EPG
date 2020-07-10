package fr.dila.solonepg.web.admin.suppression;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.context.NavigationContextBean;

@Name("suppressionActions")
@Scope(CONVERSATION)
public class SuppressionActionsBean implements Serializable {
	private static final long						serialVersionUID	= 1L;

	/**
	 * Logger.
	 */
	private static final Log						LOGGER				= LogFactory
																				.getLog(SuppressionActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean		navigationContext;
	@In(create = true)
	protected transient DocumentsListsManager		documentsListsManager;

	@In(create = true)
	protected transient NuxeoPrincipal				currentUser;

	@In(create = true)
	protected transient AdministrationActionsBean	administrationActions;

	@In(create = true, required = false)
	protected transient ContentViewActions			contentViewActions;

	/**    
	 * 
	 */
	public String									userName;

	/**
	 * 
	 */
	public String									password;
	/**
	 * 
	 */
	public String									typeAction;
	/**
	 * 
	 */
	public String									authenticationSuccess;

	public boolean isButtonToDisplay() {
		return !documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
	}

	/**
	 * Retirer Le document de la liste a supprimer
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String retirerSelection(String view) throws ClientException {

		String result = "";
		if (authentifier()) {
			List<DocumentModel> docs = documentsListsManager
					.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			for (DocumentModel dossierDoc : docs) {
				dossierService.retirerDossierFromSuppressionListUnrestricted(documentManager, dossierDoc);
			}
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			result = administrationActions.navigateToView(view);
			documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		}

		return result;
	}

	/**
	 * valider transmission de la selection a la liste de suppression
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String validerTransmissionSelection(String view) throws ClientException {
		String result = "";
		if (authentifier()) {
			List<DocumentModel> docs = documentsListsManager
					.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			final DossierService dossierService = SolonEpgServiceLocator.getDossierService();

			ArrayList<String> docsIdList = new ArrayList<String>();

			for (DocumentModel dossierDoc : docs) {
				dossierService.validerTransmissionToSuppressionListUnrestricted(documentManager, dossierDoc);
				docsIdList.add(dossierDoc.getId());
			}

			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

			// Notification
			EventProducer eventProducer = STServiceLocator.getEventProducer();
			Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
			eventProperties.put(SolonEpgEventConstant.DOCUMENT_ID_LIST, docsIdList);
			InlineEventContext inlineEventContext = new InlineEventContext(documentManager, currentUser,
					eventProperties);
			try {
				eventProducer.fireEvent(inlineEventContext
						.newEvent(SolonEpgEventConstant.AFTER_VALIDER_TRANSMISSION_TO_SUPPRESSION));
			} catch (ClientException e) {
				LOGGER.error("Erreur de lancement de l'event"
						+ SolonEpgEventConstant.AFTER_VALIDER_TRANSMISSION_TO_SUPPRESSION, e);
			}
			result = administrationActions.navigateToView(view);
			documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		}

		return result;
	}

	/**
	 * Supprimer les dossiers selectionnne
	 * 
	 * @return
	 * @throws Exception
	 */
	public String supprimerSelection(String view) throws Exception {
		String result = "";
		final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		if (authentifier()) {
			final List<DocumentModel> docs = documentsListsManager
					.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			final Principal principal = documentManager.getPrincipal();
			// on lance les suppressions des dossiers sans vérifier les droits
			new UnrestrictedSessionRunner(documentManager) {
				@Override
				public void run() throws ClientException {
					for (DocumentModel dossierDoc : docs) {
						Dossier dossier = dossierDoc.getAdapter(Dossier.class);
						if (dossierDoc.getLockInfo() == null
								&& VocabularyConstants.STATUT_INITIE.equals(dossier.getStatut())) {
							try {
								archiveService.supprimerDossier(session, dossierDoc, principal);
							} catch (Exception e) {
								throw new ClientException(e);
							}
						}
					}
				}
			}.runUnrestricted();

			documentManager.save();

			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

			// Reset de la liste de selection
			// documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			// contentViewActions.reset(SolonEpgContentView.SUPPRESSION_DOSSIER);
			result = administrationActions.navigateToView(view);
			documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		}
		return result;
	}

	/**
	 * recuperer la liste des ministeres attaches a l'utilisateur courant
	 * 
	 * @return la liste des ministeres attaches a l'utilisateur courant
	 */
	public String getMinisteresQuery() {
		Set<String> ministereIds = new HashSet<String>();
		if (currentUser != null) {
			// on récupère le ministère d'appartenance de l'utilisateur
			final SSPrincipal ssPrincipal = (SSPrincipal) currentUser;
			if (ssPrincipal.isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME)) {
				List<EntiteNode> ministereList = new ArrayList<EntiteNode>();
				try {
					ministereList = STServiceLocator.getSTMinisteresService().getMinisteres(true);
				} catch (ClientException ce) {
					LOGGER.error("Erreur de récupération de tous les noeuds ministères", ce);
				}
				for (OrganigrammeNode node : ministereList) {
					ministereIds.add(node.getId());
				}
			} else {
				ministereIds = ssPrincipal.getMinistereIdSet();
			}
		}
		return "('" + StringUtils.join(ministereIds.toArray(), "','") + "')";
	}

	/**
	 * authentifier l'acces de l'utilisateur saisie par l'utilisateur
	 * 
	 * @return
	 */
	public boolean authentifier() {
		boolean isAuthentifiedOk = false;
		if (currentUser != null) {
			UserManager userManager = STServiceLocator.getUserManager();
			if (userName != null && password != null && userManager.authenticate(userName, password)) {
				isAuthentifiedOk = true;
			}
		} else {
			isAuthentifiedOk = false;
		}
		if (isAuthentifiedOk) {
			authenticationSuccess = "check";
		} else {
			authenticationSuccess = "uncheck";
		}
		return isAuthentifiedOk;
	}

	/**
	 * Controle l'accès à la vue correspondante (suppression fonctionnelle)
	 * 
	 */
	public boolean isAccessFonctionnelAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_SUPPRESSION));
	}

	/**
	 * Controle l'accès à la vue correspondante (suppression ministérielle)
	 * 
	 */
	public boolean isAccessMinisterielAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_SUPPRESSION));
	}

	public String getUserName() {
		userName = currentUser.getName();
		return userName;
	}

	public String resetAuthenticationSuccess() {
		authenticationSuccess = null;
		return "check";
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	public void setToDoAction(String typeAction) {
		this.typeAction = typeAction;
		this.authenticationSuccess = null;
	}

	public String getAuthenticationSuccess() {
		return authenticationSuccess;
	}

	public void setAuthenticationSuccess(String authenticationSuccess) {
		this.authenticationSuccess = authenticationSuccess;
	}

	/**
	 * Retirer Le document de la liste a supprimer
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String retirerSelection() throws ClientException {
		List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		for (DocumentModel dossierDoc : docs) {
			dossierService.retirerDossierFromSuppressionListUnrestricted(documentManager, dossierDoc);
		}
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		administrationActions.navigateToView(contentViewActions.getCurrentContentView().getName());
		return contentViewActions.getCurrentContentView().getName();
	}

}