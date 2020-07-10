package fr.dila.solonepg.web.requeteur;

import static fr.dila.solonepg.api.constant.RechercheConstants.REQUETEUR_CONTENT_VIEW;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.convert.Converter;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.contentbrowser.DocumentActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgRequeteurConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.web.alerte.EPGAlertActionsBean;
import fr.dila.solonepg.web.recherche.FavorisRechercheActionsBean;
import fr.dila.solonepg.web.recherche.RechercheWebActionsBean;
import fr.dila.solonepg.web.recherche.RechercheWebActionsBean.TypeSearch;
import fr.dila.solonepg.web.recherche.RequeteurDossierSimpleActionsBean;
import fr.dila.solonepg.web.recherche.TableauDynamiqueActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.alert.Alert;
import fr.dila.st.api.constant.STAlertConstant;
import fr.dila.st.api.constant.STRequeteurExpertConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.requeteur.RequeteExperte;
import fr.dila.st.api.service.STRequeteurWidgetGeneratorService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.requeteur.STRequeteurConverterActions;

/**
 * Bean seam pour gérer les actions du requeteur.
 * 
 * @author jgomez
 */
@Name("requeteurActions")
@Scope(ScopeType.CONVERSATION)
public class RequeteurActionsBean implements Serializable {

    /**
	 * Serial UID
	 */
	private static final long serialVersionUID = -5033539881594155931L;

	private static final String MESREQUETES_PATH = "/mesrequetes/";

    public static final String SMART_FOLDER = "SmartFolder";

    @In(create = true, required = true)
    protected transient RechercheWebActionsBean rechercheWebActions;

    @In(create = true, required = true)
    protected ContentViewActions contentViewActions;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true, required = false)
    protected transient ActionManager actionManager;

    @In(create = true)
    protected transient NavigationWebActionsBean navigationWebActions;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected DocumentActions documentActions;

    @In(create = true, required = true)
    protected transient EPGSmartNXQLQueryActions smartNXQLQueryActions;

    @In(create = true, required = true)
    protected transient EPGAlertActionsBean alertActions;
    
    @In(create = true, required = true)
    protected transient RequeteurDossierSimpleActionsBean requeteurDossierSimpleActions;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient UserWorkspaceManagerActionsBean userWorkspaceManagerActions;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected FavorisRechercheActionsBean favorisRechercheActions;

    @In(create = true, required = false)
    protected TableauDynamiqueActionsBean tableauDynamiqueActions;

    @In(create = true, required = false)
    protected STRequeteurConverterActions stRequeteurConverterActions;

    private static final STLogger LOGGER = STLogFactory.getLog(RequeteurActionsBean.class);

    /**
     * Liste des catégories autorisées pour l'utilisateur dans la recherche experte
     */
    protected List<String> categories;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    /**
     * Retourne le converter approprié à partir du searchField
     * 
     * @param searchField
     * @return
     */
    public Converter getConverter(String searchField) {
        return stRequeteurConverterActions.getConverter(searchField, SolonEpgRequeteurConstant.REQUETEUR_EXPERT_DOSSIER_CONTRIBUTION);
    }

    /**
     * Va vers les résultats du requêteur.
     * 
     * @return la vue des résultats du requêteur.
     * @throws ClientException
     */
    public String goToResults() throws ClientException {
        if (beginWithStarQuery()) {
            return null;
        }
        rechercheWebActions.initResults();
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.RECHERCHE_RESULTATS_REQUETEUR_VIEW);
        // rechercheWebActions.setContentViewName(getContentViewName());
        return SolonEpgViewConstant.RECHERCHE_RESULTATS_REQUETEUR_VIEW;
    }

    public String generateAlert() {
        try {
            if (beginWithStarQuery()) {
                return null;
            }
            RequeteExperte requeteExperte = saveQueryAsRequeteExperte();
            // Sauvegarde la recherche, pour pouvoir revenir en arrière si besoin
            return alertActions.createAlertFromRequete(requeteExperte.getDocument());

        } catch (ClientException e) {
        	LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_ALERT_TEC, e);
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
        DocumentModel doc = documentManager.createDocumentModel(SMART_FOLDER);
        RequeteExperte requeteExperte = doc.getAdapter(RequeteExperte.class);
        String queryPart = getQueryPart();
        requeteExperte.setWhereClause(queryPart);
        navigationContext.setChangeableDocument(doc);
        // création du document
        documentActions.saveDocument();
        // récupération du document
        doc = navigationContext.getChangeableDocument();
        return doc.getAdapter(RequeteExperte.class);
    }

    /**
     * Renvoie la requête experte
     * 
     * @return la requête experte
     */
    protected String getQueryPart() {
    	if (rechercheWebActions.isRechercheDossierSimple()){
    		try {
    			return requeteurDossierSimpleActions.getCurrentRequetePattern();
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_UFNXQL_TEC);
			}
        } else if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView()) && !rechercheWebActions.isRechercheDossierSimple()){
        	return smartNXQLQueryActions.getQueryPart();
        }
    	return "";
    }
    
    /**
     * Renvoie la requête experte
     * 
     * @return la requête experte
     */
    public String getQueryPartFromAlert() {
    	DocumentModel changeableDocument = navigationContext.getCurrentDocument();
        if (!changeableDocument.hasSchema(STAlertConstant.ALERT_SCHEMA)) {
            changeableDocument = navigationContext.getChangeableDocument();
        }
        Alert alert = changeableDocument.getAdapter(Alert.class);
    	String requete = alert.getRequeteExperte(documentManager).getWhereClause();
    	return getQueryFromSimpleSearch(requete);
    }
    
    public String getQueryPartFromTableauDynamique() {    	
    	String requete = null;
    	DocumentModel tabDynDoc = tableauDynamiqueActions.getCurrentTableauDynamique();
    	if (tabDynDoc != null) {
    		final TableauDynamique tableauDynamique = tabDynDoc.getAdapter(TableauDynamique.class);
    		requete = tableauDynamique.getQuery();
    	} else if (rechercheWebActions.isRechercheDossierSimple()){
            try {
				requete = requeteurDossierSimpleActions.getCurrentRequetePattern();
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_UFNXQL_TEC);
			}
        } else if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView()) && !rechercheWebActions.isRechercheDossierSimple()){
        	requete = smartNXQLQueryActions.getFullQuery();
        }
    	if (requete == null) {
    		requete = "";
    	}
    	return getQueryFromSimpleSearch(requete);
    }
    
    /**
     * Renvoie la requête experte à partir d'une requête de recherche simple.
     * La requête initiale est nettoyée des champs non reconnus par la requête experte
     * et le requeteur est initialisé avec cette nouvelle requête
     * 
     * @return la requête experte
     */
    private String getQueryFromSimpleSearch(String requete) {
    	// Suivant l'origine de la requete, elle contient ou non WHERE et testAcl
    	int startRequest = requete.indexOf("WHERE");
    	if (startRequest == -1) {
    		startRequest = 0;
    	}
    	int endRequest = requete.indexOf("AND testAcl");
    	if (endRequest == -1) {
    		endRequest = requete.length();
    	}
    	requete = requete.substring(startRequest, endRequest);
    	// On nettoie les filtres suivants qui peuvent être présents
    	requete = requete.replaceAll("WHERE ", "")
    				.replaceAll(" AND f.filepg:relatedDocument = d.ecm:uuid", "")
    				.replaceAll(" AND e.rtsk:documentRouteId = d.dos:lastDocumentRoute", "")
    				.replaceAll(" AND c.commentedDocId = e.ecm:uuid", "");
    	
    	// Pattern regex pour récupérer les données entre simple quotes et % (recherche LIKE champs string)
    	Pattern p = Pattern.compile("(?<!\\\\)'%(.*?)(?<!\\\\)%'");
    	Matcher m = p.matcher(requete);
    	String[] requeteSplit = requete.split(p.pattern());
    	int i = 0;
    	StringBuilder cleanedRequest = new StringBuilder(requeteSplit[i++]);
    	
    	while (m.find()) {
    		// Pour chaque résultat, on modifie le '=' de la requête en 'LIKE'
    		if (cleanedRequest.lastIndexOf("=") == cleanedRequest.length() - 2) {
    			cleanedRequest.deleteCharAt(cleanedRequest.lastIndexOf("=")).append("LIKE ");
    		}
    		// on ajoute des doubles quotes, et on retire les simples quotes et les %
    		cleanedRequest.append("\"")
    			.append(requete.substring(m.start()+2, m.end()-2)).append("\"")
    			.append(requeteSplit[i++]);
    	}
    	// On modifie les caractères d'échappement des simples quotes qui ne sont plus nécessaires
    	requete = cleanedRequest.toString().replaceAll("\\\\'", "'");
    	smartNXQLQueryActions.initCurrentSmartQuery(requete, true);
    	return getQueryPart();
    }

    /**
     * Renvoie la requête experte complète.
     * 
     * @return la requête experte complète.
     */
    protected String getFullQuery() {
        return smartNXQLQueryActions.getFullQuery();
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

    /**
     * Va vers la racine des requêtes utilisateurs.
     * 
     * @throws ClientException
     */
    public void navigateToUserRequeteRoot() throws ClientException {
        userWorkspaceManagerActions.navigateToCurrentUserPersonalWorkspace();
        String requeteRootPath = userWorkspaceManagerActions.getCurrentUserPersonalWorkspace().getPathAsString() + MESREQUETES_PATH;
        navigationContext.navigateToRef(new PathRef(requeteRootPath));
    }

    public String getContentViewName() {
        return REQUETEUR_CONTENT_VIEW;
    }

    public ContentView getContentView() throws ClientException {
        return contentViewActions.getContentViewWithProvider(getContentViewName());
    }

    public void reset() {
        contentViewActions.resetPageProvider(getContentViewName());
        categories = null;
    }

    public Boolean isSortable() {
        if (routingWebActions.getFeuilleRouteView() != null && routingWebActions.getFeuilleRouteView().startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String navigateToAddFavoris() {
        if (beginWithStarQuery()) {
            return null;
        }
        rechercheWebActions.setSelectedTypeSearch(TypeSearch.DOSSIER_EXPERT);
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER);
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
        rechercheWebActions.setSelectedTypeSearch(TypeSearch.DOSSIER_EXPERT);
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER);
        try {
            return tableauDynamiqueActions.navigateToAddTableauDynamique();
        } catch (ClientException e) {
        	LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_NAVIGATE_TO_TAB_DYN_FONC, e);
            facesMessages.add(StatusMessage.Severity.ERROR, "Impossible de naviguer vers l'ajout de tableau dynamique");
            return null;
        }
    }

    public String getSortPropertyName(String name) {

        String propertyName = "";

        String view = routingWebActions.getFeuilleRouteView();

        if (view != null) {
            if (view.startsWith(SolonEpgViewConstant.VIEW_ESPACE_RECHERCHE_FDR) || view.startsWith(SolonEpgViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT)) {
                propertyName = name;
            } else if ("col1".equals(name)) {
                if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_DOSSIER_VIEW)) {
                    propertyName = "d.dos:numeroNor";
                } else if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_USER_VIEW)) {
                    propertyName = "username";
                }
            } else if ("col2".equals(name)) {
                if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_DOSSIER_VIEW)) {
                    propertyName = "d.dc:created";
                } else if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_USER_VIEW)) {
                    propertyName = "lastName";
                }
            } else if ("col3".equals(name)) {
                if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_DOSSIER_VIEW)) {
                    propertyName = "";
                } else if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_USER_VIEW)) {
                    propertyName = "firstName";
                }
            } else if ("col4".equals(name)) {
                if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_DOSSIER_VIEW)) {
                    propertyName = "";
                } else if (view.startsWith(SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_USER_VIEW)) {
                    propertyName = "dateDebut";
                }
            }
        }

        return propertyName;
    }

    /**
     * Retourne les catégories possibles pour les widgets
     * 
     * @return une liste de catégories
     */
    public List<String> getCategories() {
        if (categories == null || categories.size() < 1) {
            final STRequeteurWidgetGeneratorService generateurService = STServiceLocator.getSTRequeteurWidgetGeneratorService();
            // récupération de toutes les catégories disponible
            categories = generateurService.getRequeteurContribution(SolonEpgRequeteurConstant.REQUETEUR_EXPERT_DOSSIER_CONTRIBUTION).getCategories();
            // la categorie "tous" n'est pas utilisée
            categories.remove(STRequeteurExpertConstants.REQUETEUR_EXPERT_ALL_CATEGORY);
            categories.remove(STRequeteurExpertConstants.REQUETEUR_EXPERT_HIDDEN_CATEGORY);
            if (!ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_READER)) {
                categories.remove(SolonEpgRequeteDossierConstants.CATEGORIE_TRAITEMENT_PAPIER);
            }
            if (!ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER)) {
                categories.remove(SolonEpgRequeteDossierConstants.CATEGORIE_ACTIVITE_NORMATIVE_LOI);
            }
            if (!ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER)) {
                categories.remove(SolonEpgRequeteDossierConstants.CATEGORIE_ACTIVITE_NORMATIVE_ORDONNANCE);
            }
            if (!ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_SGG_READER)) {
                categories.remove(SolonEpgRequeteDossierConstants.CATEGORIE_TEXTE_SIGNALE);
            }

        }
        return categories;
    }

}