package fr.dila.solonepg.web.recherche;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;

/**
 * Le bean gérant les actions communes aux actions du requeteur de dossier simple
 * et au requeteur de dossier expert.
 * @author jgomez
 *
 */
@Name("requeteurDossierActions")
@Scope(ScopeType.CONVERSATION)
public class RequeteurDossierActionsBean implements Serializable {

	private static final String TAB_REQUETEUR_DOSSIER_SIMPLE = "TAB_REQUETEUR_DOSSIER_SIMPLE";

    private static final long serialVersionUID = 1L;
	
    @In(create = true, required = false)
	protected transient NavigationContext navigationContext;
    
    @In(create = true, required = false)
    protected transient ActionManager actionManager;

    private Action currentTabAction;
	
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(RequeteurDossierActionsBean.class);
    
    /**
     * Positionne l'action courante du requeteur de dossier
     * @param currentTabAction
     */
    public void setCurrentTabAction(Action currentTabAction) {
        this.currentTabAction = currentTabAction;
    }

    /**
     * Retourne l'action courante du requêteur de dossier
     * @return
     */
    public Action getCurrentTabAction() {
        if (this.currentTabAction == null){
            return actionManager.getAction(TAB_REQUETEUR_DOSSIER_SIMPLE);
        }
        return this.currentTabAction;
    }


}
