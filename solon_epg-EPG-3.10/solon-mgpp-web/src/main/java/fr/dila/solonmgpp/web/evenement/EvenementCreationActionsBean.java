package fr.dila.solonmgpp.web.evenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.apache.commons.lang.StringUtils;
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
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.forms.layout.api.BuiltinModes;
import org.nuxeo.ecm.platform.forms.layout.api.FieldDefinition;
import org.nuxeo.ecm.platform.forms.layout.api.Widget;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.service.NORServiceImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierDistributionActionsBean;
import fr.dila.solonmgpp.api.constant.ModeCreationConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PieceJointeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.service.EvenementTypeService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleActionsBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationOEPActionsBean;
import fr.dila.solonmgpp.web.metadonnees.MetaDonneesActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.CorbeilleService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;
import fr.dila.st.web.dossier.DossierLockActionsBean;
import fr.dila.st.web.lock.STLockActionsBean;
import fr.sword.xsd.solon.epp.NiveauLectureCode;

/**
 * bean de gestion de la creation des evenements
 * 
 * @author asatre
 * 
 */
@Name("evenementCreationActions")
@Scope(ScopeType.CONVERSATION)
public class EvenementCreationActionsBean implements Serializable {

	private static final String							FIELD_NAME_NIVEAU_LECTURE_NIVEAU	= EvenementDTO.NIVEAU_LECTURE
																									+ "."
																									+ EvenementDTO.NIVEAU_LECTURE_NIVEAU;

	private static final long							serialVersionUID					= -6559975643469180671L;

	private static final String							MAIL_OBJET							= "objetMail";
	private static final String							MAIL_DESTINATAIRE					= "destinataireMail";
	private static final String							MAIL_MESSAGE						= "messageMail";

	private Map<String, String>							mailEvenement						= new HashMap<String, String>();
	private Map<String, String>							liaisonOEPEvt						= new HashMap<String, String>();

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER								= STLogFactory
																									.getLog(EvenementCreationActionsBean.class);

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient EspaceParlementaireActionsBean	espaceParlementaireActions;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient MetaDonneesActionsBean			metadonneesActions;

	@In(create = true, required = false)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true, required = false)
	protected transient DossierDistributionActionsBean	dossierDistributionActions;

	@In(create = true, required = false)
	protected transient STLockActionsBean				stLockActions;

	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	/**
	 * Map des evenements createurs par onglet
	 */
	private Map<String, List<SelectItem>>				mapEvenementCreateur;

	/**
	 * typeEvenement courant pour la creation d'un evenment successif
	 */
	private String										currentTypeEvenementSuccessif;

	/**
	 * typeEvenement courant pour la creation d'un evenment createur
	 */
	private String										currentTypeEvenement;

	private boolean										publier;

	private Set<String>									requiredField;

	private Boolean										mailPopupPanelVisible				= Boolean.FALSE;

	private Boolean										liaisonOEPPopupPanelVisible			= Boolean.FALSE;

	protected final List<String>						ignoredFromSortItems				= Arrays.asList(
																									"GENERIQUE",
																									"FUSION", "ALERTE",
																									"EVT53");

	// public EvenementDTO evtDTO = new EvenementDTOImpl();

	public void setCurrentTypeEvenement(String currentTypeEvenement) {
		this.currentTypeEvenement = currentTypeEvenement;
	}

	public String getCurrentTypeEvenement() {
		return currentTypeEvenement;
	}

	public String navigateToCreationCommunication() {

		currentTypeEvenementSuccessif = null;
		publier = false;
		requiredField = new HashSet<String>();
		mailPopupPanelVisible = Boolean.FALSE;

		if (StringUtils.isEmpty(currentTypeEvenement)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		// initialisation des métadonnees de l'evenement
		try {
			EvenementDTO evenementDTO = SolonMgppServiceLocator.getEvenementService().initialiserEvenement(
					currentTypeEvenement, documentManager);
			navigationContext.resetCurrentDocument();
			navigationContext.setCurrentEvenement(evenementDTO);
		} catch (ClientException e) {
			return logError(STLogEnumImpl.FAIL_INIT_COMM_TEC, "Erreur lors de l'initialisation de la communication", e);
		}

		metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_CREER);

		// Renseigne le vue courante
		navigationContext.setCurrentView(SolonMgppViewConstant.VIEW_CREATE_EVENEMENT);

		return SolonMgppViewConstant.VIEW_CREATE_EVENEMENT;

	}

	public String navigateToCreationCommunicationSuccessive() {
		currentTypeEvenement = null;
		publier = false;
		requiredField = new HashSet<String>();
		mailPopupPanelVisible = Boolean.FALSE;

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		if (StringUtils.isEmpty(currentTypeEvenementSuccessif)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		// initialisation des métadonnees de l'evenement
		try {
			evenementDTO = SolonMgppServiceLocator.getEvenementService().initialiserEvenementSuccessif(evenementDTO,
					currentTypeEvenementSuccessif, documentManager);
			FichePresentationOEP fpOEP = SolonMgppServiceLocator.getDossierService().findFicheOEP(documentManager,
					navigationContext.getCurrentEvenement().getIdDossier());
			if (fpOEP != null) {
				evenementDTO.setIdDossier(fpOEP.getIdDossier());
			}
			navigationContext.resetCurrentDocument();
			navigationContext.setCurrentEvenement(evenementDTO);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_INIT_COMM_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_CREER);

		// Renseigne le vue courante
		navigationContext.setCurrentView(SolonMgppViewConstant.VIEW_CREATE_EVENEMENT);

		return SolonMgppViewConstant.VIEW_CREATE_EVENEMENT;

	}

	private String resetAndNavigate(Boolean fireEvent) throws ClientException {
		return resetAndNavigate(fireEvent, null);
	}

	private String resetAndNavigate(Boolean fireEvent, MessageDTO messageDTO) throws ClientException {

		String view = espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());

		if (fireEvent) {
			// reset du provider
			Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);

			// refresh du provider
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

			// refresh de la corbeille courante
			Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);

		}

		if (messageDTO != null) {
			navigationContext.setCurrentMessage(messageDTO);
		}

		mailPopupPanelVisible = Boolean.FALSE;
		// retourne au menusecondaire courant
		return view;
	}

	private void reset() throws ClientException {
		mailPopupPanelVisible = Boolean.FALSE;
		currentTypeEvenement = null;
		currentTypeEvenementSuccessif = null;
		navigationContext.resetCurrentDocument();
	}

	/**
	 * Retourne la liste des événements filtrés par procédure.
	 * 
	 * @param procedure
	 *            Procédure législative
	 * @return Liste d'événements
	 */
	public List<SelectItem> getEvenementList(String procedure) {
		final EvenementTypeService evenementTypeService = SolonMgppServiceLocator.getEvenementTypeService();
		List<EvenementTypeDescriptor> eventTypeList = evenementTypeService.findEvenementTypeByProcedure(procedure);
		List<SelectItem> evenementList = new ArrayList<SelectItem>();

		buildListEvenement(evenementList, eventTypeList);

		// tri par label
		Collections.sort(evenementList, new Comparator<SelectItem>() {

			@Override
			public int compare(SelectItem o1, SelectItem o2) {
				// les événements générique toujours à la fin de la liste
				String value1 = (String) o1.getValue();
				String value2 = (String) o2.getValue();

				if (inIgnoredList(value1) && !inIgnoredList(value2)) {
					return 1;
				}
				if (inIgnoredList(value2) && !inIgnoredList(value1)) {
					return -1;
				}
				return (value1).compareTo(value2);
			}
		});

		return evenementList;
	}

	private boolean inIgnoredList(String value) {
		for (String str : this.ignoredFromSortItems) {
			if (value != null && value.trim().contains(str)) {
				return true;
			}
		}
		return false;
	}

	public List<SelectItem> getEvenementCreateurList() {
		if (mapEvenementCreateur == null) {
			mapEvenementCreateur = new HashMap<String, List<SelectItem>>();
		}

		Action currentMenu = navigationWebActions.getCurrentSecondMenuAction();

		if (currentMenu != null) {
			String currentMainAction = currentMenu.getId();

			if (mapEvenementCreateur.get(currentMainAction) == null) {
				List<SelectItem> list = new ArrayList<SelectItem>();
				final EvenementTypeService evenementTypeService = SolonMgppServiceLocator.getEvenementTypeService();
				List<EvenementTypeDescriptor> eventTypeList = evenementTypeService
						.findEvenementTypeCreateur(currentMainAction);

				buildListEvenement(list, eventTypeList);

				mapEvenementCreateur.put(currentMainAction, list);

				return list;
			} else {
				return mapEvenementCreateur.get(currentMainAction);
			}

		}

		return new ArrayList<SelectItem>();
	}

	public List<SelectItem> getEvenementSuccessifList() {
		final EvenementTypeService evenementTypeService = SolonMgppServiceLocator.getEvenementTypeService();

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		List<SelectItem> list = new ArrayList<SelectItem>();

		List<String> listSuivants = evenementDTO.getEvenementsSuivants();

		if (listSuivants != null) {
			for (String evtSuivant : listSuivants) {
				SelectItem item;
				try {
					item = new SelectItem(evtSuivant, evenementTypeService.getEvenementType(evtSuivant).getLabel());
				} catch (ClientException e) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_EVENT_TYPE_TEC);
					item = new SelectItem(evtSuivant, evtSuivant);
				}

				list.add(item);
			}
		}

		// tri par value
		Collections.sort(list, new Comparator<SelectItem>() {

			@Override
			public int compare(SelectItem obj1, SelectItem obj2) {
				return ((String) obj1.getValue()).compareTo(((String) obj2.getValue()));
			}

		});

		String evenementType = evenementDTO.getTypeEvenementName();

		List<SelectItem> listAutres = new ArrayList<SelectItem>();

		List<EvenementTypeDescriptor> eventTypeList = evenementTypeService.findEvenementTypeSuccessif(evenementType,
				evenementDTO.getEvenementsSuivants());

		buildListEvenement(listAutres, eventTypeList);

		SelectItemGroup selectItemGroupSuivant = new SelectItemGroup("Communications conseillées");
		selectItemGroupSuivant.setSelectItems(list.toArray(new SelectItem[0]));

		SelectItemGroup selectItemGroupOthers = new SelectItemGroup("Autres communications");
		selectItemGroupOthers.setSelectItems(listAutres.toArray(new SelectItem[0]));

		List<SelectItem> options = new ArrayList<SelectItem>();
		options.add(selectItemGroupSuivant);
		options.add(selectItemGroupOthers);

		return options;

	}

	private void buildListEvenement(List<SelectItem> list, List<EvenementTypeDescriptor> eventTypeList) {
		for (EvenementTypeDescriptor eventType : eventTypeList) {
			SelectItem item = new SelectItem(eventType.getName(), eventType.getLabel());
			list.add(item);
		}

		// tri par label
		Collections.sort(list, new Comparator<SelectItem>() {
			@Override
			public int compare(SelectItem obj1, SelectItem obj2) {
				return ((String) obj1.getValue()).compareTo(((String) obj2.getValue()));
			}
		});
	}

	public String getCurrentTypeLibelle() {
		String type = null;
		try {
			type = currentTypeEvenement != null ? currentTypeEvenement : currentTypeEvenementSuccessif;
			return getTypeEvenementLibelle(type);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_META_DONNEE_TEC, e);
		}

		return type;

	}

	public String getTypeEvenementLibelle(String type) throws ClientException {
		if (type != null) {
			EvenementTypeDescriptor descriptor = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(
					type);
			if (descriptor != null) {
				return descriptor.getLabel();
			}
		}

		return null;
	}

	public PieceJointeDescriptor getDescriptor(String pieceJointeType) throws ClientException {
		String type = currentTypeEvenement != null ? currentTypeEvenement : currentTypeEvenementSuccessif;
		if (type != null) {
			EvenementTypeDescriptor descriptor = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(
					type);
			return descriptor.getPieceJointe().get(pieceJointeType);
		}
		return null;
	}

	public PieceJointeDTO addPieceJointe(String pieceJointeType) {
		try {
			EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();

			String type = currentTypeEvenement != null ? currentTypeEvenement : currentTypeEvenementSuccessif;
			EvenementTypeDescriptor descriptor = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(
					type);
			if (descriptor != null) {
				Map<String, PieceJointeDescriptor> map = descriptor.getPieceJointe();
				if (map != null) {
					PieceJointeDescriptor pieceJointeDescriptor = map.get(pieceJointeType);
					if (pieceJointeDescriptor != null) {
						if (!pieceJointeDescriptor.isMultivalue()
								&& evenementDTO.getListPieceJointe(pieceJointeType).size() > 0) {
							return null;
						}
					}
				}
			}

			if ("AUTRE".equals(pieceJointeType)) {
				pieceJointeType = "AUTRES";
			}

			String titlePieceJointe = STServiceLocator.getVocabularyService().getEntryLabel(
					VocabularyConstants.VOCABULARY_PIECE_JOINTE_DIRECTORY, pieceJointeType);
			if (VocabularyServiceImpl.UNKNOWN_ENTRY.equals(titlePieceJointe)) {
				titlePieceJointe = pieceJointeType;
			}

			titlePieceJointe = titlePieceJointe.replace("(s)", "");
			PieceJointeDTO pieceJointeDTO = SolonMgppServiceLocator.getPieceJointeService().createPieceJointe(
					pieceJointeType);
			pieceJointeDTO.setLibelle(titlePieceJointe);
			evenementDTO.getListPieceJointe(pieceJointeType).add(pieceJointeDTO);
			return pieceJointeDTO;
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_INIT_COMM_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}
	}

	public void removePieceJointe(PieceJointeDTO pieceJointeDTO) {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		evenementDTO.getListPieceJointe(pieceJointeDTO.getType()).remove(pieceJointeDTO);
	}

	public void removePieceJointeFichier(PieceJointeDTO pieceJointeDTO, PieceJointeFichierDTO pieceJointeFichierDTO) {
		pieceJointeDTO.getFichier().remove(pieceJointeFichierDTO);
	}

	/**
	 * Sauvegarde le document en cours de création après avoir vérifié que les metadonnées requise sont bel et bien
	 * présentent.
	 * 
	 * 
	 * @param publie
	 * @return
	 */
	public String saveCreationEvenement(boolean publie) {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {

			String idDossier = evenementDTO.getIdDossier();

			if (StringUtils.isNotBlank(idDossier)) {
				if (!evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT28)
						&& !evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT44)
						&& !evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT44TER)) {
					evenementDTO.setNor(idDossier);
				}
			} else {
				idDossier = evenementDTO.getNor();
				evenementDTO.setIdDossier(idDossier);
			}

			if (!checkMetaDonnee(evenementDTO, publier)) {
				return null;
			}

			// sauvegarde de la communication
			SolonMgppServiceLocator.getEvenementService().createEvenement(evenementDTO, publie, documentManager);

			if (publier) {
				String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_PUBLIER);
				facesMessages.add(StatusMessage.Severity.INFO, message);
			} else {
				String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_CREER);
				facesMessages.add(StatusMessage.Severity.INFO, message);
			}

			return refreshEvenement();

		} catch (ClientException e) {
			String message = null;
			if (publier) {
				message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_PUBLIER);
			} else {
				message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_CREER);
			}
			return logError(STLogEnumImpl.FAIL_CREATE_COMM_TEC, message, e);
		}

	}

	private String logError(STLogEnumImpl stLogEnumImpl, String messageClient, ClientException exception) {
		LOGGER.error(documentManager, stLogEnumImpl, exception);
		facesMessages.add(StatusMessage.Severity.WARN, messageClient);
		facesMessages.add(StatusMessage.Severity.WARN, exception.getMessage());
		TransactionHelper.setTransactionRollbackOnly();
		return null;
	}

	public String cancelCreationEvenement() throws ClientException {
		// Renseigne le vue courante
		navigationContext.setCurrentView(null);
		reset();
		// retourne au menusecondaire courant
		return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
	}

	public boolean checkMetaDonnee(EvenementDTO evenementDTO, boolean publier) throws ClientException {
		// check nor / idDossier

		/**
		 * on récupère la liste des metadonnée qu'il faut pour valider l'événement en cours.
		 */
		Map<String, PropertyDescriptor> map = SolonMgppServiceLocator.getMetaDonneesService().getMapProperty(
				evenementDTO.getTypeEvenementName());
		NORService norService = SolonEpgServiceLocator.getNORService();
		String messageList="";

		PropertyDescriptor nor = map.get("nor");

		if (nor != null && nor.isObligatoire() && StringUtils.isBlank(evenementDTO.getNor())) {
			messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_NOR));
		}else if (nor != null && nor.isObligatoire() && !StringUtils.isBlank(evenementDTO.getNor()) 
				&& !norService.isStructNorValide(evenementDTO.getNor())) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.NOR_STRUCTURE_INCORRECTE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		PropertyDescriptor dossier = map.get("dossier");

		if (dossier != null && dossier.isObligatoire() && StringUtils.isBlank(evenementDTO.getIdDossier())) {
			messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_IDENTIFIANT_DOSSIER));
		}

		// check cohérence Emetteur/Destinataire/Copie
		PropertyDescriptor emetteur = map.get("emetteur");
		PropertyDescriptor destinataire = map.get("destinataire");
		PropertyDescriptor copie = map.get("destinataireCopie");

		if (emetteur != null && emetteur.isObligatoire() && StringUtils.isBlank(evenementDTO.getEmetteur())) {
			messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_EMETTEUR));
		}

		if (destinataire != null && destinataire.isObligatoire() && StringUtils.isBlank(evenementDTO.getDestinataire())) {
			messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_DESTINATAIRE));
		}

		if (copie != null && copie.isObligatoire() && (evenementDTO.getCopie() == null || evenementDTO.getCopie().isEmpty())) {
			messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_COPIE));
		}

		if (evenementDTO.getEmetteur().equals(evenementDTO.getDestinataire())) {
			String message = resourcesAccessor.getMessages()
					.get(SolonMgppI18nConstant.SAME_EMETTEUR_DESTIANTAIRE_ERROR);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		if (copie != null && evenementDTO.getCopie() != null
				&& evenementDTO.getCopie().contains(evenementDTO.getEmetteur())) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.COPIE_CONTAINS_EMETTEUR);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		if (copie != null && evenementDTO.getCopie() != null
				&& evenementDTO.getCopie().contains(evenementDTO.getDestinataire())) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.COPIE_CONTAINS_DESTINATAIRE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return false;
		}

		if (copie == null && evenementDTO.getCopie() != null) {
			// clear list des destinataireCopie
			evenementDTO.getCopie().clear();
		}
		if (publier) {
			// check field
			for (String fieldName : requiredField) {
				if (!isRequiredFieldValid(evenementDTO, fieldName)) {
					messageList = completeMessage(messageList, SolonMgppI18nConstant.REQUIRED_FIELD_MAP.get(fieldName) != null ?
							resourcesAccessor.getMessages().get(SolonMgppI18nConstant.REQUIRED_FIELD_MAP.get(fieldName)) : fieldName);
				}
			}

			// check piece jointe
			EvenementTypeDescriptor descriptor = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(
					evenementDTO.getTypeEvenementName());

			for (String typePieceJointe : descriptor.getPieceJointe().keySet()) {
				if (descriptor.getPieceJointe().get(typePieceJointe).isObligatoire()) {
					if (evenementDTO.getListPieceJointe(typePieceJointe) == null
							|| evenementDTO.getListPieceJointe(typePieceJointe).isEmpty()) {
						messageList = completeMessage(messageList, resourcesAccessor.getMessages().get(SolonMgppI18nConstant.LABEL_PIECE_JOINTE));
						break;
					}
				}
				if (evenementDTO.getListPieceJointe(typePieceJointe) != null) {
					for (PieceJointeDTO pieceJointeDTO : evenementDTO.getListPieceJointe(typePieceJointe)) {
						if (pieceJointeDTO.getFichier() == null || pieceJointeDTO.getFichier().isEmpty()) {
							String message = resourcesAccessor.getMessages().get(
									SolonMgppI18nConstant.PIECE_JOINTE_FICHIER_MANQUANT);
							facesMessages.add(StatusMessage.Severity.WARN, message);

							return false;
						}
					}
				}
			}

			// si EVT 19 : RG-PROC-LEG-03
			try {
				SolonMgppServiceLocator.getEvenementService().checkProcedureAcceleree(documentManager, evenementDTO);
			} catch (ClientException e) {
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				return false;
			}

		}
		if(!messageList.isEmpty()) {
			messageList = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.METAS_MANQUANTES_LIST) + " " + messageList ;
			facesMessages.add(StatusMessage.Severity.WARN, messageList);
			return false;
		}
		return true;
	}
	
	public String completeMessage(String message, String ajout) {
		if(message.isEmpty()) {
			message = ajout;
		}else {
			message = message + ", "+ ajout;
		}
		return message;
	}

	public String rectifierEvenement() {

		currentTypeEvenementSuccessif = navigationContext.getCurrentEvenement().getTypeEvenementName();
		if (StringUtils.isEmpty(currentTypeEvenementSuccessif)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_RECTIFIER);

		publier = false;
		requiredField = new HashSet<String>();

		// Renseigne le vue courante
		navigationContext.setCurrentView(SolonMgppViewConstant.VIEW_RECTIFIER_EVENEMENT);
		return SolonMgppViewConstant.VIEW_RECTIFIER_EVENEMENT;
	}

	public String cancelRectifierEvenement() throws ClientException {
		return refreshEvenement();
	}

	public String saveRectifierEvenement(boolean publier) {

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {

			if (!checkMetaDonnee(evenementDTO, publier)) {
				return null;
			}

			// sauvegarde de la rectification
			SolonMgppServiceLocator.getEvenementService().rectifierEvenement(evenementDTO, publier, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_RECTIFIER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return refreshEvenement();

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_RECTIFIER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}
	}

	public String completerEvenement() {

		currentTypeEvenementSuccessif = navigationContext.getCurrentEvenement().getTypeEvenementName();
		if (StringUtils.isEmpty(currentTypeEvenementSuccessif)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_COMPLETER);

		publier = false;
		requiredField = new HashSet<String>();

		// Renseigne le vue courante
		navigationContext.setCurrentView(SolonMgppViewConstant.VIEW_COMPLETER_EVENEMENT);
		return SolonMgppViewConstant.VIEW_COMPLETER_EVENEMENT;
	}

	public String cancelCompleterEvenement() throws ClientException {
		return refreshEvenement();
	}

	public String saveCompleterEvenement(boolean publier) {

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {

			if (!checkMetaDonnee(evenementDTO, publier)) {
				return null;
			}

			// sauvegarde de la completion
			SolonMgppServiceLocator.getEvenementService().completerEvenement(evenementDTO, publier, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_COMPLETER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return refreshEvenement();

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_COMPLETER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}
	}

	public String modifierEvenement() {

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		currentTypeEvenementSuccessif = evenementDTO.getTypeEvenementName();
		if (StringUtils.isEmpty(currentTypeEvenementSuccessif)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		String modeCreation = evenementDTO.getVersionCouranteModeCreation();

		if (ModeCreationConstants.BROUILLON_INIT.equals(modeCreation)) {
			metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_CREER);
		} else if (ModeCreationConstants.BROUILLON_COMPLETION.equals(modeCreation)) {
			metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_COMPLETER);
		} else if (ModeCreationConstants.BROUILLON_RECTIFICATION.equals(modeCreation)) {
			metadonneesActions.setCurrentLayoutMode(MetaDonneesActionsBean.LAYOUT_MODE_RECTIFIER);
		}

		publier = false;
		requiredField = new HashSet<String>();

		// Renseigne le vue courante
		navigationContext.setCurrentView(SolonMgppViewConstant.VIEW_MODIFIER_EVENEMENT);

		return SolonMgppViewConstant.VIEW_MODIFIER_EVENEMENT;

	}

	public String cancelModifierEvenement() throws ClientException {
		return refreshEvenement();
	}

	public String saveModifierEvenement(boolean publier) {

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		currentTypeEvenementSuccessif = evenementDTO.getTypeEvenementName();
		if (StringUtils.isEmpty(currentTypeEvenementSuccessif)) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.TYPE_EVENEMENT_VIDE);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			TransactionHelper.setTransactionRollbackOnly();
			return null;
		}

		try {

			if (!checkMetaDonnee(evenementDTO, publier)) {
				return null;
			}

			// sauvegarde de la modification
			SolonMgppServiceLocator.getEvenementService().modifierEvenement(evenementDTO, publier, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_MODIFIER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return refreshEvenement();

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_MODIFIER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}

	}

	public String suivreTransitionTraite() throws ClientException {

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		DossierLink dossierLink = null;

		MessageDTO messageDTO = navigationContext.getCurrentMessage();

		if (dossierDoc != null) {

			final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
			List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager, dossierDoc.getId());

			if (dossierLinkList.size() > 0) {
				dossierLink = dossierLinkList.get(0).getAdapter(DossierLink.class);
			}
		}

		try {
			SolonMgppServiceLocator.getEvenementService().traiterEvenement(messageDTO, evenementDTO, documentManager,
					dossierLink);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_TRAITER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return resetAndNavigate(Boolean.TRUE, messageDTO);

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_TRAITER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}

	}

	public String suivreTransitionEnCoursDeTraitement() throws ClientException {

		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		DossierLink dossierLink = null;

		MessageDTO messageDTO = navigationContext.getCurrentMessage();

		if (dossierDoc != null) {

			final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
			List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager, dossierDoc.getId());

			if (dossierLinkList.size() > 0) {
				dossierLink = dossierLinkList.get(0).getAdapter(DossierLink.class);
			}
		}

		try {
			SolonMgppServiceLocator.getEvenementService().enCoursDeTraitementEvenement(messageDTO, evenementDTO,
					documentManager, dossierLink);

			String message = resourcesAccessor.getMessages()
					.get(SolonMgppI18nConstant.EVENEMENT_EN_COURS_DE_TRAITEMENT);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return resetAndNavigate(Boolean.TRUE, messageDTO);

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_TRAITER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}

	}

	public String accepterVersion() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {
			SolonMgppServiceLocator.getEvenementService().accepterVersion(evenementDTO, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_ACCEPTER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			// return resetAndNavigate(Boolean.TRUE);
			return refreshEvenement();
		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_ACCEPTER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}

	}

	public String rejeterVersion() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {
			SolonMgppServiceLocator.getEvenementService().rejeterVersion(evenementDTO, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_REJETER);
			facesMessages.add(StatusMessage.Severity.INFO, message);
			return refreshEvenement();
			// return resetAndNavigate(Boolean.TRUE);

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_REJETER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}

	}

	public String accuserReceptionVersion() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {
			SolonMgppServiceLocator.getEvenementService().accuserReceptionVersion(evenementDTO, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_AR);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			// return resetAndNavigate(Boolean.TRUE);
			return refreshEvenement();
		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_AR);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}
	}

	public String abandonnerVersion() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {
			SolonMgppServiceLocator.getEvenementService().abandonnerVersion(evenementDTO, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_ABANDON);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return resetAndNavigate(Boolean.TRUE);

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_ABANDON);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}
	}

	public String annulerEvenement() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {
			SolonMgppServiceLocator.getEvenementService().annulerEvenement(evenementDTO, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_ANNULER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return resetAndNavigate(Boolean.TRUE);

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_ANNULER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}
	}

	public String creerAlerte() throws ClientException {

		EvenementDTO evenementDTO;
		if (navigationContext.getCurrentEvenement() != null) {
			evenementDTO = navigationContext.getCurrentEvenement();
		} else {
			return null;
		}

		final EvenementTypeService evenementTypeService = SolonMgppServiceLocator.getEvenementTypeService();
		EvenementTypeDescriptor evtParentTypeDescriptor = evenementTypeService.getEvenementType(evenementDTO
				.getTypeEvenementName());
		if (VocabularyConstants.CATEGORIE_EVENEMENT_PROCEDURE_LEGISLATIVE_VALUE.equals(evtParentTypeDescriptor
				.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE01;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_ORGANISATION_SESSION_EXTRAORDINAIRE_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE03;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_CONSULTATION_ASSEMBLEE_PROJET_NOMINATION_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE04;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_CONVOCATION_CONGRES_VALUE.equals(evtParentTypeDescriptor
				.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE05;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_DEMANDE_PROLONGATION_INTERVENTION_EXTERIEURE_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE06;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_RESOLUTION_ARTICLE_34_1_VALUE.equals(evtParentTypeDescriptor
				.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE07;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_DEPOT_RAPPORT_PARLEMENT_VALUE.equals(evtParentTypeDescriptor
				.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE08;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_INSERTION_INFORMATION_PARLEMENTAIRE_JO_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE09;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_ORGANISME_EXTRA_PARLEMENTAIRE_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE10;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE15;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_DECLARATION_DE_POLITIQUE_GENERALE_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE11;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE12;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_28_3C_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE13;
		} else if (VocabularyConstants.CATEGORIE_EVENEMENT_DEMANDE_AUDITION_PERSONNES_EMPLOIS_ENVISAGEE_VALUE
				.equals(evtParentTypeDescriptor.getProcedure())) {
			currentTypeEvenementSuccessif = TypeEvenementConstants.ALERTE14;
		}

		return navigateToCreationCommunicationSuccessive();
	}

	public String supprimerEvenement() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO == null) {
			return null;
		}

		try {

			if (!checkMetaDonnee(evenementDTO, publier)) {
				return null;
			}

			SolonMgppServiceLocator.getEvenementService().supprimerEvenement(evenementDTO, documentManager);

			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_SUPPRIMER);
			facesMessages.add(StatusMessage.Severity.INFO, message);

			return resetAndNavigate(Boolean.TRUE);

		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_SUPPRIMER);
			return logError(STLogEnumImpl.FAIL_UPDATE_COMM_TEC, message, e);
		}
	}

	public String initTransmettreParMail() {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		if (evenementDTO != null) {
			mailEvenement.clear();
			mailEvenement.put(MAIL_OBJET, evenementDTO.getTypeEvenementLabel() + " " + evenementDTO.getObjet());
		}

		mailPopupPanelVisible = Boolean.TRUE;

		return null;
	}

	public Boolean isMailPopupPanelVisible() {
		return mailPopupPanelVisible;
	}

	public String transmettreParMailEnvoyer() {

		String text = mailEvenement.get(MAIL_MESSAGE);
		String subject = mailEvenement.get(MAIL_OBJET);
		String addresses = mailEvenement.get(MAIL_DESTINATAIRE);

		mailPopupPanelVisible = Boolean.FALSE;

		try {
			SolonMgppServiceLocator.getMailService().sendMailEpp(text, subject, addresses, documentManager,
					navigationContext.getCurrentEvenement());
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_MAIL);
			facesMessages.add(StatusMessage.Severity.INFO, message);
			return null;
		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EVENEMENT_NON_MAIL);
			return logError(STLogEnumImpl.SEND_MAIL_TEC, message, e);
		}

	}

	public String transmettreParMailAnnuler() {
		mailPopupPanelVisible = Boolean.FALSE;
		return null;
	}

	public Map<String, String> getMailEvenement() {
		return mailEvenement;
	}

	public void setMailEvenement(Map<String, String> mailEvenement) {
		this.mailEvenement = mailEvenement;
	}

	public void setCurrentTypeEvenementSuccessif(String currentTypeEvenementSuccessif) {
		this.currentTypeEvenementSuccessif = currentTypeEvenementSuccessif;
	}

	public String getCurrentTypeEvenementSuccessif() {
		return currentTypeEvenementSuccessif;
	}

	public void setPublier(boolean publier) {
		this.publier = publier;
	}

	public boolean isPublier() {
		return publier;
	}

	public Boolean displayErrorMessage(FieldDefinition[] fieldDefinitions, String restrictionName, Widget widget) {
		Boolean required = metadonneesActions.isColumnRequired(restrictionName);
		if (required) {
			for (FieldDefinition fieldDefinition : fieldDefinitions) {
				String fieldName = fieldDefinition.getFieldName();
				if (requiredField == null) {
					requiredField = new HashSet<String>();
				}
				// register required field
				requiredField.add(fieldName);
			}
		}

		if (!publier || !BuiltinModes.isBoundToEditMode(widget.getMode()) || widget.isRequired() || !required) {
			return false;
		} else {
			EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
			if (evenementDTO == null) {
				return true;
			}
			for (FieldDefinition fieldDefinition : fieldDefinitions) {
				String fieldName = fieldDefinition.getFieldName();

				return !isRequiredFieldValid(evenementDTO, fieldName);
			}
		}

		return true;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Boolean isRequiredFieldValid(EvenementDTO evenementDTO, String fieldName) {
		Serializable value = null;
		String[] props = fieldName.split("\\.");
		for (String prop : props) {
			if (value == null) {
				value = evenementDTO.get(prop);
			} else {
				value = ((Map<String, Serializable>) value).get(prop);
			}
		}

		if (FIELD_NAME_NIVEAU_LECTURE_NIVEAU.equals(fieldName)) {
			// recuperation du codeLecture si autre que AN ou SENAT, le niveau n'est pas obligatoire
			String code = (String) ((Map<String, Serializable>) evenementDTO.get(EvenementDTO.NIVEAU_LECTURE))
					.get(EvenementDTO.NIVEAU_LECTURE_CODE);
			if (!(NiveauLectureCode.AN.value().equals(code) || NiveauLectureCode.SENAT.value().equals(code))) {
				return true;
			}
		}

		if (value == null) {
			return false;
		} else if (value instanceof String && StringUtils.isEmpty((String) value)) {
			return false;
		} else if (value instanceof Collection && ((Collection) value).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public String mettreEnAttente() throws ClientException {
		SolonMgppServiceLocator.getEvenementService().mettreEnAttenteRelancer(documentManager,
				navigationContext.getCurrentEvenement().getIdEvenement(), true);
		MessageDTO messageEnAttente = navigationContext.getCurrentMessage();
		messageEnAttente.setEnAttente(Boolean.TRUE);
		return resetAndNavigate(Boolean.TRUE, messageEnAttente);
	}

	public String relancerMessage() throws ClientException {
		SolonMgppServiceLocator.getEvenementService().mettreEnAttenteRelancer(documentManager,
				navigationContext.getCurrentEvenement().getIdEvenement(), false);
		MessageDTO messageRelance = navigationContext.getCurrentMessage();
		messageRelance.setEnAttente(Boolean.FALSE);
		return resetAndNavigate(Boolean.TRUE, messageRelance);
	}

	/**
	 * refresh evenement
	 * 
	 * @return
	 * @throws ClientException
	 */
	private String refreshEvenement() throws ClientException {
		corbeilleActions.refreshCorbeille();
		return corbeilleActions.refreshEvenement();
	}

	public Boolean isLiaisonOEPPopupPanelVisible() {
		return liaisonOEPPopupPanelVisible;
	}

	public String lierOEPConfirmer() {

		liaisonOEPPopupPanelVisible = Boolean.FALSE;

		try {

			FichePresentationOEP fpOEPSelected = SolonMgppServiceLocator.getDossierService()
					.findFicheOEPbyIdDossierEPP(documentManager, liaisonOEPEvt.get("OEP"));
			if (fpOEPSelected == null) {
				facesMessages.add(StatusMessage.Severity.ERROR,
						"Impossible de récupérer la fiche de présentation de l'OEP liée.");
			} else {
				// Ajout de l'OEP désigné dans la communication
				fpOEPSelected.addToIdsANATLies(navigationContext.getCurrentEvenement().getIdDossier());

				// suppression de la fiche de présentation de l'ancienne OEP
				FichePresentationOEP fpOEPOld = SolonMgppServiceLocator.getDossierService().findFicheOEP(
						documentManager, navigationContext.getCurrentEvenement().getIdDossier());
				if (fpOEPOld != null) {
					documentManager.removeDocument(fpOEPOld.getDocument().getRef());
				}
				documentManager.saveDocument(fpOEPSelected.getDocument());
				documentManager.save();

				Events.instance().raiseEvent(FichePresentationOEPActionsBean.REPLACE_FP_OEP_EVENT,
						fpOEPSelected.getDocument());

				facesMessages.add(StatusMessage.Severity.INFO,
						"La liaison avec l'organisme extra-parlementaire a été effectuée.");
			}

		} catch (ClientException e) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"Impossible de lier le dossier EPP à un organisme extra-parlementaire.");
		}

		return null;

	}

	public String lierOEPAnnuler() {
		liaisonOEPPopupPanelVisible = Boolean.FALSE;
		return null;
	}

	public void afficherPopupLiaisonOEP() {
		liaisonOEPPopupPanelVisible = Boolean.TRUE;
	}

	public Map<String, String> getLiaisonOEPEvt() {
		return liaisonOEPEvt;
	}

	public void setLiaisonOEPEvt(Map<String, String> liaisonOEPEvt) {
		this.liaisonOEPEvt = liaisonOEPEvt;
	}
}
