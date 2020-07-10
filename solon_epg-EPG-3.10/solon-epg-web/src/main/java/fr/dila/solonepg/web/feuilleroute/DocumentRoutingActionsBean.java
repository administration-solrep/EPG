package fr.dila.solonepg.web.feuilleroute;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.common.collections.ScopedMap;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.event.CoreEventConstants;
import org.nuxeo.ecm.platform.types.Type;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.EpreuvePostesDto;
import fr.dila.solonepg.api.dto.EtapeFeuilleRouteSqueletteDTO;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.dto.EpreuvePostesDtoImpl;
import fr.dila.solonepg.core.dto.EtapeFeuilleRouteSqueletteDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.dossier.DossierDistributionActionsBean;
import fr.dila.ss.api.dto.EtapeFeuilleDeRouteDTO;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.groupcomputer.MinistereGroupeHelper;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.widget.RouteElementStateRenderDTO;

/**
 * WebBean qui surcharge et étend celui de Nuxeo.
 * 
 * @author jtremeaux
 */
@Scope(CONVERSATION)
@Name("routingActions")
@Install(precedence = Install.FRAMEWORK + 2)
public class DocumentRoutingActionsBean extends fr.dila.ss.web.feuilleroute.DocumentRoutingActionsBean implements
		Serializable {


	/**
	 * Serial UID.
	 */
	private static final long			serialVersionUID					= -6391337067078995231L;

	private static final STLogger		LOGGER								= STLogFactory.getLog(DocumentRoutingActionsBean.class);

	private static Map<String, String>	lifeCycleStateToImg					= null;
	private static Map<String, String>	lifeCycleStateToLabel				= null;
	private static Map<String, String>	validStatusToImg					= null;
	private static Map<String, String>	validStatusToLabel					= null;

	private static final Set<String>	INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB	= Collections
																					.unmodifiableSet(initIncompatibleSteps());

	private static Set<String> initIncompatibleSteps() {
		Set<String> incompatibleSteps = new HashSet<String>();
		incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE);
		incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO);
		incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO);
		return incompatibleSteps;
	}

	static {
		lifeCycleStateToImg = new HashMap<String, String>();
		lifeCycleStateToLabel = new HashMap<String, String>();

		lifeCycleStateToImg.put("draft", "/img/icons/bullet_ball_glass_grey_16.png");
		lifeCycleStateToLabel.put("draft", "label.epg.feuilleRoute.etape.draft");

		lifeCycleStateToImg.put("validated", "/img/icons/bullet_ball_glass_green_16.png");
		lifeCycleStateToLabel.put("validated", "label.epg.feuilleRoute.etape.validated");

		lifeCycleStateToImg.put("ready", "/img/icons/bullet_ball_glass_grey_16.png");
		lifeCycleStateToLabel.put("ready", "label.epg.feuilleRoute.etape.ready");

		lifeCycleStateToImg.put("running", "/img/icons/bullet_ball_glass_yellow_16.png");
		lifeCycleStateToLabel.put("running", "label.epg.feuilleRoute.etape.running");

		validStatusToImg = new HashMap<String, String>();
		validStatusToLabel = new HashMap<String, String>();

		// État validée manuellement
		validStatusToLabel.put("1", "label.epg.feuilleRoute.etape.valide.manuellement");
		validStatusToImg.put("1", "/img/icons/check_16.png");

		// État avis favorable avec correction
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE,
				"label.epg.feuilleRoute.etape.valide.avisFavorableCorrection");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE,
				"/img/icons/check_yellow_16.png");

		// État invalidée
		validStatusToLabel.put("2", "label.epg.feuilleRoute.etape.valide.refusValidation");
		validStatusToImg.put("2", "/img/icons/check_red_16.png");

		// État validée automatiquement
		validStatusToLabel.put("3", "label.epg.feuilleRoute.etape.valide.automatiquement");
		validStatusToImg.put("3", "/img/icons/bullet_ball_glass_green_16.png");

		// État avec sortie 'non concerné'
		validStatusToLabel.put("4", "label.epg.feuilleRoute.etape.valide.nonConcerne");
		validStatusToImg.put("4", "/img/icons/non_concerne.png");

		// État retour pour modification
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE,
				"label.epg.feuilleRoute.etape.valide.retourModification");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE,
				"/img/icons/back_update_16.png");

		// État validée manuellement : avis rendu
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE,
				"label.epg.feuilleRoute.etape.valide.manuellement.avisRendu");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE,
				"/img/icons/check_16.png");

		// État invalidée : dessaisissement
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE,
				"label.epg.feuilleRoute.etape.valide.refusValidation.dessaisissement");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE,
				"/img/icons/check_red_16.png");

		// État invalidée : retrait
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE,
				"label.epg.feuilleRoute.etape.valide.refusValidation.retrait");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE,
				"/img/icons/check_red_16.png");

		// État invalidée : refus
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE,
				"label.epg.feuilleRoute.etape.valide.manuellement.refus");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE,
				"/img/icons/check_red_16.png");

		// État invalidée : renvoi
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE,
				"label.epg.feuilleRoute.etape.valide.manuellement.renvoi");
		validStatusToImg.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE,
				"/img/icons/check_red_16.png");
	}

	@In(create = true, required = false)
	protected transient DossierDistributionActionsBean	dossierDistributionActions;

	@In(create = true, required = false)
	protected transient RoutingTaskFilterBean			routingTaskFilter;

	private EpreuvePostesDto							postesEpreuvage;

	@Override
	public boolean isEditableRouteElement(DocumentModel stepDoc) throws ClientException {

		boolean result = true;

		final STFeuilleRoute route = getRelatedRoute();

		if (!super.isEditableRouteElement(stepDoc)) {
			return false;
		} else if (route.isRunning()) {
			// À l'étape "pour impression", le dossier est en lecture seule sauf pour les administrateurs
			if (!dossierDistributionActions.isEtapePourImpressionUpdater()) {
				result = false;
			}
		} else if (route.isDraft()) {
			// condition sur les modèles de feuilles de route non validé (brouillon)
			final List<String> groups = currentUser.getGroups();
			if (groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR)) {
				result = true;
			} else {
				// l'administrateur ministériel ne peut modifier que les feuilles de route affecté à ses ministères sauf
				// ceux crées par un Administrateur fonctionnel
				if (isFeuilleDeRouteCreeParAdminFonctionnel(stepDoc)) {
					result = false;
				} else {
					final String ministere = route.getMinistere();
					final String groupMinistere = MinistereGroupeHelper.ministereidToGroup(ministere);
					if (StringUtils.isBlank(ministere) || !groups.contains(groupMinistere)
							|| !groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER)) {
						result = false;
					}
				}
			}
		}

		return result;
	}

	/**
	 * Récupère les feuilles de routes associées au dossier : utilisé lorsque l'on ouvre automatiquement un dossier dans
	 * une liste de dossiers ne possédant qu'un dossier.
	 * 
	 * @throws ClientException
	 */
	public void computeRelatedRoutes() throws ClientException {
		relatedRoutes = relatedRouteAction.findRelatedRoute();
	}

	@Override
	public boolean hasRelatedRoute() throws ClientException {
		if (relatedRoutes.isEmpty()) {
			// si les feuilles de route associées par défaut sont vides, on récupère les feuilles de route associées au
			// dossier
			computeRelatedRoutes();
		}
		return !relatedRoutes.isEmpty();
	}

	/**
	 * Retourne vrai si le document est crée par l'adminitrateur fonctionnel.
	 * 
	 * @param doc
	 * @return
	 * @throws ClientException
	 */
	private boolean isFeuilleDeRouteCreeParAdminFonctionnel(DocumentModel doc) throws ClientException {
		String feuilleDeRouteCreateur = DublincoreSchemaUtils.getCreator(doc);

		UserManager userManager = STServiceLocator.getUserManager();
		SSPrincipal principal = (SSPrincipal) userManager.getPrincipal(feuilleDeRouteCreateur);

		if (principal == null) {
			return false;
		} else {
			return principal.isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME);
		}
	}

	public RouteElementStateRenderDTO getActionFeuilleRouteInfos(final String currentLifecycleState,
			final String validationStatus, final String routingTaskType) throws ClientException {

		if ("done".equals(currentLifecycleState)) {
			final String img = validStatusToImg.get(validationStatus);
			if (img == null) {
				return null;
			} else {
				final String labelKey = validStatusToLabel.get(validationStatus);
				final String style = "vertical-align: top;";
				final String text = dossierDistributionActions.getActionFeuilleRouteButtonLabel(routingTaskType,
						labelKey);
				return new RouteElementStateRenderDTO(img, text, text, style);
			}
		} else {
			final String img = lifeCycleStateToImg.get(currentLifecycleState);
			if (img == null) {
				return null;
			} else {
				final String label = resourcesAccessor.getMessages().get(
						lifeCycleStateToLabel.get(currentLifecycleState));
				return new RouteElementStateRenderDTO(img, label);
			}
		}
	}

	@Override
	public String saveRouteElement() throws ClientException {

		if (checkValidityStep()) {
			return super.saveRouteElement();
		}
		return STViewConstant.ERROR_VIEW;
	}

	@Override
	public String updateRouteElement() throws ClientException {
		if (checkValidityStep()) {
			return super.updateRouteElement();
		}
		return STViewConstant.ERROR_VIEW;
	}

	/**
	 * Permet la création d'étapes pour l'épreuvage.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String createStepsPourEpreuve() throws ClientException {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		// gestion du cas où le currentDoc est un dossier
		DocumentRef routeRef = getRouteRefFromDocument(currentDocument);
		DocumentModel routeDoc = documentManager.getDocument(routeRef);
		// Il faut vérifier que les paramètres pour la génération automatique des étapes d'épreuvage sont renseignés, à
		// savoir : poste DAN, poste publication et poste BDC
		final TableReferenceService tdrService = SolonEpgServiceLocator.getTableReferenceService();
		String posteDanId = tdrService.getPosteDanId(documentManager);
		String postePublicationId = tdrService.getPostePublicationId(documentManager);
		String posteBdcId = getPosteBdcIdFromRoute(routeDoc);
		if (StringUtil.isEmpty(posteBdcId) || StringUtil.isEmpty(postePublicationId) || StringUtil.isEmpty(posteDanId)) {
			return "initialize_postes_epreuvage";
		} else {
			IdRef sourceDocRef = new IdRef(hiddenSourceDocId);
			DocumentModel sourceDoc = getSourceDocFromRef(sourceDocRef);

			String sourceDocName = null;
			String parentPath = null;
			if (STEP_ORDER_IN.equals(hiddenDocOrder)) {
				parentPath = sourceDoc.getPathAsString();
			} else {
				DocumentModel parentDoc = documentManager.getParentDocument(sourceDocRef);
				parentPath = parentDoc.getPathAsString();
				sourceDocName = getSourceDocName(sourceDoc, parentDoc);
			}

			SolonEpgServiceLocator.getDocumentRoutingService().createStepsPourEpreuve(documentManager, posteBdcId,
					postePublicationId, posteDanId, routeDoc, sourceDocName, parentPath);
			facesMessages.add(StatusMessage.Severity.INFO,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.info.add.step.epreuvage"));
		}

		return routingWebActions.getFeuilleRouteView();
	}

	/**
	 * Permet la création d'étapes pour l'épreuvage.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String createStepsPourEpreuveApresInit() throws ClientException {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		DocumentRef routeRef = getRouteRefFromDocument(currentDocument);
		DocumentModel routeDoc = documentManager.getDocument(routeRef);
		IdRef sourceDocRef = new IdRef(hiddenSourceDocId);
		DocumentModel sourceDoc = getSourceDocFromRef(sourceDocRef);

		String posteDanId = postesEpreuvage.getEpreuvePosteDan();
		String postePublicationId = postesEpreuvage.getEpreuvePostePublication();
		String posteBdcId = postesEpreuvage.getEpreuvePosteBdc();

		String sourceDocName = null;
		String parentPath = null;
		if (STEP_ORDER_IN.equals(hiddenDocOrder)) {
			parentPath = sourceDoc.getPathAsString();
		} else {
			DocumentModel parentDoc = documentManager.getParentDocument(sourceDocRef);
			parentPath = parentDoc.getPathAsString();
			sourceDocName = getSourceDocName(sourceDoc, parentDoc);
		}

		if ((!isSqueletteFeuilleRoute(currentDocument) && StringUtil.isEmpty(posteBdcId))
				|| StringUtil.isEmpty(postePublicationId) || StringUtil.isEmpty(posteDanId)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.add.step.epreuvage"));
			return null;
		}
		SolonEpgServiceLocator.getDocumentRoutingService().createStepsPourEpreuve(documentManager, posteBdcId,
				postePublicationId, posteDanId, routeDoc, sourceDocName, parentPath);

		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.info.add.step.epreuvage"));
		return routingWebActions.getFeuilleRouteView();
	}

	public String annulerPostesEpreuvage() {
		postesEpreuvage = null;
		return routingWebActions.getFeuilleRouteView();
	}

	public boolean canAddEpreuveSteps() {
		if (currentUser.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR)) {
			return true;
		}
		return false;
	}

	public EpreuvePostesDto getPostesEpreuvage() throws ClientException {
		if (postesEpreuvage == null) {
			postesEpreuvage = new EpreuvePostesDtoImpl();
			DocumentModel currentDocument = navigationContext.getCurrentDocument();
			DocumentRef routeRef = getRouteRefFromDocument(currentDocument);
			DocumentModel routeDoc = documentManager.getDocument(routeRef);
			final TableReferenceService tdrService = SolonEpgServiceLocator.getTableReferenceService();
			String posteDanId = tdrService.getPosteDanId(documentManager);
			String postePublicationId = tdrService.getPostePublicationId(documentManager);

			if(!isSqueletteFeuilleRoute(routeDoc)) {
				String posteBdcId = getPosteBdcIdFromRoute(routeDoc);
				postesEpreuvage.setEpreuvePosteBdc(posteBdcId);
			}
			
			postesEpreuvage.setEpreuvePostePublication(postePublicationId);
			postesEpreuvage.setEpreuvePosteDan(posteDanId);
		}
		return postesEpreuvage;
	}

	private String getPosteBdcIdFromRoute(DocumentModel routeDoc) throws ClientException {
		final STPostesService posteService = STServiceLocator.getSTPostesService();

		// Récupération du ministère attributaire du dossier lié à la fdr
		// qui est aussi le ministère de la fdr
		SolonEpgFeuilleRoute route = routeDoc.getAdapter(SolonEpgFeuilleRoute.class);
		String minAttributaireId = route.getMinistere();
		if (StringUtil.isEmpty(minAttributaireId)) {
			// On n'a pas pu récupérer l'id du ministère à partir de la fdr. Il s'agit peut être de la feuille de route
			// par défaut qui n'est liée à aucun ministère. On est obligé de récupérer le dossier
			String query = "select * from Dossier where dos:lastDocumentRoute='" + routeDoc.getId() + "'";
			DocumentModelList dossiersList = QueryUtils.doQuery(documentManager, query, 1, 0);
			if (dossiersList.isEmpty()) {
				return "";
			}
			DocumentModel dossierDoc = dossiersList.get(0);
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			minAttributaireId = dossier.getMinistereAttache();
		}

		PosteNode posteBdc = posteService.getPosteBdcInEntite(minAttributaireId);
		if (posteBdc == null) {
			return "";
		}
		return posteBdc.getId();
	}

	/**
	 * Copie la sélection vers le presse-papier et vide la sélection.
	 * Si une étape de feuille de route 'Pour Initialisation' est copiée, cela la retire et affiche un message de warning
	 */
	@Override
	public void putSelectionInClipboardAndEmptySelection() {
		
		if (!documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION)) {
			List<DocumentModel> docsList = documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			
			Iterator<DocumentModel> itDocs = docsList.iterator();
			
			while (itDocs.hasNext()) {
				DocumentModel document = itDocs.next();
				String typeEtape = null;
				if (STConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(document.getType()) || 
						SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(document.getType())) {
					STRouteStep routeStep = document.getAdapter(STRouteStep.class);
					typeEtape = routeStep.getType();
					if (VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(typeEtape)) {
						itDocs.remove();
						facesMessages.add(StatusMessage.Severity.WARN,
								resourcesAccessor.getMessages().get("feedback.solonepg.copy.pourinitialisation"));
					}
				}
			}
		}
		
		clipboardActions.putSelectionInClipboard();
		
		if (!documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION)) {
			documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION).clear();
		}
	}
	
	@Override
	protected boolean checkCompatibilityBeforePaste(final List<DocumentModel> stepDocsToPaste) {
		if (super.checkCompatibilityBeforePaste(stepDocsToPaste)) {
			try {
				for (DocumentModel stepDoc : stepDocsToPaste) {
					if (!checkValidityStep(stepDoc)) {
						return false;
					}
				}
			} catch (ClientException ce) {
				LOGGER.error(documentManager, SSLogEnumImpl.FAIL_UPDATE_FDR_FONC, ce);
				facesMessages.add(StatusMessage.Severity.WARN, "Echec du traitement. Le traitement a été annulé.");
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	public boolean checkValiditySteps(SolonEpgFeuilleRoute route, List<DocumentModel> stepsDoc) throws ClientException {
		for (DocumentModel stepDoc : stepsDoc) {
			STRouteStep step = stepDoc.getAdapter(STRouteStep.class);
			if (!step.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION) && !checkStepWithTxtNonPub(step,route.getTypeActe())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean checkValidityStep(DocumentModel stepDoc) throws ClientException {
		SolonEpgRouteStep routeStep = stepDoc.getAdapter(SolonEpgRouteStep.class);
		
		final boolean isSqueletteStep = SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(stepDoc.getType());
		final boolean isDestinataireOrganigramme = !isSqueletteStep
				|| (SqueletteStepTypeDestinataire.ORGANIGRAMME.equals(routeStep.getTypeDestinataire()));

		if (isSqueletteStep && !checkValidityStepSqueletteMailbox(routeStep, isDestinataireOrganigramme)) {
			return false;
		}
		
		String errorCompatibility = "";
		
		// vérifie la compatibilité des postes avec l'étape suivante
		if (!isSqueletteStep || isDestinataireOrganigramme) {
			errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
					.isNextPostCompatibleWithNextStep(documentManager, routeStep);
		}

		// On vérifie également que l'étape est bien compatible avec le type de dossier
		if ("".equals(errorCompatibility) && docWithAttachedRouteId != null) {
			errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService().isNextStepCompatibleWithTypeActe(
					routeStep, documentManager, docWithAttachedRouteId);
		}
		if (StringUtil.isNotBlank(errorCompatibility)) {
			facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
			return false;
		}

		if (docWithAttachedRouteId != null) {
			// on va voir le type d'acte du dossier rattaché pour gérer le cas où l'instance de fdr
			// est une copie du modele de fdr par défaut.
			// N.B. : la méta donnée type acte d'une fdr est renseignée dans les modèles, mais pas mis à jour dans les
			// instances
			IdRef docRef = new IdRef(docWithAttachedRouteId);
			if (documentManager.exists(docRef)) {
				DocumentModel dossierDoc = documentManager.getDocument(docRef);
				Dossier dossier = dossierDoc.getAdapter(Dossier.class);
				return checkStepWithTxtNonPub(routeStep, dossier.getTypeActe());
			}
		} else {
			STFeuilleRoute stFeuilleRoute = getRelatedRoute();
			SolonEpgFeuilleRoute feuilleRoute = stFeuilleRoute.getDocument().getAdapter(SolonEpgFeuilleRoute.class);
			return checkStepWithTxtNonPub(routeStep, feuilleRoute.getTypeActe());
		}
		return true;
	}

	protected boolean checkValidityStepSqueletteMailbox(SolonEpgRouteStep routeStep,
			final boolean isDestinataireOrganigramme) {
		// Si c'est un squelette, le type de destinataire est obligatoire
		if (routeStep.getTypeDestinataire() == null) {
			facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages()
					.get("feedback.feuilleRoute.squelette.step.typeDestinataireEmpty"));
			return false;
		}
		// Si c'est un squelette et qu'on a choisi le type de destinataire
		// organigramme, le choix du poste est obligatoire
		if (isDestinataireOrganigramme && StringUtil.isEmpty(routeStep.getDistributionMailboxId())) {
			facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages()
					.get("feedback.feuilleRoute.squelette.step.organigramme.posteEmpty"));
			return false;
		}
		
		return true;
	}

	/**
	 *
	 * Lors de l'ajout d'une étape à une FdR: Vérifie que le type d'étape soit valide par rapport au type d'acte
	 *
	 * @return
	 * @throws ClientException
	 */
	private boolean checkValidityStep() throws ClientException {
		DocumentModel newDocument = navigationContext.getChangeableDocument();
		
		return checkValidityStep(newDocument);
	}

	/**
	 * Vérifie la compatibilité de l'acte textes non publiés avec l'étape
	 * 
	 * @param typeActe
	 * @return
	 */
	private boolean checkStepWithTxtNonPub(STRouteStep routeStep, String typeActe) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		if (acteService.isActeTexteNonPublie(typeActe)
				&& INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(routeStep.getType())) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.error.add.step.typeActe"));
			return false;
		}
		return true;
	}

	/**
	 * Indique si on manipule un squelette plutôt qu'un modèle "classique" de feuille de route
	 * 
	 * @return true ssi le modèle chargé est un squelette de feuille de route.
	 */
	public boolean isSqueletteFeuilleRoute() {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		return isSqueletteFeuilleRoute(currentDocument);
	}
	
	private boolean isSqueletteFeuilleRoute(DocumentModel docModel) {
		return SolonEpgServiceLocator.getDocumentRoutingService().isSqueletteFeuilleRoute(docModel);
	}
	
	/**
	 * Retourne vrai si l'utilisateur peut modifier l'instance de feuille de route.
	 * 
	 * @return Vrai si l'utilisateur peut modifier l'instance de feuille de route.
	 */
	@Override
	public boolean isFeuilleRouteUpdatable() {
		if(isSqueletteFeuilleRoute()) {
			return true;
		}
		return super.isFeuilleRouteUpdatable();
	}
	
	/**
	 * Vérifie les étapes entrées par l'utilisateur lors de la création d'étapes de squelettes de feuille de route en masse.
	 * Remplit les erreurs facesMessages si besoin.
	 * @return true si les étapes sont ok, false sinon.
	 * @throws ClientException 
	 */
	private boolean checkInputMassSquelette() throws ClientException {
		if (lstEtapes == null || lstEtapes.isEmpty()) {
			// l'utilisateur n'a remplit aucune ligne dans le tableau
			final String message = resourcesAccessor.getMessages().get("label.epg.feuilleRoute.create.error.listEmpty");
			facesMessages.add(StatusMessage.Severity.ERROR, message);
			return false;
		}

		for (EtapeFeuilleDeRouteDTO element : lstEtapes) {
			EtapeFeuilleRouteSqueletteDTO etape = (EtapeFeuilleRouteSqueletteDTO) element;
			if (etape.getTypeDestinataire() == null) {
				facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages()
						.get("feedback.feuilleRoute.squelette.step.typeDestinataireEmpty"));
				return false;
			}

			if (etape.getTypeDestinataire().equals(SqueletteStepTypeDestinataire.ORGANIGRAMME)
					&& StringUtil.isEmpty(element.getDistributionMailboxId())) {
				facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages()
						.get("feedback.feuilleRoute.squelette.step.organigramme.posteEmpty"));
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Crée un DocumentModel de type étape de squelette de feuille de route (RouteStep) à partir d'un EtapeFeuilleRouteSqueletteDTO
	 * @return
	 * @throws ClientException 
	 */
	private DocumentModel createSqueletteEtapeFromDTO(EtapeFeuilleRouteSqueletteDTO eFDR) throws ClientException {
		
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		DocumentRef routeRef = getRouteRefFromDocument(currentDocument);
		DocumentRef sourceDocRef = new IdRef(hiddenSourceDocId);

		DocumentModel sourceDoc = getSourceDocFromRef(sourceDocRef);

		String sourceDocName = null;
		String parentPath = null;
		if (STEP_ORDER_IN.equals(hiddenDocOrder)) {
			parentPath = sourceDoc.getPathAsString();
		} else {
			DocumentModel parentDoc = documentManager.getParentDocument(sourceDocRef);
			parentPath = parentDoc.getPathAsString();
			sourceDocName = getSourceDocName(sourceDoc, parentDoc);
		}

		String typeToCreateName = SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE;

		Type docType = typeManager.getType(typeToCreateName);

		// we cannot use typesTool as intermediary since the DataModel callback
		// will alter whatever type we set
		typesTool.setSelectedType(docType);
		
		DocumentModel changeableDocument = documentManager.createDocumentModel(typeToCreateName);
		ScopedMap context = changeableDocument.getContextData();
		context.put(CoreEventConstants.PARENT_PATH, parentPath);
		context.put(SOURCE_DOC_NAME, sourceDocName);
		context.put(ROUTE_DOCUMENT_REF, routeRef);
		
		SolonEpgRouteStep routeStepDesired = changeableDocument.getAdapter(SolonEpgRouteStep.class);
		
		routeStepDesired.setType(eFDR.getTypeEtape());
		routeStepDesired.setDistributionMailboxId(eFDR.getDistributionMailboxId());
		routeStepDesired
				.setAutomaticValidation(eFDR.getAutomaticValidation() != null ? eFDR.getAutomaticValidation() : false);
		routeStepDesired.setObligatoireSGG(eFDR.getObligatoireSGG() != null ? eFDR.getObligatoireSGG() : false);
		routeStepDesired.setObligatoireMinistere(
				eFDR.getObligatoireMinistere() != null ? eFDR.getObligatoireMinistere() : false);
		if (eFDR.getDeadLine() != null) {
			routeStepDesired.setDeadLine(Long.parseLong(eFDR.getDeadLine()));
		}
		
		routeStepDesired.setTypeDestinataire(eFDR.getTypeDestinataire());
		
		return changeableDocument;
	}
	
	@Override
	public String saveRouteElementMass() throws ClientException {
		if(!isSqueletteFeuilleRoute()) {
			return super.saveRouteElementMass();
		}
		
		if (!this.checkInputMassSquelette()) {
			return STViewConstant.ERROR_VIEW;
		}
		
		/* Création des DocumentModels des étapes à créer et vérification de la validité */
		List<DocumentModel> etapesFDRLst = new ArrayList<DocumentModel>();
		boolean canCreate = true;
		
		for (EtapeFeuilleDeRouteDTO eFDR : lstEtapes) {
			DocumentModel changeableDocument = createSqueletteEtapeFromDTO((EtapeFeuilleRouteSqueletteDTO) eFDR);
			etapesFDRLst.add(changeableDocument);
			
			boolean isValid = checkValidityStep(changeableDocument);
			if (!isValid) {
				canCreate = false;
			}
		}
		if (!canCreate) {
			// les étapes ne sont pas valides, on ne crée pas les étapes et l'on demande à ce qu'elles soient modifiée
			// les message d'erreur sont ajoutés à facesMessages par la méthode checkValidityStep
			return STViewConstant.ERROR_VIEW;
		}
		
		if ("true".equals(serial)) {
			return this.saveRouteElementSerialMass(etapesFDRLst);
		} else {
			return this.saveRouteElementParallelMass(etapesFDRLst);
		}
	}
	
	@Override
	public String addEtapeFeuilleDeRoute() throws ClientException {
		if (lstEtapes != null) {
			
			DocumentModel etapeDoc = this.newSimpleSqueletteRouteStep();
			SolonEpgRouteStep routeStepDesired = etapeDoc.getAdapter(SolonEpgRouteStep.class);
			getLstEtapes().add(new EtapeFeuilleRouteSqueletteDTOImpl(routeStepDesired));
		} else {
			facesMessages.add(StatusMessage.Severity.ERROR, "Erreur, liste des étapes non initialisée");
		}
		return null;
	}
	
	/**
	 * Creation simple d'une squelette routeStep pour la création en masse
	 * 
	 * @return DocumentModel la nouvelle étape à créer
	 * @throws ClientException
	 */
	public DocumentModel newSimpleSqueletteRouteStep() throws ClientException {
		DocumentRef sourceDocRef = new IdRef(hiddenSourceDocId);

		DocumentModel sourceDoc = getSourceDocFromRef(sourceDocRef);

		String parentPath = null;
		if (STEP_ORDER_IN.equals(hiddenDocOrder)) {
			parentPath = sourceDoc.getPathAsString();
		} else {
			DocumentModel parentDoc = documentManager.getParentDocument(sourceDocRef);
			parentPath = parentDoc.getPathAsString();
		}
		
		UUID newId = UUID.randomUUID();
		DocumentModel desiredDocument = documentManager.createDocumentModel(SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE);
		desiredDocument.setPathInfo(parentPath, newId.toString());
		
		return desiredDocument;
	}
	
	@Override
	public List<EtapeFeuilleDeRouteDTO> getLstEtapes() throws ClientException {
		if (lstEtapes == null) {
			lstEtapes = new ArrayList<EtapeFeuilleDeRouteDTO>();
			DocumentModel etapeDoc = this.newSimpleSqueletteRouteStep();
			SolonEpgRouteStep routeStepDesired = etapeDoc.getAdapter(SolonEpgRouteStep.class);
			getLstEtapes().add(new EtapeFeuilleRouteSqueletteDTOImpl(routeStepDesired));
		}
		return lstEtapes;
	}
}
