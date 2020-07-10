package fr.dila.solonepg.web.refresh;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;

import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.st.api.constant.STEventConstant;

/**
 * WebBean qui permet de lancer le rafraissement automatique.
 * 
 * @author arolin
 */
@Name("refreshActions")
@Scope(ScopeType.SESSION)
public class RefreshActionsBean {

    public void refreshPage() {

        // Event qui lance le rafraichissement des listes
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

        // Event qui lance le rafraichissement du document courant
        Events.instance().raiseEvent(STEventConstant.CURRENT_DOCUMENT_CHANGED_EVENT);
    }

}
