package fr.dila.solonepg.web.recherche;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean seam pour gérer les actions communes des recherches.
 * 
 * @author jgomez
 */
@Name("rechercheWebActions")
@Scope(ScopeType.CONVERSATION)
public class RechercheWebActionsBean implements Serializable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient ActionManager actionManager;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true)
    protected transient NavigationWebActionsBean navigationWebActions;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true, required = false)
    protected transient CorbeilleActionsBean corbeilleActions;

    @In(create = true)
    protected transient ContentViewActions contentViewActions;

    public static final String VIEW_RECHERCHE_BANDEAU_SERVICE = "view_recherche_bandeau_service";

    public enum TypeSearch {
        DOSSIER_SIMPLE, DOSSIER_EXPERT, ACTIVITE_NORMATIVE
    };

    /**
     * Utlisé pour distinguer le type de recherche que l'on fait
     */
    public TypeSearch selectedTypeSearch;

    /**
     * Navigue vers l'espace de recherche.
     * 
     * @return Vue
     * @throws ClientException
     */
    public String navigateTo() throws ClientException {
        // Réinitialise le document courant
        navigationContext.resetCurrentDocument();

        // Renseigne le menu du haut
        Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_RECHERCHE);
        navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

        // Renseigne le panneau de gauche
        Action leftMenuAction = actionManager.getAction(SolonEpgActionConstant.LEFT_MENU_ESPACE_RECHERCHE);
        navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

        // Renseigne la vue de route des étapes de feuille de route vers les dossiers
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER);

        return SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER;
    }

    /**
     * Retourne l'action qui affiche la mini-vue de la recherche simple dans le bandeau du haut.
     * 
     * @return L'action de la recherche simple
     */
    public Action getRechercheBandeauServiceAction() {
        return actionManager.getAction(VIEW_RECHERCHE_BANDEAU_SERVICE);
    }

    public void initResults() throws ClientException {
        navigationContext.resetCurrentDocument();
    }

    public void setSelectedTypeSearch(TypeSearch typeSearch) {
        selectedTypeSearch = typeSearch;
    }

    public Boolean isRechercheDossierSimple() {
        return selectedTypeSearch == TypeSearch.DOSSIER_SIMPLE;
    }

    public Boolean isRechercheDossierExpert() {
        return selectedTypeSearch == TypeSearch.DOSSIER_EXPERT;
    }

    public boolean displayDossierRecherche() throws ClientException {
        if (navigationContext.getCurrentDocument() != null
                && navigationContext.getCurrentDocument().getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)) {
            return true;
        }
        return corbeilleActions.oneDossierInList(contentViewActions.getCurrentContentView().getName());
    }

}