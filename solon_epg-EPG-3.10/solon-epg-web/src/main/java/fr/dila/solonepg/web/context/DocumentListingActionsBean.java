package fr.dila.solonepg.web.context;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.webapp.documentsLists.ConversationDocumentsListsManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

/**
 * override nuxeeo document listings rendering.
 *
 * @author asatre
 */
@Name("documentListingActions")
@Scope(CONVERSATION)
@Install(precedence=Install.APPLICATION + 1)
public class DocumentListingActionsBean extends  org.nuxeo.ecm.webapp.contentbrowser.DocumentListingActionsBean{

	private static final long serialVersionUID = 1043364199067739980L;
	
	@In(create = true)
    private ConversationDocumentsListsManager conversationDocumentsListsManager;
	    

	public void processUserSelectRow(DocumentModelImpl doc, String contentViewName,
            String listName, Boolean selection) {
        String lName = (listName == null) ? DocumentsListsManager.CURRENT_DOCUMENT_SELECTION
                : listName;
        if (Boolean.TRUE.equals(selection)) {
        	conversationDocumentsListsManager.getWorkingList(lName).add(doc);
        } else {
        	conversationDocumentsListsManager.getWorkingList(lName).remove(doc);
        }
    }
}
