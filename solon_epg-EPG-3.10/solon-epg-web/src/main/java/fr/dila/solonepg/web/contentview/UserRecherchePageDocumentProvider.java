package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

import fr.dila.ss.web.admin.utilisateur.UserManagerActionsBean;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;

/**
 * Provider for user
 * @author asatre
 *
 */
public class UserRecherchePageDocumentProvider extends CoreQueryDocumentPageProvider implements HiddenColumnPageProvider {
    
private static final long serialVersionUID = 1L;
    
    private static final Log log = LogFactory.getLog(UserRecherchePageDocumentProvider.class);

	private static final String USER_MANAGER_ACTION = "userManagerActions";
    
    @Override
    public List<DocumentModel> getCurrentPage() {  
    	checkQueryCache();
        if (currentPageDocuments == null) {
            error = null;
            errorMessage = null;

            Map<String, Serializable> props = getProperties();
           
            UserManagerActionsBean bean =  (UserManagerActionsBean) props.get(USER_MANAGER_ACTION);

            currentPageDocuments = new ArrayList<DocumentModel>();
            
            DocumentModelList list;
			try {
				list = bean.getUsers();
				int index = 0;
	            while(offset+index < list.size() && index < pageSize){
		            currentPageDocuments.add(list.get((int) (offset+index)));
		            index++;
	            }
		            
		        resultsCount = list.size();
			} catch (ClientException e) {
				log.error("", e);
			}
            
           
        }
        return currentPageDocuments;
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
