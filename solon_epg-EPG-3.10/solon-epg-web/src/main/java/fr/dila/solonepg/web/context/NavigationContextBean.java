package fr.dila.solonepg.web.context;

import static org.jboss.seam.ScopeType.CONVERSATION;
import static org.jboss.seam.annotations.Install.FRAMEWORK;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentLocation;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.ui.web.util.DocumentLocator;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.platform.util.RepositoryLocation;

import fr.dila.cm.caselink.CaseLinkConstants;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.core.event.helper.DocumentSearchEventHelper;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.EspaceRechercheActionsBean;
import fr.dila.solonepg.web.helper.SolonEpgStartupHelper;
import fr.dila.solonepg.web.recherchenor.RechercheNorActionsBean;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.web.action.NavigationWebActionsBean;

/**
 * Surcharge du NavigationContextBean de Nuxeo.
 */
@Name("navigationContext")
@Scope(CONVERSATION)
@Install(precedence = FRAMEWORK + 2)
public class NavigationContextBean extends fr.dila.st.web.context.NavigationContextBean {

	private enum MenuAnchor {
		ESPACE_TRAITEMENT(SolonEpgActionConstant.ESPACE_TRAITEMENT, "solon-epg_espaceDeTraitement"), ESPACE_CREATION(
				SolonEpgActionConstant.ESPACE_CREATION, "solon-epg_espaceDeCreation"), ESPACE_SUIVI(
				SolonEpgActionConstant.ESPACE_SUIVI, "solon-epg_espaceDeSuivi"), ESPACE_RECHERCHE(
				SolonEpgActionConstant.ESPACE_RECHERCHE, "solon-epg_espaceDeRecherche"), ESPACE_PARLEMENTAIRE(
				SolonEpgActionConstant.ESPACE_PARLEMENTAIRE, "solon-epg_espaceParlementaire"), ESPACE_ADMINISTRATION(
				SolonEpgActionConstant.ESPACE_ADMINISTRATION, "solon-epg_espaceAdministration");

		private String	menu;
		private String	anchor;

		MenuAnchor(String menu, String anchor) {
			this.menu = menu;
			this.anchor = anchor;
		}

		public static MenuAnchor getByMenu(String menu) {
			for (MenuAnchor menuAnchor : MenuAnchor.values()) {
				if (menuAnchor.menu.equals(menu)) {
					return menuAnchor;
				}
			}
			return null;
		}

		public String getAnchor() {
			return anchor;
		}

	}

	private static final long							serialVersionUID	= 4916815797725588221L;

	private static final Log							log					= LogFactory
																					.getLog(NavigationContextBean.class);

	@In(create = true)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient EspaceRechercheActionsBean		espaceRechercheActions;

	@In(create = true, required = false)
	protected transient UserWorkspaceManagerActionsBean	userWorkspaceManagerActions;

	@In(create = true, required = false)
	protected transient RechercheNorActionsBean			rechercheNorActions;

	@In(create = true, required = false)
	protected transient SolonEpgStartupHelper			startupHelper;

	@RequestParameter
	String												docRef;

	@Override
	protected void raiseEvent(DocumentModel documentModel) throws ClientException {
		super.raiseEvent(documentModel);

		if (documentModel != null
				&& (documentModel.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA) || documentModel
						.hasSchema(STSchemaConstant.FEUILLE_ROUTE_SCHEMA))
				&& navigationWebActions.getCurrentMainMenuAction() != null
				&& SolonEpgActionConstant.ESPACE_RECHERCHE.equals(navigationWebActions.getCurrentMainMenuAction()
						.toString())) {
			final String resultatConsulteRootPath = userWorkspaceManagerActions.getCurrentUserPersonalWorkspace()
					.getPathAsString();
			DocumentSearchEventHelper.raiseEvent(documentManager, documentModel, resultatConsulteRootPath);

			if (espaceRechercheActions.isInResultatsConsultes()) {
				// refresh de la contentview pour les consultations
				Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			}
		}
	}

	// start a new conversation if needed, join main if possible
	@SuppressWarnings("deprecation")
	@Begin(id = "#{conversationIdGenerator.currentOrNewMainConversationId}", join = true)
	public String navigateToDocumentURL() throws ClientException {
		try {
			final DocumentLocation docLoc;

			docLoc = DocumentLocator.parseDocRef(docRef);
			RepositoryLocation repLoc = new RepositoryLocation(docLoc.getServerName());
			// création de la session si elle existe pas
			if (!repLoc.equals(getCurrentServerLocation())) {
				setCurrentServerLocation(repLoc);
			}

			DocumentModel documentModel = documentManager.getDocument(docLoc.getDocRef());
			if (documentModel != null && documentModel.hasSchema(CaseLinkConstants.CASE_LINK_SCHEMA)) {
				DossierLink dossierLink = documentModel.getAdapter(DossierLink.class);
				DocumentModel dossierDoc = documentManager.getDocument(new IdRef(dossierLink.getDossierId()));
				Dossier dossier = dossierDoc.getAdapter(Dossier.class);
				rechercheNorActions.setNor(dossier.getNumeroNor());
				return rechercheNorActions.goToResults();
			} else if (documentModel != null && documentModel.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
				Dossier dossier = documentModel.getAdapter(Dossier.class);
				rechercheNorActions.setNor(dossier.getNumeroNor());
				return rechercheNorActions.goToResults();
			}
		} catch (Exception e) {
			log.error("docRef error : " + docRef, e);
		}

		return startupHelper.navigateToMainEspace();
	}

	public String getAnchor() {
		Action currentMainMenuAction = navigationWebActions.getCurrentMainMenuAction();
		if (currentMainMenuAction != null) {
			MenuAnchor menuAnchor = MenuAnchor.getByMenu(currentMainMenuAction.getId());
			if (menuAnchor != null) {
				return "#" + menuAnchor.getAnchor();
			}
		}
		return "";
	}

}
