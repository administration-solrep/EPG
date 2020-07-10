package fr.dila.solonmgpp.web.requeteur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.smart.query.jsf.IncrementalSmartNXQLQuery;
import org.nuxeo.ecm.platform.smart.query.jsf.SmartNXQLQueryActions;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.MGPPJointureService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.Requeteur;
import fr.dila.st.core.query.translation.TranslatedStatement;
import fr.dila.st.core.requeteur.STIncrementalSmartNXQLQuery;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.event.STEventNames;

@Name("mgppSmartNXQLQueryActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class MGPPSmartNXQLQueryActions extends SmartNXQLQueryActions implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String completeQuery;
    
    protected Requeteur requeteur;
    
    @In(create = true)
    protected transient NavigationContextBean navigationContext;
    
    @RequestParameter
    protected Integer index;
    
    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;
    
    @In(create = true, required = false)
    protected transient CoreSession documentManager;   
    
    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(MGPPSmartNXQLQueryActions.class);    

    private String currentCategory="";

    public void initCurrentSmartQuery(String existingQueryPart,boolean resetHistory) {
        
        LOGGER.info(documentManager, MgppLogEnumImpl.INIT_MGPP_REQUESTOR_TEC) ;
        
        super.initCurrentSmartQuery(existingQueryPart,resetHistory);
        currentSmartQuery = new STIncrementalSmartNXQLQuery(existingQueryPart);
        requeteur = new Requeteur();
        this.completeQuery = "SELECT * FROM FicheLoi AS fl WHERE " + existingQueryPart ;
        requeteur.setQuery(this.completeQuery);
        
        currentSmartQuery = new STIncrementalSmartNXQLQuery(existingQueryPart);
        requeteur.updateTranslation();
    }
    
    public String getFullQuery(){
        final MGPPJointureService mgppJointureService = SolonMgppServiceLocator.getMGPPJointureService();
        QueryAssembler assembler = mgppJointureService.getQueryAssembler();
        assembler.setWhereClause(getQueryPart());
        String fullquery = assembler.getFullQuery();
        //la recherche NOR ne doit pas commencer par '*' : on ne veut pas que l'utilisateur puisse rechercher tous les dossiers.
        if(FullTextUtil.beginWithStarQuery(fullquery)){
            LOGGER.info(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Query before star error :" + fullquery) ;
            fullquery = "queryBeginStarError";
        }         
        LOGGER.info(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Requête du requeteur de dossier expert") ;
        LOGGER.info(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,fullquery) ;
        return fullquery;
    }
    
    /**
     * Cette méthode est appelé par la classe mère lors de l'ajout d'une entrée.
     * On veut déclencher un évenement seam.
     * 
     */
    public void buildQueryPart(ActionEvent event) throws ClientException {
        Events.instance().raiseEvent(STEventNames.REQUETEUR_MGPP_QUERY_PART_ADDED);
        super.buildQueryPart(event);
    }
    
    /**
     * Cette méthode est appelé par la classe mère lors de la modification de la requête.
     * On veut déclencher un évenement seam.
     * 
     */
    public void setQueryPart(ActionEvent event, String newQuery,
            boolean rebuildSmartQuery) throws ClientException {
        Events.instance().raiseEvent(STEventNames.REQUETEUR_MGPP_QUERY_PART_CHANGED);
        if (!rebuildSmartQuery){
            this.completeQuery = "SELECT * FROM FicheLoi AS fl WHERE ";            
            LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Set QueryPart completeQuery = " + completeQuery) ;
        }
        super.setQueryPart(event,newQuery,rebuildSmartQuery);
    }
    
    /**
     *  Met le libellé utilisateur correspondant à la portion de requête ajouté. 
     */
    @Observer(STEventNames.REQUETEUR_MGPP_QUERY_PART_CHANGED)
    public void updateQuery(){

        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Update User Info") ;
        updateCompleteQuery();
        requeteur.setQuery( this.completeQuery );
        requeteur.updateTranslation();
        
        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"User info : " + completeQuery) ;

    }
    
    /**
     * Met à jour l'attribut completeQuery qui contient la requête.
     */
    protected void updateCompleteQuery(){
        IncrementalSmartNXQLQuery query = currentSmartQuery;
        this.completeQuery = "SELECT * FROM FicheLoi AS fl WHERE " + query.getExistingQueryPart(); 
    }
    
    public List<TranslatedStatement> getUserInfo(){
        
      LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Get User Info" ) ;

        List<TranslatedStatement> statements = new ArrayList<TranslatedStatement>();
        try{
           requeteur.updateTranslation();
           statements = requeteur.getStatements();
           LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"QUERY = " + requeteur.getQuery()) ;
        }
        catch (Exception e) {
            // Erreur de parsing qui ne devrait pas arriver dans cette méthode, l'utilisateur ne devrait pouvoir constituer que des 
            // requêtes valides.
            // On ne peut pas récupérer l'erreur à ce stade. On réinitialise la requête et le tableau de traduction.
            // La requête de l'utilisateur est perdue.
            facesMessages.add(StatusMessage.Severity.INFO,resourcesAccessor.getMessages().get( "error.smart.query.invalidQuery"));
            return statements;
        }
        return statements;
    }
    

    /**
     * Validates the query part: throws a {@link ValidatorException} if query
     * is not a String, or if {@link IncrementalSmartNXQLQuery#isValid(String)}
     * returns false.
     *
     * @see IncrementalSmartNXQLQuery#isValid(String)
     */
    public void validateQueryPart(FacesContext context, UIComponent component,
            Object value) {
        if (value == null || !(value instanceof String)) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, ComponentUtils.translate(context, "error.smart.query.invalidQuery"), null);
            context.addMessage(null, message);
            throw new ValidatorException(message);
        }
    }
    
    @Override
    public void clearQueryPart(ActionEvent event) throws ClientException {
        super.clearQueryPart(event);
        
        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Clear requeteur") ;
        this.completeQuery = "SELECT * FROM FicheLoi AS fl WHERE "; 
        requeteur.setQuery( this.completeQuery );
        currentCategory = "";
        setSelectedCategoryName("");
        reinitialiseSmartQuery();
    }
    
    private void reinitialiseSmartQuery() {
        this.setQueryPart(requeteur.getWherePart());
        currentSmartQuery.setExistingQueryPart(requeteur.getWherePart());
        initCurrentSmartQuery(requeteur.getWherePart(),false);
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot root = context.getViewRoot();
            UIComponent composant = findComponent(root, queryPartComponentId);
            EditableValueHolder queryPartComp = ComponentUtils.getComponent(composant, queryPartComponentId, EditableValueHolder.class);
            if (queryPartComp != null) {
                queryPartComp.setSubmittedValue(requeteur.getWherePart());
                queryPartComp.setValue(requeteur.getWherePart());
            }
        } 
        catch (Exception e) {
          LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_MGPP_REQUESTOR_PROCESS_TEC,e) ;
        }
        
        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Portion: " +  this.queryPart) ;
    }
    
    public void delete(ActionEvent event){
      
        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Supprimer à l'index " + index) ;
        
        requeteur.remove(index);
        requeteur.updateTranslation();
        
        LOGGER.info(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Met la requête à vide") ;
        
        reinitialiseSmartQuery();
        if(requeteur.getStatements().size()==0){
            currentCategory = "";   
        }
    }
    
    public void up(ActionEvent event){
        
        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Monter à l'index " + index) ;
        requeteur.up(index);
        requeteur.updateTranslation();
        reinitialiseSmartQuery();
    }
    
    public void down(ActionEvent event){
        LOGGER.debug(documentManager, MgppLogEnumImpl.MGPP_REQUESTOR_PROCESS_TEC,"Descendre à l'index " + index) ;
        requeteur.down(index);
        requeteur.updateTranslation();
        reinitialiseSmartQuery();
    }
    
    public Requeteur getCurrentRequeteur(){
        requeteur.setQuery( this.completeQuery);
        return requeteur;
    }
    
    public void setSelectedCategoryName(String categoryName) {
        if(currentSmartQuery instanceof STIncrementalSmartNXQLQuery ){
            ((STIncrementalSmartNXQLQuery)currentSmartQuery).setSelectedCategoryName(categoryName);
        }
  }

    /**
     * Finds component with the given id
     */
    private UIComponent findComponent(UIComponent component, String identifier) {
      if (identifier.equals(component.getId())) {
        return component;
      }
      Iterator<UIComponent> kids = component.getFacetsAndChildren();
      while (kids.hasNext()) {
        UIComponent found = findComponent(kids.next(), identifier);
        if (found != null) {
          return found;
        }
      }
      return null;
    }

    public String getCurrentCategory() {
      return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
      this.currentCategory = currentCategory;
    }
}
