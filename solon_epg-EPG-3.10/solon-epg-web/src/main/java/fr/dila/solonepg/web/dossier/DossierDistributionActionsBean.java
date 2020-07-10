package fr.dila.solonepg.web.dossier;

import static org.jboss.seam.ScopeType.EVENT;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;
import org.nuxeo.ecm.webapp.action.WebActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.exception.DocumentRouteAlredayLockedException;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonepg.web.espace.EspaceCreationActionsBean;
import fr.dila.solonepg.web.espace.EspaceTraitementActionsBean;
import fr.dila.ss.api.constant.SSTreeConstants;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.exception.LocalizedClientException;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.jeton.JetonDoc;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.dossier.DossierLockActionsBean;
import fr.dila.st.web.lock.STLockActionsBean;
import fr.sword.xsd.solon.epg.PublicationIntOuExtType;
import fr.sword.xsd.solon.epg.TypeModification;

/**
 * WebBean permettant de gérer la distribution des dossiers.
 * 
 * @author ARN
 */
@Name("dossierDistributionActions")
@Scope(ScopeType.CONVERSATION)
public class DossierDistributionActionsBean implements Serializable {

	/**
	 * Serial UID.
	 */
	private static final long							serialVersionUID	= -6359405896496460937L;

	/**
	 * Logger.
	 */
	private static final Log							log					= LogFactory
																					.getLog(DossierDistributionActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient DocumentRoutingActionsBean		routingActions;

	@In(create = true, required = false)
	protected transient ContentViewActions				contentViewActions;

	@In(create = true, required = false)
	protected transient EspaceCreationActionsBean		espaceCreationActions;

	@In(create = true, required = false)
	protected transient EspaceTraitementActionsBean		espaceTraitementActions;

	@In(create = true, required = false)
	protected transient BordereauActionsBean			bordereauActions;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	@In(create = true, required = false)
	protected transient STLockActionsBean				stLockActions;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@Out(required = false)
	protected Boolean									retourModificationPopup;

	@In(create = true, required = false)
	protected transient DossierListingActionsBean		dossierListingActions;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient WebActionsBean					webActions;
	protected final FeuilleRouteService					feuilleRouteService	= SolonEpgServiceLocator
																					.getFeuilleRouteService();

	protected transient List<FondDeDossierFile>			listFDDFileModifiesFDD;

	protected transient List<FondDeDossierFile>			listFDDFileModifiesSaisineRectificative;

	/**
	 * FEV560 : Liste des clé identifiants-des postes du DAN des tables des références
	 */
	private List<VocabularyEntry>								retourDANList;
	// private static Map<String,Object> retourDANList;
	
	
	/**
	 * FEV560 : id du poste du DAN sélectionné
	 */
	private String												retourDAN;
	
	@In(required = true, create = true)
	protected NuxeoPrincipal							currentUser;
	
	protected String									norDossierCopieFdr;

	/**
	 * Vrai si il manque des manque des métadonnées dans le bordereau et que l'on doit afficher en surbrillance les
	 * champs obligatoires du bordereau
	 */
	protected boolean									displayRequiredMetadonneBordereau;

	/**
	 * Get the current DossierSolonEpg.
	 * 
	 */
	@Factory(value = "dossier", scope = EVENT)
	public DocumentModel getDossierSolonEpg() {
		return navigationContext.getCurrentDocument();
	}

	/**
	 * Détermine si l'utilisateur a le droit de modifier un dossier distribué à l'état "Pour impression".
	 * 
	 * @return Condition
	 * @throws ClientException
	 */
	public boolean isEtapePourImpressionUpdater() throws ClientException {
		// Si l'utilisateur n'a pas chargé le DossierLink, ce test est non appliquable
		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		if (dossierLink == null) {
			return true;
		}

		// Traite uniquement l'étape "Pour impression"
		if (!VocabularyConstants.ROUTING_TASK_TYPE_IMPRESSION.equals(dossierLink.getRoutingTaskType())) {
			return true;
		}

		/*
		 * L'administrateur fonctionnel et ministériel peuvent modifier le dossier. Rq : pour l'admin. ministériel, si
		 * il a pu charger le DossierLink c'est qu'il est distribué dans son ministère.
		 */
		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_MIN_UPDATER)) {
			return true;
		}

		return false;
	}

	/**
	 * Retourne vrai si le dossier en cours est à l'état pour initialisation.
	 * 
	 * @return Vrai si le dossier en cours est à l'état pour initialisation
	 * @throws ClientException
	 */
	public boolean isCurrentDossierPourInitialisation() throws ClientException {
		final DocumentModel doc = getDossierSolonEpg();
		return doc.getCurrentLifeCycleState().equals("init");
	}

	/**
	 * Retourne le libellé des boutons d'action de la feuille de route Si un message spécifique au type d'étape existe,
	 * il est retourné, sinon le message correspondant à labelKey est retourné.
	 * 
	 * @param labelKey
	 *            Clé du libellé
	 * @return Libellé
	 * @throws ClientException
	 */
	public String getActionFeuilleRouteButtonLabel(final String labelKey) throws ClientException {
		final DossierLink currentDossierLink = corbeilleActions.getCurrentDossierLink();
		String routingTaskType = "";
		if (currentDossierLink != null) {
			routingTaskType = currentDossierLink.getRoutingTaskType();
		}

		return getActionFeuilleRouteButtonLabel(routingTaskType, labelKey);
	}

	/**
	 * Retourne le libellé des boutons d'action de la feuille de route Si un message spécifique au type d'étpae existe,
	 * il est retourné, sinon le message correspondant à labelKey est retourné.
	 * 
	 * @param routingTaskType
	 *            Type d'étape
	 * @param labelKey
	 *            Clé du libellé
	 * @return Libellé
	 * @throws ClientException
	 */
	public String getActionFeuilleRouteButtonLabel(final String routingTaskType, final String labelKey)
			throws ClientException {
		final String key = labelKey + "." + routingTaskType;
		final String label = resourcesAccessor.getMessages().get(key);
		if (!key.equals(label)) {
			return label;
		} else {
			return resourcesAccessor.getMessages().get(labelKey);
		}
	}

	public DossierLink getDossierLink() {
		return corbeilleActions.getCurrentDossierLink();
	}

	/**
	 * Valide l'étape correspondant au DossierLink en cours.
	 * 
	 * @return Vue de la liste des dossiers
	 * @throws ClientException
	 *             ClientException
	 */
	public String validerEtape() throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkDossierLink() || !checkCanunlockCurrentDossier()) {
			return null;
		}

		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		final DocumentModel dossierLinkDoc = dossierLink.getDocument();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

		// vérifie que le Dossier est complet
		if (!checkDossierValid(dossier, dossierLink)) {
			return null;
		}
		// vérifie la compatibilité des postes avec l'étape suivante
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
				dossier.getLastDocumentRoute(), stepDoc, null);
		if (!nextStepsDoc.isEmpty()) {
			String errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
					.isNextPostCompatibleWithNextStep(documentManager,
							nextStepsDoc.get(0).getAdapter(STRouteStep.class));
			if (StringUtil.isNotEmpty(errorCompatibility)) {
				facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
				return null;
			}
		}
		// vérifie la validité du type d'acte avec l'étape suivante
		if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())
				&& !SolonEpgServiceLocator.getFeuilleRouteService().isNextStepCompatibleWithActeTxtNonPub(
						documentManager, dossier.getLastDocumentRoute(), stepDoc)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.nextStep.typeActe"));
			return null;
		}

		// Vérifie la valeur de dos:publicationIntegraleOuExtrait
		if (getTypePublicationFromDossier(dossier) == null) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages()
							.get("feedback.solonepg.feuilleRoute.error.nextStep.typePublication"));
			return null;
		}

		// Change le statut en publié si l'étape en cours est "Pour publication à la DILA JO"
		final List<DocumentModel> runningSteps = feuilleRouteService.getRunningSteps(documentManager,
				dossier.getLastDocumentRoute());
		if (runningSteps != null && runningSteps.size() == 1) {
			STRouteStep routeStep = runningSteps.get(0).getAdapter(STRouteStep.class);
			if (routeStep.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO)
					&& !dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
				new UnrestrictedSessionRunner(documentManager) {
					@Override
					public void run() throws ClientException {
						DossierService dossierService = SolonEpgServiceLocator.getDossierService();
						dossierService.publierDossier(dossier, session);
						dossier.save(session);
					}
				}.runUnrestricted();
			}
		}

		// Valide l'étape avec avis favorable
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		dossierDistributionService.validerEtape(documentManager, dossierDoc, dossierLinkDoc);

		// Déverrouille automatiquement le dossier lors de l'action sur l'étape
		// TODO ajouter l'option silent à unlockCurrentDossier
		dossierLockActions.unlockCurrentDossier();
		FacesMessages.afterPhase();
		facesMessages.clear();

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.message.avisFavorable");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		return null;
	}

	/**
	 * Valide avec correction l'étape correspondant au DossierLink en cours.
	 * 
	 * @return Vue
	 * @throws ClientException
	 *             ClientException
	 */
	public String validerEtapeCorrection() throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkDossierLink() || !checkCanunlockCurrentDossier()) {
			return null;
		}

		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		final DocumentModel dossierLinkDoc = dossierLink.getDocument();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

		// vérifie la compatibilité des postes avec l'étapde suivante
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
				dossier.getLastDocumentRoute(), stepDoc, null);
		if (!nextStepsDoc.isEmpty()) {
			String errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
					.isNextPostCompatibleWithNextStep(documentManager,
							nextStepsDoc.get(0).getAdapter(STRouteStep.class));
			if (StringUtil.isNotEmpty(errorCompatibility)) {
				facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
				return null;
			}
		}
		// vérifie que le Dossier est complet
		if (!checkDossierValid(dossier, dossierLink)) {
			return null;
		}

		// vérifie la validité du type d'acte avec l'étape suivante
		if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())
				&& !SolonEpgServiceLocator.getFeuilleRouteService().isNextStepCompatibleWithActeTxtNonPub(
						documentManager, dossier.getLastDocumentRoute(), stepDoc)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.nextStep.typeActe"));
			return null;
		}

		// Valide l'étape avec correction
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		dossierDistributionService.validerEtapeCorrection(documentManager, dossierDoc, dossierLinkDoc);

		// Déverrouille automatiquement le dossier lors de l'action sur l'étape
		// TODO ajouter l'option silent à unlockCurrentDossier
		dossierLockActions.unlockCurrentDossier();
		FacesMessages.afterPhase();
		facesMessages.clear();

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get(
				"label.epg.feuilleRoute.message.avisFavorableCorrection");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		// réinitialise l'espace de traitement
		espaceTraitementActions.refreshPage();

		return null;
	}

	/**
	 * Valide avec refus l'étape correspondant au DossierLink en cours.
	 * 
	 * @return Vue
	 * @throws ClientException
	 *             ClientException
	 */
	public String validerEtapeRefus() throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkDossierLink() || !checkCanunlockCurrentDossier()) {
			return null;
		}

		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		final DocumentModel dossierLinkDoc = dossierLink.getDocument();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

		// vérifie que le Dossier est complet
		if (!checkDossierValid(dossier, dossierLink)) {
			return null;
		}

		// vérifie la compatibilité des postes avec l'étapde suivante
		// SSServiceLocator.getDocumentRoutingService().getDocumentRouteTree
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
				dossier.getLastDocumentRoute(), stepDoc, null);
		if (!nextStepsDoc.isEmpty()) {
			String errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
					.isNextPostCompatibleWithNextStep(documentManager,
							nextStepsDoc.get(0).getAdapter(STRouteStep.class));
			if (StringUtil.isNotEmpty(errorCompatibility)) {
				facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
				return null;
			}
		}
		// vérifie la validité du type d'acte avec l'étape suivante
		if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())
				&& !SolonEpgServiceLocator.getFeuilleRouteService().isNextStepCompatibleWithActeTxtNonPub(
						documentManager, dossier.getLastDocumentRoute(), stepDoc)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.nextStep.typeActe"));
			return null;
		}

		// Valide l'étape avec refus de validation
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		dossierDistributionService.validerEtapeRefus(documentManager, dossierDoc, dossierLinkDoc);

		// Déverrouille automatiquement le dossier lors de l'action sur l'étape
		// TODO ajouter l'option silent à unlockCurrentDossier
		dossierLockActions.unlockCurrentDossier();
		FacesMessages.afterPhase();
		facesMessages.clear();

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.message.avisDefavorable");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		// réinitialise l'espace de traitement
		espaceTraitementActions.refreshPage();

		return null;
	}

	/**
	 * Valide l'étape correspondant au DossierLink en cours avec la sortie "non concerné".
	 * 
	 * @param ajoutEtape
	 *            Ajout d'étape
	 * @return null
	 * @throws ClientException
	 */
	public String validerEtapeNonConcerne(final boolean ajoutEtape) throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkDossierLink() || !checkCanunlockCurrentDossier()) {
			return null;
		}

		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		final DocumentModel dossierLinkDoc = dossierLink.getDocument();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

		// vérifie que le Dossier est complet
		if (!checkDossierValid(dossier, dossierLink)) {
			return null;
		}

		// vérifie la compatibilité des postes avec l'étape suivante
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
				dossier.getLastDocumentRoute(), stepDoc, null);
		// error compatibility par défaut, aucune étape suivante, donc erreur
		String errorCompatibility = "Aucune étape à venir. Invalidation annulée.";
		if (!nextStepsDoc.isEmpty()) {
			// Le service renvoi une chaine vide si le poste suivant est compatible
			errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService().isNextPostCompatibleWithNextStep(
					documentManager, nextStepsDoc.get(0).getAdapter(STRouteStep.class));
		}

		if (StringUtil.isNotEmpty(errorCompatibility)) {
			facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
			return null;
		}

		// vérifie la validité du type d'acte avec l'étape suivante
		if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())
				&& !SolonEpgServiceLocator.getFeuilleRouteService().isNextStepCompatibleWithActeTxtNonPub(
						documentManager, dossier.getLastDocumentRoute(), stepDoc)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.nextStep.typeActe"));
			return null;
		}

		// Valide l'étape avec "non concerné"
		try {
			final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
					.getDossierDistributionService();
			if (ajoutEtape) {
				dossierDistributionService.validerEtapeNonConcerne(documentManager, dossierDoc, dossierLinkDoc);
			} else {
				dossierDistributionService.validerEtapeNonConcerneAjoutEtape(documentManager, dossierDoc,
						dossierLinkDoc);
			}
		} catch (final LocalizedClientException e) {
			// Affiche un message d'erreur
			final String message = resourcesAccessor.getMessages().get(e.getMessage());
			facesMessages.add(StatusMessage.Severity.WARN, message);

			return null;
		}

		// Déverrouille automatiquement le dossier lors de l'action sur l'étape
		// TODO ajouter l'option silent à unlockCurrentDossier
		dossierLockActions.unlockCurrentDossier();
		FacesMessages.afterPhase();
		facesMessages.clear();

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.message.nonConcerne");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		return null;
	}

	/**
	 * Valide l'étape correspondant au DossierLink en cours avec la sortie "retour pour modification".
	 * 
	 * @param ajoutEtape
	 *            Ajout d'étape
	 * @return null
	 * @throws ClientException
	 */
	public String validerEtapeRetourModification(final boolean ajoutEtape) throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkDossierLink() || !checkCanunlockCurrentDossier()) {
			return null;
		}

		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		final DocumentModel dossierLinkDoc = dossierLink.getDocument();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

		// vérifie que le Dossier est complet
		if (!checkDossierValid(dossier, dossierLink)) {
			return null;
		}

		// vérifie la compatibilité des postes avec l'étapde suivante
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
		final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
		final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
				dossier.getLastDocumentRoute(), stepDoc, null);
		if (!nextStepsDoc.isEmpty()) {
			String errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
					.isNextPostCompatibleWithNextStep(documentManager,
							nextStepsDoc.get(0).getAdapter(STRouteStep.class));
			if (StringUtil.isNotEmpty(errorCompatibility)) {
				facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
				return null;
			}
		}
		// Valide l'étape avec "retour pour modification"
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		if (ajoutEtape) {
			try {
				dossierDistributionService.validerEtapeRetourModificationAjoutEtape(documentManager, dossierDoc,
						dossierLinkDoc, retourDAN);
			} catch (final LocalizedClientException e) {
				// Propose à l'utilisateur de faire un retour avec modification sans ajout d'étape
				retourModificationPopup = true;

				return null;
			}
		} else {
			try {
				dossierDistributionService.validerEtapeRetourModification(documentManager, dossierDoc, dossierLinkDoc);
			} catch (final LocalizedClientException e) {
				// Affiche un message d'erreur
				final String message = resourcesAccessor.getMessages().get(e.getMessage());
				facesMessages.add(StatusMessage.Severity.WARN, message);

				return null;
			}
		}

		// Déverrouille automatiquement le dossier lors de l'action sur l'étape
		// TODO ajouter l'option silent à unlockCurrentDossier
		dossierLockActions.unlockCurrentDossier();
		FacesMessages.afterPhase();
		facesMessages.clear();

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.message.retourModification");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		return null;
	}

	/**
	 * Substitution de la feuille de route en cours : - Annule la feuille de route ; - Démarre une nouvelle feuille de
	 * route.
	 * 
	 * @return Vue de la liste des dossiers
	 * @throws ClientException
	 */
	public String substituerRoute() throws ClientException {
		// Récupère la nouvelle feuille de route sélectionnée par l'utilisateur
		final String routeId = routingActions.getRelatedRouteModelDocumentId();
		DocumentModel newRouteDoc = null;
		if (routeId != null && !"".equals(routeId)) {
			newRouteDoc = documentManager.getDocument(new IdRef(routeId));
		}

		if (newRouteDoc == null) {
			// Aucun modèle n'est sélectionné
			final String message = resourcesAccessor.getMessages().get(
					"feedback.reponses.document.route.no.valid.route");
			facesMessages.add(StatusMessage.Severity.WARN, message);

			return null;
		}

		// vérifie la compatibilité des postes avec l'étapde suivante
		DocumentModel initStep = feuilleRouteService.getEtapePourInitialisation(documentManager,
				newRouteDoc.getAdapter(DocumentRoute.class));
		List<DocumentModel> steps = feuilleRouteService.findNextSteps(documentManager, routeId, initStep, null);
		String errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
				.isNextPostsCompatibleWithNextStep(documentManager, steps);
		if (StringUtil.isNotEmpty(errorCompatibility)) {
			facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
			return null;
		}

		// Récupère l'ancienne instance de feuille de route
		final STFeuilleRoute oldRoute = routingActions.getRelatedRoute();
		final DocumentModel oldRouteDoc = oldRoute.getDocument();

		// Substitue la feuille de route
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		final DocumentModel newFeuilleRoute = dossierDistributionService.substituerFeuilleRoute(documentManager,
				navigationContext.getCurrentDocument(), oldRouteDoc, newRouteDoc,
				STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_SUBSTITUTION);
		// reverrouille la FDR si le dossier est verrouillé
		if (stLockActions.isCurrentDocumentLocked()) {
			try {
				final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();
				documentRoutingService.lockDocumentRoute(newFeuilleRoute.getAdapter(STFeuilleRoute.class),
						documentManager);
			} catch (final DocumentRouteAlredayLockedException e) {
				// deja verroullier, on ignore
			}

		}
		// Réinitialise la sélection de la feuille de route
		routingActions.resetRelatedRouteDocumentId();

		// envoi message information utilisateur
		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("st.dossierDistribution.action.substituer.success"));

		// Recharge le dossierLink
		final String identifier = navigationContext.getCurrentDocument().getId();
		// Décharge le dossier
		navigationContext.resetCurrentDocument();
		// Décharge le DossierLink et rafraichit la corbeille
		corbeilleActions.resetCurrentDossier();

		// recharge le dossier
		dossierListingActions.navigateToDossier(documentManager.getDocument(new IdRef(identifier)));

		if (corbeilleActions.getCurrentDossierLink() == null) {
			// le dossier link est plus chez nous lisible
			// Décharge le dossier
			navigationContext.resetCurrentDocument();
			corbeilleActions.resetCurrentDossier();

			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("solonepg.dossierLink.notreadable"));
		} else {
			// on remet sur l'onglet feuille de route
			webActions.setCurrentTabId(SolonEpgActionConstant.TAB_CASE_MANAGEMENT_VIEW_RELATED_ROUTE);
		}

		return routingWebActions.getFeuilleRouteView();
	}

	/**
	 * Lance un dossier. TODO faire une méthode basée sur le dossier + dossierlink courant.
	 * 
	 * @param dossierDoc
	 *            Dossier à lancer
	 * @param currentDossierLink
	 *            DossierLink du dossier à lancer
	 * @param forceLoaded
	 *            ???
	 * @return Vue
	 * @throws ClientException
	 */
	public String lancerDossier(final DocumentModel dossierDoc, final DossierLink currentDossierLink,
			final boolean forceLoaded) throws ClientException {
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

		if (forceLoaded && !corbeilleActions.isDossierLoadedInCorbeille()) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.message.dossierLance.error"));
			return STViewConstant.ERROR_VIEW;
		}

		if (!checkDossierValid(dossier, currentDossierLink)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.message.dossierLance.error"));
			return STViewConstant.ERROR_VIEW;
		}

		final DocumentModel dossierLinkDoc = currentDossierLink.getDocument();

		// Déverrouille le dossier si il est verrouillé
		if (dossierLockActions.getCanUnlockCurrentDossier()) {
			dossierLockActions.unlockCurrentDossier();
		}

		// Lance le dossier
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();

		dossierDistributionService.lancerDossier(documentManager, dossierDoc, dossierLinkDoc);

		// reset de la contentview de l'espace de création
		contentViewActions.reset(SolonEpgContentViewConstant.ESPACE_CREATION_DOSSIERS_CONTENT_VIEW);

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.message.dossierLance");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		// Retour à l'espace de création
		return espaceCreationActions.navigateTo();
	}

	/**
	 * Appelée lorsqu'on affiche un dossier
	 * 
	 * @return boolean FALSE si l'étape suivante est de type "Pour publication DILA JO" et qu'il existe une publication
	 *         conjointe / TRUE sinon
	 * @throws ClientException
	 */
	public boolean checkPublicationConjointeAvantDILAJO() throws ClientException {
		// FEV 391_5: sur une publication conjointe, en "Pour publication DILA JO"
		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		if (dossierLink != null && stLockActions.isCurrentDocumentLocked()) { // On ne check que les documents
																				// verrouillés pour optimiser

			final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

			List<String> pubsConjointes = dossier.getPublicationsConjointes();
			if (pubsConjointes != null && pubsConjointes.size() > 0) {

				final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();
				final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
						dossier.getLastDocumentRoute(), stepDoc, null);
				for (final DocumentModel nextStepDoc : nextStepsDoc) {
					final SolonEpgRouteStep nextStep = nextStepDoc.getAdapter(SolonEpgRouteStep.class);
					if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(nextStep.getType())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * FEV507 : Contrôle des avis des chargés de mission avant envoi en publication. Cette méthode est appelée à chaque
	 * action sur le dossier.
	 * 
	 * @return false si une confirmation doit être demandée côté client au changement d'étape true dans tout autre cas
	 * @throws ClientException
	 */
	public boolean checkAvisChargesDeMissionAvantDILAJO() throws ClientException {
		final DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		// On ne checke que les documents verrouillés pour optimiser
		if (dossierLink != null && stLockActions.isCurrentDocumentLocked()) {
			final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

			final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
					dossier.getLastDocumentRoute(), stepDoc, null);

			// Si on est sur la dernière étape on retourne true
			if (nextStepsDoc == null || nextStepsDoc.isEmpty()) {
				return true;
			}
			// Si la prochaine étape est Publication DILA JO on retourne true
			if (nextStepsDoc.size() >= 1) {
				final SolonEpgRouteStep nextStep = nextStepsDoc.get(0).getAdapter(SolonEpgRouteStep.class);
				if (!VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(nextStep.getType())) {
					return true;
				}
			}

			// Récupération des étapes conidérées pour le controle
			final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
					.getDossierDistributionService();
			Collection<DocumentModel> controledSteps = dossierDistributionService.findAvisChargesDeMissionSteps(
					documentManager, stepDoc);
			if (controledSteps == null || controledSteps.isEmpty()) {
				// Pas d'étape à vérifier
				return true;
			}

			// Une de ces étapes est-elle invalide ?
			for (DocumentModel controledStep : controledSteps) {
				SolonEpgRouteStep etape = controledStep.getAdapter(SolonEpgRouteStep.class);

				if (etape.isDone()
						&& (!(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE
								.equals(etape.getValidationStatus()) || STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE
								.equals(etape.getValidationStatus())))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * On vérifie que le dossier est valide : la parapheur doit être complet et les méta-données obligatoires du
	 * bordereaux doivent être remplies.
	 * 
	 * @return vrai si le dossier est valide : la parapheur doit être complet et les méta-données obligatoires du
	 *         bordereaux doivent être remplies.
	 * @throws ClientException
	 */
	protected boolean checkDossierValid(final Dossier dossier, final DossierLink dossierLink) throws ClientException {
		final FeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getFeuilleRouteService();

		// Vérification du parapheur
		if (!dossier.getIsParapheurComplet()) {

			String message;
			if (dossier.getIsActeReferenceForNumeroVersion()) {
				// la pièce manquante est un acte
				message = resourcesAccessor.getMessages().get("feedback.solonepg.document.parapheur.acte.no.existing");
			} else {
				// la pièce manquante est un extrait
				message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.document.parapheur.extrait.no.existing");
			}

			// on envoie un message pour indiquer à l'utilisateur que des fichiers doivent être présent dans le
			// parapheur
			facesMessages.add(StatusMessage.Severity.WARN, message);

			// On reste dans l'onglet de feuille de route
			return false;
		}

		// Vérification des méta-données obligatoires
		if (!bordereauActions.isBordereauComplet(dossier.getDocument())) {
			StringBuilder message = new StringBuilder(resourcesAccessor.getMessages().get(
					"feedback.solonepg.document.bordereau.noncomplet"));
			// on récupère la liste des métadonnées obligatoires non remplies
			message.append(bordereauActions.getBordereauMetadonnesObligatoiresManquantes(dossier.getDocument()));
			facesMessages.add(StatusMessage.Severity.WARN, message.toString());

			// on signale que l'on doit mettre en surbrillance les champs du bordereau
			this.displayRequiredMetadonneBordereau = true;

			// On reste dans l'onglet courant
			return false;
		}

		// on vérifie qu'il y a une prochaine étape
		if (!feuilleRouteService.hasMoreThanOneStep(documentManager, dossier.getLastDocumentRoute())) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.NoNextStep"));
			return false;
		}

		// Vérifications supplémentaires si l'étape suivante est "Pour publication DILA JO"
		final DocumentModel stepDoc = documentManager.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

		final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(documentManager,
				dossier.getLastDocumentRoute(), stepDoc, null);
		boolean nextStepIsValid = true;
		for (final DocumentModel nextStepDoc : nextStepsDoc) {
			final SolonEpgRouteStep nextStep = nextStepDoc.getAdapter(SolonEpgRouteStep.class);

			// Vérification que le mode de parution n'est pas vide si on la prochaine étape est ou contient une étape de
			// publication
			nextStepIsValid &= checkModeParution(dossier, nextStep);

			if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(nextStep.getType())) {
				nextStepIsValid &= checkStepPourPublicationDilaJO(dossier);
				nextStepIsValid &= checkDateEffetTexteEntreprise(dossier);
			} else if (VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(nextStep.getType())) {
				nextStepIsValid &= checkStepPourFournitureEpreuve(dossier);
			} else if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(nextStep.getType())
					|| VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO.equals(nextStep.getType())) {
				nextStepIsValid &= checkStepPourPublication(dossier);
				nextStepIsValid &= checkBulletinOfficiel(dossier);
				nextStepIsValid &= checkDateEffetTexteEntreprise(dossier);
			}

			if (!nextStepIsValid) {
				break;
			}
		}

		return nextStepIsValid;
	}

	private boolean checkStepPourFournitureEpreuve(Dossier dossier) {
		if (dossier.getDatePourFournitureEpreuve() == null
				|| !(dossier.getDatePourFournitureEpreuve().after(Calendar.getInstance()) || DateUtil.isSameDay(
						dossier.getDatePourFournitureEpreuve(), Calendar.getInstance()))) {

			facesMessages
					.add(StatusMessage.Severity.WARN,
							resourcesAccessor.getMessages().get(
									"feedback.solonepg.feuilleRoute.error.nextStep.dateFourniture"));
			return false;
		}

		return true;
	}

	private boolean checkStepPourPublication(Dossier dossier) throws ClientException {
		if (!checkDelaiPublication(dossier)) {
			return false;
		}

		if (!checkPublicationIntegrale(dossier)) {
			return false;
		}

		if (!checkDateSignature(dossier)) {
			return false;
		}

		return true;
	}

	/**
	 * Si on est dans l'étape de publication (dila_jo ou dila_bo ou bo), on ne peut pas la valider tant que le mode de
	 * parution est vide
	 * 
	 * @param dossier
	 * @param curStep
	 * @return faut si on est dans l'étape de contrôle et que le mode de parution est vide, vrai sinon
	 */
	private boolean checkModeParution(Dossier dossier, SolonEpgRouteStep curStep) {
		if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(curStep.getType())
				|| VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(curStep.getType())
				|| VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO.equals(curStep.getType())) {
			RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
			if (StringUtils.isBlank(retourDila.getModeParution())) {
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.feuilleRoute.error.curStep.modeParution");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return false;
			}
		}

		return true;
	}

	/**
	 * Renvoie les identifiants des vecteurs/bulletins officiels d'un dossier. Cela permet de filtrer car dans certains
	 * cas, on veut faire des contrôle uniquement sur l'un ou l'autre des ensembles. Concernant les identifiants, pour
	 * les vecteurs on a un UID, et pour les bulletins un libellé.
	 * 
	 * @param dossier
	 * @param vecteur
	 *            TRUE=indique que l'on renvoie uniquement les vecteurs / FALSE=uniquement les bulletins
	 * @return
	 */
	private List<String> getVecteurPublicationOuBulletinOfficielDossier(Dossier dossier, boolean vecteur) {
		List<VecteurPublicationDTO> dtos = bordereauActions.getVecteurPublicationDossier(dossier.getDocument());
		List<String> elements = new ArrayList<String>();
		for (VecteurPublicationDTO dto : dtos) {
			// Si id = intitule, il s'agit d'un bulletin officiel ! C'est crade, mais le modèle ne distingue pas
			// clairement les 2
			// notions. Donc cette complexité gratuite en découle,
			boolean isIntitule = dto.getId().equals(dto.getIntitule());
			if ((vecteur && !isIntitule) || (!vecteur && isIntitule)) {
				elements.add(dto.getId());
			}
		}
		return elements;
	}

	private boolean checkBulletinOfficiel(Dossier dossier) throws ClientException {
		final BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();

		// Si on n'a pas de bulletins officiels ou bien que certains sont inactifs on lance une alerte
		List<String> bulletins = getVecteurPublicationOuBulletinOfficielDossier(dossier, false);
		if (bulletins == null || bulletins.isEmpty()
				|| !bulletinService.isAllBulletinOfficielActif(documentManager, bulletins)) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.feuilleRoute.error.nextStep.vecteur");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}
		return true;
	}

	private boolean checkVecteurPublication(Dossier dossier) throws ClientException {
		final BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();

		// Si on n'a pas de vecteurs de publication ou bien que certains sont inactifs on lance une alerte
		List<String> vecteurs = getVecteurPublicationOuBulletinOfficielDossier(dossier, true);
		if (vecteurs == null || vecteurs.isEmpty()
				|| !bulletinService.isAllVecteurPublicationActif(documentManager, vecteurs)) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.feuilleRoute.error.nextStep.vecteur");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}
		return true;
	}

	private boolean checkDelaiPublication(Dossier dossier) {
		// Contrôle du délai de publication
		if (StringUtils.isBlank(dossier.getDelaiPublication())) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.feuilleRoute.error.nextStep.delaipublication");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		} else {
			if (dossier.getDelaiPublication().equals(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE)
					&& dossier.getDatePreciseePublication().before(Calendar.getInstance())) {

				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.feuilleRoute.error.nextStep.datepublication");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return false;
			}
		}

		return true;
	}

	private boolean checkDelaiPublicationDilaJO(Dossier dossier) {
		// Contrôle du délai de publication
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);

		if (StringUtils.isBlank(dossier.getDelaiPublication())) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.feuilleRoute.error.nextStep.delaipublication");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		} else {
			if (dossier.getDelaiPublication().equals(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE)
					&& dossier.getDatePreciseePublication().before(yesterday)) {

				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.feuilleRoute.error.nextStep.datepublication");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return false;
			}
		}

		return true;
	}

	private boolean checkPublicationIntegrale(Dossier dossier) {

		if (StringUtils.isBlank(dossier.getPublicationIntegraleOuExtrait())) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.feuilleRoute.error.nextStep.publicationIntegr");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		return true;
	}

	private boolean checkDateSignature(Dossier dossier) throws ClientException {
		final TableReferenceService tabRefService = SolonEpgServiceLocator.getTableReferenceService();
		final VocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();

		// Vérification de la date de signature pour le type d'acte
		if (dossier.getDateSignature() == null && StringUtils.isNotBlank(dossier.getTypeActe())) {
			TableReference tabRef = tabRefService.getTableReference(documentManager).getAdapter(TableReference.class);
			String libelleTypeActe = vocService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY,
					dossier.getTypeActe());
			if (StringUtils.isNotBlank(libelleTypeActe) && tabRef.getTypesActe().contains(libelleTypeActe)) {
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.feuilleRoute.error.nextStep.typeActe");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return false;
			}
		}

		return true;
	}

	private boolean checkStepPourPublicationDilaJO(Dossier dossier) throws ClientException {
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		final ActeService acteService = SolonEpgServiceLocator.getActeService();

		if (acteService.isActeTexteNonPublie(dossier.getTypeActe())) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.nextStep.typeActe"));
			return false;
		}
		if (parapheurService.getPieceParapheur(documentManager, dossier, false).size() == 0) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.dossier.publication.parapheur.error");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		if (dossier.getPublicationRapportPresentation()
				&& fondDeDossierService.getRapportPresentationFiles(documentManager, dossier).size() == 0) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.dossier.publication.fonddossier.error");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}
		if (dossier.isMesureNominative()) {
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.dossier.publication.confidentialite.error");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		// FEV 500: on accepte aussi la date du jour pour DILA JO
		if (!checkDelaiPublicationDilaJO(dossier)) {
			return false;
		}

		// Contrôle commun aux publication dila ou non dans les BO
		if (!checkPublicationIntegrale(dossier)) {
			return false;
		}
		if (!checkDateSignature(dossier)) {
			return false;
		}
		if (!checkVecteurPublication(dossier)) {
			return false;
		}
		return true;
	}

	/**
	 * Réinitialise le champ qui indique si l'on doit mettre en surbrillance les champs obligatoire du bordereau
	 * 
	 * @throws ClientException
	 */
	@Observer(DossierSolonEpgConstants.DISPLAY_REQUIRED_METADONNEE_BORDEREAU_EVENT)
	public void resetDisplayRequiredMetadonneBordereau() {
		this.displayRequiredMetadonneBordereau = false;
	}

	/**
	 * Lance un dossier.
	 * 
	 * @param dossierDoc
	 *            Dossier
	 * @return Vue
	 * @throws ClientException
	 */
	public String lancerDossier(final DocumentModel dossierDoc) throws ClientException {

		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkDossierLink() || !checkCanunlockCurrentDossier()) {
			return null;
		}

		final DossierLink currentDossierLink = corbeilleActions.getCurrentDossierLink();
		return lancerDossier(dossierDoc, currentDossierLink, true);

	}

	public String norAttribue(final DocumentModel dossierDoc) throws ClientException {

		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

		if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())) {
			// Affiche un message de non possibilité d'action
			final String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.action.impossible.norAttribue");
			facesMessages.add(StatusMessage.Severity.WARN, MessageFormat.format(message, dossier.getNumeroNor()));
			return null;
		}

		// Mantis #38727
		if (StringUtils.isBlank(dossier.getTitreActe())) {
			final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
			final StringBuilder builder = new StringBuilder(resourcesAccessor.getMessages().get(
					"feedback.solonepg.document.bordereau.noncomplet"));
			builder.append(vocService.getLabelFromId(STVocabularyConstants.BORDEREAU_LABEL,
					DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY, STVocabularyConstants.COLUMN_LABEL));

			facesMessages.add(StatusMessage.Severity.WARN, builder.toString());
			return null;
		}

		// Déverrouille le dossier si il est verrouillé
		if (dossierLockActions.getCanUnlockCurrentDossier()) {
			dossierLockActions.unlockCurrentDossier();
		}

		// Passe le dossier à l'état "NOR attribué"
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		dossierDistributionService.norAttribue(documentManager, dossierDoc);

		// Affiche un message de confirmation
		final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.message.norAttribue");
		facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

		// on signale que le dossier a changé : met à jour les informations du dossier dans la liste de sélection
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		return null;
	}

	/**
	 * Redémarre un dossier dont la feuille de route a été terminée.
	 * 
	 * @return Vue
	 * @throws ClientException
	 *             ClientException
	 */
	public String redemarrerDossier() throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkCanunlockCurrentDossier()) {
			return null;
		}

		// Redémarre le dossier
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		dossierDistributionService.restartDossier(documentManager, dossierDoc);

		// Déverrouille automatiquement le dossier lors de l'action sur l'étape
		// TODO ajouter l'option silent à unlockCurrentDossier
		dossierLockActions.unlockCurrentDossier();
		FacesMessages.afterPhase();
		facesMessages.clear();

		// Affiche un message d'information
		final String message = resourcesAccessor.getMessages().get("epg.distribution.action.restart.success");
		facesMessages.add(StatusMessage.Severity.INFO, message);

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		return null;
	}

	/**
	 * Redémarre un dossier abandonne
	 * 
	 * @return Vue
	 * @throws ClientException
	 *             ClientException
	 */
	public String relancerDossierAbandonne() throws ClientException {
		// Déverrouille le dossier si il est verrouillé
		if (dossierLockActions.getCanUnlockCurrentDossier()) {
			dossierLockActions.unlockCurrentDossier();
		}

		// Redémarre le dossier
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		dossierService.restartAbandonDossier(documentManager, dossierDoc);

		// Affiche un message d'informatresourcesAccessorion
		final String message = resourcesAccessor.getMessages().get("epg.distribution.action.restart.success");
		facesMessages.add(StatusMessage.Severity.INFO, message);

		// Décharge le dossier courant
		corbeilleActions.resetCurrentDossier();

		return null;
	}

	/**
	 * reset l'id utilisé pour la selection de la feuille de route
	 * 
	 * @return la vue pour selectionner la feuille de route
	 */
	public String navigateToFdrModelSelection() {
		routingActions.resetRelatedRouteDocumentId();
		return "view_modeles_feuille_route_for_selection";
	}

	/**
	 * Vérifie la présence d'un dossier et d'un DossierLink correspondant en session (blindage).
	 * 
	 * @return Dossier déverrouillé
	 * @throws ClientException
	 */
	protected boolean checkDossierLink() throws ClientException {
		final DossierLink currentDossierLink = corbeilleActions.getCurrentDossierLink();
		final DocumentModel currentDossier = navigationContext.getCurrentDocument();
		if (navigationContext.getCurrentDocument() == null || currentDossierLink == null
				|| !currentDossierLink.getDossierId().equals(currentDossier.getId())) {
			log.warn("Appel à une action de la feuille de route alors que le dossier n'est pas correctement chargé");
			return false;
		}

		return true;
	}

	/**
	 * Vérifie si le dossier est toujours verrouillé et peut être déverrouillé par l'utilisateur courant.
	 * 
	 * @return Dossier déverrouillé
	 * @throws ClientException
	 */
	protected boolean checkCanunlockCurrentDossier() throws ClientException {
		if (stLockActions.isCurrentDocumentLocked() && !dossierLockActions.getCanUnlockCurrentDossier()) {
			// Le dossier a été verrouillé entre temps par un autre utilisateur
			final String message = resourcesAccessor.getMessages().get("epg.dossier.lock.alreadyLocked.error");
			facesMessages.add(StatusMessage.Severity.WARN, message);

			return false;
		}

		return true;
	}

	/**
	 * Setter de retourModificationPopup.
	 * 
	 * @param retourModificationPopup
	 *            retourModificationPopup
	 */
	public void setRetourModificationPopup(final Boolean retourModificationPopup) {
		this.retourModificationPopup = retourModificationPopup;
	}

	public boolean getIsDisplayRequiredMetadonneBordereau() {
		return displayRequiredMetadonneBordereau;
	}

	public boolean isDisplayRequiredMetadonneBordereau() {
		return displayRequiredMetadonneBordereau;
	}

	public void setDisplayRequiredMetadonneBordereau(final boolean displayRequiredMetadonneBordereau) {
		this.displayRequiredMetadonneBordereau = displayRequiredMetadonneBordereau;
	}

	private static PublicationIntOuExtType getTypePublicationFromDossier(Dossier dossier) {
		String publicationIntegraleOuExtrait = dossier.getPublicationIntegraleOuExtrait();
		if (publicationIntegraleOuExtrait.equals(VocabularyConstants.TYPE_PUBLICATION_IN_EXTENSO)) {
			return PublicationIntOuExtType.IN_EXTENSO;
		} else if (publicationIntegraleOuExtrait.equals(VocabularyConstants.TYPE_PUBLICATION_EXTRAIT)) {
			return PublicationIntOuExtType.EXTRAIT;
		}
		return null;
	}

	/**
	 * Action de saisine rectificative Ouvre un nouvel écran pour que l'utilisateur puisse confirmer l'action
	 * 
	 * @return
	 */
	public String prepareSaisineRectificative() throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkCanunlockCurrentDossier()) {
			return null;
		}

		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {
				runPrepareEnvoi(session);
			}
		}.runUnrestricted();
		return "prepare_saisine_rectificative";
	}

	/**
	 * Effectue la saisine rectificative - avec la création d'un jeton pour chaque fichier envoyé
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String doSaisineRectificative() throws ClientException {
		final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		fddService.sendSaisineRectificative(documentManager, navigationContext.getCurrentDocument(), 
				listFDDFileModifiesSaisineRectificative, "Saisine rectificative pour les fichiers");

		clearFilesListsCE();
		// On revient au dossier là où on était avant
		return routingWebActions.getFeuilleRouteView();
	}

	public List<FondDeDossierFile> getListDocModifiesFDD() {
		return this.listFDDFileModifiesFDD;
	}

	public List<FondDeDossierFile> getListDocModifiesSaisineRectificative() {
		return this.listFDDFileModifiesSaisineRectificative;
	}

	/**
	 * Méthode qui renvoi la date de saisine rectificative la plus récente. Si date de saisine rectificative non
	 * trouvée, renvoi la date de début de l'étape Pour avis CE
	 * 
	 * @param dossier
	 *            : Dossier concerné par la saisine rectificative
	 * @return
	 */
	public Calendar getDateDerniereSaisineRectificative(CoreSession session, Dossier dossier) {
		FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		Calendar dateRetour = null;
		// Requête en base pour retrouver tous les jetons pour ce nor et pour ce type de WS
		// (rechercherModificationDossier) et pour ce type de modification (SaisineRectificative)
		List<DocumentModel> jetonListDoc = new ArrayList<DocumentModel>();
		jetonListDoc = fddService.getListSaisineRectificativeOuListTransmissionPiecesComplementaireForDossier(dossier,
				session, TypeModification.SAISINE_RECTIFICATIVE.name());
		// On renseigne par défaut la date du début de l'étape pour avis CE
		Calendar dateTemporaire = fddService.getStartStepPourAvisCE(dossier.getDocument(), session);
		if (jetonListDoc != null && !jetonListDoc.isEmpty()) {
			// On a trouvé des jetons. On va parcourir les jetons pour récupérer la date de la saisine rectificative la
			// plus récente
			for (DocumentModel unJetonDonc : jetonListDoc) {
				JetonDoc unJeton = unJetonDonc.getAdapter(JetonDoc.class);
				// On souhaite récupérer la date du jeton
				if (dateTemporaire == null || dateTemporaire.compareTo(unJeton.getCreated()) < 0) {
					dateTemporaire = unJeton.getCreated();
				}
			}
		}
		dateRetour = dateTemporaire;
		return dateRetour;
	}

	/**
	 * Méthode qui renvoi la date de transmission des pièces complémentaires la plus récente. Si date de transmission de
	 * pièces complémentaires non trouvée, renvoi la date de début de l'étape Pour avis CE
	 * 
	 * @param dossier
	 *            : Dossier concerné par étape pour avis CE
	 * @return
	 */
	public Calendar getDateDerniereTransmissionPiecesComplementaires(CoreSession session, Dossier dossier) {
		FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		Calendar dateRetour = null;
		// Requête en base pour retrouver tous les jetons pour ce nor et pour ce type de WS
		// (rechercherModificationDossier) et pour ce type de modification (Envoi de pièces complémentaires)
		List<DocumentModel> jetonListDoc = new ArrayList<DocumentModel>();
		// jetonListDoc = fddService.getListEnvoiPiecesComplementairesForDossier(dossier, documentManager);
		jetonListDoc = fddService.getListSaisineRectificativeOuListTransmissionPiecesComplementaireForDossier(dossier,
				session, TypeModification.PIECE_COMPLEMENTAIRE.name());
		// On renseigne par défaut la date du début de l'étape pour avis CE
		Calendar dateTemporaire = fddService.getStartStepPourAvisCE(dossier.getDocument(), session);
		if (jetonListDoc != null && !jetonListDoc.isEmpty()) {
			// On a trouvé des jetons. On va parcourir les jetons pour récupérer la date de la saisine rectificative la
			// plus récente
			for (DocumentModel unJetonDonc : jetonListDoc) {
				JetonDoc unJeton = unJetonDonc.getAdapter(JetonDoc.class);
				// On souhaite récupérer la date du jeton
				if (dateTemporaire == null || dateTemporaire.compareTo(unJeton.getCreated()) < 0) {
					dateTemporaire = unJeton.getCreated();
				}
			}
		}
		dateRetour = dateTemporaire;
		return dateRetour;
	}

	public Boolean hasDocumentATransmettreSaisineRectificative() {
		if (listFDDFileModifiesSaisineRectificative.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public Boolean hasDocumentATransmettrePieceComplementaire() {
		if (listFDDFileModifiesFDD.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Action d'envoi de pièces complémentaire - Accès à l'écran qui permet d'afficher la liste des pièces à envoyer
	 * 
	 * @return
	 */
	public String prepareEnvoiPieceComplementaire() throws ClientException {
		// Vérifie si le dossier est toujours verrouillé par l'utilisateur
		if (!checkCanunlockCurrentDossier()) {
			return null;
		}

		
		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {
				runPrepareEnvoi(session);
			}
		}.runUnrestricted();
		return "prepare_envoi_piece_complementaire";

	}
	
	private void runPrepareEnvoi(final CoreSession session) throws ClientException {
		// Récupération des fichiers modifiés pour l'affichage
		FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		final DocumentModel currentDossier = navigationContext.getCurrentDocument();
		Dossier dossier = currentDossier.getAdapter(Dossier.class);
		// On va récupérer la date de la dernière saisine rectificative pour ce dossier		
		Calendar dateDerniereSaisineRectificative = getDateDerniereSaisineRectificative(session, dossier);
		// On va récupérer la date de la dernière transmission de pièces complémentaires pour ce dossier
		Calendar dateDernieretransmissionPiecesComplementaires = getDateDerniereTransmissionPiecesComplementaires(session, dossier);
		// On récupère les documents modifiés dans le répertoire saisine rectificative
		listFDDFileModifiesSaisineRectificative = fddService.getAllUpdatedFilesFDDInSaisine(
				session, dossier, dateDerniereSaisineRectificative);
		// Une fois qu'on a ça on va récupérer les documents qui ont été modifiés après cette date
		// On récupère les documents modifiés pour les répertoires autre que saisine rectificative
		listFDDFileModifiesFDD = fddService.getAllUpdatedFilesFDDNotSaisine(
				session, dossier, dateDernieretransmissionPiecesComplementaires);
	}

	/**
	 * Effectue l'envoi de pièce complémentaire - avec la création d'un jeton pour chaque pièce complémentaire envoyée
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String doEnvoiPieceComplementaire() throws ClientException {
		final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		fddService.sendPiecesComplementaires(documentManager, navigationContext.getCurrentDocument(), 
				listFDDFileModifiesFDD, 
				resourcesAccessor.getMessages().get("epg.documents.piece.complementaires.envoi"));

		clearFilesListsCE();
		// On revient au dossier là où on était avant
		return routingWebActions.getFeuilleRouteView();
	}
	
	private void clearFilesListsCE() {
		// On remet les listes à vide
		if (listFDDFileModifiesFDD != null) {
			listFDDFileModifiesFDD.clear();
		}
		if (listFDDFileModifiesSaisineRectificative != null) {
			listFDDFileModifiesSaisineRectificative.clear();
		}
	}

	/**
	 * Retourne vrai si l'utilisateur a les droits pour afficher les boutons saisine rectificative et envoi de pièces
	 * complémentaires
	 */
	public boolean canSendSaisineOrPieceComplementaire() {
		Boolean isLocked = Boolean.TRUE;
		try {
			isLocked = stLockActions.isCurrentDocumentLockedByCurrentUser();
		} catch (final ClientException exc) {
			log.error("Le verrou sur le dossier n'a pas pu être récupéré", exc);
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("epg.bordereau.error.lock"));
		}
		return currentUser
				.isMemberOf(SolonEpgBaseFunctionConstant.CONSEIL_ETAT_ENVOI_SAISINE_PIECE_COMPLEMENTAIRE_EXECUTOR)
				&& isLocked;
	}

	/**
	 * Retourne vrai si texte pas de type entreprise ou si texte de type entreprise et a au moins une date d'effet
	 * postérieure à la date du jour
	 * 
	 * @param dossier
	 * @return Boolean
	 */
	private Boolean checkDateEffetTexteEntreprise(Dossier dossier) {
		if (!dossier.getTexteEntreprise()) {
			return true;
		} else {
			// Si le dossier est un texte entreprise on doit vérifier qu'on a au moins une date d'effet qui est
			// postérieure à la date du jour
			List<Calendar> listDateEffet = dossier.getDateEffet();
			if (listDateEffet.isEmpty()) {
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.bordereau.au.moins.une.date.effet.inferieure.date.jour");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return false;
			} else {
				Calendar now = Calendar.getInstance();
				for (Calendar dateEffet : listDateEffet) {
					if (dateEffet.after(now)) {
						return true;
					}
				}
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.bordereau.au.moins.une.date.effet.inferieure.date.jour");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return false;
			}
		}
	}
	
	/**
	 * Getter de retourDANList.
	 * 
	 * @return norPrmList.
	 * @throws ClientException
	 */
	public List<VocabularyEntry> getRetourDANList() throws ClientException {
		final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
		
		retourDANList = tableReferenceService.getRetoursDAN(documentManager);
				
		return retourDANList;
	}

	/**
	 * Listener appelé lors d'un changement de valeur dans le menu de choix du poste du retour pour modification
	 * 
	 * @param event
	 */
	public void retourDANChangeListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() == null) {
			return;
		}
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof String) {
			setRetourDAN((String) event.getNewValue());
		} else {
			throw new ClientException("le changement de valeur du poste de retour n'a as été correctement effectué !");
		}
	}

	public String getRetourDAN() {
		return retourDAN;
	}

	public void setRetourDAN(String retourDAN) {
		this.retourDAN = retourDAN;
	}
	
	/**
	 * Coper la feuille de route du dossier saisi  
	 * @throws ClientException 
	 */
	public String copierFdrDossier() throws ClientException {
		DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
		JournalService journalService = SolonEpgServiceLocator.getJournalService();
		CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();

		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		String poste = dossier.getCreatorPoste();
		String posteId = poste.substring(6);
		if (isNorFdrCopieValide()) {
			dossierDistributionService.startRouteCopieFromDossier(documentManager, dossierDoc, posteId, norDossierCopieFdr);

			final String message = resourcesAccessor.getMessages().get(
					"message.epg.dossier.copie.fdr");
			facesMessages.add(StatusMessage.Severity.INFO, message);

			journalService.journaliserActionFDR(documentManager, dossierDoc,
					STEventConstant.EVENT_COPIE_FDR_DEPUIS_DOSSIER, STEventConstant.COMMENT_COPIE_FDR_DEPUIS_DOSSIER + " <" + norDossierCopieFdr + ">");
		}
		norDossierCopieFdr = null;
		
		List<DocumentModel> dossierLinkDocs = corbeilleService.findDossierLink(documentManager, dossierDoc.getId());
		DossierLink dossierLink = dossierLinkDocs.get(0).getAdapter(DossierLink.class);
		corbeilleActions.setCurrentDossierLink(dossierLink);

		return routingWebActions.getFeuilleRouteView();
	}

	public String getNorDossierCopieFdr() {
		return norDossierCopieFdr;
	}

	public void setNorDossierCopieFdr(String norDossierCopieFdr) {
		this.norDossierCopieFdr = norDossierCopieFdr;
	}
	
	/**
	 * Verifie que le nor saisie pour la copie de la fdr est valide
	 * 
	 * return vrai si le nor est valide
	 * @throws ClientException 
	 */
	protected boolean isNorFdrCopieValide() throws ClientException {
		NORService norService = SolonEpgServiceLocator.getNORService();
		if (norService.getDossierFromNORWithACL(documentManager, norDossierCopieFdr) != null) {
			return true;
		} else {
			final String message = resourcesAccessor.getMessages().get("label.epg.dossier.copier.fdr.error.nor");
			facesMessages.add(StatusMessage.Severity.ERROR, message);
			return false;
		}
	}
}
