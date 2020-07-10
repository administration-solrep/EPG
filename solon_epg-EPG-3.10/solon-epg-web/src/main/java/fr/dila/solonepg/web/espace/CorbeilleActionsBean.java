package fr.dila.solonepg.web.espace;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
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
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.model.NoSuchDocumentException;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.cm.web.mailbox.CaseManagementMailboxActionsBean;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.profilutilisateur.ProfilUtilisateurAdministrationActionsBean;
import fr.dila.solonepg.web.client.DossierListingDTO;
import fr.dila.solonepg.web.client.DossierListingDTOImpl;
import fr.dila.solonepg.web.contentview.DossierPageProvider;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierActionsBean;
import fr.dila.solonepg.web.dossier.DossierCreationActionsBean;
import fr.dila.solonepg.web.dossier.DossierListingActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

@Name("corbeilleActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK + 1)
public class CorbeilleActionsBean extends fr.dila.st.web.corbeille.CorbeilleActionsBean implements Serializable {
	private static final long								serialVersionUID	= -6601690797613742328L;

	@In(create = true, required = true)
	protected transient CoreSession							documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean				navigationContext;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean		routingWebActions;

	@In(create = true, required = false)
	protected transient CaseManagementMailboxActionsBean	cmMailboxActions;

	@In(create = true, required = false)
	protected transient ActionManager						actionManager;

	@In(create = true, required = false)
	protected transient ContentViewActions					contentViewActions;

	@In(create = true, required = false)
	protected transient DossierActionsBean					dossierActions;

	@In(create = true)
	protected transient NavigationWebActionsBean			navigationWebActions;

	@In(create = true, required = false)
	protected transient DossierListingActionsBean			dossierListingActions;

	@In(create = true)
	protected ResourcesAccessor								resourcesAccessor;

	@In(create = true, required = false)
	protected FacesMessages									facesMessages;

	@In(create = true, required = false)
	protected DossierCreationActionsBean					dossierCreationActions;

	@In(create = true, required = false)
	protected EspaceTraitementActionsBean					espaceTraitementActions;

	@In(create = true, required = false)
	protected ProfilUtilisateurAdministrationActionsBean	profilUtilisateurAdministrationActions;

	private DossierLink										currentDossierLink;
	
	private static final STLogger							LOGGER = STLogFactory.getLog(CorbeilleActionsBean.class);

	/**
	 * Variable initialisée à chaque requête pour savoir si on est à l'étape pour avis CE.
	 * (le calcul est coûteux, on s'assure donc de ne le faire qu'une fois par requête)
	 */
	protected Boolean									etapePourAvisCE;
	
	/**
	 * Vérifie que le DossierLink est présent
	 * 
	 */
	@Override
	public boolean isDossierLoadedInCorbeille() {
		if (currentDossierLink == null) {
			return false;
		}

		return true;
	}

	/**
	 * Charge le DossierLink en session lors de l'ouverture d'un dossier
	 * 
	 * @param dossierLink
	 * @param dossierDoc
	 * @param read
	 * @throws ClientException
	 */

	public String readDossierLink(DocumentModel dossierLink, final DocumentModel dossierDoc, final Boolean read,
			final String contentViewName) throws ClientException {
		if (dossierLink == null) {
			// DossierLink non chargé
			final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
			final List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager,
					dossierDoc.getId());
			if (dossierLinkList.size() > 0) {
				dossierLink = dossierLinkList.get(0);
			}
		}

		if (dossierLink == null) {
			currentDossierLink = null;
		} else {
			// Champ "lu"
			if (!read) {
				final DossierLink dossierL = dossierLink.getAdapter(DossierLink.class);
				dossierL.setReadState(Boolean.TRUE);
				documentManager.saveDocument(dossierLink);
			}
			currentDossierLink = dossierLink.getAdapter(DossierLink.class);

			if (contentViewName != null) {

				final ContentView contentView = contentViewActions.getContentViewWithProvider(contentViewName);
				if (contentView == null) {
					return null;
				}
				final PageProvider<?> pageProvider = contentView.getCurrentPageProvider();

				DossierListingDTO dossierDto;
				if (pageProvider instanceof DossierPageProvider) {
					final DossierPageProvider dossierPageProvider = (DossierPageProvider) pageProvider;
					if (dossierPageProvider.retrieveIsCaseLink()) {
						dossierDto = dossierPageProvider.initDossierLinkDto(currentDossierLink, null);
						dossierPageProvider.setCurrentEntry(dossierDto);
					}
				}
			}
		}
		// Navigation vers le Dossier
		navigationContext.navigateToDocument(dossierDoc);

		// Retour à la vue courante
		return routingWebActions.getFeuilleRouteView();
	}

	/**
	 * Charge le DossierLink en session lors de l'ouverture d'un dossier
	 * 
	 * @param dossierLink
	 * @param dossierDoc
	 * @param read
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public String readDossierLink(final String dossierLinkId, final String dossierDocId, final Boolean read,
			final String contentViewName, final DossierListingDTO dossierListingDTO) throws ClientException {
		DocumentModel dossierLinkDoc = null;
		if (dossierLinkId != null) {
			dossierLinkDoc = documentManager.getDocument(new IdRef(dossierLinkId));
		}

		if (dossierLinkDoc == null) {
			// DossierLink non chargé
			final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
			final List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager, dossierDocId);
			if (dossierLinkList.size() > 0) {
				dossierLinkDoc = dossierLinkList.get(0);
			}
		}

		if (dossierLinkDoc == null) {
			currentDossierLink = null;
		} else {
			// Champ "lu"
			if (!read) {
				final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
				dossierLink.setReadState(Boolean.TRUE);
				documentManager.saveDocument(dossierLinkDoc);
			}
			currentDossierLink = dossierLinkDoc.getAdapter(DossierLink.class);

			if (contentViewName != null) {
				final PageProvider<DossierListingDTO> pageProvider = (PageProvider<DossierListingDTO>) contentViewActions
						.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
				pageProvider.setCurrentEntry(dossierListingDTO);
			}
		}

		// Navigation vers le Dossier
		navigationContext.navigateToDocument(documentManager.getDocument(new IdRef(dossierDocId)));
		if (!read) {
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		}
		// Retour à la vue courante
		return routingWebActions.getFeuilleRouteView();
	}

	public String navigateToNextDossier(final String contentViewName) throws ClientException {
		final ContentView contentView = contentViewActions.getContentViewWithProvider(contentViewName);
		if (contentView == null) {
			return null;
		}
		PageProvider<?> pageProvider = contentView.getCurrentPageProvider();

		pageProvider = refreshCurrentEntry(pageProvider);
		pageProvider.nextEntry();
		return navigateToEntry(contentViewName, pageProvider);

	}

	/**
	 * Met à jour la currentEntry du provider : nécessaire car cela n'est pas fait lorsque que l'on trie la liste;
	 * 
	 * @param contentView
	 * @return
	 * @throws ClientException
	 */
	private PageProvider<?> refreshCurrentEntry(final PageProvider<?> pageProvider) {

		try {
			if (pageProvider instanceof DossierPageProvider) {
				if (!(pageProvider.getCurrentEntry() instanceof DocumentModel)) {
					final DossierPageProvider dossierPageProvider = (DossierPageProvider) pageProvider;
					// on récupère l'objet courant
					DossierListingDTO currentDossierDto;
					if (dossierPageProvider.retrieveIsCaseLink()) {
						currentDossierDto = dossierPageProvider.initDossierLinkDto(currentDossierLink, null);
						// note : pas besoin de setter toutes les propriétés étant donné que la vérification va se faire
						// sur le caseLinkId.
					} else {
						currentDossierDto = new DossierListingDTOImpl();
						currentDossierDto.setDossierId(navigationContext.getCurrentDocument().getId());
						// note : la vérification se fait sur le dossierId
					}
					dossierPageProvider.setCurrentEntry(currentDossierDto);
				}
			}
		} catch (final Exception e) {
			// mantis #36835
			LOGGER.warn(documentManager, STLogEnumImpl.LOG_EXCEPTION_TEC, "Ignoring : ", e);
		}
		return pageProvider;
	}

	@SuppressWarnings("unused")
	protected String navigateToEntry(final String contentViewName, final PageProvider<?> pageProvider)
			throws ClientException {
		try {
			if (pageProvider.getCurrentEntry() instanceof DocumentModel) {
				final DocumentModel doc = (DocumentModel) pageProvider.getCurrentEntry();
				if (doc.hasFacet("CaseLink")) {
					final DocumentModel dossierDoc = dossierActions.getDossierFromDossierLink(doc);
					return readDossierLink(doc, dossierDoc, doc.getAdapter(DossierLink.class).isRead(), contentViewName);
				} else {
					return readDossierLink(null, doc, false, contentViewName);
				}
			} else {
				// DTO
				final DossierListingDTO dto = (DossierListingDTO) pageProvider.getCurrentEntry();
				final DocumentModel dossierDoc = documentManager.getDocument(new IdRef(dto.getDocIdForSelection()));
				return readDossierLink(null, dossierDoc, false, contentViewName);
			}
		} catch (final Exception e) {
			// mantis #36835
			LOGGER.warn(documentManager, STLogEnumImpl.LOG_EXCEPTION_TEC, "Ignoring : ", e);
		}
		return null;
	}

	public String navigateToPreviousDossier(final String contentViewName) throws ClientException {
		final ContentView contentView = contentViewActions.getContentViewWithProvider(contentViewName);
		if (contentView == null) {
			return null;
		}

		PageProvider<?> pageProvider = contentView.getCurrentPageProvider();
		pageProvider = refreshCurrentEntry(pageProvider);
		pageProvider.previousEntry();

		return navigateToEntry(contentViewName, pageProvider);

	}

	/**
	 * Remet le tag "non-lu" sur le DossierLink chargé et ferme le dossier ouvert.
	 * 
	 * @return null
	 * @throws ClientException
	 * @throws PropertyException
	 */
	public String unreadDossierLink() throws PropertyException, ClientException {
		if (currentDossierLink != null) {
			// remet le tag "non lu"
			final DocumentModel dossierLinkDoc = currentDossierLink.getDocument();
			final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
			dossierLink.setReadState(Boolean.FALSE);
			documentManager.saveDocument(dossierLinkDoc);
			// fermeture du dossier
			navigationContext.resetCurrentDocument();
		}

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		return null;
	}

	/**
	 * Retourne le DossierLink courant.
	 * 
	 * @return DossierLink courant
	 */
	@Factory(value = "currentDossierLink", scope = ScopeType.EVENT)
	public DossierLink getCurrentDossierLink() {
		return currentDossierLink;
	}

	/**
	 * Renseigne le DossierLink courant.
	 * 
	 * @param dossierLink
	 *            DossierLink courant
	 */
	public void setCurrentDossierLink(final DossierLink dossierLink) {
		currentDossierLink = dossierLink;
	}

	/**
	 * Renseigne le DossierLink courant.
	 * 
	 * @param dossierLink
	 *            DossierLink courant
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public void setDossierLinkEntryInPageProvider(final DossierLink dossierLink, final String contentViewName)
			throws ClientException {
		setCurrentDossierLink(dossierLink);
		final ContentView contentView = contentViewActions.getContentViewWithProvider(contentViewName);
		final PageProvider<DocumentModel> pageProvider = (PageProvider<DocumentModel>) contentView
				.getCurrentPageProvider();
		pageProvider.setCurrentEntry(dossierLink.getCase(documentManager).getDocument());
	}

	/**
	 * Permet de décharger le dossier courant et le DossierLink si renseigné.
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public void resetCurrentDossier() throws ClientException {
		navigationContext.resetCurrentDocument();
		currentDossierLink = null;

		// reset des count de l'epsace de traitement
		espaceTraitementActions.resetCount();

		// reset du count de l'espace de creation
		dossierCreationActions.resetCount();

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

	}

	public String navigateToDossier(final String docId, final String contentViewName,
			final DossierListingDTO dossierListingDTO) throws ClientException {
		return navigateToDossier(docId, null, contentViewName, dossierListingDTO);
	}

	@SuppressWarnings("unchecked")
	public String navigateToDossier(final String docId, final String tabId, final String contentViewName,
			final DossierListingDTO dossierListingDTO) throws ClientException {
		if (contentViewName != null) {
			final PageProvider<DossierListingDTO> pageProvider = (PageProvider<DossierListingDTO>) contentViewActions
					.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
			pageProvider.setCurrentEntry(dossierListingDTO);
		}
		try {
			final DocumentModel dossierDoc = documentManager.getDocument(new IdRef(docId));
			return dossierListingActions.navigateToDossier(dossierDoc, tabId);
		} catch (final ClientException clientException) {
			if (ExceptionUtils.getRootCause(clientException) instanceof NoSuchDocumentException) {
				// le document a été supprimé par un autre utilisateur ou l'utilisateur n'a plus les droits pour voir le
				// document : on le signale à l'utilisateur sans renvoyer une exception
				final String errorMessage = resourcesAccessor.getMessages().get(STConstant.NO_SUCH_DOCUMENT_ERROR_MSG);
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return null;
			}
			throw clientException;
		}

	}

	public String navigateToDossierLink(final String dossierDocId, final String dossierLinkDocId,
			final String contentViewName, final DossierListingDTO dossierListingDTO) throws ClientException {
		return navigateToDossierLink(dossierDocId, dossierLinkDocId, null, contentViewName, dossierListingDTO);
	}

	@SuppressWarnings("unchecked")
	public String navigateToDossierLink(final String dossierDocId, final String dossierLinkDocId, final String tabId,
			final String contentViewName, final DossierListingDTO dossierListingDTO) throws ClientException {
		if (contentViewName != null) {
			final PageProvider<DossierListingDTO> pageProvider = (PageProvider<DossierListingDTO>) contentViewActions
					.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
			pageProvider.setCurrentEntry(dossierListingDTO);
		}

		final DocumentModel dossierDoc = documentManager.getDocument(new IdRef(dossierDocId));
		final DocumentModel dossierLinkDoc = documentManager.getDocument(new IdRef(dossierLinkDocId));
		return dossierListingActions.navigateToDossierLink(dossierDoc, dossierLinkDoc, tabId);
	}

	/**
	 * Renvoie vrai si la liste de dossier courante n'a qu'un seul résultat. Dans ce cas, ce résultat devient
	 * automatiquement le document courant.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public boolean oneDossierInList(final String contentViewName) throws ClientException {
		if (StringUtils.isEmpty(contentViewName)) {
			return false;
		}
		final PageProvider<DossierListingDTO> pageProvider = (PageProvider<DossierListingDTO>) contentViewActions
				.getContentViewWithProvider(contentViewName).getCurrentPageProvider();
		if (pageProvider.getResultsCount() != 1L) {
			return false;
		}
		// on récupère le premier dossier de la page car il n'y a en a qu'un.
		final DossierListingDTO dossierListingDto = pageProvider.getCurrentPage().get(0);
		final String dossierId = dossierListingDto.getDossierId();
		if (navigationContext.getCurrentDocument() != null
				&& navigationContext.getCurrentDocument().getId().equals(dossierId)) {
			// le dossier est déjà sélectionné, on ne rien de plus
			return true;
		}
		// on sélectionne le dossier de la liste et on rècupère son dossierLink
		navigateToDossier(dossierId, contentViewName, dossierListingDto);
		return true;
	}

	public String getMesureNominativeRestriction() {
		if (profilUtilisateurAdministrationActions.isVisibleMesureNominative()) {
			return "";
		} else {
			final StringBuilder queryBuilder = new StringBuilder(" AND d.");
			queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
			queryBuilder.append(":");
			queryBuilder.append(DossierSolonEpgConstants.DOSSIER_MESURE_NOMINATIVE_PROPERTY);
			queryBuilder.append(" = 0 ");
			return queryBuilder.toString();
		}
	}
	
	public boolean isEtapePourAvisCE() {
		// mécanisme de cache
		if (etapePourAvisCE == null) {
			DocumentModel dossierDoc = navigationContext.getCurrentDocument();
			try {
				final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
				etapePourAvisCE = corbeilleService.existsPourAvisCEStep(documentManager, dossierDoc.getId());
			} catch (final ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierDoc, e);
				return false;
			}
		}
		return etapePourAvisCE;
	}
	
	public void resetIsEtapePourAvisCE() {
		etapePourAvisCE = null;
	}
	
	@Observer(value = { SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT,
			SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT,
			SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT,
			EventNames.DOCUMENT_CHANGED, EventNames.DOCUMENT_SELECTION_CHANGED })
	public void clearEtapePourAvisCE() {
		resetIsEtapePourAvisCE();
	}
}
