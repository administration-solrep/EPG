package fr.dila.solonepg.web.admin;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.selection.FeuilleRouteMassActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STViewConstant;

/**
 * Bean Seam de l'espace d'administration.
 * 
 * @author jtremeaux
 */
@Name("administrationActions")
@Scope(ScopeType.SESSION)
@Install(precedence = FRAMEWORK + 1)
public class AdministrationActionsBean extends fr.dila.ss.web.admin.AdministrationActionsBean implements Serializable {

	private static final long					serialVersionUID	= 956631279709404171L;

	@In(required = true, create = true)
	protected SSPrincipal						ssPrincipal;

	@In(required = true, create = true)
	protected FeuilleRouteMassActionsBean		feuilleRouteMassActions;

	@In(create = true)
	protected transient DocumentsListsManager	documentsListsManager;
	/**
	 * Retourne la vue active (utilisé pour l'affichage des layouts delisting des dossiers de la suppression, de
	 * l'abandon et de )
	 */
	private String								activeViewName;

	/**
	 * 
	 * @return true si dans l'espace d'administration
	 */
	public boolean canViewTransfertDossier() {
		Action mainMenuAction = actionManager.getAction(MAIN_MENU_ADMIN_ACTION);
		return navigationWebActions.getCurrentMainMenuAction() != null
				&& navigationWebActions.getCurrentMainMenuAction().getLabel().equals(mainMenuAction.getLabel())
				&& ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_TRANSFERT_DOSSIER);
	}

	// ***********************************************************************
	// Fonctions de navigation
	// ***********************************************************************
	/**
	 * Navigue vers l'écran de gestion des modèles de parapheur.
	 * 
	 * @return Écran de gestion des modèles de parapheur
	 * @throws ClientException
	 */

	public String navigateToModeleParapheurModelFolder() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		// on signale qu'il faut réinitialiser le modele de parapheur
		Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_MODEL_RESET_EVENT);
		return SolonEpgViewConstant.MODELES_PARAPHEUR_VIEW;
	}

	/**
	 * Navigue vers l'écran de gestion des modèles de fond de dossier.
	 * 
	 * @return Écran de gestion des modèles de fond de dossier
	 * @throws ClientException
	 */
	public String navigateToModeleFondDossierModelFolder() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		// on signale qu'il faut réinitialiser le modele de fond de dossier
		Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_RESET_EVENT);
		return SolonEpgViewConstant.MODELES_FOND_DOSSIER_VIEW;
	}

	/**
	 * Navigue vers l'écran de gestion du journal d'administration.
	 * 
	 * @return Écran de gestion du journal d'administration
	 * @throws ClientException
	 */
	public String navigateToJournal() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return STViewConstant.JOURNAL_VIEW;
	}

	/**
	 * Navigue vers l'écran de gestion des modèles de table de reference
	 * 
	 * @return Écran de gestion des tables de reference
	 * @throws ClientException
	 */
	public String navigateToTableReference() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return SolonEpgViewConstant.TABLE_REFERENCE_VIEW;
	}

	/**
	 * Navigue vers l'écran de gestion des bulletins Officiels.
	 * 
	 * @return Écran de gestion des bulletins Officiels
	 * @throws ClientException
	 */
	public String navigateToBulletinsOfficiels() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return SolonEpgViewConstant.BULLETIN_OFFICIEL_VIEW;
	}
	
	/**
	 * Navigue vers l'ecran de paramétrage Adamant.
	 * 
	 * @return ecran de paramétrage Adamant
	 * @throws ClientException
	 */
	public String navigateToParamAdamant() throws ClientException {
		initEspaceAdministration();
		return SolonEpgViewConstant.PARAM_ADAMANT_VIEW;
	}

	/**
	 * Navigue vers l'écran de gestion du paramétrage de l'application.
	 * 
	 * @return Écran de gestion du paramétrage de l'application.
	 * @throws ClientException
	 */
	public String navigateToParametrageApplication() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return SolonEpgViewConstant.PARAMETRAGE_APPLICATION_VIEW;
	}

	/**
	 * Navigue vers l'écran de gestion des bulletins Officiels.
	 * 
	 * @return Écran de gestion des bulletins Officiels
	 * @throws ClientException
	 */
	public String navigateToGestionIndexation() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return SolonEpgViewConstant.GESTION_INDEXATION_VIEW;
	}

	/**
	 * Navigue vers l'écran de suppression de dossiers .
	 * 
	 * @return Écran de suppression de dossiers
	 * @throws ClientException
	 */
	public String navigateToSuppression() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_SUPPRESSION_CONTENT);
		return SolonEpgViewConstant.ADMIN_SUPPRESSION_VIEW;
	}

	/**
	 * Navigue vers l'écran de suppression de dossiers ministrielle.
	 * 
	 * @return Écran de suppression de dossiers ministrielle
	 * @throws ClientException
	 */
	public String navigateToSuppressionMinitrielle() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_MINISTERIEL_SUPPRESSION_CONTENET);
		return SolonEpgViewConstant.ADMIN_MINISTERIEL_SUPPRESSION_VIEW;
	}

	/**
	 * Navigue vers l'écran de suppression de dossiers ministrielle.
	 * 
	 * @return Écran de suppression de dossiers ministrielle
	 * @throws ClientException
	 */
	public String navigateToView(String view) throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return view;
	}

	/**
	 * Navigue vers l'écran d'historique des migrations
	 * 
	 * @return Écran d'historiquesde smigrations
	 * @throws ClientException
	 */

	public String navigateToHistoriqueDesMigrations() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		return SolonEpgViewConstant.HISTORIQUE_MIGRATIONS_VIEW;
	}

	/**
	 * Navigue vers l'écran de changement de gouvernement .
	 * 
	 * @return Écran de changement de gouvernement
	 * @throws ClientException
	 */

	public String navigateToChangementGouvernement() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		return SolonEpgViewConstant.CHANGEMENT_GOUVERNEMENT_VIEW;
	}

	/**
	 * Navigue vers l'écran d'abandonne de dossiers .
	 * 
	 * @return Écran d'abandonne de dossiers
	 * @throws ClientException
	 */

	public String navigateToAbandon() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_ABANDON_CONTENT);
		return SolonEpgViewConstant.ADMIN_ABANDON_VIEW;
	}

	/**
	 * Navigue vers l'écran de dossiers a suivi par administrateur fonctionelle.
	 * 
	 * @return Écran de suivi de dossiers
	 * @throws ClientException
	 */

	public String navigateToSuppressionSuivi() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_SUPPRESSION_VIEW_SUIVI);
		return SolonEpgViewConstant.ADMIN_SUPPRESSION_VIEW_SUIVI;
	}

	/**
	 * Navigue vers l'ecran d'archivage de dossiers Adamant.
	 * 
	 * @return ecran d'archivage
	 * @throws ClientException
	 */
	public String navigateToArchivageAdamant() throws ClientException {
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_ARCHIVAGE_ADAMANT_CONTENT);
		return SolonEpgViewConstant.ADMIN_ARCHIVAGE_ADAMANT_VIEW;
	}

	/**
	 * Navigue vers l'ecran d'archivage intermediaire de dossiers.
	 * 
	 * @return ecran d'archivage
	 * @throws ClientException
	 */
	public String navigateToArchivageIntermediaire() throws ClientException {
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_ARCHIVAGE_INTERMEDIAIRE_CONTENT);
		return SolonEpgViewConstant.ADMIN_ARCHIVAGE_INTERMEDIAIRE_VIEW;
	}

	/**
	 * Navigue vers l'ecran d'archivage definitive de dossiers.
	 * 
	 * @return ecran d'archivage definitive
	 * @throws ClientException
	 */
	public String navigateToArchivageDefinitive() throws ClientException {
		initEspaceAdministration();
		setActiveViewName(SolonEpgViewConstant.ADMIN_ARCHIVAGE_DEFINITIVE_CONTENT);
		return SolonEpgViewConstant.ADMIN_ARCHIVAGE_DEFINITIVE_VIEW;
	}

	/**
	 * Navigue vers l'écran de transfert des dossiers clos.
	 * 
	 * @return "view_admin_transfert"
	 * @throws ClientException
	 */
	public String navigateToMassTransfert() throws ClientException {
		initEspaceAdministration();
		return SolonEpgViewConstant.ADMIN_TRANSFERT_VIEW;
	}

	/**
	 * Navigue vers l'écran de substitution de masse des postes dans les modèles de feuilles de routes.
	 * 
	 * @return Vue
	 * @throws ClientException
	 */
	public String navigateToModeleFeuilleRouteMassSubstituerPoste() throws ClientException {
		initEspaceAdministration();
		feuilleRouteMassActions.resetSubstitution();
		return SolonEpgViewConstant.ADMIN_MODELE_FEUILLE_ROUTE_MASS_SUBSTITUER_POSTE;
	}

	/**
	 * Navigue vers l'ecran des batchs.
	 * 
	 * @return ecran des batchs
	 * @throws ClientException
	 */
	public String navigateToBatch() throws ClientException {
		initEspaceAdministration();
		return SolonEpgViewConstant.ESPACE_ADMIN_BATCH;
	}

	public String navigateToRechercheUtilisateur() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR;
	}

	public String getActiveViewName() {
		return activeViewName;
	}

	public void setActiveViewName(String activeViewName) {
		this.activeViewName = activeViewName;
	}

	/**
	 * Navigue vers l'écran des logs d'exécution des batchs
	 * 
	 * @return écran des logs des batchs
	 * @throws ClientException
	 */
	public String navigateToViewBatchSuivi() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		navigationWebActions.setCurrentLeftMenuItemAction(actionManager.getAction("admin_batch_viewSuivi"));
		return STViewConstant.BATCH_SUIVI_VIEW;
	}

	/**
	 * Navigue vers l'écran des planifications d'exécution des batchs
	 * 
	 * @return écran des planifications des batchs
	 * @throws ClientException
	 */
	public String navigateToViewBatchSuiviPlanification() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		navigationWebActions
				.setCurrentLeftMenuItemAction(actionManager.getAction("admin_batch_viewSuiviPlanification"));
		return STViewConstant.BATCH_SUIVI_PLANIFICATION;
	}

	/**
	 * Navigue vers l'écran des notifications de suivi des batchs
	 * 
	 * @return écran des notifications
	 * @throws ClientException
	 */
	public String navigateToViewBatchSuiviNotification() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		navigationWebActions.setCurrentLeftMenuItemAction(actionManager.getAction("admin_batch_viewSuiviNotification"));
		return STViewConstant.BATCH_SUIVI_NOTIFICATION;
	}

	public String navigateToParametreMoteurRechercheFolder() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		navigationWebActions
				.setCurrentLeftMenuItemAction(actionManager.getAction("admin_param_param_moteur_recherche"));

		final ElasticParametrageService paramMoteurRechercheService = SolonEpgServiceLocator
				.getElasticParametrageService();
		DocumentModel currentDocument = paramMoteurRechercheService.getParametreFolder(documentManager);

		return navigationContext.navigateToDocument(currentDocument);
	}

	public boolean isSelectionListEmpty() {
		String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
		return documentsListsManager.getWorkingList(selectionListName).isEmpty();
	}
	
	/**
	 * Navigue vers l'écran de gestion des squelettes de feuilles de route (FEV525).
	 * 
	 * @return Écran de gestion des squelettes de feuilles de route
	 * @throws ClientException
	 */
	public String navigateToSqueletteFeuilleRouteFolder() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();
		navigationWebActions.setCurrentLeftMenuItemAction(actionManager.getAction("admin_feuille_route_squelette"));

		// Navigue vers la racine des squelettes de feuilles de route
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		DocumentModel currentDocument = feuilleRouteModelService.getFeuilleRouteSqueletteFolder(documentManager);
		navigationContext.setCurrentDocument(currentDocument);

		// Renseigne la vue de retour des feuilles de routes
		routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.SQUELETTE_FEUILLE_ROUTE_VIEW);

		// Invalide la liste des feuilles de route
		contentViewActions.reset(SolonEpgContentViewConstant.FEUILLE_ROUTE_SQUELETTE_FOLDER_CONTENT_VIEW);

		return SolonEpgViewConstant.SQUELETTES_FEUILLE_ROUTE_VIEW;
	}
	
	/**
	 * Navigue vers l'écran de création en masse de modeles de FDR via l'utilisation des squelettes de feuilles de route (FEV525).
	 * 
	 * @return Écran de gestion des squelettes de feuilles de route
	 * @throws ClientException
	 */
	public String navigateToModeleFeuilleRouteCreationEnMasse() throws ClientException {
		// Initialise le contexte de l'espace d'administration
		initEspaceAdministration();

		return SolonEpgViewConstant.CREATE_MODELE_FEUILLE_ROUTE_VIA_SQUELETTE;
	}
}
