package fr.dila.solonepg.web.documentsLists;

import static org.jboss.seam.ScopeType.SESSION;

import java.util.List;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManagerBean;

@Name("documentsListsManager")
@Scope(SESSION)
@Install(precedence = Install.FRAMEWORK + 1)
public class SolonEpgDocumentsListsManagerBean extends DocumentsListsManagerBean
        implements DocumentsListsManager {

    private static final long serialVersionUID = 2895324573454635971L;

    /**
     * Met à jour la liste des documents;
     * 
     * @param listName
     * @param currentDocument
     */
    public void refreshDocumentsLists(String listName, List<DocumentModel> currentDocument) {
        List<DocumentModel> listToRefresh = getWorkingList(listName);    
        if (listToRefresh != null && listToRefresh.size() > 0) {
            //TODO récupère les ids de document, faire une requête pour récupérer les dossiers
            //mettre des locks sur les dossiers et enlever les locks
            setWorkingList(listName, listToRefresh);
        }
    }

}
