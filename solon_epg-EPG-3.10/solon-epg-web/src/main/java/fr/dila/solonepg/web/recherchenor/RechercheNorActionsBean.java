package fr.dila.solonepg.web.recherchenor;

import static fr.dila.solonepg.api.constant.RechercheConstants.RECHERCHE_NOR_CONTENT_VIEW;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;

import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.web.espace.EspaceSuiviActionsBean;
import fr.dila.solonepg.web.recherche.RechercheWebActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.core.util.FullTextUtil;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Une classe bean pour les traitements spécifiques à la recherche simple.
 * 
 * @author jgomez
 */
@Name("rechercheNorActions")
@Scope(ScopeType.CONVERSATION)
public class RechercheNorActionsBean implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(RechercheNorActionsBean.class);

    @In(create = true, required = true)
    protected ContentViewActions contentViewActions;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = true)
    protected transient RechercheWebActionsBean rechercheWebActions;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true, required = false)
    protected transient ActionManager actionManager;

    @In(create = true)
    protected transient NavigationWebActionsBean navigationWebActions;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true)
    protected transient EspaceSuiviActionsBean espaceSuiviActions;

    /**
     * Critère de recherche : n° de NOR.
     */
    protected String nor;

    /**
     * Préparation pour les résultats de la requête NOR.
     * 
     * @return
     * @throws ClientException
     */
    public String goToResults() throws ClientException {
        if (StringUtils.isBlank(getNor())) {
            return "";
        } else {
            setNor(getNor().trim());
        }

        // Réinitialise le document courant
        navigationContext.resetCurrentDocument();

        rechercheWebActions.initResults();
        contentViewActions.refresh(RECHERCHE_NOR_CONTENT_VIEW);

        return navigateToNor();
    }

    private String navigateToNor() {
        String espaceView = StringUtils.EMPTY;
        // Si on est dans l'espace de suivi, l'affichage des resultats se fait dans l'espace de suivi
        if (SolonEpgViewConstant.ESPACE_SUIVI_VIEW.equals(routingWebActions.getFeuilleRouteView())) {
            espaceView = espaceSuiviActions.navigateToWithRechercheNorContentView();
        }
        // Sinon on va dans l'espace des résultats de recherche
        else {
            espaceView = navigateToRecherche();
        }
        return espaceView;
    }

    private String navigateToRecherche() {
        // Renseigne le menu du haut
        Action mainMenuAction = actionManager.getAction(SolonEpgActionConstant.ESPACE_RECHERCHE);
        navigationWebActions.setCurrentMainMenuAction(mainMenuAction);

        // Renseigne le panneau de gauche
        Action leftMenuAction = actionManager.getAction(SolonEpgActionConstant.LEFT_MENU_ESPACE_RECHERCHE);
        navigationWebActions.setCurrentLeftMenuAction(leftMenuAction);

        // Renseigne la vue de route des étapes de feuille de route vers les dossiers
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.RECHERCHE_RESULTATS_NOR_VIEW);
        // rechercheWebActions.setContentViewName(RECHERCHE_NOR_CONTENT_VIEW);

        return SolonEpgViewConstant.RECHERCHE_RESULTATS_NOR_VIEW;
    }

    public String getNor() {
        return nor;
    }

    public String getModifiedNor() {
        if (nor == null) {
            return nor;
        } else {
            return FullTextUtil.replaceStarByPercent(nor.toUpperCase());
        }
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

	public String getQuery() {
		String query = "SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d WHERE d.dos:archive = 0 AND testAcl(d.ecm:uuid) = 1 AND d.dos:deleted=0";
		String modifiedNor = getModifiedNor();
		if (modifiedNor != null) {
			String[] nors = modifiedNor.split(";");
			query += " AND (";
			if (nors.length > 0) {
				for (String nor : nors) {
					query += "d.dos:numeroNor LIKE '" + nor.trim() + "' OR ";
				}
				query = query.substring(0, query.length() - 3);
			} else {
				query += "d.dos:numeroNor LIKE ''";
			}
			query += ")";
		}
		return query;
	}
}