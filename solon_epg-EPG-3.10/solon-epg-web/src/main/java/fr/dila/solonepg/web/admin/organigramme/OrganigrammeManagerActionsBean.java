package fr.dila.solonepg.web.admin.organigramme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.jboss.annotation.ejb.SerializedConcurrentAccess;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.forms.layout.api.BuiltinModes;
import org.nuxeo.ecm.platform.forms.layout.api.BuiltinWidgetModes;
import org.nuxeo.ecm.platform.ui.web.component.list.UIEditableList;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.web.converter.IdMinistereToNorConverter;
import fr.dila.solonepg.web.converter.OrganigrammeMinIdToEditionConverter;
import fr.dila.solonepg.web.converter.OrganigrammeNorIdToLabelConverter;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.NorDirection;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.organigramme.UserNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.organigramme.UserNodeImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.administration.organigramme.OrganigrammeTreeBean;
import fr.dila.st.web.converter.OrganigrammeUserMailToLabelConverter;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * ActionBean de gestion de l'organigramme
 * 
 * @author FEO
 * @author asatre
 */
@Name("organigrammeManagerActions")
@SerializedConcurrentAccess
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class OrganigrammeManagerActionsBean extends fr.dila.ss.web.admin.organigramme.OrganigrammeManagerActionsBean {

	public static final String				NOR_TYPE					= "NOR_TYPE";
	public static final String				USER_MAIL_TYPE				= "USER_MAIL_TYPE";

	private static final long				serialVersionUID			= -3954647980426960266L;

	protected Map<String, String>			norDirectionMember;

	public Boolean							mailNotFound;

	private String							selectedNor;
	private List<OrganigrammeNode>			listNor;

	/*
	 * ABI : FEV 531 - quadruple appel de JSF pour la génération du CSV obligé de lui rajouter un variable "en cache"
	 * dans le bean pour feinter le 4eme appel qui reçoit un userModel null en paramètre
	 */
	private String							ministereDescriptionenCache	= "";
	private String							directionDescriptionenCache	= "";
	private String							postesDescriptionenCache	= "";

	@In(create = true, required = false)
	protected transient STLockActionsBean	stLockActions;

	private transient Converter				minOrgaConverter, norOrgaConverter, userMailOrgaConverter;
	
	/**
	 * Type de l'élément sélectionné
	 * <p>
	 */
	@RequestParameter
	protected String						idType;
	
	/**
	 * Id de l'entité sélectionnée précédée du préfixe correspondant au type d'entité.
	 */
	private String selectedPrefixedId;

	@Override
	public String getSelectedNodeId() {
		String selectedValue = selectedNodeId;
		if (idType != null && PREFIXED_ID_TYPE.equals(idType)) {
			if (DIR_TYPE.equals(organigrammeSelectionType)) {
				selectedValue = STConstant.PREFIX_US + selectedValue; 
			} else if (MIN_TYPE.equals(organigrammeSelectionType)) {
				selectedValue = STConstant.PREFIX_MIN + selectedValue; 
			} else if (POSTE_TYPE.equals(organigrammeSelectionType)) {
				selectedValue = STConstant.PREFIX_POSTE + selectedValue; 
			} else if (UST_TYPE.equals(organigrammeSelectionType)) {
				selectedValue = STConstant.PREFIX_US + selectedValue; 
			}
		}
		
		return selectedValue;
	}
	
	/**
	 * Renvoie le nor de direction pour l'id de ministere donné
	 * 
	 * @param ministereId
	 *            ministere
	 * @return
	 */
	public String getNorDirectionForMinistere(String ministereId) {

		UniteStructurelleNode uniteStructurelle;
		if (currentNode instanceof UniteStructurelleNode) {
			uniteStructurelle = (UniteStructurelleNode) currentNode;
		} else {
			return null;
		}

		return uniteStructurelle.getNorDirectionForMinistereId(ministereId);
	}

	/**
	 * Renvoie la lettre nor de direction pour un id de direction et un id de ministere donné
	 * 
	 * @param directionId
	 *            direction
	 * @param ministereId
	 *            direction
	 * @return
	 * @throws ClientException
	 */
	public String getNorDirectionForDirection(String directionId, String ministereId) throws ClientException {

		UniteStructurelleNode organigrammeNode = STServiceLocator.getSTUsAndDirectionService()
				.getUniteStructurelleNode(directionId);

		return organigrammeNode.getNorDirectionForMinistereId(ministereId);
	}

	/**
	 * Creation d'une unité structurelle
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public String createUniteStructurelle() throws ClientException {

		// copie de la liste des lettres nor
		UniteStructurelleNode uniteStructurelle = (UniteStructurelleNode) selectedNodeModel;
		if (norDirectionMember != null) {

			for (Entry<String, String> entry : norDirectionMember.entrySet()) {

				uniteStructurelle.setNorDirectionForMinistereId(entry.getKey(), entry.getValue());
			}
		}

		return super.createUniteStructurelle();
	}

	@Override
	public String updateUniteStructurelle() throws ClientException {

		if (!checkUniteStructurelleLockedByCurrentUser()) {
			return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
		}

		// copie de la liste des lettres nor
		UniteStructurelleNode uniteStructurelle = (UniteStructurelleNode) selectedNodeModel;

		if (uniteStructurelle.getType().equals(OrganigrammeType.DIRECTION)) {
			UniteStructurelleNode ustOriginal = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode(
					selectedNodeModel.getId());
			for (EntiteNode entite : ustOriginal.getEntiteParentList()) {
				if (!uniteStructurelle.getEntiteParentList().contains(entite)) {
					facesMessages.add(StatusMessage.Severity.ERROR,
							resourcesAccessor.getMessages().get("organigramme.edition.cannot.delete.entite"));
					return null;
				}
				if (!uniteStructurelle.getNorDirectionForMinistereId(entite.getId()).equals(
						norDirectionMember.get(entite.getId()))) {
					facesMessages.add(StatusMessage.Severity.ERROR,
							resourcesAccessor.getMessages().get("organigramme.edition.cannot.modify.nor"));
					return null;
				}
			}
		}

		if (norDirectionMember != null) {

			for (Entry<String, String> entry : norDirectionMember.entrySet()) {

				uniteStructurelle.setNorDirectionForMinistereId(entry.getKey(), entry.getValue());
			}
		}

		return super.updateUniteStructurelle(uniteStructurelle);
	}

	/**
	 * @return the norDirectionMember
	 */
	public Map<String, String> getNorDirectionMember() {
		if (norDirectionMember == null) {
			norDirectionMember = new HashMap<String, String>();
		}

		UniteStructurelleNode uniteStructurelle = (UniteStructurelleNode) selectedNodeModel;

		List<NorDirection> norDirList = uniteStructurelle.getNorDirectionList();
		for (NorDirection norDir : norDirList) {
			norDirectionMember.put(norDir.getId(), norDir.getNor());
		}

		return norDirectionMember;
	}

	/**
	 * @param norDirectionMember
	 *            the norDirectionMember to set
	 */
	public void setNorDirectionMember(Map<String, String> norDirectionMember) {
		this.norDirectionMember = norDirectionMember;
	}

	@Override
	public void resetNodeModel() {
		super.resetNodeModel();
		norDirectionMember = null;
	}

	@Override
	public String getOrganigrammeNodeLabel(String selectionType, String id) throws ClientException {
		if (USER_TYPE.equals(selectionType)) {
			UserNode node = STServiceLocator.getOrganigrammeService().getUserNode(id);
			if (node == null) {
				// saisie libre
				node = new UserNodeImpl();
				node.setLabel(id);
				node.setId(id);
			}
			return node.getLabel();
		} else if (NOR_TYPE.equals(selectionType)) {
			EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(id);

			return node.getNorMinistere();

		} else {
			return super.getOrganigrammeNodeLabel(selectionType, id);
		}
	}

	@Override
	public Converter getOrganigrammeConverter(String type) {
		if (MIN_TYPE.equals(type)) {
			if (minOrgaConverter == null) {
				minOrgaConverter = new OrganigrammeMinIdToEditionConverter();
			}
			return minOrgaConverter;
		} else if (NOR_TYPE.equals(type)) {
			if (norOrgaConverter == null) {
				norOrgaConverter = new OrganigrammeNorIdToLabelConverter();
			}
			return norOrgaConverter;
		} else if (USER_MAIL_TYPE.equals(type)) {
			if (userMailOrgaConverter == null) {
				userMailOrgaConverter = new OrganigrammeUserMailToLabelConverter();
			}
			return userMailOrgaConverter;
		} else {
			return super.getOrganigrammeConverter(type);
		}
	}

	@Override
	public Boolean contains(String selectionType, String type) {
		if (NOR_TYPE.equals(selectionType) && MIN_TYPE.equals(type)) {
			return true;
		} else {
			return super.contains(selectionType, type);
		}
	}

	/**
	 * Adds selection from selector as a list element
	 * <p>
	 * Must pass request parameter "organigrammeSelectionListId" holding the binding to model. Selection will be
	 * retrieved using the {@link #getSelectedValue()} method.
	 */
	public void addSelectionMailToList(ActionEvent event) {
		UIComponent component = event.getComponent();
		if (component == null) {
			return;
		}
		UIComponent base = ComponentUtils.getBase(component);
		UIEditableList list = ComponentUtils.getComponent(base, organigrammeSelectionListId, UIEditableList.class);

		if (list != null) {
			// add selected value to the list
			String selectedValue = getSelectedNodeId();
			Converter converter = new OrganigrammeUserMailToLabelConverter();

			selectedValue = converter.getAsString(null, null, selectedValue);
			if (selectedValue == null || selectedValue.isEmpty()) {
				mailNotFound = true;
			} else {
				list.addValue(selectedValue);
				mailNotFound = false;
			}
		}
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_ORGANIGRAMME_READER));
	}

	/**
	 * Controle l'accès en écriture
	 * 
	 */
	public boolean isUpdateAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(STBaseFunctionConstant.ORGANIGRAMME_UPDATER) || ssPrincipal
				.isMemberOf(STBaseFunctionConstant.ORGANIGRAMME_MINISTERE_UPDATER));
	}

	/**
	 * Controle le droit d'injection de gouverment
	 * 
	 */
	public boolean canInjectGovernment() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(STBaseFunctionConstant.ORGANIGRAMME_UPDATER));
	}

	@Override
	public String unlockNode() throws ClientException {
		loadSelectedNodeModel();
		unlockOrganigrammeNode(getSelectedNodeModel());
		Events.instance().raiseEvent(OrganigrammeTreeBean.ORGANIGRAMME_CHANGED_EVENT);
		return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
	}

	public Boolean getMailNotFound() {
		return mailNotFound;
	}

	public void setMailNotFound(Boolean mailNotFound) {
		this.mailNotFound = mailNotFound;
	}

	public void setSelectedNor(String selectedNor) {
		this.selectedNor = selectedNor;
	}

	public String getSelectedNor() {
		return selectedNor;
	}

	public boolean isNorLetterMandatory() {
		UniteStructurelleNode uniteStructurelle = (UniteStructurelleNode) selectedNodeModel;
		OrganigrammeType selectedUstType = uniteStructurelle.getType();
		if (selectedUstType != null && OrganigrammeType.DIRECTION.equals(selectedUstType)) {
			return true;
		}
		return false;
	}

	public void selectUstType(ValueChangeEvent event) {
		String type = (String) event.getNewValue();
		UniteStructurelleNode uniteStructurelle = (UniteStructurelleNode) selectedNodeModel;
		uniteStructurelle.setType(OrganigrammeType.getEnum(type));
	}

	public void entiteNorLettersValidator(FacesContext context, UIComponent component, Object value) {
		if (!(value instanceof String) || StringUtils.isEmpty(((String) value).trim())
				|| ((String) value).length() != 3) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ComponentUtils.translate(context,
					"organigramme.creation.invalid.nor"), null);
			// also add global message
			context.addMessage(null, message);
			throw new ValidatorException(message);
		}
	}

	/* renvoie la liste des directions sous forme de String de l'utilisateur en paramètres */
	public String getDirectionDescription(DocumentModel userModel) throws Exception {
		if (userModel == null) {
			// voir commentaire sur la variable "enCache"
			return directionDescriptionenCache;
		} else {
			directionDescriptionenCache = getDocNodeDescription(userModel,
					STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR);
			return directionDescriptionenCache;
		}
	}

	/* renvoie la liste des postes sous forme de String de l'utilisateur en paramètres */
	public String getPosteDescription(DocumentModel userModel) throws Exception {
		if (userModel == null) {
			// voir commentaire sur la variable "enCache"
			return postesDescriptionenCache;
		} else {
			postesDescriptionenCache = getDocNodeDescription(userModel, STConstant.ORGANIGRAMME_POSTE_DIR);
			return postesDescriptionenCache;
		}
	}

	/* renvoie la liste des ministères sous forme de String de l'utilisateur en paramètres */
	public String getMinistereDescription(DocumentModel userModel) throws Exception {
		if (userModel == null) {
			// voir commentaire sur la variable "enCache"
			return ministereDescriptionenCache;
		} else {
			ministereDescriptionenCache = getDocNodeDescription(userModel, STConstant.ORGANIGRAMME_ENTITE_DIR);
			return ministereDescriptionenCache;
		}
	}

	protected String getDocNodeDescription(DocumentModel userModel, String nodeType) throws ClientException {
		// reload user to load posteId
		userModel = STServiceLocator.getUserManager().getUserModel(userModel.getId());
		STUser stUser = userModel.getAdapter(STUser.class);
		final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		final STPostesService postesService = STServiceLocator.getSTPostesService();

		Set<String> list = new HashSet<String>();
		for (String idPoste : stUser.getPostes()) {
			if (STConstant.ORGANIGRAMME_ENTITE_DIR.equals(nodeType)) {
				for (OrganigrammeNode node : ministeresService.getMinistereParentFromPoste(idPoste)) {
					list.add(node.getLabel());
				}
			} else if (STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR.equals(nodeType)) {
				for (OrganigrammeNode node : usService.getDirectionFromPoste(idPoste)) {
					list.add(node.getLabel());
				}
			} else if (STConstant.ORGANIGRAMME_POSTE_DIR.equals(nodeType)) {
				list.add(postesService.getPoste(idPoste).getLabel());
			}
		}
		return StringUtil.join(list.toArray(new String[list.size()]), ", ", "");
	}

	/**
	 * EpgEntiteNode id to NOR converter
	 * 
	 * @return
	 */
	public static Converter getIdMinistereToNorConverter() {
		return new IdMinistereToNorConverter();
	}

	/**
	 * UniteStructurelleNode id to Label converter
	 * 
	 * @return
	 */
	public static Converter getIdDirectionConverter() {
		return new Converter() {

			@Override
			public String getAsString(FacesContext context, UIComponent component, Object value) {

				if (value instanceof String) {
					OrganigrammeNode node;
					try {
						node = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode((String) value);
					} catch (ClientException e) {
						// node non trouvé
						node = null;
					}
					return node == null ? (String) value : node.getLabel();
				}
				return " ";
			}

			@Override
			public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
				return " ";
			}
		};
	}

	public List<OrganigrammeNode> getListNor() throws ClientException {
		if (this.listNor == null) {
			this.listNor = loadMinistere();
		}
		return this.listNor;

	}

	private List<OrganigrammeNode> loadMinistere() throws ClientException {
		List<OrganigrammeNode> list = new ArrayList<OrganigrammeNode>();

		GouvernementNode gvtNode = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();

		if (gvtNode != null) {
			list = STServiceLocator.getOrganigrammeService().getChildrenList(documentManager, gvtNode, Boolean.TRUE);
		}
		return list;

	}

	public List<OrganigrammeNode> getDirectionFromNorMinistere(String idMinistere) throws ClientException {
		List<OrganigrammeNode> list = new ArrayList<OrganigrammeNode>();
		EntiteNode node = (EntiteNode) STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
		if (node != null) {
			list = STServiceLocator.getOrganigrammeService().getChildrenList(documentManager, node, Boolean.TRUE);
		}
		return list;
	}

	/**
	 * Renvoi le mode d'affichage pour le champ
	 * 
	 * @param mode
	 * @return
	 */
	public String getModePosteFields(String mode) {
		if (mode.equals(BuiltinModes.EDIT) || mode.equals(BuiltinModes.CREATE)) {
			if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.POSTE_FIELDS_UPDATER)) {
				return BuiltinModes.EDIT;
			} else if (ssPrincipal.isMemberOf(STBaseFunctionConstant.PROFIL_SGG)) {
				return BuiltinModes.VIEW;
			}
		} else if (mode.equals(BuiltinModes.VIEW)
				&& (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.POSTE_FIELDS_UPDATER) || ssPrincipal
						.isMemberOf(STBaseFunctionConstant.PROFIL_SGG))) {
			return BuiltinModes.VIEW;
		}
		return BuiltinWidgetModes.HIDDEN;
	}

	public List<EntiteNode> getMinisteres() throws ClientException {
		return STServiceLocator.getSTMinisteresService().getMinisteres(Boolean.TRUE);

	}

	public String getSelectedPrefixedId() {
		return selectedPrefixedId;
	}

	public void setSelectedPrefixedId(String selectedPrefixedId) {
		this.selectedPrefixedId = selectedPrefixedId;
	}
}