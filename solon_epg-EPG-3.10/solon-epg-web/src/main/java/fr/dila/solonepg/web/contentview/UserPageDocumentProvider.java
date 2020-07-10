package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.recherche.UserManagerActionsBean;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;

/**
 * Provider for user
 * @author asatre
 *
 */
public class UserPageDocumentProvider extends CoreQueryDocumentPageProvider implements HiddenColumnPageProvider {
    
private static final long serialVersionUID = 1L;
    
    private static final Log log = LogFactory.getLog(UserPageDocumentProvider.class);

    private static final String USER_MANAGER_ACTION = "userManagerActions";
    
    @Override
    public List<DocumentModel> getCurrentPage() {   
    	checkQueryCache();        
        if (currentPageDocuments == null) {
            error = null;
            errorMessage = null;

            CoreSession coreSession = getCoreSession();
            if (query == null) {                
                buildQuery(coreSession);                
            }
            if (query == null) {
                throw new ClientRuntimeException(String.format(
                        "Cannot perform null query: check provider '%s'",
                        getName()));
            }

            currentPageDocuments = new ArrayList<DocumentModel>();

            try {

                if (log.isDebugEnabled()) {
                    log.debug(String.format(
                            "Perform query for provider '%s': '%s' with pageSize=%s, offset=%s",
                            getName(), query, Long.valueOf(getPageSize()),
                            Long.valueOf(offset)));
                }
                
                
                IterableQueryResult res = null;
                final UserManager userManager = STServiceLocator.getUserManager();
                List<STUser> users = new ArrayList<STUser>();
                
                try {
                    res = QueryUtils.doUFNXQLQuery(coreSession, query, null);
                    Iterator<Map<String, Serializable>> iterator = res.iterator();
                    Date today = Calendar.getInstance().getTime();
                    Set<String> userToRemove = new HashSet<String>();
                    
                    while(iterator.hasNext()){
                        Map<String, Serializable> map = iterator.next();
                        DocumentModel userModel = userManager.getUserModel((String) map.get("id"));
                        if(userModel != null){
                        	STUser user = userModel.getAdapter(STUser.class);
            				if (user.getDateFin() != null && user.getDateFin().getTime() != null && user.getDateFin().getTime().compareTo(today) < 0){
            				    userToRemove.add(userModel.getId());
            					continue;
            				} 
            				if (user.getPostes() == null || user.getPostes().isEmpty()){
            				    userToRemove.add(userModel.getId());
            					continue;
            				}
                        	users.add(user);
                        }else{
                            userToRemove.add((String) map.get("id"));
                        }
                    }
                    
                    if(sortInfos != null && !sortInfos.isEmpty() && isSortable()){
                    	for (SortInfo sortinfo : sortInfos) {
                    		Collections.sort(users, new SortInfoComparator(sortinfo));
						}
                    }
                    
                    if(!userToRemove.isEmpty()){
                        //Suppression des users des derniers resultats consultés et des favoris de consultation du user courant
                        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
                        String userworkspacePath = Framework.getLocalService(UserWorkspaceService.class).getCurrentUserPersonalWorkspace(coreSession, null).getPathAsString();
                        espaceRechercheService.removeUserFromDerniersResultatsConsultes(coreSession, userworkspacePath, userToRemove);
                        espaceRechercheService.removeUserFromFavorisConsultations(coreSession, userworkspacePath, userToRemove);
                        coreSession.getPrincipal().getName();
                    }
                    
                } finally {
                    if (res != null) {
                        res.close();
                    }
                }
                
                resultsCount = QueryUtils.doCountQuery(coreSession, FlexibleQueryMaker.KeyCode.UFXNQL.key + query);
                
                Map<String, Serializable> props = getProperties();
                UserManagerActionsBean bean =  (UserManagerActionsBean) props.get(USER_MANAGER_ACTION);
                
                Long pagesize = getPageSize();
                if(pagesize<1){
                	pagesize = maxPageSize;
                }
                int index = 0;
	            while(offset+index < users.size() && index < pagesize){
		            currentPageDocuments.add(users.get((int) (offset+index)).getDocument());
		            index++;
	            }
	            
                bean.setUsersList(currentPageDocuments);
                
                if (log.isDebugEnabled()) {
                    log.debug(String.format(
                            "Performed query for provider '%s': got %s hits",
                            getName(), Long.valueOf(resultsCount)));
                }

            } catch (Exception e) {
                error = e;
                errorMessage = e.getMessage();
                log.warn(e.getMessage(), e);
            }
        }
        return currentPageDocuments;
        
    }
    
    @Override
    protected void buildQuery(CoreSession coreSession) {
    	  try {
              String newQuery;
              PageProviderDefinition def = getDefinition();
              if (def.getWhereClause() == null) {
                  newQuery = NXQLQueryBuilder.getQuery(def.getPattern(),
                          getParameters(), def.getQuotePatternParameters(),
                          def.getEscapePatternParameters());
              } else {
                  DocumentModel searchDocumentModel = getSearchDocumentModel();
                  if (searchDocumentModel == null) {
                      throw new ClientException(String.format(
                              "Cannot build query of provider '%s': "
                                      + "no search document model is set",
                              getName()));
                  }
                  newQuery = NXQLQueryBuilder.getQuery(searchDocumentModel,
                          def.getWhereClause(), getParameters());
              }

              if (newQuery != null && !newQuery.equals(query)) {
                  // query has changed => refresh
                  refresh();
                  query = newQuery;
              }
          } catch (ClientException e) {
              throw new ClientRuntimeException(e);
          }
    }
    
    private class SortInfoComparator implements Comparator<STUser> {

		private final SortInfo sortInfo;

		public SortInfoComparator(SortInfo sortinfo) {
			this.sortInfo = sortinfo;
		}

		@Override
		public int compare(STUser arg0, STUser arg1) {
			if(sortInfo != null){
				if("username".equals(sortInfo.getSortColumn())){
					if(sortInfo.getSortAscending()){
						return arg0.getUsername().toLowerCase().compareTo(arg1.getUsername().toLowerCase());
					}else{
						return arg1.getUsername().toLowerCase().compareTo(arg0.getUsername().toLowerCase());
					}
				}
			}
			return arg0.getUsername().toLowerCase().compareTo(arg1.getUsername().toLowerCase());
		}
	}
    
    @Override
    public void setSearchDocumentModel(DocumentModel searchDocumentModel) {
        if (this.searchDocumentModel != searchDocumentModel) {
            this.searchDocumentModel = searchDocumentModel;
            refresh();
        }
    }
    
    @Override
	public Boolean isHiddenColumn(String elExpression) throws ClientException {
		if (StringUtil.isNotBlank(elExpression)) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
			ELContext elContext = context.getELContext();
			ValueExpression vex = expressionFactory.createValueExpression(elContext, elExpression, Boolean.class);
			return (Boolean) vex.getValue(elContext);
		}
		return false;
	}
}
