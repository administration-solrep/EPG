package fr.dila.solonepg.web.admin.modeleparapheur;

import static org.jboss.seam.ScopeType.EVENT;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.richfaces.component.UITree;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.parapheur.ParapheurModelNode;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Classe de gestion de l'arbre contenant l'arborescence d'un modèle de parapheur.
 * 
 * @author ARN
 */
@Name("parapheurModelTree")
@Scope(ScopeType.SESSION)
public class ParapheurModelTreeBean implements Serializable {
	
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -4725108091555897065L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(ParapheurModelTreeBean.class);
    
    private TreeNode<ParapheurModelNode> rootNode;
    
    private String parapheurModelIdentifier;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;
    
    @In(create = true, required = false)
    protected transient ParapheurModelManagerActionsBean parapheurModelManagerActions;
    
    /**
     * charge l'arbre contenant l'arborescence du parapheur.
     * 
     * @throws ClientException
     */
    private void loadParapheurModelTree(CoreSession session, DocumentModel parapheurModeleDocument) throws ClientException {
        
        rootNode = new TreeNodeImpl<ParapheurModelNode>();
        
        if (parapheurModeleDocument == null || parapheurModeleDocument.getId() == null) {
            log.info(" arbre du modèle de parapheur vide");
        } else {
            // on récupère les répertoire qui composent la racine du modèle
            final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
            List<ParapheurModelNode> repertoiresRacine = parapheurModelService.getParapheurModelRootNode(session, parapheurModeleDocument);
            
            int counter = 1;
            // on parcourt les répertoires racine
            for (ParapheurModelNode elementParapheurNoeud : repertoiresRacine) {
                // si il n'y a qu'un seul répertoire parent celui ci est non supprimable.
                // if(repertoiresRacine.size()==1){
                // elementParapheurNoeud.setType(ParapheurModelType.PARAPHEUR_MODEL_FOLDER_UNDELETABLE);
                // }
                
                //on récupère le répertoire racine
                TreeNodeImpl<ParapheurModelNode> nodeImpl = new TreeNodeImpl<ParapheurModelNode>();
                nodeImpl.setData(elementParapheurNoeud);
    
                // on ajoute tous les élements fils du répertoire racine en utilisant une méthode récursive
                addParapheurModelElement(elementParapheurNoeud.getId(), nodeImpl, session);
    
                //on ajoute le répertoire racine à l'arbre
                rootNode.addChild(counter, nodeImpl);
                counter++;
            }
        }
    }
    
    /**
     * Méthode récursive qui ajoute les fils d'un noeud.
     * 
     * @param documentId nom du document contenant les documents fils
     * 
     * @param node noeud dans lequel les sous-groupes sont ajoutés
     * 
     * @throws ClientException
     */
    private void addParapheurModelElement(String documentId, TreeNodeImpl<ParapheurModelNode> node, CoreSession session) throws ClientException
    {
    	List<ParapheurModelNode> childs;
    	final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
    	childs = parapheurModelService.getChildrenParapheurModelNode(documentId, session);
    	
    	if(childs != null && !childs.isEmpty())
    	{
            int counter = 1;
            for (ParapheurModelNode childGroup : childs) {
                TreeNodeImpl<ParapheurModelNode> nodeImpl = new TreeNodeImpl<ParapheurModelNode>();
                nodeImpl.setData(childGroup);
                addParapheurModelElement(childGroup.getId(), nodeImpl, session);

                node.addChild(counter, nodeImpl);

                counter++;
            }
    	}
    }
    
    /**
     * Méthode qui renvoie l'arborescence complète du modèle de parapheur.
     * 
     * @return l'arborescence complète du parapheur chargé
     * 
     * @throws ClientException
     */
    public TreeNode<ParapheurModelNode> getParapheurModele() throws ClientException 
    {
        String currentParapheurModelId = getCurrentParapheurModelId();
        if (rootNode == null || parapheurModelIdentifier == null || !currentParapheurModelId.equals(parapheurModelIdentifier) ) {
    	    log.info("tree reloaded ");
    	    parapheurModelIdentifier = currentParapheurModelId;
            loadParapheurModelTree(documentManager,getParapheurModelDocument());
        }
        return rootNode;
    }
    
    /**
     * Rècupère l'identifiant du modèle de parapheur courant.
     * 
     */
    public String getCurrentParapheurModelId() {
        String idParapheurModel =null;
        DocumentModel doc =  parapheurModelManagerActions.getCurrentModel();
        //DocumentModel doc = navigationContext.getCurrentDocument();
        if(doc != null && doc.getId()!= null){
            idParapheurModel=doc.getId();
        } 
        return idParapheurModel;
    }
    
    public String getParapheurModelIdentifier() {
        return parapheurModelIdentifier;
    }

    public void setParapheurModelIdentifier(String parapheurModelIdentifier) {
        this.parapheurModelIdentifier = parapheurModelIdentifier;
    }

    @Observer(SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT)
    public void reloadParapheurTree() throws ClientException
    {
    	log.info("reload parapheur modele tree");
    	loadParapheurModelTree(documentManager,getParapheurModelDocument());
    }
    
    
    /**
     * Get the current ParapheurModelDocument.
     */
    @Factory(value = "parapheurModelDocument", scope = EVENT)
    public DocumentModel getParapheurModelDocument() {
    	//le document courant est le modèle de parapheur.
        DocumentModel doc = parapheurModelManagerActions.getCurrentModel();
    	if(doc == null || !doc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE)){
    	    doc = null;
    	}
        return doc;
    }

	/**
	 * Methode utilisée pour étendre l'arbre dès son appel. 
	 *
	 */
	public Boolean adviseNodeOpened(UITree tree) {
            return Boolean.TRUE;
		}
}
