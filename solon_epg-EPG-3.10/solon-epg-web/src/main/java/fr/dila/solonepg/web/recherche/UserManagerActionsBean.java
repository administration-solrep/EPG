package fr.dila.solonepg.web.recherche;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.directory.sql.PasswordHelper;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.action.LogoutAction;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.event.helper.DocumentSearchEventHelper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.documentmodel.SSInfoUtilisateurConnection;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.security.principal.STPrincipal;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STUserManager;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.recherche.LDAPElement;
import fr.dila.st.core.recherche.UserRequeteur;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.user.STPrincipalImpl;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.administration.profil.ProfileManagerActionsBean;

/**
 * Bean de gestion des utilisateurs.
 * 
 * @author asatre
 */
@Name("userManagerActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION)
public class UserManagerActionsBean extends fr.dila.ss.web.admin.utilisateur.UserManagerActionsBean {

	private static final STLogger						LOGGER				= STLogFactory
																					.getLog(UserManagerActionsBean.class);

	private static final long							serialVersionUID	= -1519245411014377549L;

	/**
	 * utilisé uniquement pour la validation du changement de mot de passe.
	 */
	protected String									userPassword;

	/**
	 * search fields
	 */
	protected String									currentProfil;

	protected String									poste;

	protected String									fonction;

	/**
	 * Liste de tous les profils.
	 */
	protected List<DocumentModel>						profils;

	/**
	 * Liste des profils pour la création d'utilisateurs.
	 */
	protected List<DocumentModel>						profilListForUserCreate;

	private UserRequeteur								userRequeteur;

	/**
     * 
     */
	protected boolean									passwordValid		= false;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true)
	protected transient ProfileManagerActionsBean		profileManagerActions;

	@In(create = true)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@In(create = true, required = false)
	protected transient UserWorkspaceManagerActionsBean	userWorkspaceManagerActions;

	@In(create = true)
	protected transient NavigationWebActionsBean		navigationWebActions;

	private String										currentUserSubTab;

	protected Date										dateDebutMin;
	protected Date										dateDebutMax;
	protected Date										dateFinMin;
	protected Date										dateFinMax;

	protected String									errorName;

	/**
	 * Permet de se souvenir quel utilisateur est selectionné, ce qui évite le problème de la mauvaise redirection lors
	 * de l'édition d'un user qui renvoit (sans cette valeur) au premier utilisateur de la liste
	 * **/
	protected DocumentModel								realSelectedUser;

	/**
	 * Default constructor
	 */
	public UserManagerActionsBean() {
		super();
		displayCancelButton = true;
	}

	@Override
	public String searchUser() throws ClientException {

		search();

		if (userRequeteur.isEmpty()) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("favoris.recherche.query.vide"));
			return SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR;
		} else {
			return goToResults();
		}
	}

	private void setUserRequeteur() throws ClientException {
		userRequeteur = new UserRequeteur();

		if (StringUtil.isNotBlank(username)) {
			userRequeteur
					.setFilterValue(new LDAPElement(UserRequeteur.ID_USERNAME, UserRequeteur.QUERY_EQUAL, username));
		}
		if (StringUtil.isNotBlank(firstName)) {
			userRequeteur.setFilterValue(new LDAPElement(UserRequeteur.ID_FIRST_NAME, UserRequeteur.QUERY_EQUAL,
					firstName));
		}
		if (StringUtil.isNotBlank(lastName)) {
			userRequeteur.setFilterValue(new LDAPElement(UserRequeteur.ID_LAST_NAME, UserRequeteur.QUERY_EQUAL,
					lastName));
		}
		if (dateDebutMin != null) {
			userRequeteur.setFilterValue(UserRequeteur.ID_DATE_DEBUT_MIN, UserRequeteur.QUERY_SUP, dateDebutMin);
		}
		if (dateDebutMax != null) {
			userRequeteur.setFilterValue(UserRequeteur.ID_DATE_DEBUT_MAX, UserRequeteur.QUERY_INF, dateDebutMax);
		}
		if (dateFinMin != null) {
			userRequeteur.setFilterValue(UserRequeteur.ID_DATE_FIN_MIN, UserRequeteur.QUERY_SUP, dateFinMin);
		}
		if (dateFinMax != null) {
			userRequeteur.setFilterValue(UserRequeteur.ID_DATE_FIN_MAX, UserRequeteur.QUERY_INF, dateFinMax);
		}

		if (StringUtil.isNotBlank(currentProfil)) {
			userRequeteur.setUserProfil(currentProfil);
		}
		if (StringUtil.isNotBlank(fonction)) {
			userRequeteur.setUserFonction(fonction);
		}
		if (StringUtil.isNotBlank(ministere)) {
			userRequeteur.setUserMinistere(ministere);
		}
		if (StringUtil.isNotBlank(direction)) {
			userRequeteur.setUserDirection(direction);
		}
		if (StringUtil.isNotBlank(poste)) {
			userRequeteur.setUserPoste(poste);
		}
	}

	private void search() throws ClientException {
		setUserRequeteur();
		setUsersList(userRequeteur.searchUsers());
	}

	public String getCurrentProfil() {
		return currentProfil;
	}

	public void setCurrentProfil(String currentProfil) {
		this.currentProfil = currentProfil;
	}

	/**
	 * Retourne la liste de tous les profils.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<DocumentModel> getProfils() throws ClientException {
		if (profils == null) {
			final ProfileService profileService = STServiceLocator.getProfileService();
			profils = profileService.findAllProfil();
		}
		return profils;
	}

	/**
	 * Retourne la liste des profils que l'utilisateur a le droit d'affecter.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<DocumentModel> getProfilListForUserCreate() throws ClientException {
		if (profilListForUserCreate == null) {
			profilListForUserCreate = new ArrayList<DocumentModel>();

			STPrincipal principal = (STPrincipal) documentManager.getPrincipal();

			final ProfileService profileService = STServiceLocator.getProfileService();
			profilListForUserCreate = profileService.getProfilListForUserCreate(principal);
		}
		return profilListForUserCreate;
	}

	public void setProfils(DocumentModelList profils) {
		this.profils = profils;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	@Override
	public String resetSearch() {
		routingWebActions.setFeuilleRouteView(null);
		searchString = null;
		searchUserModel = null;
		realSelectedUser = null;

		super.resetSearch();
		fonction = null;
		poste = null;
		profils = null;
		currentProfil = null;
		dateDebutMin = null;
		dateDebutMax = null;
		dateFinMin = null;
		dateFinMax = null;
		return SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR;
	}

	public String getSearchQuery() {
		if (userRequeteur != null) {
			return userRequeteur.getTranslatedQuery();
		} else {
			return null;
		}
	}

	public String getQueryPart() throws ClientException {
		setUserRequeteur();
		return userRequeteur.getQuery(UserRequeteur.QUERY_AND);
	}

	public String goToResults() {
		return SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTAT_UTILISATEUR;
	}

	public void setQueryPart(String queryPart) throws ClientException {
		userRequeteur = new UserRequeteur(queryPart);
		setUsersList(userRequeteur.searchUsers());
		try {
			remapProperty();
		} catch (ParseException e) {
			throw new ClientException(e);
		}
	}

	private void remapProperty() throws ParseException {
		if (userRequeteur != null) {
			username = userRequeteur.get(UserRequeteur.ID_USERNAME);
			firstName = userRequeteur.get(UserRequeteur.ID_FIRST_NAME);
			lastName = userRequeteur.get(UserRequeteur.ID_LAST_NAME);

			currentProfil = userRequeteur.get(UserRequeteur.ID_USER_PROFILS);
			fonction = userRequeteur.get(UserRequeteur.ID_USER_FUNCTION);
			ministere = userRequeteur.get(UserRequeteur.ID_USER_MINISTERE);
			direction = userRequeteur.get(UserRequeteur.ID_USER_DIRECTION);
			poste = userRequeteur.get(UserRequeteur.ID_USER_POSTE);

			SimpleDateFormat dateFormat = DateUtil.simpleDateFormat("yyyyMMddHHmmss'Z'");
			dateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
			String sDate = userRequeteur.get(UserRequeteur.ID_DATE_DEBUT_MIN);
			if (sDate != null) {
				dateDebutMin = dateFormat.parse(sDate);
			}
			sDate = userRequeteur.get(UserRequeteur.ID_DATE_DEBUT_MAX);
			if (sDate != null) {
				dateDebutMax = dateFormat.parse(sDate);
			}
			sDate = userRequeteur.get(UserRequeteur.ID_DATE_FIN_MIN);
			if (sDate != null) {
				dateFinMin = dateFormat.parse(sDate);
			}
			sDate = userRequeteur.get(UserRequeteur.ID_DATE_FIN_MAX);
			if (sDate != null) {
				dateFinMax = dateFormat.parse(sDate);
			}
		}
	}

	public String viewCurrentUserFromRecherche(DocumentModelImpl userModel) throws ClientException {
		fromRecherche = true;
		selectedUser = refreshUser(userModel.getId());
		realSelectedUser = refreshUser(userModel.getId());
		String result = super.viewUser();

		if (SolonEpgActionConstant.ESPACE_RECHERCHE.equals(navigationWebActions.getCurrentMainMenuAction().toString())) {
			// Lève un événement de chargement d'un utilisateur depuis la recherche
			fireDocumentSearchChangedEvent(realSelectedUser);
		}

		return result;
	}

	public String setCurrentUser(DocumentModel userModel) throws ClientException {
		selectedUser = refreshUser(userModel.getId());
		realSelectedUser = refreshUser(userModel.getId());
		currentUserSubTab = "view";
		if (SolonEpgActionConstant.ESPACE_RECHERCHE.equals(navigationWebActions.getCurrentMainMenuAction().toString())) {
			// event de chargement d'un utilisateur
			fireDocumentSearchChangedEvent(realSelectedUser);
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		}
		return null;
	}

	public String goBackToRecherche() {
		realSelectedUser = null;
		if (SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR.equals(routingWebActions.getFeuilleRouteView())) {
			return goToResults();
		} else {
			return "view_recherche_utilisateur";
		}
	}

	@Override
	public String viewUsers() throws ClientException {
		realSelectedUser = null;
		currentUserSubTab = "view";
		if (SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR.equals(routingWebActions.getFeuilleRouteView())
				|| SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTAT_UTILISATEUR.equals(routingWebActions
						.getFeuilleRouteView())) {
			return goToResults();
		} else if (SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR_RESULTATS_CONSULTES.equals(routingWebActions
				.getFeuilleRouteView())) {
			return SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR_RESULTATS_CONSULTES;
		} else if (SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR_FAVORIS_CONSULTATIONS.equals(routingWebActions
				.getFeuilleRouteView())) {
			return SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR_FAVORIS_CONSULTATIONS;
		} else {
			return super.viewUsers();
		}
	}

	@Override
	public String viewUser() throws ClientException {
		if (navigationWebActions.getCurrentMainMenuAction() != null
				&& SolonEpgActionConstant.ESPACE_RECHERCHE.equals(navigationWebActions.getCurrentMainMenuAction()
						.toString())) {
			// si dans l'espace de recherche on met a jour les utilisateurs consultés
			fireDocumentSearchChangedEvent(selectedUser);
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		}
		return viewUser(selectedUser, true);
	}

	private void fireDocumentSearchChangedEvent(DocumentModel selectedUserDoc) throws ClientException {
		// Lève un événement de chargement d'un utilisateur depuis la recherche
		final String resultatConsulteRootPath = userWorkspaceManagerActions.getCurrentUserPersonalWorkspace()
				.getPathAsString();
		DocumentSearchEventHelper.raiseEvent(documentManager, selectedUserDoc, resultatConsulteRootPath);
	}

	@Override
	public String clearSearch() throws ClientException {
		routingWebActions.setFeuilleRouteView(null);
		searchString = null;
		searchUserModel = null;
		realSelectedUser = null;
		return searchUsers();
	}

	public void resetAll() {
		realSelectedUser = null;
		currentUserSubTab = "view";
		resetSearch();
	}

	public DocumentModel getRealSelectedUser() {
		return realSelectedUser;
	}

	@Override
	public DocumentModel getSelectedUser() {
		if (realSelectedUser != null) {
			return realSelectedUser;
		}
		return super.getSelectedUser();
	}

	public void setUsersList(List<DocumentModel> userListPr) {
		users = new DocumentModelListImpl();
		for (DocumentModel documentModel : userListPr) {
			STUser user = null;
			if (documentModel != null) {
				user = documentModel.getAdapter(STUser.class);
				if (user != null) {
					try {
						// Recherche des informations de connexion utilisateur en BDD car celles du LDAP ne sont pas
						// correctes
						SSInfoUtilisateurConnection iuc = SSServiceLocator.getUtilisateurConnectionMonitorService()
								.getInfoUtilisateurConnection(documentManager, user.getUsername());
						if (iuc != null) {
							user.setDateLastConnection(iuc.getDateConnection());
						} else if (iuc == null) {
							user.setDateLastConnection(null);
						}
					} catch (ClientException e) {
						LOGGER.debug(STLogEnumImpl.FAIL_GET_USER_TEC,
								"Erreur lors de la tentative de récupération en BDD des informations utilisateurs, pour le document "
										+ documentModel != null ? documentModel.getId() : "");
						user.setDateLastConnection(null);
					}
					
					users.add(user.getDocument());
				}
			}
		}
	}

	// Méthodes liées au changement de mot de passe
	/**
	 * Récupère le mot de passe saisi par l'utilisateur.
	 * 
	 * @return userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Définit le mot de passe saisi par l'utilisateur.
	 * 
	 * @param userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * Reset des mots de passes saisis par l'utilisateur
	 */
	public void clearPasswordFields() {
		this.userPassword = null;
		this.newUserPasswordFirst = null;
		this.newUserPasswordSecond = null;
		this.selectedUser = null;
	}

	/**
	 * Validation des mots de passes saisis par l'utilisateur.
	 * 
	 * @param context
	 * @param component
	 * @param value
	 */
	public void validatePasswordFull(FacesContext context, UIComponent component, Object value) {
		// validation de l'ancien mot de passe
		passwordValid = false;
		setErrorName("");
		Map<String, Object> attributes = component.getAttributes();
		String oldPasswordInputId = (String) attributes.get("oldPasswordInputId");
		if (oldPasswordInputId == null) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_VALIDATE_PASSWORDS_TEC, " Input(s) not found");
			return;
		}

		UIInput oldPasswordComp = (UIInput) component.findComponent(oldPasswordInputId);
		if (oldPasswordComp == null) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_VALIDATE_PASSWORDS_TEC, " Input(s) not found");
			return;
		}

		Object oldPassword = oldPasswordComp.getLocalValue();

		if (oldPassword == null) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_VALIDATE_PASSWORDS_TEC, " Value(s) not found");
			return;
		}

		// vérification du mot de passe
		UserManager userManager = STServiceLocator.getUserManager();
		if (currentUser != null && currentUser.getName() != null
				&& !userManager.authenticate(currentUser.getName(), (String) oldPassword)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ComponentUtils.translate(context,
					"label.userManager.password.false"), null);
			setErrorName(message.getSummary());
			throw new ValidatorException(message);
		}

		// validation du nouveau mot de passe
		try {
			super.validatePassword(context, component, value);
		} catch (ValidatorException e) {
			setErrorName(e.getFacesMessage().getSummary());
			throw new ValidatorException(e.getFacesMessage());
		}

		// si aucune erreur de validation n'est détecté le mot de passe est considéré comme valide
		passwordValid = true;
	}

	/**
	 * Change le mot de passe utilisateur par "newUserPasswordFirst" sans renvoyer l'utilisateur à la page de login.
	 * 
	 * @return String
	 * @throws ClientException
	 */
	public String forcedChangePasswordWithoutLogout() throws ClientException {
		if (passwordValid) {
			if (!newUserPasswordFirst.equals(newUserPasswordSecond)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("warn.user.password.notEqual"));
				return null;
			}
			
			// on sélectionne l'utilisateur courant
			this.selectedUser = refreshUser(currentUser.getName());
			STUser user = selectedUser.getAdapter(STUser.class);
			if (user != null) {
				try {
					final ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator
							.getProfilUtilisateurService();
					ProfilUtilisateur profilUtilisateur = null;
					DocumentModel profilDoc = profilUtilisateurService.getOrCreateUserProfilFromId(documentManager,
							user.getUsername());
					if (profilDoc != null) {
						profilUtilisateur = profilDoc.getAdapter(ProfilUtilisateur.class);
					}
					if (profilUtilisateur != null) {
						// Mise à jour de la date
						profilUtilisateur.setDernierChangementMotDePasse(Calendar.getInstance());
						documentManager.saveDocument(profilUtilisateur.getDocument());
						documentManager.save();
					}
				} catch (Exception e) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_PROFIL_UTILISATEUR_TEC);
				}

				if (!PasswordHelper.isHashed(newUserPasswordFirst)) {
					user.setPassword(PasswordHelper.hashPassword(newUserPasswordFirst, PasswordHelper.SSHA));
					STServiceLocator.getSTPersistanceService().saveCurrentPassword(
							PasswordHelper.hashPassword(newUserPasswordFirst, PasswordHelper.SSHA), user.getUsername());
				}

				user.setPwdReset(false);
				((STUserManager) userManager).updateUserData(user.getDocument());

				final JournalService journalService = STServiceLocator.getJournalService();

				String comment = "Modification d'un utilisateur [" + user.getUsername() + "]";
				journalService.journaliserActionAdministration(documentManager, "user_modified", comment);
			}

			clearPasswordFields();
			// On garde le même fonctionnement qu'avant même si c'est contraire au nom de la fonction
			try {
				LogoutAction.logout();
			} catch (Exception e) {
				throw new RuntimeException("Logout failure", e);
			}
		}
		return null;
	}

	@Override
	public String changePassword() throws ClientException {
		// Ajout de la date de changement du mot de passe
		selectedUser = realSelectedUser;
		STUser user = selectedUser.getAdapter(STUser.class);
		if (user != null) {
			try {
				SolonEpgServiceLocator.getProfilUtilisateurService().changeDatePassword(documentManager,
						user.getUsername());
			} catch (Exception e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_PROFIL_UTILISATEUR_TEC);
			}
		}
		return super.changePassword();
	}

	// réparé à cause d'un problème d'héritage
	@Override
	public String forcedChangePassword() throws ClientException {
		if (!newUserPasswordFirst.equals(newUserPasswordSecond)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("warn.user.password.notEqual"));
			return null;
		}

		STUser user = selectedUser.getAdapter(STUser.class);
		if (user != null) {
			try {
				final ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator
						.getProfilUtilisateurService();
				ProfilUtilisateur profilUtilisateur = null;
				DocumentModel profilDoc = profilUtilisateurService.getOrCreateUserProfilFromId(documentManager,
						user.getUsername());
				if (profilDoc != null) {
					profilUtilisateur = profilDoc.getAdapter(ProfilUtilisateur.class);
				}
				if (profilUtilisateur != null) {
					// Mise à jour de la date
					profilUtilisateur.setDernierChangementMotDePasse(Calendar.getInstance());
					documentManager.saveDocument(profilUtilisateur.getDocument());
					documentManager.save();
				}
			} catch (Exception e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_PROFIL_UTILISATEUR_TEC);
			}

			if (!PasswordHelper.isHashed(newUserPasswordFirst)) {
				user.setPassword(PasswordHelper.hashPassword(newUserPasswordFirst, PasswordHelper.SSHA));
				STServiceLocator.getSTPersistanceService().saveCurrentPassword(
						PasswordHelper.hashPassword(newUserPasswordFirst, PasswordHelper.SSHA), user.getUsername());
			}

			user.setPwdReset(false);
		}
		return super.changePassword();
	};

	public Date getDateDebutMin() {
		return DateUtil.copyDate(dateDebutMin);
	}

	public void setDateDebutMin(Date dateDebutMin) {
		this.dateDebutMin = DateUtil.copyDate(dateDebutMin);
	}

	public Date getDateDebutMax() {
		return DateUtil.copyDate(dateDebutMax);
	}

	public void setDateDebutMax(Date dateDebutMax) {
		this.dateDebutMax = DateUtil.copyDate(dateDebutMax);
	}

	public Date getDateFinMin() {
		return DateUtil.copyDate(dateFinMin);
	}

	public void setDateFinMin(Date dateFinMin) {
		this.dateFinMin = DateUtil.copyDate(dateFinMin);
	}

	public Date getDateFinMax() {
		return DateUtil.copyDate(dateFinMax);
	}

	public void setDateFinMax(Date dateFinMax) {
		this.dateFinMax = DateUtil.copyDate(dateFinMax);
	}

	public String updateUserEpg() throws ClientException {
		selectedUser = realSelectedUser;
		updateUser();
		setCurrentUser(realSelectedUser);
		return null;
	}

	public String changePasswordEpg() throws ClientException {
		selectedUser = realSelectedUser;
		changePassword();
		return null;
	}

	public String deleteUserEpg() throws ClientException {
		selectedUser = realSelectedUser;
		if (getCanDeleteUser(selectedUser.getId())) {
			deleteUser();
			resetAll();
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		} else {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("warn.user.cannot.delete"));
		}
		return null;
	}

	public void setCurrentUserSubTab(String currentUserSubTab) {
		this.currentUserSubTab = currentUserSubTab;
	}

	public String getCurrentUserSubTab() {
		return currentUserSubTab;
	}

	protected boolean getCanDeleteUser(String userId) throws ClientException {

		TableReferenceService tableService = SolonEpgServiceLocator.getTableReferenceService();
		DocumentModel tableDoc = tableService.getTableReference(documentManager);
		if (tableDoc != null) {
			TableReference tableRef = tableDoc.getAdapter(TableReference.class);
			List<String> vecteurPub = tableRef.getVecteurPublicationMultiples();
			List<String> cabinetPMList = tableRef.getCabinetPM();
			List<String> chargesMissionList = tableRef.getChargesMission();
			List<String> signataireList = tableRef.getSignataires();
			if (vecteurPub.contains(userId) || cabinetPMList.contains(userId) || chargesMissionList.contains(userId)
					|| signataireList.contains(userId)) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	@Override
	public String deleteUser() throws ClientException {
		if (getCanDeleteUser(selectedUser.getId())) {
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			return super.deleteUser();
		} else {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("warn.user.cannot.delete"));
		}
		return null;
	}

	/**
	 * Renvoie la taille maximale du login utilisateur géré dans l'application.
	 * 
	 * @return la taille maximale du login utilisateur géré dans l'application.
	 */
	public int getUserNameMaxLength() {
		return MailboxService.MAX_USERNAME_LENGTH;
	}

	@Override
	public String updateUser() throws ClientException {
		try {
			if (realSelectedUser != null) {
				selectedUser = realSelectedUser;
			}
			final STPostesService postesService = STServiceLocator.getSTPostesService();
			// Vérifie si les postes supprimés du profil de l'utilisateur sont vides
			STUser user = userManager.getUserModel(selectedUser.getId()).getAdapter(STUser.class);
			List<String> emptyPoste = new ArrayList<String>();
			List<String> postesSaved = user.getPostes();
			List<String> postesModified = userPostes;
			if (postesModified != null) {
				if (postesSaved != null) {
					for (String posteId : postesSaved) {
						if (!postesModified.contains(posteId)) {
							PosteNode posteNode = postesService.getPoste(posteId);
							if (posteNode.getUserListSize() == 1) {
								emptyPoste.add(posteNode.getLabel());
							}
						}
					}
				}

				if (emptyPoste.size() != 0) {
					facesMessages.add(
							StatusMessage.Severity.WARN,
							resourcesAccessor.getMessages().get("warn.user.poste.empty")
									+ StringUtil.join(emptyPoste, ", "));
				}
			}

			if (userProfils == null) {
				loadUserProfils();
			}

			boolean profilsOk = addProfilesToUser(selectedUser);
			if(!profilsOk) {
				facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get("error.userManager.emptyProfils"));
				return null;
			}
			
			((STUserManager) userManager).updateUser(selectedUser, postesModified);
			facesMessages.add(StatusMessage.Severity.INFO,
					resourcesAccessor.getMessages().get("info.userManager.updated"));
			// refresh users and groups list
			resetUsers();
			// reset de la recherche
			search();
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			return viewUser(selectedUser.getId());
		} catch (Exception t) {
			throw ClientException.wrap(t);
		}
	}

	public String cancelEditUser() throws ClientException {
		try {
			userProfils = null;

			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

			return viewUser(selectedUser.getId());
		} catch (Exception t) {
			throw ClientException.wrap(t);
		}
	}

	public void setRealSelectedUser(DocumentModel realSelectedUser) {
		this.realSelectedUser = realSelectedUser;
	}

	public boolean getPasswordValid() {
		return passwordValid;
	}

	public void setPasswordValid(boolean passwordValid) {
		this.passwordValid = passwordValid;
	}

	@Override
	protected boolean getCanDeleteUsers(boolean allowCurrentUser) throws ClientException {
		if (userManager.areUsersReadOnly()) {
			return false;
		}
		if (currentUser instanceof STPrincipalImpl) {
			SSPrincipal pal = (SSPrincipal) currentUser;
			if (pal.isAdministrator() || pal.isMemberOf(STBaseFunctionConstant.UTILISATEUR_DELETER)) {
				return true;
			}
			// Test si l'utilisateur connecté peut effacer les utilisateurs
			// s'ils appartiennent au même ministère
			if (pal.isMemberOf(STBaseFunctionConstant.UTILISATEUR_MINISTERE_DELETER)) {
				Set<String> currentUserMinistere = pal.getMinistereIdSet();

				SSPrincipal sel = (SSPrincipal) userManager.getPrincipal(selectedUser.getTitle());

				if (sel != null) {

					if (!allowCurrentUser && sel.getName() != null && sel.getName().equals(currentUser.getName())) {
						return false;
					}

					// si l'utilisateur selectionné est UtilisateurDeleter (admin fonctionnel), non supprimable
					if (sel.isMemberOf(STBaseFunctionConstant.UTILISATEUR_DELETER)) {
						return false;
					}

					Set<String> selectedUserMinistere = sel.getMinistereIdSet();
					for (String ministereId : selectedUserMinistere) {
						if (currentUserMinistere.contains(ministereId)) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	public boolean getCanEditUsersNotSelf() throws ClientException {
		return getCanEditUsers(false);
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		// escape for javascript return
		this.errorName = StringEscapeUtils.escapeJavaScript(errorName);
	}

	public Boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	/**
	 * @return the userPostes
	 */
	@Override
	public List<String> getUserPostes() {
		if (userPostes == null) {
			userPostes = new ArrayList<String>();
		}
		try {
			if (realSelectedUser == null) {
				realSelectedUser = selectedUser;
			}
			if (realSelectedUser != null) {
				userPostes = STServiceLocator.getSTPostesService().getAllPosteIdsForUser(realSelectedUser.getId());
			}
		} catch (ClientException ce) {
			LOGGER.error(null, STLogEnumImpl.FAIL_GET_POSTE_TEC, ce);
		}
		return userPostes;
	}
}
