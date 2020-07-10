package fr.dila.solonepg.web.feuilleroute;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.platform.comment.web.AbstractCommentManagerActionsBean.FollowTransitionUnrestricted;
import org.nuxeo.ecm.platform.comment.web.ThreadEntry;
import org.nuxeo.ecm.platform.comment.web.UIComment;
import org.nuxeo.ecm.platform.comment.workflow.utils.CommentsConstants;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.domain.comment.Comment;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.comment.SSCommentManagerActionsBean;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.domain.comment.STComment;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * 
 * @author asatre
 *
 */
@Name("commentManagerActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION + 3)
public class CommentManagerActionsBean extends
			SSCommentManagerActionsBean implements Serializable {

    /**
     * Serial UID.
     */
	private static final long serialVersionUID = -4408842929438572965L;
	
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(CommentManagerActionsBean.class);

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    public String idVisibility;
	    
    @Override
	public String addComment() throws ClientException {
	    DocumentModel myComment = documentManager.createDocumentModel("Comment");
		myComment.setProperty("comment", "author", principal.getName());
		if(newContent.length()>2000){
			newContent = newContent.substring(0, 1999);
		}
		myComment.setProperty("comment", "text", newContent);
		myComment.setProperty("comment", "creationDate", Calendar.getInstance());
		Comment comment = myComment.getAdapter(Comment.class);
		comment.setVisibility(getIdVisibility());
		addComment(myComment);
		setIdVisibility(null);
		// do not navigate to newly-created comment, they are hidden documents
	    return null;
	}

    @Override
	public List<ThreadEntry> getCommentsAsThread(DocumentModel commentedDoc) throws ClientException {
		if (commentThread != null) {
		    return commentThread;
		}
		commentThread = new ArrayList<ThreadEntry>();
		if (uiComments == null) {
		    initComments(commentedDoc); // Fetches all the comments associated
		    // with the
		    // document into uiComments (a list of comment
		    // roots).
		}
		for (UIComment uiComment : uiComments) {
		    commentThread.add(new ThreadEntry(uiComment.getComment(), 0));
		    if (uiComment.getChildren() != null) {
		        flattenTree(commentThread, uiComment, 0);
		    }
		}
		return commentThread;
	}
    
    @Override
    public void initComments(DocumentModel commentedDoc) throws ClientException {
        commentableDoc = getCommentableDoc(commentedDoc);
        if (uiComments == null) {
            uiComments = new ArrayList<UIComment>();
            if (commentableDoc != null) {
                List<DocumentModel> comments = commentableDoc.getComments();
                for (DocumentModel comment : comments) {
                	if(isVisible(comment)){
	                    UIComment uiComment = createUIComment(null, comment);
	                    uiComments.add(uiComment);
                	}
                }
            }
        }
    }

    @Override
    protected boolean isVisible(DocumentModel commentModel) throws ClientException {
    	Comment comment = commentModel.getAdapter(Comment.class);
    	
		if (comment == null) {
			return false;
		}

		List<String> groups = principal.getGroups();
		if (groups != null && groups.contains(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME)) {
			return true;
		}

		if (comment.getAuthor() != null && comment.getAuthor().equals(principal.getName())) {
			return true;
		}

		// FEV514 : si l'utilisateur courant est dans le poste de l'auteur de la note, il peut voir la note
		if (isInAuthorPoste(comment)) {
			return true;
		}

		Boolean result = true;
        if (comment.getVisibleToMinistereId() != null) {
            result = false;
            for (String idMinistere : ssPrincipal.getMinistereIdSet()) {
                if (idMinistere.equals(comment.getVisibleToMinistereId())) {
                    result = true;
                    break;
                }
            }
        } else if (comment.getVisibleToUniteStructurelleId() != null) {
            result = false;
            for (String idDir : ssPrincipal.getDirectionIdSet()) {
                if (idDir.equals(comment.getVisibleToUniteStructurelleId())) {
                    result = true;
                    break;
                }
            }
        } else if (comment.getVisibleToPosteId() != null) {
            result = false;
            for (String idPoste : ssPrincipal.getPosteIdSet()) {
                if (idPoste.equals(comment.getVisibleToPosteId())) {
                    result = true;
                    break;
                }
            }
        }
        return result;

    }

    public String getIdVisibility() {
        return this.idVisibility;
    }

    public void setIdVisibility(String idVisibility) {
        this.idVisibility = idVisibility;
    }
    
    public boolean isNoteSuppressor() {
     // Vérifie que l'utilisateur possède la fonction unitaire de suppression de note
        log.info("peut supprimer les notes : " + principal.isMemberOf(SolonEpgBaseFunctionConstant.NOTE_ETAPE_DELETER));
        return principal.isMemberOf(SolonEpgBaseFunctionConstant.NOTE_ETAPE_DELETER); 
         
    }

}
