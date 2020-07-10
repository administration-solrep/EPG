package fr.dila.solonepg.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;

import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

@Name("espaceParlementaireActions")
@Scope(CONVERSATION)
public class EspaceParlementaireActionsBean implements Serializable {
private static final long serialVersionUID = 1L;
    
    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;
    
    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;
    
    @In(create = true)
    protected transient NavigationWebActionsBean navigationWebActions;
    
    @In(create = true, required = false)
    protected transient ActionManager actionManager;

    /**
     * Navigation vers l'espace de traitement.
     * 
     * @return Vue de l'espace de traitement
     * @throws ClientException
     */
    public String navigateTo() throws ClientException {
        // Réinitialise le document courant
        navigationContext.resetCurrentDocument();

        // Renseigne le menu du haut
        Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE);
        navigationWebActions.setCurrentMainMenuAction(mainMenuAction);
        
        // Renseigne le menu de gauche
        navigationWebActions.setCurrentLeftMenuAction(null);
        
        // Renseigne la vue de route des étapes de feuille de route vers les dossiers
        routingWebActions.setFeuilleRouteView(STViewConstant.EMPTY_VIEW);

        return STViewConstant.EMPTY_VIEW;
    }
    
}
