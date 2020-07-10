package fr.dila.solonepg.web.espace;

import static fr.dila.solonepg.api.constant.RechercheConstants.RECHERCHE_NOR_CONTENT_VIEW;
import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

@Name("espaceSuiviActions")
@Scope(CONVERSATION)
public class EspaceSuiviActionsBean implements Serializable {
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
    protected transient DocumentsListsManager documentsListsManager;
    
    private String contentViewName;
    
    /**
     * Navigue vers l'espace de suivi.
     * 
     * @throws ClientException
     */
    public String navigateTo() throws ClientException {
        // Réinitialise le document courant
        navigationContext.resetCurrentDocument();

        // Renseigne le menu du haut
        Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_SUIVI);
        navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

        //routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_VIEW);
        Action leftMenuAction = actionManager.getAction(SolonEpgActionConstant.LEFT_MENU_ESPACE_SUIVI);
        navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

        // Renseigne la vue de route des étapes de feuille de route vers les dossiers
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_VIEW);
        setContentViewName(SolonEpgContentViewConstant.ESPACE_SUIVI_DOSSIERS_CONTENT_VIEW);
        
        return SolonEpgViewConstant.ESPACE_SUIVI_VIEW;
    }
    
    
    /**
     * Navigue vers l'espace de suivi avec la content view des dossiers.
     * 
     * @throws ClientException
     */
    public String navigateToWithDossierContentView(){
        setContentViewName(SolonEpgContentViewConstant.ESPACE_SUIVI_DOSSIERS_CONTENT_VIEW);
        return SolonEpgViewConstant.ESPACE_SUIVI_VIEW;
    }
    
    /**
     * Navigue vers l'espace de suivi avec la content view de la recherhe par NOR.
     * 
     * @throws ClientException
     */
    public String navigateToWithRechercheNorContentView(){
        setContentViewName(RECHERCHE_NOR_CONTENT_VIEW);
        return SolonEpgViewConstant.ESPACE_SUIVI_VIEW;
    }

    /**
     * Renvoie le répertoire contenant les dossiers signalés de l'utilisateur.
     * 
     * @return idDossiersSignalesFolder
     * @throws ClientException
     */
    public String getQueryDossiersSignales() throws ClientException {
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_DOSSIER_SIGNALES_VIEW);
        DossierSignaleService dossierSignaleService= SolonEpgServiceLocator.getDossierSignaleService();
        return dossierSignaleService.getQueryDossierSignale(documentManager);
    }
    
    /**
     * Vide la corbeille des dossiers signalés de l'utilisateur.
     * 
     * @throws ClientException
     */
    public void viderCorbeilleDossiersSignales() throws ClientException {
        DossierSignaleService dossierSignaleService= SolonEpgServiceLocator.getDossierSignaleService();
        try {
            dossierSignaleService.viderCorbeilleDossiersSignales(documentManager);
            Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
        } catch (ClientException e) {
            throw new ClientException("Impossible de vider la corbeille des dossiers signales !");
        } 
    }

    /**
     * Retirer les dossiers signalés sélectionné de la corbeille de l'utilisateur.
     * 
     * @throws ClientException
     */
    public void retirerDossiersSignales() throws ClientException {
        List<DocumentModel> selection = documentsListsManager.getWorkingList(SolonEpgConstant.DOSSIERS_SIGNALES_SELECTION);
        DossierSignaleService dossierSignaleService= SolonEpgServiceLocator.getDossierSignaleService();
        try {
            dossierSignaleService.retirer(documentManager, selection);
            Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
        } catch (ClientException e) {
            throw new ClientException("Impossible de vider la corbeille des dossiers signales !");
        } 
    }
    

    /**
     * Met la content view utilisée pour afficher les dossiers de l'espace de suivi.
     */
    public void setContentViewName(String contentViewName){
        this.contentViewName = contentViewName;
    }
    
    /**
     * Retourne la content view utilisée pour afficher les dossiers de l'espace de suivi.
     */
    public String getContentViewName(){
        return this.contentViewName;
    }
}
