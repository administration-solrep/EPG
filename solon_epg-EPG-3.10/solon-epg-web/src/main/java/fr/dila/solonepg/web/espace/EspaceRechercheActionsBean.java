package fr.dila.solonepg.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.trash.TrashService;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.recherche.UserManagerActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

@Name("espaceRechercheActions")
@Scope(CONVERSATION)
public class EspaceRechercheActionsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;
    
    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;
    
    @In(create = true, required = false)
    protected transient ActionManager actionManager;
    
    @In(create = true)
    protected transient NavigationWebActionsBean navigationWebActions;
    
    @In(create = true)
    protected transient ContentViewActions contentViewActions;
    
    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;
    
    @In(create = true)
    protected ResourcesAccessor resourcesAccessor;
    
	@In(create = true)
	protected transient FacesMessages facesMessages;
	
	@In(create = true, required = false)
	protected transient UserWorkspaceManagerActionsBean userWorkspaceManagerActions;
	
    @In(create = true, required = false)
    protected transient UserManagerActionsBean userManagerActions;
    
	@In(required = true, create = true)
	protected SSPrincipal ssPrincipal;
    
    /**
     * Retourne de l'espace recherche. (pour l'instant affichage du requêteur).
     * 
     * @return
     * @throws ClientException
     */
    public String navigateTo() throws ClientException {
        return SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER;
    }
    
    /**
     * La vue de résultats de la recherche NOR.
     * 
     * @return la vue résultat recherche NOR
     * @throws ClientException
     */
    public String navigateToResultsNOR() throws ClientException {
        return SolonEpgViewConstant.RECHERCHE_RESULTATS_NOR_VIEW;
    }
    
    /**
     * La vue de résultats de la recherche par le requeteur.
     * 
     * @return la vue résultat recherche NOR
     * @throws ClientException
     */
    public String navigateToResultsRequeteur() throws ClientException {
        return SolonEpgViewConstant.RECHERCHE_RESULTATS_REQUETEUR_VIEW;
    }
    
    /**
     * La vue d'affichage du requêteur.
     * 
     * @return la vue d'affichage du requeteur.
     * @throws ClientException
     */
    public String navigateToRequeteur() throws ClientException {
        return SolonEpgViewConstant.REQUETEUR_DOSSIER_VIEW;
    }
    
    public boolean isInResultatsConsultes() {
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTATS_CONSULTES_VIEW);
    }
    
    public boolean isInFavorisConsultations() {
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW);
    }
    
    public boolean isInFavorisRecherche() {
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_RECHERCHE_CONTENT_VIEW);
    }
 
    /**
     * Renvoie vraie si on se trouve dans les résultats de la recherche de dossier experte
     */
    public boolean isInRechercheDossier() {
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER_RESUTAT);
    }
    
    /**
     * Renvoie vraie si on se trouve dans les résultats de la recherche de dossier simple
     */
    public boolean isInRechercheDossierSimple() {
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.REQUETEUR_DOSSIER_SIMPLE_RESULTATS);
    }
    
    /**
     * Renvoie vraie si on se trouve dans les résultats de la recherche de modèle de feuille de route
     */
    public boolean isInRechercheModeleFeuilleDeRoute() {
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT);
    }
    
    public boolean isInRechercheUser() {
        Action action = navigationWebActions.getCurrentMainMenuAction();
        if(action != null && !SolonEpgActionConstant.ESPACE_RECHERCHE.equals(action.toString())){
            return false;
        }
        
        String currentViewName = routingWebActions.getFeuilleRouteView();
        return currentViewName != null && currentViewName.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR);
    }

	/**
	 * Ajout des documents sélectionnés aux favoris de consultation.
	 * 
	 * @throws ClientException
	 */
	public void putSelectionInFavorisConsultation() throws ClientException {
	    // Si aucun document n'est sélectionné, ne fait rien
        String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
        if (documentsListsManager.isWorkingListEmpty(selectionListName)) {
            return;
        }
        
        List<DocumentModel> docsList = documentsListsManager.getWorkingList(selectionListName);
        
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        int dossiersAjoutes = espaceRechercheService.addToFavorisConsultation(documentManager, userWorkspaceManagerActions.getCurrentUserPersonalWorkspace().getPathAsString(), docsList);

        Object[] params = { dossiersAjoutes };
        facesMessages.add(StatusMessage.Severity.INFO, "#0 " + resourcesAccessor.getMessages().get("label.epg.document.ajout" + (dossiersAjoutes > 1 ? "s" : "")), params);

        if (dossiersAjoutes < docsList.size()) {
            facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("label.epg.favoris.ajout.impossible"));
        }

        documentsListsManager.resetWorkingList(selectionListName);
    }
	
	/**
	 * Suppression des documents dans les favoris de consultation
	 * @throws ClientException
	 */
	public void removeSelectionFromFavorisConsultation() throws ClientException  {
        String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();

        if (!documentsListsManager.isWorkingListEmpty(selectionListName)) {
            List<DocumentModel> docsList = documentsListsManager.getWorkingList(selectionListName);
            Object[] params = { docsList.size() };
            
            EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
            espaceRechercheService.removeFromFavorisConsultation(documentManager, userWorkspaceManagerActions.getCurrentUserPersonalWorkspace().getPathAsString(), docsList);
            
            facesMessages.add(StatusMessage.Severity.INFO, "#0 " + resourcesAccessor.getMessages().get("label.epg.document.retire" + (docsList.size()>1 ? "s": "") ), params);
            documentsListsManager.resetWorkingList(selectionListName);
            navigationContext.resetCurrentDocument();
        } 
    }
	
	public boolean getCanDelete() throws Exception {
		TrashService trashService = Framework.getService(TrashService.class);
		
		String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
        List<DocumentModel> docs = documentsListsManager.getWorkingList(selectionListName);
        try {
            return trashService.canDelete(docs, documentManager.getPrincipal(), false);
        } catch (ClientException e) {
            return false;
        }
    }
	
	/**
	 * Renvoie vrai si l'utilisateur peut utiliser la recherche libre
	 * 
	 * @throws ClientException
	 */
	public boolean canUseRechercheLibre() throws ClientException {
		EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
		return espaceRechercheService.canUseRechercheLibre(documentManager);
		// return true;
	}

	/**
	 * Suppression des documents dans les favoris de recherche.
	 * 
	 * @throws Exception
	 */
	public void deleteSelection() throws Exception  {
        String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();

        if (!documentsListsManager.isWorkingListEmpty(selectionListName)) {
            List<DocumentModel> docsList = documentsListsManager.getWorkingList(selectionListName);
            Object[] params = { docsList.size() };
            
            TrashService trashService = Framework.getService(TrashService.class);
            trashService.trashDocuments(docsList);
            facesMessages.add(StatusMessage.Severity.INFO, "#0 " + resourcesAccessor.getMessages().get("label.epg.document.supprime" + (docsList.size()>1 ? "s": "") ), params);
            documentsListsManager.resetWorkingList(selectionListName);
            navigationContext.resetCurrentDocument();
        } 
    }
	
	public String searchUser() throws ClientException{
	   String view = userManagerActions.searchUser();
	   userManagerActions.setRealSelectedUser(null);
	   return view; 
	}
}
