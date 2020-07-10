package fr.dila.solonmgpp.web.requeteur;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
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

import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppRequeteurConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.MGPPRequeteurWidgetGeneratorService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.converter.OrganismeMgppDisplayConverter;
import fr.dila.solonmgpp.web.converter.UserMgppDisplayConverter;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STRequeteurExpertConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.requeteur.RequeteExperte;
import fr.dila.st.api.requeteur.RequeteurWidgetDescription;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.converter.DateClauseConverter;
import fr.dila.st.web.converter.OrganigrammeMultiIdToLabelConverter;
import fr.dila.st.web.converter.VocabularyIdsConverter;

/**
 * Bean seam pour gérer les actions du requeteur.
 * 
 */
@Name("requeteurMgppActions")
@Scope(ScopeType.CONVERSATION)
public class RequeteurActionsBean implements Serializable {

    private static final String MESREQUETES_PATH = "/mesrequetes/";

    public static final String SMART_FOLDER = "SmartFolder";

    private static final long serialVersionUID = 1L;

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

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient UserWorkspaceManagerActionsBean userWorkspaceManagerActions;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = true)
    protected transient MGPPSmartNXQLQueryActions mgppSmartNXQLQueryActions;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(RequeteurActionsBean.class);

    /**
     * Liste des catégories autorisées pour l'utilisateur dans la recherche experte
     */
    protected List<String> categorie;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    /**
     * Va vers les résultats du requêteur.
     * 
     * @return la vue des résultats du requêteur.
     * @throws ClientException
     */
    public String goToResults() throws ClientException {

        String result = null;

        if (!beginWithStarQuery()) {

            if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FicheLoi.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_LOI);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_LOI;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationOEP.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_OEP);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_OEP;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationAVI.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_AVI);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_AVI;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationDR.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_DR);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_DR;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationDecret.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_DECRET);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_DECRET;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentation341.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_341);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_341;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationIE.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_IE);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_IE;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationDPG.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_DPG);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_DPG;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationSD.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_SD);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_SD;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationJSS.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_JSS);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_JSS;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationDOC.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_DOC);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_DOC;
            } else if (mgppSmartNXQLQueryActions.getCurrentCategory().equals(FichePresentationAUD.SCHEMA)) {
                navigationWebActions.setCurrentRechercheCategory(SolonMgppActionConstant.RECHERCHE_FICHE_AUD);
                result = SolonMgppViewConstant.VIEW_RECHERCHE_RESULTAT_AUD;
            }
        }
        return result;
    }

    /**
     * Retourne le converter approprié à partir du searchField
     * 
     * @param searchField
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Converter getConverter(String searchField) {
        Map<String, Class<? extends Converter>> typeToConverterClassMap = getTypeToConverterClassMap();

        final MGPPRequeteurWidgetGeneratorService generateurService = SolonMgppServiceLocator.getMGPPRequeteurWidgetGeneratorService();
        Map<String, RequeteurWidgetDescription> widgetsMap = generateurService.getWigetDescriptionRegistry();
        if (!widgetsMap.containsKey(searchField)) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_MGPP_REQUESTOR_PROCESS_TEC, "Le requeteur expert a rencontré une erreur, on veut traduire le champ " + searchField);
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_MGPP_REQUESTOR_PROCESS_TEC, "Ce champ n'existe pas dans la liste des widgets");
            return null;
        }
        RequeteurWidgetDescription description = widgetsMap.get(searchField);

        String type = description.getType();
        if (!typeToConverterClassMap.containsKey(type)) {
            return null;
        }
        Class<? extends Converter> classConverter = typeToConverterClassMap.get(type);
        Converter converter;
        try {
            converter = classConverter.newInstance();
        } catch (Exception e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_MGPP_REQUESTOR_PROCESS_TEC, "La conversion des labels pour le requêteur expert a echoué pour la classe " + classConverter);
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_MGPP_REQUESTOR_PROCESS_TEC, "Verifiez le converter");

            // On retourne sans message d'erreur, et sans convertir.
            return null;
        }

        // Propriétés du converter de vocabulaire
        if (converter instanceof VocabularyIdsConverter) {
            ((VocabularyIdsConverter) converter).setDirectoryName(description.getDirectoryName());
            ((VocabularyIdsConverter) converter).setHasToConvertLabel(description.getHasToConvertLabel());
        }
        return converter;
    }

    /**
     * Retourne une map <type, converter>
     * 
     * @return une map qui représente une équivalence entre un type de widget et un converter de valeur.
     */
    protected Map<String, Class<? extends Converter>> getTypeToConverterClassMap() {
        Map<String, Class<? extends Converter>> typeToConverterClassMap = new HashMap<String, Class<? extends Converter>>();
        typeToConverterClassMap.put(STRequeteurExpertConstants.REQUETEUR_TYPE_ORGANIGRAMME, OrganigrammeMultiIdToLabelConverter.class);
        typeToConverterClassMap.put(STRequeteurExpertConstants.REQUETEUR_TYPE_DATE, DateClauseConverter.class);
        typeToConverterClassMap.put(STRequeteurExpertConstants.REQUETEUR_TYPE_DIRECTORY, VocabularyIdsConverter.class);
        typeToConverterClassMap.put(SolonMgppRequeteurConstants.REQUETEUR_TYPE_USER, UserMgppDisplayConverter.class);
        typeToConverterClassMap.put(SolonMgppRequeteurConstants.REQUETEUR_TYPE_ORGANISME, OrganismeMgppDisplayConverter.class);
        return typeToConverterClassMap;
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
        return mgppSmartNXQLQueryActions.getQueryPart();
    }

    /**
     * Renvoie la requête experte complète.
     * 
     * @return la requête experte complète.
     */
    protected String getFullQuery() {
        return mgppSmartNXQLQueryActions.getFullQuery();
    }

    /**
     * Signale si la requête experte contient un champ complexe (numéro NOR) commençant par le caractère '*'
     * 
     * @return
     */
    protected boolean beginWithStarQuery() {
        // String fullquery = getFullQuery();
        // TODO voir si besoin de traiter le cas où la requête est vide
        // if (fullquery != null && fullquery.equals(DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR)) {
        // // si la recherche NOR commence par le caractère '*', on envoie un avertissement à l'utilisateur.
        // facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("feedback.search.star"));
        // return true;
        // }
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

    public ContentView getContentView() throws ClientException {
        return contentViewActions.getContentViewWithProvider("");
    }

    public void reset() {
        contentViewActions.resetPageProvider("");
        categorie = null;
    }

    public Boolean isSortable() {
        if (routingWebActions.getFeuilleRouteView() != null && routingWebActions.getFeuilleRouteView().startsWith("")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String getSortPropertyName(String name) {
        String view = routingWebActions.getFeuilleRouteView();
        if (view != null) {
            return "test";
        }

        return "";
    }

    /**
     * Retourne les catégories possibles pour les widgets
     * 
     * @return une liste de catégories
     */
    public List<String> getCategories() {
        if (categorie == null || categorie.size() < 1) {
            final MGPPRequeteurWidgetGeneratorService generateurService = SolonMgppServiceLocator.getMGPPRequeteurWidgetGeneratorService();
            // récupération de toutes les catégories disponible
            categorie = generateurService.getCategories();
            // la categorie "tous" n'est pas utilisée
            categorie.remove(STRequeteurExpertConstants.REQUETEUR_EXPERT_ALL_CATEGORY);
        }
        return categorie;
    }

    /**
     * sauvegarder la catégorie sélectionnée
     * 
     * @param category
     */
    public void setCurrentCategory(String category) {
        if (!"".equals(category) && !STRequeteurExpertConstants.REQUETEUR_EXPERT_ALL_CATEGORY.equals(category)) {
            mgppSmartNXQLQueryActions.setCurrentCategory(category);
        }
    }

    /**
     * empêcher de construire la requette sur differentes catégories dans la meme recherche
     * 
     * @return disable la liste des categories
     */
    public boolean isDisabled() {
        boolean disable = false;
        String qry = getQueryPart();
        if (qry != null && !StringUtils.isBlank(qry) && !StringUtils.isBlank(mgppSmartNXQLQueryActions.getCurrentCategory())) {
            disable = true;
            mgppSmartNXQLQueryActions.setSelectedCategoryName(mgppSmartNXQLQueryActions.getCurrentCategory());
        } else if (!StringUtils.isBlank(mgppSmartNXQLQueryActions.getCurrentCategory())) {
            mgppSmartNXQLQueryActions.setSelectedCategoryName(STRequeteurExpertConstants.REQUETEUR_EXPERT_ALL_CATEGORY);
        } else {
            mgppSmartNXQLQueryActions.setSelectedCategoryName("");
        }
        return disable;
    }
}