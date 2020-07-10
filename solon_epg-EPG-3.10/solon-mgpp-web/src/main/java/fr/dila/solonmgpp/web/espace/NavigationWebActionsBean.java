package fr.dila.solonmgpp.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.platform.actions.Action;

/**
 * override de {@link fr.dila.solonepg.web.espace.NavigationWebActionsBean} pour pouvoir ajouter une deuxieme barre de menu
 * 
 * @author asatre
 * 
 */
@Name("navigationWebActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION + 2)
public class NavigationWebActionsBean extends fr.dila.solonepg.web.espace.NavigationWebActionsBean {

    private static final long serialVersionUID = -2170432778113811409L;

    public static final String MENU_CHANGE_EVENT = "MENU_CHANGE_EVENT";

    /**
     * Action du menu principal sélectionné.
     */
    @Out(required = false)
    protected Action currentSecondMenuAction;
    /**
     * Action du menu principal sélectionné.
     */
    @Out(required = false)
    protected String currentRechercheCategory;

    /**
     * Retourne le menu secondaire sélectionné.
     * 
     * @return Menu secondaire sélectionné
     */
    public Action getCurrentSecondMenuAction() {
        return currentSecondMenuAction;
    }

    /**
     * Renseigne le menu secondaire sélectionné.
     * 
     * @param currentMainMenuAction Menu secondaire sélectionné
     */
    public void setCurrentSecondMenuAction(Action currentSecondMenuAction) {
        if (this.currentSecondMenuAction == null || currentSecondMenuAction == null
                || !this.currentSecondMenuAction.getId().equals(currentSecondMenuAction.getId())) {
            // Event de changement de menu
            Events.instance().raiseEvent(MENU_CHANGE_EVENT);
        }
        this.currentSecondMenuAction = currentSecondMenuAction;

    }

    @Override
    public void setCurrentMainMenuAction(Action currentMainMenuAction) {
        
        if (this.currentMainMenuAction == null || currentMainMenuAction == null
                || !this.currentMainMenuAction.getId().equals(currentMainMenuAction.getId())) {
            setCurrentSecondMenuAction(null);
        }
        
        this.currentMainMenuAction = currentMainMenuAction;

       
    }

    public String getCurrentRechercheCategory() {
      return currentRechercheCategory;
    }

    public void setCurrentRechercheCategory(String currentRechercheCategory) {
      this.currentRechercheCategory = currentRechercheCategory;
    }
}
