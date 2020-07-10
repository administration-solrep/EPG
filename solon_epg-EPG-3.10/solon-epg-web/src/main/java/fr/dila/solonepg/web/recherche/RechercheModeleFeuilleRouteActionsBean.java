package fr.dila.solonepg.web.recherche;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.nuxeo.ecm.core.model.NoSuchDocumentException;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.uidgen.service.ServiceHelper;
import org.nuxeo.ecm.platform.uidgen.service.UIDGeneratorService;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.criteria.FeuilleRouteCriteria;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.dao.FeuilleRouteDao;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.NavigationWebActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.groupcomputer.MinistereGroupeHelper;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.CollectionUtil;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * Bean Seam permettant de gérer les recherches de feuilles de route.
 * 
 * @author jtremeaux
 */
@Name("rechercheModeleFeuilleRouteActions")
@Scope(ScopeType.CONVERSATION)
public class RechercheModeleFeuilleRouteActionsBean implements Serializable {
	/**
	 * Serial UID.
	 */
	private static final long							serialVersionUID	= 1L;

	private static final Log							log					= LogFactory.getLog(RechercheModeleFeuilleRouteActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient ContentViewActions				contentViewActions;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient UserWorkspaceManagerActionsBean	userWorkspaceManagerActions;

	@In(create = true)
	protected transient DocumentsListsManager			documentsListsManager;

	/**
	 * Document favori de recherche en cours.
	 */
	private DocumentModel								favoriRechercheDoc;

	/**
	 * Critères de recherche sous forme textuelle.
	 */
	private String										searchQueryString;
	
	@In(required = true, create = true)
    protected SSPrincipal ssPrincipal;
	
	@In(create = true, required = false)
    protected FacesMessages facesMessages;

	@In(create = true)
	protected STLockActionsBean stLockActions;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean navigationWebActions;

	/**
	 * Retourne vrai si l'utilisateur des modèles de feuilles de route sont sélectionnés dans une des listes de l'espace
	 * de recherche.
	 * 
	 * @return Condition
	 */
	public boolean isModeleFeuilleRouteSelected() {
		String currentContentViewName = contentViewActions.getCurrentContentView().getName();
		String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
		return (SolonEpgContentViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT_CONTENT_VIEW
				.equals(currentContentViewName)
				|| SolonEpgContentViewConstant.RECHERCHE_RESULTATS_CONSULTES_MODELE_FEUILLE_ROUTE_CONTENT_VIEW
						.equals(currentContentViewName) || SolonEpgContentViewConstant.RECHERCHE_FAVORIS_CONSULTATION_MODELE_FEUILLE_ROUTE_CONTENT_VIEW
					.equals(currentContentViewName))
				&& !documentsListsManager.isWorkingListEmpty(selectionListName);
	}

	/**
	 * Exécute la recherche des modèles de feuille de route.
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public String search() throws ClientException {
		searchQueryString = null;

		// Invalide la content view des résultats de recherche
		contentViewActions.reset("recherche_modele_feuille_route_resultat");

		// Invalide le modèle de feuille de route affiché
		navigationContext.resetCurrentDocument();

		routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT);

		return SolonEpgViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT;
	}

	/**
	 * Retourne les critères de recherche des modèles de feuille de route sous forme textuelle.
	 * 
	 * @return Critères de recherche des modèles de feuille de route sous forme textuelle
	 * @throws ClientException
	 */
	public String getSearchQueryString() throws ClientException {
		final EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
		FeuilleRouteCriteria criteria = espaceRechercheService.getFeuilleRouteCriteria(favoriRechercheDoc);
		FeuilleRouteDao feuilleRouteDao = new FeuilleRouteDao(documentManager, criteria);

		return feuilleRouteDao.getQueryString();
	}

	/**
	 * Retourne les critères de recherche des modèles de feuille de route sous forme textuelle.
	 * 
	 * @return Critères de recherche des modèles de feuille de route sous forme textuelle
	 * @throws ClientException
	 */
	public List<Object> getSearchQueryParameter() throws ClientException {
		final EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
		FeuilleRouteCriteria criteria = espaceRechercheService.getFeuilleRouteCriteria(favoriRechercheDoc);
		FeuilleRouteDao feuilleRouteDao = new FeuilleRouteDao(documentManager, criteria);

		return feuilleRouteDao.getParamList();
	}

	/**
	 * Retourne les critères de recherche des modèles de feuille de route pour l'affichage.
	 * 
	 * @return Critères de recherche pour l'affichage
	 * @throws ClientException
	 */
	public String getSearchQueryStringForDisplay() throws ClientException {
		if (searchQueryString == null) {
			final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
			final STUserService stUserService = STServiceLocator.getSTUserService();
			final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
			FavorisRecherche favorisRecherche = favoriRechercheDoc.getAdapter(FavorisRecherche.class);
			List<String> criteriaList = new ArrayList<String>();

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteTitle())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteIntitule.criteria");
				criteriaList.add(MessageFormat.format(message, favorisRecherche.getFeuilleRouteTitle()));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteTypeActe())) {
				String message = resourcesAccessor.getMessages().get(
						"epg.recherche.feuilleRoute.feuilleRoute.feuilleRouteTypeActe.criteria");
				String label = vocabularyService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY,
						favorisRecherche.getFeuilleRouteTypeActe());
				criteriaList.add(MessageFormat.format(message, label));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteNumero())) {
				String message = resourcesAccessor.getMessages().get(
						"epg.recherche.feuilleRoute.feuilleRoute.feuilleRouteNumero.criteria");
				criteriaList.add(MessageFormat.format(message, favorisRecherche.getFeuilleRouteNumero()));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteCreationUtilisateur())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteCreationUtilisateur.criteria");
				String label = stUserService.getUserFullName(favorisRecherche.getFeuilleRouteCreationUtilisateur());
				criteriaList.add(MessageFormat.format(message, label));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteMinistere())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteMinistere.criteria");
				OrganigrammeNode entiteNode = STServiceLocator.getSTMinisteresService().getEntiteNode(
						favorisRecherche.getFeuilleRouteMinistere());
				criteriaList.add(MessageFormat.format(message, entiteNode.getLabel()));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteDirection())) {
				String message = resourcesAccessor.getMessages().get(
						"epg.recherche.feuilleRoute.feuilleRoute.feuilleRouteDirection.criteria");
				OrganigrammeNode entiteNode = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode(
						favorisRecherche.getFeuilleRouteDirection());
				criteriaList.add(MessageFormat.format(message, entiteNode.getLabel()));
			}

			if (favorisRecherche.getFeuilleRouteCreationDateMin() != null) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteCreationDateMin.criteria");
				String date = DateUtil.formatDDMMYYYYSlash(favorisRecherche.getFeuilleRouteCreationDateMin());
				criteriaList.add(MessageFormat.format(message, date));
			}

			if (favorisRecherche.getFeuilleRouteCreationDateMax() != null) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteCreationDateMax.criteria");
				String date = DateUtil.formatDDMMYYYYSlash(favorisRecherche.getFeuilleRouteCreationDateMax());
				criteriaList.add(MessageFormat.format(message, date));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteValidee())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteValidee.criteria");
				String label = vocabularyService.getEntryLabel(VocabularyConstants.BOOLEAN_VOCABULARY,
						favorisRecherche.getFeuilleRouteValidee());
				criteriaList.add(MessageFormat.format(message, label));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getRouteStepRoutingTaskType())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.routeStep.routeStepRoutingTaskType.criteria");
				String labelKey = vocabularyService.getEntryLabel(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY,
						favorisRecherche.getRouteStepRoutingTaskType());
				criteriaList.add(MessageFormat.format(message, resourcesAccessor.getMessages().get(labelKey)));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getRouteStepDistributionMailboxId())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.routeStep.routeStepDistributionMailboxId.criteria");
				String posteId = mailboxPosteService.getPosteIdFromMailboxId(favorisRecherche
						.getRouteStepDistributionMailboxId());
				OrganigrammeNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteId);
				criteriaList.add(MessageFormat.format(message, posteNode.getLabel()));
			}

			if (favorisRecherche.getRouteStepEcheanceIndicative() != null) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.routeStep.routeStepEcheanceIndicative.criteria");
				criteriaList.add(MessageFormat.format(message, favorisRecherche.getRouteStepEcheanceIndicative()));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getRouteStepAutomaticValidation())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.routeStep.routeStepAutomaticValidation.criteria");
				String label = vocabularyService.getEntryLabel(VocabularyConstants.BOOLEAN_VOCABULARY,
						favorisRecherche.getRouteStepAutomaticValidation());
				criteriaList.add(MessageFormat.format(message, label));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireSgg())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.routeStep.routeStepObligatoireSgg.criteria");
				String label = vocabularyService.getEntryLabel(VocabularyConstants.BOOLEAN_VOCABULARY,
						favorisRecherche.getRouteStepObligatoireSgg());
				criteriaList.add(MessageFormat.format(message, label));
			}

			if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireMinistere())) {
				String message = resourcesAccessor.getMessages().get(
						"ss.recherche.feuilleRoute.routeStep.routeStepObligatoireMinistere.criteria");
				String label = vocabularyService.getEntryLabel(VocabularyConstants.BOOLEAN_VOCABULARY,
						favorisRecherche.getRouteStepObligatoireMinistere());
				criteriaList.add(MessageFormat.format(message, label));
			}

			searchQueryString = StringUtils.join(criteriaList,
					" " + resourcesAccessor.getMessages().get("ss.recherche.feuilleRoute.criteria.separator") + " ");
		}
		return searchQueryString;
	}

	/**
	 * Retourne le document favori de recherche en cours.
	 * 
	 * @return Document favori de recherche en cours
	 * @throws ClientException
	 */
	public DocumentModel getFavoriRechercheDoc() throws ClientException {
		if (favoriRechercheDoc == null) {
			EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
			favoriRechercheDoc = espaceRechercheService.createBareFavorisRecherche(documentManager,
					SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_MODELE_FEUILLE_ROUTE_VALUE);
		}

		return favoriRechercheDoc;
	}

	/**
	 * Renseigne le document du favori de recherche.
	 * 
	 * @param favoriRechercheDoc
	 *            Document du favori de recherche
	 */
	public void setFavoriRechercheDoc(DocumentModel favoriRechercheDoc) {
		this.favoriRechercheDoc = favoriRechercheDoc;
	}

	/**
	 * Réinitialise l'écran de saisie des critères de recherche.
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public String reset() throws ClientException {
		favoriRechercheDoc = null;
		searchQueryString = null;

		// Invalide la content view des résultats de recherche
		contentViewActions.reset("recherche_modele_feuille_route_resultat");

		// Invalide le modèle de feuille de route affiché
		navigationContext.resetCurrentDocument();

		return SolonEpgViewConstant.VIEW_ESPACE_RECHERCHE_FDR;
	}

	/**
	 * Navigue vers un modèle de feuille de route à partir des résultats de recherche ou des favoris de recherche /
	 * consultation.
	 * 
	 * @param feuilleRouteDoc
	 *            Modèle de feuille de route à charger
	 * @return Vue
	 * @throws ClientException
	 */
	public String loadFeuilleRoute(DocumentModel feuilleRouteDoc) throws ClientException {
		// Charge la feuille de route comme document courant
		navigationContext.navigateToDocument(feuilleRouteDoc);

		return routingWebActions.getFeuilleRouteView();
	}
	
	/**
	 * Indique si l'utilisateur courant peut dupliquer la feuille de route en
	 * paramètre.
	 * 
	 * @param feuilleRouteDoc
	 * @return true ssi parmi les groupes de l'utilisateur courant on trouve
	 *         FEUILLE_DE_ROUTE_MODEL_UPDATER (admin fonctionnel / admin
	 *         ministériel)
	 */
	public boolean isCanDuplicate(DocumentModel feuilleRouteDoc) {
		final List<String> groups = ssPrincipal.getGroups();
		return groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER);
	}
	
	public String duplicateRouteModel(DocumentModel doc) throws ClientException {
		final List<String> groups = ssPrincipal.getGroups();
		boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);
		boolean canCreateFDR = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER);

		if (!canCreateFDR) {
			// On n'a pas le droit de dupliquer les feuilles de route
			String errorMessage = resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.cannotDuplicate");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}
		
		final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();
		
		String ministere = null;
		if (!isAdminFonctionnel) {
			// Affecte la nouvelle feuille de route au premier ministère de cet administrateur ministériel
			Set<String> ministereSet = ssPrincipal.getMinistereIdSet();
			
			if (CollectionUtil.isEmpty(ministereSet)) {
				throw new ClientException("Aucun ministère défini pour cet administrateur ministériel");
			}
			ministere = ministereSet.iterator().next();
		}
		try {
			DocumentRoute newFeuilleRoute = documentRoutingService.duplicateRouteModel(documentManager, doc, ministere);
			DocumentModel newDoc = newFeuilleRoute.getDocument();

			// Compteur
			final UIDGeneratorService uidGeneratorService = ServiceHelper.getUIDGeneratorService();
			UIDSequencer sequencer = uidGeneratorService.getSequencer();
			String numero = Integer.toString(sequencer.getNext("SOLON_EPG_FDR_MODEL") + 1);
			SolonEpgFeuilleRoute feuilleRoute = newDoc.getAdapter(SolonEpgFeuilleRoute.class);
			feuilleRoute.setNumero(numero);
			feuilleRoute.save(documentManager);

			// Recharge la liste des modèles de feuilles de route
			DocumentModel parent = documentManager.getDocument(newDoc.getParentRef());
			Events.instance().raiseEvent(EventNames.DOCUMENT_CHILDREN_CHANGED, parent);

			// Charge le nouveau document en édition
			navigationContext.setCurrentDocument(newDoc);

			// Affiche un message d'information
			String infoMessage = resourcesAccessor.getMessages().get("st.feuilleRoute.action.duplicated.success");
			facesMessages.add(StatusMessage.Severity.INFO, infoMessage);

			// on logge l'action de duplication du modèle de feuille de route
			final JournalService journalService = STServiceLocator.getJournalService();
			String comment = "Duplication du modèle de feuille de route [" + feuilleRoute.getTitle() + "]";
			journalService.journaliserActionAdministration(documentManager, SolonEpgEventConstant.DUPLICATION_MODELE_FDR_EVENT, comment);

			return routingWebActions.getFeuilleRouteView();
		} catch (ClientException clientException) {
			if (ExceptionUtils.getRootCause(clientException) instanceof NoSuchDocumentException) {
				// le document a été supprimé par un autre utilisateur ou l'utilisateur n'a plus les droits pour voir le
				// document : on le signale à l'utilisateur sans renvoyer une exception
				String errorMessage = resourcesAccessor.getMessages().get(STConstant.NO_SUCH_DOCUMENT_ERROR_MSG);
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return null;
			}
			throw clientException;
		}
	}

	/**
	 * Indique si l'utilisateur courant peut supprimer la feuille de route en paramètre.
	 * 
	 * @param feuilleRouteDoc
	 * @return boolean
	 */
	public boolean isCanDelete(DocumentModel feuilleRouteDoc) {
		// La feuille de route par défaut globale ne peut pas être supprimée
		SolonEpgFeuilleRoute route = feuilleRouteDoc.getAdapter(SolonEpgFeuilleRoute.class);
		if (route.isFeuilleRouteDefautGlobal()) {
			return false;
		}

		// Les modèles verrouillés par un autre utilisateur sont non supprimables
		if (!stLockActions.canUserLockDoc(feuilleRouteDoc)) {
			return false;
		}

		final STFeuilleRoute feuilleRoute = feuilleRouteDoc.getAdapter(STFeuilleRoute.class);
		final String ministere = feuilleRoute.getMinistere();
		final String groupMinistere = MinistereGroupeHelper.ministereidToGroup(ministere);

		// Les administrateurs fonctionnels ont le droit d'écriture sur tous les modèles
		final List<String> groups = ssPrincipal.getGroups();
		if (groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR)) {
			return true;
		} else if (groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER) && groups.contains(groupMinistere)) {
			// Les administrateurs ministériels ont le droit d'écriture sur les modèles de leur ministère sauf sur ceux crées par un Administrateur fonctionnel
			if(isFeuilleDeRouteCreeParAdminFonctionnel(feuilleRouteDoc)){
				return false;
			} else {
				return true;
			}
		} else {
			if (StringUtils.isBlank(ministere) || !groups.contains(groupMinistere) || !groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Retourne vrai si le document est crée par l'adminitrateur fonctionnel.
	 * 
	 * @param doc
	 * @return boolean
	 */
	public boolean isFeuilleDeRouteCreeParAdminFonctionnel(DocumentModel doc) {
		String feuilleDeRouteCreateur = DublincoreSchemaUtils.getCreator(doc);

		UserManager userManager = STServiceLocator.getUserManager();
		SSPrincipal principal;
		try {
			principal = (SSPrincipal) userManager.getPrincipal(feuilleDeRouteCreateur);
		} catch (ClientException e) {
			return false;
		}

		if (principal == null) {
			return false;
		} else {
			return principal.isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME);
		}
	}

	public String deleteRouteModel(DocumentModel doc) throws ClientException {
		try {
			navigationWebActions.deleteDocument(doc);
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			return null;
		} catch (ClientException e) {
			if (ExceptionUtils.getRootCause(e) instanceof NoSuchDocumentException) {
				// le document a été supprimé par un autre utilisateur ou l'utilisateur n'a plus les droits pour voir le
				// document : on le signale à l'utilisateur sans renvoyer une exception
				String errorMessage = resourcesAccessor.getMessages().get(STConstant.NO_SUCH_DOCUMENT_ERROR_MSG);
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				log.warn(e);
			} else {
				log.error(e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			}

			return null;
		}
	}
}