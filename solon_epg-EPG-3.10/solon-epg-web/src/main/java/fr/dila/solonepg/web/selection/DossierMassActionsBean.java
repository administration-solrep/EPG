package fr.dila.solonepg.web.selection;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

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
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.cm.caselink.CaseLink;
import fr.dila.ecm.platform.routing.api.DocumentRouteElement;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RechercheConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DocumentRoutingService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierCreationActionsBean;
import fr.dila.solonepg.web.dossier.DossierDistributionActionsBean;
import fr.dila.solonepg.web.dossier.TraitementPapierActionsBean;
import fr.dila.solonepg.web.feuilleroute.DocumentRoutingActionsBean;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.CorbeilleService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.web.action.ClipboardActionsBean;
import fr.dila.st.web.dossier.DossierLockActionsBean;

/**
 * Bean Seam qui permet de gérer les actions de masse des dossiers.
 * 
 * @author asatre
 */
@Name("dossierMassActions")
@Scope(ScopeType.CONVERSATION)
public class DossierMassActionsBean implements Serializable {

	/**
	 * 
	 */
	private static final long			serialVersionUID						= 6748450610595954541L;

	private static final Set<String>	INCOMPATIBLE_STATUS_WITH_PUBLICATION	= Collections
																						.unmodifiableSet(initIncompatibleStatus());
	private static final Set<String>	INCOMPATIBLE_ACTE_WITH_PUBLICATION		= Collections
																						.unmodifiableSet(initIncompatibleActes());
	@In(required = true, create = true)
	protected SSPrincipal						ssPrincipal;

	/**
	 * Les statuts incompatibles avec la publication
	 * 
	 * @return
	 */
	private static Set<String> initIncompatibleStatus() {
		Set<String> incompatibleStatus = new HashSet<String>();
		incompatibleStatus.add(VocabularyConstants.STATUT_ABANDONNE);
		incompatibleStatus.add(VocabularyConstants.STATUT_CLOS);
		incompatibleStatus.add(VocabularyConstants.STATUT_PUBLIE);
		incompatibleStatus.add(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION);
		return incompatibleStatus;
	}

	/**
	 * Les actes incompatibles avec la publication
	 * 
	 * @return
	 */
	private static Set<String> initIncompatibleActes() {
		Set<String> incompatibleActes = new HashSet<String>();
		incompatibleActes.add(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE);
		return incompatibleActes;
	}

	/**
	 * Logger.
	 */
	private static final STLogger						LOGGER	= STLogFactory.getLog(DossierMassActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient ContentViewActions				contentViewActions;

	@In(create = true, required = false)
	protected FacesMessages								facesMessages;

	@In(create = true)
	protected ResourcesAccessor							resourcesAccessor;

	@In(create = true, required = false)
	protected transient ClipboardActionsBean			clipboardActions;

	@In(create = true, required = false)
	protected transient DossierDistributionActionsBean	dossierDistributionActions;

	@In(create = true)
	protected transient DocumentsListsManager			documentsListsManager;

	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	@In(create = true)
	protected transient AdministrationActionsBean		administrationActions;

	@In(create = true)
	protected transient DossierCreationActionsBean		dossierCreationActions;

	@In(create = true)
	protected transient TraitementPapierActionsBean		papierActions;

	@In(create = true, required = false)
	protected transient DocumentRoutingActionsBean		routingActions;

	/**
	 * 
	 * Retourne vrai si des documents ont été sélectionnés pour l'ajout dans la liste de sélection.
	 * 
	 * @return Vrai si des documents ont été sélectionnés pour l'ajout dans la liste de sélection
	 */
	public boolean getCanAddToSelection() {
		String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
		return !documentsListsManager.isWorkingListEmpty(selectionListName);
	}

	public boolean isTypeAllowed() {
		String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
		if (documentsListsManager.isWorkingListEmpty(selectionListName)) {
			return false;
		} else {
			DocumentModel doc = documentsListsManager.getWorkingList(selectionListName).get(0);
			return doc.hasSchema(STSchemaConstant.CASE_LINK_SCHEMA)
					|| doc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)
					|| (doc.hasSchema(STSchemaConstant.FEUILLE_ROUTE_SCHEMA)
							&& (!SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(doc.getType())));
		}
	}

	/**
	 * Retourne vrai si la liste de sélection n'est pas vide.
	 * 
	 * @return Vrai si la liste de sélection n'est pas vide
	 */
	public boolean isCurentDocumentSelectionNotEmpty() {
		return !documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
	}

	public boolean isDossierSignale() {
		String currentContentViewName = contentViewActions.getCurrentContentView().getName();
		return SolonEpgContentViewConstant.ESPACE_SUIVI_DOSSIERS_SIGNALES_CONTENT_VIEW.equals(currentContentViewName);
	}

	/**
	 * Ajoute l'ensemble de dossiers cochés à l'outil de sélection.
	 */
	public void putAddSelectionInWorkList() {
		String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
		LOGGER.info(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION, "Liste de sélection :" + selectionListName);
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"Is working list empty : " + documentsListsManager.isWorkingListEmpty(selectionListName));
			List<DocumentModel> listeDocDansSelection = documentsListsManager.getWorkingList(selectionListName);
			if (listeDocDansSelection != null) {
				String concatListeDocDansSelection = "";
				for (DocumentModel docDansSelection : listeDocDansSelection) {
					// On vérifie qu'on a bien sélectionné des dossiers, sinon on sort de la boucle
					if (!docDansSelection.getType().equals(STConstant.DOSSIER_DOCUMENT_TYPE)) {
						break;
					}
					Dossier dossierDansSelection = docDansSelection.getAdapter(Dossier.class);
					if (dossierDansSelection != null) {
						concatListeDocDansSelection = concatListeDocDansSelection + " "
								+ dossierDansSelection.getNumeroNor();
					}
				}
				if (!concatListeDocDansSelection.isEmpty()) {
					LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION, "liste des documents dans la sélection :"
							+ concatListeDocDansSelection);
				}
			}
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION, "selectionListName : " + selectionListName);
		}
		if ("CURRENT_SELECTION_JOURNAL_DOSSIER".equals(selectionListName)
				&& documentsListsManager.isWorkingListEmpty(selectionListName)) {
			// Dans ce cas il faut récupérer la liste correspondante
			final String reqServletPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestServletPath();
			if (reqServletPath != null) {
				if (LOGGER.isDebugEnable()) {
					LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
							"Modification de la varialbe selectionListName");
				}
				if (reqServletPath.contains("espace_suivi")) {
					selectionListName = "ESPACE_SUIVI_SELECTION";
				} else if (reqServletPath.contains("espace_creation")) {
					selectionListName = "ESPACE_CREATION_SELECTION";
				} else if (reqServletPath.contains("espace_traitement")) {
					selectionListName = "ESPACE_TRAITEMENT_SELECTION";
				} else if (reqServletPath.contains("requeteur_dossier_simple")) {
					selectionListName = "CURRENT_SELECTION_REQUETEUR_DOSSIER_SIMPLE";
				} else if (reqServletPath.contains("requeteur")) {
					selectionListName = "CURRENT_SELECTION_RECH_UFNXQL";
				} else if (reqServletPath.contains("requete_resultats")) {
					selectionListName = "CURRENT_SELECTION_RECH_NOR";
				}
			}
			if (LOGGER.isDebugEnable()) {
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION, "selectionListName : " + selectionListName);
			}
		}
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"Is working list empty : " + documentsListsManager.isWorkingListEmpty(selectionListName));
		}
		if (documentsListsManager.isWorkingListEmpty(selectionListName)) {
			if (LOGGER.isDebugEnable()) {
				LOGGER.debug(STLogEnumImpl.FAIL_GET_DOCUMENT_FONC,
						"No selectable Documents in context to process copy on...");
			}
			return;
		}
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"Vérifie si les éléments ajoutés sont du même type que les éléments de la liste");
		}
		// Vérifie si les éléments ajoutés sont du même type que les éléments de la liste
		if (!clipboardActions.checkifDefaultWorkingListIsSameType(selectionListName)) {
			final String errorMessage = resourcesAccessor.getMessages().get(
					"st.clipboard.addToWorkList.notSameType.error");
			if (LOGGER.isDebugEnable()) {
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
						"Les éléments ajoutés ne sont pas du même type que les éléments de la liste");
			}
			facesMessages.add(StatusMessage.Severity.INFO, errorMessage);
			return;
		}
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"Les éléments ajoutés sont du même types que les éléments de la liste.");
		}

		List<DocumentModel> docsList = documentsListsManager.getWorkingList(selectionListName);
		List<DocumentModel> docsFinalList = new ArrayList<DocumentModel>();
		for (DocumentModel documentModel : docsList) {
			DocumentModel goodModel = null;
			if (STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE.equals(documentModel.getType())) {
				goodModel = documentModel.getAdapter(CaseLink.class).getCase(documentManager).getDocument();
			} else if (STConstant.DOSSIER_DOCUMENT_TYPE.equals(documentModel.getType())) {
				goodModel = documentModel;
			} else {
				if (LOGGER.isDebugEnable()) {
					LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
							"Impossible de récupérer le documentModel. Le document n'est pas ajouté à  la liste finale de sélection.");
				}
				continue;
			}
			docsFinalList.add(goodModel);
			if (LOGGER.isDebugEnable()) {
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION, "le document " + goodModel.getPathAsString()
						+ " a été ajouté à la liste de documents qui seront ensuite ajoutés à la sélection");
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
						"La liste contient à présent " + Integer.toString(docsFinalList.size())
								+ " documents à ajouter à la sélection");
			}
		}
		Object[] params = { docsFinalList.size() };
		if (docsFinalList.isEmpty()) {
			if (LOGGER.isDebugEnable()) {
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION, "La liste finale de documents est vide");
			}
		}
		facesMessages.add(StatusMessage.Severity.INFO, "#0 " + resourcesAccessor.getMessages().get("n_copied_docs"),
				params);
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"La liste finale de documents contient " + Integer.toString(docsFinalList.size())
							+ " à ajouter à la sélection");
			// On essaie de récupérer ce qu'il y a dans l'outil de sélection pour vérifier qu'on y accède bien
			List<DocumentModel> docOutilSelection = documentsListsManager
					.getWorkingList(DocumentsListsManager.DEFAULT_WORKING_LIST);
			if (docOutilSelection != null) {
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
						"Il y a actuellement " + Integer.toString(docOutilSelection.size())
								+ " documents dans l'outil de sélection");
			} else {
				LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
						"Impossible de récupérer la liste des documents contenus dans l'outil de sélection");
			}
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"Ajout des documents contenus dans la liste finale Ã  la sélection.");

		}
		documentsListsManager.addToWorkingList(DocumentsListsManager.DEFAULT_WORKING_LIST, docsFinalList);
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(STLogEnumImpl.ADD_DOCUMENT_TO_SELECTION,
					"Nettoyage de la liste contenant les documents dont la case est cochée.");
		}
		documentsListsManager.getWorkingList(selectionListName).clear();
	}

	/**
	 * Valide en masse l'étape de feuille de route des dossiers versés dans l'outil de sélection.
	 * 
	 * @throws ClientException
	 */
	public void massValidation() throws ClientException {
		if (documentsListsManager.getWorkingList() == null) {
			return;
		}

		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		final DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
		List<DocumentModel> docList = new ArrayList<DocumentModel>();
		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			if (!STConstant.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType())) {
				facesMessages.add(StatusMessage.Severity.WARN,
						"Elément" + "#0 " + resourcesAccessor.getMessages().get("non traité"), dossierDoc.getTitle());

				continue;
			}

			List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager, dossierDoc.getId());
			String result = null;
			if (!dossierLinkList.isEmpty()) {
				for (DocumentModel dossierLinkDoc : dossierLinkList) {
					DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
					// cas où l'utilisateur n'as pas les droits
					if (!documentManager.hasPermission(dossierLinkDoc.getRef(), SecurityConstants.WRITE)
							|| !documentRoutingService.isRoutingTaskTypeValiderAllowed(ssPrincipal, dossierLink)) {
						facesMessages.add(StatusMessage.Severity.WARN, "Droit insuffisant pour " + "#0 ",
								dossierDoc.getTitle());
						result = null;
						continue;
					}
					Dossier dossier = dossierDoc.getAdapter(Dossier.class);
					if (VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dossier.getStatut())) {
						facesMessages.add(StatusMessage.Severity.WARN,
								"Impossible de valider un dossier dont le nor est attribué ! " + "#0 ",
								dossierDoc.getTitle());
						result = null;
					} else if (STDossier.DossierState.init.name().equals(dossierDoc.getCurrentLifeCycleState())) {
						result = dossierDistributionActions.lancerDossier(dossierDoc, dossierLink, false);
					} else {
						// Déverrouille le dossier TODO gérer le cas où l'utilisateur n'a pas le verrou
						if (dossierLockActions.getCanUnlockDossier(dossierDoc)) {
							dossierLockActions.unlockDossier(dossierDoc);
						}

						// Valide l'étape avec avis favorable
						try {
							dossierDistributionService.validerEtape(documentManager, dossierDoc, dossierLinkDoc);

							// Affiche un message de confirmation
							String message = resourcesAccessor.getMessages().get(
									"label.epg.feuilleRoute.message.avisFavorable");
							facesMessages.add(StatusMessage.Severity.INFO,
									MessageFormat.format(message, dossier.getNumeroNor()));

							result = "ok";
						} catch (ClientException exc) {
							LOGGER.debug(documentManager, SSLogEnumImpl.FAIL_UPDATE_FDR_TEC, exc);
							facesMessages.add(StatusMessage.Severity.WARN, exc.getMessage());
						}
					}
				}
			}
			if (result != null) {
				docList.add(dossierDoc);
			}
		}

		for (DocumentModel documentModel : docList) {
			// Retire les éléments traités de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
	}

	public void massDossiersSignales() throws ClientException {
		if (documentsListsManager.getWorkingList() == null) {
			return;
		}

		final DossierSignaleService dossierSignaleService = SolonEpgServiceLocator.getDossierSignaleService();
		List<DocumentModel> docList = new ArrayList<DocumentModel>();
		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			// TODO ARN refreshDocument();
			if (STConstant.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType())) {
				Dossier dossier = dossierDoc.getAdapter(Dossier.class);
				if ("1".equals(dossier.getStatut()) || "2".equals(dossier.getStatut())) {
					docList.add(dossierDoc);
				}
			} else {
				facesMessages.add(StatusMessage.Severity.WARN,
						"Elément" + "#0 " + resourcesAccessor.getMessages().get("non traité"), dossierDoc.getTitle());
			}
		}
		try {
			int result = dossierSignaleService.verserDossiers(documentManager, docList);

			if (result == -1) {
				// le dossierLink n'as pas pu être ajouté à la liste des
				// dossiers signalés de l'utilisateur
				facesMessages.add(
						StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get(
								"feedback.solonepg.dossier.impossible.cannotAddDossierLinksToDossierSignale"));
			} else {
				if (result == 0) {
					// le dossierLink n'as pas pu être ajouté à la liste des
					// dossiers signalés de l'utilisateur
					facesMessages.add(StatusMessage.Severity.WARN,
							resourcesAccessor.getMessages().get("feedback.solonepg.dossier.deja.verse.all"));
				} else {
					if (result < docList.size()) {
						Object[] params = { docList.size() - result };
						// le dossierLink n'as pas pu être ajouté à la liste des
						// dossiers signalés de l'utilisateur
						facesMessages.add(
								StatusMessage.Severity.WARN,
								"#0 "
										+ resourcesAccessor.getMessages().get(
												"feedback.solonepg.dossier.deja.verse" + (result > 1 ? "s" : "")),
								params);
					}
					Object[] params = { result };
					facesMessages.add(
							StatusMessage.Severity.INFO,
							"#0 "
									+ resourcesAccessor.getMessages().get(
											"feedback.solonepg.dossier.verse" + (result > 1 ? "s" : "")), params);
					for (DocumentModel documentModel : docList) {
						// une fois traité on les retire de la liste
						documentsListsManager.removeFromWorkingList(documentModel);
					}
				}
			}
		} catch (ClientException exc) {
			LOGGER.debug(documentManager, STLogEnumImpl.FAIL_UPDATE_DOSSIER_FONC, exc);
			facesMessages.add(
					StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get(
							"feedback.solonepg.dossier.erreur.versement.cannotAddDossierLinksToDossierSignale"));
		}
	}

	/**
	 * 
	 * @return la liste des document de type dossier dans la liste de selection
	 * @throws ClientException
	 */
	public List<DocumentModel> getAllDossierTransfert() throws ClientException {
		List<DocumentModel> docList = new ArrayList<DocumentModel>();
		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			if (STConstant.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType())) {
				docList.add(dossierDoc);
			}
		}

		return docList;
	}

	/**
	 * Renvoie vrai si le dossier est transferable.
	 * 
	 * @return si le dossier est transferable
	 * @throws ClientException
	 */
	public Boolean isTransferable(DocumentModel dossierDoc) throws ClientException {
		if (dossierDoc != null && STConstant.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType())) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			return dossierService.isTransferable(dossier);
		}
		return Boolean.FALSE;
	}

	/**
	 * Renvoie vrai si le dossier est un rectificatif.
	 * 
	 * @return vrai si le dossier est un rectificatif
	 * @throws ClientException
	 */
	public Boolean isRectificatif(DocumentModel dossierDoc) throws ClientException {
		if (dossierDoc != null && STConstant.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType())) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			final ActeService acteService = SolonEpgServiceLocator.getActeService();
			return acteService.isRectificatif(dossier.getTypeActe());
		}
		return Boolean.FALSE;
	}

	/**
	 * Renvoie vrai si le dossier est reattribuable.
	 * 
	 * @return vrai si le dossier est reattribuable
	 * @throws ClientException
	 */
	public Boolean isReattribuable(DocumentModel dossierDoc) throws ClientException {
		if (dossierDoc != null && STConstant.DOSSIER_DOCUMENT_TYPE.equals(dossierDoc.getType())) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			return dossierService.isReattribuable(dossier);
		}
		return Boolean.FALSE;
	}

	public String saveTransfertDossier() throws ClientException {
		List<DocumentModel> docList = new ArrayList<DocumentModel>();
		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			if (isTransferable(dossierDoc)) {
				docList.add(dossierDoc);
			} else {
				String message = resourcesAccessor.getMessages().get(
						"message.error.document.transfert");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return null;
			}
		}

		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		dossierService.transfertDossiersUnrestricted(docList, dossierCreationActions.getMinistereResp(),
				dossierCreationActions.getDirectionResp(), documentManager);

		for (DocumentModel documentModel : docList) {
			// une fois traité on les vire de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}
		
		String message = resourcesAccessor.getMessages().get(
				"feedback.epg.document.transfert");
		facesMessages.add(StatusMessage.Severity.INFO, message);

		return administrationActions.navigateToEspaceAdministration();
	}

	public String saveReattributionDossier() {
		try {
			List<DocumentModel> docList = new ArrayList<DocumentModel>();
			for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
				if (isReattribuable(dossierDoc)) {
					docList.add(dossierDoc);
				} else {
					String message = resourcesAccessor.getMessages().get(
							"message.error.document.reattribution");
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return null;
				}
			}

			final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			dossierService.reattribuerDossiersUnrestricted(docList, dossierCreationActions.getMinistereResp(),
					dossierCreationActions.getDirectionResp(), documentManager);

			for (DocumentModel documentModel : docList) {
				// une fois traité on les vire de la liste
				documentsListsManager.removeFromWorkingList(documentModel);
			}
			
			String message = resourcesAccessor.getMessages().get(
					"feedback.epg.document.reattribution");
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return administrationActions.navigateToEspaceAdministration();
		} catch (ClientException exc) {
			LOGGER.debug(documentManager, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, exc);
			facesMessages.add(StatusMessage.Severity.WARN, exc.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}
	}

	/**
	 * creer une nouvelle liste de mise en signature
	 * 
	 * @throws ClientException
	 */
	public void creerNouvelleListeMiseEnSignature() throws ClientException {

		final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator
				.getListeTraitementPapierService();

		List<String> docIdsList = new ArrayList<String>();
		List<DocumentModel> docList = documentsListsManager.getWorkingList();
		List<DocumentModel> docWorkingList = new ArrayList<DocumentModel>();

		String typeSignataireId = "";
		if (!docList.isEmpty()) {
			TraitementPapier traitementPapier = docList.get(0).getAdapter(TraitementPapier.class);
			typeSignataireId = traitementPapier.getSignataire();
			if (typeSignataireId.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN)) {
				final String errorMessage = resourcesAccessor.getMessages().get(
						"selection.mass.nouvelle.Liste.aucun.signature.error");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return;
			}
		}

		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
			// papierActions.isSignataireSGGSelected(dossierDoc);
			String currentTypeSignataire = traitementPapier.getSignataire();
			if (typeSignataireId.equals(currentTypeSignataire)) {
				docIdsList.add(dossierDoc.getId());
			} else {
				// TODO erreur a traiter
				final String errorMessage = resourcesAccessor.getMessages().get(
						"selection.mass.nouvelle.Liste.notSameSignature.error");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return;
			}
			docWorkingList.add(dossierDoc);
		}
		if (docWorkingList != null && !docWorkingList.isEmpty()) {
			listeTraitementPapierService.creerNouvelleListeTraitementPapierMiseEnSignature(documentManager,
					typeSignataireId, docWorkingList);
		}
		for (DocumentModel documentModel : docWorkingList) {
			// une fois traité on les retire de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}
	}

	/**
	 * creer une nouvelle liste de Demande d'epreuve
	 * 
	 * @throws ClientException
	 */
	public void creerNouvelleListeDemandeEpreuve() throws ClientException {

		final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator
				.getListeTraitementPapierService();

		List<DocumentModel> docList = documentsListsManager.getWorkingList();
		List<DocumentModel> docWorkingList = new ArrayList<DocumentModel>();
		StringBuilder displayIncompatibleDossiers = new StringBuilder(
				"Les dossiers suivants ne peuvent être ajoutés à la liste du fait de leur type d'acte : ");
		boolean isExistingIncompatible = false;

		for (DocumentModel dossierDoc : docList) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			if (INCOMPATIBLE_ACTE_WITH_PUBLICATION.contains(dossier.getTypeActe())) {
				displayIncompatibleDossiers.append(" - N° ").append(dossier.getNumeroNor());
				isExistingIncompatible = true;
			} else {
				docWorkingList.add(dossierDoc);
			}
		}

		if (isExistingIncompatible) {
			LOGGER.info(documentManager, STLogEnumImpl.FAIL_UPDATE_DOSSIER_FONC,
					"Tentative de liste demande épreuve avec dossiers non compatibles : " + displayIncompatibleDossiers);
			facesMessages.add(StatusMessage.Severity.WARN, displayIncompatibleDossiers.toString());
		}

		if (docWorkingList != null && !docWorkingList.isEmpty()) {
			listeTraitementPapierService.creerNouvelleListeTraitementPapierDemandesEpreuve(documentManager,
					docWorkingList);
		}

		for (DocumentModel documentModel : docWorkingList) {
			// une fois traité on les retire de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}
	}

	/**
	 * creer une nouvelle liste de Demande de Publication
	 * 
	 * @throws ClientException
	 */
	public void creerNouvelleListeDemandeDePublication() throws ClientException {

		final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator
				.getListeTraitementPapierService();

		List<DocumentModel> docWorkingList = new ArrayList<DocumentModel>();
		List<DocumentModel> docList = documentsListsManager.getWorkingList();
		StringBuilder displayIncompatibleDossiers = new StringBuilder(
				"Les dossiers suivants ne peuvent être publiés du fait de leur statut ou type d'acte : ");
		boolean isExistingIncompatible = false;
		// On créé la working liste des documents model en omettant les dossiers dont les statuts ne sont pas
		// compatibles
		// avec une publication
		for (DocumentModel dossierDoc : docList) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			if (INCOMPATIBLE_STATUS_WITH_PUBLICATION.contains(dossier.getStatut())
					|| INCOMPATIBLE_ACTE_WITH_PUBLICATION.contains(dossier.getTypeActe())) {
				displayIncompatibleDossiers.append(" - N° ").append(dossier.getNumeroNor());
				isExistingIncompatible = true;
			} else {
				docWorkingList.add(dossierDoc);
			}
		}

		if (isExistingIncompatible) {
			LOGGER.info(documentManager, STLogEnumImpl.FAIL_UPDATE_DOSSIER_FONC,
					"Tentative de liste publication avec dossiers non compatibles : " + displayIncompatibleDossiers);
			facesMessages.add(StatusMessage.Severity.WARN, displayIncompatibleDossiers.toString());
		}

		if (docWorkingList != null && !docWorkingList.isEmpty()) {
			listeTraitementPapierService.creerNouvelleListeTraitementPapierDemandesDePublication(documentManager,
					docWorkingList);
		}
		for (DocumentModel documentModel : docWorkingList) {
			// une fois traités on les retire de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}
	}

	/**
	 * supprimer Dossiers
	 * 
	 * @throws Exception
	 */
	public void supprimerDossiers() throws Exception {

		// utilisation d'uneunrestrictedSession pour la methode candidateDossierToSuppression
		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {

				List<DocumentModel> docWorkingList = new ArrayList<DocumentModel>();
				List<DocumentModel> docList = documentsListsManager.getWorkingList();

				for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
					docWorkingList.add(dossierDoc);
					Dossier dossier = dossierDoc.getAdapter(Dossier.class);
					if (!VocabularyConstants.STATUT_INITIE.equals(dossier.getStatut())) {
						final String errorMessage = resourcesAccessor.getMessages().get(
								"selection.mass.suppression.error");
						facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
						return;
					}
				}
				final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
				for (DocumentModel dossierDoc : docList) {
					try {
						dossierService.candidateDossierToSuppression(session, dossierDoc);
					} catch (Exception exc) {
						LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_FONC, dossierDoc, exc);
					}
					Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
				}

				for (DocumentModel documentModel : docWorkingList) {
					// une fois traité on les retire de la liste
					documentsListsManager.removeFromWorkingList(documentModel);
				}
			}
		}.runUnrestricted();
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessTransfertAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return ssPrincipal.isAdministrator()
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_TRANSFERT_DOSSIER);
	}

	/**
	 * supprimer Dossiers
	 * 
	 * @throws Exception
	 */
	public void abandonDossiers() throws Exception {

		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();

		List<DocumentModel> docWorkingList = new ArrayList<DocumentModel>();
		List<DocumentModel> docList = documentsListsManager.getWorkingList();

		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			docWorkingList.add(dossierDoc);
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);

			if (!DocumentRouteElement.ElementLifeCycleState.running.name()
					.equals(dossierDoc.getCurrentLifeCycleState())
					|| !VocabularyConstants.STATUT_LANCE.equals(dossier.getStatut())) {
				final String errorMessage = resourcesAccessor.getMessages().get("selection.mass.abandon.error");
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return;
			}
		}

		for (DocumentModel dossierDoc : docList) {
			dossierService.abandonDossier(documentManager, dossierDoc);
		}
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		for (DocumentModel documentModel : docWorkingList) {
			// une fois traité on les vire de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}
	}

	/**
	 * Retourne vrai si la contentView a la possibilité d'imprimer une liste de résultat : c a d si la liste fair parti
	 * de l'une de ces listes : recherche nor simple espace recherche : recherche utilisateur et recherche dossier
	 * espace suivi : Mon infocentre : dossier traités, dossiers crées, dossiers non traités
	 * 
	 * @return vrai si la contentView a la possibilité d'imprimer une liste de résultat.
	 */
	public boolean hasViewPrintListOption() {
		String contentViewName = null;
		if (contentViewActions.getCurrentContentView() != null) {
			contentViewName = contentViewActions.getCurrentContentView().getName();
		}

		// Nom des contentview à ajouter si on souhaite exporter les résultats au format PDF avec un if
		// contentviewname="". Désactivé pour passer tout
		// en format csv
		return false;
	}

	/**
	 * Retourne vrai si la contentView a la possibilité d'etre exporter au format csv
	 * 
	 * @return vrai si la contentView a la possibilité d'imprimer une liste de résultat.
	 */
	public boolean hasViewExportCsvListOption() {
		String contentViewName = null;
		if (contentViewActions.getCurrentContentView() != null) {
			contentViewName = contentViewActions.getCurrentContentView().getName();
		}

		return contentViewName != null
				&& (contentViewName.equals(SolonEpgContentViewConstant.ESPACE_JOURNAL)
						|| contentViewName.equals(SolonEpgContentViewConstant.PAN_JOURNAL)
						|| contentViewName.equals(SolonEpgContentViewConstant.ESPACE_RECHERCHE_DOSSIER_NOR)
						|| contentViewName.equals(SolonEpgContentViewConstant.ESPACE_RECHERCHE_DOSSIER_REQUETE)
						|| contentViewName.equals(SolonEpgContentViewConstant.ESPACE_SUIVI_DOSSIERS_CONTENT_VIEW)
						|| contentViewName.equals(RechercheConstants.REQUETEUR_CONTENT_VIEW)
						|| contentViewName.equals(RechercheConstants.REQUETEUR_DOSSIER_CONTENT_VIEW)
						|| contentViewName
								.equals(SolonEpgContentViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT_CONTENT_VIEW)
						|| contentViewName
								.equals(SolonEpgContentViewConstant.RECHERCHE_RESULTATS_CONSULTES_MODELE_FEUILLE_ROUTE_CONTENT_VIEW) || contentViewName
						.equals(SolonEpgContentViewConstant.RECHERCHE_FAVORIS_CONSULTATION_MODELE_FEUILLE_ROUTE_CONTENT_VIEW));
	}

	/**
	 * Substitution de la feuille de route en cours : - Annule la feuille de route ; - Démarre une nouvelle feuille de
	 * route.
	 * 
	 * @return Vue de la liste des dossiers
	 * @throws ClientException
	 */
	public void substituerMassRoute() throws ClientException {

		// Substitue la feuille de route
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();

		List<DocumentModel> docWorkingList = new ArrayList<DocumentModel>();
		List<DocumentModel> docList = documentsListsManager.getWorkingList();

		for (DocumentModel dossierDoc : documentsListsManager.getWorkingList()) {
			if (documentManager.hasPermission(dossierDoc.getRef(), SecurityConstants.WRITE)) {
				docWorkingList.add(dossierDoc);
			} else {
				facesMessages
						.add(StatusMessage.Severity.WARN, "Droit insuffisant pour " + "#0 ", dossierDoc.getTitle());
				return;
			}
		}

		String routeId = routingActions.getRelatedRouteModelDocumentId();
		DocumentModel newRouteDoc = null;
		if (routeId != null && !"".equals(routeId)) {
			newRouteDoc = documentManager.getDocument(new IdRef(routeId));
		}

		if (newRouteDoc == null) {
			// Aucun modèle n'est sélectionné
			String message = resourcesAccessor.getMessages().get("feedback.reponses.document.route.no.valid.route");
			facesMessages.add(StatusMessage.Severity.WARN, message);

			return;
		}

		for (DocumentModel dossierDoc : docList) {
			// recupere l'ancien feuille de route
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			if (VocabularyConstants.STATUT_INITIE.equals(dossier.getStatut())) {
				DocumentModel oldRouteDoc = dossierDistributionService.getLastDocumentRouteForDossier(documentManager,
						dossierDoc);
				dossierDistributionService.substituerFeuilleRoute(documentManager, dossierDoc, oldRouteDoc,
						newRouteDoc, STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_SUBSTITUTION);
			}
		}
		// Réinitialise la sélection de la feuille de route
		routingActions.resetRelatedRouteDocumentId();

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		for (DocumentModel documentModel : docWorkingList) {
			// une fois traité on les vire de la liste
			documentsListsManager.removeFromWorkingList(documentModel);
		}

		// envoi message information utilisateur
		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("selection.mass.action.substituer.success"));

	}

	public String navigateToMassSubstituerFeuilleRoute() {
		routingActions.resetRelatedRouteDocumentId();
		return "view_mass_substituer_feuille_route";
	}

}
