package fr.dila.solonepg.web.action;

import static org.nuxeo.ecm.webapp.helpers.EventNames.DOCUMENT_CHILDREN_CHANGED;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.trash.TrashInfo;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;

import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.web.admin.modelefeuilleroute.ModeleFeuilleRouteActionsBean;

/**
 * surcharge de la classe DeleteActionsBean afin de gérer les règles de supression specifique aux modèles de fond de dossier.
 * 
 * @author admin
 */
@Name("deleteActions")
@Scope(ScopeType.EVENT)
@Install(precedence = Install.FRAMEWORK + 1)
public class DeleteActionsBean extends org.nuxeo.ecm.webapp.action.DeleteActionsBean {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(DeleteActionsBean.class);

    @In(create = true, required = false)
    protected ModeleFeuilleRouteActionsBean modeleFeuilleRouteActions;

    @In(create = true, required = false)
    protected transient ContentViewActions contentViewActions;

    public boolean getCanDeleteModeleFdr() {
        if (getCanDelete()) {
            // on verifie les règles de suppressions spécifique au modele de feuille de route
            String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
            List<DocumentModel> docs = documentsListsManager.getWorkingList(selectionListName);
            for (DocumentModel documentModel : docs) {
                try {
                    if (!modeleFeuilleRouteActions.canUserDeleteRoute(documentModel)) {
                        // cas où l'un des document ne verifie pas les règles de suppressions spécifique au modele de feuille de route
                        return false;
                    }
                } catch (ClientException e) {
                    log.error("erreur lors de la vérification des règles de suppression des modeles de feuille de route", e);
                    return false;
                }
            }
            // cas où tous les documents vérifient les règles de suppression.
            return true;
        }
        // cas où l'un des document ne peut pas être supprimé
        return false;
    }
    
	public boolean getCanDeleteSqueletteFdr() {
		return getCanDeleteModeleFdr();
	}

    @Override
    public boolean getCanDelete() {
        String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
        List<DocumentModel> docs = documentsListsManager.getWorkingList(selectionListName);
        List<DocumentModel> filteredDocs = new ArrayList<DocumentModel>();
        // enleve les doc pas lie a une session : ce qui est le cas des document recupéré du UserManager (user/group)
        for(DocumentModel doc : docs){
        	if(doc.getCoreSession() != null){
        		filteredDocs.add(doc);
        	} 
        }
        try {
            return getTrashService().canDelete(filteredDocs, currentUser, false);
        } catch (ClientException e) {
            log.error("Cannot check delete permission", e);
            return false;
        }
    }

    @Override
    public String deleteSelection() throws ClientException {
        String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();
        if (!documentsListsManager.isWorkingListEmpty(selectionListName)) {
        	if(getCanDeleteModeleFdr()){
        		String result = null;
        		List <DocumentModel> selectionList = documentsListsManager.getWorkingList(selectionListName);
        		StringBuilder fdrSupprime = new StringBuilder();
        		for (DocumentModel mfdrDoc : selectionList) {
    				SolonEpgFeuilleRoute mfdr = mfdrDoc.getAdapter(SolonEpgFeuilleRoute.class);
    				if (!fdrSupprime.toString().isEmpty()) {
    					fdrSupprime.append(", ");
    				}
    				fdrSupprime.append(mfdr.getNumero()).append(" - ").append(mfdr.getTitle());
    			}
        		try {
        			result = deleteSelection(selectionList);
        			log.info("Suppression du/des modèle(s) de feuille de route suivant : " + fdrSupprime);
        		} catch (ClientException e){
        			log.error("Erreur lors de la suppression du/des modèle(s) de feuille de route suivant : " + fdrSupprime, e);
        		}
        		return result;
        	} else {
        		log.info("No documents selection in context to process delete on...");
        		return null;
        	}
        } else {
            log.info("No documents selection in context to process delete on...");
            return null;
        }
    }
    
    @Override
    protected String actOnSelection(int op, List<DocumentModel> docs) throws ClientException {
        if (docs == null) {
            return null;
        }
        TrashInfo info;
        try {
            info = getTrashService().getTrashInfo(docs, currentUser, false,
                    false);
        } catch (ClientException e) {
            log.error("Cannot check delete permission", e);
            return null;
        }
        
        DocumentModel targetContext = null;
        if (navigationContext.getCurrentDocument() != null) {
            targetContext = getTrashService().getAboveDocument(
                navigationContext.getCurrentDocument(), info.rootPaths);
        }
        // remove from all lists
        documentsListsManager.removeFromAllLists(info.docs);
        
        Set<DocumentRef> parentRefs;
        String msgid;
        // operation to do
        switch (op) {
        case OP_PURGE:
            getTrashService().purgeDocuments(documentManager, info.rootRefs);
            parentRefs = info.rootParentRefs;
            msgid = "n_deleted_docs";
            break;
        case OP_DELETE:
            getTrashService().trashDocuments(info.docs);
            parentRefs = info.rootParentRefs;
            msgid = "n_deleted_docs";
            break;
        case OP_UNDELETE:
            parentRefs = getTrashService().undeleteDocuments(info.docs);
            msgid = "n_undeleted_docs";
            break;
        default:
            throw new AssertionError(op);
        }
        
        // Update context if needed
        navigationContext.setCurrentDocument(targetContext);
        
        // Notify parents
        for (DocumentRef parentRef : parentRefs) {
            DocumentModel parent = documentManager.getDocument(parentRef);
            if (parent != null) {
                Events.instance().raiseEvent(DOCUMENT_CHILDREN_CHANGED, parent);
            }
        }
        
        // User feedback
        if (info.proxies > 0) {
            FacesMessage message = FacesMessages.createFacesMessage(
                    FacesMessage.SEVERITY_WARN, "can_not_delete_proxies",
                    (Object[]) null);
            facesMessages.add(message);
        }
        Object[] params = { Integer.valueOf(info.docs.size()) };
        FacesMessage message = FacesMessages.createFacesMessage(
                FacesMessage.SEVERITY_INFO, "#0 "
                        + resourcesAccessor.getMessages().get(msgid), params);
        facesMessages.add(message);
        
        return computeOutcome(DELETE_OUTCOME);
    }

}
