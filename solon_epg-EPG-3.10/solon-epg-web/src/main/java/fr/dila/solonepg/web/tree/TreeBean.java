package fr.dila.solonepg.web.tree;

import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;

import org.jboss.seam.annotations.In;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelIterator;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.richfaces.component.UITree;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.api.tree.SSTreeNode;
import fr.dila.st.api.constant.STLifeCycleConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringUtil;

public abstract class TreeBean implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6727405903600698344L;

    private static final STLogger LOGGER = STLogFactory.getLog(TreeBean.class);
    
    protected TreeNode<SSTreeNode> rootNode;    

    protected String dossierIdentifier;
    
    @In(required = true, create = true)
    protected NuxeoPrincipal currentUser;
        
    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;
    
    @In(create = true, required = false)
    protected FacesMessages facesMessages;
    
    protected final static String IMG_ICON_FOLDER_EMPTY = "/img/icons/folder_gris.png";
    
    protected final static String IMG_ICON_FOLDER_NOTEMPTY = "/img/icons/folder.png";
    
    
    public boolean linkedToFolder() {
        return navigationContext.getCurrentDocument() ==null?false :"Dossier".equals(navigationContext.getCurrentDocument().getType());
    }
    
    
    /**
     * charge l'arbre contenant l'arborescence.
     * 
     * @throws ClientException
     */
    protected void loadTree() throws ClientException {
        
        if (getDossierDocument() == null || getDossierDocument().getId() == null) {
            //throw new ClientException("Dossier not found");
            return  ;
        }
        
        if (getArborescenceDocument() == null || getArborescenceDocument().getId() == null) {
            throw new ClientException("Arborescence not found");
        }

        rootNode = new TreeNodeImpl<SSTreeNode>();

        // on récupère les répertoire qui composent la racine du fond de dossier
        List<? extends SSTreeNode> repertoiresRacine = getRootFolders();

        int counter = 0;
        // on parcourt les éléments du fond de dossier en partant des répertoires
        for (SSTreeNode elementFddNoeud : repertoiresRacine) {
            TreeNodeImpl<SSTreeNode> nodeImpl = new TreeNodeImpl<SSTreeNode>();
            addElement(nodeImpl, elementFddNoeud);
            rootNode.addChild(counter, nodeImpl);
            counter++;
        }
    }    
    
    /**
     * Méthode récursive qui ajoute les fils d'un noeud.
     * 
     * @param documentId nom du document contenant les documents fils
     * @param root noeud dans lequel les sous-groupes sont ajoutés
     * @param node node dans lequel sont stocké les inforamtions du noeud
     * @throws ClientException
     */
    protected void addElement(TreeNodeImpl<SSTreeNode> root, SSTreeNode node) throws ClientException {
        
        List<? extends SSTreeFolder> childsFolder =  getChildrenFolder(node.getDocument());
        List<? extends SSTreeFile> childsFile = getChildrenFile(node.getDocument());

        int currentDepth = node.getDepth();
        root.setData(node);
        int counter = 0;
        if (childsFolder != null && !childsFolder.isEmpty()) {            
            for (SSTreeFolder childGroup : childsFolder) {
                TreeNodeImpl<SSTreeNode> nodeImpl = new TreeNodeImpl<SSTreeNode>();
                childGroup.setDepth(currentDepth + 1);
                addElement(nodeImpl, childGroup);
                root.addChild(counter, nodeImpl);
                counter++;
            }
        } 
        
        // On ajoute les fichiers enfants
        if (childsFile != null && !childsFile.isEmpty()) {
            for (SSTreeFile childFile : childsFile) {
                boolean isVisible = true;
                if (childFile.isDeleted()) {
                    isVisible = false;
                }
                
                if (isVisible) {
                    TreeNodeImpl<SSTreeNode> nodeImpl = new TreeNodeImpl<SSTreeNode>();
                    childFile.setDepth(currentDepth + 1);
                    nodeImpl.setData(childFile);
                    root.addChild(counter, nodeImpl);
                    counter++;
                }
            }
        }
        
        // Renseigne l'attribut isEmpty pour l'affichage des icones
        if(root.getChild(0) != null) {
            ((SSTreeFolder)node).setIsEmpty(false);
        }
    }
    
    protected abstract DocumentModel getArborescenceDocument() throws ClientException;
    
    /**
     * Récupère la liste de repertoires racines d'un dossier
     * @param session
     * @param dossierDoc le documentModel du dossier des enfants à récupérer
     * @param currentUser l'utilisateur 
     * @return
     */
    protected abstract List<? extends SSTreeNode> getRootFolders() throws ClientException;
    
    /**
     * Récupère une liste de repertoires enfants d'un document parent
     * @param session
     * @param parentDoc le parent des enfants à récupérer 
     * @return
     */
    protected abstract List<? extends SSTreeFolder> getChildrenFolder(DocumentModel parentDoc) throws ClientException;
    
    /**
     * Récupère une liste de fichiers enfants d'un document parent
     * @param session
     * @param parentDoce parent des enfants à récupérer
     * @return
     */
    protected abstract List<? extends SSTreeFile> getChildrenFile(DocumentModel parentDoc) throws ClientException;
    
    
    /**
     * Ajouter pour f:convertDateTime
     * @return
     */
    public TimeZone getTimeZone(){
      return TimeZone.getDefault();
    }
    
    /**
     * Récupère le dossier courant.
     */
    protected DocumentModel getDossierDocument() {
        // on récupère le dossier à partir du document courant de la navigation
        return navigationContext.getCurrentDocument();
    }
    
    /**
     * Get the current Dossier from context.
     * 
     */
    public String getCurrentDossierId() {
        String idDossier = "";
        DocumentModel doc = navigationContext.getCurrentDocument();
        if(doc != null && doc.getId()!= null){
            idDossier=doc.getId();
        }
        return idDossier;
    }
    
    public String getDossierIdentifier() {
        return dossierIdentifier;
    }

    public void setDossierIdentifier(String dossierIdentifier) {
        this.dossierIdentifier = dossierIdentifier;
    }
    
    /**
     * Methode utilisée pour étendre l'arbre dès son appel.
     */
    public Boolean adviseNodeOpened(UITree tree) {
        return Boolean.TRUE;
    }
    
    /**
     * Retourne le chemin de l'image de répertoire gris ou jaune suivant si le 
     * repertoire est vde ou non
     * @param folder
     * @return
     * @throws ClientException
     */
    public String getImgIcon(SSTreeFolder folder) throws ClientException {
        try {
            if(folder == null || folder.getDocument() == null){
                return IMG_ICON_FOLDER_EMPTY;        
                } else {
                    if (documentManager.hasChildren(folder.getDocument().getRef())) {
                        DocumentModelIterator childrenIterator = documentManager.getChildrenIterator(folder.getDocument().getRef());
                        while(childrenIterator.hasNext()) {
                            if (!STLifeCycleConstant.DELETED_STATE.equals(childrenIterator.next().getCurrentLifeCycleState())) {
                                return IMG_ICON_FOLDER_NOTEMPTY;
                            }
                        }                
                        return IMG_ICON_FOLDER_EMPTY;
                    }
            }
        } catch (ClientException e) {            
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_INFORMATION_TEC, folder, e);                
            facesMessages.add(StatusMessage.Severity.WARN, "Echec de récupération d'information concernant le répertoire ");
        }
        return IMG_ICON_FOLDER_EMPTY;
    }
    
    public String escapeQuotes(String stringWithQuotes) {
    	return StringUtil.escapeQuotes(stringWithQuotes);
    }
}
