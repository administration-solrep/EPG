package fr.dila.solonepg.web.requeteuran;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.contentbrowser.DocumentActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.web.activitenormative.ActiviteNormativeActionsBean;
import fr.dila.solonepg.web.activitenormative.EspaceActiviteNormativeTreeBean;
import fr.dila.solonepg.web.alerte.EPGAlertActionsBean;
import fr.dila.solonepg.web.recherche.FavorisRechercheActionsBean;
import fr.dila.solonepg.web.recherche.RechercheWebActionsBean;
import fr.dila.solonepg.web.recherche.RechercheWebActionsBean.TypeSearch;
import fr.dila.solonepg.web.recherche.TableauDynamiqueActionsBean;
import fr.dila.solonepg.web.requeteur.RequeteurActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.requeteur.RequeteExperte;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean seam pour gérer les actions du requêteur de l'activité normative.
 * 
 * @author jgomez
 */
@Name("EPGANrequeteurExpertActions")
@Scope(ScopeType.CONVERSATION)
public class EPGANRequeteurExpertActionsBean implements Serializable {

	private static final String LABEL_REQUETEUR_TITLE = "label.requeteur.title.";

	private static final String CONTENT_VIEW_RECHERCHE_ACTIVITE_NORMATIVE = "recherche_activite_normative_";
    
	public static final String SMART_FOLDER = "SmartFolder";
    
	private static final long serialVersionUID = 125698L;

    @In(create = true, required = true)
    protected transient RechercheWebActionsBean rechercheWebActions;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient ActiviteNormativeActionsBean activiteNormativeActions;
    
    @In(create = true, required = false)
    protected transient EspaceActiviteNormativeTreeBean espaceActiviteNormativeTree;
    
    @In(create = true, required = false)
    protected transient ActionManager actionManager;
    
    @In(create = true, required = false)
    protected EPGANSmartNXQLQueryActions epgANSmartNXQLQueryActions;

    @In(create = true, required = false)
    protected FavorisRechercheActionsBean favorisRechercheActions;

    @In(create = true, required = false)
    protected TableauDynamiqueActionsBean tableauDynamiqueActions;
    
    @In(create = true, required = false)
    protected RequeteurActionsBean requeteurActions;
    
    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    
    @In(create = true, required = true)
    protected transient EPGAlertActionsBean alertActions;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected DocumentActions documentActions;
    
    private static final Log LOGGER = LogFactory.getLog(EPGANRequeteurExpertActionsBean.class);
    
    /**
     * Va vers les résultats du requêteur.
     * 
     * @return la vue de l'espace de l'activité normative
     * @throws ClientException
     */
    public String goToResults() {
    	return goTo(ActiviteNormativeConstants.TAB_AN_RESULTATS_RECHERCHE);
    }
    
    /**
     * Va vers les résultats du requêteur, en prenant le la bonne cible (application des lois, ordonnances, ...)
     * 
     * @return la vue de l'espace de l'activité normative
     * @throws ClientException
     */
    public String goToResults(String target) {
    	try {
    		  espaceActiviteNormativeTree.setElementId(target);
    	}
    	catch (ClientException e) {
         	facesMessages.add(StatusMessage.Severity.INFO,resourcesAccessor.getMessages().get("error.activiteNormative.recherche.accesResultats"));
         }
    	return goTo(ActiviteNormativeConstants.TAB_AN_RESULTATS_RECHERCHE);
    }

    /**
     * Va vers le requêteur.
     * 
     * @return la vue de l'espace de l'activité normative
     * @throws ClientException
     */
    public String goToRequeteur() {
    	return goTo(ActiviteNormativeConstants.TAB_AN_RECHERCHE);
    }
    
	private String goTo(String tabActionId) {
        try {
			rechercheWebActions.initResults();
			activiteNormativeActions.setCurrentTabAction(actionManager.getAction(tabActionId));
        } 
        catch (ClientException e) {
        	facesMessages.add(StatusMessage.Severity.INFO,resourcesAccessor.getMessages().get("error.activiteNormative.recherche.resetDocument"));
        }
        return SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE;
	}

	public String getContentViewName(){
		return CONTENT_VIEW_RECHERCHE_ACTIVITE_NORMATIVE + espaceActiviteNormativeTree.getCurrentItem().getName();
	}

    /**
     * Les catégories affichées dépendent de l'activité normative dans laquelle on se trouve
     * 
     * @return une liste de catégories
     */
    public List<String> getCategories() {
    	List<String> categories = new ArrayList<String>();
        ActiviteNormativeEnum currentActiviteNormative = espaceActiviteNormativeTree.getActiviteNormativeEnum();
        if (ActiviteNormativeEnum.APPICATION_DES_LOIS.equals(currentActiviteNormative) || ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.equals(currentActiviteNormative)) {
           	categories.add(ActiviteNormativeConstants.RECHERCHE_AN_CATEGORY_DECRET);
           	categories.add(ActiviteNormativeConstants.RECHERCHE_AN_CATEGORY_MESURE);
        }
        categories.add(currentActiviteNormative.getAttributSchema());
        return categories;
    }
	
    /**
     * Renvoie la requête experte complète.
     * 
     * @return la requête experte complète.
     */
    protected String getFullQuery() {
        return epgANSmartNXQLQueryActions.getFullQuery();
    }

    /**
     * Signale si la requête experte contient un champ complexe (numéro NOR) commençant par le caractère '*'
     * 
     * @return
     */
    protected boolean beginWithStarQuery() {
        String fullquery = getFullQuery();
        // TODO voir si besoin de traiter le cas où la requête est vide
        if (fullquery != null && fullquery.equals(DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR)) {
            // si la recherche NOR commence par le caractère '*', on envoie un avertissement à l'utilisateur.
            facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("feedback.search.star"));
            return true;
        }
        return false;
    }    
    
    public String navigateToAddFavoris() {
        if (beginWithStarQuery()) {
            return null;
        }
        rechercheWebActions.setSelectedTypeSearch(TypeSearch.ACTIVITE_NORMATIVE);
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE);
        return favorisRechercheActions.navigateToAddFavoris();
    }

    /**
     * Se dirige vers l'écran d'ajout d'un tableau dynamique
     * 
     * @return la vue de l'ajout d'un tableau, null en cas d'échec
     */
    public String navigateToAddTableauDynamique() {
        if (beginWithStarQuery()) {
            return null;
        }
        rechercheWebActions.setSelectedTypeSearch(TypeSearch.ACTIVITE_NORMATIVE);
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE);
        try {
            return tableauDynamiqueActions.navigateToAddTableauDynamique();
        } catch (ClientException e) {
            LOGGER.error("Impossible de naviguer vers l'ajout de tableau dynamique à partir de la recherche experte", e);
            facesMessages.add(StatusMessage.Severity.ERROR, "Impossible de naviguer vers l'ajout de tableau dynamique");
            return null;
        }
    }
    
    /**
     * Le titre du requêteur
     * 
     * @return un titre
     */
    public String getTitle() {
        return LABEL_REQUETEUR_TITLE + espaceActiviteNormativeTree.getActiviteNormativeEnum().getAttributSchema();
    }
    
    public String generateAlert() {
        try {
            if (beginWithStarQuery()) {
                return null;
            }
            RequeteExperte requeteExperte = saveQueryAsRequeteExperte();
            // Sauvegarde la recherche, pour pouvoir revenir en arrière si besoin
            return alertActions.createAlertFromRequeteForAN(requeteExperte.getDocument());
        } catch (ClientException e) {
            LOGGER.error("Erreur lors de la génération de l'alerte", e);
            facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get("error.generation_alert.failed"));
            return null;
        }
    }

    
    /**
     * Sauvegarde un document à partir des valeurs mise dans le bean seam smartQueryActions
     * 
     * @param docType
     * @return
     * @throws ClientException
     */
    public RequeteExperte saveQueryAsRequeteExperte() throws ClientException {
        if (beginWithStarQuery()) {
            return null;
        }
        navigateToUserRequeteRoot();
        DocumentModel doc = documentManager.createDocumentModel("SmartFolder");
        RequeteExperte requeteExperte = doc.getAdapter(RequeteExperte.class);
        String queryPart = epgANSmartNXQLQueryActions.getQueryPart();
        requeteExperte.setWhereClause(queryPart);
        navigationContext.setChangeableDocument(doc);
        //création du document 
        documentActions.saveDocument();
        //récupération du document
        doc = navigationContext.getChangeableDocument();
        return doc.getAdapter(RequeteExperte.class);
    }


    /**
     * Va vers la racine des requêtes utilisateurs.
     * 
     * @throws ClientException
     */
    public void navigateToUserRequeteRoot() throws ClientException {
    	requeteurActions.navigateToUserRequeteRoot();
    }
    
}