package fr.dila.solonepg.web.recherche;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.api.service.TableauDynamiqueService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.requeteur.EPGSmartNXQLQueryActions;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.web.context.NavigationContextBean;
import fr.sword.xsd.solon.epp.IdentiteDenormalise;

@Name("tableauDynamiqueActions")
@Scope(ScopeType.CONVERSATION)
public class TableauDynamiqueActionsBean implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -8320370239881231020L;

	@In(create = true, required = true)
	protected transient CoreSession							documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean				navigationContext;

	@In(create = true)
	protected transient FacesMessages						facesMessages;

	@In(create = true, required = false)
	protected transient UserWorkspaceManagerActionsBean		userWorkspaceManagerActions;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean		routingWebActions;

	@In(create = true, required = false)
	protected transient EPGSmartNXQLQueryActions			smartNXQLQueryActions;

	@In(create = true, required = false)
	protected transient UserManagerActionsBean				userManagerActions;

	@In(create = true, required = false)
	protected transient RechercheWebActionsBean				rechercheWebActions;

	@In(create = true, required = false)
	protected transient RequeteurDossierSimpleActionsBean	requeteurDossierSimpleActions;

	@In(create = true)
	protected ResourcesAccessor								resourcesAccessor;

	@In(required = true, create = true)
	protected SSPrincipal									ssPrincipal;

	private String											intitule;

	private List<String>									destinataires;

	private DocumentModel									currentTableauDynamique;

	private String											tableauDynamique	= null;

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public List<String> getDestinataires() {
		return destinataires;
	}

	public void setDestinataires(List<String> destinataires) {
		this.destinataires = destinataires;
	}

	public void donothing() throws ClientException {
		// nothing
	}

	public String save() throws ClientException {
		Boolean error = Boolean.FALSE;
		if (intitule == null || intitule.isEmpty()) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("favoris.recherche.intitule.vide"));
			error = Boolean.TRUE;
		}

		String queryPart = null;
		String type = null;
		if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView())
				&& !rechercheWebActions.isRechercheDossierSimple()) {
			queryPart = smartNXQLQueryActions.getQueryPart();
			type = "user";
		} else if (rechercheWebActions.isRechercheDossierSimple()) {
			queryPart = requeteurDossierSimpleActions.getCurrentRequetePattern();
			type = "user";
		}

		if (queryPart == null || queryPart.isEmpty()) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("favoris.recherche.query.vide"));
			error = Boolean.TRUE;
		} else if (queryPart.equals(DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR)) {
			facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("feedback.search.star"));
			error = Boolean.TRUE;
		}

		if (!error) {
			final TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();

			Map<Integer, String> result = new HashMap<Integer, String>();
			if (rechercheWebActions.isRechercheDossierSimple()) {
				result = tableauDynamiqueService.createTableauDynamique(documentManager, type,
						requeteurDossierSimpleActions.getCurrentRequetePattern(), destinataires, intitule);
			} else if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView())
					&& !rechercheWebActions.isRechercheDossierSimple()) {
				result = tableauDynamiqueService.createTableauDynamique(documentManager, type,
						smartNXQLQueryActions.getFullQuery(), destinataires, intitule);
			}
			Events.instance().raiseEvent(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_CHANGED_EVENT);

			String ok = result.get(0);
			if (!StringUtils.isEmpty(ok)) {
				facesMessages.add(StatusMessage.Severity.INFO,
						resourcesAccessor.getMessages().get("tableau.dynamique.ajout.ok") + " : " + ok);
			}
			String ko = result.get(1);
			if (!StringUtils.isEmpty(ko)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("tableau.dynamique.ajout.ko") + " : " + ko);
			}
		}

		destinataires = null;
		intitule = null;

		return routingWebActions.getFeuilleRouteView();
	}

	public String update() throws ClientException {
		if (currentTableauDynamique == null) {
			return null;
		}

		Boolean error = Boolean.FALSE;
		if (intitule == null || intitule.isEmpty()) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("favoris.recherche.intitule.vide"));
			error = Boolean.TRUE;
		}

		if (!error) {
			final TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
			Map<Integer, String> result = tableauDynamiqueService.updateTableauDynamique(documentManager,
					currentTableauDynamique, destinataires, intitule);
			Events.instance().raiseEvent(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_CHANGED_EVENT);

			String ok = result.get(0);
			if (!StringUtils.isEmpty(ok)) {
				facesMessages.add(StatusMessage.Severity.INFO,
						resourcesAccessor.getMessages().get("tableau.dynamique.modif.ok") + " : " + ok);
			}
			String ko = result.get(1);
			if (!StringUtils.isEmpty(ko)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("tableau.dynamique.modif.ko") + " : " + ko);
			}

		}

		destinataires = null;
		intitule = null;

		return routingWebActions.getFeuilleRouteView();
	}

	public String navigateToBack() {
		return routingWebActions.getFeuilleRouteView();
	}

	public boolean isDirectionViewer() {
		return ssPrincipal != null
				&& ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_TABLEAU_DYNAMIQUE_DESINATAIRE_VIEWER);
	}

	public String navigateToAddTableauDynamique() throws ClientException {
		setDestinataires(new ArrayList<String>());
		setIntitule(null);
		setCurrentTableauDynamique(null);
		return SolonEpgViewConstant.ESPACE_RECHERCHE_ADD_TABLEAU_DYNAMIQUE;
	}

	public String navigateToEditTableauDynamique() {
		return SolonEpgViewConstant.ESPACE_RECHERCHE_EDIT_TABLEAU_DYNAMIQUE;
	}

	public DocumentModel getCurrentTableauDynamique() {
		return currentTableauDynamique;
	}

	/**
	 * charge le tableau dynamque (maj des champs)
	 * 
	 * @param currentTableauDynamique
	 * @throws ClientException
	 */
	public void setCurrentTableauDynamique(DocumentModel currentTab) throws ClientException {
		if (currentTab != null) {
			TableauDynamique tableauDynamique = currentTab.getAdapter(TableauDynamique.class);
			setDestinataires(tableauDynamique.getDestinatairesId());
			setIntitule(DublincoreSchemaUtils.getTitle(currentTab));
		}
		this.currentTableauDynamique = currentTab;
	}

	/**
	 * @param tableauDynamique
	 *            the tableauDynamique to set
	 */
	public void setTableauDynamique(String tableauDynamique) {
		this.tableauDynamique = tableauDynamique;
	}

	/**
	 * @return the tableauDynamique
	 */
	public String getTableauDynamique() {
		return tableauDynamique;
	}

	/**
	 * Supprime le tableau dynamique courant
	 */
	private void deleteTableauDynamique() {
		final TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
		try {
			final DocumentModel documentTableauDynamique = documentManager.getDocument(new IdRef(tableauDynamique));
			final String nomTableauDynamique = documentTableauDynamique.getTitle();
			final String utilisateurCourant = ssPrincipal.getName();

			tableauDynamiqueService.deleteTableauDynamique(documentManager,
					documentManager.getDocument(new IdRef(tableauDynamique)));
			facesMessages.add(
					StatusMessage.Severity.INFO,
					resourcesAccessor.getMessages().get(
							"Tableau dynamique '" + nomTableauDynamique + "' supprimé pour l'utilisateur "
									+ utilisateurCourant));
			Events.instance().raiseEvent(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_CHANGED_EVENT);
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		} catch (ClientException e) {
			facesMessages.add(
					StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get(
							"La suppression du tableau dynamique a échouée" + tableauDynamique), e);
		}
	}

	/**
	 * Supprime le tableau dynamique courant
	 * 
	 * @throws ClientException
	 */
	public String deleteTableauDynamiqueAndSave() throws ClientException {
		boolean ok = true;
		if (tableauDynamique == null || tableauDynamique.isEmpty()) {
			ok = false;
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get("Vous devez selectionner un tableau dynamique à supprimer."));
		}
		if (intitule == null || intitule.isEmpty()) {
			ok = false;
			facesMessages.add(
					StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get(
							"Vous devez choisir un intitulé.pour le nouveau tableau dynamique."));
		}
		if (ok) {
			this.deleteTableauDynamique();
			return this.save();
		} else {
			return this.navigationContext.getCurrentView();
		}
	}

	/**
	 * Indique s'il le bouton 'supprimer' dans l'écran de suppression de tableau dynamique est à l'état 'disabled' (lors
	 * de la création d'un nouveau Tableau Dynamique qui demande de supprimer un ancien tableau). On ne permet de
	 * supprimer un tableau que si le tableau à supprimer a été sélectionné et que un intitulé a été choisi pour le
	 * nouveau tableau.
	 */
	public boolean displaySupprimerTableauDynamique() {
		if (tableauDynamique == null || tableauDynamique.isEmpty() || intitule == null || intitule.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true si l'utilisateur courant a un nombre de tableaux dynamiques inférieur au nombre maximal autorisé.
	 */
	public Boolean canAddNewTableauDynamique() {
		final TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
		try {
			return tableauDynamiqueService.userIsUnderQuota(documentManager, ssPrincipal.getName());
		} catch (ClientException e) {
			facesMessages.add(
					StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get(
							"Erreur dans l'estimation du nombre de tableaux dynamiques actuels"), e);
		}
		return false;
	}
}
