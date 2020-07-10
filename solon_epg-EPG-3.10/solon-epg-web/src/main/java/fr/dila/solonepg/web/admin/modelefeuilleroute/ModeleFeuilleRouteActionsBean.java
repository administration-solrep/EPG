package fr.dila.solonepg.web.admin.modelefeuilleroute;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.model.NoSuchDocumentException;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;
import org.nuxeo.ecm.platform.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.uidgen.service.ServiceHelper;
import org.nuxeo.ecm.platform.uidgen.service.UIDGeneratorService;
import org.nuxeo.ecm.webapp.helpers.EventNames;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.DocumentRouteElement;
import fr.dila.ecm.platform.routing.api.DocumentRouteElement.ElementLifeCycleState;
import fr.dila.ecm.platform.routing.api.exception.DocumentRouteAlredayLockedException;
import fr.dila.ecm.platform.routing.api.exception.DocumentRouteNotLockedException;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.criteria.FeuilleRouteCriteria;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.dao.FeuilleRouteDao;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.NavigationWebActionsBean;
import fr.dila.solonepg.web.feuilleroute.DocumentRoutingActionsBean;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STContentViewConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.PropertyUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * Actions permettant de gérer un modèle de feuille de route dans le SOLON EPG.
 * 
 * @author jtremeaux
 */
@Name("modeleFeuilleRouteActions")
@Scope(CONVERSATION)
@Install(precedence = Install.FRAMEWORK + 1)
public class ModeleFeuilleRouteActionsBean extends fr.dila.ss.web.feuilleroute.ModeleFeuilleRouteActionsBean implements
		Serializable {

	/**
	 * Serial UID.
	 */
	private static final long			serialVersionUID					= 5195004950574843681L;

	private static final Log			log									= LogFactory
																					.getLog(ModeleFeuilleRouteActionsBean.class);

	private static final Set<String>	INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB	= Collections
																					.unmodifiableSet(initIncompatibleSteps());

	private static Set<String> initIncompatibleSteps() {
		Set<String> incompatibleSteps = new HashSet<String>();
		incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE);
		incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO);
		incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO);
		return incompatibleSteps;
	}

	@In(create = true, required = false)
	protected transient ContentViewActions			contentViewActions;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean	navigationWebActions;
	
    @In(required = false, create = true)
    protected transient DocumentRoutingActionsBean routingActions;
	

	private String intituleLibre = null;
	private String intituleFixe = null;
	private String idModele = null;
	
	private boolean intituleLibreFieldValidationMsg;
	
	private String ministereId;
	private String bureauCabinetId;
	private String chargeMissionId;
	private String secretariatChargeMissionId;
	private String conseillerPmId;
	private String resultMsg;
	private String infoMsg;
	@In(required = true, create = true)
	protected AdministrationActionsBean		administrationActions;

	/**
	 * indique si les modeles sans ministere valide par le sgg doivent etre ajoute a la liste des modeles de feuille de
	 * route candidate pour la substitution
	 */
	protected boolean								includeSggModelForSubstitution	= true;

	/**
	 * Retourne vrai si l'utilisateur est sur la liste d'administration des modèles de feuilles de route.
	 * 
	 * @return Condition
	 */
	public boolean isInAdministrationModeleFeuilleRoute() {
		String currentContentViewName = contentViewActions.getCurrentContentView().getName();
		return STContentViewConstant.FEUILLE_ROUTE_MODEL_FOLDER_CONTENT_VIEW.equals(currentContentViewName);
	}
	
	/**
	 * Retourne vrai si l'utilisateur est sur la liste d'administration des squelettes de feuilles de route.
	 * 
	 * @return Condition
	 */
	public boolean isInAdministrationSqueletteFeuilleRoute() {
		String currentContentViewName = contentViewActions.getCurrentContentView().getName();
		return SolonEpgContentViewConstant.FEUILLE_ROUTE_SQUELETTE_FOLDER_CONTENT_VIEW.equals(currentContentViewName);
	}

	/**
	 * Retourne vrai si le modèle chargé est le modèle de feuille de route par défaut global.
	 * 
	 * @return Modèle de feuille de route par défaut global
	 */
	public boolean isFeuilleRouteDefautGlobal() {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		SolonEpgFeuilleRoute route = currentDocument.getAdapter(SolonEpgFeuilleRoute.class);

		return route.isFeuilleRouteDefautGlobal();
	}

	@Override
	public boolean canUserDeleteRoute(DocumentModel doc) throws ClientException {
		// La feuille de route par défaut globale ne peut pas être supprimée
		SolonEpgFeuilleRoute route = doc.getAdapter(SolonEpgFeuilleRoute.class);
		if (route.isFeuilleRouteDefautGlobal()) {
			facesMessages.add(StatusMessage.Severity.INFO, "Vous n'avez pas les droits nécessaires à la suppression");
			return false;
		}
		if (!super.canUserDeleteRoute(doc)) {
			facesMessages.add(StatusMessage.Severity.INFO, "Vous n'avez pas les droits nécessaires à la suppression");
			return false;
		} else {
			return true;
		}
	}

	public String duplicateRouteModel(String docId) {
		try {
			DocumentModel doc = documentManager.getDocument(new IdRef(docId));
			return duplicateRouteModel(doc);
		} catch (ClientException e) {
			if (ExceptionUtils.getRootCause(e) instanceof NoSuchDocumentException) {
				log.warn(e);

				// le document a été supprimé par un autre utilisateur ou l'utilisateur n'a plus les droits pour voir le
				// document : on le signale à l'utilisateur sans renvoyer une exception
				String errorMessage = resourcesAccessor.getMessages().get(STConstant.NO_SUCH_DOCUMENT_ERROR_MSG);
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			} else {
				log.error(e);
				facesMessages.add(StatusMessage.Severity.INFO, e.getMessage());
			}
			return null;
		}
	}

	@Override
	public String duplicateRouteModel(DocumentModel doc) throws ClientException {
		final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();

		final List<String> groups = ssPrincipal.getGroups();
		boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);
		String ministere = null;
		if (!isAdminFonctionnel) {
			// Affecte la nouvelle feuille de route au premier ministère de cet administrateur ministériel
			Set<String> ministereSet = ssPrincipal.getMinistereIdSet();
			if (ministereSet == null || ministereSet.size() <= 0) {
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
			String numero = Integer.valueOf(sequencer.getNext("SOLON_EPG_FDR_MODEL") + 1).toString();
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
			logActionModeleFdr(feuilleRoute, SolonEpgEventConstant.DUPLICATION_MODELE_FDR_EVENT);

			return documentActions.editDocument();
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
	* FEV525
	* Les intitules sont de la forme '*trigramme_ministère* - *lettre_direction* - *type_acte* - intitulé libre'
	* Decoupe un intitule en 2 parties, l'intitule fixe (XXX - X - xxxxxx) et l'intitule libre.
	* 
	* @return Une table de 2 String. [0] Contient la partie fixe et [1] contient la partie libre.
	* @throws ClientException 
	*/
	private String[] splitIntitule(String intitule, String ministereID, String directionID, String typeActeId) throws ClientException {
		String[] splited = new String[] {"", ""};

		if (intitule == null || intitule.isEmpty()) { 
			return splited;
		}

		String fixe = null;
		String libre = null;

		if (typeActeId != null && StringUtils.isNotBlank(typeActeId)) {
			// le type d'acte est renseigné. 
			// Tout ce qui est avant lui dans l'intitulé est la partie fixe, ce qui est après est la partie libre
			String typeActeLabel = null;

			//on récupère les vocabulaires du directory
			final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
			final List<String> listId = new ArrayList<String>(); 
			listId.add(typeActeId);

			final List<VocabularyEntry> voc = vocService.getVocabularyEntryListFromDirectory(VocabularyConstants.TYPE_ACTE_VOCABULARY, VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA, listId);
			final Iterator<VocabularyEntry> itActes = voc.iterator();
			
			while(itActes.hasNext() && typeActeLabel==null ) {
				VocabularyEntry acte = itActes.next();
				if (typeActeId.equals(acte.getId())) {
					typeActeLabel = acte.getLabel();
				}
			}

			if (typeActeLabel==null) {
				log.warn("Non récupération du label de l'acte " + typeActeId 
						+ "Lors de la construction du titre du modèle de feuille de route.");
			}

			fixe = StringUtils.substringBefore(intitule, typeActeLabel) + typeActeLabel;
			libre = StringUtils.substringAfter(intitule, typeActeLabel);
			libre = StringUtils.substring(libre,3); // enleve le " - "

		} else {
			if (StringUtils.isNotBlank(directionID)) {
				// Si la direction est renseignée, le ministère l'est aussi obligatoirement
				// la chaine de début est donc du style "XXX - X - " (10 chars)
				fixe = intitule.substring(0, 7);
				libre = intitule.substring(10);
				
			} else {
				if (StringUtils.isNotBlank(ministereID)) {
					// seul le trigramme du ministère est renseigné
					// la chaine de début est donc du style "XXX - " (6 chars)
					fixe = intitule.substring(0, 3);
					libre = intitule.substring(6);
				}
				else {
					// Rien n'est renseigné Tout va dans le titre libre
					fixe = "";
					libre = intitule;
				}
			}
		}
		return new String[] {fixe, libre};
	}

	@Override
	public String saveDocument() throws ClientException {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument();
		SolonEpgFeuilleRoute route = changeableDocument.getAdapter(SolonEpgFeuilleRoute.class);
		
		if (StringUtils.isEmpty(this.intituleLibre)) {
			this.intituleLibreFieldValidationMsg = true;
			return "error";
		} else {
			this.intituleLibreFieldValidationMsg = false;
		}
		
		// Si le ministère n'est pas renseigné, la direction doit être non renseignée
		if (route.getMinistere() == null || StringUtils.isBlank(route.getMinistere())) {
			if (route.getDirection() != null && StringUtils.isNotBlank(route.getDirection())) {
				String errorMessage = resourcesAccessor.getMessages().get(
						"epg.feuilleRoute.action.save.direction.need.ministere");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return "error";
			}
		}

		// Il faut que la direction appartienne au ministère
		if (route.getMinistere() != null && StringUtils.isNotBlank(route.getMinistere())
				&& route.getDirection() != null && StringUtils.isNotBlank(route.getDirection())) {
			final OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();
			UniteStructurelleNode directionNode = (UniteStructurelleNode) organigrammeService.getOrganigrammeNodeById(route.getDirection(), OrganigrammeType.DIRECTION);
			final String NORDirection = directionNode.getNorDirectionForMinistereId(route.getMinistere());
			
			if (NORDirection == null || StringUtils.isBlank(NORDirection)) {
				String errorMessage = resourcesAccessor.getMessages().get(
						"epg.feuilleRoute.action.save.direction.not.in.ministere");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				
				return "error";
			}
		}

		// Nettoyage des données en entrée
		if (StringUtils.isBlank(route.getMinistere())) {
			route.setMinistere(null);
		}
		if (StringUtils.isBlank(route.getDirection())) {
			route.setDirection(null);
		}
		if (StringUtils.isBlank(route.getTypeActe())) {
			route.setTypeActe(null);
		}

		final FeuilleRouteModelService fdrmService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		final String intitule = fdrmService.creeTitleModeleFDR(route.getMinistere(), route.getDirection(), route.getTypeActe(), intituleLibre);
		route.setTitle(intitule);

		// Vérifie l'unicité de l'intitulé de modèle de feuille de route par ministère
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		if (!feuilleRouteModelService.isIntituleUnique(documentManager, route)) {
			String errorMessage = resourcesAccessor.getMessages().get("st.feuilleRoute.action.unicite.intitule.error");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return STViewConstant.ERROR_VIEW;
		}

		// Vérifie l'unicité de la feuille de route par défaut
		if (route.isFeuilleRouteDefaut()) {
			FeuilleRouteCriteria criteria = new FeuilleRouteCriteria();
			criteria.setFeuilleRouteDefaut(true);
			if (StringUtils.isNotBlank(route.getMinistere())) {
				criteria.setMinistere(route.getMinistere());
			} else {
				criteria.setMinistereNull(true);
			}
			if (StringUtils.isNotBlank(route.getDirection())) {
				criteria.setDirection(route.getDirection());
			} else {
				criteria.setDirectionNull(true);
			}
			if (StringUtils.isNotBlank(route.getTypeActe())) {
				criteria.setTypeActe(route.getTypeActe());
			} else {
				criteria.setTypeActeNull(true);
			}
			FeuilleRouteDao feuilleRouteDao = new FeuilleRouteDao(documentManager, criteria);
			List<DocumentModel> feuilleRouteList = feuilleRouteDao.list(documentManager);
			if (!feuilleRouteList.isEmpty()) {
				facesMessages
						.add(StatusMessage.Severity.WARN,
								resourcesAccessor.getMessages().get(
										"epg.feuilleRoute.action.save.duplicateFeuilleRoute.error"));
				return STViewConstant.ERROR_VIEW;
			}
		}

		// Vérifie que l'on n'insère pas deux modèles de feuille de route par défaut globalement
		if (route.isFeuilleRouteDefautGlobal()) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages()
							.get("st.feuilleRoute.action.save.duplicateFeuilleRouteGlobal.error"));
			route.setFeuilleRouteDefaut(false);
			return STViewConstant.ERROR_VIEW;
		}

		// Compteur
		final UIDGeneratorService uidGeneratorService = ServiceHelper.getUIDGeneratorService();
		UIDSequencer sequencer = uidGeneratorService.getSequencer();
		String numero = Integer.valueOf(sequencer.getNext("SOLON_EPG_FDR_MODEL") + 1).toString();
		route.setNumero(numero);

		this.idModele = null; // permet de recharger les intitules
		return documentActions.saveDocument();
	}

	@Override
	public String updateDocument() throws ClientException {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument();
		SolonEpgFeuilleRoute route = changeableDocument.getAdapter(SolonEpgFeuilleRoute.class);

		if (StringUtils.isEmpty(this.intituleLibre)) {
			this.intituleLibreFieldValidationMsg = true;
			return "error";
		} else {
			this.intituleLibreFieldValidationMsg = false;
		}
		
		// Vérifie si le document est verrouillé par l'utilisateur en cours
		if (!stLockActions.isDocumentLockedByCurrentUser(changeableDocument)) {
			String errorMessage = resourcesAccessor.getMessages().get("st.lock.action.lockLost.error");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}
		
		// Vérifie la compatibilité des étapes avec le type d'acte
		SSFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getSSFeuilleRouteService();
		List<DocumentModel> stepsDoc = feuilleRouteService.findAllSteps(documentManager, changeableDocument.getId());
		if (!routingActions.checkValiditySteps(route, stepsDoc)) {
			facesMessages.clear();
			String errorMessage = resourcesAccessor.getMessages().get("feedback.feuilleRoute.squelette.typeActe.incompatible");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		// Si le ministère n'est pas renseigné, la direction doit être non renseignée
		if (route.getMinistere() == null || StringUtils.isBlank(route.getMinistere())) {
			if (route.getDirection() != null && StringUtils.isNotBlank(route.getDirection())) {
				String errorMessage = resourcesAccessor.getMessages().get(
						"epg.feuilleRoute.action.save.direction.need.ministere");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				
				return "error";
			}
		}
		
		// Il faut que la direction appartienne au ministère
		if (route.getMinistere() != null && StringUtils.isNotBlank(route.getMinistere())
				&& route.getDirection() != null && StringUtils.isNotBlank(route.getDirection())) {
			final OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();
			UniteStructurelleNode directionNode = (UniteStructurelleNode) organigrammeService.getOrganigrammeNodeById(route.getDirection(), OrganigrammeType.DIRECTION);
			final String NORDirection = directionNode.getNorDirectionForMinistereId(route.getMinistere());
			
			if (NORDirection == null || StringUtils.isBlank(NORDirection)) {
				String errorMessage = resourcesAccessor.getMessages().get(
						"epg.feuilleRoute.action.save.direction.not.in.ministere");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				
				return "error";
			}
		}

		// Nettoyage des données en entrée
		if (StringUtils.isBlank(route.getMinistere())) {
			route.setMinistere(null);
		}
		if (StringUtils.isBlank(route.getDirection())) {
			route.setDirection(null);
		}
		if (StringUtils.isBlank(route.getTypeActe())) {
			route.setTypeActe(null);
		}

		final FeuilleRouteModelService fdrmService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		final String intitule = fdrmService.creeTitleModeleFDR(route.getMinistere(), route.getDirection(), route.getTypeActe(), intituleLibre);
		route.setTitle(intitule);

		// Vérifie l'unicité de l'intitulé de modèle de feuille de route par ministère
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		if (!feuilleRouteModelService.isIntituleUnique(documentManager, route)) {
			String errorMessage = resourcesAccessor.getMessages().get("st.feuilleRoute.action.unicite.intitule.error");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		// Vérifie l'unicité de la feuille de route
		if (route.isFeuilleRouteDefaut()) {
			FeuilleRouteCriteria criteria = new FeuilleRouteCriteria();
			criteria.setFeuilleRouteDefaut(true);
			if (StringUtils.isNotBlank(route.getMinistere())) {
				criteria.setMinistere(route.getMinistere());
			} else {
				criteria.setMinistereNull(true);
			}
			if (StringUtils.isNotBlank(route.getDirection())) {
				criteria.setDirection(route.getDirection());
			} else {
				criteria.setDirectionNull(true);
			}
			if (StringUtils.isNotBlank(route.getTypeActe())) {
				criteria.setTypeActe(route.getTypeActe());
			} else {
				criteria.setTypeActeNull(true);
			}
			FeuilleRouteDao feuilleRouteDao = new FeuilleRouteDao(documentManager, criteria);
			List<DocumentModel> feuilleRouteList = feuilleRouteDao.list(documentManager);
			if (!feuilleRouteList.isEmpty() && !changeableDocument.getId().equals(feuilleRouteList.get(0).getId())) {
				facesMessages
						.add(StatusMessage.Severity.WARN,
								resourcesAccessor.getMessages().get(
										"epg.feuilleRoute.action.save.duplicateFeuilleRoute.error"));
				return "error";
			}
		}

		// Vérifie que l'on n'insère pas deux modèles de feuille de route par défaut globalement
		if (route.isFeuilleRouteDefautGlobal()) {
			DocumentModel oldDoc = documentManager.getDocument(new IdRef(changeableDocument.getId()));
			SolonEpgFeuilleRoute oldRoute = oldDoc.getAdapter(SolonEpgFeuilleRoute.class);
			if (!oldRoute.isFeuilleRouteDefaut()) {
				facesMessages.add(
						StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get(
								"st.feuilleRoute.action.save.duplicateFeuilleRouteGlobal.error"));
				return null;
			}
		}

		// on logge l'action de modification du modèle de feuille de route
		logActionModeleFdr(route, SolonEpgEventConstant.UPDATE_MODELE_FDR_EVENT);

		this.idModele = null; // permet de recharger les intitules
		
		documentActions.updateDocument();
		return routingWebActions.getFeuilleRouteView();
	}
	
	/**
	 * on logge l'action du modèle de feuille de route.
	 * 
	 * @param route
	 * @param event
	 * @throws ClientException
	 */
	protected void logActionModeleFdr(SolonEpgFeuilleRoute route, String event) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		String comment = "Action sur le modèle de feuille de route " + route.getTitle();
		if (SolonEpgEventConstant.UPDATE_MODELE_FDR_EVENT.equals(event)) {
			comment = "Modification du modèle de feuille de route [" + route.getTitle() + "]";
		} else if (SolonEpgEventConstant.DUPLICATION_MODELE_FDR_EVENT.equals(event)) {
			comment = "Duplication du modèle de feuille de route [" + route.getTitle() + "]";
		}
		journalService.journaliserActionAdministration(documentManager, event, comment);
	}

	/**
	 * Retourne le prédicat permettant de filter les modèles de feuille compatible avec un dossier
	 * 
	 * @param dossier
	 *            : le dossier sur lequel on veut subsituer la feuille de route
	 * @return Prédicta NXQL
	 * @throws ClientException
	 */
	public String getForMassSubstitutionCriteria() throws ClientException {

		Set<String> ministeres = ssPrincipal.getMinistereIdSet();
		StringBuilder strBuilder = new StringBuilder("AND (");
		// if (includeSggModelForSubstitution) {
		strBuilder.append("fdr:ministere IN ('");
		strBuilder.append(StringUtils.join(ministeres, "','"));
		strBuilder.append("') ");
		strBuilder.append(" OR (");
		strBuilder.append("fdr:ministere IS NULL ");
		strBuilder.append("AND fdr:direction IS NULL ");
		strBuilder.append("AND fdr:typeActe IS NULL ");
		strBuilder.append("AND fdr:feuilleRouteDefaut != 1 ");
		strBuilder.append(")");
		// }

		strBuilder.append(")");
		return strBuilder.toString();
	}

	/**
	 * Retourne le prédicat permettant de filter les modèles de feuille compatible avec un dossier
	 * 
	 * @param dossier
	 *            : le dossier sur lequel on veut subsituer la feuille de route
	 * @return Prédicta NXQL
	 * @throws ClientException
	 */
	public String getForSubstitutionCriteria(DocumentModel dossierDocModel) throws ClientException {
		Dossier dossier = dossierDocModel.getAdapter(Dossier.class);
		StringBuilder strBuilder = new StringBuilder("AND (");
		if (includeSggModelForSubstitution) {
			strBuilder.append("fdr:ministere = '");
			strBuilder.append(dossier.getMinistereAttache());
			strBuilder.append("' OR (");
			strBuilder.append("fdr:ministere IS NULL ");
			strBuilder.append("AND fdr:direction IS NULL ");
			strBuilder.append("AND fdr:typeActe IS NULL ");
			strBuilder.append("AND fdr:feuilleRouteDefaut != 1 ");
			strBuilder.append(")");
		} else {
			strBuilder.append("(fdr:ministere = '");
			strBuilder.append(dossier.getMinistereAttache());
			strBuilder.append("' AND fdr:direction = '");
			strBuilder.append(dossier.getDirectionAttache());
			strBuilder.append("' AND fdr:typeActe = '");
			strBuilder.append(dossier.getTypeActe());
			strBuilder.append("')");
			// ajout du modèle par défaut commun à tout les ministères
			strBuilder.append(" OR (");
			strBuilder.append("fdr:ministere IS NULL ");
			strBuilder.append("AND fdr:direction IS NULL ");
			strBuilder.append("AND fdr:typeActe IS NULL ");
			strBuilder.append("AND fdr:feuilleRouteDefaut = 1 ");
			strBuilder.append(")");
		}

		strBuilder.append(")");
		return strBuilder.toString();
	}

	public boolean getIncludeSggModelForSubstitution() {
		return includeSggModelForSubstitution;
	}

	public void setIncludeSggModelForSubstitution(boolean includeSggModelForSubstitution) {
		this.includeSggModelForSubstitution = includeSggModelForSubstitution;
	}

	public Boolean isObligatoireSggUpdater() {
		final List<String> groups = ssPrincipal.getGroups();

		boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);
		boolean isEtapeObligatoirUpdater = groups
				.contains(SolonEpgBaseFunctionConstant.FEUILLE_ROUTE_OBLIGATOIRE_SGG_UPDATER);
		return isAdminFonctionnel || isEtapeObligatoirUpdater;
	}

	public String deleteDocument(String docId) {
		try {
			DocumentModel doc = documentManager.getDocument(new IdRef(docId));
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

	/**
	 * Surcharge de validateRouteModel pour gérer le cas du type d'acte texte non publié
	 */
	@Override
	public String validateRouteModel() throws ClientException {
		DocumentModel routeDoc = navigationContext.getCurrentDocument();

		SolonEpgFeuilleRoute fdr = routeDoc.getAdapter(SolonEpgFeuilleRoute.class);
		final DocumentRoutingService docRoutingService = SSServiceLocator.getDocumentRoutingService();

		List<DocumentModel> stepsDoc = docRoutingService.getOrderedRouteElement(routeDoc.getId(), documentManager);

		if (stepsDoc.size() > 0) {
			String errorCompatibility = SolonEpgServiceLocator.getDocumentRoutingService()
					.isNextPostsCompatibleWithNextStep(documentManager, stepsDoc.subList(1, stepsDoc.size()));
			if (StringUtil.isNotEmpty(errorCompatibility)) {
				facesMessages.add(StatusMessage.Severity.WARN, errorCompatibility);
				return STViewConstant.ERROR_VIEW;
			}
		}

		if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(fdr.getTypeActe())) {
			for (DocumentModel stepDoc : stepsDoc) {
				SolonEpgRouteStep step = stepDoc.getAdapter(SolonEpgRouteStep.class);
				// On vérifie step != null pour les stepFolder
				if (step != null) {
					String stepType = step.getType();
					if (INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(stepType)) {
						facesMessages.add(StatusMessage.Severity.WARN,
								resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.cannotValidate"));
						facesMessages.add(StatusMessage.Severity.WARN,
								resourcesAccessor.getMessages().get("feedback.solonepg.feuilleRoute.acteIncompatible"));
						return STViewConstant.ERROR_VIEW;
					}
				}
			}
		}

		return super.validateRouteModel();
	}

	public String getIntituleLibre() throws ClientException {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument(); // a une valeur (id) uniquement lors de la modification
		//DocumentModel currentDocument = navigationContext.getCurrentDocument(); // a une valeur lors de la création
		
		if (changeableDocument.getId()==null) {
			// le document n'existe pas encore. Premiere création ou erreur de création.
			this.intituleLibre = null;
			return null;
		}
		
		return this.intituleLibre; // recalculé dans getIntituleFixe
	}

	public void setIntituleLibre(String intituleLibre) {
		this.intituleLibre = intituleLibre;
	}

	public String getIntituleFixe() throws ClientException {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument(); // a une valeur (id) uniquement lors de la modification
		
		if (changeableDocument.getId()==null) {
			// le document n'existe pas encore
			this.intituleFixe = null;
			return null;
		}
		
		if (changeableDocument.getId()!=null && !changeableDocument.getId().equals(this.idModele)) {
			// il y a un document de chargé et il est différent de l'ancien. Donc il faut recalcule les intitules
			
			SolonEpgFeuilleRoute route = changeableDocument.getAdapter(SolonEpgFeuilleRoute.class);
			
			final String[] intitules =  splitIntitule(route.getTitle(), route.getMinistere(), route.getDirection(), route.getTypeActe());
			this.intituleFixe = intitules[0];
			this.intituleLibre = intitules[1];
			this.idModele = changeableDocument.getId();
			
			return intituleFixe;
		} else {
			return this.intituleFixe;
		}
		//}
	}

	/**
	 * Retourne vrai si l'on doit afficher une erreur de validation pour le champ
	 * 
	 * @param property
	 * @return
	 */
	public boolean displayFieldValidationMessage() {
		return this.intituleLibreFieldValidationMsg;
	}
	
	/**
	 * Retourne vrai si l'utilisateur courant peut valider le squelette de
	 * feuille de route.
	 * 
	 * @return Condition
	 * @throws ClientException
	 */
	public boolean canValidateSquelette() throws ClientException {
		final STFeuilleRoute feuilleRoute = getRelatedRoute();
		DocumentModel doc = feuilleRoute.getDocument();

		// Les squelettes verrouillés par un autre utilisateur sont non modifiables
		if (!stLockActions.isDocumentLockedByCurrentUser(doc)) {
			return false;
		}

		final String lifeCycleState = doc.getCurrentLifeCycleState();
		return ElementLifeCycleState.draft.name().equals(lifeCycleState);
	}

	/**
	 * Retourne vrai si l'utilisateur courant peut invalider le squelette de
	 * feuille de route.
	 * 
	 * @return Condition
	 * @throws ClientException
	 */
	public boolean canInvalidateSquelette() throws ClientException {
		final STFeuilleRoute feuilleRoute = getRelatedRoute();

		// On peut invalider un squelette de feuille de route uniquement à l'état validé
		final String lifeCycleState = feuilleRoute.getDocument().getCurrentLifeCycleState();
		return ElementLifeCycleState.validated.name().equals(lifeCycleState);
	}

	public String validateSquelette() throws ClientException {
		DocumentModel routeDoc = navigationContext.getCurrentDocument();
		DocumentRoute currentRouteModel = getRelatedRoute();

		// Vérifie si le document est verrouillé par l'utilisateur en cours
		if (!stLockActions.isDocumentLockedByCurrentUser(routeDoc)) {
			String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		final fr.dila.solonepg.api.service.DocumentRoutingService documentRoutingService = SolonEpgServiceLocator
				.getDocumentRoutingService();

		// Vérifie l'unicité du squelette pour ce type d'acte
		SolonEpgFeuilleRoute fdr = routeDoc.getAdapter(SolonEpgFeuilleRoute.class);
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		if (feuilleRouteModelService.existsSqueletteWithTypeActe(documentManager, fdr.getTypeActe())) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.feuilleRoute.squelette.validate.error.typeNotUnique");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return STViewConstant.ERROR_VIEW;
		}

		try {
			currentRouteModel = documentRoutingService.validateSquelette(currentRouteModel, documentManager);
		} catch (DocumentRouteNotLockedException e) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.casemanagement.document.route.not.locked");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		try {
			documentRoutingService.unlockDocumentRoute(currentRouteModel, documentManager);
		} catch (DocumentRouteAlredayLockedException e) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.casemanagement.document.route.already.locked");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		Events.instance().raiseEvent(EventNames.DOCUMENT_CHILDREN_CHANGED, currentRouteModel.getDocument());

		// Affiche un message d'information
		String infoMessage = resourcesAccessor.getMessages().get("feuilleRoute.squelette.action.validated.success");
		facesMessages.add(StatusMessage.Severity.INFO, infoMessage);

		return null;
	}

	public String updateSquelette() throws ClientException {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument();
		SolonEpgFeuilleRoute route = changeableDocument.getAdapter(SolonEpgFeuilleRoute.class);

		// Vérifie si le document est verrouillé par l'utilisateur en cours
		if (!stLockActions.isDocumentLockedByCurrentUser(changeableDocument)) {
			String errorMessage = resourcesAccessor.getMessages().get("st.lock.action.lockLost.error");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}
		
		// Vérifie la compatibilité des étapes avec le type d'acte
		SSFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getSSFeuilleRouteService();
		List<DocumentModel> stepsDoc = feuilleRouteService.findAllSteps(documentManager, changeableDocument.getId());
		if (!routingActions.checkValiditySteps(route, stepsDoc)) {
			facesMessages.clear();
			String errorMessage = resourcesAccessor.getMessages().get("feedback.feuilleRoute.squelette.typeActe.incompatible");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		// on logge l'action de modification du squelette de feuille de route
		logActionSqueletteFdr(route, SolonEpgEventConstant.UPDATE_SQUELETTE_FDR_EVENT);

		documentActions.updateDocument();
		return routingWebActions.getFeuilleRouteView();
	}

	/**
	 * on logge l'action du squelette de feuille de route.
	 * 
	 * @param squelette
	 * @param event
	 * @throws ClientException
	 */
	protected void logActionSqueletteFdr(SolonEpgFeuilleRoute squelette, String event) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		String comment = "Action sur le squelette de feuille de route " + squelette.getTitle();
		if (SolonEpgEventConstant.UPDATE_SQUELETTE_FDR_EVENT.equals(event)) {
			comment = "Modification du squelette de feuille de route [" + squelette.getTitle() + "]";
		} else if (SolonEpgEventConstant.DUPLICATION_SQUELETTE_FDR_EVENT.equals(event)) {
			comment = "Duplication du squelette de feuille de route [" + squelette.getTitle() + "]";
		}
		journalService.journaliserActionAdministration(documentManager, event, comment);
	}

	/**
	 * Invalide le squelette de feuille de route en cours.
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public String invalidateSquelette() throws ClientException {
		DocumentRoute currentRouteModel = getRelatedRoute();

		final fr.dila.solonepg.api.service.DocumentRoutingService documentRoutingService = SolonEpgServiceLocator
				.getDocumentRoutingService();
		boolean hasError = false;
		try {
			documentRoutingService.lockDocumentRoute(currentRouteModel, documentManager);
		} catch (DocumentRouteAlredayLockedException e) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.casemanagement.document.route.squelette.already.locked");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			hasError = true;
		}
		try {
			documentRoutingService.invalidateSquelette(currentRouteModel, documentManager);
		} catch (DocumentRouteNotLockedException e) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.casemanagement.document.route.squelette.not.locked");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			hasError = true;
		}
		
		if (!hasError) {
			Events.instance().raiseEvent(EventNames.DOCUMENT_CHILDREN_CHANGED, currentRouteModel.getDocument());
			// on verrouille le squelette
			verrouillerSquelette();
			// Affiche un message d'information
			String infoMessage = resourcesAccessor.getMessages()
					.get("feuilleRoute.squelette.action.invalidated.success");
			facesMessages.add(StatusMessage.Severity.INFO, infoMessage);
		}

		return null;
	}

	public String saveSquelette() throws ClientException {
		String retour = documentActions.saveDocument();

		DocumentModel changeableDocument = navigationContext.getChangeableDocument();
		DocumentRouteElement docRouteElement = changeableDocument.getAdapter(DocumentRouteElement.class);
		DocumentRoute route = docRouteElement.getDocumentRoute(documentManager);

		final fr.dila.solonepg.api.service.DocumentRoutingService documentRoutingService = SolonEpgServiceLocator
				.getDocumentRoutingService();
		try {
			documentRoutingService.lockDocumentRoute(route, documentManager);
		} catch (DocumentRouteAlredayLockedException e) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.casemanagement.document.route.squelette.already.locked");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		return retour;
	}

	/**
	 * Détermine si l'utilisateur a le droit de verrouiller le squelette.
	 * 
	 * @return Droit de verrouiller
	 * @throws ClientException
	 */
	public boolean canUserVerrouillerSquelette() throws ClientException {
		final STFeuilleRoute feuilleRoute = getRelatedRoute();

		// On peut verrouiller un squelette de feuille de route uniquement à l'état brouillon
		final String lifeCycleState = feuilleRoute.getDocument().getCurrentLifeCycleState();
		if (!ElementLifeCycleState.draft.name().equals(lifeCycleState)) {
			return false;
		}
		// Verifie si le document est verrouillé
		DocumentModel doc = navigationContext.getCurrentDocument();
		if (stLockActions.isDocumentLockedByAnotherUser(doc) || !stLockActions.isDocumentLocked(doc)) {
			return true;
		}

		return false;
	}

	/**
	 * Verrouille le squelette
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public String verrouillerSquelette() throws ClientException {
		final STLockService stLockService = STServiceLocator.getSTLockService();
		DocumentRoute currentRouteModel = getRelatedRoute();
		DocumentModel doc = navigationContext.getCurrentDocument();

		if (stLockActions.isDocumentLockedByAnotherUser(doc)) {
			try {
				stLockService.unlockDocUnrestricted(documentManager, navigationContext.getCurrentDocument());
			} catch (ClientException e) {
				String errorMessage = resourcesAccessor.getMessages().get("st.lock.action.unlock.error");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return null;
			}
			Events.instance().raiseEvent(EventNames.DOCUMENT_CHILDREN_CHANGED, currentRouteModel.getDocument());
		}
		try {
			stLockService.lockDoc(documentManager, navigationContext.getCurrentDocument());
		} catch (ClientException e) {
			String errorMessage = resourcesAccessor.getMessages()
					.get("feedback.casemanagement.document.route.squelette.already.locked");
			facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
			return null;
		}

		// Affiche un message d'information
		String infoMessage = resourcesAccessor.getMessages().get("info.organigrammeManager.node.lock");
		facesMessages.add(StatusMessage.Severity.INFO, infoMessage);

		return null;
	}

	public String createModeleFDRMass() throws ClientException {
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		final EpgOrganigrammeService organigrameService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		final JournalService journalService = SolonEpgServiceLocator.getJournalService();

		EntiteNode ministereNode = (EntiteNode) organigrameService.getOrganigrammeNodeById(this.getMinistereId(),
				OrganigrammeType.MINISTERE);
		PosteNode bdcNode = (PosteNode) organigrameService.getOrganigrammeNodeById(this.getBureauCabinetId(),
				OrganigrammeType.POSTE);
		PosteNode cdmNode = (PosteNode) organigrameService.getOrganigrammeNodeById(this.getChargeMissionId(),
				OrganigrammeType.POSTE);
		PosteNode scdmNode = (PosteNode) organigrameService.getOrganigrammeNodeById(this.getSecretariatChargeMissionId(),
				OrganigrammeType.POSTE);
		PosteNode cpmNode = (PosteNode) organigrameService.getOrganigrammeNodeById(this.getConseillerPmId(),
				OrganigrammeType.POSTE);

		this.setInfoMsg(null);
		this.setResultMsg(null);

		boolean isError = false;

		List<PosteNode> listePostesMin = new ArrayList<PosteNode>();

		isError = checkErrorBeforeCreateModeleFDRMass(ministereNode, bdcNode, cdmNode, scdmNode, cpmNode, isError, listePostesMin);

		if (isError) {
			return "error";
		}

		final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
		StringBuilder infoMsgBuilder = new StringBuilder("");
		String lInfoMsg = "";

		// si des modèles existent déjà pour le ministère, il faut lister
		// leur Type d'acte
		final List<DocumentModel> lstModeleExistant = feuilleRouteModelService
				.getNonDeletedFdrModelFromMinistereAndDirection(documentManager, this.getMinistereId(), null, false);

		if (lstModeleExistant != null && !lstModeleExistant.isEmpty()) {
			infoMsgBuilder.append(
					"Des modèles de feuille de route par défaut existaient déjà pour les types d'actes suivant : ");

			final List<String> listTypeActeEntry = new ArrayList<String>();

			for (DocumentModel model : lstModeleExistant) {
				final SolonEpgFeuilleRoute solonEpgFeuilleRoute = model.getAdapter(SolonEpgFeuilleRoute.class);
				if (solonEpgFeuilleRoute.isFeuilleRouteDefaut()
						&& !listTypeActeEntry.contains(solonEpgFeuilleRoute.getTypeActe())) {
					listTypeActeEntry.add(solonEpgFeuilleRoute.getTypeActe());
				}
			}

			final List<VocabularyEntry> vocLst = vocService.getVocabularyEntryListFromDirectory(
					VocabularyConstants.TYPE_ACTE_VOCABULARY, VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
					listTypeActeEntry);

			for (VocabularyEntry voc : vocLst) {
				infoMsgBuilder.append(voc.getLabel() + ", ");
			}

			lInfoMsg = infoMsgBuilder.toString().substring(0, infoMsgBuilder.length() - 2);
		}

		final Integer nbModeleCrees = feuilleRouteModelService.creeModelesViaSquelette(documentManager,
				this.getMinistereId(), this.getBureauCabinetId(), this.getChargeMissionId(), this.getSecretariatChargeMissionId(), this.getConseillerPmId());

		final String journalMsg = nbModeleCrees + " modeles de feuille de route créés pour le ministère '"
				+ ministereNode.getLabel() + "', bureau des cabinets : '" + bdcNode.getLabel()
				+ "', chargé de mission : '" + cdmNode.getLabel()+ "', secrétariat du chargé de mission : '" + scdmNode.getLabel()
				+ "', conseiller PM : '" + cpmNode.getLabel()+ "'.";

		this.setResultMsg(journalMsg);
		this.setInfoMsg(lInfoMsg);
		facesMessages.add(StatusMessage.Severity.INFO, "Création des modèles terminée");

		journalService.journaliserActionAdministration(documentManager, SolonEpgEventConstant.CREATE_MODELE_FDR_EVENT,
				journalMsg);

		return "";
	}
	
	/**
	 * Clic sur le bouton annuler dans l'écran de création de modeles en masse
	 * @return
	 * @throws ClientException
	 */
	public String cancelCreateModeleFDRMass() throws ClientException {
		this.setMinistereId(null);
		this.setBureauCabinetId(null);
		this.setChargeMissionId(null);
		this.setSecretariatChargeMissionId(null);
		this.setConseillerPmId(null);
		this.setInfoMsg(null);
		this.setResultMsg(null);
		
		return administrationActions.navigateToModeleFeuilleRouteCreationEnMasse();
	}

	/**
	 * Vérification des entrant pour la création de modele de feuille de route en masse
	 * @param ministereNode
	 * @param bdcNode
	 * @param cdmNode
	 * @param scdmNode
	 * @param cpmNode
	 * @param isError
	 * @param listePostesMin
	 * @return
	 * @throws ClientException
	 */
	private boolean checkErrorBeforeCreateModeleFDRMass(EntiteNode ministereNode, PosteNode bdcNode, PosteNode cdmNode,
			PosteNode scdmNode, PosteNode cpmNode, boolean isError, List<PosteNode> listePostesMin) throws ClientException {
		final OrganigrammeService organigrameService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		
		if (ministereNode == null) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Problème lors de la récupération du ministère dans l'organigramme");
			isError = true;
		}
		if (bdcNode == null) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Problème lors de la récupération du bureau du cabinet dans l'organigramme");
			isError = true;
		}
		if (cdmNode == null) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Problème lors de la récupération du chargé de mission dans l'organigramme");
			isError = true;
		}
		if (scdmNode == null) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Problème lors de la récupération du secrétariat du chargé de mission dans l'organigramme");
			isError = true;
		}
		if (cpmNode == null) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Problème lors de la récupération du conseiller PM dans l'organigramme");
			isError = true;
		}

		if(ministereNode != null) {
			listePostesMin = organigrameService.getAllSubPostes(ministereNode);
		}

		boolean isBdcInMin = listePostesMin.contains(bdcNode);

		if (!isBdcInMin) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Le poste bureau des cabinets doit appartenir au ministère");
			isError = true;
		}
		return isError;
	}

	public String getTypeDestinataire() {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument();
		return PropertyUtil.getStringProperty(changeableDocument, STSchemaConstant.ROUTING_TASK_SCHEMA,
				SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY);
	}

	public void setTypeDestinataire(String typeDestinataire) {
		DocumentModel changeableDocument = navigationContext.getChangeableDocument();
		PropertyUtil.setProperty(changeableDocument, STSchemaConstant.ROUTING_TASK_SCHEMA,
				SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY, typeDestinataire);
	}

	public String getMinistereId() {
		return ministereId;
	}

	public void setMinistereId(String ministereId) {
		this.ministereId = ministereId;
	}

	public String getBureauCabinetId() {
		return bureauCabinetId;
	}

	public void setBureauCabinetId(String bureauCabinetId) {
		this.bureauCabinetId = bureauCabinetId;
	}

	public String getChargeMissionId() {
		return chargeMissionId;
	}

	public void setChargeMissionId(String chargeMissionId) {
		this.chargeMissionId = chargeMissionId;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public String getConseillerPmId() {
		return conseillerPmId;
	}

	public void setConseillerPmId(String conseillerPmId) {
		this.conseillerPmId = conseillerPmId;
	}

	public String getSecretariatChargeMissionId() {
		return secretariatChargeMissionId;
	}

	public void setSecretariatChargeMissionId(String secretariatChargeMissionId) {
		this.secretariatChargeMissionId = secretariatChargeMissionId;
	}
}
