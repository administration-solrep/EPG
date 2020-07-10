package fr.dila.solonepg.web.recherche;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.activitenormative.ActiviteNormativeActionsBean;
import fr.dila.solonepg.web.activitenormative.EspaceActiviteNormativeTreeBean;
import fr.dila.solonepg.web.requeteur.EPGSmartNXQLQueryActions;
import fr.dila.solonepg.web.requeteur.RequeteurActionsBean;
import fr.dila.solonepg.web.requeteuran.EPGANRequeteurExpertActionsBean;
import fr.dila.solonepg.web.requeteuran.EPGANSmartNXQLQueryActions;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.user.STUser;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam permettant de gérer les favoris de recherche.
 * 
 * @author asartre
 * @author jtremeaux
 */
@Name("favorisRechercheActions")
@Scope(ScopeType.CONVERSATION)
public class FavorisRechercheActionsBean implements Serializable {
    /**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7090738500397217742L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient UserWorkspaceManagerActionsBean userWorkspaceManagerActions;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true, required = false)
    protected transient EPGSmartNXQLQueryActions smartNXQLQueryActions;

    @In(create = true, required = false)
    protected transient UserManagerActionsBean userManagerActions;

    @In(create = true, required = false)
    protected transient RequeteurActionsBean requeteurActions;

    @In(create = true, required = false)
    protected transient RechercheModeleFeuilleRouteActionsBean rechercheModeleFeuilleRouteActions;

    @In(create = true, required = false)
    protected transient RequeteurDossierSimpleActionsBean requeteurDossierSimpleActions;

    @In(create = true, required = false)
    protected transient RechercheWebActionsBean rechercheWebActions;
    
    @In(create = true, required = false)
    protected EPGANSmartNXQLQueryActions epgANSmartNXQLQueryActions;
    
    @In(create = true, required = false)
    protected EPGANRequeteurExpertActionsBean EPGANrequeteurExpertActions;

    @In(create = true, required = false)
    protected transient ActiviteNormativeActionsBean activiteNormativeActions;
    
    @In(create = true)
    protected ResourcesAccessor resourcesAccessor;
    
    @In(create = true, required = false)
    protected transient EspaceActiviteNormativeTreeBean espaceActiviteNormativeTree;


    /**
     * Intitulé du favori de recherche.
     */
    private String intitule;

    /**
     * Postes de diffusion du favori de recherche.
     */
    private List<String> postes;

    /**
     * Sauvegarde un favori de recherche.
     * 
     * @return Vue
     * @throws ClientException
     */
    public String save() throws ClientException {
        Boolean error = Boolean.FALSE;
        //note : si aucune diffusion n'est défini, on enregistre le favoris de recherche pour l'utilisateur courant.
        if (intitule == null || intitule.isEmpty()) {
            facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("favoris.recherche.intitule.vide"));
            error = Boolean.TRUE;
        }

        String queryPart = null;
        String type = null;
        if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView())
                && rechercheWebActions.isRechercheDossierExpert()) {
            queryPart = smartNXQLQueryActions.getQueryPart();
            type = SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_DOSSIER_VALUE;

        } else if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView())
                && rechercheWebActions.isRechercheDossierSimple()) {
            queryPart = requeteurDossierSimpleActions.getCurrentRequetePattern();
            type = SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_DOSSIER_SIMPLE_VALUE;
        } else if (SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR.equals(routingWebActions.getFeuilleRouteView())) {
            queryPart = userManagerActions.getQueryPart();
            type = SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_USER_VALUE;
        } else if (SolonEpgViewConstant.RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT.equals(routingWebActions.getFeuilleRouteView()) 
        		|| SolonEpgViewConstant.VIEW_ESPACE_RECHERCHE_FDR.equals(routingWebActions.getFeuilleRouteView())) {
            queryPart = rechercheModeleFeuilleRouteActions.getSearchQueryString();
            type = SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_MODELE_FEUILLE_ROUTE_VALUE;
        } else if (SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE.equals(routingWebActions.getFeuilleRouteView())) {
            queryPart = epgANSmartNXQLQueryActions.getQueryPart();
            type = SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_ACTIVITE_NORMATIVE_VALUE + "." + espaceActiviteNormativeTree.getCurrentItem().getName();
        }

        if (StringUtils.isBlank(queryPart)) {
            facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("favoris.recherche.query.vide"));
            error = Boolean.TRUE;
        } else if (DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR.equals(queryPart)) {
            facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("feedback.search.star"));
            error = Boolean.TRUE;
        }

        if (!error) {
            final EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
            List<DocumentModel> listUserError = null;
            if (SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER.equals(routingWebActions.getFeuilleRouteView())
                    || SolonEpgViewConstant.ESPACE_RECHERCHE_UTILISATEUR.equals(routingWebActions.getFeuilleRouteView()) || SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE.equals(routingWebActions.getFeuilleRouteView())) {
                listUserError = espaceRechercheService.addToFavorisRecherche(documentManager, postes, intitule, queryPart, type);
            } else {
                DocumentModel favorisRechercheDoc = rechercheModeleFeuilleRouteActions.getFavoriRechercheDoc();
                FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);
                favorisRecherche.setTitle(intitule);
                listUserError = espaceRechercheService.addToFavorisRecherche(documentManager, postes, favorisRechercheDoc);
            }
            if (listUserError != null && !listUserError.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                int cpt = 0;
                for (DocumentModel userModel : listUserError) {

                    STUser stUser = userModel.getAdapter(STUser.class);
                    String name = null;
                    
                    if (stUser.getLastName() == null || stUser.getFirstName() == null) {
                        if (stUser.getFirstName() == null) {
                            name = stUser.getLastName();
                        }
                    } else {
                        name = stUser.getFirstName() + " " + stUser.getLastName();
                    }
                    
                    
                    builder.append(name);

                    if (cpt > 0 && cpt < listUserError.size()) {
                        builder.append(", ");
                    }
                }

                facesMessages.add(StatusMessage.Severity.WARN,
                        resourcesAccessor.getMessages().get("favoris.recherche.limite.atteinte") + " " + builder.toString());
            }
        }

        postes = null;
        intitule = null;

        return routingWebActions.getFeuilleRouteView();
    }

    /**
     * Charge un document favori de recherche et effectue la recherche.
     * 
     * @param favoriRechercheDoc Document favori de recherche
     * @return Vue
     * @throws ClientException
     */
    public String navigateToRecherche(DocumentModel favoriRechercheDoc) throws ClientException {
        FavorisRecherche favoriRecherche = favoriRechercheDoc.getAdapter(FavorisRecherche.class);
        if (favoriRecherche.isTypeActiviteNormative()) {
        	epgANSmartNXQLQueryActions.initCurrentSmartQuery(favoriRecherche.getQueryPart(), false);
        	epgANSmartNXQLQueryActions.setQueryPart(favoriRecherche.getQueryPart());
        	activiteNormativeActions.navigateToEspaceActiviteNormative();
            return EPGANrequeteurExpertActions.goToResults(favoriRecherche.getActiviteNormativeTarget());
        }
        if (favoriRecherche.isTypeDossier()) {
            smartNXQLQueryActions.initCurrentSmartQuery(favoriRecherche.getQueryPart(), false);
            smartNXQLQueryActions.setQueryPart(favoriRecherche.getQueryPart());
            return requeteurActions.goToResults();
        }
        if (favoriRecherche.isTypeDossierSimple()) {
            navigationContext.resetCurrentDocument();
            requeteurDossierSimpleActions.setFavoriRechercheDoc(favoriRechercheDoc);
            routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTAT_UTILISATEUR);
            return requeteurDossierSimpleActions.searchWithFavori();
        } else if (favoriRecherche.isTypeUser()) {
            navigationContext.resetCurrentDocument();
            routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTAT_UTILISATEUR);
            userManagerActions.setQueryPart(favoriRecherche.getQueryPart());

            return userManagerActions.goToResults();
        } else if (favoriRecherche.isTypeModeleFeuilleRoute()) {
            // Charge le favori de recherche de modèles de feuille de route
            rechercheModeleFeuilleRouteActions.setFavoriRechercheDoc(favoriRechercheDoc);
            routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTAT_UTILISATEUR);

            return rechercheModeleFeuilleRouteActions.search();
        } else {
            throw new ClientException("Type de favori de recherche inconnu");
        }
    }

    public String navigateToAddFavoris() {
        return SolonEpgViewConstant.ESPACE_RECHERCHE_ADD_FAVORIS_RECHERCHE;
    }

    public String navigateToBack() {
    	return routingWebActions.getFeuilleRouteView();
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<String> getPostes() {
        return postes;
    }

    public void setPostes(List<String> postes) {
        this.postes = postes;
    }
}
