package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentService;
import org.nuxeo.ecm.core.lifecycle.LifeCycleConstants;
import org.nuxeo.ecm.platform.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.uidgen.service.ServiceHelper;
import org.nuxeo.ecm.platform.uidgen.service.UIDGeneratorService;
import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.DocumentRouteElement;
import fr.dila.ecm.platform.routing.api.DocumentRouteElement.ElementLifeCycleTransistion;
import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;
import fr.dila.ecm.platform.routing.api.exception.DocumentRouteNotLockedException;
import fr.dila.ecm.platform.routing.core.impl.EventFirer;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DocumentRoutingService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.ss.api.domain.feuilleroute.StepFolder;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.exception.LocalizedClientException;
import fr.dila.st.api.feuilleroute.DocumentRouteTreeElement;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

/**
 * Implémentation du service de document routing de SOLON EPG.
 * 
 * @author jtremeaux
 */
public class DocumentRoutingServiceImpl extends fr.dila.ss.core.service.DocumentRoutingServiceImpl implements
		DocumentRoutingService {

	private static final Log		LOGGER					= LogFactory.getLog(DocumentRoutingServiceImpl.class);
	
	@Override
	public void validateMoveRouteStepBefore(DocumentModel routeStepDoc) throws ClientException {
		// Une étape ne peut pas être déplacée avant l'étape "Pour initialisation"
		if (!routeStepDoc.hasFacet("Folderish")) {
			SolonEpgRouteStep routeStep = routeStepDoc.getAdapter(SolonEpgRouteStep.class);
			if (routeStep != null && routeStep.isPourInitialisation()) {
				throw new LocalizedClientException("st.feuilleRoute.action.moveUp.avantPourInitialisation.error");
			}
		}
	}

	@Override
	public boolean isEtapeObligatoireUpdater(CoreSession session, DocumentModel routeStepDoc) throws ClientException {
		// Traite uniquement les étapes de feuille de route et pas les conteneurs
		if (routeStepDoc.hasFacet("Folderish")) {
			return true;
		}
		final STRouteStep routeStep = routeStepDoc.getAdapter(STRouteStep.class);

		// Seul l'administrateur fonctionnel ou ministeriel a le droit de modifier les étapes obligatoires SGG

		if (routeStep.isObligatoireSGG()) {
			final NuxeoPrincipal nuxeoPrincipal = (NuxeoPrincipal) session.getPrincipal();
			final List<String> groups = nuxeoPrincipal.getGroups();

			boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);
			boolean isEtapeObligatoirUpdater = groups
					.contains(SolonEpgBaseFunctionConstant.FEUILLE_ROUTE_OBLIGATOIRE_SGG_UPDATER);
			return isAdminFonctionnel || isEtapeObligatoirUpdater;
		}

		// Seul l'administrateur ministériel a le droit de modifier les étapes obligatoires ministère
		if (routeStep.isObligatoireMinistere()) {
			final NuxeoPrincipal nuxeoPrincipal = (NuxeoPrincipal) session.getPrincipal();
			final List<String> groups = nuxeoPrincipal.getGroups();

			return groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER);
		}

		return true;
	}

	/**
	 * @author FLT Allow to check if the next step is compatible with the configured CE minister.
	 * 
	 */
	@Override
	public String isNextPostCompatibleWithNextStep(final CoreSession session, final STRouteStep nextstepDocument)
			throws ClientException {
		// on vérifie que le post spécifié pour l'étape est un posteCE
		DocumentModel tableReferenceDoc = SolonEpgServiceLocator.getTableReferenceService().getTableReference(session);
		TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
		String ministereCeId = tableReference.getMinistereCE();
		return isNextPostCompatibleWithNextStep(ministereCeId, nextstepDocument,
				VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
	}

	/**
	 * @author FLT Allow to check if the next steps are compatibles with the configured CE minister.
	 */
	@Override
	public String isNextPostsCompatibleWithNextStep(final CoreSession session, final List<DocumentModel> steps)
			throws ClientException {
		for (DocumentModel step : steps) {
			if (step.getAdapter(STRouteStep.class) != null) {// permet d'éviter de tester les branches qui ne sont pas,
																// techniquement, des étapes.
				String nextStepValidity = isNextPostCompatibleWithNextStep(session, step.getAdapter(STRouteStep.class));
				if (StringUtil.isNotEmpty(nextStepValidity)) {
					return nextStepValidity;
				}
			}
		}
		return "";
	}

	/**
	 * @author FLT Allow to check if the next step is compatible with the asked minister.
	 * 
	 */
	@Override
	public String isNextPostCompatibleWithNextStep(String ministereRequisId, final STRouteStep nextstepDocument,
			final String typeAvis) throws ClientException {

		String posteDestinationId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(
				nextstepDocument.getDistributionMailboxId());
		final PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteDestinationId);

		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		List<EntiteNode> entiteNodeList = ministeresService.getMinisteresParents(posteNode);
		Boolean isPosteCe = false;
		for (EntiteNode entiteNode : entiteNodeList) {
			if (ministereRequisId != null && ministereRequisId.equals(entiteNode.getId())) {
				isPosteCe = true;
				break;
			}
		}
		// on vérifie pour le cas de "Pour Avis CE"
		if (nextstepDocument.getType().equals(typeAvis)) {
			if (!isPosteCe) {
				return "Le poste lié n'est pas un poste du Conseil d'Etat alors que l'étape est 'Pour avis CE'.";
			}
		} else {
			if (isPosteCe) {
				return "Le poste lié est un poste du Conseil d'Etat alors que l'étape n'est pas 'Avis pour CE'.";
			}
		}

		return "";
	}

	/**
	 * @author FLT Allow to check if the next steps are compatibles with the asked minister.
	 * 
	 */
	@Override
	public String isNextPostsCompatibleWithNextStep(final String ministereRequisId, final List<DocumentModel> steps,
			final String typeAvisCe) throws ClientException {

		for (DocumentModel step : steps) {
			String nextStepValidity = isNextPostCompatibleWithNextStep(ministereRequisId,
					step.getAdapter(STRouteStep.class), typeAvisCe);
			if (StringUtil.isNotEmpty(nextStepValidity)) {
				return nextStepValidity;
			}
		}
		return "";
	}

	@Override
	public void createStepsPourEpreuve(CoreSession session, String posteBdcId, String postePublicationId,
			String posteDanId, DocumentModel routeDoc, String sourceDocName, String parentPath) throws ClientException {

		String stepAttributionName = "Etape" + VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION + "-" + posteDanId;
		String stepFournitureName = "Etape" + VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE + "-"
				+ postePublicationId;
		String stepBatName;
		
		if (isSqueletteFeuilleRoute(routeDoc)) {
			stepBatName = "Etape" + VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER;
			
			// Création du document model de l'étape "Pour fourniture
			// d'épreuves" au poste de publication
			createRouteStepSquelette(session, parentPath, stepFournitureName,
					VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE, postePublicationId, true, routeDoc,
					sourceDocName,SqueletteStepTypeDestinataire.ORGANIGRAMME);
			// Création du document model de l'étape "Pour attribution" au poste
			// du DAN
			createRouteStepSquelette(session, parentPath, stepAttributionName, VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
					posteDanId, true, routeDoc, sourceDocName,SqueletteStepTypeDestinataire.ORGANIGRAMME);

			// Création du document model de l'étape "Pour bon à tirer" au poste
			// BDC du min attributaire
			createRouteStepSquelette(session, parentPath, stepBatName,
					VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER, null, false, routeDoc, sourceDocName,
					SqueletteStepTypeDestinataire.BUREAU_DU_CABINET);

			// Création du document model de l'étape "Pour attribution" au poste
			// du DAN
			createRouteStepSquelette(session, parentPath, stepAttributionName, VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
					posteDanId, true, routeDoc, sourceDocName,SqueletteStepTypeDestinataire.ORGANIGRAMME);
		} else {
			stepBatName = "Etape" + VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER + "-" + posteBdcId;
			
			// Création du document model de l'étape "Pour fourniture
			// d'épreuves" au poste de publication
			createRouteStep(session, parentPath, stepFournitureName,
					VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE, postePublicationId, true, routeDoc,
					sourceDocName);
			// Création du document model de l'étape "Pour attribution" au poste
			// du DAN
			createRouteStep(session, parentPath, stepAttributionName, VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
					posteDanId, true, routeDoc, sourceDocName);

			// Création du document model de l'étape "Pour bon à tirer" au poste
			// BDC du min attributaire
			createRouteStep(session, parentPath, stepBatName, VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER,
					posteBdcId, false, routeDoc, sourceDocName);

			// Création du document model de l'étape "Pour attribution" au poste
			// du DAN
			createRouteStep(session, parentPath, stepAttributionName, VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
					posteDanId, true, routeDoc, sourceDocName);
		}
		
		session.save();
	}
	
	@Override
	public boolean isSqueletteFeuilleRoute(DocumentModel docModel) {
		return (SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(docModel.getType()) ||
				SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(docModel.getType()));
	}

	/**
	 * Création d'une étape de feuille de route à l'état ready
	 */
	private SolonEpgRouteStep createRouteStep(CoreSession session, String parentPath, String stepName, String stepType,
			String posteId, boolean obligatoireSgg, DocumentModel routeDoc, String previousStepName) throws ClientException {
		return createGenericRouteStep(session, parentPath, stepName, stepType, posteId, obligatoireSgg, routeDoc,
				previousStepName, SqueletteStepTypeDestinataire.ORGANIGRAMME, STConstant.ROUTE_STEP_DOCUMENT_TYPE);
	}
	
	/**
	 * Création d'une étape de feuille de route à l'état ready
	 */
	private SolonEpgRouteStep createRouteStepSquelette(CoreSession session, String parentPath, String stepName, String stepType,
			String posteId, boolean obligatoireSgg, DocumentModel routeDoc, String previousStepName,
			SqueletteStepTypeDestinataire typeDestinataire) throws ClientException {
		return createGenericRouteStep(session, parentPath, stepName, stepType, posteId, obligatoireSgg, routeDoc,
				previousStepName, typeDestinataire, SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE);
	}
	
	private SolonEpgRouteStep createGenericRouteStep(CoreSession session, String parentPath, String stepName, String stepType,
			String posteId, boolean obligatoireSgg, DocumentModel routeDoc, String previousStepName,
			SqueletteStepTypeDestinataire typeDestinataire, String documentType) throws ClientException {
		// Initialisation du documentmodel du document qui sera créé
		DocumentModel stepFournitureDoc = session.createDocumentModel(parentPath, stepName, documentType);
		SolonEpgRouteStep step = stepFournitureDoc.getAdapter(SolonEpgRouteStep.class);
		
		if(posteId != null) {
			String mailboxPosteId = SSServiceLocator.getMailboxPosteService().getPosteMailboxId(posteId);
			step.setDistributionMailboxId(mailboxPosteId);
		}
		
		step.setObligatoireSGG(obligatoireSgg);
		step.setType(stepType);
		step.setDocumentRouteId(routeDoc.getId());
		step.setTypeDestinataire(typeDestinataire);
		// création du document
		stepFournitureDoc = session.createDocument(step.getDocument());
		session.save();
		step = stepFournitureDoc.getAdapter(SolonEpgRouteStep.class);
		step.setValidated(session);
		step.save(session);
		step.setReady(session);
		session.orderBefore(new PathRef(parentPath), stepFournitureDoc.getName(), previousStepName);
		step.save(session);
		return step;
	}

	/**
	 * Contrôle pour éviter de créer des étapes sur des types d'actes non compatibles (exemple 'Pour avis CE')
	 * 
	 * @param nextstepDocument
	 */
	@Override
	public String isNextStepCompatibleWithTypeActe(STRouteStep nextstepDocument, CoreSession documentManager,
			String idDoc) throws ClientException {
		String error = "";
		// on regarde si l'étape qu'on veut créer est de type 'Pour avis CE'
		if (nextstepDocument.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE)) {
			// On regarde alors si le document a un type qui autorise les étapes pour avis CE
			IdRef docRef = new IdRef(idDoc);
			if (documentManager.exists(docRef)) {
				DocumentModel dossierDoc = documentManager.getDocument(docRef);
				Dossier dossier = dossierDoc.getAdapter(Dossier.class);
				final ActeService acteService = SolonEpgServiceLocator.getActeService();
				Boolean hasTypeActeSaisineRectificative = acteService.hasTypeActeSaisineRectificative(dossier
						.getTypeActe());
				if (!hasTypeActeSaisineRectificative) {
					error = "Une étape 'Pour avis CE' ne peut être appliquée à ce type d'acte";
				}
			}
		}
		return error;
	}
	
	@Override
	public DocumentRoute duplicateRouteModel(final CoreSession session, final DocumentModel routeToCopyDoc,
			final String ministere) throws ClientException {
		// Copie le modèle de feuille de route
		final STFeuilleRoute routeToCopy = routeToCopyDoc.getAdapter(STFeuilleRoute.class);
		String newTitle = routeToCopy.getName() + " (Copie)";
		String escapedTitle = newTitle.replace('/', '-');
		escapedTitle = escapedTitle.replace('\\', '-');
		final DocumentModel newFeuilleRouteDoc = session.copy(routeToCopyDoc.getRef(), routeToCopyDoc.getParentRef(),
				escapedTitle);

		final STFeuilleRoute newFeuilleRoute = newFeuilleRouteDoc.getAdapter(STFeuilleRoute.class);
		lockDocumentRoute(newFeuilleRoute, session);

		// Si le modèle copié était validé, invalide le nouveau modèle
		if (newFeuilleRoute.isValidated()) {
			invalidateRouteModel(newFeuilleRoute, session);
		}

		// Réinitialise l'état de la feuille de route
		newFeuilleRoute.setCreator(session.getPrincipal().getName());
		newFeuilleRoute.setDemandeValidation(false);
		newFeuilleRoute.setFeuilleRouteDefaut(false);
		if (StringUtils.isNotEmpty(ministere) && StringUtils.isEmpty(newFeuilleRoute.getMinistere())) {
			newFeuilleRoute.setMinistere(ministere);
			
			final OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();

			final EntiteNode ministereNode = (EntiteNode) organigrammeService.getOrganigrammeNodeById(ministere, OrganigrammeType.MINISTERE);
			final String NORMinistere = ministereNode.getNorMinistere();
			newTitle = NORMinistere + " - " + newTitle;
		}
		newFeuilleRoute.setTitle(newTitle);
		
		// Redonne le bon titre au modèle de feuille de route
		newFeuilleRouteDoc.setPropertyValue("dc:title", newTitle);

		session.saveDocument(newFeuilleRouteDoc);
		session.save();

		// Initialise les étapes de la nouvelle feuille de route
		initDocumentRouteStep(session, newFeuilleRouteDoc);

		if(!isSqueletteFeuilleRoute(routeToCopyDoc)) {
			
			// Si un ministère est fourni et que la feuille de route est non affecté
			unlockDocumentRoute(newFeuilleRoute, session);
		}

		return newFeuilleRoute;
	}	

	@Override
	public DocumentRoute validateSquelette(final DocumentRoute squelette, final CoreSession session)
			throws ClientException {
		if (!isLockedByCurrentUser(squelette, session)) {
			throw new DocumentRouteNotLockedException();
		}
		
		checkAndMakeAllStateStepDraft(session, squelette);

		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				final STFeuilleRoute route = session.getDocument(squelette.getDocument().getRef()).getAdapter(
						STFeuilleRoute.class);
				EventFirer.fireEvent(session, route, null, DocumentRoutingConstants.Events.beforeRouteValidated.name());

				// Valide la feuille de route
				route.setValidated(session);
				// Annule la demande de validation
				route.setDemandeValidation(false);
				session.saveDocument(route.getDocument());
				session.save();
				EventFirer.fireEvent(session, route, null, DocumentRoutingConstants.Events.afterRouteValidated.name());
			}
		}.runUnrestricted();
		return session.getDocument(squelette.getDocument().getRef()).getAdapter(DocumentRoute.class);
	}
	
	@Override
	public DocumentRoute invalidateSquelette(final DocumentRoute squelette, final CoreSession session)
			throws ClientException {
		if (!isLockedByCurrentUser(squelette, session)) {
			throw new DocumentRouteNotLockedException();
		}
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				EventFirer.fireEvent(session, squelette, null, SolonEpgEventConstant.BEFORE_INVALIDATE_SQUELETTE);
				squelette.followTransition(ElementLifeCycleTransistion.toDraft, session, false);
				EventFirer.fireEvent(session, squelette, null, SolonEpgEventConstant.AFTER_INVALIDATE_SQUELETTE);
			}
		}.runUnrestricted();

		return squelette;
	}
	
	@Override
	public SolonEpgFeuilleRoute createNewModelInstanceFromSquelette(SolonEpgFeuilleRoute squel, final DocumentModel location, 
			final String ministereId, final String bdcId, final String cdmId, final String scdmId, final String cpmId, CoreSession session) {
		LOGGER.info("Création d'un modèle pour le squelette : " + squel.getName());
		
		final FeuilleRouteModelService fdrmService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		final DocumentRoutingService docRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
		final STLockService lockService = STServiceLocator.getSTLockService();
		
		DocumentModel newModeleFDRDocumentModel = null;
		SolonEpgFeuilleRoute newMFDR = null;
		
		try {
			boolean defautFDRAlreadyExist = !checkFeuilleRouteParDefautExist(session, ministereId, squel.getTypeActe());
			
			newModeleFDRDocumentModel = session.createDocumentModel(location.getPathAsString(), squel.getName(), STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE);
			
			newMFDR = newModeleFDRDocumentModel.getAdapter(SolonEpgFeuilleRoute.class);
			String intitule = fdrmService.creeTitleModeleFDR(ministereId, null, squel.getTypeActe(), squel.getName());
			newMFDR.setTitle(intitule);
			newMFDR.setMinistere(ministereId);
			newMFDR.setTypeActe(squel.getTypeActe());
			newMFDR.setFeuilleRouteDefaut(defautFDRAlreadyExist);
			
			// Compteur
			final UIDGeneratorService uidGeneratorService = ServiceHelper.getUIDGeneratorService();
			UIDSequencer sequencer = uidGeneratorService.getSequencer();
			String numero = Integer.valueOf(sequencer.getNext("SOLON_EPG_FDR_MODEL") + 1).toString();
			newMFDR.setNumero(numero);
			
			newModeleFDRDocumentModel.setPropertyValue(DocumentRoutingConstants.DESCRIPTION_PROPERTY_NAME, squel.getDescription());
			
			newModeleFDRDocumentModel = session.createDocument(newModeleFDRDocumentModel);
			
			if (defautFDRAlreadyExist) {
				// Valide la feuille de route
				newMFDR = newModeleFDRDocumentModel.getAdapter(SolonEpgFeuilleRoute.class);
				docRoutingService.lockDocumentRoute(newMFDR, session);
				
				DocumentRoute documentRouteValidated = docRoutingService.validateRouteModel(newMFDR.getDocumentRoute(session), session);
				session.saveDocument(documentRouteValidated.getDocument());
				session.save();
				docRoutingService.unlockDocumentRoute(newMFDR, session);
			}
			
			List<DocumentModel> listSteps = docRoutingService.getOrderedRouteElement(squel.getDocument().getId(), session);
						
			lockService.lockDoc(session, newModeleFDRDocumentModel);
			
			this.createRouteStepHierarchyFromRouteStepSquelette(session, listSteps, bdcId, cdmId, scdmId, cpmId, newModeleFDRDocumentModel, newModeleFDRDocumentModel);
			
			lockService.unlockDoc(session, newModeleFDRDocumentModel);
			
		} catch (ClientException e) {
			LOGGER.error("Erreur lors de la duplication du squelette", e);
			try {
				if (newModeleFDRDocumentModel != null && lockService.isLockByCurrentUser(session, newModeleFDRDocumentModel)) {
					lockService.unlockDoc(session, newModeleFDRDocumentModel);
				}
			} catch (ClientException e1) {
				LOGGER.error("Erreur lors de la duplication du squelette", e1);
			}
		}
		return newMFDR;
	}
	
	/**
	 * Vérifie si une feuille de route par défaut existe déjà pour ce ministère et ce type d'étape <br/>
	 * ministèreId et typeActe sont obligatoire
	 * @return
	 * @throws ClientException 
	 */
	private boolean checkFeuilleRouteParDefautExist(CoreSession session, final String ministèreId, final String typeActe) throws ClientException {
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		
		final List<DocumentModel> lstModeleExistant = feuilleRouteModelService.getNonDeletedFdrModelFromMinistereAndDirection(session, ministèreId, null, false);
		
		if (lstModeleExistant != null && !lstModeleExistant.isEmpty()) {
			
			for (DocumentModel model : lstModeleExistant) {
				final SolonEpgFeuilleRoute solonEpgFeuilleRoute = model.getAdapter(SolonEpgFeuilleRoute.class);
				if (solonEpgFeuilleRoute.isFeuilleRouteDefaut() && typeActe.equals(solonEpgFeuilleRoute.getTypeActe())) {
					// la feuille de route est par défaut pour ce type d'acte
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Crée les étapes de feuille de route d'un modele à partir des étapes d'un squelette.
	 * La méthode est récursive afin de prendre en compte les conteneurs d'étape des étapes en parallele.
	 */
	private void createRouteStepHierarchyFromRouteStepSquelette(final CoreSession session, final List<DocumentModel> stepListToCopy, final String bdcId, final String cdmId,
			final String scdmId, final String cpmId, final DocumentModel parentDocument, final DocumentModel modelFDRParent) throws ClientException {
		for (DocumentModel stepDocModel : stepListToCopy) {
			if(!STConstant.STEP_FOLDER_DOCUMENT_TYPE.equals(stepDocModel.getType())) {
				SolonEpgRouteStep stepToCopy = stepDocModel.getAdapter(SolonEpgRouteStep.class);
				// Etape de feuille de route
				if (!VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(stepToCopy.getType())) { // si c'est celle pour initialisation, on ne la copie pas
					// Etape non imbriquée, cas classique
					DocumentModel newstp = this.createRouteStepFromRouteStepSquelette(session, stepDocModel, bdcId, cdmId, scdmId, cpmId, parentDocument);
					newstp = this.addDocumentRouteElementToRoute(session, modelFDRParent, parentDocument.getRef(), null, newstp);
				}
			} else {
				// Conteneur d'étape
				final STLockService lockService = STServiceLocator.getSTLockService();
				StepFolder stepFolder = stepDocModel.getAdapter(StepFolder.class);
				DocumentModel stepFolderDoc = null;
				if(STSchemaConstant.STEP_FOLDER_EXECUTION_PARALLEL_VALUE.equals(stepFolder.getExecution())) {
					// Conteneur parallele
					stepFolderDoc = session.createDocumentModel(STConstant.STEP_FOLDER_DOCUMENT_TYPE);
					StepFolder parallelStepFolder = stepFolderDoc.getAdapter(StepFolder.class);
					parallelStepFolder.setExecution(STSchemaConstant.STEP_FOLDER_EXECUTION_PARALLEL_VALUE);
					stepFolderDoc = this.addDocumentRouteElementToRoute(session,modelFDRParent, parentDocument.getRef(), null, stepFolderDoc);
				} else {
					// Conteneur serie
					stepFolderDoc = session.createDocumentModel(STConstant.STEP_FOLDER_DOCUMENT_TYPE);
					StepFolder serialStepFolder = stepFolderDoc.getAdapter(StepFolder.class);
					serialStepFolder.setExecution(STSchemaConstant.STEP_FOLDER_EXECUTION_SERIAL_VALUE);
					stepFolderDoc = this.addDocumentRouteElementToRoute(session,modelFDRParent, parentDocument.getRef(), null, stepFolderDoc);
				}
				
				final DocumentModelList children = this.getOrderedRouteElement(stepDocModel.getId(), session);
				lockService.lockDoc(session, stepFolderDoc);
				createRouteStepHierarchyFromRouteStepSquelette(session, children, bdcId, cdmId, scdmId, cpmId, stepFolderDoc, modelFDRParent);
				lockService.unlockDoc(session, stepFolderDoc);
			}
		}
	}
	
	/**
	 * FEV525
	 * Crée un nouveau RouteStep en copiant les valeurs d'un squelette de RouteStep passé en parametre,
	 */
	private DocumentModel createRouteStepFromRouteStepSquelette(final CoreSession session, final DocumentModel squeletteToCopy, final String bdcId, final String cdmId,
			final String scdmId, final String cpmId, final DocumentModel parentDocument) throws ClientException {
		
		DocumentModel newDocument = session.createDocumentModel(parentDocument.getPathAsString(), squeletteToCopy.getName(), STConstant.ROUTE_STEP_DOCUMENT_TYPE);
		newDocument.setPropertyValue(DocumentRoutingConstants.TITLE_PROPERTY_NAME, squeletteToCopy.getTitle());
		
		
		SolonEpgRouteStep routeStepDesired = newDocument.getAdapter(SolonEpgRouteStep.class);
		SolonEpgRouteStep squelEPG = squeletteToCopy.getAdapter(SolonEpgRouteStep.class);
		SqueletteStepTypeDestinataire destinataire = squelEPG.getTypeDestinataire();
		
		routeStepDesired.setType(squelEPG.getType());
		routeStepDesired.setAutomaticValidation(squelEPG.isAutomaticValidation());
		routeStepDesired.setObligatoireSGG(squelEPG.isObligatoireSGG());
		routeStepDesired.setObligatoireMinistere(squelEPG.isObligatoireMinistere());
		routeStepDesired.setDeadLine(squelEPG.getDeadLine());

		switch (destinataire) {
			case BUREAU_DU_CABINET : 
				routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + bdcId);
				break;
			case CHARGE_DE_MISSION : 
				routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + cdmId);
				break;
			case SECRETARIAT_CHARGE_DE_MISSION : 
				routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + scdmId);
				break;
			case CONSEILLER_PM : 
				routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + cpmId);
				break;
			case ORGANIGRAMME : 
				routeStepDesired.setDistributionMailboxId(squelEPG.getDistributionMailboxId());
				break;
			default : 
				routeStepDesired.setDistributionMailboxId(squelEPG.getDistributionMailboxId());
				break;
		}
		
		return newDocument;
	}
	
	@Override
	public DocumentModel duplicateRouteStep(final CoreSession session, final DocumentModel docToCopy) throws ClientException {
		if(!isSqueletteFeuilleRoute(docToCopy)) {
			return super.duplicateRouteStep(session, docToCopy);
		}
		
		// Cas de l'étape de squelette
		DocumentModel newDocument = session.createDocumentModel(
				persister.getOrCreateRootOfDocumentRouteInstanceStructure(session).getPathAsString(), docToCopy.getName() + "_copie",
				SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE);
		newDocument.setPropertyValue(DocumentRoutingConstants.TITLE_PROPERTY_NAME, docToCopy.getTitle());
		
		
		SolonEpgRouteStep routeStepDesired = newDocument.getAdapter(SolonEpgRouteStep.class);
		SolonEpgRouteStep routeStepToCopy = docToCopy.getAdapter(SolonEpgRouteStep.class);
		
		routeStepDesired.setType(routeStepToCopy.getType());
		routeStepDesired.setDistributionMailboxId(routeStepToCopy.getDistributionMailboxId());
		routeStepDesired.setAutomaticValidation(routeStepToCopy.isAutomaticValidation());
		routeStepDesired.setObligatoireSGG(routeStepToCopy.isObligatoireSGG());
		routeStepDesired.setObligatoireMinistere(routeStepToCopy.isObligatoireMinistere());
		routeStepDesired.setDeadLine(routeStepToCopy.getDeadLine());
		
		routeStepDesired.setTypeDestinataire(routeStepToCopy.getTypeDestinataire());
		
		// alreadyDuplicated, automaticValidated ignoré de manière volontaire
		
		return newDocument;
	}
	
	@Override
	public DocumentRoute duplicateFeuilleRoute(final CoreSession session, final DocumentModel parentDocument, 
			final DocumentModel docToCopy, String routeId) throws ClientException {
		
		SSFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getSSFeuilleRouteService();
		FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
		ActeService acteService = SolonEpgServiceLocator.getActeService();
		STLockService lockService = SolonEpgServiceLocator.getSTLockService();
		Dossier dossier = parentDocument.getAdapter(Dossier.class);
		String routeToCopyId = routeId;
		
		List<DocumentModel> routeStepsDoc = feuilleRouteService.findAllSteps(session, routeToCopyId);
		
		DocumentRoute newRoute = feuilleRouteModelService.getDefaultRouteGlobal(session);
		
		// Instancie la feuille de route
		final List<String> docIds = new ArrayList<String>();
		docIds.add(parentDocument.getId());
		final DocumentModel routeInstanceDoc = documentRoutingService.createNewInstance(session,
				newRoute.getDocument(), docIds);
		final STFeuilleRoute stRouteInstance = routeInstanceDoc.getAdapter(STFeuilleRoute.class);
		stRouteInstance.setTypeCreation(STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_INSTANCIATION);
		stRouteInstance.setTitle(dossier.getNumeroNor() + "_" + stRouteInstance.getTitle());
		session.save();
		
		DocumentModel newFeuilleRoute = stRouteInstance.getDocument();
		
		if (!isLockedByCurrentUser(routeInstanceDoc.getAdapter(DocumentRoute.class), session)) {
			lockService.lockDoc(session, newFeuilleRoute);
		}
		
		// Charge l'arborescence complete des documents à copier
		// On suppose que tous les éléments sont du meme arbre (pas de vérification, trop couteux)
		final DocumentRouteTreeElement sourceTree = getDocumentRouteTree(session, routeStepsDoc.get(0));

		// Marque les documents à coller et leurs parents
		final Set<String> documentToPasteIdSet = new HashSet<String>();
		for (final DocumentModel doc : routeStepsDoc) {
			STRouteStep route = doc.getAdapter(STRouteStep.class);
			if (!route.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION) && 
					!(acteService.isActeTexteNonPublie(dossier.getTypeActe()) && 
					FeuilleRouteServiceImpl.INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(route.getType()))) {
				documentToPasteIdSet.add(doc.getId());
			}
		}
		// on initialise la source comme étant a copier, contournement pour mantis. 
		sourceTree.setToPaste(true);
		markElementToPaste(sourceTree, documentToPasteIdSet);

		// Construit des nouveaux arbres avec uniquement les éléments à copier
		final List<DocumentRouteTreeElement> destTreeList = pruneDocumentRouteTree(sourceTree, newFeuilleRoute);

		// Duplique les documents du nouvel arbre récursivement
		for (final DocumentRouteTreeElement routeTree : destTreeList) {
			pasteRouteTreeIntoRoute(session, newFeuilleRoute, routeTree, newFeuilleRoute, null, false);
		}
		
		lockService.unlockDoc(session, newFeuilleRoute);
		
		return stRouteInstance;
	}
	
	@Override
	public DocumentModel addDocumentRouteElementToRoute(final CoreSession session,
			final DocumentModel documentRouteDoc, final DocumentRef parentDocumentRef, final String sourceName,
			DocumentModel routeElementDoc) throws ClientException {
		final STLockService lockService = STServiceLocator.getSTLockService();
		if (!lockService.isLockByCurrentUser(session, documentRouteDoc)) {
			throw new DocumentRouteNotLockedException();
		}

		// Renseigne l'UUID de la feuille de route dans l'étape (champ dénormalisé)
		String docName = null;
		final PathSegmentService pathSegmentService = STServiceLocator.getPathSegmentService();
		final DocumentModel parentDocument = session.getDocument(parentDocumentRef);
		if (STConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(routeElementDoc.getType())
				|| SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(routeElementDoc.getType())) {
			final STRouteStep routeStep = routeElementDoc.getAdapter(STRouteStep.class);
			routeStep.setDocumentRouteId(documentRouteDoc.getId());
			docName = pathSegmentService.generatePathSegment(routeElementDoc);
		} else if (STConstant.STEP_FOLDER_DOCUMENT_TYPE.equals(routeElementDoc.getType())) {
			final StepFolder stepFolder = routeElementDoc.getAdapter(StepFolder.class);
			if (stepFolder.isParallel()) {
				docName = DOCUMENT_NAME_PARALLELE;
			} else {
				docName = DOCUMENT_NAME_SERIE;
			}
		}
		
		
 		routeElementDoc.setPathInfo(parentDocument.getPathAsString(), docName);
		final String lifecycleState = parentDocument.getCurrentLifeCycleState().equals(
				DocumentRouteElement.ElementLifeCycleState.draft.name()) ? DocumentRouteElement.ElementLifeCycleState.draft
				.name() : DocumentRouteElement.ElementLifeCycleState.ready.name();
		routeElementDoc.putContextData(LifeCycleConstants.INITIAL_LIFECYCLE_STATE_OPTION_NAME, lifecycleState);

		routeElementDoc = session.createDocument(routeElementDoc);
		session.orderBefore(parentDocumentRef, routeElementDoc.getName(), sourceName);
		session.save();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Création dans le conteneur " + parentDocument.getName() + " de l'élément "
					+ routeElementDoc.getName() + " " + " avant l'élément " + sourceName);
		}

		return routeElementDoc;
	}

	@Override
	public boolean isRoutingTaskTypeValiderAllowed(SSPrincipal ssPrincipal, DossierLink dossierLink) {
		if (dossierLink == null) {
			return false;
		} else {
			String routingTaskType = dossierLink.getRoutingTaskType();
			if (routingTaskType == null) {
				return false;
			}
			Integer routingTaskTypeInt = Integer.parseInt(routingTaskType);
			Integer[] typeTaskAllowedArray = { 2, 3, 4, 6, 10, 11, 12, 13, 14, 15, 16 };
			List<Integer> typeTaskAllowedList = Arrays.asList(typeTaskAllowedArray);
			final List<String> groups = ssPrincipal.getGroups();
			return typeTaskAllowedList.contains(routingTaskTypeInt)
					|| (Integer.valueOf(5).equals(routingTaskTypeInt)
							&& groups.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_ATTRIBUTION_SGG_EXECUTOR))
					|| (Integer.valueOf(7).equals(routingTaskTypeInt)
							&& groups.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_SIGNATURE_EXECUTOR))
					|| (Integer.valueOf(8).equals(routingTaskTypeInt)
							&& groups.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_CONTRESEING_EXECUTOR));

		}
	}
}
