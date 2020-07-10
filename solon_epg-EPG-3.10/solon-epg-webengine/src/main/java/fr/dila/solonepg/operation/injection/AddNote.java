package fr.dila.solonepg.operation.injection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.platform.comment.workflow.utils.CommentsConstants;


@Operation(id = AddNote.ID, category = Constants.CAT_DOCUMENT, label = "add step note", description = "add step note")
public class AddNote {

    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddNote";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;
    
    @Param(name = "entete", required = true, order = 1)
    protected String entete;
    
    @Param(name = "description", required = true, order = 1)
    protected String description;

    @OperationMethod
    public void run(DocumentModel doc)throws Exception {
        repriseLog.debug("add note" + doc.getId());
        try {
            StringBuilder contenu= new StringBuilder(); 
            if (entete != null && !entete.trim().equals("")) {
                contenu.append(entete).append(":\n") ;
            }
            if (description != null && !description.trim().equals("")) {
                contenu.append(description) ;                
            }
            DocumentModel myComment = session.createDocumentModel("Comment");
            myComment.setProperty("comment", "author", "reprise");
            myComment.setProperty("comment", "text",  contenu);
            CommentableDocument commentableDoc = doc.getAdapter(CommentableDocument.class);
            myComment = commentableDoc.addComment(myComment);
            session.followTransition(myComment.getRef(), CommentsConstants.TRANSITION_TO_PUBLISHED_STATE);
            repriseLog.debug("add note step" + doc.getId() + "-> OK ");
        } catch (Exception e) {
            repriseLog.debug("add note step" + doc.getId() + "-> KO ", e);
            throw new Exception("Erreur lors de l'ajout d'une note de l'etape" + doc.getId(), e);
        }
    }
}
