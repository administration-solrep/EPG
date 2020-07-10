package fr.dila.solonepg.web.helper;

import static org.jboss.seam.ScopeType.SESSION;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;

import fr.dila.cm.web.helper.CaseManagementStartupHelper;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.espace.EspaceSuiviActionsBean;
import fr.dila.solonepg.web.espace.EspaceTraitementActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.admin.utilisateur.UserManagerActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.administration.EtatApplication;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.service.EtatApplicationService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Overwrite CaseManagementStartupHelper to provide custom startup page for casemanagement.
 * 
 * @author FEO
 */
@Name("startupHelper")
@Scope(SESSION)
@Synchronized(timeout = 2000)
@Install(precedence = Install.DEPLOYMENT + 1)
public class SolonEpgStartupHelper extends CaseManagementStartupHelper {

	private static final long							serialVersionUID	= -3606085944027894437L;

	private static final Log							log					= LogFactory
																					.getLog(SolonEpgStartupHelper.class);

	@In(create = true)
	protected transient NuxeoPrincipal					currentNuxeoPrincipal;

	@In(create = true)
	protected transient UserManagerActionsBean			userManagerActions;

	@In(create = true)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient ActionManager					actionManager;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	/**
	 * Chargement des bean seam des différents espaces.
	 */

	@In(create = true, required = false)
	protected transient EspaceTraitementActionsBean		espaceTraitementActions;

	@In(create = true, required = false)
	protected transient EspaceSuiviActionsBean			espaceSuiviActions;

	@Override
	public String initServerAndFindStartupPage() throws ClientException {
		super.initServerAndFindStartupPage();

		final String user = currentNuxeoPrincipal.getName();

		final ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
		final STUserService userService = STServiceLocator.getSTUserService();

		// Si l'utilisateur est administrator on le déconnecte tout le temps
		// Ou Sinon si l'utilisateur n'a pas le droit d'accéder à l'interface access et qu'il n'a pas besoin de
		// changerson mot de passe
		if (currentNuxeoPrincipal.isAdministrator()
				|| !currentNuxeoPrincipal.isMemberOf(STBaseFunctionConstant.INTERFACE_ACCESS)
				&& !userService.isUserPasswordResetNeeded(user)) {
			return logout();
		}

		// si l'utilisateur fait parti du conseil d'état il ne peut pas accéder à l'application : il doit passer par les
		// services web.
		SSPrincipal currentNuxeoSSPrincipal = (SSPrincipal) currentNuxeoPrincipal;
		Set<String> ministereIdSet = currentNuxeoSSPrincipal.getMinistereIdSet();
		TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
		TableReference tableRef = tableReferenceService.getTableReference(documentManager).getAdapter(
				TableReference.class);
		// Si le ministère appartient au conseil d'état mais qu'il n'a pas besoin de changer son mdp alors on le
		// déconnecte
		if (ministereIdSet != null && ministereIdSet.contains(tableRef.getMinistereCE())
				&& !userService.isUserPasswordResetNeeded(user)) {
			return logout();
		}

		// Vérification de la date de dernier changement de mot de passe par rapport au paramètre
		if (profilUtilisateurService.isUserPasswordOutdated(documentManager, user)) {
			userService.forceChangeOutdatedPassword(user);
		}

		// Navigue vers l'écran permettant à l'utilisateur de changer son mot de passe
		if (userService.isUserPasswordResetNeeded(user)) {
			if (log.isDebugEnabled()) {
				log.debug("Changement de mot de passe obligatoire pour l'utilisateur : " + user);
			}
			return userManagerActions.resetCurrentUserPassword(user);
		}

		EtatApplicationService etatApplicationService = STServiceLocator.getEtatApplicationService();
		EtatApplication etatApplication = etatApplicationService.getEtatApplicationDocument(documentManager);

		if (etatApplication.getRestrictionAcces()) {
			if (!currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_ACCESS_UNRESTRICTED)) {
				try {
					return navigationWebActions.logout();
				} catch (Exception e) {
					throw new ClientException(e);
				}
			}
		}

		return navigateToMainEspace();
	}

	/**
	 * Renvoie le premier espace disponible pour l'utilisateur connecté.
	 * 
	 * @return le premier espace disponible pour l'utilisateur connecté.
	 * @throws ClientException
	 */
	public String navigateToMainEspace() throws ClientException {
		String mainSpace = null;
		if (currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_TRAITEMENT_READER)
				|| currentNuxeoPrincipal.isAdministrator()) {
			mainSpace = navigateToEspace(SolonEpgActionConstant.ESPACE_TRAITEMENT);
		} else if (currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_CREATION_READER)) {
			mainSpace = navigateToEspace(SolonEpgActionConstant.ESPACE_CREATION);
		} else if (currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_SUIVI_READER)) {
			mainSpace = navigateToEspace(SolonEpgActionConstant.ESPACE_SUIVI);
		} else if (currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_RECHERCHE_READER)) {
			mainSpace = navigateToEspace(SolonEpgActionConstant.ESPACE_RECHERCHE);
		} else if (currentNuxeoPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER)
				|| currentNuxeoPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER)
				|| currentNuxeoPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER)
				|| currentNuxeoPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER)
				|| currentNuxeoPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER)) {
			mainSpace = navigateToEspace(ActiviteNormativeConstants.MAIN_MENU_ACTIVITE_NORMATIVE_ACTION);
		} else if (currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ADMINISTRATION_READER)) {
			mainSpace = navigateToEspace(SolonEpgActionConstant.ESPACE_ADMINISTRATION);
		} else if (currentNuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_PARLEMENTAIRE_READER)) {
			mainSpace = navigateToEspace(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE);
		}
		return mainSpace;
	}

	/**
	 * Renvoie l'espace
	 * <p>
	 * espaceName
	 * </p>
	 * 
	 * @param espaceName
	 * @return
	 * @throws ClientException
	 */
	private String navigateToEspace(String espaceName) throws ClientException {
		String menuName = null;
		String leftMenuName = null;
		String feuilleRouteView = STViewConstant.EMPTY_VIEW;
		String viewName = STViewConstant.EMPTY_VIEW;
		if (StringUtils.isEmpty(espaceName)) {
			return null;
		} else if (espaceName.equals(SolonEpgActionConstant.ESPACE_TRAITEMENT)) {
			// note : on passe par le bean car on initialise des variables private du bean.
			return espaceTraitementActions.navigateTo();
		} else if (espaceName.equals(SolonEpgActionConstant.ESPACE_CREATION)) {
			menuName = SolonEpgActionConstant.ESPACE_CREATION;
			leftMenuName = SolonEpgActionConstant.LEFT_MENU_ESPACE_CREATION;
			feuilleRouteView = SolonEpgViewConstant.ESPACE_CREATION_VIEW;
			viewName = SolonEpgViewConstant.ESPACE_CREATION_VIEW;
		} else if (espaceName.equals(SolonEpgActionConstant.ESPACE_RECHERCHE)) {
			menuName = SolonEpgActionConstant.ESPACE_RECHERCHE;
			leftMenuName = SolonEpgActionConstant.LEFT_MENU_ESPACE_RECHERCHE;
			feuilleRouteView = SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER;
			viewName = SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER;
		} else if (espaceName.equals(ActiviteNormativeConstants.MAIN_MENU_ACTIVITE_NORMATIVE_ACTION)) {
			menuName = ActiviteNormativeConstants.MAIN_MENU_ACTIVITE_NORMATIVE_ACTION;
			leftMenuName = ActiviteNormativeConstants.LEFT_MENU_ACTIVITE_NORMATIVE_ACTION;
		} else if (espaceName.equals(SolonEpgActionConstant.ESPACE_ADMINISTRATION)) {
			menuName = SolonEpgActionConstant.ESPACE_ADMINISTRATION;
			leftMenuName = SolonEpgActionConstant.LEFT_MENU_ESPACE_ADMINISTRATION;
		} else if (espaceName.equals(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE)) {
			menuName = SolonEpgActionConstant.ESPACE_PARLEMENTAIRE;
		} else if (espaceName.equals(SolonEpgActionConstant.ESPACE_SUIVI)) {
			// note : on passe par le bean car on initialise des variables private du bean.
			return espaceSuiviActions.navigateTo();
		}

		// Réinitialise le document courant
		navigationContext.resetCurrentDocument();

		// Renseigne le menu du haut
		Action mainMenuAction = actionManager.getAction(menuName);
		navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

		// Renseigne le menu de gauche
		Action leftMenuAction = actionManager.getAction(leftMenuName);
		navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

		// Réinitialise la selection du menu gauche
		navigationWebActions.setCurrentLeftMenuItemAction(null);

		// Renseigne la vue de route des étapes de feuille de route vers les dossiers
		routingWebActions.setFeuilleRouteView(feuilleRouteView);

		return viewName;
	}

	/**
	 * Logout avec le message "Utilisateur non autorisé".
	 * 
	 * @return String
	 * @throws ClientException
	 */
	private String logout() throws ClientException {
		try {
			return navigationWebActions.logout();
		} catch (Exception e) {
			log.error("L'utilisateur n'a pas le droit d'accéder à l'application", e);
			throw new ClientException(e);
		}
	}
}
