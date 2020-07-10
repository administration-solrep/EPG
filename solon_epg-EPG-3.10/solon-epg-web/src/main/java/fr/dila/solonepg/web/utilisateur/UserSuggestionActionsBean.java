package fr.dila.solonepg.web.utilisateur;

import static org.jboss.seam.ScopeType.PAGE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.annotation.ejb.SerializedConcurrentAccess;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoGroup;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.impl.SimpleDocumentModel;

import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Bean seam de gestion des suggestions des utilisateurs
 * 
 * @author arolin
 * @see org.nuxeo.ecm.webapp.security.UserSuggestionActionsBean
 */
@Name("userSuggestionActions")
@SerializedConcurrentAccess
@Scope(PAGE)
@Install(precedence = Install.APPLICATION + 1)
public class UserSuggestionActionsBean  extends  org.nuxeo.ecm.webapp.security.UserSuggestionActionsBean{

	private static final long serialVersionUID = 2774501488951539686L;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;
    
    @Override
    public List<DocumentModel> getUserSuggestions(Object input) throws ClientException {
        String searchPattern = (String) input;
        List<DocumentModel> userSuggestions = super.getUserSuggestions(searchPattern);
        if (StringUtils.isNotEmpty(searchPattern)) {
            if (userSuggestions == null) {
                userSuggestions = new ArrayList<DocumentModel>();
            }
            if (STConstant.SYSTEM_USERNAME.startsWith(searchPattern)) {
                userSuggestions.add(addSimpleDocModelUser(STConstant.SYSTEM_USERNAME));
            }
            if (STConstant.NUXEO_SYSTEM_USERNAME.startsWith(searchPattern)) {
                userSuggestions.add(addSimpleDocModelUser(STConstant.NUXEO_SYSTEM_USERNAME));
            }
        }
        return userSuggestions;
    }
    
    protected DocumentModel addSimpleDocModelUser(String username) throws ClientException{
        List<String> userSchema = new ArrayList<String>();
        userSchema.add(STSchemaConstant.USER_SCHEMA);
        DocumentModel simpleDocModel = new SimpleDocumentModel(userSchema);
        simpleDocModel.setProperty(STSchemaConstant.USER_SCHEMA, STSchemaConstant.USER_USERNAME, username);
        simpleDocModel.setProperty(STSchemaConstant.USER_SCHEMA, STSchemaConstant.USER_FIRST_NAME, username);
        return simpleDocModel;
    }
    
    @Override
    public Object getSuggestions(Object input) throws ClientException {
        if (equals(cachedUserSuggestionSearchType, userSuggestionSearchType)
                && equals(cachedUserSuggestionMaxSearchResults,
                        userSuggestionMaxSearchResults)
                && equals(cachedInput, input)) {
            return cachedSuggestions;
        }

        List<DocumentModel> users = Collections.emptyList();
        if (USER_TYPE.equals(userSuggestionSearchType)
                || StringUtils.isEmpty(userSuggestionSearchType)) {
            users = getUserSuggestions(input);
        }

        List<DocumentModel> groups = Collections.emptyList();
        if (GROUP_TYPE.equals(userSuggestionSearchType)
                || StringUtils.isEmpty(userSuggestionSearchType)) {
            groups = getGroupsSuggestions(input);
        }

        int userSize = users.size();
        int groupSize = groups.size();
        int totalSize = userSize + groupSize;

        if (userSuggestionMaxSearchResults != null
                && userSuggestionMaxSearchResults > 0) {
            if (userSize > userSuggestionMaxSearchResults
                    || groupSize > userSuggestionMaxSearchResults
                    || totalSize > userSuggestionMaxSearchResults) {
                addSearchOverflowMessage();
                return null;
            }
        }

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(
                totalSize);

        for (DocumentModel user : users) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put(TYPE_KEY_NAME, USER_TYPE);
            entry.put(ENTRY_KEY_NAME, user);
            //note : on n'utilise pas l'id dans le cas de l'utilisateur système pour éviter une UnsupportedOperationException
            String username = (String) user.getProperty(STSchemaConstant.USER_SCHEMA, STSchemaConstant.USER_USERNAME);
            if(!username.equals(STConstant.SYSTEM_USERNAME) && !username.equals(STConstant.NUXEO_SYSTEM_USERNAME)){
                String userId = user.getId();
                entry.put(ID_KEY_NAME, userId);
                entry.put(PREFIXED_ID_KEY_NAME, NuxeoPrincipal.PREFIX + userId);
            }
            result.add(entry);
        }
        
        for (DocumentModel group : groups) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put(TYPE_KEY_NAME, GROUP_TYPE);
            entry.put(ENTRY_KEY_NAME, group);
            String groupId = group.getId();
            entry.put(ID_KEY_NAME, groupId);
            entry.put(PREFIXED_ID_KEY_NAME, NuxeoGroup.PREFIX + groupId);
        }

        cachedInput = input;
        cachedUserSuggestionSearchType = userSuggestionSearchType;
        cachedUserSuggestionMaxSearchResults = userSuggestionMaxSearchResults;
        cachedSuggestions = result;

        return result;
    }
    
    public Object getSuggestionSuiviBatchNotification(Object input) throws ClientException {
		List<DocumentModel> users = Collections.emptyList();
        if (USER_TYPE.equals(userSuggestionSearchType)
                || StringUtils.isEmpty(userSuggestionSearchType)) {
            users = getUserSuggestions(input);
        }
        final List<STUser> adminUsers = STServiceLocator.getProfileService().getUsersFromBaseFunction(STBaseFunctionConstant.BATCH_READER);
        if (users==null || users.size()==0 || adminUsers==null){
        	return null;
        }
        List<String> result = new ArrayList<String>();
        for (DocumentModel user : users) {
        	STUser stUser = user.getAdapter(STUser.class);
        	if (adminUsers.contains(stUser)) {
        		result.add(stUser.getUsername());
        	}
        }
        return result;
	}

}
